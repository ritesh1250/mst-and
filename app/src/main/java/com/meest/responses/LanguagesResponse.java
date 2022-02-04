package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LanguagesResponse {

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



    public class Row {

        @SerializedName("languageNameNative")
        @Expose
        private String languageNameNative;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("languageNameEnglish")
        @Expose
        private String languageNameEnglish;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("status")
        @Expose
        private Boolean status;



        public String getLanguageNameNative() {
            return languageNameNative;
        }

        public void setLanguageNameNative(String languageNameNative) {
            this.languageNameNative = languageNameNative;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLanguageNameEnglish() {
            return languageNameEnglish;
        }

        public void setLanguageNameEnglish(String languageNameEnglish) {
            this.languageNameEnglish = languageNameEnglish;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }



    }





}
