package com.meest.videomvvmmodule.model.follower;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("success")
    @Expose
    private boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
