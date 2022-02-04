package com.meest.social.socialViewModel.model.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoCommentResponse {

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
            return count == null ? 0 : count;
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

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("postId")
        @Expose
        private String postId;
        @SerializedName("videoId")
        @Expose
        private String videoId;
        @SerializedName("campaignId")
        @Expose
        private String campaignId;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("liked")
        @Expose
        private Integer liked;
        @SerializedName("subCount")
        private Integer subCount;
        @SerializedName("isFriend")
        @Expose
        private Integer isFriend;
        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("videoCommentLikes")
        @Expose
        private List<Object> videoCommentLikes = null;
        @SerializedName("subCommentData")
        @Expose
        private List<VideoSubComment> videoSubComments = null;

        public String getId() {
            return id;
        }

        public String getCampaignId() {
            return campaignId;
        }

        public void setCampaignId(String campaignId) {
            this.campaignId = campaignId;
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

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
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

        public Integer getLiked() {
            return liked;
        }

        public void setLiked(Integer liked) {
            this.liked = liked;
        }

        public Integer getIsFriend() {
            return isFriend;
        }

        public Integer getSubCount() {
            return subCount;
        }

        public void setSubCount(Integer subCount) {
            this.subCount = subCount;
        }

        public void setIsFriend(Integer isFriend) {
            this.isFriend = isFriend;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public List<Object> getVideoCommentLikes() {
            return videoCommentLikes;
        }

        public void setVideoCommentLikes(List<Object> videoCommentLikes) {
            this.videoCommentLikes = videoCommentLikes;
        }

        public List<VideoSubComment> getVideoSubComments() {
            return videoSubComments;
        }

        public void setVideoSubComments(List<VideoSubComment> videoSubComments) {
            this.videoSubComments = videoSubComments;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
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

    public class VideoSubComment {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("comment")
        @Expose
        private String comment;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("user")
        @Expose
        private User user;

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

    }
}
