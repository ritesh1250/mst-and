package com.meest.metme.model;

public class ChatBoatModel {


    private String chatProfileImage;
    private String chatUserName;
    private String lastSeen;
    private String typing;
    private String chatHeadId;
    private String userId;
    private boolean isOnline;
    private String previousMsg;
    private String previousDate;

    public String getPreviousDate() {
        return previousDate;
    }

    public void setPreviousDate(String previousDate) {
        this.previousDate = previousDate;
    }

    public String getPreviousMsg() {
        return previousMsg;
    }

    public void setPreviousMsg(String previousMsg) {
        this.previousMsg = previousMsg;
    }

    public String getChatHeadId() {
        return chatHeadId;
    }

    public void setChatHeadId(String chatHeadId) {
        this.chatHeadId = chatHeadId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getTyping() {
        return typing;
    }

    public void setTyping(String typing) {
        this.typing = typing;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    @Override
    public String toString() {
        return "ChatBoatModel{" +
                "chatProfileImage='" + chatProfileImage + '\'' +
                ", chatUserName='" + chatUserName + '\'' +
                ", lastSeen='" + lastSeen + '\'' +
                ", typing='" + typing + '\'' +
                ", chatHeadId='" + chatHeadId + '\'' +
                ", userId='" + userId + '\'' +
                ", isOnline=" + isOnline +
                ", previousDate=" + previousDate +
                '}';
    }
}
