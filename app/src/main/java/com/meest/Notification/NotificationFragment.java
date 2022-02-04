package com.meest.Notification;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.meest.Activities.FollowRequestActivity;
import com.meest.Adapters.NotificationAdapter;
import com.meest.Interfaces.NotificationDeleteCallback;
import com.meest.MainActivity;
import com.meest.R;
import com.meest.responses.FollowRequestResponse;
import com.meest.responses.FollowerListResponse;
import com.meest.meestbhart.model.AllNotificationResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.utils.PaginationListener;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.networkcheck.OnNoInternetRetry;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.meest.meestbhart.utilss.Utilss.notificationTypeFollowRequest;

public class NotificationFragment extends Fragment implements NotificationDeleteCallback, OnNoInternetRetry {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private RelativeLayout no_notificationLayout;
    CardView follow_request;
    private LottieAnimationView loading;
    private boolean isSVS;
    TextView followName, followTime, followViewAll, followAccept, followDecline;
    ImageView followImage;
    int pagePerRecord = 10;
//    int count = 10;
//    int startPage = 0;

    int pageno = 1;
    int totalRowCount = 0;
    boolean Loading = true;
    AllNotificationResponse.Row firstRequest;
    List<AllNotificationResponse.Row> feedNotification = new ArrayList<>();
    List<AllNotificationResponse.Row> feedRequestNotification = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment, viewGroup, false);

        ((MainActivity) requireActivity()).setNotificationCount("0");
        SharedPrefreances.setSharedPreferenceInt(getActivity(), SharedPrefreances.NOIFICATION_COUNT,0);
        ImageView back_arrow = view.findViewById(R.id.back_arrow);
        no_notificationLayout = view.findViewById(R.id.no_notificationLayout);
        loading = view.findViewById(R.id.loading);
        follow_request = view.findViewById(R.id.follow_request);
        followName = view.findViewById(R.id.follow_name);
        followTime = view.findViewById(R.id.follow_time);
        followViewAll = view.findViewById(R.id.follow_view_all);
        followImage = view.findViewById(R.id.follow_image);
        followAccept = view.findViewById(R.id.follow_accept);
        followDecline = view.findViewById(R.id.follow_decline);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        });

        followAccept.setOnClickListener(v -> acceptRequest(firstRequest.getUserId()));
        followDecline.setOnClickListener(v -> rejectRequest(firstRequest.getUserId()));


        loading.setAnimation("loading.json");
        loading.playAnimation();
        loading.loop(true);

