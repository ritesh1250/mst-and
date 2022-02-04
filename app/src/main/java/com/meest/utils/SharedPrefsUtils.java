package com.meest.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsUtils {
    private final static String PREF_FILE = "Meest";
    private static SharedPreferences mSharedPref;

    public static boolean getSharedPreferenceBoolean(Context context, String key, boolean defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getBoolean(key, defValue);
    }

    public static void setSharedPreferenceBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }



}
