package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoPostResponse {

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

    public class Hashtags {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("data")
        @Expose
        private List<Integer> data = null;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Integer> getData() {
            return data;
        }

        public void setData(List<Integer> data) {
            this.data = data;
        }

    }

    public class Row {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private Object userId;
        @SerializedName("videoURL")
        @Expose
        private Object videoURL;
        @SerializedName("hashtags")
        @Expose
        private Hashtags hashtags;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("likes")
        @Expose
        private Integer likes;
        @SerializedName("comments")
        @Expose
        private Integer comments;
        @SerializedName("isFriend")
        @Expose
        private Integer isFriend;
        @SerializedName("user")
        @Expose
        private Object user;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public Object getVideoURL() {
            return videoURL;
        }

        public void setVideoURL(Object videoURL) {
            this.videoURL = videoURL;
        }

        public Hashtags getHashtags() {
            return hashtags;
        }

        public void setHashtags(Hashtags hashtags) {
            this.hashtags = hashtags;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getLikes() {
            return likes;
        }

        public void setLikes(Integer likes) {
            this.likes = likes;
        }

        public Integer getComments() {
            return comments;
        }

        public void setComments(Integer comments) {
            this.comments = comments;
        }

        public Integer getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(Integer isFriend) {
            this.isFriend = isFriend;
        }

        public Object getUser() {
            return user;
        }

        public void setUser(Object user) {
            this.user = user;
        }

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
}
