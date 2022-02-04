package com.meest.metme.model;

public class ChatListModel {

    private String username;
    private String msg;
    private String time;
    private Boolean setCheckBox = true;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(String msgCount) {
        this.msgCount = msgCount;
    }

    private String msgCount;

    public Boolean getSetCheckBox() {
        return setCheckBox;
    }

    public void setSetCheckBox(Boolean setCheckBox) {
        this.setCheckBox = setCheckBox;
    }
}
