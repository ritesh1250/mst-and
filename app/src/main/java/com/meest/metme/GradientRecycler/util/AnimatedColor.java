package com.meest.metme.GradientRecycler.util;

import android.graphics.Color;
import android.util.Log;

public class AnimatedColor {
    public float[] startHSV;
    public float[] endHSV;
    public float[] move;
    public int startColor;
    public int endColor;

    public int with(float delta) {
        if (delta <= (float) 0) {
            return this.startColor;
        } else {
            return delta >= (float) 1 ? this.endColor : Color.HSVToColor(move(delta));
        }
    }

    public float[] move(float delta) {
        Log.e("@@@@@@@@@endHSV", String.valueOf(this.endHSV[0]));
        Log.e("@@@@@@@@@startHSV", String.valueOf(this.startHSV[0]));
        Log.e("@@@@@@@@@Delta", String.valueOf(delta));
        this.move[0] = (this.endHSV[0] - this.startHSV[0]) * delta + this.startHSV[0];
        Log.e("@@@@@@@@@move_0", String.valueOf(this.move[0]));
        this.move[1] = (this.endHSV[1] - this.startHSV[1]) * delta + this.startHSV[1];
        Log.e("@@@@@@@@@move_1", String.valueOf(this.move[1]));
        this.move[2] = (this.endHSV[2] - this.startHSV[2]) * delta + this.startHSV[2];
        Log.e("@@@@@@@@@move_2", String.valueOf(this.move[2]));
        return this.move;
    }

    public float[] toHSV(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return hsv;
    }

    public AnimatedColor(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.move = new float[3];
        this.startHSV = this.toHSV(this.startColor);
        this.endHSV = this.toHSV(this.endColor);
    }

}
