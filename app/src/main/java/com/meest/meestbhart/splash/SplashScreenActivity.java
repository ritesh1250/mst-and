package com.meest.meestbhart.splash;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.meest.MainActivity;
import com.meest.R;
import com.meest.chat_calling.utils.SharedPreferencesManager;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.social.socialViewModel.view.login.LoginSignUp;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.svs.activities.VideoOtherUserActivity;
import com.meest.svs.activities.VideoPlayerActivity;
import com.meest.videomvvmmodule.SplashScreenVideoActivity;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {

    VideoView videoView;
    Vibrator v;
    public SessionManager sessionManager = null;
    List<HttpCookie> cookieList;
    OkHttpClient client = new OkHttpClient();

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getStringExtra(Const.userId) != null && !getIntent().getStringExtra(Const.userId).isEmpty()) {
            SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.GET_NOTIFICATION_INTENT_USER, getIntent().getStringExtra(Const.userId));
            setIntent(new Intent());
        }
        if (getIntent().getStringExtra(Const.videoId) != null && !getIntent().getStringExtra(Const.videoId).isEmpty()) {
            SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.GET_NOTIFICATION_INTENT_VIDEO, getIntent().getStringExtra(Const.videoId));
            setIntent(new Intent());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        onNewIntent(getIntent());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sessionManager = new SessionManager(this);
        String lang_id = SharedPreferencesManager.getLanguage();
        if (lang_id.equals("en")) {
            //7254beda-a1d7-4ac2-a870-53e87da8e1ac
            setAppLocale("en");
            Log.v("Splash Screen lang : ", lang_id);
        } else if (lang_id.equals("hi")) {
            //b0847af4-5f16-4bee-bb9a-1f6bdc79a5d1
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale("hi");
        } else if (lang_id.equals("ur")) {
            //7bf31330-c8c2-4ece-9d76-5357c463e290
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale("ur");
        } else if (lang_id.equals("pa")) {
            //6adc98c2-9b04-414c-9cde-683139ed34e1
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale("pa");
        } else if (lang_id.equals("ta")) {
            //bf267bbf-ea14-4005-8fa1-ba74251f24c8
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale("ta");
        } else if (lang_id.equals("kn")) {
            //d5943bdb-b8dd-41fc-9123-b46a8d79596f
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale("kn");
        } else if (lang_id.equals("bn")) {
            //568c0b05-23cb-4921-80e4-727878a7f6c2
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale("bn");
        } else if (lang_id.equals("ml")) {
            //7b6381e1-e21b-49af-a9f6-ba03f1173d88
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale("ml");
        } else if (lang_id.equals("od")) {
            //d2b8f570-b7e5-4691-ad48-2082db576350
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale("od");
        } else if (lang_id.equals("as")) {
            //e4de2084-7d3b-46fc-9b2c-3a8b140f210c
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale("as");
        } else if (lang_id.equals("mr")) {
            //ec2ab884-c65e-43fc-9a3a-830e08b37803
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale("mr");
        } else if (lang_id.equals("te")) {
            //ec44519d-d7ca-4317-95cb-6c708b9b3d16
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale("te");
        } else if (lang_id.equals("gu")) {
            //f96662f6-c67a-4be4-98a6-398daf0128ed
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale("gu");
        }
        setContentView(R.layout.splash_screen_activity);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // handling notification
        if (getIntent().getExtras() != null && SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN).length() > 0) {
            HashMap<String, String> data = new HashMap<>();
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                data.put(key, value);
                Log.d("harsh", "Key: " + key + " Value: " + value);
            }
            if (data.size() > 2) {
                handleNotification(data);
            }
        }

        videoView = findViewById(R.id.videoView);
        Log.w("token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), "token"));

        getDynamikLink();

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hello);
        videoView.setVideoURI(video);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                Global.checkUniqueKey(SplashScreenActivity.this);
                if (!SharedPrefreances.getSharedPreferenceString(SplashScreenActivity.this, SharedPrefreances.GETINTENT_VIDEO).isEmpty()) {
                    if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                        Global.accessToken = "";
                        Global.userId = sessionManager.getUser().getData().getUserId();
                        Intent intent = new Intent(SplashScreenActivity.this, MainVideoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, SplashScreenVideoActivity.class);
                        intent.putExtra("whereFrom", "skip");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    finish();
                } else if (!SharedPrefreances.getSharedPreferenceString(SplashScreenActivity.this, SharedPrefreances.GET_NOTIFICATION_INTENT_VIDEO).isEmpty()) {
                    if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                        Global.accessToken = "";
                        Global.userId = sessionManager.getUser().getData().getUserId();
                        Intent intent = new Intent(SplashScreenActivity.this, MainVideoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, SplashScreenVideoActivity.class);
                        intent.putExtra("whereFrom", "skip");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    finish();
                } else if (SharedPrefreances.getSharedPreferenceString(SplashScreenActivity.this, "login").equalsIgnoreCase("1")) {
                    if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(), "ch_new").equalsIgnoreCase("1")) {
                        Intent intent = new Intent(SplashScreenActivity.this, LoginSignUp.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.WHEREFROM).equalsIgnoreCase("Video")) {
                            if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                                Global.accessToken = "";
                                Global.userId = sessionManager.getUser().getData().getUserId();
                                Intent intent = new Intent(SplashScreenActivity.this, MainVideoActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(SplashScreenActivity.this, SplashScreenVideoActivity.class);
                                intent.putExtra("whereFrom", "register");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        } else {
                            if (!SharedPrefreances.getSharedPreferenceString(SplashScreenActivity.this, SharedPrefreances.GETINTENT_USER).isEmpty()) {
                                if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                                    Global.accessToken = "";
                                    Global.userId = sessionManager.getUser().getData().getUserId();
                                    Intent intent = new Intent(SplashScreenActivity.this, MainVideoActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(SplashScreenActivity.this, SplashScreenVideoActivity.class);
                                    intent.putExtra("whereFrom", "register");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            } else {
                                if (SharedPrefreances.getSharedPreferenceString(SplashScreenActivity.this, SharedPrefreances.GET_NOTIFICATION_INTENT_USER).isEmpty()) {
                                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                                        Global.accessToken = "";
                                        Global.userId = sessionManager.getUser().getData().getUserId();
                                        Intent intent = new Intent(SplashScreenActivity.this, MainVideoActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(SplashScreenActivity.this, SplashScreenVideoActivity.class);
                                        intent.putExtra("whereFrom", "register");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }

                            }
                        }
                        finish();
                    }
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginSignUp.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                vibrateOn(v);
            }
        });
        vibrateOn(v);
        videoView.start();
    }

    private void vibrateOn(Vibrator vibrator) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }

    private void handleNotification(Map<String, String> data) {
        String type = null;
        try {
            Log.v("harsh", "inTry clause");
            JSONObject jsonObject = new JSONObject(data);
            type = jsonObject.getString("type");
            Log.v("harsh", "for feed/svs clause");

            String userId = jsonObject.getString("userId");
            String notificationType = jsonObject.getString("notificationType");
            String id_push = jsonObject.getString("userId");
            String chanel_name = "";

            if (type.equalsIgnoreCase("goLive"))
                chanel_name = jsonObject.getString("channel_Name");
            if (!userId.equals(null) && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("Follower")) {
                Intent intent = new Intent(getApplicationContext(), OtherUserAccount.class);// open followers in profile activity
                intent.putExtra("id_push", id_push);
                intent.putExtra("from_push", "1");
                intent.putExtra("userId", userId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else if (!userId.equals(null) && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("PostLike")) {
                String postId = jsonObject.getString("postId");
                Intent intent = new Intent(getApplicationContext(), NotificationSocialFeedActivity.class);// open followers in profile activity
                intent.putExtra("id_push", id_push);
                intent.putExtra("from_push", "1");
                intent.putExtra("userId", userId);
                intent.putExtra("postId", postId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else if (!userId.equals(null) && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("PostComment")) {
                String postId = jsonObject.getString("postId");
                Intent intent = new Intent(getApplicationContext(), NotificationSocialFeedActivity.class);// open followers in profile activity
                intent.putExtra("id_push", id_push);
                intent.putExtra("from_push", "1");
                intent.putExtra("userId", userId);
                intent.putExtra("postId", postId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else if (!userId.equals(null) && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("PostTag")) {
                String postId = jsonObject.getString("postId");
                Intent intent = new Intent(getApplicationContext(), NotificationSocialFeedActivity.class);// open followers in profile activity
                intent.putExtra("id_push", id_push);
                intent.putExtra("from_push", "1");
                intent.putExtra("userId", userId);
                intent.putExtra("postId", postId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else if (!userId.equals(null) && type.equalsIgnoreCase("svs") && notificationType.equalsIgnoreCase("PostComment")) {
                String postId = jsonObject.getString("postId");
                Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);// open followers in profile activity
                intent.putExtra("id_push", id_push);
                intent.putExtra("from_push", "1");
                intent.putExtra("userId", userId);
                intent.putExtra("postId", postId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else if (!userId.equals(null) && type.equalsIgnoreCase("svs") && notificationType.equalsIgnoreCase("PostLike")) {
                String postId = jsonObject.getString("postId");
                Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);// open followers in profile activity
                intent.putExtra("id_push", id_push);
                intent.putExtra("from_push", "1");
                intent.putExtra("userId", userId);
                intent.putExtra("postId", postId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else if (!userId.equals(null) && type.equalsIgnoreCase("svs") && notificationType.equalsIgnoreCase("Follower")) {
                Intent intent = new Intent(getApplicationContext(), VideoOtherUserActivity.class);// open followers in profile activity
                intent.putExtra("id_push", id_push);
                intent.putExtra("from_push", "1");
                intent.putExtra("userId", userId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAppLocale(String localeCode) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(localeCode.toLowerCase()));
        resources.updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(localeCode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }

    private void getDynamikLink() {
        SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.GETINTENT_USER, "");
        SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.GETINTENT_VIDEO, "");
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    if (pendingDynamicLinkData != null) {
                        Uri deepLink = pendingDynamicLinkData.getLink();
                        Log.d("TAG", "==========onSuccess: " + deepLink);
                        Log.d("TAG", "==========onSuccess: " + deepLink.getQueryParameter("user"));

                        Log.d("TAG", "==========onSuccessVideo: " + deepLink.getQueryParameter("video"));
                        Log.d("TAG", "==========onSuccessPost: " + deepLink.getQueryParameter("post"));
                        Log.d("TAG", "==========onSuccessUserid: " + deepLink.getQueryParameter("userid"));
                        if (deepLink.getQueryParameter("post") != null && deepLink.getQueryParameter("userid") != null) {
                            Intent intent = new Intent(SplashScreenActivity.this, NotificationSocialFeedActivity.class);
                            intent.putExtra("userId", deepLink.getQueryParameter("userid"));
                            intent.putExtra("postId", deepLink.getQueryParameter("post"));
                            startActivity(intent);
                        }
                        SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.GETINTENT_USER, deepLink.getQueryParameter("user"));
                        SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.GETINTENT_VIDEO, deepLink.getQueryParameter("video"));
                    }
                })
                .addOnFailureListener(this, e -> Log.w("TAG", "getDynamicLink:onFailure", e))
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null) {
                        setIntent(new Intent());
                    }
                });
    }
}