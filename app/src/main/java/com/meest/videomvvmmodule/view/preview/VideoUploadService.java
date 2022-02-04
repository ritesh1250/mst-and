package com.meest.videomvvmmodule.view.preview;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.meest.R;
import com.meest.videomvvmmodule.model.videos.AddPostResponse;
import com.meest.videomvvmmodule.model.videos.DataUploadToS3;
import com.meest.videomvvmmodule.model.videos.UploadVideoThumbnailResponse;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.GlobalApi;
import com.meest.videomvvmmodule.utils.ProgressRequestBody;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

public class VideoUploadService extends Service {
    public String videoPath = "", postDescription = "";
    public String hashTag = " ", canComment = "", soundId = "";
    public int canSave = 1, canDuet = 1;
    public String soundPath = "";
    public String sound_title = "";
    public String videoUrl = "";
    private Double post_lat = 0.0d;
    private Double post_long = 0.0d;
    public String thumbnailUrl = "";
    public String videoThumbnail = "";
    private CompositeDisposable disposable = new CompositeDisposable();
    final int ID_INDETERMINATE_SERVICE = 9;
    final int ID_NOTIFICATION_COMPLETE = 4;
    private static String CHANNEL_ID = "post_upload";
    public String urlSoundId = "", urlSound = "";
    NotificationCompat.Builder notification = null;
    NotificationManagerCompat notificationManager = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManagernew = getSystemService(NotificationManager.class);
            NotificationChannel priorityNotificationChannel = new NotificationChannel(CHANNEL_ID, "post upload channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManagernew.createNotificationChannel(mChannel);
            notificationManagernew.createNotificationChannel(priorityNotificationChannel);
        }
        notificationManager = NotificationManagerCompat.from(VideoUploadService.this);
        videoPath = intent.getExtras().getString("videoPath");
        postDescription = intent.getExtras().getString("postDescription");
        hashTag = intent.getExtras().getString("hashTag");
        canComment = intent.getExtras().getString("canComment");
        canSave = intent.getExtras().getInt("canSave");
        canDuet = intent.getExtras().getInt("canDuet");
        soundPath = intent.getExtras().getString("soundPath");
        soundId = intent.getExtras().getString("soundId");
        sound_title = intent.getExtras().getString("sound_title");
        videoThumbnail = intent.getExtras().getString("thumbnail_path");
        post_lat = intent.getExtras().getDouble("lat");
        post_long = intent.getExtras().getDouble("lng");
        fetchVideoUrl();

