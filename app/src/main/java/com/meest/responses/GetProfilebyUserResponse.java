
package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProfilebyUserResponse {

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

        @SerializedName("user")
        @Expose
        private User user;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }




        public class User {

            @SerializedName("dob")
            @Expose
            private String dob;
            @SerializedName("displayPicture")
            @Expose
            private String displayPicture;

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

            @SerializedName("bio")
            @Expose
            private String bio;

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

            public String getCoverPicture() {
                return coverPicture;
            }

            public void setCoverPicture(String coverPicture) {
                this.coverPicture = coverPicture;
            }

            @SerializedName("likes")
            @Expose
            private Integer likes;

            @SerializedName("totalFollowers")
            @Expose
            private Integer totalFollowers;
            @SerializedName("totalFollowings")
            @Expose
            private Integer totalFollowings;
            @SerializedName("totalPosts")
            @Expose
            private Integer totalPosts;
            @SerializedName("posts")
            @Expose
            private List<Object> posts = null;
            @SerializedName("follows")
            @Expose
            private List<Follow> follows = null;

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
            public String getBio() {
                return bio;
            }

            public void setBio(String bio) {
                this.bio = bio;
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



            public String getGender() {
                return gender;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
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

            public Integer getLikes() {
                return likes;
            }

            public void setLikes(Integer likes) {
                this.likes = likes;
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

            public Integer getTotalPosts() {
                return totalPosts;
            }

            public void setTotalPosts(Integer totalPosts) {
                this.totalPosts = totalPosts;
            }

            public List<Object> getPosts() {
                return posts;
            }

            public void setPosts(List<Object> posts) {
                this.posts = posts;
            }

            public List<Follow> getFollows() {
                return follows;
            }

            public void setFollows(List<Follow> follows) {
                this.follows = follows;
            }

            public class Follow {

                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("userId")
                @Expose
                private String userId;
                @SerializedName("followingId")
                @Expose
                private String followingId;
                @SerializedName("status")
                @Expose
                private Boolean status;
                @SerializedName("accepted")
                @Expose
                private Boolean accepted;

                @SerializedName("followingData")
                @Expose
                private FollowingData followingData;

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

                public String getFollowingId() {
                    return followingId;
                }

                public void setFollowingId(String followingId) {
                    this.followingId = followingId;
                }

                public Boolean getStatus() {
                    return status;
                }

                public void setStatus(Boolean status) {
                    this.status = status;
                }

                public Boolean getAccepted() {
                    return accepted;
                }

                public void setAccepted(Boolean accepted) {
                    this.accepted = accepted;
                }




                public FollowingData getFollowingData() {
                    return followingData;
                }

                public void setFollowingData(FollowingData followingData) {
                    this.followingData = followingData;
                }




                public class FollowingData {

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

                    public String getMobile() {
                        return mobile;
                    }

                    public void setMobile(String mobile) {
                        this.mobile = mobile;
                    }

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




                }


            }



        }



    }


}
