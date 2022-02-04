package com.meest.videomvvmmodule.viewmodel;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.databinding.ItemFollowerBinding;
import com.meest.videomvvmmodule.adapter.FollowersAdapter;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentFollowersViewModel extends ViewModel {
    public SessionManager sessionManager;
    public FollowersAdapter adapter = new FollowersAdapter("0");
    public String itemType;
    public MutableLiveData<Boolean> onLoadMoreComplete = new MutableLiveData<>();
    public String userId;
    ObservableBoolean isloading = new ObservableBoolean(true);
    private CompositeDisposable disposable = new CompositeDisposable();
    public int followerStart = 0;
    public int followingStart = 0;
    private int count = 15;
    public ObservableBoolean isFollowLoading = new ObservableBoolean(false);
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public void fetchFollowers(boolean isLoadMore) {
        if (Global.userId.equals(userId)) {
            disposable.add(Global.initRetrofit().getFollowerList(Global.apikey, userId, count, followerStart, Global.userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                    .doOnTerminate(() -> {
                        onLoadMoreComplete.setValue(true);
                        isLoading.setValue(false);
                    })
                    .subscribe((follower, throwable) -> {
                        if (follower != null && follower.getData() != null) {

                            if (isLoadMore) {
                                adapter.loadMore(follower.getData());
                            } else {
                                adapter.updateData(follower.getData());
                            }
                            followerStart = followerStart + count;
                        }
                    }));
        } else {
            disposable.add(Global.initRetrofit().getFollowerListForOther(Global.apikey, userId, count, followerStart, Global.userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                    .doOnTerminate(() -> {
                        onLoadMoreComplete.setValue(true);
                        isLoading.setValue(false);
                    })
                    .subscribe((follower, throwable) -> {
                        if (follower != null && follower.getData() != null) {

                            if (isLoadMore) {
                                adapter.loadMore(follower.getData());
                            } else {
                                adapter.updateData(follower.getData());
                            }
                            followerStart = followerStart + count;
                        }
                    }));
        }

    }

    public void fetchFollowing(boolean isLoadMore) {
        if (Global.userId.equals(userId)) {
            disposable.add(Global.initRetrofit().getFollowingList(Global.apikey, userId, count, followingStart, Global.userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable1 -> isloading.set(true))
                    .doOnTerminate(() -> {
                        onLoadMoreComplete.setValue(true);
                        isloading.set(false);
                    })
                    .subscribe((following, throwable) -> {
                        if (following != null && following.getData() != null) {
                            if (isLoadMore) {
                                adapter.loadMore(following.getData());
                            } else {
                                adapter.updateData(following.getData());
                            }
                            followingStart = followingStart + count;
                        }
                    }));
        } else {
            disposable.add(Global.initRetrofit().getFollowingListForOther(Global.apikey, userId, count, followingStart, Global.userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable1 -> isloading.set(true))
                    .doOnTerminate(() -> {
                        onLoadMoreComplete.setValue(true);
                        isloading.set(false);
                    })
                    .subscribe((following, throwable) -> {
                        if (following != null && following.getData() != null) {
                            if (isLoadMore) {
                                adapter.loadMore(following.getData());
                            } else {
                                adapter.updateData(following.getData());
                            }
                            followingStart = followingStart + count;
                        }
                    }));
        }
    }

    public void follow(String userId, Context context, ItemFollowerBinding binding) {
        disposable.add(Global.initRetrofit().followUnFollow(Global.apikey, Global.userId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> {
                    isFollowLoading.set(true);
                })
                .doOnTerminate(() -> isFollowLoading.set(false))
                .subscribe((followRequest, throwable) -> {
                    if (followRequest != null && followRequest.getStatus() != null) {
                        Toast.makeText(context, followRequest.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.follow.setVisibility(View.GONE);
                        binding.btnRemove.setVisibility(View.VISIBLE);
                        Global.followUnfollow.put(userId, true);
//                        followApi.setValue(followRequest);
                    }
                }));
    }

    public void unfollow(String userId, Context context, ItemFollowerBinding binding, int position, String itemType) {
        disposable.add(Global.initRetrofit().followUnFollow(Global.apikey, Global.userId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> {
                    isFollowLoading.set(true);
                })
                .doOnTerminate(() -> isFollowLoading.set(false))
                .subscribe((followRequest, throwable) -> {
                    if (followRequest != null && followRequest.getStatus() != null) {

                        if (followRequest.getMessage().equalsIgnoreCase("Follow Successfully")) {
                            Toast.makeText(context, context.getResources().getString(R.string.Follow_Successfully), Toast.LENGTH_SHORT).show();
                            Global.followUnfollow.put(userId, true);
                        } else if (followRequest.getMessage().equalsIgnoreCase("Unfollow Successfully")) {
                            Toast.makeText(context, context.getResources().getString(R.string.Unfollow_Successfully), Toast.LENGTH_SHORT).show();
                            Global.followUnfollow.put(userId, false);
                        }

                        if (itemType.equals("1")) {
                            adapter.notifyItemRemoved(position);
                        }
                        binding.follow.setVisibility(View.VISIBLE);
                        binding.btnRemove.setVisibility(View.GONE);

//                        followApi.setValue(followRequest);
                    }
                }));
    }

    public void onFollowersLoadMore() {
        fetchFollowers(true);
    }

    public void onFollowingLoadMore() {
        fetchFollowing(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }


}
