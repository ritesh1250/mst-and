package com.meest.social.socialViewModel.adapter.trendingAdapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.TopTrendingItemBinding;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.social.socialViewModel.model.TopResponse;
import com.meest.utils.goLiveUtils.CommonUtils;

import java.util.List;


public class TopTrendingAdapter extends RecyclerView.Adapter<TopTrendingAdapter.peopleViewHolder> {

    private static final String TAG = "TopTrendingAdapter";


    private List<TopResponse.Datum> personsList;


    @NonNull
    @Override
    public peopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_trending_item, parent, false);
        return new peopleViewHolder(v);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull peopleViewHolder holder, int position) {
        final TopResponse.Datum model = personsList.get(position);

//        final boolean[] isFollowing = {model.getIsFriend() == 1};
//
//        holder.name.setText(model.getFirstName() + " " + model.getLastName());
//        holder.username.setText(model.getUsername());
//        if (isFollowing[0]) {
//            holder.follow.setVisibility(View.GONE);
//            holder.unfollow.setVisibility(View.VISIBLE);
//        } else {
//            holder.follow.setVisibility(View.VISIBLE);
//            holder.unfollow.setVisibility(View.GONE);
//        }

        //Glide.with(context).load(model.getDisplayPicture()).into(holder.vhc_image);


        CommonUtils.loadImage(holder.binding.vhcImage, model.getThumbnail(), holder.binding.vhcImage.getContext(), R.drawable.image_placeholder);

//        holder.tvHashTagName.setText("#"+model.getHashtag());
        holder.binding.tvHashTagName.setVisibility(View.GONE);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), NotificationSocialFeedActivity.class);
                intent.putExtra("postId", model.getId());
                intent.putExtra("userId", model.getId());
                //intent.putExtra("postId", model.getId());
                v.getContext().startActivity(intent);

//
//                Intent intent=new Intent(context, NotificationSocialFeedActivity.class);
//                intent.putExtra("userId",model.getAddedBy());
//
//                context.startActivity(intent);
//                    Intent intent = new Intent(context, VideoOtherUserActivity.class);
//                    intent.putExtra("userId", model.getId());
//                    context.startActivity(intent);
            }
        });

//        holder.unfollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loading.setVisibility(View.VISIBLE);
//
//                Map<String, String> header = new HashMap<>();
//                header.put("Accept", "application/json");
//                header.put("Content-Type", "application/json");
//                header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
//                // body
//                HashMap<String, String> body = new HashMap<>();
//                body.put("followingId", model.getId());
//                body.put("status", String.valueOf(false));
//                body.put("svs", String.valueOf(true));
//
//                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
//                Call<FollowResponse> call = webApi.removeFollow(header, body);
//                call.enqueue(new Callback<FollowResponse>() {
//                    @Override
//                    public void onResponse(@NotNull Call<FollowResponse> call, @NotNull Response<FollowResponse> response) {
//                        loading.setVisibility(View.GONE);
//                        if (response.code() == 200 && response.body().getSuccess()) {
//                            isFollowing[0] = false;
//                            holder.follow.setVisibility(View.VISIBLE);
//                            holder.unfollow.setVisibility(View.GONE);
//                        } else {
//                            Utilss.showToast(context, "Server Error", R.color.msg_fail);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NotNull Call<FollowResponse> call, @NotNull Throwable t) {
//                        loading.setVisibility(View.GONE);
//                        Utilss.showToast(context, "Server Error", R.color.msg_fail);
//                    }
//                });
//            }
//        });
//
//        holder.follow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loading.setVisibility(View.VISIBLE);
//
//                Map<String, String> header = new HashMap<>();
//                header.put("Accept", "application/json");
//                header.put("Content-Type", "application/json");
//                header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
//
//                HashMap<String, String> body = new HashMap<>();
//                body.put("followingId", model.getId());
//                body.put("svs", String.valueOf(true));
//
//                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
//                Call<FollowResponse> call;
//                call = webApi.followRequest(header, body);
//                call.enqueue(new Callback<FollowResponse>() {
//                    @Override
//                    public void onResponse(@NotNull Call<FollowResponse> call, @NotNull Response<FollowResponse> response) {
//                        loading.setVisibility(View.GONE);
//                        if (response.code() == 200 && response.body().getSuccess()) {
//                            isFollowing[0] = true;
//                            holder.follow.setVisibility(View.GONE);
//                            holder.unfollow.setVisibility(View.VISIBLE);
//                        } else {
//                            Utilss.showToast(context, "Server Error", R.color.msg_fail);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NotNull Call<FollowResponse> call, @NotNull Throwable t) {
//                        loading.setVisibility(View.GONE);
//                        Utilss.showToast(context, "Server Error", R.color.msg_fail);
//                    }
//                });
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (personsList != null)
            return personsList.size();
        else
            return 0;

    }

    public void setData(List<TopResponse.Datum> list) {

        personsList = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<TopResponse.Datum> data) {
        for (int i = 0; i < data.size(); i++) {
            personsList.add(data.get(i));
            notifyItemInserted(personsList.size() - 1);
        }
    }

    public class peopleViewHolder extends RecyclerView.ViewHolder {
        TopTrendingItemBinding binding;

        public peopleViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
