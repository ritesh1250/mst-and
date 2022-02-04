
package com.meest.videomvvmmodule.model.follower;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Follower {

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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public static class Data {

        @SerializedName("created_date")
        private String createdDate;
        @SerializedName("follower_id")
        private String followerId;
        @SerializedName("followers_count")
        private Long followersCount;
        @SerializedName("following_count")
        private Long followingCount;
        @SerializedName("from_user_id")
        private String fromUserId;
        @SerializedName("full_name")
        private String fullName;
        @SerializedName("first_name")
        private String firstName;
        @SerializedName("last_name")
        private String lastName;
        @SerializedName("is_verify")
        private String isVerify;
        @SerializedName("my_post_count")
        private Long myPostCount;
        @SerializedName("my_post_likes")
        private Long myPostLikes;
        @SerializedName("to_user_id")
        private String toUserId;
        @SerializedName("user_name")
        private String userName;
        @SerializedName("user_profile")
        private String userProfile;
        @SerializedName("is_follower")
        private String is_follower;
        @SerializedName("is_following")
        private String is_following;
        @SerializedName("button_visible_or_not")
        private String button_visible_or_not;
        @SerializedName("friend_status")
        private String friend_status;
        @SerializedName("button_show")
        private String button_show;
//follow
        public String getButton_show() {
            return button_show;
        }

        public void setButton_show(String button_show) {
            this.button_show = button_show;
        }

        public String getFriend_status() {
            return friend_status;
        }

        public void setFriend_status(String friend_status) {
            this.friend_status = friend_status;
        }

        public String getButton_visible_or_not() {
            return button_visible_or_not;
        }

        public void setButton_visible_or_not(String button_visible_or_not) {
            this.button_visible_or_not = button_visible_or_not;
        }

        public boolean isFollower() {
            return is_follower.equals("1");
        }

        public void setIs_follower(String is_follower) {
            this.is_follower = is_follower;
        }

        public boolean isFollowing() {
            return is_following.equals("1");
        }

        public void setIs_following(String is_following) {
            this.is_following = is_following;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getFollowerId() {
            return followerId;
        }

        public void setFollowerId(String followerId) {
            this.followerId = followerId;
        }

        public Long getFollowersCount() {
            return followersCount;
        }

        public void setFollowersCount(Long followersCount) {
            this.followersCount = followersCount;
        }

        public Long getFollowingCount() {
            return followingCount;
        }

        public void setFollowingCount(Long followingCount) {
            this.followingCount = followingCount;
        }

        public String getFromUserId() {
            return fromUserId;
        }

        public void setFromUserId(String fromUserId) {
            this.fromUserId = fromUserId;
        }

        public boolean getIsVerify() {
            return isVerify.equals("1");
        }

        public void setIsVerify(String isVerify) {
            this.isVerify = isVerify;
        }

        public Long getMyPostCount() {
            return myPostCount;
        }

        public void setMyPostCount(Long myPostCount) {
            this.myPostCount = myPostCount;
        }

        public Long getMyPostLikes() {
            return myPostLikes;
        }

        public void setMyPostLikes(Long myPostLikes) {
            this.myPostLikes = myPostLikes;
        }

        public String getToUserId() {
            return toUserId;
        }

        public void setToUserId(String toUserId) {
            this.toUserId = toUserId;
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
    }
}
