package com.meest.meestbhart.register.fragment.otp.model;

public class MobileOtpVerificationParam {
    String userName;
    String otp;
    String fcmToken;

    public MobileOtpVerificationParam(String userName, String otp) {
        this.userName = userName;
        this.otp = otp;
    }
}
