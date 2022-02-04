package com.meest.videomvvmmodule.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.meest.R;
import com.meest.databinding.ItemVideoListBinding;
import com.meest.videomvvmmodule.adapter.VideoFullAdapter;
import com.meest.videomvvmmodule.model.user.RestResponse;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VideoPlayerViewModel extends ViewModel {
    public SessionManager sessionManager;
    public VideoFullAdapter adapter = new VideoFullAdapter();
    public ArrayList<Video.Data> list = new ArrayList<>();
    public int position = 0;
    public int type = 0;
    public MutableLiveData<Boolean> onCommentSuccess = new MutableLiveData<>();
    public MutableLiveData<Boolean> onLoadMoreComplete = new MutableLiveData<>();
    public MutableLiveData<RestResponse> coinSend = new MutableLiveData<>();
    public int count = 10;
    public int start = 0;
    public String userId = "";
    public String hashTag = "";
    public String soundId = "";
    public String keyword = "";
    public String postId;
    public String videoPath;
    public Video.Data videoModel;
    public String sendDiamond;
    public ArrayList<Video.Data> list2;
    String comment;
    CompositeDisposable disposable = new CompositeDisposable();

    public void handleType(Intent intent) {
        switch (type) {
            //UserVideos
            //UserLikesVideos
            case 0:
            case 1:
                userId = intent.getStringExtra("user_id");
                break;
            //HasTagVideo
            case 2:
                hashTag = intent.getStringExtra("hash_tag");
                break;
            //SearchVideos
            case 3:
                keyword = intent.getStringExtra("keyword");
                break;
            //SoundVideos
            case 4:
                soundId = intent.getStringExtra("sound_id");
                break;
            //Single Post
            case 5:
                break;
        }
    }

    public void onLoadMore() {
        switch (type) {
            //UserVideos
            case 0:
                getUserVideos();
                break;
            //UserLikesVideos
            case 1:
                getUserLikesVideos();
                break;
            //HasTagVideo
            case 2:
                getHasTagVideo();
                break;
            //SearchVideos
            case 3:
                getSearchVideos();
                break;
            //SoundVideos
            case 4:
                getSoundVideos();
                break;
            //SoundVideos
            case 5:

                break;
        }
    }


    public void getUserVideos() {

        disposable.add(Global.initRetrofit().getUserVideos(Global.apikey, userId, count, start, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnTerminate(() -> onLoadMoreComplete.setValue(true))
                .subscribe((video, throwable) -> {
                    if (video != null && video.getData() != null && !video.getData().isEmpty()) {
                        adapter.loadMore(video.getData());
                        start = start + count;
                    }
                }));
    }

    public void getUserLikesVideos() {
        disposable.add(Global.initRetrofit().getUserLikedVideos(Global.apikey, userId, count, start, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnTerminate(() -> onLoadMoreComplete.setValue(true))
                .subscribe((video, throwable) -> {
                    if (video != null && video.getData() != null && !video.getData().isEmpty()) {
                        adapter.loadMore(video.getData());
                        start = start + count;
                    }
                }));
    }

    public void afterUserNameTextChanged(CharSequence s) {
        comment = s.toString();
    }

    public void getHasTagVideo() {
        disposable.add(Global.initRetrofit().fetchHasTagVideo(Global.apikey, hashTag, count, start, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnTerminate(() -> onLoadMoreComplete.setValue(true))
                .subscribe((video, throwable) -> {
                    if (video != null && video.getData() != null && !video.getData().isEmpty()) {
                        adapter.loadMore(video.getData());
                        start = start + count;
                    }
                }));
    }

    public void getSearchVideos() {
        disposable.add(Global.initRetrofit().searchVideo(Global.apikey, keyword, count, start, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnTerminate(() -> onLoadMoreComplete.setValue(true))
                .subscribe((video, throwable) -> {
                    if (video != null && video.getData() != null && !video.getData().isEmpty()) {
                        adapter.loadMore(video.getData());
                        start = start + count;
                    }
                }));
    }

    public void getSoundVideos() {
        disposable.add(Global.initRetrofit().getSoundVideos(Global.apikey, count, start, soundId, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnTerminate(() -> onLoadMoreComplete.setValue(true))
                .subscribe((video, throwable) -> {
                    if (video != null && video.getData() != null && !video.getData().isEmpty()) {
                        adapter.loadMore(video.getData());
                        start = start + count;
                    }
                }));
    }

    public void sendBubble(String toUserId, String coin) {
        sendDiamond = coin;
        disposable.add(Global.initRetrofit().sendCoin(Global.apikey, coin, toUserId, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnTerminate(() -> onLoadMoreComplete.setValue(true))
                .subscribe((coinSend, throwable) -> {
                    if (coinSend != null && coinSend.getStatus() != null) {
                        this.coinSend.setValue(coinSend);
                    }
                }));
    }

    public void likeUnlikePost(String postId, Context context, Video.Data model, ItemVideoListBinding binding, int position) {
        disposable.add(Global.initRetrofit().likeUnlike(Global.apikey, postId, sessionManager.getUser().getData().getFirstName(), Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((likeRequest, throwable) -> {
                    binding.likebtn.setEnabled(true);

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
//                    list.set(position, model);

                }));
    }

    public void addComment() {
      /*  if (!TextUtils.isEmpty(comment)) {
            callApiToSendComment();
        }*/
    }

//    public void showFABMenu(ItemVideoListBinding binding) {
////        isFABOpen = true;
//        binding.rectcurveFab.setVisibility(View.VISIBLE);
//        binding.viewLike.setVisibility(View.VISIBLE);
//        binding.viewComment.setVisibility(View.VISIBLE);
//        binding.viewShare.setVisibility(View.VISIBLE);
//        binding.viewReport.setVisibility(View.VISIBLE);
////        fabBGLayout.setVisibility(View.VISIBLE);
//        binding.fabButton.animate().rotationBy(360);
//
//        binding.fabButton.animate().scaleX(1f);
//        binding.fabButton.animate().scaleY(1f);
////        binding.fabButton.setRotation(180);
//        slideUp(binding.rectcurveFab);
//    }

//    public void closeFABMenu(ItemVideoListBinding binding) {
////        isFABOpen = false;
////        fabBGLayout.setVisibility(View.GONE);
//        binding.viewLike.setVisibility(View.INVISIBLE);
//        binding.viewComment.setVisibility(View.INVISIBLE);
//        binding.viewShare.setVisibility(View.INVISIBLE);
//        binding.viewReport.setVisibility(View.INVISIBLE);
//        binding.fabButton.animate().rotation(0);
//        binding.fabButton.animate().scaleX(1.0f);
//        binding.fabButton.animate().scaleY(1.0f);
//        slideDown(binding.rectcurveFab);
//    }

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

//    public void slideUp(View view) {
//        view.setVisibility(View.VISIBLE);
//        TranslateAnimation animate = new TranslateAnimation(
//                0,                 // fromXDelta
//                0,                 // toXDelta
//                view.getHeight(),  // fromYDelta
//                0);                // toYDelta
//        animate.setDuration(100);
//        animate.setFillAfter(true);
//        view.startAnimation(animate);
//    }

/*    public void showFABMenu(ItemVideoListBinding binding) {
        binding.rectcurveFab1.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                binding.rectcurveFab1.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(360);
        animate.setFillAfter(true);
        binding.rectcurveFab1.startAnimation(animate);
        binding.viewLike.setVisibility(View.VISIBLE);
        binding.viewComment.setVisibility(View.VISIBLE);
        binding.viewShare.setVisibility(View.VISIBLE);
        binding.viewReport.setVisibility(View.VISIBLE);
        binding.fabView.setVisibility(View.GONE);
        binding.fabButton.animate().rotationBy(360);

        binding.fabButton.animate().scaleX(1f);
        binding.fabButton.animate().scaleY(1f);

        binding.viewLike.animate().rotationBy(0);
        binding.viewComment.animate().rotationBy(0);
        binding.viewShare.animate().rotationBy(0);
        binding.viewReport.animate().rotationBy(0);

        binding.viewShare.animate().translationY(-420);
//        binding.viewLike.animate().translationY(-380);
        binding.viewLike.animate().translationY(-135);
        binding.viewComment.animate().translationY(-280);
        binding.viewReport.animate().translationY(-560);
    }

    public void closeFABMenu(ItemVideoListBinding binding) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                binding.rectcurveFab1.getHeight()); // toYDelta
        animate.setDuration(90);
        animate.setFillAfter(true);
        binding.rectcurveFab1.startAnimation(animate);
        binding.rectcurveFab1.setVisibility(View.GONE);
        binding.fabView.setVisibility(View.GONE);
        binding.fabButton.animate().rotation(0);
        binding.fabButton.animate().scaleX(1.0f);
        binding.fabButton.animate().scaleY(1.0f);

        //fabLayout1.animate().rotationBy(360);

        binding.viewLike.animate().translationY(0);
        binding.viewComment.animate().translationY(0);
        binding.viewShare.animate().translationY(0);
        binding.viewReport.animate().translationY(0);
       *//* binding.viewShare.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!ForUFragment.isFABOpen) {

                    binding.viewLike.setVisibility(View.INVISIBLE);
                    binding.viewComment.setVisibility(View.INVISIBLE);
                    binding.viewShare.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {
api
            }
        });*//*
    }*/

    public void followUnfollow(Video.Data model, Context context, ItemVideoListBinding binding, FragmentStateAdapter videoPagerAdapter) {
        disposable.add(Global.initRetrofit().followUnFollow(Global.apikey, Global.userId, model.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((followRequest, throwable) -> {
                    String tempfollowornot = "";

                    if (followRequest != null && followRequest.getStatus() != null) {
                        // Toast.makeText(context, followRequest.getMessage(), Toast.LENGTH_SHORT).show();

                        if (followRequest.getMessage().equals("Follow Successfully")) {
                            model.setFollowOrNot("1");
                            binding.tvFollowUnfollow.setText(context.getString(R.string.following));
                            Toast.makeText(context, context.getResources().getString(R.string.Follow_Successfully), Toast.LENGTH_SHORT).show();
                            Global.followUnfollow.put(userId, true);
                            tempfollowornot = "1";
                        } else if (followRequest.getMessage().equals("Unfollow Successfully")) {
                            model.setFollowOrNot("0");
                            binding.tvFollowUnfollow.setText(context.getString(R.string.follow));
                            Toast.makeText(context, context.getResources().getString(R.string.Unfollow_Successfully), Toast.LENGTH_SHORT).show();
                            Global.followUnfollow.put(userId, false);
                            tempfollowornot = "0";
                        }

                        Video.Data temp;
                        List<Video.Data> requireNonNull = Objects.requireNonNull(list);
                        for (int i = 0; i < requireNonNull.size(); i++) {
                            Video.Data data = requireNonNull.get(i);
                            if (model.getUserId().equals(data.getUserId())) {
                                temp = data;
                                temp.setFollowOrNot(tempfollowornot);
                                list.set(i, temp);
                            }
                        }
                        videoPagerAdapter.notifyDataSetChanged();
                    }
                }));
    }

    public void addView(Video.Data model, ItemVideoListBinding binding) {
        disposable.add(Global.initRetrofit().addView(Global.apikey, model.getPostId(), Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((addView, throwable) -> {
                    if (addView != null && addView.getStatus() != null) {
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

    public void sharePost(String postId, Video.Data model, ItemVideoListBinding binding, int position) {
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
                        try {
                            list.set(position, model);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }));
    }
}
