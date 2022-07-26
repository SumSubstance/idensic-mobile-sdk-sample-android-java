package com.sumsub.idensic.manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class PrefManager {

    private final SharedPreferences mPrefs;

    public PrefManager(Context context) {
        mPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public void setUrl(String url) {
        mPrefs.edit().putString(KEY_URL, url).apply();
    }

    @Nullable
    public String getUrl() {
        return mPrefs.getString(KEY_URL, null);
    }

    public void setToken(String token) {
        mPrefs.edit().putString(KEY_TOKEN, token).apply();
    }

    @Nullable
    public String getToken() {
        return mPrefs.getString(KEY_TOKEN, null);
    }

    public void setAccessToken(String accessToken) {
        mPrefs.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Nullable
    public String getAccessToken() {
        return mPrefs.getString(KEY_ACCESS_TOKEN, null);
    }

    public void setUserId(String userId) {
        mPrefs.edit().putString(KEY_USER_ID, userId).apply();
    }

    @Nullable
    public String getUserId() {
        return mPrefs.getString(KEY_USER_ID, null);
    }

    public void setActionId(String actionId) {
        mPrefs.edit().putString(KEY_ACTION_ID, actionId).apply();
    }

    @Nullable
    public String getActionId() {
        return mPrefs.getString(KEY_ACTION_ID, null);
    }

    public void setAccessTokenAction(String accessToken) {
        mPrefs.edit().putString(KEY_ACCESS_TOKEN_ACTION, accessToken).apply();
    }

    @Nullable
    public String getAccessTokenAction() {
        return mPrefs.getString(KEY_ACCESS_TOKEN_ACTION, null);
    }

    public boolean isSandbox() {
        return mPrefs.getBoolean(KEY_SANDBOX, false);
    }

    public void setSandbox(boolean value) {
        mPrefs.edit().putBoolean(KEY_SANDBOX, value).apply();
    }

    @Nullable
    public String getClientId() {
        return mPrefs.getString(KEY_CLIENT_ID, null);
    }

    public void setClientId(String value) {
        mPrefs.edit().putString(KEY_CLIENT_ID, value).apply();
    }

    private static final String KEY_URL = "url";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_ACCESS_TOKEN_ACTION = "access_token_action";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ACTION_ID = "action_id";

    private static final String KEY_SANDBOX = "sandbox";
    private static final String KEY_CLIENT_ID = "clientid";
}
