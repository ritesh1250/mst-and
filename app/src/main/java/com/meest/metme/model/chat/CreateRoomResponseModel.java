package com.meest.metme.model.chat;

public class CreateRoomResponseModel {

    public int code;
    public Data data;

    public int getCode() {
        return code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean success;
}
