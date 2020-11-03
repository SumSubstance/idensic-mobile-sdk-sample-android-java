package com.sumsub.idensic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import timber.log.Timber;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.security.ProviderInstaller;

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
    public void onProviderInstallFailed(@NonNull int errorCode, @NonNull Intent recoveryIntent) {
        Timber.d("onProviderInstallFailed: errorCode=%s", errorCode);
    }
}