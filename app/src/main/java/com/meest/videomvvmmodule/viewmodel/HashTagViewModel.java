package com.meest.videomvvmmodule.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.videomvvmmodule.adapter.VideoListAdapter;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.Global;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HashTagViewModel extends ViewModel {

    public VideoListAdapter adapter ;
    public MutableLiveData<Boolean> onLoadMoreComplete = new MutableLiveData<>();
    public int start = 0;
    public String hashtag;
    public MutableLiveData<Video> video = new MutableLiveData<>();
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(true);
    private int count = 10;
    private CompositeDisposable disposable = new CompositeDisposable();

    public void fetchHashTagVideos(boolean isLoadMore) {
        if (!disposable.isDisposed()) {
            disposable.clear();
        }
        disposable.add(Global.initRetrofit().fetchHasTagVideo(Global.apikey, hashtag, count, start, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> {
                    if (!isLoadMore) {
                        isloading.setValue(true);
                    }
                })
                .doOnTerminate(() -> {
                    if (!isLoadMore) {
                        isloading.setValue(false);
                    }
                    onLoadMoreComplete.setValue(true);
                })
                .subscribe((video, throwable) -> {
                    if (video != null && video.getData() != null && !video.getData().isEmpty()) {
                        this.video.setValue(video);
                        if (isLoadMore) {
                            adapter.loadMore(video.getData());
                        } else {
                            adapter.updateData(video.getData());
                        }
                        start = start + count;
                    }

                }));
    }

    public void onLoadMore() {
        fetchHashTagVideos(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

