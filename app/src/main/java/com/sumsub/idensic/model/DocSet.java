package com.sumsub.idensic.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocSet {

    @SerializedName("idDocSetType")
    private final DocSetType idDocSetType;
    @SerializedName("types")
    private final List<DocType> types;
    @SerializedName("subTypes")
    private List<DocSubType> subTypes;

    public DocSet(DocSetType idDocSetType, List<DocType> types, List<DocSubType> subTypes) {
        this.idDocSetType = idDocSetType;
        this.types = types;
        this.subTypes = subTypes;
    }


    public DocSetType getIdDocSetType() {
        return idDocSetType;
    }

    public List<DocType> getTypes() {
        return types;
    }

    public List<DocSubType> getSubTypes() {
        return subTypes;
    }

    public void setSubTypes(List<DocSubType> subTypes) {
        this.subTypes = subTypes;
    }

}