package com.meest.videomvvmmodule.view.preview;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.coremedia.iso.boxes.Container;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.meest.R;
import com.meest.databinding.ActivityPreviewBinding;
import com.meest.databinding.DailogProgressBinding;
import com.meest.databinding.ItemUploadSheetBinding;
import com.meest.videoEditing.UtilCommand;
import com.meest.videoEditing.audiovideomixer.AudioVideoMixer;
import com.meest.videoEditing.fastmotionvideo.FastMotionVideoActivity;
import com.meest.videoEditing.slowmotionvideo.SlowMotionVideoActivity;
import com.meest.videoEditing.videocrop.VideoCropActivity;
import com.meest.videoEditing.videocutter.VideoCutter;
import com.meest.videoEditing.videojoiner.VideoJoinerActivity;
import com.meest.videoEditing.videomirror.VideoMirrorActivity;
import com.meest.videoEditing.videomute.VideoMuteActivity;
import com.meest.videoEditing.videoreverse.VideoReverseActivity;
import com.meest.videoEditing.videorotate.VideoRotateActivity;
import com.meest.videomvvmmodule.AudioExtractor;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.web.WebViewActivity;
import com.meest.videomvvmmodule.viewmodel.PreviewViewModel;
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
import java.util.Objects;

public class PreviewActivity extends BaseActivity {

    private static final String TAG = "PreviewActivity";

    private ActivityPreviewBinding binding;
    private PreviewViewModel viewModel;
    private CustomDialogBuilder customDialogBuilder;
    //   private SimpleExoPlayer player;

    private Dialog mBuilder;
    private DailogProgressBinding progressBinding;
    FusedLocationProviderClient fusedLocationProviderClient;
    private Double post_lat = 0.0d;
    private Double post_long = 0.0d;

    public String y, videoPath;

    ImageView btnPlayVideo;
    int width, height;
    public boolean isVideoEditor = false;
    String format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview);

        Log.e(TAG, "PreviewActivity Called: ");

        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new PreviewViewModel()).createFor()).get(PreviewViewModel.class);
        customDialogBuilder = new CustomDialogBuilder(this);
        viewModel.sessionManager = sessionManager;
        if (getIntent().getExtras() != null) {
            viewModel.videoPath = getIntent().getStringExtra("post_video");
            viewModel.videoThumbnail = getIntent().getStringExtra("post_image");
            viewModel.soundPath = getIntent().getStringExtra("post_sound");
            viewModel.soundImage = getIntent().getStringExtra("sound_image");
            viewModel.soundId = getIntent().getStringExtra("soundId");
        }
        format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(this, Uri.parse(viewModel.videoPath));
        width = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        height = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        Log.e("======Preview", "width:" + width);
        Log.e("======Preview", "height:" + height);
        Log.e("sound_path", "===============" + viewModel.soundPath);
        Log.e("sound_Image", "===============" + viewModel.soundImage);
        Log.e("sound_Id", "===============" + viewModel.soundId);
        Log.e("thumbnail", "===============" + viewModel.videoThumbnail);
        Log.e("video+path", getIntent().getStringExtra("post_video"));
        initView();
        playVideo();
        initObserve();
        initListener();
        initProgressDialog();

//        if (android.os.Build.VERSION.SDK_INT < 30) {
//            binding.editTab.setVisibility(View.VISIBLE);
//        }
//        new SessionManager(this).getUser().getData().getTotalViewCount();
//        viewModel.setIconColor(binding, this);
    }

    private void initView() {
//        viewModel.getThumbnailUrl();
        requestNewLocationData();
        btnPlayVideo = findViewById(R.id.btnPlayVideo);
        binding.editVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (android.os.Build.VERSION.SDK_INT < 30) {
//                    binding.editTab.setVisibility(binding.editTab.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
//                } else {
//                    Utilss.showToast(PreviewActivity.this, getString(R.string.comming_soon), R.color.red);
//                }

//                Intent intent=     new Intent(PreviewActivity.this, StoryComingSoonActivity.class);
//                intent.putExtra("title","Video Editor");
//                startActivity(intent);
            }
        });

        binding.videoCutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewModel.setIconColor(binding, PreviewActivity.this);
//                binding.videoCutter.setColorFilter(ContextCompat.getColor(PreviewActivity.this, R.color.red));
                isVideoEditor = true;
                Intent intent = new Intent(PreviewActivity.this, VideoCutter.class);
                intent.putExtra("videoPath", viewModel.videoPath);
                intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
                intent.putExtra("soundPath", viewModel.soundPath);
                intent.putExtra("soundImage", viewModel.soundImage);
                intent.putExtra("soundId", viewModel.soundId);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(PreviewActivity.this, binding.playerView, "videoView");
                startActivityForResult(intent, 101);
