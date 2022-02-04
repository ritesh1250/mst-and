package com.meest.social.socialViewModel.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.meest.social.socialViewModel.utils.ParameterConstant.SocialPrefrences.SOCIAL_PREFRENCES;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_ANDROID_VERSION;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_DEVICE_ID;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_DEVICE_MODEL;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_DEVICE_NAME;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_FIREBASE_TOKEN;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_IMEI_ID;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_IS_INTEREST;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_IS_REGISTER;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LANG;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LATITUDE;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LONGITUDE;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_MOBILE_NUMBER;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_TIME_ZONE;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_USER_ID;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL__TOKEN;

public class SocialPrefrences {
    private static final String MyPREFERENCES = SOCIAL_PREFRENCES;

    public static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public static void setUserId(Context context, String value) {
        getSharedPreference(context).edit().putString(SOCIAL_USER_ID, value).apply();
    }

    public static String getUserId(Context context) {
        return getSharedPreference(context).getString(SOCIAL_USER_ID, "");
    }


    public static void setSaveLang(Context context, String value) {
        getSharedPreference(context).edit().putString(SOCIAL_LANG, value).apply();
    }

    public static String getLang(Context context) {
        return getSharedPreference(context).getString(SOCIAL_LANG, "");
    }

    public static void setIMEIID(Context context, String value) {
        getSharedPreference(context).edit().putString(SOCIAL_IMEI_ID, value).apply();
    }

    public static String getIMEIID(Context context) {
        return getSharedPreference(context).getString(SOCIAL_IMEI_ID, "");
    }

    public static void setCurrentTimeZone(Context context, String value) {
        getSharedPreference(context).edit().putString(SOCIAL_TIME_ZONE, value).apply();
    }

    public static String getCurrentTimeZone(Context context) {
        return getSharedPreference(context).getString(SOCIAL_TIME_ZONE, "");
    }

    public static void setDeviceModel(Context context, String value) {
        getSharedPreference(context).edit().putString(SOCIAL_DEVICE_MODEL, value).apply();
    }

    public static String getDeviceModel(Context context) {
        return getSharedPreference(context).getString(SOCIAL_DEVICE_MODEL, "");
    }

    public static void setDeviceId(Context context, String value) {
        getSharedPreference(context).edit().putString(SOCIAL_DEVICE_ID, value).apply();
    }

    public static String getDeviceId(Context context) {
        return getSharedPreference(context).getString(SOCIAL_DEVICE_ID, "");
    }

    public static void setDeviceName(Context context, String value) {
        getSharedPreference(context).edit().putString(SOCIAL_DEVICE_NAME, value).apply();
    }

    public static String getDeviceName(Context context) {
        return getSharedPreference(context).getString(SOCIAL_DEVICE_NAME, "");
    }

    public static void setAndroidVersion(Context context, String value) {
        getSharedPreference(context).edit().putString(SOCIAL_ANDROID_VERSION, value).apply();
    }

    public static String getAndroidVersion(Context context) {
        return getSharedPreference(context).getString(SOCIAL_ANDROID_VERSION, "");
    }

    public static void setLongitude(Context context, String value) {
        getSharedPreference(context).edit().putString(SOCIAL_LONGITUDE, value).apply();
    }

    public static String getLongitude(Context context) {
        return getSharedPreference(context).getString(SOCIAL_LONGITUDE, "");
    }

    public static void setLatitude(Context context, String value) {
        getSharedPreference(context).edit().putString(SOCIAL_LATITUDE, value).apply();
    }

    public static String getLatitude(Context context) {
        return getSharedPreference(context).getString(SOCIAL_LATITUDE, "");
    }

    public static void setFireBaseToken(Context context, String value) {
        getSharedPreference(context).edit().putString(SOCIAL_FIREBASE_TOKEN, value).apply();
    }

    public static String getFireBaseToken(Context context) {
        return getSharedPreference(context).getString(SOCIAL_FIREBASE_TOKEN, "");
    }

/*    public static void setToken(Context context, String value) {
        getSharedPreference(context).edit().putString(SOCIAL__TOKEN, value).apply();
    }

    public static String getToken(Context context) {
        return getSharedPreference(context).getString(SOCIAL__TOKEN, "");
    }*/

    public static void setMobileNumber(Context context, String value) {
        getSharedPreference(context).edit().putString(SOCIAL_MOBILE_NUMBER, value).apply();
    }

    public static String getMobileNumber(Context context) {
        return getSharedPreference(context).getString(SOCIAL_MOBILE_NUMBER, "");
    }

    public static void setisVerified(Context context, boolean value) {
        getSharedPreference(context).edit().putBoolean(SOCIAL_IS_REGISTER, value).apply();
    }

    public static boolean getisVerified(Context context) {
        return getSharedPreference(context).getBoolean(SOCIAL_IS_REGISTER, false);
    }

    public static void setisInterest(Context context, boolean value) {
        getSharedPreference(context).edit().putBoolean(SOCIAL_IS_INTEREST, value).apply();
    }

    public static boolean getisInterest(Context context) {
        return getSharedPreference(context).getBoolean(SOCIAL_IS_INTEREST, false);
    }

    //set login
    public static void setisLogin(Context context, boolean value) {
        getSharedPreference(context).edit().putBoolean(SOCIAL_IS_REGISTER, value).apply();
    }

    public static boolean getisLogin(Context context) {
        return getSharedPreference(context).getBoolean(SOCIAL_IS_REGISTER, false);
    }

    public interface SocialKeyPrams {
        String SOCIAL_LAN_EN = "en";
        String SOCIAL_LAN_HI = "hi";
        String SOCIAL_LAN_UR = "ur";
        String SOCIAL_LAN_PA = "pa";
        String SOCIAL_LAN_TA = "ta";
        String SOCIAL_LAN_KN = "kn";
        String SOCIAL_LAN_BN = "bn";
        String SOCIAL_LAN_ML = "ml";
        String SOCIAL_LAN_OD = "od";
        String SOCIAL_LAN_AS = "as";
        String SOCIAL_LAN_MR = "mr";
        String SOCIAL_LAN_TE = "te";
        String SOCIAL_LAN_GU = "gu";
        String SOCIAL_USER_ID = "userid";
        String SOCIAL_LATITUDE = "latitude";
        String SOCIAL_LONGITUDE = "longitude";
        String SOCIAL_LANG = "socialLang";
        String SOCIAL_IMEI_ID = "socialIMEIID";
        String SOCIAL_TIME_ZONE = "socialTimeZone";
        String SOCIAL_DEVICE_MODEL = "socialdeviceModel";
        String SOCIAL_DEVICE_ID = "socialdeviceId";
        String SOCIAL_DEVICE_NAME = "socialdeviceName";
        String SOCIAL_ANDROID_VERSION = "socialandroidVersion";
        String SOCIAL_FIREBASE_TOKEN = "firebaseToken";
        String SOCIAL__TOKEN = "token";
        String SOCIAL_MOBILE_NUMBER = "mobileNumber";
        String SOCIAL_IS_REGISTER = "register";
        String SOCIAL_IS_INTEREST = "interest";
        String SOCIAL_IS_LOGIN = "is_login";


    }
}
