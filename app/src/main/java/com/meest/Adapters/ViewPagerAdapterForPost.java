package com.meest.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.meest.Fragments.HomeFragments;
import com.meest.R;
import com.meest.responses.FeedResponse;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

public class ViewPagerAdapterForPost extends PagerAdapter implements Player.EventListener {

    private Context context;
    private LayoutInflater layoutInflater;
    private ProgressBar progressBar;
    private List<FeedResponse.Post> feedData;
    private int mPlayerCurrentPosition;
    private SimpleExoPlayer player;

    public ViewPagerAdapterForPost(Context context, List<FeedResponse.Post> feedData, ProgressBar progressBar) {
        this.feedData = feedData;
        this.context = context;
        this.progressBar = progressBar;
    }


    @Override
    public int getCount() {
        return feedData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout_for_post, null);
        try {

            ImageView imageView = view.findViewById(R.id.imageView);
            ImageView imageViewBackground = view.findViewById(R.id.imageViewBackground);
            PlayerView playerView = view.findViewById(R.id.videoViewPlayer);

            // loading video or photo based on type
            Log.v("harsh", "instantiateItem == " + position);
            if (feedData.get(position) != null) {
                if (feedData.get(position).getImage() != null && feedData.get(position).getImage() == 1) {
                    playerView.setVisibility(View.GONE);
                    if (feedData.get(position).getPost().length() > 0)
                        Glide.with(context).load(feedData.get(position).getPost())
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(imageView);
                    try {
                        if (feedData.get(position).getPost() != null) {
                            imageView.setVisibility(View.VISIBLE);
                        } else {
                            imageView.setVisibility(View.GONE);
                            playerView.setVisibility(View.GONE);
                        }

                        playerView.setVisibility(View.GONE);
                        if (feedData.get(position).getPost() != null && feedData.get(position).getPost().length() > 0) {
                            Glide.with(context).load(feedData.get(position).getPost())
                                    .error(R.drawable.avatar)
                                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                    .into(imageView);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    imageView.setVisibility(View.GONE);
                    playerView.setVisibility(View.VISIBLE);




                    releasePlayer();
                    // playing video

                    DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory( Util.getUserAgent(context,  context.getString(R.string.app_name)));
                    LoadControl loadControlnew=new  DefaultLoadControl.Builder().setBufferDurationsMs(25000, 50000, 1500, 2000).createDefaultLoadControl();
                    @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode = DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER;
                    RenderersFactory renderersFactorynew =new DefaultRenderersFactory(context) .setExtensionRendererMode(extensionRendererMode);
                    // for mp4 media
//                    MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
//                            .createMediaSource(Uri.parse(feedData.get(position).getPost()));

                    // for hls media
                    MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(feedData.get(position).getPost()));
                    player=new  SimpleExoPlayer.Builder(context,renderersFactorynew).setLoadControl(loadControlnew).build();
                    player.prepare(mediaSource,true,true);
                    player.setRepeatMode(Player.REPEAT_MODE_ALL);
                    player.addListener(this);
                    playerView.setPlayer(player);
                    playerView.setUseController(false);
                    player.setPlayWhenReady(true);
                    HomeFragments.previousPlayer = player;


                }
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
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.SOME_ERROR), Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.GONE);


        return view;

    }

    public int getCurrentPlayerPosition() {

        if (HomeFragments.previousPlayer != null) {
            return (int) HomeFragments.previousPlayer.getCurrentPosition();
        } else
            return 0;
    }

    public void pausePlayer() {
        mPlayerCurrentPosition = getCurrentPlayerPosition();
        HomeFragments.previousPlayer.setPlayWhenReady(false);
        HomeFragments.previousPlayer.release(); // this will make the player object eligible for GC
    }

    public void releasePlayer() {
        if (HomeFragments.previousPlayer != null) {

            mPlayerCurrentPosition = getCurrentPlayerPosition();
            HomeFragments.previousPlayer.setPlayWhenReady(false);
            HomeFragments.previousPlayer.release();

        }
    }

    public void resumePlaybackFromPreviousPosition() {
        if (HomeFragments.previousPlayer != null)
            HomeFragments.previousPlayer.seekTo(mPlayerCurrentPosition);

    }


    public void resumePlayer() {
        if (HomeFragments.previousPlayer != null) {
            HomeFragments.previousPlayer.setPlayWhenReady(true);
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
        if (playbackState == Player.STATE_BUFFERING && progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        } else if (playbackState == Player.STATE_READY && progressBar != null) {
            progressBar.setVisibility(View.GONE);
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
}
