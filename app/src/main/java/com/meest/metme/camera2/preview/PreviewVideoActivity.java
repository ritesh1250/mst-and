package com.meest.metme.camera2.preview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.meest.R;
import com.meest.metme.camera2.CameraActivity;
import com.meest.metme.camera2.utills.CameraUtil;
import com.meest.metme.camera2.videoEditing.RecordAudioActivity;
import com.meest.metme.camera2.videoEditing.UtilCommand;
import com.meest.metme.camera2.videoEditing.VideoPlayerState;
import com.meest.metme.camera2.videoEditing.fastmotionvideo.FastMotionVideoActivity;
import com.meest.metme.camera2.videoEditing.slowmotionvideo.FileUtils;

import com.meest.metme.camera2.videoEditing.videocrop.VideoCropActivity;
import com.meest.metme.camera2.videoEditing.videocutter.VideoCutter;
import com.meest.metme.camera2.videoEditing.videomirror.VideoMirrorActivity;
import com.meest.metme.camera2.videoEditing.videomute.VideoMuteActivity;
import com.meest.metme.camera2.videoEditing.videoreverse.VideoReverseActivity;
import com.meest.metme.camera2.videoEditing.videorotate.VideoRotateActivity;
import com.meest.videoEditing.slowmotionvideo.SlowMotionVideoActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;



import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class PreviewVideoActivity extends AppCompatActivity {
    private static final String TAG = "PreviewActivity2";
    ImageView downloadMedia;
    String imageUri;
    PlayerView exoPlayerView;

    // creating a variable for exoplayer
    SimpleExoPlayer absPlayerInternal;

    // aditya code
    private Uri mCropImageUri;
    final int PIC_CROP = 1;
    public boolean isVideoEditor = false;
    HorizontalScrollView edit_tab;
    View video_cutter, addMusic, fast_motion, slow_motion, video_crop, video_rotate,
            video_mirror, video_reverse, video_mute;
    Button video_next_btn;
//    LinearLayout image_options;

    // url of video which we are loading.
    int appNameStringRes = R.string.app_name;
    String videoURL;
    Bitmap bitmap;
    private ArrayList<String> handsFreeVideos = new ArrayList<>();

    private LinearLayout handsFreeLayout;
    private ImageView handsFreeImage1, handsFreeImage2, handsFreeImage3;

    private final int slomoValue = 2;
    private String b, editedVideoCacheURL;
    private String videoType;
    public VideoPlayerState p = new VideoPlayerState();

    private ProgressDialog boomDialog;

    private ArrayList<String> mergeVideos = new ArrayList<>();

    VideoView video_view;
    String type;
    private int duetType;
    private int screenWidth, screenHeight;

    ImageView nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        initButtons();

        video_cutter = findViewById(R.id.video_cutter);
        addMusic = findViewById(R.id.addMusic);
        fast_motion = findViewById(R.id.fast_motion);
        slow_motion = findViewById(R.id.slow_motion);
        video_crop = findViewById(R.id.video_crop);
        video_rotate = findViewById(R.id.video_rotate);
        video_mirror = findViewById(R.id.video_mirror);
        video_mute = findViewById(R.id.video_mute);
        video_reverse = findViewById(R.id.video_reverse);
        nextButton = findViewById(R.id.nextButton);

        video_view = findViewById(R.id.video_view);

        video_cutter.setOnClickListener(v -> {
//            Intent intent = new Intent(this, AdjustActivity.class);
//            intent.putExtra("videoPath",videoURL);
//            startActivityForResult(intent,999);

            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, VideoCutter.class);
            intent.putExtra("videoPath", videoURL);
            startActivityForResult(intent, 101);

        });

        fast_motion.setOnClickListener(v -> {
            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, FastMotionVideoActivity.class);
            intent.putExtra("videoPath", videoURL);
//            intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
//            intent.putExtra("soundPath", viewModel.soundPath);
//            intent.putExtra("soundImage", viewModel.soundImage);
//            intent.putExtra("soundId", viewModel.soundId);
            startActivityForResult(intent, 101);
        });

        slow_motion.setOnClickListener(v -> {
            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, SlowMotionVideoActivity.class);
            intent.putExtra("videoPath", videoURL);
//            intent.putExtra("videoPath", viewModel.videoPath);
//            intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
//            intent.putExtra("soundPath", viewModel.soundPath);
//            intent.putExtra("soundImage", viewModel.soundImage);
//            intent.putExtra("soundId", viewModel.soundId);
            startActivityForResult(intent, 101);
        });

        video_crop.setOnClickListener(v -> {
            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, VideoCropActivity.class);
            intent.putExtra("videoPath", videoURL);
//            intent.putExtra("videoPath", viewModel.videoPath);
//            intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
//            intent.putExtra("soundPath", viewModel.soundPath);
//            intent.putExtra("soundImage", viewModel.soundImage);
//            intent.putExtra("soundId", viewModel.soundId);
            startActivityForResult(intent, 101);
        });

        video_rotate.setOnClickListener(v -> {
            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, VideoRotateActivity.class);
            intent.putExtra("videoPath", videoURL);
//            intent.putExtra("videoPath", viewModel.videoPath);
//            intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
//            intent.putExtra("soundPath", viewModel.soundPath);
//            intent.putExtra("soundImage", viewModel.soundImage);
//            intent.putExtra("soundId", viewModel.soundId);
            startActivityForResult(intent, 101);
        });

        video_mirror.setOnClickListener(v -> {
            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, VideoMirrorActivity.class);
            intent.putExtra("videoPath", videoURL);
//            intent.putExtra("videoPath", viewModel.videoPath);
//            intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
//            intent.putExtra("soundPath", viewModel.soundPath);
//            intent.putExtra("soundImage", viewModel.soundImage);
//            intent.putExtra("soundId", viewModel.soundId);
            startActivityForResult(intent, 101);
        });

        video_reverse.setOnClickListener(v -> {
            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, VideoReverseActivity.class);
            intent.putExtra("videoPath", videoURL);
//            intent.putExtra("videoPath", viewModel.videoPath);
//            intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
//            intent.putExtra("soundPath", viewModel.soundPath);
//            intent.putExtra("soundImage", viewModel.soundImage);
//            intent.putExtra("soundId", viewModel.soundId);
            startActivityForResult(intent, 101);
        });

        video_mute.setOnClickListener(v -> {
            isVideoEditor = true;
            Intent intent = new Intent(PreviewVideoActivity.this, VideoMuteActivity.class);
            intent.putExtra("videoPath", videoURL);
//            intent.putExtra("videoPath", viewModel.videoPath);
//            intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
//            intent.putExtra("soundPath", viewModel.soundPath);
//            intent.putExtra("soundImage", viewModel.soundImage);
//            intent.putExtra("soundId", viewModel.soundId);
            startActivityForResult(intent, 101);
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("name", "");
                intent.putExtra("path",videoURL);
                intent.putExtra("isVideo",true);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });


