package com.meest.videomvvmmodule.model.elasticsearch;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class Source {

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("meestUserId")
    @Expose
    private String meestUserId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("user_name")
    @Expose
    private String user_name;
    @SerializedName("user_email")
    @Expose
    private String user_email;
    @SerializedName("user_mobile_no")
    @Expose
    private String user_mobile_no;
    @SerializedName("user_profile")
    @Expose
    private String user_profile;
    @SerializedName("cover_profile")
    @Expose
    private String cover_profile;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMeestUserId() {
        return meestUserId;
    }

    public void setMeestUserId(String meestUserId) {
        this.meestUserId = meestUserId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_mobile_no() {
        return user_mobile_no;
    }

    public void setUser_mobile_no(String user_mobile_no) {
        this.user_mobile_no = user_mobile_no;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getCover_profile() {
        return cover_profile;
    }

    public void setCover_profile(String cover_profile) {
        this.cover_profile = cover_profile;
    }
}