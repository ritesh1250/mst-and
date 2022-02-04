package com.meest.svs.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoByPostIdResponse {


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
        @SerializedName("isPrivate")
        @Expose
        private Boolean isPrivate;
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
        @SerializedName("access")
        @Expose
        private String access;
        @SerializedName("isAllowComment")
        @Expose
        private Boolean isAllowComment;
        @SerializedName("isAllowDuet")
        @Expose
        private Boolean isAllowDuet;
        @SerializedName("isAllowDownload")
        @Expose
        private Boolean isAllowDownload;
        @SerializedName("isAllowTrio")
        @Expose
        private Boolean isAllowTrio;
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
        private User user;
        @SerializedName("audio")
        @Expose
        private Audio audio;

        public String getAudioFile() {
            return audioFile;
        }

        public void setAudioFile(String audioFile) {
            this.audioFile = audioFile;
        }

        public Boolean getIsPrivate() {
            return isPrivate;
        }

        public void setIsPrivate(Boolean isPrivate) {
            this.isPrivate = isPrivate;
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

        public String getAccess() {
            return access;
        }

        public void setAccess(String access) {
            this.access = access;
        }

        public Boolean getIsAllowComment() {
            return isAllowComment;
        }

        public void setIsAllowComment(Boolean isAllowComment) {
            this.isAllowComment = isAllowComment;
        }

        public Boolean getIsAllowDuet() {
            return isAllowDuet;
        }

        public void setIsAllowDuet(Boolean isAllowDuet) {
            this.isAllowDuet = isAllowDuet;
        }

        public Boolean getIsAllowDownload() {
            return isAllowDownload;
        }

        public void setIsAllowDownload(Boolean isAllowDownload) {
            this.isAllowDownload = isAllowDownload;
        }

        public Boolean getIsAllowTrio() {
            return isAllowTrio;
        }

        public void setIsAllowTrio(Boolean isAllowTrio) {
            this.isAllowTrio = isAllowTrio;
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

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Audio getAudio() {
            return audio;
        }

        public void setAudio(Audio audio) {
            this.audio = audio;
        }

    }


    public class Audio {

        @SerializedName("audioURL")
        @Expose
        private String audioURL;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("catagoryId")
        @Expose
        private Object catagoryId;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("approvalStatus")
        @Expose
        private String approvalStatus;
        @SerializedName("hashtags")
        @Expose
        private Object hashtags;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("deletedAt")
        @Expose
        private Object deletedAt;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

        public String getAudioURL() {
            return audioURL;
        }

        public void setAudioURL(String audioURL) {
            this.audioURL = audioURL;
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

        public Object getCatagoryId() {
            return catagoryId;
        }

        public void setCatagoryId(Object catagoryId) {
            this.catagoryId = catagoryId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getApprovalStatus() {
            return approvalStatus;
        }

        public void setApprovalStatus(String approvalStatus) {
            this.approvalStatus = approvalStatus;
        }

        public Object getHashtags() {
            return hashtags;
        }

        public void setHashtags(Object hashtags) {
            this.hashtags = hashtags;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
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
        @SerializedName("lastName")
        @Expose
        private String lastName;

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


}
