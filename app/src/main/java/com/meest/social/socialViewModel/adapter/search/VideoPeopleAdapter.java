package com.meest.social.socialViewModel.adapter.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.databinding.PeopleItemListBinding;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.responses.FollowResponse;

import com.meest.social.socialViewModel.model.search.PeopleSearchResponse;
import com.meest.svs.activities.VideoOtherUserActivity;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VideoPeopleAdapter extends RecyclerView.Adapter<VideoPeopleAdapter.peopleViewHolder> {

    private Context context;
    private List<PeopleSearchResponse.Datum> personsList;
    private LottieAnimationView loading;

    public VideoPeopleAdapter(Context context, List<PeopleSearchResponse.Datum> personsList,
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
        final PeopleSearchResponse.Datum model = personsList.get(position);
        final boolean[] isFollowing = {model.getIsFriend() == 1};

        holder.binding.name.setText(model.getFirstName() + " " + model.getLastName());
        holder.binding.name2.setText(model.getUsername());
        if (isFollowing[0]) {
            holder.binding.relFollow.setVisibility(View.GONE);
            holder.binding.relUnfollow.setVisibility(View.GONE);
        } else {
            holder.binding.relFollow.setVisibility(View.GONE);
            holder.binding.relUnfollow.setVisibility(View.GONE);
        }

        Glide.with(context).load(model.getDisplayPicture()).into(holder.binding.imgProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoOtherUserActivity.class);
                intent.putExtra("userId", model.getId());
                context.startActivity(intent);

            }
        });

        holder.binding.relUnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);

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
                            holder.binding.relFollow.setVisibility(View.GONE);
                            holder.binding.relUnfollow.setVisibility(View.GONE);
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

        holder.binding.relFollow.setOnClickListener(new View.OnClickListener() {
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
                            holder.binding.relFollow.setVisibility(View.GONE);
                            holder.binding.relUnfollow.setVisibility(View.GONE);
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

    public void notifyFeedsDataSetChanged(List<PeopleSearchResponse.Datum> list) {
//        this.personsList.addAll(list);
        notifyItemInserted(personsList.size() - 1);
    }

    public void update(List<PeopleSearchResponse.Datum> data) {
        this.personsList.clear();
        this.personsList.addAll(data);
        notifyDataSetChanged();
    }

    public class peopleViewHolder extends RecyclerView.ViewHolder {


        PeopleItemListBinding binding;

        public peopleViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);

//            profile_img = itemView.findViewById(R.id.img_profile);
//            name = itemView.findViewById(R.id.name);
//            username = itemView.findViewById(R.id.name2);
//            unfollow = itemView.findViewById(R.id.btn_remove);
//            follow = itemView.findViewById(R.id.follow);
        }
    }
}
