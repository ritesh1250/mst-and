package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsertPostLikeResponse {

    public class Data {

        @SerializedName("likeCount")
        @Expose
        private Integer likeCount;
        @SerializedName("dislikeCount")
        @Expose
        private Integer dislikeCount;
        @SerializedName("lastLikedBy")
        @Expose
        private String recentUsername;

        public Integer getLikeCount() {
            return likeCount;
        }

        public void setLikeCount(Integer likeCount) {
            this.likeCount = likeCount;
        }

        public Integer getDislikeCount() {
            return dislikeCount;
        }

        public void setDislikeCount(Integer dislikeCount) {
            this.dislikeCount = dislikeCount;
        }

        public String getRecentUsername() {
            return recentUsername == null ? "" : recentUsername;
        }

        public void setRecentUsername(String recentUsername) {
            this.recentUsername = recentUsername;
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




}
