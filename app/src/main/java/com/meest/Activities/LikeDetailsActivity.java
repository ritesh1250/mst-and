package com.meest.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.LikeDetailsAdapter;
import com.meest.R;
import com.meest.responses.LikeDetailsResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LikeDetailsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LikeDetailsAdapter peopleAdapter;
    RecyclerView.LayoutManager manager;
    String postId;
    ArrayList<LikeDetailsResponse.Row> Peopleslist = new ArrayList<>();
    LottieAnimationView loading;

    RelativeLayout img_back_arroe_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_details);


        loading = findViewById(R.id.loading);
        recyclerView = findViewById(R.id.recycler_view);
        img_back_arroe_profile = findViewById(R.id.img_back_arroe_profile);


        loading = findViewById(R.id.loading);
        loading.setAnimation("loading.json");
        loading.playAnimation();
        loading.loop(true);

        postId = getIntent().getExtras().getString("postId");


        img_back_arroe_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fetchData();
    }

    private void fetchData() {
        loading.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(LikeDetailsActivity.this, SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);

        HashMap<String, String> body = new HashMap<>();
        body.put("postId", postId);

        Call<LikeDetailsResponse> call = webApi.getLikeDetails(map, body);
        call.enqueue(new Callback<LikeDetailsResponse>() {
            @Override
            public void onResponse(Call<LikeDetailsResponse> call, Response<LikeDetailsResponse> response) {
                try {
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        loading.setVisibility(View.GONE);
                        // Peopleslist.clear();
                        for (int i = 0; i < response.body().getData().getRows().size(); i++) {
                            Peopleslist.add(response.body().getData().getRows().get(i));
                        }
                        peopleAdapter = new LikeDetailsAdapter(Peopleslist, LikeDetailsActivity.this, loading);
                        manager = new LinearLayoutManager(LikeDetailsActivity.this);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(peopleAdapter);
                        recyclerView.setLayoutManager(manager);

                    } else {
                        loading.setVisibility(View.GONE);
                        Utilss.showToast(getApplicationContext(), getString(R.string.No_Data_Found), R.color.social_background_blue);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(LikeDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LikeDetailsResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Utilss.showToast(LikeDetailsActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }
}