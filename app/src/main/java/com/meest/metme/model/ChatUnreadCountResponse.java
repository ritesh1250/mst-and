package com.meest.metme.model;

public class ChatUnreadCountResponse {
    private int code;
    private int data;
    private Boolean success;

    public int getData() {
        return data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }
}
