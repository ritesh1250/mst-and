package com.meest.social.socialViewModel.viewModel.comment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meest.R;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.model.HomeModel;
import com.meest.responses.FeedSubCommentResponse;

import com.meest.responses.SvsSubCommentResponse;
import com.meest.responses.VideoCommentLikeResponse;
import com.meest.responses.VideoSearchResponse;
import com.meest.social.socialViewModel.adapter.comment.VideoCommentAdapter;
import com.meest.social.socialViewModel.adapter.subcomment.ReportAdapter;
import com.meest.social.socialViewModel.model.comment.VideoCommentResponse;


import com.meest.social.socialViewModel.view.comment.VideoCommentReplyActivityNew;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoCommentReplyViewModel extends ViewModel {

    public MutableLiveData<Boolean> loader = new MutableLiveData<>();
    public MutableLiveData<Boolean> showRecyclerView = new MutableLiveData<>();
    public MutableLiveData<Boolean> dataNotFound = new MutableLiveData<>();

    public MutableLiveData<Boolean> setBlank = new MutableLiveData<>();
    public MutableLiveData<Boolean> setData = new MutableLiveData<>();
    public MutableLiveData<Boolean> bindSVSData = new MutableLiveData<>();

    public MutableLiveData<Boolean> isLike = new MutableLiveData<>();

    public MutableLiveData<Boolean> finishActivity = new MutableLiveData<>();

    private final CompositeDisposable disposable = new CompositeDisposable();

    public boolean isSvs = true, isAd = false;

    public String data;
    public String mediaType;

    public ArrayList<VideoSearchResponse.Row> list = new ArrayList<>();

    public VideoCommentAdapter commentAdapter;

    public String videoId = "";

    public int position, commentCount, subcommentCount = 0, totalCount = 0;


    public com.meest.responses.VideoCommentResponse.Row commentData;

    public Map<String, String> header;

    public FeedSubCommentResponse subCommentResponse;

    public HomeModel homeModel = new HomeModel();

    public SvsSubCommentResponse svsSubCommentResponse;

    public void sendFeedSubComment(Context context, String encodedComment) {

        HashMap<String, String> body = new HashMap<>();
        body.put("postId", commentData.getPostId());
        body.put("subCommentId", commentData.getId());
        body.put("comment", encodedComment);
        body.put("status", "true");

        disposable.add(Global.initSocialRetrofit().insertSubComment(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {
                        setBlank.setValue(true);
                        fetchFeedSubComment(context);
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void fetchFeedSubComment(Context context) {

        // creating header
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

        // creating body
        HashMap<String, String> body = new HashMap<>();
        body.put("postId", commentData.getPostId());
        body.put("commentId", commentData.getId());

        disposable.add(Global.initSocialRetrofit().getFeedSubComment(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null) {

                        subCommentResponse = response;

                        // saving count
                        commentCount = subCommentResponse.getData().getCount();
                        setData.setValue(true);

                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void sendAdFeedSubCommet(Context context, String encodedComment) {

        HashMap<String, String> body = new HashMap<>();
        body.put("campaignId", commentData.getCampaignId());
        body.put("parentCommentId", commentData.getId());
        body.put("comment", encodedComment);

        disposable.add(Global.initSocialRetrofit().insertAdSubComment(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {
                        setBlank.setValue(true);
                        fetchAdFeedSubComment(context);
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void fetchAdFeedSubComment(Context context) {

        // creating header
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

        // creating body
        HashMap<String, String> body = new HashMap<>();
        body.put("parentCommentId", commentData.getId());


        disposable.add(Global.initSocialRetrofit().getAdFeedSubComment(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {

                        subCommentResponse = response;
                        homeModel.getFeedResponse().setCommentCounts(subCommentResponse.getData().getCount());
                        setData.setValue(true);

                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void sendSvsSubCommet(Context context, String encodedComment) {

        HashMap<String, String> body = new HashMap<>();
        body.put("videoId", commentData.getVideoId());
        body.put("parentCommentId", commentData.getId());
        body.put("comment", encodedComment);

        disposable.add(Global.initSocialRetrofit().addVideoCommentReply(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {
                        setBlank.setValue(true);
                        fetchSvsComment(context);
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void fetchSvsComment(Context context) {

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

        // creating body
        HashMap<String, String> body = new HashMap<>();
        body.put("commentID", commentData.getId());

        disposable.add(Global.initSocialRetrofit().getSvsSubComment(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {
                        svsSubCommentResponse = response;
                        bindSVSData.setValue(true);
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void likeAPI(Context context) {

        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

        // body
        HashMap<String, Object> body = new HashMap<>();
        if (isAd) {
            body.put("campaignId", commentData.getCampaignId());
            body.put("commentId", commentData.getId());

        } else {
            body.put("commentId", commentData.getId());
            body.put("postId", commentData.getPostId());
            if (commentData.getLiked() == 0) {
                body.put("status", true);
            } else {
                body.put("status", false);
            }
        }

        Single<VideoCommentLikeResponse> call;
        if (isSvs) {
            call = Global.initSocialRetrofit().getAllVideoCommentLike(header, body);
        } else {
            if (isAd) {
                call = Global.initSocialRetrofit().insertAdPostCommentLike(header, body);
            } else {
                call = Global.initSocialRetrofit().insertPostCommentsLike(header, body);
            }
        }

        disposable.add(call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {
                        if (commentData.getLiked() == 0) {
                            isLike.setValue(true);
                            commentData.setLiked(1);
                        } else {
                            isLike.setValue(false);
                            commentData.setLiked(0);
                        }

                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void reportAPI(Context context) {

        final androidx.appcompat.app.AlertDialog.Builder builder = new
                androidx.appcompat.app.AlertDialog.Builder(context, R.style.CustomAlertDialog);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_svs, null, false);

        builder.setView(dialogView);

        androidx.appcompat.app.AlertDialog alertDialog = builder.create();

        TextView report_option = dialogView.findViewById(R.id.report_option);
        ProgressBar progressBar = dialogView.findViewById(R.id.progress_bar);
        RecyclerView report_recycler = dialogView.findViewById(R.id.report_recycler);

        report_option.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Map<String, String> myHeader = new HashMap<>();
        myHeader.put("Accept", "application/json");
        myHeader.put("Content-Type", "application/json");
        myHeader.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

        // body
        String type = "Comment";
        HashMap<String, Object> body = new HashMap<>();
        body.put("reportType", type);
        body.put("reportedData", false);

        disposable.add(Global.initSocialRetrofit().getReportNewTypes(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {
                        ReportAdapter reportAdapter = new ReportAdapter(context,
                                response, alertDialog, progressBar, commentData.getId(), type);
                        report_recycler.setLayoutManager(new LinearLayoutManager(context));
                        report_recycler.setAdapter(reportAdapter);
                        report_recycler.setVisibility(View.VISIBLE);
                    } else {
                        alertDialog.dismiss();
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
        alertDialog.show();
    }

    public void deleteCommment(Context context) {

        View conView = LayoutInflater.from(context).inflate(R.layout.delete_notification_popup, null);
        Button yes = conView.findViewById(R.id.yes);
        Button no = conView.findViewById(R.id.no);
        BottomSheetDialog dia = new BottomSheetDialog(context);

        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

        HashMap<String, String> body = new HashMap<>();
        body.put("commentId", commentData.getId());

        dia.setContentView(conView);

        yes.setOnClickListener(v -> {
            Single<ApiResponse> call;

            if (isSvs) {
                call = Global.initSocialRetrofit().deleteSvsComment(header, body);
            } else {
                if (isAd) {
                    call = Global.initSocialRetrofit().deleteAdPostComment(header, body);
                } else {
                    call = Global.initSocialRetrofit().deletePostComment(header, body);
                }
            }

            disposable.add(call
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable1 -> loader.setValue(true))
                    .doOnTerminate(() -> loader.setValue(false))
                    .subscribe((response, throwable) -> {
                        dia.dismiss();
                        if (response != null && response.getSuccess()) {
                            Utilss.showToast(context, context.getString(R.string.Deleted_Successfully), R.color.grey);
                            finishActivity.setValue(true);
                        } else {
                            Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                        }
                    }));
        });
        no.setOnClickListener(v -> dia.dismiss());
        dia.show();

    }



}
