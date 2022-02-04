package com.meest.metme.camera2;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.R;
import com.meest.mediapikcer.utils.PictureFacer;
import com.meest.metme.MetMeGallery;
import com.meest.metme.camera2.camera.AspectRatio;
import com.meest.metme.camera2.camera.Camera2Fragment;
import com.meest.metme.camera2.gallery.GalleryBottomSheet;
import com.meest.metme.camera2.gallery2.Camera2Gallery;
import com.meest.metme.camera2.preview.PreviewImageActivity;
import com.meest.metme.camera2.preview.PreviewVideoActivity;
import com.meest.metme.camera2.utills.CameraConstants;
import com.meest.metme.camera2.utills.CameraUtil;
import com.meest.metme.model.gallery.GalleryPhotoModel;
import com.meest.utils.ParameterConstants;

import org.apache.commons.io.FilenameUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

public class CameraActivity extends AppCompatActivity
        implements View.OnClickListener, SensorEventListener {
    private static final String TAG = "MainActivity";
    private static final String FRAGMENT_DIALOG = "aspect_dialog";

    private static final int[] FLASH_OPTIONS = {
            CameraConstants.FLASH_OFF,
            CameraConstants.FLASH_TORCH,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private int mCurrentFlashIndex = 0;
    private boolean boomActive = false;

    private Camera2Fragment mCamera2Fragment;

    private ImageView camera_close, switch_flash, aspect_ratio;
    private ImageView gallery, image, video, switch_camera;
    private CardView cardViewParentLayout;
    private ImageView gridButton, superZoomButton, superZoomRecord, handsFreeButton, handsFreeRecord, multiSnapButton, multiSnapCapture,
            multiCaptureNextButton, timerButton, timerButton3, timerButton10, cameraWithTimer, focusButton, speedButton, boomVideo, boomButton;
    private LinearLayout gridLineParent, handsFreeParentLayout, multiSnapParentLayout, superZoomParentLayout, showOptionsParentLayout,
            timerParentLayout, focusParentLayout, aspectRatioParentLayout, speedParentLayout, photoVideoToggleParent;
    private RelativeLayout handsFreeChildLayout, multiSnapChildLayout, superZoomChildLayout, timerChildLayout;

    private TextView gridTV, superZoomTV, layoutsTV, handsFreeTV, multiSnapTV, timerText, speedTV, focusTV, aspectRatioTV;

    private LinearLayout childSpeedLayout;
    LottieAnimationView animationView;


    private TextView captureImageSwitch, recordVideoSwitch, timerView;

    private CountDownTimer countDownTimer;

    RelativeLayout gridLines;

    private ImageView aspectRatio1_1, aspectRatio4_3, aspectRatio16_9, aspectRatioFullScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_camera2_main);

        disableOptions();


        gallery = findViewById(R.id.gallery);
        cardViewParentLayout = findViewById(R.id.cardViewParentLayout);
        mCamera2Fragment = Camera2Fragment.newInstance();
        getFragmentManager().beginTransaction()
                .replace(R.id.container, mCamera2Fragment)
                .commit();
//        if (null == savedInstanceState) {
//            Log.e(TAG, "IF Condition: ");
//
//        } else {
//            Log.e(TAG, "ELSE Condition: ");
//            mCamera2Fragment = (Camera2Fragment) getFragmentManager().findFragmentById(R.id.container);
//        }
        initViews();
        initGridLayout();
        initAspectRatios();
//        Handler handler = new Handler();
//        Runnable runnable = this::setAspectRatio;
//        handler.postDelayed(runnable, 1000);
        showLayouts();
        initSensor();
        videoProgressbar();
    }

    private void setAspectRatio() {
        final Set<AspectRatio> ratios = mCamera2Fragment.getSupportedAspectRatios();
        if (ratios != null) {
            if (ratios.size() > 0) {
                CameraUtil.fsAspectRatio = Collections.max(ratios);
                mCamera2Fragment.setAspectRatio(CameraUtil.fsAspectRatio);
            }
        }


    }

    private ProgressBar progressbarMC;

    private void initViews() {

        progressbarMC = findViewById(R.id.progressbarMC);
        boomParentLayout = findViewById(R.id.boomParentLayout);
        boomVideo = findViewById(R.id.boomVideo);
        boomButton = findViewById(R.id.boomButton);


        camera_close = findViewById(R.id.camera_close);
        switch_flash = findViewById(R.id.switch_flash);
        aspect_ratio = findViewById(R.id.aspect_ratio);

        gallery = findViewById(R.id.gallery);
        image = findViewById(R.id.picture);
        video = findViewById(R.id.video);
        switch_camera = findViewById(R.id.switch_camera);
        gridButton = findViewById(R.id.gridButton);
        gridLines = findViewById(R.id.gridLines);
        superZoomButton = findViewById(R.id.superZoomButton);
        superZoomRecord = findViewById(R.id.superZoomRecord);
        animationView = findViewById(R.id.animationView);

        gridLineParent = findViewById(R.id.gridLineParent);
        handsFreeParentLayout = findViewById(R.id.handsFreeParentLayout);
        multiSnapParentLayout = findViewById(R.id.multiSnapParentLayout);
        superZoomParentLayout = findViewById(R.id.superZoomParentLayout);
        timerParentLayout = findViewById(R.id.timerParentLayout);
        focusParentLayout = findViewById(R.id.focusParentLayout);
        aspectRatioParentLayout = findViewById(R.id.aspectRatioParentLayout);
        speedParentLayout = findViewById(R.id.speedParentLayout);
        photoVideoToggleParent = findViewById(R.id.photoVideoToggleParent);

        superZoomChildLayout = findViewById(R.id.superZoomChildLayout);
        timerChildLayout = findViewById(R.id.timerChildLayout);
        childSpeedLayout = findViewById(R.id.childSpeedLayout);
        handsFreeChildLayout = findViewById(R.id.handsFreeChildLayout);
        multiSnapChildLayout = findViewById(R.id.multiSnapChildLayout);

        handsFreeButton = findViewById(R.id.handsFreeButton);
        handsFreeRecord = findViewById(R.id.handsFreeRecord);

        multiSnapButton = findViewById(R.id.multiSnapButton);
        multiSnapCapture = findViewById(R.id.multiSnapCapture);

        multiCaptureNextButton = findViewById(R.id.multiCaptureNextButton);

        timerButton = findViewById(R.id.timerButton);
        timerButton3 = findViewById(R.id.timerButton3);
        timerButton10 = findViewById(R.id.timerButton10);

        cameraWithTimer = findViewById(R.id.cameraWithTimer);

        focusButton = findViewById(R.id.focusButton);

        speedButton = findViewById(R.id.speedButton);

        camera_close.setOnClickListener(this);
        switch_flash.setOnClickListener(this);
        aspect_ratio.setOnClickListener(this);

        gridButton.setOnClickListener(this);
        superZoomButton.setOnClickListener(this);
        superZoomRecord.setOnClickListener(this);
        handsFreeButton.setOnClickListener(this);
        handsFreeRecord.setOnClickListener(this);
        multiSnapButton.setOnClickListener(this);
        multiSnapCapture.setOnClickListener(this);
        cameraWithTimer.setOnClickListener(this);

        boomParentLayout.setOnClickListener(this);
        boomVideo.setOnClickListener(this);



        gallery.setOnClickListener(this);
        image.setOnClickListener(this);
        video.setOnClickListener(this);
        switch_camera.setOnClickListener(this);

        initHandsFreeImageView();

        captureImageSwitch = findViewById(R.id.captureImageSwitch);
        recordVideoSwitch = findViewById(R.id.recordVideoSwitch);
        findViewById(R.id.photoCapture).setOnClickListener(v -> {
            CameraUtil.currentCamera = "Photo";
            captureImageSwitch.setTextColor(getColor(R.color.white));
            image.setVisibility(View.VISIBLE);
            video.setVisibility(View.GONE);
            recordVideoSwitch.setTextColor(getColor(R.color.light_grey));
            switchOptions();

        });
        findViewById(R.id.videoRecord).setOnClickListener(v -> {

            //switchAnimation(recordVideoSwitch);
            CameraUtil.currentCamera = "Video";
            captureImageSwitch.setTextColor(getColor(R.color.light_grey));
            image.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);
            recordVideoSwitch.setTextColor(getColor(R.color.white));
            switchOptions();
        });

        timerView = findViewById(R.id.timerView);
        timerText = findViewById(R.id.timerText);

        timerButton.setOnClickListener(v -> {
            if (timerChildLayout.getVisibility() == View.GONE) {
                timerButton.setImageResource(R.drawable.ic_timer_cross);
                CameraUtil.timerEnable = true;
                timerButton3.setVisibility(View.VISIBLE);
                timerButton10.setVisibility(View.VISIBLE);
                timerChildLayout.setVisibility(View.VISIBLE);
                //timerText.setVisibility(View.GONE);

                hideLayouts();
                timerButton.setImageResource(R.drawable.ic_timer_selected);
            } else {
                timerButton.setImageResource(R.drawable.ic_timer_cross);
                CameraUtil.timerEnable = false;
                timerButton3.setVisibility(View.GONE);
                timerButton10.setVisibility(View.GONE);
                timerText.setVisibility(View.VISIBLE);

                timerChildLayout.setVisibility(View.GONE);
                showLayouts();
                timerButton.setImageResource(R.drawable.ic_timer_cross);
            }

        });

        timerButton3.setOnClickListener(v -> {
            timerValue = 4000;

            timerButton.setImageResource(R.drawable.ic_icon_3);
            timerButton3.setVisibility(View.GONE);
            timerButton10.setVisibility(View.GONE);

        });

        timerButton10.setOnClickListener(v -> {
            timerValue = 11000;
            timerButton.setImageResource(R.drawable.ic_icon_10);
            timerButton3.setVisibility(View.GONE);
            timerButton10.setVisibility(View.GONE);

        });

        focusButton.setOnClickListener(v -> {
            if (!CameraUtil.focusEnable) {
                CameraUtil.focusEnable = true;
                focusButton.setImageResource(R.drawable.ic_focus_selected);
            } else {
                CameraUtil.focusEnable = false;
                focusButton.setImageResource(R.drawable.ic_focus);
            }

        });

        speedButton.setOnClickListener(v -> {
            if (!CameraUtil.speedEnable) {
                CameraUtil.speedEnable = true;
                childSpeedLayout.setVisibility(View.VISIBLE);
                hideLayouts();
                speedButton.setImageResource(R.drawable.ic_speed_selected);
                video.setVisibility(View.VISIBLE);
            } else {
                CameraUtil.speed = "Normal";
                slowSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
                fastSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
                normalSpeed.setBackgroundResource(R.drawable.rounded_corner_selected);
                CameraUtil.speedEnable = false;
                childSpeedLayout.setVisibility(View.GONE);
                showLayouts();
                speedButton.setImageResource(R.drawable.ic_baseline_speed_24);
            }
        });

        slowSpeed = findViewById(R.id.slowSpeed);
        normalSpeed = findViewById(R.id.normalSpeed);
        fastSpeed = findViewById(R.id.fastSpeed);
        normalSpeed.setBackgroundResource(R.drawable.rounded_corner_selected);
        slowSpeed.setOnClickListener(v -> {
            CameraUtil.speed = "Slow";

            normalSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
            fastSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
            slowSpeed.setBackgroundResource(R.drawable.rounded_corner_selected);

        });

        normalSpeed.setOnClickListener(v -> {
            CameraUtil.speed = "Normal";

            slowSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
            fastSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
            normalSpeed.setBackgroundResource(R.drawable.rounded_corner_selected);

        });

        fastSpeed.setOnClickListener(v -> {
            CameraUtil.speed = "Fast";
            normalSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
            slowSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
            fastSpeed.setBackgroundResource(R.drawable.rounded_corner_selected);


        });


        initOptionTV();

    }

    private void switchAnimation(View view) {


    }


    Button slowSpeed, normalSpeed, fastSpeed;

    int timerValue = 0;

    private void initOptionTV() {
        gridTV = findViewById(R.id.gridTV);
        superZoomTV = findViewById(R.id.superZoomTV);
        layoutsTV = findViewById(R.id.layoutsTV);
        handsFreeTV = findViewById(R.id.handsFreeTV);
        multiSnapTV = findViewById(R.id.multiSnapTV);
        timerText = findViewById(R.id.timerText);
        speedTV = findViewById(R.id.speedTV);
        focusTV = findViewById(R.id.focusTV);
        aspectRatioTV = findViewById(R.id.aspectRatioTV);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //This is used to hide/show 'Status Bar' & 'System Bar'. Swip bar to get it as visible.
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | SYSTEM_UI_FLAG_FULLSCREEN
                            | SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private ImageView handsFreeImage1, handsFreeImage2, handsFreeImage3;

    private void initHandsFreeImageView() {
        handsFreeImage1 = findViewById(R.id.handsFreeImage1);
        handsFreeImage2 = findViewById(R.id.handsFreeImage2);
        handsFreeImage3 = findViewById(R.id.handsFreeImage3);
    }

    ArrayList<AspectRatio> ratios2 = new ArrayList<>();

    private void initAspectRatios() {
        aspectRatioTV = findViewById(R.id.aspectRatioTV);
        aspectRatio1_1 = findViewById(R.id.aspectRatio1_1);
        aspectRatio4_3 = findViewById(R.id.aspectRatio4_3);
        aspectRatio16_9 = findViewById(R.id.aspectRatio16_9);
        aspectRatioFullScreen = findViewById(R.id.aspectRatioFullScreen);

        clickEventsAspectRatio();
    }

    private void clickEventsAspectRatio() {
        aspectRatio1_1.setOnClickListener(v -> {
            //aspectRatioTV.setVisibility(View.GONE);
            CameraUtil.fsEnable = false;
            CameraUtil.aspectRatioEnable = false;
            Log.e(TAG, "aspectRatio: " + ratios2.get(0));
            mCamera2Fragment.setAspectRatio(ratios2.get(0));
            aspectRatio1_1.setVisibility(View.GONE);
            aspectRatio4_3.setVisibility(View.GONE);
            aspectRatio16_9.setVisibility(View.GONE);
            aspectRatioFullScreen.setVisibility(View.GONE);
            aspect_ratio.setImageResource(R.drawable.ic_1_1);
        });

        aspectRatio4_3.setOnClickListener(v -> {
            CameraUtil.fsEnable = false;
            CameraUtil.aspectRatioEnable = false;
            Log.e(TAG, "aspectRatio: " + ratios2.get(1));
            mCamera2Fragment.setAspectRatio(ratios2.get(1));
            aspectRatio1_1.setVisibility(View.GONE);
            aspectRatio4_3.setVisibility(View.GONE);
            aspectRatio16_9.setVisibility(View.GONE);
            aspectRatioFullScreen.setVisibility(View.GONE);

            aspect_ratio.setImageResource(R.drawable.ic_4_3);
        });

        aspectRatio16_9.setOnClickListener(v -> {
            CameraUtil.fsEnable = false;
            CameraUtil.aspectRatioEnable = false;
            mCamera2Fragment.setAspectRatio(CameraConstants.DEFAULT_ASPECT_RATIO);
            Log.e(TAG, "aspectRatio: " + ratios2.get(2));
            aspectRatio1_1.setVisibility(View.GONE);
            aspectRatio4_3.setVisibility(View.GONE);
            aspectRatio16_9.setVisibility(View.GONE);
            aspectRatioFullScreen.setVisibility(View.GONE);

            aspect_ratio.setImageResource(R.drawable.ic_16_9);
        });
        aspectRatioFullScreen.setOnClickListener(v -> {
            // CameraUtil.fsEnable = true;
            CameraUtil.aspectRatioEnable = false;
            mCamera2Fragment.setAspectRatio(ratios2.get(3));
            Log.e(TAG, "aspectRatio: " + ratios2.get(3));
            aspectRatio1_1.setVisibility(View.GONE);
            aspectRatio4_3.setVisibility(View.GONE);
            aspectRatio16_9.setVisibility(View.GONE);
            aspectRatioFullScreen.setVisibility(View.GONE);
            aspect_ratio.setImageResource(R.drawable.ic_full_screen);
        });
    }

    private void setFullScreen() {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION |
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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

    public static GalleryBottomSheet bottomSheet;
    int LAUNCH_SECOND_ACTIVITY = 1231;
    String callType = "";

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camera_close: {
                finish();
                break;
            }
            case R.id.gallery: {
                Intent intent = new Intent(this, MetMeGallery.class);
                intent.putExtra("whereFrom", "camera");
                intent.putExtra("Mediatype", CameraUtil.currentCamera);
                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
                break;
            }

            case R.id.picture: {
                mCamera2Fragment.takePicture();
                break;
            }
            case R.id.video: {

                if (mCamera2Fragment.handlerRunning)
                {
                    mCamera2Fragment.mHandler.removeMessages(0);
                }
                if (mCamera2Fragment.isRecordingVideo()) {
                    mCamera2Fragment.stopRecordingVideo();
                    video.setImageResource(R.drawable.circle_thumb);

                } else {
                    mCamera2Fragment.startRecordingVideo();
                    video.setImageResource(R.drawable.circle_thumb);

                }


                invalidateOptionsMenu();
                break;
            }

            case R.id.aspect_ratio:
                if (ratios2 != null) {
                    ratios2.clear();
                }
                if (!CameraUtil.aspectRatioEnable) {
                    CameraUtil.aspectRatioEnable = true;
                    final Set<AspectRatio> ratios = mCamera2Fragment.getSupportedAspectRatios();
                    CameraUtil.fsAspectRatio = Collections.max(ratios);
//                    final AspectRatio currentRatio = mCamera2Fragment.getAspectRatio()
                    for (AspectRatio ratio : ratios) {

                        if (ratio.toString().equalsIgnoreCase("1:1") || ratio.toString().equalsIgnoreCase("4:3") || ratio.toString().equalsIgnoreCase("16:9")) {
                            ratios2.add(ratio);

                        }
                    }
                    ratios2.add(CameraUtil.fsAspectRatio);
                    Collections.sort(ratios2);
                    //aspectRatioTV.setVisibility(View.GONE);
                    aspectRatio1_1.setVisibility(View.VISIBLE);
                    aspectRatio4_3.setVisibility(View.VISIBLE);
                    aspectRatio16_9.setVisibility(View.VISIBLE);
                    aspectRatioFullScreen.setVisibility(View.VISIBLE);

                } else {
                    CameraUtil.aspectRatioEnable = false;
                    aspectRatio1_1.setVisibility(View.GONE);
                    aspectRatio4_3.setVisibility(View.GONE);
                    aspectRatio16_9.setVisibility(View.GONE);
                    aspectRatioFullScreen.setVisibility(View.GONE);
                    // aspectRatioTV.setVisibility(View.VISIBLE);
                }


                break;
            case R.id.switch_flash:
                Log.e(TAG, "mCurrentFlashIndex: " + mCurrentFlashIndex);
                mCurrentFlashIndex = (mCurrentFlashIndex + 1) % FLASH_OPTIONS.length;
                switch_flash.setImageResource(FLASH_ICONS[mCurrentFlashIndex]);
                mCamera2Fragment.setFlash(FLASH_OPTIONS[mCurrentFlashIndex]);
                break;
            case R.id.switch_camera:

                int facing = mCamera2Fragment.getFacing();
                mCamera2Fragment.setFacing(facing == CameraConstants.FACING_FRONT ?
                        CameraConstants.FACING_BACK : CameraConstants.FACING_FRONT);
                invalidateOptionsMenu();
                break;

            case R.id.gridButton:
                if (gridLines.getVisibility() == View.GONE) {
                    gridLines.setVisibility(View.VISIBLE);
                    CameraUtil.gridCamera = true;
                    sensorMan.registerListener(this, accelerometer,
                            SensorManager.SENSOR_DELAY_UI);
                    hideLayouts();
                    gridButton.setImageResource(R.drawable.ic_grid_selected);
                    photoVideoToggleParent.setVisibility(View.GONE);
                    if (CameraUtil.currentCamera.equalsIgnoreCase("Photo"))
                        image.setVisibility(View.VISIBLE);
                    else
                        video.setVisibility(View.VISIBLE);

                } else {
                    gridLines.setVisibility(View.GONE);
                    CameraUtil.gridCamera = false;
                    sensorMan.unregisterListener(this);
                    gridButton.setImageResource(R.drawable.ic_baseline_grid_on_24);
                    showLayouts();

                }
                invalidateOptionsMenu();
                break;
            case R.id.superZoomButton:
                if (!CameraUtil.superZoomEnable) {
                    CameraUtil.superZoomEnable = true;

                    superZoomChildLayout.setVisibility(View.VISIBLE);
                    hideLayouts();
                    superZoomButton.setImageResource(R.drawable.ic_superzoom_selected);
                } else {
                    CameraUtil.superZoomEnable = false;
                    superZoomChildLayout.setVisibility(View.GONE);

                    showLayouts();
                    superZoomButton.setImageResource(R.drawable.superzoom);
                }
                invalidateOptionsMenu();
                break;
            case R.id.superZoomRecord:
                if (mCamera2Fragment.isRecordingVideo()) {
                    mCamera2Fragment.zoomHandler.removeCallbacksAndMessages(null);
                    mCamera2Fragment.stopRecordingVideo();
                } else {
                    mCamera2Fragment.startRecordingVideo();

                }


                invalidateOptionsMenu();
                break;
            case R.id.handsFreeButton:
                if (!CameraUtil.handsFreeEnable) {

                    CameraUtil.handsFreeEnable = true;
                    handsFreeChildLayout.setVisibility(View.VISIBLE);
                    mCamera2Fragment.handsFreeRecordMethod();
                    hideLayouts();
                    handsFreeButton.setImageResource(R.drawable.ic_handfree_selected);
                } else {
                    handsFreeChildLayout.setVisibility(View.GONE);
                    CameraUtil.handsFreeEnable = false;
                    showLayouts();
                    if (mCamera2Fragment.handsFreeVideos != null) {
                        mCamera2Fragment.handsFreeVideos.clear();
                    }
                    handsFreeButton.setImageResource(R.drawable.handsfree);

                }
                invalidateOptionsMenu();
                break;
            case R.id.multiSnapButton:
                if (!CameraUtil.multiSnapEnable) {

                    CameraUtil.multiSnapEnable = true;
                    multiSnapChildLayout.setVisibility(View.VISIBLE);
                    hideLayouts();
                    multiSnapButton.setImageResource(R.drawable.multi_snap_selected);

                } else {

                    CameraUtil.multiSnapEnable = false;
                    multiSnapChildLayout.setVisibility(View.GONE);
                    showLayouts();
                    if (mCamera2Fragment.multiSnapList != null) {
                        mCamera2Fragment.multiSnapList.clear();
                        clearMultiCaptureThumbs();
                    }
                    multiSnapButton.setImageResource(R.drawable.multisnap);
                    multiSnapCapture.setEnabled(true);
                    progressbarMC.setProgress(0);
                }
                invalidateOptionsMenu();
                break;
            case R.id.multiSnapCapture:
                mCamera2Fragment.takePicture();
                invalidateOptionsMenu();
                break;
            case R.id.cameraWithTimer:
                if (mCamera2Fragment.isRecordingVideo()) {
                    mCamera2Fragment.stopRecordingVideo();
                }
                if (CameraUtil.isRunningTimer) {
                    countDownTimer.cancel();
                    timerView.setText("");
                    CameraUtil.isRunningTimer = false;
                    timerChildLayout.setVisibility(View.GONE);

                } else {
                    countDownTimer = new CountDownTimer(timerValue, 1000) {
                        public void onTick(long millisUntilFinished) {
                            timerView.setText("" + millisUntilFinished / 1000);
                            CameraUtil.isRunningTimer = true;
                            //here you can have your logic to set text to edittext
                        }

                        public void onFinish() {
                            CameraUtil.isRunningTimer = false;
                            timerView.setText("0");
                            timerView.setText("");
                            if (CameraUtil.currentCamera.equalsIgnoreCase("Photo"))
                                mCamera2Fragment.takePicture();
                            else {
                                if (mCamera2Fragment.isRecordingVideo())
                                    mCamera2Fragment.stopRecordingVideo();
                                else
                                    mCamera2Fragment.startRecordingVideo();

                            }
                        }

                    }.start();
                }
                invalidateOptionsMenu();
                break;

            case R.id.boomParentLayout:
                if(boomActive){
                    boomActive = false;
                    boomVideo.setVisibility(View.GONE);
                    video.setVisibility(View.VISIBLE);
                    showLayouts();
                    boomButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_boomerang));

                }
                else {
                    boomActive = true;
                    video.setVisibility(View.GONE);
                    hideLayouts();
                    boomVideo.setVisibility(View.VISIBLE);
                    boomButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.boomerang_selected));
                }
                break;

            case R.id.boomVideo:

                mCamera2Fragment.startRecordingVideo();
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCamera2Fragment.stopRecordingBoomVideo();
                    }
                }, 1500);

        }
    }

    //Grid Layout
    RelativeLayout gridLayoutChild, gridCaptureLayout;
    ImageView gridCameraClick, checkImage, gridLayout;

    ImageView twoGridLayout, threeGridLayout, fourGridLayout, sixGridLayout, twoGridLayoutHorizontal;

    LinearLayout gidParentLayout, gridLayoutList, boomParentLayout;
    ProgressBar progressBar;

    private void initGridLayout() {
        progressBar = findViewById(R.id.progressBar);

        gidParentLayout = findViewById(R.id.gidParentLayout);
        gridLayout = findViewById(R.id.gridLayout);
        gridCameraClick = findViewById(R.id.cameraClick);
        gridLayoutList = findViewById(R.id.gridLayoutList);
        gridCaptureLayout = findViewById(R.id.gridCaptureLayout);
        gridCameraClick = findViewById(R.id.cameraClick);

        checkImage = findViewById(R.id.checkImage);

        twoGridLayout = findViewById(R.id.twoGridLayout);
        threeGridLayout = findViewById(R.id.threeGridLayout);
        fourGridLayout = findViewById(R.id.fourGridLayout);
        sixGridLayout = findViewById(R.id.sixGridLayout);
        twoGridLayoutHorizontal = findViewById(R.id.twoGridLayoutHorizontal);

        gridLayoutChild = findViewById(R.id.gridLayoutChild);
        initOptionsLayouts();
        gridLayoutClicks();
    }

    private final Handler stopVideoRecordingHandler = new Handler();
    private Runnable runnable;
    private final int timeLimit = 6000;

    private void gridLayoutClicks() {
        gridLayout.setOnClickListener(v -> {
            if (!CameraUtil.gridLayoutActive) {
                CameraUtil.gridLayoutActive = true;
                //gridLayout.setBackgroundResource(R.drawable.selected_grid_layout);
                gridLayoutList.setVisibility(View.VISIBLE);
                if (CameraUtil.currentCamera.equalsIgnoreCase("Video")) {
                    twoGridLayout.setVisibility(View.GONE);
                    threeGridLayout.setVisibility(View.GONE);
                    fourGridLayout.setVisibility(View.GONE);
                    sixGridLayout.setVisibility(View.GONE);
                } else {
                    twoGridLayout.setVisibility(View.VISIBLE);
                    threeGridLayout.setVisibility(View.VISIBLE);
                    fourGridLayout.setVisibility(View.VISIBLE);
                    sixGridLayout.setVisibility(View.VISIBLE);
                }
                gridLayoutChild.setVisibility(View.VISIBLE);

                hideLayouts();
                gridLayout.setImageResource(R.drawable.ic_layout_selected);

            } else {
                //gridLayout.setBackgroundResource(R.drawable.gridlayout);
                gridLayoutChild.setVisibility(View.GONE);
                gridLayoutList.setVisibility(View.GONE);
                CameraUtil.gridLayoutActive = false;

                hideGridLayout();
                showLayouts();
                gridCaptureLayout.setVisibility(View.GONE);

                gridLayout.setImageResource(R.drawable.gridlayout);
            }
        });

        gridCameraClick.setOnClickListener(v -> {
            Log.e(TAG, "CameraUtil.twoGridValue : " + CameraUtil.twoGridValue);

            if (CameraUtil.gridLayout2Active) {
                if (CameraUtil.twoGridValue == 2) {
                    if (CameraUtil.previewComplete) {
                        Intent intent = new Intent(this, PreviewImageActivity.class);
                        intent.putExtra("screenshotImage", "screenshotImage");
                        intent.putExtra("whereFrom", getIntent().getStringExtra("whereFrom"));
                        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);

                    } else {
                        Toast.makeText(this, "Please wait image is processing...", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mCamera2Fragment.takePicture();
                }
            }
            if (CameraUtil.gridLayoutHVideoActive) {
                if (CameraUtil.twoGridValue == 2) {
                    Intent intent = new Intent(this, PreviewVideoActivity.class);
                    intent.putStringArrayListExtra("mergeVideo", mCamera2Fragment.mergeVideo);
                    intent.putExtra("duetType", 0);
                    startActivityForResult(intent, ParameterConstants.PICKER);
                } else {

                    checkImage.setVisibility(View.GONE);
                    gridCameraClick.setEnabled(false);
                    runnable = () -> {
                        if (mCamera2Fragment.isRecordingVideo()) {
                            mCamera2Fragment.stopRecordingVideo();
                        }
                        checkImage.setVisibility(View.VISIBLE);
                        gridCameraClick.setEnabled(true);
                    };
                    stopVideoRecordingHandler.postDelayed(runnable, timeLimit);
                    if (CameraUtil.twoGridValue < 2)
                        mCamera2Fragment.startRecordingVideo();
                }
            } else if (CameraUtil.gridLayout3Active) {
                if (CameraUtil.threeGridValue == 3) {
                    if (CameraUtil.previewComplete) {
                        Intent intent = new Intent(this, PreviewImageActivity.class);
                        intent.putExtra("screenshotImage", "screenshotImage");
                        startActivityForResult(intent, ParameterConstants.PICKER);
                    } else {
                        Toast.makeText(this, "Please wait image is processing...", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mCamera2Fragment.takePicture();
                }
            } else if (CameraUtil.gridLayout4Active) {
                if (CameraUtil.fourGridValue == 4) {
                    if (CameraUtil.previewComplete) {
                        Intent intent = new Intent(this, PreviewImageActivity.class);
                        intent.putExtra("screenshotImage", "screenshotImage");
                        startActivityForResult(intent, ParameterConstants.PICKER);
                    } else {
                        Toast.makeText(this, "Please wait image is processing...", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mCamera2Fragment.takePicture();
                }
            } else if (CameraUtil.gridLayout6Active) {
                if (CameraUtil.sixGridValue == 6) {
                    if (CameraUtil.previewComplete) {
                        Intent intent = new Intent(this, PreviewImageActivity.class);
                        intent.putExtra("screenshotImage", "screenshotImage");
                        startActivityForResult(intent, ParameterConstants.PICKER);
                    } else {
                        Toast.makeText(this, "Please wait image is processing...", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mCamera2Fragment.takePicture();
                }
            } else if (CameraUtil.gridLayout2ActiveHorizontal) {
                if (CameraUtil.twoGridValueHorizontal == 2) {
                    if (CameraUtil.previewComplete) {
                        Intent intent = new Intent(this, PreviewImageActivity.class);
                        intent.putExtra("screenshotImage", "screenshotImage");
                        startActivityForResult(intent, ParameterConstants.PICKER);
                    } else {
                        Toast.makeText(this, "Please wait image is processing...", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mCamera2Fragment.takePicture();
                }
            }

        });

        twoGridLayout.setOnClickListener(v -> {
            gridLayoutList.setVisibility(View.GONE);
            gridCaptureLayout.setVisibility(View.VISIBLE);
            mCamera2Fragment.gridLayoutTwo();
        });

        threeGridLayout.setOnClickListener(v -> {
            gridLayoutList.setVisibility(View.GONE);
            gridCaptureLayout.setVisibility(View.VISIBLE);
            mCamera2Fragment.gridLayoutThree();
        });

        fourGridLayout.setOnClickListener(v -> {
            gridLayoutList.setVisibility(View.GONE);
            gridCaptureLayout.setVisibility(View.VISIBLE);
            mCamera2Fragment.gridLayoutFour();
        });

        sixGridLayout.setOnClickListener(v -> {
            gridLayoutList.setVisibility(View.GONE);
            gridCaptureLayout.setVisibility(View.VISIBLE);
            mCamera2Fragment.gridLayoutSix();
        });

        twoGridLayoutHorizontal.setOnClickListener(v -> {
            gridLayoutList.setVisibility(View.GONE);
            gridCaptureLayout.setVisibility(View.VISIBLE);
            if (CameraUtil.currentCamera.equalsIgnoreCase("Photo"))
                mCamera2Fragment.gridLayoutTwoHorizontal();
            else {

                mCamera2Fragment.gridLayoutTwoVideoH();
            }
        });

    }

    private void initOptionsLayouts() {

        handsFreeParentLayout = findViewById(R.id.handsFreeParentLayout);
        multiSnapParentLayout = findViewById(R.id.multiSnapParentLayout);

    }

    //Code for Hide the Layouts
    private void hideLayouts() {

        if (CameraUtil.currentCamera.equalsIgnoreCase("Video")) {
            CameraUtil.progressTimer = false;
            progressTimerParent.setVisibility(View.GONE);
            progressBarTimer.setVisibility(View.GONE);
        }

        if (gallery.getVisibility() == View.VISIBLE) {
            gallery.setVisibility(View.GONE);
            cardViewParentLayout.setVisibility(View.GONE);
        }


        if (image.getVisibility() == View.VISIBLE)
            image.setVisibility(View.GONE);

        if (video.getVisibility() == View.VISIBLE)
            video.setVisibility(View.GONE);

        if (!CameraUtil.gridLayoutActive)
            gidParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.superZoomEnable)
            superZoomParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.gridCamera)
            gridLineParent.setVisibility(View.GONE);

        if (!CameraUtil.handsFreeEnable)
            handsFreeParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.multiSnapEnable)
            multiSnapParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.timerEnable)
            timerParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.focusEnable)
            focusParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.aspectRatioEnable)
            aspectRatioParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.speedEnable)
            speedParentLayout.setVisibility(View.GONE);

        if (!boomActive)
            boomParentLayout.setVisibility(View.GONE);


        photoVideoToggleParent.setVisibility(View.GONE);
    }

    private void showLayouts() {
        photoVideoToggleParent.setVisibility(View.VISIBLE);
        cardViewParentLayout.setVisibility(View.VISIBLE);
        gallery.setVisibility(View.VISIBLE);

        if (CameraUtil.currentCamera.equalsIgnoreCase("Photo")) {
//            gridLineParent.setVisibility(View.VISIBLE);
//            gidParentLayout.setVisibility(View.VISIBLE);
//            aspectRatioParentLayout.setVisibility(View.VISIBLE);
//            multiSnapParentLayout.setVisibility(View.VISIBLE);
//            timerParentLayout.setVisibility(View.VISIBLE);
//            focusParentLayout.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);

        } else {
            video.setVisibility(View.VISIBLE);
//            gridLineParent.setVisibility(View.VISIBLE);
//            gidParentLayout.setVisibility(View.VISIBLE);
//            aspectRatioParentLayout.setVisibility(View.VISIBLE);
//            superZoomParentLayout.setVisibility(View.VISIBLE);
//            handsFreeParentLayout.setVisibility(View.VISIBLE);
//            timerParentLayout.setVisibility(View.VISIBLE);
//            speedParentLayout.setVisibility(View.VISIBLE);
//            focusParentLayout.setVisibility(View.VISIBLE);
//            CameraUtil.progressTimer = true;
//            progressTimerParent.setVisibility(View.VISIBLE);
//            progressBarTimer.setVisibility(View.VISIBLE);
//            boomParentLayout.setVisibility(View.VISIBLE);

        }

    }

    private void switchOptions() {
        if (CameraUtil.currentCamera.equalsIgnoreCase("Photo")) {
            multiSnapParentLayout.setVisibility(View.VISIBLE);
            superZoomParentLayout.setVisibility(View.GONE);
            handsFreeParentLayout.setVisibility(View.GONE);
            speedParentLayout.setVisibility(View.GONE);
            progressTimerParent. setVisibility(View.GONE);
            progressBarTimer.setVisibility(View.GONE);
            boomParentLayout.setVisibility(View.GONE);
        } else {
            multiSnapParentLayout.setVisibility(View.GONE);
            superZoomParentLayout.setVisibility(View.VISIBLE);
            handsFreeParentLayout.setVisibility(View.VISIBLE);
            speedParentLayout.setVisibility(View.VISIBLE);

            //progressTimerParent. setVisibility(View.VISIBLE);
            //progressBarTimer.setVisibility(View.VISIBLE);

            boomParentLayout.setVisibility(View.VISIBLE);

        }
    }

    void hideGridLayout() {
        if (mCamera2Fragment.grid2Layout.getVisibility() == View.VISIBLE)
            mCamera2Fragment.grid2Layout.setVisibility(View.GONE);
        if (mCamera2Fragment.grid3Layout.getVisibility() == View.VISIBLE)
            mCamera2Fragment.grid3Layout.setVisibility(View.GONE);

        if (mCamera2Fragment.grid4Layout.getVisibility() == View.VISIBLE)
            mCamera2Fragment.grid4Layout.setVisibility(View.GONE);

        if (mCamera2Fragment.grid6Layout.getVisibility() == View.VISIBLE)
            mCamera2Fragment.grid6Layout.setVisibility(View.GONE);

        if (mCamera2Fragment.grid2LayoutHorizontal.getVisibility() == View.VISIBLE)
            mCamera2Fragment.grid2LayoutHorizontal.setVisibility(View.GONE);

        clearGridValues();
    }

    private void clearGridValues() {
        //Grid 2 Layout
        CameraUtil.twoGridValue = 0;
        CameraUtil.gridLayout2Active = false;

        //Grid 3 Layout
        CameraUtil.threeGridValue = 0;
        CameraUtil.gridLayout3Active = false;

        //Grid 6 Layout
        CameraUtil.fourGridValue = 0;
        CameraUtil.gridLayout4Active = false;

        //Grid 6 Layout
        CameraUtil.sixGridValue = 0;
        CameraUtil.gridLayout6Active = false;

        //Grid 2 Layout Horizontal
        CameraUtil.twoGridValueHorizontal = 0;
        CameraUtil.gridLayout2ActiveHorizontal = false;

        //Grid 2 Video Layout Horizontal
        CameraUtil.gridLayoutHVideoActive = false;


        checkImage.setImageResource(0);
        checkImage.setImageResource(R.drawable.ic_outline_add_24);

        progressBar.setProgress(0);


        ObjectAnimator anim1 = ObjectAnimator.ofFloat(mCamera2Fragment.mTextureView, "translationY", (float) Camera2Fragment.screenHeight / 2, 0);
        anim1.setDuration(200);
        anim1.start();

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(mCamera2Fragment.mTextureView, "translationX", (float) Camera2Fragment.screenWidth / 2, 0);
        anim2.setDuration(200);
        anim2.start();


    }


    private void clearMultiCaptureThumbs() {
        multiCaptureNextButton.setVisibility(View.GONE);
        mCamera2Fragment.multiCaptureImage1.setImageResource(0);
        mCamera2Fragment.multiCaptureImage2.setImageResource(0);
        mCamera2Fragment.multiCaptureImage3.setImageResource(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // getImagesFromGallery();
    }

    //Sensor Code:-

    private SensorManager sensorMan;
    private Sensor accelerometer;

    private float[] mGravity;
    private ImageView balanceImage;

    private void initSensor() {
        balanceImage = findViewById(R.id.balanceImage);
        sensorMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        balanceImage.setColorFilter(ContextCompat.getColor(this, R.color.yellow_green_color_picker), android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            int min = 0;
            //Log.e(TAG, "min: "+min );
            int max = 3;

            int value1 = Math.round(x);
            int value = absVal(value1);
            Log.e(TAG, "value: " + value);
            if (value >= min && value < max)
                balanceImage.setColorFilter(ContextCompat.getColor(this, R.color.yellow_green_color_picker), android.graphics.PorterDuff.Mode.MULTIPLY);
            else
                balanceImage.setColorFilter(ContextCompat.getColor(this, R.color.red_color_picker), android.graphics.PorterDuff.Mode.MULTIPLY);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }

    private int absVal(int value) {
        if (value < 0)
            value = (-1 * value);
        return value;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void disableOptions() {
        if (CameraUtil.bitmap != null) {
            CameraUtil.bitmap.recycle();
            CameraUtil.bitmap = null;
        }
        CameraUtil.currentCamera = "Photo";

        CameraUtil.twoGridValue = 0;
        CameraUtil.threeGridValue = 0;
        CameraUtil.fourGridValue = 0;
        CameraUtil.sixGridValue = 0;
        CameraUtil.twoGridValueHorizontal = 0;

        CameraUtil.previewComplete = false;
        CameraUtil.gridLayoutActive = false;

        CameraUtil.gridLayout2Active = false;
        CameraUtil.gridLayout3Active = false;
        CameraUtil.gridLayout4Active = false;
        CameraUtil.gridLayout6Active = false;
        CameraUtil.gridLayout2ActiveHorizontal = false;

        CameraUtil.gridLayoutHVideoActive = false;


        CameraUtil.gridCamera = false;
        CameraUtil.superZoomEnable = false;
        CameraUtil.handsFreeEnable = false;
        CameraUtil.multiSnapEnable = false;
        CameraUtil.timerEnable = false;
        CameraUtil.isRunningTimer = false;
        CameraUtil.speedEnable = false;
        CameraUtil.aspectRatioEnable = false;


        CameraUtil.focusEnable = false;
        CameraUtil.speed = "Normal";
        CameraUtil.imageUri = null;

    }

    List<PictureFacer> selectedItem;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        String typemedia = "";

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<GalleryPhotoModel> result = data.getParcelableArrayListExtra("result");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", result);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        if (requestCode == ParameterConstants.PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String path = data.getStringExtra("result");
                    String ext = FilenameUtils.getExtension(path);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", path);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                //ArrayList<GalleryPhotoModel> result = data.getParcelableArrayListExtra("result");
            }
        }


    }

    //Horizontal Progressbar for Video


    private TextView timer5, timer10, timer15;
    LinearLayout progressTimerParent;
    ProgressBar progressBarTimer;
    private void videoProgressbar() {
        CameraUtil.progressTimer = true;
        progressTimerParent = findViewById(R.id.progressTimerParent);
        progressBarTimer = findViewById(R.id.progressbar);
        timer5 = findViewById(R.id.timer5);
        timer10 = findViewById(R.id.timer10);
        timer15 = findViewById(R.id.timer15);

        timer5.setOnClickListener(v -> {
            mCamera2Fragment.progressTimer = 60;
            CameraUtil.progressTimer = true;

            timer5.setBackgroundResource(R.drawable.bg_black_corner_5);
            timer10.setBackgroundResource(R.drawable.bg_white_corner_5);
            timer15.setBackgroundResource(R.drawable.bg_white_corner_5);

            timer5.setTextColor(getResources().getColor(R.color.white));
            timer10.setTextColor(getResources().getColor(R.color.black));
            timer15.setTextColor(getResources().getColor(R.color.black));

        });
        timer10.setOnClickListener(v -> {
            mCamera2Fragment.progressTimer = 110;
            CameraUtil.progressTimer = true;
            timer10.setBackgroundResource(R.drawable.bg_black_corner_5);
            timer5.setBackgroundResource(R.drawable.bg_white_corner_5);
            timer15.setBackgroundResource(R.drawable.bg_white_corner_5);

            timer10.setTextColor(getResources().getColor(R.color.white));
            timer5.setTextColor(getResources().getColor(R.color.black));
            timer15.setTextColor(getResources().getColor(R.color.black));
        });
        timer15.setOnClickListener(v -> {
            mCamera2Fragment.progressTimer = 160;
            CameraUtil.progressTimer = true;
            timer15.setBackgroundResource(R.drawable.bg_black_corner_5);
            timer5.setBackgroundResource(R.drawable.bg_white_corner_5);
            timer10.setBackgroundResource(R.drawable.bg_white_corner_5);

            timer15 .setTextColor(getResources().getColor(R.color.white));
            timer5.setTextColor(getResources().getColor(R.color.black));
            timer10.setTextColor(getResources().getColor(R.color.black));
        });
    }


}