//
//        adjust_video_btn.setOnClickListener(view -> {
//            Intent intent = new Intent(this, AdjustActivity.class);
//            intent.putExtra("videoPath",videoURL);
//            String adjustCode = "ADJUST";
//            intent.putExtra("ADJ_CODE", adjustCode);
//            startActivityForResult(intent,999);
//        });

        exoPlayerView = findViewById(R.id.exo_player);
        handsFreeLayout = findViewById(R.id.handsFreeLayout);
        downloadMedia = findViewById(R.id.downloadMedia);
        Log.e(TAG, "PreviewActivity: ");
        if (getIntent().getStringExtra("videoPath") != null) {
            videoURL = getIntent().getStringExtra("videoPath");
            Log.e(TAG, "videoURL: " + videoURL);
            previewSetup(videoURL);
        }

        else if (getIntent().getStringExtra("boomerang") != null) {
            videoURL = getIntent().getStringExtra("boomerang");
            Log.e(TAG, "boomerang: " + videoURL);
            muteTheVid();
        }

        if (getIntent().getStringArrayListExtra("handsFreeVideos") != null) {
            handsFreeVideos = getIntent().getStringArrayListExtra("handsFreeVideos");

            handsFree();
        }

        if (getIntent().getStringArrayListExtra("mergeVideo") != null) {
            mergeVideos = getIntent().getStringArrayListExtra("mergeVideo");
            duetType = getIntent().getIntExtra("duetType", 0);

            progressDialog.setMessage("Preparing Video..");
            progressDialog.show();

            runOnUiThread(() -> {
                Handler handler = new Handler();
                Runnable runnable = this::makesameHieghtVideo1;
                handler.postDelayed(runnable, 500);
            });

        }

        if (getIntent().getStringExtra("speedVideoPath") != null) {
            videoURL = getIntent().getStringExtra("speedVideoPath");
            Log.e(TAG, "videoURL Called: " + videoURL);
            videoType = CameraUtil.speed;
            Log.e(TAG, "videoType: " + videoType);
            switch (videoType) {
                case "Slow":
                    slowmoCommand();
                    break;
                case "Normal":
                    previewSetup(videoURL);
                    break;
                case "Fast":
                    fastMotionCommand(2);
                    break;
            }
        }


        findViewById(R.id.close_btn).setOnClickListener(v -> {
            startActivity();
        });


        downloadMedia.setOnClickListener(v -> {

            if (SDK_INT >= Build.VERSION_CODES.Q) {
                addToApi29Gallery(new File(videoURL));
            } else {
                addVideo(new File(videoURL));
            }


//            if (SDK_INT >= Build.VERSION_CODES.R) {
//                if (!checkPermission()) {
//                    requestPermission();
//                }
//                else
//                {
//                    convertImageToBitmap();
//                }
//            }
//            else
//            {
//                Bitmap  bitmap = ((BitmapDrawable) showImage.getDrawable()).getBitmap();
//                try {
//                    saveImage(bitmap,"testFileNEw");
//                } catch (IOException e) {
//                    Log.e(TAG, "IOException: "+e.getMessage());
//                    e.printStackTrace();
//                }
//
//                MediaScannerConnection.scanFile(this,
//                        new String[] {imageUri}, null,
//                        (path, uri) -> {
//                            Log.e("ExternalStorage", "Scanned " + path + ":");
//                            Log.i("ExternalStorage", "-> uri=" + uri);
//                        });
//
//            }

        });


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

    }

    private void muteTheVid() {
        String muteVid = getVideoFilePath();
        boomDialog = ProgressDialog.show(this, "",
                "Creating Boomerang", true);
        boomDialog.setCancelable(false);
        boomDialog.setMessage("Creating Boomerang");
        boomDialog.show();

        String s = "volume=" + String.format(Locale.US, "%.1f", 0.0);
        String[] command = {"-y","-i", videoURL, "-vcodec", "copy", "-af", s, "-preset", "ultrafast", muteVid};
        FFmpeg.executeAsync(command, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                File fdelete = new File(Uri.parse(videoURL).getPath());
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        // System.out.println("file Deleted :" + Uri.parse(mNextVideoAbsolutePath).getPath());
                    } else {
                        //System.out.println("file not Deleted :" + Uri.parse(mNextVideoAbsolutePath).getPath());
                    }
                }
                Log.e(TAG, "muteTheVid: "+muteVid);
                compressTheVid(muteVid);
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                if (boomDialog != null)
                    Toast.makeText(this, "Muting canceled", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();
            } else {
                if (boomDialog != null)
                    Toast.makeText(this, "Muting Error", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();
            }
        });
    }

    private void compressTheVid(String muteVid) {
        String compVid = getVideoFilePath01();
        String[] command = {"-y","-i", muteVid, compVid};
        FFmpeg.executeAsync(command, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {

                File fdelete = new File(Uri.parse(muteVid).getPath());
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        System.out.println("file Deleted :" + Uri.parse(muteVid).getPath());
                    } else {
                        System.out.println("file not Deleted :" + Uri.parse(muteVid).getPath());
                    }
                }
                Log.e(TAG, "compVid: "+compVid);
                reverseTheVid(compVid);
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                if (boomDialog != null)
                    Toast.makeText(this, "Compressing canceled", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();
            } else {
                if (boomDialog != null)
                    Toast.makeText(this, "Compressing Error", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();
            }
        });
    }

    public String getVideoFilePath01() {
        //String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "duet.mp4";
        String fname = System.currentTimeMillis()+".mp4";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            File appSpecificExternalDir = new File(getExternalCacheDir(), fname);
            pathkya = appSpecificExternalDir.getAbsolutePath();
        } else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/";
            File dir = new File(path);

            boolean isDirectoryCreated = dir.exists();
            if (!isDirectoryCreated) {
                dir.mkdir();
            }
            pathkya = path + fname;
        }
        return pathkya;
    }

    private void reverseTheVid(String compVid) {
        String revVid = getVideoFilePath();
        String[] command = {"-y","-i", compVid, "-vf", "reverse", revVid};

        FFmpeg.executeAsync(command, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                Log.e(TAG, "revVid: "+revVid);
                finalMerge(compVid, revVid);
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                if (boomDialog != null)
                    Toast.makeText(this, "Reversing canceled", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();
            } else {
                if (boomDialog != null)
                    Toast.makeText(this, "Reversing Error", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();
            }
        });
    }

    private void finalMerge(String compVid, String revVid) {
        String boomVid = getVideoFilePath01();

        String[] command = {"-y", "-i", compVid, "-i", revVid, "-filter_complex", "[0:v] [0:a:0] [1:v] [1:a:0] concat=n=2:v=1:a=1 [v] [a]", "-map", "[v]", "-map", "[a]", boomVid};
        FFmpeg.executeAsync(command, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                boomDialog.dismiss();

                File fdelete1 = new File(Uri.parse(compVid).getPath());
                File fdelete2 = new File(Uri.parse(revVid).getPath());
                if (fdelete1.exists() && fdelete2.exists()) {
                    if (fdelete1.delete() && fdelete2.delete()) {
                        System.out.println("All Files deleted");
                    } else {
                        System.out.println("Some files couldn't be deleted");
                    }
                }
                videoURL = boomVid;
                Log.e(TAG, "videoURL: "+videoURL);

                //playVideo(videoURL);
                previewSetup(videoURL);
//                Toast.makeText(this, boomVid, Toast.LENGTH_LONG).show();
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                if (boomDialog != null)
                    Toast.makeText(this, "Merging canceled", Toast.LENGTH_SHORT).show();

                boomDialog.dismiss();
            } else {
                if (boomDialog != null)
                    Toast.makeText(this, "Merging Error", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();
            }
        });
    }


    private void initButtons() {
        video_cutter = findViewById(R.id.video_cutter);
        fast_motion = findViewById(R.id.fast_motion);
        slow_motion = findViewById(R.id.slow_motion);
        video_crop = findViewById(R.id.video_crop);
        video_rotate = findViewById(R.id.video_rotate);
        video_mirror = findViewById(R.id.video_mirror);
        video_mute = findViewById(R.id.video_mute);
        video_reverse = findViewById(R.id.video_reverse);
        edit_tab = findViewById(R.id.edit_tab);
        addMusic = findViewById(R.id.addMusic);

        addMusic.setOnClickListener(v -> {
            Intent intent = new Intent(PreviewVideoActivity.this, RecordAudioActivity.class);
            intent.putExtra("videoPath", videoURL);
            startActivityForResult(intent, 101);
        });

    }

    ProgressDialog progressDialog;
    String pathkya, newfirstVideoPath, newsecondVideoPath, firstVideoPath, secondVideoPath;
    String folderName = "files";
    String progressMsg = "Loading";

    private void makesameHieghtVideo1() {

        firstVideoPath = mergeVideos.get(0);


        newfirstVideoPath = getVideoFilePath();

        String[] command = {"-y", "-i", firstVideoPath, "-preset", "ultrafast", "-vf", "scale=" + screenWidth / 2 + ":" + "" + screenHeight, newfirstVideoPath};
        long rc = FFmpeg.execute(command);
        if (rc == RETURN_CODE_SUCCESS) {

            makesameHieghtVideo2(progressDialog);
        } else {
            progressDialog.dismiss();
        }
    }

    //if make vertical duet then make width same of both videos
