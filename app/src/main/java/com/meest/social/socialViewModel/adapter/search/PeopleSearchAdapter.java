package com.meest.social.socialViewModel.adapter.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.meest.social.socialViewModel.model.UnfollowResponse;
import com.meest.social.socialViewModel.model.search.PeopleSearchResponse;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PeopleSearchAdapter extends RecyclerView.Adapter<PeopleSearchAdapter.peopleViewHolder> {

    List<PeopleSearchResponse.Datum> peopleslist;
    Activity activity;
    String xToken;
    private LottieAnimationView loading;

    public PeopleSearchAdapter(List<PeopleSearchResponse.Datum> peopleslist, Activity activity,
                               LottieAnimationView loading) {
        this.peopleslist = peopleslist;
        this.activity = activity;
        this.loading = loading;
    }

    @NonNull
    @Override
    public peopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_other_item_list, parent, false);
        peopleViewHolder PVH = new peopleViewHolder(v);
        return PVH;
    }


    @Override
    public void onBindViewHolder(@NonNull peopleViewHolder holder, int position) {
        PeopleSearchResponse.Datum model = peopleslist.get(position);
        boolean isPrivate = !model.getAccountType().equalsIgnoreCase("public");
        // final boolean[] isFollowing = {model.get.equals("0")};
        xToken = SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.AUTH_TOKEN);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, OtherUserAccount.class)
                        .putExtra("userId", model.getId()));
            }
        });

        Log.e("TAg", "persondata=" + model.getIsFriend());
        Log.e("shaban", "Id" + model.getId());
        Log.e("shaban", "svae Id" + SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.ID));

        if (model.getId().equalsIgnoreCase(SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.ID))) {
            holder.follow.setVisibility(View.GONE);
        }
        holder.btn_remove.setVisibility(View.GONE);
        holder.follow.setVisibility(View.VISIBLE);

        holder.name.setText(model.getFirstName() + " " + model.getLastName());
        holder.name2.setText(model.getUsername());

        if (model.getThumbnail()!=null && !model.getThumbnail().isEmpty())
         Glide.with(activity).load(model.getThumbnail()).placeholder(R.mipmap.ic_launcher).into(holder.profile_img);
        else
            Glide.with(activity).load(model.getDisplayPicture()).placeholder(R.mipmap.ic_launcher).into(holder.profile_img);


        if (model.getId().equalsIgnoreCase(SharedPrefreances.getSharedPreferenceString(holder.itemView.getContext(), SharedPrefreances.ID))) {
            holder.follow.setVisibility(View.GONE);
        }
        if (model.getFirendStatus().equalsIgnoreCase("Following")) {
            holder.follow.setText(activity.getString(R.string.unfollow));
            holder.follow.setTextColor(activity.getResources().getColor(R.color.social_background_blue));
            holder.follow.setBackgroundResource(R.drawable.ic_people_search_unfollow);
        } else if (model.getFirendStatus().equalsIgnoreCase("Waiting")) {
            holder.follow.setText(activity.getString(R.string.waiting));
            holder.follow.setTextColor(activity.getResources().getColor(R.color.social_background_blue));
            holder.follow.setBackgroundResource(R.drawable.ic_people_search_unfollow);
        } else if (model.getFirendStatus().equalsIgnoreCase("NoFriend") || model.getFirendStatus().equalsIgnoreCase("follower")) {
            holder.follow.setText(activity.getString(R.string.follow));
            holder.follow.setTextColor(activity.getResources().getColor(R.color.white));
            holder.follow.setBackgroundResource(R.drawable.ic_people_search_follow);
        }

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getFirendStatus().equalsIgnoreCase("Following")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomAlertDialog);
                    ViewGroup viewGroup = view.findViewById(android.R.id.content);
                    final View dView = LayoutInflater.from(activity).inflate(R.layout.custom_layout_remove_people_alert, viewGroup, false);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    TextView tvYes = dView.findViewById(R.id.tvYes);
                    ImageView ivPeople = dView.findViewById(R.id.ivPeople);
                    TextView tvNamePeople = dView.findViewById(R.id.tvNamePeople);
                    TextView tvPeopleTitle = dView.findViewById(R.id.tvPeopleTitle);
                    Glide.with(activity).load(model.getDisplayPicture()).into(ivPeople);
                    tvNamePeople.setText(model.getFirstName() + " " + model.getLastName());
                    tvPeopleTitle.setText(model.getUsername());

                    TextView tvNo = dView.findViewById(R.id.tvNo);
                    tvYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            unfollow();
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
//                  //  alertDialog.show();
                } else if (model.getFirendStatus().equalsIgnoreCase("Waiting")) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomAlertDialog);
                    ViewGroup viewGroup = view.findViewById(android.R.id.content);
                    final View dView = LayoutInflater.from(activity).inflate(R.layout.custom_layout_remove_people_alert, viewGroup, false);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    TextView tvYes = dView.findViewById(R.id.tvYes);
                    TextView tvNo = dView.findViewById(R.id.tvNo);

                    ImageView ivPeople = dView.findViewById(R.id.ivPeople);
                    TextView tvNamePeople = dView.findViewById(R.id.tvNamePeople);
                    TextView tvPeopleTitle = dView.findViewById(R.id.tvPeopleTitle);
                    Glide.with(activity).load(model.getDisplayPicture()).into(ivPeople);
                    tvNamePeople.setText(model.getFirstName() + " " + model.getLastName());
                    tvPeopleTitle.setText(model.getUsername());
                    tvYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            unfollow();
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
                    alertDialog.show();

                } else if (model.getFirendStatus().equalsIgnoreCase("noFriend") || model.getFirendStatus().equalsIgnoreCase("follower")) {
                    followRequest();
                }
            }

            // unfollow people Api call here......................................................

            private void unfollow() {
                Map<String, String> map = new HashMap<>();
                map.put("x-token", xToken);

                HashMap<String, Object> body = new HashMap<>();
                body.put("followingId", model.getId());
                body.put("status", false);

                loading.setVisibility(View.VISIBLE);
                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<UnfollowResponse> call = webApi.followingStatus(map, body);
                call.enqueue(new Callback<UnfollowResponse>() {
                    @Override
                    public void onResponse(Call<UnfollowResponse> call, Response<UnfollowResponse> response) {
                        loading.setVisibility(View.GONE);
                        if (response.code() == 200 && response.body().getSuccess()) {
                            model.setFirendStatus("NoFriend");
                            peopleslist.get(position).setFirendStatus("NoFriend");
                            holder.follow.setText(activity.getString(R.string.Follow));
                            holder.follow.setTextColor(activity.getResources().getColor(R.color.white));
                            holder.follow.setBackgroundResource(R.drawable.ic_people_search_follow);
                        } else {
                            Utilss.showToast(activity, activity.getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<UnfollowResponse> call, Throwable t) {
                        loading.setVisibility(View.GONE);
                        Log.w("error", t.toString());
                        Utilss.showToast(activity, activity.getString(R.string.error_msg), R.color.grey);
                    }
                });
            }

            public void followRequest() {
                Map<String, String> map = new HashMap<>();
                map.put("x-token", xToken);

                HashMap<String, String> body = new HashMap<>();
                body.put("followingId", model.getId());

                loading.setVisibility(View.VISIBLE);
                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<ApiResponse> call = webApi.followingRequest(map, body);
                call.enqueue(new Callback<ApiResponse>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        loading.setVisibility(View.GONE);
                        if (response.code() == 200 && response.body().getSuccess()) {
                            if (isPrivate) {
                                holder.follow.setText(activity.getString(R.string.waiting));
                                model.setFirendStatus("Waiting");
                                peopleslist.get(position).setFirendStatus("Waiting");
                            } else {
                                holder.follow.setText(activity.getString(R.string.waiting));
                                holder.follow.setBackgroundColor(R.color.social_background_blue);
                                model.setFirendStatus("Following");
                                peopleslist.get(position).setFirendStatus("Following");
                            }
                            holder.follow.setTextColor(activity.getResources().getColor(R.color.social_background_blue));
                            holder.follow.setBackgroundResource(R.drawable.ic_people_search_unfollow);

                        } else {
                            Utilss.showToast(activity, activity.getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        loading.setVisibility(View.GONE);
                        Log.w("error", t.toString());
                        Utilss.showToast(activity, activity.getString(R.string.error_msg), R.color.grey);
                    }
                });
            }

        });
    }

    @Override
    public int getItemCount() {
        return peopleslist == null ? 0 : peopleslist.size();
    }

    public void notifyFeedsDataSetChanged(List<PeopleSearchResponse.Datum> list) {
        notifyItemInserted(peopleslist.size()-1);
    }

    public void update(List<PeopleSearchResponse.Datum> data) {
        this.peopleslist.clear();
        this.peopleslist.addAll(data);
        notifyDataSetChanged();
    }

    public class peopleViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile_img;
        public TextView name;
        public TextView name2;
        public TextView follow;
        private Button btn_remove;
        private RelativeLayout layout_main;

        public peopleViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_img = itemView.findViewById(R.id.img_profile);
            name = itemView.findViewById(R.id.name);
            name2 = itemView.findViewById(R.id.name2);
            follow = itemView.findViewById(R.id.follow);
            btn_remove = itemView.findViewById(R.id.btn_remove);
            layout_main = itemView.findViewById(R.id.layout_main);

        }
    }



}
