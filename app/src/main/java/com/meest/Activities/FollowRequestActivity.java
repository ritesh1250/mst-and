package com.meest.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.FollowRequestAdaptor;
import com.meest.Interfaces.CampaignCallback;
import com.meest.R;
import com.meest.meestbhart.model.AllNotificationResponse;
import com.meest.responses.FollowerListResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.meest.meestbhart.utilss.Utilss.notificationTypeFollowRequest;

public class FollowRequestActivity extends AppCompatActivity implements CampaignCallback {

    RecyclerView recyclerView;
    ImageView id_back;
    LottieAnimationView lottieAnimationView;

    ArrayList<FollowerListResponse.Follower> arrayList = new ArrayList<>();

    List<AllNotificationResponse.Row> feedRequestNotification = new ArrayList<>();

    @Override
    public void lineChartClick(int position) {
        getData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_request);

        lottieAnimationView = findViewById(R.id.loading);

        recyclerView = findViewById(R.id.recycler_view);
        id_back = findViewById(R.id.id_back);
        id_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();

    }

//    void getData() {
//        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
//        Call<FollowerListResponse> call = webApi.followsData(Constant.token(this), SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.ID));
//        call.enqueue(new Callback<FollowerListResponse>() {
//            @Override
//            public void onResponse(Call<FollowerListResponse> call, Response<FollowerListResponse> response) {
//                if (response.code() == 200) {
//                    if (response.body().getCode() == 1) {
//                        arrayList.clear();
//                        for (int i = 0; i < response.body().getData().getFollower().size(); i++) {
//                            if (!response.body().getData().getFollower().get(i).getAccepted()) {
//                                arrayList.add(response.body().getData().getFollower().get(i));
//                            }
//                        }
//                        FollowRequestAdaptor followRequestActivity = new FollowRequestAdaptor(arrayList, FollowRequestActivity.this);
//                        recyclerView.setAdapter(followRequestActivity);
//                        followRequestActivity.notifyDataSetChanged();
//
//                    } else {
//                        Utilss.showToast(FollowRequestActivity.this, "Server Error", R.color.msg_fail);
//
//                    }
//                } else {
//                    Utilss.showToast(FollowRequestActivity.this, "Server Error", R.color.msg_fail);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<FollowerListResponse> call, Throwable t) {
//                Log.w("error", t.toString());
//                Utilss.showToast(FollowRequestActivity.this, "Server Error", R.color.msg_fail);
//            }
//        });
//    }

    void getData() {
        lottieAnimationView.setVisibility(View.VISIBLE);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        HashMap<String, Object> body = new HashMap<>();
        body.put("pageNumber", 1);
        body.put("pageSize", 1000);
        body.put("typeSearch", "Feed");

        Call<AllNotificationResponse> call = webApi.getAllNotification(Constant.token(FollowRequestActivity.this), body);
        call.enqueue(new Callback<AllNotificationResponse>() {
            @Override
            public void onResponse(Call<AllNotificationResponse> call, Response<AllNotificationResponse> response) {
                lottieAnimationView.setVisibility(View.GONE);
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Log.e("Notification+++", response.body() + "");
                    if (response.body().getData().getRows().size() > 0) {
                        for (AllNotificationResponse.Row model : response.body().getData().getRows()) {
                            if (model.getNotificationType().equals(notificationTypeFollowRequest)) {
                                feedRequestNotification.add(model);
                            }
                        }

                        if (feedRequestNotification.size() >= 0) {
                            FollowRequestAdaptor followRequestActivity = new FollowRequestAdaptor(feedRequestNotification, FollowRequestActivity.this);
                            recyclerView.setAdapter(followRequestActivity);
                            followRequestActivity.notifyDataSetChanged();
                        }

                    }
                } else {
                    Utilss.showToast(FollowRequestActivity.this, getString(R.string.error_msg), R.color.grey);
                }
                lottieAnimationView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AllNotificationResponse> call, Throwable t) {
                lottieAnimationView.setVisibility(View.GONE);
                Log.w("error", t.toString());
                Utilss.showToast(FollowRequestActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }
}
