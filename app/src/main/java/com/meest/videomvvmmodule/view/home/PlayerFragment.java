package com.meest.videomvvmmodule.view.home;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.meest.Meeast;
import com.meest.R;
import com.meest.databinding.ItemVideoListBinding;
import com.meest.utils.Helper;
import com.meest.videomvvmmodule.adapter.VideoFullAdapter;
import com.meest.videomvvmmodule.customview.transformation.BlurTransformation;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.CommonUtils;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.viewmodel.HomeViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PlayerFragment extends Fragment implements Player.EventListener {
    public VideoFullAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick;
    int minBufTime = 900;
    int maxBufTime = 1000;
    int buffForPlayTime = 700;
    int buffForPlayAfterTime = 700;
    ItemVideoListBinding binding;
    private HomeViewModel parentViewModel;
    private Bundle bundle;
    private Video.Data data;
    private SimpleExoPlayer player;
    private SimpleCache simpleCache;
    private CacheDataSourceFactory cacheDataSourceFactory;
    private String proxyUrl;
    private static int position = 0;
    private CustomDialogBuilder customDialogBuilder;
    public int MIN_BUFFER_DURATION = 1000;
    //Max Video you want to buffer during PlayBack
    public int MAX_BUFFER_DURATION = 5000;
    //Min Video you want to buffer before start Playing it
    public int MIN_PLAYBACK_START_BUFFER = 750;
    //Min video You want to buffer when user resumes video
    public int MIN_PLAYBACK_RESUME_BUFFER = 750;

    CompositeDisposable disposable = new CompositeDisposable();

    public PlayerFragment(VideoFullAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    public PlayerFragment() {
        // doesn't do anything special
    }

    public static PlayerFragment newInstance(Video.Data video, VideoFullAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick, int position1) {
        if (position1 != 0)
            position = position1 - 1;
        PlayerFragment playerFragment = new PlayerFragment(onRecyclerViewItemClick);
        Bundle bundle = new Bundle();
        bundle.putString("video", new Gson().toJson(video));
        playerFragment.setArguments(bundle);
        return playerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.item_video_list, container, false);
        parentViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        customDialogBuilder = new CustomDialogBuilder(getContext());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            bundle = getArguments();
            data = new Gson().fromJson(bundle.getString("video"), Video.Data.class);
        }
        binding.setModel(data);
        String url = "";
        if (data.getThumbnail_image() != null) {
            url = data.getThumbnail_image();
        } else {
            url = data.getPostVideo();
        }

        if (new SessionManager(getContext()).getBooleanValue(Const.IS_LOGIN)) {
            if (!Global.userId.equals(data.getUserId())) {
//                binding.imgSendBubble.setVisibility(View.VISIBLE);
                binding.viewGift.setVisibility(View.VISIBLE);
                binding.flOption.setVisibility(View.VISIBLE);
            } else {
                binding.viewGift.setVisibility(View.GONE);
                binding.flOption.setVisibility(View.VISIBLE);
            }
        } else {
//            binding.imgSendBubble.setVisibility(View.VISIBLE);
            binding.viewGift.setVisibility(View.VISIBLE);
            binding.flOption.setVisibility(View.VISIBLE);
        }

        Glide.with(binding.getRoot().getContext()).load(url)
                .transform(new BlurTransformation())
//                    .apply(
//                            new RequestOptions().error(
//                                    R.color.transparent
//                            ).priority(Priority.HIGH)
//                    )
                .into(binding.ivThumb);

//        Glide.with(binding.getRoot().getContext()).load(data.getUserProfile())
//                .placeholder(R.drawable.dp_bg_user)
//                  .into(binding.imgUser);

        if (new SessionManager(getContext()).getBooleanValue(Const.IS_LOGIN)) {
            if (!Global.userId.equals(data.getUserId())) {
//              binding.imgSendBubble.setVisibility(View.VISIBLE);
                binding.viewGift.setVisibility(View.VISIBLE);
                binding.flOption.setVisibility(View.VISIBLE);
            } else {
                binding.viewGift.setVisibility(View.GONE);
                binding.flOption.setVisibility(View.VISIBLE);
            }
        } else {
//            binding.imgSendBubble.setVisibility(View.VISIBLE);
            binding.viewGift.setVisibility(View.VISIBLE);
            binding.flOption.setVisibility(View.VISIBLE);
        }

        binding.tvSoundName.setSelected(true);
        binding.tvLikeCount.setText(Global.prettyCount(data.getDummyLikeCount()));
        binding.tvCommentCount.setText(Global.prettyCount(data.getPostCommentsCount()));
        binding.tvShareCount.setText(Global.prettyCount(Integer.valueOf(data.getShareCount())));

        if (data.getFollowOrNot()) {
            binding.tvFollowUnfollow.setText(R.string.Following);
        } else {
            binding.tvFollowUnfollow.setText(R.string.follow);
        }

        /*binding.fabButton.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector gestureDetector1 = new GestureDetector(binding.getRoot().getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    onRecyclerViewItemClick.onItemClick(data, 11, binding, position);
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    onRecyclerViewItemClick.onItemClick(data, 11, binding, position);
                    super.onLongPress(e);
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    return true;
                }
            });
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector1.onTouchEvent(event);
                return false;
            }
        });*/

        binding.ivPause.setOnClickListener(v -> {
            if (player != null) {
                player.setPlayWhenReady(!player.getPlayWhenReady());
                if (player.getPlayWhenReady()) {
                    binding.ivPause.setVisibility(View.GONE);
                } else {
                    binding.ivPause.setVisibility(View.VISIBLE);
                }
//                Toast.makeText(getContext(), "hhhhhh", Toast.LENGTH_SHORT).show();
            }
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = () -> {
            binding.ivUnmute.setVisibility(View.GONE);
        };
        
//        currentvolume = player.getVolume();
        binding.playerView.setOnTouchListener(new View.OnTouchListener() {
            final GestureDetector gestureDetector = new GestureDetector(binding.getRoot().getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (player != null) {
                        if (player.getVolume() == 0f) {
                            player.setVolume(Global.currentVolume);
                            Global.isMuted = false;
                            binding.ivUnmute.setVisibility(View.VISIBLE);
                            binding.ivUnmute.setImageResource(R.drawable.ic_mute_off_icon_medley);
                            handler.removeCallbacks(runnable);
                            handler.postDelayed(runnable, 2000);
                        } else {
                            Global.currentVolume = player.getVolume();
                            player.setVolume(0f);
                            Global.isMuted = true;
                            binding.ivUnmute.setVisibility(View.VISIBLE);
                            binding.ivUnmute.setImageResource(R.drawable.ic_mute_on_icon_medley);
                            handler.removeCallbacks(runnable);
                            handler.postDelayed(runnable, 2000);
                        }
                    }
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
//                    onRecyclerViewItemClick.onItemClick(data, 8, binding, position);
                    if (player != null) {
                        player.setPlayWhenReady(!player.getPlayWhenReady());
                        if (player.getPlayWhenReady()) {
                            binding.ivPause.setVisibility(View.GONE);
                        } else {
                            binding.ivPause.setVisibility(View.VISIBLE);
                        }
                    }
                    super.onLongPress(e);
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    onRecyclerViewItemClick.onDoubleClick(data, e, binding);
                    if (player != null) {
                        player.setPlayWhenReady(true);
                        if (player.getPlayWhenReady()) {
                            binding.ivPause.setVisibility(View.GONE);
                        }
                    }
                    return true;
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });

//        binding.tvDescreption.setOnHashtagClickListener((view, text) -> onRecyclerViewItemClick.onHashTagClick(text.toString()));

        binding.imgUser.setOnClickListener(view -> onRecyclerViewItemClick.onItemClick(data, 1, binding, position, player));
        binding.loutUser.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(data, 1, binding, position, player));
//        binding.fabButton.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(data, 1, binding, position, player));

        binding.imgSendBubble.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(data, 3, binding, position, player));
        binding.flOption.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(data, 11, binding, position, player));
        binding.duoOpen.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(data, 12, binding, position, player));
