package com.sumsub.idensic.model;

import com.google.gson.annotations.SerializedName;

public class InfoAddress {

    @SerializedName("country")
    private final String country;
    @SerializedName("postCode")
    private final String postCode;
    @SerializedName("town")
    private final String town;
    @SerializedName("street")
    private final String street;
    @SerializedName("subStreet")
    private final String subStreet;
    @SerializedName("state")
    private final String state;
    @SerializedName("buildingName")
    private final String buildingName;
    @SerializedName("flatNumber")
    private final String flatNumber;
    @SerializedName("buildingNumber")
    private final String buildingNumber;
    @SerializedName("startDate")
    private final String startDate;
    @SerializedName("endDate")
    private final String endDate;

    public InfoAddress(String country, String postCode, String town, String street, String subStreet, String state, String buildingName, String flatNumber, String buildingNumber, String startDate, String endDate) {
        this.country = country;
        this.postCode = postCode;
        this.town = town;
        this.street = street;
        this.subStreet = subStreet;
        this.state = state;
        this.buildingName = buildingName;
        this.flatNumber = flatNumber;
        this.buildingNumber = buildingNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCountry() {
        return country;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getTown() {
        return town;
    }

    public String getStreet() {
        return street;
    }

    public String getSubStreet() {
        return subStreet;
    }

    public String getState() {
        return state;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

}
