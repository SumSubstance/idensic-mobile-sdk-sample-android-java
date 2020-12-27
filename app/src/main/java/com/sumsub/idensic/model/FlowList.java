package com.sumsub.idensic.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FlowList {

    @SerializedName("items")
    private final List<FlowItem> items;

    FlowList(List<FlowItem> items) {
        this.items = items;
    }

    public List<FlowItem> getItems() {
        return items;
    }
}
