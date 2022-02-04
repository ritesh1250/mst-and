package com.meest.Services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.meest.metme.jitsi.IncomingCallActivity;
import com.meest.Fragments.HomeFragments;
import com.meest.MainActivity;
import com.meest.Meeast;
import com.meest.chat_calling.utils.SharedPreferencesManager;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.R;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.metme.ChatBoatActivity;
import com.meest.metme.ChatRequestActivity;
import com.meest.metme.model.ChatUnreadCountResponse;
import com.meest.metme.service.HeadsUpNotificationService;
import com.meest.social.socialViewModel.view.SplashScreen;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.svs.activities.NotificationSvsActivity;
import com.meest.svs.activities.VideoOtherUserActivity;
import com.meest.utils.IncomingCall;
import com.meest.utils.ParameterConstants;
import com.meest.utils.goLiveUtils.CommonUtils;
import com.meest.videomvvmmodule.model.NotificationModel;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static final String CHANNEL_ID = "channel_id";
    private static final String PRIORITY_CHANNEL_ID = "pr_channel_id";
    private static final String CALLERID_DATA_KEY = "callerId";
    private static final String NOTIF_TITLE_TEXT = "Someone is calling";
    private static final String NOTIF_BODY_TEXT = "Tap to pick up";
    private static RemoteViews contentView;
    private static Handler mNotifCancelHandler;
    final long[] VIBRATE_PATTERN = {0, 500};
    String type;
    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    JSONObject jsonObject;
    String userId = "";
    String notificationType = "";
    String id_push = "";
    Ringtone r;
    Uri notification;
    Activity mContext;
    String username, fullName, profilePicture;
    NotificationCompat.Builder mBuilder;
    private CompositeDisposable disposables = new CompositeDisposable();
    private NotificationManagerCompat notificationManagerCompat;
