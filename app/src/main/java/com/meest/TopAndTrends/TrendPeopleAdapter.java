package com.meest.TopAndTrends;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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

import com.meest.R;
import com.meest.responses.TrendPeopleResponse;
import com.meest.social.socialViewModel.model.UnfollowResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.meestbhart.login.model.ApiResponse;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrendPeopleAdapter extends RecyclerView.Adapter<TrendPeopleAdapter.peopleViewHolder> {

    private Context context;
    private List<TrendPeopleResponse.Datum> personsList;
    TrendPeopleAdapter.OnItemClickListenerUnFollow mItemClickListener;
    String xToken;

    public TrendPeopleAdapter(Context context, List<TrendPeopleResponse.Datum> personsList) {
        this.context = context;
        this.personsList = personsList;
    }

    @NonNull
    @Override
    public peopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_people_item_list, parent, false);
        peopleViewHolder PVH = new peopleViewHolder(v);
        return PVH;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull peopleViewHolder holder, int position) {
        final TrendPeopleResponse.Datum model = personsList.get(position);
        final boolean[] isFollowing = {model.getFollowerCount() == 1};
        xToken = SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN);

        boolean isPrivate = !model.getAccountType().equalsIgnoreCase("public");

        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);
            }
        });

        holder.name.setText(model.getFirstName() + " " + model.getLastName());
        holder.username.setText(model.getUserName());
