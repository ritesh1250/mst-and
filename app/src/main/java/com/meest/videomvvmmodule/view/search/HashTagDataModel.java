package com.meest.videomvvmmodule.view.search;

/**
 * Created by sanket kumar.
 */
public class HashTagDataModel {

    public String icon;
    public String name;

    public HashTagDataModel(String icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
