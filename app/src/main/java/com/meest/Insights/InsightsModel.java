package com.meest.Insights;

public class InsightsModel {

    private int imageViewProfile;
    private String textViewTitle, textViewDate, tvheading;

    public InsightsModel() {
    }

    public InsightsModel(int imageViewProfile, String textViewTitle, String textViewDate, String tvheading) {
        this.imageViewProfile = imageViewProfile;
        this.textViewTitle = textViewTitle;
        this.textViewDate = textViewDate;
        this.tvheading = tvheading;
    }

    public int getImageViewProfile() {
        return imageViewProfile;
    }

    public void setImageViewProfile(int imageViewProfile) {
        this.imageViewProfile = imageViewProfile;
    }

    public String getTextViewTitle() {
        return textViewTitle;
    }

    public void setTextViewTitle(String textViewTitle) {
        this.textViewTitle = textViewTitle;
    }

    public String getTextViewDate() {
        return textViewDate;
    }

    public void setTextViewDate(String textViewDate) {
        this.textViewDate = textViewDate;
    }

    public String getTvheading() {
        return tvheading;
    }

    public void setTvheading(String tvheading) {
        this.tvheading = tvheading;
    }
}