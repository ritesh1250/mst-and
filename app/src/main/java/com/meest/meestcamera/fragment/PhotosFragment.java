package com.meest.meestcamera.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.meest.social.socialViewModel.view.NewPostUploadActivity;
import com.meest.Activities.VideoPost.PreviewVideoActivity;
import com.meest.Activities.preview.PreviewPhotoActivity;
import com.meest.R;
import com.meest.utils.goLiveUtils.utils.MeestLoaderDialog;
import com.meest.databinding.ActivityMeestCameraBinding;
import com.meest.databinding.DailogProgressBinding;
import com.meest.mediapikcer.GalleryFoldersActivity;
import com.meest.mediapikcer.utils.PictureFacer;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestcamera.MeestMainCameraActivity;
import com.meest.meestcamera.viewmodule.MeestCameraViewModel;
import com.meest.videomvvmmodule.utils.AutoFitPreviewBuilder;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.view.music.MusicFrameFragment;
import com.meest.videomvvmmodule.view.preview.PreviewActivity;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.lang.reflect.Type;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class PhotosFragment extends Fragment {
    private static final String TAG = "PhotosFragment";

    private static final String ARG_IS_VIDEO = "is_video";
    private static final String CALL_TYPE = "call_type";

    private static final int MY_PERMISSIONS_REQUEST = 101;
    int LAUNCH_SECOND_ACTIVITY = 1231;
    public MeestCameraViewModel viewModel;
    private ActivityMeestCameraBinding binding;
    private CustomDialogBuilder customDialogBuilder;
    private Dialog mBuilder;
    String callType = "";
    private DailogProgressBinding progressBinding;
    private MeestMainCameraActivity mActivity;
    private MeestLoaderDialog meestLoaderDialog;
    int rotation;


    private boolean switchVideo = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (MeestMainCameraActivity) context;
    }


    public static PhotosFragment newInstance(boolean switchToVideo, String callType) {
        Log.e(TAG, "switchToVideo 1: "+switchToVideo);
        Log.e(TAG, "callType 1: "+callType);
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_VIDEO, switchToVideo);
        args.putString(CALL_TYPE, callType);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            switchVideo = getArguments().getBoolean(ARG_IS_VIDEO);
            Log.e(TAG, "switchVideo 2: "+switchVideo);
            callType = getArguments().getString(CALL_TYPE);
            Log.e(TAG, "callType 2: "+callType);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_meest_camera, container, false);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new MeestCameraViewModel()).createFor()).get(MeestCameraViewModel.class);
        customDialogBuilder = new CustomDialogBuilder(mActivity);
        meestLoaderDialog = new MeestLoaderDialog(mActivity);

        if (callType == null) {
            callType = "feed";
        }

        initView(switchVideo);
        initListener();
        initObserve();
        initProgressDialog();
        binding.setViewModel(viewModel);

        if (switchVideo) {
            binding.btnCapture.setImageResource(R.drawable.ic_story_video_outline);
        }

        OrientationEventListener orientationEventListener = new OrientationEventListener(getContext()) {
            @SuppressLint("RestrictedApi")
            @Override
            public void onOrientationChanged(int orientation) {
                int rotation;

                // Monitors orientation values to determine the target rotation value
                if (orientation >= 45 && orientation < 135) {
                    rotation = Surface.ROTATION_270;
                } else if (orientation >= 135 && orientation < 225) {
                    rotation = Surface.ROTATION_180;
                } else if (orientation >= 225 && orientation < 315) {
                    rotation = Surface.ROTATION_90;
                } else {
                    rotation = Surface.ROTATION_0;
                }


                if (switchVideo) {
                    viewModel.videoCapture.setTargetRotation(rotation);
                } else {
                    viewModel.imageCapture.setTargetRotation(rotation);
                }
            }
        };


        orientationEventListener.enable();
        return binding.getRoot();
    }

    @SuppressLint("RestrictedApi")
    private void initView(boolean switchFor) {

        if (switchFor) {
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
                    Toast.makeText(mActivity, getString(R.string.Make_sure_video_is_longer_than_5s), Toast.LENGTH_LONG).show();
                }
            });
            binding.progressBar.setShader(new int[]{ContextCompat.getColor(mActivity, R.color.colorTheme), ContextCompat.getColor(mActivity, R.color.colorTheme), ContextCompat.getColor(mActivity, R.color.colorTheme)});


        } else {

            viewModel.isCapturing.set(true);
            binding.ivSelect.setVisibility(View.GONE);
            binding.card.setVisibility(View.GONE);
        }

    }


    private void initListener() {
        binding.btnCapture.setOnClickListener(v -> {
            if (switchVideo) {
                if (!viewModel.isRecording.get()) {
                    if (android.os.Build.VERSION.SDK_INT < 30) {
                        startReCording();
                    } else {
                        if (viewModel.count > 0) {
                            buildAlertMessageFor11();
                        } else {
                            startReCording();
                        }
                    }
                } else {
                    stopRecording();
                }
            } else {
                takePhoto();
            }
        });


        binding.btnFlip.setOnClickListener(v -> {
            viewModel.isFacingFront.set(!viewModel.isFacingFront.get());
            if (viewModel.isFlashOn.get()) {
                viewModel.onClickFlash();
            }
            if (viewModel.isFacingFront.get()) {
                viewModel.lensFacing = CameraX.LensFacing.FRONT;
            } else {
                viewModel.lensFacing = CameraX.LensFacing.BACK;
            }
            toggleCamera();
        });
        binding.tvSelect.setOnClickListener(v -> {

            Intent i = new Intent(mActivity, GalleryFoldersActivity.class);

            if (switchVideo) {
                i.putExtra("filter", "Video");
            } else {
                i.putExtra("filter", "Image");
            }

            i.putExtra("call_type", callType);
            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);

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
        viewModel.onItemClick.observe(getViewLifecycleOwner(), type -> {
            if (type != null) {
                if (type == 1) {
                    MusicFrameFragment frameFragment = new MusicFrameFragment();
                    frameFragment.show(getChildFragmentManager(), frameFragment.getClass().getSimpleName());
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
        viewModel.onDurationUpdate.observe(getViewLifecycleOwner(), aLong -> binding.progressBar.enableAutoProgressView(aLong));
    }


    private void recreateCamera() {
        binding.viewFinder.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                CameraX.unbindAll();
                rotation = (int) binding.viewFinder.getRotation();
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
        try {
            TextureView viewFinder = binding.viewFinder;
            AspectRatio ratio = AspectRatio.RATIO_4_3;
            viewModel.builder = new PreviewConfig.Builder();
            viewModel.previewConfig = viewModel.builder.setTargetAspectRatio(ratio)
                    .setLensFacing(viewModel.lensFacing)
                    .setTargetRotation(rotation)
                    //.set
                    .build();
            viewModel.preview = AutoFitPreviewBuilder.Companion.build(viewModel.previewConfig, viewFinder);

            if (switchVideo) {
                //video
                viewModel.builder1 = new VideoCaptureConfig.Builder();
                viewModel.videoCaptureConfig = viewModel.builder1.setTargetAspectRatio(ratio)
                        .setLensFacing(viewModel.lensFacing)
                        .setVideoFrameRate(24)
                        .setTargetRotation(rotation)
                        .build();
                viewModel.videoCapture = new VideoCapture(viewModel.videoCaptureConfig);
                CameraX.bindToLifecycle(this, viewModel.preview, viewModel.videoCapture);
            } else {
                //photo
                viewModel.builder2 = new ImageCaptureConfig.Builder();
                viewModel.imageCaptureConfig = viewModel.builder2.setTargetAspectRatio(ratio)
                        .setLensFacing(viewModel.lensFacing)
                        .setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                        .setTargetRotation(rotation)
                        .build();
                viewModel.imageCapture = new ImageCapture(viewModel.imageCaptureConfig);
                CameraX.bindToLifecycle(this, viewModel.preview, viewModel.imageCapture);

            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    @SuppressLint("RestrictedApi")
    private void stopRecording() {
        binding.btnCapture.clearAnimation();
        binding.btnCapture.setImageResource(R.drawable.ic_story_video_outline);
        if (viewModel.audio != null) {
            viewModel.audio.pause();
        }
        viewModel.count += 1;
        binding.progressBar.pause();
        binding.progressBar.addDivider();
        viewModel.isRecording.set(false);
        binding.ivSelect.setVisibility(View.VISIBLE);

        mActivity.hidegone(false);
        viewModel.videoCapture.stopRecording();
    }

    private void initPermission() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST);
        } else {
            recreateCamera();
        }
    }


    public void initProgressDialog() {
        mBuilder = new Dialog(mActivity);
        mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        if (mBuilder.getWindow() != null) {
            mBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        progressBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dailog_progress, null, false);
        progressBinding.progressBar.setShader(new int[]{ContextCompat.getColor(mActivity, R.color.colorTheme), ContextCompat.getColor(mActivity, R.color.colorTheme), ContextCompat.getColor(mActivity, R.color.colorTheme)});


        DrawableImageViewTarget target = new DrawableImageViewTarget(progressBinding.ivParent);
//        Glide.with(this)
//                .load(R.drawable.loader_gif)
//                .into(target);


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
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                toggleCamera();
            }
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    onBackPressed();
                }
            }
        }
    }

    @Override
    public void onStart() {
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
        binding.ivSelect.setVisibility(View.GONE);
        mActivity.hidegone(true);
        binding.btnCapture.setImageResource(R.drawable.ic_recording_pause);
        Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.scale);
        binding.btnCapture.startAnimation(animation);
//        viewModel.videoPaths.clear();
        if (binding.progressBar.getProgressPercent() != 100) {
//            if (viewModel.audio != null) {
//                viewModel.audio.start();
//            }

            File file = new File(getPath(), "video".concat(String.valueOf(viewModel.count)).concat(".mp4"));
            viewModel.videoPaths.add(getPath().getPath().concat("/video").concat(String.valueOf(viewModel.count)).concat(".mp4"));

//            String recordvideo = Constant.createOutputPath(mActivity, ".mp4");
//            File file = new File(recordvideo);
//            viewModel.videoPaths.add(recordvideo);
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
                Log.e(TAG, "onImageSaved: "+photoFile.getPath() );
                Intent intent = new Intent(mActivity, PreviewPhotoActivity.class);
                intent.putExtra("imagePath", photoFile.getPath());
                if (callType.equalsIgnoreCase("story")) {
                    intent.putExtra("isStory", true);
                    startActivity(intent);
                    mActivity.finish();
                } else {
                    intent.putExtra("isStory", false);
                    startActivity(intent);
                    mActivity.finish();
                }
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
            String output = Constant.createOutputPath(mActivity, ".mp4");
            TranscoderOptions.Builder options = Transcoder.into(output);
            for (int i = 0; i < viewModel.videoPaths.size(); i++) {
                options.addDataSource(viewModel.videoPaths.get(i));
            }
            options.setVideoTrackStrategy(strategy);
            options.setListener(new TranscoderListener() {
                public void onTranscodeProgress(double progress) {
                    showProgressDialog();
                    if (progressBinding != null) {
                        if (viewModel.audio != null) {

                            progressBinding.progressBar.publishProgress((float) progress / 2);
                        } else {
                            if (viewModel.isFlashOn.get()) {
                                viewModel.onClickFlash();
                            }
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

                                        Intent intent = new Intent(mActivity, PreviewActivity.class);
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
                                String thumbnail = Constant.createOutputPath(mActivity, ".JPEG");
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
                                Intent intent = new Intent(mActivity, PreviewVideoActivity.class);
                                intent.putExtra("videoPath", output);
                                intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.aac"));
                                intent.putExtra("thumbPath", thumbFile.getPath());
                                if (callType.equalsIgnoreCase("story")) {
                                    intent.putExtra("type", "story");
                                } else {
                                    intent.putExtra("type", "feed");
                                }

                                intent.putExtra("typeCamera", true);
                                startActivity(intent);
                                mActivity.finish();

                              /*  Glide.with(mActivity)
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
                                                Intent intent = new Intent(mActivity, PreviewActivity.class);
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
                                                Intent intent = new Intent(mActivity, PreviewActivity.class);
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
                String thumbnail = Constant.createOutputPath(mActivity, ".JPEG");
                File thumbFile = new File(thumbnail);
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
                Intent intent = new Intent(mActivity, PreviewVideoActivity.class);
                intent.putExtra("videoPath", viewModel.videoPaths.get(0));
                intent.putExtra("thumbPath", thumbFile.getPath());

                if (callType.equalsIgnoreCase("story")) {
                    intent.putExtra("type", "story");
                } else {
                    intent.putExtra("type", "feed");
                }

                intent.putExtra("typeCamera", true);
                //   intent.putExtra("sound_image", getPath().getPath().concat("/soundImage.jpeg"));
                startActivity(intent);
                mActivity.finish();
             /*   Glide.with(mActivity)
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
                                Intent intent = new Intent(mActivity, PreviewActivity.class);
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
                                Intent intent = new Intent(mActivity, PreviewActivity.class);
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
            filesDir = mActivity.getExternalFilesDir(null);
        } else {
            // Load another directory, probably local memory
            filesDir = mActivity.getFilesDir();
        }
        if (filesDir != null) {
            viewModel.parentPath = filesDir.getPath();
        }
        return filesDir;
    }

    @Override
    public void onDestroy() {
        if (viewModel.isRecording.get()) {
            stopRecording();
        }
        CameraX.unbindAll();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "onResume: ");
        //  initPermission();

        if (viewModel.preview != null)
            CameraX.unbind(viewModel.preview);
        startCamera();

    }

    @Override
    public void onPause() {
        if (viewModel.isRecording.get()) {
            stopRecording();
        }
        if (viewModel.isFlashOn.get()) {
            Log.e("TAG", "FlashON: 1 ");
            viewModel.onClickFlash();
        }
        CameraX.unbindAll();
        super.onPause();
    }

    @Override
    public void onStop() {
        if (viewModel.isRecording.get()) {
            stopRecording();
        }
        if (viewModel.isFlashOn.get()) {
            Log.e("TAG", "FlashON: 2");
            viewModel.onClickFlash();
        }
        CameraX.unbindAll();
        super.onStop();
    }


    List<PictureFacer> selectedItem;
    int c = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        String typemedia = "";
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                Gson gson = new Gson();
                Type type = new TypeToken<List<PictureFacer>>() {
                }.getType();
                selectedItem = gson.fromJson(result, type);

                if (callType.equalsIgnoreCase("feed")) {

                    if (selectedItem != null && selectedItem.size() > 0) {
                        for (PictureFacer pictureFacer : selectedItem) {
                            Log.e("onActivityResult", "onActivityResult: " + pictureFacer.getPicturePath());
                            if (pictureFacer.getType().equalsIgnoreCase("Image")) {
                                if (selectedItem.size() == 1) {
                                    Intent intent = new Intent(mActivity, PreviewPhotoActivity.class);
                                    intent.putExtra("imagePath", selectedItem.get(0).getPicturePath());
                                    intent.putExtra("isStory", false);
                                    startActivity(intent);
                                    mActivity.finish();
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
                                            String mediaPath = Constant.compressImage(mActivity, pictureFacer.getPicturePath());
                                            pictureFacer.setPicturePath(mediaPath);
                                        }
                                    } catch (Exception e) {
                                    }
                                    Log.e("for", "ccount : " + c);

                                    if (selectedItem.size() == c) {
                                        Log.e("for", "UploadFilesToServer : " + selectedItem.size());
                                        meestLoaderDialog.hideDialog();
                                        Intent it = new Intent(mActivity, NewPostUploadActivity.class);
                                        it.putExtra("multipleMedia", new Gson().toJson(selectedItem));
                                        it.putExtra("mediaPath", selectedItem.get(0).getPicturePath());
                                        it.putExtra("isVideo", false);
                                        it.putExtra("isMultiple", true);
                                        it.putExtra("recode", requestCode);
                                        startActivity(it);
                                        mActivity.finish();


                                    }
                                }
                            } else {

//                                File chkFileSize = new File(pictureFacer.getPictureSize());
                                try {
                                    long fileSizeInBytes = Long.parseLong(pictureFacer.getPictureSize());
                                    long fileSizeInKB = fileSizeInBytes / 1024;
                                    long fileSizeInMB = fileSizeInKB / 1024;

                                    if (selectedItem.size() == 1) {
                                        viewModel.videoPaths.clear();
                                        viewModel.videoPaths.add(pictureFacer.getPicturePath());
                                        if (fileSizeInMB > 5) {
                                            saveData(true);
                                        } else {
                                            saveData(false);
                                        }
                                    } else {
                                        String output = Constant.createOutputPath(mActivity, ".mp4");
                                        String thumbnail = Constant.createOutputPath(mActivity, ".jpg");
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
                        viewModel.videoPaths.clear();
                        viewModel.videoPaths.add(selectedItem.get(0).getPicturePath());
                        saveData(true);
                    } else if (selectedItem != null && selectedItem.size() == 1) {
                        Intent intent = new Intent(mActivity, PreviewPhotoActivity.class);
                        intent.putExtra("imagePath", selectedItem.get(0).getPicturePath());
                        intent.putExtra("isStory", true);
                        startActivity(intent);
                        mActivity.finish();

                    }

                }


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else {
            viewModel.isBack = true;
            onBackPressed();
        }


    }

    private void onBackPerform(boolean isBack) {

    }

    public void onBackPressed() {
        if (viewModel.isBack) {
            mActivity.setResult(RESULT_OK);
            CameraX.unbindAll();
            mActivity.onBackPressed();
        } else {
            if (switchVideo) {
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
            } else
                mActivity.onBackPressed();

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
                    if (callType.equalsIgnoreCase("feed")) {
                        if (successCode == 0 && lsine == c && lsine > 1) {
                            binding.progressBar.cancel();
                            hideProgressDialog();
//                            meestLoaderDialog.showDialog();
//                            meestLoaderDialog.setTexMsg("Uploading Video");
                            Log.e("transcode", "UploadFilesToServer: ");
                            Intent it = new Intent(mActivity, NewPostUploadActivity.class);
                            it.putExtra("multipleMedia", new Gson().toJson(selectedItem));
                            it.putExtra("mediaPath", selectedItem.get(0).getPicturePath());
                            it.putExtra("isVideo", true);
                            it.putExtra("isMultiple", true);
                            it.putExtra("thumbPath", thumbFile.getPath());
                            it.putExtra("recode", requestCode);
                            startActivity(it);
                            mActivity.finish();
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


    private void buildAlertMessageFor11() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Please make single track video")
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}