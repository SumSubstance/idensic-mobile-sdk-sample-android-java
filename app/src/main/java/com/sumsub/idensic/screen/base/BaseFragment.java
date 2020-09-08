package com.sumsub.idensic.screen.base;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    private Handler mHandler;
    private int mOriginalSoftInputMode;

    protected BaseFragment(@LayoutRes int resId) {
        super(resId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOriginalSoftInputMode = requireActivity().getWindow().getAttributes().softInputMode;

        setSoftInputMode(getSoftInputMode());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHandler = new Handler();
    }

    @Override
    public void onDestroyView() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        setSoftInputMode(mOriginalSoftInputMode);
        super.onDestroy();
    }

    protected Handler getHandler() {
        return mHandler;
    }

    protected int getSoftInputMode() {
        return WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING;
    }

    private void setSoftInputMode(int mode) {
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().setSoftInputMode(mode);
        }
    }
}
