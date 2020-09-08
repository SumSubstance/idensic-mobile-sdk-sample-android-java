package com.sumsub.idensic.model;

import com.google.gson.annotations.SerializedName;

public class PayloadResponse {
    @SerializedName("status")
    private final String status;
    @SerializedName("payload")
    private final String payload;

    public PayloadResponse(String status, String payload) {
        this.status = status;
        this.payload = payload;
    }

    public String getStatus() {
        return status;
    }

    public String getPayload() {
        return payload;
    }
}
