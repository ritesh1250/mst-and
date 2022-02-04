package com.meest.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.material.tabs.TabLayout;
import com.master.exoplayer.ExoPlayerHelper;
import com.master.exoplayer.MasterExoPlayer;
import com.master.exoplayer.MasterExoPlayerHelper;
import com.meest.Activities.LikeDetailsActivity;
import com.meest.Activities.fullscreenexoplayer.FullScreenExoPlayerActivity;
import com.meest.Fragments.HomeFragments;
import com.meest.Interfaces.PostItemsCallback;
import com.meest.MainActivity;
import com.meest.R;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.metme.MetMeUtils.ToolTipWindow;
import com.meest.model.AdMediaData;
import com.meest.model.HomeModel;
import com.meest.model.SharedPost;
import com.meest.responses.FeedResponse;
import com.meest.responses.InsertPostLikeResponse;
import com.meest.responses.ShowFeedAdOne;
import com.meest.responses.UserFollowerStoryResponse;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.svs.adapters.ReportAdapter;
//import com.meest.svs.models.ReportTypeResponse;
import com.meest.social.socialViewModel.model.ReportTypeResponse;
import com.meest.utils.CirclePagerIndicatorDecoration;
import com.meest.utils.HeightWrappingViewPager;
import com.meest.utils.MyAlertDialog;
import com.meest.utils.goLiveUtils.CommonUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class UserPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final int SINGLE_MEDIA = 1;
    final int MULTIPLE_MEDIA = 2;
    final int PROGRESS_LOADER = 6;
    final int AD = 7;
    String TAG = "UserPostAdapter.java";
    PostItemsCallback itemCallback;
    HomeFragments fragment;
    RecyclerView postRecycler;
    ToolTipWindow tipWindow;
    Vibrator x = null;
    private HomeFragments ctx;
    private Context context;
    private ArrayList<HomeModel> feedResponse;
    private MasterExoPlayerHelper masterExoPlayerHelper;
    //listner
    private RecycleListener clickListener;

    private ArrayList<UserFollowerStoryResponse.Row> userStoryList = new ArrayList<>();

    public UserPostAdapter(ArrayList<HomeModel> feedResponse, MasterExoPlayerHelper masterExoPlayerHelper, Context mContext,
                           HomeFragments fragment, ArrayList<UserFollowerStoryResponse.Row> userStoryList, HomeFragments ctx, RecyclerView postRecyclerView, RecycleListener clickListener) {
        this.context = mContext;
        this.feedResponse = feedResponse;
        this.userStoryList = userStoryList;
        this.masterExoPlayerHelper = masterExoPlayerHelper;
        masterExoPlayerHelper.makeLifeCycleAware(MainActivity.getInstance());
        this.fragment = fragment;
        this.itemCallback = fragment;
        this.ctx = ctx;
        this.postRecycler = postRecyclerView;
        this.clickListener = clickListener;

    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() && !HomeFragments.isLastPage) {
            return PROGRESS_LOADER;
        }
        try {
            if (feedResponse.get(position).getAD()) {
                return AD;
            }
            if (feedResponse.get(position).getFeedResponse().getPosts().size() > 1) {
                return MULTIPLE_MEDIA;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        masterExoPlayerHelper.attachToRecyclerView(postRecycler);
        return SINGLE_MEDIA;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        tipWindow = new ToolTipWindow(context);
        x = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        switch (viewType) {
            case PROGRESS_LOADER:
                View progressLoaderView = LayoutInflater.from(context).inflate(R.layout.progress_loader, parent, false);
                return new ProgressLoaderViewHolder(progressLoaderView);
            case MULTIPLE_MEDIA:
                View multipleMediaRow = LayoutInflater.from(context).inflate(R.layout.user_post_multiple_media, parent, false);
                return new MultipleMediaViewHolder(multipleMediaRow);
            case AD:
                View Ad = LayoutInflater.from(context).inflate(R.layout.item_campaign_drafted, parent, false);
                return new AdViewHolder(Ad);
            default:
                View singleMediaRow = LayoutInflater.from(context).inflate(R.layout.user_post_single_media_row, parent, false);

                return new SingleMediaViewHolder(singleMediaRow);
        }
    }

    public void remove(int position) {
        feedResponse.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, feedResponse.size());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull final RecyclerView.ViewHolder holder, final int position) {
        clickListener.itemClicked(position);
        if (holder instanceof StoryViewHolder) {
            StoryViewHolder mHolder = (StoryViewHolder) holder;
            mHolder.setAdapter();
        } else if (holder instanceof ProgressLoaderViewHolder) {
            ProgressLoaderViewHolder mHolder = (ProgressLoaderViewHolder) holder;
            mHolder.image.setAnimation("loading.json");
            mHolder.image.playAnimation();
            mHolder.image.loop(true);
        } else if (holder instanceof AdViewHolder) {
            try {
                final AdViewHolder userViewHolder = (AdViewHolder) holder;
                final ShowFeedAdOne.Data model = feedResponse.get(position).getShowAdvtResponse().getData();
                List<AdMediaData> adList = new ArrayList<>();

                AdMediaData data = new AdMediaData();
                userViewHolder.iv_down.setVisibility(VISIBLE);
                userViewHolder.iv_down.setOnClickListener(v -> {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

                    ViewGroup viewGroup = userViewHolder.itemView.findViewById(android.R.id.content);
                    final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_post, viewGroup, false);

                    TextView txt_delete = dialogView.findViewById(R.id.txt_delete);
                    TextView txt_report = dialogView.findViewById(R.id.txt_report);
                    TextView txt_copyLink = dialogView.findViewById(R.id.txt_copyLink);
                    TextView txt_shareTo = dialogView.findViewById(R.id.txt_shareTo);

                    txt_delete.setText(context.getString(R.string.Report_Ad));
                    txt_report.setText(context.getString(R.string.Hide_Ad));

                    txt_delete.setTextColor(context.getColor(R.color.edit_line_red));
                    txt_report.setTextColor(context.getColor(R.color.edit_line_red));

                    txt_copyLink.setVisibility(View.GONE);
                    txt_shareTo.setVisibility(View.GONE);

                    builder.setView(dialogView);
                    final AlertDialog alertDialog = builder.create();

                    txt_report.setOnClickListener(v1 -> {
                        //hide ad
                        feedResponse.remove(position);
                        notifyItemRemoved(position);
                        alertDialog.dismiss();
                    });


                    txt_delete.setOnClickListener(v12 -> {
                        //report
                        final androidx.appcompat.app.AlertDialog.Builder builder1 = new
                                androidx.appcompat.app.AlertDialog.Builder(context, R.style.CustomAlertDialog);
                        final View dialogView1 = LayoutInflater.from(context).inflate(R.layout.dialog_svs, null, false);

                        builder1.setView(dialogView1);

                        androidx.appcompat.app.AlertDialog alertDialog1 = builder1.create();

                        TextView report_option = dialogView1.findViewById(R.id.report_option);
                        ProgressBar progressBar = dialogView1.findViewById(R.id.progress_bar);
                        RecyclerView report_recycler = dialogView1.findViewById(R.id.report_recycler);

                        report_option.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);

                        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                        Map<String, String> myHeader = new HashMap<>();
                        myHeader.put("Accept", "application/json");
                        myHeader.put("Content-Type", "application/json");
                        myHeader.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                        // body
                        String type = "Post";
                        HashMap<String, Object> body = new HashMap<>();
                        body.put("reportType", type);
                        body.put("reportedData", false);

                        Call<ReportTypeResponse> call = webApi.getReportTypes(myHeader, body);
                        call.enqueue(new Callback<ReportTypeResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<ReportTypeResponse> call, @NotNull Response<ReportTypeResponse> response) {
                                progressBar.setVisibility(View.GONE);

                                if (response.body() != null) {
                                    if (response.code() == 200 && response.body().getSuccess()) {
                                        feedResponse.remove(position);
                                        ReportAdapter reportAdapter = new ReportAdapter(context,
                                                response.body(), alertDialog1, progressBar, model.getId(), type);
                                        report_recycler.setLayoutManager(new LinearLayoutManager(context));
                                        report_recycler.setAdapter(reportAdapter);
                                        report_recycler.setVisibility(View.VISIBLE);
                                    } else {
                                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                                        alertDialog1.dismiss();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ReportTypeResponse> call, @NotNull Throwable t) {
                                progressBar.setVisibility(View.GONE);

                                Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                                alertDialog1.dismiss();
                            }
                        });
                        alertDialog1.show();
                    });
                    alertDialog.show();
                });


                try {
                    if (model.getTotalLike() != 0) {
                        userViewHolder.txt_like.setText("" + model.getTotalLike());
                        if (model.getTotalLike() == 1) {
                            userViewHolder.text_others_like.setVisibility(View.GONE);
                            userViewHolder.and_text.setVisibility(View.GONE);
                        } else {
                            userViewHolder.text_others_like.setText(" " + (model.getTotalLike() - 1) + " " + context.getString(R.string.others));
                        }
                        userViewHolder.layout_likes_details.setVisibility(VISIBLE);
                    } else {
                        userViewHolder.txt_like.setText("0");
                        userViewHolder.layout_likes_details.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //------------------------------------------------------------------------- changes for show last 3 user profile liked this post ----------------


//                if (feedResponse.get(position).getFeedResponse().getPostLikes() != null && feedResponse.get(position).getFeedResponse().getPostLikes().size() > 0) {
//
//
//                    if (feedResponse.get(position).getFeedResponse().getPostLikes().size() == 1) {
//                        userViewHolder.likeUser1.setVisibility(VISIBLE);
//                        userViewHolder.likeUser2.setVisibility(GONE);
//                        userViewHolder.likeUser3.setVisibility(GONE);
//                        Log.d("onBindViewHolder:", " " + feedResponse.get(position).getFeedResponse().getPostLikes().size());
//                        CommonUtils.loadImage(userViewHolder.ivLikeUser1, feedResponse.get(position).getFeedResponse().getPostLikes().get(0).getUser().getThumbnail(), context);
//
//
//                    } else if (feedResponse.get(position).getFeedResponse().getPostLikes().size() == 2) {
//                        userViewHolder.likeUser1.setVisibility(VISIBLE);
//                        userViewHolder.likeUser2.setVisibility(VISIBLE);
//                        userViewHolder.likeUser3.setVisibility(GONE);
//                        CommonUtils.loadImage(userViewHolder.ivLikeUser1, feedResponse.get(position).getFeedResponse().getPostLikes().get(0).getUser().getThumbnail(), context);
//                        CommonUtils.loadImage(userViewHolder.ivLikeUser2, feedResponse.get(position).getFeedResponse().getPostLikes().get(1).getUser().getThumbnail(), context);
//                        Log.d("onBindViewHolder:", " " + feedResponse.get(position).getFeedResponse().getPostLikes().size());
//
//                    } else if (feedResponse.get(position).getFeedResponse().getPostLikes().size() == 3) {
//                        userViewHolder.likeUser1.setVisibility(VISIBLE);
//                        userViewHolder.likeUser2.setVisibility(VISIBLE);
//                        userViewHolder.likeUser3.setVisibility(VISIBLE);
//                        CommonUtils.loadImage(userViewHolder.ivLikeUser1, feedResponse.get(position).getFeedResponse().getPostLikes().get(0).getUser().getThumbnail(), context);
//                        CommonUtils.loadImage(userViewHolder.ivLikeUser2, feedResponse.get(position).getFeedResponse().getPostLikes().get(1).getUser().getThumbnail(), context);
//                        CommonUtils.loadImage(userViewHolder.ivLikeUser3, feedResponse.get(position).getFeedResponse().getPostLikes().get(2).getUser().getThumbnail(), context);
//                        Log.d("onBindViewHolder:", " " + feedResponse.get(position).getFeedResponse().getPostLikes().size());
//
//                    } else {
//                        userViewHolder.likeUser1.setVisibility(VISIBLE);
//                        userViewHolder.likeUser2.setVisibility(VISIBLE);
//                        userViewHolder.likeUser3.setVisibility(VISIBLE);
//                        CommonUtils.loadImage(userViewHolder.ivLikeUser1, feedResponse.get(position).getFeedResponse().getPostLikes().get(0).getUser().getThumbnail(), context);
//                        CommonUtils.loadImage(userViewHolder.ivLikeUser2, feedResponse.get(position).getFeedResponse().getPostLikes().get(1).getUser().getThumbnail(), context);
//                        CommonUtils.loadImage(userViewHolder.ivLikeUser3, feedResponse.get(position).getFeedResponse().getPostLikes().get(2).getUser().getThumbnail(), context);
//                        Log.d("onBindViewHolder:", " " + feedResponse.get(position).getFeedResponse().getPostLikes().size());
//
//
//                    }
//
//
//                }
//                else {
//                    userViewHolder.likeUser1.setVisibility(GONE);
//                    userViewHolder.likeUser2.setVisibility(GONE);
//                    userViewHolder.likeUser3.setVisibility(GONE);
//
//                }

                //------------------------------------------------------------------------- changes for show last 3 user profile liked this post end ----------------

//                userViewHolder.layout_like.setOnLongClickListener(v -> {
////                    Toast.makeText(context, "Long Press", Toast.LENGTH_SHORT).show();
////                    if (!tipWindow.isTooltipShown()) {
////                        tipWindow.showToolTip(v);
////                    }
//                    return false;
//                });


                userViewHolder.layout_like.setOnClickListener(view -> {
//                    Toast.makeText(context, "*****************", Toast.LENGTH_SHORT).show();
//                    if (!tipWindow.isTooltipShown()) {
//                        tipWindow.showToolTip(view);
//                    }
                    Map<String, String> map = new HashMap<>();
                    map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
                    WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);
                    HashMap<String, String> HashMap = new HashMap<>();
                    HashMap.put("campaignId", model.getId());
                    Call<InsertPostLikeResponse> call1 = webApi1.insertLikeAd(map, HashMap);
                    call1.enqueue(new Callback<InsertPostLikeResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<InsertPostLikeResponse> call, @NotNull Response<InsertPostLikeResponse> response) {
                            try {
                                if (response.body() != null) {
                                    if (response.code() == 200 && response.body().getSuccess()) {
                                        if (feedResponse.get(position).getShowAdvtResponse().getData().getIsLiked() == 0) {
                                            userViewHolder.txt_like_txt.setText(context.getString(R.string.like));
                                            feedResponse.get(position).getShowAdvtResponse().getData().setTotalLike(response.body().getData().getLikeCount());
                                            feedResponse.get(position).getShowAdvtResponse().getData().setIsLiked(1);
                                            userViewHolder.img_like.setImageResource(R.drawable.like_diamond_filled);
                                        } else {
                                            userViewHolder.txt_like_txt.setText(context.getString(R.string.like));
                                            feedResponse.get(position).getShowAdvtResponse().getData().setTotalLike(response.body().getData().getLikeCount());
                                            feedResponse.get(position).getShowAdvtResponse().getData().setIsLiked(0);
                                            userViewHolder.img_like.setImageResource(R.drawable.like_diamond);
                                        }

                                        // changing like counts and data
                                        final int li = response.body().getData().getLikeCount();
                                        if (li != 0) {
                                            userViewHolder.txt_like.setText("" + li);
                                            userViewHolder.txt_recent_user_name.setText(" " + 1 + " ");
                                            //                                            userViewHolder.txt_recent_user_name.setText(" " + model.getPostLikes().get(0).getUser().getUsername() + " ");
                                            if (li == 1) {
                                                userViewHolder.text_others_like.setVisibility(View.GONE);
                                                userViewHolder.and_text.setVisibility(View.GONE);
                                            } else {
                                                userViewHolder.text_others_like.setText(" " + (li - 1) + " " + context.getString(R.string.others));
                                            }
                                            userViewHolder.layout_likes_details.setVisibility(VISIBLE);
                                        } else {
                                            userViewHolder.txt_like.setText("0");
                                            userViewHolder.layout_likes_details.setVisibility(View.GONE);
                                        }
                                    } else {
                                        assert response.errorBody() != null;
                                        Utilss.showToast(context, response.errorBody().string(), R.color.msg_fail);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<InsertPostLikeResponse> call, @NotNull Throwable t) {
                            Log.w("error", t);
                            Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                        }
                    });
                });

                userViewHolder.layout_comment.setOnClickListener(view -> itemCallback.userCommentClicked(position, model.getId(), model.getName(), true, true, model.getTotalComments()));

                userViewHolder.layout_share.setOnClickListener(v -> {
                    final String appPackageName = context.getPackageName();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Checkout this awesome app");
                    intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
                    context.startActivity(Intent.createChooser(intent, "Share link"));
                });

                userViewHolder.like_comment_container.setVisibility(VISIBLE);
                data.setFileUrl(model.getFileURL());
                data.setHeading(model.getCampaignTitle());
                data.setSubHeading(model.getCampaignText());
                data.setWebsiteUrl(model.getWebsiteUrl());

                if (model.getOtherImageVideos() != null) {
                    for (int i = 0; i < model.getOtherImageVideos().size(); i++) {
                        AdMediaData adMediaData = new AdMediaData();
                        adMediaData.setFileUrl(model.getOtherImageVideos().get(i).getFileUrl());
                        adMediaData.setSubHeading(model.getOtherImageVideos().get(i).getSubHeading());
                        adMediaData.setHeading(model.getOtherImageVideos().get(i).getHeading());
                        adMediaData.setFileType(model.getOtherImageVideos().get(i).getFileType());
                        adMediaData.setWebsiteUrl(model.getOtherImageVideos().get(i).getWebsiteUrl());
                        adList.add(adMediaData);
                    }

                    userViewHolder.btActivate.setVisibility(View.GONE);
                    userViewHolder.btDelete.setVisibility(View.GONE);
                    userViewHolder.btEdit.setVisibility(View.GONE);

                    Log.e("alhaj11", model.getId());


                    ViewPagerAdaptorCampaignFeed viewPagerAdapter = new ViewPagerAdaptorCampaignFeed(context, adList, model.getStartDate() + " " + model.getEndDate(), model.getCampaignType(), model.getId(), ctx);
                    userViewHolder.CampViewPagerDraft.setAdapter(viewPagerAdapter);
                    userViewHolder.CampViewPagerDraft.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    userViewHolder.CampViewPagerDraft.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                }

                if (feedResponse.get(position).getShowAdvtResponse().getData().getOtherImageVideos().size() > 1) {
                    userViewHolder.tab_layout.setVisibility(VISIBLE);
                    userViewHolder.tab_layout.setupWithViewPager(userViewHolder.CampViewPagerDraft, true);
                }
                if (model.getName().equalsIgnoreCase("")) {
                    userViewHolder.txtAdTitleName.setText("Ad title");
                } else {
                    userViewHolder.txtAdTitleName.setText(model.getName());
                }

                userViewHolder.tvStartDate.setText("sponsored");
                userViewHolder.tvStartDate.setTextColor(context.getColor(R.color.blue));
                userViewHolder.tvEndDateData.setVisibility(View.GONE);
                userViewHolder.tvStartDateData.setVisibility(View.GONE);
                userViewHolder.tvEndDate.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            ViewHolder userViewHolder = (ViewHolder) holder;
            String caption = "";
            try {
                caption = URLDecoder.decode(feedResponse.get(position).getFeedResponse().getCaption(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            userViewHolder.txt_data.setText(caption);
//            userViewHolder.txt_data.setShowingLine(2);
//            userViewHolder.txt_data.addShowMoreText("show more");
//            userViewHolder.txt_data.addShowLessText("show less");
//            userViewHolder.txt_data.setShowMoreColor(Color.BLACK);
//            userViewHolder.txt_data.setShowLessTextColor(Color.BLACK);

            try {
                if (holder instanceof MultipleMediaViewHolder) {
                    //Multiple Media
                    MultipleMediaViewHolder mHolder = (MultipleMediaViewHolder) holder;
                    if (position == 0) {
                        mHolder.dummyPadding.setVisibility(GONE);

                    } else {
                        mHolder.dummyPadding.setVisibility(View.GONE);
                    }


                    mHolder.setAdapter(feedResponse.get(position).getFeedResponse().getPosts());

                    mHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            itemCallback.onLongClicked(feedResponse.get(position).getFeedResponse().getId(), userViewHolder.txt_like_txt, userViewHolder.img_like, userViewHolder.txt_like, userViewHolder.layout_likes_details, userViewHolder.txt_recent_user_name, userViewHolder.text_others_like, userViewHolder.and_text, position, userViewHolder.img_save);

                            return false;
                        }
                    });

                    //------------------------------------------------------------------------- changes for show last 3 user profile liked this post ----------------


                    if (feedResponse.get(position).getFeedResponse().getPostLikes() != null && feedResponse.get(position).getFeedResponse().getPostLikes().size() > 0) {


                        if (feedResponse.get(position).getFeedResponse().getPostLikes().size() == 1) {
                            mHolder.likeUser1.setVisibility(VISIBLE);
                            mHolder.likeUser2.setVisibility(GONE);
                            mHolder.likeUser3.setVisibility(GONE);
                            Log.d("onBindViewHolder:", " " + feedResponse.get(position).getFeedResponse().getPostLikes().size());
                            CommonUtils.loadImage(mHolder.ivLikeUser1, feedResponse.get(position).getFeedResponse().getPostLikes().get(0).getUser().getThumbnail(), context);


                        } else if (feedResponse.get(position).getFeedResponse().getPostLikes().size() == 2) {
                            mHolder.likeUser1.setVisibility(VISIBLE);
                            mHolder.likeUser2.setVisibility(VISIBLE);
                            mHolder.likeUser3.setVisibility(GONE);
                            CommonUtils.loadImage(mHolder.ivLikeUser1, feedResponse.get(position).getFeedResponse().getPostLikes().get(0).getUser().getThumbnail(), context);
                            CommonUtils.loadImage(mHolder.ivLikeUser2, feedResponse.get(position).getFeedResponse().getPostLikes().get(1).getUser().getThumbnail(), context);
                            Log.d("onBindViewHolder:", " " + feedResponse.get(position).getFeedResponse().getPostLikes().size());

                        } else if (feedResponse.get(position).getFeedResponse().getPostLikes().size() == 3) {
                            mHolder.likeUser1.setVisibility(VISIBLE);
                            mHolder.likeUser2.setVisibility(VISIBLE);
                            mHolder.likeUser3.setVisibility(VISIBLE);
                            CommonUtils.loadImage(mHolder.ivLikeUser1, feedResponse.get(position).getFeedResponse().getPostLikes().get(0).getUser().getThumbnail(), context);
                            CommonUtils.loadImage(mHolder.ivLikeUser2, feedResponse.get(position).getFeedResponse().getPostLikes().get(1).getUser().getThumbnail(), context);
                            CommonUtils.loadImage(mHolder.ivLikeUser3, feedResponse.get(position).getFeedResponse().getPostLikes().get(2).getUser().getThumbnail(), context);
                            Log.d("onBindViewHolder:", " " + feedResponse.get(position).getFeedResponse().getPostLikes().size());

                        } else {
                            mHolder.likeUser1.setVisibility(VISIBLE);
                            mHolder.likeUser2.setVisibility(VISIBLE);
                            mHolder.likeUser3.setVisibility(VISIBLE);
                            CommonUtils.loadImage(mHolder.ivLikeUser1, feedResponse.get(position).getFeedResponse().getPostLikes().get(0).getUser().getThumbnail(), context);
                            CommonUtils.loadImage(mHolder.ivLikeUser2, feedResponse.get(position).getFeedResponse().getPostLikes().get(1).getUser().getThumbnail(), context);
                            CommonUtils.loadImage(mHolder.ivLikeUser3, feedResponse.get(position).getFeedResponse().getPostLikes().get(2).getUser().getThumbnail(), context);
                            Log.d("onBindViewHolder:", " " + feedResponse.get(position).getFeedResponse().getPostLikes().size());


                        }


                    } else {
                        mHolder.likeUser1.setVisibility(GONE);
                        mHolder.likeUser2.setVisibility(GONE);
                        mHolder.likeUser3.setVisibility(GONE);

                    }

                    //------------------------------------------------------------------------- changes for show last 3 user profile liked this post end ----------------

                    //------------------------------------------------------------------------- changes for show last 3 user profile liked this post end ----------------
                } else if (holder instanceof SingleMediaViewHolder) {
                    //Single Photo || Single Video
                    SingleMediaViewHolder mHolder = (SingleMediaViewHolder) holder;

                    mHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            itemCallback.onLongClicked(feedResponse.get(position).getFeedResponse().getId(), userViewHolder.txt_like_txt, userViewHolder.img_like, userViewHolder.txt_like, userViewHolder.layout_likes_details, userViewHolder.txt_recent_user_name, userViewHolder.text_others_like, userViewHolder.and_text, position, userViewHolder.img_save);
                            return false;
                        }
                    });


                    //------------------------------------------------------------------------- changes for show last 3 user profile liked this post ----------------


                    if (feedResponse.get(position).getFeedResponse().getPostLikes() != null && feedResponse.get(position).getFeedResponse().getPostLikes().size() > 0) {


                        if (feedResponse.get(position).getFeedResponse().getPostLikes().size() == 1) {
                            mHolder.likeUser1.setVisibility(VISIBLE);
                            mHolder.likeUser2.setVisibility(GONE);
                            mHolder.likeUser3.setVisibility(GONE);
                            Log.d("onBindViewHolder:", " " + feedResponse.get(position).getFeedResponse().getPostLikes().size());
                            CommonUtils.loadImage(mHolder.ivLikeUser1, feedResponse.get(position).getFeedResponse().getPostLikes().get(0).getUser().getThumbnail(), context);


                        } else if (feedResponse.get(position).getFeedResponse().getPostLikes().size() == 2) {
                            mHolder.likeUser1.setVisibility(VISIBLE);
                            mHolder.likeUser2.setVisibility(VISIBLE);
                            mHolder.likeUser3.setVisibility(GONE);
                            CommonUtils.loadImage(mHolder.ivLikeUser1, feedResponse.get(position).getFeedResponse().getPostLikes().get(0).getUser().getThumbnail(), context);
                            CommonUtils.loadImage(mHolder.ivLikeUser2, feedResponse.get(position).getFeedResponse().getPostLikes().get(1).getUser().getThumbnail(), context);
                            Log.d("onBindViewHolder:", " " + feedResponse.get(position).getFeedResponse().getPostLikes().size());

                        } else if (feedResponse.get(position).getFeedResponse().getPostLikes().size() == 3) {
                            mHolder.likeUser1.setVisibility(VISIBLE);
                            mHolder.likeUser2.setVisibility(VISIBLE);
                            mHolder.likeUser3.setVisibility(VISIBLE);
                            CommonUtils.loadImage(mHolder.ivLikeUser1, feedResponse.get(position).getFeedResponse().getPostLikes().get(0).getUser().getThumbnail(), context);
                            CommonUtils.loadImage(mHolder.ivLikeUser2, feedResponse.get(position).getFeedResponse().getPostLikes().get(1).getUser().getThumbnail(), context);
                            CommonUtils.loadImage(mHolder.ivLikeUser3, feedResponse.get(position).getFeedResponse().getPostLikes().get(2).getUser().getThumbnail(), context);
                            Log.d("onBindViewHolder:", " " + feedResponse.get(position).getFeedResponse().getPostLikes().size());

                        } else {
                            mHolder.likeUser1.setVisibility(VISIBLE);
                            mHolder.likeUser2.setVisibility(VISIBLE);
                            mHolder.likeUser3.setVisibility(VISIBLE);
                            CommonUtils.loadImage(mHolder.ivLikeUser1, feedResponse.get(position).getFeedResponse().getPostLikes().get(0).getUser().getThumbnail(), context);
                            CommonUtils.loadImage(mHolder.ivLikeUser2, feedResponse.get(position).getFeedResponse().getPostLikes().get(1).getUser().getThumbnail(), context);
                            CommonUtils.loadImage(mHolder.ivLikeUser3, feedResponse.get(position).getFeedResponse().getPostLikes().get(2).getUser().getThumbnail(), context);
                            Log.d("onBindViewHolder:", " " + feedResponse.get(position).getFeedResponse().getPostLikes().size());


                        }


                    } else {
                        mHolder.likeUser1.setVisibility(GONE);
                        mHolder.likeUser2.setVisibility(GONE);
                        mHolder.likeUser3.setVisibility(GONE);

                    }

                    //------------------------------------------------------------------------- changes for show last 3 user profile liked this post end ----------------

                    if (feedResponse.get(position).getFeedResponse().getSharedPosts() != null && feedResponse.get(position).getFeedResponse().getSharedPosts().size() > 0) {
                        Log.d("UUUUUUUU", feedResponse.get(position).getFeedResponse().getSharedPosts().toString());
                        // mHolder.linBottomReShare.setVisibility(VISIBLE);
                        mHolder.linReShare.setVisibility(VISIBLE);
                        SharedPost sharedPost = feedResponse.get(position).getFeedResponse().getSharedPosts().get(0);
                        CommonUtils.loadImage(mHolder.img_profile_re_share, sharedPost.getUser().getThumbnail(), context);
                        mHolder.txt_usernsme_re_share.setText(sharedPost.getUser().getUsername());
                        mHolder.txt_time_re_share.setText(sharedPost.getOriginalPostDate());
                        mHolder.text_reshare_count.setText(String.valueOf(feedResponse.get(position).getFeedResponse().getShareCount()));

                        View.OnClickListener onClickListener = v -> itemCallback.openOtherUserProfile(sharedPost.getUserId());
                        mHolder.img_profile_re_share.setOnClickListener(onClickListener);
                        mHolder.txt_usernsme_re_share.setOnClickListener(onClickListener);
                        mHolder.txt_time_re_share.setOnClickListener(onClickListener);

                    } else {
                        Log.d("UUUUUUUU", "No VIEW");
                        mHolder.linBottomReShare.setVisibility(GONE);
                        mHolder.linReShare.setVisibility(GONE);
                    }

                    if (feedResponse.get(position).getFeedResponse().getPosts().get(0).getVideo() == 1) {
                        //Single Video
                        CommonUtils.loadImage(mHolder.imageVideoThumbnail, feedResponse.get(position).getFeedResponse().getThumbnail(), context);
                        mHolder.sensitiveRl.getLayoutParams().height = (int) (420 * context.getResources().getDisplayMetrics().density);
                        mHolder.liViewsVideoCount.setVisibility(GONE);
                        mHolder.exo_fullscreen_icon.setVisibility(VISIBLE);

                        if (feedResponse.get(position).getFeedResponse().getSensitiveContent() != null) {
                            if (feedResponse.get(position).getFeedResponse().getSensitiveContent()) {
                                mHolder.sensitiveRl.setVisibility(VISIBLE);
                                mHolder.seePostButton.setOnClickListener(v -> mHolder.sensitiveRl.setVisibility(GONE));

                                Glide.with(mHolder.itemView.getContext())
                                        .load(feedResponse.get(position).getFeedResponse().getThumbnail())
                                        .transform(new BlurTransformation(100))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .apply(CommonUtils.requestOptions)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                                mHolder.sensitiveBackground.setColorFilter(ContextCompat.getColor(context, R.color.grey_black), PorterDuff.Mode.MULTIPLY);
                                                return false;
                                            }
                                        })
                                        .into(mHolder.sensitiveBackground);
                            } else {
                                mHolder.sensitiveRl.setVisibility(GONE);
                            }
                        } else {
                            mHolder.sensitiveRl.setVisibility(GONE);
                        }


                        mHolder.image.setVisibility(GONE);
                        mHolder.frame_single.setVisibility(VISIBLE);
                        mHolder.frame_single.setUrl(feedResponse.get(position).getFeedResponse().getPosts().get(0).getPost());
                        Log.e("player", "onToggleControllerVisible   " + feedResponse.get(position).getFeedResponse().getPosts().get(0).getPost());
                        mHolder.postText.setText("");

                        mHolder.exo_fullscreen_icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, FullScreenExoPlayerActivity.class);
