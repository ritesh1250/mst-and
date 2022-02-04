package com.meest.model;

public class GoingItem {

    private int going_img;

    public int getGoing_img() {
        return going_img;
    }

    public String getGoing_name() {
        return going_name;
    }

    public GoingItem(int going_img, String going_name) {
        this.going_img = going_img;
        this.going_name = going_name;
    }

    private String going_name;
}
