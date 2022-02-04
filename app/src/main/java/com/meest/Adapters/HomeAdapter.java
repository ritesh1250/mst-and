package com.meest.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.meest.Activities.LikeDetailsActivity;
import com.meest.Fragments.HomeFragments;
import com.meest.Interfaces.FeedDataCallback;
import com.meest.MainActivity;
import com.meest.Paramaters.InsertLikeParameters;
import com.meest.R;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.metme.MetMeUtils.ToolTipWindow;
import com.meest.model.AdMediaData;
import com.meest.model.HomeModel;
import com.meest.responses.FeedResponse;
import com.meest.responses.InsertPostLikeResponse;
import com.meest.responses.ShowFeedAdOne;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.svs.adapters.ReportAdapter;
//import com.meest.svs.models.ReportTypeResponse;
import com.meest.social.socialViewModel.model.ReportTypeResponse;
import com.meest.utils.HeightWrappingViewPager;

import org.jetbrains.annotations.NotNull;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    HomeAdapter.OnItemClickListener mItemClickListener;
    HomeAdapter.OnItemClickListenerDisLike mItemClickListenerdisike;
    HomeAdapter.OnItemClickListenerComment mItemClickListenerComment;
    HomeAdapter.OnItemClickListenerOptions OnItemClickListenerOptions;

    private Context context;
    private FeedDataCallback feedDataCallback;
    private ArrayList<HomeModel> feedResponse;
    private boolean isTrend;
    private ViewPagerAdapterForPost viewPagerAdapter;
    private HomeFragments ctx;
    int counter = 5;
    private boolean isSaved = true;
    ToolTipWindow tipWindow;

    public HomeAdapter(Context context, ArrayList<HomeModel> feedResponse, FeedDataCallback feedDataCallback, boolean isTrend, HomeFragments ctx) {
        this.context = context;
        this.feedResponse = feedResponse;
        this.feedDataCallback = feedDataCallback;
        this.isTrend = isTrend;
        this.ctx = ctx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.home_adapter, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_campaign_drafted, parent, false);
            return new AdViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull final RecyclerView.ViewHolder holder, final int position) {

        tipWindow = new ToolTipWindow(context);
        try {
            if (position >= counter - 3) {
                counter += 10;
                Thread thread = new Thread() {
                    public void run() {
                        if (!isTrend) {
                            getAD();
                        }
                    }
                };

                thread.start();
            }

            if (feedResponse.get(position).getAD()) {

                final HomeAdapter.AdViewHolder userViewHolder = (HomeAdapter.AdViewHolder) holder;
                final ShowFeedAdOne.Data model = feedResponse.get(position).getShowAdvtResponse().getData();



                List<AdMediaData> adList = new ArrayList<>();

                AdMediaData data = new AdMediaData();
                //on success

//        campaignDeleteCallback.campaignDeleted();
                userViewHolder.iv_down.setVisibility(VISIBLE);
                userViewHolder.iv_down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);

                        ViewGroup viewGroup = userViewHolder.itemView.findViewById(android.R.id.content);
                        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_post, viewGroup, false);

                        TextView txt_delete = dialogView.findViewById(R.id.txt_delete);
                        TextView txt_report = dialogView.findViewById(R.id.txt_report);
                        TextView txt_copyLink = dialogView.findViewById(R.id.txt_copyLink);
                        TextView txt_shareTo = dialogView.findViewById(R.id.txt_shareTo);

                        txt_delete.setText(context.getResources().getString(R.string.Report_Ad));
                        txt_report.setText(context.getResources().getString(R.string.Hide_Ad));

                        txt_delete.setTextColor(context.getColor(R.color.edit_line_red));
                        txt_report.setTextColor(context.getColor(R.color.edit_line_red));

                        txt_copyLink.setVisibility(View.GONE);
                        txt_shareTo.setVisibility(View.GONE);

                        builder.setView(dialogView);
                        final AlertDialog alertDialog = builder.create();

                        txt_report.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //hide ad
                                feedResponse.remove(position);
                                HomeAdapter.this.notifyItemRemoved(position);
                                alertDialog.dismiss();
                                alertDialog.dismiss();
                            }
                        });


                        txt_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //report


                                final androidx.appcompat.app.AlertDialog.Builder builder = new
                                        androidx.appcompat.app.AlertDialog.Builder(context, R.style.CustomAlertDialog);
                                final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_svs, null, false);

                                builder.setView(dialogView);

                                androidx.appcompat.app.AlertDialog alertDialog = builder.create();

                                TextView report_option = dialogView.findViewById(R.id.report_option);
                                ProgressBar progressBar = dialogView.findViewById(R.id.progress_bar);
                                RecyclerView report_recycler = dialogView.findViewById(R.id.report_recycler);

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
                                    public void onResponse(Call<ReportTypeResponse> call, Response<ReportTypeResponse> response) {
                                        progressBar.setVisibility(View.GONE);

                                        if (response.code() == 200 && response.body().getSuccess()) {
                                            feedResponse.remove(position);
                                            ReportAdapter reportAdapter = new ReportAdapter(context,
                                                    response.body(), alertDialog, progressBar, model.getId(), type);
                                            report_recycler.setLayoutManager(new LinearLayoutManager(context));
                                            report_recycler.setAdapter(reportAdapter);
                                            report_recycler.setVisibility(View.VISIBLE);
                                        } else {
                                            Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                                            alertDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ReportTypeResponse> call, Throwable t) {
                                        progressBar.setVisibility(View.GONE);

                                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                                        alertDialog.dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                        });
                        alertDialog.show();
                    }
                });


                if (model.getCampaignType().equalsIgnoreCase("CPC")) {

                } else if (model.getCampaignType().equalsIgnoreCase("CPL")) {

                } else if (model.getCampaignType().equalsIgnoreCase("CPM/CPV")) {
                }

                if (model.getTotalLike() != 0) {
                    userViewHolder.txt_like.setText("" + model.getTotalLike());
//                    userViewHolder.txt_recent_user_name.setText(" " + model.getPostLikes().get(0).getUser().getUsername() + " ");
                    if (model.getTotalLike() == 1) {
                        userViewHolder.text_others_like.setVisibility(View.GONE);
                        userViewHolder.and_text.setVisibility(View.GONE);
                    } else {
                        userViewHolder.text_others_like.setText(" " + (model.getTotalLike() - 1) + context.getString(R.string.others));
                    }
                    userViewHolder.layout_likes_details.setVisibility(VISIBLE);
                } else {
                    userViewHolder.txt_like.setText("0");
                    userViewHolder.layout_likes_details.setVisibility(View.GONE);
                }




                userViewHolder.layout_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Map<String, String> map = new HashMap<>();
                        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);

                        HashMap<String, String> HashMap = new HashMap();
                        HashMap.put("campaignId", model.getId());
                        Call<InsertPostLikeResponse> call1 = webApi1.insertLikeAd(map, HashMap);
                        call1.enqueue(new Callback<InsertPostLikeResponse>() {
                            @Override
                            public void onResponse(Call<InsertPostLikeResponse> call, Response<InsertPostLikeResponse> response) {
                                try {
                                    if (response.code() == 200 && response.body().getSuccess()) {
//                                        if (model.getLiked() == 0) {
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
                                                userViewHolder.text_others_like.setText(" " + (li - 1) + context.getString(R.string.others));
                                            }
                                            userViewHolder.layout_likes_details.setVisibility(VISIBLE);
                                        } else {
                                            userViewHolder.txt_like.setText("0");
                                            userViewHolder.layout_likes_details.setVisibility(View.GONE);
                                        }
                                    } else {
                                        Utilss.showToast(context, response.errorBody().string(), R.color.msg_fail);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<InsertPostLikeResponse> call, Throwable t) {
                                Log.w("error", t);
                                Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                            }
                        });
                    }
                });

                userViewHolder.layout_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        feedDataCallback.commentClicked(position, model.getId(), model.getName(), true, true, 0);
                    }
                });

                userViewHolder.layout_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String appPackageName = context.getPackageName();

                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Checkout this awesome app");
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
                        context.startActivity(Intent.createChooser(intent, "Share link"));
                    }
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
//        adMediaData.setButtonType(campaignViewResponse.getData().getCallToAction());
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
            } else {




                final HomeAdapter.ViewHolder userViewHolder = (HomeAdapter.ViewHolder) holder;


                final FeedResponse.Row model = feedResponse.get(position).getFeedResponse();
                if (model.getTags() != null) {
                    if (model.getTags().size() > 0) {
                        ((ViewHolder) holder).iv_tag.setVisibility(VISIBLE);
                        ((ViewHolder) holder).iv_tag.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ctx.showBottomSheetDialog(model.getTags());
                            }
                        });
                    }
                }

                if (model.getPostType().equalsIgnoreCase("feedPost")) {
                    if (model.getPosts().size() > 0) {
                        showViewPagerPost(model, userViewHolder);
                    }
                } else if (model.getPostType().equalsIgnoreCase("writeText")) {
                    if (model.getPosts().size() > 0) {
                        if (model.getPosts().get(0).getIsText().equals("1")) {
                            ((ViewHolder) holder).viewPager.setVisibility(View.GONE);
                            ((ViewHolder) holder).postText.setVisibility(VISIBLE);
                            ((ViewHolder) holder).postText.setPadding(120, 120, 120, 120);
                            ((ViewHolder) holder).postText.setText(model.getPosts().get(0).getPost());
                            ((ViewHolder) holder).postText.setTextColor(Color.parseColor(model.getPosts().get(0).getTextColorCode()));
                        } else {
                            showViewPagerPost(model, userViewHolder);
                        }
                    }
                } else if (model.getPostType().equalsIgnoreCase("feelingPost")) {
                    if (model.getPosts().size() > 0) {
                        if (model.getPosts().get(0).getIsText().equals("1")) {
                            ((ViewHolder) holder).viewPager.setVisibility(View.GONE);
                            ((ViewHolder) holder).postText.setVisibility(VISIBLE);
                            ((ViewHolder) holder).postText.setText(model.getPosts().get(0).getPost());
                        } else {
                            showViewPagerPost(model, userViewHolder);
                        }
                    }

//                    if (model.getActivityPost() != null && model.getSubActivityPost() != null) {
                    if (model.getActivityPost() != null || model.getSubActivityPost() != null) {
                        userViewHolder.feeling_post_layout.setVisibility(VISIBLE);
                        Glide.with(context).load(model.getSubActivityPost().getIcon()).into(userViewHolder.imvFeelIcon);
                        userViewHolder.feeling1.setText(" " + "is " + " " + model.getActivityPost().getTitle() + " " + model.getSubActivityPost().getTitle());
                    }
                }

                userViewHolder.txt_usernsme.setText(model.getUser().getUsername());

                if (model.getPostSaved() == 1) {
                    userViewHolder.img_save.setImageResource(R.drawable.ic_bookmark_fill);
                }

                userViewHolder.img_profile_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!model.getUserId().equals(SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID))) {
//                            context.startActivity(new Intent(context, OtherUserActivity.class).putExtra("userId", model.getUserId()));
                            context.startActivity(new Intent(context, OtherUserAccount.class).putExtra("userId", model.getUserId()));
                        } else {
                            MainActivity demo = (MainActivity) context;
                            demo.goToProfile();
                        }
                    }
                });

                if (model.getLiked() == 1) {
                    userViewHolder.txt_like_txt.setText(context.getString(R.string.like));
                    userViewHolder.img_like.setImageResource(R.drawable.like_diamond_filled);
                } else {

                    userViewHolder.txt_like_txt.setText(context.getString(R.string.like));
                    userViewHolder.img_like.setImageResource(R.drawable.like_diamond);
                }
                try {
                    if (model.getUser().getDisplayPicture() == null) {
                        userViewHolder.img_profile.setImageResource(R.drawable.ic_profile_icons_avtar);
                    } else {
                        Glide.with(context).load(model.getUser().getDisplayPicture()).into(userViewHolder.img_profile);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //  userViewHolder.txt_data.setText(model.getCaption());
                String test = URLDecoder.decode(model.getCaption(), "UTF-8");
                userViewHolder.txt_data.setText(test);

                if (model.getCommentCounts() < 1) {
                    userViewHolder.txt_comment.setText(model.getCommentCounts() +" "+context.getResources().getString(R.string.comment));
                } else {
                    userViewHolder.txt_comment.setText(model.getCommentCounts() +" "+context.getResources().getString(R.string.comment));
                }
                if (model.getLikeCounts() != 0) {
                    userViewHolder.txt_like.setText("" + model.getLikeCounts());
                    userViewHolder.txt_recent_user_name.setText(" " + model.getPostLikes().get(0).getUser().getUsername() + " ");
                    if (model.getLikeCounts() == 1) {
                        userViewHolder.text_others_like.setVisibility(View.GONE);
                        userViewHolder.and_text.setVisibility(View.GONE);
                    } else {
                        userViewHolder.text_others_like.setText(" " + (model.getLikeCounts() - 1) +context.getString(R.string.others));
                    }
                    userViewHolder.layout_likes_details.setVisibility(VISIBLE);


                    userViewHolder.layout_like_list.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LikeDetailsActivity.class);
                            intent.putExtra("postId", model.getId());
                            context.startActivity(intent);
                        }
                    });
                } else {
                    userViewHolder.txt_like.setText("0");
                    userViewHolder.layout_likes_details.setVisibility(View.GONE);
                }

                userViewHolder.txt_time.setText(model.getCreatedAt());

                userViewHolder.layout_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, String> map = new HashMap<>();
                        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);

                        InsertLikeParameters insertLikeParameters = new InsertLikeParameters(model.getId());
                        Call<InsertPostLikeResponse> call1 = webApi1.insertLike(map, insertLikeParameters);
                        call1.enqueue(new Callback<InsertPostLikeResponse>() {
                            @Override
                            public void onResponse(Call<InsertPostLikeResponse> call, Response<InsertPostLikeResponse> response) {
                                try {
                                    if (response.code() == 200 && response.body().getSuccess()) {
                                        if (model.getLiked() == 0) {
                                            userViewHolder.txt_like_txt.setText(context.getString(R.string.like));
                                            feedResponse.get(position).getFeedResponse().setLikeCounts(response.body().getData().getLikeCount());
                                            feedResponse.get(position).getFeedResponse().setLiked(1);
                                            userViewHolder.img_like.setImageResource(R.drawable.like_diamond_filled);
                                        } else {
                                            userViewHolder.txt_like_txt.setText(context.getString(R.string.like));
                                            feedResponse.get(position).getFeedResponse().setLikeCounts(response.body().getData().getLikeCount());
                                            feedResponse.get(position).getFeedResponse().setLiked(0);
                                            userViewHolder.img_like.setImageResource(R.drawable.like_diamond);
                                        }

                                        // changing like counts and data
                                        final int li = response.body().getData().getLikeCount();
                                        if (li != 0) {
                                            userViewHolder.txt_like.setText("" + li);
                                            userViewHolder.txt_recent_user_name.setText(" " + model.getPostLikes().get(0).getUser().getUsername() + " ");
                                            if (li == 1) {
                                                userViewHolder.text_others_like.setVisibility(View.GONE);
                                                userViewHolder.and_text.setVisibility(View.GONE);
                                            } else {
                                                userViewHolder.text_others_like.setText(" " + (li - 1) + context.getString(R.string.others));
                                            }
                                            userViewHolder.layout_likes_details.setVisibility(VISIBLE);
                                        } else {
                                            userViewHolder.txt_like.setText("0");
                                            userViewHolder.layout_likes_details.setVisibility(View.GONE);
                                        }
                                    } else {
                                        Utilss.showToast(context, response.body().getSuccess().toString(), R.color.msg_fail);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<InsertPostLikeResponse> call, Throwable t) {
                                Log.w("error", t);
                                Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                            }
                        });
                    }
                });

                userViewHolder.layout_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        feedDataCallback.commentClicked(position, model.getId(), model.getCaption(), model.getAllowComment(), false, 0);
                    }
                });

                userViewHolder.layout_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String appPackageName = context.getPackageName();

                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Checkout this awesome app");
                        intent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
                        context.startActivity(Intent.createChooser(intent, "Share link"));
                    }
                });

                userViewHolder.img_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, String> map = new HashMap<>();
                        map.put("Accept", "application/json");
                        map.put("Content-Type", "application/json");
                        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);

                        HashMap<String, Object> body = new HashMap<>();
                        body.put("postId", model.getId());
                        body.put("status", true);

                        Call<ApiResponse> call1 = webApi1.savePost(map, body);
                        call1.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                try {
                                    if (response.code() == 200 && response.body().getSuccess()) {
                                        userViewHolder.loading.setVisibility(View.GONE);
                                        if (model.getPostSaved() == 0) {
                                            feedResponse.get(position).getFeedResponse().setPostSaved(1);
                                            isSaved = true;
                                            userViewHolder.img_save.setImageResource(R.drawable.ic_bookmark_fill);
                                        } else {
                                            feedResponse.get(position).getFeedResponse().setPostSaved(0);
                                            isSaved = false;
                                            userViewHolder.img_save.setImageResource(R.drawable.ic_bookmark_outline);
                                        }
                                    } else {
                                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                Log.w("error", t);
                                Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                            }
                        });
                    }
                });


                userViewHolder.img_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            feedDataCallback.optionsClicked(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


                userViewHolder.img_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            if (!model.getUserId().equals(SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID))) {
//                                context.startActivity(new Intent(context, OtherUserActivity.class).putExtra("userId", model.getUserId()));
                                context.startActivity(new Intent(context, OtherUserAccount.class).putExtra("userId", model.getUserId()));
                            } else {
                                MainActivity demo = (MainActivity) context;
                                demo.goToProfile();
                            }
                        } catch (Exception e) {

                        }


                    }

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getAD() {
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json");
        map.put("Content-Type", "application/json");
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);

