package com.meest.model;

public class BlockAccountItem {
    public int getProfile_img() {
        return profile_img;
    }

    public String getName() {
        return name;
    }

    private int profile_img;
    private String name;

    public BlockAccountItem(int profile_img, String name) {
        this.profile_img = profile_img;
        this.name = name;
    }
}
