package com.meest.svs.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HashtagVideoResponse {

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

        @SerializedName("videos")
        @Expose
        private Videos videos;

        public Videos getVideos() {
            return videos;
        }

        public void setVideos(Videos videos) {
            this.videos = videos;
        }

    }

    public class Videos {

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("rows")
        @Expose
        private List<VideoDetailModel> rows = null;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<VideoDetailModel> getRows() {
            return rows;
        }

        public void setRows(List<VideoDetailModel> rows) {
            this.rows = rows;
        }

    }

    public class PostedBy {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("hashtag")
        @Expose
        private String hashtag;
        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("addedBy")
        @Expose
        private String addedBy;
        @SerializedName("status")
        @Expose
        private Boolean status;


        @SerializedName("isFriend")
        @Expose
        private Integer isFriend;
        @SerializedName("user")
        @Expose
        private UserDataModel user;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHashtag() {
            return hashtag;
        }

        public void setHashtag(String hashtag) {
            this.hashtag = hashtag;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getAddedBy() {
            return addedBy;
        }

        public void setAddedBy(String addedBy) {
            this.addedBy = addedBy;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }


        public Integer getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(Integer isFriend) {
            this.isFriend = isFriend;
        }

        public UserDataModel getUser() {
            return user;
        }

        public void setUser(UserDataModel user) {
            this.user = user;
        }

    }

}
