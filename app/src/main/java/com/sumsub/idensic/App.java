package com.sumsub.idensic;

import com.sumsub.idensic.manager.ApiManager;
import com.sumsub.idensic.manager.PrefManager;

import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication {

    private static App instance;
    private PrefManager mPrefManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mPrefManager = new PrefManager(this);
    }

    public PrefManager getPrefManager() {
        return mPrefManager;
    }

    public static App getInstance() {
        return instance;
    }
}
