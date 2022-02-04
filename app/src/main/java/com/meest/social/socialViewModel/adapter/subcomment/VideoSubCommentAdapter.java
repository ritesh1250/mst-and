package com.meest.social.socialViewModel.adapter.subcomment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meest.R;
import com.meest.databinding.CommentOfCommentBinding;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.responses.SvsSubCommentResponse;
import com.meest.social.socialViewModel.model.ReportTypeResponse;
import com.meest.social.socialViewModel.view.comment.VideoCommentReplyActivityNew;
import com.meest.svs.activities.VideoOtherUserActivity;
import com.meest.svs.adapters.ReportAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VideoSubCommentAdapter extends RecyclerView.Adapter<VideoSubCommentAdapter.ViewHolder> {

    private VideoCommentReplyActivityNew context;
    private List<SvsSubCommentResponse.Datum> subComment;
    private Boolean isSvs, isAd;

    public VideoSubCommentAdapter(VideoCommentReplyActivityNew context, List<SvsSubCommentResponse.Datum> subComment, Boolean isSvs, boolean isAd) {
        this.context = context;
        this.subComment = subComment;
        this.isSvs = isSvs;
        this.isAd = isAd;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_of_comment, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final SvsSubCommentResponse.Datum model = subComment.get(position);

        holder.binding.txtUserName.setText(model.getUser().getUsername());

        if (!model.getUser().getDisplayPicture().isEmpty()) {
            Glide.with(context).load(model.getUser().getDisplayPicture()).into(holder.binding.imgProfile);
        }


        if (model.getUserId().equalsIgnoreCase(SharedPrefreances.getSharedPreferenceString(holder.itemView.getContext(), SharedPrefreances.ID))) {
            holder.binding.txtDelate.setVisibility(View.VISIBLE);
            holder.binding.txtReport.setVisibility(View.GONE);
        } else {
            holder.binding.txtDelate.setVisibility(View.GONE);
            holder.binding.txtReport.setVisibility(View.VISIBLE);
        }

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
                            call = webApi.deleteSubSvsComment(header, body);
                        } else {
                            if (isAd) {
                                call = webApi.deleteAdSubPostComment(header, body);
                            } else {
                                call = webApi.deleteSubPostComment(header, body);
                            }
                        }

                        call.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                dia.dismiss();

                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {

                                    Utilss.showToast(context, context.getString(R.string.Deleted_Successfully), R.color.green);

                                    context.changedComment();

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

        holder.binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoOtherUserActivity.class);
                intent.putExtra("userId", model.getUserId());
                context.startActivity(intent);
            }
        });

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
                            ReportAdapter reportAdapter = new ReportAdapter(context, response.body(),
                                    alertDialog, progressBar, model.getId(), type);
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

        try {
            String test = URLDecoder.decode(model.getComment(), "UTF-8");
            holder.binding.txtComment.setText(test);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        holder.binding.txtTime.setText(model.getCreatedAt());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return subComment.size();
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

        CommentOfCommentBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}