//                startActivity(intent);
            }
        });
        binding.videoJoiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewModel.setIconColor(binding, PreviewActivity.this);
//                binding.videoJoiner.setColorFilter(ContextCompat.getColor(PreviewActivity.this, R.color.red));
                isVideoEditor = true;
                Intent intent = new Intent(PreviewActivity.this, VideoJoinerActivity.class);
                intent.putExtra("videoPath", viewModel.videoPath);
                intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
                intent.putExtra("soundPath", viewModel.soundPath);
                intent.putExtra("soundImage", viewModel.soundImage);
                intent.putExtra("soundId", viewModel.soundId);
                startActivityForResult(intent, 101);
//                startActivity(intent);
            }
        });

        binding.fastMotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewModel.setIconColor(binding, PreviewActivity.this);
//                binding.fastMotion.setColorFilter(ContextCompat.getColor(PreviewActivity.this, R.color.red));
                isVideoEditor = true;
                Intent intent = new Intent(PreviewActivity.this, FastMotionVideoActivity.class);
                intent.putExtra("videoPath", viewModel.videoPath);
                intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
                intent.putExtra("soundPath", viewModel.soundPath);
                intent.putExtra("soundImage", viewModel.soundImage);
                intent.putExtra("soundId", viewModel.soundId);
                startActivityForResult(intent, 101);
            }
        });
        binding.videoMixer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewModel.setIconColor(binding, PreviewActivity.this);
//                binding.videoMixer.setColorFilter(ContextCompat.getColor(PreviewActivity.this, R.color.red));
                isVideoEditor = true;
                Intent intent = new Intent(PreviewActivity.this, AudioVideoMixer.class);
                intent.putExtra("videoPath", viewModel.videoPath);
                intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
                intent.putExtra("soundPath", viewModel.soundPath);
                intent.putExtra("soundImage", viewModel.soundImage);
                intent.putExtra("soundId", viewModel.soundId);
                startActivityForResult(intent, 101);
            }
        });

        binding.slowMotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewModel.setIconColor(binding, PreviewActivity.this);
//                binding.slowMotion.setColorFilter(ContextCompat.getColor(PreviewActivity.this, R.color.red));
                isVideoEditor = true;
                Intent intent = new Intent(PreviewActivity.this, SlowMotionVideoActivity.class);
                intent.putExtra("videoPath", viewModel.videoPath);
                intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
                intent.putExtra("soundPath", viewModel.soundPath);
                intent.putExtra("soundImage", viewModel.soundImage);
                intent.putExtra("soundId", viewModel.soundId);
                startActivityForResult(intent, 101);
            }
        });


        binding.videoCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewModel.setIconColor(binding, PreviewActivity.this);
//                binding.videoCrop.setColorFilter(ContextCompat.getColor(PreviewActivity.this, R.color.red));
                isVideoEditor = true;
                Intent intent = new Intent(PreviewActivity.this, VideoCropActivity.class);
                intent.putExtra("videoPath", viewModel.videoPath);
                intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
                intent.putExtra("soundPath", viewModel.soundPath);
                intent.putExtra("soundImage", viewModel.soundImage);
                intent.putExtra("soundId", viewModel.soundId);
                startActivityForResult(intent, 101);
            }
        });

        binding.videoMirror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewModel.setIconColor(binding, PreviewActivity.this);
//                binding.videoMirror.setColorFilter(ContextCompat.getColor(PreviewActivity.this, R.color.red));
                isVideoEditor = true;
                Intent intent = new Intent(PreviewActivity.this, VideoMirrorActivity.class);
                intent.putExtra("videoPath", viewModel.videoPath);
                intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
                intent.putExtra("soundPath", viewModel.soundPath);
                intent.putExtra("soundImage", viewModel.soundImage);
                intent.putExtra("soundId", viewModel.soundId);
                startActivityForResult(intent, 101);
            }
        });
        binding.videoMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewModel.setIconColor(binding, PreviewActivity.this);