//        binding.viewReport.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(data, 8, binding, position,player));
        binding.flFollowButton.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(data, 9, binding, position, player));
        binding.imgSendBubble.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(data, 3, binding, position, player));
//        binding.likebtn.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(data, 4, binding, position));

        binding.tvProfileCategory.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(data, 13, binding, position, player));

      /*  binding.viewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player != null) {
                    player.setPlayWhenReady(false);
                    if (player.getPlayWhenReady()) {
                        binding.ivPause.setVisibility(View.GONE);
                    } else {
                        binding.ivPause.setVisibility(View.VISIBLE);
                    }
                }
                onRecyclerViewItemClick.onItemClick(data, 8, binding, position,player);
            }
        });
      */
        binding.likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                binding.likebtn.setEnabled(false);
                v.setEnabled(false);
                new Handler().postDelayed(() -> {
                    //Do your work
                    v.setEnabled(true);
                }, 2000);
                onRecyclerViewItemClick.onItemClick(data, 4, binding, position, player);
            }
        });

        if (data.getVideoIsLiked()) {
//                binding.likebtn.setLiked(true);
            binding.likebtn.setImageResource(R.drawable.filled_diamond_icon);
        } else {
//                binding.likebtn.setLiked(false);
            binding.likebtn.setImageResource(R.drawable.ic_diamond);
        }

        /*binding.likebtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                onRecyclerViewItemClick.onItemClick(data, 4, binding, position);
                data.setPostLikesCount(String.valueOf(Integer.parseInt(data.getPostLikesCount()) + 1));
                binding.tvLikeCount.setText(Global.prettyCount(Long.parseLong(data.getPostLikesCount())));
                data.setVideoIsLiked(1);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                onRecyclerViewItemClick.onItemClick(data, 4, binding, position);
                data.setPostLikesCount(String.valueOf(Integer.parseInt(data.getPostLikesCount()) - 1));
                binding.tvLikeCount.setText(Global.prettyCount(Long.parseLong(data.getPostLikesCount())));
                data.setVideoIsLiked(0);
            }
        });*/

        binding.viewComment.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(data, 5, binding, position, player));
        binding.viewShare.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(data, 6, binding, position, player));
        binding.llSound.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(data, 7, binding, position, player));

        if (data.getPostViewCount() != null && data.getPostViewCount() != 0) {
            binding.llViews.setVisibility(View.VISIBLE);
        } else {
            binding.llViews.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(data.getPostDescription())) {
            binding.llDescription.setVisibility(View.VISIBLE);
        } else {
            binding.llDescription.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(data.getSoundTitle())) {
            binding.llSound.setVisibility(View.VISIBLE);
        } else {
            binding.llSound.setVisibility(View.GONE);
        }

