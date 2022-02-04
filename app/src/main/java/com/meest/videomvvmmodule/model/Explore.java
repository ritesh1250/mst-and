
package com.meest.videomvvmmodule.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Explore {

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

        @SerializedName("hash_tag_name")
        private String hashTagName;
        @SerializedName("hash_tag_profile")
        private String hashTagProfile;
        @SerializedName("hash_tag_videos_count")
        private String hashTagVideosCount;
        @SerializedName("user_id")
        private String user_id;
        @SerializedName("first_name")
        private String first_name;
        @SerializedName("user_name")
        private String user_name;
        @SerializedName("user_profile")
        private String user_profile;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_profile() {
            return user_profile;
        }

        public void setUser_profile(String user_profile) {
            this.user_profile = user_profile;
        }

        public String getHashTagName() {
            return hashTagName;
        }

        public void setHashTagName(String hashTagName) {
            this.hashTagName = hashTagName;
        }

        public String getHashTagProfile() {
            return hashTagProfile;
        }

        public void setHashTagProfile(String hashTagProfile) {
            this.hashTagProfile = hashTagProfile;
        }

        public String getHashTagVideosCount() {
            return hashTagVideosCount;
        }

        public void setHashTagVideosCount(String hashTagVideosCount) {
            this.hashTagVideosCount = hashTagVideosCount;
        }
    }
}
