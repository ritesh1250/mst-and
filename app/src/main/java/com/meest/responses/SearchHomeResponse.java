package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchHomeResponse {

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
        private List<Video> videos = null;
        @SerializedName("hashtags")
        @Expose
        private List<Hashtag> hashtags = null;


        public List<Persons> getPeoples() {
            return peoples;
        }

        public void setPeoples(List<Persons> peoples) {
            this.peoples = peoples;
        }

        public List<Video> getVideos() {
            return videos;
        }

        public void setVideos(List<Video> videos) {
            this.videos = videos;
        }

        public List<Hashtag> getHashtags() {
            return hashtags;
        }

        public void setHashtags(List<Hashtag> hashtags) {
            this.hashtags = hashtags;
        }

        public class Persons {

            @SerializedName("isFriend")
            @Expose
            private String isFriend;
            @SerializedName("dob")
            @Expose
            private String dob;
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
            private String mobile;
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

            public String getGToken() {
                return gToken;
            }

            public void setGToken(String gToken) {
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

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
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



            public String getIsFriend() {
                return isFriend;
            }

            public void setIsFriend(String isFriend) {
                this.isFriend = isFriend;
            }

            public String getFirendStatus() {
                return firendStatus;
            }

            public void setFirendStatus(String firendStatus) {
                this.firendStatus = firendStatus;
            }
        }


        public class Video {


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

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
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
            @SerializedName("status")
            @Expose
            private Boolean status;

            @SerializedName("updatedAt")
            @Expose
            private String updatedAt;

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

            public Boolean getStatus() {
                return status;
            }

            public void setStatus(Boolean status) {
                this.status = status;
            }


            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

        }


    }


}
