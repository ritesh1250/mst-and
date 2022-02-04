package com.meest.model;

import androidx.recyclerview.widget.RecyclerView;

public class Celebrating_item {

    private int celebratingA_img;
    private String celebrating_name;

    public int getCelebratingA_img() {
        return celebratingA_img;
    }

    public String getCelebrating_name() {
        return celebrating_name;
    }

    public Celebrating_item(int celebratingA_img, String celebrating_name) { RecyclerView recyclerView;
        RecyclerView.Adapter adapter;
        RecyclerView.LayoutManager manager;
        this.celebratingA_img = celebratingA_img;
        this.celebrating_name = celebrating_name;
    }
}