//===============
Socket mSocket;
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static void isAppRunning() {
        System.out.println("http app " + ParameterConstants.APP_STATE);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

//        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
//
//        Map<String, String> map = new HashMap<>();
//        map.put("Accept", "application/json");
//        map.put("Content-Type", "application/json");
//        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
//
//        HashMap<String, String> body = new HashMap<>();
//        body.put("fcmToken", s);
//        Call<ApiResponse> call = webApi.updateUserProfile(map, body);
//        call.enqueue(new Callback<ApiResponse>() {
//            @Override
//            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse> call, Throwable t) {
//                onNewToken(s);
//            }
//        });
    }

    @SuppressLint("RemoteViewLayout")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        System.out.println("http fcm message::  " + remoteMessage);

        if (remoteMessage.getData() != null) {
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            try {
                System.out.println("pppppppppppp  " + new Gson().toJson(remoteMessage.getData()));
                jsonObject = new JSONObject(remoteMessage.getData());
                type = jsonObject.getString("type");

                if (type.equalsIgnoreCase("Logout")) {
                    CommonUtils.logoutNow(this);
                    return;
                }
                if (type.equalsIgnoreCase(ParameterConstants.VOICE_CALL)||type.equalsIgnoreCase(ParameterConstants.VIDEO_CALL)) {
                    Intent intent = new Intent(getApplicationContext(), IncomingCallActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    IncomingCall incomingCall = new IncomingCall();
                    incomingCall.setJitsiToken(jsonObject.getString("jitsiToken"));
                    incomingCall.setUpdatedAt(jsonObject.getString("updatedAt"));
                    incomingCall.setDescription(jsonObject.getString("description"));
                    incomingCall.setProfilePicture(jsonObject.getString("profilePicture"));
                    incomingCall.setStatus(jsonObject.getString("status"));
                    incomingCall.setUserId(jsonObject.getString("userId"));
                    incomingCall.setUsername(jsonObject.getString("username"));
                    incomingCall.setUrl(jsonObject.getString("url"));
                   // incomingCall.setUrl("https://meet.jit.si");
                    incomingCall.setType(jsonObject.getString("type"));
                    incomingCall.setGroup(jsonObject.getString("group"));
                    incomingCall.setVideo(jsonObject.getString("video"));
                    incomingCall.setCreatedAt(jsonObject.getString("createdAt"));
                    incomingCall.setFullName(jsonObject.getString("fullName"));
                    incomingCall.setUnreadNotification(jsonObject.getString("unreadNotification"));
                    incomingCall.setUnreadNotification(jsonObject.getString("unreadNotification"));
                    incomingCall.setNotificationType(jsonObject.getString("notificationType"));
                    incomingCall.setCallRoomId(jsonObject.getString("callRoomId"));
                    incomingCall.setId(jsonObject.getString("id"));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        Intent serviceIntent = new Intent(getApplicationContext(), HeadsUpNotificationService.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("inititator", jsonObject.getString("fullName"));
                        mBundle.putString("call_type",notificationType);
                        mBundle.putString("data", new Gson().toJson(incomingCall));
                        serviceIntent.putExtras(mBundle);
                        ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
                    }
                  /*  if (Build.VERSION.SDK_INT >= 23) {
                        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) && PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                            incomingCall(intent);
                        }
                    } else {
                        incomingCall(intent);
                    }*/
                    return;
                }

                if (type.equalsIgnoreCase("Medley")) {
                    createMedleyNotification(new Gson().toJson(remoteMessage.getData()));
                    return;
                }

                if (!type.equalsIgnoreCase("goLive")) {
                    Log.v("harsh", "for feed/svs clause");
                    Log.e("shaban", "object" + jsonObject);
                    userId = jsonObject.getString("userId");
                    notificationType = jsonObject.getString("notificationType");
                    id_push = jsonObject.getString("userId");
                }
//                int (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE) = 1;
                String channelId = "channel-01";
                String channelName = "Channel Name";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel(
                            channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(mChannel);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                    NotificationManager notificationManagernew = getSystemService(NotificationManager.class);
                    NotificationChannel priorityNotificationChannel = new NotificationChannel(PRIORITY_CHANNEL_ID, "priority notification channel", NotificationManager.IMPORTANCE_HIGH);
                    notificationManagernew.createNotificationChannel(mChannel);
                    notificationManagernew.createNotificationChannel(priorityNotificationChannel);
                }
                // feed background dashboard refresh notification
                if (userId != null && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("NewPost")){
                    System.out.println("NewPost::  " + remoteMessage);
                    int nc = 1 + SharedPrefreances.getSharedPreferenceInt(this, SharedPrefreances.POSTWALL_REFRESH_COUNT);
                    SharedPrefreances.setSharedPreferenceInt(this, SharedPrefreances.POSTWALL_REFRESH_COUNT, nc);
//                    Intent local = new Intent();
//                    local.putExtra("status", "NewPost");
//                    local.setAction("post_upload");
//                    sendBroadcast(local);

                } else  if (userId != null && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("Story")){
                    System.out.println("NewPost::  " + remoteMessage);
                    int nc = 1 + SharedPrefreances.getSharedPreferenceInt(this, SharedPrefreances.POSTWALL_REFRESH_COUNT);
                    SharedPrefreances.setSharedPreferenceInt(this, SharedPrefreances.POSTWALL_REFRESH_COUNT, nc);
//                    Intent local = new Intent();
//                    local.putExtra("status", "story");
//                    local.setAction("post_upload");
//                    sendBroadcast(local);

                }
                // feed following notification
                else  if (userId != null && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("Follower")) {
                    String title = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(title + " " + getString(R.string.started_following_you))
                            .setSound(soundUri)
                            .setVibrate(VIBRATE_PATTERN)
                            .setPriority(NotificationCompat.PRIORITY_MAX);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), OtherUserAccount.class);// open followers in profile activity
                    intent.putExtra("id_push", id_push);
                    intent.putExtra("from_push", "1");
                    intent.putExtra("userId", userId);
                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setAutoCancel(true);
                    notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());
                    countNotificationFeed();
                }
                //feed post like notification
                else if (userId != null && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("PostLike")) {
                    String postId = jsonObject.getString("postId");
                    String title = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(title + " " + getResources().getString(R.string.Liked_your_post))
                            .setSound(soundUri)
                            .setVibrate(VIBRATE_PATTERN)
                            .setPriority(NotificationCompat.PRIORITY_MAX);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), NotificationSocialFeedActivity.class);// open followers in profile activity
                    intent.putExtra("id_push", id_push);
                    intent.putExtra("from_push", "1");
                    intent.putExtra("userId", userId);
                    intent.putExtra("postId", postId);
                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setAutoCancel(true);
                    notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());
                    countNotificationFeed();
                }
                // feed post comment notification
                else if (userId != null && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("PostComment")) {
                    String postId = jsonObject.getString("postId");
                    /// String comment = jsonObject.getString("comment");

                    // String ct4 = comment.replace("+", " ");

                    String title = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(title + " " + getString(R.string.commented_on_your_post))
                            //.setContentText(ct4)
                            .setSound(soundUri)
                            .setVibrate(VIBRATE_PATTERN)
                            .setPriority(NotificationCompat.PRIORITY_MAX);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), NotificationSocialFeedActivity.class);// open followers in profile activity
                    intent.putExtra("id_push", id_push);
                    intent.putExtra("from_push", "1");
                    intent.putExtra("userId", userId);
                    intent.putExtra("postId", postId);
                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setAutoCancel(true);
                    notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());
                    countNotificationFeed();
                }
                // feed post like notification
                else if (userId != null && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("CommentLike")) {
                    String postId = jsonObject.getString("postId");
                    /// String comment = jsonObject.getString("comment");

                    // String ct4 = comment.replace("+", " ");
                    String title = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(title + " " + getString(R.string.liked_your_comment))
                            //.setContentText(ct4)
                            .setSound(soundUri)
                            .setVibrate(VIBRATE_PATTERN)
                            .setPriority(NotificationCompat.PRIORITY_MAX);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), NotificationSocialFeedActivity.class);// open followers in profile activity
                    intent.putExtra("id_push", id_push);
                    intent.putExtra("from_push", "1");
                    intent.putExtra("userId", userId);
                    intent.putExtra("postId", postId);
                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setAutoCancel(true);
                    notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());
                    countNotificationFeed();
                }
                // feed post like notification
                else if (userId != null && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("CommentofComment")) {
                    String postId = jsonObject.getString("postId");
                    /// String comment = jsonObject.getString("comment");

                    // String ct4 = comment.replace("+", " ");
                    String title = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(title + " " + getString(R.string.replied_to_your_Comment))
                            //.setContentText(ct4)
                            .setSound(soundUri)
                            .setVibrate(VIBRATE_PATTERN)
                            .setPriority(NotificationCompat.PRIORITY_MAX);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), NotificationSocialFeedActivity.class);// open followers in profile activity
                    intent.putExtra("id_push", id_push);
                    intent.putExtra("from_push", "1");
                    intent.putExtra("userId", userId);
                    intent.putExtra("postId", postId);
                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setAutoCancel(true);
                    notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());
                    countNotificationFeed();
                }
                // feed post tag notification..
                else if (userId != null && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("PostTag")) {
                    String postId = jsonObject.getString("postId");
                    String comment = jsonObject.getString("caption");
                    String title = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(title + " " + getString(R.string.Tagged_you_in_a_post))
                            .setContentText(comment)
                            .setSound(soundUri)
                            .setVibrate(VIBRATE_PATTERN)
                            .setPriority(NotificationCompat.PRIORITY_MAX);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), NotificationSocialFeedActivity.class);// open followers in profile activity
                    intent.putExtra("id_push", id_push);
                    intent.putExtra("from_push", "1");
                    intent.putExtra("userId", userId);
                    intent.putExtra("postId", postId);
                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setAutoCancel(true);
                    notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());
                    countNotificationFeed();
                }
                // svs post comment notification
                //BannerAds
                else if (userId != null && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("bannerAds")) {
                    String postId = jsonObject.getString("postId");
                    String userID = jsonObject.getString("userId");
                    String caption = jsonObject.getString("caption");
                    String title = jsonObject.getString("title");
                    String displayPicture = jsonObject.getString("displayPicture");
                    String image = jsonObject.getString("image");
                    String thumbnail = jsonObject.getString("thumbnail");
                    String name = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(name + " ")
                            .setContentText(title)
                            .setCategory(caption)
                            .setSound(soundUri)
                            .setVibrate(VIBRATE_PATTERN)
                            .setPriority(NotificationCompat.PRIORITY_MAX);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), NotificationSocialFeedActivity.class);// open followers in profile activity
                    intent.putExtra("id_push", id_push);
                    intent.putExtra("from_push", "1");
                    intent.putExtra("userId", userId);
                    intent.putExtra("postId", postId);
                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
