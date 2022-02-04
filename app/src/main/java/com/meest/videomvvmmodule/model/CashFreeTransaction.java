package com.meest.videomvvmmodule.model;

import com.google.gson.annotations.Expose;

public class CashFreeTransaction {

    @Expose
    private CashFreeTransaction.Data data;
    @Expose
    private String message, name, phone, appID;
    @Expose
    private boolean status;
    @Expose
    private String order_id;

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public static class Data {

        @Expose
        private String status, message, cftoken;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCftoken() {
            return cftoken;
        }

        public void setCftoken(String cftoken) {
            this.cftoken = cftoken;
        }

    }
}