//if make horizontal duet then make layout_height same of both videos
    private void makesameHieghtVideo2(ProgressDialog progressDialog) {
        secondVideoPath = mergeVideos.get(1);
        newsecondVideoPath = getVideoFilePath();
        String[] command = {"-y", "-i", secondVideoPath, "-preset", "ultrafast", "-vf", "scale=" + screenWidth / 2 + ":" + "" + screenHeight, newsecondVideoPath};
        long rc = FFmpeg.execute(command);
        if (rc == RETURN_CODE_SUCCESS) {
            addTwoVideo();
        } else {
            progressDialog.dismiss();
        }
    }


    protected String getVideoFilePath() {
        String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "duet.mp4";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File appSpecificExternalDir = new File(getExternalCacheDir(), fname);
            pathkya = appSpecificExternalDir.getAbsolutePath();
        } else {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/";
            File dir = new File(path);

            boolean isDirectoryCreated = dir.exists();
            if (!isDirectoryCreated) {
                dir.mkdir();
            }
            pathkya = path + fname;
        }
        return pathkya;
    }

    private void addTwoVideo() {
        String outputVideo = getVideoFilePath();

        if (duetType == 0)
            type = "hstack";
        else
            type = "vstack";
        Config.enableLogCallback(message -> Log.e(Config.TAG, message.getText()));
        Config.enableStatisticsCallback(newStatistics -> progressDialog.setMessage("progress : video "));

        String[] command = {"-y", "-i", newfirstVideoPath, "-i", newsecondVideoPath, "-preset", "ultrafast", "-filter_complex", "hstack", outputVideo};
        FFmpeg.executeAsync(command, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                progressDialog.dismiss();
                Log.e(TAG, "outFile: " + outputVideo);
                previewSetup(outputVideo);
                videoURL =  outputVideo;
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                if (progressDialog != null)
                    progressDialog.dismiss();
            } else {
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }


    private void playVideo(String outputVideo) {
        video_view.setVisibility(View.VISIBLE);
        video_view.setVideoPath(outputVideo);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video_view);
        video_view.setMediaController(mediaController);
        video_view.seekTo(1);
        video_view.start();
        video_view.setOnPreparedListener(mp -> mp.setLooping(true));
    }


