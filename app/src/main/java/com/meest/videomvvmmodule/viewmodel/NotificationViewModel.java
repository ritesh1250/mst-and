package com.meest.videomvvmmodule.viewmodel;

import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.databinding.ItemNotificationBinding;
import com.meest.videomvvmmodule.adapter.NotificationAdapter;
import com.meest.videomvvmmodule.model.notification.Notification;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.Global;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class NotificationViewModel extends ViewModel {
    public MutableLiveData<String> toast = new MutableLiveData<>();
    public String userId;
    public int count = 15, start = 0;
    public MutableLiveData<Boolean> onLoadMoreComplete = new MutableLiveData<>();
    public NotificationAdapter adapter = new NotificationAdapter();
    public ObservableBoolean isEmpty = new ObservableBoolean(false);
    CompositeDisposable disposable = new CompositeDisposable();
    ObservableBoolean isLoading = new ObservableBoolean();
    public ObservableBoolean loadingPost = new ObservableBoolean();
    public MutableLiveData<Video> video = new MutableLiveData<>();
    public ObservableBoolean isFollowLoading = new ObservableBoolean(false);

    public void fetchPostById(String postId) {
        disposable.add(Global.initRetrofit().getPostById(Global.apikey, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loadingPost.set(true))
                .doOnTerminate(() -> {
                    onLoadMoreComplete.setValue(true);
                    loadingPost.set(false);
                })
                .subscribe((video, throwable) -> {
                    if (video != null && video.getData() != null)
                        this.video.setValue(video);
                }));
    }

    public void fetchNotificationData(boolean isLoadMore) {
        disposable.add(Global.initRetrofit().getNotificationList(Global.apikey, Global.userId, count, start)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> {
                    onLoadMoreComplete.setValue(true);
                    isLoading.set(false);
                })
                .subscribe((notification, throwable) -> {
                    if (notification != null && notification.getData() != null) {
                        if (isLoadMore) {
                            adapter.loadMore(notification.getData());
                        } else {
                            adapter.updateData(notification.getData());
                        }
                        start = start + count;
                    }
                    isEmpty.set(adapter.getDataList().isEmpty());
                }));
    }

    public void followUnfollow(Notification.Data data, ItemNotificationBinding binding) {
//        isloading.set(true);
        disposable.add(Global.initRetrofit().followUnFollow(Global.apikey, Global.userId, data.getSenderUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> isFollowLoading.set(true))
                .doOnTerminate(() -> isFollowLoading.set(false))
                .unsubscribeOn(Schedulers.io())
                .subscribe((followRequest, throwable) -> {

                    if (followRequest != null && followRequest.getStatus() != null) {
                        if (followRequest.getMessage().equalsIgnoreCase("Follow Successfully")) {
                            data.setFollow_or_not("1");
                            Timber.e(data.getFollow_or_not());
                            binding.btnUnfollow.setVisibility(View.VISIBLE);
                            binding.btnFollow.setVisibility(View.GONE);
                            Global.followUnfollow.put(userId, true);

                        } else {
                            data.setFollow_or_not("0");
                            Timber.e(data.getFollow_or_not());
                            binding.btnUnfollow.setVisibility(View.GONE);
                            binding.btnFollow.setVisibility(View.VISIBLE);
                            Global.followUnfollow.put(userId, false);
                        }

                        if (followRequest.getMessage().equalsIgnoreCase("Follow Successfully")) {
                            Toast.makeText(binding.getRoot().getContext(), binding.getRoot().getContext().getResources().getString(R.string.Follow_Successfully), Toast.LENGTH_SHORT).show();
                        } else if (followRequest.getMessage().equalsIgnoreCase("Unfollow Successfully")) {
                            Toast.makeText(binding.getRoot().getContext(), binding.getRoot().getContext().getResources().getString(R.string.Unfollow_Successfully), Toast.LENGTH_SHORT).show();
                        }
                        // Toast.makeText(binding.getRoot().getContext(), followRequest.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }
}
