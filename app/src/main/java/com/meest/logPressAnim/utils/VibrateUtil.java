package com.meest.logPressAnim.utils;

import android.content.Context;
import android.os.Vibrator;

public class VibrateUtil {

    private static final Object mSingletonLock = new Object();
    private static Vibrator vibrator = null;

    private static Vibrator getInstance(Context context) {
        synchronized (mSingletonLock) {
            if (vibrator != null)
                return vibrator;
            if (context != null)
                vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            return vibrator;
        }
    }

    public static void vibtate(Context context, int vibrationDuration) {
        if (vibrationDuration > 0)
            getInstance(context).vibrate(vibrationDuration);
    }
}