//                    mBuilder.setAutoCancel(true);
                    Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(displayPicture).placeholder(R.drawable.image_placeholder)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    mBuilder.setLargeIcon(resource);
                                    if (thumbnail.equals("")) {
                                        NotificationManagerCompat.from(getApplicationContext()).notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());
                                    }
                                    Glide.with(getApplicationContext())
                                            .asBitmap()
                                            .load(thumbnail)
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource2, @Nullable Transition<? super Bitmap> transition) {
                                                    mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource2));
                                                    NotificationManagerCompat.from(getApplicationContext()).notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());
                                                    countNotificationFeed();
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                }
                                            });
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                } else if (userId != null && type.equalsIgnoreCase("SVS") && notificationType.equalsIgnoreCase("Comment")) {
                    String videoId = jsonObject.getString("videoId");
                    String comment = jsonObject.getString("comment");
                    String ct = comment.replace("+", " ");
                    String title = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(title + " " + getString(R.string.commented_on_your_post))
                            //.setContentText(ct)
                            .setSound(soundUri)
                            .setVibrate(VIBRATE_PATTERN)
                            .setPriority(NotificationCompat.PRIORITY_MAX);

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());


                    Intent intent = new Intent(getApplicationContext(), NotificationSvsActivity.class);// open followers in profile activity
                    intent.putExtra("id_push", id_push);
                    intent.putExtra("from_push", "1");
                    intent.putExtra("videoId", videoId);

                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setAutoCancel(true);
                    notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());

                }
                // svs post Like notification
                else if (userId != null && type.equalsIgnoreCase("SVS") && notificationType.equalsIgnoreCase("Like")) {
                    String videoId = jsonObject.getString("videoId");
                    // String comment = jsonObject.getString("comment");
                    // String ct = comment.replace("+", " ");
                    String title = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(title + " " + getString(R.string.Liked_Your_Video_Post))
                            // .setContentText(ct)
                            .setSound(soundUri)
                            .setVibrate(VIBRATE_PATTERN)
                            .setPriority(NotificationCompat.PRIORITY_MAX);

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());

                    Intent intent = new Intent(getApplicationContext(), NotificationSvsActivity.class);// open followers in profile activity
                    intent.putExtra("id_push", id_push);
                    intent.putExtra("from_push", "1");
                    intent.putExtra("videoId", videoId);
                    intent.putExtra("isNotification", true);


                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setAutoCancel(true);
                    notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());

                }
                // svs following notification
                else if (userId != null && type.equalsIgnoreCase("SVS") && notificationType.equalsIgnoreCase("Follower")) {

                    //String userId=jsonObject.getString("UserId");
                    // String FollowerId=jsonObject.getString("FollowerId");
                    String title = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");


                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(title + " " + getString(R.string.started_following_you))
                            .setSound(soundUri)
                            .setVibrate(VIBRATE_PATTERN)
                            .setPriority(NotificationCompat.PRIORITY_MAX);
                    ;
                    // .setContentText("following");

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());


                    Intent intent = new Intent(getApplicationContext(), VideoOtherUserActivity.class);// open followers in profile activity
                    intent.putExtra("id_push", id_push);
                    intent.putExtra("from_push", "1");
                    intent.putExtra("userId", userId);

                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setAutoCancel(true);
                    notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());

                }
                // svs post comment  like notification
                else if (userId != null && type.equalsIgnoreCase("SVS") && notificationType.equalsIgnoreCase("CommentLike")) {

                    //String userId=jsonObject.getString("userId");
                    String videoId = jsonObject.getString("videoId");
                    // String comment = jsonObject.getString("comment");
                    String title = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");

                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(title + getString(R.string.liked_your_comment))
                            .setSound(soundUri)
                            .setVibrate(VIBRATE_PATTERN)
                            .setPriority(NotificationCompat.PRIORITY_MAX);
                    ;
                    // .setContentText("following");

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), NotificationSvsActivity.class);// open followers in profile activity
                    intent.putExtra("id_push", id_push);
                    intent.putExtra("from_push", "1");
                    intent.putExtra("userId", userId);
                    intent.putExtra("videoId", videoId);


                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setAutoCancel(true);
                    notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());

                } else if (userId != null && type.equalsIgnoreCase("Feed") && notificationType.equalsIgnoreCase("Message")) {
                    //  newMessageHandler.handleChatCountNotification();
                    String lang_eng = SharedPreferencesManager.getLanguage();
                    setAppLocale(lang_eng);
                    String title = jsonObject.getString("otherFullName");
                    String attachment = jsonObject.getString("attachment");
                    if (attachment.equalsIgnoreCase("true")){
                        String attachmentType = jsonObject.getString("attachmentType");
                        if (attachmentType.equalsIgnoreCase("Message"))
                            title=title+" "+getString(R.string.send)+" "+getString(R.string.you)+" "+getString(R.string.message);
                        else if(attachmentType.equalsIgnoreCase("Image"))
                            title=title+" "+getString(R.string.send)+" "+getString(R.string.you)+" "+getString(R.string.image);
                        else if(attachmentType.equalsIgnoreCase("Video"))
                            title=title+" "+getString(R.string.send)+" "+getString(R.string.you)+" "+getString(R.string.video);
                        else if(attachmentType.equalsIgnoreCase("Audio"))
                            title=title+" "+getString(R.string.send)+" "+getString(R.string.you)+" "+getString(R.string.audio);
                        else
                            title=title+" "+getString(R.string.send)+" "+getString(R.string.you)+" "+getString(R.string.message);
                    }else
                        title=title+" "+getString(R.string.send)+" "+getString(R.string.you)+" "+getString(R.string.message);

                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(title)
                            // .setContentText(jsonObject.getString("msg"))
                            .setSound(soundUri)
                            .setVibrate(VIBRATE_PATTERN)
                            .setPriority(NotificationCompat.PRIORITY_MAX);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), ChatBoatActivity.class);// open ChatActivity activity
                    intent.putExtra("user_id_to", jsonObject.getString("otherUserId"));
                    intent.putExtra("chatHeadId", jsonObject.getString("chatHeadId"));
                    intent.putExtra("pro_file_pic", jsonObject.getString("profilePicture"));
                    intent.putExtra("from_push", "0");
                    intent.putExtra("isSingle", true);
                    intent.putExtra("demoid", jsonObject.getString("otherUserId"));
                    intent.putExtra("user_id_name", jsonObject.getString("otherFullName"));
                    intent.putExtra("username", jsonObject.getString("otherFullName"));
                    intent.putExtra("profilePicture", jsonObject.getString("profilePicture"));
                    intent.putExtra("notification", true);
                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setAutoCancel(true);
                    notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());
                    handleChatCountNotification();
