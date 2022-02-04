package com.meest.metme.model;

public class ChatUserContactModel {
    public String chatProfileImage;
    public String chatUserName;
    public String chatHeadId;
    public String follower;
    public String following;
    public String post;
    public String bio;
    public String userId;
    public String fullname;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getChatProfileImage() {
        return chatProfileImage;
    }

    public void setChatProfileImage(String chatProfileImage) {
        this.chatProfileImage = chatProfileImage;
    }

    public String getChatUserName() {
        return chatUserName;
    }

    public void setChatUserName(String chatUserName) {
        this.chatUserName = chatUserName;
    }

    public String getChatHeadId() {
        return chatHeadId;
    }

    public void setChatHeadId(String chatHeadId) {
        this.chatHeadId = chatHeadId;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
