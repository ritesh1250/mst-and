package com.meest.medley_camera2.camera2.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraCharacteristics;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.googlecode.mp4parser.authoring.Track;
import com.meest.Activities.preview.PreviewPhotoActivity;
import com.meest.R;
import com.meest.databinding.ActivityCamera2MetmeBinding;
import com.meest.databinding.DailogProgressBinding;
import com.meest.mediapikcer.GalleryFoldersActivity;
import com.meest.mediapikcer.utils.PictureFacer;
import com.meest.medley_camera2.camera2.cameraUtils.AspectRatio;
import com.meest.medley_camera2.camera2.utills.CameraConstants;
import com.meest.medley_camera2.camera2.utills.CameraUtil;
import com.meest.medley_camera2.camera2.view.fragment.MeestCamera2Fragment;
import com.meest.medley_camera2.camera2.viewmodels.MeestCameraActivityViewModel;
import com.meest.meestbhart.utilss.Constant;
import com.meest.social.socialViewModel.view.NewPostUploadActivity;
import com.meest.utils.goLiveUtils.utils.MeestLoaderDialog;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.TranscoderOptions;
import com.otaliastudios.transcoder.engine.TrackType;
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategy;
import com.otaliastudios.transcoder.strategy.size.AtMostResizer;
import com.otaliastudios.transcoder.strategy.size.FractionResizer;

