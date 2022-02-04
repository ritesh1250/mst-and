package com.meest.social.socialViewModel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileRespone1 {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private DataUser1 dataUser;
    @SerializedName("success")

    @Expose
    private Boolean success;

    @SerializedName("errorMessage")
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public DataUser1 getDataUser() {
        return dataUser;
    }

    public void setDataUser(DataUser1 dataUser) {
        this.dataUser = dataUser;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}