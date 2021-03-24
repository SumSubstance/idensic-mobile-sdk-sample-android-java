package com.sumsub.idensic.screen.splash;

import android.view.View;

import com.sumsub.idensic.App;
import com.sumsub.idensic.R;
import com.sumsub.idensic.screen.base.BaseFragment;

import java.util.concurrent.TimeUnit;

import androidx.navigation.fragment.NavHostFragment;

public class SplashFragment extends BaseFragment {

    private final Runnable selectEntryPoint;

    public SplashFragment() {
        super(R.layout.fragment_splash);
        selectEntryPoint = () -> {
            boolean loggedIn = App.getInstance().getPrefManager().getToken() != null;
            int actionId = loggedIn ? R.id.action_splash_to_main : R.id.action_splash_to_sign_in;
            NavHostFragment.findNavController(SplashFragment.this).navigate(actionId);
        };
    }

    @Override
    public void onPause() {
        final View view = getView();
        if (view != null) {
            view.removeCallbacks(selectEntryPoint);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        final View view = getView();
        if (view != null) {
            view.postDelayed(selectEntryPoint, TimeUnit.SECONDS.toMillis(1));
        }
    }
}
