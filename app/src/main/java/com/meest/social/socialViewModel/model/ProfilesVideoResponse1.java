package com.meest.social.socialViewModel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfilesVideoResponse1 {


    public class Data {

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("rows")
        @Expose
        private List<Row> rows = null;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<Row> getRows() {
            return rows;
        }

        public void setRows(List<Row> rows) {
            this.rows = rows;
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


    public class PostLike {

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


        @SerializedName("user")
        @Expose
        private User user;

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



        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }


    public class Row {

        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("posts")
        @Expose
        private List<Post> posts = null;
        @SerializedName("caption")
        @Expose
        private String caption;
        @SerializedName("hashTags")
        @Expose
        private List<Object> hashTags = null;

        @SerializedName("topicId")
        @Expose
        private String topicId;
        @SerializedName("userId")
        @Expose
        private String userId;
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
        private List<Object> postComments = null;
        @SerializedName("topic")
        @Expose
        private Topic topic;
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;

        public String getThumbnail() {
            return thumbnail == null ? "" : thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        @SerializedName("user")
        @Expose
        private User_ user;

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public List<Object> getPostComments() {
            return postComments;
        }

        public void setPostComments(List<Object> postComments) {
            this.postComments = postComments;
        }

        public Topic getTopic() {
            return topic;
        }

        public void setTopic(Topic topic) {
            this.topic = topic;
        }

        public User_ getUser() {
            return user;
        }

        public void setUser(User_ user) {
            this.user = user;
        }

    }


    public class Topic {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("topic")
        @Expose
        private String topic;
        @SerializedName("status")
        @Expose
        private Boolean status;



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
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
        @SerializedName("accountType")
        @Expose
        private String accountType;
        @SerializedName("likes")
        @Expose
        private Integer likes;
        @SerializedName("status")
        @Expose
        private Boolean status;



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



    }



}