//        HashMap<String, Object> body = new HashMap<>();

        Call<ShowFeedAdOne> call1 = webApi1.showAdvtFeedOne(map);
        call1.enqueue(new Callback<ShowFeedAdOne>() {
            @Override
            public void onResponse(Call<ShowFeedAdOne> call, Response<ShowFeedAdOne> response) {
                try {
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        HomeModel homeModel = new HomeModel();
                        homeModel.setAD(true);
                        homeModel.setShowAdvtResponse(response.body());
                        feedResponse.add(homeModel);
//                        showAdvtResponse=response.body();
////                       showAdAdapter = new ShowAdAdapter(getContext(), showAdvtResponse);
//                       showAdAdapter.setHasStableIds(true);
//                        recyclerView_post.setAdapter(showAdAdapter);
//                        showAdAdapter.notifyDataSetChanged();

                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<ShowFeedAdOne> call, Throwable t) {

                Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void showViewPagerPost(FeedResponse.Row model, HomeAdapter.ViewHolder userViewHolder) {
        if (model.getPosts().size() > 1) {
            userViewHolder.pager_dots.setVisibility(VISIBLE);
            addDotsIndicator(userViewHolder.pager_dots, 0, model.getPosts().size());
        }
        userViewHolder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.v("harsh", "onPageSelected == " + position);
                if (model.getPosts().size() > 1) {
                    userViewHolder.pager_dots.setVisibility(VISIBLE);
                    addDotsIndicator(userViewHolder.pager_dots, position, model.getPosts().size());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (model.getPosts() != null && model.getPosts().size() > 0) {
            viewPagerAdapter = new ViewPagerAdapterForPost(context, model.getPosts(), userViewHolder.progressBar);
            userViewHolder.viewPager.setAdapter(viewPagerAdapter);
            userViewHolder.viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    public void homeFragmentFeedListingService(String pageNumber) {
        Thread thread = new Thread() {
            public void run() {
                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Map<String, String> map = new HashMap<>();
                map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
//        map.put("pageNumber", pageNumber);
//        map.put("pageSize", String.valueOf(pagePerRecord));
                HashMap<String, String> body = new HashMap<>();
                body.put("pageNumber", pageNumber);
                body.put("pageSize", String.valueOf(HomeFragments.pagePerRecord));


                final Call<FeedResponse> call = webApi.getAllFeed(map, body);
                call.enqueue(new Callback<FeedResponse>() {
                    @Override
                    public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                        Log.d("response====", String.valueOf(response));
                        try {
                            if (response.code() == 200 && response.body().getSuccess()) {
                                HomeFragments.totalCount = response.body().getData().getCount();
                                Log.d("totalCount===", String.valueOf(HomeFragments.totalCount));
                                for (int i = 0; i < response.body().getData().getRows().size(); i++) {
                                    HomeModel homeModel = new HomeModel();
                                    homeModel.setFeedResponse(response.body().getData().getRows().get(i));
                                    homeModel.setAD(false);
                                    feedResponse.add(homeModel);
                                    HomeAdapter.this.notifyDataSetChanged();
                                }
                            } else {
                                Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<FeedResponse> call, Throwable t) {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                        //Utilss.showToast(getActivity(),"Server Error", Toast.LENGTH_LONG);
                    }
                });

            }
        };
        thread.start();
    }

    private void addDotsIndicator(LinearLayout dotLayout, int position, int size) {
        TextView[] dots = new TextView[size];
        dotLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(context);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(context.getColor(R.color.light_grey));
            dotLayout.addView(dots[i]);
        }
        // adding the selected page indicator effect
        if (dots.length > 0) {
            dots[position].setTextColor(context.getColor(R.color.gray));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (feedResponse.get(position).getAD()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return feedResponse == null ? 0 : feedResponse.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        HeightWrappingViewPager viewPager;
        CircleImageView img_profile;
        RelativeLayout img_profile_layout;
        TextView txt_usernsme, txt_time, txt_like, txt_comment,
                txt_dis_like, txt_recent_user_name, text_others_like, and_text,
                txt_like_txt;
        EmojiconTextView txt_data;
        LinearLayout pager_dots, layout_share;
        LinearLayout layout_like, layout_dislike, layout_comment;
        LinearLayout layout_likes_details;
        ImageView img_dislike, img_like, img_save;
        com.airbnb.lottie.LottieAnimationView loading;
        RelativeLayout img_menu;
        ProgressBar progressBar;
        TextView feeling1, postText;
        LinearLayout feeling_post_layout;
        ImageView iv_tag, imvFeelIcon;
        LinearLayout layout_like_list;

        View dummyPadding;

        public ViewHolder(View itemView) {
            super(itemView);

            dummyPadding = itemView.findViewById(R.id.dummy_padding);

            feeling_post_layout = itemView.findViewById(R.id.feeling_post_layout);
            iv_tag = itemView.findViewById(R.id.iv_tag);
            feeling1 = itemView.findViewById(R.id.feeling1);
            imvFeelIcon = itemView.findViewById(R.id.imvFeelIcon);
            txt_usernsme = itemView.findViewById(R.id.txt_usernsme);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_like_txt = itemView.findViewById(R.id.txt_like);
            txt_like = itemView.findViewById(R.id.txt_like_users);
            txt_comment = itemView.findViewById(R.id.txt_comment);
            txt_dis_like = itemView.findViewById(R.id.txt_dis_like);
            img_profile = itemView.findViewById(R.id.img_profile);
            and_text = itemView.findViewById(R.id.and_text);
            img_profile_layout = itemView.findViewById(R.id.img_profile_layout);
            pager_dots = itemView.findViewById(R.id.pager_dots);
            viewPager = itemView.findViewById(R.id.viewPager);
            layout_like = itemView.findViewById(R.id.layout_like);
            layout_comment = itemView.findViewById(R.id.layout_comment);
            layout_share = itemView.findViewById(R.id.layout_share);
            layout_dislike = itemView.findViewById(R.id.layout_dislike);
            txt_data = itemView.findViewById(R.id.txt_data);
            text_others_like = itemView.findViewById(R.id.text_others_like);
            txt_recent_user_name = itemView.findViewById(R.id.txt_recent_user_name);
            layout_likes_details = itemView.findViewById(R.id.layout_likes_details);
            img_dislike = itemView.findViewById(R.id.img_dislike);
            img_like = itemView.findViewById(R.id.img_like);
            img_save = itemView.findViewById(R.id.img_save);
            loading = itemView.findViewById(R.id.loading);
            loading.setAnimation("saved_icon.json");
            loading.playAnimation();
            loading.loop(true);
            img_menu = itemView.findViewById(R.id.img_menu);
            progressBar = itemView.findViewById(R.id.progressBar);
            postText = itemView.findViewById(R.id.postText);
            layout_like_list = itemView.findViewById(R.id.layout_like_list);


        }
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



        }
    }

    public void setOnItemClickListener(final HomeAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

    public void setOnItemClickListenerDislike(final HomeAdapter.OnItemClickListenerDisLike mItemClickListenerdisike) {
        this.mItemClickListenerdisike = mItemClickListenerdisike;
    }

    public interface OnItemClickListenerDisLike {
        void onItemClick(int position);
    }

    public void setOnItemClickListenerComment(final HomeAdapter.OnItemClickListenerComment mItemClickListenerComment) {
        this.mItemClickListenerComment = mItemClickListenerComment;
    }

    public interface OnItemClickListenerComment {
        void onItemClick(int position);
    }

    public void setOnItemClickListenerOptions(final HomeAdapter.OnItemClickListenerOptions OnItemClickListenerOptions) {
        this.OnItemClickListenerOptions = OnItemClickListenerOptions;
    }

    public interface OnItemClickListenerOptions {

        void onItemClick(int position);
    }

}
