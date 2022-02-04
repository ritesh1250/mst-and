package com.meest.medley_camera2.camera2.view.activity;

import static android.os.Build.VERSION.SDK_INT;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.googlecode.mp4parser.authoring.Track;
import com.meest.MainActivity;
import com.meest.R;
import com.meest.Services.StoryPostuploadService;
import com.meest.databinding.ActivityPreviewVideoMedleyBinding;
import com.meest.databinding.DailogProgressBinding;
import com.meest.databinding.ItemUploadSheetMedlyBinding;
import com.meest.medley_camera2.camera2.utills.CameraUtil;
import com.meest.medley_camera2.camera2.viewmodels.PreviewVideoViewModel;
import com.meest.social.socialViewModel.view.NewPostUploadActivity;
import com.meest.videoEditing.fastmotionvideo.FastMotionVideoActivity;
import com.meest.videoEditing.slowmotionvideo.SlowMotionVideoActivity;
import com.meest.videoEditing.videocrop.VideoCropActivity;
import com.meest.videoEditing.videocutter.VideoCutter;
import com.meest.videoEditing.videomirror.VideoMirrorActivity;
import com.meest.videoEditing.videomute.VideoMuteActivity;
import com.meest.videoEditing.videoreverse.VideoReverseActivity;
import com.meest.videoEditing.videorotate.VideoRotateActivity;
import com.meest.videomvvmmodule.AudioExtractor;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.preview.VideoUploadService;
import com.meest.videomvvmmodule.view.web.WebViewActivity;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.TranscoderOptions;
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategy;
import com.otaliastudios.transcoder.strategy.size.FractionResizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PreviewVideoActivity extends BaseActivity {
    private static final String TAG = "PreviewActivity2";
    private static final int PERMISSION_REQUEST_CODE = 2296;
    public boolean isVideoEditor = false;
    String format;
    private Dialog mBuilder;
    private DailogProgressBinding progressBinding;
    private ActivityPreviewVideoMedleyBinding binding;
    private PreviewVideoViewModel viewModel;
    private CustomDialogBuilder customDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview_video_medley);

        if (CameraUtil.comeFrom.equalsIgnoreCase("Meest")) {
            binding.editTab.setVisibility(View.GONE);
        }
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new PreviewVideoViewModel(this, binding))).get(PreviewVideoViewModel.class);

        binding.setViewModel(viewModel);
        customDialogBuilder = new CustomDialogBuilder(this);
        viewModel.sessionManager = sessionManager;
        viewModel.requestNewLocationData();
        viewModel.initProgressDialog();
        viewModel.initData();
        viewModel.getData();
        buttonClicks();
        binding.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.videoView.isPlaying()) {
                    binding.videoView.pause();
                    binding.btnPlayVideo.setBackgroundResource(R.drawable.ic_play);
                } else {
                    binding.videoView.start();
                    binding.btnPlayVideo.setBackgroundResource(0);
                }
            }
        });
//        initProgressDialog();
//        playVideo();
    }

    private void playVideo() {
        // player = new SimpleExoPlayer.Builder(this).build();

//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
//                Util.getUserAgent(this, "Medley"));

//        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(Uri.parse(viewModel.videoPath));

//        player.prepare(videoSource);
//        player.setRepeatMode(Player.REPEAT_MODE_ALL);
//        player.setPlaybackParameters(PlaybackParameters.DEFAULT);
//        player.setPlaybackParameters(PlaybackParameters.DEFAULT);
        binding.videoView.setVideoPath(viewModel.videoPath);

        binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        binding.videoView.start();
        binding.btnPlayVideo.setBackgroundResource(0);

        binding.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.videoView.isPlaying()) {
                    binding.videoView.pause();
                    binding.btnPlayVideo.setBackgroundResource(R.drawable.ic_play);
                } else {
                    binding.videoView.start();
                    binding.btnPlayVideo.setBackgroundResource(0);
                }
            }
        });

        binding.btnPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.videoView.isPlaying()) {
                    binding.videoView.pause();
                    binding.btnPlayVideo.setBackgroundResource(R.drawable.ic_play);
                } else {
                    binding.videoView.start();
                    binding.btnPlayVideo.setBackgroundResource(0);
                }
            }
        });
        // binding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