//======================================= send deliver message

                    JSONObject jsonObjectRead = new JSONObject();
                    try {
                        Meeast app = (Meeast) getApplicationContext();
                        mSocket = app.getSocket();
                        jsonObjectRead.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
                        jsonObjectRead.put("toUserId", jsonObject.getString("otherUserId"));
                        jsonObjectRead.put("chatHeadId", jsonObject.getString("chatHeadId"));
                        jsonObjectRead.put("readStatus", Global.ReadStatus.delivered.name());
                        mSocket.emit("messageRead", jsonObjectRead);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//   ===================================
                } else if (type.equals("Feed") && notificationType.equalsIgnoreCase("Request")) {
                    //  newMessageHandler.handleChatCountNotification();
                    String title = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");
                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(title + " " + getString(R.string.request_you))
                            // .setContentText(jsonObject.getString("msg"))
                            .setSound(soundUri)
                            .setVibrate(VIBRATE_PATTERN)
                            .setPriority(NotificationCompat.PRIORITY_MAX);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), ChatRequestActivity.class);// open ChatActivity activity
                    intent.putExtra("userId", jsonObject.getString("userId"));
                    intent.putExtra("notification", true);
                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setAutoCancel(true);
                    notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());

                }
                // svs post comment of comment notification
                else {

                    //String userId=jsonObject.getString("userId");
                    String videoId = jsonObject.getString("videoId");
                    //String comment = jsonObject.getString("comment");
                    String title = jsonObject.getString("firstName") + " " + jsonObject.getString("lastName");

                    mBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                            .setSmallIcon(R.drawable.notification_icon)
                            .setContentTitle(title + getString(R.string.commented_on_your_Comment))
                            //.setContentText(comment)
                            .setSound(soundUri)
                            .setVibrate(VIBRATE_PATTERN)
                            .setPriority(NotificationCompat.PRIORITY_MAX);
                    ;
                    // .setContentText("following");

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    Intent intent = new Intent(getApplicationContext(), NotificationSvsActivity.class);// open followers in profile activity
                    intent.putExtra("id_push", id_push);
                    intent.putExtra("from_push", "1");
                    intent.putExtra("userId", userId);
                    intent.putExtra("videoId", videoId);


                    stackBuilder.addNextIntent(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(resultPendingIntent);
                    mBuilder.setAutoCancel(true);
                    notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), mBuilder.build());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createMedleyNotification(String jsonString) {
        createMedleyNotificationChannel();

        NotificationModel notificationModel = (new Gson()).fromJson(jsonString, NotificationModel.class);
        String title = "";
//        Intent notificationIntent = new Intent(this, SplashScreenActivity.class);
        Intent notificationIntent = new Intent(this, SplashScreen.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Const.channelId)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(Const.appName)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setVibrate(VIBRATE_PATTERN)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        switch (notificationModel.getNotificationType()) {
            case "1": //like

                title = notificationModel.getFirstName() + " " + notificationModel.getLastName() + " " + getString(R.string.Liked_your_post);
                notificationIntent.putExtra(Const.videoId, notificationModel.getPostId());

                break;
            case "2": //comment

                title = notificationModel.getFirstName() + " " + notificationModel.getLastName() + " " + getString(R.string.commented_on_your_post);
                notificationIntent.putExtra(Const.videoId, notificationModel.getPostId());

                break;
            case "3": //follow

                title = notificationModel.getFirstName() + " " + notificationModel.getLastName() + " " + getString(R.string.started_following_you);
                notificationIntent.putExtra(Const.userId, notificationModel.getUserId());

                break;
            case "4": //milestone or view milestone

                title = getString(R.string.congratulation_your_video_crossed) + " " + notificationModel.getAddView() + " " + getString(R.string.views);
                notificationIntent.putExtra(Const.videoId, notificationModel.getPostId());

                break;
            case "5": //Comment like

                title = notificationModel.getFirstName() + " " + notificationModel.getLastName() + " " + getString(R.string.liked_your_comment_medley);
                notificationIntent.putExtra(Const.videoId, notificationModel.getPostId());

                break;
            case "6": //Comment reply

                title = notificationModel.getFirstName() + " " + notificationModel.getLastName() + " " + getString(R.string.replied_to_your_Comment_medley);
                notificationIntent.putExtra(Const.videoId, notificationModel.getPostId());

                break;

            case "7": //Promotion Notification

                title = notificationModel.getMessage();
                notificationIntent.putExtra(Const.videoId, notificationModel.getPostId());
            default:
                break;

        }


        notificationBuilder.setContentText(title);
        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(intent);

        Glide.with(getApplicationContext())
                .asBitmap()
                .load(notificationModel.getDisplayPicture())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        if (!notificationModel.getNotificationType().equals("7"))
                            notificationBuilder.setLargeIcon(resource);

                        if (notificationModel.getThumbnail().equals("")) {
                            NotificationManagerCompat.from(getApplicationContext()).notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), notificationBuilder.build());
                        }

                        Glide.with(getApplicationContext())
                                .asBitmap()
                                .load(notificationModel.getThumbnail())
                                .into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource2, @Nullable Transition<? super Bitmap> transition) {
                                        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource2));
                                        NotificationManagerCompat.from(getApplicationContext()).notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), notificationBuilder.build());
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                    }
                                });
