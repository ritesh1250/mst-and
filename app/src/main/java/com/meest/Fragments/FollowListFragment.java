package com.meest.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.FollowAdapter;
import com.meest.R;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.responses.FeedFollowResponse;
import com.meest.responses.UserResponse;
import com.meest.utils.PaginationListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowListFragment extends Fragment {

    private Context context;
    public static RecyclerView recyclerView;
    private FeedFollowResponse feedFollowResponse;
    private List<UserResponse> follower = new ArrayList<>();
    private List<UserResponse> following = new ArrayList<>();
    private String type;
    private LottieAnimationView loading;
    boolean isSvs = true;
    public static int pageno = 1;
    int pagePerRecord = 10;
    String Changerole;
    public static LinearLayoutManager manager;
    public String userId;
    FollowAdapter followAdapter;
    boolean Loading = true;

    public FollowListFragment(Context context, FeedFollowResponse feedFollowResponse, String type, String changerole, boolean isSvs, String userId) {
        this.type = type;
        this.context = context;
        this.feedFollowResponse = feedFollowResponse;
        this.Changerole = changerole;
        this.isSvs = isSvs;
        this.userId = userId;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, viewGroup, false);
        recyclerView = view.findViewById(R.id.people_recycleView);
        loading = view.findViewById(R.id.loading);
        loading.setAnimation(getString(R.string.loading_json));
        loading.playAnimation();
        loading.loop(true);
        pageno = 1;
        fetchFeedData(pageno);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        feedPagination();
        return view;
    }


    private void fetchFeedData(int pageno1) {
        loading.setVisibility(View.VISIBLE);

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        HashMap<String, Object> body = new HashMap<>();
        body.put("userId", userId);
        body.put("pageNumber", pageno1);
        body.put("pageSize", pagePerRecord);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FeedFollowResponse> call = webApi.getFeedFollowerData(header, body);
        call.enqueue(new Callback<FeedFollowResponse>() {
            @Override
            public void onResponse(Call<FeedFollowResponse> call, Response<FeedFollowResponse> response) {
                loading.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getSuccess()) {
                    feedFollowResponse = response.body();
                    bindData();
                } else {
                    Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<FeedFollowResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void bindData() {
        if (feedFollowResponse.getData().getFollowing().size()>0 || feedFollowResponse.getData().getFollower().size()>0){
            following.addAll(feedFollowResponse.getData().getFollowing());
            follower.addAll(feedFollowResponse.getData().getFollower());
            if (type.equalsIgnoreCase("follower")) {
                Log.e("TAG", "type" + Changerole);

                if (Changerole.equals("true")) {
                    followAdapter = new FollowAdapter(context, following, loading, isSvs);
                } else {
                    followAdapter = new FollowAdapter(context, follower, loading, isSvs);
                }
            } else {
                Log.e("TAG", "type" + type);
                if (Changerole.equals("true")) {
                    followAdapter = new FollowAdapter(context, follower, loading, isSvs);
                } else {
                    followAdapter = new FollowAdapter(context, following, loading, isSvs);
                }
            }
            Loading = true;
            recyclerView.setAdapter(followAdapter);
        }
    }


    public void feedPagination() {
        recyclerView.addOnScrollListener(new PaginationListener(manager, PaginationListener.PAGE_SIZE_10) {
            @Override
            public void loadMoreItems() {
                if (Loading) {
                    Loading = false;
                    pageno++;
                    fetchFeedData(pageno);
                }
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return false;
            }
        });
    }
}
