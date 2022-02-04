package com.meest.videomvvmmodule.viewmodel;

import android.widget.TextView;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.meest.videomvvmmodule.adapter.ProfileVideosAdapter;
import com.meest.videomvvmmodule.model.user.RestResponse;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileVideosViewModel extends ViewModel {
    public int userVidStart = 0;
    public int likeVidStart = 0;
    public int count = 9;
    public int vidType;
    public SessionManager sessionManager;
    public ProfileVideosAdapter adapter = new ProfileVideosAdapter();
    public String userId;
    public ObservableBoolean noUserVideos = new ObservableBoolean(false);
    public ObservableBoolean noLikedVideos = new ObservableBoolean(false);
    public MutableLiveData<Boolean> onLoadMoreComplete = new MutableLiveData<>();
    ObservableBoolean isLoading = new ObservableBoolean(true);
    CompositeDisposable disposable = new CompositeDisposable();
    public int viewCount = 0;
    public int postCount = 0;

    public void fetchUserVideos(boolean isLoadMore) {
        adapter.setVideosViewModel(this);
        disposable.add(Global.initRetrofit().getUserProfileVideos(Global.apikey, userId, count, userVidStart, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> {
                    onLoadMoreComplete.setValue(true);
                    isLoading.set(false);
                })
                .subscribe((Video videos, Throwable throwable) -> {
                    if (videos != null && videos.getData() != null) {
                        if (isLoadMore) {
                            adapter.loadMore(videos.getData());
                        } else {
                            if (!new Gson().toJson(videos.getData()).equals(new Gson().toJson(adapter.getmList()))) {
                                adapter.updateData(videos.getData());
                            }
                        }
                        noUserVideos.set(adapter.getmList().isEmpty());
                        userVidStart = userVidStart + count;
                    }
                }));
    }

    public void fetchUserLikedVideos(boolean isLoadMore) {
        adapter.setVideosViewModel(this);

        disposable.add(Global.initRetrofit().getProfileUserLikedVideos(Global.apikey, userId, count, likeVidStart, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> {
                    onLoadMoreComplete.setValue(true);
                    isLoading.set(false);
                })
                .subscribe((Video videos, Throwable throwable) -> {
                    if (videos != null && videos.getData() != null) {
                        if (isLoadMore) {
                            adapter.loadMore(videos.getData());
                        } else {
                            if (!new Gson().toJson(videos.getData()).equals(new Gson().toJson(adapter.getmList()))) {
                                adapter.updateData(videos.getData());
                            }
                        }
                        noLikedVideos.set(adapter.getmList().isEmpty());
                        likeVidStart = likeVidStart + count;
                    }
                }));
    }

    public void onUserVideoLoadMore() {
        fetchUserVideos(true);
    }

    public void onUserLikedVideoLoadMore() {
        fetchUserLikedVideos(true);
    }

    public void deletePost(String postId, int position, TextView tvViewCount, Integer postViewCount, TextView tvPostCount) {
        disposable.add(Global.initRetrofit().deletePost(Global.apikey, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((RestResponse deletePost, Throwable throwable) -> {

                    if (deletePost != null && deletePost.getStatus() != null) {
                        adapter.getmList().remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeRemoved(position, adapter.getmList().size());
//                      new ProfileViewModel().fetchUserById(Global.userId);
//                      Log.d("AAAAAAAAAAA", "viewCount: -==================================" + viewCount);
                        viewCount = viewCount - postViewCount;
//                      Log.d("AAAAAAAAAAA", "viewCount: -==================================" + viewCount);
                        postCount = postCount - 1;
                        tvViewCount.setText(Global.prettyCount(viewCount));
                        tvPostCount.setText(Global.prettyCount(postCount));
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

}
