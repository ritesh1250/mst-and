package com.meest.social.socialViewModel.viewModel.splashViewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.meest.MainActivity;
import com.meest.databinding.SplashScreenActivityModelBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.social.socialViewModel.view.login.LoginSignUp;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.social.socialViewModel.view.splash.SplashScreenVideo;
import com.meest.svs.activities.VideoOtherUserActivity;
import com.meest.svs.activities.VideoPlayerActivity;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;

import org.json.JSONObject;

import java.util.Locale;
import java.util.Map;

import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_AS;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_BN;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_EN;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_GU;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_HI;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_KN;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_ML;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_MR;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_OD;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_PA;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_TA;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_TE;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_UR;

public class SplashViewModel extends ViewModel {
    private Context context;
    public Activity activity;
    SplashScreenActivityModelBinding binding;
    SessionManager sessionManager;
    Vibrator v;

    public SplashViewModel(Activity activity, Context context, SplashScreenActivityModelBinding binding) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;
        sessionManager = new SessionManager(activity);
        v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void localSaveValue() {
        String lang_id = SocialPrefrences.getLang(context);
        if (lang_id.equals(SOCIAL_LAN_EN)) {
            //7254beda-a1d7-4ac2-a870-53e87da8e1ac
            setAppLocale(SOCIAL_LAN_EN);
            Log.v("Splash Screen lang : ", lang_id);
        } else if (lang_id.equals(SOCIAL_LAN_HI)) {
            //b0847af4-5f16-4bee-bb9a-1f6bdc79a5d1
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale(SOCIAL_LAN_HI);
        } else if (lang_id.equals(SOCIAL_LAN_UR)) {
            //7bf31330-c8c2-4ece-9d76-5357c463e290
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale(SOCIAL_LAN_UR);
        } else if (lang_id.equals(SOCIAL_LAN_PA)) {
            //6adc98c2-9b04-414c-9cde-683139ed34e1
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale(SOCIAL_LAN_PA);
        } else if (lang_id.equals(SOCIAL_LAN_TA)) {
            //bf267bbf-ea14-4005-8fa1-ba74251f24c8
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale(SOCIAL_LAN_TA);
        } else if (lang_id.equals(SOCIAL_LAN_KN)) {
            //d5943bdb-b8dd-41fc-9123-b46a8d79596f
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale(SOCIAL_LAN_KN);
        } else if (lang_id.equals(SOCIAL_LAN_BN)) {
            //568c0b05-23cb-4921-80e4-727878a7f6c2
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale(SOCIAL_LAN_BN);
        } else if (lang_id.equals(SOCIAL_LAN_ML)) {
            //7b6381e1-e21b-49af-a9f6-ba03f1173d88
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale(SOCIAL_LAN_ML);
        } else if (lang_id.equals(SOCIAL_LAN_OD)) {
            //d2b8f570-b7e5-4691-ad48-2082db576350
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale(SOCIAL_LAN_OD);
        } else if (lang_id.equals(SOCIAL_LAN_AS)) {
            //e4de2084-7d3b-46fc-9b2c-3a8b140f210c
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale(SOCIAL_LAN_AS);
        } else if (lang_id.equals(SOCIAL_LAN_MR)) {
            //ec2ab884-c65e-43fc-9a3a-830e08b37803
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale(SOCIAL_LAN_MR);
        } else if (lang_id.equals(SOCIAL_LAN_TE)) {
            //ec44519d-d7ca-4317-95cb-6c708b9b3d16
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale(SOCIAL_LAN_TE);
        } else if (lang_id.equals(SOCIAL_LAN_GU)) {
            //f96662f6-c67a-4be4-98a6-398daf0128ed
            Log.v("Splash Screen lang : ", lang_id);
            setAppLocale(SOCIAL_LAN_GU);
        }
    }

