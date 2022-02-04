package com.meest.meestbhart.login.model;

public class ResetEmailPasswordParam {
    String otp;
    String email;
    String newPassword;

    public ResetEmailPasswordParam(String otp, String email, String newPassword) {
        this.otp = otp;
        this.email = email;
        this.newPassword = newPassword;
    }
}
