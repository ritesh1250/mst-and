package com.meest.model;

public class EatingItem {

    private int eating_img1;
    private int eating_img2;
    private String eating_name;

    public int getEating_img1() {
        return eating_img1;
    }

    public int getEating_img2() {
        return eating_img2;
    }

    public String getEating_name() {
        return eating_name;
    }

    public EatingItem(int eating_img1, int eating_img2, String eating_name) {
        this.eating_img1 = eating_img1;
        this.eating_img2 = eating_img2;
        this.eating_name = eating_name;
    }
}
