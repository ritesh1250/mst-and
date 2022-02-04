package com.meest.svs.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import com.meest.MainActivity;
import com.meest.R;
import com.meest.responses.AddLikeResponse;
import com.meest.responses.FollowResponse;
import com.meest.Services.DownloadVideo;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.social.socialViewModel.view.comment.VideoCommentActivityNew;
import com.meest.svs.models.AudioDataModel;
import com.meest.svs.models.VideoByPostIdResponse;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationSvsActivity extends AppCompatActivity implements Player.EventListener {
    CircleImageView ivUser;
    TextView tvUsername, tvLikes, tvComments, tvDesc, hashtags, musicName, txt_follow, views;
    ImageView ivPlay, ivDiscover, ivLike, ivMore, ivMusic, option_menu;
    LinearLayout viewLike, viewComment, viewShare, layout_profile;
    RelativeLayout main;
    CircularProgressBar cpb;
    RelativeLayout viewProgressbar;
    com.meest.utils.CircularProgressBar downloadProgressbar;
    ScrollView scrollView;
    CardView ivMusicLayout;
    VideoByPostIdResponse videoByPostIdResponse;
    ImageView img_back_arrow_Photo;
    private static final int COMMENT_OPEN_CODE = 219;
    boolean isPaused = false;
    SimpleExoPlayer previousPlayer;
    String videoId;
    LottieAnimationView loading;
    private Timer timer;
    private PlayerView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_svs);
        videoId = getIntent().getExtras().getString("videoId");

        loading = findViewById(R.id.loading);
        loading.setAnimation("loading.json");
        loading.playAnimation();
        loading.loop(true);


        img_back_arrow_Photo = findViewById(R.id.img_back_arrow_Photo);
        img_back_arrow_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findIds();
        fetchVideo();


    }

    private void fetchVideo() {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");

        HashMap<String, String> body = new HashMap<>();
        body.put("videoId", videoId);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<VideoByPostIdResponse> call = webApi.getVideoData(header, body);
        call.enqueue(new Callback<VideoByPostIdResponse>() {
            @Override
            public void onResponse(Call<VideoByPostIdResponse> call, Response<VideoByPostIdResponse> response) {

                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {

                    videoByPostIdResponse = response.body();
                    bindData();
                } else {
                    Utilss.showToast(NotificationSvsActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<VideoByPostIdResponse> call, Throwable t) {
                Utilss.showToast(NotificationSvsActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void bindData() {
        tvUsername.setText("@" + videoByPostIdResponse.getData().getRows().get(0).getUser().getUsername());
        tvLikes.setText(videoByPostIdResponse.getData().getRows().get(0).getLikes().toString());
        tvComments.setText(videoByPostIdResponse.getData().getRows().get(0).getComments().toString());
        views.setText(videoByPostIdResponse.getData().getRows().get(0).getViews().toString());
        tvDesc.setText(videoByPostIdResponse.getData().getRows().get(0).getDescription().toString());


        if (!videoByPostIdResponse.getData().getRows().get(0).getUser().getDisplayPicture().equals("")) {
            Glide.with(getApplicationContext()).
                    load(videoByPostIdResponse.getData().getRows().get(0).getUser().getDisplayPicture()).
                    thumbnail(0.1f).
                    into(ivUser);
        }

        if (videoByPostIdResponse.getData().getRows().get(0).getAudio() != null) {
            musicName.setText(videoByPostIdResponse.getData().getRows().get(0).getAudio().getDescription());
        }

        if (videoByPostIdResponse.getData().getRows().get(0).getIsFriend() > 0) {
            txt_follow.setText(getString(R.string.Unfollow));
        } else {
            txt_follow.setText(getString(R.string.Follow));
        }


        if (videoByPostIdResponse.getData().getRows().get(0).getIsLiked() > 0) {
            ivLike.setImageResource(R.drawable.like_diamond_filled);
        } else {
            ivLike.setImageResource(R.drawable.diamond);
        }

        views.setText(String.valueOf(videoByPostIdResponse.getData().getRows().get(0).getViews()));
        if (videoByPostIdResponse.getData().getRows().get(0).getHashtags() != null && videoByPostIdResponse.getData().getRows().get(0).getHashtags().size() > 0) {
            StringBuilder hashTagText = new StringBuilder();
            for (String tag : videoByPostIdResponse.getData().getRows().get(0).getHashtags()) {
                hashTagText.append("#").append(tag).append(" ");
            }

            // setting hashtag
            hashtags.setText(hashTagText.toString());
        } else {
            hashtags.setVisibility(View.GONE);
        }


        boolean showDialog = false;

        try {
            if (videoByPostIdResponse.getData().getRows().get(0).getIsAllowDownload()) {
                showDialog = true;
            }
            if (videoByPostIdResponse.getData().getRows().get(0).getIsAllowDuet()) {
                showDialog = true;
            }
            if (videoByPostIdResponse.getData().getRows().get(0).getIsAllowTrio()) {
                showDialog = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!showDialog) {
            viewShare.setVisibility(View.GONE);
        }


        ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(ivMusic,
                "rotation", 0f, 360f);
        imageViewObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        imageViewObjectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        imageViewObjectAnimator.setDuration(1500);
        imageViewObjectAnimator.setInterpolator(new LinearInterpolator());
        imageViewObjectAnimator.start();

        setPlayer();


        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TAg", "maintavideoshare=" + "hhhhhh");
                new AlertDialog.Builder(NotificationSvsActivity.this)
                        .setMessage("Create Duo, Trio or Download video")
                        .setPositiveButton("Make Trio", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new DownloadVideo(NotificationSvsActivity.this, "trio", new AudioDataModel()).execute(videoByPostIdResponse.getData().getRows().get(0).getVideoURL());
                            }
                        })
                        .setNegativeButton("Make Duo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new DownloadVideo(NotificationSvsActivity.this, "duo", new AudioDataModel()).execute(videoByPostIdResponse.getData().getRows().get(0).getVideoURL());
                            }
                        }).setNeutralButton("Download Video", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DownloadVideo(NotificationSvsActivity.this, "download", new AudioDataModel()).execute(videoByPostIdResponse.getData().getRows().get(0).getVideoURL());
                    }
                }).show();
            }
        });

        txt_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);

                Map<String, String> header = new HashMap<>();
                header.put("Accept", "application/json");
                header.put("Content-Type", "application/json");
                header.put("x-token", SharedPrefreances.getSharedPreferenceString(NotificationSvsActivity.this, SharedPrefreances.AUTH_TOKEN));
                // body
                HashMap<String, String> body = new HashMap<>();
                body.put("followingId", videoByPostIdResponse.getData().getRows().get(0).getUserId());
                body.put("svs", String.valueOf(true));
                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<FollowResponse> call;
                if (videoByPostIdResponse.getData().getRows().get(0).getIsFriend() == 0) {
                    call = webApi.followRequest(header, body);
                } else {
                    body.put("status", String.valueOf(false));
                    call = webApi.removeFollow(header, body);
                }
                call.enqueue(new Callback<FollowResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<FollowResponse> call, @NotNull Response<FollowResponse> response) {
                        loading.setVisibility(View.GONE);

                        if (response.code() == 200 && response.body().getSuccess()) {
                            if (videoByPostIdResponse.getData().getRows().get(0).getIsFriend() == 0) {
                                videoByPostIdResponse.getData().getRows().get(0).setIsFriend(1);
                                txt_follow.setText(getString(R.string.Unfollow));
                            } else {
                                videoByPostIdResponse.getData().getRows().get(0).setIsFriend(0);
                                txt_follow.setText("");
                                txt_follow.setText(getString(R.string.Follow));
                            }
                        } else {
                            Utilss.showToast(NotificationSvsActivity.this, getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<FollowResponse> call, @NotNull Throwable t) {
                        loading.setVisibility(View.GONE);

                        Utilss.showToast(NotificationSvsActivity.this, getString(R.string.error_msg), R.color.grey);
                    }
                });
            }
        });
        viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationSvsActivity.this, VideoCommentActivityNew.class);
                intent.putExtra("videoId", videoId);
                intent.putExtra("msg", videoByPostIdResponse.getData().getRows().get(0).getDescription());
                intent.putExtra("commentCount", videoByPostIdResponse.getData().getRows().get(0).getComments());
                intent.putExtra("isCommentAllowed", videoByPostIdResponse.getData().getRows().get(0).getIsAllowComment());
                intent.putExtra("svs", true);
                startActivityForResult(intent, COMMENT_OPEN_CODE);
            }
        });

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);

                Map<String, String> header = new HashMap<>();
                header.put("Accept", "application/json");
                header.put("Content-Type", "application/json");
                header.put("x-token", SharedPrefreances.getSharedPreferenceString(NotificationSvsActivity.this, SharedPrefreances.AUTH_TOKEN));

                // body
                HashMap<String, String> body = new HashMap<>();
                body.put("videoId", videoByPostIdResponse.getData().getRows().get(0).getId());
                body.put("svs", String.valueOf(true));

                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<AddLikeResponse> call = webApi.getLikeVideo(header, body);
                call.enqueue(new Callback<AddLikeResponse>() {
                    @Override
                    public void onResponse(Call<AddLikeResponse> call, Response<AddLikeResponse> response) {
                        loading.setVisibility(View.GONE);

                        if (response.code() == 200 && response.body().getSuccess()) {
                            if (videoByPostIdResponse.getData().getRows().get(0).getIsLiked() == 0) {
                                videoByPostIdResponse.getData().getRows().get(0).setIsLiked(1);
                                ivLike.setImageResource(R.drawable.like_diamond_filled);
                            } else {
                                videoByPostIdResponse.getData().getRows().get(0).setIsLiked(0);
                                ivLike.setImageResource(R.drawable.diamond);
                            }
                            tvLikes.setText(String.valueOf(response.body().getData().getLikeCount()));
                        } else {
                            Utilss.showToast(NotificationSvsActivity.this, getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<AddLikeResponse> call, Throwable t) {
                        loading.setVisibility(View.GONE);

                        Utilss.showToast(NotificationSvsActivity.this, getString(R.string.error_msg), R.color.grey);
                    }
                });
            }
        });

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationSvsActivity.this, VideoOtherUserActivity.class);
                intent.putExtra("userId", videoByPostIdResponse.getData().getRows().get(0).getUserId());
                startActivity(intent);
            }
        });
    }


    private void findIds() {
        ivUser = findViewById(R.id.ivUser);
        tvUsername = findViewById(R.id.tvUsername);
        layout_profile = findViewById(R.id.layout_profile);
        tvDesc = findViewById(R.id.tvDesc);
        ivMore = findViewById(R.id.ivMore);
        ivMusic = findViewById(R.id.ivMusic);
        txt_follow = findViewById(R.id.txt_follow);
        ivPlay = findViewById(R.id.ivPlay);
        tvLikes = findViewById(R.id.tvLikes);
        tvComments = findViewById(R.id.tvComments);
        option_menu = findViewById(R.id.option_menu);
        viewLike = findViewById(R.id.viewLike);
        viewComment = findViewById(R.id.viewComment);
        viewShare = findViewById(R.id.viewShare);
        views = findViewById(R.id.txt_videowatch);
        hashtags = findViewById(R.id.hashtags);
        ivMusicLayout = findViewById(R.id.ivMusicLayout);
        musicName = findViewById(R.id.musicName);
        cpb = findViewById(R.id.cpb);
        main = findViewById(R.id.main);
        viewProgressbar = findViewById(R.id.viewProgressbar);
        downloadProgressbar = findViewById(R.id.downloadProgressbar);
        ivDiscover = findViewById(R.id.ivDiscover);
        ivLike = findViewById(R.id.ivLike);
        scrollView = findViewById(R.id.scrollView);
        videoView = findViewById(R.id.videoView);

    }

    public void setPlayer() {
        //   swipeRefresh.setRefreshing(false);

        // cancelling timer
        if (timer != null) {
            timer.cancel();
        }

        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(NotificationSvsActivity.this, trackSelector);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(NotificationSvsActivity.this,
                Util.getUserAgent(NotificationSvsActivity.this, "tunemist"));
        // for mp4 media
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoByPostIdResponse.getData().getRows().get(0).getVideoURL()));

//        MediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(item.getVideoURL()));

        player.prepare(mediaSource);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);

        videoView.setPlayer(player);
        videoView.setUseController(false);

        player.setPlayWhenReady(true);
        previousPlayer = player;


        videoView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(NotificationSvsActivity.this, new GestureDetector.SimpleOnGestureListener() {

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

//        // setting timer for adding view
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                addViewToVideo();
            }
        }, 5000);
    }

    private void addViewToVideo() {
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
        if (timer != null) {
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
        if (timer != null) {
            timer.cancel();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMMENT_OPEN_CODE && data != null) {
            int commentCount = data.getExtras().getInt("commentCount", 0);
            videoByPostIdResponse.getData().getRows().get(0).setComments(commentCount);

        }
    }

    @Override
    public void onBackPressed() {
        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
        if (taskList.get(0).numActivities == 1 && taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
            Log.e("Running", "This is last activity in the stack");
            Intent intent = new Intent(NotificationSvsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        finish();
    }

}