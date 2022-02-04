package com.meest.model;

import java.util.List;

public class VideoCallModel {
    String callId, mainOwnerId;
    boolean isVideo;
    boolean active;
    List<User> userList;

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getMainOwnerId() {
        return mainOwnerId;
    }

    public void setMainOwnerId(String mainOwnerId) {
        this.mainOwnerId = mainOwnerId;
    }

    public static class User{
        String id, ownerId;
        String name, profilePicture, username;
        Boolean accepted;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Boolean getAccepted() {
            return accepted;
        }

        public void setAccepted(Boolean accepted) {
            this.accepted = accepted;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePicture() {
            return profilePicture == null ? "" : profilePicture;
        }

        public void setProfilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getOwnerId() {
            return ownerId == null ? "" : ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }
    }

}
