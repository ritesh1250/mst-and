
package com.meest.videomvvmmodule.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Notification {

    @Expose
    private List<Data> data;
    @Expose
    private String message;
    @Expose
    private Boolean status;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public static class Data {

        @SerializedName("created_date")
        private String createdDate;
        @SerializedName("full_name")
        private String fullName;

        @SerializedName("first_name")
        private String first_name;

        @Expose
        private String message;
        @SerializedName("notification_type")
        private String notificationType;
        @SerializedName("received_user_id")
        private String receivedUserId;
        @SerializedName("sender_user_id")
        private String senderUserId;
        @SerializedName("user_name")
        private String userName;
        @SerializedName("user_profile")
        private String userProfile;
        @SerializedName("item_id")
        private String itemId;
        @SerializedName("post_video")
        private String postVideo;
        @SerializedName("follow_or_not")
        private String follow_or_not;
        @SerializedName("time")
        private String time;

        public String getFollow_or_not() {
            return follow_or_not;
        }

        public void setFollow_or_not(String follow_or_not) {
            this.follow_or_not = follow_or_not;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPostVideo() {
            return postVideo;
        }

        public void setPostVideo(String postVideo) {
            this.postVideo = postVideo;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getNotificationType() {
            return notificationType;
        }

        public void setNotificationType(String notificationType) {
            this.notificationType = notificationType;
        }

        public String getReceivedUserId() {
            return receivedUserId;
        }

        public void setReceivedUserId(String receivedUserId) {
            this.receivedUserId = receivedUserId;
        }

        public String getSenderUserId() {
            return senderUserId;
        }

        public void setSenderUserId(String senderUserId) {
            this.senderUserId = senderUserId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserProfile() {
            return userProfile;
        }

        public void setUserProfile(String userProfile) {
            this.userProfile = userProfile;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }
    }
}
