package com.sumsub.idensic.model;

import com.google.gson.annotations.SerializedName;

public class ApplicantResponse {

    @SerializedName("id")
    private String id = "";
    @SerializedName("createdAt")
    private final String createdAt;
    @SerializedName("key")
    private final String key;
    @SerializedName("clientId")
    private final String clientId;
    @SerializedName("inspectionId")
    private final String inspectionId;
    @SerializedName("externalUserId")
    private final String externalUserId;
    @SerializedName("info")
    private final InfoAttribute info;
    @SerializedName("env")
    private final String env;
    @SerializedName("requiredIdDocs")
    private final DocSets requiredIdDocs;
    @SerializedName("review")
    private final Review review;
    @SerializedName("type")
    private final String type;

    public ApplicantResponse(String id, String createdAt, String key, String clientId, String inspectionId, String externalUserId, InfoAttribute info, String env, DocSets requiredIdDocs, Review review, String type) {
        this.id = id;
        this.createdAt = createdAt;
        this.key = key;
        this.clientId = clientId;
        this.inspectionId = inspectionId;
        this.externalUserId = externalUserId;
        this.info = info;
        this.env = env;
        this.requiredIdDocs = requiredIdDocs;
        this.review = review;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getKey() {
        return key;
    }

    public String getClientId() {
        return clientId;
    }

    public String getInspectionId() {
        return inspectionId;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public InfoAttribute getInfo() {
        return info;
    }

    public String getEnv() {
        return env;
    }

    public DocSets getRequiredIdDocs() {
        return requiredIdDocs;
    }

    public Review getReview() {
        return review;
    }

    public String getType() {
        return type;
    }

    static class Review {

        @SerializedName("createDate")
        private final String createDate;
        @SerializedName("reviewStatus")
        private final String reviewStatus;
        @SerializedName("notificationFailureCnt")
        private final Integer notificationFailureCnt;
        @SerializedName("priority")
        private final Integer priority;
        @SerializedName("autoChecked")
        private final Boolean autoChecked;

        public Review(String createDate, String reviewStatus, Integer notificationFailureCnt, Integer priority, Boolean autoChecked) {
            this.createDate = createDate;
            this.reviewStatus = reviewStatus;
            this.notificationFailureCnt = notificationFailureCnt;
            this.priority = priority;
            this.autoChecked = autoChecked;
        }

        public String getCreateDate() {
            return createDate;
        }

        public String getReviewStatus() {
            return reviewStatus;
        }

        public Integer getNotificationFailureCnt() {
            return notificationFailureCnt;
        }

        public Integer getPriority() {
            return priority;
        }

        public Boolean getAutoChecked() {
            return autoChecked;
        }
    }
}
