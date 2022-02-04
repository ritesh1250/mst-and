package com.meest.videomvvmmodule.viewmodel;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.videomvvmmodule.adapter.MusicsListAdapter;
import com.meest.videomvvmmodule.model.music.Musics;
import com.meest.videomvvmmodule.utils.Global;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MusicMainViewModel extends ViewModel {
    public ObservableBoolean isSearch = new ObservableBoolean(false);
    public ObservableBoolean isMore = new ObservableBoolean(false);
    public ObservableBoolean isSearchEmpty = new ObservableBoolean(false);
    public MusicsListAdapter searchMusicAdapter = new MusicsListAdapter();
    public MutableLiveData<Musics.SoundList> music = new MutableLiveData<>();
    public MutableLiveData<Boolean> stopMusic = new MutableLiveData<>();
    private String searchText = "";
    private CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public void onSearchTextChanged(CharSequence text) {
        isSearchEmpty.set(text.toString().isEmpty());
        searchText = text.toString();
        callApiForSearchMusic();
    }

    private void callApiForSearchMusic() {

        if (!disposable.isDisposed()) {
            disposable.clear();
        }
        disposable.add(Global.initRetrofit().searchSoundList(Global.apikey, Global.accessToken, searchText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((musicList, throwable) -> {
                    if (musicList != null && musicList.getData() != null && !musicList.getData().isEmpty()) {
                        searchMusicAdapter.updateData(musicList.getData());
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

