package com.meest.videomvvmmodule.model.follower;


import com.google.gson.annotations.Expose;

public class Followerstatus {
    @Expose
    public String status;
    @Expose
    public int code;
    @Expose
    public String message;

    public String isStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
