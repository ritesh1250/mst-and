package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("displayPicture")
    @Expose
    private String displayPicture;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lag")
    @Expose
    private String lag;
    @SerializedName("otpExpires")
    @Expose
    private String otpExpires;
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
    private Boolean ios;
    @SerializedName("adharFront")
    @Expose
    private String adharFront;
    @SerializedName("adharBack")
    @Expose
    private String adharBack;
    @SerializedName("adharNumber")
    @Expose
    private String adharNumber;
    @SerializedName("isAdharVerified")
    @Expose
    private String isAdharVerified;
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
    @SerializedName("passwordChangedOn")
    @Expose
    private String passwordChangedOn;
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
    private Long mobile;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("notification")
    @Expose
    private Boolean notification;
    @SerializedName("mediaAutoDownload")
    @Expose
    private Boolean mediaAutoDownload;
    @SerializedName("isReal")
    @Expose
    private Boolean isReal;
    @SerializedName("dnd")
    @Expose
    private Boolean dnd;
    @SerializedName("accountType")
    @Expose
    private String accountType;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("firendStatus")
    @Expose
    private String friendStatus;
    @SerializedName("isFollower")
    @Expose
    private int isFollower;
    @SerializedName("isFollowing")
    @Expose
    private int isFollowing;
    @SerializedName("accepted")
    @Expose
    private boolean accepted;


    public int getIsFollower() {
        return isFollower;
    }

    public void setIsFollower(int isFollower) {
        this.isFollower = isFollower;
    }

    public int getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(int isFollowing) {
        this.isFollowing = isFollowing;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLag() {
        return lag;
    }

    public void setLag(String lag) {
        this.lag = lag;
    }

    public String getOtpExpires() {
        return otpExpires;
    }

    public void setOtpExpires(String otpExpires) {
        this.otpExpires = otpExpires;
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

    public Boolean getIos() {
        return ios;
    }

    public void setIos(Boolean ios) {
        this.ios = ios;
    }

    public String getAdharFront() {
        return adharFront;
    }

    public void setAdharFront(String adharFront) {
        this.adharFront = adharFront;
    }

    public String getAdharBack() {
        return adharBack;
    }

    public void setAdharBack(String adharBack) {
        this.adharBack = adharBack;
    }

    public String getAdharNumber() {
        return adharNumber;
    }

    public void setAdharNumber(String adharNumber) {
        this.adharNumber = adharNumber;
    }

    public String getIsAdharVerified() {
        return isAdharVerified;
    }

    public void setIsAdharVerified(String isAdharVerified) {
        this.isAdharVerified = isAdharVerified;
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

    public String getPasswordChangedOn() {
        return passwordChangedOn;
    }

    public void setPasswordChangedOn(String passwordChangedOn) {
        this.passwordChangedOn = passwordChangedOn;
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

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public Boolean getIsReal() {
        return isReal;
    }

    public void setIsReal(Boolean isReal) {
        this.isReal = isReal;
    }

    public Boolean getDnd() {
        return dnd;
    }

    public void setDnd(Boolean dnd) {
        this.dnd = dnd;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFriendStatus() {
        return friendStatus == null ? "NoFriend" : friendStatus;
    }

    public void setFriendStatus(String friendStatus) {
        this.friendStatus = friendStatus;
    }
}
