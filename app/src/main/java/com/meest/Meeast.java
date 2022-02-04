package com.meest;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ShortcutManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;

import com.danikula.videocache.HttpProxyCacheServer;
import com.facebook.FacebookSdk;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.CacheEvictor;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.GsonBuilder;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.metme.network.NetworkUtil;
import com.meest.utils.ParameterConstants;
import com.meest.chat_calling.utils.SharedPreferencesManager;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.videomvvmmodule.utils.Global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.branch.referral.Branch;
import io.socket.client.IO;
import io.socket.client.Socket;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class Meeast extends Application implements Application.ActivityLifecycleCallbacks, LifeCycleDelegate {
    private static Meeast mApp = null;
    private static String currentChatId = "";
    private static boolean isCallActive = false;
    private static boolean baseActivityVisible;
    private boolean hasMovedToForeground = false;
    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;
    private FirebaseAnalytics mFirebaseAnalytics;
    String token;
    public static SimpleCache simpleCache = null;
    public static LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor = null;
    public static ExoDatabaseProvider exoDatabaseProvider = null;
    public static Long exoPlayerCacheSize = (long) (90 * 1024 * 1024);
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Socket mSocket;
    static WebApi api;
    public static boolean activityVisible;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();
        AppLifecycleHandler lifeCycleHandler = new
                AppLifecycleHandler(Meeast.this);
        registerLifecycleHandler(lifeCycleHandler);
        // enable cookies
        java.net.CookieManager cookieManager = new java.net.CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        //Socket
        try {
            mSocket = IO.socket(ApiUtils.SOCKET_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        FirebaseApp.initializeApp(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        SharedPreferencesManager.init(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(), "token").equals("")) {
                            if (task.getResult() != null) {
                                token = task.getResult().getToken();
                                Global.firebaseDeviceToken = task.getResult().getToken();

                                updateToken(task.getResult().getToken());
                            }
                            Log.e("firebase_token", "Meeast:" + token);
                            SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "token", token);
                        }
                    }
                });

        LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(94371840L);
        DatabaseProvider databaseProvider = (DatabaseProvider) (new ExoDatabaseProvider((Context) this));
        if (simpleCache == null) {
            simpleCache = new SimpleCache(this.getCacheDir(), (CacheEvictor) leastRecentlyUsedCacheEvictor, databaseProvider);
        }

        Branch.getAutoInstance(this);
        AudienceNetworkAds.initialize(this);
        FacebookSdk.sdkInitialize(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("notification", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        if (task.getResult() != null) {
                            String token = task.getResult().getToken();
                            Global.firebaseDeviceToken = token;
                            Log.d("notification fcm token ", token);
                        }
                        // Log and toast
                    }
                });
       /* Global.accessToken = sessionManager.getUser() != null ? sessionManager.getUser().getData().getToken() : "";
        Global.userId = sessionManager.getUser() != null ? sessionManager.getUser().getData().getUserId() : "";*/
        if (leastRecentlyUsedCacheEvictor == null) {
            leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(exoPlayerCacheSize);
        }

        if (exoDatabaseProvider != null) {
            exoDatabaseProvider = new ExoDatabaseProvider(this);
        }

        if (simpleCache == null) {
            simpleCache = new SimpleCache(getCacheDir(), leastRecentlyUsedCacheEvictor, exoDatabaseProvider);
            if (simpleCache.getCacheSpace() >= 400207768) {
                freeMemory();
            }
            Log.i(TAG, "onCreate: " + simpleCache.getCacheSpace());
        }
        mApp = this;
    }

    private void updateToken(String token) {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json");
        map.put("Content-Type", "application/json");
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
//        body.put("fcmToken", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), "token"));
        body.put("isOnline", "true");
        body.put("fcmToken", token);
        Call<ApiResponse> call = webApi.updateUserProfile(map, body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Logout_TAG", "e======================================" + "logout");
            }
        });
    }

    public Socket getSocket() {
        return mSocket;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void disableshourtcuts() {
        List<String> idds = new ArrayList<>();
        idds.add("addstory");
        idds.add("group");
        idds.add("user1");
        idds.add("user2");
        ShortcutManager shortcutManager2 = this.getSystemService(ShortcutManager.class);
        shortcutManager2.disableShortcuts(idds);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /*video code implementation*/
    public void freeMemory() {

        try {
            File dir = getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        Meeast app = (Meeast) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)
                .maxCacheFilesCount(20)
                .build();

    }

    public static Context context() {
        return mApp.getApplicationContext();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
            hasMovedToForeground = true;

        } else {
            hasMovedToForeground = false;
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        activityVisible = true;
    }


    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            // App enters background
            //  SharedPreferencesManager.setLastActive(System.currentTimeMillis());
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    @Override
    public void onAppBackgrounded() {
        Log.d("http app Awww", "App in background");
        ParameterConstants.APP_STATE = "b";
    }

    @Override
    public void onAppForegrounded() {
        Log.d("http app Yeeey", "App in foreground");
        ParameterConstants.APP_STATE = "f";
    }

    private void registerLifecycleHandler(AppLifecycleHandler lifeCycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler);
        registerComponentCallbacks(lifeCycleHandler);
    }

    // current activity state

    public static boolean isActivityVisible() {
        return activityVisible; // return true or false
    }

    public static void activityResumed() {
        activityVisible = true;// this will set true when activity resumed

    }

    public static void activityPaused() {
        activityVisible = false;// this will set false when activity paused

    }
}

