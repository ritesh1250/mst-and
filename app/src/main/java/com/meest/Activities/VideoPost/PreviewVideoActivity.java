package com.meest.Activities.VideoPost;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
//import com.meest.Activities.NewPostUploadActivity;
import com.meest.social.socialViewModel.view.NewPostUploadActivity;
import com.meest.MainActivity;
import com.meest.R;
import com.meest.Services.StoryPostuploadService;
import com.meest.utils.LoadingDialog;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;


public class PreviewVideoActivity extends AppCompatActivity implements Player.EventListener {

    SimpleExoPlayer player;
    Boolean playWhenReady = true;
    int currentWindow = 0;
    Long playbackPosition = 0L;
    PlayerView video_view;
    String type, videoPath, audioId, thumbPath;
    ImageView ivVideoDone, ivCancelVideo;
    //    LottieAnimationView loadingView;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_preview_video);
        video_view = findViewById(R.id.video_view);
        ivVideoDone = findViewById(R.id.ivVideoDone);
        ivCancelVideo = findViewById(R.id.ivCancelVideo);
        loadingDialog = new LoadingDialog(this);
        type = getIntent().getStringExtra("type");
        audioId = getIntent().getStringExtra("audioId");
        videoPath = getIntent().getStringExtra("videoPath");
        thumbPath = getIntent().getStringExtra("thumbPath");

        initializePlayer(videoPath);

        ivVideoDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadVideo();
//                uploadStory();
            }
        });

        ivCancelVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void uploadVideo() {

        
        Intent intent;
        if (type.equalsIgnoreCase("svs")) {
            intent = new Intent(PreviewVideoActivity.this, PostVideoActivity.class);
            intent.putExtra("video_path", videoPath);
            intent.putExtra("audioId", audioId);
            intent.putExtra("type", type);
//            startActivityForResult(intent, DISCARD_UPLOAD_CODE);
            startActivity(intent);
            finish();
        } else if (type.equalsIgnoreCase("feed")) {
            intent = new Intent(PreviewVideoActivity.this, NewPostUploadActivity.class);
            intent.putExtra("mediaPath", videoPath);
            intent.putExtra("thumbPath", thumbPath);
            intent.putExtra("isVideo", true);
//            startActivityForResult(intent, DISCARD_UPLOAD_CODE);
            startActivity(intent);
            finish();
        } else if (type.equalsIgnoreCase("story")) {
             intent = new Intent(PreviewVideoActivity.this, StoryPostuploadService.class);
            intent.putExtra("storyMediaPath", videoPath);
            intent.putExtra("storyThumbPath", thumbPath);
            intent.putExtra("isStoryVideo", true);
            startService(intent);
            Intent intent1 = new Intent(PreviewVideoActivity.this, MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            finish();
        } else if (type.equalsIgnoreCase("duo")) {
            intent = new Intent(PreviewVideoActivity.this, PostVideoActivity.class);
            intent.putExtra("video_path", videoPath);
            intent.putExtra("audioId", audioId);
            intent.putExtra("type", type);
//            startActivityForResult(intent, DISCARD_UPLOAD_CODE);
            startActivity(intent);
            finish();
        } else if (type.equalsIgnoreCase("trio")) {
            intent = new Intent(PreviewVideoActivity.this, PostVideoActivity.class);
            intent.putExtra("video_path", videoPath);
            intent.putExtra("audioId", audioId);
            intent.putExtra("type", type);
//            startActivityForResult(intent, DISCARD_UPLOAD_CODE);
            startActivity(intent);
            finish();
        }
    }

//    private void hidePrd() {
//        if (loadingView != null) {
//            loadingDialog.setVisibility(View.GONE);
//            ivVideoDone.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void showPrd() {
//        if (loadingView != null) {
//            loadingView.setVisibility(View.VISIBLE);
//            ivVideoDone.setVisibility(View.GONE);
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            player.setPlayWhenReady(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            player.setPlayWhenReady(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            releasePlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializePlayer(String path) {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(this, new DefaultRenderersFactory(this), new DefaultTrackSelector(), new DefaultLoadControl());
        }
        video_view.setPlayer(player);

        video_view.requestFocus();
        ExtractorMediaSource audioSource = new ExtractorMediaSource(
                Uri.parse(path),
                new DefaultDataSourceFactory(this, "MyExoplayer"),
                new DefaultExtractorsFactory(),
                null,
                null
        );
        player.prepare(audioSource);
        video_view.setPlayer(player);
        player.setRepeatMode(Player.REPEAT_MODE_ONE);
        player.setPlayWhenReady(true);

    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.removeListener(this);
            player.release();
            player = null;
        }
    }
}