    public void setAppLocale(String localeCode) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(localeCode.toLowerCase()));
        resources.updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(localeCode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public void handleNotification(Map<String, String> data) {
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
                Intent intent = new Intent(context, OtherUserAccount.class);// open followers in profile activity
                intent.putExtra("id_push", id_push);
                intent.putExtra("from_push", "1");
                intent.putExtra("userId", userId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            } else if (!userId.equals(null) && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("PostLike")) {
                String postId = jsonObject.getString("postId");
                Intent intent = new Intent(context, NotificationSocialFeedActivity.class);// open followers in profile activity
                intent.putExtra("id_push", id_push);
                intent.putExtra("from_push", "1");
                intent.putExtra("userId", userId);
                intent.putExtra("postId", postId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            } else if (!userId.equals(null) && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("PostComment")) {
                String postId = jsonObject.getString("postId");
                Intent intent = new Intent(context, NotificationSocialFeedActivity.class);// open followers in profile activity
                intent.putExtra("id_push", id_push);
                intent.putExtra("from_push", "1");
                intent.putExtra("userId", userId);
                intent.putExtra("postId", postId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            } else if (!userId.equals(null) && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("PostTag")) {
                String postId = jsonObject.getString("postId");
                Intent intent = new Intent(context, NotificationSocialFeedActivity.class);// open followers in profile activity
                intent.putExtra("id_push", id_push);
                intent.putExtra("from_push", "1");
                intent.putExtra("userId", userId);
                intent.putExtra("postId", postId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            } else if (!userId.equals(null) && type.equalsIgnoreCase("svs") && notificationType.equalsIgnoreCase("PostComment")) {
                String postId = jsonObject.getString("postId");
                Intent intent = new Intent(context, VideoPlayerActivity.class);// open followers in profile activity
                intent.putExtra("id_push", id_push);
                intent.putExtra("from_push", "1");
                intent.putExtra("userId", userId);
                intent.putExtra("postId", postId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            } else if (!userId.equals(null) && type.equalsIgnoreCase("svs") && notificationType.equalsIgnoreCase("PostLike")) {
                String postId = jsonObject.getString("postId");
                Intent intent = new Intent(context, VideoPlayerActivity.class);// open followers in profile activity
                intent.putExtra("id_push", id_push);
                intent.putExtra("from_push", "1");
                intent.putExtra("userId", userId);
                intent.putExtra("postId", postId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            } else if (!userId.equals(null) && type.equalsIgnoreCase("svs") && notificationType.equalsIgnoreCase("Follower")) {
                Intent intent = new Intent(context, VideoOtherUserActivity.class);// open followers in profile activity
                intent.putExtra("id_push", id_push);
                intent.putExtra("from_push", "1");
                intent.putExtra("userId", userId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVideoView(Uri video) {
        binding.videoView.setVideoURI(video);
        binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                Global.checkUniqueKey(context);
                if (!SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.GETINTENT_VIDEO).isEmpty()) {
                    if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {

                        Global.accessToken = "";
                        Global.userId = sessionManager.getUser().getData().getUserId();
                        Intent intent = new Intent(context, MainVideoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, SplashScreenVideo.class);
                        intent.putExtra("whereFrom", "skip");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                    activity.finish();
                } else if (!SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.GET_NOTIFICATION_INTENT_VIDEO).isEmpty()) {
                    if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                        Global.accessToken = "";
                        Global.userId = sessionManager.getUser().getData().getUserId();
                        Intent intent = new Intent(context, MainVideoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, SplashScreenVideo.class);
                        intent.putExtra("whereFrom", "skip");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    }
                    activity.finish();
                } else if (SharedPrefreances.getSharedPreferenceString(context, "login").equalsIgnoreCase("1")) {
                    if (SharedPrefreances.getSharedPreferenceString(context, "ch_new").equalsIgnoreCase("1")) {
                        Intent intent = new Intent(context, LoginSignUp.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                        activity.finish();
                    } else {
                        if (SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.WHEREFROM).equalsIgnoreCase("Video")) {
                            if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {

                                Global.accessToken = "";
                                Global.userId = sessionManager.getUser().getData().getUserId();
                                Intent intent = new Intent(context, MainVideoActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                            } else {
                                Intent intent = new Intent(context, SplashScreenVideo.class);
                                intent.putExtra("whereFrom", "register");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                            }
                        } else {
                            if (!SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.GETINTENT_USER).isEmpty()) {
                                if (SocialPrefrences.getisLogin(binding.getRoot().getContext())) {

                                    Global.accessToken = "";
                                    Global.userId = sessionManager.getUser().getData().getUserId();
                                    Intent intent = new Intent(context, MainVideoActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                } else {
                                    Intent intent = new Intent(context, SplashScreenVideo.class);
                                    intent.putExtra("whereFrom", "register");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                }
                            } else {
                                if (SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.GET_NOTIFICATION_INTENT_USER).isEmpty()) {
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(intent);
                                } else {
                                    if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {

                                        Global.accessToken = "";
                                        Global.userId = sessionManager.getUser().getData().getUserId();
                                        Intent intent = new Intent(context, MainVideoActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        context.startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(context, SplashScreenVideo.class);
                                        intent.putExtra("whereFrom", "register");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        context.startActivity(intent);
                                    }
                                }

                            }
                        }
                        activity.finish();
                    }
                } else {
                    Intent intent = new Intent(context, LoginSignUp.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    activity.finish();
                }
                vibrateOn(v);
            }
        });
        vibrateOn(v);
        binding.videoView.start();
    }

    private void vibrateOn(Vibrator vibrator) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }

}
