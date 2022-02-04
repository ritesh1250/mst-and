package com.meest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.meest.Activities.CampaignActivity;
import com.meest.Activities.HelpActivity;
import com.meest.Activities.InviteFriendActivity;
//import com.meest.Activities.NewPostUploadActivity;
import com.meest.social.socialViewModel.view.NewPostUploadActivity;
import com.meest.Activities.VideoPost.CameraActivity;
import com.meest.Activities.base.ApiCallActivity;
import com.meest.Activities.preview.PreviewPhotoActivity;
import com.meest.Fragments.HomeFragments;
import com.meest.Insights.InsightsTabActivity;
import com.meest.Interfaces.FragmentCommunication;
import com.meest.Interfaces.SwitchViewCallback;
import com.meest.chat_calling.utils.SharedPreferencesManager;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.register.DefaultAppResponse;
import com.meest.meestbhart.register.DefaultParam;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.social.socialViewModel.NotificationFragment;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.social.socialViewModel.view.SplashScreen;
import com.meest.social.socialViewModel.view.createTextPost.CreateTextPost;
import com.meest.social.socialViewModel.view.home.navigation.MyBookmarks;
import com.meest.social.socialViewModel.view.home.navigation.MyOption;
import com.meest.social.socialViewModel.view.home.navigation.MySetting;
import com.meest.social.socialViewModel.view.login.LoginSignUp;
import com.meest.social.socialViewModel.view.myAccountFrag.MyAccountFrag;
import com.meest.social.socialViewModel.view.searchLocation.SearchLocation;
import com.meest.social.socialViewModel.view.trending.TrendingFragment;
import com.meest.utils.LoadingDialog;
import com.meest.utils.Permission;
import com.meest.utils.SingleShotLocationProvider;
import com.meest.utils.Variables;
import com.meest.videomvvmmodule.SplashScreenVideoActivity;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;
import org.jsoup.Jsoup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;
import static com.meest.meestbhart.utilss.SharedPrefreances.AUTH_TOKEN;
import static com.meest.meestbhart.utilss.SharedPrefreances.HOME_INTRO;
import static com.meest.social.socialViewModel.utils.ParameterConstant.SocialPrefrences.USER_ID;
import static com.meest.utils.goLiveUtils.CommonUtils.getTargetView;

public class MainActivity extends ApiCallActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentCommunication, SwitchViewCallback {
    public static final String MESSAGE_STATUS = "message_status";
    public static final int PERMISSION_CODE = 123;
    public static final int CAMERA_PERMISSION_CODE = 124;
    public static final int STORAGE_PERMISSION_CODE = 127;
    public static final int VIDEO_PERMISSION_CODE = 125;
    public static final int SVS_VIDEO_PERMISSION_CODE = 126;
    private static final int REQUEST_LOCATION = 1;
    private final static String TAG = MainActivity.class.getSimpleName();
    public static boolean isMainOpened = true, isHomeSelected = true;
    private static MainActivity instanceMain;
    public DrawerLayout drawer;
    public ImageView img_search;
    public Animation animZoomin1;
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment1 = new HomeFragments();
    Fragment fragment2 = new TrendingFragment();
    Fragment fragment3 = new NotificationFragment();
    Fragment fragment4 = new MyAccountFrag();
    LocationManager locationManager;
    String latitude, longitude;
    RelativeLayout layout_my_account, layout_home, layout_setting, layout_help, layout_manage, layout_activity, layout_invite_friends, layout_search, layout_post,
            layout_notification, layout_collection, layout_delete_account;
    RelativeLayout layout_home_video, layout_search_video, layout_post_video,
            layout_notification_video, layout_my_account_video, layout_insights;
    ImageView img_profile;
    NavigationView navigationView;
    TextView tvNotificationCount;
    int PICK_MEDIA_CODE = 10;
    LinearLayout layout_bottom, layout_bottom_video;
    LoadingDialog loadingDialog;
    List<Integer> icons = new ArrayList<>();
    String currentVersion;
    SwitchCompat switchDefault_meest, switchDefault_medly;
    String pref_font;
    BottomNavigationView bottomNavigationView;
    BottomNavigationItemView itemView;
    Menu menu;
    View badge;
    Fragment active = fragment1;
    private boolean mIsBackVisible = false, isSvs = false;
    private Disposable disposable;
    private NavController navController;
    private FragmentRefreshListener fragmentRefreshListener;
    private Boolean boolScroll = true;