//                binding.videoMute.setColorFilter(ContextCompat.getColor(PreviewActivity.this, R.color.red));
                isVideoEditor = true;
                Intent intent = new Intent(PreviewActivity.this, VideoMuteActivity.class);
                intent.putExtra("videoPath", viewModel.videoPath);
                intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
                intent.putExtra("soundPath", viewModel.soundPath);
                intent.putExtra("soundImage", viewModel.soundImage);
                intent.putExtra("soundId", viewModel.soundId);
                startActivityForResult(intent, 101);
            }
        });
        binding.videoRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewModel.setIconColor(binding, PreviewActivity.this);
//                binding.videoRotate.setColorFilter(ContextCompat.getColor(PreviewActivity.this, R.color.red));
                isVideoEditor = true;
                Intent intent = new Intent(PreviewActivity.this, VideoRotateActivity.class);
                intent.putExtra("videoPath", viewModel.videoPath);
                intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
                intent.putExtra("soundPath", viewModel.soundPath);
                intent.putExtra("soundImage", viewModel.soundImage);
                intent.putExtra("soundId", viewModel.soundId);
                startActivityForResult(intent, 101);
            }
        });
        binding.videoReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewModel.setIconColor(binding, PreviewActivity.this);
//                binding.videoReverse.setColorFilter(ContextCompat.getColor(PreviewActivity.this, R.color.red));
                isVideoEditor = true;
                Intent intent = new Intent(PreviewActivity.this, VideoReverseActivity.class);
                intent.putExtra("videoPath", viewModel.videoPath);
                intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
                intent.putExtra("soundPath", viewModel.soundPath);
                intent.putExtra("soundImage", viewModel.soundImage);
                intent.putExtra("soundId", viewModel.soundId);
                startActivityForResult(intent, 101);
            }
        });
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


        binding.playerView.setVideoPath(viewModel.videoPath);

        binding.playerView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        binding.playerView.start();

        binding.playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.playerView.isPlaying()) {
                    binding.playerView.pause();
                    btnPlayVideo.setBackgroundResource(R.drawable.ic_play);
                } else {
                    binding.playerView.start();
                    btnPlayVideo.setBackgroundResource(0);
                }
            }
        });

        // binding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
