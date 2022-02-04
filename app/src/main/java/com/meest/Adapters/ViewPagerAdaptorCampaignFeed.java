package com.meest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.meest.Fragments.HomeFragments;
import com.meest.model.AdMediaData;
import com.meest.R;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.List;

public class ViewPagerAdaptorCampaignFeed extends PagerAdapter implements Player.EventListener {

    public static final int PERMISSION_CODE = 123;
    private Context context;
    private LayoutInflater layoutInflater;
    private List<AdMediaData> mediaList;
    private String campaignType;
    private String id;
    private HomeFragments homeFragments;

    public ViewPagerAdaptorCampaignFeed(Context context, List<AdMediaData> mediaList, String date, String campaignType, String id, HomeFragments homeFragments) {
        this.mediaList = mediaList;
        this.context = context;
        this.campaignType = campaignType;
        this.id = id;
        this.homeFragments = homeFragments;
    }

    public ViewPagerAdaptorCampaignFeed(Context context, List<AdMediaData> mediaList, String date, String campaignType) {
        this.mediaList = mediaList;
        this.context = context;
        this.campaignType = campaignType;
    }

    @Override
    public int getCount() {
        return mediaList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_feed_campaign, null);

        ImageView imageView = view.findViewById(R.id.imageView);
        PlayerView playerView = view.findViewById(R.id.videoViewPlayer);
        TextView tvTextDetails = view.findViewById(R.id.tvTextDetails);
        TextView tv_sub_title = view.findViewById(R.id.tv_sub_title);
        Button button2 = view.findViewById(R.id.button2);


        // loading video or photo based on type
        Log.v("alhaj", "instantiateItem == " + position);

        if (mediaList.get(position) != null) {
            if (mediaList.get(position).getFileType().equalsIgnoreCase("image")) {
                playerView.setVisibility(View.GONE);
                if (mediaList.get(position).getFileUrl().length() > 0)
                    //    Glide.with(context).load(mediaList.get(position).getFileUrl()).placeholder(R.drawable.image_placeholder).into(imageView);
                    Glide.with(context)
                            .load(mediaList.get(position).getFileUrl())
                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .error(R.drawable.placeholder))
                            .into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);

                releasePlayer();

                // playing video
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(bandwidthMeter);
                TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
                final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "tunemist"));

                // for mp4 media
                // MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mediaList.get(position).getFileUrl()));
//                MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mediaList.get(position).getFileUrl()),
//                        new CacheDataSourceFactory(context, 100 * 1024 * 1024, 5 * 1024 * 1024), new DefaultExtractorsFactory(), null, null);
//                // for hls media
//               MediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mediaList.get(position).getFileUrl()));
                MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.fromFile(new File(mediaList.get(position).getFileUrl())));
                player.addListener(new Player.EventListener() {
                    @Override
                    public void onTimelineChanged(Timeline timeline, int reason) {

                    }

                    @Override
                    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                    }

                    @Override
                    public void onLoadingChanged(boolean isLoading) {

                    }

                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        switch (playbackState) {

                        }
                    }

                    @Override
                    public void onIsPlayingChanged(boolean isPlaying) {

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
                });

                player.setRepeatMode(Player.REPEAT_MODE_ALL);
                player.addListener(this);
                playerView.setPlayer(player);
                playerView.setUseController(false);
                player.setPlayWhenReady(true);
                HomeFragments.previousPlayer = player;
            }
        }

        if (mediaList.get(position).getButtonType() != null && !mediaList.get(position).getButtonType().equalsIgnoreCase("")) {
            button2.setText(mediaList.get(position).getButtonType());
        }

        tvTextDetails.setText(mediaList.get(position).getHeading());
        tv_sub_title.setText(mediaList.get(position).getSubHeading());

        if (campaignType.equalsIgnoreCase("CPC")) {
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mediaList.get(position).getWebsiteUrl()));
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + mediaList.get(position).getWebsiteUrl()));
                        context.startActivity(intent);
                    }
                }
            });
        } else if (campaignType.equalsIgnoreCase("CPL")) {

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeFragments.onSignUpClicked(mediaList, position, id);
                }
            });
        } else {
            button2.setVisibility(View.GONE);
        }

   /*     Glide.with(context)
                .load(sliderImg.get(position))
                .into(imageView);*/

        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position == 0){
                    Toast.makeText(context, "Slide 1 Clicked", Toast.LENGTH_SHORT).show();
                } else if(position == 1){
                    Toast.makeText(context, "Slide 2 Clicked", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Slide 3 Clicked", Toast.LENGTH_SHORT).show();
                }

            }
        });*/


        // txt_title.setText(sliderImg.get(position).getText());

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    public void releasePlayer() {
        if (HomeFragments.previousPlayer != null) {
            HomeFragments.previousPlayer.removeListener(this);
            HomeFragments.previousPlayer.release();
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

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
}
