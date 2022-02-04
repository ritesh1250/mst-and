package com.meest.social.socialViewModel.adapter.comment;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meest.Interfaces.CommentDeleteCallback;
import com.meest.Interfaces.VideoCommentActionCallbackNew;
import com.meest.R;
import com.meest.databinding.CommentAdapterBinding;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.responses.VideoCommentLikeResponse;
import com.meest.social.socialViewModel.model.ReportTypeResponse;
import com.meest.social.socialViewModel.model.comment.VideoCommentResponse;
import com.meest.social.socialViewModel.view.comment.VideoCommentActivityNew;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.svs.adapters.ReportAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoCommentAdapter extends RecyclerView.Adapter<VideoCommentAdapter.ViewHolder> implements CommentDeleteCallback {
    private VideoCommentActivityNew context;
    private VideoCommentResponse videoCommentResponse;
    private VideoCommentActionCallbackNew commentReplyCallback;
    OnItemClickListener mItemClickListener;
    OnItemClickListenerLike mItemClickListenerlike;
    // private final CommentDeleteCallback commentDeleteCallback;
    private final boolean isSvs, isAd;

    public VideoCommentAdapter(VideoCommentActivityNew context, VideoCommentResponse videoCommentResponse, boolean isSvs, VideoCommentActionCallbackNew commentReplyCallback, boolean isAd) {
        this.context = context;
        this.isSvs = isSvs;
        this.videoCommentResponse = videoCommentResponse;
        this.commentReplyCallback = commentReplyCallback;
        this.isAd = isAd;
    }

    @Override
    public void delete() {
        context.changedComment();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_adapter, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final VideoCommentResponse.Row model = videoCommentResponse.getData().getRows().get(position);
        if (model.getUser().getUsername() != null) {
            holder.binding.txtUserName.setText(model.getUser().getUsername());
        }
        if (!model.getUser().getDisplayPicture().isEmpty()) {
            Glide.with(context).load(model.getUser().getDisplayPicture()).into(holder.binding.imgProfile);
        }

        holder.binding.layoutImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OtherUserAccount.class);
                intent.putExtra("userId", model.getUserId());
                context.startActivity(intent);
            }
        });
        holder.binding.txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OtherUserAccount.class);
                intent.putExtra("userId", model.getUserId());
                context.startActivity(intent);
            }
        });

        if (model.getVideoSubComments() != null && model.getVideoSubComments().size() > 1) {
            holder.binding.previousCommentText.setVisibility(View.VISIBLE);

            holder.binding.previousCommentText.setText(context.getString(R.string.view_previous_replies) +" "+ (model.getVideoSubComments().size() - 1) + " "+ (model.getVideoSubComments().size() - 1 >1 ?context.getString(R.string.replies): context.getString(R.string.reply)));
//            holder.previous_comment_text.setText(context.getString(R.string.view_previous_replies) +" "+ (model.getVideoSubComments().size() - 1) +" "+context.getString(R.string.replies));
            holder.binding.previousCommentText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentReplyCallback.replyClicked(model);
                }
            });

        } else {
            holder.binding.previousCommentText.setVisibility(View.GONE);

        }

        if (model.getLiked() == 0) {
//            holder.txt_like.setText("Like");
            holder.binding.txtLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_diamond));
        } else {
            holder.binding.txtLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_diamond_filled));
//            holder.txt_like.setText("Unlike");
//            holder.txt_like.setTextColor(context.getResources().getColor(R.color.blue));
            //  holder.txt_like.setText(R.string.Like);
        }
