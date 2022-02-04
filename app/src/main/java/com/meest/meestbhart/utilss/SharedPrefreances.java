package com.meest.meestbhart.utilss;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefreances {

    public final static String APP_NEW_UPDATE = "APP_NEW_UPDATE";
    public final static String APP_NEW_UPDATE_CHECK = "APP_NEW_UPDATE_CHECK";
    private final static String PREF_FILE = "Meest";
    public static String Reg_F_NAME = "Reg_F_NAME";
    public static String Reg_L_NAME = "Reg_L_NAME";
    public static String Reg_BOD = "Reg_BOD";
    public static String Reg_USERNAME = "Reg_USERNAME";
    public static String Reg_PASSWORD = "Reg_PASSWORD";
    public static String Reg_GENDER = "Reg_GENDER";
    public static String Reg_MOBILE = "Reg_MOBILE";
    public static String PROFILE = "PROFILE";
    public static String PROFILE_IMAGE = "image";
    public static String Reg_OTP = "Reg_OTP";
    public static String BIO = "bio";
    public static String Reg_EMAil = "EMAil";
    public static String is_EMAil = "isEmail";
    public static String CACHE_FOR_TIKTOK = "CACHE_FOR_TIKTOK";
    public static String IS_OTP_VERIFY = "isOtp";
    public static String IS_REGISTER_VERIFY = "register";
    public static String IS_INTEREST = "interset";
    public static String IS_LOGGED_IN = "loggedIn";

    public static String F_NAME = "F_NAME";
    public static String L_NAME = "L_NAME";
    public static String BOD = "BOD";
    public static String ID = "ID";
    public static String WHEREFROM = "WHEREFROM";
    public static String USERNAME = "USERNAME";
    public static String PASSWORD = "PASSWORD";
    public static String GENDER = "GENDER";
    public static String MOBILE = "MOBILE";
    public static String OTP = "OTP";
    public static String IS_BLOCK = "isblock";
    public static String EMAil = "EMAil";
    public static String AUTH_TOKEN = "AUTH_TOKEN";
    public static String GETINTENT_USER = "GETINTENT_USER";
    public static String GETINTENT_VIDEO = "GETINTENT_VIDEO";

    public static String GET_NOTIFICATION_INTENT_USER = "GET_NOTIFICATION_INTENT_USER";
    public static String GET_NOTIFICATION_INTENT_VIDEO = "GET_NOTIFICATION_INTENT_VIDEO";
//    public static String GET_NOTIFICATION_FRAG_INTENT_VIDEO = "GET_NOTIFICATION_FRAG_INTENT_VIDEO";

    public static String PERMISSION_PREFERENCE = "PERMISSION_PREFERENCE";
    public static String PERMISSION_PREFERENCE_MICRO = "PERMISSION_PREFERENCE_MICRO";


    public static String Reg_lat = "Reg_lat";
    public static String Reg_lng = "Reg_lng";
    public static String Reg_deviceName = "Reg_deviceName";
    public static String Reg_deviceModel = "Reg_deviceModel";
    public static String Reg_deviceVersion = "deviceVersion";
    public static String Reg_osType = "osType";
    public static String Reg_imeiNumber = "imeiNumber";
    public static String Reg_dateZone = "dateZone";
    public static String Reg_timeZone = "Reg_timeZone";
    public static String SHOW_PERMISSION = "true";
    public static String HOME_INTRO = "HOME_INTRO";
    public static String MEDLEY_INTRO = "MEDLEY_INTRO";
    public static String MEDLEY_VIDEO_INTRO = "MEDLEY_VIDEO_INTRO";
    public static String MEDLEY_QR_INTRO = "MEDLEY_QR_INTRO";
    public static String NOIFICATION_COUNT = "NOIFICATION_COUNT";
    public static String POSTWALL_REFRESH_COUNT = "POSTWALL_REFRESH_COUNT";
    public static String CHAT_NOTIFICATION_COUNT = "CHAT_NOTIFICATION_COUNT";
    public static String BASE_URL_DUMMY = "BASE_URL_DUMMY";
    public static String UNIQUE_KEY = "UNIQUE_KEY";


    public static String Video_Post_Active = "VideoPostActive";
    public static String Back = "back";


    public static void setSharedPreferenceString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setSharedPreferenceBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public static String getSharedPreferenceString(Context context, String key) {
        if (context != null) {
            SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
            return settings.getString(key, "");
        }
        return "";
    }

    public static boolean getSharedPreferenceBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getBoolean(key, false);
    }


    public static void setSharedPreferenceLong(Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static Long getSharedPreferenceLong(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getLong(key, 0L);
    }


    public static void setSharedPreferenceInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static Integer getSharedPreferenceInt(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getInt(key, 0);
    }

    public  static void removeSharedPrefrenceString(Context context, String key){
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.apply();

    }

}
