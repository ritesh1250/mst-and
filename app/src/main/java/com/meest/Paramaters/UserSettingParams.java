package com.meest.Paramaters;

public class UserSettingParams {
    boolean notification = false;
    boolean mediaAutoDownload = false;
    boolean dnd = false;
    String accountType;
    String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isNotification() {
        return notification;
    }

    public boolean isMediaAutoDownload() {
        return mediaAutoDownload;
    }

    public boolean isDnd() {
        return dnd;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean getNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public boolean getMediaAutoDownload() {
        return mediaAutoDownload;
    }

    public void setMediaAutoDownload(boolean mediaAutoDownload) {
        this.mediaAutoDownload = mediaAutoDownload;
    }

    public boolean getDnd() {
        return dnd;
    }

    public void setDnd(boolean dnd) {
        this.dnd = dnd;
    }
}
