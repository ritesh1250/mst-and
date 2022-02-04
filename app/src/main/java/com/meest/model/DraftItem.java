package com.meest.model;

public class DraftItem {
    private int img_draft;
    private String txt1draft;
    private String txt2draft;
    private String txt3draft;

    public int getImg_draft() {
        return img_draft;
    }

    public String getTxt1draft() {
        return txt1draft;
    }

    public String getTxt2draft() {
        return txt2draft;
    }

    public String getTxt3draft() {
        return txt3draft;
    }

    public String getTxt4draft() {
        return txt4draft;
    }

    public DraftItem(int img_draft, String txt1draft, String txt2draft, String txt3draft, String txt4draft) {
        this.img_draft = img_draft;
        this.txt1draft = txt1draft;
        this.txt2draft = txt2draft;
        this.txt3draft = txt3draft;
        this.txt4draft = txt4draft;
    }

    private String txt4draft;
}
