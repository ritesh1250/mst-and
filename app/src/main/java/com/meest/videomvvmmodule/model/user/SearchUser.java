
package com.meest.videomvvmmodule.model.user;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class SearchUser {

    @Expose
    private List<User> data;
    @Expose
    private String message;
    @Expose
    private Boolean status;

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
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

    public static class User {

        @SerializedName("full_name")
        private String fullName;
        @SerializedName("first_name")
        private String first_name;
        @SerializedName("last_name")
        private String last_name;
        @SerializedName("user_email")
        private String userEmail;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("user_mobile_no")
        private String userMobileNo;
        @SerializedName("user_name")
        private String userName;
        @SerializedName("user_profile")
        private String userProfile;
        @SerializedName("is_verify")
        private String isVerify;
        @SerializedName("my_post_count")
        private int userPostCount;
        @SerializedName("followers_count")
        private int userFollowerCount;
        @SerializedName("friend_or_not")
        private String friend_or_not;
        @SerializedName("is_follower")
        private String is_follower;
        @SerializedName("is_following")
        private String is_following;
        @SerializedName("my_post_likes")
        private String my_post_likes;

        public String getMy_post_likes() {
            return my_post_likes;
        }

        public void setMy_post_likes(String my_post_likes) {
            this.my_post_likes = my_post_likes;
        }

        public boolean isfollower() {
            return is_follower.equals("0");
        }

        public void setIs_follower(String is_follower) {
            this.is_follower = is_follower;
        }

        public boolean isfollowing() {
            return is_following.equals("0");
        }

        public void setIs_following(String is_following) {
            this.is_following = is_following;
        }

        public String getFriend_or_not() {
            return friend_or_not;
        }

        public void setFriend_or_not(String friend_or_not) {
            this.friend_or_not = friend_or_not;
        }

        public boolean getIsVerify() {
            return isVerify.equals("1");
        }

        public void setIsVerify(String isVerify) {
            this.isVerify = isVerify;
        }

        public int getUserPostCount() {
            return userPostCount;
        }

        public void setUserPostCount(int userPostCount) {
            this.userPostCount = userPostCount;
        }

        public int getUserFollowerCount() {
            return userFollowerCount;
        }

        public void setUserFollowerCount(int userFollowerCount) {
            this.userFollowerCount = userFollowerCount;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserMobileNo() {
            return userMobileNo;
        }

        public void setUserMobileNo(String userMobileNo) {
            this.userMobileNo = userMobileNo;
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
