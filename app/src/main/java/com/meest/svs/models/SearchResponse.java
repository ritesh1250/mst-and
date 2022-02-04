package com.meest.svs.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

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

        @SerializedName("peoples")
        @Expose
        private List<Persons> peoples = null;
        @SerializedName("videos")
        @Expose
        private Video videos = null;
        @SerializedName("hashtags")
        @Expose
        private List<Hashtag> hashtags = null;

        public List<Persons> getPeoples() {
            return peoples;
        }

        public void setPeoples(List<Persons> peoples) {
            this.peoples = peoples;
        }

        public Video getVideos() {
            return videos;
        }

        public void setVideos(Video videos) {
            this.videos = videos;
        }

        public List<Hashtag> getHashtags() {
            return hashtags;
        }

        public void setHashtags(List<Hashtag> hashtags) {
            this.hashtags = hashtags;
        }

    }

    public class Hashtag {

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


    }

    public class Persons {

        @SerializedName("dob")
        @Expose
        private String dob;
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
        @SerializedName("isFriend")
        @Expose
        private Integer isFriend;
        @SerializedName("accountType")
        @Expose
        private String accountType;
        @SerializedName("firendStatus")
        @Expose
        private String firendStatus;

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

        public Integer getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(Integer isFriend) {
            this.isFriend = isFriend;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getFirendStatus() {
            return firendStatus;
        }

        public void setFirendStatus(String firendStatus) {
            this.firendStatus = firendStatus;
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

    public class Video{
        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("rows")
        @Expose
        private List<VideoDetail> rows;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<VideoDetail> getRows() {
            return rows;
        }

        public void setRows(List<VideoDetail> rows) {
            this.rows = rows;
        }
    }

    public class VideoDetail {

        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("videoURL")
        @Expose
        private String videoURL;
        @SerializedName("title")
        @Expose
        private Object title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("likes")
        @Expose
        private Integer likes;
        @SerializedName("user")
        @Expose
        private User user;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVideoURL() {
            return videoURL;
        }

        public void setVideoURL(String videoURL) {
            this.videoURL = videoURL;
        }

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Integer getLikes() {
            return likes;
        }

        public void setLikes(Integer likes) {
            this.likes = likes;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
}
