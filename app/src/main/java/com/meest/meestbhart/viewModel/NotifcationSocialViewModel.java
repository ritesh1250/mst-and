package com.meest.meestbhart.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.meest.Activities.FollowRequestActivity;
import com.meest.meestbhart.view.notification.NotificationAdapter;
import com.meest.R;
import com.meest.responses.FollowRequestResponse;
import com.meest.responses.FollowerListResponse;
import com.meest.databinding.NotificationFragmentBinding;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.model.AllNotificationResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;

import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.social.socialViewModel.NotificationFragment;
import com.meest.videomvvmmodule.utils.Global;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.meest.meestbhart.utilss.Utilss.notificationTypeFollowRequest;

public class NotifcationSocialViewModel extends ViewModel {
    public MutableLiveData<String> toast = new MutableLiveData<>();
    public String userId;
    public int count = 15, start = 0;
    private Context context;
    public Activity activity;
    public NotificationFragmentBinding binding;
    public MutableLiveData<Boolean> onLoadMoreComplete = new MutableLiveData<>();
    public List<FollowerListResponse.Follower> followerList = new ArrayList<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public NotificationAdapter adapter = new NotificationAdapter();

    AllNotificationResponse.Row firstRequest;
    List<AllNotificationResponse.Row> feedNotification ;
    List<AllNotificationResponse.Row> feedRequestNotification;
    List<AllNotificationResponse.Row> notificationlistadd;
    NotificationFragment notificationFragment;

    public ObservableBoolean isEmpty = new ObservableBoolean(false);
    private CompositeDisposable disposable = new CompositeDisposable();
    int pageno = 1, pagePerRecord = 10;
    public static int totalCount = -1;
    public boolean Loading = true;

    public NotifcationSocialViewModel(Context context, Activity activity, NotificationFragmentBinding binding,NotificationFragment notificationFragment) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;
        this.notificationFragment=notificationFragment;
        feedNotification = new ArrayList<>();
        feedRequestNotification = new ArrayList<>();
        notificationlistadd=new ArrayList<>();
    }



    public void fetchNotificationData(int pageno) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("pageNumber", pageno);
        body.put("pageSize", pagePerRecord);
        disposable.add(Global.initSocialRetrofit().getAllNotification(Constant.token(context), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doOnTerminate(() -> {
                    onLoadMoreComplete.setValue(true);
                    isLoading.setValue(false);
                })
                .subscribe((notification, throwable) -> {
                    if (notification.getCode() == 1 && notification.getSuccess() != null) {
                        if (notification != null && notification.getData().getRows() != null) {
                            totalCount=notification.getData().getCount();
                            notificationlistadd.addAll(notification.getData().getRows());
                            feedNotification.clear();
                            feedRequestNotification.clear();
                            if(totalCount!=notificationlistadd.size() && notification.getData().getRows().size()>0){
                                Loading = true;
                            }
                            for (AllNotificationResponse.Row model : notification.getData().getRows()) {
                                if (model.getNotificationType().equals(notificationTypeFollowRequest)) {
                                    feedRequestNotification.add(model);
                                } else {
                                    feedNotification.add(model);
                                }
                            }

                            Log.e("RequestSIze",feedRequestNotification.size()+"");
                            if (feedRequestNotification.size() > 0) {
                                firstRequest = feedRequestNotification.get(0);
                                binding.followRequest.setVisibility(View.VISIBLE);
                                binding.followName.setText(String.format("%s %s", firstRequest.getUser().getFirstName(), firstRequest.getUser().getLastName()));
                                binding.followTime.setText(firstRequest.getCreatedAt());
                                //  CommonUtils.loadImage(followImage,firstRequest.getUser().getThumbnail(),getActivity());
                                Glide.with(context).load(firstRequest.getUser().getThumbnail()).placeholder(R.drawable.placeholder).into(binding.followImage);
                                binding.followViewAll.setOnClickListener(v -> {
                                    binding.followRequest.setVisibility(View.GONE);
                                    binding.getNotifcationViewModel().context.startActivity(new Intent(context, FollowRequestActivity.class));
                                });
                            } else {
                                binding.followRequest.setVisibility(View.GONE);
                            }
                            if (pageno==1){
                                adapter.updateData(feedNotification,notificationFragment);
                            } else {
                                adapter.loadMore(feedNotification,notificationFragment);
                            }
                        }
                        isEmpty.set(adapter.getDataList().isEmpty());
                    }
                }));

    }





    public void followsData() {
        HashMap<String, Object> body = new HashMap<>();
        body.put("pageNumber", pageno);
        body.put("pageSize", pagePerRecord);
        disposable.add(Global.initSocialRetrofit().followsData(Constant.token(context), SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doOnTerminate(() -> {
                    onLoadMoreComplete.setValue(true);
                    isLoading.setValue(false);
                })
                .subscribe(new BiConsumer<FollowerListResponse, Throwable>() {
                    @Override
                    public void accept(FollowerListResponse followerListResponse, Throwable throwable) throws Exception {
                        if (followerListResponse != null && followerListResponse.getData() != null) {
                            for (int i = 0; i < followerListResponse.getData().getFollower().size(); i++) {
                                if (!followerListResponse.getData().getFollower().get(i).getAccepted()) {
                                    followerList.add(followerListResponse.getData().getFollower().get(i));
                                }
                                if (followerList.size() > 0 && followerList != null) {
                                    SpannableStringBuilder str = new SpannableStringBuilder(context.getString(R.string.You_have) + followerList.size() + " ");
                                    str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    SpannableStringBuilder str1 = new SpannableStringBuilder(context.getString(R.string.following_request));
                                    str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    binding.followName.setText(str + "" + R.string.pending + str1 + R.string.approve_or_disapprove);
                                }
                            }
                        }
                        isEmpty.set(adapter.getDataList().isEmpty());
                    }
                }));
    }


    public void onBack() {
        activity.onBackPressed();
    }


    public void acceptRequest() {
        // body
        String id=firstRequest.getUserId();
        HashMap<String, Object> body = new HashMap<>();
        body.put("followingId", id);
        body.put("status", true);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FollowRequestResponse> call = webApi.acceptFollow(Constant.token(context), body);

        call.enqueue(new Callback<FollowRequestResponse>() {
            @Override
            public void onResponse(@NotNull Call<FollowRequestResponse> call, @NotNull Response<FollowRequestResponse> response) {
                if (response.code() == 200 && response.body().getSuccess()) {
                    binding.followRequest.setVisibility(View.GONE);
                    Utilss.showToast(context, context.getString(R.string.Request_Accepted), R.color.grey);
                } else {
                    Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(@NotNull Call<FollowRequestResponse> call, @NotNull Throwable t) {
                Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    public void rejectRequest() {
        String id=firstRequest.getUserId();
        HashMap<String, Object> body = new HashMap<>();
        body.put("followingId", id);
        body.put("status", false);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FollowRequestResponse> call = webApi.rejectFollow(Constant.token(context), body);

        call.enqueue(new Callback<FollowRequestResponse>() {
            @Override
            public void onResponse(@NotNull Call<FollowRequestResponse> call, @NotNull Response<FollowRequestResponse> response) {
                if (response.code() == 200 && response.body().getSuccess()) {
                    binding.followRequest.setVisibility(View.GONE);
                    Utilss.showToast(context, context.getString(R.string.Request_Rejected), R.color.grey);
                } else {
                    Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(@NotNull Call<FollowRequestResponse> call, @NotNull Throwable t) {
                Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
            }
        });
    }
}
