
package com.meest.videomvvmmodule.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileCategory {

    @Expose
    private List<Data> data;
    @Expose
    private String message;
    @Expose
    private Boolean status;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public static class Data {

        @SerializedName("profile_category_id")
        private String profileCategoryId;
        @SerializedName("profile_category_image")
        private String profileCategoryImage;
        @SerializedName("profile_category_name")
        private String profileCategoryName;

        public String getProfileCategoryId() {
            return profileCategoryId;
        }

        public void setProfileCategoryId(String profileCategoryId) {
            this.profileCategoryId = profileCategoryId;
        }

        public String getProfileCategoryImage() {
            return profileCategoryImage;
        }

        public void setProfileCategoryImage(String profileCategoryImage) {
            this.profileCategoryImage = profileCategoryImage;
        }

        public String getProfileCategoryName() {
            return profileCategoryName;
        }

        public void setProfileCategoryName(String profileCategoryName) {
            this.profileCategoryName = profileCategoryName;
        }

    }
}
