package com.sumsub.idensic.model;

import com.google.gson.annotations.SerializedName;

public class FlowListResponse {

    @SerializedName("list")
    private final FlowList list;
    @SerializedName("totalItems")
    private final int totalItems;

    FlowListResponse(FlowList list, int totalItems) {
        this.list = list;
        this.totalItems = totalItems;
    }

    public FlowList getList() {
        return list;
    }

    public int getTotalItems() {
        return totalItems;
    }
}
