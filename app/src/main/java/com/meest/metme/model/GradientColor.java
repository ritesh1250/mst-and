package com.meest.metme.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GradientColor {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<Data> data;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public class Data {
        @SerializedName("firstcolor")
        @Expose
        private String firstcolor;
        @SerializedName("secondcolor")
        @Expose
        private String secondcolor;
        @SerializedName("id")
        @Expose
        private int id;

        public String getFirstcolor() {
            return firstcolor;
        }

        public void setFirstcolor(String firstcolor) {
            this.firstcolor = firstcolor;
        }

        public String getSecondcolor() {
            return secondcolor;
        }

        public void setSecondcolor(String secondcolor) {
            this.secondcolor = secondcolor;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

}

