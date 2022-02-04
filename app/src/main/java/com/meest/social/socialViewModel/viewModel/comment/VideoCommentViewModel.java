package com.meest.social.socialViewModel.viewModel.comment;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.responses.VideoSearchResponse;
import com.meest.social.socialViewModel.adapter.comment.VideoCommentAdapter;
import com.meest.social.socialViewModel.model.comment.VideoCommentResponse;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VideoCommentViewModel extends ViewModel {

    public MutableLiveData<Boolean> loader = new MutableLiveData<>();
    public MutableLiveData<Boolean> showRecyclerView = new MutableLiveData<>();
    public MutableLiveData<Boolean> dataNotFound = new MutableLiveData<>();

    public MutableLiveData<Boolean> setBlank = new MutableLiveData<>();
    public MutableLiveData<Boolean> setData = new MutableLiveData<>();

    private final CompositeDisposable disposable = new CompositeDisposable();

    public boolean isSvs;
    public String data;
    public String mediaType;

    public ArrayList<VideoSearchResponse.Row> list = new ArrayList<>();

    public VideoCommentAdapter commentAdapter;

    public String videoId = "";

    public int position, commentCount, subcommentCount = 0, totalCount = 0;

    public VideoCommentResponse videoCommentResponse;

    public void addPostComment(Context context, String encodedComment) {

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");

        HashMap<String, String> body = new HashMap<>();
        body.put("postId", videoId);
        body.put("comment", encodedComment);
        body.put("status", "true");


        disposable.add(Global.initSocialRetrofit().insertComment(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {
                        setBlank.setValue(true);
                        fetchPostComments(context);
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }


    public void fetchPostComments(Context context) {

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");

        // creating body
        HashMap<String, String> body = new HashMap<>();
        body.put("postId", videoId);

        disposable.add(Global.initSocialRetrofit().getComments(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {

                        videoCommentResponse = response;

                        // saving count
                        commentCount = videoCommentResponse.getData().getCount();
                        subcommentCount=0;
                        for (int i = 0; i < response.getData().getRows().size(); i++) {
                            if (response.getData().getRows().get(i).getSubCount() != null) {
                                subcommentCount = subcommentCount + response.getData().getRows().get(i).getSubCount();
                            }
                        }
                        totalCount = commentCount + subcommentCount;

                        setData.setValue(true);

                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void addAdPostComment(Context context, String encodedComment) {

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(
                context, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");

        HashMap<String, String> body = new HashMap<>();
        body.put("campaignId", videoId);
        body.put("comment", encodedComment);
        body.put("status", "true");

        disposable.add(Global.initSocialRetrofit().insertAdComment(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {
                        setBlank.setValue(true);
                        fetchAdComments(context);
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void fetchAdComments(Context context) {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");

        // creating body
        HashMap<String, String> body = new HashMap<>();
        body.put("campaignId", videoId);

        disposable.add(Global.initSocialRetrofit().getAdComments(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {
                        videoCommentResponse = response;

                        // saving count
                        commentCount = videoCommentResponse.getData().getCount();
                        setData.setValue(true);

                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void addVideoComment(Context context, String encodedComment) {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(
                context, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");

        HashMap<String, String> body = new HashMap<>();
        body.put("videoId", videoId);
        body.put("comment", encodedComment);
        body.put("status", "true");

        disposable.add(Global.initSocialRetrofit().addVideoComment(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {
                        setBlank.setValue(true);
                        fetchVideoComments(context);
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void fetchVideoComments(Context context) {

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

        // creating body
        HashMap<String, String> body = new HashMap<>();
        body.put("videoId", videoId);

        disposable.add(Global.initSocialRetrofit().getVideoComments(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {
                        videoCommentResponse = response;
                        // saving count
                        commentCount = videoCommentResponse.getData().getCount();
                        setData.setValue(true);
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }


}
