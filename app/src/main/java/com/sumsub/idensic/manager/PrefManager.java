package com.sumsub.idensic.manager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class PrefManager {

    private final SharedPreferences mPrefs;

    public PrefManager(Context context) {
        mPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    public void setUsername(String name) {
        mPrefs.edit().putString(KEY_USER_USERNAME, name).apply();
    }

    @Nullable
    public String getUsername() {
        return mPrefs.getString(KEY_USER_USERNAME, null);
    }

    public void setPassword(String password) {
        mPrefs.edit().putString(KEY_USER_PASSWORD, password).apply();
    }

    @Nullable
    public String getPassword() {
        return mPrefs.getString(KEY_USER_PASSWORD, null);
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

    public void setApplicantId(String applicantId) {
        mPrefs.edit().putString(KEY_APPLICANT_ID, applicantId).apply();
    }

    @Nullable
    public String getApplicantId() {
        return mPrefs.getString(KEY_APPLICANT_ID, null);
    }

    public void setUserId(String userId) {
        mPrefs.edit().putString(KEY_USER_ID, userId).apply();
    }

    @Nullable
    public String getUserId() {
        return mPrefs.getString(KEY_USER_ID, null);
    }

    private static final String KEY_USER_USERNAME = "user_username";
    private static final String KEY_USER_PASSWORD = "user_password";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_APPLICANT_ID = "applicant_id";
    private static final String KEY_USER_ID = "user_id";

}
