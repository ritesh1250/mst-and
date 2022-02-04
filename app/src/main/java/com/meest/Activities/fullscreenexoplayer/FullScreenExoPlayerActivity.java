package com.meest.Activities.fullscreenexoplayer;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.meest.R;

public class FullScreenExoPlayerActivity extends AppCompatActivity {

    SimpleExoPlayer player;
    PlayerView playerView;
    ImageView fullscreenButton;
    ImageView cross_icon;
    boolean fullscreen = false;
    String videoUrl;
    long currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_exo_player);

        videoUrl = getIntent().getStringExtra("url");
        currentPosition = 0;

        if (player == null) {

            player = new SimpleExoPlayer.Builder(FullScreenExoPlayerActivity.this).build();
        } else {
            player.release();
            player = null;
            player = new SimpleExoPlayer.Builder(FullScreenExoPlayerActivity.this).build();

        }
        playerView = findViewById(R.id.player);
        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
        cross_icon = playerView.findViewById(R.id.cross_im);
        cross_icon.setOnClickListener(view -> {
            finish();
        });

        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullscreen) {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullScreenExoPlayerActivity.this, R.drawable.ic_fullscreen_open));

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().hide();
                    }

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
//                    params.height = (int) ( 200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);

                    fullscreen = false;
                } else {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullScreenExoPlayerActivity.this, R.drawable.ic_fullscreen_close));

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().hide();
                    }

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);

                    fullscreen = true;
                }
            }
        });

        playerView.setPlayer(player);
//        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), getApplicationContext().getString(R.string.app_name)));

        MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(videoUrl));

        player.prepare(videoSource);
        player.seekTo(currentPosition);
        player.setPlayWhenReady(true);


    }

    @Override
    public void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);
    }

    @Override
    public void onDestroy() {
        player.release();
        super.onDestroy();
    }
}
