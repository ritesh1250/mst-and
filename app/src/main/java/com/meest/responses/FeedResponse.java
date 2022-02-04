package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.meest.model.SharedPost;

import java.util.List;

public class FeedResponse {


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

    public class Location {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("coordinates")
        @Expose
        private List<Double> coordinates = null;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Double> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Double> coordinates) {
            this.coordinates = coordinates;
        }

    }

    public class Data {

        @SerializedName("count")
        @Expose
        private Integer count;

        @SerializedName("unreadNotification")
        @Expose
        private Integer unreadNotification;
        @SerializedName("rows")
        @Expose
        private List<Row> rows = null;

        public Integer getUnreadNotification() {
            return unreadNotification;
        }

        public void setUnreadNotification(Integer unreadNotification) {
            this.unreadNotification = unreadNotification;
        }

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

    public class Post {

        @SerializedName("post")
        @Expose
        private String post;
        @SerializedName("image")
        @Expose
        private Integer image = 0;
        @SerializedName("video")
        @Expose
        private Integer video = 0;
        @SerializedName("isText")
        @Expose
        private String isText = "0";
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
            return image == null ? 0 : image;
        }

        public void setImage(Integer image) {
            this.image = image;
        }

        public Integer getVideo() {
            return video == null ? 0 : video;
        }

        public void setVideo(Integer video) {
            this.video = video;
        }

        public String getIsText() {
            return isText == null ? "0" : isText;
        }

        public void setIsText(String isText) {
            this.isText = isText;
        }

        public String getTextColorCode() {
            return textColorCode == null ? "#000000" : textColorCode;
        }

        public void setTextColorCode(String textColorCode) {
            this.textColorCode = textColorCode;
        }
    }

    public class PostComment {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("subCommentId")
        @Expose
        private Object subCommentId;
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

        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("commentLikes")
        @Expose
        private List<Object> commentLikes = null;
        @SerializedName("subCommentData")
        @Expose
        private Object subCommentData;

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

        public Object getSubCommentId() {
            return subCommentId;
        }

        public void setSubCommentId(Object subCommentId) {
            this.subCommentId = subCommentId;
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


        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public List<Object> getCommentLikes() {
            return commentLikes;
        }

        public void setCommentLikes(List<Object> commentLikes) {
            this.commentLikes = commentLikes;
        }

        public Object getSubCommentData() {
            return subCommentData;
        }

        public void setSubCommentData(Object subCommentData) {
            this.subCommentData = subCommentData;
        }
    }

    public static class PostLike {

        public PostLike() {
        }


        public PostLike(String id, String postId, String userId, Boolean disliked, Boolean status, String createdAt, UserLikedPost user) {
            this.id = id;
            this.postId = postId;
            this.userId = userId;
            this.disliked = disliked;
            this.status = status;
            this.createdAt = createdAt;
            this.user = user;
        }

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

        @SerializedName("user")
        @Expose
        private UserLikedPost user;

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


        public UserLikedPost getUser() {
            return user;
        }

        public void setUser(UserLikedPost user) {
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
        @SerializedName("liked")
        @Expose
        private Integer liked;
        @SerializedName("disliked")
        @Expose
        private Integer disliked;
        @SerializedName("postSaved")
        @Expose
        private Integer postSaved;
        @SerializedName("likeCounts")
        @Expose
        private Integer likeCounts;
        @SerializedName("dislikeCounts")
        @Expose
        private Integer dislikeCounts;
        @SerializedName("postComments")
        @Expose
        private List<PostComment> postComments = null;
        @SerializedName("topic")
        @Expose
        private Topic topic;
        @SerializedName("user")
        @Expose
        private User_ user;
        @SerializedName("postLikes")
        @Expose
        private List<PostLike> postLikes = null;
        @SerializedName("postType")
        @Expose
        private String postType;
        @SerializedName("subFeeling")
        @Expose
        private String subFeeling;
        @SerializedName("feeling")
        @Expose
        private String feeling;
        @SerializedName("tags")
        @Expose
        private List<FeedResponse.UserTags> tags;
        @SerializedName("allowComment")
        @Expose
        private Boolean allowComment;
        @SerializedName("sensitiveContent")
        @Expose
        private Boolean sensitiveContent;
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;


        @SerializedName("sharedPosts")
        @Expose
        private List<SharedPost> sharedPosts;

        @SerializedName("shareCount")
        @Expose
        private String shareCount;
        @SerializedName("activityPost")
        @Expose
        private ActivityPost activityPost;
        @SerializedName("subActivityPost")
        @Expose
        private SubActivityPost subActivityPost;

        private boolean isMuteVideo=false;

        public Boolean getSensitiveContent() {
            return sensitiveContent;
        }

        public void setSensitiveContent(Boolean sensitiveContent) {
            this.sensitiveContent = sensitiveContent;
        }

        public String getShareCount() {
            return shareCount;
        }

        public List<SharedPost> getSharedPosts() {
            return sharedPosts;
        }

        public void setSharedPosts(List<SharedPost> sharedPosts) {
            this.sharedPosts = sharedPosts;
        }

        public void setShareCount(String shareCount) {
            this.shareCount = shareCount;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public boolean isMuteVideo() {
            return isMuteVideo;
        }

        public void setMuteVideo(boolean muteVideo) {
            isMuteVideo = muteVideo;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
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

        public List<PostComment> getPostComments() {
            return postComments;
        }

        public void setPostComments(List<PostComment> postComments) {
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

        public List<PostLike> getPostLikes() {
            return postLikes;
        }

        public void setPostLikes(List<PostLike> postLikes) {
            this.postLikes = postLikes;
        }

        public Integer getPostSaved() {
            return postSaved == null ? 0 : postSaved;
        }

        public void setPostSaved(Integer postSaved) {
            this.postSaved = postSaved;
        }

        public String getPostType() {
            return postType;
        }

        public void setPostType(String postType) {
            this.postType = postType;
        }

        public String getSubFeeling() {
            return subFeeling;
        }

        public void setSubFeeling(String subFeeling) {
            this.subFeeling = subFeeling;
        }

        public String getFeeling() {
            return feeling;
        }

        public void setFeeling(String feeling) {
            this.feeling = feeling;
        }

        public List<UserTags> getTags() {
            return tags;
        }

        public void setTags(List<UserTags> tags) {
            this.tags = tags;
        }

        public Boolean getAllowComment() {
            return allowComment;
        }

        public void setAllowComment(Boolean allowComment) {
            this.allowComment = allowComment;
        }

        public ActivityPost getActivityPost() {
            return activityPost;
        }

        public void setActivityPost(ActivityPost activityPost) {
            this.activityPost = activityPost;
        }

        public SubActivityPost getSubActivityPost() {
            return subActivityPost;
        }

        public void setSubActivityPost(SubActivityPost subActivityPost) {
            this.subActivityPost = subActivityPost;
        }
    }

    public class SubActivityPost {

        @SerializedName("activityType")
        @Expose
        private String activityType;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("icon")
        @Expose
        private String icon;

        public String getActivityType() {
            return activityType;
        }

        public void setActivityType(String activityType) {
            this.activityType = activityType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public class ActivityPost {

        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("icon")
        @Expose
        private String icon;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
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

        @SerializedName("createdAt")
        @Expose
        private String createdAt;


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


        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }


    }


    public static class UserTags {

        @SerializedName("userId")
        @Expose
        private String id;
        @SerializedName("userName")
        @Expose
        private String username;
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("firstName")
        @Expose
        private String firstName = "";
        @SerializedName("lastName")
        @Expose
        private String lastName = "";

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

        public String getDisplayPicture() {
            return displayPicture;
        }

        public void setDisplayPicture(String displayPicture) {
            this.displayPicture = displayPicture;
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

    public static class User {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;

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

        public String getDisplayPicture() {
            return displayPicture;
        }

        public void setDisplayPicture(String displayPicture) {
            this.displayPicture = displayPicture;
        }

    }

    public class User_ {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("dob")
        @Expose
        private Object dob;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("gToken")
        @Expose
        private Object gToken;
        @SerializedName("fcmToken")
        @Expose
        private Object fcmToken;
        @SerializedName("fbToken")
        @Expose
        private Object fbToken;
        @SerializedName("likes")
        @Expose
        private Integer likes;
        @SerializedName("status")
        @Expose
        private Boolean status;

        @SerializedName("createdAt")
        @Expose
        private String createdAt;


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

        public Object getDob() {
            return dob;
        }

        public void setDob(Object dob) {
            this.dob = dob;
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

        public String getDisplayPicture() {
            return displayPicture;
        }

        public void setDisplayPicture(String displayPicture) {
            this.displayPicture = displayPicture;
        }
        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
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

        public Object getGToken() {
            return gToken;
        }

        public void setGToken(Object gToken) {
            this.gToken = gToken;
        }

        public Object getFcmToken() {
            return fcmToken;
        }

        public void setFcmToken(Object fcmToken) {
            this.fcmToken = fcmToken;
        }

        public Object getFbToken() {
            return fbToken;
        }

        public void setFbToken(Object fbToken) {
            this.fbToken = fbToken;
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

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }


    }

    public class UserLikedPost {

        public UserLikedPost() {
        }

        public UserLikedPost(String id, String username, String displayPicture, String thumbnail) {
            this.id = id;
            this.username = username;
            this.displayPicture = displayPicture;
            this.thumbnail = thumbnail;
        }

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;

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

        public String getDisplayPicture() {
            return displayPicture;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public void setDisplayPicture(String displayPicture) {
            this.displayPicture = displayPicture;
        }

    }

}