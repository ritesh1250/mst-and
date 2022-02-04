package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AudioResponse {

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

    public class Hashtags {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("data")
        @Expose
        private List<Integer> data = null;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Integer> getData() {
            return data;
        }

        public void setData(List<Integer> data) {
            this.data = data;
        }

    }

    public class Row {

        @SerializedName("audioURL")
        @Expose
        private String audioURL;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("catagoryId")
        @Expose
        private String catagoryId;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("approvalStatus")
        @Expose
        private String approvalStatus;
        @SerializedName("hashtags")
        @Expose
        private List<String> hashtags = null;
        @SerializedName("status")
        @Expose
        private Boolean status;



        public String getAudioURL() {
            return audioURL;
        }

        public void setAudioURL(String audioURL) {
            this.audioURL = audioURL;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCatagoryId() {
            return catagoryId;
        }

        public void setCatagoryId(String catagoryId) {
            this.catagoryId = catagoryId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getApprovalStatus() {
            return approvalStatus;
        }

        public void setApprovalStatus(String approvalStatus) {
            this.approvalStatus = approvalStatus;
        }

        public List<String> getHashtags() {
            return hashtags;
        }

        public void setHashtags(List<String> hashtags) {
            this.hashtags = hashtags;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }



    }

}
