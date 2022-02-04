package com.meest.videomvvmmodule.model.follower;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FollowerResponse {
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

        @SerializedName("follower")
        @Expose
        private List<FollowData> follower = null;
        @SerializedName("following")
        @Expose
        private List<FollowData> following = null;

        public List<FollowData> getFollower() {
            return follower;
        }

        public void setFollower(List<FollowData> follower) {
            this.follower = follower;
        }

        public List<FollowData> getFollowing() {
            return following;
        }

        public void setFollowing(List<FollowData> following) {
            this.following = following;
        }
    }
    public class FollowData {
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
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
        @SerializedName("isFriend")
        @Expose
        private String isFriend;
        @SerializedName("isFollower")
        @Expose
        private String isFollower;
        @SerializedName("isFollowing")
        @Expose
        private String isFollowing;

        public boolean isFollower() {
            return isFollower.equals("1");
        }

        public void setIsFollower(String isFollower) {
            this.isFollower = isFollower;
        }

        public boolean isFollowing() {
            return isFollowing.equals("1");
        }

        public void setIsFollowing(String isFollowing) {
            this.isFollowing = isFollowing;
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


        public boolean getIsFriend() {
            return isFriend.equals("1");
        }

        public void setIsFriend(String isFriend) {
            this.isFriend = isFriend;
        }

    }
}
