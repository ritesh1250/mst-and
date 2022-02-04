package com.meest.Adapters;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Interfaces.CommentClicked;
import com.meest.model.DeleteVideoParam;
import com.meest.R;
import com.meest.responses.AddLikeResponse;
import com.meest.responses.FollowResponse;
import com.meest.responses.VideosResponse;
import com.meest.Services.DownloadVideo;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.svs.activities.VideoOtherUserActivity;
import com.meest.svs.adapters.ReportAdapter;
import com.meest.svs.interfaces.VideoDeleteCallback;
import com.meest.meestbhart.login.model.ApiResponse;
//import com.meest.svs.models.ReportTypeResponse;
import com.meest.social.socialViewModel.model.ReportTypeResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeVideoAdapter extends RecyclerView.Adapter<HomeVideoAdapter.ViewHolder> {

    private Context context;
    private List<VideosResponse.Row> videoList;
    private LottieAnimationView loading;
    private VideoDeleteCallback videoDeleteCallback;
    private CommentClicked commentClicked;


    public HomeVideoAdapter(Context context, List<VideosResponse.Row> videoList,
                            LottieAnimationView loading, VideoDeleteCallback videoDeleteCallback,
                            CommentClicked commentClicked) {
        this.context = context;
        this.videoList = videoList;
        this.loading = loading;
        this.videoDeleteCallback = videoDeleteCallback;
        this.commentClicked = commentClicked;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home_videos_item, parent, false);
        itemLayoutView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        return new ViewHolder(itemLayoutView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VideosResponse.Row model = videoList.get(position);
        holder.ivPlay.setVisibility(View.GONE);

        final boolean isCurrentUser = model.getUserId().equals(SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID));

        if (isCurrentUser) {
            holder.layout_profile.setVisibility(View.GONE);
            holder.txt_follow.setVisibility(View.GONE);
            holder.option_menu.setVisibility(View.GONE);
        }

        if (model.getAllowComment() != null) {
            if (!model.getAllowComment()) {
                holder.viewComment.setVisibility(View.GONE);
            }
        }

        String decodedText;
        try {
            decodedText = URLDecoder.decode(model.getDescription(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            decodedText = model.getDescription();
        }
        holder.tvDesc.setText(decodedText);

        // setting data
        holder.tvUsername.setText("@" + model.getUser().getUsername());
        holder.tvLikes.setText(String.valueOf(model.getLikes()));
        holder.tvComments.setText(String.valueOf(model.getComments()));
        if (model.getAudioDataModel() != null) {
            holder.musicName.setText(model.getAudioDataModel().getDescription());
        }
        holder.txt_firstname.setText(model.getUser().getFirstName());
        holder.views.setText(String.valueOf(model.getViews()));
        if (model.getHashtags() != null && model.getHashtags().size() > 0) {
            StringBuilder hashTagText = new StringBuilder();
            for (String tag : model.getHashtags()) {
                hashTagText.append("#").append(tag).append(" ");
            }

            // setting hashtag
            holder.hashtags.setText(hashTagText.toString());
        } else {
            holder.hashtags.setVisibility(View.GONE);
        }

        if (videoList.get(position).getIsLiked() > 0) {
            holder.ivLike.setImageResource(R.drawable.like_diamond_filled);
        } else {
            holder.ivLike.setImageResource(R.drawable.diamond);
        }

        if (videoList.get(position).getIsFriend() > 0) {
            holder.txt_follow.setText(context.getString(R.string.Unfollow));
        } else {
            holder.txt_follow.setText(context.getString(R.string.Follow));
        }

        // setting image
        if (model.getUser() != null && model.getUser().getDisplayPicture() != null && model.getUser().getDisplayPicture().length() > 0) {
            holder.ivUser.setBorderColor(context.getResources().getColor(R.color.white));
            holder.ivUser.setBorderWidth((int) context.getResources().getDimension(R.dimen._1sdp));

            Glide.with(context)
                    .load(model.getUser().getDisplayPicture())
                    .apply(new RequestOptions().override(400, 600)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .error(R.drawable.ic_profile_icons_avtar))
                    .into(holder.ivUser);
        } else {
            holder.ivUser.setImageResource(R.drawable.ic_profile_icons_avtar);
        }

        boolean showDialog = false;

        try {
            if (model.getAllowDownload()) {
                showDialog = true;
            }
            if (model.getAllowDuet()) {
                showDialog = true;
            }
            if (model.getAllowTrio()) {
                showDialog = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!showDialog) {
            holder.share_layout.setVisibility(View.GONE);
        }

        holder.share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setMessage("Create Duo, Trio or Download video");
                try {
                    if (model.getAllowTrio()) {
                        ad.setPositiveButton("Make Trio", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new DownloadVideo(context, "trio", model.getAudioDataModel()).execute(model.getVideoURL());
                            }
                        });
                    }

                    if (model.getAllowDuet()) {
                        ad.setNegativeButton("duo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new DownloadVideo(context, "duo", model.getAudioDataModel()).execute(model.getVideoURL());
                            }
                        });
                    }
                    if (model.getAllowDownload()) {
                        ad.setNeutralButton("Download/Share Video", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new DownloadVideo(context, "download", model.getAudioDataModel()).execute(model.getVideoURL());
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                ad.show();
            }
        });

        holder.viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentClicked.commentClicked(position, model.getId(), model.getDescription(), model.getAllowComment());
            }
        });

        holder.ivMusicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
