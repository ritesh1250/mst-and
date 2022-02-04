package com.meest.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.meest.Interfaces.CompressMedia;
import com.meest.Paramaters.TextPostUploadParam;
import com.meest.R;
import com.meest.responses.FeedResponse;
import com.meest.mediapikcer.utils.PictureFacer;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.register.fragment.profile.model.UploadimageResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.model.StoryDataModel;
import com.meest.utils.MediaUploadInterface;
import com.meest.utils.UploadFilesToServer;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryPostuploadService extends Service implements CompressMedia, MediaUploadInterface {
    private static final int COMPRESS_VIDEO_CODE = 712;
    private static final int CREATE_THUMB = 232;
    String storyMediaPath, storyThumbPath, userTag, storyPostText, type, address_name, storyMediaUrl, selectedLocation;
    ArrayList<String> multipleMedia;
    List<PictureFacer> selectedItem;
    private String storycompressedVideo;
    int recode = 0;
    public static List<FeedResponse.UserTags> taggedUsername = new ArrayList<>();
    boolean allowComment = true;
    boolean sensitiveContent = false;
    private boolean isCompressed = false, isThumb = false;
    private String latitude, longitude;
    private Double post_lat = 0.0d;
    private Double post_long = 0.0d;
    final int ID_INDETERMINATE_SERVICE = 9;
    final int ID_NOTIFICATION_COMPLETE = 4;
    final int MAX_PROGRESS = 250;
    private static String CHANNEL_ID = "post_upload";
    NotificationCompat.Builder notification = null;
    NotificationManagerCompat notificationManager = null;
    boolean isStoryVideo = false, isStoryMultiple = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManagernew = getSystemService(NotificationManager.class);
            NotificationChannel priorityNotificationChannel = new NotificationChannel(CHANNEL_ID, "post upload channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManagernew.createNotificationChannel(mChannel);
            notificationManagernew.createNotificationChannel(priorityNotificationChannel);
        }
        notificationManager = NotificationManagerCompat.from(StoryPostuploadService.this);
        Gson gson = new Gson();
        Type typeTaguser = new TypeToken<List<FeedResponse.UserTags>>() {
        }.getType();
        isStoryMultiple = intent.getExtras().getBoolean("isStoryMultiple", false);
        storyMediaPath = intent.getExtras().getString("storyMediaPath");
        storyThumbPath = intent.getExtras().getString("storyThumbPath");
        isStoryVideo = intent.getExtras().getBoolean("isStoryVideo", false);
        allowComment = intent.getExtras().getBoolean("allowStoryComment", false);
        sensitiveContent = intent.getBooleanExtra("sensitiveContent", false);
        userTag = intent.getStringExtra("taggedUsername");

        taggedUsername = gson.fromJson(userTag, typeTaguser);
        storyPostText = intent.getStringExtra("storyPostText");
        post_lat = intent.getDoubleExtra("post_lat", 0.0d);
        post_long = intent.getDoubleExtra("post_long", 0.0d);

//        taggedUsername=(List<FeedResponse.UserTags>)intent.getSerializableExtra("taggedUsername");
        if (type != null && type.equalsIgnoreCase("location")) {
            if (type.equalsIgnoreCase("location")) {
                latitude = intent.getStringExtra("lat");
                longitude = intent.getStringExtra("log");
                address_name = intent.getStringExtra("address_name");
            }
        }
        if (isStoryMultiple) {
            String resul = intent.getStringExtra("multipleMedia");
            Gson gson_resul = new Gson();
            Type type = new TypeToken<List<PictureFacer>>() {
            }.getType();
            selectedItem = gson_resul.fromJson(resul, type);
            recode = intent.getExtras().getInt("recode");
        }

        postFeed();
        return super.onStartCommand(intent, flags, startId);

    }


    public void postFeed() {
        showBar();
        if (isStoryMultiple) {
            if (isStoryVideo) {
                UploadFilesToServer asyncTask = new UploadFilesToServer(selectedItem, StoryPostuploadService.this, this, recode, false);
                asyncTask.startMediaUpload();
            } else {
                UploadFilesToServer asyncTask = new UploadFilesToServer(selectedItem, StoryPostuploadService.this, this, recode, true);
                asyncTask.startMediaUpload();
            }

        } else if (isStoryVideo) {
            isCompressed = true;
            uploadPost(storyThumbPath, false, true);
        }
        storyMediaUrl = storyMediaPath;
        uploadPost(storyMediaUrl, false, false);

    }

    public void showBar() {
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.Uploading))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        notification.setProgress(0, 0, true);

        startForeground(ID_INDETERMINATE_SERVICE, notification.build());
    }

    public void showBarComplete(String msg) {
        notification.setContentText(msg)
                .setProgress(0, 0, false);
        notificationManager.notify(ID_NOTIFICATION_COMPLETE, notification.build());
        stopSelf();
    }

    private void uploadPost(String fileUri, boolean isVideo, boolean isThumb) {
        // uploading media first
        RequestBody requestFile;
        MultipartBody.Part body;
        File file = new File(fileUri);
        if (isVideo) {
            Log.e("Video upload start", fileUri);
            requestFile = RequestBody.create(MediaType.parse("video/*"), file);
            body = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
        } else {
            requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }

        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<UploadimageResponse> call = webApi.uploadImageRequest(map, body);

        call.enqueue(new Callback<UploadimageResponse>() {
            @Override
            public void onResponse(Call<UploadimageResponse> call, Response<UploadimageResponse> response) {
                try {
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        if (isThumb) {
                            Log.e("thumbnailImage", "upload done");
                            storyThumbPath = response.body().getData().getUrl();
                            if (isStoryMultiple) {
                                postNow();

                            }
//                            else {
//                                uploadPost(mediaPath, true, false);
//                            }
                        } else {
                            storyMediaUrl = response.body().getData().getUrl();
//                            Log.e("Video upload done", mediaUrl);
                            postNow();
                        }
                    } else {
                        showBarComplete(getResources().getString(R.string.error_msg));
//                        loading.setVisibility(View.GONE);
                        storyMediaUrl = "";
//                        Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showBarComplete(getResources().getString(R.string.error_msg));

//                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    storyMediaUrl = "";
                }
            }

            @Override
            public void onFailure(Call<UploadimageResponse> call, Throwable t) {
                showBarComplete(getString(R.string.error_msg));
//                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void postNow() {
        List<String> hashTagList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);

        StoryDataModel storyDataModel = new StoryDataModel();
        TextPostUploadParam.Location loc = new TextPostUploadParam.Location();
        try {
            if (isStoryMultiple) {
                List<TextPostUploadParam.PostDatum> postDatumList = new ArrayList<>();
                for (String uploadUrl : multipleMedia) {
                    if (isStoryVideo) {
                        storyDataModel.setImage(0);
                    } else {
                        storyDataModel.setImage(1);
                    }
                    storyDataModel.setStory(uploadUrl);
                }
            } else if (isStoryVideo) {
                storyDataModel.setImage(0);
                storyDataModel.setStory(storyMediaUrl);
            } else {
                storyDataModel.setImage(1);
                storyDataModel.setStory(storyMediaUrl);
            }
            storyDataModel.setLocation(loc);
            loc.setLat(0.0);
            loc.setLong(0.0);
            storyDataModel.setHashTags(hashTagList);
            storyDataModel.setStatus(true);
            storyDataModel.setCaption("");

        } catch (JsonIOException e) {
            showBarComplete(getResources().getString(R.string.error_msg));
            e.printStackTrace();
        }
        Log.e("ERROR ss", storyDataModel.toString());
        Call<ApiResponse> call = webApi.insertStory(map, storyDataModel);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                try {
                    if (response.code() == 200 && response.body().getSuccess()) {
                        Log.e("ERROR", response.body().getSuccess().toString());
//                        loading.setVisibility(View.GONE);
                        if (isStoryVideo) {
                            showBarComplete("Story Successfully");
                            Intent local = new Intent();
                            local.putExtra("status", "story");
                            local.setAction("post_upload");
                            sendBroadcast(local);
                            onDestroy();
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showBarComplete("Story Successfully");
                                    Intent local = new Intent();
                                    local.putExtra("status", "story");
                                    local.setAction("post_upload");
                                    sendBroadcast(local);
                                    onDestroy();
                                }
                            }, 1000);
                        }
                    } else {
//                        loading.setVisibility(View.GONE);
                        showBarComplete(getString(R.string.error_msg));
                        onDestroy();
//                        Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    }
                } catch (Exception e) {
//                    loading.setVisibility(View.GONE);
                    showBarComplete(getString(R.string.error_msg));
                    e.printStackTrace();
                    onDestroy();
                }
            }

            // gghhgg
            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
