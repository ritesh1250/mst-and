package com.meest.videomvvmmodule.model;

import com.google.gson.annotations.Expose;

public class PaytmTransaction {
    @Expose
    private String message;
    @Expose
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
