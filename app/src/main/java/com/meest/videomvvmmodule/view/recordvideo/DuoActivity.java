package com.meest.videomvvmmodule.view.recordvideo;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.googlecode.mp4parser.authoring.Track;
import com.meest.R;
import com.meest.databinding.DailogProgressBinding;
import com.meest.databinding.DuoActivityBinding;
import com.meest.medley_camera2.camera2.view.activity.CameraActivity;
import com.meest.videoEditing.UtilCommand;
import com.meest.videomvvmmodule.AudioExtractor;
import com.meest.videomvvmmodule.gallery.GalleryFoldersActivityMedley;
import com.meest.videomvvmmodule.utils.AutoFitPreviewBuilder;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.preview.PreviewActivity;
import com.meest.videomvvmmodule.viewmodel.DuoViewModel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DuoActivity extends BaseActivity {
    private static final int MY_PERMISSIONS_REQUEST = 101;
    public DuoViewModel viewModel;
    private DuoActivityBinding binding;
    private CustomDialogBuilder customDialogBuilder, customDialogBuilderForPermission;
    private Dialog mBuilder;
    private DailogProgressBinding progressBinding;
    String format;
    private String thumbnail;
    MediaPlayer mediaPlayer;
    private String recordedVideoUri;
    private String outFile;
    List<String> filepathListTemp;
    private String rescaleVideo;
    private String recordedRescaleVideo;
    private String videoURL;
    private int width, height;
    private String musicUrl;
    public MediaPlayer audio;
    private boolean sizeGreaterThan300;
    float minResolution = 0;
    Thread thread;

    String callType = "video", uri;

    int LAUNCH_SECOND_ACTIVITY = 11;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = DataBindingUtil.setContentView(this, R.layout.duo_activity);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new DuoViewModel()).createFor()).get(DuoViewModel.class);
        customDialogBuilder = new CustomDialogBuilder(DuoActivity.this);
        customDialogBuilderForPermission = new CustomDialogBuilder(DuoActivity.this);
        filepathListTemp = new ArrayList<>();
        filepathListTemp.add("firstFile");
        filepathListTemp.add("secondFile");
        initView();
        initListener();
        initObserve();
        initProgressDialog();
        binding.setViewModel(viewModel);

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

    @SuppressLint("RestrictedApi")
    private void initView() {
        customDialogBuilder.showLoadingDialog();
        if (getIntent().getExtras() != null) {
            musicUrl = getIntent().getStringExtra("music_url");
//            Log.e("sound_url", musicUrl);
        }

        if (musicUrl != null && !musicUrl.isEmpty()) {
            if (getIntent().getStringExtra("soundId") != null) {
                viewModel.soundId = getIntent().getStringExtra("soundId");
                downLoadMusic(getIntent().getStringExtra("music_url"));
            }
        }

        if (getIntent().getStringExtra("videoPath") != null) {

            videoURL = getIntent().getStringExtra("videoPath");
            thumbnail = getIntent().getExtras().getString("videoPath");
            Glide.with(this).load(thumbnail).into(binding.videoViewThumbnail);
//            Log.e("videoPath++++", getIntent().getStringExtra("videoPath"));
        }

//        Log.e("==========videoFile", videoURL.toString());
        thread = new Thread(() -> {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(videoURL, new HashMap<>());
            width = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            height = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            retriever.release();

            long timeInMilliSec = Long.parseLong(time);
            viewModel.onDurationUpdate.postValue(timeInMilliSec);
            customDialogBuilder.hideLoadingDialog();
        });

        thread.start();

        binding.videoView.setVideoPath(videoURL);
        binding.videoView.setOnPreparedListener(mp -> mediaPlayer = mp);
        if (viewModel.onDurationUpdate.getValue() != null) {
            binding.progressBar.enableAutoProgressView(viewModel.onDurationUpdate.getValue());
        }
        binding.progressBar.setDividerColor(Color.WHITE);
        binding.progressBar.setDividerEnabled(true);
        binding.progressBar.setDividerWidth(4);
        binding.progressBar.setListener(mills -> {
            if (mills == viewModel.onDurationUpdate.getValue()) {
                stopRecording();
                saveData(true);
            }
        });
//        binding.recordingTime.setText(String.valueOf(viewModel.onDurationUpdate.getValue()));
        binding.ivSelect.setOnClickListener(v -> {
            viewModel.preview.enableTorch(false);
            viewModel.isFlashOn.set(false);
            binding.ivFlash.setBackgroundResource(R.drawable.ic_flash_off);
            saveData(true);
        });
        binding.progressBar.setShader(new int[]{ContextCompat.getColor(this, R.color.new_action_color),
                ContextCompat.getColor(this, R.color.new_action_color_blue)});
    }

    private void initListener() {
        binding.btnCapture.setOnClickListener(v -> {
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
            binding.ivFlash.setBackgroundResource(R.drawable.ic_flash_off);
            viewModel.preview.enableTorch(false);
            viewModel.isFlashOn.set(false);
            toggleCamera();
        });

        binding.ivClose.setOnClickListener(v -> customDialogBuilder.showSimpleDialog("", getString(R.string.Do_you_really_wan_to_go_back),
                getString(R.string.no), getString(R.string.yes), new CustomDialogBuilder.OnDismissListener() {
                    @Override
                    public void onPositiveDismiss() {
                        //1.86
                        viewModel.preview.enableTorch(false);
                        viewModel.isFlashOn.set(false);
                        binding.ivFlash.setBackgroundResource(R.drawable.ic_flash_off);
                        viewModel.isBack = true;
                        onBackPressed();
                    }

                    @Override
                    public void onNegativeDismiss() {

                    }
                }));
//        binding.tvSelect.setOnClickListener(v -> {
//            viewModel.preview.enableTorch(false);
//            viewModel.isFlashOn.set(false);
//            binding.ivFlash.setImageResource(R.drawable.ic_flash_off);
//            BottomSheetImagePicker bottomSheetImagePicker = BottomSheetImagePicker.Companion.getNewInstance(1);
//            bottomSheetImagePicker.setOnDismiss(uri -> {
//                String fileExtention = "";
//
//                int i = uri.lastIndexOf('.');
//                if (i > 0) {
//                    fileExtention = uri.substring(i + 1);
//                }
//
//                Log.e("filePath=======", fileExtention);
//                if (fileExtention.equalsIgnoreCase("mp4")) {
//                    if (!uri.isEmpty()) {
//                        File file = new File(uri);
//                        // Get length of file in bytes
//                        long fileSizeInBytes = file.length();
//                        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
//                        long fileSizeInKB = fileSizeInBytes / 1024;
//                        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
//                        long fileSizeInMB = fileSizeInKB / 1024;
//
//                        if (fileSizeInMB > 300) {
//                            sizeGreaterThan300 = true;
//                        }
//
////                        if (fileSizeInMB > 50 && fileSizeInMB <= 100) {
////                            compressionLevel = 1;
////                        } else if (fileSizeInMB > 100 && fileSizeInMB <= 150) {
////                            compressionLevel = 2;
////                        } else if (fileSizeInMB > 150 && fileSizeInMB <= 200) {
////                            compressionLevel = 3;
////                        } else if (fileSizeInMB > 200 && fileSizeInMB <= 250) {
////                            compressionLevel = 4;
////                        } else if (fileSizeInMB > 250 && fileSizeInMB <= 300) {
////                            compressionLevel = 5;
////                        } else if (fileSizeInMB > 300 && fileSizeInMB <= 350) {
////                            compressionLevel = 6;
////                        } else if (fileSizeInMB > 350 && fileSizeInMB <= 400) {
////                            compressionLevel = 7;
////                        } else if (fileSizeInMB > 400 && fileSizeInMB <= 450) {
////                            compressionLevel = 8;
////                        } else if (fileSizeInMB > 450 && fileSizeInMB <= 500) {
////                            compressionLevel = 9;
////                        } else {
////                            compressionLevel = 10;
////                        }
//
//                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                        retriever.setDataSource(this, Uri.fromFile(new File(uri)));
//                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//
//                        minResolution = Math.min(Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)), Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)));
//
//                        long timeInMilliSec = Long.parseLong(time);
//                        Log.e("timeInMilliSec", "=======================" + timeInMilliSec);
//                        if (timeInMilliSec < 4900) {
//                            customDialogBuilder.showSimpleDialog(getResources().getString(R.string.Video_Too_Short), getResources().getString(R.string.This_video_is_shorter_than_5_seconds_Please_select_another),
//                                    getResources().getString(R.string.cancel), getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {
//
//                                        @Override
//                                        public void onPositiveDismiss() {
//                                            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
//                                        }
//
//                                        @Override
//                                        public void onNegativeDismiss() {
//
//                                        }
//                                    });
//                        } else if (timeInMilliSec >= 60200) {
//                            customDialogBuilder.showSimpleDialog(getResources().getString(R.string.Video_Too_Long), getResources().getString(R.string.This_video_is_longer_than_1_min_Please_select_another),
//                                    getResources().getString(R.string.cancel), getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {
//
//                                        @Override
//                                        public void onPositiveDismiss() {
//                                            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
//                                        }
//
//                                        @Override
//                                        public void onNegativeDismiss() {
//
//                                        }
//                                    });
//                        } else if (fileSizeInMB < 500) {
//                            viewModel.videoPaths = new ArrayList<>();
//                            viewModel.videoPaths.add(uri);
//                            if (fileSizeInMB > 5) {
//                                saveData(true);
//                            } else {
//                                customDialogBuilder.showLoadingDialog();
//                                saveData(false);
//                            }
//                        } else {
//                            customDialogBuilder.showSimpleDialog(getResources().getString(R.string.Too_long_video_s_size), getResources().getString(R.string.This_video_s_size_is_greater_than_500MbPlease_select_another),
//                                    getResources().getString(R.string.cancel), getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {
//                                        @Override
//                                        public void onPositiveDismiss() {
//                                            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
//                                        }
//
//                                        @Override
//                                        public void onNegativeDismiss() {
//
//                                        }
//                                    });
//                        }
//                        retriever.release();
//                    }
//                } else {
//                    Toast.makeText(this, getString(R.string.Please_Select_mp4_format_video), Toast.LENGTH_SHORT).show();
//                }
//            });
//            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
//        });
        binding.tvSelect.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(DuoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(DuoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(DuoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                        ContextCompat.checkSelfPermission(DuoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_storage_permission, null), DuoActivity.this.getResources().getString(R.string.to_capture_storage_denied),
                            DuoActivity.this.getResources().getString(R.string.not_now), DuoActivity.this.getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                                @Override
                                public void onPositiveDismiss() {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", DuoActivity.this.getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }

                                @Override
                                public void onNegativeDismiss() {

                                }
                            });
                } else {
                    customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_storage_permission, null), DuoActivity.this.getResources().getString(R.string.to_capture_storage),
                            DuoActivity.this.getResources().getString(R.string.not_now), DuoActivity.this.getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

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
                Intent i = new Intent(DuoActivity.this, GalleryFoldersActivityMedley.class);
                i.putExtra("filter", "Video");
                i.putExtra("call_type", callType);
                startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
            }
        });
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
        viewModel.previewConfig = viewModel.builder.setTargetAspectRatio(ratio)
                .setLensFacing(viewModel.lensFacing)
                .setTargetRotation(Surface.ROTATION_90)
                .build();
