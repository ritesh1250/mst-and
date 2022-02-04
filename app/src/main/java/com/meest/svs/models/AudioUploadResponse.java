package com.meest.svs.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AudioUploadResponse {

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

        @SerializedName("audioURL")
        @Expose
        private String audioURL;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("approvalStatus")
        @Expose
        private String approvalStatus;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;

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

        public String getApprovalStatus() {
            return approvalStatus;
        }

        public void setApprovalStatus(String approvalStatus) {
            this.approvalStatus = approvalStatus;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }
    }
}
