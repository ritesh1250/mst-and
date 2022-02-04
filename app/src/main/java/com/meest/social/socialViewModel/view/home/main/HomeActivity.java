package com.meest.social.socialViewModel.view.home.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

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
import com.meest.Activities.ActivityOption;
import com.meest.Activities.CampaignActivity;
import com.meest.Activities.ColletionActivity;
import com.meest.Activities.HelpActivity;
import com.meest.Activities.InviteFriendActivity;
//import com.meest.Activities.NewPostUploadActivity;
import com.meest.social.socialViewModel.view.NewPostUploadActivity;
import com.meest.Activities.SettingActivity;
import com.meest.Activities.VideoPost.CameraActivity;
import com.meest.Activities.base.ApiCallActivity;
import com.meest.Activities.preview.PreviewPhotoActivity;
import com.meest.Insights.InsightsTabActivity;
import com.meest.R;
import com.meest.UpdateAppDialog;
import com.meest.databinding.ActivityMainModelBinding;
import com.meest.databinding.AppBarNewModelBinding;
import com.meest.databinding.NavHeaderMainMdoelBinding;
import com.meest.databinding.NewContainMainModelBinding;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.social.socialViewModel.view.createTextPost.CreateTextPost;
import com.meest.social.socialViewModel.view.searchLocation.SearchLocation;
import com.meest.social.socialViewModel.viewModel.navigationViewModel.mainViewModel.MainActivityViewModel;
import com.meest.utils.LoadingDialog;
import com.meest.utils.Permission;
import com.meest.utils.SingleShotLocationProvider;
import com.meest.utils.Variables;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;
import static com.meest.meestbhart.utilss.SharedPrefreances.HOME_INTRO;
import static com.meest.social.socialViewModel.utils.ParameterConstant.Permission.CAMERA_PERMISSION_CODE;
import static com.meest.social.socialViewModel.utils.ParameterConstant.Permission.PERMISSION_CODE;
import static com.meest.social.socialViewModel.utils.ParameterConstant.Permission.PICK_MEDIA_CODE;
import static com.meest.social.socialViewModel.utils.ParameterConstant.Permission.REQUEST_LOCATION;
import static com.meest.social.socialViewModel.utils.ParameterConstant.Permission.STORAGE_PERMISSION_CODE;
import static com.meest.social.socialViewModel.utils.ParameterConstant.Permission.SVS_VIDEO_PERMISSION_CODE;
import static com.meest.social.socialViewModel.utils.ParameterConstant.Permission.VIDEO_PERMISSION_CODE;
import static com.meest.utils.goLiveUtils.CommonUtils.getTargetView;