//        PreviewConfig.Builder().apply {
//            setTargetAspectRatio(Rational(1, 1))
//            setTargetResolution(Size(640, 640))
//        }.build()
        viewModel.preview = AutoFitPreviewBuilder.Companion.build(viewModel.previewConfig, viewFinder);
        viewModel.builder1 = new VideoCaptureConfig.Builder();
        viewModel.videoCaptureConfig = viewModel.builder1.setTargetAspectRatio(ratio)
                .setLensFacing(viewModel.lensFacing)
                .setVideoFrameRate(24)
                .setTargetRotation(Surface.ROTATION_0)
                .build();
        viewModel.videoCapture = new VideoCapture(viewModel.videoCaptureConfig);
        CameraX.bindToLifecycle(this, viewModel.preview, viewModel.videoCapture);
    }

    private void initObserve() {
        viewModel.parentPath = getPath().getPath();
        viewModel.onDurationUpdate.observe(this, aLong -> binding.progressBar.enableAutoProgressView(aLong));
    }

    @SuppressLint("RestrictedApi")
    private void stopRecording() {
        viewModel.preview.enableTorch(false);
        viewModel.isFlashOn.set(false);
        binding.videoView.pause();
        binding.ivFlash.setBackgroundResource(R.drawable.ic_flash_off);
        binding.btnCapture.clearAnimation();
        if (viewModel.audio != null) {
            viewModel.audio.pause();
        }
        viewModel.count += 1;
        binding.progressBar.pause();
        binding.progressBar.addDivider();
        viewModel.isRecording.set(false);
        viewModel.videoCapture.stopRecording();
    }

    private void initPermission() {
        if (ContextCompat.checkSelfPermission(DuoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(DuoActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                customDialogBuilderForPermission.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_permission, null), DuoActivity.this.getResources().getString(R.string.to_capture_photos_and_videos),
                        DuoActivity.this.getResources().getString(R.string.not_now), DuoActivity.this.getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", DuoActivity.this.getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            } else {
                customDialogBuilderForPermission.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_permission, null), DuoActivity.this.getResources().getString(R.string.to_capture_photos_and_videos),
                        DuoActivity.this.getResources().getString(R.string.not_now), DuoActivity.this.getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                requestPermissionLauncher.launch(
                                        Manifest.permission.CAMERA);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            }
        } else if (ContextCompat.checkSelfPermission(DuoActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(DuoActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                customDialogBuilderForPermission.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_microphone_permission, null), DuoActivity.this.getResources().getString(R.string.to_capture_microphone),
                        DuoActivity.this.getResources().getString(R.string.not_now), DuoActivity.this.getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", DuoActivity.this.getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            } else {
                customDialogBuilderForPermission.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_microphone_permission, null), DuoActivity.this.getResources().getString(R.string.to_capture_microphone),
                        DuoActivity.this.getResources().getString(R.string.not_now), DuoActivity.this.getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                requestPermissionLauncher.launch(
                                        Manifest.permission.RECORD_AUDIO);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            }
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

    @SuppressLint("RestrictedApi")
    private void startReCording() {
        viewModel.isFirstTime.set(false);
        viewModel.isStartRecording.set(true);
        binding.videoViewThumbnail.setVisibility(View.GONE);
        binding.videoView.start();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        binding.btnCapture.startAnimation(animation);
        if (binding.progressBar.getProgressPercent() != 100) {
//            Log.e("========mayank", "progress:true");
            if (viewModel.audio != null) {
//                Log.e("========mayank", "progress:music");
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
        } else {
//            Log.e("========mayank", "progress:false");
        }
    }

    public void saveData(boolean isCompress) {
        if (isCompress) {
            DefaultVideoStrategy strategy = new DefaultVideoStrategy.Builder()
                    .addResizer(new FractionResizer(0.6F))
                    .addResizer(new AtMostResizer(1000))
                    .build();
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
                            Log.e("=========Progress_0", "Preparing..." + (long) ((progress / 2) * 100) + "%");
                        } else {
                            progressBinding.progressBar.publishProgress((float) progress);
                            Log.e("=========Progress_1", String.valueOf(progressBinding.progressBar.getProgressPercent()));
                        }
                    }
                    Log.d("TAG", "onTranscodeProgress: " + progress);
                }

                public void onTranscodeCompleted(int successCode) {

                    Log.d("TAG", "onTranscodeCompleted: " + successCode);

                    if (viewModel.audio != null) {

                        Transcoder.into(getPath().getPath().concat("/finally.mp4"))
                                .addDataSource(TrackType.VIDEO, getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"))
                                .addDataSource(TrackType.AUDIO, getPath().getPath().concat("/Medley_" + sessionManager.getUser().getData().getUserName() + "_" + viewModel.soundId + ".mp3"))
                                .setVideoTrackStrategy(strategy)
                                .setListener(new TranscoderListener() {
                                    @Override
                                    public void onTranscodeProgress(double progress) {
                                        if (progressBinding != null && viewModel.audio != null) {
                                            progressBinding.progressBar.publishProgress((float) (1 + progress) / 2);
//                                            progressBinding.preparing.setText("Preparing..." + (long) (((1 + progress) / 2) * 100) + "%");
                                            Log.e("===========Progress_2", "Preparing..." + (long) (((1 + progress) / 2) * 100) + "%");
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
                                            }
                                            stream.flush();
                                            stream.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        hideProgressDialog();
                                        recordedVideoUri = getPath().getPath().concat("/finally.mp4");
                                        concatenate();
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
                                Track audio;
                                try {
//                                    Movie m1 = MovieCreator.build(getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"));
//                                    audio = m1.getTracks().get(1);
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
                                Glide.with(DuoActivity.this)
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
                                                recordedVideoUri = getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4");
                                                concatenate();


                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }

                                            @Override
                                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                                super.onLoadFailed(errorDrawable);

                                                hideProgressDialog();
                                                recordedVideoUri = getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4");
                                                concatenate();


                                            }
                                        });
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
                try {
//                    Movie m1 = MovieCreator.build(viewModel.videoPaths.get(0));
//                    audio = m1.getTracks().get(1);
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
                Glide.with(DuoActivity.this)
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
                                recordedVideoUri = viewModel.videoPaths.get(0);
                                concatenate();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                customDialogBuilder.hideLoadingDialog();
                                recordedVideoUri = viewModel.videoPaths.get(0);
                                concatenate();
                            }
                        });
            }).start();
        }
    }

    public File getPath() {
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            filesDir = getExternalCacheDir();
        } else {
            // Load another directory, probably local memory
            filesDir = getFilesDir();
        }
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
        Log.d("TAG", "onResume: ");
        initPermission();
