package com.meest.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Activities.VideoPost.VideoPostPrivacySetting;
import com.meest.Activities.VideoPost.VideoPostVisibilitySetting;
import com.meest.R;
import com.meest.responses.VideoPostResponse;
import com.meest.responses.VideoUploadResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.bumptech.glide.Glide;

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


public class ConfirmVideoActivity extends AppCompatActivity implements View.OnClickListener {

    TextView privacy_settings, video_post_visibility, post_video, draft_video, discard;
    String postVisibility = getString(R.string.Public);
    String videoUri;
    EditText description;
    ImageView image_back, thumbnail;
    File videoFile;
    boolean allowComments = true, allowDuet = true, allowDownload = true;
    LottieAnimationView loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_video);

        findIds();

        if (getIntent().getExtras() != null) {
            videoUri = getIntent().getExtras().getString("video_path");
            videoFile = new File(videoUri);

            if (!videoFile.exists()) {
                Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later),R.color.msg_fail);
                finish();
            }

            // setting thumbnail
            Uri uri = Uri.fromFile(new File(videoUri));
            Glide.with(this).
                    load(uri).
                    thumbnail(0.1f).
                    into(thumbnail);
        }

        loading.setAnimation("loading.json");
        loading.loop(true);

        video_post_visibility.setText(postVisibility);

        discard.setOnClickListener(this);
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

                startActivityForResult(intent, 2);
                break;
            case R.id.acv_video_post_visibility:
                intent = new Intent(this, VideoPostVisibilitySetting.class);
                intent.putExtra("postVisibility", postVisibility);

                startActivityForResult(intent, 1);
                break;
            case R.id.acv_video_post:
                Map<String, String> header = new HashMap<>();
                header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));

                String descriptionText = description.getText().toString();
                List<String> hashTags = new ArrayList<>();
                hashTags.add("#1st");
                hashTags.add("#2nd");

                File videoFile = new File(videoUri);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), videoFile.getPath());
                MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", videoFile.getName(), requestFile);

                loading.playAnimation();
                loading.setVisibility(View.VISIBLE);

                // posting video
                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<VideoUploadResponse> call = webApi.uploadVideo(header, multipartBody);
                call.enqueue(new Callback<VideoUploadResponse>() {
                    @Override
                    public void onResponse(Call<VideoUploadResponse> call, Response<VideoUploadResponse> response) {
                        if (response.code() == 200 && response.body().getSuccess()) {
                            // now posting video

                            // adding extra header for body data
                            header.put("Accept", "application/json");
                            header.put("Content-Type", "application/json");

                            HashMap<String, Object> body = new HashMap<>();
                            body.put("videoURL", response.body().getData());
                            body.put("description", descriptionText);
                            body.put("hashtags", hashTags);

                            Call<VideoPostResponse> call2 = webApi.postVideo(header, body);
                            call2.enqueue(new Callback<VideoPostResponse>() {
                                @Override
                                public void onResponse(Call<VideoPostResponse> call, Response<VideoPostResponse> response) {
                                    if (response.code() == 200 && response.body().getSuccess()) {
                                        Utilss.showToast(ConfirmVideoActivity.this, getResources().getString(R.string.Video_posted_successfully), R.color.green);
//                                        Toast.makeText(ConfirmVideoActivity.this, getResources().getString(R.string.Video_posted_successfully), Toast.LENGTH_SHORT).show();
                                        loading.cancelAnimation();
                                        loading.setVisibility(View.GONE);
                                    } else {
                                        showError();
                                    }
                                }

                                @Override
                                public void onFailure(Call<VideoPostResponse> call, Throwable t) {
                                    showError();
                                }
                            });
                        } else {
                            showError();
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoUploadResponse> call, Throwable t) {
                        showError();
                    }
                });
                break;
            case R.id.acv_video_draft:
                // drafting video
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

    private void showError() {
        Utilss.showToast(ConfirmVideoActivity.this, getString(R.string.error_msg), R.color.grey);
        loading.cancelAnimation();
        loading.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                postVisibility = data.getExtras().getString("postVisibility");
                video_post_visibility.setText(postVisibility);
            }
        } else if (requestCode == 2) {
            if (data != null) {
                allowComments = data.getExtras().getBoolean("allowComments");
                allowDuet = data.getExtras().getBoolean("allowDuet");
                allowDownload = data.getExtras().getBoolean("allowDownload");
            }
        }
    }
}
