package com.meest.videomvvmmodule.model.videos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosResponse {

    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("success")
    @Expose
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public class Data {

        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("rows")
        @Expose
        private List<Row> rows = null;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
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
        private int likes;
        @SerializedName("comments")
        @Expose
        private int comments;
        @SerializedName("isFriend")
        @Expose
        private int isFriend;
        @SerializedName("views")
        @Expose
        private int views;

        @SerializedName("isLiked")
        @Expose
        private int isLiked;
        @SerializedName("user")
        @Expose
        private UserDataModel user;
        @SerializedName("isPrivate")
        @Expose
        private boolean isPrivate = false;
        @SerializedName("isAllowComment")
        @Expose
        private boolean isAllowComment;
        @SerializedName("isAllowDownload")
        @Expose
        private boolean isAllowDownload;
        @SerializedName("isAllowDuet")
        @Expose
        private boolean isAllowDuet;
        @SerializedName("isAllowTrio")
        @Expose
        private Boolean isAllowTrio;
        @SerializedName("audio")
        @Expose
        private AudioDataModel audioDataModel;

        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }

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

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public int getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(int isFriend) {
            this.isFriend = isFriend;
        }

        public int getIsLiked() {
            return isLiked;
        }

        public void setIsLiked(int isLiked) {
            this.isLiked = isLiked;
        }

        public UserDataModel getUser() {
            return user;
        }

        public void setUser(UserDataModel user) {
            this.user = user;
        }

        public boolean getPrivate() {
            return isPrivate;
        }

        public void setPrivate(boolean aPrivate) {
            isPrivate = aPrivate;
        }

        public boolean getAllowComment() {
            return isAllowComment;
        }

        public void setAllowComment(boolean allowComment) {
            isAllowComment = allowComment;
        }

        public boolean getAllowDownload() {
            return isAllowDownload == false ? true : isAllowDownload;
        }

        public void setAllowDownload(boolean allowDownload) {
            isAllowDownload = allowDownload;
        }

        public boolean getAllowDuet() {
            return isAllowDuet == false ? true : isAllowDuet;
        }

        public void setAllowDuet(boolean allowDuet) {
            isAllowDuet = allowDuet;
        }

        public boolean getAllowTrio() {
            return isAllowTrio == null ? true : isAllowTrio;
        }

        public void setAllowTrio(boolean allowTrio) {
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
