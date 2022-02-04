package com.meest.Paramaters;

public class UpdatePasswordParams {
    String oldPassword;
    String newPassword;

    public UpdatePasswordParams(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
