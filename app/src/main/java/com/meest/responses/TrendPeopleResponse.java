package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrendPeopleResponse {

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
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("userName")
        @Expose
        private String userName;
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("viewPost")
        @Expose
        private String viewPost;
        @SerializedName("about")
        @Expose
        private String about;
        @SerializedName("followerCount")
        @Expose
        private Integer followerCount;
        @SerializedName("firendStatus")
        @Expose
        private String firendStatus;

        @SerializedName("accountType")
        @Expose
        private String accountType;

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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getDisplayPicture() {
            return displayPicture;
        }

        public void setDisplayPicture(String displayPicture) {
            this.displayPicture = displayPicture;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getViewPost() {
            return viewPost;
        }

        public void setViewPost(String viewPost) {
            this.viewPost = viewPost;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public Integer getFollowerCount() {
            return followerCount;
        }

        public void setFollowerCount(Integer followerCount) {
            this.followerCount = followerCount;
        }

        public String getFirendStatus() {
            return firendStatus == null ? "noFriend" : firendStatus;
        }

        public void setFirendStatus(String firendStatus) {
            this.firendStatus = firendStatus;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }
    }
}
