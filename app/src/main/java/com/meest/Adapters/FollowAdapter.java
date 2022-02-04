package com.meest.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.social.socialViewModel.model.MediaPostResponse1;
import com.meest.social.socialViewModel.model.UnfollowResponse;
import com.meest.responses.UserResponse;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.svs.activities.VideoOtherUserActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.peopleViewHolder> {

    private Context context;
    private List<UserResponse> personsList;
    private LottieAnimationView loading;
    private boolean isSVS;

    public FollowAdapter(Context context, List<UserResponse> personsList,
                         LottieAnimationView loading, boolean isSVS) {
        this.context = context;
        this.personsList = personsList;
        this.loading = loading;
        this.isSVS = isSVS;
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
        final UserResponse model = personsList.get(position);

        holder.name.setText(model.getFirstName() + " " + model.getLastName());
        holder.username.setText(model.getUsername());

        if (model.getFriendStatus().equalsIgnoreCase("Following")) {
            holder.rel_follow.setVisibility(View.GONE);
            holder.rel_unfollow.setVisibility(View.VISIBLE);
            holder.tv_unfollow.setText(context.getString(R.string.Unfollow));
        } else if (model.getFriendStatus().equalsIgnoreCase("Waiting")) {
            holder.rel_follow.setVisibility(View.GONE);
            holder.rel_unfollow.setVisibility(View.VISIBLE);
            holder.tv_unfollow.setText(context.getString(R.string.Waiting));
        } else if (model.getFriendStatus().equalsIgnoreCase("NoFriend") || model.getFriendStatus().equalsIgnoreCase("follower")) {
            holder.rel_follow.setVisibility(View.VISIBLE);
            holder.rel_unfollow.setVisibility(View.GONE);
            holder.tv_follow.setText(context.getString(R.string.Follow));
        }

//        if (model.getId().equalsIgnoreCase(SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID))) {
//            holder.rel_follow.setVisibility(View.GONE);
//            holder.rel_unfollow.setVisibility(View.GONE);
//        }

        Glide.with(context).load(model.getDisplayPicture()).into(holder.profile_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSVS) {
                    Intent intent = new Intent(context, VideoOtherUserActivity.class);
                    intent.putExtra("userId", model.getId());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, OtherUserAccount.class);
                    intent.putExtra("userId", model.getId());
                    context.startActivity(intent);
                }
            }
        });

        holder.rel_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("unfollow", "===========================Done");
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
                        loading.setVisibility(View.VISIBLE);
                        Map<String, String> map = new HashMap<>();
                        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
                        HashMap<String, Object> body = new HashMap<>();
                        body.put("followingId", model.getId());
                        body.put("status", false);

                        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                        Call<UnfollowResponse> call = webApi.followingStatus(map, body);
                        call.enqueue(new Callback<UnfollowResponse>() {
                            @Override
                            public void onResponse(Call<UnfollowResponse> call, Response<UnfollowResponse> response) {
                                loading.setVisibility(View.GONE);
                                if (response.code() == 200 && response.body().getSuccess()) {
                                    holder.rel_follow.setVisibility(View.VISIBLE);
                                    holder.rel_unfollow.setVisibility(View.GONE);
                                    holder.tv_follow.setText(context.getString(R.string.Follow));

                                    model.setFriendStatus("NoFriend");
                                    personsList.get(position).setFriendStatus("NoFriend");
                                } else {
                                    Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                                }
                            }

                            @Override
                            public void onFailure(Call<UnfollowResponse> call, Throwable t) {
                                loading.setVisibility(View.GONE);
                                Log.w("error", t.toString());
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
                        loading.setVisibility(View.VISIBLE);
                    }
                });
                alertDialog.setContentView(dView);
            }
        });

        holder.rel_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);

                Map<String, String> map = new HashMap<>();
                map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                HashMap<String, String> body = new HashMap<>();
                body.put("followingId", model.getId());

                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<ApiResponse> call = webApi.followingRequest(map, body);
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        loading.setVisibility(View.GONE);

                        if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                            if (model.getAccountType().equalsIgnoreCase("private")) {
                                holder.rel_follow.setVisibility(View.GONE);
                                holder.rel_unfollow.setVisibility(View.VISIBLE);
                                holder.tv_unfollow.setText(context.getString(R.string.Waiting));
                                model.setFriendStatus("Waiting");
                                personsList.get(position).setFriendStatus("Waiting");
                            } else {
                                holder.rel_follow.setVisibility(View.GONE);
                                holder.rel_unfollow.setVisibility(View.VISIBLE);
                                holder.tv_unfollow.setText(context.getString(R.string.Unfollow));
                                model.setFriendStatus("Following");
                                personsList.get(position).setFriendStatus("Following");
                            }
                        } else {
                            Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        loading.setVisibility(View.GONE);
                        Log.w("error", t.toString());
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                });
            }
        });
    }

    public void loadMore(List<UserResponse> data) {
        for (int i = 0; i < data.size(); i++) {
            personsList.add(data.get(i));
            notifyItemInserted(personsList.size() - 1);
        }

    }

    @Override
    public int getItemCount() {
        return personsList.size();
    }

    public class peopleViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile_img;
        public TextView name, username, tv_follow, tv_unfollow;
        private RelativeLayout rel_follow, rel_unfollow;

        public peopleViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_img = itemView.findViewById(R.id.img_profile);
            name = itemView.findViewById(R.id.name);
            username = itemView.findViewById(R.id.name2);
            rel_follow = itemView.findViewById(R.id.rel_follow);
            rel_follow = itemView.findViewById(R.id.rel_follow);
            rel_unfollow = itemView.findViewById(R.id.rel_unfollow);
            tv_follow = itemView.findViewById(R.id.tv_follow);
            tv_unfollow = itemView.findViewById(R.id.tv_unfollow);
        }
    }
}