//                        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(getBitmapFromURL(notificationModel.getDisplayPicture())));
//                        NotificationManagerCompat.from(getApplicationContext()).notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), notificationBuilder.build());
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    private void createMedleyNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(Const.channelId, Const.channelName, NotificationManager.IMPORTANCE_HIGH); // please don't create string for this, Contact-Himanshu Anand
            channel.setDescription(Const.channelDescription);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mNotifCancelHandler == null) {
            mNotifCancelHandler = new Handler();
        }
    }

    //  for updating notfication count
    private void countNotificationFeed() {
        if (MainActivity.getInstance() != null) {
            int nc = 1 + SharedPrefreances.getSharedPreferenceInt(this, SharedPrefreances.NOIFICATION_COUNT);
            SharedPrefreances.setSharedPreferenceInt(this, SharedPrefreances.NOIFICATION_COUNT, nc);
            Log.e(TAG, "handleResults: ---" + SharedPrefreances.getSharedPreferenceInt(this, SharedPrefreances.NOIFICATION_COUNT));
            MainActivity.getInstance().setNotificationCount(String.valueOf(SharedPrefreances.getSharedPreferenceInt(MyFirebaseMessagingService.this, SharedPrefreances.NOIFICATION_COUNT)));
        }

//        Log.e(TAG, "handleResults: ---" +SharedPrefreances.getSharedPreferenceInt(this, SharedPrefreances.NOIFICATION_COUNT));
    }

    private void sendNotification(IncomingCall incomingCall) {
        Intent intent = new Intent(this, IncomingCallActivity.class);
        intent.putExtra("data", new Gson().toJson(incomingCall));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

//        custom notification view
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_push);
        contentView.setImageViewResource(R.id.image, R.drawable.notification_icon);
        contentView.setTextViewText(R.id.title, incomingCall.getFullName());
        contentView.setTextViewText(R.id.text, "Missed Call");
        contentView.setTextViewText(R.id.time, "23/06/2021");
