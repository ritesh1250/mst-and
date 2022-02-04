package com.meest.svs.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.R;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.svs.adapters.HashtagVideosAdapter;
import com.meest.svs.models.MusicVideoResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MusicVideosActivity extends AppCompatActivity {

    ImageView ivHeartFavLike, ivHeartFavDisLike;
    RecyclerView recyclerView;
    RelativeLayout backLayout;
    String audioId, type, trioVideo1 = "", audioName;
    TextView hashtag, postCount, circleViewText, favText, hashtagText;
    LottieAnimationView loadingProgress;
    LinearLayout haFavLayout;
    boolean isHashtag = true;
    ImageView favIcon;
    MusicVideoResponse musicVideoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hashtag_activity);

        if (getIntent().getExtras() == null) {
            Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            finish();
        }
        audioId = getIntent().getExtras().getString("audioId", "");
        type = getIntent().getExtras().getString("type", "");
        audioName = getIntent().getExtras().getString("audioName", "");

        if (type.equalsIgnoreCase("trio")) {
            isHashtag = false;
            trioVideo1 = getIntent().getExtras().getString("trioVideo1");
        }

        findIds();

        hashtag.setText(audioName);
        hashtagText.setText(getResources().getString(R.string.select_audio));
        circleViewText.setText("");
        circleViewText.setBackgroundResource(R.drawable.music_white_icon);

        loadingProgress.setAnimation("loading.json");
        loadingProgress.playAnimation();
        loadingProgress.loop(true);

        fetchData();

        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void fetchData() {
        loadingProgress.setVisibility(View.VISIBLE);

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");

        HashMap<String, Object> body = new HashMap<>();
        body.put("audioId", audioId);
        body.put("svs", true);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<MusicVideoResponse> apiCall = webApi.searchVideoByAudio(header, body);
        apiCall.enqueue(new Callback<MusicVideoResponse>() {
            @Override
            public void onResponse(Call<MusicVideoResponse> call, Response<MusicVideoResponse> response) {
                loadingProgress.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getSuccess()) {
                    musicVideoResponse = response.body();
                    bindData();
                } else {
                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<MusicVideoResponse> call, Throwable t) {
                loadingProgress.setVisibility(View.GONE);
                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void bindData() {
        String postCountData = musicVideoResponse.getData().getCount() < 100 ?
                "Fewer than 100 posts" : (musicVideoResponse.getData().getCount() + " posts");
        postCount.setText(postCountData);

        HashtagVideosAdapter hashtagVideosAdapter = new HashtagVideosAdapter(this,
                musicVideoResponse.getData().getRows(), isHashtag, trioVideo1);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(hashtagVideosAdapter);
    }

    private void findIds() {
        ivHeartFavDisLike = findViewById(R.id.ivHeartFavDisLike);
        ivHeartFavLike = findViewById(R.id.ivHeartFavLike);
        loadingProgress = findViewById(R.id.loading);
        hashtag = findViewById(R.id.ha_hashtag);
        postCount = findViewById(R.id.ha_postCount);
        recyclerView = findViewById(R.id.ha_recycler);
        backLayout = findViewById(R.id.backLayout);
        circleViewText = findViewById(R.id.circle_view_text);
        haFavLayout = findViewById(R.id.ha_fav_layout);
        favIcon = findViewById(R.id.ha_fav_icon);
        favText = findViewById(R.id.ha_fav_text);
        hashtagText = findViewById(R.id.ha_hashtag_text);
    }
}