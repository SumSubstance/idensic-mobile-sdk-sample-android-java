package com.sumsub.idensic;

import com.sumsub.idensic.manager.ApiManager;
import com.sumsub.idensic.manager.PrefManager;

import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication {

    private static App instance;
    private PrefManager mPrefManager;
    private ApiManager apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mPrefManager = new PrefManager(this);
        apiManager = new ApiManager();
    }

    public PrefManager getPrefManager() {
        return mPrefManager;
    }

    public ApiManager getApiManager() {
        return apiManager;
    }

    public static App getInstance() {
        return instance;
    }
}