//                                masterExoPlayerHelper.getExoPlayerHelper().pause();
                                intent.putExtra("url", feedResponse.get(position).getFeedResponse().getPosts().get(0).getPost());
//                                intent.putExtra("currentPosition",  mHolder.frame.getPlayerView().getControllerShowTimeoutMs());

                                context.startActivity(intent);
                            }
                        });


//                      DrawableImageViewTarget target = new DrawableImageViewTarget(mHolder.imageVideoThumbnail);
//                        Glide.with(context)
//                                .load(R.drawable.feed_loader)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .apply(CommonUtils.requestOptions)
//                                .into((mHolder.imageVideoThumbnail)


                        if (mHolder.frame_single.isActivated()) {
                            mHolder.imageVideoThumbnail.setVisibility(GONE);
                        } else {
                            mHolder.imageVideoThumbnail.setVisibility(VISIBLE);
                        }


                        ExoPlayerHelper.Listener listener2 = new ExoPlayerHelper.Listener() {
                            @Override
                            public void onPlayerReady() {
                                mHolder.imageVideoThumbnail.setVisibility(GONE);

                            }

                            @Override
                            public void onStart() {
                                mHolder.ivVolume.setVisibility(VISIBLE);
                                if (mHolder.frame_single.isMute()) {
                                    mHolder.ivVolume.setImageResource(R.drawable.volume_off_24);
                                } else {
                                    mHolder.ivVolume.setImageResource(R.drawable.volume_up_24);
                                }
                                Log.e("player", "onStart");
                            }

                            @Override
                            public void onStop() {
//                                Log.e("player", "Stop");

                                mHolder.ivVolume.setVisibility(GONE);
                                masterExoPlayerHelper.getExoPlayerHelper().pause();
                                masterExoPlayerHelper.getExoPlayerHelper().stop();
                                mHolder.frame_single.removePlayer();

                            }

                            @Override
                            public void onProgress(long l) {

                            }

                            @Override
                            public void onError(@Nullable ExoPlaybackException e) {
                                mHolder.imageVideoThumbnail.setVisibility(GONE);
                                mHolder.ivVolume.setVisibility(GONE);
                            }

                            @Override
                            public void onBuffering(boolean b) {
                                mHolder.imageVideoThumbnail.setVisibility(VISIBLE);

                            }

                            @Override
                            public void onToggleControllerVisible(boolean b) {
                                Log.e("player", "onToggleControllerVisible   " + b);

                            }
                        };

                        mHolder.frame_single.setListener(listener2);
