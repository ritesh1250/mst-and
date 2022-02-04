package com.meest.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;

import com.meest.Adapters.HomeVideoAdapter;
import com.meest.Interfaces.CommentClicked;
import com.meest.Interfaces.FragmentCommunication;
import com.meest.R;
import com.meest.responses.VideosResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.social.socialViewModel.view.comment.VideoCommentActivityNew;
import com.meest.svs.interfaces.VideoDeleteCallback;
import com.meest.meestbhart.login.model.ApiResponse;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.meest.Meeast.simpleCache;

public class VideoPostFragment extends Fragment implements Player.EventListener, VideoDeleteCallback, CommentClicked {

    private static final int COMMENT_OPEN_CODE = 28;
    SwipeRefreshLayout swipeRefresh;
    LinearLayoutManager layoutManager;
    WebApi webAPI;
    int page = 1, total_record, total_page;
    List<VideosResponse.Row> videoList = new ArrayList<>();
    ArrayList<VideosResponse.Row> data_list = new ArrayList<>();
    int currentPage = -1;
    RecyclerView rvHomeVideos;
    HomeVideoAdapter homeVideoAdapter;
    ProgressBar p_bar;
    RelativeLayout txt_for_you;
    LinearLayout img_search;
    RelativeLayout txt_for_you_explore;
    TextView txt_for_you_1_explore, txt_for_you_1;
    RelativeLayout layout_back;
    LottieAnimationView loading;
    LinearLayout layout_bottom_video;

    boolean isPaused = false, isForYou = false;

    public static SimpleExoPlayer previousPlayer;
    private Context context;
    private FragmentCommunication fragmentCommunication;
    private Timer timer;
    private View view;
    private int openedPosition = 0;


    int pastVisibleItemCount = 0;
    int totalItemCount = 0;
    int visibleItemCount = 0;
    int pagePerRecord = 10;
    int pageno = 1;
    boolean Loading = true;
    int totalCount = 0;

    public VideoPostFragment(Context context, FragmentCommunication fragmentCommunication) {
        this.context = context;
        this.fragmentCommunication = fragmentCommunication;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.video_fragment, null);

        rvHomeVideos = view.findViewById(R.id.rvHomeVideos);
        p_bar = view.findViewById(R.id.p_bar);
        loading = view.findViewById(R.id.loading);
        layout_bottom_video = view.findViewById(R.id.layout_bottom_video);


        loading.setAnimation("loading.json");
        loading.playAnimation();
        loading.loop(true);

