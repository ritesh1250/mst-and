package com.meest.videomvvmmodule.model;

import org.jetbrains.annotations.NotNull;

public class NotificationModel {
    String message = "", lastName = "", firstName = "", displayPicture = "", user_name = "", userId = "", notificationType = "", postId = "", comment_id = "", thumbnail = "", addView = "";

    @NotNull
    @Override
    public String toString() {
        return "NotificationModel{" +
                "message='" + message + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", displayPicture='" + displayPicture + '\'' +
                ", user_name='" + user_name + '\'' +
                ", userId='" + userId + '\'' +
                ", notificationType='" + notificationType + '\'' +
                ", postId='" + postId + '\'' +
                ", comment_id='" + comment_id + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", addView='" + addView + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddView() {
        return addView;
    }

    public void setAddView(String addView) {
        this.addView = addView;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
