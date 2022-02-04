package com.meest.svs.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SVSProfileModel {

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

        @SerializedName("user")
        @Expose
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }

    public class User {

        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("isOnline")
        @Expose
        private Boolean isOnline;
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
        private Long mobile;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("accountType")
        @Expose
        private String accountType;
        @SerializedName("notification")
        @Expose
        private Boolean notification;
        @SerializedName("mediaAutoDownload")
        @Expose
        private Boolean mediaAutoDownload;
        @SerializedName("dnd")
        @Expose
        private Boolean dnd;
        @SerializedName("likes")
        @Expose
        private Integer likes;
        @SerializedName("totalFollowers")
        @Expose
        private Integer totalFollowers;
        @SerializedName("totalFollowings")
        @Expose
        private Integer totalFollowings;
        @SerializedName("totalPosts")
        @Expose
        private Integer totalPosts;
        @SerializedName("friendStatus")
        @Expose
        private String friendStatus;
        @SerializedName("isFriend")
        @Expose
        private Integer isFriend = 0;

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

        public Boolean getIsOnline() {
            return isOnline;
        }

        public void setIsOnline(Boolean isOnline) {
            this.isOnline = isOnline;
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

        public Long getMobile() {
            return mobile;
        }

        public void setMobile(Long mobile) {
            this.mobile = mobile;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
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

        public Boolean getNotification() {
            return notification;
        }

        public void setNotification(Boolean notification) {
            this.notification = notification;
        }

        public Boolean getMediaAutoDownload() {
            return mediaAutoDownload;
        }

        public void setMediaAutoDownload(Boolean mediaAutoDownload) {
            this.mediaAutoDownload = mediaAutoDownload;
        }

        public Boolean getDnd() {
            return dnd;
        }

        public void setDnd(Boolean dnd) {
            this.dnd = dnd;
        }

        public Integer getLikes() {
            return likes;
        }

        public void setLikes(Integer likes) {
            this.likes = likes;
        }

        public Integer getTotalFollowers() {
            return totalFollowers;
        }

        public void setTotalFollowers(Integer totalFollowers) {
            this.totalFollowers = totalFollowers;
        }

        public Integer getTotalFollowings() {
            return totalFollowings;
        }

        public void setTotalFollowings(Integer totalFollowings) {
            this.totalFollowings = totalFollowings;
        }

        public Integer getTotalPosts() {
            return totalPosts;
        }

        public void setTotalPosts(Integer totalPosts) {
            this.totalPosts = totalPosts;
        }

        public String getFriendStatus() {
            return friendStatus;
        }

        public void setFriendStatus(String friendStatus) {
            this.friendStatus = friendStatus;
        }

        public Integer getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(Integer isFriend) {
            this.isFriend = isFriend;
        }
    }
}
