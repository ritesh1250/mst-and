package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class UserFAQCategoryResponse {

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

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("rows")
        @Expose
        private List<Row> rows = null;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<Row> getRows() {
            return rows;
        }

        public void setRows(List<Row> rows) {
            this.rows = rows;
        }

    }

    public class Row {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("catagoryName")
        @Expose
        private String catagoryName;
        @SerializedName("status")
        @Expose
        private Boolean status;



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCatagoryName() {
            return catagoryName;
        }

        public void setCatagoryName(String catagoryName) {
            this.catagoryName = catagoryName;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }




    }

}