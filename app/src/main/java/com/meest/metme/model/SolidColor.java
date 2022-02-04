package com.meest.metme.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SolidColor {
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
        int count;
        List<Row> rows;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<Row> getList() {
            return rows;
        }

        public void setList(List<Row> list) {
            this.rows = list;
        }
    }

    public class Row {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("sendercolor")
        @Expose
        private String sendercolor;
        @SerializedName("bgColor")
        @Expose
        private String bgColor;
        @SerializedName("recievercolor")
        @Expose
        private String recievercolor;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSendercolor() {
            return sendercolor;
        }

        public void setSendercolor(String sendercolor) {
            this.sendercolor = sendercolor;
        }

        public String getBgColor() {
            return bgColor;
        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }

        public String getRecievercolor() {
            return recievercolor;
        }

        public void setRecievercolor(String recievercolor) {
            this.recievercolor = recievercolor;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}

