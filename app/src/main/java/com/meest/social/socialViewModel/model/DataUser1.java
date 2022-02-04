package com.meest.social.socialViewModel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class DataUser1 {

    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("displayPicture")
    @Expose
    private String displayPicture;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("coverPicture")
    @Expose
    private String coverPicture;
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
    @SerializedName("isAdharVerified")
    @Expose
    private Boolean isAdharVerified;
    @SerializedName("about")
    @Expose
    private String about;
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
    private BigInteger mobile;
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
    @SerializedName("referral")
    @Expose
    private String referral;
    @SerializedName("notification")
    @Expose
    private Boolean notification;
    @SerializedName("mediaAutoDownload")
    @Expose
    private Boolean mediaAutoDownload;
    @SerializedName("dnd")
    @Expose
    private Boolean dnd;
    @SerializedName("agoraId")
    @Expose
    private Integer agoraId;
    @SerializedName("totalFollowings")
    @Expose
    private Integer totalFollowings;
    @SerializedName("isFollower")
    @Expose
    private Integer isFollower;
    @SerializedName("totalFollowers")
    @Expose
    private Integer totalFollowers;
    @SerializedName("blockedByMe")
    @Expose
    private Integer blockedByMe;
    @SerializedName("isBlocked")
    @Expose
    private Integer isBlocked;
    @SerializedName("totalPosts")
    @Expose
    private Integer totalPosts;
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
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
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

    public Boolean getIsAdharVerified() {
        return isAdharVerified;
    }

    public void setIsAdharVerified(Boolean isAdharVerified) {
        this.isAdharVerified = isAdharVerified;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

    public BigInteger getMobile() {
        return mobile;
    }

    public void setMobile(BigInteger mobile) {
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

    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
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

    public Integer getAgoraId() {
        return agoraId;
    }

    public void setAgoraId(Integer agoraId) {
        this.agoraId = agoraId;
    }

    public Integer getTotalFollowings() {
        return totalFollowings;
    }

    public void setTotalFollowings(Integer totalFollowings) {
        this.totalFollowings = totalFollowings;
    }

    public Integer getIsFollower() {
        return isFollower;
    }

    public void setIsFollower(Integer isFollower) {
        this.isFollower = isFollower;
    }

    public Integer getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(Integer totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public Integer getBlockedByMe() {
        return blockedByMe;
    }

    public void setBlockedByMe(Integer blockedByMe) {
        this.blockedByMe = blockedByMe;
    }

    public Integer getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Integer isBlocked) {
        this.isBlocked = isBlocked;
    }

    public Integer getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(Integer totalPosts) {
        this.totalPosts = totalPosts;
    }

    public String getFirendStatus() {
        return firendStatus;
    }

    public void setFirendStatus(String firendStatus) {
        this.firendStatus = firendStatus;
    }

}