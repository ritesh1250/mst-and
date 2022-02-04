package com.meest.videomvvmmodule.model.like;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddLikeResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("success")
    @Expose
    private boolean success;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public class Data {

        @SerializedName("likeCount")
        @Expose
        private Integer likeCount;
        @SerializedName("dislikeCount")
        @Expose
        private Integer dislikeCount;

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
    }
}
