package com.meest.meestbhart.register.fragment.otp.model;

import com.google.gson.annotations.SerializedName;

public class VerifiyEmailAndMobileParam {

    @SerializedName("userName")
    String userName;

    public VerifiyEmailAndMobileParam(String userName) {
        this.userName = userName;
    }
}
