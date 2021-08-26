package com.sumsub.idensic.model;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

public final class LevelListResponse {
   @SerializedName("list")
   @NotNull
   private final LevelList list;
   @SerializedName("totalItems")
   private final int totalItems;

   @NotNull
   public final LevelList getList() {
      return this.list;
   }

   public final int getTotalItems() {
      return this.totalItems;
   }

   public LevelListResponse(@NotNull LevelList list, int totalItems) {
      this.list = list;
      this.totalItems = totalItems;
   }
}
