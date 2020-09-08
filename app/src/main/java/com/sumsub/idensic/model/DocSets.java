package com.sumsub.idensic.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocSets {

    @SerializedName("docSets")
    private final List<DocSet> docSets;

    public DocSets(List<DocSet> docSets) {
        this.docSets = docSets;
    }

    public List<DocSet> getDocSets() {
        return docSets;
    }
}
