package com.meest.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Activities.VideoPost.CameraActivity;
import com.meest.MainActivity;
import com.meest.Paramaters.TextPostUploadParam;
import com.meest.R;
import com.meest.utils.MediaUploadInterface;
import com.meest.utils.Permission;
import com.meest.mediapikcer.utils.PictureFacer;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.register.fragment.profile.model.UploadimageResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.model.StoryDataModel;


import org.jetbrains.annotations.NotNull;

import java.io.File;
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


public class CreateStoryActivity extends AppCompatActivity implements MediaUploadInterface {

    LinearLayout layout_text, layout_photo,
            layout_video;
    int PICK_MEDIA_CODE = 10;
    ImageView img_close;
    public static final int PERMISSION_CODE = 123;
    public static final int CAMERA_PERMISSION_CODE = 124;
    public static final int VIDEO_PERMISSION_CODE = 125;
    LottieAnimationView loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_status_activity);
        findIds();
        loading.setAnimation("loading.json");
        loading.playAnimation();
        loading.loop(true);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layout_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateStoryActivity.this, WriteTextActivity.class);
                startActivity(intent);
            }
        });

        layout_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(CreateStoryActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(CreateStoryActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(CreateStoryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateStoryActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);

                } else {
                    ActivityCompat.requestPermissions(CreateStoryActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
                }
            }
        });

        layout_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ContextCompat.checkSelfPermission(CreateStoryActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(CreateStoryActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(CreateStoryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                       ActivityCompat.requestPermissions(CreateStoryActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, VIDEO_PERMISSION_CODE);

                } else {
                    ActivityCompat.requestPermissions(CreateStoryActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, VIDEO_PERMISSION_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_MEDIA_CODE && resultCode == RESULT_OK) {
            if (data == null || data.getData() == null) {
                Utilss.showToast(this, "Something went wrong, please try again later",
                        R.color.msg_fail);
                return;
            }
            Uri mediaUri = data.getData();
            String selectedMediaPath = Constant.getPath(CreateStoryActivity.this, mediaUri);

            if (mediaUri.toString().contains("image")) {
//                uploadStory(selectedMediaPath);
            } else {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(CreateStoryActivity.this, mediaUri);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                if (time != null) {
                    long timeInMillisec = Long.parseLong(time);

                    if (timeInMillisec > 60000) {
//                        uploadVideoStory(selectedMediaPath);
                    } else {
                        Utilss.showToast(this, "Something went wrong, please try again later",
                                R.color.msg_fail);
                    }
                } else {
                    Utilss.showToast(this, "Please select video of less than 1 min length",
                            R.color.msg_fail);
                }
                retriever.release();
            }

        }
    }


    private void postNow(String imageUrl) {
        List<String> hashTagList = new ArrayList<>();

        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(CreateStoryActivity.this, SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        StoryDataModel storyDataModel = new StoryDataModel();
        TextPostUploadParam.Location loc = new TextPostUploadParam.Location();
        storyDataModel.setLocation(loc);
        loc.setLat(0.0);
        loc.setLong(0.0);
        storyDataModel.setHashTags(hashTagList);
        storyDataModel.setStory(imageUrl);
        storyDataModel.setStatus(true);
        storyDataModel.setImage(1);

        Call<ApiResponse> call = webApi.insertStory(map, storyDataModel);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                try {
                    if (response.code() == 200 && response.body().getSuccess()) {
                        loading.setVisibility(View.GONE);

                        Utilss.showToast(CreateStoryActivity.this, "Story posted successfully", R.color.green);
                        Intent intent = new Intent(CreateStoryActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        loading.setVisibility(View.GONE);
                        Utilss.showToast(CreateStoryActivity.this, "Server Error", Toast.LENGTH_LONG);
                    }
                } catch (Exception e) {
                    loading.setVisibility(View.GONE);
                    e.printStackTrace();
                    Utilss.showToast(CreateStoryActivity.this, "Server Error", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                loading.setVisibility(View.GONE);
                Utilss.showToast(CreateStoryActivity.this, "Server Error", Toast.LENGTH_LONG);
            }
        });
    }

    // uploading story
    private void uploadVideoStory(String path) {
        loading.setVisibility(View.VISIBLE);

        // uploading media first
        File videoFile = new File(path);
        RequestBody requestFile;
        MultipartBody.Part body;
        requestFile = RequestBody.create(MediaType.parse("video/*"), videoFile);
        body = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFile);

        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<UploadimageResponse> call = webApi.uploadImageRequest(map, body);

        call.enqueue(new Callback<UploadimageResponse>() {
            @Override
            public void onResponse(Call<UploadimageResponse> call, Response<UploadimageResponse> response) {
                if (response.code() == 200 && response.body().getSuccess()) {
                    postVideoNow(response.body().getData().getUrl());
                } else {
                    loading.setVisibility(View.GONE);
                    Utilss.showToast(CreateStoryActivity.this, "Server Error", R.color.msg_fail);
                }
            }

            @Override
            public void onFailure(Call<UploadimageResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Utilss.showToast(CreateStoryActivity.this, "Server Error", R.color.msg_fail);
            }
        });
    }

    private void postVideoNow(String videoUrl) {
        List<String> hashTagList = new ArrayList<>();

        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(CreateStoryActivity.this, SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);

        StoryDataModel storyDataModel = new StoryDataModel();
        TextPostUploadParam.Location loc = new TextPostUploadParam.Location();
        storyDataModel.setLocation(loc);
        loc.setLat(0.0);
        loc.setLong(0.0);
        storyDataModel.setHashTags(hashTagList);
        storyDataModel.setStory(videoUrl);
        storyDataModel.setStatus(true);
        storyDataModel.setImage(0);

        Call<ApiResponse> call = webApi.insertStory(map, storyDataModel);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                try {
                    if (response.code() == 200 && response.body().getSuccess()) {
                        loading.setVisibility(View.GONE);

                        Utilss.showToast(CreateStoryActivity.this, "Story posted successfully", R.color.green);
                        Intent intent = new Intent(CreateStoryActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        loading.setVisibility(View.GONE);
                        Utilss.showToast(CreateStoryActivity.this, "Server Error", Toast.LENGTH_LONG);
                    }
                } catch (Exception e) {
                    loading.setVisibility(View.GONE);
                    e.printStackTrace();
                    Utilss.showToast(CreateStoryActivity.this, "Server Error", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                loading.setVisibility(View.GONE);
                Utilss.showToast(CreateStoryActivity.this, "Server Error", Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == VIDEO_PERMISSION_CODE){
            if (Permission.hasPermissions(CreateStoryActivity.this, permissions)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(CreateStoryActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(CreateStoryActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(CreateStoryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        // Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(CreateStoryActivity.this, CameraActivity.class);
                        intent.putExtra("type", "story");
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied ", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        else if (requestCode == CAMERA_PERMISSION_CODE){
            if (Permission.hasPermissions(CreateStoryActivity.this, permissions)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(CreateStoryActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(CreateStoryActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(CreateStoryActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        // Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(CreateStoryActivity.this, CreateAPostActivityFinal.class);
                        intent.putExtra("fragmentType", "remove");
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied ", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    private void findIds() {
        layout_text = findViewById(R.id.layout_text);
        layout_photo = findViewById(R.id.layout_photo);
        layout_video = findViewById(R.id.layout_video);
        loading = findViewById(R.id.loading);
        img_close = findViewById(R.id.img_close);
    }

    @Override
    public void getUploadedMedia() {
        loading.setVisibility(View.GONE);
        Toast.makeText(this, "Story uploaded successfully", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void getUploadedMedia(int requestCode, ArrayList<String> mediaUrlList, boolean isImage, List<PictureFacer> localMedia) {

    }


}
