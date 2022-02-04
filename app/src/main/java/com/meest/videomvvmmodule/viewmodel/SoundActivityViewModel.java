package com.meest.videomvvmmodule.viewmodel;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.videomvvmmodule.adapter.SoundVideoAdapter;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.Global;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SoundActivityViewModel extends ViewModel {

    public String soundId;
    public int start = 0;
    public int count = 10;
    public MutableLiveData<Boolean> onLoadMoreComplete = new MutableLiveData<>();
    public SoundVideoAdapter adapter = new SoundVideoAdapter();
    public MutableLiveData<Video.SoundData> soundData = new MutableLiveData<>();
    public ObservableBoolean isPlaying = new ObservableBoolean(false);
    public String soundUrl;
    public ObservableBoolean isFavourite = new ObservableBoolean(false);
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(true);
    private CompositeDisposable disposable = new CompositeDisposable();

    public void fetchSoundVideos(boolean isLoadMore) {
        if (!disposable.isDisposed()) {
            disposable.clear();
        }
        disposable.add(Global.initRetrofit().getSoundVideos(Global.apikey, count, start, soundId, Global.userId)
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
                        if (isLoadMore) {
                            adapter.loadMore(video.getData());
                        } else {
                            adapter.updateData(video.getData());
                            this.soundData.setValue(video.getSoundData());
                        }

                        start = start + count;
                    }

                }));
    }

    public void onLoadMore() {
        fetchSoundVideos(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

}
