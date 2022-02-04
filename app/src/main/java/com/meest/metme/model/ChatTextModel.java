package com.meest.metme.model;

public class ChatTextModel {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getImage() {
        return image;
    }

    public String getImageText() {
        return ImageText;
    }

    public void setImageText(String imageText) {
        ImageText = imageText;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String ImageText;
    private String message;
    private String userName;
    private String msgTime;
    private String read;
    private String image;

}
