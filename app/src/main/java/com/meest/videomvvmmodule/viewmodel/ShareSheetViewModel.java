package com.meest.videomvvmmodule.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.databinding.ItemVideoListBinding;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ShareSheetViewModel extends ViewModel {

    public Video.Data video;
    public MutableLiveData<Integer> onItemClick = new MutableLiveData<>();
    public String shareUrl;
    CompositeDisposable disposable = new CompositeDisposable();

    public MutableLiveData<ArrayList<Video.Data>> onDataLoad = new MutableLiveData<>();

    public void setOnItemClick(int type) {
        onItemClick.setValue(type);
    }
    public void sharePost(String postId, Context context, Video.Data model, ItemVideoListBinding binding, int position) {
        disposable.add(Global.initRetrofit().sharePost(Global.apikey, Global.userId, "post", postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((shareResponse, throwable) -> {
                    if (shareResponse != null && shareResponse.getStatus() != null) {
                        if (shareResponse.getStatus()) {
                            model.setShareCount(String.valueOf(Integer.parseInt(model.getShareCount()) + 1));
                            binding.tvShareCount.setText(Global.prettyCount(Long.parseLong(model.getShareCount())));
                        }
//                        Objects.requireNonNull(onDataLoad.getValue()).set(position, model);
                    }
                }));
    }

}
