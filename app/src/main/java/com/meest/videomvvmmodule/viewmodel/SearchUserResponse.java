package com.meest.videomvvmmodule.viewmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchUserResponse {
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("data")
    @Expose
    private List<Data> data;
    @SerializedName("success")
    @Expose
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class Data {
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("accountType")
        @Expose
        private String accountType;
        @SerializedName("isFriend")
        @Expose
        private int isFriend = 0;
        @SerializedName("friendStatus")
        @Expose
        private String friendStatus;
        @SerializedName("isFollower")
        private int isFollower;
        @SerializedName("isFollowing")
        private int isFollowing;
        @SerializedName("isAccepted")
        private int isAccepted;

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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public int getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(int isFriend) {
            this.isFriend = isFriend;
        }

        public String getFriendStatus() {
            return friendStatus;
        }

        public void setFriendStatus(String friendStatus) {
            this.friendStatus = friendStatus;
        }

        public int getIsFollower() {
            return isFollower;
        }

        public void setIsFollower(int isFollower) {
            this.isFollower = isFollower;
        }

        public int getIsFollowing() {
            return isFollowing;
        }

        public void setIsFollowing(int isFollowing) {
            this.isFollowing = isFollowing;
        }

        public int getIsAccepted() {
            return isAccepted;
        }

        public void setIsAccepted(int isAccepted) {
            this.isAccepted = isAccepted;
        }
    }
}