        SharedPrefreances.setSharedPreferenceString(context, "back", "2");
        SharedPrefreances.setSharedPreferenceString(context, "VideoPostActive", "1");
        initUI();
        txt_for_you_1_explore = view.findViewById(R.id.txt_for_you_1_explore);
        txt_for_you_1 = view.findViewById(R.id.txt_for_you_1);
        layout_back = view.findViewById(R.id.layout_back);
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentCommunication.initAnimation();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onPause();
                    }
                }, 1000);


            }
        });
        img_search = view.findViewById(R.id.img_search);
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txt_for_you = view.findViewById(R.id.txt_for_you);

        txt_for_you_explore = view.findViewById(R.id.txt_for_you_explore);
        txt_for_you_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isForYou) {
                    return;
                }

                isForYou = false;
                isPaused = true;
                txt_for_you_1_explore.setTypeface(Typeface.DEFAULT_BOLD);
                txt_for_you_1.setTypeface(Typeface.DEFAULT);
                getVideoAPI(String.valueOf(pageno));
            }
        });

        txt_for_you.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isForYou) {
                    return;
                }

                isForYou = true;
                isPaused = true;
                txt_for_you_1.setTypeface(Typeface.DEFAULT_BOLD);
                txt_for_you_1_explore.setTypeface(Typeface.DEFAULT);

                getVideoAPI(String.valueOf(pageno));
            }
        });

        rvHomeVideos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                System.out.println("==PAGINATION STARTED " + pageno);
                if (Loading) {
                    Loading = false;
                    pageno = pageno + 1;
                    getVideoAPI(String.valueOf(pageno));
                }
            }

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //here we find the current item number
                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                int page_no = scrollOffset / height;

                if (page_no != currentPage) {
                    currentPage = page_no;

                    releasePreviousPlayer();
                    setPlayer(currentPage);
                }
            }
        });

        swipeRefresh = view.findViewById(R.id.swiperefresh);
        swipeRefresh.setProgressViewOffset(false, 0, 200);

        swipeRefresh.setColorSchemeResources(R.color.black);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                txt_for_you_1_explore.setTextColor(getContext().getResources().getColor(R.color.white));
                txt_for_you_1.setTextColor(getContext().getResources().getColor(R.color.gray));
                currentPage = -1;
                getVideoAPI(String.valueOf(pageno));
            }
        });
        return view;
    }


    private void initUI() {
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        homeVideoAdapter = new HomeVideoAdapter(context, videoList, loading, VideoPostFragment.this, VideoPostFragment.this);
        webAPI = ApiUtils.getClient().create(WebApi.class);
        rvHomeVideos.setLayoutManager(layoutManager);
        rvHomeVideos.setHasFixedSize(false);
        homeVideoAdapter.setHasStableIds(true);
        rvHomeVideos.setAdapter(homeVideoAdapter);
        SnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rvHomeVideos);
    }

    @Override
    public void onResume() {
        super.onResume();
        getVideoAPI(String.valueOf(pageno));

        if (HomeFragments.previousPlayer != null) {
            HomeFragments.previousPlayer.release();
        }
    }

    private void getVideoAPI(String pageno) {
        if (!com.meest.utils.Network.isNetworkAvailable(context)) {
            try {

                if (page == 1) {
                    videoList.clear();
                }

                VideosResponse response = new Gson().fromJson(SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.CACHE_FOR_TIKTOK), VideosResponse.class);

                total_record = Integer.parseInt(String.valueOf(response.getData().getCount()));
                int tempTotal = total_record % 30;
                if (tempTotal != 0) {
                    total_page = (total_record / 30) + 1;
                } else {
                    total_page = (total_record / 30);
                }
                videoList.addAll(response.getData().getRows());
                homeVideoAdapter.notifyDataSetChanged();
                data_list.addAll(response.getData().getRows());
                isPaused = true;
                if (isPaused) {
                    releasePreviousPlayer();
                    setPlayer(currentPage);
                    isPaused = false;
                }
            } catch (Exception c) {
                c.printStackTrace();
            }
            return;
        }
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
        HashMap<String, Object> body = new HashMap<>();

        body.put("forYou", isForYou);
        body.put("svs", true);
        body.put("pageNumber", pageno);
        body.put("pageSize", pagePerRecord);
        body.put("userId", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.ID));

        Call<VideosResponse> call;

        call = webAPI.getAllVideosForYou(header, body);

        call.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                swipeRefresh.setRefreshing(false);
                if (response.body() != null) {

                    if (response.body().getSuccess()) {
                        if (page == 1) {
                            videoList.clear();
                        }
                        total_record = Integer.parseInt(String.valueOf(response.body().getData().getCount()));
                        int tempTotal = total_record % 30;
                        if (tempTotal != 0) {
                            total_page = (total_record / 30) + 1;
                        } else {
                            total_page = (total_record / 30);
                        }
                        String jsonObjectForSave = new Gson().toJson(response.body());
                        SharedPrefreances.setSharedPreferenceString(context, SharedPrefreances.CACHE_FOR_TIKTOK, jsonObjectForSave);
                        videoList.addAll(response.body().getData().getRows());
                        homeVideoAdapter.notifyDataSetChanged();

                        data_list.addAll(response.body().getData().getRows());

                        if (isPaused) {
                            releasePreviousPlayer();
                            setPlayer(currentPage);
                            isPaused = false;
                        }
                    } else {
                        Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                    }
                } else {
                    Utilss.showErrorToast(context);
                }
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Utilss.showErrorToast(context);
            }
        });
    }

    // when we swipe for another video this will relaese the privious player
    public void releasePreviousPlayer() {
        if (previousPlayer != null) {
            previousPlayer.removeListener(this);
            previousPlayer.release();
        }
    }

    public void startPlayer() {
        currentPage = -1;
    }

    public void setPlayer(final int currentPage) {

        if (currentPage > data_list.size() || currentPage == -1) {
            return;
        }

        // cancelling timer
        if (timer != null) {
            timer.cancel();
        }

        final SimpleExoPlayer player;
        MediaSource mediaSource;
        final VideosResponse.Row item;
        item = data_list.get(currentPage);

        if (false) {

            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            DefaultLoadControl loadControl = new DefaultLoadControl.Builder().setBufferDurationsMs(32 * 1024, 64 * 1024, 1024, 1024).createDefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "tunemist"));


            // for mp4 media
            mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(item.getVideoURL()));

            player.prepare(mediaSource);
            // for hls media
