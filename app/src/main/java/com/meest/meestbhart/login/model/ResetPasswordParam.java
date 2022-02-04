package com.meest.meestbhart.login.model;

public class ResetPasswordParam {
    String otp;
    String mobile;
    String newPassword;

    public ResetPasswordParam(String otp, String mobile, String newPassword) {
        this.otp = otp;
        this.mobile = mobile;
        this.newPassword = newPassword;
    }
}
