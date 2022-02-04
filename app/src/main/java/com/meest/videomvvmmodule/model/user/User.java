
package com.meest.videomvvmmodule.model.user;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @Expose
    private Data data;
    @Expose
    private String message;
    @Expose
    private Boolean status;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("meestUserId")
        @Expose
        private String meestUserId;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("user_email")
        @Expose
        private String userEmail;
        @SerializedName("user_mobile_no")
        @Expose
        private String userMobileNo;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("user_profile")
        @Expose
        private String userProfile;
        @SerializedName("cover_profile")
        @Expose
        private String coverProfile;
        @SerializedName("login_type")
        @Expose
        private String loginType;
        @SerializedName("identity")
        @Expose
        private String identity;
        @SerializedName("platform")
        @Expose
        private String platform;
        @SerializedName("device_token")
        @Expose
        private String deviceToken;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("is_verify")
        @Expose
        private String isVerify;
        @SerializedName("total_received")
        @Expose
        private String totalReceived;
        @SerializedName("total_send")
        @Expose
        private String totalSend;
        @SerializedName("my_wallet")
        @Expose
        private String myWallet;
        @SerializedName("spen_in_app")
        @Expose
        private String spenInApp;
        @SerializedName("check_in")
        @Expose
        private String checkIn;
        @SerializedName("upload_video")
        @Expose
        private String uploadVideo;
        @SerializedName("from_fans")
        @Expose
        private String fromFans;
        @SerializedName("purchased")
        @Expose
        private String purchased;
        @SerializedName("bio")
        @Expose
        private String bio;
        @SerializedName("profile_category")
        @Expose
        private String profileCategory;
        @SerializedName("fb_url")
        @Expose
        private String fbUrl;
        @SerializedName("insta_url")
        @Expose
        private String instaUrl;
        @SerializedName("youtube_url")
        @Expose
        private String youtubeUrl;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("block_or_not")
        @Expose
        private String blockOrNot;
        @SerializedName("freez_or_not")
        @Expose
        private String freezOrNot;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
        @SerializedName("profile_category_name")
        @Expose
        private String profileCategoryName;
        @SerializedName("followers_count")
        @Expose
        private Integer followersCount;
        @SerializedName("following_count")
        @Expose
        private Integer followingCount;
        @SerializedName("my_post_likes")
        @Expose
        private Integer myPostLikes;
        @SerializedName("is_following")
        @Expose
        private Integer isFollowing;
        @SerializedName("total_post")
        @Expose
        private Integer totalPost;
        @SerializedName("total_likes_count")
        @Expose
        private Integer totalLikesCount;
        @SerializedName("total_view_count")
        @Expose
        private Integer totalViewCount;
        @SerializedName("total_share_count")
        @Expose
        private Integer totalShareCount;
        @SerializedName("total_comment_count")
        @Expose
        private Integer totalCommentCount;

        @Override
        public String toString() {
            return "Data{" +
                    "userId='" + userId + '\'' +
                    ", meestUserId='" + meestUserId + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", dob='" + dob + '\'' +
                    ", userName='" + userName + '\'' +
                    ", userEmail='" + userEmail + '\'' +
                    ", userMobileNo='" + userMobileNo + '\'' +
                    ", gender='" + gender + '\'' +
                    ", userProfile='" + userProfile + '\'' +
                    ", coverProfile='" + coverProfile + '\'' +
                    ", loginType='" + loginType + '\'' +
                    ", identity='" + identity + '\'' +
                    ", platform='" + platform + '\'' +
                    ", deviceToken='" + deviceToken + '\'' +
                    ", token='" + token + '\'' +
                    ", isVerify='" + isVerify + '\'' +
                    ", totalReceived='" + totalReceived + '\'' +
                    ", totalSend='" + totalSend + '\'' +
                    ", myWallet='" + myWallet + '\'' +
                    ", spenInApp='" + spenInApp + '\'' +
                    ", checkIn='" + checkIn + '\'' +
                    ", uploadVideo='" + uploadVideo + '\'' +
                    ", fromFans='" + fromFans + '\'' +
                    ", purchased='" + purchased + '\'' +
                    ", bio='" + bio + '\'' +
                    ", profileCategory='" + profileCategory + '\'' +
                    ", fbUrl='" + fbUrl + '\'' +
                    ", instaUrl='" + instaUrl + '\'' +
                    ", youtubeUrl='" + youtubeUrl + '\'' +
                    ", status='" + status + '\'' +
                    ", blockOrNot='" + blockOrNot + '\'' +
                    ", freezOrNot='" + freezOrNot + '\'' +
                    ", createdDate='" + createdDate + '\'' +
                    ", profileCategoryName='" + profileCategoryName + '\'' +
                    ", followersCount=" + followersCount +
                    ", followingCount=" + followingCount +
                    ", myPostLikes=" + myPostLikes +
                    ", isFollowing=" + isFollowing +
                    ", totalPost=" + totalPost +
                    ", totalLikesCount=" + totalLikesCount +
                    ", totalViewCount=" + totalViewCount +
                    ", totalShareCount=" + totalShareCount +
                    ", totalCommentCount=" + totalCommentCount +
                    '}';
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getMeestUserId() {
            return meestUserId;
        }

        public void setMeestUserId(String meestUserId) {
            this.meestUserId = meestUserId;
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

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getUserMobileNo() {
            if (userMobileNo.length() > 10) {
                userMobileNo = userMobileNo.substring(userMobileNo.length() - 10);
                return userMobileNo;
            }
            else {
                return userMobileNo;
            }
        }

        public void setUserMobileNo(String userMobileNo) {
            this.userMobileNo = userMobileNo;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getUserProfile() {
            return userProfile;
        }

        public void setUserProfile(String userProfile) {
            this.userProfile = userProfile;
        }

        public String getCoverProfile() {
            return coverProfile;
        }

        public void setCoverProfile(String coverProfile) {
            this.coverProfile = coverProfile;
        }

        public String getLoginType() {
            return loginType;
        }

        public void setLoginType(String loginType) {
            this.loginType = loginType;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void setIsVerify(String isVerify) {
            this.isVerify = isVerify;
        }

        public boolean isVerified() {
            return isVerify.equals("1");
        }

        public String getTotalReceived() {
            return totalReceived;
        }

        public void setTotalReceived(String totalReceived) {
            this.totalReceived = totalReceived;
        }

        public String getTotalSend() {
            return totalSend;
        }

        public void setTotalSend(String totalSend) {
            this.totalSend = totalSend;
        }

        public String getMyWallet() {
            return myWallet;
        }

        public void setMyWallet(String myWallet) {
            this.myWallet = myWallet;
        }

        public String getSpenInApp() {
            return spenInApp;
        }

        public void setSpenInApp(String spenInApp) {
            this.spenInApp = spenInApp;
        }

        public String getCheckIn() {
            return checkIn;
        }

        public void setCheckIn(String checkIn) {
            this.checkIn = checkIn;
        }

        public String getUploadVideo() {
            return uploadVideo;
        }

        public void setUploadVideo(String uploadVideo) {
            this.uploadVideo = uploadVideo;
        }

        public String getFromFans() {
            return fromFans;
        }

        public void setFromFans(String fromFans) {
            this.fromFans = fromFans;
        }

        public String getPurchased() {
            return purchased;
        }

        public void setPurchased(String purchased) {
            this.purchased = purchased;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }


        public String getFbUrl() {
            return fbUrl;
        }

        public void setFbUrl(String fbUrl) {
            this.fbUrl = fbUrl;
        }

        public String getInstaUrl() {
            return instaUrl;
        }

        public void setInstaUrl(String instaUrl) {
            this.instaUrl = instaUrl;
        }

        public String getYoutubeUrl() {
            return youtubeUrl;
        }

        public void setYoutubeUrl(String youtubeUrl) {
            this.youtubeUrl = youtubeUrl;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBlockOrNot() {
            return blockOrNot;
        }

        public void setBlockOrNot(String blockOrNot) {
            this.blockOrNot = blockOrNot;
        }

        public String getFreezOrNot() {
            return freezOrNot;
        }

        public void setFreezOrNot(String freezOrNot) {
            this.freezOrNot = freezOrNot;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }


        public Integer getFollowersCount() {
            return followersCount;
        }

        public void setFollowersCount(Integer followersCount) {
            this.followersCount = followersCount;
        }

        public Integer getFollowingCount() {
            return followingCount;
        }

        public void setFollowingCount(Integer followingCount) {
            this.followingCount = followingCount;
        }

        public String getProfileCategory() {
            return profileCategory;
        }

        public void setProfileCategory(String profileCategory) {
            this.profileCategory = profileCategory;
        }

        public String getProfileCategoryName() {
            return profileCategoryName;
        }

        public void setProfileCategoryName(String profileCategoryName) {
            this.profileCategoryName = profileCategoryName;
        }

        public Integer getMyPostLikes() {
            return myPostLikes;
        }

        public void setMyPostLikes(Integer myPostLikes) {
            this.myPostLikes = myPostLikes;
        }

        public long getIsFollowing() {
            return isFollowing;
        }

        public void setIsFollowing(int isFollowing) {
            this.isFollowing = isFollowing;
        }

        public Integer getTotalPost() {
            return totalPost;
        }

        public void setTotalPost(Integer totalPost) {
            this.totalPost = totalPost;
        }

        public Integer getTotalLikesCount() {
            return totalLikesCount;
        }

        public void setTotalLikesCount(Integer totalLikesCount) {
            this.totalLikesCount = totalLikesCount;
        }

        public Integer getTotalViewCount() {
            return totalViewCount;
        }

        public void setTotalViewCount(Integer totalViewCount) {
            this.totalViewCount = totalViewCount;
        }

        public Integer getTotalShareCount() {
            return totalShareCount;
        }

        public void setTotalShareCount(Integer totalShareCount) {
            this.totalShareCount = totalShareCount;
        }

        public Integer getTotalCommentCount() {
            return totalCommentCount;
        }

        public void setTotalCommentCount(Integer totalCommentCount) {
            this.totalCommentCount = totalCommentCount;
        }
    }
}