//                        mHolder.frame.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
//                            @Override
//                            public void onViewAttachedToWindow(View v) {
////                                mHolder.ivVolume.setVisibility(GONE);
////                                mHolder.frame.getListener().onPlayerReady();
////                                Log.e("player", "onToggleControllerVisible   " +"Attached");
//                            }
//
//                            @Override
//                            public void onViewDetachedFromWindow(View v) {
////                                mHolder.frame.getListener().onStop();
//                                if (masterExoPlayerHelper.getExoPlayerHelper().isPlaying()) {
//                                    Log.e("SinglePlayer","isActivated");
//                                    masterExoPlayerHelper.getExoPlayerHelper().pause();
//                                    masterExoPlayerHelper.getExoPlayerHelper().stop();
//                                }
//
//                            }
//                        });


                        mHolder.frame_single.setOnClickListener(v -> {

                            mHolder.frame_single.setMute(!mHolder.frame_single.isMute());

                            if (mHolder.frame_single.isMute()) {
                                mHolder.ivVolume.setImageResource(R.drawable.volume_off_24);
                            } else {
                                mHolder.ivVolume.setImageResource(R.drawable.volume_up_24);
                            }


                        });
                    } else if (feedResponse.get(position).getFeedResponse().getPosts().get(0).getImage() == 1) {
                        //Single Photo
                        mHolder.frame_single.setVisibility(GONE);
                        mHolder.frame_single.setUrl("https://bestone-bucket-node.s3.ap-south-1.amazonaws.com/post-background/6.jpg");
                        mHolder.exo_fullscreen_icon.setVisibility(GONE);
                        mHolder.liViewsVideoCount.setVisibility(GONE);
                        mHolder.imageVideoThumbnail.setVisibility(GONE);
                        mHolder.ivVolume.setVisibility(GONE);
//
                        mHolder.image.setVisibility(VISIBLE);
                        if (feedResponse.get(position).getFeedResponse().getSensitiveContent() != null) {
                            if (feedResponse.get(position).getFeedResponse().getSensitiveContent()) {
                                mHolder.sensitiveRl.setVisibility(VISIBLE);
                                mHolder.seePostButton.setOnClickListener(v -> {
                                    mHolder.sensitiveRl.setVisibility(GONE);
                                    mHolder.image.setVisibility(VISIBLE);
                                });

                                Glide.with(mHolder.itemView.getContext())
                                        .load(feedResponse.get(position).getFeedResponse().getThumbnail())
                                        .transform(new BlurTransformation(100))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .apply(CommonUtils.requestOptions)
                                        .listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                                mHolder.sensitiveBackground.setColorFilter(ContextCompat.getColor(context, R.color.grey_black), PorterDuff.Mode.MULTIPLY);
                                                return false;
                                            }
                                        })
                                        .into(mHolder.sensitiveBackground);
                            } else {
                                mHolder.sensitiveRl.setVisibility(GONE);
                            }
                        } else {
                            mHolder.sensitiveRl.setVisibility(GONE);
                        }

                        Glide.with(mHolder.itemView.getContext())
                                .load(feedResponse.get(position).getFeedResponse().getPosts().get(0).getPost())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .apply(CommonUtils.requestOptions)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        mHolder.imageVideoThumbnail.setVisibility(GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object mod, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        mHolder.imageVideoThumbnail.setVisibility(GONE);
                                        if (feedResponse.get(position).getFeedResponse().getSensitiveContent() != null && feedResponse.get(position).getFeedResponse().getSensitiveContent()) {
                                            mHolder.image.setVisibility(View.INVISIBLE);
                                        } else {
                                            mHolder.image.setVisibility(VISIBLE);
                                        }
                                        return false;
                                    }
                                })
                                .into(mHolder.image);
                        mHolder.postText.setText("");
                        mHolder.image.setOnClickListener(v -> {
                            List<String> imagesList = new ArrayList<>();
                            imagesList.add(feedResponse.get(position).getFeedResponse().getPosts().get(0).getPost());
                            MyAlertDialog.display(((AppCompatActivity) context).getSupportFragmentManager(), imagesList, 0);
                        });

                        if (feedResponse.get(position).getFeedResponse().getActivityPost() != null || feedResponse.get(position).getFeedResponse().getSubActivityPost() != null) {
                            userViewHolder.feeling_post_layout.setVisibility(VISIBLE);
                            String a="", b="";
                            Log.e(TAG, "onBindViewHolder: " + "IF RUN");
                            try {
                                if (feedResponse.get(position).getFeedResponse().getActivityPost().getTitle() != null) {
                                    a = feedResponse.get(position).getFeedResponse().getActivityPost().getTitle();
                                }
                            } catch (Exception e) {
                                a = " ";
                                e.printStackTrace();
                            }

                            try {
                                b = feedResponse.get(position).getFeedResponse().getSubActivityPost().getTitle();
                            } catch (Exception e) {
                                b = " ";
                                e.printStackTrace();
                            }

                            try {
                                Glide.with(context).load(feedResponse.get(position).getFeedResponse().getSubActivityPost().getIcon()).centerCrop().apply(CommonUtils.requestOptions).diskCacheStrategy(DiskCacheStrategy.ALL).into(userViewHolder.imvFeelIcon);
                            } catch (Exception e) {
                                userViewHolder.imvFeelIcon.setVisibility(GONE);
                                e.printStackTrace();
                            }

                            try {
                                if (feedResponse.get(position).getFeedResponse().getActivityPost().getIcon() != null) {
                                    Glide.with(context).load(feedResponse.get(position).getFeedResponse().getActivityPost().getIcon()).centerCrop().apply(CommonUtils.requestOptions).diskCacheStrategy(DiskCacheStrategy.ALL).into(userViewHolder.imvActivityIcon);
                                }
                            } catch (Exception e) {
                                userViewHolder.imvActivityIcon.setVisibility(GONE);
                                e.printStackTrace();
                            }
                            userViewHolder.feeling1.setText(" " + "is " + " " + a + " " + b);
                        } else {
                            userViewHolder.feeling_post_layout.setVisibility(GONE);
                        }
                    } else if (feedResponse.get(position).getFeedResponse().getPosts().get(0).getIsText().equals("1")) {
                        //Single Text Post
                        mHolder.liViewsVideoCount.setVisibility(GONE);
                        mHolder.frame_single.setVisibility(GONE);
                        mHolder.imageVideoThumbnail.setVisibility(GONE);
                        mHolder.ivVolume.setVisibility(GONE);
                        mHolder.frame_single.setUrl("");
                        mHolder.postText.setText(feedResponse.get(position).getFeedResponse().getPosts().get(0).getPost());
                        mHolder.postText.setVisibility(VISIBLE);
                        mHolder.progressBar.setVisibility(GONE);
                        mHolder.image.setVisibility(GONE);
                        mHolder.exo_fullscreen_icon.setVisibility(GONE);
                        mHolder.postText.setText("");
                    }
                }
                /*else {

                    try {
                        TextMediaViewHolder mHolder = (TextMediaViewHolder) holder;
                        mHolder.postText.setVisibility(VISIBLE);
                        ((ViewHolder) holder).postText.setText( feedResponse.get(position).getFeedResponse().getPosts().get(0).getPost());
                        ((ViewHolder) holder).postText.setTextColor(Color.parseColor( feedResponse.get(position).getFeedResponse().getPosts().get(0).getTextColorCode()));
                        Glide.with(mHolder.itemView.getContext())
                                .load( feedResponse.get(position).getFeedResponse().getPosts().get(0).getPost())
                                .into(mHolder.image);

                        if ( feedResponse.get(position).getFeedResponse().getActivityPost() != null &&  feedResponse.get(position).getFeedResponse().getSubActivityPost() != null) {
                            userViewHolder.feeling_post_layout.setVisibility(VISIBLE);
                            Glide.with(context).load( feedResponse.get(position).getFeedResponse().getSubActivityPost().getIcon()).into(userViewHolder.imvFeelIcon);
                            userViewHolder.feeling1.setText(" " + "is " + " " +  feedResponse.get(position).getFeedResponse().getActivityPost().getTitle() + " " +  feedResponse.get(position).getFeedResponse().getSubActivityPost().getActivityType());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
            } catch (Exception e) {
                Log.e(TAG, "onBindViewHolder: ********");
                e.printStackTrace();
            }
            try {
                if (feedResponse.get(position).getFeedResponse().getTags() != null) {
                    if (feedResponse.get(position).getFeedResponse().getTags().size() > 0) {
                        ((ViewHolder) holder).iv_tag.setVisibility(VISIBLE);
                        ((ViewHolder) holder).iv_tag.setOnClickListener(v -> itemCallback.openTagsBottomSheet(feedResponse.get(position).getFeedResponse().getTags()));
                    } else
                        ((ViewHolder) holder).iv_tag.setVisibility(GONE);
                }

                userViewHolder.txt_usernsme.setText(feedResponse.get(position).getFeedResponse().getUser().getUsername());

                if (feedResponse.get(position).getFeedResponse().getPostSaved() == 1) {
                    userViewHolder.img_save.setImageResource(R.drawable.ic_bookmark_fill);
                } else {
                    userViewHolder.img_save.setImageResource(R.drawable.ic_bookmark_outline);

                }

                if (feedResponse.get(position).getFeedResponse().getLiked() == 1) {
                    userViewHolder.txt_like_txt.setText(context.getString(R.string.like));
                    userViewHolder.img_like.setImageResource(R.drawable.like_diamond_filled);
                } else {
                    userViewHolder.txt_like_txt.setText(context.getString(R.string.like));
                    userViewHolder.img_like.setImageResource(R.drawable.like_diamond);
                }

                try {
                    if (feedResponse.get(position).getFeedResponse().getUser().getThumbnail() == null) {
                        userViewHolder.img_profile.setImageResource(R.drawable.ic_profile_icons_avtar);
                    } else {
                        Glide.with(context).load(feedResponse.get(position).getFeedResponse().getUser().getThumbnail()).apply(CommonUtils.requestOptions).diskCacheStrategy(DiskCacheStrategy.ALL).into(userViewHolder.img_profile);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (feedResponse.get(position).getFeedResponse().getCommentCounts() <= 1) {
                    userViewHolder.txt_comment.setText(feedResponse.get(position).getFeedResponse().getCommentCounts() + " " + context.getResources().getString(R.string.Comment));
                } else {
                    userViewHolder.txt_comment.setText(feedResponse.get(position).getFeedResponse().getCommentCounts() + " " + context.getResources().getString(R.string.Comment));
                }
                if (feedResponse.get(position).getFeedResponse().getLikeCounts() != 0) {
                    userViewHolder.txt_like.setText("" + feedResponse.get(position).getFeedResponse().getLikeCounts());
                    userViewHolder.txt_recent_user_name.setText(" " + feedResponse.get(position).getFeedResponse().getPostLikes().get(0).getUser().getUsername() + " ");
                    if (feedResponse.get(position).getFeedResponse().getLikeCounts() == 1) {
                        userViewHolder.text_others_like.setVisibility(GONE);
                        userViewHolder.and_text.setVisibility(GONE);
                    } else {
                        userViewHolder.text_others_like.setText(" " + (feedResponse.get(position).getFeedResponse().getLikeCounts() - 1) + " " + context.getString(R.string.others));
                    }
                    userViewHolder.layout_likes_details.setVisibility(VISIBLE);
                    userViewHolder.layout_like_list.setVisibility(VISIBLE);
                } else {
                    userViewHolder.txt_like.setText("0");
                    userViewHolder.layout_likes_details.setVisibility(GONE);
                    userViewHolder.layout_like_list.setVisibility(GONE);
                }

                userViewHolder.layout_like_list.setOnClickListener(v -> {
                    Intent intent = new Intent(context, LikeDetailsActivity.class);
                    intent.putExtra("postId", feedResponse.get(position).getFeedResponse().getId());
                    context.startActivity(intent);
                });

                userViewHolder.txt_time.setText(feedResponse.get(position).getFeedResponse().getCreatedAt());


                userViewHolder.img_profile_layout.setOnClickListener(view -> itemCallback.openOtherUserProfile(feedResponse.get(position).getFeedResponse().getUserId()));


                View.OnClickListener viewOnclick = v -> {
                    itemCallback.likePost(feedResponse.get(position).getFeedResponse().getId(), userViewHolder.txt_like_txt, userViewHolder.img_like, userViewHolder.txt_like,
                            userViewHolder.layout_likes_details, userViewHolder.txt_recent_user_name, userViewHolder.text_others_like, userViewHolder.and_text, position, userViewHolder.layout_like_list);
                };

                ///////////////////////////////////////////////
                userViewHolder.layout_like.setOnLongClickListener(v -> {
                    if (!tipWindow.isTooltipShown()) {
                        tipWindow.showToolTip(v);
                        x.vibrate(30);
                    }
                    return false;
                });
                ///////////////////////////////////////////////

                userViewHolder.img_like.setOnClickListener(viewOnclick);
                userViewHolder.layout_like.setOnClickListener(viewOnclick);
                userViewHolder.txt_like.setOnClickListener(viewOnclick);


                userViewHolder.layout_comment.setOnClickListener(view -> itemCallback.userCommentClicked(position, feedResponse.get(position).getFeedResponse().getId(), feedResponse.get(position).getFeedResponse().getCaption(), feedResponse.get(position).getFeedResponse().getAllowComment(), false, feedResponse.get(position).getFeedResponse().getCommentCounts()));

                userViewHolder.layout_share.setOnClickListener(v -> itemCallback.sharePostClicked(position));

                userViewHolder.img_save.setOnClickListener(view -> itemCallback.saveClicked(feedResponse.get(position).getFeedResponse().getId(), position, userViewHolder.img_save));

                userViewHolder.img_menu.setOnClickListener(view -> {
                    try {
                        itemCallback.menuClicked(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                userViewHolder.img_profile.setOnClickListener(view -> context.startActivity(new Intent(context, OtherUserAccount.class)
                        .putExtra("userId", feedResponse.get(position).getFeedResponse().getUserId())));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public int getItemCount() {
        return feedResponse == null ? 0 : feedResponse.size();
    }

    public void notifyDataSetChanged(ArrayList<UserFollowerStoryResponse.Row> userStoryList) {
        this.userStoryList = userStoryList;
        notifyDataSetChanged();
    }

    public void notifyFeedsDataSetChanged(ArrayList<HomeModel> feedArrayList) {
        this.feedResponse = feedArrayList;
        notifyDataSetChanged();
    }

    public interface RecycleListener {
        void itemClicked(int position);
    }

    public static class AdViewHolder extends RecyclerView.ViewHolder {

        HeightWrappingViewPager CampViewPagerDraft;
        TextView txtAdTitleName, tvStartDateData, tvEndDateData, tvEndDate, tvStartDate;
        Button btActivate, btEdit, btDelete;
        ImageView iv_down, img_like;
        TabLayout tab_layout;
        LinearLayout like_comment_container;
        LinearLayout layout_share;
        LinearLayout layout_like, layout_comment, layout_likes_details;
        TextView txt_like, txt_recent_user_name, text_others_like, and_text,
                txt_like_txt;
        View dummyPadding;

        // new Changes in Home UI dated - 8/april /2021
        RelativeLayout likeUser1, likeUser2, likeUser3;
        ImageView ivLikeUser1, ivLikeUser2, ivLikeUser3;


        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            CampViewPagerDraft = itemView.findViewById(R.id.CampViewPagerDraft);
            txtAdTitleName = itemView.findViewById(R.id.txtAdTitleName);
            tvStartDateData = itemView.findViewById(R.id.tvStartDateData);
            tvEndDateData = itemView.findViewById(R.id.tvEndDateData);
            layout_likes_details = itemView.findViewById(R.id.layout_likes_details);
            img_like = itemView.findViewById(R.id.img_like);
            btActivate = itemView.findViewById(R.id.btActivate);
            btEdit = itemView.findViewById(R.id.btEdit);
            btDelete = itemView.findViewById(R.id.btDelete);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            iv_down = itemView.findViewById(R.id.iv_down);
            tab_layout = itemView.findViewById(R.id.tab_layout);
            like_comment_container = itemView.findViewById(R.id.like_comment_container);
            layout_like = itemView.findViewById(R.id.layout_like);
            layout_comment = itemView.findViewById(R.id.layout_comment);
            and_text = itemView.findViewById(R.id.and_text);
            layout_share = itemView.findViewById(R.id.layout_share);
            txt_like_txt = itemView.findViewById(R.id.txt_like);
            txt_like = itemView.findViewById(R.id.txt_like_users);
            text_others_like = itemView.findViewById(R.id.text_others_like);
            txt_recent_user_name = itemView.findViewById(R.id.txt_recent_user_name);
            dummyPadding = itemView.findViewById(R.id.dummy_padding);
            likeUser1 = itemView.findViewById(R.id.likeUser1);
            likeUser2 = itemView.findViewById(R.id.likeUser2);
            likeUser3 = itemView.findViewById(R.id.likeUser3);
            ivLikeUser1 = itemView.findViewById(R.id.ivLikeUser1);
            ivLikeUser2 = itemView.findViewById(R.id.ivLikeUser2);
            ivLikeUser3 = itemView.findViewById(R.id.ivLikeUser3);
        }
    }

/*    public class TextMediaViewHolder extends ViewHolder {
        ImageView image;

        public TextMediaViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }*/

    public class ProgressLoaderViewHolder extends ViewHolder {
        LottieAnimationView image;

        public ProgressLoaderViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.loading);
        }
    }

    public class MultipleMediaViewHolder extends ViewHolder {

        MyAdapter adapter;
        RecyclerView recyclerView;
        View dummyPadding;
        PagerSnapHelper pagerSnapHelper;

        public MultipleMediaViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            dummyPadding = itemView.findViewById(R.id.dummy_padding);
        }

        void setAdapter(List<FeedResponse.Post> posts) {
            adapter = new MyAdapter(posts);
            recyclerView.setAdapter(adapter);
            if (pagerSnapHelper == null) {
                pagerSnapHelper = new PagerSnapHelper();
            }
            pagerSnapHelper.attachToRecyclerView(recyclerView);

            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false));
            recyclerView.addItemDecoration(new CirclePagerIndicatorDecoration());