//        player.setPlayWhenReady(true);
    }

    private void initObserve() {

       /* viewModel.isLoading.observe(this, isLoading -> {
            if (isLoading) {
                customDialogBuilder.showLoadingDialog();
//                showProgressDialog();
            } else {
                deleteRecursive(getPath());
                customDialogBuilder.hideLoadingDialog();
//                hideProgressDialog();
                setResult(RESULT_OK);
                Toast.makeText(this, "Video Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                onBackPressed();
                finish();
            }
        });*/
    }

    public void executeCompressCommand() {
        String[] strArr;
        String[] strArr2 = new String[0];
        String format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsoluteFile());
        sb.append("/");
        sb.append(getResources().getString(R.string.MainFolderName));
        sb.append("/");
        sb.append(getResources().getString(R.string.VideoMute));
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Environment.getExternalStorageDirectory().getAbsoluteFile());
        sb2.append("/");
        sb2.append(getResources().getString(R.string.MainFolderName));
        sb2.append("/");
        sb2.append(getResources().getString(R.string.VideoMute));
        sb2.append("/videomute");
        sb2.append(format);
        sb2.append(".mp4");
        y = sb2.toString();

        saveImage();
        strArr = new String[]{"-i", videoPath, "-i", Environment.getExternalStorageDirectory() + "/watermark.png", "-filter_complex", "overlay=main_w-overlay_w-5:main_h-overlay_h-5", y};

        a(strArr, y);
    }

    private void saveImage() {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.medley_watermark);
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File file = new File(extStorageDirectory, "watermark.png");
        if (!file.exists()) {
            try {
                FileOutputStream outStream = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void a(String[] strArr, final String str) {
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
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(Uri.fromFile(new File(y)));
                    sendBroadcast(intent);
                    c();
                    refreshGallery(str);

                } else if (returnCode == RETURN_CODE_CANCEL) {
                    Log.d("ffmpegfailure", str);
                    try {
                        new File(str).delete();
                        deleteFromGallery(str);
                        Toast.makeText(PreviewActivity.this, getString(R.string.Error_Creating_Video), Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                } else {
                    Log.d("ffmpegfailure", str);
                    try {
                        new File(str).delete();
                        deleteFromGallery(str);
                        Toast.makeText(PreviewActivity.this, getString(R.string.Error_Creating_Video), Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }


            }
        });
        getWindow().clearFlags(16);
    }

    public void c() {
        Intent intent = new Intent();
        intent.setFlags(67108864);
        intent.putExtra("song", y);
        setResult(RESULT_OK, intent);
//        startActivity(intent);
        finish();
    }

    public void refreshGallery(String str) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(str)));
        sendBroadcast(intent);
    }

    public void deleteFromGallery(String str) {
        String[] strArr = {"_id"};
        String[] strArr2 = {str};
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor query = contentResolver.query(uri, strArr, "_data = ?", strArr2, null);
        if (query.moveToFirst()) {
            try {
                contentResolver.delete(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, query.getLong(query.getColumnIndexOrThrow("_id"))), null, null);
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            }
        } else {
            try {
                new File(str).delete();
                refreshGallery(str);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        query.close();
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
//
//        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
//        Animation reverseAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_reverse);
//
//        progressBinding.ivParent.startAnimation(rotateAnimation);
//        progressBinding.ivChild.startAnimation(reverseAnimation);
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

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(0);
            mLocationRequest.setFastestInterval(0);
            mLocationRequest.setNumUpdates(1);

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.Your_GPS_seems_to_be_disabled_do_you_want_to_enable_it))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
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

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            post_lat = mLastLocation.getLatitude();
            post_long = mLastLocation.getLongitude();

            Log.e("Lat Long", post_lat + "   " + post_long);
        }
    };

    private void initListener() {
        binding.setOnBackClick(v -> onBackPressed());
        binding.setOnUploadClick(v -> {
            binding.playerView.pause();
            btnPlayVideo.setBackgroundResource(R.drawable.ic_play);

            onPause();
            if (isVideoEditor) {
                Log.e("TAG", "initListener: " + viewModel.videoPath);
                viewModel.videoPaths.add(viewModel.videoPath);
                File file = new File(viewModel.videoPath);
                // Get length of file in bytes
                long fileSizeInBytes = file.length();
                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB = fileSizeInBytes / 1024;
                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                long fileSizeInMB = fileSizeInKB / 1024;
                if (fileSizeInMB > 5) {
                    saveData(true);
                    Log.e("TAG", "File_size_after_edit: " + fileSizeInMB);
                } else {
                    saveData(false);
                    Log.e("TAG", "File_size_after_edit: " + fileSizeInMB);
                }
            } else {
                openPostBottomLayout();
            }
        });
    }

    private void openPostBottomLayout() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        ItemUploadSheetBinding uploadSheetBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.item_upload_sheet, null, false);
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

                Intent intent = new Intent(PreviewActivity.this, VideoUploadService.class);
                intent.putExtra("videoPath", viewModel.videoPath);
                intent.putExtra("postDescription", viewModel.postDescription);
                intent.putExtra("hashTag", viewModel.hashTag);
                intent.putExtra("canComment", viewModel.canComment);
                intent.putExtra("canSave", viewModel.canSave);
                intent.putExtra("canDuet", viewModel.canDuet);
                intent.putExtra("soundPath", viewModel.soundPath);
                intent.putExtra("soundId", viewModel.soundId);
                intent.putExtra("thumbnail_path", viewModel.videoThumbnail);
                intent.putExtra("lat", post_lat);
                intent.putExtra("lng", post_long);
                intent.putExtra("sound_title", viewModel.sessionManager.getUser().getData().getUserName());
                startService(intent);
//                    deleteRecursive(getPath());
//                    customDialogBuilder.hideLoadingDialog();
//                hideProgressDialog();
                setResult(RESULT_OK);

