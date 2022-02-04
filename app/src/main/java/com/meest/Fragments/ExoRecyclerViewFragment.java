package com.meest.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.meest.Activities.fullscreenexoplayer.FullScreenExoPlayerActivity;
import com.meest.R;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.responses.FeedResponse;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

public class ExoRecyclerViewFragment extends Fragment {

    private static final String ARGS_ITEM = "args_item";

    private SimpleExoPlayer player;
    private ImageView iv_mute_unmute_new, exo_fullscreen_icon;



    private DefaultTrackSelector trackSelector;

    private FeedResponse.Post currentGalleryModel;

    private SimpleExoPlayerView simpleExoPlayerView;
    ImageView imageView;

    public static ExoRecyclerViewFragment newInstance(String item) {
        ExoRecyclerViewFragment fragment = new ExoRecyclerViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_ITEM, item);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_see_full_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.imgPreview);
        iv_mute_unmute_new = view.findViewById(R.id.iv_mute_unmute_new);
        simpleExoPlayerView = view.findViewById(R.id.video_player_view);
        exo_fullscreen_icon = view.findViewById(R.id.exo_fullscreen_post);


        String itemdata = getArguments().getString(ARGS_ITEM);
        Gson gson = new Gson();
        currentGalleryModel = gson.fromJson(itemdata, FeedResponse.Post.class);
        if (currentGalleryModel == null) {
            return;
        }


        if (currentGalleryModel.getVideo() == 1) {
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            iv_mute_unmute_new.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            simpleExoPlayerView.setOnClickListener(view1 -> {

            });


        } else if (currentGalleryModel.getImage() == 1) {
            simpleExoPlayerView.setVisibility(View.GONE);
            iv_mute_unmute_new.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(currentGalleryModel.getPost())
                    .placeholder(android.R.color.black)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);

            exo_fullscreen_icon.setVisibility(View.GONE);

        }


        exo_fullscreen_icon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FullScreenExoPlayerActivity.class);
            intent.putExtra("url", currentGalleryModel.getPost());
//                    intent.putExtra("currentPosition", simpleExoPlayerView.getControllerShowTimeoutMs());
            getActivity().startActivity(intent);
        });

        iv_mute_unmute_new.setOnClickListener(v -> {
            if(NotificationSocialFeedActivity.volume){
                NotificationSocialFeedActivity.volume = false;
                setMute(NotificationSocialFeedActivity.volume);

            }else {
                NotificationSocialFeedActivity.volume = true;
                setMute(NotificationSocialFeedActivity.volume);
            }

        });
    }

    public void setMute(boolean toMute) {
        if (player != null) {
            if (toMute) {
                iv_mute_unmute_new.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.volume_off_24));
                player.setVolume(0f);
            } else {
                iv_mute_unmute_new.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.volume_up_24));
                player.setVolume(1f);
            }
        }
    }

    private void initializePlayer() {
//        Log.v("harsh", "onPageSelected == " + "initializePlayer");
        MediaSource mediaSource;
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), (TransferListener) bandwidthMeter);

        if (currentGalleryModel.getPost() != null) {

            mediaSource = new ExtractorMediaSource(Uri.parse(currentGalleryModel.getPost()),
            mediaDataSourceFactory, extractorsFactory, null, null);
            player.setPlayWhenReady(true);
            player.prepare(mediaSource);
            player.setRepeatMode(Player.REPEAT_MODE_ONE);
            setMute(NotificationSocialFeedActivity.volume);


        }


        if (simpleExoPlayerView != null) simpleExoPlayerView.setPlayer(player);

    }

    private void releasePlayer() {
        if (player != null) {
//            player.stop();
            player.release();
            player = null;
            trackSelector = null;
            simpleExoPlayerView.setPlayer(null);
        }
    }

    @Override
    public void onStart() {
        if (player == null && currentGalleryModel != null) {

        //    initializePlayer();
        }
        super.onStart();

    }

    @Override
    public void onResume() {
        if (player == null && currentGalleryModel != null) {
            initializePlayer();
        }
        super.onResume();

    }

    @Override
    public void onPause() {
        releasePlayer();
        super.onPause();

    }

    @Override
    public void onStop() {
        releasePlayer();
        super.onStop();

    }


    public void imHiddenNow() {
        releasePlayer();
    }

    public void imVisibleNow() {
        initializePlayer();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        imHiddenNow();
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        imHiddenNow();
        super.onDetach();

    }


}
