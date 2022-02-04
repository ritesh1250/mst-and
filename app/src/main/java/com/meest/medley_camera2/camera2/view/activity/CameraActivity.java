package com.meest.medley_camera2.camera2.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraCharacteristics;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.meest.R;
import com.meest.databinding.ActivityCamera2MedlyBinding;
import com.meest.medley_camera2.camera2.cameraUtils.AspectRatio;
import com.meest.medley_camera2.camera2.utills.CameraConstants;
import com.meest.medley_camera2.camera2.utills.CameraUtil;
import com.meest.medley_camera2.camera2.utills.SoundEventListener;
import com.meest.medley_camera2.camera2.view.fragment.Camera2Fragment;
import com.meest.medley_camera2.camera2.viewmodels.CameraActivityViewModel;
import com.meest.videomvvmmodule.AudioExtractor;
import com.meest.videomvvmmodule.gallery.GalleryFoldersActivityMedley;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.UriUtils;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.music.MusicFrameFragment;
import com.meest.videomvvmmodule.view.music_bottomsheet.MusicSheetFragment;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.TranscoderOptions;
import com.otaliastudios.transcoder.engine.TrackType;
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategy;
import com.otaliastudios.transcoder.strategy.size.AtMostResizer;
import com.otaliastudios.transcoder.strategy.size.FractionResizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class CameraActivity extends BaseActivity
        implements SensorEventListener, SoundEventListener {
    private static final String TAG = "MainActivity";
    String callType = "video", uri;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    boolean sizeGreaterThan300 = false;
    String path = "";
    float minResolution = 0;

    public static final int[] FLASH_OPTIONS = {
            CameraConstants.FLASH_OFF,
            CameraConstants.FLASH_TORCH,
    };

    public static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    public static int mCurrentFlashIndex = 0;

    private Camera2Fragment mCamera2Fragment;

    private CountDownTimer countDownTimer;

    public ActivityCamera2MedlyBinding binding;

    CameraActivityViewModel viewModel;

    int timerValue = 0;
    ArrayList<AspectRatio> ratios2 = new ArrayList<>();

    private final Handler stopVideoRecordingHandler = new Handler();
    private Runnable runnable;
    private final int timeLimit = 6000;

    int LAUNCH_SECOND_ACTIVITY = 1231;

    public ArrayList<String> videoPaths = new ArrayList<>();

    private SensorManager sensorMan;
    private Sensor accelerometer;

    float[] mGravity;
    String format;

    private CustomDialogBuilder customDialogBuilder;
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        CameraUtil.comeFrom = "Medley";
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera2_medly);
        customDialogBuilder = new CustomDialogBuilder(CameraActivity.this);
        CameraUtil.oneTime = true;
//        setContentView(R.layout.activity_camera2_main);
        format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        mCamera2Fragment = Camera2Fragment.newInstance();
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new CameraActivityViewModel(this, CameraActivity.this, binding, mCamera2Fragment)).createFor()).get(CameraActivityViewModel.class);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, mCamera2Fragment)
                .commit();
        initViews();
        gridLayoutClicks();
        viewModel.showLayouts();
        initSensor();
        videoProgressbar();
        String type = getIntent().getType();
        if (type != null) {
            if (type.startsWith("video/")) {
                Uri videoUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
                path = UriUtils.getPathFromUri(this, videoUri);
                assert path != null;
                handleSendVideo(path);
            }
        }
        //viewModel = ViewModelProviders.of(this, new ViewModelFactory(new CameraActivityViewModel(this, CameraActivity.this,binding,mCamera2Fragment )).createFor()).get(CameraActivityViewModel.class);
        binding.setViewModel(viewModel);
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        binding.gallery.performClick();
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });

        viewModel.disableOptions();

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        if (data != null && data.getBooleanExtra("is_preview", false)) {
                            finish();
                        }
//                            doSomeOperations();
                    }
                });


    }

    private void handleSendVideo(String uri) {
        String fileExtension = "";
        int i = uri.lastIndexOf('.');
        if (i > 0) {
            fileExtension = uri.substring(i + 1);
        }
        if (fileExtension.equalsIgnoreCase("mp4") || fileExtension.equalsIgnoreCase("mkv") || fileExtension.equalsIgnoreCase("3gp") || fileExtension.equalsIgnoreCase("mov") || fileExtension.equalsIgnoreCase("webm")) {
            if (!uri.isEmpty()) {
                File file = new File(String.valueOf(uri));
                // Get length of file in bytes
                long fileSizeInBytes = file.length();
                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB = fileSizeInBytes / 1024;
                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                long fileSizeInMB = fileSizeInKB / 1024;

                if (fileSizeInMB > 300) {
                    sizeGreaterThan300 = true;
                }
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(this, Uri.fromFile(new File(uri)));
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                minResolution = Math.min(Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)), Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)));
                long timeInMilliSec = Long.parseLong(time);
