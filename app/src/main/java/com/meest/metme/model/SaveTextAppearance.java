package com.meest.metme.model;

public class SaveTextAppearance {
    String firstColor, secondColor, bgColor, fontUrl, gradient, toUserId,wallpaper,bgFirstColor,bgSecondColor,fontId;
    public SaveTextAppearance(String firstColor, String secondColor, String bgColor, String fontUrl, String gradient, String toUserId, String bgFirstColor, String bgSecondColor, String wallpaper, String fontId) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.bgColor = bgColor;
        this.fontUrl = fontUrl;
        this.gradient = gradient;
        this.toUserId = toUserId;
        this.bgFirstColor = bgFirstColor;
        this.bgSecondColor = bgSecondColor;
        this.wallpaper = wallpaper;
        this.fontId=fontId;
    }

    public String getFontId() {
        return fontId;
    }

    public void setFontId(String fontId) {
        this.fontId = fontId;
    }

    public String getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(String wallpaper) {
        this.wallpaper = wallpaper;
    }

    public String getBgFirstColor() {
        return bgFirstColor;
    }

    public void setBgFirstColor(String bgFirstColor) {
        this.bgFirstColor = bgFirstColor;
    }

    public String getBgSecondColor() {
        return bgSecondColor;
    }

    public void setBgSecondColor(String bgSecondColor) {
        this.bgSecondColor = bgSecondColor;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFirstColor() {
        return firstColor;
    }

    public void setFirstColor(String firstColor) {
        this.firstColor = firstColor;
    }

    public String getSecondColor() {
        return secondColor;
    }

    public void setSecondColor(String secondColor) {
        this.secondColor = secondColor;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public String getFontUrl() {
        return fontUrl;
    }

    public void setFontUrl(String fontUrl) {
        this.fontUrl = fontUrl;
    }

    public String getGradient() {
        return gradient;
    }

    public void setGradient(String gradient) {
        this.gradient = gradient;
    }
}
