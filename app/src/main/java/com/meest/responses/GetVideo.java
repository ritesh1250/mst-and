package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetVideo {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("total_records")
    @Expose
    private String totalRecords;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public class Datum {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("contest_id")
        @Expose
        private String contestId;
        @SerializedName("total_likes")
        @Expose
        private String totalLikes;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("last_updated")
        @Expose
        private String lastUpdated;
        @SerializedName("user_name")
        @Expose
        private String user_name;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("bio")
        @Expose
        private String bio;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("total_comments")
        @Expose
        private String totalComments;
        @SerializedName("is_like_video")
        @Expose
        private String isLikeVideo;


        @SerializedName("product_url")
        @Expose
        private String product_url;


        @SerializedName("product_id")
        @Expose
        private String product_id;

        public String getProduct_url() {
            return product_url;
        }

        public void setProduct_url(String product_url) {
            this.product_url = product_url;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getContestId() {
            return contestId;
        }

        public void setContestId(String contestId) {
            this.contestId = contestId;
        }

        public String getTotalLikes() {
            return totalLikes;
        }

        public void setTotalLikes(String totalLikes) {
            this.totalLikes = totalLikes;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public String getUserName() {
            return user_name;
        }

        public void setUserName(String userName) {
            this.user_name = userName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTotalComments() {
            return totalComments;
        }

        public void setTotalComments(String totalComments) {
            this.totalComments = totalComments;
        }

        public String getIsLikeVideo() {
            return isLikeVideo;
        }

        public void setIsLikeVideo(String isLikeVideo) {
            this.isLikeVideo = isLikeVideo;
        }
    }
}