//        if (!TextUtils.isEmpty(data.getPostHashTag())) {
//            binding.tvDescreption.setVisibility(View.VISIBLE);
//        } else {
//            binding.tvDescreption.setVisibility(View.GONE);
//        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(binding.soundIcon,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleX", 1.2f));
        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();
       /* if(data.getPostViewCount().equals("1")){
            binding.tvWatchViews.setText("0");
        }else {
            int counts = Integer.parseInt(data.getPostViewCount());
            if ( counts % 2 == 0 ){
                counts = counts/2;
            }else {
                counts = (counts+1)/2;
            }
            binding.tvWatchViews.setText(Global.prettyCount(Long.parseLong(String.valueOf(counts))));
        }*/

//        binding.fabButton.animate().scaleX(1.0f);
//        binding.fabButton.animate().scaleY(1.0f);
        prePair();
//        if (position == 1) {
//            if (SharedPrefreances.getSharedPreferenceString((MainVideoActivity) getContext(), MEDLEY_VIDEO_INTRO) == null || SharedPrefreances.getSharedPreferenceString((MainVideoActivity) getContext(), MEDLEY_VIDEO_INTRO).equals("")) {
//                TapTargetSequence tapTargetSequence = new TapTargetSequence((MainVideoActivity) binding.getRoot().getContext()).targets(
//                        //getTargetView(this, img_home, getResources().getString(R.string.homeIntro), ""),
//                        //getTargetView(this, img_search, getResources().getString(R.string.searchIntro), ""),
//                        //getTargetView(this, img_coming_soon, getResources().getString(R.string.comingSoon), ""),
//                        getTargetView((MainVideoActivity) getContext(), binding.imgSendBubble, getContext().getString(R.string.giftIntro), "", 40),
//                        getTargetView((MainVideoActivity) getContext(), binding.fabButton, getContext().getString(R.string.spinButtonIntro), "")
//                        //getTargetView(this, img_notification, getResources().getString(R.string.notificationIntro), ""),
//                        //getTargetView(this, ivUser, getResources().getString(R.string.profileIntro), "")
//                ).listener(new TapTargetSequence.Listener() {
//                    @Override
//                    public void onSequenceFinish() {
//                        SharedPrefreances.setSharedPreferenceString((MainVideoActivity) getContext(), MEDLEY_VIDEO_INTRO, "false");
//
//                    }
//
//                    @Override
//                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
//
//                    }
//
//                    @Override
//                    public void onSequenceCanceled(TapTarget lastTarget) {
//
//                    }
//                });
//                tapTargetSequence.start();
//            }
//        }
    }

    private void prePair() {
        Animation animation = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.slow_rotate);
        binding.imgSound.startAnimation(animation);

        if (getActivity() != null && data != null && data.getPostVideo() != null && !data.getPostVideo().isEmpty()) {
            HttpProxyCacheServer proxy = Meeast.getProxy(getActivity());
            proxyUrl = proxy.getProxyUrl(data.getPostVideo());
//            LoadControl loadControl = new DefaultLoadControl.Builder()
//                    .setAllocator(new DefaultAllocator(true, 16))
//                    .setBufferDurationsMs(1 * 1024, 1 * 1024, 500, 1024)
//                    .setTargetBufferBytes(-1)
//                    .setPrioritizeTimeOverSizeThresholds(true)
//                    .createDefaultLoadControl();
//
//            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
//            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            //Minimum Video you want to buffer while Playing
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);

            LoadControl loadControl = new DefaultLoadControl.Builder()
                    .setAllocator(new DefaultAllocator(true, 16))
                    .setBufferDurationsMs(MIN_BUFFER_DURATION,
                            MAX_BUFFER_DURATION,
                            MIN_PLAYBACK_START_BUFFER,
                            MIN_PLAYBACK_RESUME_BUFFER)
                    .setTargetBufferBytes(-1)
                    .setPrioritizeTimeOverSizeThresholds(true).createDefaultLoadControl();

            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), getActivity().getResources().getString(R.string.app_name)));

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(proxyUrl));

            binding.playerView.setPlayer(player);
            player.seekTo(0, 0);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
            binding.playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            player.prepare(videoSource);
            player.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                    Log.e(TAG, "onPlayerStateChanged: " + playbackState);
                    if (playbackState == Player.STATE_READY) {
                        long durationMillis = player.getDuration();
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis);

                        if (Global.isMuted) {
                            player.setVolume(0f);
                        }

                        if (player.getPlayWhenReady()) {
                            binding.ivPause.setVisibility(View.GONE);
                        } else {
                            binding.ivPause.setVisibility(View.VISIBLE);
                        }