//        notification code
        final String CHANNEL_ID = "channel_01";
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.meest_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.meest_logo))
                .setColor(Color.RED)
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tone))
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(incomingCall.getFullName())
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContent(contentView)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setOngoing(true);
//        builder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(0, builder.build());
//        wakeLock();
//        tone();

    }

    void wakeLock() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (isScreenOn == false) {
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
            wl.acquire(10000);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");
            wl_cpu.acquire(10000);
        }
    }

    public void tone() {
        MediaPlayer mediaPlayer;
        Vibrator vib;
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.tone);
        mediaPlayer.start();
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(111110000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.stop();
                vib.cancel();
            }
        }, 20000);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, IncomingCallActivity.class).putExtra("msg", messageBody);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "idddd";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(MyFirebaseMessagingService.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("FCM Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public void handleChatCountNotification() {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<ChatUnreadCountResponse> searchCall = webApi.chatTotalUnReadCount(header);
        searchCall.enqueue(new Callback<ChatUnreadCountResponse>() {
            @Override
            public void onResponse(Call<ChatUnreadCountResponse> call, Response<ChatUnreadCountResponse> response) {
                try {
                    if (response.body() != null) {
                        if (response.body().getCode() == 1)
                            HomeFragments.getInstanceHomeFragment().setChatNotificationCount(response.body().getData());
                            // SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.CHAT_NOTIFICATION_COUNT,""+response.body().getData());
                        else
                            HomeFragments.getInstanceHomeFragment().setChatNotificationCount(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ChatUnreadCountResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void incomingCall(Intent intent) {
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean result = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH && powerManager.isInteractive() || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH && powerManager.isScreenOn();

        if (!result) {
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MH24_SCREENLOCK");
            wl.acquire(10000);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MH24_SCREENLOCK");
            wl_cpu.acquire(10000);
        }

        intent.setAction("OPEN_ACTIVITY_1");
        if (ParameterConstants.APP_STATE.equals("f")) {
            startActivity(intent);
        } else {
            startActivity(intent);
        }
        sendNotification("incoming call");
        isAppRunning();
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
}