//        player.setPlayWhenReady(true);
    }

    private void buttonClicks() {

        binding.downloadMedia.setOnClickListener(v -> {

            if (SDK_INT >= Build.VERSION_CODES.Q) {
                viewModel.addToApi29Gallery();
            } else {
                viewModel.addVideo();
            }

        });


        binding.closeBtn.setOnClickListener(v -> {
            onBackPressed();

        });


        binding.videoCutter.setOnClickListener(v -> {

            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, VideoCutter.class);
            intent.putExtra("videoPath", viewModel.videoURL);
            startActivityForResult(intent, 101);

        });

        binding.fastMotion.setOnClickListener(v -> {
            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, FastMotionVideoActivity.class);
            intent.putExtra("videoPath", viewModel.videoURL);

            startActivityForResult(intent, 101);
        });

        binding.slowMotion.setOnClickListener(v -> {
            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, SlowMotionVideoActivity.class);
            intent.putExtra("videoPath", viewModel.videoURL);
            startActivityForResult(intent, 101);
        });

        binding.videoCrop.setOnClickListener(v -> {
            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, VideoCropActivity.class);
            intent.putExtra("videoPath", viewModel.videoURL);

            startActivityForResult(intent, 101);
        });

        binding.videoRotate.setOnClickListener(v -> {
            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, VideoRotateActivity.class);
            intent.putExtra("videoPath", viewModel.videoURL);

            startActivityForResult(intent, 101);
        });

        binding.videoMirror.setOnClickListener(v -> {
            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, VideoMirrorActivity.class);
            intent.putExtra("videoPath", viewModel.videoURL);
            startActivityForResult(intent, 101);
        });

        binding.videoReverse.setOnClickListener(v -> {
            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, VideoReverseActivity.class);
            intent.putExtra("videoPath", viewModel.videoURL);
            startActivityForResult(intent, 101);
        });

        binding.videoMute.setOnClickListener(v -> {
            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, VideoMuteActivity.class);
            intent.putExtra("videoPath", viewModel.videoURL);
            startActivityForResult(intent, 101);
        });

        binding.addMusic.setOnClickListener(v -> {
            Intent intent = new Intent(PreviewVideoActivity.this, RecordAudioActivity.class);
            intent.putExtra("videoPath", viewModel.videoURL);
            startActivityForResult(intent, 101);
        });

        binding.ivSelect.setOnClickListener(v -> {
//            viewModel.showProgressDialog();
            customDialogBuilder.showLoadingDialog();
            binding.videoView.pause();

            binding.btnPlayVideo.setBackgroundResource(R.drawable.ic_play);
//            onPause();
            if (CameraUtil.comeFrom.equalsIgnoreCase("Meest")) {
                uploadVideo();
            } else {
                File file = new File(viewModel.videoURL);
                // Get length of file in bytes
                long fileSizeInBytes = file.length();
                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB = fileSizeInBytes / 1024;
                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                long fileSizeInMB = fileSizeInKB / 1024;
                if (fileSizeInMB > 5) {
                    saveData(viewModel.videoURL, true);
                    Log.e("TAG", "File_size_after_edit: " + fileSizeInMB);
                } else {
                    saveData(viewModel.videoURL, false);
                    Log.e("TAG", "File_size_after_edit: " + fileSizeInMB);
                }
            }

//            if (isVideoEditor) {
//                viewModel.videoPaths.add(videoURL);
//            File file = new File(viewModel.videoURL);
//            // Get length of file in bytes
//            long fileSizeInBytes = file.length();
//            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
//            long fileSizeInKB = fileSizeInBytes / 1024;
//            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
//            long fileSizeInMB = fileSizeInKB / 1024;
//            if (fileSizeInMB > 5) {
//                saveData(viewModel.videoURL, true);
//                Log.e("TAG", "File_size_after_edit: " + fileSizeInMB);
//            } else {
//                saveData(viewModel.videoURL, false);
//                Log.e("TAG", "File_size_after_edit: " + fileSizeInMB);
//            }
//            } else {
//                openPostBottomLayout();
//            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!binding.videoView.isPlaying()) {
            binding.videoView.start();
            binding.btnPlayVideo.setBackgroundResource(0);
        }
    }

    @Override
    public void onBackPressed() {
        if (!CameraUtil.comeFrom.equalsIgnoreCase("Meest")) {
            final Dialog dialog = new Dialog(PreviewVideoActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.post_edit_video_are_you_sure);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView tvYes = dialog.findViewById(R.id.tvYes);
            TextView tvNo = dialog.findViewById(R.id.tvNo);
            dialog.show();
            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent;
                    if (CameraUtil.comeFrom.equalsIgnoreCase("Meest")) {
                        intent = new Intent(PreviewVideoActivity.this, MeestCameraActivity.class);
                    } else {
                        intent = new Intent(PreviewVideoActivity.this, CameraActivity.class);
                    }
                    startActivity(intent);

                    finish();
                }
            });
            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            Intent intent;
            intent = new Intent(this, MeestCameraActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //Android 11 Permission Code:-

    @Override
    protected void onPause() {
        super.onPause();
        if (binding.videoView.isPlaying()) {
            binding.videoView.pause();
            binding.btnPlayVideo.setBackgroundResource(R.drawable.ic_play);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 999) {
                viewModel.videoURL = data.getStringExtra("videoPath");
                Log.e("line 321", "321:  " + viewModel.videoURL);
                viewModel.playVideo(viewModel.videoURL);
            }

            if (requestCode == 101 && data != null) {
                viewModel.videoURL = data.getStringExtra("song");
                Log.e(TAG, "videoURL: " + viewModel.videoURL);
                viewModel.playVideo(viewModel.videoURL);
            }
        } else {
            viewModel.playVideo(viewModel.videoURL);
        }

//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (SDK_INT >= Build.VERSION_CODES.R) {
//                if (Environment.isExternalStorageManager()) {
//                    Log.e(TAG, "onActivityResult: ");
//                    convertImageToBitmap();
//                    // perform action when allow permission success
//                } else {
//                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean WRITE_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (WRITE_EXTERNAL_STORAGE) {
                    Log.e(TAG, "onRequestPermissionsResult : ");

                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void saveData(String videoURL, boolean isCompress) {
        Log.e("filePath===========", videoURL);
        format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        if (isCompress) {

            DefaultVideoStrategy strategy;

            strategy = new DefaultVideoStrategy.Builder()
                    .addResizer(new FractionResizer(0.5f))
                    .build();

            TranscoderOptions.Builder options = Transcoder.into(viewModel.getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"));
//            for (int i = 0; i < viewModel.videoPaths.size(); i++) {
//                options.addDataSource(viewModel.videoPaths.get(i));
            options.addDataSource(videoURL);
//            }
            options.setListener(new TranscoderListener() {
                public void onTranscodeProgress(double progress) {
//                    showProgressDialog();
                    if (progressBinding != null) {
                        progressBinding.preparing.setVisibility(View.GONE);
                        progressBinding.progressBar.publishProgress((float) progress);
                        Log.e("=========Progress_1", String.valueOf(progressBinding.progressBar.getProgressPercent()));
                    }
                    Log.d("TAG", "onTranscodeProgress: " + progress);
                }

                public void onTranscodeCompleted(int successCode) {
                    Log.d("TAG", "onTranscodeCompleted: " + successCode);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Track audio;
                            try {
//                                Movie m1 = MovieCreator.build(viewModel.getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"));
//                                audio = m1.getTracks().get(1);
//                                Movie m2 = new Movie();
//                                m2.addTrack(audio);
//                                DefaultMp4Builder builder = new DefaultMp4Builder();
//                                Container stdMp4 = builder.build(m2);
//                                FileOutputStream fos = new FileOutputStream(viewModel.getPath().getPath().concat("/originalSound.mp3"));
//                                stdMp4.writeContainer(fos.getChannel());
//                                fos.close();

                                File temp = new File(viewModel.getPath().getPath().concat("/originalSound.mp3"));

                                if (!temp.exists()) {
                                    temp.createNewFile();
                                }

                                new AudioExtractor().genVideoUsingMuxer(viewModel.getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"), temp.getAbsolutePath(), -1, -1, true, false);

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            File thumbFile = new File(viewModel.getPath(), "/" + "user_thumbnail_" + sessionManager.getUser().getData().getUserName() + format + ".jpg");
                            try {
                                FileOutputStream stream = new FileOutputStream(thumbFile);

                                Bitmap bmThumbnail;
                                bmThumbnail = ThumbnailUtils.createVideoThumbnail(viewModel.getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"),
                                        MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                                if (bmThumbnail != null) {
                                    bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                }
                                stream.flush();
                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Glide.with(PreviewVideoActivity.this)
                                    .asBitmap()
                                    .load(sessionManager.getUser().getData().getUserProfile())
//                                        .load(Const.ITEM_BASE_URL + sessionManager.getUser().getData().getUserProfile())
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            File soundImage = new File(viewModel.getPath(), "soundImage.jpeg");
                                            try {
                                                FileOutputStream stream = new FileOutputStream(soundImage);
                                                resource.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                                stream.flush();
                                                stream.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
//                                            hideProgressDialog();
                                            viewModel.videoURL = viewModel.getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4");
                                            viewModel.videoThumbnail = thumbFile.getPath();
                                            viewModel.soundPath = viewModel.getPath().getPath().concat("/originalSound.mp3");
                                            viewModel.soundImage = viewModel.getPath().getPath().concat("/soundImage.jpeg");
                                            openPostBottomLayout();
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                        }

                                        @Override
                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                            super.onLoadFailed(errorDrawable);

//                                            hideProgressDialog();
                                            viewModel.videoURL = viewModel.getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4");
                                            viewModel.videoThumbnail = thumbFile.getPath();
                                            viewModel.soundPath = viewModel.getPath().getPath().concat("/originalSound.mp3");
                                            openPostBottomLayout();
                                        }
                                    });

                        }
                    }).start();

                }

                public void onTranscodeCanceled() {
                    Log.d("TAG", "onTranscodeCanceled: ");
                }

                public void onTranscodeFailed(@NonNull Throwable exception) {
                    Log.d("TAG", "onTranscodeCanceled: " + exception);
                }
            }).transcode();

        } else {
//            showProgressDialog();
            new Thread(() -> {
                Track audio;
                try {
//                    Movie m1 = MovieCreator.build(videoURL);
//                    audio = m1.getTracks().get(0);
//                    Movie m2 = new Movie();
//                    m2.addTrack(audio);
//                    DefaultMp4Builder builder = new DefaultMp4Builder();
//                    Container stdMp4 = builder.build(m2);
//                    FileOutputStream fos = new FileOutputStream(viewModel.getPath().getPath().concat("/originalSound.mp3"));
//                    stdMp4.writeContainer(fos.getChannel());
//                    fos.close();

                    File temp = new File(viewModel.getPath().getPath().concat("/originalSound.mp3"));

                    if (!temp.exists()) {
                        temp.createNewFile();
                    }

                    new AudioExtractor().genVideoUsingMuxer(videoURL, temp.getAbsolutePath(), -1, -1, true, false);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                File thumbFile = new File(viewModel.getPath(), "/" + "user_thumbnail_" + sessionManager.getUser().getData().getUserName() + format + ".jpg");

                try {

                    FileOutputStream stream = new FileOutputStream(thumbFile);

                    Bitmap bmThumbnail;
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(videoURL,
                            MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                    if (bmThumbnail != null) {
                        bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                    }

                    stream.flush();
                    stream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(PreviewVideoActivity.this)
                        .asBitmap()
                        .load(sessionManager.getUser().getData().getUserProfile())
//                        .load(Const.ITEM_BASE_URL + sessionManager.getUser().getData().getUserProfile())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                File soundImage = new File(viewModel.getPath(), "soundImage.jpeg");
                                try {
                                    FileOutputStream stream = new FileOutputStream(soundImage);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                    stream.flush();
                                    stream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
//                                customDialogBuilder.showLoadingDialog();
                                viewModel.videoURL = videoURL;
                                viewModel.videoThumbnail = thumbFile.getPath();
                                viewModel.soundPath = viewModel.getPath().getPath().concat("/originalSound.mp3");
                                viewModel.soundImage = viewModel.getPath().getPath().concat("/soundImage.jpeg");
                                PreviewVideoActivity.this.runOnUiThread(() -> {
//                                    customDialogBuilder.showLoadingDialog();
                                    openPostBottomLayout();
                                    // This is where your UI code goes.
                                });
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                customDialogBuilder.showLoadingDialog();
                                viewModel.videoURL = videoURL;
                                viewModel.videoThumbnail = thumbFile.getPath();
                                viewModel.soundPath = viewModel.getPath().getPath().concat("/originalSound.mp3");
                                PreviewVideoActivity.this.runOnUiThread(() -> {
//                                    customDialogBuilder.showLoadingDialog();
                                    openPostBottomLayout();
                                    // This is where your UI code goes.
                                });
                            }

                        });
            }).start();
        }
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

        mBuilder.setContentView(progressBinding.getRoot());
    }

    private void openPostBottomLayout() {
//        viewModel.hideProgressDialog();
        customDialogBuilder.hideLoadingDialog();
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        ItemUploadSheetMedlyBinding uploadSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.item_upload_sheet_medly, null, false);
        dialog.setContentView(uploadSheetBinding.getRoot());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setDismissWithAnimation(true);

        uploadSheetBinding.setViewModel(viewModel);
//          uploadSheetBinding.edtDes.setHashtagColor(Color.RED);
        uploadSheetBinding.switchComment.setOnClickListener(view -> {
            if (uploadSheetBinding.switchComment.isChecked()) {
                viewModel.canComment = "0";
                Log.d("CAN_COMMENT", "initListener: " + viewModel.canComment);
            } else {
                viewModel.canComment = "1";
                Log.d("CAN_COMMENT", "initListener: " + viewModel.canComment);
            }
        });
        uploadSheetBinding.switchSave.setOnClickListener(view -> {
            if (uploadSheetBinding.switchSave.isChecked()) {
                viewModel.canSave = 1;
            } else {
                viewModel.canSave = 0;
            }
            Log.d("CAN_SAVE", "initListener: " + viewModel.canSave);
        });
        uploadSheetBinding.switchDuet.setOnClickListener(view -> {
            if (uploadSheetBinding.switchDuet.isChecked()) {
                viewModel.canDuet = 1;
            } else {
                viewModel.canDuet = 0;
            }
            Log.d("CAN_DUET", "initListener: " + viewModel.canDuet);
        });

        uploadSheetBinding.btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!uploadSheetBinding.edtDes.getHashtags().isEmpty()) {
                    viewModel.hashTag = TextUtils.join(",", uploadSheetBinding.edtDes.getHashtags());
                }
                viewModel.videoURL = viewModel.videoURL;
                Intent intent = new Intent(PreviewVideoActivity.this, VideoUploadService.class);
                intent.putExtra("videoPath", viewModel.videoURL);
                intent.putExtra("postDescription", viewModel.postDescription);
                intent.putExtra("hashTag", viewModel.hashTag);
                intent.putExtra("canComment", viewModel.canComment);
                intent.putExtra("canSave", viewModel.canSave);
                intent.putExtra("canDuet", viewModel.canDuet);
                intent.putExtra("soundPath", viewModel.soundPath);
                intent.putExtra("soundId", viewModel.soundId);
                intent.putExtra("thumbnail_path", viewModel.videoThumbnail);
                intent.putExtra("lat", viewModel.post_lat);
                intent.putExtra("lng", viewModel.post_long);
                intent.putExtra("sound_title", viewModel.sessionManager.getUser().getData().getUserName());
                startService(intent);
