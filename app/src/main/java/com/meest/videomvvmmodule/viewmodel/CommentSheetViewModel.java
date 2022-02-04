package com.meest.videomvvmmodule.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.databinding.FragmentCommentSheetBinding;
import com.meest.databinding.ItemCommentListBinding;
import com.meest.videomvvmmodule.adapter.CommentAdapter;
import com.meest.videomvvmmodule.model.comment.Comment;
import com.meest.videomvvmmodule.model.user.RestResponse;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CommentSheetViewModel extends ViewModel {
    public String postId;
    public String comment;
    public String commentId = "";
    public ObservableInt commentCount = new ObservableInt(0);
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    public ObservableBoolean isEmpty = new ObservableBoolean(true);
    public ObservableBoolean sendComment = new ObservableBoolean(false);
    public CommentAdapter adapter = new CommentAdapter();
    public int start = 0;
    public MutableLiveData<Boolean> onLoadMoreComplete = new MutableLiveData<>();
    public SessionManager sessionManager;
    int count = 15;
    CompositeDisposable disposable = new CompositeDisposable();


    public void afterCommentTextChanged(CharSequence s) {
        comment = s.toString();
    }

    public void fetchComments(boolean isLoadMore) {
        commentId = "";
        Log.e("print", "count");
        disposable.add(Global.initRetrofit().getPostComments(Global.apikey, postId, count, start)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> {
                    onLoadMoreComplete.setValue(true);
                    isLoading.set(false);
                    sendComment.set(false);
                })
                .subscribe((Comment comment, Throwable throwable) -> {
                    if (comment != null && comment.getData() != null && !comment.getData().isEmpty()) {
                        Log.e("print", "in_comment");
                        if (isLoadMore) {
                            Log.e("print", "in_comment_true");
                            Log.e("print", "===================" + comment.getData());
                            adapter.loadMore(comment.getData());
                        } else {
                            Log.e("print", "in_comment_false");
                            Log.e("print", "===================" + comment.getData());
                            adapter.updateData(comment.getData());
                        }
                        start = start + count;
                    }
                    isEmpty.set(adapter.getmList().isEmpty());
                }));
    }

    public void onLoadMore() {
        fetchComments(true);
    }

    public void addComment(Context context, FragmentCommentSheetBinding binding) {
        if (comment != null) {
            comment = comment.replaceAll("\\s+", " ");
                if (comment.startsWith("@")) {
                    if (!TextUtils.isEmpty(comment.substring(comment.indexOf(" ")).trim())) {
                        callApiToSendComment(binding);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.please_enter_text), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!TextUtils.isEmpty(comment.trim())) {
                        callApiToSendComment(binding);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.please_enter_text), Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    private void callApiToSendComment(FragmentCommentSheetBinding binding) {
        disposable.add(Global.initRetrofit().addComment(Global.apikey, postId, comment, sessionManager.getUser().getData().getFirstName(), commentId, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe((comment, throwable) -> {
                    if (comment != null && comment.getStatus() != null) {
                        Log.d("ADDED", "Success");
                        start = 0;
                        sendComment.set(false);
                        fetchComments(false);
                        onLoadMoreComplete.setValue(false);
                        commentCount.set(commentCount.get() + 1);
                    }
                }));
    }

    public void callApitoDeleteComment(String commentId, int position, Comment.Data comment) {
        disposable.add(Global.initRetrofit().deleteComment(Global.apikey, commentId, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isLoading.set(true))
                .doOnTerminate(() -> isLoading.set(false))
                .subscribe((deleteComment, throwable) -> {
                    if (deleteComment != null && deleteComment.getStatus() != null) {
                        Log.d("DELETED", "Success");
                     /*if(adapter.getmList().size()-1==position){
                            adapter.getmList().remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeRemoved(position, adapter.getmList().size());
                        }*/
                        Log.e("DELETED", "==================Success");
                        adapter.getmList().remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeRemoved(position, adapter.getmList().size());
                        start = 0;
                        fetchComments(false);
                        onLoadMoreComplete.setValue(false);
//                        commentCount.set(commentCount.get() + 1);
                        int size = comment.getComments_reply().size() + 1;
                        if (size > 0) {
                            if (commentCount.get() > size) {
                                commentCount.set(commentCount.get() - size);
                            } else {
                                commentCount.set(size - commentCount.get());
                            }
                        } else {
                            commentCount.set(commentCount.get() - 1);
                        }
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public void likeUnlikePost(String commentsId, Comment.Data model, ItemCommentListBinding binding) {
        disposable.add(Global.initRetrofit().addCommentLike(Global.apikey, postId, commentsId, sessionManager.getUser().getData().getFirstName(), Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((RestResponse likeRequest, Throwable throwable) -> {
                    if (likeRequest.getMessage().equals("Comment like added successfully")) {
                        binding.likebtn.setImageResource(R.drawable.comment_like);
//                        model.set(String.valueOf(Integer.parseInt(model.getPostLikesCount()) + 1));
//                        binding.tvLikeCount.setText(Global.prettyCount(Long.parseLong(model.getPostLikesCount())));
                        model.setCommentLike("1");
                    } else {
                        binding.likebtn.setImageResource(R.drawable.diamond);
//                        model.setPostLikesCount(String.valueOf(Integer.parseInt(model.getPostLikesCount()) - 1));
//                        binding.tvLikeCount.setText(Global.prettyCount(Long.parseLong(model.getPostLikesCount())));
                        model.setCommentLike("0");
                    }
                    Log.e("============like", likeRequest.getMessage());
                }));
    }

}
