package com.sumsub.idensic.screen.signin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sumsub.idensic.App;
import com.sumsub.idensic.R;
import com.sumsub.idensic.model.PayloadResponse;
import com.sumsub.idensic.screen.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.navigation.fragment.NavHostFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInFragment extends BaseFragment {

    private Group gContent;
    private ProgressBar pbProgress;
    private TextInputLayout tlUsername;
    private TextInputEditText etUsername;
    private TextInputLayout tlPassword;
    private TextInputEditText etPassword;

    public SignInFragment() {
        super(R.layout.fragment_sign_in);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gContent = view.findViewById(R.id.g_content);
        pbProgress = view.findViewById(R.id.pb_progress);
        tlUsername = view.findViewById(R.id.tl_username);
        etUsername = view.findViewById(R.id.et_username);
        tlPassword = view.findViewById(R.id.tl_password);
        etPassword = view.findViewById(R.id.et_password);
        MaterialButton btnSignIn = view.findViewById(R.id.btn_sign_in);

        showProgress(false);
        btnSignIn.setOnClickListener(v -> login());

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tlUsername.setError(null);
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tlPassword.setError(null);
            }
        });
    }

    private void login() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        boolean error = false;

        if (username == null || username.isEmpty()) {
            error = true;
            tlUsername.setError("Username is empty");
        }

        if (password == null || password.isEmpty()) {
            error = true;
            tlPassword.setError("Password is empty");
        }

        if (error) {
            return;
        }

        showProgress(true);

        App.getInstance().getApiManager().login(username, password).enqueue(new Callback<PayloadResponse>() {
            @Override
            public void onResponse(@NotNull Call<PayloadResponse> call, @NotNull Response<PayloadResponse> response) {
                App.getInstance().getPrefManager().setUsername(username);
                App.getInstance().getPrefManager().setPassword(password);
                App.getInstance().getPrefManager().setToken(response.body().getPayload());

                NavHostFragment.findNavController(SignInFragment.this).navigate(R.id.action_sign_in_to_main);
                showProgress(false);
            }

            @Override
            public void onFailure(@NotNull Call<PayloadResponse> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), "Please, check your credentials or the internet connection and try again", Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
    }

    private void showProgress(boolean show) {
        gContent.setVisibility(show ? View.GONE : View.VISIBLE);
        pbProgress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    protected int getSoftInputMode() {
        return WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    }
}