public class HomeActivity extends ApiCallActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainModelBinding activityMainModelBinding;
    AppBarNewModelBinding appBarNewModelBinding;
    NewContainMainModelBinding newContainMainModelBinding;
    NavHeaderMainMdoelBinding navHeaderMainMdoelBinding;
    MainActivityViewModel model;
    Menu menu;
    View badge;
    TextView tvNotificationCount;
    BottomNavigationView bottomNavigationView;
    BottomNavigationItemView itemView;
    private NavController navController;
    LoadingDialog loadingDialog;
    RelativeLayout layout_post;
    String latitude, longitude, currentVersion;
    private CompositeDisposable disposable = new CompositeDisposable();
    public static final String STORY_UPLOAD = "story_upload";
    public static final String POST_UPLOAD = "post_upload";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainModelBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_model);
        appBarNewModelBinding = DataBindingUtil.setContentView(this, R.layout.app_bar_new_model);
        newContainMainModelBinding = DataBindingUtil.setContentView(this, R.layout.new_contain_main_model);
        navHeaderMainMdoelBinding = DataBindingUtil.setContentView(this, R.layout.nav_header_main_mdoel);
        model = new ViewModelProvider(this, new ViewModelFactory(new MainActivityViewModel(newContainMainModelBinding)).createFor()).get(MainActivityViewModel.class);
        activityMainModelBinding.setMainModel(model);
        appBarNewModelBinding.setAppbarModel(model);
        newContainMainModelBinding.setNewContainModel(model);
        navHeaderMainMdoelBinding.setNavMainModel(model);
        initViews();
    }


    private void initViews() {
        setSupportActionBar(appBarNewModelBinding.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        }
        updateToken();
        permissionOverLayDialog();
        inItNavigation();
        MaterialShapeDrawable navViewBackground = (MaterialShapeDrawable) activityMainModelBinding.navView.getBackground();
        navViewBackground.setShapeAppearanceModel(
                navViewBackground.getShapeAppearanceModel()
                        .toBuilder()
                        .setBottomLeftCorner(CornerFamily.ROUNDED, 100)
                        .setTopLeftCorner(CornerFamily.ROUNDED, 100)
                        .build());
        activityMainModelBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        activityMainModelBinding.navView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                HomeActivity.this, activityMainModelBinding.drawerLayout, appBarNewModelBinding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityMainModelBinding.drawerLayout.addDrawerListener(toggle);
        if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.WHEREFROM).equalsIgnoreCase("Video")) {
            navHeaderMainMdoelBinding.switchMedly.setChecked(true);
            navHeaderMainMdoelBinding.switchMeest.setChecked(false);
        } else {
            navHeaderMainMdoelBinding.switchMeest.setChecked(true);
            navHeaderMainMdoelBinding.switchMedly.setChecked(false);
        }

        navHeaderMainMdoelBinding.switchMeest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    navHeaderMainMdoelBinding.switchMedly.setChecked(false);

                    model.setDefault(0,HomeActivity.this);
                } else {
                    navHeaderMainMdoelBinding.switchMedly.setChecked(true);
                }
            }
        });

        navHeaderMainMdoelBinding.switchMedly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    navHeaderMainMdoelBinding.switchMeest.setChecked(false);
                    model.setDefault(1,HomeActivity.this);
                } else {
                    navHeaderMainMdoelBinding.switchMeest.setChecked(true);
                }
            }
        });
        getCurrentVersion();
        navHeaderMainMdoelBinding.layoutCollection.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ColletionActivity.class);
            startActivity(intent);
            activityMainModelBinding.drawerLayout.closeDrawers();
        });
        navHeaderMainMdoelBinding.layoutActivity.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ActivityOption.class);
            startActivity(intent);
            activityMainModelBinding.drawerLayout.closeDrawers();
        });
        navHeaderMainMdoelBinding.layoutInsights.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, InsightsTabActivity.class);
            startActivity(intent);
            activityMainModelBinding.drawerLayout.closeDrawers();
        });
        navHeaderMainMdoelBinding.layoutInviteFriends.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, InviteFriendActivity.class);
            startActivity(intent);
            activityMainModelBinding.drawerLayout.closeDrawers();
        });
        navHeaderMainMdoelBinding.layoutHelp.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
            startActivity(intent);
            activityMainModelBinding.drawerLayout.closeDrawers();
        });
        navHeaderMainMdoelBinding.layoutManage.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CampaignActivity.class);
            startActivity(intent);
            activityMainModelBinding.drawerLayout.closeDrawers();
        });
        navHeaderMainMdoelBinding.layoutSetting.setOnClickListener(v -> {
            SharedPreferences sharedPreferences;
            sharedPreferences = getSharedPreferences("Theme", Context.MODE_PRIVATE);
            String themeName = sharedPreferences.getString("ThemeName", "Default");
            Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
            startActivity(intent);
            activityMainModelBinding.drawerLayout.closeDrawers();
        });
        navHeaderMainMdoelBinding.layoutLogout.setOnClickListener(v -> {
            model.logoutNow();
        });
        navHeaderMainMdoelBinding.layoutDeleteAccount.setOnClickListener(v -> {
            deleteDialog();
        });

    }

    private void deleteDialog() {
        View conView = getLayoutInflater().inflate(R.layout.delete_account_confirmation_second, null);
        Button btnConfirm = conView.findViewById(R.id.btnConfirm);
        Button btnClose = conView.findViewById(R.id.btnClose);
        BottomSheetDialog dia = new BottomSheetDialog(HomeActivity.this);
        dia.setContentView(conView);
        btnClose.setOnClickListener(v -> {
            dia.dismiss();
        });
        btnConfirm.setOnClickListener(v -> {
            model.deleteAccount();
        });
        dia.show();

    }


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

    private void inItNavigation() {
        menu = bottomNavigationView.getMenu();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        badge = LayoutInflater.from(this).inflate(R.layout.custom_badge_layout, bottomNavigationView, false);
        tvNotificationCount = badge.findViewById(R.id.tvNotificationCount);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                case R.id.navigation_home:
                    menu.findItem(R.id.navigation_home).setIcon(R.drawable.selected_home);
                    menu.findItem(R.id.navigation_notification).setIcon(R.drawable.unselected_notification);
                    menu.findItem(R.id.navigation_trend).setIcon(R.drawable.unselected_hashtag);
                    menu.findItem(R.id.navigation_profile).setIcon(R.drawable.unselected_profile);
                    break;
                case R.id.navigation_trend:
                    menu.findItem(R.id.navigation_home).setIcon(R.drawable.unselected_home);
                    menu.findItem(R.id.navigation_notification).setIcon(R.drawable.unselected_notification);
                    menu.findItem(R.id.navigation_trend).setIcon(R.drawable.selected_hashtag);
                    menu.findItem(R.id.navigation_profile).setIcon(R.drawable.unselected_profile);
                    break;
                case R.id.navigation_notification:
                    menu.findItem(R.id.navigation_home).setIcon(R.drawable.unselected_home);
                    menu.findItem(R.id.navigation_notification).setIcon(R.drawable.selected_notification);
                    menu.findItem(R.id.navigation_trend).setIcon(R.drawable.unselected_hashtag);
                    menu.findItem(R.id.navigation_profile).setIcon(R.drawable.unselected_profile);
                    break;
                case R.id.navigation_profile:
                    menu.findItem(R.id.navigation_home).setIcon(R.drawable.unselected_home);
                    menu.findItem(R.id.navigation_notification).setIcon(R.drawable.unselected_notification);
                    menu.findItem(R.id.navigation_trend).setIcon(R.drawable.unselected_hashtag);
                    menu.findItem(R.id.navigation_profile).setIcon(R.drawable.selected_profile);
                    break;
            }
        });
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

    private void permissionOverLayDialog() {
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

    @Override
    protected void onResume() {
        super.onResume();
        String lan_change = SocialPrefrences.getLang(this);
        model.updatelanguage(lan_change);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("story_upload");
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
        menu = bottomNavigationView.getMenu();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        itemView = bottomNavigationView.findViewById(R.id.navigation_notification);
        badge = LayoutInflater.from(this).inflate(R.layout.custom_badge_layout, bottomNavigationView, false);
        tvNotificationCount = badge.findViewById(R.id.tvNotificationCount);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (Permission.hasPermissions(HomeActivity.this, permissions)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        statusCheck();
                    }
                } else {
                    Utilss.showToast(HomeActivity.this, getString(R.string.Permission_Denied), R.color.social_background_blue);
                }
            }
        } else if (requestCode == CAMERA_PERMISSION_CODE) {
            if (Permission.hasPermissions(HomeActivity.this, permissions)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        openCameraSelectionDialog();
                    }
                } else {
                    Utilss.showToast(HomeActivity.this, getString(R.string.Permission_Denied), R.color.social_background_blue);
                }
            }
        } else if (requestCode == VIDEO_PERMISSION_CODE) {
            if (Permission.hasPermissions(HomeActivity.this, permissions)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        // Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
                        intent.putExtra("type", "feed");
                        startActivity(intent);
                    }
                } else {
                    Utilss.showToast(HomeActivity.this, getString(R.string.Permission_Denied), R.color.social_background_blue);
                }
            }
        } else if (requestCode == SVS_VIDEO_PERMISSION_CODE) {
            if (Permission.hasPermissions(HomeActivity.this, permissions)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        // Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "main_data", "3");
                        Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
                        intent.putExtra("type", "svs");
                        startActivity(intent);
                    }
                } else {
                    Utilss.showToast(HomeActivity.this, getString(R.string.Permission_Denied), R.color.social_background_blue);
                }

            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (Permission.hasPermissions(HomeActivity.this, permissions)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

//                        Intent intent = new Intent(HomeActivity.this, CreateTextPostActivity.class);
                        Intent intent = new Intent(HomeActivity.this, CreateTextPost.class);
                        startActivity(intent);
                    }
                } else {
                    Utilss.showToast(HomeActivity.this, getString(R.string.Permission_Denied), R.color.social_background_blue);
                }

            }
        }
    }

    private void openCameraSelectionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.camera_selection_dialog, viewGroup, false);
        LinearLayout dialogPhoto = dialogView.findViewById(R.id.dialog_photo);
        LinearLayout dialogVideo = dialogView.findViewById(R.id.dialog_video);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();

        dialogPhoto.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        dialogVideo.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
            intent.putExtra("type", "feed");
            startActivity(intent);
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    private void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;

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
//                                    Intent i = new Intent(HomeActivity.this, SearchLocationsActivity.class);
                                    Intent i = new Intent(HomeActivity.this, SearchLocation.class);

                                    i.putExtra("imagePath", url);
                                    i.putExtra("type", "location");
                                    i.putExtra("lat", latitude);
                                    i.putExtra("log", longitude);
                                    startActivity(i);
                                    loadingDialog.hide();
                                } catch (Exception e) {
                                    e.printStackTrace();
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

    public void showTargetForMainActivity() {
        TapTargetSequence tapTargetSequence = new TapTargetSequence(this).targets(
                getTargetView(this, layout_post, getResources().getString(R.string.videoIntro), "", 40)
//
        ).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                SharedPrefreances.setSharedPreferenceString(HomeActivity.this, HOME_INTRO, "false");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 654 && resultCode == RESULT_OK) {
            if (data == null || data.getData() == null) {
                Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later),
                        R.color.msg_fail);
                return;
            }
            Uri mediaUri = data.getData();
            String selectedMediaPath = Constant.getPath(HomeActivity.this, mediaUri);

            Intent intent = new Intent(this, PreviewPhotoActivity.class);
            intent.putExtra("imagePath", selectedMediaPath);
            intent.putExtra("isStory", false);
            startActivity(intent);
        }


        if (requestCode == PICK_MEDIA_CODE && resultCode == RESULT_OK) {
            if (data == null || data.getData() == null) {
                Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later),
                        R.color.msg_fail);
                return;
            }
            Uri mediaUri = data.getData();
            String selectedMediaPath = Constant.getPath(HomeActivity.this, mediaUri);
            if (mediaUri.toString().contains("image")) {
                Intent intent = new Intent(HomeActivity.this, NewPostUploadActivity.class);
                intent.putExtra("mediaPath", selectedMediaPath);
                intent.putExtra("isVideo", false);
                startActivity(intent);
            } else {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(HomeActivity.this, mediaUri);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                if (time != null) {
                    long timeInMillisec = Long.parseLong(time);

                    if (timeInMillisec > 60000) {
                        Intent intent = new Intent(HomeActivity.this, NewPostUploadActivity.class);
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

    //get current version by asyntask
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
                    if (SharedPrefreances.getSharedPreferenceString(HomeActivity.this, SharedPrefreances.APP_NEW_UPDATE_CHECK).equals("") || SharedPrefreances.getSharedPreferenceString(HomeActivity.this, SharedPrefreances.APP_NEW_UPDATE_CHECK).equals("false")) {
                        UpdateAppDialog updateDialog = new UpdateAppDialog(HomeActivity.this, getString(R.string.Update), getString(R.string.New_Version_of_the_Meest_App_is_available_please_update));
                        updateDialog.show();
                    } else if (SharedPrefreances.getSharedPreferenceString(HomeActivity.this, SharedPrefreances.APP_NEW_UPDATE_CHECK).equals("true")) {
                        try {
                            UpdateAppDialog updateDialog = new UpdateAppDialog(HomeActivity.this, getString(R.string.Update), getString(R.string.new_version_update));
                            updateDialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    SharedPrefreances.setSharedPreferenceString(HomeActivity.this, SharedPrefreances.APP_NEW_UPDATE_CHECK, "false");
                }
            }
        }
    }
}
