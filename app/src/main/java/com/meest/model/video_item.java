package com.meest.model;

public class video_item {

    public video_item(int profile_img, int backgroung_img, String like_count, String name) {
        this.profile_img = profile_img;
        this.backgroung_img = backgroung_img;
        this.like_count = like_count;
        Name = name;
    }

    private int profile_img;
    private int backgroung_img;
    private String like_count;
    private String Name;

    public int getProfile_img() {
        return profile_img;
    }

    public int getBackgroung_img() {
        return backgroung_img;
    }

    public String getLike_count() {
        return like_count;
    }

    public String getName() {
        return Name;
    }


}
