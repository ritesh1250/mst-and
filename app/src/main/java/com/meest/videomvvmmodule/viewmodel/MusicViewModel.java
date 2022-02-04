package com.meest.videomvvmmodule.viewmodel;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.videomvvmmodule.adapter.MusicsListAdapter;
import com.meest.videomvvmmodule.model.music.Musics;
import com.meest.videomvvmmodule.utils.Global;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MusicViewModel extends ViewModel {
    public ObservableInt selectPosition = new ObservableInt(0);

    public ObservableBoolean isMore = new ObservableBoolean(false);
    public MusicsListAdapter searchMusicAdapter = new MusicsListAdapter();
    public MutableLiveData<Musics.SoundList> music = new MutableLiveData<>();
    public MutableLiveData<Boolean> stopMusic = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public void categoryWiseSoundList(String categoryId) {
        disposable.add(Global.initRetrofit().categoryWiseSoundList(Global.apikey, categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doOnTerminate(() -> isLoading.setValue(false))
                .subscribe((reward, throwable) -> {
                    if (reward != null && reward.getStatus()) {
                        searchMusicAdapter.updateData(reward.getData());
                    }
                }));
    }
}
