package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class UserActivityResponse {

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
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;

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

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

    }

    public class PostDatas {

        @SerializedName("caption")
        @Expose
        private String caption;
        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("posts")
        @Expose
        private List<Post> posts = null;
        @SerializedName("id")
        @Expose
        private String id;

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public List<Post> getPosts() {
            return posts;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    public class StorieDatas {

        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("story")
        @Expose
        private String story;

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

        public String getStory() {
            return story;
        }

        public void setStory(String story) {
            this.story = story;
        }

    }

    public class Row {

        @SerializedName("postId")
        @Expose
        private String postId;
        @SerializedName("followingId")
        @Expose
        private String followingId;
        @SerializedName("commentId")
        @Expose
        private String commentId;
        @SerializedName("storiesId")
        @Expose
        private String storiesId;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("status")
        @Expose
        private Boolean status;

        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        @SerializedName("followingData")
        @Expose
        private FollowingData followingData;

        public FollowingData getFollowingData() {
            return followingData;
        }

        public void setFollowingData(FollowingData followingData) {
            this.followingData = followingData;
        }


        @SerializedName("postDatas")
        @Expose
        private PostDatas postDatas;
        @SerializedName("storieDatas")
        @Expose
        private StorieDatas storieDatas;

        public StorieDatas getStorieDatas() {
            return storieDatas;
        }

        public void setStorieDatas(StorieDatas storieDatas) {
            this.storieDatas = storieDatas;
        }

        @SerializedName("postcomments")
        @Expose
        private Postcomments postcomments;

        public Postcomments getPostcomments() {
            return postcomments;
        }

        public void setPostcomments(Postcomments postcomments) {
            this.postcomments = postcomments;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getFollowingId() {
            return followingId;
        }

        public void setFollowingId(String followingId) {
            this.followingId = followingId;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getStoriesId() {
            return storiesId;
        }

        public void setStoriesId(String storiesId) {
            this.storiesId = storiesId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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


        public PostDatas getPostDatas() {
            return postDatas;
        }

        public void setPostDatas(PostDatas postDatas) {
            this.postDatas = postDatas;
        }
    }

    public class FollowingData_ {

        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("id")
        @Expose
        private String id;

        public String getDisplayPicture() {
            return displayPicture;
        }

        public void setDisplayPicture(String displayPicture) {
            this.displayPicture = displayPicture;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    public class FollowingData {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("followingData")
        @Expose
        private FollowingData_ followingData;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public FollowingData_ getFollowingData() {
            return followingData;
        }

        public void setFollowingData(FollowingData_ followingData) {
            this.followingData = followingData;
        }

    }

    public class Postcomments {

        @SerializedName("subCommentId")
        @Expose
        private String subCommentId;

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

        public String getSubCommentId() {
            return subCommentId;
        }

        public void setSubCommentId(String subCommentId) {
            this.subCommentId = subCommentId;
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

    }
}