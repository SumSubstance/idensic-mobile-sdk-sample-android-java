package com.sumsub.idensic.screen.signin;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Keep;

@Keep
class LoginData {
    @SerializedName("url")
    private final String url;
    @SerializedName("t")
    private final String t;

    @SerializedName("email")
    private final String email;

    @SerializedName("sandbox")
    private final boolean legacySandBox;

    @SerializedName("sbx")
    private final int sbx;

    @SerializedName("c")
    private final String clientId;

    public LoginData(String url, String t, String email, Boolean legacySandBox, int sbx, String clientId) {
        this.url = url;
        this.t = t;
        this.email = email;
        this.legacySandBox = legacySandBox;
        this.sbx = sbx;
        this.clientId = clientId;
    }

    public String getUrl() {
        return url;
    }

    public String getT() {
        return t;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSandbox() {
        return sbx == 1 || legacySandBox;
    }

    public String getClientId() {
        return clientId;
    }
}
