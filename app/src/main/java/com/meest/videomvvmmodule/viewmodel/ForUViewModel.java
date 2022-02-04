package com.meest.videomvvmmodule.viewmodel;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.databinding.FragmentForUBinding;
import com.meest.databinding.ItemVideoListBinding;
import com.meest.utils.Helper;
import com.meest.videomvvmmodule.adapter.FamousCreatorAdapter;
import com.meest.videomvvmmodule.adapter.VideoFullAdapter;
import com.meest.videomvvmmodule.adapter.VideoPagerAdapter;
import com.meest.videomvvmmodule.model.user.RestResponse;
import com.meest.videomvvmmodule.model.user.TopCreatorResponse;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ForUViewModel extends ViewModel {
    public Context context;
    public FragmentForUBinding forUBinding;
    public String type;
    public VideoFullAdapter adapter = new VideoFullAdapter();
    public MutableLiveData<Boolean> onLoadMoreComplete = new MutableLiveData<>();
    public int start = 0;
    public int count = 25;
    public int page_count = 1;
    public int commentCount = 0;
    public MutableLiveData<Boolean> followUnfollow=new MutableLiveData<>(false);
    public String postType;
    public ObservableBoolean isFollowingFragment = new ObservableBoolean(false);
    public FamousCreatorAdapter famousAdapter = new FamousCreatorAdapter();
    public MutableLiveData<Boolean> isEmpty = new MutableLiveData<>(false);
    public ObservableBoolean isScrolling = new ObservableBoolean(false);
    public MutableLiveData<RestResponse> coinSend = new MutableLiveData<>();
    public String postId = "";
    public String deepLinkId = "";
    public MutableLiveData<Boolean> onCacheComplete = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Video.Data>> onDataLoad = new MutableLiveData<>();
    public String videoPath;
    public Video.Data videoModel;
    public String audioPath;
    public String soundId;
    public String sendDiamond;
    ObservableBoolean isloading = new ObservableBoolean(true);
    CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<Boolean> isload = new MutableLiveData<>(false);
    public SessionManager sessionManager;
    public boolean isLoadMore = false;
    public Double post_lat;
    public Double post_lng;

    public void reloadSuggestions() {
        famousAdapter.setCount(0);
        if (postType.equals("following")) {
            disposable.add(Global.initRetrofit().getTopCreators(Global.apikey, 0, 50, Global.userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
//                .doOnSubscribe(it -> onLoadMoreComplete.setValue(false))
//                .doOnTerminate(() -> onLoadMoreComplete.setValue(true))
                    .subscribe((TopCreatorResponse topCreatorResponse, Throwable throwable) -> {
                        if (topCreatorResponse != null) {
                            famousAdapter.setmList(topCreatorResponse.getData());
                            famousAdapter.notifyDataSetChanged();
                        }
                    }));
        }
//            Log.e("TAG", "fetchPostVideos: trending user fetched " + postType);
    }

    public void fetchPostVideos(boolean isLoadMore, FragmentForUBinding binding) {
        this.isLoadMore = isLoadMore;
        isloading.set(true);
//        if (postType.equals("following")) {
//            disposable.add(Global.initRetrofit().getTopCreators(Global.apikey, 0, 50, Global.userId)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .unsubscribeOn(Schedulers.io())
////                .doOnSubscribe(it -> onLoadMoreComplete.setValue(false))
////                .doOnTerminate(() -> onLoadMoreComplete.setValue(true))
//                    .subscribe((TopCreatorResponse topCreatorResponse, Throwable throwable) -> {
//                        if (topCreatorResponse != null) {
//                            famousAdapter.setmList(topCreatorResponse.getData());
//                            famousAdapter.notifyDataSetChanged();
//                        }
//                    }));
////            Log.e("TAG", "fetchPostVideos: trending user fetched " + postType);
//        }
        disposable.add(Global.initRetrofit().getPostDeepLinkVideos(Global.apikey, postType, count, start, Global.userId, deepLinkId, post_lat, post_lng)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(it -> onLoadMoreComplete.setValue(false))
                .doOnTerminate(() -> onLoadMoreComplete.setValue(true))
                .subscribe((Video video, Throwable throwable) -> {
                    if (video != null && video.getData() != null && !video.getData().isEmpty()) {
                        /*if (isLoadMore) {
                            adapter.loadMore(video.getData());
                        } else {
                            adapter.updateData(video.getData());
                        }*/
                        isloading.set(false);

//                        count = Integer.parseInt(video.getCount());
//                        page_count = page_count+1;
                        isScrolling.set(true);
                        onDataLoad.setValue(video.getData());
                        if (start == 0)
                            isEmpty.setValue(false);
                        start = start + count;
                        if(!Helper.isNetworkAvailable(context))
                        onCacheComplete.setValue(true);
                    } else {
                        isScrolling.set(false);
                        if (start == 0)
                            isEmpty.setValue(true);
//                        isEmpty.set(Objects.requireNonNull(onDataLoad.getValue()).isEmpty());
//                        Log.e("TAG", "===============fetchPostVideos: " + adapter.getmList().size());
                    }
                }));
    }
   /* private void fetchFamousVideos() {
        disposable.add(Global.initRetrofit().getPostVideos(Global.apikey, "trending", 50, 0, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(it -> onLoadMoreComplete.setValue(false))
                .doOnTerminate(() -> onLoadMoreComplete.setValue(true))
                .subscribe((video, throwable) -> {
                    if (video != null && video.getData() != null && !video.getData().isEmpty()) {
                        famousAdapter.updateData(video.getData());
                    }
                }));
    }*/

    public void onLoadMore() {
        fetchPostVideos(true, null);
    }

    //sessionManager.getUser().getData().getFirstName()
    public void likeUnlikePost(String postId, Context context, Video.Data model, ItemVideoListBinding binding, int position, MutableLiveData<ArrayList<Video.Data>> onDataLoad) {
        disposable.add(Global.initRetrofit().likeUnlike(Global.apikey, postId, sessionManager.getUser().getData().getFirstName(), Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((likeRequest, throwable) -> {
//                    binding.likebtn.setEnabled(true);
//                    int index = position;
//                    for (Video.Data data : Objects.requireNonNull(onDataLoad.getValue())) {
//                        if (data.getPostId().equals(postId)) {
//                            break;
//                        }
//                        index++;
//                    }
                    if (likeRequest.getMessage().equals("Like successful")) {
                        binding.likebtn.setImageResource(R.drawable.filled_diamond_icon);
                        model.setPostLikesCount(String.valueOf(Integer.parseInt(model.getPostLikesCount()) + 1));
                        binding.tvLikeCount.setText(Global.prettyCount(Long.parseLong(model.getPostLikesCount())));
                        model.setVideoIsLiked(1);
                    } else if (likeRequest.getMessage().equals("Unlike successful")) {
                        binding.likebtn.setImageResource(R.drawable.ic_diamond);
                        model.setPostLikesCount(String.valueOf(Integer.parseInt(model.getPostLikesCount()) - 1));
                        binding.tvLikeCount.setText(Global.prettyCount(Long.parseLong(model.getPostLikesCount())));
                        model.setVideoIsLiked(0);
                    } else {
                        Toast.makeText(context, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
//                    Objects.requireNonNull(onDataLoad.getValue()).set(position, model);
//                    Log.e("===========like", "done");
                }));
    }

    public void sendBubble(String toUserId, String coin) {

        disposable.add(Global.initRetrofit().sendCoin(Global.apikey, coin, toUserId, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(it -> onLoadMoreComplete.setValue(false))
                .doOnTerminate(() -> {
                    onLoadMoreComplete.setValue(true);
                })
                .subscribe((coinSend, throwable) -> {
                    if (coinSend != null && coinSend.getStatus() != null) {
                        this.coinSend.setValue(coinSend);
                    }
                }));
    }

    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(100);
        animate.setFillAfter(true);

        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                view.setVisibility(View.GONE);

            }
        });

        view.startAnimation(animate);
    }

    public void followUnfollow(Video.Data model, Context context, ItemVideoListBinding binding, VideoPagerAdapter videoPagerAdapter, int pos) {
//        isloading.set(true);
        disposable.add(Global.initRetrofit().followUnFollow(Global.apikey, Global.userId, model.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> isload.setValue(true))
                .doOnTerminate(() -> isload.setValue(false))
                .unsubscribeOn(Schedulers.io())
                .subscribe((followRequest, throwable) -> {
                    String tempfollowornot = "";

                    if (followRequest != null && followRequest.getStatus() != null) {
                        followUnfollow.setValue(true);
//                        isloading.set(false);
                        if (followRequest.getMessage().equalsIgnoreCase("Follow Successfully")) {
                            model.setFollowOrNot("1");
                            binding.tvFollowUnfollow.setText(context.getResources().getString(R.string.Following));
                            Log.e("===========like", "1");
                            Global.followUnfollow.put(model.getUserId(), true);
                            Toast.makeText(context, context.getResources().getString(R.string.Follow_Successfully), Toast.LENGTH_SHORT).show();

                            tempfollowornot = "1";
                        } else if (followRequest.getMessage().equals("Unfollow Successfully")) {
                            model.setFollowOrNot("0");
                            binding.tvFollowUnfollow.setText(context.getResources().getString(R.string.Follow));
                            Log.e("===========like", "0");
                            Toast.makeText(context, context.getResources().getString(R.string.Unfollow_Successfully), Toast.LENGTH_SHORT).show();
                            Global.followUnfollow.put(model.getUserId(), false);

//                            videoPagerAdapter.removeFragment(pos);

                            tempfollowornot = "0";
                        }

                        Video.Data temp;
                        List<Video.Data> requireNonNull = Objects.requireNonNull(onDataLoad.getValue());
                        for (int i = 0; i < requireNonNull.size(); i++) {
                            Video.Data data = requireNonNull.get(i);
                            if (model.getUserId().equals(data.getUserId())) {
                                temp = data;
                                temp.setFollowOrNot(tempfollowornot);
                                onDataLoad.getValue().set(i, temp);
                            }
                        }

                        videoPagerAdapter.notifyDataSetChanged();
//                        start=0;
//                        fetchPostVideos(false);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public void addView(Video.Data model, ItemVideoListBinding binding) {
//        Log.e("====addview", "call");
        disposable.add(Global.initRetrofit().addView(Global.apikey, model.getPostId(), Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((addView, throwable) -> {
                    if (addView != null && addView.getStatus() != null) {
//                        Log.e("====addviewStatus", "call");
//                        model.setPostViewCount(model.getPostViewCount() + 1);
                        binding.tvWatchViews.setText(Global.prettyCount(model.getPostViewCount()));
                       /* if(model.getPostViewCount().equals("1")){
                            binding.tvWatchViews.setText("0");
                        }else {
                            int counts = Integer.parseInt(model.getPostViewCount());
                            if ( counts % 2 == 0 ){
                                counts = counts/2;
                            }else {
                                counts = (counts+1)/2;
                            }
                            binding.tvWatchViews.setText(Global.prettyCount(Long.parseLong(String.valueOf(counts))));
                        }*/
                    }
                }));
    }
}