//                loading.setVisibility(View.GONE);
                showBarComplete(getString(R.string.error_msg));
                onDestroy();
            }
        });
    }


    @Override
    public void compressDone(boolean isDone, int requestCode) {

        if (requestCode == COMPRESS_VIDEO_CODE) {
            storyThumbPath = Constant.createOutputPath(StoryPostuploadService.this,
                    ".png");
            isCompressed = isDone;
            Constant.createThumbnail(storycompressedVideo, storyThumbPath, this, CREATE_THUMB);
        } else {
            isThumb = isDone;
            if (isThumb) {
                uploadPost(storyThumbPath, false, true);
            } else {
                if (isStoryMultiple) {
                    postNow();
                } else if (isCompressed) {
                    uploadPost(storyThumbPath, true, false);
                } else {
                    uploadPost(storyMediaPath, true, false);
                }
            }
        }
    }

    @Override
    public void getUploadedMedia() {
        System.out.println("==UPLOADED DATA ");
    }

    @Override
    public void getUploadedMedia(int requestCode, ArrayList<String> mediaUrlList, boolean isImage, List<PictureFacer> localMedia) {

        if (isImage) {
            multipleMedia = mediaUrlList;
            System.out.println("==UPLOADED DATA " + multipleMedia);
            postNow();
        } else {
            multipleMedia = mediaUrlList;
            Log.e("UPLOADED DATA ", multipleMedia + storyThumbPath);
            uploadPost(storyThumbPath, false, true);
//            postNow();
//            System.out.println("==UPLOADED DATA " + multipleMedia);
//            thumbnailImage = Constant.createOutputPath(NewPostUploadService.this, ".png");
//            Constant.createThumbnail(mediaPath, thumbnailImage, NewPostUploadService.this, CREATE_THUMB);
        }

//            Intent it = new Intent(mActivity, NewPostUploadActivity.class);
//            it.putStringArrayListExtra("multipleMedia", mediaUrlList);
//            it.putExtra("mediaPath", localMedia.get(0).getPicturePath());
//            it.putExtra("isVideo", !isImage);
//            it.putExtra("isMultiple", true);
//            startActivity(it);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
