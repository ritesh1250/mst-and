package com.meest.TopAndTrends;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.R;
import com.meest.responses.PostHashTagResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TopHashtagActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RelativeLayout backLayout;
    String hashtag;
    TextView hashtagText, postCount;
    LottieAnimationView loadingProgress;
    private TextView not_found;
    PostHashTagResponse hashtagePostResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hashtag_activity);
        findIds();
        if (getIntent().getExtras() == null) {
            Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            finish();
        }
        hashtag = getIntent().getExtras().getString("hashtag", "");


        hashtagText.setText(hashtag);

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
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");

        HashMap<String, String> body = new HashMap<>();
        body.put("key", hashtag);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<PostHashTagResponse> apiCall = webApi.searchPostByHashtag(header, body);
        apiCall.enqueue(new Callback<PostHashTagResponse>() {
            @Override
            public void onResponse(Call<PostHashTagResponse> call, Response<PostHashTagResponse> response) {
                loadingProgress.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getSuccess()) {
                    hashtagePostResponse = response.body();
                    Log.e("hashTag", hashtagePostResponse.toString());
                    bindData();
                } else {
                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<PostHashTagResponse> call, Throwable t) {
                loadingProgress.setVisibility(View.GONE);
                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                Log.i("SE_MESSAGE", t.getMessage());
            }
        });
    }

    private void bindData() {
        if (hashtagePostResponse == null || hashtagePostResponse.getData() == null || hashtagePostResponse.getData().getRows().size() == 0) {
            not_found.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }
        not_found.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        String postCountData = hashtagePostResponse.getData().getCount() < 100 ? "Fewer than 100 posts" : (hashtagePostResponse.getData().getCount() + " posts");
        postCount.setText(postCountData);

        HashtagPostAdapter hashtagPostAdapter = new HashtagPostAdapter(this, hashtagePostResponse.getData().getRows(), true, "");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(hashtagPostAdapter);
    }

    private void findIds() {
        not_found = findViewById(R.id.hash_no_data);
        loadingProgress = findViewById(R.id.loading);
        hashtagText = findViewById(R.id.ha_hashtag);
        postCount = findViewById(R.id.ha_postCount);
        recyclerView = findViewById(R.id.ha_recycler);
        backLayout = findViewById(R.id.backLayout);
    }
}