//        else {
//            holder.txt_like.setText(R.string.Unlike);
//            holder.txt_like.setTextColor(context.getResources().getColor(R.color.blue));
//        }

        try {
            String test = URLDecoder.decode(model.getComment(), "UTF-8");
            holder.binding.txtComment.setText(test);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        holder.binding.txtReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentReplyCallback.replyClicked(videoCommentResponse.getData().getRows().get(position));
            }
        });

        holder.binding.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);
            }
        });

        // comment like section..........................................
        holder.binding.txtLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // final VideoCommentLikeResponse videoCommentLikeResponse = new VideoCommentLikeResponse();
                Map<String, String> header = new HashMap<>();
                header.put("Accept", "application/json");
                header.put("Content-Type", "application/json");
                header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                // body
                HashMap<String, Object> body = new HashMap<>();

                if (isSvs) {
                    body.put("commentId", model.getId());
                    body.put("videoId", model.getVideoId());
                    if (model.getLiked() == 0) {

                        body.put("status", true);
                    } else {
                        body.put("status", false);
                    }
                } else {
                    if (isAd) {
                        body.put("campaignId", model.getCampaignId());
                        body.put("commentId", model.getId());

                    } else {
                        body.put("commentId", model.getId());
                        body.put("postId", model.getPostId());
                        if (model.getLiked() == 0) {
                            body.put("status", true);
                        } else {
                            body.put("status", false);
                        }
                    }
                }

                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<VideoCommentLikeResponse> call;
                if (isSvs) {
                    call = webApi.getAllVideoCommentLike(header, body);
                } else {
                    if (isAd) {
                        call = webApi.insertAdPostCommentLike(header, body);
                    } else {
                        call = webApi.insertPostCommentLike(header, body);
                    }
                }
                call.enqueue(new Callback<VideoCommentLikeResponse>() {
                    @Override
                    public void onResponse(Call<VideoCommentLikeResponse> call, Response<VideoCommentLikeResponse> response) {
                        if (response.code() == 200 && response.body().getSuccess()) {
                            if (model.getLiked() == 0) {
//                                holder.txt_like.setText("Unlike");
//                                holder.txt_like.setTextColor(context.getResources().getColor(R.color.blue));
                                holder.binding.txtLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_diamond_filled));
                                videoCommentResponse.getData().getRows().get(position).setLiked(1);
                            } else {
//                                holder.txt_like.setText("Like");
//                                holder.txt_like.setTextColor(context.getResources().getColor(R.color.gray));
                                holder.binding.txtLike.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.like_diamond));
//                                holder.txt_like.setText(R.string.Unlike);
//                                holder.txt_like.setTextColor(context.getResources().getColor(R.color.blue));
                                videoCommentResponse.getData().getRows().get(position).setLiked(0);
                            }
//                            else {
//                                holder.txt_like.setText(R.string.Like);
//                                holder.txt_like.setTextColor(context.getResources().getColor(R.color.gray));
//                                videoCommentResponse.getData().getRows().get(position).setLiked(0);
//                            }
                        } else {
                            Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoCommentLikeResponse> call, Throwable t) {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                });
            }
        });

        // comment report section............................................
        holder.binding.txtReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                String type = "Comment";
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

        if (!model.getUserId().equalsIgnoreCase(SharedPrefreances.getSharedPreferenceString(holder.itemView.getContext(), SharedPrefreances.ID))) {
            holder.binding.txtDelate.setVisibility(View.GONE);
            holder.binding.txtReport.setVisibility(View.VISIBLE);
        } else {
            holder.binding.txtReport.setVisibility(View.GONE);
            holder.binding.txtDelate.setVisibility(View.VISIBLE);
        }

        // comment delete section..........................................
        holder.binding.txtDelate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View conView = LayoutInflater.from(context).inflate(R.layout.delete_notification_popup, null);
                Button yes = conView.findViewById(R.id.yes);
                Button no = conView.findViewById(R.id.no);
                BottomSheetDialog dia = new BottomSheetDialog(context);

                Map<String, String> header = new HashMap<>();
                header.put("Accept", "application/json");
                header.put("Content-Type", "application/json");
                header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                HashMap<String, String> body = new HashMap<>();
                body.put("commentId", model.getId());

                dia.setContentView(conView);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Call<ApiResponse> call;
                        WebApi webApi = ApiUtils.getClient().create(WebApi.class);

                        if (isSvs) {
                            call = webApi.deleteSvsComment(header, body);
                        } else {
                            if (isAd) {
                                call = webApi.deleteAdPostComment(header, body);
                            } else {
                                call = webApi.deletePostComment(header, body);
                            }
                        }

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                dia.dismiss();

                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {

                                    Utilss.showToast(context, context.getString(R.string.Deleted_Successfully), R.color.green);

                                    commentReplyCallback.changedComment();

                                } else {
                                    Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                //loading.setVisibility(View.GONE);
                                Log.w("error", t.toString());
                                Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                            }
                        });
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.dismiss();
                    }
                });
                dia.show();
            }
        });


        if (model.getVideoSubComments() != null && model.getVideoSubComments().size() > 0) {
            // showing sub comments
            holder.binding.subCommentRecycler.setVisibility(View.VISIBLE);
            SubCommentHalfAdapter subCommentAdapter = new SubCommentHalfAdapter(context, model.getVideoSubComments(), isSvs, this, isAd);
            holder.binding.subCommentRecycler.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false));
            holder.binding.subCommentRecycler.setItemAnimator(new DefaultItemAnimator());
            holder.binding.subCommentRecycler.setAdapter(subCommentAdapter);

            holder.binding.subCommentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentReplyCallback.replyClicked(model);
                }
            });
        }

        holder.binding.txtTime.setText(model.getCreatedAt());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return videoCommentResponse.getData().getRows().size();
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

        CommentAdapterBinding binding;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }


    public void setOnItemClickListenerLike(final OnItemClickListenerLike mItemClickListenerlike) {
        this.mItemClickListenerlike = mItemClickListenerlike;
    }

    public interface OnItemClickListenerLike {

        void onItemClick(int position);
    }
}
