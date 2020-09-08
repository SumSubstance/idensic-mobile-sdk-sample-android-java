package com.sumsub.idensic.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InfoAttribute {

    @SerializedName("firstName")
    private final String firstName;
    @SerializedName("lastName")
    private final String lastName;
    @SerializedName("middleName")
    private final String middleName;
    @SerializedName("legalName")
    private final String legalName;
    @SerializedName("gender")
    private final String gender;
    @SerializedName("dob")
    private final String dob;
    @SerializedName("placeOfBirth")
    private final String placeOfBirth;
    @SerializedName("countryOfBirth")
    private final String countryOfBirth;
    @SerializedName("stateOfBirth")
    private final String stateOfBirth;
    @SerializedName("country")
    private final String country;
    @SerializedName("nationality")
    private final String nationality;
    @SerializedName("phone")
    private final String phone;
    @SerializedName("addresses")
    private final List<InfoAddress> addresses;

    public InfoAttribute() {
        this.firstName = null;
        this.lastName = null;
        this.middleName = null;
        this.legalName = null;
        this.gender = null;
        this.dob = null;
        this.placeOfBirth = null;
        this.countryOfBirth = null;
        this.stateOfBirth = null;
        this.country = null;
        this.nationality = null;
        this.phone = null;
        this.addresses = null;
    }

    public InfoAttribute(String firstName, String lastName, String middleName, String legalName, String gender, String dob, String placeOfBirth, String countryOfBirth, String stateOfBirth, String country, String nationality, String phone, List<InfoAddress> addresses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.legalName = legalName;
        this.gender = gender;
        this.dob = dob;
        this.placeOfBirth = placeOfBirth;
        this.countryOfBirth = countryOfBirth;
        this.stateOfBirth = stateOfBirth;
        this.country = country;
        this.nationality = nationality;
        this.phone = phone;
        this.addresses = addresses;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLegalName() {
        return legalName;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public String getStateOfBirth() {
        return stateOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPhone() {
        return phone;
    }

    public List<InfoAddress> getAddresses() {
        return addresses;
    }
}