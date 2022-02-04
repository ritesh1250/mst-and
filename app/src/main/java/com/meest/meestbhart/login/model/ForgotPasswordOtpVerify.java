package com.meest.meestbhart.login.model;

public class ForgotPasswordOtpVerify {
    String mobile;
    String otp;

    public ForgotPasswordOtpVerify(String mobile, String otp) {
        this.mobile = mobile;
        this.otp = otp;
    }
}