    public static MainActivity getInstance() {
        return instanceMain;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateToken();
//        SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN, "");
        if(SharedPrefreances.getSharedPreferenceString(MainActivity.this,AUTH_TOKEN).equalsIgnoreCase("") && SharedPrefreances.getSharedPreferenceString(MainActivity.this,USER_ID).equalsIgnoreCase("")){
            checkApiUth();
        }
        setContentView(R.layout.activity_main);
        instanceMain = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findIds();

        inItNavigation();
        if (getIntent().getExtras() != null) {
            isSvs = getIntent().getExtras().getBoolean("isSvs", false);
        }

        loadingDialog = new LoadingDialog(this);
//        permissionOverLayDialog();

        layout_post.setOnClickListener(v -> {
            Intent videoIntent = new Intent(MainActivity.this, SplashScreenVideoActivity.class);
            videoIntent.putExtra("whereFrom", "social");
            videoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(videoIntent);
            finish();
        });


     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        }*/
        navigationView = findViewById(R.id.nav_view);
        MaterialShapeDrawable navViewBackground = (MaterialShapeDrawable) navigationView.getBackground();
        navViewBackground.setShapeAppearanceModel(
                navViewBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setBottomLeftCorner(CornerFamily.ROUNDED, 100)
                        .setTopLeftCorner(CornerFamily.ROUNDED, 100)
                        .build());
        drawer = findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        View headerView = navigationView.getHeaderView(0);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        RelativeLayout layout_logout = headerView.findViewById(R.id.layout_logout);
        ImageView img_close = headerView.findViewById(R.id.img_close);
        layout_activity = headerView.findViewById(R.id.layout_activity);
        layout_setting = headerView.findViewById(R.id.layout_setting);
        layout_help = headerView.findViewById(R.id.layout_help);

        layout_manage = headerView.findViewById(R.id.layout_manage);
        layout_invite_friends = headerView.findViewById(R.id.layout_invite_friends);
        layout_setting = headerView.findViewById(R.id.layout_setting);
        layout_collection = headerView.findViewById(R.id.layout_collection);
        layout_delete_account = headerView.findViewById(R.id.layout_delete_account);
        layout_insights = headerView.findViewById(R.id.layout_insights);
        switchDefault_meest = headerView.findViewById(R.id.switch_meest);
        switchDefault_medly = headerView.findViewById(R.id.switch_medly);
        if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.WHEREFROM).equalsIgnoreCase("Video")) {
            switchDefault_medly.setChecked(true);
            switchDefault_meest.setChecked(false);
        } else {
            switchDefault_meest.setChecked(true);
            switchDefault_medly.setChecked(false);
        }

        switchDefault_meest.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                switchDefault_medly.setChecked(false);
                setdefaultApp(0);
            } else {
                switchDefault_medly.setChecked(true);
            }
        });

        switchDefault_medly.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                switchDefault_meest.setChecked(false);
                setdefaultApp(1);
            } else {
                switchDefault_meest.setChecked(true);
            }
        });

        TextView txt_name = headerView.findViewById(R.id.txt_name);
        txt_name.setText(SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.USERNAME));
        img_close.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.RIGHT);
        });

        layout_collection.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MyBookmarks.class);
            startActivity(intent);
            drawer.closeDrawers();
        });

        layout_activity.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MyOption.class);
            drawer.closeDrawers();
            startActivity(intent);
        });

        layout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutNow();
            }
        });

        layout_delete_account.setOnClickListener(v -> {
            View conView = getLayoutInflater().inflate(R.layout.delete_account_confirmation_second, null);
            Button btnConfirm = conView.findViewById(R.id.btnConfirm);
            Button btnClose = conView.findViewById(R.id.btnClose);
            BottomSheetDialog dia = new BottomSheetDialog(MainActivity.this);
            dia.setContentView(conView);
            btnClose.setOnClickListener(v1 -> {
                dia.dismiss();
            });

            btnConfirm.setOnClickListener(v1 -> {
                deleteAccount();
            });
            dia.show();
        });


        layout_insights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsightsTabActivity.class);
                startActivity(intent);
            }
        });

        layout_invite_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InviteFriendActivity.class);
                startActivity(intent);
            }
        });


        layout_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
                drawer.closeDrawers();
            }
        });
        layout_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CampaignActivity.class);
                startActivity(intent);
                drawer.closeDrawers();
            }
        });
        layout_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences;
                sharedPreferences = getSharedPreferences("Theme", Context.MODE_PRIVATE);
                String themeName = sharedPreferences.getString("ThemeName", "Default");
                Log.v("1111111111111", themeName);
                Intent intent = new Intent(MainActivity.this, MySetting.class);
