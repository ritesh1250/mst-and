package com.meest.social.socialViewModel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RemoveBlockedUserResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
//    @SerializedName("data")
//    @Expose
//    private List<Integer> data = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

//    public List<Integer> getData() {
//        return data;
//    }
//
//    public void setData(List<Integer> data) {
//        this.data = data;
//    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}