package com.meest.videomvvmmodule.viewmodel;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.databinding.ActivityVideoCommentReplyBinding;
import com.meest.videomvvmmodule.adapter.ReplyCommentAdapter;
import com.meest.videomvvmmodule.model.comment.VideoCommentResponse;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.home.VideoCommentReplyActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import io.reactivex.disposables.CompositeDisposable;

public class VideoReplyCommentViewModel extends ViewModel {
    public String comment;
    public VideoCommentResponse.Data.Row commentData;
    public boolean isSvs;
    public String encodedComment;
    public ObservableInt commentCount = new ObservableInt(0);
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    public ObservableBoolean isEmpty = new ObservableBoolean(true);
    public ReplyCommentAdapter adapter = new ReplyCommentAdapter();
    public int start = 0;
    public MutableLiveData<Boolean> onLoadMoreComplete = new MutableLiveData<>();
    public SessionManager sessionManager;
    private int count = 15;
    private CompositeDisposable disposable = new CompositeDisposable();

    public void afterCommentTextChanged(CharSequence s) {
        comment = s.toString();
    }

    public void addComment(VideoCommentReplyActivity context) {
        if (!TextUtils.isEmpty(comment)) {
            try {
                encodedComment = URLEncoder.encode(comment,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            callApiToSendComment();
        }else {
            Toast.makeText(context, context.getResources().getString(R.string.Please_Enter_Something), Toast.LENGTH_SHORT).show();
        }
    }

/*    private void callApiToSendComment() {
        HashMap<String, String> body = new HashMap<>();
        body.put("videoId", commentData.getVideoId());
        body.put("parentCommentId", commentData.getId());
        body.put("comment", encodedComment);
        disposable.add(Global.initRetrofit().addVideoCommentReply(Global.apikey, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe((comment, throwable) -> {
                    if (comment != null && comment.getSuccess() != null) {
//                        Log.d("ADDED", "Success");
//                        start = 0;
                        fetchSubComments(false);
                        onLoadMoreComplete.setValue(false);
                        commentCount.set(commentCount.get() + 1);
                    }
                }));
    }*/

  /*  public void onLoadMore() {
        fetchSubComments(false);
    }*/

/*    public void fetchSubComments(boolean isLoadMore) {
        // creating body
        HashMap<String, String> body = new HashMap<>();
        body.put("commentID", commentData.getId());
        disposable.add(Global.initRetrofit().getVideoSubComments(Global.apikey, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> {
                    onLoadMoreComplete.setValue(true);
                    isLoading.set(false);
                })
                .subscribe((VideoSubCommentResponse comment, Throwable throwable) -> {

                    if (comment != null && comment.getData() != null && !comment.getData().isEmpty()) {
                     *//*   if (isLoadMore) {
//                            adapter.loadMore(comment.getData().getRows());
                            adapter.loadMore(comment.getData().getRows());
                        } else {
                            adapter.updateData(comment.getData().getRows());
                        }
                        start = start + count;*//*
                        adapter.updateData(comment.getData());
                    }
                    isEmpty.set(adapter.getmList().isEmpty());
                }));

     *//*   disposable.add(Global.initRetrofit().getVideoComments(Global.apikey, postId, count, start)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> {
                    onLoadMoreComplete.setValue(true);
                    isLoading.set(false);
                })
                .subscribe((comment, throwable) -> {

                    if (comment != null && comment.getData() != null && !comment.getData().isEmpty()) {

                        if (isLoadMore) {
                            adapter.loadMore(comment.getData());
                        } else {
                            adapter.updateData(comment.getData());
                        }
                        start = start + count;
                    }
                    isEmpty.set(adapter.getmList().isEmpty());

                }));*//*
    }*/

  /*  public void callApitoDeleteComment(String commentId, VideoCommentReplyActivity context,int position) {
        HashMap<String, String> body = new HashMap<>();
        body.put("commentId", commentId);
        disposable.add(Global.initRetrofit().deleteVideoSubComment(Global.apikey, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe((deleteComment, throwable) -> {
                    if (deleteComment != null && deleteComment.getSuccess() != null) {
//                        Log.d("DELETED", "Success");
                        adapter.getmList().remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeRemoved(position, adapter.getmList().size());
                        commentCount.set(commentCount.get() - 1);
                        Toast.makeText(context, "Comment successfully deleted!!", Toast.LENGTH_SHORT).show();
                    }
                }));
    }*/

    public void setData(ActivityVideoCommentReplyBinding binding, VideoCommentReplyActivity context){
        if(commentData.liked()){
            binding.txtLike.setText(context.getResources().getString(R.string.Unlike));
            binding.txtLike.setTextColor(ContextCompat.getColor(context, R.color.colorTheme2));
            commentData.setLiked("1");
        }else {
            binding.txtLike.setText(context.getResources().getString(R.string.Like));
            binding.txtLike.setTextColor(ContextCompat.getColor(context, R.color.color_text_light));
            commentData.setLiked("0");
        }
    }

   /* public void callApiToLikeComment(ActivityVideoCommentReplyBinding binding, VideoCommentReplyActivity context) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("commentId", commentData.getId());
        body.put("videoId", commentData.getVideoId());
        body.put("status", true);
        disposable.add(Global.initRetrofit().getVideoCommentLike(Global.apikey, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe((VideoCommentLikeResponse likeComment, Throwable throwable) -> {
                    if (likeComment != null && likeComment.getSuccess()) {
//                        Log.d("DELETED", "Success");
                        if(!commentData.liked()){
                            binding.txtLike.setText("Unlike");
                            binding.txtLike.setTextColor(ContextCompat.getColor(context, R.color.colorTheme2));
                            commentData.setLiked("1");
                        }else {
                            binding.txtLike.setText("Like");
                            binding.txtLike.setTextColor(ContextCompat.getColor(context, R.color.color_text_light));
                            commentData.setLiked("0");
                        }
                        *//*holder.txt_like.setText("Unlike");
                        holder.txt_like.setTextColor(context.getResources().getColor(R.color.blue));
                        videoCommentResponse.getData().getRows().get(position).setLiked(1);*//*
                  *//*      adapter.getmList().remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeRemoved(position, adapter.getmList().size());
                        commentCount.set(commentCount.get() - 1);*//*
                    }
                }));
    }*/

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

}
