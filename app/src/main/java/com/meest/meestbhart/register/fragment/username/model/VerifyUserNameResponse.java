package com.meest.meestbhart.register.fragment.username.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VerifyUserNameResponse {

    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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

    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public class Data {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("suggestions")
        @Expose
        private List<String> suggestions = null;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<String> getSuggestions() {
            return suggestions;
        }

        public void setSuggestions(List<String> suggestions) {
            this.suggestions = suggestions;
        }

    }
}
