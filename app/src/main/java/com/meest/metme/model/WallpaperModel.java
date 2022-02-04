package com.meest.metme.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WallpaperModel {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("data")
    @Expose
    private Data[] data;

    @SerializedName("success")
    @Expose
    private String success;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Data[] getData() {
        return data;
    }

    public void setData(Data[] data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


    public class Data {
        @SerializedName("image")
        @Expose
        private String image;

        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        @SerializedName("deletedAt")
        @Expose
        private String deletedAt;

        @SerializedName("isDeleted")
        @Expose
        private String isDeleted;

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("title")
        @Expose
        private String title;

        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;


        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
        }

        public String getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(String isDeleted) {
            this.isDeleted = isDeleted;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
