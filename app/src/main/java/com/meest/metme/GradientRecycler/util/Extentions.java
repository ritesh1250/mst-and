package com.meest.metme.GradientRecycler.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.graphics.drawable.DrawableCompat;

public  class Extentions {
    public static  float absY( View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return (float)location[1] - (float)view.getHeight();
    }

    public static  void updateTint(Drawable drawable, int color) {
        Drawable var10000 = DrawableCompat.wrap(drawable);
        if (var10000 != null) {
            Drawable var2 = var10000;
            DrawableCompat.setTint(var2, color);
        }

    }

    public static  int dpToPx(int $this$dpToPx) {
        Resources var10001 = Resources.getSystem();
        return $this$dpToPx * (int)var10001.getDisplayMetrics().density;
    }
}

