package com.meest.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

public class LinearLayoutThatDetectsSoftKeyboard extends LinearLayout
{

    public LinearLayoutThatDetectsSoftKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public interface Listener {
        public void onSoftKeyboardShown(boolean isShowing);
    }
    private Listener listener;
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        Activity activity = (Activity)getContext();
//        Rect rect = new Rect();
//        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
//        int statusBarHeight = rect.top;
//        int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
//        int diff = (screenHeight - statusBarHeight) - height;
//        if (listener != null) {
//            listener.onSoftKeyboardShown(diff>128); // assume all soft keyboards are at least 128 pixels high
//        }
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

        int statusBarHeight = 0;
        if (resourceId > 0) {

            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

       DisplayMetrics dm = getResources().getDisplayMetrics();

        int screenHeight = dm.heightPixels;
        int diff = (screenHeight - statusBarHeight) - height;
        if(listener != null){
            listener.onSoftKeyboardShown(diff>128);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}