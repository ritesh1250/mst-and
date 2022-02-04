package com.meest.social.socialViewModel.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class TopResponse {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("code")
    private Integer code;
    @SerializedName("count")
    private Integer count;
    @SerializedName("data")
    private List<TopResponse.Datum> data = null;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<TopResponse.Datum> getData() {
        return data;
    }

    public void setData(List<TopResponse.Datum> data) {
        this.data = data;
    }
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public class Datum {
        @SerializedName("id")
        private String id;
        @SerializedName("userId")
        private String userId;
        @SerializedName("thumbnail")
        private String thumbnail;
        @SerializedName("topCount")
        private Integer count;

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

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}