import org.apache.commons.io.FilenameUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MeestCameraActivity extends AppCompatActivity implements SensorEventListener {

    public static final int[] FLASH_OPTIONS = {
            CameraConstants.FLASH_OFF,
            CameraConstants.FLASH_TORCH,
    };
    public static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };
    public static int mCurrentFlashIndex = 0;
    private boolean boomActive = false;
    private MeestCamera2Fragment mMeestCamera2Fragment;
    private CountDownTimer countDownTimer;
    public MeestCameraActivityViewModel viewModel;
    public ActivityCamera2MetmeBinding binding;
    String format;
    public static final String CALL_FEED = "feed";
    public static final String CALL_STORY = "story";
    public static String CALL_TYPE = "";

    private DailogProgressBinding progressBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CameraUtil.comeFrom = "Meest";

        setFullScreen();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera2_metme);
        CameraUtil.timer = 30000;
        meestLoaderDialog = new MeestLoaderDialog(this);
        initProgressDialog();
        if (getIntent().hasExtra("call_type")) {
            if (getIntent().getStringExtra("call_type").equalsIgnoreCase(CALL_STORY)) {
                CALL_TYPE = CALL_STORY;
            } else {
                CALL_TYPE = CALL_FEED;
            }
        }

        mMeestCamera2Fragment = MeestCamera2Fragment.newInstance();
        CameraUtil.oneTime = true;
        format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new MeestCameraActivityViewModel(this, MeestCameraActivity.this, binding, mMeestCamera2Fragment)).createFor()).get(MeestCameraActivityViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.disableOptions();
        getFragmentManager().beginTransaction().replace(R.id.container, mMeestCamera2Fragment).commit();
        initViews();
        gridLayoutClicks();
        clickEventsAspectRatio();
        showLayouts();
        initSensor();
        viewModel.videoProgressbar();
        CameraUtil.oneTime = true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public File getPath() {
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            filesDir = getExternalFilesDir(null);
        } else {
            // Load another directory, probably local memory
            filesDir = getFilesDir();
        }
        if (filesDir != null) {
            viewModel.parentPath = filesDir.getPath();
        }
        return filesDir;
    }

    private void initViews() {
        layoutclickevent();
        binding.photoCapture.setOnClickListener(v -> {
            CameraUtil.currentCamera = "Photo";
            binding.captureImageSwitch.setTextColor(getColor(R.color.white));
            binding.picture.setVisibility(View.VISIBLE);
            binding.video.setVisibility(View.GONE);
            binding.recordVideoSwitch.setTextColor(getColor(R.color.light_grey));
            switchOptions();

        });
        binding.videoRecord.setOnClickListener(v -> {
            CameraUtil.currentCamera = "Video";
            binding.captureImageSwitch.setTextColor(getColor(R.color.light_grey));
            binding.picture.setVisibility(View.GONE);
            binding.video.setVisibility(View.VISIBLE);
            binding.recordVideoSwitch.setTextColor(getColor(R.color.white));
            switchOptions();
        });

        binding.timerButton.setOnClickListener(v -> {
            if (binding.timerChildLayout.getVisibility() == View.GONE) {
                binding.timerButton.setImageResource(R.drawable.ic_timer_cross);
                CameraUtil.timerEnable = true;
                binding.timerButton3.setVisibility(View.VISIBLE);
                binding.timerButton10.setVisibility(View.VISIBLE);
                binding.timerChildLayout.setVisibility(View.VISIBLE);
                hideLayouts();
                binding.timerButton.setImageResource(R.drawable.ic_timer_selected);
            } else {
                binding.timerButton.setImageResource(R.drawable.ic_timer_cross);
                CameraUtil.timerEnable = false;
                binding.timerButton3.setVisibility(View.GONE);
                binding.timerButton10.setVisibility(View.GONE);
                binding.timerText.setVisibility(View.VISIBLE);
                binding.timerChildLayout.setVisibility(View.GONE);
                showLayouts();
                binding.timerButton.setImageResource(R.drawable.ic_timer_cross);
            }

        });

        binding.timerButton3.setOnClickListener(v -> {
            timerValue = 4000;
            binding.timerButton.setImageResource(R.drawable.ic_icon_3);
            binding.timerButton3.setVisibility(View.GONE);
            binding.timerButton10.setVisibility(View.GONE);
        });

        binding.timerButton10.setOnClickListener(v -> {
            timerValue = 11000;
            binding.timerButton.setImageResource(R.drawable.ic_icon_10);
            binding.timerButton3.setVisibility(View.GONE);
            binding.timerButton10.setVisibility(View.GONE);

        });

        binding.focusButton.setOnClickListener(v -> {
            if (!CameraUtil.focusEnable) {
                CameraUtil.focusEnable = true;
                binding.focusButton.setImageResource(R.drawable.ic_focus_selected);
            } else {
                CameraUtil.focusEnable = false;
                binding.focusButton.setImageResource(R.drawable.ic_focus);
            }

        });

        binding.speedParentLayout.setOnClickListener(v -> {
            if (!CameraUtil.speedEnable) {
                CameraUtil.speedEnable = true;
                binding.childSpeedLayout.setVisibility(View.VISIBLE);
                hideLayouts();
                binding.speedButton.setImageResource(R.drawable.ic_speed_selected);
                binding.video.setVisibility(View.VISIBLE);
            } else {
                CameraUtil.speed = "Normal";
                binding.slowSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
                binding.fastSpeed.setBackgroundResource(R.drawable.round_corner_gradient);
                binding.normalSpeed.setBackgroundResource(R.drawable.rounded_corner_selected);
                CameraUtil.speedEnable = false;
                binding.childSpeedLayout.setVisibility(View.GONE);
                showLayouts();
                binding.speedButton.setImageResource(R.drawable.ic_baseline_speed_24);
            }
        });

        binding.normalSpeed.setBackgroundResource(R.drawable.rounded_corner_selected);

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

    }

    int timerValue = 0;
    ArrayList<AspectRatio> ratios2 = new ArrayList<>();

    private void clickEventsAspectRatio() {
        binding.aspectRatio11.setOnClickListener(v -> {
            CameraUtil.fsEnable = false;
            CameraUtil.aspectRatioEnable = false;
            mMeestCamera2Fragment.setAspectRatio(ratios2.get(0));
            binding.aspectRatio11.setVisibility(View.GONE);
            binding.aspectRatio43.setVisibility(View.GONE);
            binding.aspectRatio169.setVisibility(View.GONE);
            binding.aspectRatioFullScreen.setVisibility(View.GONE);
            binding.aspectRatio.setImageResource(R.drawable.ic_1_1);


        });

        binding.aspectRatio43.setOnClickListener(v -> {
            CameraUtil.fsEnable = false;
            CameraUtil.aspectRatioEnable = false;
            mMeestCamera2Fragment.setAspectRatio(ratios2.get(1));
            binding.aspectRatio11.setVisibility(View.GONE);
            binding.aspectRatio43.setVisibility(View.GONE);
            binding.aspectRatio169.setVisibility(View.GONE);
            binding.aspectRatioFullScreen.setVisibility(View.GONE);

            binding.aspectRatio.setImageResource(R.drawable.ic_4_3);
        });

        binding.aspectRatio169.setOnClickListener(v -> {
            CameraUtil.fsEnable = false;
            CameraUtil.aspectRatioEnable = false;
            mMeestCamera2Fragment.setAspectRatio(CameraConstants.DEFAULT_ASPECT_RATIO);
            binding.aspectRatio11.setVisibility(View.GONE);
            binding.aspectRatio43.setVisibility(View.GONE);
            binding.aspectRatio169.setVisibility(View.GONE);
            binding.aspectRatioFullScreen.setVisibility(View.GONE);

            binding.aspectRatio.setImageResource(R.drawable.ic_16_9);
        });

        binding.aspectRatioFullScreen.setOnClickListener(v -> {
            CameraUtil.aspectRatioEnable = false;
            mMeestCamera2Fragment.setAspectRatio(ratios2.get(3));
            binding.aspectRatio11.setVisibility(View.GONE);
            binding.aspectRatio43.setVisibility(View.GONE);
            binding.aspectRatio169.setVisibility(View.GONE);
            binding.aspectRatioFullScreen.setVisibility(View.GONE);
            binding.aspectRatio.setImageResource(R.drawable.ic_full_screen);
        });
    }


    private void setFullScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION | WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    int LAUNCH_SECOND_ACTIVITY = 1231;

    public ArrayList<String> videoPaths = new ArrayList<>();

    public void layoutclickevent() {

        binding.cameraClose.setOnClickListener(v -> {
            finish();
        });
        binding.gallery.setOnClickListener(v -> {
//            Intent i = new Intent(this, Camera2Gallery.class);
//            if (CameraUtil.currentCamera.equalsIgnoreCase("Video"))
//                i.putExtra("type", "Video");
//            else
//                i.putExtra("type", "Image");
//            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);

            Intent i = new Intent(this, GalleryFoldersActivity.class);

            if (CameraUtil.currentCamera.equalsIgnoreCase("Video")) {
                i.putExtra("filter", "Video");
            } else {
                i.putExtra("filter", "Image");
            }

            i.putExtra("call_type", CALL_TYPE);
            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
        });
        binding.picture.setOnClickListener(v -> {
            mMeestCamera2Fragment.takePicture();
        });
        binding.video.setOnClickListener(v -> {
            if (mMeestCamera2Fragment.isRecordingVideo()) {
                mMeestCamera2Fragment.stopRecordingVideo();
                binding.video.setImageResource(R.drawable.ic_record_start);
            } else {
                if (CameraUtil.speedEnable) {
                    binding.childSpeedLayout.setVisibility(View.GONE);
                }
                hideLayouts();
                if (CameraUtil.gridCamera) {
                    binding.gridLineParent.setVisibility(View.GONE);
                }
                binding.speedParentLayout.setVisibility(View.GONE);
                CameraUtil.progressTimer = true;
                binding.video.setVisibility(View.VISIBLE);
                mMeestCamera2Fragment.startRecordingVideo();
                binding.video.setImageResource(R.drawable.ic_record_stop_);

            }
        });
        binding.aspectRatio.setOnClickListener(v -> {
            if (ratios2 != null) {
                ratios2.clear();
            }

            CameraUtil.oneTime = false;
            if (!CameraUtil.aspectRatioEnable) {
                CameraUtil.aspectRatioEnable = true;
                final Set<AspectRatio> ratios = mMeestCamera2Fragment.getSupportedAspectRatios();
                CameraUtil.fsAspectRatio = Collections.max(ratios);
                AspectRatio minAspectRatio = Collections.min(ratios);
                for (AspectRatio ratio : ratios) {
                    if (ratio.toString().equalsIgnoreCase("4:3") || ratio.toString().equalsIgnoreCase("16:9")) {
                        ratios2.add(ratio);
                    }
                }
                ratios2.add(minAspectRatio);
                ratios2.add(CameraUtil.fsAspectRatio);
                Collections.sort(ratios2);
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
            }
            invalidateOptionsMenu();

        });
        binding.switchFlash.setOnClickListener(v -> {
            mCurrentFlashIndex = (mCurrentFlashIndex + 1) % FLASH_OPTIONS.length;
            binding.switchFlash.setImageResource(FLASH_ICONS[mCurrentFlashIndex]);
            mMeestCamera2Fragment.setFlash(FLASH_OPTIONS[mCurrentFlashIndex]);
            invalidateOptionsMenu();
        });
        binding.switchCamera.setOnClickListener(v -> {
            int facing = mMeestCamera2Fragment.getFacing();
            mMeestCamera2Fragment.setFacing(facing == CameraConstants.FACING_FRONT ?
                    CameraConstants.FACING_BACK : CameraConstants.FACING_FRONT);
            invalidateOptionsMenu();
        });
        binding.gridLineParent.setOnClickListener(v -> {
            if (binding.gridLines.getVisibility() == View.GONE) {
                binding.gridLines.setVisibility(View.VISIBLE);
                CameraUtil.gridCamera = true;
                sensorMan.registerListener(this, accelerometer,
                        SensorManager.SENSOR_DELAY_UI);
                hideLayouts();
                binding.gridButton.setImageResource(R.drawable.ic_grid_selected);
                binding.photoVideoToggleParent.setVisibility(View.GONE);
                CameraUtil.progressTimer = true;
                if (CameraUtil.currentCamera.equalsIgnoreCase("Photo"))
                    binding.picture.setVisibility(View.VISIBLE);
                else
                    binding.video.setVisibility(View.VISIBLE);
            } else {
                binding.gridLines.setVisibility(View.GONE);
                CameraUtil.gridCamera = false;
                sensorMan.unregisterListener(this);
                binding.gridButton.setImageResource(R.drawable.ic_baseline_grid_on_24);
                showLayouts();
            }
            invalidateOptionsMenu();
        });

        binding.superZoomParentLayout.setOnClickListener(v -> {
            if (!CameraUtil.superZoomEnable) {
                CameraUtil.superZoomEnable = true;

                binding.superZoomChildLayout.setVisibility(View.VISIBLE);
                hideLayouts();
                binding.superZoomButton.setImageResource(R.drawable.ic_superzoom_selected);
            } else {
                CameraUtil.superZoomEnable = false;
                binding.superZoomChildLayout.setVisibility(View.GONE);

                showLayouts();
                binding.superZoomButton.setImageResource(R.drawable.superzoom);
            }
            invalidateOptionsMenu();
        });
        binding.superZoomRecord.setOnClickListener(v -> {
            if (mMeestCamera2Fragment.isRecordingVideo()) {
                mMeestCamera2Fragment.zoomHandler.removeCallbacksAndMessages(null);
                mMeestCamera2Fragment.stopRecordingVideo();
            } else {
                binding.superZoomParentLayout.setVisibility(View.GONE);
                double maxValue = mMeestCamera2Fragment.mCameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
                maxValue = maxValue * 1000;
                mMeestCamera2Fragment.startRecordingVideo();

            }

            invalidateOptionsMenu();
        });

        binding.handsFreeParentLayout.setOnClickListener(v -> {
            if (!CameraUtil.handsFreeEnable) {
                CameraUtil.handsFreeEnable = true;
                binding.handsFreeChildLayout.setVisibility(View.VISIBLE);
                mMeestCamera2Fragment.handsFreeRecordMethod();
                hideLayouts();
                binding.handsFreeButton.setImageResource(R.drawable.ic_handfree_selected);
            } else {
                binding.handsFreeChildLayout.setVisibility(View.GONE);
                CameraUtil.handsFreeEnable = false;
                showLayouts();
                if (mMeestCamera2Fragment.handsFreeVideos != null) {
                    mMeestCamera2Fragment.handsFreeVideos.clear();
                }
                binding.handsFreeButton.setImageResource(R.drawable.handsfree);
            }
            invalidateOptionsMenu();

        });
        binding.multiSnapParentLayout.setOnClickListener(v -> {
            if (!CameraUtil.multiSnapEnable) {
                CameraUtil.multiSnapEnable = true;
                binding.multiSnapChildLayout.setVisibility(View.VISIBLE);
                hideLayouts();
                binding.multiSnapButton.setImageResource(R.drawable.multi_snap_selected);
            } else {
                CameraUtil.multiSnapEnable = false;
                binding.multiSnapChildLayout.setVisibility(View.GONE);
                showLayouts();
                if (mMeestCamera2Fragment.multiSnapList != null) {
                    mMeestCamera2Fragment.multiSnapList.clear();
                    clearMultiCaptureThumbs();
                }
                binding.multiSnapButton.setImageResource(R.drawable.multisnap);
                binding.multiSnapCapture.setEnabled(true);
                binding.progressbarMC.setProgress(0);
            }
            invalidateOptionsMenu();
        });
        binding.multiSnapCapture.setOnClickListener(v -> {
            mMeestCamera2Fragment.takePicture();
            invalidateOptionsMenu();
        });
        binding.cameraWithTimer.setOnClickListener(v -> {
            binding.cameraWithTimer.setVisibility(View.GONE);
            if (CameraUtil.currentCamera.equalsIgnoreCase("Video")) {
                if (mMeestCamera2Fragment.isRecordingVideo()) {
                    mMeestCamera2Fragment.stopRecordingVideo();
                } else {
                    binding.timerParentLayout.setVisibility(View.GONE);
                    countDownTimer = new CountDownTimer(timerValue, 1000) {
                        public void onTick(long millisUntilFinished) {
                            binding.timerView.setText("" + millisUntilFinished / 1000);
                            CameraUtil.isRunningTimer = true;
                        }
                        public void onFinish() {
                            binding.cameraWithTimer.setEnabled(true);
                            CameraUtil.isRunningTimer = false;
                            binding.timerView.setText("0");
                            binding.timerView.setText("");
                            binding.timerView.setVisibility(View.GONE);
                            binding.cameraWithTimer.setEnabled(true);
                            binding.cameraWithTimer.setVisibility(View.VISIBLE);
                            if (mMeestCamera2Fragment.isRecordingVideo())
                                mMeestCamera2Fragment.stopRecordingVideo();
                            else {
                                binding.progressTimerParent.setVisibility(View.GONE);
                                mMeestCamera2Fragment.startRecordingVideo();
                            }
                        }

                    }.start();
                }
            } else {

                binding.timerParentLayout.setVisibility(View.GONE);
                countDownTimer = new CountDownTimer(timerValue, 1000) {
                    public void onTick(long millisUntilFinished) {
                        binding.timerView.setText("" + millisUntilFinished / 1000);
                        CameraUtil.isRunningTimer = true;
                    }

                    public void onFinish() {
                        binding.cameraWithTimer.setEnabled(true);
                        CameraUtil.isRunningTimer = false;
                        binding.timerView.setText("0");
                        binding.timerView.setText("");
                        binding.timerView.setVisibility(View.GONE);
                        binding.cameraWithTimer.setEnabled(true);
                        binding.cameraWithTimer.setVisibility(View.VISIBLE);
                        binding.progressTimerParent.setVisibility(View.GONE);
                        mMeestCamera2Fragment.takePicture();
                        binding.timerChildLayout.setVisibility(View.GONE);
                    }

                }.start();

            }

            invalidateOptionsMenu();
        });
        binding.boomParentLayout.setOnClickListener(v -> {
            if (boomActive) {
                boomActive = false;
                binding.boomVideo.setVisibility(View.GONE);
                binding.video.setVisibility(View.VISIBLE);
                showLayouts();
                binding.boomButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_boomerang));

            } else {
                boomActive = true;
                binding.video.setVisibility(View.GONE);
                hideLayouts();
                binding.boomVideo.setVisibility(View.VISIBLE);
                binding.boomButton.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_boomerang_selected));
            }

        });
        binding.boomVideo.setOnClickListener(v -> {
            binding.boomParentLayout.setVisibility(View.GONE);
            mMeestCamera2Fragment.startRecordingVideo();
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> mMeestCamera2Fragment.stopRecordingBoomVideo(), 1500);
            invalidateOptionsMenu();
        });
    }

    private final Handler stopVideoRecordingHandler = new Handler();
    private Runnable runnable;
    private final int timeLimit = 6000;

    private void gridLayoutClicks() {

        binding.gridLayout.setOnClickListener(v -> {
            if (!CameraUtil.gridLayoutActive) {
                CameraUtil.gridLayoutActive = true;
                binding.gridLayoutList.setVisibility(View.VISIBLE);
                if (CameraUtil.currentCamera.equalsIgnoreCase("Video")) {
                    binding.twoGridLayout.setVisibility(View.GONE);
                    binding.threeGridLayout.setVisibility(View.GONE);
                    binding.fourGridLayout.setVisibility(View.GONE);
                    binding.sixGridLayout.setVisibility(View.GONE);
                } else {
                    binding.twoGridLayout.setVisibility(View.VISIBLE);
                    binding.threeGridLayout.setVisibility(View.VISIBLE);
                    binding.fourGridLayout.setVisibility(View.VISIBLE);
                    binding.sixGridLayout.setVisibility(View.VISIBLE);
                }
                binding.gridLayoutChild.setVisibility(View.VISIBLE);
                hideLayouts();
                binding.gridLayout.setImageResource(R.drawable.ic_layout_selected);
            } else {
                binding.gridLayoutChild.setVisibility(View.GONE);
                binding.gridLayoutList.setVisibility(View.GONE);
                CameraUtil.gridLayoutActive = false;
                mMeestCamera2Fragment.hideGridLayouts();
                viewModel.clearGridValues();
                showLayouts();
                binding.gridCaptureLayout.setVisibility(View.GONE);

                binding.gridLayout.setImageResource(R.drawable.gridlayout);
            }
        });

        binding.cameraClick.setOnClickListener(v -> {
            if (CameraUtil.gridLayout2Active) {
                if (CameraUtil.twoGridValue == 2) {
                    if (CameraUtil.previewComplete) {
                        Intent intent = new Intent(this, PreviewPhotoActivity.class);

                        String filePath = String.valueOf(mMeestCamera2Fragment.filePath);
                        intent.putExtra("imagePath", filePath);
                        intent.putExtra("isStory", CALL_TYPE.equalsIgnoreCase("story"));
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(this, "Please wait image is processing...", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mMeestCamera2Fragment.takePicture();
                }
            }
            if (CameraUtil.gridLayoutHVideoActive) {
                if (CameraUtil.twoGridValue == 2) {
                    Intent intent = new Intent(this, PreviewVideoActivity.class);
                    intent.putStringArrayListExtra("mergeVideo", mMeestCamera2Fragment.mergeVideo);
                    intent.putExtra("duetType", 0);
                    startActivityForResult(intent, 1001);
                    finish();
                } else {

                    binding.checkImage.setVisibility(View.GONE);
                    binding.cameraClick.setEnabled(false);
                    runnable = () -> {
                        if (mMeestCamera2Fragment.isRecordingVideo()) {
                            mMeestCamera2Fragment.stopRecordingVideo();
                        }
                        binding.checkImage.setVisibility(View.VISIBLE);
                        binding.cameraClick.setEnabled(true);
                    };
                    stopVideoRecordingHandler.postDelayed(runnable, timeLimit);
                    if (CameraUtil.twoGridValue < 2)
                        mMeestCamera2Fragment.startRecordingVideo();
                }
            } else if (CameraUtil.gridLayout3Active) {
                if (CameraUtil.threeGridValue == 3) {
                    if (CameraUtil.previewComplete) {
                        Intent intent = new Intent(this, PreviewPhotoActivity.class);
                        String filePath = String.valueOf(mMeestCamera2Fragment.filePath);
                        intent.putExtra("imagePath", filePath);
                        intent.putExtra("isStory", CALL_TYPE.equalsIgnoreCase("story"));
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Please wait image is processing...", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mMeestCamera2Fragment.takePicture();
                }
            } else if (CameraUtil.gridLayout4Active) {
                if (CameraUtil.fourGridValue == 4) {
                    if (CameraUtil.previewComplete) {
                        Intent intent = new Intent(this, PreviewPhotoActivity.class);
                        String filePath = String.valueOf(mMeestCamera2Fragment.filePath);
                        intent.putExtra("imagePath", filePath);
                        intent.putExtra("isStory", CALL_TYPE.equalsIgnoreCase("story"));
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Please wait image is processing...", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mMeestCamera2Fragment.takePicture();
                }
            } else if (CameraUtil.gridLayout6Active) {
                if (CameraUtil.sixGridValue == 6) {
                    if (CameraUtil.previewComplete) {
                        Intent intent = new Intent(this, PreviewPhotoActivity.class);
                        String filePath = String.valueOf(mMeestCamera2Fragment.filePath);
                        intent.putExtra("imagePath", filePath);
                        intent.putExtra("isStory", CALL_TYPE.equalsIgnoreCase("story"));
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Please wait image is processing...", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mMeestCamera2Fragment.takePicture();
                }
            } else if (CameraUtil.gridLayout2ActiveHorizontal) {
                if (CameraUtil.twoGridValueHorizontal == 2) {
                    if (CameraUtil.previewComplete) {
                        Intent intent = new Intent(this, PreviewPhotoActivity.class);
                        String filePath = String.valueOf(mMeestCamera2Fragment.filePath);
                        intent.putExtra("imagePath", filePath);
                        intent.putExtra("isStory", CALL_TYPE.equalsIgnoreCase("story"));
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Please wait image is processing...", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    mMeestCamera2Fragment.takePicture();
                }
            }
        });

        binding.twoGridLayout.setOnClickListener(v -> {
            binding.gridLayoutList.setVisibility(View.GONE);
            binding.gridCaptureLayout.setVisibility(View.VISIBLE);
            mMeestCamera2Fragment.gridLayoutTwo();
        });

        binding.threeGridLayout.setOnClickListener(v -> {
            binding.gridLayoutList.setVisibility(View.GONE);
            binding.gridCaptureLayout.setVisibility(View.VISIBLE);
            mMeestCamera2Fragment.gridLayoutThree();
        });

        binding.fourGridLayout.setOnClickListener(v -> {
            binding.gridLayoutList.setVisibility(View.GONE);
            binding.gridCaptureLayout.setVisibility(View.VISIBLE);
            mMeestCamera2Fragment.gridLayoutFour();
        });

        binding.sixGridLayout.setOnClickListener(v -> {
            binding.gridLayoutList.setVisibility(View.GONE);
            binding.gridCaptureLayout.setVisibility(View.VISIBLE);
            mMeestCamera2Fragment.gridLayoutSix();
        });

        binding.twoGridLayoutHorizontal.setOnClickListener(v -> {
            binding.gridLayoutList.setVisibility(View.GONE);
            binding.gridCaptureLayout.setVisibility(View.VISIBLE);
            if (CameraUtil.currentCamera.equalsIgnoreCase("Photo"))
                mMeestCamera2Fragment.gridLayoutTwoHorizontal();
            else {
                mMeestCamera2Fragment.gridLayoutTwoVideoH();
            }
        });

    }

    //Code for Hide the Layouts

    private void hideLayouts() {
        binding.switchCamera.setVisibility(View.GONE);

        if (CameraUtil.currentCamera.equalsIgnoreCase("Video")) {
            CameraUtil.progressTimer = false;
            binding.progressTimerParent.setVisibility(View.GONE);
        }
        if (binding.gallery.getVisibility() == View.VISIBLE) {
            binding.gallery.setVisibility(View.GONE);
            binding.cardViewParentLayout.setVisibility(View.GONE);
        }

        if (binding.picture.getVisibility() == View.VISIBLE)
            binding.picture.setVisibility(View.GONE);

        if (binding.video.getVisibility() == View.VISIBLE)
            binding.video.setVisibility(View.GONE);

        if (!CameraUtil.gridLayoutActive)
            binding.gidParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.superZoomEnable)
            binding.superZoomParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.gridCamera)
            binding.gridLineParent.setVisibility(View.GONE);

        if (!CameraUtil.handsFreeEnable)
            binding.handsFreeParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.multiSnapEnable)
            binding.multiSnapParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.timerEnable)
            binding.timerParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.timerEnable)
            binding.timerParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.focusEnable)
            binding.focusParentLayout.setVisibility(View.GONE);

        binding.aspectRatioParentLayout.setVisibility(View.GONE);

        if (!CameraUtil.speedEnable)
            binding.speedParentLayout.setVisibility(View.GONE);

        if (!boomActive)
            binding.boomParentLayout.setVisibility(View.GONE);

        binding.photoVideoToggleParent.setVisibility(View.GONE);

    }

    private void showLayouts() {

        binding.switchCamera.setVisibility(View.VISIBLE);
        binding.photoVideoToggleParent.setVisibility(View.VISIBLE);
        binding.cardViewParentLayout.setVisibility(View.VISIBLE);
        binding.gallery.setVisibility(View.VISIBLE);

        if (CameraUtil.currentCamera.equalsIgnoreCase("Photo")) {
            binding.gridLineParent.setVisibility(View.VISIBLE);
            binding.gidParentLayout.setVisibility(View.GONE);
            binding.aspectRatioParentLayout.setVisibility(View.VISIBLE);
            binding.multiSnapParentLayout.setVisibility(View.GONE);
            binding.timerParentLayout.setVisibility(View.VISIBLE);
            binding.focusParentLayout.setVisibility(View.VISIBLE);
            binding.picture.setVisibility(View.VISIBLE);

        } else {
            binding.video.setVisibility(View.VISIBLE);
            binding.gridLineParent.setVisibility(View.VISIBLE);
            binding.gidParentLayout.setVisibility(View.GONE);
            binding.aspectRatioParentLayout.setVisibility(View.VISIBLE);
            binding.superZoomParentLayout.setVisibility(View.VISIBLE);
            binding.handsFreeParentLayout.setVisibility(View.GONE);
            binding.timerParentLayout.setVisibility(View.VISIBLE);
            binding.speedParentLayout.setVisibility(View.VISIBLE);
            binding.focusParentLayout.setVisibility(View.VISIBLE);
            CameraUtil.progressTimer = true;
            binding.progressTimerParent.setVisibility(View.VISIBLE);
            binding.boomParentLayout.setVisibility(View.VISIBLE);

        }

    }

    private void switchOptions() {
        if (CameraUtil.currentCamera.equalsIgnoreCase("Photo")) {
            binding.multiSnapParentLayout.setVisibility(View.GONE);
            binding.superZoomParentLayout.setVisibility(View.GONE);
            binding.handsFreeParentLayout.setVisibility(View.GONE);
            binding.speedParentLayout.setVisibility(View.GONE);
            binding.progressTimerParent.setVisibility(View.GONE);

            binding.boomParentLayout.setVisibility(View.GONE);
        } else {
            binding.multiSnapParentLayout.setVisibility(View.GONE);
            binding.superZoomParentLayout.setVisibility(View.VISIBLE);
            binding.handsFreeParentLayout.setVisibility(View.GONE);
            binding.speedParentLayout.setVisibility(View.VISIBLE);

            binding.progressTimerParent. setVisibility(View.VISIBLE);
            binding.boomParentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void clearMultiCaptureThumbs() {
        binding.multiCaptureNextButton.setVisibility(View.GONE);
        binding.multiCaptureImage1.setImageResource(0);
        binding.multiCaptureImage2.setImageResource(0);
        binding.multiCaptureImage3.setImageResource(0);
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
            int max = 2;
            int value1 = Math.round(x);
            int value = absVal(value1);
            if (value >= min && value < max)
                balanceImage.setColorFilter(ContextCompat.getColor(this, R.color.yellow_green_color_picker), android.graphics.PorterDuff.Mode.MULTIPLY);
            else
                balanceImage.setColorFilter(ContextCompat.getColor(this, R.color.red_color_picker), android.graphics.PorterDuff.Mode.MULTIPLY);
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


    List<PictureFacer> selectedItem;
    int c = 0;
    private MeestLoaderDialog meestLoaderDialog;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
//            if (resultCode == Activity.RESULT_OK) {
//                if (data != null) {
//                    String path = data.getStringExtra("result");
//                    String ext = FilenameUtils.getExtension(path);
//
//                    if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpeg")) {
//                        Intent  intent = new Intent(this, PreviewPhotoActivity.class);
//                        intent.putExtra("imagePath", path);
//                        intent.putExtra("isStory", CALL_TYPE.equalsIgnoreCase("story"));
//                        startActivity(intent);
//                        finish();
//                    } else {
//                       mMeestCamera2Fragment.saveData("Simple", "",path);
//                    }
//
//                }
//            }

            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                Gson gson = new Gson();
                Type type = new TypeToken<List<PictureFacer>>() {
                }.getType();
                selectedItem = gson.fromJson(result, type);

                if (CALL_TYPE.equalsIgnoreCase("feed")) {

                    if (selectedItem != null && selectedItem.size() > 0) {
                        for (PictureFacer pictureFacer : selectedItem) {
                            Log.e("onActivityResult", "onActivityResult: " + pictureFacer.getPicturePath());
                            if (pictureFacer.getType().equalsIgnoreCase("Image")) {
                                if (selectedItem.size() == 1) {
                                    Intent intent = new Intent(this, PreviewPhotoActivity.class);
                                    intent.putExtra("imagePath", selectedItem.get(0).getPicturePath());
                                    intent.putExtra("isStory", false);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    meestLoaderDialog.showDialog();
                                    meestLoaderDialog.setTexMsg("Preparing images..");
                                    ++c;
//                                    File chkFileSize = new File(pictureFacer.getPictureSize());
                                    try {
                                        long fileSizeInBytes = Long.parseLong(pictureFacer.getPictureSize());
                                        long fileSizeInKB = fileSizeInBytes / 1024;

//                    long fileSizeInMB = fileSizeInKB / 1024;

                                        if (fileSizeInKB > 500) {
                                            String mediaPath = Constant.compressImage(this, pictureFacer.getPicturePath());
                                            pictureFacer.setPicturePath(mediaPath);
                                        }
                                    } catch (Exception e) {
                                    }
                                    Log.e("for", "ccount : " + c);

                                    if (selectedItem.size() == c) {
                                        Log.e("for", "UploadFilesToServer : " + selectedItem.size());
                                        meestLoaderDialog.hideDialog();
                                        Intent it = new Intent(this, NewPostUploadActivity.class);
                                        it.putExtra("multipleMedia", new Gson().toJson(selectedItem));
                                        it.putExtra("mediaPath", selectedItem.get(0).getPicturePath());
                                        it.putExtra("isVideo", false);
                                        it.putExtra("isMultiple", true);
                                        it.putExtra("recode", requestCode);
                                        startActivity(it);
                                        finish();


                                    }
                                }
                            } else {

//                                File chkFileSize = new File(pictureFacer.getPictureSize());
                                try {
                                    long fileSizeInBytes = Long.parseLong(pictureFacer.getPictureSize());
                                    long fileSizeInKB = fileSizeInBytes / 1024;
                                    long fileSizeInMB = fileSizeInKB / 1024;

                                    if (selectedItem.size() == 1) {
                                        videoPaths.clear();
                                        videoPaths.add(pictureFacer.getPicturePath());
                                        if (fileSizeInMB > 5) {
                                            saveData(true);
                                        } else {
                                            saveData(false);
                                        }
                                    } else {
                                        String output = Constant.createOutputPath(this, ".mp4");
                                        String thumbnail = Constant.createOutputPath(this, ".jpg");
                                        videoCompress(pictureFacer.getPicturePath(), output, thumbnail, requestCode, selectedItem.size());
                                        Log.e("Check", "onActivityResult: " + output + "  " + thumbnail);
                                        pictureFacer.setPicturePath(output);
                                        pictureFacer.setThumbnail(thumbnail);
                                    }
                                } catch (Exception e) {
                                }
                            }

                        }
                    }
                } else {

                    if (selectedItem != null && selectedItem.size() == 1 && selectedItem.get(0).getType().equalsIgnoreCase("Video")) {
                        videoPaths.clear();
                        videoPaths.add(selectedItem.get(0).getPicturePath());
                        saveData(true);
                    } else if (selectedItem != null && selectedItem.size() == 1) {
                        Intent intent = new Intent(this, PreviewPhotoActivity.class);
                        intent.putExtra("imagePath", selectedItem.get(0).getPicturePath());
                        intent.putExtra("isStory", true);
                        startActivity(intent);
                        finish();

                    }

                }


            }
        }

    }


    public void saveData(boolean isCompress) {

        if (isCompress) {
            DefaultVideoStrategy strategy = new DefaultVideoStrategy.Builder()
                    .addResizer(new FractionResizer(0.7F))
                    .addResizer(new AtMostResizer(1000))
                    .build();
            String output = Constant.createOutputPath(this, ".mp4");
            TranscoderOptions.Builder options = Transcoder.into(output);
            for (int i = 0; i < videoPaths.size(); i++) {
                options.addDataSource(videoPaths.get(i));
            }
            options.setVideoTrackStrategy(strategy);
            options.setListener(new TranscoderListener() {
                public void onTranscodeProgress(double progress) {
                    showProgressDialog();
                    if (progressBinding != null) {
                        if (viewModel.audio != null) {

                            progressBinding.progressBar.publishProgress((float) progress / 2);
                        } else {

                            progressBinding.progressBar.publishProgress((float) progress);
                        }
                    }
                    Log.d("TAG", "onTranscodeProgress: " + progress);
                }

                public void onTranscodeCompleted(int successCode) {
                    Log.d("TAG", "onTranscodeCompleted: " + successCode);
                    if (viewModel.audio != null) {

                        Transcoder.into(getPath().getPath().concat("/finally.mp4"))
                                .addDataSource(TrackType.VIDEO, getPath().getPath().concat("/append.mp4"))
                                .addDataSource(TrackType.AUDIO, getPath().getPath().concat("/recordSound.aac"))
                                .setVideoTrackStrategy(strategy)
                                .setListener(new TranscoderListener() {
                                    @Override
                                    public void onTranscodeProgress(double progress) {
                                        if (progressBinding != null && viewModel.audio != null) {
                                            progressBinding.progressBar.publishProgress((float) (1 + progress) / 2);
                                        }
                                    }

                                    @Override
                                    public void onTranscodeCompleted(int successCode) {
                                        File thumbFile = new File(getPath(), "temp.jpg");
                                        try {
                                            FileOutputStream stream = new FileOutputStream(thumbFile);

                                            Bitmap bmThumbnail;
                                            bmThumbnail = ThumbnailUtils.createVideoThumbnail(getPath().getPath().concat("/append.mp4"),
                                                    MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                                            if (bmThumbnail != null) {
                                                bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                            }
                                            stream.flush();
                                            stream.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        hideProgressDialog();

                                        Intent intent = new Intent(MeestCameraActivity.this, PreviewVideoActivity.class);
                                        intent.putExtra("videoPath", getPath().getPath().concat("/finally.mp4"));
                                        intent.putExtra("thumbPath", thumbFile.getPath());
                                        intent.putExtra("Filter", "Simple");

                                        if (viewModel.soundId != null && !viewModel.soundId.isEmpty()) {
                                            intent.putExtra("soundId", viewModel.soundId);
                                        }
//                                        intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.aac"));
//                                        intent.putExtra("sound_image", getPath().getPath().concat("/soundImage.jpeg"));
                                        startActivityForResult(intent, 101);


                                    }

                                    @Override
                                    public void onTranscodeCanceled() {

                                    }

                                    @Override
                                    public void onTranscodeFailed(@NonNull Throwable exception) {

                                    }
                                }).transcode();

                    } else {
                        Log.i("TAG", "onCombineFinished: " + "is original sound");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
//                                Track audio;
//                                try {
//                                    Movie m1 = MovieCreator.build(getPath().getPath().concat("/append.mp4"));
//                                    audio = m1.getTracks().get(1);
//                                    Movie m2 = new Movie();
//                                    m2.addTrack(audio);
//                                    DefaultMp4Builder builder = new DefaultMp4Builder();
//                                    Container stdMp4 = builder.build(m2);
//                                    FileOutputStream fos = new FileOutputStream(getPath().getPath().concat("/originalSound.aac"));
//                                    stdMp4.writeContainer(fos.getChannel());
//                                    fos.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
                                String thumbnail = Constant.createOutputPath(MeestCameraActivity.this, ".JPEG");
                                File thumbFile = new File(thumbnail);
                                try {
                                    FileOutputStream stream = new FileOutputStream(thumbFile);

                                    Bitmap bmThumbnail;
                                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(output,
                                            MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                                    if (bmThumbnail != null) {
                                        bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    }
                                    stream.flush();
                                    stream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                hideProgressDialog();
                                Intent intent = new Intent(MeestCameraActivity.this, PreviewVideoActivity.class);
                                intent.putExtra("Filter", "Simple");
                                intent.putExtra("videoPath", output);
                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.aac"));
                                intent.putExtra("thumbPath", thumbFile.getPath());
                                if (CALL_TYPE.equalsIgnoreCase("story")) {
                                    intent.putExtra("type", "story");
                                } else {
                                    intent.putExtra("type", "feed");
                                }

                                intent.putExtra("typeCamera", true);
                                startActivity(intent);
                                finish();


                            }
                        }).start();
                    }
                }

                public void onTranscodeCanceled() {
                    Log.d("TAG", "onTranscodeCanceled: ");
                }

                public void onTranscodeFailed(@NonNull Throwable exception) {
                    Log.d("TAG", "onTranscodeCanceled: " + exception);
                }
            }).transcode();

        } else {
            new Thread(() -> {
                Track audio;

                String thumbnail = Constant.createOutputPath(this, ".JPEG");
                File thumbFile = new File(thumbnail);
                try {
                    FileOutputStream stream = new FileOutputStream(thumbFile);

                    Bitmap bmThumbnail;
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(videoPaths.get(0),
                            MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                    if (bmThumbnail != null) {
                        bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    }
                    stream.flush();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(this, PreviewVideoActivity.class);

                intent.putExtra("Filter", "Simple");

                intent.putExtra("videoPath", videoPaths.get(0));
                intent.putExtra("thumbPath", thumbFile.getPath());

                if (CALL_TYPE.equalsIgnoreCase("story")) {
                    intent.putExtra("type", "story");
                } else {
                    intent.putExtra("type", "feed");
                }

                intent.putExtra("typeCamera", true);
                //   intent.putExtra("sound_image", getPath().getPath().concat("/soundImage.jpeg"));
                startActivity(intent);
                finish();


            }).start();
        }

    }
    private Dialog mBuilder;
    public void initProgressDialog() {
        mBuilder = new Dialog(this);
        mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        if (mBuilder.getWindow() != null) {
            mBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        progressBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dailog_progress, null, false);
        progressBinding.progressBar.setShader(new int[]{ContextCompat.getColor(this, R.color.colorTheme), ContextCompat.getColor(this, R.color.colorTheme), ContextCompat.getColor(this, R.color.colorTheme)});


        DrawableImageViewTarget target = new DrawableImageViewTarget(progressBinding.ivParent);

        mBuilder.setContentView(progressBinding.getRoot());
    }

    public void showProgressDialog() {
        if (!mBuilder.isShowing()) {
            mBuilder.show();
        }
    }

    public void hideProgressDialog() {
        try {
            mBuilder.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isVideocnps = false;

    private boolean videoCompress(String input, String output, String thumbnail, int requestCode, int lsine) {
        isVideocnps = false;

        DefaultVideoStrategy strategy = new DefaultVideoStrategy.Builder()
                .addResizer(new FractionResizer(0.7F))
                .addResizer(new AtMostResizer(1000))
                .build();
        TranscoderOptions.Builder options = Transcoder.into(output);
        options.addDataSource(input);
        options.setVideoTrackStrategy(strategy);
        options.setListener(new TranscoderListener() {
            @Override
            public void onTranscodeProgress(double progress) {

                showProgressDialog();
                progressBinding.progressBar.publishProgress((float) progress);

                Log.e("TAG", "onTranscodeProgress: " + progress);

            }

            @Override
            public void onTranscodeCompleted(int successCode) {
                ++c;
                File thumbFile = new File(thumbnail);
                try {
                    FileOutputStream stream = new FileOutputStream(thumbFile);
                    Bitmap bmThumbnail;
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(input, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                    if (bmThumbnail != null) {
                        bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    }
                    stream.flush();
                    stream.close();
                    isVideocnps = true;
                    if (CALL_TYPE.equalsIgnoreCase("feed")) {
                        if (successCode == 0 && lsine == c && lsine > 1) {

                            hideProgressDialog();
//                            meestLoaderDialog.showDialog();
//                            meestLoaderDialog.setTexMsg("Uploading Video");
                            Log.e("transcode", "UploadFilesToServer: ");
                            Intent it = new Intent(MeestCameraActivity.this, NewPostUploadActivity.class);
                            it.putExtra("multipleMedia", new Gson().toJson(selectedItem));
                            it.putExtra("mediaPath", selectedItem.get(0).getPicturePath());
                            it.putExtra("isVideo", true);
                            it.putExtra("isMultiple", true);
                            it.putExtra("thumbPath", thumbFile.getPath());
                            it.putExtra("recode", requestCode);
                            startActivity(it);
                            finish();
                        }
                    }

//                    binding.progressBar.addDivider();


                    Log.e("transcode", "onTranscodesuccessCode: ");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("transcode", "onTranscodesuccessCode: " + e.getMessage());
                    isVideocnps = false;
                }

            }

            @Override
            public void onTranscodeCanceled() {
                Log.e("transcode", "onTranscodeCanceled: ");
            }

            @Override
            public void onTranscodeFailed(@NonNull Throwable exception) {
                isVideocnps = false;
                Log.e("transcode", "onTranscodeCanceled: " + exception);
            }
        }).transcode();
        return isVideocnps;
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mCurrentFlashIndex == 1) {
                mMeestCamera2Fragment.setFlash(FLASH_OPTIONS[0]);
                binding.switchFlash.setImageResource(FLASH_ICONS[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