//                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                drawer.closeDrawers();
            }
        });

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


    @SuppressLint("NonConstantResourceId")
    private void inItNavigation() {
        menu = bottomNavigationView.getMenu();
//        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        itemView = bottomNavigationView.findViewById(R.id.navigation_notification);
        badge = LayoutInflater.from(this).inflate(R.layout.custom_badge_layout, bottomNavigationView, false);
        tvNotificationCount = badge.findViewById(R.id.tvNotificationCount);


        // bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragment4, "4").hide(fragment4).commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragment3, "3").hide(fragment3).commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragment2, "2").hide(fragment2).commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragment1, "1").commit();

        showhideFragment();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.navigation_home:
                    menu.findItem(R.id.navigation_home).setIcon(R.drawable.selected_home);
                    menu.findItem(R.id.navigation_notification).setIcon(R.drawable.unselected_notification);
                    menu.findItem(R.id.navigation_trend).setIcon(R.drawable.unselected_hashtag);
                    menu.findItem(R.id.navigation_profile).setIcon(R.drawable.unselected_profile);
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    if (boolScroll) {
                        if (HomeFragments.postRecyclerView != null) {
                            HomeFragments.postRecyclerView.scrollToPosition(0);
                        }
                    }
                    boolScroll = true;
                    return true;

                case R.id.navigation_trend:
                    boolScroll = false;
                    menu.findItem(R.id.navigation_home).setIcon(R.drawable.unselected_home);
                    menu.findItem(R.id.navigation_notification).setIcon(R.drawable.unselected_notification);
                    menu.findItem(R.id.navigation_trend).setIcon(R.drawable.selected_hashtag);
                    menu.findItem(R.id.navigation_profile).setIcon(R.drawable.unselected_profile);
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;

                case R.id.navigation_notification:
                    boolScroll = false;
                    fragment3 = new NotificationFragment();
                    fm.beginTransaction().add(R.id.nav_host_fragment, fragment3, "3").hide(fragment3).commit();
                    menu.findItem(R.id.navigation_home).setIcon(R.drawable.unselected_home);
                    menu.findItem(R.id.navigation_notification).setIcon(R.drawable.selected_notification);
                    menu.findItem(R.id.navigation_trend).setIcon(R.drawable.unselected_hashtag);
                    menu.findItem(R.id.navigation_profile).setIcon(R.drawable.unselected_profile);
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
                case R.id.navigation_profile:
                    boolScroll = false;
                    fragment4 = new MyAccountFrag();
                    fm.beginTransaction().add(R.id.nav_host_fragment, fragment4, "4").hide(fragment4).commit();
                    menu.findItem(R.id.navigation_home).setIcon(R.drawable.unselected_home);
                    menu.findItem(R.id.navigation_notification).setIcon(R.drawable.unselected_notification);
                    menu.findItem(R.id.navigation_trend).setIcon(R.drawable.unselected_hashtag);
                    menu.findItem(R.id.navigation_profile).setIcon(R.drawable.selected_profile);
                    fm.beginTransaction().hide(active).show(fragment4).commit();
                    active = fragment4;
                    return true;
            }
            return false;
        });

    }

    public void showhideFragment() {
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragment4, "4").hide(fragment4).commit();
//        fm.beginTransaction().add(R.id.nav_host_fragment, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, fragment1, "1").commit();
    }

    public void showTargetForMainActivity() {
        TapTargetSequence tapTargetSequence = new TapTargetSequence(this).targets(
                getTargetView(this, layout_post, getResources().getString(R.string.videoIntro), "", 40)
        ).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {

            }
        });
        tapTargetSequence.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        disposable.dispose();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String lang_eng = SharedPreferencesManager.getLanguage();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("story_upload");
        getCurrentVersion();
    }



    private void logoutNow() {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json");
        map.put("Content-Type", "application/json");
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
//        body.put("fcmToken", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), "token"));
        body.put("isOnline", "false");
        body.put("fcmToken", "");
        Call<ApiResponse> call = webApi.updateUserProfile(map, body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(SharedPrefreances.getSharedPreferenceString(MainActivity.this, SharedPrefreances.ID));
                // clearing stored data
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID, "");
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.PROFILE, "");
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "login", "0");
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "Profile", "");
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN, "");
                SocialPrefrences.setisLogin(MainActivity.this, false);
                SocialPrefrences.setisInterest(MainActivity.this, false);
                SocialPrefrences.setisVerified(MainActivity.this, false);
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.PROFILE_IMAGE, "");
                SocialPrefrences.setisLogin(MainActivity.this, false);
                SessionManager sessionManager = null;
                sessionManager = new SessionManager(MainActivity.this);
                sessionManager.saveBooleanValue(Const.IS_LOGIN, false);
                FirebaseMessaging.getInstance().deleteToken();
                Intent intent = new Intent(MainActivity.this, LoginSignUp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Logout_TAG", "e======================================" + "logout");
            }
        });

    }

    private void checkApiUth() {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json");
        map.put("Content-Type", "application/json");
        map.put("x-token", "");
        HashMap<String, String> body = new HashMap<>();
        body.put("isOnline", "false");
        body.put("fcmToken", "");

        Call<ApiResponse> call = webApi.updateAuthToken(map, body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(SharedPrefreances.getSharedPreferenceString(MainActivity.this, SharedPrefreances.ID));
                // clearing stored data
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID, "");
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.PROFILE, "");
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "login", "0");
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "Profile", "");
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN, "");
                SocialPrefrences.setisLogin(MainActivity.this, false);
                SocialPrefrences.setisInterest(MainActivity.this, false);
                SocialPrefrences.setisVerified(MainActivity.this, false);
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.PROFILE_IMAGE, "");
                SocialPrefrences.setisLogin(MainActivity.this, false);
                SessionManager sessionManager = null;
                sessionManager = new SessionManager(MainActivity.this);
                sessionManager.saveBooleanValue(Const.IS_LOGIN, false);
                FirebaseMessaging.getInstance().deleteToken();
                Intent intent = new Intent(MainActivity.this, LoginSignUp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Logout_TAG", "e======================================" + "logout");
            }
        });

    }

    private void deleteAccount() {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        HashMap<String, Object> body = new HashMap<>();
        body.put("id", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.ID));
        body.put("isDeleted", true);

        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);
        Call<ApiResponse> call1 = webApi1.delUserAccount(map, body);
        call1.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                try {
                    if (response.code() == 200 && response.body().getSuccess()) {
                        SharedPrefreances.setSharedPreferenceString(MainActivity.this, "login", "0");
                        Intent intent = new Intent(MainActivity.this, SplashScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Utilss.showToast(MainActivity.this, getString(R.string.error_msg), R.color.grey);
                    }
                } catch (Exception e) {
                    Utilss.showToast(MainActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.w("error", t);
                Utilss.showToast(MainActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void findIds() {
        img_search = findViewById(R.id.img_search);
        bottomNavigationView = findViewById(R.id.bottom_nav_meest);
        layout_my_account = findViewById(R.id.layout_my_account);
        layout_home = findViewById(R.id.layout_home);
        img_profile = findViewById(R.id.img_profile);
        layout_search = findViewById(R.id.layout_search);
        layout_post = findViewById(R.id.layout_post);
        layout_notification = findViewById(R.id.layout_notification);
        layout_bottom = findViewById(R.id.layout_bottom);
        layout_bottom_video = findViewById(R.id.layout_bottom_video);
        layout_home_video = findViewById(R.id.layout_home_video);
        layout_search_video = findViewById(R.id.layout_search_video);
        layout_post_video = findViewById(R.id.layout_post_video);
        layout_notification_video = findViewById(R.id.layout_notification_video);
        layout_my_account_video = findViewById(R.id.layout_my_account_video);
    }

    public boolean pushFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    // permission section........................
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (Permission.hasPermissions(MainActivity.this, permissions)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        statusCheck();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA_PERMISSION_CODE) {
            if (Permission.hasPermissions(MainActivity.this, permissions)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        openCameraSelectionDialog();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == VIDEO_PERMISSION_CODE) {
            if (Permission.hasPermissions(MainActivity.this, permissions)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        // Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                        intent.putExtra("type", "feed");
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == SVS_VIDEO_PERMISSION_CODE) {
            if (Permission.hasPermissions(MainActivity.this, permissions)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        // Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "main_data", "3");
                        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                        intent.putExtra("type", "svs");
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();
                }

            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (Permission.hasPermissions(MainActivity.this, permissions)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

//                        Intent intent = new Intent(MainActivity.this, CreateTextPostActivity.class);
                        Intent intent = new Intent(MainActivity.this, CreateTextPost.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void setNotificationCount(String count) {
        Log.e("TotalCount ", "Count  " + count);
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (count.equalsIgnoreCase("0")) {
                        tvNotificationCount.setVisibility(View.GONE);
                        if (itemView.getChildCount() == 3) {
//

                            itemView.removeView(badge);
                        }
                    } else if (count.equalsIgnoreCase("100")) {
                        tvNotificationCount.setText("99+");
                        tvNotificationCount.setVisibility(View.VISIBLE);
                        itemView.addView(badge);
                    } else {
                        tvNotificationCount.setText(count);
                        tvNotificationCount.setVisibility(View.VISIBLE);
                        itemView.addView(badge);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

    }

    protected Meeast application() {
        return (Meeast) getApplication();
    }

    private void openCameraSelectionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.camera_selection_dialog, viewGroup, false);
        LinearLayout dialogPhoto = dialogView.findViewById(R.id.dialog_photo);
        LinearLayout dialogVideo = dialogView.findViewById(R.id.dialog_video);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();

        dialogPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        dialogVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra("type", "feed");
                startActivity(intent);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later),
                        R.color.msg_fail);
            }

            if (!gps_enabled) {
                Toast.makeText(this, getString(R.string.Please_enable_location_services_first), Toast.LENGTH_LONG).show();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            } else {
                loadingDialog.show();
                SingleShotLocationProvider.requestSingleUpdate(this,
                        new SingleShotLocationProvider.LocationCallback() {
                            @Override
                            public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                                try {
                                    latitude = String.valueOf(location.latitude);
                                    longitude = String.valueOf(location.longitude);

                                    String url = Variables.local(latitude + "", longitude + "");
//                                    Intent i = new Intent(MainActivity.this, SearchLocationsActivity.class);
                                    Intent i = new Intent(MainActivity.this, SearchLocation.class);
                                    i.putExtra("imagePath", url);
                                    i.putExtra("type", "location");
                                    i.putExtra("lat", latitude);
                                    i.putExtra("log", longitude);
                                    startActivity(i);

//
//                                    String url = Variables.local(latitude + "", longitude + "");
//                                    Intent i = new Intent(MainActivity.this, PreviewPhotoActivity.class);
//                                    i.putExtra("imagePath", url);
//                                    i.putExtra("type", "location");
//                                    startActivity(i);
//


                                    loadingDialog.hide();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("YYYYYYY", "llllpoooooooooo=" + e.getMessage());
                                    loadingDialog.hide();
                                }
                            }
                        });
            }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.Your_GPS_seems_to_be_disabled_do_you_want_to_enable_it))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void onStart() {
        super.onStart();

        String uid = SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.ID);
        if (uid != null) {
            FirebaseMessaging.getInstance().subscribeToTopic(uid);
        }
    }

    private void changeCameraDistance() {
        int distance = 12000;
        float scale = getResources().getDisplayMetrics().density * distance;
        layout_bottom.setCameraDistance(scale);
        layout_bottom_video.setCameraDistance(scale);
    }

    private void loadAnimations() {
//        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.anim.out_animation);
//        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.anim.in_animation);
    }

    public void goToProfile() {
        img_search.setImageResource(R.drawable.unselected_search);
    }

    public void done(int a) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (a == 1) {
                    animZoomin1 = AnimationUtils.loadAnimation(MainActivity.this,
                            R.anim.zoom_in);
//                    bottom_nav_parent.startAnimation(animZoomin1);
                } else {
                    animZoomin1 = AnimationUtils.loadAnimation(MainActivity.this,
                            R.anim.zoom_in);
//                    bottom_nav_parent.startAnimation(animZoomin1);
                }
            }
        }, 2500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (a == 1) {
                    layout_bottom_video.setVisibility(View.VISIBLE);

                    mIsBackVisible = true;
                } else {
                    layout_bottom.setVisibility(View.VISIBLE);

                    mIsBackVisible = false;
                }
            }
        }, 900);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (a == 1) {
                    layout_bottom.setVisibility(View.GONE);
                } else {
                    layout_bottom_video.setVisibility(View.GONE);
                }
            }
        }, 1600);
    }

    @Override
    public void onBackPressed() {

        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_home) {
            super.onBackPressed();
//            finish();
        } else {
            drawer.closeDrawers();
            menu.findItem(R.id.navigation_home).setIcon(R.drawable.selected_home);
            menu.findItem(R.id.navigation_notification).setIcon(R.drawable.unselected_notification);
            menu.findItem(R.id.navigation_trend).setIcon(R.drawable.unselected_hashtag);
            menu.findItem(R.id.navigation_profile).setIcon(R.drawable.unselected_profile);
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 654 && resultCode == RESULT_OK) {
            if (data == null || data.getData() == null) {
//                Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later),
//                        R.color.msg_fail);
                return;
            }
            Uri mediaUri = data.getData();
            String selectedMediaPath = Constant.getPath(MainActivity.this, mediaUri);

            Intent intent = new Intent(this, PreviewPhotoActivity.class);
            intent.putExtra("imagePath", selectedMediaPath);
            intent.putExtra("isStory", false);
            startActivity(intent);
        }


        if (requestCode == PICK_MEDIA_CODE && resultCode == RESULT_OK) {
            if (data == null || data.getData() == null) {
//                Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later),
//                        R.color.msg_fail);
                return;
            }
            Uri mediaUri = data.getData();
            String selectedMediaPath = Constant.getPath(MainActivity.this, mediaUri);

            if (mediaUri.toString().contains("image")) {
                Intent intent = new Intent(MainActivity.this, NewPostUploadActivity.class);
                intent.putExtra("mediaPath", selectedMediaPath);
                intent.putExtra("isVideo", false);
                startActivity(intent);
            } else {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(MainActivity.this, mediaUri);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                if (time != null) {
                    long timeInMillisec = Long.parseLong(time);

                    if (timeInMillisec > 60000) {
                        Intent intent = new Intent(MainActivity.this, NewPostUploadActivity.class);
                        intent.putExtra("mediaPath", selectedMediaPath);
                        intent.putExtra("isVideo", true);
                        startActivity(intent);
                    } else {
                        Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later),
                                R.color.msg_fail);
                    }
                } else {
                    Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later),
                            R.color.msg_fail);
                }
                retriever.release();
            }

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void initAnimation() {

    }

    @Override
    public void changeMainView() {

    }

    @Override
    public void startAnimation(boolean isStart) {

    }

    //update app to latest version code
    private void getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        currentVersion = pInfo.versionName;

        new GetVersionCode().execute();
    }

    private void setdefaultApp(int str) {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));

        DefaultParam defaultParam = new DefaultParam(str);
        //  VerifyParam verifyParam1 = new  VerifyParam(edit_email_mobile.getText().toString());
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<DefaultAppResponse> call = webApi.defaultApp(map, defaultParam);
        call.enqueue(new Callback<DefaultAppResponse>() {
            @Override
            public void onResponse(Call<DefaultAppResponse> call, Response<DefaultAppResponse> response) {
                try {
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getCode() == 1) {
                            if (response.body().getData().getDefaultApp().equals("1")) {
                                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.WHEREFROM, "Video");
                                Toast.makeText(MainActivity.this, getString(R.string.Default_App_changed_to_Medley), Toast.LENGTH_SHORT).show();
                            } else {
                                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.WHEREFROM, "Social");

                                Toast.makeText(MainActivity.this, getString(R.string.Default_App_changed_to_Meest), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<DefaultAppResponse> call, Throwable t) {
                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    void permissionOverLayDialog() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                if ("xiaomi".equals(Build.MANUFACTURER.toLowerCase(Locale.ROOT))) {
                    final Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter",
                            "com.miui.permcenter.permissions.PermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", getPackageName());
                    new AlertDialog.Builder(this)
                            .setTitle("Please Enable the additional permissions")
                            .setMessage("You will not receive notifications while the app is in background if you disable these permissions")
                            .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(intent);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setCancelable(true)
                            .show();
                } else {
                    Intent overlaySettings = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(overlaySettings, REQUEST_CODE);
                }
            }
        }
    }

    public void updateToken() {
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Map<String, String> map = new HashMap<>();
                map.put("Accept", "application/json");
                map.put("Content-Type", "application/json");
                map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
                HashMap<String, String> body = new HashMap<>();
                body.put("isOnline", "true");
                body.put("fcmToken", instanceIdResult.getToken());
                Global.firebaseDeviceToken = instanceIdResult.getToken();
                apiCallBack(getApi().updateUserProfile(map, body));
            }
        });

    }

    public interface FragmentRefreshListener {
        void onRefresh();
    }

    private class GetVersionCode extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;
            try {
                currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            try {
                newVersion = Jsoup.connect(ApiUtils.PLAY_STORE_URL + getPackageName())
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        //  .select("div[itemprop=softwareVersion]")
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();
                return newVersion;
            } catch (Exception e) {
                return newVersion;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);
            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                int crrVersion = Integer.parseInt(currentVersion.replace(".", ""));
                int onVersion = Integer.parseInt(onlineVersion.replace(".", ""));
                if (crrVersion < onVersion) {
                    // Long currentTimeStamp = System.currentTimeMillis()/1000;
                    if (SharedPrefreances.getSharedPreferenceString(MainActivity.this, SharedPrefreances.APP_NEW_UPDATE_CHECK).equals("") || SharedPrefreances.getSharedPreferenceString(MainActivity.this, SharedPrefreances.APP_NEW_UPDATE_CHECK).equals("false")) {
                        UpdateAppDialog updateDialog = new UpdateAppDialog(MainActivity.this, getString(R.string.Update), getString(R.string.New_Version_of_the_Meest_App_is_available_please_update));
                        updateDialog.show();

                        //  String currentTimeStampString = currentTimeStamp.toString();
                        //  SharedPrefreances.setSharedPreferenceString(MainActivity.this, SharedPrefreances.APP_NEW_UPDATE, currentTimeStampString);
                        //   SharedPrefreances.setSharedPreferenceString(MainActivity.this, SharedPrefreances.APP_NEW_UPDATE_CHECK, "true");
                    } else if (SharedPrefreances.getSharedPreferenceString(MainActivity.this, SharedPrefreances.APP_NEW_UPDATE_CHECK).equals("true")) {
                        try {
                            // Long lastTimeStamp = Long.valueOf(SharedPrefreances.getSharedPreferenceString(MainActivity.this,SharedPrefreances.APP_NEW_UPDATE));
                            UpdateAppDialog updateDialog = new UpdateAppDialog(MainActivity.this, getString(R.string.Update), getString(R.string.new_version_update));
                            updateDialog.show();
                            //   SharedPrefreances.setSharedPreferenceString(MainActivity.this, SharedPrefreances.APP_NEW_UPDATE, currentTimeStamp.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    SharedPrefreances.setSharedPreferenceString(MainActivity.this, SharedPrefreances.APP_NEW_UPDATE_CHECK, "false");
                }
            }
        }
    }


}