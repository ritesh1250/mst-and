package com.meest.responses;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowAdvtResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
    public class OtherImageVideo {

        @SerializedName("fileUrl")
        @Expose
        private String fileUrl;
        @SerializedName("heading")
        @Expose
        private String heading;
        @SerializedName("fileType")
        @Expose
        private String fileType;
        @SerializedName("subHeading")
        @Expose
        private String subHeading;
        @SerializedName("websiteUrl")
        @Expose
        private String websiteUrl;

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public String getHeading() {
            return heading;
        }

        public void setHeading(String heading) {
            this.heading = heading;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getSubHeading() {
            return subHeading;
        }

        public void setSubHeading(String subHeading) {
            this.subHeading = subHeading;
        }

        public String getWebsiteUrl() {
            return websiteUrl;
        }

        public void setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
        }

    }

    public class Post {

        @SerializedName("post")
        @Expose
        private String post;
        @SerializedName("image")
        @Expose
        private Integer image;
        @SerializedName("video")
        @Expose
        private Integer video;
        @SerializedName("isText")
        @Expose
        private Integer isText;
        @SerializedName("textColorCode")
        @Expose
        private String textColorCode;

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public Integer getImage() {
            return image;
        }

        public void setImage(Integer image) {
            this.image = image;
        }

        public Integer getVideo() {
            return video;
        }

        public void setVideo(Integer video) {
            this.video = video;
        }

        public Integer getIsText() {
            return isText;
        }

        public void setIsText(Integer isText) {
            this.isText = isText;
        }

        public String getTextColorCode() {
            return textColorCode;
        }

        public void setTextColorCode(String textColorCode) {
            this.textColorCode = textColorCode;
        }

    }

    public class PostComment {

        @SerializedName("subCommentId")
        @Expose
        private String subCommentId;
        @SerializedName("deletedAt")
        @Expose
        private String deletedAt;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("postId")
        @Expose
        private String postId;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("user")
        @Expose
        private User_ user;
        @SerializedName("commentLikes")
        @Expose
        private List<Object> commentLikes = null;
        @SerializedName("subCommentData")
        @Expose
        private List<SubCommentDatum> subCommentData = null;

        public String getSubCommentId() {
            return subCommentId;
        }

        public void setSubCommentId(String subCommentId) {
            this.subCommentId = subCommentId;
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

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public User_ getUser() {
            return user;
        }

        public void setUser(User_ user) {
            this.user = user;
        }

        public List<Object> getCommentLikes() {
            return commentLikes;
        }

        public void setCommentLikes(List<Object> commentLikes) {
            this.commentLikes = commentLikes;
        }

        public List<SubCommentDatum> getSubCommentData() {
            return subCommentData;
        }

        public void setSubCommentData(List<SubCommentDatum> subCommentData) {
            this.subCommentData = subCommentData;
        }

    }

    public class PostLike {

        @SerializedName("deletedAt")
        @Expose
        private String deletedAt;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("postId")
        @Expose
        private String postId;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("disliked")
        @Expose
        private Boolean disliked;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("user")
        @Expose
        private User user;

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

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Boolean getDisliked() {
            return disliked;
        }

        public void setDisliked(Boolean disliked) {
            this.disliked = disliked;
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

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }

    public class SubCommentDatum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("postId")
        @Expose
        private String postId;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("deletedAt")
        @Expose
        private Object deletedAt;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("ParentCommentId")
        @Expose
        private String parentCommentId;
        @SerializedName("user")
        @Expose
        private User__ user;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
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

        public String getParentCommentId() {
            return parentCommentId;
        }

        public void setParentCommentId(String parentCommentId) {
            this.parentCommentId = parentCommentId;
        }

        public User__ getUser() {
            return user;
        }

        public void setUser(User__ user) {
            this.user = user;
        }

    }

    public class User {

        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("username")
        @Expose
        private String username;

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

    }
    public class User_ {

        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("username")
        @Expose
        private String username;

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

    }

    public class User__ {

        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("username")
        @Expose
        private String username;

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

    }

    public class User___ {

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
        private String mobile;
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

    public class Datum {

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
        @SerializedName("viewPost")
        @Expose
        private String viewPost;
        @SerializedName("allowComment")
        @Expose
        private Boolean allowComment;
        @SerializedName("tags")
        @Expose
        private List<Object> tags = null;
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
        @SerializedName("idArchiveUser")
        @Expose
        private Integer idArchiveUser;
        @SerializedName("postLikes")
        @Expose
        private List<PostLike> postLikes = null;
        @SerializedName("postComments")
        @Expose
        private List<PostComment> postComments = null;
        @SerializedName("user")
        @Expose
        private User___ user;
        @SerializedName("fileURL")
        @Expose
        private String fileURL;
        @SerializedName("campaignTitle")
        @Expose
        private String campaignTitle;
        @SerializedName("campaignText")
        @Expose
        private String campaignText;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobileNumber")
        @Expose
        private String mobileNumber;
        @SerializedName("websiteUrl")
        @Expose
        private String websiteUrl;
        @SerializedName("gender")
        @Expose
        private List<String> gender = null;
        @SerializedName("startAge")
        @Expose
        private String startAge;
        @SerializedName("endAge")
        @Expose
        private String endAge;
        @SerializedName("campaignType")
        @Expose
        private String campaignType;
        @SerializedName("otherImageVideos")
        @Expose
        private List<OtherImageVideo> otherImageVideos = null;
        @SerializedName("startDate")
        @Expose
        private String startDate;
        @SerializedName("endDate")
        @Expose
        private String endDate;
        @SerializedName("isAdvertise")
        @Expose
        private Boolean isAdvertise;
        @SerializedName("signedUp")
        @Expose
        private Integer signedUp;
        @SerializedName("campaignLikes")
        @Expose
        private List<Object> campaignLikes = null;
        @SerializedName("campaignComments")
        @Expose
        private List<Object> campaignComments = null;

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

        public String getViewPost() {
            return viewPost;
        }

        public void setViewPost(String viewPost) {
            this.viewPost = viewPost;
        }

        public Boolean getAllowComment() {
            return allowComment;
        }

        public void setAllowComment(Boolean allowComment) {
            this.allowComment = allowComment;
        }

        public List<Object> getTags() {
            return tags;
        }

        public void setTags(List<Object> tags) {
            this.tags = tags;
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

        public Integer getIdArchiveUser() {
            return idArchiveUser;
        }

        public void setIdArchiveUser(Integer idArchiveUser) {
            this.idArchiveUser = idArchiveUser;
        }

        public List<PostLike> getPostLikes() {
            return postLikes;
        }

        public void setPostLikes(List<PostLike> postLikes) {
            this.postLikes = postLikes;
        }

        public List<PostComment> getPostComments() {
            return postComments;
        }

        public void setPostComments(List<PostComment> postComments) {
            this.postComments = postComments;
        }

        public User___ getUser() {
            return user;
        }

        public void setUser(User___ user) {
            this.user = user;
        }

        public String getFileURL() {
            return fileURL;
        }

        public void setFileURL(String fileURL) {
            this.fileURL = fileURL;
        }

        public String getCampaignTitle() {
            return campaignTitle;
        }

        public void setCampaignTitle(String campaignTitle) {
            this.campaignTitle = campaignTitle;
        }

        public String getCampaignText() {
            return campaignText;
        }

        public void setCampaignText(String campaignText) {
            this.campaignText = campaignText;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getWebsiteUrl() {
            return websiteUrl;
        }

        public void setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
        }

        public List<String> getGender() {
            return gender;
        }

        public void setGender(List<String> gender) {
            this.gender = gender;
        }

        public String getStartAge() {
            return startAge;
        }

        public void setStartAge(String startAge) {
            this.startAge = startAge;
        }

        public String getEndAge() {
            return endAge;
        }

        public void setEndAge(String endAge) {
            this.endAge = endAge;
        }

        public String getCampaignType() {
            return campaignType;
        }

        public void setCampaignType(String campaignType) {
            this.campaignType = campaignType;
        }

        public List<OtherImageVideo> getOtherImageVideos() {
            return otherImageVideos;
        }

        public void setOtherImageVideos(List<OtherImageVideo> otherImageVideos) {
            this.otherImageVideos = otherImageVideos;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public Boolean getIsAdvertise() {
            return isAdvertise;
        }

        public void setIsAdvertise(Boolean isAdvertise) {
            this.isAdvertise = isAdvertise;
        }

        public Integer getSignedUp() {
            return signedUp;
        }

        public void setSignedUp(Integer signedUp) {
            this.signedUp = signedUp;
        }

        public List<Object> getCampaignLikes() {
            return campaignLikes;
        }

        public void setCampaignLikes(List<Object> campaignLikes) {
            this.campaignLikes = campaignLikes;
        }

        public List<Object> getCampaignComments() {
            return campaignComments;
        }

        public void setCampaignComments(List<Object> campaignComments) {
            this.campaignComments = campaignComments;
        }

    }


}