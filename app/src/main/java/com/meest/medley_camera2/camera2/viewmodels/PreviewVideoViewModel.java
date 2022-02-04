package com.meest.medley_camera2.camera2.viewmodels;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.meest.R;
import com.meest.databinding.ActivityPreviewVideoMedleyBinding;
import com.meest.databinding.DailogProgressBinding;
import com.meest.medley_camera2.camera2.utills.CameraUtil;
import com.meest.medley_camera2.camera2.view.activity.MeestCameraActivity;
import com.meest.medley_camera2.camera2.view.activity.PreviewVideoActivity;
import com.meest.meestbhart.utilss.Constant;
import com.meest.videoEditing.UtilCommand;
import com.meest.videoEditing.VideoPlayerState;
import com.meest.videoEditing.slowmotionvideo.FileUtils;
import com.meest.videomvvmmodule.model.videos.AddPostResponse;
import com.meest.videomvvmmodule.model.videos.UploadVideoThumbnailResponse;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.GlobalApi;
import com.meest.videomvvmmodule.utils.ProgressRequestBody;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.TranscoderOptions;
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategy;
import com.otaliastudios.transcoder.strategy.size.AtMostResizer;
import com.otaliastudios.transcoder.strategy.size.FractionResizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PreviewVideoViewModel extends ViewModel {
    private static final String TAG = "PreviewVideoViewModel";

    public String postDescription = "", hashTag = "";
    public String videoThumbnail = "", soundImage = "", soundPath = "", soundId = "";
    public String videoUrl = "";
    public String thumbnailUrl = "";
    public MutableLiveData<Integer> onItemClick = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<String> onClickUpload = new MutableLiveData<>();
    public ObservableInt position = new ObservableInt(0);
    public SessionManager sessionManager;
    public ArrayList<String> videoPaths = new ArrayList<>();
    public String videoPath;
    private CompositeDisposable disposable = new CompositeDisposable();
    public String canComment = "0";
    public int canSave = 1, canDuet = 1;
    public ObservableBoolean postUpload = new ObservableBoolean(false);
    public boolean postSuccess = false;

    PreviewVideoActivity activity;
    ActivityPreviewVideoMedleyBinding binding;

    private String videoType;

    public String videoURL;
    public String thumbPath;

    public String pathkya;

    private ProgressDialog boomDialog;

    String folderName = "files";

    public ArrayList<String> handsFreeVideos = new ArrayList<>();

    public ArrayList<String> mergeVideos = new ArrayList<>();

    private int duetType;
    public ProgressDialog progressDialog;

    private final int slomoValue = 2;

    public VideoPlayerState q = new VideoPlayerState();
    private String b, editedVideoCacheURL;
    public VideoPlayerState p = new VideoPlayerState();
    String newfirstVideoPath, newsecondVideoPath, firstVideoPath, secondVideoPath;
    private int screenWidth, screenHeight;

    String type;

    public Double post_lat = 0.0d;
    public Double post_long = 0.0d;

    public ArrayList<String> handsFreeVideo = new ArrayList<>();

    public PreviewVideoViewModel(PreviewVideoActivity previewVideoActivity, ActivityPreviewVideoMedleyBinding binding) {
        this.activity = previewVideoActivity;
        this.binding = binding;
        initProgressDialog();
    }

    public void initData() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
    }

    public void getData() {
        if (activity.getIntent().getStringExtra("videoPath") != null) {
            videoURL = activity.getIntent().getStringExtra("videoPath");
            thumbPath = activity.getIntent().getStringExtra("thumbPath");
            soundPath = activity.getIntent().getStringExtra("post_sound");
            soundImage = activity.getIntent().getStringExtra("sound_image");
            soundId = activity.getIntent().getStringExtra("soundId");
            if (activity.getIntent().getStringExtra("Filter") != null) {
                if (activity.getIntent().getStringExtra("Filter").equalsIgnoreCase("Simple")) {
                    playVideo(videoURL);
                } else if (activity.getIntent().getStringExtra("Filter").equalsIgnoreCase("speedVideoPath")) {
                    videoType = CameraUtil.speed;
                    Log.e(TAG, "videoType: " + videoType);
                    switch (videoType) {
                        case "Slow":
                            slowmoCommand();
                            break;
                        case "Normal":
                            playVideo(videoURL);
                            break;
                        case "Fast":
                            fastMotionCommand(2);
                            break;
                    }
                } else if (activity.getIntent().getStringExtra("Filter").equalsIgnoreCase("videoWithSound")) {
                    soundId = activity.getIntent().getStringExtra("Extra");
                    playVideo(videoURL);
                } else if (activity.getIntent().getStringExtra("Filter").equalsIgnoreCase("Boomerang")) {
                    muteTheVid();
                }
            }
        }
        if (activity.getIntent().getStringExtra("boomerang") != null) {
            videoURL = activity.getIntent().getStringExtra("boomerang");
            Log.e(TAG, "boomerang: " + videoURL);
            muteTheVid();
        }
        if (activity.getIntent().getStringArrayListExtra("handsFreeVideos") != null) {
            handsFreeVideos = activity.getIntent().getStringArrayListExtra("handsFreeVideos");
            handsFree();
        }

        if (activity.getIntent().getStringArrayListExtra("mergeVideo") != null) {
            mergeVideos = activity.getIntent().getStringArrayListExtra("mergeVideo");
            duetType = activity.getIntent().getIntExtra("duetType", 0);
            Log.e(TAG, "First Video: ");
            compressHandsFree(mergeVideos.get(0));
        }

//        if (activity.getIntent().getStringExtra("speedVideoPath") != null) {
//            videoURL = activity.getIntent().getStringExtra("speedVideoPath");
//            Log.e(TAG, "videoURL Called: " + videoURL);
//            videoType = CameraUtil.speed;
//            Log.e(TAG, "videoType: " + videoType);
//            switch (videoType) {
//                case "Slow":
//                    slowmoCommand();
//                    break;
//                case "Normal":
//                    playVideo(videoURL);
//                    break;
//                case "Fast":
//                    fastMotionCommand(2);
//                    break;
//            }
//        }

    }


    private void inverseVideo(String compVid) {

        boomDialog = new ProgressDialog(activity);
        boomDialog.setMessage("Preparing Video..");
        boomDialog.setCancelable(false);
        boomDialog.show();

        String revVid = getVideoFilePath();

        String[] command = {"-y", "-i", compVid, "-vf", "vflip", "-metadata:s:v", "rotate=180", "-preset", "ultrafast", revVid};

        FFmpeg.executeAsync(command, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                Log.e(TAG, "revVid: " + revVid);
                boomDialog.dismiss();
                playVideo(revVid);
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {

                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();
            } else {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();

            }
        });
    }

    public void setOnItemClick(int type) {
        onItemClick.setValue(type);

    }

    public void onDescriptionTextChanged(CharSequence text) {
        postDescription = text.toString();
        position.set(postDescription.length());
    }

    public void fetchVideoUrl() {
        onClickUpload.setValue("hash");

//        video multipart
        MultipartBody.Part body;
        File file = new File(videoURL);
        ProgressRequestBody requestBody = new ProgressRequestBody(file, percentage -> {

        });
        body = MultipartBody.Part.createFormData("video", file.getName(), requestBody);

        MultipartBody.Part body1;
        File file1 = new File(videoThumbnail);
        ProgressRequestBody requestBody1 = new ProgressRequestBody(file1, percentage -> {

        });
        body1 = MultipartBody.Part.createFormData("thumbnail", file1.getName(), requestBody1);

        disposable.add(Global.initRetrofit().getVideoAndThumbnailUrl(Global.apikey, body, body1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doOnTerminate(() -> isLoading.setValue(false))
                .subscribe((UploadVideoThumbnailResponse updateUser, Throwable throwable) -> {
                    if (updateUser.getStatus() != null) {
                        videoUrl = updateUser.getData().getVideo_url();
                        thumbnailUrl = updateUser.getData().getThumbnail_path();
                        Log.d("video_url", "===============================================" + videoUrl);
//                        fetchImageUrl();
                        uploadPost();
                    }
                }));
    }

    public void getThumbnailUrl() {
        MultipartBody.Part body = null;
        if (videoThumbnail != null && !videoThumbnail.isEmpty()) {
            File file = new File(videoThumbnail);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            disposable.add(Global.initRetrofit().uploadImageRequest(Global.apikey, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe((uploadImageResponse, throwable) -> {
                        if (uploadImageResponse != null && uploadImageResponse.getStatus()) {
                            thumbnailUrl = uploadImageResponse.getData();
                            Log.e("Profile_Url", "===============================================" + thumbnailUrl);
                        }
                    }));
        }
    }

//    public void fetchImageUrl(){
//        onClickUpload.setValue("hash");
////        thumbnail
//        File file1 = new File(videoThumbnail);
//        ProgressRequestBody requestBody2 = new ProgressRequestBody(file1, percentage -> {
//
//        });
//
//        MultipartBody.Part body1 = MultipartBody.Part.createFormData("file", file1.getName(), requestBody2);
//
//        disposable.add(Global.initRetrofit().uploadImageRequest(Global.apikey,body1)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
//                .doOnTerminate(() -> isLoading.setValue(false))
//                .subscribe((UploadImageResponse updateUser, Throwable throwable) -> {
//                    if(updateUser.getStatus() != null){
//                        thumbnailUrl = updateUser.getData();
//                        Log.d("thumbnail_url","==============================================="+thumbnailUrl);
//                        uploadPost();
//                    }
//                }));
//    }

    public void uploadPost() {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("post_description", postDescription);
        hashMap.put("post_hash_tag", hashTag);
        hashMap.put("can_comment", canComment);
        hashMap.put("can_save", canSave);
        hashMap.put("can_duet", canDuet);
        hashMap.put("user_id", Global.userId);
        hashMap.put("post_video", videoUrl);
        hashMap.put("thumbnail_image", thumbnailUrl);
        hashMap.put("is_trending", "1");
        hashMap.put("is_orignal_sound", soundPath == null || soundPath.isEmpty() ? "0" : "1");
        Log.d("video_url", "===============================================" + videoUrl);
        if (soundId != null && !soundId.isEmpty()) {
            hashMap.put("sound_id", soundId);
        } else {
            hashMap.put("sound_title", "Original sound by " + sessionManager.getUser().getData().getUserName());
            hashMap.put("duration", "1:00");
            hashMap.put("singer", sessionManager.getUser().getData().getUserName());
        }

/*        HashMap<String, RequestBody> hashMap = new HashMap<>();
        hashMap.put("post_description", toRequestBody(postDescription));
        hashMap.put("post_hash_tag", toRequestBody(hashTag));
        hashMap.put("can_comment", toRequestBody(canComment));
        hashMap.put("can_save", toRequestBody(canSave));
        hashMap.put("can_duet", toRequestBody(canDuet));
        hashMap.put("user_id", toRequestBody(Global.userId));
        hashMap.put("post_video", toRequestBody("http://medley.s3.amazonaws.com/uploads/append.mp4"));
        hashMap.put("is_orignal_sound", toRequestBody(soundPath == null || soundPath.isEmpty() ? "0" : "1"));

        if (soundId != null && !soundId.isEmpty()) {
            hashMap.put("sound_id", toRequestBody(soundId));
        } else {
            hashMap.put("sound_title", toRequestBody("Original sound by " + sessionManager.getUser().getData().getUserName()));
            hashMap.put("duration", toRequestBody("1:00"));
            hashMap.put("singer", toRequestBody(sessionManager.getUser().getData().getUserName()));
        }*/
   /*     MultipartBody.Part body;

        File file = new File(videoPath);
        ProgressRequestBody requestBody = new ProgressRequestBody(file, percentage -> {

        });


        body = MultipartBody.Part.createFormData("post_video", file.getName(), requestBody);*/

    /*    File file1 = new File(videoThumbnail);
        ProgressRequestBody requestBody2 = new ProgressRequestBody(file1, percentage -> {

        });

        MultipartBody.Part body1 = MultipartBody.Part.createFormData("post_image", file1.getName(), requestBody2);*/
     /*   MultipartBody.Part body2 = null;
        MultipartBody.Part body3 = null;
        if (soundId == null || soundId.isEmpty()) {Preview

            File file2 = new File(soundPath);
            RequestBody requestFile2 =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file2);

            body2 = MultipartBody.Part.createFormData("post_sound", file2.getName(), requestFile2);

            if (soundImage != null) {
                File file3 = new File(soundImage);
                RequestBody requestFile3 =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file3);

                body3 = MultipartBody.Part.createFormData("sound_image", file1.getName(), requestFile3);
            }
        }*/
//        disposable.add(Global.initRetrofit().uploadPost(Global.apikey, Global.accessToken, hashMap, body, body1, body2, body3)
        disposable.add(Global.initRetrofit().uploadPost(Global.apikey, hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doOnTerminate(() -> isLoading.setValue(false))
                .subscribe((AddPostResponse updateUser, Throwable throwable) -> {
                    if (updateUser.getStatus()) {
//                        postSuccess=true;
                        new GlobalApi().rewardUser("3");
                    }
                }));
    }

    public RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public void playVideo(String outputVideo) {
        activity.runOnUiThread(() -> {
            binding.videoView.setVisibility(View.VISIBLE);
            binding.videoView.setVideoPath(outputVideo);
            MediaController mediaController = new MediaController(activity);
            binding.videoView.setMediaController(mediaController);
            if (!binding.videoView.isPlaying()) {
                binding.videoView.start();
            }
            binding.videoView.setOnPreparedListener(mp -> mp.setLooping(true));
            binding.videoView.setMediaController(null);
        });
    }


    private void muteTheVid() {
        String muteVid = getVideoFilePath();
        boomDialog = ProgressDialog.show(activity, "",
                "Creating Boomerang", true);
        boomDialog.setCancelable(false);
        boomDialog.setMessage("Creating Boomerang");
        boomDialog.show();

        String s = "volume=" + String.format(Locale.US, "%.1f", 0.0);
        String[] command = {"-y", "-i", videoURL, "-vcodec", "copy", "-af", s, "-preset", "ultrafast", muteVid};
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
                Log.e(TAG, "muteTheVid: " + muteVid);
                compressTheVid(muteVid);
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                if (boomDialog != null)
                    Toast.makeText(activity, "Muting canceled", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();
            } else {
                if (boomDialog != null)
                    Toast.makeText(activity, "Muting Error", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();
            }
        });
    }

    private void compressTheVid(String muteVid) {
        String compVid = getVideoFilePath01();
        String[] command = {"-y", "-i", muteVid, compVid};
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
                Log.e(TAG, "compVid: " + compVid);
                reverseTheVid(compVid);
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                if (boomDialog != null)
                    Toast.makeText(activity, "Compressing canceled", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();
            } else {
                if (boomDialog != null)
                    Toast.makeText(activity, "Compressing Error", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();
            }
        });
    }


    private void reverseTheVid(String compVid) {
        String revVid = getVideoFilePath();
        String[] command = {"-y", "-i", compVid, "-vf", "reverse", revVid};

        FFmpeg.executeAsync(command, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                Log.e(TAG, "revVid: " + revVid);
                finalMerge(compVid, revVid);
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                if (boomDialog != null)
                    Toast.makeText(activity, "Reversing canceled", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();
            } else {
                if (boomDialog != null)
                    Toast.makeText(activity, "Reversing Error", Toast.LENGTH_SHORT).show();
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
                Log.e(TAG, "videoURL: " + videoURL);

                //playVideo(videoURL);
                playVideo(videoURL);
//                Toast.makeText(this, boomVid, Toast.LENGTH_LONG).show();
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                if (boomDialog != null)
                    Toast.makeText(activity, "Merging canceled", Toast.LENGTH_SHORT).show();

                boomDialog.dismiss();
            } else {
                if (boomDialog != null)
                    Toast.makeText(activity, "Merging Error", Toast.LENGTH_SHORT).show();
                boomDialog.dismiss();
            }
        });
    }


    public String getVideoFilePath() {
        String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "duet.mp4";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File appSpecificExternalDir = new File(activity.getExternalCacheDir(), fname);
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

    public String getVideoFilePath01() {
        //String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "duet.mp4";
        String fname = System.currentTimeMillis() + ".mp4";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            File appSpecificExternalDir = new File(activity.getExternalCacheDir(), fname);
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


    //Hands-free Code:-
    void handsFree() {
        binding.handsFreeLayout.setVisibility(View.VISIBLE);
        if (handsFreeVideos.size() > 0) {
            playVideo(handsFreeVideos.get(0));
        }
    }

    public void clickEvents() {
        Log.e(TAG, "handsFreeVideos Size : " + handsFreeVideos.size());

        if (handsFreeVideos.size() > 0) {
            if (handsFreeVideos.size() == 1) {
                Log.e(TAG, "clickEvents: 1" + handsFreeVideos.get(0));
                Log.e(TAG, "handsFreeVideos 1 : " + handsFreeVideos.get(0));
                CameraUtil.setVideoThumbnail(binding.handsFreeImage01, handsFreeVideos.get(0));
                videoURL = handsFreeVideos.get(0);
                binding.handsFreeImage01.setOnClickListener(v -> playVideo(handsFreeVideos.get(0)));
            }

            if (handsFreeVideos.size() == 2) {
                Log.e(TAG, "clickEvents: 1" + handsFreeVideos.get(0));
                Log.e(TAG, "clickEvents: 1" + handsFreeVideos.get(1));

                Log.e(TAG, "handsFreeVideos 1 : " + handsFreeVideos.get(0));
                CameraUtil.setVideoThumbnail(binding.handsFreeImage01, handsFreeVideos.get(0));
                videoURL = handsFreeVideos.get(0);
                binding.handsFreeImage01.setOnClickListener(v -> playVideo(handsFreeVideos.get(0)));

                CameraUtil.setVideoThumbnail(binding.handsFreeImage02, handsFreeVideos.get(1));
                binding.handsFreeImage02.setOnClickListener(v -> playVideo(handsFreeVideos.get(1)));
                videoURL = handsFreeVideos.get(1);
            }

            if (handsFreeVideos.size() == 3) {
                Log.e(TAG, "clickEvents: 1" + handsFreeVideos.get(0));
                Log.e(TAG, "clickEvents: 1" + handsFreeVideos.get(1));
                Log.e(TAG, "clickEvents: 1" + handsFreeVideos.get(2));

                CameraUtil.setVideoThumbnail(binding.handsFreeImage01, handsFreeVideos.get(0));
                videoURL = handsFreeVideos.get(0);
                binding.handsFreeImage01.setOnClickListener(v -> playVideo(handsFreeVideos.get(0)));

                CameraUtil.setVideoThumbnail(binding.handsFreeImage02, handsFreeVideos.get(1));
                binding.handsFreeImage02.setOnClickListener(v -> playVideo(handsFreeVideos.get(1)));
                videoURL = handsFreeVideos.get(1);
                CameraUtil.setVideoThumbnail(binding.handsFreeImage03, handsFreeVideos.get(2));
                binding.handsFreeImage03.setOnClickListener(v -> playVideo(handsFreeVideos.get(2)));
                videoURL = handsFreeVideos.get(2);
            }

        } else {
            Toast.makeText(activity, "Can't Set The Video", Toast.LENGTH_SHORT).show();
        }


    }


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
        this.b = FileUtils.getTargetFileName(activity, filename, activity.getExternalCacheDir());
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
                activity.finish();
            }
            // Toast.makeText(this, getString(R.string.please_select_Quality), Toast.LENGTH_LONG).show();
        }
    }


    public void fastMotionCommand(int fastValue) {
        String[] strArr;
        String str = "";
        float f2 = fastValue == 2 ? 0.5f : fastValue == 3 ? 0.33333334f : fastValue == 4 ? 0.25f : fastValue == 5 ? 0.2f : fastValue == 6 ? 0.16666667f : fastValue == 7 ? 0.14285715f : fastValue == 8 ? 0.125f : 2.0f;
        String valueOf = String.valueOf(this.p.getStart() / 1000);
        String valueOf2 = String.valueOf(this.p.getDuration() / 1000);
        String filename = videoURL;
        this.b = FileUtils.getTargetFileName(activity, filename, activity.getExternalCacheDir());
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
                activity.finish();
            }
            //Toast.makeText(this, getString(R.string.please_select_Quality), 0).show();
        }
    }

    private void a(String[] strArr, final String str) {

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(activity.getResources().getString(R.string.please_wait));

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
                activity.sendBroadcast(intent);

                videoURL = b;
                playVideo(videoURL);
                refreshGallery(str);

            } else if (returnCode == RETURN_CODE_CANCEL) {
                Log.d("ffmpegfailure", str);
                try {
                    new File(str).delete();
                    deleteFromGallery(str);
                    Toast.makeText(activity, activity.getString(R.string.Error_Creating_Video), Toast.LENGTH_LONG).show();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            } else {
                Log.d("ffmpegfailure", str);
                try {
                    new File(str).delete();
                    deleteFromGallery(str);
                    Toast.makeText(activity, activity.getString(R.string.Error_Creating_Video), Toast.LENGTH_LONG).show();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }

        });

        activity.getWindow().clearFlags(16);

    }


    public void refreshGallery(String str) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(str)));
        activity.sendBroadcast(intent);
    }

    public void deleteFromGallery(String str) {
        String[] strArr = {"_id"};
        String[] strArr2 = {str};
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = activity.getContentResolver();
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

    private void makesameHieghtVideo1() {

        firstVideoPath = handsFreeVideo.get(0);

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
        secondVideoPath = handsFreeVideo.get(1);
        newsecondVideoPath = getVideoFilePath();
        String[] command = {"-y", "-i", secondVideoPath, "-preset", "ultrafast", "-vf", "scale=" + screenWidth / 2 + ":" + "" + screenHeight, newsecondVideoPath};
        long rc = FFmpeg.execute(command);
        if (rc == RETURN_CODE_SUCCESS) {
            addTwoVideo();
        } else {
            progressDialog.dismiss();
        }
    }


    private void addTwoVideo() {
        String outputVideo = getVideoFilePath();

        if (duetType == 0)
            type = "hstack";
        else
            type = "vstack";
        Config.enableLogCallback(message -> Log.e(Config.TAG, message.getText()));
        Config.enableStatisticsCallback(newStatistics -> progressDialog.setMessage("progress : video "));

        String[] command = {"-y", "-i", handsFreeVideo.get(0), "-i", handsFreeVideo.get(1), "-preset", "ultrafast", "-filter_complex", "hstack", outputVideo};
        FFmpeg.executeAsync(command, (executionId, returnCode) -> {
            if (returnCode == RETURN_CODE_SUCCESS) {
                progressDialog.dismiss();
//<<<<<<<HEAD
                saveData(outputVideo);

                Log.e(TAG, "outFile: " + outputVideo);
//                playVideo(outputVideo);
//                videoURL = outputVideo;

            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                if (progressDialog != null)
                    progressDialog.dismiss();
            } else {
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });
    }


    public void addVideo() {

        progressDialog.setMessage("Saving Media..");
        progressDialog.show();
        new Thread(() -> {
            try {
                ContentValues values = new ContentValues(3);
                values.put(MediaStore.Video.Media.TITLE, System.currentTimeMillis());
                values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                values.put(MediaStore.Video.Media.DATA, new File(videoURL).getAbsolutePath());
                activity.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                // do background stuff here
                activity.runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "Video save successfully.", Toast.LENGTH_LONG).show();
                    // OnPostExecute stuff here
                });
            } catch (Exception e) {
                progressDialog.dismiss();
                Toast.makeText(activity, "Something went wrong.", Toast.LENGTH_LONG).show();
            }
        }).start();

    }

    public void addToApi29Gallery() {
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
            ContentResolver resolver = activity.getContentResolver();
            Uri collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY); //all video files on primary external storage
            Uri uriSavedVideo = resolver.insert(collection, valuesvideos);

            ParcelFileDescriptor pfd;

            try {
                pfd = activity.getContentResolver().openFileDescriptor(uriSavedVideo, "w");

                assert pfd != null;
                FileOutputStream out = new FileOutputStream(pfd.getFileDescriptor());

                // Get the already saved video as fileinputstream from here
                FileInputStream in = new FileInputStream(new File(videoURL));


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

                activity.getContentResolver().update(uriSavedVideo, valuesvideos, null, null);

            } catch (Exception e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }
            activity.runOnUiThread(() -> {

                progressDialog.dismiss();
                Toast.makeText(activity, "Video save successfully.", Toast.LENGTH_LONG).show();
                // OnPostExecute stuff here
            });
        }).start();


    }

    public File getPath() {
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            filesDir = activity.getExternalFilesDir(null);
        } else {
            filesDir = activity.getFilesDir();
        }
        return filesDir;
    }

    @SuppressLint("MissingPermission")
    public void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods

        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(0);
            mLocationRequest.setFastestInterval(0);
            mLocationRequest.setNumUpdates(1);

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
            fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
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

    private FusedLocationProviderClient fusedLocationProviderClient;


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.Your_GPS_seems_to_be_disabled_do_you_want_to_enable_it))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(activity.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private DailogProgressBinding progressBinding;
    private Dialog mBuilder;

    public void initProgressDialog() {
        mBuilder = new Dialog(activity);
        mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        if (mBuilder.getWindow() != null) {
            mBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        progressBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dailog_progress, null, false);
        progressBinding.progressBar.setShader(new int[]{ContextCompat.getColor(activity, R.color.colorTheme), ContextCompat.getColor(activity, R.color.colorTheme), ContextCompat.getColor(activity, R.color.colorTheme)});


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

    public void compressHandsFree(String path) {

        DefaultVideoStrategy strategy = new DefaultVideoStrategy.Builder()
                .addResizer(new FractionResizer(0.7F))
                .addResizer(new AtMostResizer(1000))
                .build();
        String output = Constant.createOutputPath(activity, ".mp4");
        TranscoderOptions.Builder options = Transcoder.into(output);

        options.addDataSource(path);
        options.setVideoTrackStrategy(strategy);
        options.setListener(new TranscoderListener() {
            public void onTranscodeProgress(double progress) {
                showProgressDialog();
                if (progressBinding != null) {
                    //  if (viewModel.audio != null) {
                    progressBinding.progressBar.publishProgress((float) progress / 2);

                }
            }

            public void onTranscodeCompleted(int successCode) {

                new Thread(() -> {

                    String thumbnail = Constant.createOutputPath(activity, ".JPEG");
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
                    handsFreeVideo.add(output);

                    if (handsFreeVideo.size() == 1)
                    {
                        Log.e(TAG, "First Video Complete:");
                        compressHandsFree(mergeVideos.get(1));
                    }
                    else if (handsFreeVideo.size() == 2)
                    {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Log.e(TAG, "Second Video Complete:");
                                progressDialog.setMessage("Preparing Video..");
                                progressDialog.show();
                            }
                        });

                        addTwoVideo();
                        //makesameHieghtVideo1();

                    }


                }).start();
            }
            // }

            public void onTranscodeCanceled() {
                Log.d("TAG", "onTranscodeCanceled: ");
            }

            public void onTranscodeFailed(@NonNull Throwable exception) {
                Log.d("TAG", "onTranscodeCanceled: " + exception);
            }
        }).transcode();



    }

    public void saveData(String path) {

        DefaultVideoStrategy strategy = new DefaultVideoStrategy.Builder()
                .addResizer(new FractionResizer(0.7F))
                .addResizer(new AtMostResizer(1000))
                .build();
        String output = Constant.createOutputPath(activity, ".mp4");
        TranscoderOptions.Builder options = Transcoder.into(output);

        options.addDataSource(path);
        options.setVideoTrackStrategy(strategy);
        options.setListener(new TranscoderListener() {
            public void onTranscodeProgress(double progress) {
                showProgressDialog();
                if (progressBinding != null) {
                    progressBinding.progressBar.publishProgress((float) progress / 2);
                }
            }

            public void onTranscodeCompleted(int successCode) {
                new Thread(() -> {
                    String thumbnail = Constant.createOutputPath(activity, ".JPEG");
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
                    Intent intent = new Intent(activity, PreviewVideoActivity.class);
                    intent.putExtra("videoPath", output);
                    intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.aac"));
                    intent.putExtra("thumbPath", thumbFile.getPath());

                    thumbPath = thumbFile.getPath();
                    videoURL = output;
                    playVideo(output);

                }).start();
            }
            // }

            public void onTranscodeCanceled() {
            }

            public void onTranscodeFailed(@NonNull Throwable exception) {
                Log.d("TAG", "onTranscodeCanceled: ");
            }
        }).transcode();
    }
}