//            masterExoPlayerHelper.makeLifeCycleAware(fragment);
            masterExoPlayerHelper.attachToRecyclerView(recyclerView);


        }

        class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> {
            List<FeedResponse.Post> postsList;

            public MyAdapter(List<FeedResponse.Post> postsList) {
                this.postsList = postsList;
            }

            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_rows, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull Holder mHolder, int position) {

                try {
                    if (postsList.get(position).getVideo() == 1) {

                        mHolder.image_multi.setVisibility(GONE);
                        mHolder.frame_multi.setVisibility(VISIBLE);
                        mHolder.multiVideoThumbnail_multi.setVisibility(VISIBLE);
                        mHolder.liViewsVideoCount.setVisibility(GONE);
                        mHolder.exo_fullscreen_icon.setVisibility(VISIBLE);
                        mHolder.frame_multi.setUrl(postsList.get(position).getPost());


                        //    CommonUtils.loadVideoThumbnail(context,postsList.get(position).getPost(),mHolder.multiVideoThumbnail_multi);

                        mHolder.exo_fullscreen_icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, FullScreenExoPlayerActivity.class);
                                intent.putExtra("url", postsList.get(position).getPost());
//                                mHolder.frame_multi.removePlayer();
//                                masterExoPlayerHelper.getExoPlayerHelper().pause();
//                                intent.putExtra("currentPosition",  mHolder.frame_multi.getPlayerView().getControllerShowTimeoutMs()/1000);
                                context.startActivity(intent);
                            }
                        });
                        if (mHolder.frame_multi.isActivated()) {
                            mHolder.multiVideoThumbnail_multi.setVisibility(GONE);
                        } else {
                            mHolder.multiVideoThumbnail_multi.setVisibility(VISIBLE);
                        }

                        ExoPlayerHelper.Listener listener1 = new ExoPlayerHelper.Listener() {

                            @Override
                            public void onPlayerReady() {

                                mHolder.multiVideoThumbnail_multi.setVisibility(GONE);

                            }

                            @Override
                            public void onStart() {
                                mHolder.ivVolume_multi.setVisibility(VISIBLE);
                                if (mHolder.frame_multi.isMute()) {
                                    mHolder.ivVolume_multi.setImageResource(R.drawable.volume_off_24);
                                } else {
                                    mHolder.ivVolume_multi.setImageResource(R.drawable.volume_up_24);
                                }
                            }

                            @Override
                            public void onStop() {


                                mHolder.ivVolume_multi.setVisibility(GONE);
                                masterExoPlayerHelper.getExoPlayerHelper().pause();
                                masterExoPlayerHelper.getExoPlayerHelper().stop();
                                mHolder.frame_multi.removePlayer();

                            }

                            @Override
                            public void onProgress(long l) {

                            }

                            @Override
                            public void onError(@Nullable ExoPlaybackException e) {
//                                Log.e("EXOExceptiononError",e.getMessage());
                                mHolder.multiVideoThumbnail_multi.setVisibility(GONE);
                                mHolder.ivVolume_multi.setVisibility(GONE);
                            }

                            @Override
                            public void onBuffering(boolean b) {
                                if (b)
                                    mHolder.multiVideoThumbnail_multi.setVisibility(VISIBLE);
                            }

                            @Override
                            public void onToggleControllerVisible(boolean b) {

                            }
                        };

                        mHolder.frame_multi.setListener(listener1);


