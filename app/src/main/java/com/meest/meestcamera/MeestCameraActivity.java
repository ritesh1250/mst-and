package com.meest.meestcamera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.PreviewConfig;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.VideoCaptureConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.meest.Activities.VideoPost.PreviewVideoActivity;
import com.meest.Activities.preview.PreviewPhotoActivity;
import com.meest.R;

import com.meest.databinding.ActivityMeestCameraBinding;
import com.meest.databinding.DailogProgressBinding;

import com.meest.mediapikcer.GalleryFoldersActivity;
import com.meest.mediapikcer.utils.PictureFacer;
import com.meest.meestcamera.viewmodule.MeestCameraViewModel;
import com.meest.videomvvmmodule.utils.AutoFitPreviewBuilder;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.music.MusicFrameFragment;
import com.meest.videomvvmmodule.view.preview.PreviewActivity;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;
import com.googlecode.mp4parser.authoring.Track;
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
import java.util.ArrayList;

public class MeestCameraActivity extends BaseActivity {

    private static final int MY_PERMISSIONS_REQUEST = 101;
    int LAUNCH_SECOND_ACTIVITY = 1231;
    public MeestCameraViewModel viewModel;
    private ActivityMeestCameraBinding binding;
    private CustomDialogBuilder customDialogBuilder;
    private Dialog mBuilder;
    boolean isVideo = false;
    String callType = "";
    private DailogProgressBinding progressBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meest_camera);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_meest_camera);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new MeestCameraViewModel()).createFor()).get(MeestCameraViewModel.class);
        customDialogBuilder = new CustomDialogBuilder(MeestCameraActivity.this);
        initView();
        initListener();
        initObserve();
        initProgressDialog();
        binding.setViewModel(viewModel);

    }
    @SuppressLint("RestrictedApi")
    private void initView() {
        isVideo = getIntent().getBooleanExtra("isVideo", false);
        callType = getIntent().getStringExtra("type");
        if (callType==null)
        {
            callType = "feed";
        }

        Log.i("CALLLL",callType+"---"+isVideo);

        if(isVideo){
            if (viewModel.onDurationUpdate.getValue() != null) {
                binding.progressBar.enableAutoProgressView(viewModel.onDurationUpdate.getValue());
            }
            binding.progressBar.setDividerColor(Color.WHITE);
            binding.progressBar.setDividerEnabled(true);
            binding.progressBar.setDividerWidth(4);
            binding.progressBar.setListener(mills -> {
                viewModel.isEnabled.set(mills >= 4500);
                if (mills == viewModel.onDurationUpdate.getValue()) {
                    stopRecording();
                }
            });
            binding.ivSelect.setOnClickListener(v -> {
                if (!viewModel.isRecording.get() && (binding.progressBar.getTimePassed() >= 4500 || binding.progressBar.getTimePassed() >= viewModel.onDurationUpdate.getValue() - 500)) {
                    saveData(true);
                } else {
                    Toast.makeText(this,
                            getString(R.string.Make_sure_video_is_longer_than_5s), Toast.LENGTH_LONG).show();
                }
            });
            binding.progressBar.setShader(new int[]{ContextCompat.getColor(this, R.color.colorTheme), ContextCompat.getColor(this, R.color.colorTheme), ContextCompat.getColor(this, R.color.colorTheme)});


        }else{

            viewModel.isCapturing.set(true);
            binding.card.setVisibility(View.GONE);
        }

    }


    private void initListener() {
        binding.btnCapture.setOnClickListener(v -> {
            if(isVideo) {
                if (!viewModel.isRecording.get()) {
                    startReCording();
                } else {
                    stopRecording();
                }
            }else{

                takePhoto();
            }
        });
        binding.btnFlip.setOnClickListener(v -> {
            viewModel.isFacingFront.set(!viewModel.isFacingFront.get());
            if (viewModel.isFacingFront.get()) {
                viewModel.lensFacing = CameraX.LensFacing.FRONT;
            } else {
                viewModel.lensFacing = CameraX.LensFacing.BACK;
            }
            toggleCamera();
        });
        binding.tvSelect.setOnClickListener(v -> {
            Intent i = new Intent(MeestCameraActivity.this, GalleryFoldersActivity.class);
            if (isVideo){
                i.putExtra("filter", "Video");
            }else {
                i.putExtra("filter", "Image");
            }
            i.putExtra("call_type", callType);
            i.putExtra("isVideo", isVideo);
            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);

//
//            if(isVideo){
//                BottomSheetImagePicker bottomSheetImagePicker = BottomSheetImagePicker.Companion.getNewInstance(1);
//                bottomSheetImagePicker.setOnDismiss(uri -> {
//                    if (!uri.isEmpty()) {
//                        File file = new File(uri);
//                        // Get length of file in bytes
//                        long fileSizeInBytes = file.length();
//                        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
//                        long fileSizeInKB = fileSizeInBytes / 1024;
//                        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
//                        long fileSizeInMB = fileSizeInKB / 1024;
//                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                        retriever.setDataSource(this, Uri.fromFile(new File(uri)));
//                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                        long timeInMilliSec = Long.parseLong(time);
//                        if (timeInMilliSec > 60000) {
//                            customDialogBuilder.showSimpleDialogMeest("Too long video", "This video is longer than 1 min.\nPlease select another..",
//                                    "Cancel", "Select another", new CustomDialogBuilder.OnDismissListener() {
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
//                        } else if (fileSizeInMB <100) {
//
//                            viewModel.videoPaths = new ArrayList<>();
//                            viewModel.videoPaths.add(uri);
//                            if (fileSizeInMB > 5) {
//                                saveData(true);
//                            } else {
//                                customDialogBuilder.showLoadingDialog();
//                                saveData(false);
//                            }
//                        } else {
//                            customDialogBuilder.showSimpleDialogMeest("Too long video's size", "This video's size is grater than 100Mb.\nPlease select another..",
//                                    "Cancel", "Select another", new CustomDialogBuilder.OnDismissListener() {
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
//                });
//                bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
//            }else{
//                BottomSheetImagePicker bottomSheetImagePicker = BottomSheetImagePicker.Companion.getNewInstance(2);
//                bottomSheetImagePicker.setOnDismiss(uri -> {
//                    if(!uri.isEmpty()){
//                        File photoFile = new File(uri);
//                        Intent intent = new Intent(MeestCameraActivity.this, PreviewPhotoActivity.class);
//                        intent.putExtra("imagePath", photoFile.getPath());
//                        if (callType.equalsIgnoreCase("story"))
//                           intent.putExtra("isStory", true);
//                        else
//                            intent.putExtra("isStory", false);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//                bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
//            }
        });

        binding.ivClose.setOnClickListener(v -> customDialogBuilder.showSimpleDialogMeest(getString(R.string.are_you_sure), getString(R.string.Do_you_really_wan_to_go_back),
                getString(R.string.no), getString(R.string.yes), new CustomDialogBuilder.OnDismissListener() {
                    @Override
                    public void onPositiveDismiss() {
                        //1.86
                        onBackPressed();
                    }

                    @Override
                    public void onNegativeDismiss() {

                    }
                }));
    }

    private void initObserve() {
        viewModel.parentPath = getPath().getPath();
        viewModel.onItemClick.observe(this, type -> {
            if (type != null) {
                if (type == 1) {
                    MusicFrameFragment frameFragment = new MusicFrameFragment();
                    frameFragment.show(getSupportFragmentManager(), frameFragment.getClass().getSimpleName());
                }
                viewModel.onItemClick.setValue(null);
            }
        });
//        viewModel.onSoundSelect.observe(this, sound -> {
//            if (sound != null) {
//                viewModel.soundId = sound.getSoundId();
//                binding.tvSoundTitle.setText(sound.getSoundTitle());
////                downLoadMusic(sound.getSound());
//            }
//        });
        viewModel.onDurationUpdate.observe(this, aLong -> binding.progressBar.enableAutoProgressView(aLong));
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
        AspectRatio ratio = AspectRatio.RATIO_16_9;
        viewModel.builder = new PreviewConfig.Builder();
        viewModel.previewConfig = viewModel.builder.setTargetAspectRatio(ratio)
                .setLensFacing(viewModel.lensFacing)
                .setTargetRotation(Surface.ROTATION_90)
                .build();
        viewModel.preview = AutoFitPreviewBuilder.Companion.build(viewModel.previewConfig, viewFinder);

        if (isVideo){
            //video
            viewModel.builder1 = new VideoCaptureConfig.Builder();
            viewModel.videoCaptureConfig = viewModel.builder1.setTargetAspectRatio(ratio)
                    .setLensFacing(viewModel.lensFacing)
                    .setVideoFrameRate(24)
                    .setTargetRotation(Surface.ROTATION_0)
                    .build();
            viewModel.videoCapture = new VideoCapture(viewModel.videoCaptureConfig);
            CameraX.bindToLifecycle(this, viewModel.preview, viewModel.videoCapture);
        }else {
            //photo
            viewModel.builder2 = new ImageCaptureConfig.Builder();
            viewModel.imageCaptureConfig = viewModel.builder2.setTargetAspectRatio(ratio)
                    .setLensFacing(viewModel.lensFacing)
                    .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                    .setTargetRotation(Surface.ROTATION_0)
                    .build();
            viewModel.imageCapture = new ImageCapture(viewModel.imageCaptureConfig);
            CameraX.bindToLifecycle(this, viewModel.preview, viewModel.imageCapture);

        }
    }


    @SuppressLint("RestrictedApi")
    private void stopRecording() {
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
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


        DrawableImageViewTarget target = new DrawableImageViewTarget( progressBinding.ivParent);
        Glide.with(this)
                .load(R.drawable.loader_video)
                .into(target);


        //  Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        //  Animation reverseAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_reverse);
        //   progressBinding.ivParent.startAnimation(rotateAnimation);
        // progressBinding.ivChild.startAnimation(reverseAnimation);
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

/*    private void downLoadMusic(String endPoint) {

        PRDownloader.download(Const.ITEM_BASE_URL + endPoint, getPath().getPath(), "recordSound.aac")
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
    }*/

    @SuppressLint("RestrictedApi")
    private void startReCording() {
        viewModel.isStartRecording.set(true);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        binding.btnCapture.startAnimation(animation);
        if (binding.progressBar.getProgressPercent() != 100) {
//            if (viewModel.audio != null) {
//                viewModel.audio.start();
//            }
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


    private void takePhoto() {

        String path = "/" + "Meest_" + System.currentTimeMillis() + ".jpg";
        File photoFile = new File(getPath(), path);

        viewModel.imageCapture.takePicture(photoFile, Runnable::run, new ImageCapture.OnImageSavedListener() {
            @Override
            public void onImageSaved(@NonNull File file) {
                Intent intent = new Intent(MeestCameraActivity.this, PreviewPhotoActivity.class);
                intent.putExtra("imagePath", photoFile.getPath());
                if (callType.equalsIgnoreCase("story")) {
                    intent.putExtra("isStory", true);
                }
                else
                    intent.putExtra("isStory", false);

                startActivity(intent);
                finish();

            }

            @Override
            public void onError(@NonNull ImageCapture.ImageCaptureError imageCaptureError, @NonNull String message, @Nullable Throwable cause) {

            }
        });

    }
    public void saveData(boolean isCompress) {

        if (isCompress) {
            DefaultVideoStrategy strategy = new DefaultVideoStrategy.Builder()
                    .addResizer(new FractionResizer(0.7F))
                    .addResizer(new AtMostResizer(1000))
                    .build();
            TranscoderOptions.Builder options = Transcoder.into(getPath().getPath().concat("/append.mp4"));
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
                                        Intent intent = new Intent(MeestCameraActivity.this, PreviewActivity.class);
                                        intent.putExtra("post_video", getPath().getPath().concat("/finally.mp4"));
                                        intent.putExtra("post_image", thumbFile.getPath());
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
                                intent.putExtra("videoPath", getPath().getPath().concat("/append.mp4"));
                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.aac"));
                                intent.putExtra("thumbPath", thumbFile.getPath());
                                if (callType.equalsIgnoreCase("story"))
                                {
                                    intent.putExtra("type", "story");
                                }
                                else
                                {
                                    intent.putExtra("type", "feed");
                                }

                                intent.putExtra("typeCamera",true);
                                startActivity(intent);
                                finish();

                              /*  Glide.with(MeestCameraActivity.this)
                                        .asBitmap()
                                        .load(Const.ITEM_BASE_URL + sessionManager.getUser().getData().getUserData().getDisplayPicture())
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
                                                Intent intent = new Intent(MeestCameraActivity.this, PreviewActivity.class);
                                                intent.putExtra("post_video", getPath().getPath().concat("/append.mp4"));
                                                intent.putExtra("post_image", thumbFile.getPath());
                                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.aac"));
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
                                                Intent intent = new Intent(MeestCameraActivity.this, PreviewActivity.class);
                                                intent.putExtra("post_video", getPath().getPath().concat("/append.mp4"));
                                                intent.putExtra("post_image", thumbFile.getPath());
                                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.aac"));

                                                startActivityForResult(intent, 101);
                                            }
                                        });*/

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
         /*       try {
                    Movie m1 = MovieCreator.build(viewModel.videoPaths.get(0));
                    audio = m1.getTracks().get(1);
                    Movie m2 = new Movie();
                    m2.addTrack(audio);
                    DefaultMp4Builder builder = new DefaultMp4Builder();
                    Container stdMp4 = builder.build(m2);
                    FileOutputStream fos = new FileOutputStream(getPath().getPath().concat("/originalSound.aac"));
                    stdMp4.writeContainer(fos.getChannel());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                File thumbFile = new File(getPath(), "temp.jpg");
                try {
                    FileOutputStream stream = new FileOutputStream(thumbFile);

                    Bitmap bmThumbnail;
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(viewModel.videoPaths.get(0),
                            MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                    if (bmThumbnail != null) {
                        bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    }
                    stream.flush();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }






                Intent intent = new Intent(MeestCameraActivity.this, PreviewVideoActivity.class);
                intent.putExtra("videoPath", viewModel.videoPaths.get(0));
                intent.putExtra("thumbPath", thumbFile.getPath());

                if (callType.equalsIgnoreCase("story"))
                {
                    intent.putExtra("type", "story");
                }
                else
                {
                    intent.putExtra("type", "feed");
                }

                intent.putExtra("typeCamera",true);
                //   intent.putExtra("sound_image", getPath().getPath().concat("/soundImage.jpeg"));
                startActivity(intent);
                finish();
             /*   Glide.with(MeestCameraActivity.this)
                        .asBitmap()
                        .load(Const.ITEM_BASE_URL + sessionManager.getUser().getData().getUserData())
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
                                Intent intent = new Intent(MeestCameraActivity.this, PreviewActivity.class);
                                intent.putExtra("post_video", viewModel.videoPaths.get(0));
                                intent.putExtra("post_image", thumbFile.getPath());
                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.aac"));
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
                                Intent intent = new Intent(MeestCameraActivity.this, PreviewActivity.class);
                                intent.putExtra("post_video", viewModel.videoPaths.get(0));
                                intent.putExtra("post_image", thumbFile.getPath());
                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.aac"));
                                //   intent.putExtra("sound_image", getPath().getPath().concat("/soundImage.jpeg"));
                                startActivityForResult(intent, 101);
                            }
                        });*/

            }).start();
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
        Log.e("TAG", "onResume: ");
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
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                ArrayList<PictureFacer> selectedItem =data.getParcelableArrayListExtra("result");
                // Log.d("", "onActivityResult: "+selectedItem.get(0));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        else
        {
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
            if (isVideo)
            {
                stopRecording();
                customDialogBuilder.showSimpleDialogMeest(getString(R.string.are_you_sure), getString(R.string.Do_you_really_wan_to_go_back),
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
            else
                super.onBackPressed();

        }
    }




}