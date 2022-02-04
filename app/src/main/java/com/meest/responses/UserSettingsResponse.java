package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserSettingsResponse {
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


    public class Post {


        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("posts")
        @Expose
        private List<Post_> posts = null;
        @SerializedName("caption")
        @Expose
        private Object caption;
        @SerializedName("hashTags")
        @Expose
        private List<Object> hashTags = null;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("topicId")
        @Expose
        private Object topicId;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("status")
        @Expose
        private Boolean status;

        @SerializedName("fontColor")
        @Expose
        private Object fontColor;
        @SerializedName("backImg")
        @Expose
        private Object backImg;
        @SerializedName("viewPost")
        @Expose
        private String viewPost;
        @SerializedName("allowComment")
        @Expose
        private Boolean allowComment;
        @SerializedName("postType")
        @Expose
        private String postType;
        @SerializedName("tags")
        @Expose
        private Object tags;
        @SerializedName("locationFrom")
        @Expose
        private Object locationFrom;
        @SerializedName("locationTo")
        @Expose
        private Object locationTo;

        @SerializedName("postComments")
        @Expose
        private List<Object> postComments = null;
        @SerializedName("postLikes")
        @Expose
        private List<PostLike> postLikes = null;



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Post_> getPosts() {
            return posts;
        }

        public void setPosts(List<Post_> posts) {
            this.posts = posts;
        }

        public Object getCaption() {
            return caption;
        }

        public void setCaption(Object caption) {
            this.caption = caption;
        }

        public List<Object> getHashTags() {
            return hashTags;
        }

        public void setHashTags(List<Object> hashTags) {
            this.hashTags = hashTags;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Object getTopicId() {
            return topicId;
        }

        public void setTopicId(Object topicId) {
            this.topicId = topicId;
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



        public Object getFontColor() {
            return fontColor;
        }

        public void setFontColor(Object fontColor) {
            this.fontColor = fontColor;
        }

        public Object getBackImg() {
            return backImg;
        }

        public void setBackImg(Object backImg) {
            this.backImg = backImg;
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

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public Object getTags() {
            return tags;
        }

        public void setTags(Object tags) {
            this.tags = tags;
        }

        public Object getLocationFrom() {
            return locationFrom;
        }

        public void setLocationFrom(Object locationFrom) {
            this.locationFrom = locationFrom;
        }

        public Object getLocationTo() {
            return locationTo;
        }

        public void setLocationTo(Object locationTo) {
            this.locationTo = locationTo;
        }


        public List<Object> getPostComments() {
            return postComments;
        }

        public void setPostComments(List<Object> postComments) {
            this.postComments = postComments;
        }

        public List<PostLike> getPostLikes() {
            return postLikes;
        }

        public void setPostLikes(List<PostLike> postLikes) {
            this.postLikes = postLikes;
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

        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

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

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

    public class Post_ {

        @SerializedName("post")
        @Expose
        private String post;
        @SerializedName("image")
        @Expose
        private Integer image;

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

    }


    public class User {

        @SerializedName("dob")
        @Expose
        private String dob;

        @SerializedName("language")
        @Expose
        private String language;
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
        @SerializedName("bio")
        @Expose
        private Object bio;
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
        @SerializedName("posts")
        @Expose
        private List<Post> posts = null;
        @SerializedName("follows")
        @Expose
        private List<Object> follows = null;

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
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

        public Object getBio() {
            return bio;
        }

        public void setBio(Object bio) {
            this.bio = bio;
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

        public List<Post> getPosts() {
            return posts;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

        public List<Object> getFollows() {
            return follows;
        }

        public void setFollows(List<Object> follows) {
            this.follows = follows;
        }

    }
}