//        if (mediaPlayer != null) {
//            mediaPlayer.start();
//        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (viewModel.isRecording.get()) {
            stopRecording();
        }
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
        }
//        thread.interrupt();
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        viewModel.isBack = true;
//        onBackPressed();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 101) {
                finish();
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
                        retriever.setDataSource(DuoActivity.this, Uri.fromFile(new File(uri)));
                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                        minResolution = Math.min(Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)), Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)));

                        long timeInMilliSec = Long.parseLong(time);
                        Log.e("timeInMilliSec", "=======================" + timeInMilliSec);
                        if (timeInMilliSec < 4900) {
                            customDialogBuilder.showSimpleDialog(DuoActivity.this.getResources().getString(R.string.Video_Too_Short), DuoActivity.this.getResources().getString(R.string.This_video_is_shorter_than_5_seconds_Please_select_another),
                                    DuoActivity.this.getResources().getString(R.string.cancel), DuoActivity.this.getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {

                                        @Override
                                        public void onPositiveDismiss() {
//                                            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
                                            viewModel.preview.enableTorch(false);
                                            viewModel.isFlashOn.set(false);
                                            binding.ivFlash.setImageResource(R.drawable.ic_flash_off);
                                            Intent i = new Intent(DuoActivity.this, GalleryFoldersActivityMedley.class);
                                            i.putExtra("filter", "Video");
                                            i.putExtra("call_type", callType);
                                            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
                                        }

                                        @Override
                                        public void onNegativeDismiss() {

                                        }
                                    });
                        } else if (timeInMilliSec >= 60200) {
                            customDialogBuilder.showSimpleDialog(DuoActivity.this.getResources().getString(R.string.Video_Too_Long), DuoActivity.this.getResources().getString(R.string.This_video_is_longer_than_1_min_Please_select_another),
                                    DuoActivity.this.getResources().getString(R.string.cancel), DuoActivity.this.getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {

                                        @Override
                                        public void onPositiveDismiss() {
//                                            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
                                            viewModel.preview.enableTorch(false);
                                            viewModel.isFlashOn.set(false);
                                            binding.ivFlash.setImageResource(R.drawable.ic_flash_off);
                                            Intent i = new Intent(DuoActivity.this, GalleryFoldersActivityMedley.class);
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
                                DuoActivity.this.saveData(true);
                            } else {
                                customDialogBuilder.showLoadingDialog();
                                DuoActivity.this.saveData(false);
                            }
                        } else {
                            customDialogBuilder.showSimpleDialog(DuoActivity.this.getResources().getString(R.string.Too_long_video_s_size), DuoActivity.this.getResources().getString(R.string.This_video_s_size_is_greater_than_500MbPlease_select_another),
                                    DuoActivity.this.getResources().getString(R.string.cancel), DuoActivity.this.getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {
                                        @Override
                                        public void onPositiveDismiss() {
//                                            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
                                            viewModel.preview.enableTorch(false);
                                            viewModel.isFlashOn.set(false);
                                            binding.ivFlash.setImageResource(R.drawable.ic_flash_off);
                                            Intent i = new Intent(DuoActivity.this, GalleryFoldersActivityMedley.class);
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
                    Toast.makeText(DuoActivity.this, DuoActivity.this.getString(R.string.Please_Select_mp4_format_video), Toast.LENGTH_SHORT).show();
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

    private void onBackPerform(boolean isBack) {

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

    public void concatenate() {
        StringBuilder sb1 = new StringBuilder();
        sb1.append(getExternalCacheDir());
        File file = new File(sb1.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder stringBuilder_recorded = new StringBuilder();
        stringBuilder_recorded.append(getExternalCacheDir());
        stringBuilder_recorded.append("/recorded_rescale");
        stringBuilder_recorded.append(format);
        stringBuilder_recorded.append(".mp4");
        rescaleVideo = stringBuilder_recorded.toString();
        String scale = "scale=" + width + ":" + height;
        Log.e("TAG", "concatenate: " + scale);
        Log.e("TAG", "rescaleVideo: " + rescaleVideo);
        recordedRescaleVideo = rescaleVideoFFMPEG(new String[]{"-y", "-i", recordedVideoUri, "-preset", "ultrafast", "-vf", scale, rescaleVideo}, rescaleVideo);

//        ===================================================================recording video scalling end ==================================================================

        StringBuilder sb2 = new StringBuilder();
        sb2.append(getExternalCacheDir());
        sb2.append("/Duet_video_" + sessionManager.getUser().getData().getUserName() + "_");
        sb2.append(format);
        sb2.append(".mp4");
        outFile = sb2.toString();
        Log.e("TAG", "concatenate: " + outFile);
        Log.e("recordingUrl", "====================================" + recordedRescaleVideo);
//      execFFmpegBinaryForConcat(new String[]{"-i", recordedVideoUri, "-i", videoFile, "-filter_complex", "[0:v]pad=iw*2:ih[int];[int][1:v]overlay=W/2:0[vid]", "-map", "[vid]", "-crf"," 23", "-preset", "veryfast", outFile}, outFile);//working code without sound
    }

    private String rescaleVideoFFMPEG(String[] strings, String rescaleFile) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        String ffmpegCommand = UtilCommand.main(strings);
        FFmpeg.executeAsync(ffmpegCommand, new ExecuteCallback() {
            @Override
            public void apply(final long executionId, final int returnCode) {

                progressDialog.dismiss();

                Log.d("TAG", String.format("FFmpeg process exited with rc %d.", returnCode));

                Log.d("TAG", "FFmpeg process output:");

                Config.printLastCommandOutput(Log.INFO);

                if (returnCode == RETURN_CODE_SUCCESS) {
                    Intent intent1 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent1.setData(Uri.fromFile(new File(DuoActivity.this.rescaleVideo)));
                    DuoActivity.this.sendBroadcast(intent1);
                    execFFmpegBinaryForConcat(new String[]{"-y", "-i", rescaleVideo, "-i", videoURL, "-filter_complex", "[0:v]pad=iw*2:ih:0:0[intv];[intv][1:v]overlay=W/2:0[vid]", "-map", "[vid]", "-map", "1:a", "-crf", "22", "-preset", "ultrafast", outFile}, outFile);//working code without sound
//                  execFFmpegBinaryForConcat(new String[]{"-i", recordedVideoUri, "-i", videoFile, "-filter_complex", "[0:v]pad=iw*2:ih:0:0[intv];[intv][1:v]overlay=W/2:0[vid]", "-map", "[vid]", "-map", "0:a", "-crf", "22", "-preset", "veryfast", outFile}, outFile);//working code without sound
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    Log.d("ffmpegFailure", outFile);
                    try {
                        Toast.makeText(DuoActivity.this, "Error Creating Video", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    try {
                        Toast.makeText(DuoActivity.this, "Error Creating Video", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", returnCode));
                }
            }
        });
        getWindow().clearFlags(16);
        return DuoActivity.this.rescaleVideo;
    }

    private void execFFmpegBinaryForConcat(String[] strArr, final String str) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        String ffmpegCommand = UtilCommand.main(strArr);
        FFmpeg.executeAsync(ffmpegCommand, new ExecuteCallback() {

            @Override
            public void apply(final long executionId, final int returnCode) {
                Log.d("TAG", String.format("FFmpeg process exited with rc %d.", returnCode));

                Log.d("TAG", "FFmpeg process output:");

                Config.printLastCommandOutput(Log.INFO);

                progressDialog.dismiss();
                if (returnCode == RETURN_CODE_SUCCESS) {
                    progressDialog.dismiss();
                    Intent intent1 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent1.setData(Uri.fromFile(new File(DuoActivity.this.outFile)));
                    DuoActivity.this.sendBroadcast(intent1);
//                    new File(str).delete();
                    File thumbFile = new File(getPath(), "/" + "user_thumbnail_" + sessionManager.getUser().getData().getUserName() + format + ".jpg");
                    try {
                        FileOutputStream stream = new FileOutputStream(thumbFile);

                        Bitmap bmThumbnail;

                        bmThumbnail = ThumbnailUtils.createVideoThumbnail(DuoActivity.this.outFile,
                                MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                        if (bmThumbnail != null) {
                            bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                        }
                        stream.flush();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(DuoActivity.this, PreviewActivity.class);
                    intent.putExtra("post_video", outFile);
                    intent.putExtra("post_image", thumbFile.getPath());
                    intent.putExtra("post_sound", getPath().getPath().concat("/Medley_" + sessionManager.getUser().getData().getUserName() + "_" + viewModel.soundId + ".mp3"));
                    intent.putExtra("sound_image", getPath().getPath().concat("/soundImage.jpeg"));
                    startActivityForResult(intent, 101);
//                    viewModel.fetchVideoUrl(outFile);
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    Log.d("ffmpegfailure", str);
                    new File(str).delete();
                    try {
                        Toast.makeText(DuoActivity.this, "Error Creating Video", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    new File(str).delete();
                    try {
                        Toast.makeText(DuoActivity.this, "Error Creating Video", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", returnCode));
                }
            }
        });
        getWindow().clearFlags(16);
    }

    private void downLoadMusic(String endPoint) {
        String parent = new File(getExternalCacheDir().getPath()).getPath();
        String path = new File(parent.concat("/Medley_" + sessionManager.getUser().getData().getUserName() + "_" + viewModel.soundId + ".mp3")).getPath();
        Log.e("path=======", path);
        PRDownloader.download(endPoint, new File(getExternalCacheDir().getPath()).getPath(), "Medley_" + sessionManager.getUser().getData().getUserName() + "_" + viewModel.soundId + ".mp3")
//        PRDownloader.download(Const.ITEM_BASE_URL + endPoint, getPath().getPath(), "recordSound.mp3")
                .build()
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        viewModel.createAudioForCamera(sessionManager, DuoActivity.this);
                    }

                    @Override
                    public void onError(Error error) {
                    }
                });
    }

}