package com.meest.font;

import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;

public class AppAppearanceManager {
    private AppCompatActivity appCompatActivity;

    public static final String REGULAR = "Regular";
    public static final String ITALIC = "Italic";
    public static final String BOLD = "Bold";


    public AppAppearanceManager(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }
    public void setFont(String fontName){

        switch (fontName){
            case REGULAR:{
                appCompatActivity.getTheme().applyStyle(R.style.Regular, true);
                break;
            }
            case ITALIC:{
                appCompatActivity.getTheme().applyStyle(R.style.Italic, true);
                break;
            }
            case BOLD:{
                appCompatActivity.getTheme().applyStyle(R.style.Bold, true);
                break;
            }
        }
    }
}
