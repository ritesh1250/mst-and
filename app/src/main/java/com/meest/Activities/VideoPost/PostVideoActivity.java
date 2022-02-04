package com.meest.Activities.VideoPost;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Interfaces.CompressMedia;
import com.meest.R;
import com.meest.meestbhart.register.fragment.profile.model.UploadimageResponse;
import com.meest.responses.VideoPostResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostVideoActivity extends AppCompatActivity implements View.OnClickListener,
        CompressMedia {

    TextView privacy_settings, video_post_visibility, post_video, draft_video, discard;
    String postVisibility = getString(R.string.Public);
    String videoPath;
    EmojiconEditText description;
    ImageView image_back, thumbnail;
    boolean allowComments = true, allowDuet = true, allowDownload = true,
            allowTrio = true, isDraft = false, isCompressed = false, isThumb = false;
    LottieAnimationView loading;
    String descriptionText;
    WebApi webApi;
    String type, audioId;
    Map<String, String> header;
    ArrayList<String> hashtagList = new ArrayList<>();
    String compressedVideo, thumbnailImage;
    private final int COMPRESS_VIDEO_CODE = 112, CREATE_THUMB = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_video_activity);

        if (getIntent().getExtras() == null) {
            Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later),
                    R.color.msg_fail);
            finish();
        }

        findIds();

        videoPath = getIntent().getExtras().getString("video_path", "");
        audioId = getIntent().getStringExtra("audioId");
        type = getIntent().getExtras().getString("type", "");

        if (!new File(videoPath).exists()) {
            Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later),
                    R.color.msg_fail);
            finish();
        }

        webApi = ApiUtils.getClient().create(WebApi.class);
        header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));

        // setting thumbnail
        Uri uri = Uri.fromFile(new File(videoPath));
        Glide.with(this).
                load(uri).
                thumbnail(0.1f).
                into(thumbnail);

        loading.setAnimation("loading.json");
        loading.loop(true);

        video_post_visibility.setText(postVisibility);

        discard.setOnClickListener(this);
        image_back.setOnClickListener(this);
        post_video.setOnClickListener(this);
        draft_video.setOnClickListener(this);
        privacy_settings.setOnClickListener(this);
        video_post_visibility.setOnClickListener(this);
    }

    private void findIds() {
        privacy_settings = findViewById(R.id.acv_privacy_settings);
        post_video = findViewById(R.id.acv_video_post);
        draft_video = findViewById(R.id.acv_video_draft);
        video_post_visibility = findViewById(R.id.acv_video_post_visibility);
        description = findViewById(R.id.acv_description);
        image_back = findViewById(R.id.image_back);
        thumbnail = findViewById(R.id.acv_thumbnail);
        discard = findViewById(R.id.acv_discard);
        loading = findViewById(R.id.loading);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.acv_privacy_settings:
                intent = new Intent(this, VideoPostPrivacySetting.class);
                intent.putExtra("allowComments", allowComments);
                intent.putExtra("allowDuet", allowDuet);
                intent.putExtra("allowDownload", allowDownload);
                intent.putExtra("allowTrio", allowTrio);

                startActivityForResult(intent, 2);
                break;
            case R.id.acv_video_post_visibility:
                intent = new Intent(this, VideoPostVisibilitySetting.class);
                intent.putExtra("postVisibility", postVisibility);

                startActivityForResult(intent, 1);
                break;
            case R.id.acv_video_post:
                isDraft = false;
                compressVideoPost();
                break;
            case R.id.acv_video_draft:
                // drafting video
                isDraft = true;
                compressVideoPost();
                break;
            case R.id.image_back:
                onBackPressed();
                break;
            case R.id.acv_discard:
                Intent i = new Intent();
                i.putExtra("discard", true);
                setResult(RESULT_OK, i);
                finish();
                break;
        }
    }

    private void compressVideoPost() {
        showPrd();
        compressedVideo = Constant.createOutputPath(PostVideoActivity.this,
                videoPath.substring(videoPath.lastIndexOf(".")));
        Constant.compressVideo(videoPath, compressedVideo, this, COMPRESS_VIDEO_CODE);
    }

    private void showError(String a) {
        Utilss.showToast(PostVideoActivity.this, getString(R.string.Server_Error) + a, R.color.msg_fail);
        hidePrd();
    }

    private void showError() {
        Utilss.showToast(PostVideoActivity.this, getString(R.string.Server_Error), R.color.msg_fail);
        hidePrd();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null && data.getExtras() != null) {
            postVisibility = data.getExtras().getString("postVisibility");
            video_post_visibility.setText(postVisibility);
        } else if (requestCode == 2 && data != null && data.getExtras() != null) {
            allowComments = data.getExtras().getBoolean("allowComments");
            allowDuet = data.getExtras().getBoolean("allowDuet");
            allowDownload = data.getExtras().getBoolean("allowDownload");
            allowTrio = data.getExtras().getBoolean("allowTrio");
        }
    }

    private void uploadCreatedVideo(String videoPath, boolean isVideo) {
        File file = new File(videoPath);

        RequestBody requestFile;
        MultipartBody.Part body;
        if (isVideo) {
            requestFile = RequestBody.create(MediaType.parse("video/*"), file);
            body = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
        } else {
            requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }

        // posting video
//        Call<VideoUploadResponse> call = webApi.uploadVideo(header, fileToUpload);
        Call<UploadimageResponse> call = webApi.uploadImageRequest(header, body);
        call.enqueue(new Callback<UploadimageResponse>() {
            @Override
            public void onResponse(Call<UploadimageResponse> call, Response<UploadimageResponse> response) {
                if (response.code() == 200 && response.body().getSuccess()) {

                    if(isVideo) {
                        // header for form data
                        header.put("Accept", "application/json");
                        header.put("Content-Type", "application/json");

                        // creating data that need to be sent
                        descriptionText = description.getText().toString();

                        // extracting hashtags from text
                        hashtagList.clear();
                        String regexPattern = "(#\\w+)";

                        Pattern p = Pattern.compile(regexPattern);
                        Matcher m = p.matcher(descriptionText);
                        while (m.find()) {
                            String hashtag = m.group(1);
                            hashtagList.add(hashtag);
                        }

                        postNow(response.body().getData().getUrl());
                    } else {
                        thumbnailImage = response.body().getData().getUrl();
                        if(isCompressed) {
                            uploadCreatedVideo(compressedVideo, true);
                        } else {
                            uploadCreatedVideo(videoPath, true);
                        }
                    }
                } else {
                    showError(getString(R.string.error_msg));
                }
            }

            @Override
            public void onFailure(Call<UploadimageResponse> call, Throwable t) {
                showError(getString(R.string.error_msg));
            }
        });
    }

    private void postNow(String uploadVideoUrl) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("videoURL", uploadVideoUrl);
        String test;
        try {
            test = URLDecoder.decode(descriptionText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            test = descriptionText;
        }
        body.put("description", test);
        body.put("hashtags", hashtagList);
        body.put("isAllowComment", allowComments);
        if (type.equals("duo") || type.equals("trio")) {
            body.put("isAllowTrio", false);
            body.put("isAllowDuet", false);
        } else {
            body.put("isAllowTrio", allowTrio);
            body.put("isAllowDuet", allowDuet);
        }
        body.put("isAllowDownload", allowDownload);
        body.put("access", postVisibility);
        body.put("thumbnail", thumbnailImage);
        body.put("audio_file", audioId);

        if (isDraft) {
            body.put("access", "Draft");
            body.put("isPrivate", true);
        } else {
            body.put("isPrivate", false);
        }

        String data = new Gson().toJson(body);

        Log.v("harsh", "body == " + data);

        Call<VideoPostResponse> call2 = webApi.postVideo(header, body);
        call2.enqueue(new Callback<VideoPostResponse>() {
            @Override
            public void onResponse(Call<VideoPostResponse> call, Response<VideoPostResponse> response) {
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    if (isDraft) {
                        Utilss.showToast(PostVideoActivity.this, getString(R.string.Video_added_to_draft_successfully), R.color.green);
                    } else {
                       // Utilss.showToast(PostVideoActivity.this, getString(R.string.Video_posted_successfully), R.color.green);
                        Toast.makeText(PostVideoActivity.this, getResources().getString(R.string.Video_posted_successfully), Toast.LENGTH_SHORT).show();

                    }
                    hidePrd();
                    onBackPressed();
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<VideoPostResponse> call, Throwable t) {
                showError();
            }
        });
    }

    private void showPrd() {
        loading.playAnimation();
        loading.setVisibility(View.VISIBLE);
    }

    private void hidePrd() {
        loading.cancelAnimation();
        loading.setVisibility(View.GONE);
    }

    @Override
    public void compressDone(boolean isDone, int requestCode) {
        if (requestCode == COMPRESS_VIDEO_CODE) {
            thumbnailImage = Constant.createOutputPath(PostVideoActivity.this, ".png");
            isCompressed = isDone;
            Constant.createThumbnail(compressedVideo, thumbnailImage, this, CREATE_THUMB);
        } else {
            isThumb = isDone;
            if (isThumb) {
                uploadCreatedVideo(thumbnailImage, false);
            } else {
                if(isCompressed) {
                    uploadCreatedVideo(compressedVideo, true);
                } else {
                    uploadCreatedVideo(videoPath, true);
                }
            }
        }
    }
}
