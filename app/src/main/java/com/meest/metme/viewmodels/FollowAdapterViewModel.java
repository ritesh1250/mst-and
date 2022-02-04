package com.meest.metme.viewmodels;

import android.content.Context;
import android.view.View;

import androidx.databinding.ObservableBoolean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.meest.metme.adapter.ForwardMessageAdapter;
import com.meest.metme.model.GetForwardListResponse;

public class FollowAdapterViewModel {

    GetForwardListResponse.Data following;
    Context context;
    public ObservableBoolean isSelected = new ObservableBoolean(false);
    ForwardMessageAdapter adapter;

    public FollowAdapterViewModel(Context context, ObservableBoolean isSelected, GetForwardListResponse.Data following, ForwardMessageAdapter adapter) {
        this.following = following;
        this.context = context;
        this.isSelected = isSelected;
        this.username=following.getUsername();

        this.adapter=adapter;
    }








    public void onItemClick(View view) {

        adapter.onClick(view,following);



    }

    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("displayPicture")
    @Expose
    private String displayPicture;
    @SerializedName("gToken")
    @Expose
    private String gToken;
    @SerializedName("fcmToken")
    @Expose
    private String fcmToken;
    @SerializedName("deviceVoipToken")
    @Expose
    private String deviceVoipToken;
    @SerializedName("fbToken")
    @Expose
    private String fbToken;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lag")
    @Expose
    private String lag;
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
    private Boolean isAdharVerified;
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
    @SerializedName("deletedAt")
    @Expose
    private String deletedAt;
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
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
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
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("isFollower")
    @Expose
    private Integer isFollower;
    @SerializedName("isFollowing")
    @Expose
    private Integer isFollowing;
    @SerializedName("accepted")
    @Expose
    private Boolean accepted;
    @SerializedName("firendStatus")
    @Expose
    private String firendStatus;

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDisplayPicture() {
        return displayPicture;
    }

    public void setDisplayPicture(String displayPicture) {
        this.displayPicture = displayPicture;
    }

    public String getgToken() {
        return gToken;
    }

    public void setgToken(String gToken) {
        this.gToken = gToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getDeviceVoipToken() {
        return deviceVoipToken;
    }

    public void setDeviceVoipToken(String deviceVoipToken) {
        this.deviceVoipToken = deviceVoipToken;
    }

    public String getFbToken() {
        return fbToken;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
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

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    public Integer getIsFollower() {
        return isFollower;
    }

    public void setIsFollower(Integer isFollower) {
        this.isFollower = isFollower;
    }

    public Integer getIsFollowing() {
        return isFollowing;
    }

    public void setIsFollowing(Integer isFollowing) {
        this.isFollowing = isFollowing;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public String getFirendStatus() {
        return firendStatus;
    }

    public void setFirendStatus(String firendStatus) {
        this.firendStatus = firendStatus;
    }
}
