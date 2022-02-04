package com.meest.social.socialViewModel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuggestionResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("gTOken")
        @Expose
        private Object gTOken;
        @SerializedName("fbToken")
        @Expose
        private Object fbToken;
        @SerializedName("likes")
        @Expose
        private Integer likes;
        @SerializedName("status")
        @Expose
        private Integer status;

        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("otp")
        @Expose
        private Object otp;
        @SerializedName("otpExpires")
        @Expose
        private Object otpExpires;
        @SerializedName("fcmToken")
        @Expose
        private String fcmToken;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("isOnline")
        @Expose
        private Integer isOnline;
        @SerializedName("lastLoggedIn")
        @Expose
        private Object lastLoggedIn;
        @SerializedName("ip")
        @Expose
        private Object ip;
        @SerializedName("ios")
        @Expose
        private Object ios;
        @SerializedName("about")
        @Expose
        private Object about;
        @SerializedName("accountType")
        @Expose
        private String accountType;
        @SerializedName("deviceVoipToken")
        @Expose
        private Object deviceVoipToken;
        @SerializedName("adharFront")
        @Expose
        private Object adharFront;
        @SerializedName("adharBack")
        @Expose
        private Object adharBack;
        @SerializedName("adharNumber")
        @Expose
        private Object adharNumber;
        @SerializedName("isAdharVerified")
        @Expose
        private Integer isAdharVerified;
        @SerializedName("lat")
        @Expose
        private Object lat;
        @SerializedName("lag")
        @Expose
        private Object lag;

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

        public String getDisplayPicture() {
            return displayPicture;
        }

        public void setDisplayPicture(String displayPicture) {
            this.displayPicture = displayPicture;
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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Object getGTOken() {
            return gTOken;
        }

        public void setGTOken(Object gTOken) {
            this.gTOken = gTOken;
        }

        public Object getFbToken() {
            return fbToken;
        }

        public void setFbToken(Object fbToken) {
            this.fbToken = fbToken;
        }

        public Integer getLikes() {
            return likes;
        }

        public void setLikes(Integer likes) {
            this.likes = likes;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }


        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Object getOtp() {
            return otp;
        }

        public void setOtp(Object otp) {
            this.otp = otp;
        }

        public Object getOtpExpires() {
            return otpExpires;
        }

        public void setOtpExpires(Object otpExpires) {
            this.otpExpires = otpExpires;
        }

        public String getFcmToken() {
            return fcmToken;
        }

        public void setFcmToken(String fcmToken) {
            this.fcmToken = fcmToken;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public Integer getIsOnline() {
            return isOnline;
        }

        public void setIsOnline(Integer isOnline) {
            this.isOnline = isOnline;
        }

        public Object getLastLoggedIn() {
            return lastLoggedIn;
        }

        public void setLastLoggedIn(Object lastLoggedIn) {
            this.lastLoggedIn = lastLoggedIn;
        }

        public Object getIp() {
            return ip;
        }

        public void setIp(Object ip) {
            this.ip = ip;
        }

        public Object getIos() {
            return ios;
        }

        public void setIos(Object ios) {
            this.ios = ios;
        }

        public Object getAbout() {
            return about;
        }

        public void setAbout(Object about) {
            this.about = about;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public Object getDeviceVoipToken() {
            return deviceVoipToken;
        }

        public void setDeviceVoipToken(Object deviceVoipToken) {
            this.deviceVoipToken = deviceVoipToken;
        }

        public Object getAdharFront() {
            return adharFront;
        }

        public void setAdharFront(Object adharFront) {
            this.adharFront = adharFront;
        }

        public Object getAdharBack() {
            return adharBack;
        }

        public void setAdharBack(Object adharBack) {
            this.adharBack = adharBack;
        }

        public Object getAdharNumber() {
            return adharNumber;
        }

        public void setAdharNumber(Object adharNumber) {
            this.adharNumber = adharNumber;
        }

        public Integer getIsAdharVerified() {
            return isAdharVerified;
        }

        public void setIsAdharVerified(Integer isAdharVerified) {
            this.isAdharVerified = isAdharVerified;
        }

        public Object getLat() {
            return lat;
        }

        public void setLat(Object lat) {
            this.lat = lat;
        }

        public Object getLag() {
            return lag;
        }

        public void setLag(Object lag) {
            this.lag = lag;
        }

    }

}
