package com.meest.meestbhart.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefaultAppResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
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

    public class Data {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("defaultApp")
        @Expose
        private String defaultApp;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getDefaultApp() {
            return defaultApp;
        }

        public void setDefaultApp(String defaultApp) {
            this.defaultApp = defaultApp;
        }

    }
}
