package com.meest.videomvvmmodule.view.home;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.meest.meestbhart.utilss.SharedPrefreances.MEDLEY_INTRO;
import static com.meest.utils.goLiveUtils.CommonUtils.getTargetView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;
import com.meest.R;
import com.meest.databinding.ActivityMainVideoBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.networkcheck.OnNoInternetRetry;

import com.meest.videomvvmmodule.SplashScreenVideoActivity;
import com.meest.videomvvmmodule.adapter.MainViewPagerAdapter;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.GlobalApi;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.utils.TimerTask;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.recordvideo.Utilss;
import com.meest.videomvvmmodule.viewmodel.MainViewModel;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class MainVideoActivity extends BaseActivity implements OnNoInternetRetry {
    static int CAMERA = 101;
    public CustomDialogBuilder customDialogBuilder;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
    private ActivityMainVideoBinding binding;
    private MainViewModel viewModel;
    private TimerTask timerTask;
    int LOCATION_SETTINGS_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStatusBarTransparentFlag();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_video);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Global.accessToken = "";

        getLocation();
        startReceiver();
        initView();
        initObserve();

        if (timerTask != null) {
            timerTask.doTimerTask();
        }

        rewardDailyCheckIn();
        binding.setViewModel(viewModel);

        if (ConnectionUtils.isConnected(MainVideoActivity.this)) {
            initTabLayout();
        } else {
            ConnectionUtils.showNoConnectionDialog(MainVideoActivity.this, this);
        }
    }

    public void showTargetForMainVideoActivity() {
        TapTargetSequence tapTargetSequence = new TapTargetSequence(this).targets(
                getTargetView(this, findViewById(R.id.layout_flip), getResources().getString(R.string.socialIntro), "", 40)
        ).listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                SharedPrefreances.setSharedPreferenceString(MainVideoActivity.this, MEDLEY_INTRO, "false");
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

    private void initObserve() {
        customDialogBuilder = new CustomDialogBuilder(MainVideoActivity.this);
        customDialogBuilder.showLoadingDialog();
    }

    private void initTabLayout() {
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager(),
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this, customDialogBuilder, viewModel.count);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(4);
    }

    @SuppressLint("NonConstantResourceId")
    private void initView() {
        timerTask = new TimerTask();
        binding.tvUser.setText(SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.GETINTENT_USER));
        binding.constraintLayout.setBackgroundResource(R.color.transparent);
        binding.bottomNav.setItemIconTintList(null);
        binding.bottomNav.setOnItemSelectedListener(item -> {

            viewModel.onStop.postValue(binding.bottomNav.getSelectedItemId() != R.id.action_home);

            switch (item.getItemId()) {
                case R.id.action_home:
                    setStatusBarTransparentFlag();
                    viewModel.selectedPosition.setValue(0);
                    binding.viewPager.setCurrentItem(0);

                    return true;
                case R.id.action_search:

                    if (new SessionManager(MainVideoActivity.this).getBooleanValue(Const.IS_LOGIN)) {
                        removeStatusBarTransparentFlag();
                        viewModel.selectedPosition.setValue(1);
                        binding.viewPager.setCurrentItem(1);

                    } else {
                        Utilss.callLoginSign(MainVideoActivity.this);
                    }

                    return true;
                case R.id.action_notification:

                    if (new SessionManager(MainVideoActivity.this).getBooleanValue(Const.IS_LOGIN)) {
                        removeStatusBarTransparentFlag();
                        viewModel.selectedPosition.setValue(2);
                        binding.viewPager.setCurrentItem(2);

                    } else {
                        Utilss.callLoginSign(MainVideoActivity.this);
                    }

                    return true;
                case R.id.action_profile:
                    if (new SessionManager(MainVideoActivity.this).getBooleanValue(Const.IS_LOGIN)) {
                        removeStatusBarTransparentFlag();

                        binding.viewPager.setCurrentItem(3);
                        viewModel.selectedPosition.setValue(3);

                    } else {
                        Utilss.callLoginSign(MainVideoActivity.this);
                    }

                    return true;
            }
            return false;
        });

        binding.layoutFlip.setOnClickListener(v -> {
            if (new SessionManager(MainVideoActivity.this).getBooleanValue(Const.IS_LOGIN)) {
                viewModel.count = 0;
                Intent intent = new Intent(MainVideoActivity.this, SplashScreenVideoActivity.class);
                intent.putExtra("whereFrom", "video");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            } else {
                Utilss.callLoginSign(MainVideoActivity.this);
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CAMERA) {
            viewModel.selectedPosition.setValue(0);
            binding.viewPager.setCurrentItem(0);
            binding.constraintLayout.setBackgroundResource(R.color.transparent);
        }
    }

    private void rewardDailyCheckIn() {
        if (sessionManager.getStringValue("inTime").isEmpty()) {
            new GlobalApi().rewardUser("2");
            sessionManager.saveStringValue("inTime", new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date()));
        } else {
            try {
                simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                Date date1 = simpleDateFormat.parse(sessionManager.getStringValue("inTime"));
                Date date2 = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                long difference = 0;
                if (date2 != null) {
                    if (date1 != null) {
                        difference = date2.getTime() - date1.getTime();
                    }
                }
                int days = (int) (difference / (1000 * 60 * 60 * 24));
                int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                hours = (hours < 0 ? -hours : hours);
                if (hours >= 24) {
                    new GlobalApi().rewardUser("2");
                    sessionManager.saveStringValue("inTime", new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        viewModel.onStop.setValue(true);
        if (timerTask != null) {
            timerTask.stopTimerTask();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        viewModel.onStop.setValue(true);
        if (timerTask != null) {
            timerTask.stopTimerTask();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterNetworkChanges();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        long size = 0;
        File[] files = getExternalCacheDir().listFiles();
        if (files != null) {
            for (File f : files) {
                size = size + f.length();
            }
        }


        if (binding.bottomNav.getSelectedItemId() == R.id.action_home) {
            viewModel.onStop.setValue(false);
        }
        if (timerTask != null) {
            timerTask.doTimerTask();
        }
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        if (viewModel.selectedPosition.getValue() != null && viewModel.selectedPosition.getValue() != 0) {

            binding.bottomNav.setSelectedItemId(R.id.action_home);

            viewModel.selectedPosition.setValue(0);
            binding.viewPager.setCurrentItem(0);
            viewModel.selectedPosition.setValue(0);
            binding.constraintLayout.setBackgroundResource(R.color.transparent_black_10);
        } else if (viewModel.selectedPosition.getValue() != null && viewModel.selectedPosition.getValue() == 0 && !viewModel.isBack) {
            viewModel.isBack = true;
            Toast.makeText(this, getString(R.string.Press_Again_To_Exit), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> viewModel.isBack = false, 2000);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRetry() {
        if (ConnectionUtils.isConnected(this)) {
            initTabLayout();
        } else {
            ConnectionUtils.showNoConnectionDialog(MainVideoActivity.this, this);
        }
    }

    public void getLocation() {
        if (!canGetLocation()) {
            Toast.makeText(this, "Please Enable Location", Toast.LENGTH_SHORT).show();
            displayLocationSettingsRequest();
        } else {
            requestLocationUpdates();
        }
    }


    public void displayLocationSettingsRequest() {

        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1000);

        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        settingsBuilder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(settingsBuilder.build());

        result.addOnCompleteListener(task -> {
            try {
                task.getResult(ApiException.class);
            } catch (ApiException ex) {
                switch (ex.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Timber.i("All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        ResolvableApiException resolvableApiException =
                                (ResolvableApiException) ex;
                        try {
                            resolvableApiException
                                    .startResolutionForResult(MainVideoActivity.this,
                                            LOCATION_SETTINGS_REQUEST);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;
                }
            }
        });

    }

    private void requestLocationUpdates() {

        LocationRequest request = LocationRequest.create();
        request.setInterval(2000);
        request.setNumUpdates(6);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    Global.post_lat = location.getLatitude();
                    Global.post_lng = location.getLongitude();
                }
            });
        }
    }

    public boolean canGetLocation() {
        int locationMode = 0;
        try {
            locationMode = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }
}