        return super.onStartCommand(intent, flags, startId);
    }

    public void fetchVideoUrl() {
        Log.e("videoPath", "==================" + videoPath);
        Log.e("videoThumbnail", "==================" + videoThumbnail);
        Log.e("soundPath", "==================" + soundPath);
        Log.e("hashTag", "==================" + hashTag);
//        video multipart
        MultipartBody.Part body;
        File file = new File(videoPath);
        ProgressRequestBody requestBody = new ProgressRequestBody(file, percentage -> {

        });
        body = MultipartBody.Part.createFormData("video", file.getName(), requestBody);

        MultipartBody.Part body1;
        File file1 = new File(videoThumbnail);
        ProgressRequestBody requestBody1 = new ProgressRequestBody(file1, percentage -> {

        });
        body1 = MultipartBody.Part.createFormData("thumbnail", file1.getName(), requestBody1);
        showBar();
        if (soundPath != null) {
            MultipartBody.Part body2;
            File file2 = new File(soundPath);
            ProgressRequestBody requestBody2 = new ProgressRequestBody(file2, percentage -> {

            });

            body2 = MultipartBody.Part.createFormData("sound", file2.getName(), requestBody2);
            disposable.add(Global.initRetrofit().getDataUploadToS3(Global.apikey, body, body1, body2)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
//                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
//                .doOnTerminate(() -> isLoading.setValue(false))
                    .subscribe((DataUploadToS3 updateUser, Throwable throwable) -> {
                        try {
                            if (updateUser.getStatus() != null) {
                                videoUrl = updateUser.getData().getVideo_url();
                                thumbnailUrl = updateUser.getData().getThumbnail_path();
                                urlSound = updateUser.getData().getSound_path();
                                urlSoundId = updateUser.getData().getSound_id();
                                Log.d("urlSoundId", "===============================================" + urlSoundId);
//                        fetchImageUrl();
                                uploadPost();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
//                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }));
        } else {
            disposable.add(Global.initRetrofit().getVideoAndThumbnailUrl(Global.apikey, body, body1)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe((UploadVideoThumbnailResponse updateUser, Throwable throwable) -> {
                        if (updateUser.getStatus() != null) {
                            videoUrl = updateUser.getData().getVideo_url();
                            thumbnailUrl = updateUser.getData().getThumbnail_path();
                            Log.d("urlSoundId", "===============================================" + urlSoundId);
//                        fetchImageUrl();
                            uploadPost();
                        }
                    }));
        }

    }

    public void uploadPost() {
        Log.e("soundId", "=========mayank=========" + soundId);
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
        if (post_lat != null && post_long != null) {
            hashMap.put("lat", post_lat);
            hashMap.put("lng", post_long);
        } else {
            hashMap.put("lat", 0);
            hashMap.put("lng", 0);
        }
        hashMap.put("is_orignal_sound", soundPath == null || soundPath.isEmpty() ? "0" : "1");
        Log.d("video_url", "===============================================" + videoUrl);
        if (soundId != null && !soundId.isEmpty()) {
            Log.e("=========mayank===", "true");
            hashMap.put("sound_id", soundId);
        } else {
            Log.e("=========mayank===", "false");
            hashMap.put("sound_title", "Original sound by " + sound_title);
            hashMap.put("duration", "1:00");
            hashMap.put("singer", sound_title);
            hashMap.put("sound_id", urlSoundId);
            hashMap.put("sound", urlSound);
        }
        disposable.add(Global.initRetrofit().uploadPost(Global.apikey, hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
//                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
//                .doOnTerminate(() -> isLoading.setValue(false))
                .subscribe((AddPostResponse updateUser, Throwable throwable) -> {
                    if (updateUser.getStatus()) {
//                        postSuccess=true;
                        new GlobalApi().rewardUser("3");
                        showBarComplete(getString(R.string.video_uploaded));
//                        deleteRecursive(getPath());
                        Toast.makeText(this, getString(R.string.Video_Uploaded_Successfully), Toast.LENGTH_SHORT).show();
                        File dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + getResources().getString(R.string.MainFolderName));
//                        deleteRecursive(dir);
                        stopSelf();
                    }
                }));
    }

    private void deleteFfmpegFile() {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + getResources().getString(R.string.MainFolderName));
        Log.e("isDirectory==", String.valueOf(dir.isDirectory()));
        if (dir.isDirectory()) {
            dir.delete();
//            String[] children = dir.list();
//            Log.e("TAG", "deleteFfmpegFile: "+children.length);
//            for (int i = 0; i < children.length; i++)
//            {
//                new File(dir, children[i]).delete();
//                Log.e("TAG", "deleteFfmpegFile: "+children[i]);
//            }
        }
    }

    public File getPath() {
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            filesDir = getExternalCacheDir();
        } else {
            filesDir = getFilesDir();
        }
        return filesDir;
    }

    public void showBar() {
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(Const.appName)
                .setContentText(getString(R.string.Uploading))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.new_action_color));
        notification.setProgress(0, 0, true);
        startForeground(ID_INDETERMINATE_SERVICE, notification.build());
    }

    public void showBarComplete(String msg) {
        notification.setContentText(msg).setProgress(0, 0, false);
        notificationManager.notify(ID_NOTIFICATION_COMPLETE, notification.build());
        stopSelf();
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

}
