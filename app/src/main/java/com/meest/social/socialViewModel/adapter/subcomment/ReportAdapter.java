package com.meest.social.socialViewModel.adapter.subcomment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.meest.R;
import com.meest.databinding.DialogChildBinding;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.social.socialViewModel.model.subcomment.ReportTypeNewResponse;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportHolder> {

    private final Context context;
    private final AlertDialog alertDialog;
    private final ProgressBar progressBar;
    private String key;
    private List<ReportTypeNewResponse.ReportName> reportList;
    private final String id;
    private RecyclerView.Adapter adaptor;
    String type;

    public ReportAdapter(Context context, ReportTypeNewResponse reportTypeResponse,
                         AlertDialog alertDialog, ProgressBar progressBar,
                         String id, String type) {
        this.id = id;
        this.context = context;
        this.alertDialog = alertDialog;
        this.progressBar = progressBar;
        this.type = type;
        reportList = reportTypeResponse.getData();

        if(reportList == null) {
            reportList = new ArrayList<>();
        }

        switch (type) {
            case "Post":
                key = "postId";
                break;
            case "Video":
                key = "videoId";
                break;
            case "Comment":
                key = "commentId";
                break;
            case "Subcomment":
                key = "subCommentID";
                break;
            case "Profile":
                key = "profileId";
                break;
            case "Group":
                key = "groupId";
                break;
            case "Audio":
                key = "audioId";
                break;
        }
    }

    @NonNull
    @Override
    public ReportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_child, parent, false);
        return new ReportHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportHolder holder, int position) {
        ReportTypeNewResponse.ReportName model = reportList.get(position);
        holder.binding.reportOption.setText(model.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                Map<String, String> myHeader = new HashMap<>();
                myHeader.put("Accept", "application/json");
                myHeader.put("Content-Type", "application/json");
                myHeader.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                HashMap<String, Object> body = new HashMap<>();
                // body
                body.put("reportType", type);
                body.put("reportText", model.getName());
                body.put(key, id);

                Log.v("harsh", "error == " + new Gson().toJson(body));

                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<ApiResponse> call = webApi.addReport(myHeader, body);
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.code() == 200 && response.body().getSuccess()) {
                            Utilss.showToast(context, context.getString(R.string.Reported_successfully), R.color.green);
                            if (adaptor != null) {
                                adaptor.notifyItemRemoved(position);
                            }
                            alertDialog.dismiss();
                        } else {
//                            Utilss.showToast(context, "Server Error", R.color.msg_fail);
                            Utilss.showToast(context, context.getString(R.string.Reported_successfully), R.color.green);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);

//                        Utilss.showToast(context, "Server Error", R.color.msg_fail);
                        Utilss.showToast(context, context.getString(R.string.Reported_successfully), R.color.green);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class ReportHolder extends RecyclerView.ViewHolder {

        DialogChildBinding binding;

        public ReportHolder(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);

        }
    }
}
