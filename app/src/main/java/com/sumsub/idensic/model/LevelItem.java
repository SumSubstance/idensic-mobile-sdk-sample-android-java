package com.sumsub.idensic.model;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

public final class LevelItem {
   @SerializedName("id")
   @Nullable
   private final String id;
   @SerializedName("key")
   @Nullable
   private final String key;
   @SerializedName("clientId")
   @Nullable
   private final String clientId;
   @SerializedName("name")
   @Nullable
   private final String name;
   @SerializedName("reviewReasonCode")
   @Nullable
   private final String reviewReasonCode;
   @SerializedName("createdAt")
   @Nullable
   private final String createdAt;
   @SerializedName("createdBy")
   @Nullable
   private final String createdBy;
   @SerializedName("modifiedAt")
   @Nullable
   private final String modifiedAt;
   @SerializedName("msdkFlowId")
   @Nullable
   private final String msdkFlowId;

   @Nullable
   public final String getId() {
      return this.id;
   }

   @Nullable
   public final String getKey() {
      return this.key;
   }

   @Nullable
   public final String getClientId() {
      return this.clientId;
   }

   @Nullable
   public final String getName() {
      return this.name;
   }

   @Nullable
   public final String getReviewReasonCode() {
      return this.reviewReasonCode;
   }

   @Nullable
   public final String getCreatedAt() {
      return this.createdAt;
   }

   @Nullable
   public final String getCreatedBy() {
      return this.createdBy;
   }

   @Nullable
   public final String getModifiedAt() {
      return this.modifiedAt;
   }

   @Nullable
   public final String getMsdkFlowId() {
      return this.msdkFlowId;
   }

   public LevelItem(@Nullable String id, @Nullable String key, @Nullable String clientId, @Nullable String name, @Nullable String reviewReasonCode, @Nullable String createdAt, @Nullable String createdBy, @Nullable String modifiedAt, @Nullable String msdkFlowId) {
      this.id = id;
      this.key = key;
      this.clientId = clientId;
      this.name = name;
      this.reviewReasonCode = reviewReasonCode;
      this.createdAt = createdAt;
      this.createdBy = createdBy;
      this.modifiedAt = modifiedAt;
      this.msdkFlowId = msdkFlowId;
   }

}