//                Log.e("timeInMilliSec", "=======================" + timeInMilliSec);
                if (timeInMilliSec < 4900) {
                    customDialogBuilder.showSimpleDialog(getResources().getString(R.string.Video_Too_Short), getResources().getString(R.string.This_video_is_shorter_than_5_seconds_Please_select_another),
                            getResources().getString(R.string.cancel), getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {
                                @Override
                                public void onPositiveDismiss() {
                                    finish();
                                }

                                @Override
                                public void onNegativeDismiss() {
                                }
                            });
                } else if (timeInMilliSec >= 60200) {
                    customDialogBuilder.showSimpleDialog(getResources().getString(R.string.Video_Too_Long), getResources().getString(R.string.This_video_is_longer_than_1_min_Please_select_another),
                            getResources().getString(R.string.cancel), getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {

                                @Override
                                public void onPositiveDismiss() {
                                    finish();
                                }

                                @Override
                                public void onNegativeDismiss() {

                                }
                            });
                } else if (fileSizeInMB < 500) {
                    viewModel.videoPaths = new ArrayList<>();
                    viewModel.videoPaths.add(uri);
                    saveData(fileSizeInMB > 5);
                } else {
                    customDialogBuilder.showSimpleDialog(getResources().getString(R.string.Too_long_video_s_size), getResources().getString(R.string.This_video_s_size_is_greater_than_500MbPlease_select_another),
                            getResources().getString(R.string.cancel), getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {
                                @Override
                                public void onPositiveDismiss() {
                                    finish();
                                }

                                @Override
                                public void onNegativeDismiss() {
                                }
                            });
                }
                retriever.release();
            }
        } else {
            Toast.makeText(this, getString(R.string.Please_Select_mp4_format_video), Toast.LENGTH_SHORT).show();
        }
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

    private void initViews() {
        String musicUrl = getIntent().getStringExtra("music_url");
        Log.e("TAG", "initView: " + getIntent().getStringExtra("music_url"));
        Log.e("TAG", "initView: " + getIntent().getStringExtra("music_title"));
        Log.e("TAG", "initView: " + getIntent().getStringExtra("soundId"));

        if (musicUrl != null && !musicUrl.isEmpty()) {
            viewModel.downLoadMusic(getIntent().getStringExtra("music_url"));
            if (getIntent().getStringExtra("music_title") != null) {
                binding.tvSoundTitle.setVisibility(View.VISIBLE);
                binding.cardViewParentLayout.setVisibility(View.GONE);
                binding.llGalleryLayout.setVisibility(View.GONE);
                binding.tvSoundTitle.setText(getIntent().getStringExtra("music_title"));
//                binding.llVideoTiming.setVisibility(View.GONE);
//                binding.divider15.setVisibility(View.GONE);
//                binding.divider30.setVisibility(View.GONE);
//                binding.divider45.setVisibility(View.GONE);
//                binding.divider60.setVisibility(View.GONE);
            }
            if (getIntent().getStringExtra("soundId") != null) {
                viewModel.soundId = getIntent().getStringExtra("soundId");
            }
        }
//        if (binding.tvSoundTitle.getText().toString().trim().equalsIgnoreCase("")) {
//            binding.llVideoTiming.setVisibility(View.VISIBLE);
//        } else {
//            binding.llVideoTiming.setVisibility(View.GONE);
//        }
        binding.segmentedProgressBar.enableAutoProgressView(0);
        binding.normalSpeed.setBackgroundResource(R.drawable.rounded_corner_selected);
        layoutClickEvents();
        clickEventsAspectRatio();

    }

    private void clickEventsAspectRatio() {
        binding.aspectRatio11.setOnClickListener(v -> {
            //aspectRatioTV.setVisibility(View.GONE);
            CameraUtil.fsEnable = false;
            CameraUtil.aspectRatioEnable = false;
            Log.e(TAG, "aspectRatio: " + ratios2.get(0));
            mCamera2Fragment.setAspectRatio(ratios2.get(0));
            binding.aspectRatio11.setVisibility(View.GONE);
            binding.aspectRatio43.setVisibility(View.GONE);
            binding.aspectRatio169.setVisibility(View.GONE);
            binding.aspectRatioFullScreen.setVisibility(View.GONE);
            binding.aspectRatio.setImageResource(R.drawable.ic_1_1);
        });

        binding.aspectRatio43.setOnClickListener(v ->
        {
            CameraUtil.fsEnable = false;
            CameraUtil.aspectRatioEnable = false;
            Log.e(TAG, "aspectRatio: " + ratios2.get(1));
            mCamera2Fragment.setAspectRatio(ratios2.get(1));
            binding.aspectRatio11.setVisibility(View.GONE);
            binding.aspectRatio43.setVisibility(View.GONE);
            binding.aspectRatio169.setVisibility(View.GONE);
            binding.aspectRatioFullScreen.setVisibility(View.GONE);

            binding.aspectRatio.setImageResource(R.drawable.ic_4_3);
        });

        binding.aspectRatio169.setOnClickListener(v ->
        {
            CameraUtil.fsEnable = false;
            CameraUtil.aspectRatioEnable = false;
            mCamera2Fragment.setAspectRatio(CameraConstants.DEFAULT_ASPECT_RATIO);
            Log.e(TAG, "aspectRatio: " + ratios2.get(2));
            binding.aspectRatio11.setVisibility(View.GONE);
            binding.aspectRatio43.setVisibility(View.GONE);
            binding.aspectRatio169.setVisibility(View.GONE);
            binding.aspectRatioFullScreen.setVisibility(View.GONE);

            binding.aspectRatio.setImageResource(R.drawable.ic_16_9);
        });

        binding.aspectRatioFullScreen.setOnClickListener(v -> {
            // CameraUtil.fsEnable = true;
            CameraUtil.aspectRatioEnable = false;
            mCamera2Fragment.setAspectRatio(ratios2.get(3));
            Log.e(TAG, "aspectRatio: " + ratios2.get(3));
            binding.aspectRatio11.setVisibility(View.GONE);
            binding.aspectRatio43.setVisibility(View.GONE);
            binding.aspectRatio169.setVisibility(View.GONE);
            binding.aspectRatioFullScreen.setVisibility(View.GONE);
            binding.aspectRatio.setImageResource(R.drawable.ic_full_screen);
        });


    }

    private void setFullScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION |
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
    }

    private void layoutClickEvents() {

        binding.musicParentLayout.setOnClickListener(v -> {
            if (!CameraUtil.music) {
                CameraUtil.music = true;
                //hideLayouts();
                MusicFrameFragment frameFragment = new MusicFrameFragment();
//                MusicSheetFragment frameFragment = new MusicSheetFragment();
                frameFragment.show(getSupportFragmentManager(), frameFragment.getClass().getSimpleName());
            } else {
                //showLayouts();
                CameraUtil.music = false;
            }

        });

        binding.superZoomRecord.setOnClickListener(v -> {
            if (mCamera2Fragment.isRecordingVideo()) {
                mCamera2Fragment.zoomHandler.removeCallbacksAndMessages(null);
                mCamera2Fragment.stopRecordingVideo();
            } else {
                binding.superZoomParentLayout.setVisibility(View.GONE);
                double maxValue = mCamera2Fragment.mCameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
                maxValue = maxValue * 1000;
                viewModel.initProgressBar((long) maxValue);
                mCamera2Fragment.startRecordingVideo();

            }
        });

        binding.slowSpeed.setOnClickListener(v -> {
            CameraUtil.speed = "Slow";

            binding.normalSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
            binding.fastSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
            binding.slowSpeed.setBackgroundResource(R.drawable.rounded_corner_selected);

        });

        binding.normalSpeed.setOnClickListener(v -> {
            CameraUtil.speed = "Normal";

            binding.slowSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
            binding.fastSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
            binding.normalSpeed.setBackgroundResource(R.drawable.rounded_corner_selected);

        });

        binding.fastSpeed.setOnClickListener(v -> {
            CameraUtil.speed = "Fast";
            binding.normalSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
            binding.slowSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
            binding.fastSpeed.setBackgroundResource(R.drawable.rounded_corner_selected);

        });

        binding.boomVideo.setOnClickListener(v -> {

            binding.boomParentLayout.setVisibility(View.GONE);
            mCamera2Fragment.startRecordingVideo();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> mCamera2Fragment.stopRecordingBoomVideo(), 1500);
        });

        binding.cameraWithTimer.setOnClickListener(v -> {
            binding.cameraWithTimer.setEnabled(false);
            if (mCamera2Fragment.isRecordingVideo()) {
                mCamera2Fragment.stopRecordingVideo();
            }
            if (CameraUtil.isRunningTimer) {
                countDownTimer.cancel();
                binding.timerView.setText("");
                CameraUtil.isRunningTimer = false;
                binding.timerChildLayout.setVisibility(View.GONE);

            } else {
                binding.timerParentLayout.setVisibility(View.GONE);
                countDownTimer = new CountDownTimer(timerValue, 1000) {
                    public void onTick(long millisUntilFinished) {
                        binding.timerView.setText("" + millisUntilFinished / 1000);
                        CameraUtil.isRunningTimer = true;
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        binding.cameraWithTimer.setEnabled(true);
                        CameraUtil.isRunningTimer = false;
                        binding.timerView.setText("0");
                        binding.timerView.setText("");
                        binding.timerView.setVisibility(View.GONE);
                        binding.cameraWithTimer.setEnabled(true);
                        binding.cameraWithTimer.setVisibility(View.VISIBLE);
                        if (mCamera2Fragment.isRecordingVideo())
                            mCamera2Fragment.stopRecordingVideo();

                        else {

                            binding.progressTimerParent.setVisibility(View.GONE);
                            mCamera2Fragment.startRecordingVideo();
                        }


                    }

                }.start();
            }
        });

        binding.timerButton10.setOnClickListener(v -> {
            timerValue = 11000;
            binding.timerButton.setImageResource(R.drawable.ic_icon_10);
            binding.timerButton3.setVisibility(View.GONE);
            binding.timerButton10.setVisibility(View.GONE);
            binding.cameraWithTimer.setVisibility(View.VISIBLE);
        });

        binding.timerButton3.setOnClickListener(v -> {
            timerValue = 4000;

            binding.timerButton.setImageResource(R.drawable.ic_icon_3);
            binding.timerButton3.setVisibility(View.GONE);
            binding.timerButton10.setVisibility(View.GONE);

            binding.cameraWithTimer.setVisibility(View.VISIBLE);

        });


        binding.speedParentLayout.setOnClickListener(v -> {
            if (!CameraUtil.speedEnable) {
                CameraUtil.speedEnable = true;
                binding.childSpeedLayout.setVisibility(View.VISIBLE);
                viewModel.hideLayouts();
                binding.speedButton.setImageResource(R.drawable.ic_speed_selected);
                binding.video.setVisibility(View.VISIBLE);
            } else {
                CameraUtil.speed = "Normal";
                binding.slowSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
                binding.fastSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
                binding.normalSpeed.setBackgroundResource(R.drawable.rounded_corner_selected);
                CameraUtil.speedEnable = false;
                binding.childSpeedLayout.setVisibility(View.GONE);
                viewModel.showLayouts();
                binding.speedButton.setImageResource(R.drawable.ic_baseline_speed_24);
            }
        });

        binding.focusParentLayout.setOnClickListener(v -> {
            if (!CameraUtil.focusEnable) {
                CameraUtil.focusEnable = true;
                binding.focusButton.setImageResource(R.drawable.ic_focus_selected);
            } else {
                CameraUtil.focusEnable = false;
                binding.focusButton.setImageResource(R.drawable.ic_focus);
            }

        });

        binding.timerParentLayout.setOnClickListener(v -> {
            if (binding.timerChildLayout.getVisibility() == View.GONE) {
                binding.timerButton.setImageResource(R.drawable.ic_timer_cross);
                CameraUtil.timerEnable = true;
                binding.timerButton3.setVisibility(View.VISIBLE);
                binding.timerButton10.setVisibility(View.VISIBLE);
                binding.timerChildLayout.setVisibility(View.VISIBLE);

                binding.cameraWithTimer.setVisibility(View.GONE);
                viewModel.hideLayouts();

                binding.progressTimerParent.setVisibility(View.VISIBLE);
                CameraUtil.progressTimer = true;
                binding.segmentedProgressBar.setVisibility(View.VISIBLE);
                binding.timerButton.setImageResource(R.drawable.ic_timer_selected);
            } else {
                binding.timerButton.setImageResource(R.drawable.ic_timer_cross);
                CameraUtil.timerEnable = false;
                binding.timerButton3.setVisibility(View.GONE);
                binding.timerButton10.setVisibility(View.GONE);
                binding.timerText.setVisibility(View.VISIBLE);
                binding.timerChildLayout.setVisibility(View.GONE);
                viewModel.showLayouts();

                binding.cameraWithTimer.setVisibility(View.GONE);
                binding.timerButton.setImageResource(R.drawable.ic_timer_cross);
            }

        });


        binding.superZoomParentLayout.setOnClickListener(v -> {
            if (!CameraUtil.superZoomEnable) {
                CameraUtil.superZoomEnable = true;

                binding.superZoomChildLayout.setVisibility(View.VISIBLE);

                viewModel.hideLayouts();
                binding.segmentedProgressBar.setVisibility(View.VISIBLE);
                binding.superZoomButton.setImageResource(R.drawable.ic_superzoom_selected);
            } else {
                CameraUtil.superZoomEnable = false;
                binding.superZoomChildLayout.setVisibility(View.GONE);

                viewModel.showLayouts();
                binding.superZoomButton.setImageResource(R.drawable.superzoom);
            }
        });


        binding.handsFreeParentLayout.setOnClickListener(v -> {
            if (!CameraUtil.handsFreeEnable) {

                CameraUtil.handsFreeEnable = true;
                binding.handsFreeChildLayout.setVisibility(View.VISIBLE);
                mCamera2Fragment.handsFreeRecordMethod();
                viewModel.hideLayouts();
                binding.handsFreeButton.setImageResource(R.drawable.ic_handfree_selected);
            } else {
                binding.handsFreeChildLayout.setVisibility(View.GONE);
                CameraUtil.handsFreeEnable = false;
                viewModel.showLayouts();
                if (mCamera2Fragment.handsFreeVideos != null) {
                    mCamera2Fragment.handsFreeVideos.clear();
                }
                binding.handsFreeButton.setImageResource(R.drawable.handsfree);

            }
        });

        binding.gridLineParent.setOnClickListener(v -> {
            if (binding.gridLines.getVisibility() == View.GONE) {
                binding.gridLines.setVisibility(View.VISIBLE);
                CameraUtil.gridCamera = true;
                sensorMan.registerListener(this, accelerometer,
                        SensorManager.SENSOR_DELAY_UI);
                viewModel.hideLayouts();
                binding.gridButton.setImageResource(R.drawable.ic_grid_selected);
                CameraUtil.progressTimer = true;
                binding.segmentedProgressBar.setVisibility(View.VISIBLE);
                binding.video.setVisibility(View.VISIBLE);

            } else {
                binding.gridLines.setVisibility(View.GONE);
                CameraUtil.gridCamera = false;
                sensorMan.unregisterListener(this);
                binding.gridButton.setImageResource(R.drawable.ic_baseline_grid_on_24);

                viewModel.showLayouts();
            }
        });


        binding.layoutSwitchCamera.setOnClickListener(v -> {
            int facing = mCamera2Fragment.getFacing();
            mCamera2Fragment.setFacing(facing == CameraConstants.FACING_FRONT ?
                    CameraConstants.FACING_BACK : CameraConstants.FACING_FRONT);
        });
        binding.video.setEnabled(true);
        binding.video.setOnClickListener(v -> {
            if (mCamera2Fragment.isRecordingVideo()) {
                if (binding.segmentedProgressBar.getTimePassed() <= 4500) {
                    Toast.makeText(this, getString(R.string.Make_sure_video_is_longer_than_5s), Toast.LENGTH_LONG).show();
                    return;
                }
                mCamera2Fragment.stopRecordingVideo();
                binding.segmentedProgressBar.pause();
                if (viewModel.audio != null) {
                    viewModel.audio.pause();
                }
                binding.video.setImageResource(R.drawable.ic_record_start);
                //showLayouts();

            } else {


                if (CameraUtil.speedEnable) {
                    binding.childSpeedLayout.setVisibility(View.GONE);
                }
                viewModel.videoClickChk.set(true);
                viewModel.hideLayouts();
                if (CameraUtil.gridCamera) {
                    binding.gridLineParent.setVisibility(View.GONE);
                }

                binding.speedParentLayout.setVisibility(View.GONE);

                CameraUtil.music = false;
                CameraUtil.progressTimer = true;


                binding.segmentedProgressBar.setVisibility(View.VISIBLE);
                binding.video.setVisibility(View.VISIBLE);

                mCamera2Fragment.startRecordingVideo();

                binding.video.setImageResource(R.drawable.ic_record_stop_);
                if (viewModel.audio != null) {
                    viewModel.audio.start();
                }
            }
        });
        binding.boomParentLayout.setOnClickListener(v -> {
            if (viewModel.boomActive) {
                viewModel.boomActive = false;
                binding.boomVideo.setVisibility(View.GONE);
                binding.video.setVisibility(View.VISIBLE);
                viewModel.showLayouts();
                binding.boomButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_boomerang));

            } else {
                viewModel.boomActive = true;

                binding.video.setVisibility(View.GONE);
                viewModel.hideLayouts();

                binding.boomVideo.setVisibility(View.VISIBLE);
                binding.boomButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.boomerang_selected));
            }
        });

        binding.layoutclose.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.layoutFlash.setOnClickListener(v -> {
            mCurrentFlashIndex = (mCurrentFlashIndex + 1) % FLASH_OPTIONS.length;
            Log.e(TAG, "mCurrentFlashIndex: " + mCurrentFlashIndex);
            binding.switchFlash.setImageResource(FLASH_ICONS[mCurrentFlashIndex]);
            mCamera2Fragment.setFlash(FLASH_OPTIONS[mCurrentFlashIndex]);
        });

        binding.aspectRatio.setOnClickListener(v -> {
            if (ratios2 != null) {
                ratios2.clear();
            }

            CameraUtil.oneTime = false;
            if (!CameraUtil.aspectRatioEnable) {
                CameraUtil.aspectRatioEnable = true;
                final Set<AspectRatio> ratios = mCamera2Fragment.getSupportedAspectRatios();
                CameraUtil.fsAspectRatio = Collections.max(ratios);
                AspectRatio minAspectRatio = Collections.min(ratios);
//                    final AspectRatio currentRatio = mCamera2Fragment.getAspectRatio()
                for (AspectRatio ratio : ratios) {

                    if (ratio.toString().equalsIgnoreCase("4:3") || ratio.toString().equalsIgnoreCase("16:9")) {
                        ratios2.add(ratio);
                    }
                }
                ratios2.add(minAspectRatio);
                ratios2.add(CameraUtil.fsAspectRatio);
                Collections.sort(ratios2);
                //aspectRatioTV.setVisibility(View.GONE);
                binding.aspectRatio11.setVisibility(View.VISIBLE);
                binding.aspectRatio43.setVisibility(View.VISIBLE);
                binding.aspectRatio169.setVisibility(View.VISIBLE);
                binding.aspectRatioFullScreen.setVisibility(View.VISIBLE);

            } else {
                CameraUtil.aspectRatioEnable = false;
                binding.aspectRatio11.setVisibility(View.GONE);
                binding.aspectRatio43.setVisibility(View.GONE);
                binding.aspectRatio169.setVisibility(View.GONE);
                binding.aspectRatioFullScreen.setVisibility(View.GONE);
                // aspectRatioTV.setVisibility(View.VISIBLE);
            }
        });

        binding.gallery.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                        ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(CameraActivity.this.getResources(), R.drawable.ic_storage_permission, null), CameraActivity.this.getResources().getString(R.string.to_capture_storage_denied),
                            CameraActivity.this.getResources().getString(R.string.not_now), CameraActivity.this.getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {
                                @Override
                                public void onPositiveDismiss() {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", CameraActivity.this.getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }

                                @Override
                                public void onNegativeDismiss() {

                                }
                            });
                } else {
                    customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(CameraActivity.this.getResources(), R.drawable.ic_storage_permission, null), CameraActivity.this.getResources().getString(R.string.to_capture_storage),
                            CameraActivity.this.getResources().getString(R.string.not_now), CameraActivity.this.getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                                @Override
                                public void onPositiveDismiss() {
                                    requestPermissionLauncher.launch(
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                    requestPermissionLauncher.launch(
                                            Manifest.permission.READ_EXTERNAL_STORAGE);
                                }

                                @Override
                                public void onNegativeDismiss() {

                                }
                            });
                }
            } else {
                Intent i = new Intent(CameraActivity.this, GalleryFoldersActivityMedley.class);
                i.putExtra("filter", "Video");
                i.putExtra("call_type", callType);
                CameraActivity.this.startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
                CameraActivity.this.invalidateOptionsMenu();
            }
        });
    }

    public void saveData(boolean isCompress) {
        customDialogBuilder.showLoadingDialog();
        if (isCompress) {
            DefaultVideoStrategy strategy;
            if (sizeGreaterThan300) {
                float a = 1f / (minResolution / 550f);
                if (a > 1) {
                    strategy = new DefaultVideoStrategy.Builder()
                            .addResizer(new FractionResizer(0.8f))
                            .addResizer(new AtMostResizer(540))
                            .build();
                } else {
                    strategy = new DefaultVideoStrategy.Builder()
                            .addResizer(new FractionResizer(a))
                            .addResizer(new AtMostResizer(540))
                            .build();
                }
            } else {
                float a = 1f / (minResolution / 650f);
                if (a > 1) {
                    strategy = new DefaultVideoStrategy.Builder()
                            .addResizer(new FractionResizer(0.8f))
                            .addResizer(new AtMostResizer(640))
                            .build();
                } else {
                    strategy = new DefaultVideoStrategy.Builder()
                            .addResizer(new FractionResizer(a))
                            .addResizer(new AtMostResizer(640))
                            .build();
                }
            }
            TranscoderOptions.Builder options = Transcoder.into(getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"));
            for (int i = 0; i < viewModel.videoPaths.size(); i++) {
                options.addDataSource(viewModel.videoPaths.get(i));
            }
            if (viewModel.audio == null) {
                options.setVideoTrackStrategy(strategy);
            }
            options.setListener(new TranscoderListener() {
                public void onTranscodeProgress(double progress) {
//                    showProgressDialog();
//                    if (progressBinding != null) {
//                        if (viewModel.audio != null) {
//                            progressBinding.progressBar.publishProgress((float) progress / 2);
//                        } else {
//                            progressBinding.progressBar.publishProgress((float) progress);
//                        }
//                    }
                }

                public void onTranscodeCompleted(int successCode) {
//                    Log.d("TAG", "onTranscodeCompleted: " + successCode);
                    if (viewModel.audio != null) {
                        Transcoder.into(getPath().getPath().concat("/finally.mp4"))
                                .addDataSource(TrackType.VIDEO, getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"))
                                .addDataSource(TrackType.AUDIO, getPath().getPath().concat("/recordSound.mp3"))
                                .setVideoTrackStrategy(strategy)
                                .setListener(new TranscoderListener() {
                                    @Override
                                    public void onTranscodeProgress(double progress) {
                                        if (viewModel.audio != null) {
                                        }
                                    }

                                    @Override
                                    public void onTranscodeCompleted(int successCode) {
                                        File thumbFile = new File(getPath(), "/" + "user_thumbnail_" + sessionManager.getUser().getData().getUserName() + format + ".jpg");
                                        try {
                                            FileOutputStream stream = new FileOutputStream(thumbFile);

                                            Bitmap bmThumbnail;
                                            bmThumbnail = ThumbnailUtils.createVideoThumbnail(getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"),
                                                    MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                                            if (bmThumbnail != null) {
                                                bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                                bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                            }
                                            stream.flush();
                                            stream.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        customDialogBuilder.hideLoadingDialog();
                                        Intent intent = new Intent(CameraActivity.this, PreviewVideoActivity.class);
                                        intent.putExtra("videoPath", getPath().getPath().concat("/finally.mp4"));
                                        intent.putExtra("post_image", thumbFile.getPath());
                                        intent.putExtra("Filter", "Simple");
                                        if (viewModel.soundId != null && !viewModel.soundId.isEmpty()) {
                                            intent.putExtra("soundId", viewModel.soundId);
                                        }
//                                        startActivityForResult(intent, 101);
                                        someActivityResultLauncher.launch(intent);
                                    }

                                    @Override
                                    public void onTranscodeCanceled() {

                                    }

                                    @Override
                                    public void onTranscodeFailed(@NonNull Throwable exception) {

                                    }
                                }).transcode();

                    } else {
//                        Log.i("TAG", "onCombineFinished: " + "is original sound");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
//                                Track audio;
                                try {
//                                    Movie m1 = MovieCreator.build(getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"));
//                                    audio = m1.getTracks().get(0);
//                                    Movie m2 = new Movie();
//                                    m2.addTrack(audio);
//                                    DefaultMp4Builder builder = new DefaultMp4Builder();
//                                    Container stdMp4 = builder.build(m2);
//                                    FileOutputStream fos = new FileOutputStream(getPath().getPath().concat("/originalSound.mp3"));
//                                    stdMp4.writeContainer(fos.getChannel());
//                                    fos.close();

                                    File temp = new File(getPath().getPath().concat("/originalSound.mp3"));

                                    if (!temp.exists()) {
                                        temp.createNewFile();
                                    }

                                    new AudioExtractor().genVideoUsingMuxer(getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"), temp.getAbsolutePath(), -1, -1, true, false);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                File thumbFile = new File(getPath(), "/" + "user_thumbnail_" + sessionManager.getUser().getData().getUserName() + format + ".jpg");
                                try {
                                    FileOutputStream stream = new FileOutputStream(thumbFile);

                                    Bitmap bmThumbnail;
                                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"),
                                            MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                                    if (bmThumbnail != null) {
                                        bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                    }
                                    stream.flush();
                                    stream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Glide.with(CameraActivity.this)
                                        .asBitmap()
                                        .load(sessionManager.getUser().getData().getUserProfile())
//                                        .load(Const.ITEM_BASE_URL + sessionManager.getUser().getData().getUserProfile())
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                File soundImage = new File(getPath(), "soundImage.jpeg");
                                                try {
                                                    FileOutputStream stream = new FileOutputStream(soundImage);
                                                    resource.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                                    stream.flush();
                                                    stream.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
//                                                Camera2.unbindAll();
                                                customDialogBuilder.hideLoadingDialog();
                                                Intent intent = new Intent(CameraActivity.this, PreviewVideoActivity.class);
                                                intent.putExtra("videoPath", getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"));
                                                intent.putExtra("post_image", thumbFile.getPath());
                                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.mp3"));
                                                intent.putExtra("sound_image", getPath().getPath().concat("/soundImage.jpeg"));
                                                intent.putExtra("Filter", "Simple");
//                                                startActivityForResult(intent, 101);
                                                someActivityResultLauncher.launch(intent);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }

                                            @Override
                                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                                super.onLoadFailed(errorDrawable);
                                                customDialogBuilder.hideLoadingDialog();
                                                Intent intent = new Intent(CameraActivity.this, PreviewVideoActivity.class);
                                                intent.putExtra("videoPath", getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"));
                                                intent.putExtra("post_image", thumbFile.getPath());
                                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.mp3"));
                                                intent.putExtra("Filter", "Simple");
//                                                startActivityForResult(intent, 101);
                                                someActivityResultLauncher.launch(intent);
                                            }
                                        });
                            }
                        }).start();
                    }
                }

                public void onTranscodeCanceled() {
//                    Log.d("TAG", "onTranscodeCanceled: ");
                }

                public void onTranscodeFailed(@NonNull Throwable exception) {
//                    Log.d("TAG", "onTranscodeCanceled:/// " + exception);
                }
            }).transcode();

        } else {
            new Thread(() -> {
//                Track audio;
                try {
//                    File tempFile = new File(getExternalCacheDir(), "/" + "myAwesomeFile.mp3");

//                    if (!tempFile.exists())
//                    {
//                        try {
//                            tempFile.createNewFile();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    File temp = new File(getPath().getPath().concat("/originalSound.mp3"));

                    if (!temp.exists()) {
                        temp.createNewFile();
                    }

                    new AudioExtractor().genVideoUsingMuxer(viewModel.videoPaths.get(0), temp.getAbsolutePath(), -1, -1, true, false);

//                    Movie m1 = MovieCreator.build(viewModel.videoPaths.get(0));
//                    audio = m1.getTracks().get(0);
//                    Movie m2 = new Movie();
//                    m2.addTrack(audio);
//                    DefaultMp4Builder builder = new DefaultMp4Builder();
//                    Container stdMp4 = builder.build(m2);
//                    FileOutputStream fos = new FileOutputStream(getPath().getPath().concat("/originalSound.mp3"));
//                    stdMp4.writeContainer(fos.getChannel());
//                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                File thumbFile = new File(getPath(), "/" + "user_thumbnail_" + sessionManager.getUser().getData().getUserName() + format + ".jpg");

                try {

                    FileOutputStream stream = new FileOutputStream(thumbFile);

                    Bitmap bmThumbnail;
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(viewModel.videoPaths.get(0),
                            MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                    if (bmThumbnail != null) {
                        bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                    }

                    stream.flush();
                    stream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(CameraActivity.this)
                        .asBitmap()
                        .load(sessionManager.getUser().getData().getUserProfile())
//                        .load(Const.ITEM_BASE_URL + sessionManager.getUser().getData().getUserProfile())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                File soundImage = new File(getPath(), "soundImage.jpeg");
                                try {
                                    FileOutputStream stream = new FileOutputStream(soundImage);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                    stream.flush();
                                    stream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                customDialogBuilder.hideLoadingDialog();
                                Intent intent = new Intent(CameraActivity.this, PreviewVideoActivity.class);
                                intent.putExtra("videoPath", viewModel.videoPaths.get(0));
                                intent.putExtra("post_image", thumbFile.getPath());
                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.mp3"));
                                intent.putExtra("sound_image", getPath().getPath().concat("/soundImage.jpeg"));
                                intent.putExtra("Filter", "Simple");
//                                startActivityForResult(intent, 101);
                                someActivityResultLauncher.launch(intent);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                customDialogBuilder.hideLoadingDialog();
                                Intent intent = new Intent(CameraActivity.this, PreviewVideoActivity.class);
                                intent.putExtra("videoPath", viewModel.videoPaths.get(0));
                                intent.putExtra("post_image", thumbFile.getPath());
                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.mp3"));
                                intent.putExtra("Filter", "Simple");
                                //   intent.putExtra("sound_image", getPath().getPath().concat("/soundImage.jpeg"));
//                                startActivityForResult(intent, 101);
                                someActivityResultLauncher.launch(intent);
                            }
                        });
            }).start();
        }
    }

//    public File getPath() {
//        String state = Environment.getExternalStorageState();
//        File filesDir;
////        if (Environment.MEDIA_MOUNTED.equals(state)) {
////            // We can read and write the media
////            filesDir = getExternalCacheDir();
////        } else {
////            // Load another directory, probably local memory
////            filesDir = getFilesDir();
////        }
//
//        filesDir = getExternalCacheDir();
//        if (filesDir != null) {
//            viewModel.parentPath = filesDir.getPath();
//        }
//        return filesDir;
//    }

    public File getPath() {
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            filesDir = getExternalFilesDir(null);
        } else {
            filesDir = getFilesDir();
        }
        return filesDir;
    }

    private void gridLayoutClicks() {
        binding.gidParentLayout.setOnClickListener(v -> {
            if (!CameraUtil.gridLayoutActive) {
                CameraUtil.gridLayoutActive = true;
                //gridLayout.setBackgroundResource(R.drawable.selected_grid_layout);
                binding.gridLayoutList.setVisibility(View.VISIBLE);
                binding.gridLayoutChild.setVisibility(View.VISIBLE);

                viewModel.hideLayouts();
                binding.gridLayout.setImageResource(R.drawable.ic_layout_selected);

            } else {
                //gridLayout.setBackgroundResource(R.drawable.gridlayout);
                binding.gridLayoutChild.setVisibility(View.GONE);
                binding.gridLayoutList.setVisibility(View.GONE);
                CameraUtil.gridLayoutActive = false;

                viewModel.hideGridLayout();
                viewModel.showLayouts();
                binding.gridCaptureLayout.setVisibility(View.GONE);

                binding.gridLayout.setImageResource(R.drawable.gridlayout);

            }
        });

        binding.cameraClick.setOnClickListener(v -> {
            Log.e(TAG, "CameraUtil.twoGridValue : " + CameraUtil.twoGridValue);
            if (CameraUtil.gridLayoutHVideoActive) {
                if (CameraUtil.twoGridValue == 2) {


                    Intent intent = new Intent(this, PreviewVideoActivity.class);
                    intent.putStringArrayListExtra("mergeVideo", mCamera2Fragment.mergeVideo);

                    intent.putExtra("duetType", 0);
//                    startActivityForResult(intent, 101);
                    someActivityResultLauncher.launch(intent);
                } else {

                    binding.checkImage.setVisibility(View.GONE);
                    binding.cameraClick.setEnabled(false);
                    runnable = () -> {
                        if (mCamera2Fragment.isRecordingVideo()) {
                            mCamera2Fragment.stopRecordingVideo();
                        }
                        binding.checkImage.setVisibility(View.VISIBLE);
                        binding.cameraClick.setEnabled(true);
                    };
                    stopVideoRecordingHandler.postDelayed(runnable, timeLimit);
                    if (CameraUtil.twoGridValue < 2)
                        mCamera2Fragment.startRecordingVideo();
                }
            }

        });

        binding.twoGridLayoutHorizontal.setOnClickListener(v -> {
            binding.gridLayoutList.setVisibility(View.GONE);
            binding.gridCaptureLayout.setVisibility(View.VISIBLE);
            mCamera2Fragment.gridLayoutTwoVideoH();
        });

    }


    private void initSensor() {
        sensorMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        binding.balanceImage.setColorFilter(ContextCompat.getColor(this, R.color.yellow_green_color_picker), android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            int min = 0;
            //Log.e(TAG, "min: "+min );
            int max = 2;

            int value1 = Math.round(x);
            int value = viewModel.absVal(value1);
            Log.e(TAG, "value: " + value);
            if (value >= min && value < max)
                binding.balanceImage.setColorFilter(ContextCompat.getColor(this, R.color.yellow_green_color_picker), android.graphics.PorterDuff.Mode.MULTIPLY);
            else
                binding.balanceImage.setColorFilter(ContextCompat.getColor(this, R.color.red_color_picker), android.graphics.PorterDuff.Mode.MULTIPLY);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorMan != null) {
            sensorMan.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }


    @Override
    public void onBackPressed() {
        customDialogBuilder.showSimpleDialog(getString(R.string.are_you_sure), getString(R.string.Do_you_really_wan_to_go_back),
                getString(R.string.no), getString(R.string.yes), new CustomDialogBuilder.OnDismissListener() {
                    @Override
                    public void onPositiveDismiss() {
                        //1.86
                        if (viewModel.audio != null) {
                            if (viewModel.audio.isPlaying()) {
                                viewModel.audio.stop();
                            }
                        }
                        finish();
                    }

                    @Override
                    public void onNegativeDismiss() {
                    }
                });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*        if (data != null) {
            if (requestCode == 101 && !path.equals("") && !data.getBooleanExtra("cancelled", false)) {
//                Intent intent = new Intent(CameraActivity.this, MainVideoActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//            finish();

                mCamera2Fragment.saveData("Simple", "",path);
            }
        }*/

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {

                assert data != null;
                String result = data.getStringExtra("result");
                int position = data.getIntExtra("position", 0);
//                Gson gson = new Gson();
//                Type type = new TypeToken<List<PictureFacer>>() {
//                }.getType();
//                selectedItem = gson.fromJson(result, type);

                String fileExtention = "";

//                uri = selectedItem.get(position).getPicturePath();
                uri = result;
//                Log.e("ImageUri", data.getStringExtra("result"));
//                uri = data.getStringExtra("result");
                int i = uri.lastIndexOf('.');
                if (i > 0) {
                    fileExtention = uri.substring(i + 1);
                }

                if (fileExtention.equalsIgnoreCase("mp4") || fileExtention.equalsIgnoreCase("3gp") || fileExtention.equalsIgnoreCase("mkv") || fileExtention.equalsIgnoreCase("mov") || fileExtention.equalsIgnoreCase("webm")) {
                    if (!uri.isEmpty()) {
                        File file = new File(uri);
                        // Get length of file in bytes
                        long fileSizeInBytes = file.length();
                        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                        long fileSizeInMB = fileSizeInKB / 1024;

                        if (fileSizeInMB > 300) {
                            sizeGreaterThan300 = true;
                        }


                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(CameraActivity.this, Uri.fromFile(new File(uri)));
                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                        minResolution = Math.min(Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)), Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)));

                        long timeInMilliSec = Long.parseLong(time);
//                        Log.e("timeInMilliSec", "=======================" + timeInMilliSec);
                        if (timeInMilliSec < 4900) {
                            customDialogBuilder.showSimpleDialog(CameraActivity.this.getResources().getString(R.string.Video_Too_Short), CameraActivity.this.getResources().getString(R.string.This_video_is_shorter_than_5_seconds_Please_select_another),
                                    CameraActivity.this.getResources().getString(R.string.cancel), CameraActivity.this.getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {

                                        @Override
                                        public void onPositiveDismiss() {
//                                            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
                                            Intent i = new Intent(CameraActivity.this, GalleryFoldersActivityMedley.class);
                                            i.putExtra("filter", "Video");
                                            i.putExtra("call_type", callType);
                                            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
                                        }

                                        @Override
                                        public void onNegativeDismiss() {

                                        }
                                    });
                        } else if (timeInMilliSec >= 60200) {
                            customDialogBuilder.showSimpleDialog(CameraActivity.this.getResources().getString(R.string.Video_Too_Long), CameraActivity.this.getResources().getString(R.string.This_video_is_longer_than_1_min_Please_select_another),
                                    CameraActivity.this.getResources().getString(R.string.cancel), CameraActivity.this.getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {

                                        @Override
                                        public void onPositiveDismiss() {
//                                            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
                                            Intent i = new Intent(CameraActivity.this, GalleryFoldersActivityMedley.class);
                                            i.putExtra("filter", "Video");
                                            i.putExtra("call_type", callType);
                                            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
                                        }

                                        @Override
                                        public void onNegativeDismiss() {

                                        }
                                    });
                        } else if (fileSizeInMB < 500) {
                            viewModel.videoPaths = new ArrayList<>();
                            viewModel.videoPaths.add(uri);
                            if (fileSizeInMB > 5) {
                                CameraActivity.this.saveData(true);
                            } else {
                                CameraActivity.this.saveData(false);
                            }
                        } else {
                            customDialogBuilder.showSimpleDialog(CameraActivity.this.getResources().getString(R.string.Too_long_video_s_size), CameraActivity.this.getResources().getString(R.string.This_video_s_size_is_greater_than_500MbPlease_select_another),
                                    CameraActivity.this.getResources().getString(R.string.cancel), CameraActivity.this.getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {
                                        @Override
                                        public void onPositiveDismiss() {
//                                            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
                                            Intent i = new Intent(CameraActivity.this, GalleryFoldersActivityMedley.class);
                                            i.putExtra("filter", "Video");
                                            i.putExtra("call_type", callType);
                                            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
                                        }

                                        @Override
                                        public void onNegativeDismiss() {

                                        }
                                    });
                        }
                        retriever.release();
                    }
                } else {
                    Toast.makeText(CameraActivity.this, CameraActivity.this.getString(R.string.Please_Select_mp4_format_video), Toast.LENGTH_SHORT).show();
                }

            } /*else {
                onBackPressed();
            }*/
        }/* else {
            onBackPressed();
        }*/
    }

    //Horizontal Progressbar for Video
    private void videoProgressbar() {
        CameraUtil.progressTimer = true;

        binding.timer15.setOnClickListener(v -> {
            mCamera2Fragment.progressTimer = 16000;
            CameraUtil.progressTimer = true;

            binding.timer15.setBackgroundResource(R.drawable.bg_black_corner_5);
            binding.timer30.setBackgroundResource(R.drawable.bg_white_corner_5);
            binding.timer45.setBackgroundResource(R.drawable.bg_white_corner_5);
            binding.timer60.setBackgroundResource(R.drawable.bg_white_corner_5);

            binding.timer15.setTextColor(getResources().getColor(R.color.white));
            binding.timer30.setTextColor(getResources().getColor(R.color.black));
            binding.timer45.setTextColor(getResources().getColor(R.color.black));
            binding.timer60.setTextColor(getResources().getColor(R.color.black));
        });
        binding.timer30.setOnClickListener(v -> {
            mCamera2Fragment.progressTimer = 31000;
            CameraUtil.progressTimer = true;
            binding.timer30.setBackgroundResource(R.drawable.bg_black_corner_5);
            binding.timer15.setBackgroundResource(R.drawable.bg_white_corner_5);
            binding.timer45.setBackgroundResource(R.drawable.bg_white_corner_5);
            binding.timer60.setBackgroundResource(R.drawable.bg_white_corner_5);

            binding.timer30.setTextColor(getResources().getColor(R.color.white));
            binding.timer15.setTextColor(getResources().getColor(R.color.black));
            binding.timer45.setTextColor(getResources().getColor(R.color.black));
            binding.timer60.setTextColor(getResources().getColor(R.color.black));
        });
        binding.timer45.setOnClickListener(v -> {
            mCamera2Fragment.progressTimer = 46000;
            CameraUtil.progressTimer = true;
            binding.timer45.setBackgroundResource(R.drawable.bg_black_corner_5);
            binding.timer15.setBackgroundResource(R.drawable.bg_white_corner_5);
            binding.timer30.setBackgroundResource(R.drawable.bg_white_corner_5);
            binding.timer60.setBackgroundResource(R.drawable.bg_white_corner_5);

            binding.timer45.setTextColor(getResources().getColor(R.color.white));
            binding.timer15.setTextColor(getResources().getColor(R.color.black));
            binding.timer30.setTextColor(getResources().getColor(R.color.black));
            binding.timer60.setTextColor(getResources().getColor(R.color.black));
        });
        binding.timer60.setOnClickListener(v -> {
            mCamera2Fragment.progressTimer = 61000;
            CameraUtil.progressTimer = true;
            binding.timer60.setBackgroundResource(R.drawable.bg_black_corner_5);
            binding.timer15.setBackgroundResource(R.drawable.bg_white_corner_5);
            binding.timer30.setBackgroundResource(R.drawable.bg_white_corner_5);
            binding.timer45.setBackgroundResource(R.drawable.bg_white_corner_5);

            binding.timer60.setTextColor(getResources().getColor(R.color.white));
            binding.timer15.setTextColor(getResources().getColor(R.color.black));
            binding.timer30.setTextColor(getResources().getColor(R.color.black));
            binding.timer45.setTextColor(getResources().getColor(R.color.black));
        });
    }


    //Sound Library Click Handle
    @Override
    public void clickEvent(String soundID, String soundUrl, String title) {
        mCamera2Fragment.soundID = soundID;
        viewModel.downLoadMusic(soundUrl);
        binding.tvSoundTitle.setVisibility(View.VISIBLE);
        binding.tvSoundTitle.setText(title);
        binding.cardViewParentLayout.setVisibility(View.GONE);
//        binding.llGalleryLayout.setVisibility(View.GONE);
        if (binding.videoWithSoundParent.getVisibility() == View.GONE) {
            binding.videoWithSoundParent.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {

            if (mCurrentFlashIndex == 1) {
                mCamera2Fragment.setFlash(FLASH_OPTIONS[0]);
                binding.switchFlash.setImageResource(FLASH_ICONS[0]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void downLoadMusic(String endPoint) {
//
//        PRDownloader.download(endPoint, getPath().getPath(), "recordSound.mp3")
////        PRDownloader.download(Const.ITEM_BASE_URL + endPoint, getPath().getPath(), "recordSound.mp3")
//                .build()
//                .setOnStartOrResumeListener(() -> customDialogBuilder.showLoadingDialog())
//                .start(new OnDownloadListener() {
//                    @Override
//                    public void onDownloadComplete() {
//                        customDialogBuilder.hideLoadingDialog();
////                        viewModel.isStartRecording.set(true);
//                        viewModel.createAudioForCamera();
//                    }
//
//                    @Override
//                    public void onError(Error error) {
//                        customDialogBuilder.hideLoadingDialog();
//                    }
//                });
//    }

}
