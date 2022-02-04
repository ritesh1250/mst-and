package com.meest.videomvvmmodule.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.videomvvmmodule.model.music.Musics;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.meest.videomvvmmodule.adapter.MusicsCategoryAdapter;
import com.meest.videomvvmmodule.adapter.MusicsListAdapter;
import com.meest.videomvvmmodule.model.music.SearchMusic;
import com.meest.videomvvmmodule.utils.Global;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class MusicChildViewModel extends ViewModel {

    public int type = 0;
    public Context context;
    public MusicsCategoryAdapter categoryAdapter = new MusicsCategoryAdapter();
    public MusicsListAdapter musicsListAdapter = new MusicsListAdapter();
    private CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();


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

    public void getFavMusicList(List<String> favouriteMusic) {
        JsonObject jsonObject = new JsonObject();
        JsonArray ids = new JsonArray();
        for (int i = 0; i < favouriteMusic.size(); i++) {
            ids.add(favouriteMusic.get(i));
        }

        jsonObject.add("sound_ids", ids);
        disposable.add(Global.initRetrofit().getFavSoundList(Global.apikey, Global.accessToken, jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new BiConsumer<SearchMusic, Throwable>() {
                    @Override
                    public void accept(SearchMusic soundList, Throwable throwable) throws Exception {
                        if (soundList != null/* && soundList.getStatus()*/ && soundList.getData() != null && !soundList.getData().isEmpty()) {
                            musicsListAdapter.updateData(soundList.getData());
                        }
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
