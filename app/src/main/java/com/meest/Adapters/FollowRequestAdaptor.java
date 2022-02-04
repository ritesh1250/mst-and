package com.meest.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.Activities.FollowRequestActivity;
import com.meest.R;
import com.meest.meestbhart.model.AllNotificationResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.responses.FollowRequestResponse;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FollowRequestAdaptor extends RecyclerView.Adapter<FollowRequestAdaptor.mViewHolder> {
    List<AllNotificationResponse.Row> data;
    FollowRequestActivity followRequestActivity;

    public FollowRequestAdaptor(List<AllNotificationResponse.Row> data, FollowRequestActivity followRequestActivity) {
        this.data = data;
        this.followRequestActivity = followRequestActivity;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follow_request_new, parent, false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        holder.setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class mViewHolder extends RecyclerView.ViewHolder {

        TextView followName, followTime, followAccept, followDecline;
        ImageView followImage;
        LinearLayout userLayout;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            followName = itemView.findViewById(R.id.follow_name);
            followTime = itemView.findViewById(R.id.follow_time);
            followImage = itemView.findViewById(R.id.follow_image);
            followAccept = itemView.findViewById(R.id.follow_accept);
            followDecline = itemView.findViewById(R.id.follow_decline);
            userLayout = itemView.findViewById(R.id.user_layout);
        }

        public void setData(AllNotificationResponse.Row data) {
            followTime.setText(data.getCreatedAt());
            followName.setText(String.format("%s %s", data.getUser().getFirstName(), data.getUser().getLastName()));

            Glide.with(itemView.getContext()).load(data.getUser().getDisplayPicture()).placeholder(R.drawable.image_placeholder).into(followImage);
            userLayout.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), OtherUserAccount.class);
                intent.putExtra("userId", data.getUserId());
                itemView.getContext().startActivity(intent);
            });

            followAccept.setOnClickListener(v -> acceptRequest(data.getUserId()));
            followDecline.setOnClickListener(v -> rejectRequest(data.getUserId()));
        }

        void acceptRequest(String id) {
            Map<String, String> header = new HashMap<>();
            header.put("x-token", SharedPrefreances.getSharedPreferenceString(itemView.getContext(), SharedPrefreances.AUTH_TOKEN));
            // body
            HashMap<String, Object> body = new HashMap<>();
            body.put("followingId", id);
            body.put("status", true);

            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
            Call<FollowRequestResponse> call = webApi.acceptFollow(header, body);

            call.enqueue(new Callback<FollowRequestResponse>() {
                @Override
                public void onResponse(@NotNull Call<FollowRequestResponse> call, @NotNull Response<FollowRequestResponse> response) {
                    if (response.code() == 200 && response.body().getSuccess()) {
                        Utilss.showToast(itemView.getContext(), itemView.getContext().getString(R.string.Request_Accepted), R.color.grey);
                    } else {
                        Utilss.showToast(itemView.getContext(), itemView.getContext().getString(R.string.error_msg), R.color.grey);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<FollowRequestResponse> call, @NotNull Throwable t) {
                    Utilss.showToast(itemView.getContext(), itemView.getContext().getString(R.string.error_msg), R.color.grey);
                }
            });
        }

        void rejectRequest(String id) {
            Map<String, String> header = new HashMap<>();
            header.put("x-token", SharedPrefreances.getSharedPreferenceString(itemView.getContext(), SharedPrefreances.AUTH_TOKEN));
            // body
            HashMap<String, Object> body = new HashMap<>();
            body.put("followingId", id);
            body.put("status", false);

            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
            Call<FollowRequestResponse> call = webApi.rejectFollow(header, body);

            call.enqueue(new Callback<FollowRequestResponse>() {
                @Override
                public void onResponse(@NotNull Call<FollowRequestResponse> call, @NotNull Response<FollowRequestResponse> response) {
                    if (response.code() == 200 && response.body().getSuccess()) {
                        Utilss.showToast(itemView.getContext(), itemView.getContext().getString(R.string.Request_Rejected), R.color.grey);
                    } else {
                        Utilss.showToast(itemView.getContext(), itemView.getContext().getString(R.string.error_msg), R.color.grey);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<FollowRequestResponse> call, @NotNull Throwable t) {
                    Utilss.showToast(itemView.getContext(), itemView.getContext().getString(R.string.error_msg), R.color.grey);
                }
            });
        }
    }
}
