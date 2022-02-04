package com.meest.svs.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;

import com.meest.Interfaces.CommentClicked;
import com.meest.R;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.social.socialViewModel.view.comment.VideoCommentActivityNew;
import com.meest.svs.adapters.VideoPlayAdapter;
import com.meest.svs.interfaces.VideoDeleteCallback;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.svs.models.VideoDetailModel;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SVSVideoActivity extends AppCompatActivity implements Player.EventListener,
        VideoDeleteCallback, CommentClicked {

    private static final int COMMENT_OPEN_CODE = 219;
    SwipeRefreshLayout swipeRefresh;
    LinearLayoutManager layoutManager;
    WebApi webAPI;
    int page = 1, total_record, total_page;
    private List<VideoDetailModel> videosList;
    ArrayList<VideoDetailModel> data_list = new ArrayList<>();
    int currentPage = 0;
    RecyclerView rvHomeVideos;
    VideoPlayAdapter videoPlayAdapter;
    ProgressBar p_bar;
    RelativeLayout layout_back;
    boolean isPaused = false, isForYou = false;
    SimpleExoPlayer previousPlayer;
    String videosData;
    LottieAnimationView loading;
    boolean isFirstTime = true;
    SnapHelper pagerSnapHelper;
    Type listType = new TypeToken<List<VideoDetailModel>>(){}.getType();
    private Timer timer;
    private int openedPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.svs_video_activity);

        if (getIntent().getExtras() == null) {
            Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            finish();
        }

        videosData = getIntent().getExtras().getString("videosList");
        currentPage = getIntent().getExtras().getInt("selectedPosition", 0);

        if(currentPage == 0) {
            currentPage = -1;
        }
        videosList = new Gson().fromJson(videosData, listType);
        data_list = new Gson().fromJson(videosData, listType);

        findIds();

        loading.setAnimation("loading.json");
        loading.playAnimation();
        loading.loop(true);

        initUI();

        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rvHomeVideos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //here we find the current item number
                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                int page_no = scrollOffset / height;

                if (page_no != currentPage) {
                    currentPage = page_no;

                    releasePreviousPlayer();
                    setPlayer(currentPage);
                } else if (isFirstTime) {
                    isFirstTime = false;
                    releasePreviousPlayer();
                    setPlayer(currentPage);
                }
            }
        });

        swipeRefresh.setProgressViewOffset(false, 0, 200);

        swipeRefresh.setColorSchemeResources(R.color.black);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                isFirstTime = true;
                releasePreviousPlayer();
                setPlayer(currentPage);
            }
        });
    }

    private void findIds() {
        p_bar = findViewById(R.id.p_bar);
        layout_back = findViewById(R.id.layout_back);
        rvHomeVideos = findViewById(R.id.rvHomeVideos);
        swipeRefresh = findViewById(R.id.swiperefresh);
        loading = findViewById(R.id.loading);
    }

    private void initUI() {
        videoPlayAdapter = new VideoPlayAdapter(SVSVideoActivity.this, videosList, loading,
                this, this);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                super.onLayoutCompleted(state);
                Log.v("harsh", "layout loading completed");
            }
        };
        rvHomeVideos.setLayoutManager(layoutManager);
        rvHomeVideos.setHasFixedSize(false);
        videoPlayAdapter.setHasStableIds(true);
        rvHomeVideos.setAdapter(videoPlayAdapter);
        if(pagerSnapHelper == null) {
            pagerSnapHelper = new PagerSnapHelper();
            pagerSnapHelper.attachToRecyclerView(rvHomeVideos);
        }

        rvHomeVideos.scrollToPosition(currentPage);

        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isPaused) {
            releasePreviousPlayer();
            setPlayer(currentPage);
            isPaused = false;
        }
    }

    // when we swipe for another video this will release the previous player
    public void releasePreviousPlayer() {
        if (previousPlayer != null) {
            previousPlayer.removeListener(this);
            previousPlayer.release();
        }
    }

    public void setPlayer(final int currentPage) {
        swipeRefresh.setRefreshing(false);

        // cancelling timer
        if(timer != null) {
            timer.cancel();
        }

        final VideoDetailModel item = data_list.get(currentPage);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(SVSVideoActivity.this, trackSelector);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(SVSVideoActivity.this,
        Util.getUserAgent(SVSVideoActivity.this, "tunemist"));
        // for mp4 media
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(item.getVideoURL()));
        // for hls media
//        MediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(item.getVideoURL()));

        player.prepare(mediaSource);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);

        View layout = layoutManager.findViewByPosition(currentPage);
        final PlayerView playerView = layout.findViewById(R.id.videoView);

        playerView.setPlayer(player);
        playerView.setUseController(false);

        player.setPlayWhenReady(true);
        previousPlayer = player;


        // final RelativeLayout mainlayout = layout.findViewById(R.id.mainlayout);
        playerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(SVSVideoActivity.this, new GestureDetector.SimpleOnGestureListener() {

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
                this, SharedPrefreances.AUTH_TOKEN));
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
        isPaused = true;
        // stopping timer
        if(timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (previousPlayer != null) {
            previousPlayer.stop();
        }
        // stopping timer
        if(timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void videoDeleted(int position) {
        currentPage = 0;
        isFirstTime = true;

        Utilss.showToast(this, getString(R.string.Deleted_Successfully), R.color.green);

        videosList.remove(position);
        initUI();
    }

    @Override
    public void commentClicked(int position, String id, String caption, boolean isCommentAllowed) {
        openedPosition = position;

        Intent intent = new Intent(this, VideoCommentActivityNew.class);
        intent.putExtra("videoId", id);
        intent.putExtra("msg", caption);
        intent.putExtra("commentCount", data_list.get(position).getComments());
        intent.putExtra("isCommentAllowed", isCommentAllowed);
        intent.putExtra("svs", true);
        startActivityForResult(intent, COMMENT_OPEN_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMMENT_OPEN_CODE && videoPlayAdapter != null && data != null) {
            int commentCount = data.getExtras().getInt("commentCount", 0);
            data_list.get(openedPosition).setComments(commentCount);
            videoPlayAdapter.notifyItemChanged(openedPosition);
        }
    }
}