//                        Log.e("durationMillis", "======================================" + seconds + "  " + player.getCurrentPosition());
//                        Log.e("post ID", "======================================" + data.getPostId());
//                        customDialogBuilder.hideLoadingDialog();
                    } else if (playbackState == Player.STATE_BUFFERING) {
                        binding.progress.setVisibility(View.VISIBLE);
//                        customDialogBuilder.showLoadingDialog();
                    }
                }
            });
//            isWatched=true;
            player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        /*long durationMillis = player.getDuration();
        Log.e("durationMillis","======================================"+durationMillis);*/
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(requireContext(), "player fragmentt====================="+data.getUserId(), Toast.LENGTH_SHORT).show();
//        if (Global.followUnfollow.containsKey(data.getUserId())) {
//            if (Global.followUnfollow.get(data.getUserId())) {
//                binding.tvFollowUnfollow.setText(R.string.Following);
//            } else {
//                binding.tvFollowUnfollow.setText(R.string.follow);
//            }
//        }
        if (new SessionManager(getContext()).getBooleanValue(Const.IS_LOGIN)) {
            if (Helper.isNetworkAvailable(requireContext())) {
                disposable.add(Global.initRetrofit().getPostByIdNew(Global.apikey, data.getPostId(), Global.userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe((video, throwable) -> {
                            data = video.getData().get(0);
                            binding.setModel(data);
                            if (data.getVideoIsLiked()) {
//                binding.likebtn.setLiked(true);
                                binding.likebtn.setImageResource(R.drawable.filled_diamond_icon);
                            } else {
//                binding.likebtn.setLiked(false);
                                binding.likebtn.setImageResource(R.drawable.ic_diamond);
                            }
                            binding.tvSoundName.setSelected(true);
                            binding.tvLikeCount.setText(Global.prettyCount(data.getDummyLikeCount()));
                            binding.tvCommentCount.setText(Global.prettyCount(data.getPostCommentsCount()));
                            binding.tvShareCount.setText(Global.prettyCount(Integer.valueOf(data.getShareCount())));

                            if (data.getFollowOrNot()) {
                                binding.tvFollowUnfollow.setText(R.string.Following);
                            } else {
                                binding.tvFollowUnfollow.setText(R.string.follow);
                            }
                        }));
            }
        }
        if (player != null) {
            player.seekToDefaultPosition();
            player.setPlayWhenReady(true);
            binding.ivPause.setVisibility(View.INVISIBLE);
        } else {
            prePair();
        }

        new Handler().postDelayed(() -> {
            //Do your work
            if (!data.getUserId().equals(Global.userId)) {
                onRecyclerViewItemClick.onItemClick(data, 13, binding, position, player);
            }
        }, 5000);
        binding.progress.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            player.setPlayWhenReady(false);
            binding.ivPause.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }

}