package com.sumsub.idensic.model;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public final class LevelList {
   @SerializedName("items")
   @NotNull
   private final List<LevelItem> items;

   @NotNull
   public final List<LevelItem> getItems() {
      return this.items;
   }

   public LevelList(@NotNull List<LevelItem> items) {
      this.items = items;
   }
}