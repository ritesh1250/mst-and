package com.meest.meestbhart.register.fragment.otp.model;

public class EmailOtpVerificationParam {

    String email;
    String otp;

    public EmailOtpVerificationParam(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }
}
