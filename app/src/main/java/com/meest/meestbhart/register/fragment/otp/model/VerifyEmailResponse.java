package com.meest.meestbhart.register.fragment.otp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyEmailResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("errorMessage")
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }
    //    @Expose
//    private Data errorMessage;
//
//    public Data getErrorMessage() {
//        return errorMessage;
//    }

//    public void setErrorMessage(Data errorMessage) {
//        this.errorMessage = errorMessage;
//    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public class Data {

        @SerializedName("message")
        @Expose
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }
}
