package com.meest.svs.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoOtherUserResponse {

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
    public class LikedAudios {

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("rows")
        @Expose
        private List<Object> rows = null;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public List<Object> getRows() {
            return rows;
        }

        public void setRows(List<Object> rows) {
            this.rows = rows;
        }

    }
    public class Row {

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
        private String catagoryId;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("approvalStatus")
        @Expose
        private String approvalStatus;
        @SerializedName("hashtags")
        @Expose
        private List<String> hashtags = null;
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

        public String getCatagoryId() {
            return catagoryId;
        }

        public void setCatagoryId(String catagoryId) {
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

        public List<String> getHashtags() {
            return hashtags;
        }

        public void setHashtags(List<String> hashtags) {
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

    public class UploadedAudios {

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

    public class User {

        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
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
        @SerializedName("referral")
        @Expose
        private String referral;
        @SerializedName("friendReferral")
        @Expose
        private String friendReferral;
        @SerializedName("bio")
        @Expose
        private String bio;
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
        private Integer mobile;
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
        @SerializedName("notification")
        @Expose
        private Boolean notification;
        @SerializedName("mediaAutoDownload")
        @Expose
        private Boolean mediaAutoDownload;
        @SerializedName("dnd")
        @Expose
        private Boolean dnd;
        @SerializedName("totalFollowers")
        @Expose
        private Integer totalFollowers;
        @SerializedName("totalFollowings")
        @Expose
        private Integer totalFollowings;
        @SerializedName("isFriend")
        @Expose
        private Integer isFriend;
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

        public String getReferral() {
            return referral;
        }

        public void setReferral(String referral) {
            this.referral = referral;
        }

        public String getFriendReferral() {
            return friendReferral;
        }

        public void setFriendReferral(String friendReferral) {
            this.friendReferral = friendReferral;
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

        public Integer getMobile() {
            return mobile;
        }

        public void setMobile(Integer mobile) {
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

        public Boolean getNotification() {
            return notification;
        }

        public void setNotification(Boolean notification) {
            this.notification = notification;
        }

        public Boolean getMediaAutoDownload() {
            return mediaAutoDownload;
        }

        public void setMediaAutoDownload(Boolean mediaAutoDownload) {
            this.mediaAutoDownload = mediaAutoDownload;
        }

        public Boolean getDnd() {
            return dnd;
        }

        public void setDnd(Boolean dnd) {
            this.dnd = dnd;
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

        public Integer getIsFriend() {
            return isFriend;
        }

        public void setIsFriend(Integer isFriend) {
            this.isFriend = isFriend;
        }

        public String getFirendStatus() {
            return firendStatus;
        }

        public void setFirendStatus(String firendStatus) {
            this.firendStatus = firendStatus;
        }

    }
    public class Data {

        @SerializedName("user")
        @Expose
        private User user;
        @SerializedName("totalLikes")
        @Expose
        private Integer totalLikes;
        @SerializedName("uploadedAudios")
        @Expose
        private UploadedAudios uploadedAudios;
        @SerializedName("likedAudios")
        @Expose
        private LikedAudios likedAudios;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Integer getTotalLikes() {
            return totalLikes;
        }

        public void setTotalLikes(Integer totalLikes) {
            this.totalLikes = totalLikes;
        }

        public UploadedAudios getUploadedAudios() {
            return uploadedAudios;
        }

        public void setUploadedAudios(UploadedAudios uploadedAudios) {
            this.uploadedAudios = uploadedAudios;
        }

        public LikedAudios getLikedAudios() {
            return likedAudios;
        }

        public void setLikedAudios(LikedAudios likedAudios) {
            this.likedAudios = likedAudios;
        }

    }
}
