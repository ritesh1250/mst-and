package com.meest.videomvvmmodule.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopCreatorResponse {

    @Expose
    private String message;
    @Expose
    private Boolean status;
    @Expose
    private List<User> data;

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

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }

    public static class User {

        @SerializedName("user_id")
        @Expose
        private String userId;

        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;

        @SerializedName("user_name")
        @Expose
        private String userName;

        @SerializedName("user_profile")
        @Expose
        private String userProfile;

        @SerializedName("show_button")
        @Expose
        private String showButton;

        public String getShowButton() {
            return showButton;
        }

        public void setShowButton(String showButton) {
            this.showButton = showButton;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserProfile() {
            return userProfile;
        }

        public void setUserProfile(String userProfile) {
            this.userProfile = userProfile;
        }
    }

}
