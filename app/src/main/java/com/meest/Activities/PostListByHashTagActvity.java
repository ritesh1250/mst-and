package com.meest.Activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.airbnb.lottie.LottieAnimationView;
import com.meest.R;
import com.meest.responses.HashtagSearchResponse;
import com.meest.utils.goLiveUtils.CommonUtils;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.svs.adapters.HashtagDataAdapter;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostListByHashTagActvity extends AppCompatActivity {


    private Context context;
    private RecyclerView recyclerView;
    private HashtagSearchResponse searchResponse;
    private TextView not_found,tv_hashtag;
    private ImageView back_arrow;
    LottieAnimationView loading;


    public static boolean isLastPage = false;

    private  String data;
    private  int last_color_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list_by_hash_tag_actvity);

        context=this;
        recyclerView = findViewById(R.id.hashtag_recycleView);
        not_found = findViewById(R.id.not_found);
        tv_hashtag = findViewById(R.id.tv_hashtag);
        back_arrow = findViewById(R.id.back_arrow);
        not_found.setText(getString(R.string.Hashtag_not_found));
        loading = findViewById(R.id.loading);

        loading.setAnimation(getString(R.string.loading_json));
        loading.playAnimation();
        loading.loop(true);

        data = getIntent().getExtras().getString("hash_tag_name", "");
        last_color_code=getIntent().getExtras().getInt("last_color_code",R.color.gray);


        tv_hashtag.setText(data);
        tv_hashtag.setBackground(CommonUtils.changeDrawableColor(context,R.drawable.hashtaground,last_color_code));

        if (ConnectionUtils.isConnected(this))
            fetchData();
        else
            Utilss.showToast(context, getString(R.string.no_internet_connection), R.color.msg_fail);



        back_arrow.setOnClickListener(v->{
            onBackPressed();
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void fetchData() {
        // String searchText = search.getText().toString();

        loading.setVisibility(View.VISIBLE);

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");

        HashMap<String, String> body = new HashMap<>();
        body.put("key", data);



        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<HashtagSearchResponse> searchCall = webApi.searchHashTag(header, body);
        searchCall.enqueue(new Callback<HashtagSearchResponse>() {
            @Override
            public void onResponse(Call<HashtagSearchResponse> call, Response<HashtagSearchResponse> response) {
                loading.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getSuccess()) {
                    searchResponse = response.body();
                    // list.addAll(response.body().getData());
                    bindData();
                } else {
                    //Utilss.showToast(context, "there is no Hashtag", R.color.msg_fail);
                }
            }

            @Override
            public void onFailure(Call<HashtagSearchResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);

            }
        });
    }

    private void bindData() {
        if(searchResponse == null || searchResponse.getData() == null || searchResponse.getData().size()==0) {
            not_found.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }
        not_found.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        HashtagDataAdapter hashtagAdapter = new HashtagDataAdapter(context, searchResponse.getData());
        recyclerView.setAdapter(hashtagAdapter);
    }
}