package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SinglePostInsightResponse {
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


    public class Post {

        @SerializedName("post")
        @Expose
        private String post;
        @SerializedName("video")
        @Expose
        private Integer video;

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public Integer getVideo() {
            return video;
        }

        public void setVideo(Integer video) {
            this.video = video;
        }

    }

    public class Data {

        @SerializedName("caption")
        @Expose
        private String caption;
        @SerializedName("hashTags")
        @Expose
        private List<Object> hashTags = null;
        @SerializedName("topicId")
        @Expose
        private String topicId;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("posts")
        @Expose
        private List<Post> posts = null;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("shareCount")
        @Expose
        private Integer shareCount;
        @SerializedName("commentCounts")
        @Expose
        private Integer commentCounts;
        @SerializedName("postSaved")
        @Expose
        private Integer postSaved;
        @SerializedName("liked")
        @Expose
        private Integer liked;
        @SerializedName("disliked")
        @Expose
        private Integer disliked;
        @SerializedName("likeCounts")
        @Expose
        private Integer likeCounts;
        @SerializedName("dislikeCounts")
        @Expose
        private Integer dislikeCounts;
        @SerializedName("postSaveCounts")
        @Expose
        private Integer postSaveCounts;
        @SerializedName("peopleReached")
        @Expose
        private Integer peopleReached;
        @SerializedName("postView")
        @Expose
        private Integer postView;
        @SerializedName("totalUsers")
        @Expose
        private Integer totalUsers;
        @SerializedName("followersCount")
        @Expose
        private Integer followersCount;
        @SerializedName("followrPercent")
        @Expose
        private String followrPercent;
        @SerializedName("user")
        @Expose
        private User user;

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public List<Object> getHashTags() {
            return hashTags;
        }

        public void setHashTags(List<Object> hashTags) {
            this.hashTags = hashTags;
        }

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Post> getPosts() {
            return posts;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Integer getShareCount() {
            return shareCount;
        }

        public void setShareCount(Integer shareCount) {
            this.shareCount = shareCount;
        }

        public Integer getCommentCounts() {
            return commentCounts;
        }

        public void setCommentCounts(Integer commentCounts) {
            this.commentCounts = commentCounts;
        }

        public Integer getPostSaved() {
            return postSaved;
        }

        public void setPostSaved(Integer postSaved) {
            this.postSaved = postSaved;
        }

        public Integer getLiked() {
            return liked;
        }

        public void setLiked(Integer liked) {
            this.liked = liked;
        }

        public Integer getDisliked() {
            return disliked;
        }

        public void setDisliked(Integer disliked) {
            this.disliked = disliked;
        }

        public Integer getLikeCounts() {
            return likeCounts;
        }

        public void setLikeCounts(Integer likeCounts) {
            this.likeCounts = likeCounts;
        }

        public Integer getDislikeCounts() {
            return dislikeCounts;
        }

        public void setDislikeCounts(Integer dislikeCounts) {
            this.dislikeCounts = dislikeCounts;
        }

        public Integer getPostSaveCounts() {
            return postSaveCounts;
        }

        public void setPostSaveCounts(Integer postSaveCounts) {
            this.postSaveCounts = postSaveCounts;
        }

        public Integer getPeopleReached() {
            return peopleReached;
        }

        public void setPeopleReached(Integer peopleReached) {
            this.peopleReached = peopleReached;
        }

        public Integer getPostView() {
            return postView;
        }

        public void setPostView(Integer postView) {
            this.postView = postView;
        }

        public Integer getTotalUsers() {
            return totalUsers;
        }

        public void setTotalUsers(Integer totalUsers) {
            this.totalUsers = totalUsers;
        }

        public Integer getFollowersCount() {
            return followersCount;
        }

        public void setFollowersCount(Integer followersCount) {
            this.followersCount = followersCount;
        }

        public String getFollowrPercent() {
            return followrPercent;
        }

        public void setFollowrPercent(String followrPercent) {
            this.followrPercent = followrPercent;
        }

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
        @SerializedName("gToken")
        @Expose
        private String gToken;
        @SerializedName("fcmToken")
        @Expose
        private String fcmToken;
        @SerializedName("deviceVoipToken")
        @Expose
        private String deviceVoipToken;
        @SerializedName("fbToken")
        @Expose
        private String fbToken;
        @SerializedName("otp")
        @Expose
        private String otp;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lag")
        @Expose
        private String lag;
        @SerializedName("otpExpires")
        @Expose
        private String otpExpires;
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
        @SerializedName("adharFront")
        @Expose
        private String adharFront;
        @SerializedName("adharBack")
        @Expose
        private String adharBack;
        @SerializedName("adharNumber")
        @Expose
        private String adharNumber;
        @SerializedName("isAdharVerified")
        @Expose
        private String isAdharVerified;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("referral")
        @Expose
        private String referral;
        @SerializedName("friendReferral")
        @Expose
        private String friendReferral;
        @SerializedName("bio")
        @Expose
        private String bio;
        @SerializedName("passwordChangedOn")
        @Expose
        private String passwordChangedOn;
        @SerializedName("deletedAt")
        @Expose
        private String deletedAt;
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
        private long mobile;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("notification")
        @Expose
        private Boolean notification;
        @SerializedName("mediaAutoDownload")
        @Expose
        private Boolean mediaAutoDownload;
        @SerializedName("isReal")
        @Expose
        private Boolean isReal;
        @SerializedName("dnd")
        @Expose
        private Boolean dnd;
        @SerializedName("isDeleted")
        @Expose
        private Boolean isDeleted;
        @SerializedName("accountType")
        @Expose
        private String accountType;
        @SerializedName("likes")
        @Expose
        private Integer likes;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

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

        public String getGToken() {
            return gToken;
        }

        public void setGToken(String gToken) {
            this.gToken = gToken;
        }

        public String getFcmToken() {
            return fcmToken;
        }

        public void setFcmToken(String fcmToken) {
            this.fcmToken = fcmToken;
        }

        public String getDeviceVoipToken() {
            return deviceVoipToken;
        }

        public void setDeviceVoipToken(String deviceVoipToken) {
            this.deviceVoipToken = deviceVoipToken;
        }

        public String getFbToken() {
            return fbToken;
        }

        public void setFbToken(String fbToken) {
            this.fbToken = fbToken;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLag() {
            return lag;
        }

        public void setLag(String lag) {
            this.lag = lag;
        }

        public String getOtpExpires() {
            return otpExpires;
        }

        public void setOtpExpires(String otpExpires) {
            this.otpExpires = otpExpires;
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

        public String getAdharFront() {
            return adharFront;
        }

        public void setAdharFront(String adharFront) {
            this.adharFront = adharFront;
        }

        public String getAdharBack() {
            return adharBack;
        }

        public void setAdharBack(String adharBack) {
            this.adharBack = adharBack;
        }

        public String getAdharNumber() {
            return adharNumber;
        }

        public void setAdharNumber(String adharNumber) {
            this.adharNumber = adharNumber;
        }

        public String getIsAdharVerified() {
            return isAdharVerified;
        }

        public void setIsAdharVerified(String isAdharVerified) {
            this.isAdharVerified = isAdharVerified;
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

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getPasswordChangedOn() {
            return passwordChangedOn;
        }

        public void setPasswordChangedOn(String passwordChangedOn) {
            this.passwordChangedOn = passwordChangedOn;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
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

        public long getMobile() {
            return mobile;
        }

        public void setMobile(long mobile) {
            this.mobile = mobile;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
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

        public Boolean getIsReal() {
            return isReal;
        }

        public void setIsReal(Boolean isReal) {
            this.isReal = isReal;
        }

        public Boolean getDnd() {
            return dnd;
        }

        public void setDnd(Boolean dnd) {
            this.dnd = dnd;
        }

        public Boolean getIsDeleted() {
            return isDeleted;
        }

        public void setIsDeleted(Boolean isDeleted) {
            this.isDeleted = isDeleted;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
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

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

}
