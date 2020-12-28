package com.sumsub.idensic.screen.splash;

import android.os.Bundle;
import android.view.View;

import com.sumsub.idensic.App;
import com.sumsub.idensic.R;
import com.sumsub.idensic.screen.base.BaseFragment;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

public class SplashFragment extends BaseFragment {

    public SplashFragment() {
        super(R.layout.fragment_splash);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getHandler().postDelayed(() -> {
            boolean loggedIn = App.getInstance().getPrefManager().getToken() != null;
            int actionId = loggedIn ? R.id.action_splash_to_main : R.id.action_splash_to_sign_in;
            NavHostFragment.findNavController(SplashFragment.this).navigate(actionId);
        }, TimeUnit.SECONDS.toMillis(1));
    }
}
