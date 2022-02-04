package com.meest.videomvvmmodule.view.recordvideo;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraX;
import androidx.camera.core.PreviewConfig;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.VideoCaptureConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.googlecode.mp4parser.authoring.Track;
import com.meest.R;
import com.meest.databinding.ActivityCameraBinding;
import com.meest.databinding.DailogProgressBinding;
import com.meest.social.socialViewModel.view.login.LoginSignUp;
import com.meest.videomvvmmodule.AudioExtractor;
import com.meest.videomvvmmodule.gallery.GalleryFoldersActivityMedley;
import com.meest.videomvvmmodule.utils.AutoFitPreviewBuilder;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.UriUtils;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;
import com.meest.videomvvmmodule.view.music.MusicFrameFragment;
import com.meest.videomvvmmodule.view.preview.PreviewActivity;
import com.meest.videomvvmmodule.viewmodel.CameraViewModel;
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
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends BaseActivity {

    private static final int MY_PERMISSIONS_REQUEST = 101;
    public CameraViewModel viewModel;
    private int aspectRatioNumerator = 16, aspectRatioDenominator = 9;
    private ActivityCameraBinding binding;
    private CustomDialogBuilder customDialogBuilder;
    private Dialog mBuilder;
    private DailogProgressBinding progressBinding;
    //    int aspectRatioSwitchCounter = 0;
    int height, width;
    String format;
    boolean sizeGreaterThan300 = false;
    float minResolution = 0;
    boolean isRecordingForFirstTime = true;
    int LAUNCH_SECOND_ACTIVITY = 112;
    String callType = "video", uri;

    String path = "";

    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new CameraViewModel()).createFor()).get(CameraViewModel.class);
        customDialogBuilder = new CustomDialogBuilder(CameraActivity.this);

        initView();
        initListener();
        initObserve();
        initProgressDialog();
        binding.setViewModel(viewModel);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Storage, microphone and camera permission is required to use this feature", Toast.LENGTH_SHORT).show();
            } else {
                String action = getIntent().getAction();
                String type = getIntent().getType();

                if (Intent.ACTION_SEND.equals(action) && type != null) {
                    if (type.startsWith("video/")) {
                        Uri videoUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
                        path = UriUtils.getPathFromUri(this, videoUri);
                        assert path != null;
                        handleSendVideo(path);
                    }
                }
            }
        } else {
            Intent intent = new Intent(CameraActivity.this, LoginSignUp.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
//        if (getIntent().getStringExtra("filePath") != null)
//            handleSendVideo(getIntent().getStringExtra("filePath"));
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        binding.tvSelect.performClick();
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });
    }

    private void handleSendVideo(String uri) {


//        Log.e("TAG", "handleSendVideo: " + uri);
        String fileExtention = "";

        int i = uri.lastIndexOf('.');
        if (i > 0) {
            fileExtention = uri.substring(i + 1);
        }

        if (fileExtention.equalsIgnoreCase("mp4") || fileExtention.equalsIgnoreCase("mkv") || fileExtention.equalsIgnoreCase("3gp") || fileExtention.equalsIgnoreCase("mov") || fileExtention.equalsIgnoreCase("webm")) {
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
                    if (fileSizeInMB > 5) {
                        saveData(true);
                    } else {
                        customDialogBuilder.showLoadingDialog();
                        saveData(false);
                    }
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


    @SuppressLint("RestrictedApi")
    private void initView() {
        Integer[] enabled = new Integer[]{1, 4, 5, 8, 9};
        String musicUrl = getIntent().getStringExtra("music_url");
        Log.e("TAG", "initView: " + getIntent().getStringExtra("music_url"));
        Log.e("TAG", "initView: " + getIntent().getStringExtra("music_title"));
        Log.e("TAG", "initView: " + getIntent().getStringExtra("soundId"));
        if (musicUrl != null && !musicUrl.isEmpty()) {
            downLoadMusic(getIntent().getStringExtra("music_url"));
            if (getIntent().getStringExtra("music_title") != null) {
                binding.tvSoundTitle.setText(getIntent().getStringExtra("music_title"));
                binding.llVideoTiming.setVisibility(View.GONE);
                binding.divider15.setVisibility(View.GONE);
                binding.divider30.setVisibility(View.GONE);
                binding.divider45.setVisibility(View.GONE);
                binding.divider60.setVisibility(View.GONE);
            }
            if (getIntent().getStringExtra("soundId") != null) {
                viewModel.soundId = getIntent().getStringExtra("soundId");
            }
        }
        if (binding.tvSoundTitle.getText().toString().trim().equalsIgnoreCase("")) {
            binding.llVideoTiming.setVisibility(View.VISIBLE);
        } else {
            binding.llVideoTiming.setVisibility(View.GONE);
        }
//        binding.tvOpenAr.setOnClickListener(v -> {
//            Intent intent = new Intent(CameraActivity.this, MainActivity.class);
//            intent.putExtra("title", "AR Filters");
//            startActivity(intent);
//            finish();
//        });

        if (viewModel.onDurationUpdate.getValue() != null) {
            binding.progressBar.enableAutoProgressView(viewModel.onDurationUpdate.getValue());
//            Log.e("TAG", "=============================" + viewModel.onDurationUpdate.getValue());
        }
        binding.progressBar.setDividerColor(Color.RED);
        binding.progressBar.setDividerEnabled(false);
        binding.progressBar.setDividerWidth(4);
        binding.progressBar.setListener(mills -> {
            viewModel.isEnabled.set(mills >= 14500);
            if (mills == viewModel.onDurationUpdate.getValue()) {
                stopRecording();
            }
        });
//        binding.recordingTime.setText(String.valueOf(viewModel.onDurationUpdate.getValue()));
//        binding.ivAspectRatio.setOnClickListener(v -> {
//
//            if (aspectRatioSwitchCounter == 0)
//                aspectRatioSwitchCounter = 1;
//            else
//                aspectRatioSwitchCounter = 0;
//
//        });
        binding.tvSelect169.setOnClickListener(v -> {
            viewModel.aspectRatio.set(0);
            viewModel.isAspectRationExpanded.set(false);
            aspectRatioNumerator = 16;
            aspectRatioDenominator = 9;
            CameraX.unbindAll();
            binding.rtlAspectRatio.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            startCamera();

        });

        binding.tvSelect43.setOnClickListener(v -> {
            viewModel.aspectRatio.set(1);
            viewModel.isAspectRationExpanded.set(false);
            aspectRatioNumerator = 4;
            aspectRatioDenominator = 3;
            CameraX.unbindAll();
            int h = (width * 4 / 3);
            RelativeLayout.LayoutParams labelLayoutParams = new RelativeLayout.LayoutParams(width, h);
            labelLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            binding.rtlAspectRatio.setLayoutParams(labelLayoutParams);
            startCamera();
        });

        binding.tvSelect11.setOnClickListener(v -> {
            viewModel.aspectRatio.set(2);
            viewModel.isAspectRationExpanded.set(false);
            aspectRatioNumerator = 1;
            aspectRatioDenominator = 1;
            CameraX.unbindAll();
            int h = width;
            RelativeLayout.LayoutParams labelLayoutParams = new RelativeLayout.LayoutParams(width, h);
            labelLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            binding.rtlAspectRatio.setLayoutParams(labelLayoutParams);
            startCamera();
        });

        binding.ivSelect.setOnClickListener(v -> {

            viewModel.preview.enableTorch(false);
            viewModel.isFlashOn.set(false);
            binding.ivFlash.setImageResource(R.drawable.ic_flash_off);
             /*   if (!viewModel.isRecording.get() && (binding.progressBar.getTimePassed() >= 14500 || binding.progressBar.getTimePassed() >= viewModel.onDurationUpdate.getValue() - 500)) {
                    saveData(true);
                } else {
                    Toast.makeText(this, getString(R.string.Make_sure_video_is_longer_than_15s), Toast.LENGTH_LONG).show();
                }*/
            if (viewModel.onDurationUpdate.getValue() == 15000) {
                if (!viewModel.isRecording.get() && (binding.progressBar.getTimePassed() >= 4500 || binding.progressBar.getTimePassed() >= viewModel.onDurationUpdate.getValue() - 500)) {
                    saveData(true);
                } else {
                    Toast.makeText(this, getString(R.string.Make_sure_video_is_longer_than_5s), Toast.LENGTH_LONG).show();
                }
            } else if (viewModel.onDurationUpdate.getValue() == 30000) {
                if (!viewModel.isRecording.get() && (binding.progressBar.getTimePassed() >= 9500 || binding.progressBar.getTimePassed() >= viewModel.onDurationUpdate.getValue() - 500)) {
                    saveData(true);
                } else {
                    Toast.makeText(this, getString(R.string.Make_sure_video_is_longer_than_10s), Toast.LENGTH_LONG).show();
                }
            } else if (viewModel.onDurationUpdate.getValue() == 45000) {
                if (!viewModel.isRecording.get() && (binding.progressBar.getTimePassed() >= 14500 || binding.progressBar.getTimePassed() >= viewModel.onDurationUpdate.getValue() - 500)) {
                    saveData(true);
                } else {
                    Toast.makeText(this, getString(R.string.Make_sure_video_is_longer_than_15s), Toast.LENGTH_LONG).show();
                }
            } else if (viewModel.onDurationUpdate.getValue() == 60000) {
                if (!viewModel.isRecording.get() && (binding.progressBar.getTimePassed() >= 19500 || binding.progressBar.getTimePassed() >= viewModel.onDurationUpdate.getValue() - 500)) {
                    saveData(true);
                } else {
                    Toast.makeText(this, getString(R.string.Make_sure_video_is_longer_than_20s), Toast.LENGTH_LONG).show();
                }
            } else {
                if (!viewModel.isRecording.get() && (binding.progressBar.getTimePassed() >= 4500 || binding.progressBar.getTimePassed() >= viewModel.onDurationUpdate.getValue() - 500)) {
                    saveData(true);
                } else {
                    Toast.makeText(this, getString(R.string.Make_sure_video_is_longer_than_5s), Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.progressBar.setShader(new int[]{ContextCompat.getColor(this, R.color.new_action_color), ContextCompat.getColor(this, R.color.new_action_color)});
    }


    private void initListener() {

        binding.btnCapture.setOnClickListener(v -> {
            if (binding.tvSoundTitle.getText().toString().trim().equalsIgnoreCase("")) {
                binding.llVideoTiming.setVisibility(View.VISIBLE);
            } else {
                binding.llVideoTiming.setVisibility(View.GONE);
            }
            if (!viewModel.isRecording.get()) {
                startReCording();
            } else {
                stopRecording();
            }
        });

        binding.btnFlip.setOnClickListener(v -> {
            viewModel.isFacingFront.set(!viewModel.isFacingFront.get());
            if (viewModel.isFacingFront.get()) {
                viewModel.lensFacing = CameraX.LensFacing.FRONT;
            } else {
                viewModel.lensFacing = CameraX.LensFacing.BACK;
            }
            binding.ivFlash.setImageResource(R.drawable.ic_flash_off);
            viewModel.preview.enableTorch(false);
            viewModel.isFlashOn.set(false);
            toggleCamera();
        });

        binding.tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                            ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_storage_permission, null), CameraActivity.this.getResources().getString(R.string.to_capture_storage_denied),
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
                        customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_storage_permission, null), CameraActivity.this.getResources().getString(R.string.to_capture_storage),
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
                    viewModel.preview.enableTorch(false);
                    viewModel.isFlashOn.set(false);
                    binding.ivFlash.setImageResource(R.drawable.ic_flash_off);
                    Intent i = new Intent(CameraActivity.this, GalleryFoldersActivityMedley.class);
                    i.putExtra("filter", "Video");
                    i.putExtra("call_type", callType);
                    startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);

                }
            }
        });

        binding.ivClose.setOnClickListener(v -> customDialogBuilder.showSimpleDialog(getString(R.string.are_you_sure), getString(R.string.Do_you_really_wan_to_go_back),
                getString(R.string.no), getString(R.string.yes), new CustomDialogBuilder.OnDismissListener() {
                    @Override
                    public void onPositiveDismiss() {
                        //1.86
                        viewModel.preview.enableTorch(false);
                        viewModel.isFlashOn.set(false);
                        binding.ivFlash.setImageResource(R.drawable.ic_flash_off);
                        viewModel.isBack = true;
                        onBackPressed();
                    }

                    @Override
                    public void onNegativeDismiss() {

                    }
                }));
    }

    private void recreateCamera() {
        binding.viewFinder.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                CameraX.unbindAll();
                startCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        });
    }

    private void toggleCamera() {
        CameraX.unbindAll();
        startCamera();
    }

    @SuppressLint("RestrictedApi")
    private void startCamera() {
        //   binding.viewFinder.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE);
        TextureView viewFinder = binding.viewFinder;
        AspectRatio ratio = AspectRatio.RATIO_4_3;
        viewModel.builder = new PreviewConfig.Builder();
        viewModel.previewConfig = viewModel.builder//.setTargetAspectRatio(ratio)
                .setTargetAspectRatioCustom(new Rational(aspectRatioNumerator, aspectRatioDenominator))
                .setLensFacing(viewModel.lensFacing)
                .setTargetRotation(Surface.ROTATION_90)
                .build();
        viewModel.preview = AutoFitPreviewBuilder.Companion.build(viewModel.previewConfig, viewFinder);

        System.out.println("=======================   " + aspectRatioNumerator + ":" + aspectRatioDenominator + "    =========================");

        viewModel.builder1 = new VideoCaptureConfig.Builder();

        viewModel.videoCaptureConfig = viewModel.builder1//.setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setTargetAspectRatioCustom(new Rational(3, 4))
                .setLensFacing(viewModel.lensFacing)
                .setVideoFrameRate(24)
                .setTargetRotation(Surface.ROTATION_0)
                .build();

        if (aspectRatioNumerator == 16 && aspectRatioDenominator == 9) {
            viewModel.videoCaptureConfig = viewModel.builder1.setTargetAspectRatio(AspectRatio.RATIO_16_9)
//                .setTargetAspectRatioCustom(new Rational(aspectRatioNumerator,aspectRatioDenominator))
                    .setLensFacing(viewModel.lensFacing)
                    .setVideoFrameRate(24)
                    .setTargetRotation(Surface.ROTATION_0)
                    .build();

        } else if (aspectRatioNumerator == 4 && aspectRatioDenominator == 3) {
            viewModel.videoCaptureConfig = viewModel.builder1.setTargetAspectRatio(AspectRatio.RATIO_4_3)
//                .setTargetAspectRatioCustom(new Rational(aspectRatioNumerator,aspectRatioDenominator))
                    //.setTargetResolution(new Size(width,width*4/3))
                    .setLensFacing(viewModel.lensFacing)
                    .setVideoFrameRate(24)
                    .setTargetRotation(Surface.ROTATION_0)
                    .build();

        } else if (aspectRatioNumerator == 1 && aspectRatioDenominator == 1) {
            viewModel.videoCaptureConfig = viewModel.builder1//.setTargetAspectRatio(ratio)
                    .setTargetAspectRatioCustom(new Rational(aspectRatioNumerator, aspectRatioDenominator))
                    .setLensFacing(viewModel.lensFacing)
                    .setVideoFrameRate(24)
                    .setTargetRotation(Surface.ROTATION_0)
                    .build();
        }

//        else if (aspectRatioNumerator == 3 && aspectRatioDenominator == 4) {
////            viewModel.videoCaptureConfig = viewModel.builder1//.setTargetAspectRatio(AspectRatio.RATIO_4_3)
////                .setTargetAspectRatioCustom(new Rational(aspectRatioNumerator,aspectRatioDenominator))
////                    .setTargetResolution(new Size(width,width*3/4))
////                    .setLensFacing(viewModel.lensFacing)
////                    .setVideoFrameRate(24)
////                    .setTargetRotation(Surface.ROTATION_0)
////                    .build();
//
//            viewModel.videoCaptureConfig = viewModel.builder1//.setTargetAspectRatio(AspectRatio.RATIO_4_3)
//                    .setTargetAspectRatioCustom(new Rational(3, 4))
//                    //.setTargetResolution(new Size(width,width*4/3))
//                    .setLensFacing(viewModel.lensFacing)
//                    .setVideoFrameRate(24)
//                    .setTargetRotation(Surface.ROTATION_0)
//                    .build();
//
//
//        } else if (aspectRatioNumerator == 9 && aspectRatioDenominator == 16) {
//            viewModel.videoCaptureConfig = viewModel.builder1.setTargetAspectRatio(AspectRatio.RATIO_16_9)
////                .setTargetAspectRatioCustom(new Rational(aspectRatioNumerator,aspectRatioDenominator))
//                    //.setTargetResolution(new Size(width,width*9/16))
//                    .setLensFacing(viewModel.lensFacing)
//                    .setVideoFrameRate(24)
//                    .setTargetRotation(Surface.ROTATION_90)
//                    .build();
//        }


//        viewModel.videoCaptureConfig = viewModel.builder1.setTargetAspectRatio(ratio)
////                .setTargetAspectRatioCustom(new Rational(aspectRatioNumerator,aspectRatioDenominator))
//                .setLensFacing(viewModel.lensFacing)
//                .setVideoFrameRate(24)
//                .setTargetRotation(Surface.ROTATION_0)
//                .build();


        viewModel.videoCapture = new VideoCapture(viewModel.videoCaptureConfig);

        CameraX.bindToLifecycle(this, viewModel.preview, viewModel.videoCapture);

    }


    private void initObserve() {

        viewModel.parentPath = getPath().getPath();
        viewModel.onItemClick.observe(this, type -> {
            if (type != null) {
                if (type == 1) {
//                  startActivity(new Intent(CameraActivity.this, MusicActivity.class));
                    viewModel.preview.enableTorch(false);
                    viewModel.isFlashOn.set(false);
                    binding.ivFlash.setImageResource(R.drawable.ic_flash_off);
                    MusicFrameFragment frameFragment = new MusicFrameFragment();
                    frameFragment.show(getSupportFragmentManager(), frameFragment.getClass().getSimpleName());
                }

                if (type == 2) {
                    setcolorfilter();
                    binding.tv15.setBackgroundResource(R.drawable.bg_black_corner_5);
                    binding.tv15.setTextColor(ContextCompat.getColor(this, R.color.white));
                    viewModel.onDurationUpdate.setValue(15000L);
                    binding.progressBar.publishProgress(0);
                    binding.divider15.setVisibility(View.VISIBLE);
                    binding.divider30.setVisibility(View.GONE);
                    binding.divider45.setVisibility(View.GONE);
                    binding.divider60.setVisibility(View.GONE);
//                    viewModel.isRecording.set(!viewModel.isRecording.get());
                    stopVideoRecording();
                }

                if (type == 3) {
                    setcolorfilter();
                    binding.tv30.setBackgroundResource(R.drawable.bg_black_corner_5);
                    binding.tv30.setTextColor(ContextCompat.getColor(this, R.color.white));
                    viewModel.onDurationUpdate.setValue(30000L);
                    binding.progressBar.publishProgress(0);
//                    viewModel.isRecording.set(!viewModel.isRecording.get());
                    binding.divider15.setVisibility(View.GONE);
                    binding.divider30.setVisibility(View.VISIBLE);
                    binding.divider45.setVisibility(View.GONE);
                    binding.divider60.setVisibility(View.GONE);
                    stopVideoRecording();
                }

                if (type == 4) {
                    setcolorfilter();
                    binding.tv45.setBackgroundResource(R.drawable.bg_black_corner_5);
                    binding.tv45.setTextColor(ContextCompat.getColor(this, R.color.white));
                    viewModel.onDurationUpdate.setValue(45000L);
                    binding.progressBar.publishProgress(0);
                    binding.divider15.setVisibility(View.GONE);
                    binding.divider30.setVisibility(View.GONE);
                    binding.divider45.setVisibility(View.VISIBLE);
                    binding.divider60.setVisibility(View.GONE);
                    stopVideoRecording();
//                    viewModel.isRecording.set(!viewModel.isRecording.get());
                }

                if (type == 5) {
                    setcolorfilter();
                    binding.tv60.setBackgroundResource(R.drawable.bg_black_corner_5);
                    binding.tv60.setTextColor(ContextCompat.getColor(this, R.color.white));
                    viewModel.onDurationUpdate.setValue(60000L);
                    binding.progressBar.publishProgress(0);
                    binding.divider15.setVisibility(View.GONE);
                    binding.divider30.setVisibility(View.GONE);
                    binding.divider45.setVisibility(View.GONE);
                    binding.divider60.setVisibility(View.VISIBLE);
                    stopVideoRecording();
//                    viewModel.isRecording.set(!viewModel.isRecording.get());
                }

//                if (type == 2) {
//                    Intent videoEditingIntent=new Intent(this, MainVideoEditingActivity.class);
//                    videoEditingIntent.putExtra("videoPath","");
//                    startActivity(videoEditingIntent);
//                    if (!viewModel.isRecording.get() && (binding.progressBar.getTimePassed() >= 4500 || binding.progressBar.getTimePassed() >= viewModel.onDurationUpdate.getValue() - 500)) {
//                        CallVideoEditing(true);
//                    } else {
//                        Toast.makeText(this, "Make sure video is longer than 5s...!", Toast.LENGTH_LONG).show();
//                    }
//                }
                viewModel.onItemClick.setValue(null);
            }
        });

        viewModel.onSoundSelect.observe(this, sound -> {
            if (sound != null) {
                binding.divider15.setVisibility(View.GONE);
                binding.divider30.setVisibility(View.GONE);
                binding.divider45.setVisibility(View.GONE);
                binding.divider60.setVisibility(View.GONE);
                binding.llVideoTiming.setVisibility(View.GONE);
                viewModel.soundId = sound.getSoundId();
                binding.tvSoundTitle.setText(sound.getSoundTitle());
                downLoadMusic(sound.getSound());
            }
        });
        viewModel.onDurationUpdate.observe(this, aLong -> binding.progressBar.enableAutoProgressView(aLong));
    }

    private void setcolorfilter() {
        binding.tv15.setBackgroundResource(R.drawable.bg_white_corner_5);
        binding.tv30.setBackgroundResource(R.drawable.bg_white_corner_5);
        binding.tv45.setBackgroundResource(R.drawable.bg_white_corner_5);
        binding.tv60.setBackgroundResource(R.drawable.bg_white_corner_5);
        binding.tv15.setTextColor(ContextCompat.getColor(this, R.color.black));
        binding.tv30.setTextColor(ContextCompat.getColor(this, R.color.black));
        binding.tv45.setTextColor(ContextCompat.getColor(this, R.color.black));
        binding.tv60.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    @SuppressLint("RestrictedApi")
    private void stopRecording() {
        viewModel.preview.enableTorch(false);
        viewModel.isFlashOn.set(false);
        binding.ivFlash.setImageResource(R.drawable.ic_flash_off);
        binding.btnCapture.clearAnimation();
        if (viewModel.audio != null) {
            viewModel.audio.pause();
        }

        viewModel.count += 1;
        binding.progressBar.pause();
        binding.progressBar.addDivider();
        viewModel.isRecording.set(false);
        if (binding.tvSoundTitle.getText().toString().trim().equalsIgnoreCase("")) {
            binding.llVideoTiming.setVisibility(View.VISIBLE);
        } else {
            binding.llVideoTiming.setVisibility(View.GONE);
        }
        viewModel.videoCapture.stopRecording();
    }

    @SuppressLint("RestrictedApi")
    private void stopVideoRecording() {
        viewModel.preview.enableTorch(false);
        viewModel.isFlashOn.set(false);
        binding.ivFlash.setImageResource(R.drawable.ic_flash_off);
        binding.btnCapture.clearAnimation();
//        if (viewModel.audio != null) {
//            viewModel.audio.pause();
//        }
//        viewModel.count = 0;
//        binding.progressBar.pause();
//        viewModel.isRecording.set(false);
//        viewModel.videoCapture.stopRecording();
        viewModel.videoPaths = new ArrayList<>();
    }

    private void initPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST);
        } else {
            recreateCamera();
        }
    }

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
//        Glide.with(this)
//                .load(R.drawable.loader_gif)
//                .into(progressBinding.ivParent);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                toggleCamera();
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    onBackPressed();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void downLoadMusic(String endPoint) {

        PRDownloader.download(endPoint, getPath().getPath(), "recordSound.mp3")
//        PRDownloader.download(Const.ITEM_BASE_URL + endPoint, getPath().getPath(), "recordSound.mp3")
                .build()
                .setOnStartOrResumeListener(() -> customDialogBuilder.showLoadingDialog())
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        customDialogBuilder.hideLoadingDialog();
                        viewModel.isStartRecording.set(true);
                        viewModel.createAudioForCamera();
                    }

                    @Override
                    public void onError(Error error) {
                        customDialogBuilder.hideLoadingDialog();
                    }
                });
    }

    @SuppressLint("RestrictedApi")
    private void startReCording() {
        binding.llVideoTiming.setVisibility(View.GONE);
        minResolution = Math.min(CameraX.getSurfaceManager().getPreviewSize().getHeight(), CameraX.getSurfaceManager().getPreviewSize().getWidth());
        binding.tvSetCountDownTimer.setVisibility(View.GONE);
        binding.ivAspectRatio.setVisibility(View.GONE);

        if (isRecordingForFirstTime) {
            viewModel.hideArrowPermanently.set(true);
            if (viewModel.countdownTimer.get() == 3) {
                binding.animationView.setAnimation(R.raw.timer3);
                binding.animationView.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        viewModel.isStartRecording.set(true);
                        Animation animation = AnimationUtils.loadAnimation(CameraActivity.this, R.anim.scale);
                        binding.btnCapture.startAnimation(animation);
                        if (binding.progressBar.getProgressPercent() != 100) {
                            if (viewModel.audio != null) {
                                viewModel.audio.start();
                            }
                            File file = new File(getPath(), "video".concat(String.valueOf(viewModel.count)).concat(".mp4"));
                            viewModel.videoPaths.add(getPath().getPath().concat("/video").concat(String.valueOf(viewModel.count)).concat(".mp4"));
                            viewModel.videoCapture.startRecording(file, Runnable::run, new VideoCapture.OnVideoSavedListener() {
                                @Override
                                public void onVideoSaved(@NonNull File file) {
                                }

                                @Override
                                public void onError(@NonNull VideoCapture.VideoCaptureError videoCaptureError, @NonNull String message, @Nullable Throwable cause) {

                                }
                            });
                            binding.progressBar.resume();
                            viewModel.isRecording.set(true);
                            isRecordingForFirstTime = false;
                        }
                        binding.animationView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                binding.animationView.setVisibility(View.VISIBLE);

                binding.animationView.playAnimation();

            } else if (viewModel.countdownTimer.get() == 10) {
                binding.animationView.setAnimation(R.raw.timer);
                binding.animationView.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        viewModel.isStartRecording.set(true);
                        Animation animation = AnimationUtils.loadAnimation(CameraActivity.this, R.anim.scale);
                        binding.btnCapture.startAnimation(animation);
                        if (binding.progressBar.getProgressPercent() != 100) {
                            if (viewModel.audio != null) {
                                viewModel.audio.start();
                            }
                            File file = new File(getPath(), "video".concat(String.valueOf(viewModel.count)).concat(".mp4"));
                            viewModel.videoPaths.add(getPath().getPath().concat("/video").concat(String.valueOf(viewModel.count)).concat(".mp4"));
                            viewModel.videoCapture.startRecording(file, Runnable::run, new VideoCapture.OnVideoSavedListener() {
                                @Override
                                public void onVideoSaved(@NonNull File file) {
                                }

                                @Override
                                public void onError(@NonNull VideoCapture.VideoCaptureError videoCaptureError, @NonNull String message, @Nullable Throwable cause) {

                                }
                            });
                            binding.progressBar.resume();
                            viewModel.isRecording.set(true);
                            isRecordingForFirstTime = false;
                            viewModel.hideArrowPermanently.set(true);
                        }
                        binding.animationView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                binding.animationView.setVisibility(View.VISIBLE);

                binding.animationView.playAnimation();

            } else {

                viewModel.isStartRecording.set(true);
                Animation animation = AnimationUtils.loadAnimation(CameraActivity.this, R.anim.scale);
                binding.btnCapture.startAnimation(animation);
                if (binding.progressBar.getProgressPercent() != 100) {
                    if (viewModel.audio != null) {
                        viewModel.audio.start();
                    }
                    File file = new File(getPath(), "video".concat(String.valueOf(viewModel.count)).concat(".mp4"));
                    viewModel.videoPaths.add(getPath().getPath().concat("/video").concat(String.valueOf(viewModel.count)).concat(".mp4"));
                    viewModel.videoCapture.startRecording(file, Runnable::run, new VideoCapture.OnVideoSavedListener() {
                        @Override
                        public void onVideoSaved(@NonNull File file) {
                        }

                        @Override
                        public void onError(@NonNull VideoCapture.VideoCaptureError videoCaptureError, @NonNull String message, @Nullable Throwable cause) {

                        }
                    });
                    binding.progressBar.resume();
                    viewModel.isRecording.set(true);
                    isRecordingForFirstTime = false;
                    viewModel.hideArrowPermanently.set(true);
                }
            }
        } else {
            viewModel.isStartRecording.set(true);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
            binding.btnCapture.startAnimation(animation);
            if (binding.progressBar.getProgressPercent() != 100) {
                if (viewModel.audio != null) {
                    viewModel.audio.start();
                }
                File file = new File(getPath(), "video".concat(String.valueOf(viewModel.count)).concat(".mp4"));
                viewModel.videoPaths.add(getPath().getPath().concat("/video").concat(String.valueOf(viewModel.count)).concat(".mp4"));
                viewModel.videoCapture.startRecording(file, Runnable::run, new VideoCapture.OnVideoSavedListener() {
                    @Override
                    public void onVideoSaved(@NonNull File file) {
                    }

                    @Override
                    public void onError(@NonNull VideoCapture.VideoCaptureError videoCaptureError, @NonNull String message, @Nullable Throwable cause) {

                    }
                });
                binding.progressBar.resume();
                viewModel.isRecording.set(true);
            }
        }


    }

    public void saveData(boolean isCompress) {
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
                    showProgressDialog();
                    if (progressBinding != null) {
                        if (viewModel.audio != null) {
                            progressBinding.progressBar.publishProgress((float) progress / 2);
//                            progressBinding.preparing.setText("Preparing..." + (long) ((progress / 2) * 100) + "%");
//                            Log.e("=========Progress_0", "Preparing..." + (long) ((progress / 2) * 100) + "%");
                        } else {
                            progressBinding.progressBar.publishProgress((float) progress);
//                            Log.e("=========Progress_1", String.valueOf(progressBinding.progressBar.getProgressPercent()));
//                            progressBinding.preparing.setText("Preparing..." + (long) Math.round(progress * 100) + "%");
                        }
                    }
//                    Log.d("TAG", "onTranscodeProgress: " + progress);
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
                                        if (progressBinding != null && viewModel.audio != null) {
                                            progressBinding.progressBar.publishProgress((float) (1 + progress) / 2);
//                                            progressBinding.preparing.setText("Preparing..." + (long) (((1 + progress) / 2) * 100) + "%");
//                                            Log.e("===========Progress_2", "Preparing..." + (long) (((1 + progress) / 2) * 100) + "%");
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

                                        hideProgressDialog();

                                        Intent intent = new Intent(CameraActivity.this, PreviewActivity.class);
                                        intent.putExtra("post_video", getPath().getPath().concat("/finally.mp4"));
                                        intent.putExtra("post_image", thumbFile.getPath());
                                        if (viewModel.soundId != null && !viewModel.soundId.isEmpty()) {
                                            intent.putExtra("soundId", viewModel.soundId);
                                        }
//                                        intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.mp3"));
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
//                        Log.i("TAG", "onCombineFinished: " + "is original sound");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Track audio;
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
                                                hideProgressDialog();
                                                CameraX.unbindAll();
                                                Intent intent = new Intent(CameraActivity.this, PreviewActivity.class);
                                                intent.putExtra("post_video", getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"));
                                                intent.putExtra("post_image", thumbFile.getPath());
                                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.mp3"));
                                                intent.putExtra("sound_image", getPath().getPath().concat("/soundImage.jpeg"));
                                                startActivityForResult(intent, 101);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }

                                            @Override
                                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                                super.onLoadFailed(errorDrawable);

                                                hideProgressDialog();
                                                Intent intent = new Intent(CameraActivity.this, PreviewActivity.class);
                                                intent.putExtra("post_video", getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"));
                                                intent.putExtra("post_image", thumbFile.getPath());
                                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.mp3"));

                                                startActivityForResult(intent, 101);
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
                Track audio;
                try {
//                    Movie m1 = MovieCreator.build(viewModel.videoPaths.get(0));
//                    audio = m1.getTracks().get(0);
//                    Movie m2 = new Movie();
//                    m2.addTrack(audio);
//                    DefaultMp4Builder builder = new DefaultMp4Builder();
//                    Container stdMp4 = builder.build(m2);
//                    FileOutputStream fos = new FileOutputStream(getPath().getPath().concat("/originalSound.mp3"));
//                    stdMp4.writeContainer(fos.getChannel());
//                    fos.close();

                    File temp = new File(getPath().getPath().concat("/originalSound.mp3"));

                    if (!temp.exists()) {
                        temp.createNewFile();
                    }

                    new AudioExtractor().genVideoUsingMuxer(viewModel.videoPaths.get(0), temp.getAbsolutePath(), -1, -1, true, false);


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
                                customDialogBuilder.showLoadingDialog();
                                Intent intent = new Intent(CameraActivity.this, PreviewActivity.class);
                                intent.putExtra("post_video", viewModel.videoPaths.get(0));
                                intent.putExtra("post_image", thumbFile.getPath());
                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.mp3"));
                                intent.putExtra("sound_image", getPath().getPath().concat("/soundImage.jpeg"));
                                startActivityForResult(intent, 101);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                customDialogBuilder.showLoadingDialog();
                                Intent intent = new Intent(CameraActivity.this, PreviewActivity.class);
                                intent.putExtra("post_video", viewModel.videoPaths.get(0));
                                intent.putExtra("post_image", thumbFile.getPath());
                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.mp3"));
                                //   intent.putExtra("sound_image", getPath().getPath().concat("/soundImage.jpeg"));
                                startActivityForResult(intent, 101);
                            }
                        });
            }).start();
        }
    }

    public File getPath() {
        String state = Environment.getExternalStorageState();
        File filesDir;
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            // We can read and write the media
//            filesDir = getExternalCacheDir();
//        } else {
//            // Load another directory, probably local memory
//            filesDir = getFilesDir();
//        }

        filesDir = getExternalCacheDir();
        if (filesDir != null) {
            viewModel.parentPath = filesDir.getPath();
        }
        return filesDir;
    }

    @Override
    protected void onDestroy() {
        if (viewModel.isRecording.get()) {
            stopRecording();
        }
        CameraX.unbindAll();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
//        Log.d("TAG", "onResume: ");
        initPermission();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (viewModel.isRecording.get()) {
            stopRecording();
        }
        CameraX.unbindAll();
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (viewModel.isRecording.get()) {
            stopRecording();
        }
        CameraX.unbindAll();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == 101 && !path.equals("") && !data.getBooleanExtra("cancelled", false)) {
                Intent intent = new Intent(CameraActivity.this, MainVideoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
//            finish();
            }
        }

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {

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
                                            viewModel.preview.enableTorch(false);
                                            viewModel.isFlashOn.set(false);
                                            binding.ivFlash.setImageResource(R.drawable.ic_flash_off);
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
                                            viewModel.preview.enableTorch(false);
                                            viewModel.isFlashOn.set(false);
                                            binding.ivFlash.setImageResource(R.drawable.ic_flash_off);
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
                                customDialogBuilder.showLoadingDialog();
                                CameraActivity.this.saveData(false);
                            }
                        } else {
                            customDialogBuilder.showSimpleDialog(CameraActivity.this.getResources().getString(R.string.Too_long_video_s_size), CameraActivity.this.getResources().getString(R.string.This_video_s_size_is_greater_than_500MbPlease_select_another),
                                    CameraActivity.this.getResources().getString(R.string.cancel), CameraActivity.this.getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {
                                        @Override
                                        public void onPositiveDismiss() {
//                                            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
                                            viewModel.preview.enableTorch(false);
                                            viewModel.isFlashOn.set(false);
                                            binding.ivFlash.setImageResource(R.drawable.ic_flash_off);
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

            } else {
                viewModel.isBack = true;
                onBackPressed();
            }
        } else {
            viewModel.isBack = true;
            onBackPressed();
        }

    }

    @Override
    public void onBackPressed() {
        if (viewModel.isBack) {
            setResult(RESULT_OK);
            CameraX.unbindAll();
            super.onBackPressed();
        } else {
            stopRecording();
            customDialogBuilder.showSimpleDialog(getString(R.string.are_you_sure), getString(R.string.Do_you_really_wan_to_go_back),
                    getString(R.string.no), getString(R.string.yes), new CustomDialogBuilder.OnDismissListener() {
                        @Override
                        public void onPositiveDismiss() {
                            //1.86
                            viewModel.isBack = true;
                            onBackPressed();
                        }

                        @Override
                        public void onNegativeDismiss() {
                            viewModel.isBack = false;
                        }
                    });
        }
    }
}



