package com.meest.social.socialViewModel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlockUserResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("errorMessage")
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

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
        @SerializedName("isDeleted")
        @Expose
        private Boolean isDeleted;
        @SerializedName("blockedTo")
        @Expose
        private String blockedTo;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Boolean getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
        }

        public String getBlockedTo() {
            return blockedTo;
        }

        public void setBlockedTo(String blockedTo) {
            this.blockedTo = blockedTo;
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
