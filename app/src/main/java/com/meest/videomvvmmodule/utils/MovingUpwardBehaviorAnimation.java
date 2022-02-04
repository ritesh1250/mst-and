package com.meest.videomvvmmodule.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.snackbar.Snackbar;

public class MovingUpwardBehaviorAnimation extends CoordinatorLayout.Behavior<View>
{
    public MovingUpwardBehaviorAnimation() {
        super();
    }

    public MovingUpwardBehaviorAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float translationY = Math.min(0, ViewCompat.getTranslationY(dependency) - dependency.getHeight());
        ViewCompat.setTranslationY(child, translationY);
        return true;
    }
    public void onDependentViewRemoved(CoordinatorLayout parent, View child, View dependency) {
        ViewCompat.animate(child).translationY(0).start();
    }

}
