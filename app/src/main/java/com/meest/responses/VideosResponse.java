package com.meest.responses;

import com.meest.svs.models.AudioDataModel;
import com.meest.svs.models.UserDataModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosResponse {

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

    public class Row {

        @SerializedName("audio_file")
        @Expose
        private String audioFile;
        @SerializedName("hashtags")
        @Expose
        private List<String> hashtags = null;
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
        @SerializedName("thumbnail_image")
        @Expose
        private String thumbnailImage;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("videoURL")
        @Expose
        private String videoURL;
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
        @SerializedName("views")
        @Expose
        private Integer views;
        @SerializedName("isLiked")
        @Expose
        private Integer isLiked;
        @SerializedName("user")
        @Expose
        private UserDataModel user;
        @SerializedName("isPrivate")
        @Expose
        private Boolean isPrivate = false;
        @SerializedName("isAllowComment")
        @Expose
        private Boolean isAllowComment;
        @SerializedName("isAllowDownload")
        @Expose
        private Boolean isAllowDownload;
        @SerializedName("isAllowDuet")
        @Expose
        private Boolean isAllowDuet;
        @SerializedName("isAllowTrio")
        @Expose
        private Boolean isAllowTrio;
        @SerializedName("audio")
        @Expose
        private AudioDataModel audioDataModel;

        public String getAudioFile() {
            return audioFile;
        }

        public void setAudioFile(String audioFile) {
            this.audioFile = audioFile;
        }

        public List<String> getHashtags() {
            return hashtags;
        }

        public void setHashtags(List<String> hashtags) {
            this.hashtags = hashtags;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getThumbnailImage() {
            return thumbnailImage;
        }

        public void setThumbnailImage(String thumbnailImage) {
            this.thumbnailImage = thumbnailImage;
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

        public String getVideoURL() {
            return videoURL;
        }

        public void setVideoURL(String videoURL) {
            this.videoURL = videoURL;
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

        public Integer getViews() {
            return views;
        }

        public void setViews(Integer views) {
            this.views = views;
        }

        public Integer getIsLiked() {
            return isLiked;
        }

        public void setIsLiked(Integer isLiked) {
            this.isLiked = isLiked;
        }

        public UserDataModel getUser() {
            return user;
        }

        public void setUser(UserDataModel user) {
            this.user = user;
        }

        public Boolean getPrivate() {
            return isPrivate;
        }

        public void setPrivate(Boolean aPrivate) {
            isPrivate = aPrivate;
        }

        public Boolean getAllowComment() {
            return isAllowComment;
        }

        public void setAllowComment(Boolean allowComment) {
            isAllowComment = allowComment;
        }

        public Boolean getAllowDownload() {
            return isAllowDownload == null ? true : isAllowDownload;
        }

        public void setAllowDownload(Boolean allowDownload) {
            isAllowDownload = allowDownload;
        }

        public Boolean getAllowDuet() {
            return isAllowDuet == null ? true : isAllowDuet;
        }

        public void setAllowDuet(Boolean allowDuet) {
            isAllowDuet = allowDuet;
        }

        public Boolean getAllowTrio() {
            return isAllowTrio == null ? true : isAllowTrio;
        }

        public void setAllowTrio(Boolean allowTrio) {
            isAllowTrio = allowTrio;
        }

        public AudioDataModel getAudioDataModel() {
            return audioDataModel;
        }

        public void setAudioDataModel(AudioDataModel audioDataModel) {
            this.audioDataModel = audioDataModel;
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

        @SerializedName("firstName")
        @Expose
        private String firstName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

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
}