//                        mHolder.frame_multi.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
//                            @Override
//                            public void onViewAttachedToWindow(View v) {
////                                mHolder.ivVolume_multi.setVisibility(GONE);
////                                mHolder.frame_multi.getListener().onPlayerReady();
//                            }
//
//                            @Override
//                            public void onViewDetachedFromWindow(View v) {
//
//                                if (masterExoPlayerHelper.getExoPlayerHelper().isPlaying()) {
//                                    Log.e("MultiPlayer","isActivated");
//                                    masterExoPlayerHelper.getExoPlayerHelper().pause();
//                                    masterExoPlayerHelper.getExoPlayerHelper().stop();
//                                }
//
//
//                            }
//                        });
                        mHolder.frame_multi.setOnClickListener(v -> {
                            mHolder.frame_multi.setMute(!mHolder.frame_multi.isMute());
                            if (mHolder.frame_multi.isMute()) {
                                mHolder.ivVolume_multi.setImageResource(R.drawable.volume_off_24);
                            } else {
                                mHolder.ivVolume_multi.setImageResource(R.drawable.volume_up_24);
                            }
                        });

//                        mHolder.ivVolume.setOnClickListener(v -> {
//                            mHolder.frame.setMute(!mHolder.frame.isMute());
//                            if (mHolder.frame.isMute()) {
//                                mHolder.ivVolume.setImageResource(R.drawable.volume_off_24);
//                            } else {
//                                mHolder.ivVolume.setImageResource(R.drawable.volume_up_24);
//                            }
//                        });
                    } else if (postsList.get(position).getImage() == 1) {
                        mHolder.frame_multi.setVisibility(GONE);
                        mHolder.frame_multi.setUrl("https://bestone-bucket-node.s3.ap-south-1.amazonaws.com/post-background/6.jpg");
                        mHolder.liViewsVideoCount.setVisibility(GONE);
                        mHolder.multiVideoThumbnail_multi.setVisibility(GONE);
                        mHolder.ivVolume_multi.setVisibility(GONE);
                        mHolder.image_multi.setVisibility(VISIBLE);
                        mHolder.exo_fullscreen_icon.setVisibility(GONE);
                        try {
                            Glide.with(mHolder.image_multi.getContext())
                                    .load(postsList.get(position).getPost())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .apply(CommonUtils.requestOptions)
                                    .override(300, 500)
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            mHolder.multiVideoThumbnail_multi.setVisibility(GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object mod, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            mHolder.multiVideoThumbnail_multi.setVisibility(GONE);
                                            if (feedResponse.get(position).getFeedResponse().getSensitiveContent() != null && feedResponse.get(position).getFeedResponse().getSensitiveContent()) {
                                                mHolder.image_multi.setVisibility(View.INVISIBLE);
                                            } else {
                                                mHolder.image_multi.setVisibility(VISIBLE);
                                            }
                                            return false;
                                        }
                                    })
                                    .into(mHolder.image_multi);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mHolder.image_multi.setOnClickListener(v -> {
                            List<String> imagesList = new ArrayList<>();
                            for (FeedResponse.Post post : postsList) {
                                if (post.getImage() == 1) {
                                    imagesList.add(post.getPost());
                                }
                            }

                            MyAlertDialog.display(((AppCompatActivity) context).getSupportFragmentManager(), imagesList, position);
                        });
                    } else {
                        mHolder.exo_fullscreen_icon.setVisibility(GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public int getItemCount() {
                return postsList.size();
            }

            public class Holder extends RecyclerView.ViewHolder {
                MasterExoPlayer frame_multi;
                ImageView image_multi, ivVolume_multi, multiVideoThumbnail_multi;
                LinearLayout liViewsVideoCount;
                TextView tvVideoViewCount;
                AppCompatImageView exo_fullscreen_icon;

                public Holder(@NonNull View itemView) {
                    super(itemView);
                    frame_multi = itemView.findViewById(R.id.frame);
                    image_multi = itemView.findViewById(R.id.image);
                    ivVolume_multi = itemView.findViewById(R.id.ivVolume);
                    tvVideoViewCount = itemView.findViewById(R.id.tvVideoViewCount);
                    liViewsVideoCount = itemView.findViewById(R.id.liViewsVideoCount);
                    multiVideoThumbnail_multi = itemView.findViewById(R.id.videoThumbnail);
                    exo_fullscreen_icon = itemView.findViewById(R.id.exo_fullscreen_icon);
                }
            }
        }


    }

    public class SingleMediaViewHolder extends ViewHolder {
        MasterExoPlayer frame_single;
        ImageView image, imageVideoThumbnail, ivVolume;
        View dummyPadding;
        RelativeLayout sensitiveRl;
        ImageView sensitiveBackground;
        TextView seePostButton;
        LinearLayout liViewsVideoCount;
        TextView tvVideoViewCount;
        // TODO  for re share post added new view
        LinearLayout linReShare, linBottomReShare;
        TextView txt_like_share, txt_comment_share, text_reshare_count, txt_usernsme_re_share, txt_time_re_share;
        ImageView img_profile_re_share;
        AppCompatImageView exo_fullscreen_icon;

        public SingleMediaViewHolder(@NonNull View itemView) {
            super(itemView);
            frame_single = itemView.findViewById(R.id.frame);
            image = itemView.findViewById(R.id.image);
            imageVideoThumbnail = itemView.findViewById(R.id.imageVideoThumbnail);
            ivVolume = itemView.findViewById(R.id.ivVolume);
            dummyPadding = itemView.findViewById(R.id.dummy_padding);
            sensitiveRl = itemView.findViewById(R.id.sensitive_content_view);
            sensitiveBackground = itemView.findViewById(R.id.sensitive_background);
            seePostButton = itemView.findViewById(R.id.see_post_button);
            exo_fullscreen_icon = itemView.findViewById(R.id.exo_fullscreen_icon);

            liViewsVideoCount = itemView.findViewById(R.id.liViewsVideoCount);
            tvVideoViewCount = itemView.findViewById(R.id.tvVideoViewCount);

            // TODO  for re share post added new view
            linReShare = itemView.findViewById(R.id.linReShare);
            linBottomReShare = itemView.findViewById(R.id.linBottomReShare);
            txt_like_share = itemView.findViewById(R.id.txt_like_share);
            txt_comment_share = itemView.findViewById(R.id.txt_comment_share);
            text_reshare_count = itemView.findViewById(R.id.text_reshare_count);
            txt_usernsme_re_share = itemView.findViewById(R.id.txt_usernsme_re_share);
            txt_time_re_share = itemView.findViewById(R.id.txt_time_re_share);
            img_profile_re_share = itemView.findViewById(R.id.img_profile_re_share);

        }
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder {
        RecyclerView storyRecyclerView;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
//            storyRecyclerView = itemView.findViewById(R.id.storyRecyclerView);
        }

        void setAdapter() {
          /*  LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            UserStoryAdapter homeStoryAdapter = new UserStoryAdapter(itemView.getContext(), userStoryList, fragment);
            storyRecyclerView.setLayoutManager(layoutManager);
            storyRecyclerView.setHasFixedSize(true);
            storyRecyclerView.setAdapter(homeStoryAdapter);*/
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressForAd;
        ImageView img_profile;
        RelativeLayout img_profile_layout;
        TextView txt_data;
        TextView txt_usernsme, txt_time, txt_like, txt_comment,
                txt_dis_like, txt_recent_user_name, text_others_like, and_text,
                txt_like_txt;
        LinearLayout pager_dots, layout_share;
        LinearLayout layout_like, layout_dislike, layout_comment;
        LinearLayout layout_likes_details;
        ImageView img_dislike, img_like, img_save;
        com.airbnb.lottie.LottieAnimationView loading;
        ImageView img_menu;
        ProgressBar progressBar;
        TextView feeling1, postText;
        LinearLayout feeling_post_layout;
        LinearLayout layout_like_list;
        ImageView iv_tag, imvFeelIcon, imvActivityIcon;

        View dummyPadding;

        // new Changes in Home UI dated - 8/april /2021
        RelativeLayout likeUser1, likeUser2, likeUser3;
        ImageView ivLikeUser1, ivLikeUser2, ivLikeUser3;

        public ViewHolder(View itemView) {
            super(itemView);
            dummyPadding = itemView.findViewById(R.id.dummy_padding);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressForAd = itemView.findViewById(R.id.progressForAd);
            feeling_post_layout = itemView.findViewById(R.id.feeling_post_layout);
            iv_tag = itemView.findViewById(R.id.iv_tag);
            feeling1 = itemView.findViewById(R.id.feeling1);
            imvFeelIcon = itemView.findViewById(R.id.imvFeelIcon);
            imvActivityIcon = itemView.findViewById(R.id.imvActivityIcon);
            txt_usernsme = itemView.findViewById(R.id.txt_usernsme);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_like_txt = itemView.findViewById(R.id.txt_like);

            txt_comment = itemView.findViewById(R.id.txt_comment);
            txt_dis_like = itemView.findViewById(R.id.txt_dis_like);
            img_profile = itemView.findViewById(R.id.img_profile);
            txt_like = itemView.findViewById(R.id.txt_like_users);
            and_text = itemView.findViewById(R.id.and_text);
            img_profile_layout = itemView.findViewById(R.id.img_profile_layout);
            txt_data = itemView.findViewById(R.id.txt_data);
            text_others_like = itemView.findViewById(R.id.text_others_like);
            pager_dots = itemView.findViewById(R.id.pager_dots);

            layout_dislike = itemView.findViewById(R.id.layout_dislike);
            txt_recent_user_name = itemView.findViewById(R.id.txt_recent_user_name);
            layout_comment = itemView.findViewById(R.id.layout_comment);
            layout_share = itemView.findViewById(R.id.layout_share);
            layout_like = itemView.findViewById(R.id.layout_like);
            layout_likes_details = itemView.findViewById(R.id.layout_likes_details);
            img_dislike = itemView.findViewById(R.id.img_dislike);
            img_like = itemView.findViewById(R.id.img_like);
            img_save = itemView.findViewById(R.id.img_post_save);
            loading = itemView.findViewById(R.id.loading);
            loading.setAnimation("saved_icon.json");
            loading.playAnimation();
            loading.loop(true);
            img_menu = itemView.findViewById(R.id.img_menu);
            progressBar = itemView.findViewById(R.id.progressBar);
            postText = itemView.findViewById(R.id.postText);
            layout_like_list = itemView.findViewById(R.id.layout_like_list);


            // new Changes in Home UI dated - 8/april /2021

            likeUser1 = itemView.findViewById(R.id.likeUser1);
            likeUser2 = itemView.findViewById(R.id.likeUser2);
            likeUser3 = itemView.findViewById(R.id.likeUser3);

            ivLikeUser1 = itemView.findViewById(R.id.ivLikeUser1);
            ivLikeUser2 = itemView.findViewById(R.id.ivLikeUser2);
            ivLikeUser3 = itemView.findViewById(R.id.ivLikeUser3);

        }


    }


}
