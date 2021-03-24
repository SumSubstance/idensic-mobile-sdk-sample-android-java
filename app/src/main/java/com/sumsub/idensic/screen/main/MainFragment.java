package com.sumsub.idensic.screen.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.sumsub.idensic.App;
import com.sumsub.idensic.R;
import com.sumsub.idensic.common.Constants;
import com.sumsub.idensic.manager.ApiManager;
import com.sumsub.idensic.manager.PrefManager;
import com.sumsub.idensic.model.AccessTokenResponse;
import com.sumsub.idensic.model.FlowItem;
import com.sumsub.idensic.model.FlowListResponse;
import com.sumsub.idensic.screen.base.BaseFragment;
import com.sumsub.sns.core.SNSActionResult;
import com.sumsub.sns.core.SNSMobileSDK;
import com.sumsub.sns.core.SNSModule;
import com.sumsub.sns.core.data.listener.SNSActionResultHandler;
import com.sumsub.sns.core.data.listener.SNSCompleteHandler;
import com.sumsub.sns.core.data.listener.SNSErrorHandler;
import com.sumsub.sns.core.data.listener.SNSEvent;
import com.sumsub.sns.core.data.listener.SNSEventHandler;
import com.sumsub.sns.core.data.listener.SNSStateChangedHandler;
import com.sumsub.sns.core.data.listener.TokenExpirationHandler;
import com.sumsub.sns.core.data.model.AnswerType;
import com.sumsub.sns.core.data.model.FlowActionType;
import com.sumsub.sns.core.data.model.FlowType;
import com.sumsub.sns.core.data.model.SNSCompletionResult;
import com.sumsub.sns.core.data.model.SNSSDKState;
import com.sumsub.sns.prooface.SNSProoface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.navigation.fragment.NavHostFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainFragment extends BaseFragment {

    private Group gContent;
    private ProgressBar pbProgress;
    private TextInputEditText etUserId;
    private TextInputEditText etAccessToken;
    private TextInputEditText etFlowName;
    private TextInputEditText etActionId;
    private TextInputEditText etAccessTokenAction;
    private TextInputEditText etActionName;
    private ImageButton ibSelectFlow;
    private ImageButton ibSelectAction;
    private ApiManager apiManager;

    private final TokenExpirationHandler sdkFlowAccessTokeExpirationHandler = () -> {
        PrefManager prefManager = App.getInstance().getPrefManager();
        String token = prefManager.getToken();
        String userId = prefManager.getUserId();

        try {
            String newAccessToken = apiManager.getAccessTokenForFlow(token, userId).execute().body().getToken();
            prefManager.setAccessToken(newAccessToken);
            return newAccessToken;
        } catch (Exception e) {
            Timber.e(e);
            Toast.makeText(requireContext(), "An error while refreshing an access token. Please, check your internet connection", Toast.LENGTH_SHORT).show();
            return "";
        }
    };

    private final TokenExpirationHandler sdkActionAccessTokeExpirationHandler = () -> {
        PrefManager prefManager = App.getInstance().getPrefManager();
        String token = prefManager.getToken();
        String userId = prefManager.getUserId();
        String actionId = prefManager.getActionId();

        try {
            String newAccessToken = apiManager.getAccessTokenForAction(token, userId, actionId).execute().body().getToken();
            prefManager.setAccessTokenAction(newAccessToken);
            return newAccessToken;
        } catch (Exception e) {
            Timber.e(e);
            Toast.makeText(requireContext(), "An error while refreshing an access token. Please, check your internet connection", Toast.LENGTH_SHORT).show();
            return "";
        }
    };

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PrefManager prefManager = App.getInstance().getPrefManager();
        String apiUrl = prefManager.getUrl();
        if (apiUrl == null) {
            NavHostFragment.findNavController(MainFragment.this).navigate(R.id.action_main_to_sign_in);
            return;
        }

        apiManager = new ApiManager(apiUrl);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        gContent = view.findViewById(R.id.g_content);
        pbProgress = view.findViewById(R.id.pb_progress);
        etUserId = view.findViewById(R.id.et_userid);
        etUserId.setText(prefManager.getUserId());
        etAccessToken = view.findViewById(R.id.et_access_token);
        etFlowName = view.findViewById(R.id.et_flow_name);
        MaterialButton btnUserId = view.findViewById(R.id.btn_generate_userid);
        MaterialButton btnStartFlow = view.findViewById(R.id.btn_start_flow);
        etActionId = view.findViewById(R.id.et_actionid);
        etActionId.setText(prefManager.getActionId());
        etAccessTokenAction = view.findViewById(R.id.et_access_token_action);
        etActionName = view.findViewById(R.id.et_action_name);
        MaterialButton btnActionId = view.findViewById(R.id.btn_generate_action_id);
        MaterialButton btnStartAction = view.findViewById(R.id.btn_start_action);
        ibSelectFlow = view.findViewById(R.id.ib_get_flows);
        ibSelectAction = view.findViewById(R.id.ib_get_actions);

        showProgress(false);
        if (prefManager.getUserId() == null) {
            generateUserId();
        }
        if (prefManager.getActionId() == null) {
            generateActionId();
        }
        setAccessToken(prefManager.getAccessToken());
        setAccessTokenAction(prefManager.getAccessTokenAction());

        toolbar.inflateMenu(R.menu.main);

        toolbar.setOnMenuItemClickListener(item -> {
            // Clear cache
            prefManager.setUrl(null);
            prefManager.setToken(null);
            prefManager.setAccessToken(null);
            prefManager.setAccessTokenAction(null);

            NavHostFragment.findNavController(MainFragment.this).navigate(R.id.action_main_to_sign_in);
            return false;
        });
        btnUserId.setOnClickListener(v -> generateUserId());
        btnStartFlow.setOnClickListener(v -> startSDKFlow());
        btnActionId.setOnClickListener(v -> generateActionId());
        btnStartAction.setOnClickListener(v -> startSDKAction());

        ibSelectFlow.setOnClickListener(v -> {
            String token = prefManager.getToken();
            Filter<FlowItem> filter = item -> item.getTarget().equals("msdk") && item.getType() != FlowType.Actions;

            loadFlows(token, filter, flowName -> {
                etFlowName.setText(flowName);
            });
        });

        ibSelectAction.setOnClickListener(v -> {
            String token = prefManager.getToken();
            Filter<FlowItem> filter = item -> item.getTarget().equals("msdk") && item.getType() == FlowType.Actions;

            loadFlows(token, filter, flowName -> {
                etActionName.setText(flowName);
            });
        });
    }

    private void generateUserId() {
        String userId = String.format(Constants.USER_ID, UUID.randomUUID().toString());
        App.getInstance().getPrefManager().setUserId(userId);
        etUserId.setText(userId);
    }

    private void generateActionId() {
        String actionId = String.format(Constants.ACTION_ID, UUID.randomUUID().toString());
        App.getInstance().getPrefManager().setActionId(actionId);
        etActionId.setText(actionId);
    }

    private void startSDKFlow() {
        PrefManager prefManager = App.getInstance().getPrefManager();
        String token = prefManager.getToken();
        String userId = prefManager.getUserId();
        String flow = etFlowName.getText().toString();

        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(), "A token is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(requireContext(), "An user id is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);

        apiManager.getAccessTokenForFlow(token, userId).enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(@NotNull Call<AccessTokenResponse> call, @NotNull Response<AccessTokenResponse> response) {
                String accessToken = response.body().getToken();
                if (accessToken != null) {
                    setAccessToken(accessToken);
                    launchSdk(accessToken, flow, sdkFlowAccessTokeExpirationHandler);
                } else {
                    showProgress(false);
                    Toast.makeText(requireContext(), "An error while getting an access token. Please, check your credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<AccessTokenResponse> call, @NotNull Throwable t) {
                showProgress(false);
                Toast.makeText(requireContext(), "An error while getting an access token. Please, check your applicant", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void startSDKAction() {
        PrefManager prefManager = App.getInstance().getPrefManager();
        String token = prefManager.getToken();
        String userId = etUserId.getText().toString();
        String actionId = etActionId.getText().toString();
        String action = etActionName.getText().toString();

        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(), "A token is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(requireContext(), "An user id is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (actionId == null || actionId.isEmpty()) {
            Toast.makeText(requireContext(), "An action id is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);

        apiManager.getAccessTokenForAction(token, userId, actionId).enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(@NotNull Call<AccessTokenResponse> call, @NotNull Response<AccessTokenResponse> response) {
                String accessToken = response.body().getToken();
                if (accessToken != null) {
                    setAccessTokenAction(accessToken);
                    launchSdk(accessToken, action, sdkActionAccessTokeExpirationHandler);
                } else {
                    showProgress(false);
                    Toast.makeText(requireContext(), "An error while getting an access token. Please, check your credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<AccessTokenResponse> call, @NotNull Throwable t) {
                showProgress(false);
                Toast.makeText(requireContext(), "An error while getting an access token. Please, check your applicant", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void launchSdk(String accessToken, String flowOrAction, TokenExpirationHandler tokenUpdater) {

        PrefManager prefManager = App.getInstance().getPrefManager();
        String apiUrl = prefManager.getUrl();
        List<SNSModule> modules = Arrays.asList(new SNSProoface(SNSProoface.FEATURE_FACE_SHOW_SETTINGS));
        Context applicationContext = requireContext().getApplicationContext();

        SNSErrorHandler errorHandler = e -> {
            Timber.d("The SDK throws an exception. Exception: %s", e);
            Toast.makeText(applicationContext, "The SDK throws an exception. Exception: $exception", Toast.LENGTH_SHORT).show();
        };

        SNSStateChangedHandler stateChangedHandler = (previousState, currentState) -> {

            Timber.d("The SDK state was changed: " + previousState + " -> " + currentState);

            if (currentState instanceof SNSSDKState.Ready) {
                Timber.d("SDK is ready");
            } else if (currentState instanceof SNSSDKState.Failed.Unauthorized) {
                Timber.e(((SNSSDKState.Failed.Unauthorized) currentState).getException(), "Invalid token or a token can't be refreshed by the SDK. Please, check your token expiration handler");
            } else if (currentState instanceof SNSSDKState.Failed.Unknown) {
                Timber.e(((SNSSDKState.Failed.Unknown) currentState).getException(), "Unknown error");
            } else if (currentState instanceof SNSSDKState.Initial) {
                Timber.d("No verification steps are passed yet");
            } else if (currentState instanceof SNSSDKState.Incomplete) {
                Timber.d("Some but not all verification steps are passed over");
            } else if (currentState instanceof SNSSDKState.Pending) {
                Timber.d("Verification is in pending state");
            } else if (currentState instanceof SNSSDKState.FinallyRejected) {
                Timber.d("Applicant has been finally rejected");
            } else if (currentState instanceof SNSSDKState.TemporarilyDeclined) {
                Timber.d("Applicant has been declined temporarily");
            } else if (currentState instanceof SNSSDKState.Approved) {
                Timber.d("Applicant has been approved");
            }

            if (currentState instanceof SNSSDKState.ActionCompleted) {
                SNSSDKState.ActionCompleted actionState = (SNSSDKState.ActionCompleted) currentState;
                String actionId = actionState.getActionId();
                FlowActionType type = actionState.getType();
                AnswerType answer = actionState.getAnswer();
                Map<String, Object> payload = actionState.getPayload();
            }
        };

        SNSCompleteHandler completeHandler = (result, state) -> {
            Timber.d("The SDK is finished. Result: " + result + " , State: " + state);
            Toast.makeText(applicationContext, "The SDK is finished. Result: $result, State: $state", Toast.LENGTH_SHORT).show();

            if (result instanceof SNSCompletionResult.SuccessTermination) {
                Timber.d(result.toString());
            } else if (result instanceof SNSCompletionResult.AbnormalTermination) {
                Timber.d(((SNSCompletionResult.AbnormalTermination) result).getException());
            }
        };

        SNSActionResultHandler actionResultHandler = (actionId, actionType, answer, allowContinuing) -> {
            Timber.d("Action Result: actionId: " + actionId + ", answer: " + answer);
            return SNSActionResult.Continue;
        };

        SNSEventHandler eventHandler = snsEvent -> {
            if (snsEvent instanceof SNSEvent.SNSEventStepInitiated) {
                Timber.d("onEvent: step initiated");
            } else if (snsEvent instanceof SNSEvent.SNSEventStepCompleted) {
                Timber.d("onEvent: step completed");
            }
        };

        try {
            SNSMobileSDK.SDK snsSdk = new SNSMobileSDK.Builder(requireActivity(), apiUrl, flowOrAction)
                    .withAccessToken(accessToken, tokenUpdater)
                    .withDebug(true)
                    .withModules(modules)
                    .withCompleteHandler(completeHandler)
                    .withErrorHandler(errorHandler)
                    .withStateChangedHandler(stateChangedHandler)
                    .withActionResultHandler(actionResultHandler)
                    .withEventHandler(eventHandler)
                    .build();

            snsSdk.launch();

        } catch (Exception e) {
            Timber.e(e);
        }
        showProgress(false);

    }

    private void showProgress(boolean show) {
        gContent.setVisibility(show ? View.GONE : View.VISIBLE);
        pbProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void setAccessToken(String accessToken) {
        etAccessToken.setText(accessToken);
        App.getInstance().getPrefManager().setAccessToken(accessToken);
    }

    private void setAccessTokenAction(String accessToken) {
        etAccessTokenAction.setText(accessToken);
        App.getInstance().getPrefManager().setAccessTokenAction(accessToken);
    }

    private void loadFlows(String authorizationToken, Filter<FlowItem> filter, FlowSelector selector) {
        showProgress(true);
        apiManager.getFlows(authorizationToken).enqueue(new Callback<FlowListResponse>() {

            @Override
            public void onResponse(Call<FlowListResponse> call, Response<FlowListResponse> response) {
                showProgress(false);
                try {
                    Iterator<FlowItem> iterator = response.body().getList().getItems().iterator();
                    ArrayList<CharSequence> items = new ArrayList<>();
                    while (iterator.hasNext()) {
                        FlowItem item = iterator.next();
                        if (filter.filter(item)) {
                            items.add(item.getName());
                        }
                    }

                    new MaterialAlertDialogBuilder(requireContext())
                            .setItems(items.toArray(new CharSequence[0]), (dialog, which) -> {
                                dialog.dismiss();
                                selector.onFlowSelected(items.get(which));
                            })
                            .create()
                            .show();
                } catch (Exception e) {
                    Timber.e(e);
                    Toast.makeText(requireContext(), "An error while getting flow list.\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FlowListResponse> call, Throwable t) {
                showProgress(false);
                Toast.makeText(requireContext(), "An error while getting flow list. Please, check your credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected int getSoftInputMode() {
        return WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    }

    private interface Filter<T> {
        boolean filter(T item);
    }

    private interface FlowSelector {
        void onFlowSelected(CharSequence flowName);
    }

}
