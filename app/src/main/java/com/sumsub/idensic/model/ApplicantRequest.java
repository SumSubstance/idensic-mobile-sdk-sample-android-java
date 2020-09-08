package com.sumsub.idensic.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApplicantRequest {

    @SerializedName("requiredIdDocs")
    private final DocSets requiredIdDocs;
    @SerializedName("externalUserId")
    private final String externalUserId;
    @SerializedName("sourceKey")
    private final String sourceKey;
    @SerializedName("email")
    private final String email;
    @SerializedName("lang")
    private final String lang;
    @SerializedName("metadata")
    private final List<String> metadata;
    @SerializedName("info")
    private final InfoAttribute info;

    public ApplicantRequest(DocSets requiredIdDocs, String externalUserId, String sourceKey, String email, String lang, List<String> metadata, InfoAttribute info) {
        this.requiredIdDocs = requiredIdDocs;
        this.externalUserId = externalUserId;
        this.sourceKey = sourceKey;
        this.email = email;
        this.lang = lang;
        this.metadata = metadata;
        this.info = info;
    }

    public DocSets getRequiredIdDocs() {
        return requiredIdDocs;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public String getEmail() {
        return email;
    }

    public String getLang() {
        return lang;
    }

    public List<String> getMetadata() {
        return metadata;
    }

    public InfoAttribute getInfo() {
        return info;
    }
}