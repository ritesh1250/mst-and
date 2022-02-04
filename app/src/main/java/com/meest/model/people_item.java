package com.meest.model;

public class people_item {

    public int getProfile_img() {
        return profile_img;
    }

    public String getName() {
        return name;
    }

    public String getName2() {
        return name2;
    }

    public String getFollow() {
        return follow;
    }

    private int profile_img;
    private String name;
    private String name2;
    private String follow;

    public people_item(int profile_img, String name, String name2, String follow) {
        this.profile_img = profile_img;
        this.name = name;
        this.name2 = name2;
        this.follow = follow;
    }


}
