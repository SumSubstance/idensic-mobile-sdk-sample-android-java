package com.sumsub.idensic.screen.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sumsub.idensic.App;
import com.sumsub.idensic.BuildConfig;
import com.sumsub.idensic.R;
import com.sumsub.idensic.common.Constants;
import com.sumsub.idensic.manager.PrefManager;
import com.sumsub.idensic.model.AccessTokenResponse;
import com.sumsub.idensic.screen.base.BaseFragment;
import com.sumsub.sns.core.SNSActionResult;
import com.sumsub.sns.core.SNSMobileSDK;
import com.sumsub.sns.core.SNSModule;
import com.sumsub.sns.core.data.listener.TokenExpirationHandler;
import com.sumsub.sns.core.data.model.SNSCompletionResult;
import com.sumsub.sns.core.data.model.SNSException;
import com.sumsub.sns.core.data.model.SNSSDKState;
import com.sumsub.sns.liveness3d.SNSLiveness3d;
import com.sumsub.sns.prooface.SNSProoface;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.navigation.fragment.NavHostFragment;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
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

    private final TokenExpirationHandler sdkFlowAccessTokeExpirationHandler = () -> {
        PrefManager prefManager = App.getInstance().getPrefManager();
        String token = prefManager.getToken();
        String userId = prefManager.getUserId();

        try {
            String newAccessToken = App.getInstance().getApiManager().getAccessTokenForFlow(token, userId).execute().body().getToken();
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
            String newAccessToken = App.getInstance().getApiManager().getAccessTokenForAction(token, userId, actionId).execute().body().getToken();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PrefManager prefManager = App.getInstance().getPrefManager();

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
            prefManager.setUsername(null);
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

        App.getInstance().getApiManager().getAccessTokenForFlow(token, userId).enqueue(new Callback<AccessTokenResponse>() {
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

        App.getInstance().getApiManager().getAccessTokenForAction(token, userId, actionId).enqueue(new Callback<AccessTokenResponse>() {
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

        String apiUrl = BuildConfig.API_URL;
        List<SNSModule> modules = Arrays.asList(new SNSProoface(SNSProoface.FEATURE_FACE_SHOW_SETTINGS));
        Context applicationContext = requireContext().getApplicationContext();

        Function1<SNSException, Unit> onError = e -> {
            Timber.d("The SDK throws an exception. Exception: %s", e);
            Toast.makeText(applicationContext, "The SDK throws an exception. Exception: $exception", Toast.LENGTH_SHORT).show();
            return Unit.INSTANCE;
        };

        Function2<SNSSDKState, SNSSDKState, Unit> onStateChanged = (newState, prevState) -> {
            Timber.d("The SDK state was changed: " + prevState + " -> " + newState);

            if (newState instanceof SNSSDKState.Ready) {
                Timber.d("SDK is ready");
            } else if (newState instanceof SNSSDKState.Failed.Unauthorized) {
                Timber.e(((SNSSDKState.Failed.Unauthorized) newState).getException(), "Invalid token or a token can't be refreshed by the SDK. Please, check your token expiration handler");
            } else if (newState instanceof SNSSDKState.Failed.Unknown) {
                Timber.e(((SNSSDKState.Failed.Unknown) newState).getException(), "Unknown error");
            } else if (newState instanceof SNSSDKState.Initial) {
                Timber.d("No verification steps are passed yet");
            } else if (newState instanceof SNSSDKState.Incomplete) {
                Timber.d("Some but not all verification steps are passed over");
            } else if (newState instanceof SNSSDKState.Pending) {
                Timber.d("Verification is in pending state");
            } else if (newState instanceof SNSSDKState.FinallyRejected) {
                Timber.d("Applicant has been finally rejected");
            } else if (newState instanceof SNSSDKState.TemporarilyDeclined) {
                Timber.d("Applicant has been declined temporarily");
            } else if (newState instanceof SNSSDKState.Approved) {
                Timber.d("Applicant has been approved");
            }

            return Unit.INSTANCE;
        };

        Function2<SNSCompletionResult, SNSSDKState, Unit> onSDKCompletedHandler = (result, state) -> {
            Timber.d("The SDK is finished. Result: " + result + " , State: " + state);
            Toast.makeText(applicationContext, "The SDK is finished. Result: $result, State: $state", Toast.LENGTH_SHORT).show();

            if (result instanceof SNSCompletionResult.SuccessTermination) {
                Timber.d(result.toString());
            } else if (result instanceof SNSCompletionResult.AbnormalTermination) {
                Timber.d(((SNSCompletionResult.AbnormalTermination) result).getException());
            }
            return Unit.INSTANCE;
        };

        Function2<String, String, SNSActionResult> onActionResult = (actionId, answer) -> {
            Timber.d("Action Result: actionId: " + actionId + ", answer: " + answer);
            return SNSActionResult.Continue;
        };

        try {

            SNSMobileSDK.SDK snsSdk = new SNSMobileSDK.Builder(requireActivity(), apiUrl, flowOrAction)
                    .withAccessToken(accessToken, tokenUpdater)
                    .withDebug(true)
                    .withModules(modules)
                    .withHandlers(onError, onStateChanged, onSDKCompletedHandler, onActionResult)
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

    @Override
    protected int getSoftInputMode() {
        return WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    }
}
