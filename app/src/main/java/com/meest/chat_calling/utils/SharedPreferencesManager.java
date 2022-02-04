package com.meest.chat_calling.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import com.meest.R;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SharedPreferencesManager {


    //this will contains the app preferences
    private static SharedPreferences mSharedPref;
    //this will contains the users settings

    //check what user is enabled (video,audio,images) for every state (wifi,cellular,roaming)
    private static Set<String> defaultWifiSet;
    private static Set<String> defaultCellularSet;
    private static Set<String> defaultRoamingSet;


    public static void setContactSynced(boolean isSynced) {
        mSharedPref.edit().putBoolean("isSynced", isSynced).apply();
    }

    //this will return if user has enabled auto download for certain network type and for certain media type

    //get values from values array by given media type

    //save user status locally to show it his profile
    public static void saveMyStatus(String status) {
        mSharedPref.edit().putString("status", status).apply();
    }

    //save user username locally to show it his profile
    public static void saveMyUsername(String username) {
        mSharedPref.edit().putString("username", username).apply();
    }

    //save user photo path locally to show it his profile
    public static void saveMyPhoto(String path) {
        mSharedPref.edit().putString("user_image", path).apply();
    }

    //save user phone number locally to show it his profile
    public static void savePhoneNumber(String phoneNumber) {
        mSharedPref.edit().putString("phone_number", phoneNumber).apply();
    }

    public static void setAgreedToPrivacyPolicy(Boolean bool) {
        mSharedPref.edit().putBoolean("agreed_to_privacy_policy", bool).apply();
    }

    public static Boolean hasAgreedToPrivacyPolicy() {
        return mSharedPref.getBoolean("agreed_to_privacy_policy", false);
    }
    //Change  By @sanket
    public static void save_Language(String language) {
        mSharedPref.edit().putString("Language", language).apply();
    }
    public static String getLanguage() {
        return mSharedPref.getString("Language", "");
    }


    public static void save_font(String font) {
        mSharedPref.edit().putString("Font", font).apply();
    }
    public static String getfont() {
        return mSharedPref.getString("Font", "");
    }



    public static String getUserName() {
        return mSharedPref.getString("username", "");
    }

    public static String getStatus() {
        return mSharedPref.getString("status", "");
    }

    public static String getPhoneNumber() {
        return mSharedPref.getString("phone_number", "");
    }

    public static String getMyPhoto() {
        return mSharedPref.getString("user_image", "");
    }


    //check if user enabled vibration for notifications
    public static boolean isVibrateEnabled() {
        return mSharedPref.getBoolean("notifications_new_message_vibrate", false);
    }

    //get notification ringtone
    public static Uri getRingtone() {
        return Uri.parse(mSharedPref.getString("notifications_new_message_ringtone", "content://settings/system/notification_sound"));
    }

    //get notification ringtone
    public static void setRingtone(String ringtone) {
        mSharedPref.edit().putString("notifications_new_message_ringtone", ringtone).apply();
    }

    //check if user enabled notifications
    public static boolean isNotificationEnabled() {
        return mSharedPref.getBoolean("notifications_new_message", true);
    }


    //check if user info is saved when launch the app for first time
    public static boolean isUserInfoSaved() {
        return mSharedPref.getBoolean("is_userInfo_saved", false);
    }

    public static void setUserInfoSaved(boolean bool) {
        mSharedPref.edit().putBoolean("is_userInfo_saved", bool).apply();
    }

    //save country code to use it late when formatting the numbers
    public static void saveCountryCode(String phoneNumber) {
        mSharedPref.edit().putString("ccode", phoneNumber).apply();
    }

    public static void saveMyThumbImg(String thumbImg) {
        mSharedPref.edit().putString("thumbImg", thumbImg).apply();
    }

    public static String getThumbImg() {
        return mSharedPref.getString("thumbImg", "");
    }

    public static String getCountryCode() {
        return mSharedPref.getString("ccode", "");
    }


    // set notification token as saved to prevent resending it to server
    public static void setTokenSaved(boolean bool) {
        mSharedPref.edit().putBoolean("token_sent", bool).commit();
    }

    public static boolean isTokenSaved() {
        return mSharedPref.getBoolean("token_sent", false);
    }

    public static void setFetchUserGroupsSaved(boolean b) {
        mSharedPref.edit().putBoolean("fetch_user_groups_saved", b).apply();
    }

    public static boolean isFetchedUserGroups() {
        return mSharedPref.getBoolean("fetch_user_groups_saved", false);
    }

    public static void setAppVersionSaved(boolean b) {
        mSharedPref.edit().putBoolean("is_app_ver_saved", b).apply();
    }

    public static boolean isAppVersionSaved() {
        return mSharedPref.getBoolean("is_app_ver_saved", false);
    }

    public static void setLastSeenState(int lastSeenState) {
        mSharedPref.edit().putInt("lastSeenState", lastSeenState).apply();
    }

    public static int getLastSeenState() {
        return mSharedPref.getInt("lastSeenState", 0);
    }


    public static void setWallpaperPath(String value) {
        mSharedPref.edit().putString("wallpaperPath", value).apply();
    }

    public static String getWallpaperPath() {
        return mSharedPref.getString("wallpaperPath", "");
    }


    public static void setLastBackup(long value) {
        mSharedPref.edit().putLong("lastBackup", value).apply();
    }

    public static long getLastBackup() {
        return mSharedPref.getLong("lastBackup", -1);
    }


    //used to determine whether the UID,and Phone number were saved
    public static void setCurrentUserInfoSaved(boolean value) {
        mSharedPref.edit().putBoolean("currentUserInfoSaved", value).apply();
    }

    public static boolean isCurrentUserInfoSaved() {
        return mSharedPref.getBoolean("currentUserInfoSaved", false);
    }

    public static void setDoNotShowBatteryOptimizationAgain(boolean value) {
        mSharedPref.edit().putBoolean("doNotShowBatteryOptimizationAgain", value).apply();
    }

    public static boolean isDoNotShowBatteryOptimizationAgain() {
        return mSharedPref.getBoolean("doNotShowBatteryOptimizationAgain", false);
    }

    public static boolean isDeletedUnfetchedMessage() {
        return mSharedPref.getBoolean("isDeletedUnfetchedMessage", false);
    }

    public static void setDeletedUnfetchedMessage(boolean bool) {
        mSharedPref.edit().putBoolean("isDeletedUnfetchedMessage", bool).apply();
    }

    public static void setLastActive(long currentTimeMillis) {
        mSharedPref.edit().putLong("last_active", currentTimeMillis).apply();
    }

    public static long getLastActive() {
        return mSharedPref.getLong("last_active", 0);
    }



    synchronized public static void init(Context context) {
        if (mSharedPref == null)
            mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);


        if (defaultWifiSet == null)
            defaultWifiSet = new HashSet<>(Arrays.asList(context.getResources().getStringArray(R.array.autodownload_wifi_defaults)));

        if (defaultCellularSet == null)
            defaultCellularSet = new HashSet<>(Arrays.asList(context.getResources().getStringArray(R.array.autodownload_cellular_defaults)));

        if (defaultRoamingSet == null)
            defaultRoamingSet = new HashSet<>(Arrays.asList(context.getResources().getStringArray(R.array.autodownload_roaming_defaults)));

//        if (key_autodownload_wifi == null) {
//            key_autodownload_wifi = context.getResources().getString(R.string.key_autodownload_wifi);
//        }
//
//        if (key_autodownload_cellular == null) {
//            key_autodownload_cellular = context.getResources().getString(R.string.key_autodownload_cellular);
//        }
//        if (key_autodownload_roaming == null) {
//            key_autodownload_roaming = context.getResources().getString(R.string.key_autodownload_roaming);
//        }

    }


}
