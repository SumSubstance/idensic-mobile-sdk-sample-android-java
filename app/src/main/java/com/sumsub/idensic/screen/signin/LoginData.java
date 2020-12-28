package com.sumsub.idensic.screen.signin;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Keep;

@Keep
class LoginData {
    @SerializedName("url")
    private String url;
    @SerializedName("t")
    private String t;

    public LoginData(String url, String t) {
        this.url = url;
        this.t = t;
    }

    public String getUrl() {
        return url;
    }

    public String getT() {
        return t;
    }
}
