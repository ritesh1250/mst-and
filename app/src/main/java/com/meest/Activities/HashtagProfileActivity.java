package com.meest.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.hashtag_profile_adapter;
import com.meest.Paramaters.FollowPeoplesParam;
import com.meest.Paramaters.UnFollowPeoplesParam;
import com.meest.R;
import com.meest.responses.FollowsPeoplesResponse;
import com.meest.responses.HashtagProfileResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HashtagProfileActivity extends AppCompatActivity {

    hashtag_profile_adapter hashtag_profile_adapter;
    private TextView tv_hashtag_profile;
    private ImageView back_arrow_search;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    ArrayList<HashtagProfileResponse.Data.Videos.Row> HashtagProfileList = new ArrayList<>();
    ArrayList<HashtagProfileResponse.Data> PostedByUser = new ArrayList<>();
    LottieAnimationView image;
    private CircleImageView img_profile_pic;
    private Button bt_follow_hashtag_profile;
    private TextView txt_name_profile;
    String url, UserId = "";
    private SwitchCompat switch_all_comment_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag_profile);


        hashtag_profile_adapter = new hashtag_profile_adapter(HashtagProfileList, PostedByUser, HashtagProfileActivity.this);
        Log.e("TAG", "priofileurltech=" + UserId);


        bt_follow_hashtag_profile = (Button) findViewById(R.id.bt_follow_hashtag_profile);

        image = findViewById(R.id.loading);
        tv_hashtag_profile = (TextView) findViewById(R.id.tv_hashtag_profile);
        img_profile_pic = (CircleImageView) findViewById(R.id.img_profile_pic);
        txt_name_profile = (TextView) findViewById(R.id.txt_name_profile);

        switch_all_comment_post = (SwitchCompat) findViewById(R.id.switch_all_comment_post);

        back_arrow_search = findViewById(R.id.back_arrow_search);
        back_arrow_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.hashtag_video_reclyerview);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            image.setAnimation("loading.json");
            image.playAnimation();
            image.loop(true);
            image.setVisibility(View.VISIBLE);
            tv_hashtag_profile.setText("#" + bundle.getString("hashtag_profile"));
            Map<String, String> map = new HashMap<>();
            map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
            Call<HashtagProfileResponse> call = webApi.getHashtagByVideo(map, bundle.getString("hashtag_profile"));
            call.enqueue(new Callback<HashtagProfileResponse>() {
                @Override
                public void onResponse(Call<HashtagProfileResponse> call, Response<HashtagProfileResponse> response) {
                    try {
                        if (response.code() == 200) {
                            image.setVisibility(View.GONE);

                            Log.e("TAg", "maintainehash=" + response.toString());
                            //  HashtagProfileList.clear();
                            for (int i = 0; i < response.body().getData().getVideos().getRows().size(); i++) {
                                HashtagProfileList.add(response.body().getData().getVideos().getRows().get(i));
                            }
                            txt_name_profile.setText(getString(R.string.Fewer_than) + String.valueOf(response.body().getData().getVideos().getCount()));
                            Log.e("TAg", "useridmy=" + response.body().getData().getPostedBy().getUser().getId());
                            UserId = response.body().getData().getPostedBy().getUser().getId();
                            Glide.with(HashtagProfileActivity.this).load(response.body().getData().getPostedBy().getUser().getDisplayPicture()).placeholder(R.drawable.placeholder).into(img_profile_pic);
                            manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(manager);
                            recyclerView.setAdapter(hashtag_profile_adapter);
//                            recyclerView.setHasFixedSize(true);
//                            recyclerView.setItemViewCacheSize(20);
//                            recyclerView.setDrawingCacheEnabled(true);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            hashtag_profile_adapter.notifyDataSetChanged();

                            Log.e("TAh", "checkfriends=" + response.body().getData().getPostedBy().getIsFriend());

                            if (response.body().getData().getPostedBy().getIsFriend().equals(1)) {
                                bt_follow_hashtag_profile.setText(getString(R.string.Following));
                            } else if (response.body().getData().getPostedBy().getIsFriend().equals(0)) {
                                bt_follow_hashtag_profile.setText(getString(R.string.Follow));
                            }
                        } else if (response.code() == 500) {
                            image.setVisibility(View.GONE);
                            Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                        } else {
                            image.setVisibility(View.GONE);
                            // image.setVisibility(View.GONE);
                            Utilss.showToast(getApplicationContext(), response.body().getSuccess().toString(), R.color.msg_fail);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        image.setVisibility(View.GONE);
                        //Toast.makeText(getContext(),"rwereqwreqwrwrwqrqewr",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<HashtagProfileResponse> call, Throwable t) {
                    Log.e("Atg", "test =" + t.getMessage());
                    image.setVisibility(View.GONE);
                }
            });

            bt_follow_hashtag_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (bt_follow_hashtag_profile.getText().toString().equalsIgnoreCase("Follow")) {
                        followRequest();
                    } else if (bt_follow_hashtag_profile.getText().toString().equalsIgnoreCase("Following")) {
                        unfollow();
                    } else if (bt_follow_hashtag_profile.getText().toString().equalsIgnoreCase("Waiting")) {
                        unfollow();
                    }

                    Log.e("Tag", "checkingid=" + UserId);
                }
            });
        }
    }

    private void followRequest() {

        image.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));

        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);
        FollowPeoplesParam followPeoplesParam = new FollowPeoplesParam(UserId, false);
        Call<FollowsPeoplesResponse> call1 = webApi1.follow_peoples(map, followPeoplesParam);
        call1.enqueue(new Callback<FollowsPeoplesResponse>() {
            @Override
            public void onResponse(Call<FollowsPeoplesResponse> call, Response<FollowsPeoplesResponse> response) {
                try {
                    image.setVisibility(View.GONE);
                    if (response.code() == 200) {

                        if (response.body().getCode() == 1) {
                            bt_follow_hashtag_profile.setText(getString(R.string.following));
                        } else {

                        }

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<FollowsPeoplesResponse> call, Throwable t) {
                image.setVisibility(View.GONE);
                Log.w("error", t);
                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void unfollow() {
        image.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);
        UnFollowPeoplesParam unFollowPeoplesParam = new UnFollowPeoplesParam(UserId, false);
        Call<FollowsPeoplesResponse> call1 = webApi1.Unfollow_peoples(map, unFollowPeoplesParam);
        call1.enqueue(new Callback<FollowsPeoplesResponse>() {
            @Override
            public void onResponse(Call<FollowsPeoplesResponse> call, Response<FollowsPeoplesResponse> response) {
                try {
                    image.setVisibility(View.GONE);
                    if (response.code() == 200) {

                        if (response.body().getCode() == 1) {
                            bt_follow_hashtag_profile.setText(getString(R.string.Follow));
                        } else {

                        }

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<FollowsPeoplesResponse> call, Throwable t) {
                Log.w("error", t);
                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}