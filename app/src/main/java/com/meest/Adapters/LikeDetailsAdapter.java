package com.meest.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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
import com.meest.responses.LikeDetailsResponse;
import com.meest.social.socialViewModel.model.UnfollowResponse;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.utils.goLiveUtils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LikeDetailsAdapter extends RecyclerView.Adapter<LikeDetailsAdapter.ViewHolder> {

    ArrayList<LikeDetailsResponse.Row> peopleslist;
    Context context;
    PeopleAdapter.OnItemClickListenerUnFollow mItemClickListener;
    String xToken;
    private LottieAnimationView loading;

    public LikeDetailsAdapter(ArrayList<LikeDetailsResponse.Row> peopleslist, Context context, LottieAnimationView loading) {
        this.peopleslist = peopleslist;
        this.context = context;
        this.loading = loading;
    }

    @NonNull
    @Override
    public LikeDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item_list, parent, false);
        LikeDetailsAdapter.ViewHolder PVH = new LikeDetailsAdapter.ViewHolder(v);
        return PVH;
    }

    @Override
    public void onBindViewHolder(@NonNull LikeDetailsAdapter.ViewHolder holder, int position) {

        LikeDetailsResponse.Row model = peopleslist.get(position);

        xToken = SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN);

        boolean isPrivate = !model.getUser().getAccountType().equalsIgnoreCase("public");
        holder.name.setText(model.getUser().getFirstName() + " " + model.getUser().getLastName());
        holder.name2.setText(model.getUser().getUsername());

        //Glide.with(context).load(model.getUser().getDisplayPicture()).placeholder(R.drawable.profile_img).into(holder.profile_img);

        CommonUtils.loadImage(holder.profile_img, model.getUser().getDisplayPicture(), context);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OtherUserAccount.class);
                intent.putExtra("userId", model.getUser().getId());
                context.startActivity(intent);

            }
        });


        if (model.getUser().getFirendStatus().equalsIgnoreCase("Following")) {
            holder.follow.setText(context.getString(R.string.Unfollow));
            holder.follow.setTextColor(context.getResources().getColor(R.color.gray));
            holder.follow.setBackgroundResource(R.drawable.ic_people_search_unfollow_liked);
        } else if (model.getUser().getFirendStatus().equalsIgnoreCase("Waiting")) {
            holder.follow.setText(context.getString(R.string.Waiting));
            holder.follow.setTextColor(context.getResources().getColor(R.color.gray));
            holder.follow.setBackgroundResource(R.drawable.ic_people_search_unfollow_liked);
        } else if (model.getUser().getFirendStatus().equalsIgnoreCase("NoFriend") || model.getUser().getFirendStatus().equalsIgnoreCase("follower")) {
            holder.follow.setText(context.getString(R.string.Follow));
            holder.follow.setTextColor(context.getResources().getColor(R.color.white));
            holder.follow.setBackgroundResource(R.drawable.ic_people_search_follow);
        }

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getUser().getFirendStatus().equalsIgnoreCase("Following")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
                    ViewGroup viewGroup = view.findViewById(android.R.id.content);
                    final View dView = LayoutInflater.from(context).inflate(R.layout.custom_layout_remove_people_alert, viewGroup, false);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    TextView tvYes = dView.findViewById(R.id.tvYes);
                    ImageView ivPeople = dView.findViewById(R.id.ivPeople);
                    TextView tvNamePeople = dView.findViewById(R.id.tvNamePeople);
                    TextView tvPeopleTitle = dView.findViewById(R.id.tvPeopleTitle);
                    Glide.with(context).load(model.getUser().getDisplayPicture()).into(ivPeople);
                    tvNamePeople.setText(model.getUser().getFirstName() + " " + model.getUser().getLastName());
                    tvPeopleTitle.setText(model.getUser().getUsername());

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
                } else if (model.getUser().getFirendStatus().equalsIgnoreCase("Waiting")) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
                    ViewGroup viewGroup = view.findViewById(android.R.id.content);
                    final View dView = LayoutInflater.from(context).inflate(R.layout.custom_layout_remove_people_alert, viewGroup, false);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    TextView tvYes = dView.findViewById(R.id.tvYes);
                    TextView tvNo = dView.findViewById(R.id.tvNo);

                    ImageView ivPeople = dView.findViewById(R.id.ivPeople);
                    TextView tvNamePeople = dView.findViewById(R.id.tvNamePeople);
                    TextView tvPeopleTitle = dView.findViewById(R.id.tvPeopleTitle);
                    Glide.with(context).load(model.getUser().getDisplayPicture()).into(ivPeople);
                    tvNamePeople.setText(model.getUser().getFirstName() + " " + model.getUser().getLastName());
                    tvPeopleTitle.setText(model.getUser().getUsername());
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

                } else if (model.getUser().getFirendStatus().equalsIgnoreCase("noFriend") || model.getUser().getFirendStatus().equalsIgnoreCase("follower")) {
                    followRequest();
                }
            }

            // unfollow people Api call here......................................................

            private void unfollow() {
                Map<String, String> map = new HashMap<>();
                map.put("x-token", xToken);

                HashMap<String, Object> body = new HashMap<>();
                body.put("followingId", model.getUser().getId());
                body.put("status", false);

                loading.setVisibility(View.VISIBLE);
                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<UnfollowResponse> call = webApi.followingStatus(map, body);
                call.enqueue(new Callback<UnfollowResponse>() {
                    @Override
                    public void onResponse(Call<UnfollowResponse> call, Response<UnfollowResponse> response) {
                        loading.setVisibility(View.GONE);
                        if (response.code() == 200 && response.body().getSuccess()) {
                            model.getUser().setFirendStatus("NoFriend");
                            peopleslist.get(position).getUser().setFirendStatus("NoFriend");
                            holder.follow.setText(context.getString(R.string.Follow));
                            holder.follow.setTextColor(context.getResources().getColor(R.color.white));
                            holder.follow.setBackgroundResource(R.drawable.ic_people_search_follow);
                        } else {
                            Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<UnfollowResponse> call, Throwable t) {
                        // loading.setVisibility(View.GONE);
                        Log.w("error", t.toString());
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                });
            }

            public void followRequest() {
                Map<String, String> map = new HashMap<>();
                map.put("x-token", xToken);

                HashMap<String, String> body = new HashMap<>();
                body.put("followingId", model.getUser().getId());

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
                                holder.follow.setText(context.getString(R.string.waiting));
                                model.getUser().setFirendStatus("Waiting");
                                peopleslist.get(position).getUser().setFirendStatus("Waiting");
                                holder.follow.setTextColor(context.getResources().getColor(R.color.gray));
                                holder.follow.setBackgroundResource(R.drawable.ic_people_search_unfollow_liked);
                            } else {
                                holder.follow.setText(context.getString(R.string.Unfollow));
                                holder.follow.setBackgroundColor(R.color.grey);
                                model.getUser().setFirendStatus("Following");
                                peopleslist.get(position).getUser().setFirendStatus("Following");
                                holder.follow.setTextColor(context.getResources().getColor(R.color.gray));
                                holder.follow.setBackgroundResource(R.drawable.ic_people_search_unfollow_liked);
                            }
//                            holder.follow.setTextColor(context.getResources().getColor(R.color.gray));
//                            holder.follow.setBackgroundResource(R.drawable.ic_people_search_unfollow_liked);

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

    @Override
    public int getItemCount() {
        return peopleslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profile_img;
        public TextView name;
        public TextView name2;
        public TextView follow;
        public Button btn_remove;
        public RelativeLayout layout_main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_img = itemView.findViewById(R.id.img_profile);
            name = itemView.findViewById(R.id.name);
            name2 = itemView.findViewById(R.id.name2);
            follow = itemView.findViewById(R.id.tv_follow);
            btn_remove = itemView.findViewById(R.id.btn_remove);
            layout_main = itemView.findViewById(R.id.layout_main);

        }
    }

}


