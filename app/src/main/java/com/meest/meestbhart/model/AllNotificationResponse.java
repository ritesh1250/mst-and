package com.meest.meestbhart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllNotificationResponse {

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

        @SerializedName("count")
        @Expose
        private Integer count;



        @SerializedName("unReadCount")
        @Expose
        private Integer unReadCount;





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

        public Integer getUnReadCount() {
            return unReadCount;
        }

        public void setUnReadCount(Integer unReadCount) {
            this.unReadCount = unReadCount;
        }
    }

    public class NotificationBody {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("likeId")
        @Expose
        private String likeId;
        @SerializedName("postId")
        @Expose
        private String postId;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("notificationType")
        @Expose
        private String notificationType;
        @SerializedName("comment")
        @Expose
        private String comment;

        private String senderSocketId;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLikeId() {
            return likeId;
        }

        public void setLikeId(String likeId) {
            this.likeId = likeId;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getUserId() {
            return userId;
        }

        public String getSenderSocketId() {
            return senderSocketId;
        }

        public void setSenderSocketId(String senderSocketId) {
            this.senderSocketId = senderSocketId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getDisplayPicture() {
            return displayPicture;
        }

        public void setDisplayPicture(String displayPicture) {
            this.displayPicture = displayPicture;
        }

        public String getNotificationType() {
            return notificationType == null ? "" : notificationType;
        }

        public void setNotificationType(String notificationType) {
            this.notificationType = notificationType;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    public class Row {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("notifyTo")
        @Expose
        private String notifyTo;
        @SerializedName("notificationBody")
        @Expose
        private NotificationBody notificationBody;
        @SerializedName("isCleard")
        @Expose
        private Boolean isCleard;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("notificationType")
        @Expose
        private String notificationType;
        @SerializedName("status")
        @Expose
        private Boolean status;

        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        @SerializedName("user")
        @Expose
        private User user;

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

        public String getNotifyTo() {
            return notifyTo;
        }

        public void setNotifyTo(String notifyTo) {
            this.notifyTo = notifyTo;
        }

        public NotificationBody getNotificationBody() {
            return notificationBody;
        }

        public void setNotificationBody(NotificationBody notificationBody) {
            this.notificationBody = notificationBody;
        }

        public Boolean getIsCleard() {
            return isCleard;
        }

        public void setIsCleard(Boolean isCleard) {
            this.isCleard = isCleard;
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



        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }

    public class User {

        @SerializedName("displayPicture")
        @Expose
        private String displayPicture;
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("lastName")
        @Expose
        private String lastName;

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

    }

}
