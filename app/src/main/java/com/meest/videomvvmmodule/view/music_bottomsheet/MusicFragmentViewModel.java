package com.meest.videomvvmmodule.view.music_bottomsheet;

import android.content.Context;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.videomvvmmodule.adapter.MusicsCategoryAdapter;
import com.meest.videomvvmmodule.adapter.MusicsListAdapter;
import com.meest.videomvvmmodule.model.music.Musics;
import com.meest.videomvvmmodule.utils.Global;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MusicFragmentViewModel extends ViewModel {
    public MutableLiveData<Boolean> isAdd = new MutableLiveData(false);
    public int itemType = 0;
    public Context context;
    private CompositeDisposable disposable = new CompositeDisposable();
    public MusicsCategoryAdapter categoryAdapter = new MusicsCategoryAdapter();
    public MusicFavoriteAdapter favoriteAdapter = new MusicFavoriteAdapter();
    public MusicsListAdapter musicsListAdapter = new MusicsListAdapter();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<Musics.SoundList> music = new MutableLiveData<>();

    public void getMusicList() {
        disposable.add(Global.initRetrofit().getSoundList(Global.apikey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doOnTerminate(() -> isLoading.setValue(false))
                .subscribe((Musics soundList, Throwable throwable) -> {
                    if (soundList != null && soundList.getStatus() && soundList.getData() != null && !soundList.getData().isEmpty()) {
                        categoryAdapter.updateData(soundList.getData());
                    }
                }));
    }

    public void getFavoriteList(String userId) {
        disposable.add(Global.initRetrofit().getFavoriteList(Global.apikey, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doOnTerminate(() -> isLoading.setValue(false))
                .subscribe((FavoriteMusicsResponse soundList, Throwable throwable) -> {
                    if (soundList != null && soundList.getStatus() && soundList.getData() != null && !soundList.getData().isEmpty()) {
                        favoriteAdapter.updateData(soundList.getData());
                    }
                }));
    }

    public void addFavoriteMusic(String userId, String soundId) {
        disposable.add(Global.initRetrofit().addFavoriteMusic(Global.apikey, userId, soundId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.setValue(true))
                .doOnTerminate(() -> isLoading.setValue(false))
                .subscribe((AddFavoriteResponse addFavoriteResponse, Throwable throwable) -> {
                    if (addFavoriteResponse.getStatus()) {
                        isAdd.setValue(true);
                    }
                }));
    }
}
