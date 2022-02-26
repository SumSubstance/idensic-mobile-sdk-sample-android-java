package com.sumsub.idensic;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.security.ProviderInstaller;

import timber.log.Timber;

public class AppActivity extends AppCompatActivity implements ProviderInstaller.ProviderInstallListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        ProviderInstaller.installIfNeededAsync(this, this);
    }

    @Override
    public void onProviderInstalled() {
        Timber.d("onProviderInstalled");
    }

    @Override
    public void onProviderInstallFailed(int errorCode, Intent recoveryIntent) {
        Timber.d("onProviderInstallFailed: errorCode=%s", errorCode);
    }
}