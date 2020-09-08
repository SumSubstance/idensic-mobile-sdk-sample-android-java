package com.sumsub.idensic.model;

import com.google.gson.annotations.SerializedName;

public class AccessTokenResponse {

    @SerializedName("token")
    private final String token;
    @SerializedName("userId")
    private final String userId;
    @SerializedName("applicantId")
    private final String applicantId;

    public AccessTokenResponse(String token, String userId, String applicantId) {
        this.token = token;
        this.userId = userId;
        this.applicantId = applicantId;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }

    public String getApplicantId() {
        return applicantId;
    }
}