//                    deleteRecursive(getPath());
//                    customDialogBuilder.hideLoadingDialog();
//                hideProgressDialog();
                Intent intent1 = new Intent();
                intent1.putExtra("is_preview", true);
                setResult(RESULT_OK, intent1);
//                    deleteFfmpegFile();
//                    Toast.makeText(this, "Video Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                    onBackPressed();
                finish();
                dialog.dismiss();
            }
        });

           /* viewModel.onClickUpload.observe(this, s -> {
                if (!uploadSheetBinding.edtDes.getHashtags().isEmpty()) {
                    viewModel.hashTag = TextUtils.join(",", uploadSheetBinding.edtDes.getHashtags());
                }

            });*/

       /* dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!binding.videoView.isPlaying()) {
                    binding.videoView.start();
                    binding.btnPlayVideo.setBackgroundResource(0);
                }
            }
        });*/

        uploadSheetBinding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        uploadSheetBinding.tvPrivacy.setOnClickListener(v1 -> startActivity(new
                Intent(this, WebViewActivity.class).putExtra("type", 1)));
        uploadSheetBinding.edtDes.setOnHashtagClickListener((view, text) -> {

        });

        FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }

    }

    private void uploadVideo() {

        Log.e(TAG, "videoPath: " + viewModel.videoURL);
        Log.e(TAG, "thumbPath: " + viewModel.thumbPath);
        Log.e(TAG, "CALL_TYPE: " + MeestCameraActivity.CALL_TYPE);
        Intent intent = null;
        if (MeestCameraActivity.CALL_TYPE.equalsIgnoreCase(MeestCameraActivity.CALL_STORY)) {


            Intent intent1 = new Intent(PreviewVideoActivity.this, StoryPostuploadService.class);
            intent1.putExtra("storyMediaPath", viewModel.videoURL);
            intent1.putExtra("storyThumbPath", viewModel.thumbPath);
            intent1.putExtra("isStoryVideo", true);
            startService(intent1);
            intent = new Intent(PreviewVideoActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        } else if (MeestCameraActivity.CALL_TYPE.equalsIgnoreCase(MeestCameraActivity.CALL_FEED)) {
            intent = new Intent(this, NewPostUploadActivity.class);
            intent.putExtra("mediaPath", viewModel.videoURL);
            intent.putExtra("thumbPath", viewModel.thumbPath);
            intent.putExtra("isVideo", true);
        }
        startActivity(intent);
        finish();
    }
}

