package com.meest.social.socialViewModel.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UpdatePasswordResponse1 {
    public class Error {


    }

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("error")
    @Expose
    private Error error;
    @SerializedName("data")
    @Expose
    private Object data;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
