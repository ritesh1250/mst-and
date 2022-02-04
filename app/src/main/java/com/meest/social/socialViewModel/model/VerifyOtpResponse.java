package com.meest.social.socialViewModel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.meest.meestbhart.register.fragment.otp.model.VerifyEmailResponse;

public class VerifyOtpResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private VerifyEmailResponse.Data data;
    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("errorMessage")
    @Expose
    private ErrorMessage errorMessage;

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public VerifyEmailResponse.Data getData() {
        return data;
    }

    public void setData(VerifyEmailResponse.Data data) {
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
    public class ErrorMessage {

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
