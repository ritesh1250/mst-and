package com.meest.svs.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.R;
import com.meest.responses.FollowResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.svs.activities.VideoOtherUserActivity;
import com.meest.svs.models.FollowListResponse;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class VideoFollowAdapter extends RecyclerView.Adapter<VideoFollowAdapter.peopleViewHolder> {

    private Context context;
    private List<FollowListResponse.FollowData> personsList;
    private LottieAnimationView loading;

    public VideoFollowAdapter(Context context, List<FollowListResponse.FollowData> personsList,
                              LottieAnimationView loading) {
        this.context = context;
        this.personsList = personsList;
        this.loading = loading;
    }

    @NonNull
    @Override
    public peopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item_list, parent, false);
        return new peopleViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull peopleViewHolder holder, int position) {
        final FollowListResponse.FollowData model = personsList.get(position);
        final boolean[] isFollowing = {model.getIsFriend() == 1};

        holder.name.setText(model.getFirstName() + " " + model.getLastName());
        holder.username.setText(model.getUsername());
        if (isFollowing[0]) {
            holder.follow.setVisibility(View.GONE);
            holder.unfollow.setVisibility(View.VISIBLE);
        } else {
            holder.follow.setVisibility(View.VISIBLE);
            holder.unfollow.setVisibility(View.GONE);
        }


        if (model.getId().equalsIgnoreCase(SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID))) {
            holder.follow.setVisibility(View.GONE);
            holder.unfollow.setVisibility(View.GONE);
        }

        Glide.with(context).load(model.getDisplayPicture()).into(holder.profile_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoOtherUserActivity.class);
                intent.putExtra("userId", model.getId());
                context.startActivity(intent);
            }
        });

        holder.unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                final View dView = LayoutInflater.from(context).inflate(R.layout.custom_layout_remove_people_alert, viewGroup, false);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                TextView tvYes = dView.findViewById(R.id.tvYes);
                ImageView ivPeople = dView.findViewById(R.id.ivPeople);
                TextView tvNamePeople = dView.findViewById(R.id.tvNamePeople);
                TextView tvPeopleTitle = dView.findViewById(R.id.tvPeopleTitle);
                Glide.with(context).load(model.getDisplayPicture()).into(ivPeople);
                tvNamePeople.setText(model.getFirstName() + " " + model.getLastName());
                tvPeopleTitle.setText(model.getUsername());

                TextView tvNo = dView.findViewById(R.id.tvNo);
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> header = new HashMap<>();
                        header.put("Accept", "application/json");
                        header.put("Content-Type", "application/json");
                        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
                        // body
                        HashMap<String, String> body = new HashMap<>();
                        body.put("followingId", model.getId());
                        body.put("status", String.valueOf(false));
                        body.put("svs", String.valueOf(true));

                        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                        Call<FollowResponse> call = webApi.removeFollow(header, body);
                        call.enqueue(new Callback<FollowResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<FollowResponse> call, @NotNull Response<FollowResponse> response) {
                                loading.setVisibility(View.GONE);
                                if (response.code() == 200 && response.body().getSuccess()) {
                                    isFollowing[0] = false;
                                    holder.follow.setVisibility(View.VISIBLE);
                                    holder.unfollow.setVisibility(View.GONE);
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
                        alertDialog.dismiss();
                    }
                });
                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setContentView(dView);
            }
        });

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);

                Map<String, String> header = new HashMap<>();
                header.put("Accept", "application/json");
                header.put("Content-Type", "application/json");
                header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                HashMap<String, String> body = new HashMap<>();
                body.put("followingId", model.getId());
                body.put("svs", String.valueOf(true));

                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<FollowResponse> call;
                call = webApi.followRequest(header, body);
                call.enqueue(new Callback<FollowResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<FollowResponse> call, @NotNull Response<FollowResponse> response) {
                        loading.setVisibility(View.GONE);
                        if (response.code() == 200 && response.body().getSuccess()) {
                            isFollowing[0] = true;
                            holder.follow.setVisibility(View.GONE);
                            holder.unfollow.setVisibility(View.VISIBLE);
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
    }


    @Override
    public int getItemCount() {
        return personsList.size();
    }

    public class peopleViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView profile_img;
        public TextView name, username, follow;
        private Button unfollow;

        public peopleViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_img = itemView.findViewById(R.id.img_profile);
            name = itemView.findViewById(R.id.name);
            username = itemView.findViewById(R.id.name2);
            unfollow = itemView.findViewById(R.id.btn_remove);
            follow = itemView.findViewById(R.id.follow);
        }
    }
}