//    private void performCrop(Uri picUri) {
//
//        try {
//            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//            // indicate image type and Uri
//            cropIntent.setDataAndType(picUri, "image/*");
//            // set crop properties here
//            cropIntent.putExtra("crop", true);
//            // indicate aspect of desired crop
//            cropIntent.putExtra("aspectX", 1);
//            cropIntent.putExtra("aspectY", 1);
//            // indicate output X and Y
//            cropIntent.putExtra("outputX", 128);
//            cropIntent.putExtra("outputY", 128);
//            // retrieve data on return
//            cropIntent.putExtra("return-data", true);
//            // start the activity - we handle returning in onActivityResult
//            startActivityForResult(cropIntent, PIC_CROP);
//        }
//        // respond to users whose devices do not support the crop action
//        catch (ActivityNotFoundException anfe) {
//            // display an error message
//            String errorMessage = "Whoops - your device doesn't support the crop action!";
//            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }


    public void previewSetup(String videoURL) {

        if (videoURL != null) {

            exoPlayerView.setVisibility(View.VISIBLE);

            TrackSelector trackSelectorDef = new DefaultTrackSelector();
            absPlayerInternal = ExoPlayerFactory.newSimpleInstance(this, trackSelectorDef); //creating a player instance
            String userAgent = Util.getUserAgent(this, "camera2project");
            DefaultDataSourceFactory defdataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
            Uri uriOfContentUrl = Uri.parse(videoURL);
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(defdataSourceFactory).createMediaSource(uriOfContentUrl);  // creating a media source
            LoopingMediaSource loopingMediaSource = new LoopingMediaSource(mediaSource);
            absPlayerInternal.prepare(loopingMediaSource);
            absPlayerInternal.setPlayWhenReady(true);// start loading video and play it at the moment a chunk of it is available offline
            exoPlayerView.showController();
            exoPlayerView.setControllerHideOnTouch(false);
//            exoPlayerView.setControllerHideOnTouch(false);
            exoPlayerView.setPlayer(absPlayerInternal);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity();

    }

    void startActivity(){
//        Intent intent = new Intent(this, CameraActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        finish();
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 999) {
//                videoURL = data.getStringExtra("videoPath");
//                Log.e("line 321", "321:  " + videoURL);
//                previewSetup(videoURL);
//            }
//
//            if (requestCode == 101 && data != null) {
////                viewModel.videoPath = data.getStringExtra("song");
////                getAudioFile(viewModel.videoPath);
////                System.out.println("======================================" + viewModel.videoPath);
////                playVideo();
////                initProgressDialog();
//                videoURL = data.getStringExtra("videoPath");
//                Toast.makeText(this, videoURL, Toast.LENGTH_SHORT).show();
//                previewSetup(videoURL);
//            }
//
//
//            if (requestCode == PIC_CROP) {
//                if (data != null) {
//                    // get the returned data
//                    Bundle extras = data.getExtras();
//                    // get the cropped bitmap
//                    Bitmap selectedBitmap = extras.getParcelable("data");
//
//                    showImage.setImageBitmap(selectedBitmap);
//                }
//            }
//
//            if (requestCode == 123) {
//                showImage2(String.valueOf(data.getData()), showImage);
//            }
//
//            if (requestCode == 321) {
//
//                if (data != null) {
//                    // get the returned data
//                    Bundle extras = data.getExtras();
//                    // get the cropped bitmap
//                    Bitmap selectedBitmap = extras.getParcelable("data");
//
//                    showImage.setImageBitmap(selectedBitmap);
//                }
////
//            }
//
////            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
////                CropImage.ActivityResult result = CropImage.getActivityResult(data);
////                if (resultCode == RESULT_OK) {
////                    Uri resultUri = result.getUri();
////                    String photoUri = resultUri.toString();
////
////                    showImage2(photoUri, showImage);
////                }
////            }
//
////            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
////                CropImage.ActivityResult result = CropImage.getActivityResult(data);
////                if (resultCode == RESULT_OK) {
////                    Uri resultUri = result.getUri();
////                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
////                    Exception error = result.getError();
////                }
////            }
//
//            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//                CropImage.ActivityResult result = CropImage.getActivityResult(data);
//                if (resultCode == RESULT_OK) {
//                    Uri resultUri = result.getUri();
//                    String photoUri = resultUri.toString();
//                    showImage2(photoUri, showImage);
//                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                    Exception error = result.getError();
//                    Log.d("setupError", error + "");
//                }
//            }
//        }


//        if ((requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) && (resultCode == Activity.RESULT_OK)) {
//            Uri imageUri = CropImage.getPickImageResultUri(this, data);
//
//            // For API >= 23 we need to check specifically that we have permissions to read external storage.
//            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
//                // request permissions and handle the result in onRequestPermissionsResult()
//                mCropImageUri = imageUri;
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
//            } else {
//                // no permissions required or already grunted, can start crop image activity
//                startCropImageActivity(imageUri);
//            }
//        }

    // handle result of CropImageActivity
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
////                ((ImageButton) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
//                Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
//            }
//        }
    // }

    @Override
    protected void onPause() {
        super.onPause();
        if (absPlayerInternal != null) {
            if (absPlayerInternal.isPlaying()) {
                absPlayerInternal.stop();
            }
        }
    }

    @Override
    protected void onDestroy() {

        if (absPlayerInternal != null) {
            if (absPlayerInternal.isPlaying()) {
                absPlayerInternal.stop();
            }
        }
        super.onDestroy();


    }

    //Hands-free Code:-

    void handsFree() {
        initViews();
        handsFreeLayout.setVisibility(View.VISIBLE);
        if (handsFreeVideos.size() > 0) {

            previewSetup(handsFreeVideos.get(0));
        }
    }

    public void initViews() {
        handsFreeImage1 = findViewById(R.id.handsFreeImage01);
        handsFreeImage2 = findViewById(R.id.handsFreeImage02);
        handsFreeImage3 = findViewById(R.id.handsFreeImage03);
        clickEvents();
    }

    public void clickEvents() {
        Log.e(TAG, "handsFreeVideos Size : " + handsFreeVideos.size());

        if (handsFreeVideos.size() > 0) {
//            if (handsFreeVideos.get(0)!=null) {
//                Log.e(TAG, "handsFreeVideos 1 : "+handsFreeVideos.get(0));
//                CameraUtil.setVideoThumbnail(handsFreeImage1, handsFreeVideos.get(0));
//                videoURL = handsFreeVideos.get(0);
//                handsFreeImage1.setOnClickListener(v -> previewSetup(handsFreeVideos.get(0)));
//            }
//            if (handsFreeVideos.get(1)!=null) {
//                Log.e(TAG, "handsFreeVideos 2 : "+handsFreeVideos.get(1));
//                CameraUtil.setVideoThumbnail(handsFreeImage2, handsFreeVideos.get(1));
//                handsFreeImage2.setOnClickListener(v -> previewSetup(handsFreeVideos.get(1)));
//                videoURL = handsFreeVideos.get(1);
//            }
//            if (handsFreeVideos.get(2)!=null) {
//                Log.e(TAG, "handsFreeVideos 3 : "+handsFreeVideos.get(2));
//                CameraUtil.setVideoThumbnail(handsFreeImage3, handsFreeVideos.get(2));
//                handsFreeImage3.setOnClickListener(v -> previewSetup(handsFreeVideos.get(2)));
//                videoURL = handsFreeVideos.get(2);
//            }

            if (handsFreeVideos.size() == 1) {
                Log.e(TAG, "clickEvents: 1" + handsFreeVideos.get(0));
                Log.e(TAG, "handsFreeVideos 1 : " + handsFreeVideos.get(0));
                CameraUtil.setVideoThumbnail(handsFreeImage1, handsFreeVideos.get(0));
                videoURL = handsFreeVideos.get(0);
                handsFreeImage1.setOnClickListener(v -> previewSetup(handsFreeVideos.get(0)));
            }

            if (handsFreeVideos.size() == 2) {
                Log.e(TAG, "clickEvents: 1" + handsFreeVideos.get(0));
                Log.e(TAG, "clickEvents: 1" + handsFreeVideos.get(1));

                Log.e(TAG, "handsFreeVideos 1 : " + handsFreeVideos.get(0));
                CameraUtil.setVideoThumbnail(handsFreeImage1, handsFreeVideos.get(0));
                videoURL = handsFreeVideos.get(0);
                handsFreeImage1.setOnClickListener(v -> previewSetup(handsFreeVideos.get(0)));

                CameraUtil.setVideoThumbnail(handsFreeImage2, handsFreeVideos.get(1));
                handsFreeImage2.setOnClickListener(v -> previewSetup(handsFreeVideos.get(1)));
                videoURL = handsFreeVideos.get(1);
            }

            if (handsFreeVideos.size() == 3) {
                Log.e(TAG, "clickEvents: 1" + handsFreeVideos.get(0));
                Log.e(TAG, "clickEvents: 1" + handsFreeVideos.get(1));
                Log.e(TAG, "clickEvents: 1" + handsFreeVideos.get(2));

                CameraUtil.setVideoThumbnail(handsFreeImage1, handsFreeVideos.get(0));
                videoURL = handsFreeVideos.get(0);
                handsFreeImage1.setOnClickListener(v -> previewSetup(handsFreeVideos.get(0)));

                CameraUtil.setVideoThumbnail(handsFreeImage2, handsFreeVideos.get(1));
                handsFreeImage2.setOnClickListener(v -> previewSetup(handsFreeVideos.get(1)));
                videoURL = handsFreeVideos.get(1);
                CameraUtil.setVideoThumbnail(handsFreeImage3, handsFreeVideos.get(2));
                handsFreeImage3.setOnClickListener(v -> previewSetup(handsFreeVideos.get(2)));
                videoURL = handsFreeVideos.get(2);
            }

        } else {
            Toast.makeText(this, "Can't Set The Video", Toast.LENGTH_SHORT).show();
        }


    }

    public VideoPlayerState q = new VideoPlayerState();

    public void slowmoCommand() {
        String[] strArr;
        String str = "";
        float f2 = 2.0f;
        Log.e(TAG, "slowmotioncommand: " + slomoValue);
        if (slomoValue != 2) {
            if (slomoValue == 3) {
                f2 = 3.0f;
            } else if (slomoValue == 4) {
                f2 = 4.0f;
            } else if (slomoValue == 5) {
                f2 = 5.0f;
            } else if (slomoValue == 6) {
                f2 = 6.0f;
            } else if (slomoValue == 7) {
                f2 = 7.0f;
            } else if (slomoValue == 8) {
                f2 = 8.0f;
            }
        }
        String valueOf = String.valueOf(this.q.getStart() / 1000);
        String.valueOf(this.q.getStop() / 1000);
        String valueOf2 = String.valueOf(this.q.getDuration() / 1000);
        String filename = videoURL;
        this.b = FileUtils.getTargetFileName(this, filename, getExternalCacheDir());
        if (true) {
            if (slomoValue == 2) {
                str = "[0:a]atempo=0.5[a]";
            } else if (slomoValue == 3) {
                str = "[0:a]atempo=0.5[a]";
            } else if (slomoValue == 4) {
                str = "[0:a]atempo=0.5,atempo=0.5[a]";
            } else if (slomoValue == 5) {
                str = "[0:a]atempo=0.5,atempo=0.5[a]";
            } else if (slomoValue == 6) {
                str = "[0:a]atempo=0.5,atempo=0.5,atempo=0.5[a]";
            } else if (slomoValue == 7) {
                str = "[0:a]atempo=0.5,atempo=0.5,atempo=0.5[a]";
            } else if (slomoValue == 8) {
                str = "[0:a]atempo=0.5,atempo=0.5,atempo=0.5,atempo=0.5[a]";
            }
        }
        try {
            if (true) {
//                StringBuilder sb = new StringBuilder();
//                sb.append("setpts=");
//                sb.append(f2);
//                sb.append("*PTS");
                StringBuilder sb = new StringBuilder();
                sb.append("[0:v]setpts=");
                sb.append(f2);
                sb.append("*PTS[v]");
                String outFormat = sb.toString() + ";" + str;
//                strArr = new String[]{"-y", "-ss", valueOf, "-i", filename, "-filter:v", sb.toString(), "-filter:a", str, "-r", "15", "-b:v", "2500k", "-strict", "experimental", "-t", valueOf2, this.b};
                Log.e("filterComplex", outFormat);
                strArr = new String[]{"-y", "-i", filename, "-filter_complex", outFormat, "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", "-preset", "ultrafast", this.b};

            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("setpts=");
                sb2.append(f2);
                sb2.append("*PTS");
                strArr = new String[]{"-y", "-ss", valueOf, "-i", filename, "-filter:v", sb2.toString(), "-an", "-r", "15", "-ab", "128k", "-vcodec", "mpeg4", "-b:v", "2500k", "-sample_fmt", "s16", "-strict", "experimental", "-t", valueOf2, this.b};
            }
//            if (h != 1.0f)
            a(strArr, this.b);
        } catch (Exception unused) {
            File file = new File(this.b);
            if (file.exists()) {
                file.delete();
                finish();
                return;
            }
            // Toast.makeText(this, getString(R.string.please_select_Quality), Toast.LENGTH_LONG).show();
        }
    }

    private void a(String[] strArr, final String str) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));

        progressDialog.show();

        String ffmpegCommand = UtilCommand.main(strArr);
        FFmpeg.executeAsync(ffmpegCommand, (executionId, returnCode) -> {
            Log.d("TAG", String.format("FFmpeg process exited with rc %d.", returnCode));

            Log.d("TAG", "FFmpeg process output:");

            Config.printLastCommandOutput(Log.INFO);

            progressDialog.dismiss();
            if (returnCode == RETURN_CODE_SUCCESS) {
                progressDialog.dismiss();
                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                intent.setData(Uri.fromFile(new File(b)));
                sendBroadcast(intent);

                previewSetup(b);
                refreshGallery(str);

            } else if (returnCode == RETURN_CODE_CANCEL) {
                Log.d("ffmpegfailure", str);
                try {
                    new File(str).delete();
                    deleteFromGallery(str);
                    Toast.makeText(PreviewVideoActivity.this, getString(R.string.Error_Creating_Video), Toast.LENGTH_LONG).show();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            } else {
                Log.d("ffmpegfailure", str);
                try {
                    new File(str).delete();
                    deleteFromGallery(str);
                    Toast.makeText(PreviewVideoActivity.this, getString(R.string.Error_Creating_Video), Toast.LENGTH_LONG).show();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }

        });

        getWindow().clearFlags(16);

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


    public void fastMotionCommand(int fastValue) {
        String[] strArr;
        String str = "";
        float f2 = fastValue == 2 ? 0.5f : fastValue == 3 ? 0.33333334f : fastValue == 4 ? 0.25f : fastValue == 5 ? 0.2f : fastValue == 6 ? 0.16666667f : fastValue == 7 ? 0.14285715f : fastValue == 8 ? 0.125f : 2.0f;
        String valueOf = String.valueOf(this.p.getStart() / 1000);
        String valueOf2 = String.valueOf(this.p.getDuration() / 1000);
        String filename = videoURL;
        this.b = FileUtils.getTargetFileName(this, filename, getExternalCacheDir());
        editedVideoCacheURL = this.b;
        if (true) {
            if (fastValue == 2) {
                str = "atempo=2.0";
            } else if (fastValue == 3) {
                str = "atempo=2.0";
            } else if (fastValue == 4) {
                str = "atempo=2.0,atempo=2.0";
            } else if (fastValue == 5) {
                str = "atempo=2.0,atempo=2.0";
            } else if (fastValue == 6) {
                str = "atempo=2.0,atempo=2.0,atempo=2.0";
            } else if (fastValue == 7) {
                str = "atempo=2.0,atempo=2.0,atempo=2.0";
            } else if (fastValue == 8) {
                str = "atempo=2.0,atempo=2.0,atempo=2.0,atempo=2.0";
            }
        }
        try {
            if (true) {
                StringBuilder sb = new StringBuilder();
                sb.append("setpts=");
                sb.append(f2);
                sb.append("*PTS");
                strArr = new String[]{"-y", "-i", filename, "-filter:v", sb.toString(), "-filter:a", str, "-r", "15", "-b:v", "2500k", "-strict", "experimental", "-preset", "ultrafast", this.b};
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("setpts=");
                sb2.append(f2);
                sb2.append("*PTS");
                strArr = new String[]{"-y", "-ss", valueOf, "-i", filename, "-filter:v", sb2.toString(), "-an", "-r", "15", "-ab", "128k", "-vcodec", "mpeg4", "-b:v", "2500k", "-sample_fmt", "s16", "-strict", "experimental", "-t", "-preset", "ultrafast", valueOf2, this.b};
            }

            a(strArr, this.b);


        } catch (Exception unused) {
            File file = new File(this.b);
            if (file.exists()) {
                file.delete();
                finish();
                return;
            }
            //Toast.makeText(this, getString(R.string.please_select_Quality), 0).show();
        }
    }


    //Android 11 Permission Code:-

    private static final int PERMISSION_REQUEST_CODE = 2296;

    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(PreviewVideoActivity.this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(PreviewVideoActivity.this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }


    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(PreviewVideoActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 999) {
                videoURL = data.getStringExtra("videoPath");
                Log.e("line 321", "321:  " + videoURL);
                previewSetup(videoURL);
            }

            if (requestCode == 101 && data != null) {
//                viewModel.videoPath = data.getStringExtra("song");
//                getAudioFile(viewModel.videoPath);
//                System.out.println("======================================" + viewModel.videoPath);
//                playVideo();
//                initProgressDialog();
                videoURL = data.getStringExtra("videoPath");
                Log.e(TAG, "videoURL: " + videoURL);
//                Toast.makeText(this, videoURL, Toast.LENGTH_SHORT).show();
                previewSetup(videoURL);
            }
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
                    convertImageToBitmap();
                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void convertImageToBitmap() {
        //Bitmap  bitmap = ((BitmapDrawable) showImage.getDrawable()).getBitmap();

        saveImageToGallery(bitmap);
    }

    private void saveImageToGallery(Bitmap bitmap) {
        OutputStream fos;
        if (SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + ".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "Image/jpg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "TestFolder");
                Uri uriImage = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = resolver.openOutputStream(uriImage);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Objects.requireNonNull(fos);
                Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e(TAG, "FileNotFoundException: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {

        OutputStream fos;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "Camera");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + "Camera";

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);

        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();

        Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
    }

    private void addToApi29Gallery(File file) {
        progressDialog.setMessage("Saving Media..");
        progressDialog.show();
        new Thread(() -> {
            // do background stuff here
            String videoFileName = System.currentTimeMillis() + ".mp4";

            ContentValues valuesvideos;
            valuesvideos = new ContentValues();
            valuesvideos.put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/" + "Meest");
            valuesvideos.put(MediaStore.Video.Media.TITLE, videoFileName);
            valuesvideos.put(MediaStore.Video.Media.DISPLAY_NAME, videoFileName);
            valuesvideos.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            valuesvideos.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
            valuesvideos.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
            valuesvideos.put(MediaStore.Video.Media.IS_PENDING, 1);
            ContentResolver resolver = getContentResolver();
            Uri collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY); //all video files on primary external storage
            Uri uriSavedVideo = resolver.insert(collection, valuesvideos);

            ParcelFileDescriptor pfd;

            try {
                pfd = getContentResolver().openFileDescriptor(uriSavedVideo, "w");

                assert pfd != null;
                FileOutputStream out = new FileOutputStream(pfd.getFileDescriptor());

                // Get the already saved video as fileinputstream from here
                FileInputStream in = new FileInputStream(file);


                byte[] buf = new byte[8192];
                int len;
                while ((len = in.read(buf)) > 0) {

                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
                pfd.close();
                valuesvideos.clear();
                valuesvideos.put(MediaStore.Video.Media.IS_PENDING, 0);
                valuesvideos.put(MediaStore.Video.Media.IS_PENDING, 0); //only your app can see the files until pending is turned into 0

                getContentResolver().update(uriSavedVideo, valuesvideos, null, null);

            } catch (Exception e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }
            runOnUiThread(() -> {

                progressDialog.dismiss();
                Toast.makeText(this, "Video save successfully.", Toast.LENGTH_LONG).show();
                // OnPostExecute stuff here
            });
        }).start();


    }

    public void addVideo(File videoFile) {

        progressDialog.setMessage("Saving Media..");
        progressDialog.show();
        new Thread(() -> {
            try {
                ContentValues values = new ContentValues(3);
                values.put(MediaStore.Video.Media.TITLE, System.currentTimeMillis());
                values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                values.put(MediaStore.Video.Media.DATA, videoFile.getAbsolutePath());
                getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                // do background stuff here
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Video save successfully.", Toast.LENGTH_LONG).show();
                    // OnPostExecute stuff here
                });
            } catch (Exception e) {
                progressDialog.dismiss();
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG).show();
            }
        }).start();

    }

    private void mergeTwoVideo() {


        String outputVideo = getVideoFilePath();

        Config.enableLogCallback(message -> Log.e(Config.TAG, message.getText()));
        Config.enableStatisticsCallback(newStatistics -> progressDialog.setMessage("progress : video "));
        progressDialog.show();
        String[] command = {"-i", handsFreeVideos.get(0), "-i", handsFreeVideos.get(1), "-filter_complex", "[0:v] [0:a:0] [1:v] [1:a:0] concat=n=2:v=1:a=1 [v] [a]", "-map", "[v]", "-map", "[a]", outputVideo};

        FFmpeg.executeAsync(command, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                progressDialog.dismiss();
                Log.e(TAG, "outFile: " + outputVideo);
                previewSetup(outputVideo);
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                if (progressDialog != null)
                    progressDialog.dismiss();
            } else {
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }



}