//        animation.setFillAfter(true);
//        holder.ivMusic.startAnimation(animation);

        ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(holder.ivMusic,
                "rotation", 0f, 360f);
        imageViewObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        imageViewObjectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        imageViewObjectAnimator.setDuration(1500);
        imageViewObjectAnimator.setInterpolator(new LinearInterpolator());
        imageViewObjectAnimator.start();

        holder.txt_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);

                Map<String, String> header = new HashMap<>();
                header.put("Accept", "application/json");
                header.put("Content-Type", "application/json");
                header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
                // body
                HashMap<String, String> body = new HashMap<>();
                body.put("followingId", videoList.get(position).getUserId());
                body.put("svs", String.valueOf(true));

                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<FollowResponse> call;
                if (videoList.get(position).getIsFriend() == 0) {
                    call = webApi.followRequest(header, body);
                } else {
                    call = webApi.removeFollow(header, body);
                    body.put("status", String.valueOf(true));
                }
                call.enqueue(new Callback<FollowResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<FollowResponse> call, @NotNull Response<FollowResponse> response) {
                        loading.setVisibility(View.GONE);

                        if (response.code() == 200 && response.body().getSuccess()) {
                            if (videoList.get(position).getIsFriend() == 0) {
                                videoList.get(position).setIsFriend(1);
                                holder.txt_follow.setText(context.getString(R.string.Unfollow));
                            } else {
                                videoList.get(position).setIsFriend(0);
                                holder.txt_follow.setText(context.getString(R.string.Follow));
                            }
                        } else {
                            Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<FollowResponse> call, @NotNull Throwable t) {
                        loading.setVisibility(View.GONE);
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                });
            }
        });

        holder.ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
       /*         Intent intent = new Intent(context, VideoOtherUserActivity.class);
                intent.putExtra("userId", model.getUserId());
                context.startActivity(intent);*/

                context.startActivity(new Intent(context, VideoOtherUserActivity.class).putExtra("userId", model.getUserId()));
            }
        });

        holder.layout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w("userId", model.getUserId());
                context.startActivity(new Intent(context, VideoOtherUserActivity.class).putExtra("userId", model.getUserId()));
            }
        });

        holder.option_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new
                        AlertDialog.Builder(context, R.style.CustomAlertDialog);
                final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_svs, null, false);

                builder.setView(dialogView);

                AlertDialog alertDialog = builder.create();

                TextView report_option = dialogView.findViewById(R.id.report_option);
                ProgressBar progressBar = dialogView.findViewById(R.id.progress_bar);
                RecyclerView report_recycler = dialogView.findViewById(R.id.report_recycler);

                if (isCurrentUser) {
                    report_option.setText(context.getString(R.string.Delete));
                } else {
                    report_option.setText(context.getString(R.string.Report_Video));
                }

                report_option.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);

                        Map<String, String> header = new HashMap<>();
                        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                        if (isCurrentUser) {
                            List<String> ids = new ArrayList<>();
                            ids.add(model.getId());
                            DeleteVideoParam deleteVideoParam = new DeleteVideoParam();
                            deleteVideoParam.setIds(ids);
                            deleteVideoParam.setSvs(true);

                            Call<ApiResponse> call = webApi.deleteVideo(header, deleteVideoParam);
                            call.enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    progressBar.setVisibility(View.GONE);

                                    if (response.code() == 200 && response.body().getSuccess()) {
                                        videoDeleteCallback.videoDeleted(position);
                                    } else {
                                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {
                                    progressBar.setVisibility(View.GONE);

                                    Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                                }
                            });
                        } else {
                            Map<String, String> myHeader = new HashMap<>();
                            myHeader.put("Accept", "application/json");
                            myHeader.put("Content-Type", "application/json");
                            myHeader.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                            // body
                            String type = "Video";
                            HashMap<String, Object> body = new HashMap<>();
                            body.put("reportType", type);
                            body.put("reportedData", false);

                            Call<ReportTypeResponse> call = webApi.getReportTypes(myHeader, body);
                            call.enqueue(new Callback<ReportTypeResponse>() {
                                @Override
                                public void onResponse(Call<ReportTypeResponse> call, Response<ReportTypeResponse> response) {
                                    progressBar.setVisibility(View.GONE);

                                    if (response.code() == 200 && response.body().getSuccess()) {
                                        ReportAdapter reportAdapter = new ReportAdapter(context,
                                                response.body(), alertDialog, progressBar, model.getId(), type);
                                        report_recycler.setLayoutManager(new LinearLayoutManager(context));
                                        report_recycler.setAdapter(reportAdapter);

                                        report_option.setVisibility(View.GONE);
                                        report_recycler.setVisibility(View.VISIBLE);
                                    } else {
                                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ReportTypeResponse> call, Throwable t) {
                                    progressBar.setVisibility(View.GONE);

                                    Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                                }
                            });
                        }
                    }
                });
                alertDialog.show();
            }
        });

        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);

                Map<String, String> header = new HashMap<>();
                header.put("Accept", "application/json");
                header.put("Content-Type", "application/json");
                header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                // body
                HashMap<String, String> body = new HashMap<>();
                body.put("videoId", videoList.get(position).getId());
                body.put("svs", String.valueOf(true));

                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<AddLikeResponse> call = webApi.getLikeVideo(header, body);
                call.enqueue(new Callback<AddLikeResponse>() {
                    @Override
                    public void onResponse(Call<AddLikeResponse> call, Response<AddLikeResponse> response) {
                        loading.setVisibility(View.GONE);

                        if (response.code() == 200 && response.body().getSuccess()) {
                            if (videoList.get(position).getIsLiked() == 0) {
                                videoList.get(position).setIsLiked(1);
                                holder.ivLike.setImageResource(R.drawable.like_diamond_filled);
                            } else {
                                videoList.get(position).setIsLiked(0);
                                holder.ivLike.setImageResource(R.drawable.diamond);
                            }
                            holder.tvLikes.setText(String.valueOf(response.body().getData().getLikeCount()));
                        } else {
                            Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<AddLikeResponse> call, Throwable t) {
                        loading.setVisibility(View.GONE);
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                });
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivUser;
        TextView tvUsername, tvLikes, tvComments, tvDesc, hashtags, musicName, txt_follow, views, txt_firstname;
        ImageView ivPlay, ivDiscover, ivLike, ivMore, ivMusic, option_menu;
        LinearLayout viewLike, viewComment, viewShare, share_layout, layout_profile;
        CircularProgressBar cpb;
        RelativeLayout viewProgressbar;
        com.meest.utils.CircularProgressBar downloadProgressbar;
        ScrollView scrollView;
        CardView ivMusicLayout;


        public ViewHolder(View view) {
            super(view);
            share_layout = view.findViewById(R.id.share_layout);
            layout_profile = view.findViewById(R.id.layout_profile);
            ivUser = view.findViewById(R.id.ivUser);
            tvUsername = view.findViewById(R.id.tvUsername);
            tvDesc = view.findViewById(R.id.tvDesc);
            option_menu = view.findViewById(R.id.option_menu);
            ivMore = view.findViewById(R.id.ivMore);
            ivMusic = view.findViewById(R.id.ivMusic);
            txt_follow = view.findViewById(R.id.txt_follow);
            ivPlay = view.findViewById(R.id.ivPlay);
            tvLikes = view.findViewById(R.id.tvLikes);
            tvComments = view.findViewById(R.id.tvComments);
            viewLike = view.findViewById(R.id.viewLike);
            viewComment = view.findViewById(R.id.viewComment);
            viewShare = view.findViewById(R.id.viewShare);
            hashtags = view.findViewById(R.id.hashtags);
            views = view.findViewById(R.id.txt_videowatch);
            ivMusicLayout = view.findViewById(R.id.ivMusicLayout);
            musicName = view.findViewById(R.id.musicName);
            cpb = view.findViewById(R.id.cpb);
            viewProgressbar = view.findViewById(R.id.viewProgressbar);
            downloadProgressbar = view.findViewById(R.id.downloadProgressbar);
            ivDiscover = view.findViewById(R.id.ivDiscover);
            ivLike = view.findViewById(R.id.ivLike);
            scrollView = view.findViewById(R.id.scrollView);
            txt_firstname = view.findViewById(R.id.txt_firstname);


        }
    }
}
