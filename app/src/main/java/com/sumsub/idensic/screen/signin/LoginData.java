package com.sumsub.idensic.screen.signin;

import com.google.gson.annotations.SerializedName;

class LoginData {
    @SerializedName("url")
    String url;
    @SerializedName("t")
    String t;

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
