package com.sumsub.idensic.model;

import com.google.gson.annotations.SerializedName;
import com.sumsub.sns.core.data.model.FlowType;

public class FlowItem {

    @SerializedName("id")
    private final String id;
    @SerializedName("key")
    private final String key;
    @SerializedName("clientId")
    private final String clientId;
    @SerializedName("name")
    private final String name;
    @SerializedName("levelName")
    private final String levelName;
    @SerializedName("target")
    private final String target;
    @SerializedName("type")
    private final FlowType type;

    FlowItem(String id, String key, String clientId, String name, String levelName, String target, FlowType type) {
        this.id = id;
        this.key = key;
        this.clientId = clientId;
        this.name = name;
        this.levelName = levelName;
        this.target = target;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }

    public String getLevelName() {
        return levelName;
    }

    public String getTarget() {
        return target;
    }

    public FlowType getType() {
        return type;
    }
}
