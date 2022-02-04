package com.meest.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetReportResponse {


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

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("isShared")
        @Expose
        private Boolean isShared;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("postId")
        @Expose
        private String postId;
        @SerializedName("pushNotification")
        @Expose
        private Boolean pushNotification;
        @SerializedName("isMute")
        @Expose
        private Boolean isMute;
        @SerializedName("friendId")
        @Expose
        private String friendId;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("report")
        @Expose
        private String report;
        @SerializedName("isReportedByme")
        @Expose
        private Boolean isReportedByme;

        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Boolean getIsShared() {
            return isShared;
        }

        public void setIsShared(Boolean isShared) {
            this.isShared = isShared;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public Boolean getPushNotification() {
            return pushNotification;
        }

        public void setPushNotification(Boolean pushNotification) {
            this.pushNotification = pushNotification;
        }

        public Boolean getIsMute() {
            return isMute;
        }

        public void setIsMute(Boolean isMute) {
            this.isMute = isMute;
        }

        public String getFriendId() {
            return friendId;
        }

        public void setFriendId(String friendId) {
            this.friendId = friendId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getReport() {
            return report == null ? "" : report;
        }

        public void setReport(String report) {
            this.report = report;
        }

        public Boolean getIsReportedByme() {
            return isReportedByme;
        }

        public void setIsReportedByme(Boolean isReportedByme) {
            this.isReportedByme = isReportedByme;
        }



        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

    }


}
