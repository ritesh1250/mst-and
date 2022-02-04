package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
