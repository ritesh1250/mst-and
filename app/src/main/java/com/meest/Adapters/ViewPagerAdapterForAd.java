package com.meest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.meest.Fragments.HomeFragments;
import com.meest.model.AdMediaData;
import com.meest.R;
import com.meest.responses.CampaignSignUpResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.bumptech.glide.Glide;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPagerAdapterForAd extends PagerAdapter implements Player.EventListener {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<AdMediaData> mediaList;
    private String campaignType;
    private String id;

    public ViewPagerAdapterForAd(Context context, List<AdMediaData> mediaList, String date, String campaignType, String id) {
        this.mediaList = mediaList;
        this.context = context;
        this.campaignType = campaignType;
        this.id = id;
    }

    public ViewPagerAdapterForAd(Context context, List<AdMediaData> mediaList, String date, String campaignType) {
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
        View view = layoutInflater.inflate(R.layout.iten_preview_campaign, null);

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
                    Glide.with(context).load(mediaList.get(position).getFileUrl()).placeholder(R.drawable.image_placeholder).into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);

                releasePlayer();

                // playing video
                DefaultTrackSelector trackSelector = new DefaultTrackSelector();
                final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "tunemist"));

                // for mp4 media
                MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mediaList.get(position).getFileUrl()));

                // for hls media
//        MediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(item.getVideoURL()));

                player.prepare(mediaSource);
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

                    View view = LayoutInflater.from(context).inflate(R.layout.campaign_form_dialog, null);
                    TextView edName = view.findViewById(R.id.edName);
                    TextView edEmail = view.findViewById(R.id.edEmail);
                    TextView tv_title = view.findViewById(R.id.tv_title);
                    TextView edMobile = view.findViewById(R.id.edMobile);
                    Button btn_done = view.findViewById(R.id.btn_done);
                    Button btn_close = view.findViewById(R.id.btn_close);
                    LinearLayout container = view.findViewById(R.id.container);
                    View thankyou = view.findViewById(R.id.thankyou);

                    BottomSheetDialog dialog = new BottomSheetDialog(context);
                    dialog.setContentView(view);

                    if (!mediaList.get(position).getHeading().equals("")) {
                        tv_title.setText(mediaList.get(position).getHeading());
                    }


                    if (id != null) {

                        btn_done.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (TextUtils.isEmpty(edName.getText())) {
                                    edName.setError("Fill this");
                                    return;
                                }
                                if (!Utilss.isValidMail(edEmail.getText().toString())) {
                                    edEmail.setError("invalid email");
                                    return;
                                }
                                if (edMobile.getText().length() < 10) {
                                    edMobile.setError("Invalid number");
                                    return;
                                }


                                Map<String, String> header = new HashMap<>();
                                header.put("Accept", "application/json");
                                header.put("Content-Type", "application/json");
                                header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
                                // body
                                HashMap<String, Object> body = new HashMap<>();
                                body.put("name", edName.getText().toString());
                                body.put("email", edName.getText().toString());
                                body.put("mobile", edName.getText().toString());
                                body.put("isAdvertise", true);
                                body.put("campaignId", id);


                                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                                Call<CampaignSignUpResponse> call = webApi.getCampaignSignUp(header, body);
                                call.enqueue(new Callback<CampaignSignUpResponse>() {
                                    @Override
                                    public void onResponse(Call<CampaignSignUpResponse> call, Response<CampaignSignUpResponse> response) {
                                        if (response.code() == 200 && response.body().getSuccess()) {
                                            container.setVisibility(View.GONE);
                                            thankyou.setVisibility(View.VISIBLE);
                                            btn_close.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });

                                        } else {
                                            container.setVisibility(View.GONE);
                                            thankyou.setVisibility(View.VISIBLE);
                                            btn_close.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<CampaignSignUpResponse> call, Throwable t) {
                                        Utilss.showToast(context, "You have already signed up", R.color.msg_fail);
                                    }
                                });

                            }
                        });
                    } else {
                        Utilss.showToast(context, "fill form from feed only", R.color.msg_fail);
                    }
                    dialog.show();
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
