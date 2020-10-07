package com.sumsub.idensic.screen.main;

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
import com.sumsub.idensic.model.ApplicantResponse;
import com.sumsub.idensic.screen.base.BaseFragment;
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
import java.util.Collections;
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
    private TextInputEditText etApplicant;
    private TextInputEditText etAccessToken;
    private TextInputEditText etFlowName;

    private final TokenExpirationHandler sdkExpirationHandler = () -> {
        PrefManager prefManager = App.getInstance().getPrefManager();
        String token = prefManager.getToken();
        String applicantId = prefManager.getApplicantId();
        String userId = prefManager.getUserId();

        try {
            String newAccessToken = App.getInstance().getApiManager().getAccessToken(token, applicantId, userId).execute().body().getToken();
            prefManager.setAccessToken(newAccessToken);
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

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        gContent = view.findViewById(R.id.g_content);
        pbProgress = view.findViewById(R.id.pb_progress);
        etApplicant = view.findViewById(R.id.et_applicant);
        etAccessToken = view.findViewById(R.id.et_access_token);
        etFlowName = view.findViewById(R.id.et_flow_name);
        MaterialButton btnApplicant = view.findViewById(R.id.btn_generate_applicant);
        MaterialButton btnStart = view.findViewById(R.id.btn_start);

        showProgress(false);
        setApplicantId(App.getInstance().getPrefManager().getApplicantId());
        setAccessToken(App.getInstance().getPrefManager().getAccessToken());

        toolbar.inflateMenu(R.menu.main);

        toolbar.setOnMenuItemClickListener(item -> {
            // Clear cache
            PrefManager prefManager = App.getInstance().getPrefManager();
            prefManager.setUsername(null);
            prefManager.setPassword(null);
            prefManager.setToken(null);
            prefManager.setAccessToken(null);
            prefManager.setApplicantId(null);

            NavHostFragment.findNavController(MainFragment.this).navigate(R.id.action_main_to_sign_in);
            return false;
        });
        btnApplicant.setOnClickListener(v -> generateApplicant());

        btnStart.setOnClickListener(v -> startSDK());
    }

    private void generateApplicant() {
        showProgress(true);

        String token = App.getInstance().getPrefManager().getToken();
        String userId = String.format(Constants.USER_ID, UUID.randomUUID().toString());
        App.getInstance().getPrefManager().setUserId(userId);

        App.getInstance().getApiManager().getApplicantId(token, Arrays.asList(Constants.identity, Constants.selfie, Constants.proofOfResidence), userId).enqueue(new Callback<ApplicantResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApplicantResponse> call, @NotNull Response<ApplicantResponse> response) {
                showProgress(false);
                setApplicantId(response.body().getId());
                Toast.makeText(requireContext(), "New Applicant has been created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<ApplicantResponse> call, @NotNull Throwable t) {
                showProgress(false);
                Timber.e(t, "An error while creating a new applicant");
                Toast.makeText(requireContext(), "An error while creating a new applicant, Please, check logs in LogCat.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSDK() {
        PrefManager prefManager = App.getInstance().getPrefManager();
        String token = prefManager.getToken();

        String applicantId = etApplicant.getText().toString();
        String userId = prefManager.getUserId();

        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(), "A token is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (applicantId == null || applicantId.isEmpty()) {
            Toast.makeText(requireContext(), "An applicant is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(requireContext(), "An user id is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgress(true);

        App.getInstance().getApiManager().getAccessToken(token, applicantId, userId).enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(@NotNull Call<AccessTokenResponse> call, @NotNull Response<AccessTokenResponse> response) {
                String accessToken = response.body().getToken();
                setAccessToken(accessToken);
                launchSdk(accessToken);
            }

            @Override
            public void onFailure(@NotNull Call<AccessTokenResponse> call, @NotNull Throwable t) {
                showProgress(false);
                Toast.makeText(requireContext(), "An error while getting an access token. Please, check your applicant", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void launchSdk(String accessToken) {
        String apiUrl = BuildConfig.API_URL;
        List<SNSModule> modules = Arrays.asList(new SNSLiveness3d(), new SNSProoface());
        String flowName = etFlowName.getText().toString();

        Function1<SNSException, Unit> onError = e -> {
            Timber.d("The SDK throws an exception. Exception: %s", e);
            Toast.makeText(requireContext(), "The SDK throws an exception. Exception: $exception", Toast.LENGTH_SHORT).show();
            return Unit.INSTANCE;
        };

        Function2<SNSSDKState, SNSSDKState, Unit> onStateChanged = (newState, prevState) -> {
            Timber.d("The SDK state was changed: " + prevState + " -> " + newState);

            if (newState instanceof SNSSDKState.Ready) {
                Timber.d("SDK is ready");
            } else if (newState instanceof SNSSDKState.Failed.Unauthorized) {
                Timber.e(((SNSSDKState.Failed.Unauthorized) newState).getException(),"Invalid token or a token can't be refreshed by the SDK. Please, check your token expiration handler");
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
            Timber.d("The SDK is finished. Result: " +  result + " , State: " + state);
            Toast.makeText(requireContext(), "The SDK is finished. Result: $result, State: $state", Toast.LENGTH_SHORT).show();

            if (result instanceof SNSCompletionResult.SuccessTermination) {
                Timber.d(result.toString());
            } else if (result instanceof SNSCompletionResult.AbnormalTermination) {
                Timber.d(((SNSCompletionResult.AbnormalTermination) result).getException());
            }
            return Unit.INSTANCE;
        };

        SNSMobileSDK.SDK snsSdk = new SNSMobileSDK.Builder(requireActivity(), apiUrl, flowName)
                .withAccessToken(accessToken,sdkExpirationHandler)
                .withDebug(true)
                .withModules(modules)
                .withHandlers(onError, onStateChanged, onSDKCompletedHandler)
                .build();

        snsSdk.launch();
        showProgress(false);

    }


    private void showProgress(boolean show) {
        gContent.setVisibility(show ? View.GONE : View.VISIBLE);
        pbProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void setApplicantId(String applicantId) {
        etApplicant.setText(applicantId);
        App.getInstance().getPrefManager().setApplicantId(applicantId);
    }

    private void setAccessToken(String accessToken) {
        etAccessToken.setText(accessToken);
        App.getInstance().getPrefManager().setAccessToken(accessToken);
    }

    @Override
    protected int getSoftInputMode() {
        return WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    }
}
