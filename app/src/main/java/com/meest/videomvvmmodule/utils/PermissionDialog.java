package com.meest.videomvvmmodule.utils;

import android.graphics.drawable.Drawable;

public class PermissionDialog {
    private Drawable drawable;
    private String positiveText;
    private String negativeText;
    private String message;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPositiveText() {
        return positiveText;
    }

    public void setPositiveText(String positiveText) {
        this.positiveText = positiveText;
    }

    public String getNegativeText() {
        return negativeText;
    }

    public void setNegativeText(String negativeText) {
        this.negativeText = negativeText;
    }
}