//                    Toast.makeText(this, "Video Uploaded Successfully", Toast.LENGTH_SHORT).show();
//                    onBackPressed();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("cancelled", false);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

           /* viewModel.onClickUpload.observe(this, s -> {
                if (!uploadSheetBinding.edtDes.getHashtags().isEmpty()) {
                    viewModel.hashTag = TextUtils.join(",", uploadSheetBinding.edtDes.getHashtags());
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

        uploadSheetBinding.edtDes.requestFocus();

        FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        dialog.show();
    }

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

    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory != null && fileOrDirectory.isDirectory() && fileOrDirectory.listFiles() != null) {
            for (File child : Objects.requireNonNull(fileOrDirectory.listFiles())) {
                deleteRecursive(child);
            }
        }
        if (fileOrDirectory != null) {
            fileOrDirectory.delete();
        }
    }

    @Override
    protected void onResume() {
//        if (player != null) {
//            player.setPlayWhenReady(true);
//        }
        playVideo();
        super.onResume();
    }

    @Override
    protected void onPause() {
//        if (player != null) {
//            player.setPlayWhenReady(false);
//        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && data != null) {
            viewModel.videoPath = data.getStringExtra("song");
            getAudioFile(viewModel.videoPath);
            System.out.println("======================================" + viewModel.videoPath);
            playVideo();
            initProgressDialog();
        }
    }

    private void getAudioFile(String videoPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Track audio;
                try {
//                    Movie m1 = MovieCreator.build(videoPath);
//                    audio = m1.getTracks().get(1);
//                    Movie m2 = new Movie();
//                    m2.addTrack(audio);
//                    DefaultMp4Builder builder = new DefaultMp4Builder();
//                    Container stdMp4 = builder.build(m2);
//                    FileOutputStream fos = new FileOutputStream(getPath().getPath().concat("/audioMixerFile.mp3"));
//                    Log.e("path===", "=======" + getPath().getPath().concat("/audioMixerFile.mp3"));
//                    stdMp4.writeContainer(fos.getChannel());
//                    fos.close();

                    File temp = new File(getPath().getPath().concat("/audioMixerFile.mp3"));

                    if (!temp.exists()) {
                        temp.createNewFile();
                    }

                    new AudioExtractor().genVideoUsingMuxer(videoPath, temp.getAbsolutePath(), -1, -1, true, false);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(PreviewActivity.this);
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
                Intent resultIntent = new Intent();
                resultIntent.putExtra("cancelled", true);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void saveData(boolean isCompress) {
        if (isCompress) {

            DefaultVideoStrategy strategy;

            strategy = new DefaultVideoStrategy.Builder()
                    .addResizer(new FractionResizer(0.5f))
                    .build();

            TranscoderOptions.Builder options = Transcoder.into(getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"));
            for (int i = 0; i < viewModel.videoPaths.size(); i++) {
                options.addDataSource(viewModel.videoPaths.get(i));
            }
            options.setListener(new TranscoderListener() {
                public void onTranscodeProgress(double progress) {
                    showProgressDialog();
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
//                                Movie m1 = MovieCreator.build(getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"));
//                                audio = m1.getTracks().get(1);
//                                Movie m2 = new Movie();
//                                m2.addTrack(audio);
//                                DefaultMp4Builder builder = new DefaultMp4Builder();
//                                Container stdMp4 = builder.build(m2);
//                                FileOutputStream fos = new FileOutputStream(getPath().getPath().concat("/originalSound.mp3"));
//                                stdMp4.writeContainer(fos.getChannel());
//                                fos.close();

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
                            Glide.with(PreviewActivity.this)
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
                                            viewModel.videoPath = getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4");
                                            viewModel.videoThumbnail = thumbFile.getPath();
                                            viewModel.soundPath = getPath().getPath().concat("/originalSound.mp3");
                                            viewModel.soundImage = getPath().getPath().concat("/soundImage.jpeg");
                                            openPostBottomLayout();
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                        }

                                        @Override
                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                            super.onLoadFailed(errorDrawable);

                                            hideProgressDialog();
                                            viewModel.videoPath = getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4");
                                            viewModel.videoThumbnail = thumbFile.getPath();
                                            viewModel.soundPath = getPath().getPath().concat("/originalSound.mp3");
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

            Track audio;
            try {
//                Movie m1 = MovieCreator.build(viewModel.videoPaths.get(0));
//                audio = m1.getTracks().get(0);
//                Movie m2 = new Movie();
//                m2.addTrack(audio);
//                DefaultMp4Builder builder = new DefaultMp4Builder();
//                Container stdMp4 = builder.build(m2);
//                FileOutputStream fos = new FileOutputStream(getPath().getPath().concat("/originalSound.mp3"));
//                stdMp4.writeContainer(fos.getChannel());
//                fos.close();

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
            Glide.with(PreviewActivity.this)
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
                            viewModel.videoPath = viewModel.videoPaths.get(0);
                            viewModel.videoThumbnail = thumbFile.getPath();
                            viewModel.soundPath = getPath().getPath().concat("/originalSound.mp3");
                            viewModel.soundImage = getPath().getPath().concat("/soundImage.jpeg");
                            openPostBottomLayout();

                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            customDialogBuilder.showLoadingDialog();
                            viewModel.videoPath = viewModel.videoPaths.get(0);
                            viewModel.videoThumbnail = thumbFile.getPath();
                            viewModel.soundPath = getPath().getPath().concat("/originalSound.mp3");
                            openPostBottomLayout();
                        }
                    });
        }
    }
}