//        MediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(item.getVideoURL()));
        } else {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

            CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(
                    simpleCache, new DefaultHttpDataSourceFactory(Util.getUserAgent(context, "exo")));
//            MediaSource audioSource = new ExtractorMediaSource(Uri.parse(item.getVideoURL()), new com.app.meestApp.Utils.CacheDataSourceFactory( context, 200 * 1024 * 1024, 10 * 1024 * 1024), new DefaultExtractorsFactory(), null, null);

            ProgressiveMediaSource mediaSource1 = new ProgressiveMediaSource.Factory(cacheDataSourceFactory).createMediaSource(Uri.parse(item.getVideoURL()));

            player.prepare(mediaSource1, true, true);
        }

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);


        View layout = layoutManager.findViewByPosition(currentPage);
        final PlayerView playerView;
        if (layout == null) {
            Utilss.showToast(context, "Something went wrong, please try again later", R.color.msg_fail);
            return;
        }
        View centerView = layout.findViewById(R.id.player_center_view);
        playerView = layout.findViewById(R.id.videoView);


        playerView.setPlayer(player);
        playerView.setUseController(false);

        player.setPlayWhenReady(true);
        previousPlayer = player;

        // detecting touch on center layout
        centerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    super.onFling(e1, e2, velocityX, velocityY);
                    float deltaX = e1.getX() - e2.getX();
                    float deltaXAbs = Math.abs(deltaX);
                    // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe
                    if ((deltaXAbs > 100) && (deltaXAbs < 1000)) {
                        if (deltaX > 0) {
                            //OpenProfile(item,true);
                        }
                    }


                    return true;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    super.onSingleTapUp(e);
                    if (!player.getPlayWhenReady()) {
                        previousPlayer.setPlayWhenReady(true);
                    } else {
                        previousPlayer.setPlayWhenReady(false);
                    }


                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                    //Show_video_option(item);

                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    if (!player.getPlayWhenReady()) {
                        previousPlayer.setPlayWhenReady(true);
                    }
                    return super.onDoubleTap(e);

                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        // setting timer for adding view
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                addViewToVideo(item.getId());
            }
        }, 5000);
    }

    private void addViewToVideo(String videoId) {
        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(
                context, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");

        HashMap<String, Object> body = new HashMap<>();
        body.put("videoId", videoId);
        body.put("svs", true);

        Call<ApiResponse> addCommentCall = webApi1.addViewToVideo(header, body);
        addCommentCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
            }
        });
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {
            p_bar.setVisibility(View.VISIBLE);
        } else if (playbackState == Player.STATE_READY) {
            p_bar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onStop() {
        super.onStop();
        if (previousPlayer != null) {
            previousPlayer.stop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (previousPlayer != null) {
            previousPlayer.stop();
        }
        // stopping timer
        if (timer != null) {
            timer.cancel();
        }
        isPaused = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (previousPlayer != null) {
            previousPlayer.stop();
        }
        // stopping timer
        if (timer != null) {
            timer.cancel();
        }
        isPaused = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (previousPlayer != null) {
            previousPlayer.stop();
        }
        // stopping timer
        if (timer != null) {
            timer.cancel();
        }
        isPaused = false;
    }

    @Override
    public void videoDeleted(int position) {

    }

    @Override
    public void commentClicked(int position, String id, String caption, boolean isCommentAllowed) {
        openedPosition = position;

        Intent intent = new Intent(context, VideoCommentActivityNew.class);
        intent.putExtra("videoId", id);
        intent.putExtra("msg", caption);
        intent.putExtra("commentCount", videoList.get(position).getComments());
        intent.putExtra("isCommentAllowed", isCommentAllowed);
        intent.putExtra("svs", true);
        startActivityForResult(intent, COMMENT_OPEN_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMMENT_OPEN_CODE && homeVideoAdapter != null && data != null) {
            int commentCount = data.getExtras().getInt("commentCount", 0);
            videoList.get(openedPosition).setComments(commentCount);
            homeVideoAdapter.notifyItemChanged(openedPosition);
        }
    }
}
