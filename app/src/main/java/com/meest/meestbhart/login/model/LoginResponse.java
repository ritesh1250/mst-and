package com.meest.meestbhart.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    public class Data {

        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("token")
        @Expose
        private String token;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
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

    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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

    public class User {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("gToken")
        @Expose
        private Object gToken;
        @SerializedName("fbToken")
        @Expose
        private Object fbToken;
        @SerializedName("likes")
        @Expose
        private Integer likes;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("defaultApp")
        @Expose
        private String defaultApp;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getDefaultApp() {
            return defaultApp;
        }

        public void setDefaultApp(String defaultApp) {
            this.defaultApp = defaultApp;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDisplayPicture() {
            return displayPicture;
        }

        public void setDisplayPicture(String displayPicture) {
            this.displayPicture = displayPicture;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Object getGToken() {
            return gToken;
        }

        public void setGToken(Object gToken) {
            this.gToken = gToken;
        }

        public Object getFbToken() {
            return fbToken;
        }

        public void setFbToken(Object fbToken) {
            this.fbToken = fbToken;
        }

        public Integer getLikes() {
            return likes;
        }

        public void setLikes(Integer likes) {
            this.likes = likes;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }


        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }
    }



}
