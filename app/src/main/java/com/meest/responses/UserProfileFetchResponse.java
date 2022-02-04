package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfileFetchResponse {

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

        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("posts")
        @Expose
        private List<Post_> posts = null;
        @SerializedName("caption")
        @Expose
        private String caption;
        @SerializedName("hashTags")
        @Expose
        private List<String> hashTags = null;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("topicId")
        @Expose
        private String topicId;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("fontColor")
        @Expose
        private String fontColor;
        @SerializedName("backImg")
        @Expose
        private String backImg;
        @SerializedName("viewPost")
        @Expose
        private Object viewPost;
        @SerializedName("allowComment")
        @Expose
        private Boolean allowComment;
        @SerializedName("postType")
        @Expose
        private String postType;
        @SerializedName("locationFrom")
        @Expose
        private String locationFrom;
        @SerializedName("locationTo")
        @Expose
        private String locationTo;

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

        public List<Post_> getPosts() {
            return posts;
        }

        public void setPosts(List<Post_> posts) {
            this.posts = posts;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public List<String> getHashTags() {
            return hashTags;
        }

        public void setHashTags(List<String> hashTags) {
            this.hashTags = hashTags;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
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

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getFontColor() {
            return fontColor;
        }

        public void setFontColor(String fontColor) {
            this.fontColor = fontColor;
        }

        public String getBackImg() {
            return backImg;
        }

        public void setBackImg(String backImg) {
            this.backImg = backImg;
        }

        public Object getViewPost() {
            return viewPost;
        }

        public void setViewPost(Object viewPost) {
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

        public String getLocationFrom() {
            return locationFrom;
        }

        public void setLocationFrom(String locationFrom) {
            this.locationFrom = locationFrom;
        }

        public String getLocationTo() {
            return locationTo;
        }

        public void setLocationTo(String locationTo) {
            this.locationTo = locationTo;
        }

    }


    public class Post_ {

        @SerializedName("post")
        @Expose
        private String post;
        @SerializedName("image")
        @Expose
        private Integer image;
        @SerializedName("video")
        @Expose
        private Integer video;

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
    }


    public class Post__ {

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


    public class Row {

        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("posts")
        @Expose
        private List<FeedResponse.Post> posts = null;
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
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
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
        @SerializedName("topic")
        @Expose
        private Topic topic;
        @SerializedName("allowComment")
        @Expose
        private Boolean allowComment;

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

        public List<FeedResponse.Post> getPosts() {
            return posts;
        }

        public void setPosts(List<FeedResponse.Post> posts) {
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

        public Topic getTopic() {
            return topic;
        }

        public void setTopic(Topic topic) {
            this.topic = topic;
        }

        public String getThumbnail() {
            return thumbnail == null ? "" : thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public Boolean getAllowComment() {
            return allowComment == null ? true : allowComment;
        }

        public void setAllowComment(Boolean allowComment) {
            this.allowComment = allowComment;
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
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

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

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }


    public class User {

        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;


        @SerializedName("coverPicture")
        @Expose
        private String coverPicture;

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
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("accountType")
        @Expose
        private String accountType;
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
        @SerializedName("friendStatus")
        @Expose
        private String friendStatus;

        @SerializedName("isBlocked")
        @Expose
        private String isBlocked;

        @SerializedName("post")
        @Expose
        private Post__ post;

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


        public String getCoverPicture() {
            return coverPicture;
        }

        public void setCoverPicture(String coverPicture) {
            this.coverPicture = coverPicture;
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

        public String getFriendStatus() {
            return friendStatus;
        }

        public void setFriendStatus(String friendStatus) {
            this.friendStatus = friendStatus;
        }

        public Post__ getPost() {
            return post;
        }

        public void setPost(Post__ post) {
            this.post = post;
        }

        public String getIsBlocked() {
            return isBlocked;
        }

        public void setIsBlocked(String isBlocked) {
            this.isBlocked = isBlocked;
        }
    }


}