//        for (int i =0; i < 10;i++){
//            getallnotification.add("");
//
//        }

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (ConnectionUtils.isConnected(getActivity())) {
            if (feedNotification.size() > 0) {
                feedNotification.clear();
            }
            pageno = 1;
            fetchData(String.valueOf(pageno));
        } else {
            ConnectionUtils.showNoConnectionDialog(getActivity(), this);
        }

        recyclerView.addOnScrollListener(new PaginationListener((LinearLayoutManager) recyclerView.getLayoutManager(), PaginationListener.PAGE_SIZE_10) {
            @Override
            public void loadMoreItems() {
                System.out.println("==PAGINATION STARTED " + pageno);
                if (Loading) {
                    if(totalRowCount>feedNotification.size()){
                        pageno = pageno + 1;
                        fetchData(String.valueOf(pageno));
                    }
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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    private void showHideNoRecord(boolean show) {
        if (show) {
            no_notificationLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            no_notificationLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    // fetch notification data................
    private void fetchData(String pageno) {
        loading.setVisibility(View.VISIBLE);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        HashMap<String, Object> body = new HashMap<>();
        body.put("pageNumber", Integer.parseInt(pageno));
        body.put("pageSize", pagePerRecord);
        if (isSVS) {
            body.put("typeSearch", "Svs");
        } else {
            body.put("typeSearch", "Feed");
        }
        Call<AllNotificationResponse> call = webApi.getAllNotification(Constant.token(getActivity()), body);
        call.enqueue(new Callback<AllNotificationResponse>() {
            @Override
            public void onResponse(Call<AllNotificationResponse> call, Response<AllNotificationResponse> response) {

                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    loading.setVisibility(View.GONE);
                    Log.e("Notification+++", response.body() + "");
                    totalRowCount=response.body().getData().getCount();
                    if (response.body().getData().getRows().size() > 0) {
                        for (AllNotificationResponse.Row model : response.body().getData().getRows()) {
                            if (model.getNotificationType().equals(notificationTypeFollowRequest)) {
                                feedRequestNotification.add(model);
                            } else {
                                feedNotification.add(model);
                            }
                        }

                        if (feedRequestNotification.size() > 0) {
                            firstRequest = feedRequestNotification.get(0);
                            follow_request.setVisibility(View.VISIBLE);

                            followName.setText(String.format("%s %s", firstRequest.getUser().getFirstName(), firstRequest.getUser().getLastName()));
                            followTime.setText(firstRequest.getCreatedAt());
                          //  CommonUtils.loadImage(followImage,firstRequest.getUser().getThumbnail(),getActivity());
                            Glide.with(getActivity()).load(firstRequest.getUser().getThumbnail()).placeholder(R.drawable.placeholder).into(followImage);
                            followViewAll.setOnClickListener(v -> {
                                startActivity(new Intent(getActivity(), FollowRequestActivity.class));
                            });
                        } else {
                            follow_request.setVisibility(View.GONE);
                        }

                        showHideNoRecord(feedNotification.size() == 0);
                        if (adapter == null) {
                            adapter = new NotificationAdapter(getActivity(), feedNotification);
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged(feedNotification);
                        }
                    }else {
                        no_notificationLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    loading.setVisibility(View.GONE);
                    no_notificationLayout.setVisibility(View.VISIBLE);
                    Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<AllNotificationResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Log.w("error", t.toString());
                Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    void getData() {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FollowerListResponse> call = webApi.followsData(Constant.token(getActivity()), SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.ID));
        call.enqueue(new Callback<FollowerListResponse>() {
            @Override
            public void onResponse(Call<FollowerListResponse> call, Response<FollowerListResponse> response) {
                if (response.code() == 200) {
                    ArrayList<FollowerListResponse.Follower> arrayList = new ArrayList<>();
                    for (int i = 0; i < response.body().getData().getFollower().size(); i++) {
                        if (!response.body().getData().getFollower().get(i).getAccepted()) {
                            arrayList.add(response.body().getData().getFollower().get(i));
                        }
                    }
                    if (arrayList.size() != 0) {
                         follow_request.setVisibility(View.VISIBLE);
                        SpannableStringBuilder str = new SpannableStringBuilder(getString(R.string.You_have) + arrayList.size() + " ");
                        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        SpannableStringBuilder str1 = new SpannableStringBuilder(getString(R.string.following_request));
                        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        followName.setText(str +""+R.string.pending+ str1 +R.string.approve_or_disapprove);
                    } else {
                         follow_request.setVisibility(View.GONE);

                    }
                } else {
                    Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<FollowerListResponse> call, Throwable t) {
                Log.w("error", t.toString());
                Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    @Override
    public void delete() {
        pageno = 1;
        feedNotification.clear();
        fetchData(String.valueOf(pageno));
    }

    @Override
    public void onRetry() {

        if (ConnectionUtils.isConnected(getActivity())) {
            if (feedNotification.size() > 0) {
                feedNotification.clear();
            }
            pageno = 1;
            fetchData(String.valueOf(pageno));
        } else {
            ConnectionUtils.showNoConnectionDialog(getActivity(), this);
        }
    }

    void acceptRequest(String id) {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));
        // body
        HashMap<String, Object> body = new HashMap<>();
        body.put("followingId", id);
        body.put("status", true);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FollowRequestResponse> call = webApi.acceptFollow(header, body);

        call.enqueue(new Callback<FollowRequestResponse>() {
            @Override
            public void onResponse(@NotNull Call<FollowRequestResponse> call, @NotNull Response<FollowRequestResponse> response) {
                if (response.code() == 200 && response.body().getSuccess()) {
                    Utilss.showToast(getActivity(), getString(R.string.Request_Accepted), R.color.grey);
                    follow_request.setVisibility(View.GONE);
                } else {
                    Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(@NotNull Call<FollowRequestResponse> call, @NotNull Throwable t) {
                Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    void rejectRequest(String id) {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));
        // body
        HashMap<String, Object> body = new HashMap<>();
        body.put("followingId", id);
        body.put("status", false);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FollowRequestResponse> call = webApi.rejectFollow(header, body);

        call.enqueue(new Callback<FollowRequestResponse>() {
            @Override
            public void onResponse(@NotNull Call<FollowRequestResponse> call, @NotNull Response<FollowRequestResponse> response) {
                if (response.code() == 200 && response.body().getSuccess()) {
                    follow_request.setVisibility(View.GONE);
                    Utilss.showToast(getActivity(), getString(R.string.Request_Rejected), R.color.grey);
                } else {
                    Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(@NotNull Call<FollowRequestResponse> call, @NotNull Throwable t) {
                Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }
}