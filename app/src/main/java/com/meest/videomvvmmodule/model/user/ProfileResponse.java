package com.meest.videomvvmmodule.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("success")
    @Expose
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
        @SerializedName("user")
        @Expose
        private UserData user;

        public UserData getUserData() {
            return user;
        }

        public void setUserData(UserData user) {
            this.user = user;
        }
    }

    public class UserData {
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("isOnline")
        @Expose
        private boolean isOnline;
        @SerializedName("lastLoggedIn")
        @Expose
        private String lastLoggedIn;
        @SerializedName("ip")
        @Expose
        private String ip;
        @SerializedName("ios")
        @Expose
        private String ios;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("referral")
        @Expose
        private String referral;
        @SerializedName("friendReferral")
        @Expose
        private String friendReferral;
        @SerializedName("id")
        @Expose
        private String id;
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
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("status")
        @Expose
        private boolean status;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("accountType")
        @Expose
        private String accountType;
        @SerializedName("notification")
        @Expose
        private boolean notification;
        @SerializedName("mediaAutoDownload")
        @Expose
        private boolean mediaAutoDownload;
        @SerializedName("dnd")
        @Expose
        private boolean dnd;
        @SerializedName("likes")
        @Expose
        private int likes;
        @SerializedName("totalFollowers")
        @Expose
        private int totalFollowers;
        @SerializedName("totalFollowings")
        @Expose
        private int totalFollowings;
        @SerializedName("totalPosts")
        @Expose
        private int totalPosts;
        @SerializedName("friendStatus")
        @Expose
        private String friendStatus;
        @SerializedName("isFriend")
        @Expose
        private int isFriend = 0;

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getDisplayPicture() {
            return displayPicture;
        }

        public void setDisplayPicture(String displayPicture) {
            this.displayPicture = displayPicture;
        }

        public boolean getOnline() {
            return isOnline;
        }

        public void setOnline(boolean online) {
            isOnline = online;
        }

        public String getLastLoggedIn() {
            return lastLoggedIn;
        }

        public void setLastLoggedIn(String lastLoggedIn) {
            this.lastLoggedIn = lastLoggedIn;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getIos() {
            return ios;
        }

        public void setIos(String ios) {
            this.ios = ios;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getReferral() {
            return referral;
        }

        public void setReferral(String referral) {
            this.referral = referral;
        }

        public String getFriendReferral() {
            return friendReferral;
        }

        public void setFriendReferral(String friendReferral) {
            this.friendReferral = friendReferral;
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

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public boolean getNotification() {
            return notification;
        }

        public void setNotification(boolean notification) {
            this.notification = notification;
        }

        public boolean getMediaAutoDownload() {
            return mediaAutoDownload;
        }

        public void setMediaAutoDownload(boolean mediaAutoDownload) {
            this.mediaAutoDownload = mediaAutoDownload;
        }

        public boolean getDnd() {
            return dnd;
        }

        public void setDnd(boolean dnd) {
            this.dnd = dnd;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public int getTotalFollowers() {
            return totalFollowers;
        }

        public void setTotalFollowers(int totalFollowers) {
            this.totalFollowers = totalFollowers;
        }

        public int getTotalFollowings() {
            return totalFollowings;
        }

        public void setTotalFollowings(int totalFollowings) {
            this.totalFollowings = totalFollowings;
        }

        public int getTotalPosts() {
            return totalPosts;
        }

        public void setTotalPosts(int totalPosts) {
            this.totalPosts = totalPosts;
        }

        public String getFriendStatus() {
            return friendStatus;
        }

        public void setFriendStatus(String friendStatus) {
            this.friendStatus = friendStatus;
        }

        public int getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(int isFriend) {
            this.isFriend = isFriend;
        }
    }
}
