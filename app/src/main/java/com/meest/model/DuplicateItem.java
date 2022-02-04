package com.meest.model;

public class DuplicateItem {

    private int img_duplicate;
    private String txt1_duplicate;
    private String txt2_duplicate;
    private String txt3_duplicate;

    public int getImg_duplicate() {
        return img_duplicate;
    }

    public String getTxt1_duplicate() {
        return txt1_duplicate;
    }

    public String getTxt2_duplicate() {
        return txt2_duplicate;
    }

    public String getTxt3_duplicate() {
        return txt3_duplicate;
    }

    public String getTxt4_duplicate() {
        return txt4_duplicate;
    }

    public DuplicateItem(int img_duplicate, String txt1_duplicate, String txt2_duplicate, String txt3_duplicate, String txt4_duplicate) {
        this.img_duplicate = img_duplicate;
        this.txt1_duplicate = txt1_duplicate;
        this.txt2_duplicate = txt2_duplicate;
        this.txt3_duplicate = txt3_duplicate;
        this.txt4_duplicate = txt4_duplicate;
    }

    private String txt4_duplicate;

}
