package com.meest.videomvvmmodule.view.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.meest.R;
import com.meest.databinding.ActivitySoundVideosBinding;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.viewmodel.SoundActivityViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class SoundVideosActivity extends BaseActivity {


    ActivitySoundVideosBinding binding;
    SoundActivityViewModel viewModel;
    SimpleExoPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sound_videos);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new SoundActivityViewModel()).createFor()).get(SoundActivityViewModel.class);
        initView();
        initListeners();
        initObserve();
        binding.setViewmodel(viewModel);
    }

    private void initView() {
        viewModel.soundId = getIntent().getStringExtra("soundid");
        viewModel.soundUrl = getIntent().getStringExtra("sound");
        viewModel.adapter.setSoundId(viewModel.soundId);
        viewModel.fetchSoundVideos(false);

        player = new SimpleExoPlayer.Builder(this).build();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Meest"));
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(viewModel.soundUrl));
//                .createMediaSource(Uri.parse(Const.ITEM_BASE_URL + viewModel.soundUrl));
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.prepare(videoSource);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        binding.loutShoot.startAnimation(animation);
        binding.tvSoundTitle.setSelected(true);


        if (sessionManager != null && sessionManager.getFavouriteMusic() != null) {
            viewModel.isFavourite.set(sessionManager.getFavouriteMusic().contains(viewModel.soundId));
        }

    }

    private void initListeners() {
        binding.loutFavourite.setOnClickListener(v -> {
            sessionManager.saveFavouriteMusic(viewModel.soundId);
            viewModel.isFavourite.set(!viewModel.isFavourite.get());
        });

        binding.imgPlay.setOnClickListener(v -> {
            if (viewModel.isPlaying.get()) {
                player.setPlayWhenReady(false);
                viewModel.isPlaying.set(false);
            } else {
                player.setPlayWhenReady(true);
                viewModel.isPlaying.set(true);
            }
        });
        binding.refreshlout.setOnRefreshListener(() -> viewModel.onLoadMore());
        binding.imgBack.setOnClickListener(v -> onBackPressed());
        binding.loutShoot.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.meest.medley_camera2.camera2.view.activity.CameraActivity.class);
            intent.putExtra("music_url", viewModel.soundUrl);
            intent.putExtra("music_title", viewModel.soundData.getValue() != null ? viewModel.soundData.getValue().getSoundTitle() : "Sound_Title");
            intent.putExtra("soundId", viewModel.soundId);
            startActivity(intent);
        });
    }

    private void initObserve() {
        CustomDialogBuilder customDialogBuilder = new CustomDialogBuilder(this);
        viewModel.isloading.observe(this, isLoading -> {
            if (isLoading) {
                customDialogBuilder.showLoadingDialog();
            } else {
                customDialogBuilder.hideLoadingDialog();
            }
        });

        viewModel.onLoadMoreComplete.observe(this, onLoadMore -> binding.refreshlout.setRefreshing(false));
        viewModel.soundData.observe(this, soundData -> {
            binding.setSoundData(soundData);
            binding.tvVideoCount.setText(Global.prettyCount(soundData.getPostVideoCount()).concat(" Videos"));

        });
    }

    @Override
    protected void onPause() {
        if (player != null) {
            player.setPlayWhenReady(false);
            viewModel.isPlaying.set(false);
        }
        super.onPause();
    }
}