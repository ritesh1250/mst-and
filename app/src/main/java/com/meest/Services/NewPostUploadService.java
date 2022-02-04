package com.meest.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.meest.Fragments.HomeFragments;
import com.meest.Interfaces.CompressMedia;
import com.meest.Paramaters.TextPostUploadParam;
import com.meest.R;
import com.meest.responses.FeedResponse;
import com.meest.utils.MediaUploadInterface;
import com.meest.utils.UploadFilesToServer;
import com.meest.mediapikcer.utils.PictureFacer;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.register.fragment.profile.model.UploadimageResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewPostUploadService extends Service implements CompressMedia, MediaUploadInterface {

    private static final int COMPRESS_VIDEO_CODE = 712;
    private static final int CREATE_THUMB = 232;
    String mediaPath;
    ArrayList<String> multipleMedia;
    List<PictureFacer> selectedItem;
    int recode = 0;
    boolean isVideo = false,  isPost = false, isMultiple;
    Switch switch_map;
    String postText;
    String address_name;
    String mediaUrl;
    String postVisibility;
    String selectedLocation;
    boolean allowComment = true;
    boolean sensitiveContent = false;
    ArrayList<String> hashtagList = new ArrayList<>();
    public static List<FeedResponse.UserTags> taggedUsername = new ArrayList<>();
    String postType, type, userTag;
    File imageFile;
    String thumbnailImage, compressedVideo;
    RelativeLayout view_layout_cat;
    AutoCompleteTextView tvAutoComplete;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyDfcsomOyYAs3BXBUD58CpE0WulwtSz8Ms";
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

    public NewPostUploadService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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

        notificationManager = NotificationManagerCompat.from(NewPostUploadService.this);
        Gson gson = new Gson();
        Type typeTaguser = new TypeToken<List<FeedResponse.UserTags>>() {
        }.getType();

        isMultiple = intent.getExtras().getBoolean("isMultiple", false);
        mediaPath = intent.getExtras().getString("mediaPath");
        postVisibility = intent.getExtras().getString("postVisibility");
        thumbnailImage = intent.getExtras().getString("thumbPath");
        isVideo = intent.getExtras().getBoolean("isVideo", false);

        isPost = intent.getExtras().getBoolean("isPost", false);
        allowComment = intent.getExtras().getBoolean("allowComment", false);
        sensitiveContent = intent.getBooleanExtra("sensitiveContent", false);
        type = intent.getStringExtra("type");
        userTag = intent.getStringExtra("taggedUsername");

        taggedUsername = gson.fromJson(userTag, typeTaguser);
        postText = intent.getStringExtra("postText");
        post_lat = intent.getDoubleExtra("post_lat", 0.0d);
        post_long = intent.getDoubleExtra("post_long", 0.0d);
        hashtagList = intent.getStringArrayListExtra("hashtagList");

//        taggedUsername=(List<FeedResponse.UserTags>)intent.getSerializableExtra("taggedUsername");
        if (type != null && type.equalsIgnoreCase("location")) {
            if (type.equalsIgnoreCase("location")) {
                latitude = intent.getStringExtra("lat");
                longitude = intent.getStringExtra("log");
                address_name = intent.getStringExtra("address_name");
            }
        }

        if (isMultiple) {
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

    private void storyUpload() {
        if (isVideo) {
            UploadFilesToServer asyncTask = new UploadFilesToServer(selectedItem, NewPostUploadService.this, this, recode, false);
            asyncTask.startMediaUpload();
        } else {
            UploadFilesToServer asyncTask = new UploadFilesToServer(selectedItem, NewPostUploadService.this, this, recode, true);
            asyncTask.startMediaUpload();
        }

    }

    public void postFeed() {
        showBar();
        if (isMultiple) {

            if (isVideo) {
                UploadFilesToServer asyncTask = new UploadFilesToServer(selectedItem, NewPostUploadService.this, this, recode, false);
                asyncTask.startMediaUpload();
            } else {
                UploadFilesToServer asyncTask = new UploadFilesToServer(selectedItem, NewPostUploadService.this, this, recode, true);
                asyncTask.startMediaUpload();
            }

        } else if (isVideo) {
            isCompressed = true;
            uploadPost(thumbnailImage, false, true);
        } else if (type == null ) {
            File chkFileSize = new File(mediaPath);
            long fileSizeInBytes = chkFileSize.length();
            long fileSizeInKB = fileSizeInBytes / 1024;
//                    long fileSizeInMB = fileSizeInKB / 1024;

            if (fileSizeInKB > 500) {
                mediaPath = Constant.compressImage(NewPostUploadService.this, mediaPath);
                uploadPost(mediaPath, false, false);

            } else {
                uploadPost(mediaPath, false, false);
            }
        }
         else {
            mediaUrl = mediaPath;
            postNow();
        }
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
                            thumbnailImage = response.body().getData().getUrl();
                            if (isMultiple) {
                                postNow();

                            } else if (isCompressed) {
                                uploadPost(mediaPath, true, false);
                            }
//                            else {
//                                uploadPost(mediaPath, true, false);
//                            }
                        } else {
                            mediaUrl = response.body().getData().getUrl();
//                            Log.e("Video upload done", mediaUrl);
                            postNow();
                        }
                    }  else {
                        showBarComplete(getResources().getString(R.string.error_msg));
//                        loading.setVisibility(View.GONE);
                        mediaUrl = "";
//                        Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showBarComplete(getResources().getString(R.string.error_msg));

//                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    mediaUrl = "";
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
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);

        // extracting hashtags from text
        TextPostUploadParam textPostUploadParam = new TextPostUploadParam();
        TextPostUploadParam.PostDatum postArray = new TextPostUploadParam.PostDatum();
        TextPostUploadParam.Location loc = new TextPostUploadParam.Location();
        try {
            if (isMultiple) {
                List<TextPostUploadParam.PostDatum> postDatumList = new ArrayList<>();
                for (String uploadUrl : multipleMedia) {
                    TextPostUploadParam.PostDatum mediaData = new TextPostUploadParam.PostDatum();
                    if (isVideo) {
                        mediaData.setVideo(1);
                        mediaData.setImage(0);
                    } else {
                        mediaData.setVideo(0);
                        mediaData.setImage(1);
                    }
                    mediaData.setIsText(0);
                    mediaData.setPost(uploadUrl);
                    postDatumList.add(mediaData);
                }

//                if (multipleMedia != null && multipleMedia.size() > 0) {
//                    thumbnailImage =
//                    .get(0);
//                }
                textPostUploadParam.setThumbnail(thumbnailImage);
                textPostUploadParam.setPostData(postDatumList);
            } else if (isVideo) {
                postArray.setVideo(1);
                postArray.setImage(0);
                postArray.setIsText(0);
                textPostUploadParam.setThumbnail(thumbnailImage);
                postArray.setPost(mediaUrl);
                textPostUploadParam.setPostData(Collections.singletonList(postArray));
            } else {
                postArray.setImage(1);
                postArray.setIsText(0);
                postArray.setVideo(0);
                textPostUploadParam.setThumbnail(mediaUrl);
                postArray.setPost(mediaUrl);
                textPostUploadParam.setPostData(Collections.singletonList(postArray));
            }

            if (type != null && type.equalsIgnoreCase("location")) {
                loc.setLat(Double.parseDouble(latitude));
                loc.setLong(Double.parseDouble(longitude));
            } else {
                if (post_lat != 0.0 && post_long != 0.0) {
                    loc.setLat(post_lat);
                    loc.setLong(post_long);
                } else {
                    loc.setLat(0.0);
                    loc.setLong(0.0);
                }
            }

            textPostUploadParam.setLocation(loc);
            textPostUploadParam.setHashTags(hashtagList);
            textPostUploadParam.setAllowComment(allowComment);
            textPostUploadParam.setSensitiveContent(sensitiveContent);
            textPostUploadParam.setCaption(postText);
            if (taggedUsername.size() > 0) {
                textPostUploadParam.setTaggedPersons(taggedUsername);
            }
            textPostUploadParam.setPostType("feedPost");
            textPostUploadParam.setViewPost(postVisibility);
            if (selectedLocation != null && selectedLocation.length() > 0) {
                textPostUploadParam.setTagLocation(selectedLocation);
            }
        } catch (JsonIOException e) {
            showBarComplete(getResources().getString(R.string.error_msg));
            e.printStackTrace();
        }

        Call<ApiResponse> call = webApi.InsertTextPost(map, textPostUploadParam);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                try {
                    if (response.code() == 200 && response.body().getSuccess()) {
//                        loading.setVisibility(View.GONE);
                        if (isVideo) {
                            showBarComplete(getResources().getString(R.string.Posted_successfully));
                            Utilss.showToast(getApplicationContext(), getResources().getString(R.string.Posted_successfully), R.color.green);
//                            HomeFragments.getInstanceHomeFragment().onRetry();
                            Intent local = new Intent();
                            local.putExtra("status", "ok");
                            local.setAction("post_upload");
                            sendBroadcast(local);
                            onDestroy();
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showBarComplete(getResources().getString(R.string.Posted_successfully));
                                    Utilss.showToast(getApplicationContext(), getResources().getString(R.string.Posted_successfully), R.color.green);
//                                    HomeFragments.getInstanceHomeFragment().onRetry();
                                    Intent local = new Intent();
                                    local.putExtra("status", "ok");
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
            thumbnailImage = Constant.createOutputPath(NewPostUploadService.this,
                    ".png");
            isCompressed = isDone;
            Constant.createThumbnail(compressedVideo, thumbnailImage, this, CREATE_THUMB);
        } else {
            isThumb = isDone;
            if (isThumb) {
                uploadPost(thumbnailImage, false, true);
            } else {
                if (isMultiple) {
                    postNow();
                } else if (isCompressed) {
                    uploadPost(compressedVideo, true, false);
                } else {
                    uploadPost(mediaPath, true, false);
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
            thumbnailImage=multipleMedia.get(0);
            System.out.println("==UPLOADED DATA " + multipleMedia);
            postNow();
        } else {
            multipleMedia = mediaUrlList;
            Log.e("UPLOADED DATA ", multipleMedia + thumbnailImage);
            uploadPost(thumbnailImage, false, true);
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
}