//        if(isFollowing[0]) {
//            holder.followButton.setText("Unfollow");
//            holder.followButton.setBackgroundResource(R.drawable.bg_btn_remove);
//            holder.followButton.setTextColor(context.getResources().getColor(R.color.text_grey));
//        } else {
//            holder.followButton.setText("Follow");
//            holder.followButton.setBackgroundResource(R.drawable.rounded_blue_background);
//            holder.followButton.setTextColor(context.getResources().getColor(R.color.white));
//        }

        if (personsList.get(position).getFirendStatus().equalsIgnoreCase("Following")) {
            holder.follow.setText(context.getString(R.string.unfollow));
            holder.follow.setTextColor(context.getResources().getColor(R.color.gray));
            holder.follow.setBackgroundResource(R.drawable.bg_btn_remove);
        } else if (personsList.get(position).getFirendStatus().equalsIgnoreCase("Waiting")) {
            holder.follow.setText(context.getString(R.string.Waiting));
            holder.follow.setTextColor(context.getResources().getColor(R.color.gray));
            holder.follow.setBackgroundResource(R.drawable.bg_btn_remove);
        } else if (personsList.get(position).getFirendStatus().equalsIgnoreCase("NoFriend") || personsList.get(position).getFirendStatus().equalsIgnoreCase("follower")) {
            holder.follow.setText(context.getString(R.string.Follow));
            holder.follow.setTextColor(context.getResources().getColor(R.color.white));
            holder.follow.setBackgroundResource(R.drawable.bg_rounded_follow_btn);
        }

        if (model.getDisplayPicture() != null && model.getDisplayPicture().length() > 0) {
            Glide.with(context).load(model.getDisplayPicture()).into(holder.profile_img);
        }

        if (model.getFollowerCount() != null) {
            holder.tvCount.setText(String.valueOf(model.getFollowerCount()));
        }

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getFirendStatus().equalsIgnoreCase("Following") || personsList.get(position).getFirendStatus().equalsIgnoreCase("follower")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
                    ViewGroup viewGroup = view.findViewById(android.R.id.content);
                    final View dView = LayoutInflater.from(context).inflate(R.layout.custom_layout_remove_people_alert, viewGroup, false);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    TextView tvYes = dView.findViewById(R.id.tvYes);
                    ImageView ivPeople = dView.findViewById(R.id.ivPeople);
                    TextView tvNamePeople = dView.findViewById(R.id.tvNamePeople);
                    TextView tvPeopleTitle = dView.findViewById(R.id.tvPeopleTitle);
                    Glide.with(context).load(model.getDisplayPicture()).into(ivPeople);
                    tvNamePeople.setText(model.getFirstName() + " " + model.getLastName());
                    tvPeopleTitle.setText(model.getUserName());

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
                    Glide.with(context).load(model.getDisplayPicture()).into(ivPeople);
                    tvNamePeople.setText(model.getFirstName() + " " + model.getLastName());
                    tvPeopleTitle.setText(model.getUserName());
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


            private void unfollow() {
                Map<String, String> map = new HashMap<>();
                map.put("x-token", xToken);

                HashMap<String, Object> body = new HashMap<>();
                body.put("followingId", model.getId());
                body.put("status", false);

                //image.setVisibility(View.VISIBLE);
                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<UnfollowResponse> call = webApi.followingStatus(map, body);
                call.enqueue(new Callback<UnfollowResponse>() {
                    @Override
                    public void onResponse(Call<UnfollowResponse> call, Response<UnfollowResponse> response) {
                        //  image.setVisibility(View.GONE);
                        if (response.code() == 200 && response.body().getSuccess()) {
                            model.setFirendStatus("NoFriend");
                            personsList.get(position).setFirendStatus("NoFriend");
                            holder.follow.setText(context.getString(R.string.Follow));
                            holder.follow.setTextColor(context.getResources().getColor(R.color.white));
                            holder.follow.setBackgroundResource(R.drawable.bg_rounded_follow_btn);
                        } else {
                            //image.setVisibility(View.GONE);
                            Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<UnfollowResponse> call, Throwable t) {
                        //  image.setVisibility(View.GONE);
                        Log.w("error", t.toString());
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                });
            }

            public void followRequest() {
                Map<String, String> map = new HashMap<>();
                map.put("x-token", xToken);

                HashMap<String, String> body = new HashMap<>();
                body.put("followingId", model.getId());

                //image.setVisibility(View.VISIBLE);
                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<ApiResponse> call = webApi.followingRequest(map, body);
                call.enqueue(new Callback<ApiResponse>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        if (response.code() == 200 && response.body().getSuccess()) {
                            if (isPrivate) {
                                holder.follow.setText(context.getString(R.string.waiting));
                                model.setFirendStatus("Waiting");
                                personsList.get(position).setFirendStatus("Waiting");
                                holder.follow.setTextColor(context.getResources().getColor(R.color.gray));
                                holder.follow.setBackgroundResource(R.drawable.bg_btn_remove);
                            } else {
                                holder.follow.setText(context.getString(R.string.Unfollow));
                                holder.follow.setBackgroundColor(R.color.grey);
                                model.setFirendStatus("Following");
                                personsList.get(position).setFirendStatus("Following");
                                holder.follow.setTextColor(context.getResources().getColor(R.color.gray));
                                holder.follow.setBackgroundResource(R.drawable.bg_btn_remove);
                            }

                        } else {
                            //image.setVisibility(View.GONE);
                            Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        //image.setVisibility(View.GONE);
                        Log.w("error", t.toString());
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
        public TextView name;
        public TextView username;
        private Button followButton;
        TextView follow;
        public TextView tvCount, tvFollowers;
        private RelativeLayout layout_main;

        public peopleViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_img = itemView.findViewById(R.id.img_profile);
            name = itemView.findViewById(R.id.name);
            follow = itemView.findViewById(R.id.follow);
            username = itemView.findViewById(R.id.name2);
            followButton = itemView.findViewById(R.id.btn_remove);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvFollowers = itemView.findViewById(R.id.tvFollowers);
            layout_main = itemView.findViewById(R.id.layout_main);
        }
    }

    public void setOnItemClickListenerUnFollow(final TrendPeopleAdapter.OnItemClickListenerUnFollow mOnItemClickListenerUnFollow) {
        this.mItemClickListener = mOnItemClickListenerUnFollow;
    }

    public interface OnItemClickListenerUnFollow {
        void onItemClick(int position);
    }
}
