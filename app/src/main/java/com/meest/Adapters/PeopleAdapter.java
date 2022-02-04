package com.meest.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import com.meest.responses.SearchHomeResponse;
import com.meest.social.socialViewModel.model.UnfollowResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.meestbhart.login.model.ApiResponse;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.peopleViewHolder> {

    ArrayList<SearchHomeResponse.Data.Persons> Peopleslist;
    Activity activity;
    PeopleAdapter.OnItemClickListenerUnFollow mItemClickListener;
    String xToken;

    public PeopleAdapter(ArrayList<SearchHomeResponse.Data.Persons> peopleslist, Activity activity) {
        Peopleslist = peopleslist;
        this.activity = activity;
    }

    @NonNull
    @Override
    public peopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_item_list, parent, false);
        peopleViewHolder PVH = new peopleViewHolder(v);
        return PVH;
    }


    @Override
    public void onBindViewHolder(@NonNull peopleViewHolder holder, int position) {
        SearchHomeResponse.Data.Persons Peoples = Peopleslist.get(position);
        boolean isPrivate = !Peoples.getAccountType().equalsIgnoreCase("public");
        // final boolean[] isFollowing = {Peoples.get.equals("0")};
        xToken = SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.AUTH_TOKEN);

        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);
            }
        });
        Log.e("TAg", "persondata=" + Peoples.getIsFriend());

        Log.e("shaban", "Id" + Peoples.getId());
        Log.e("shaban", "svae Id" + SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.ID));

        if (Peoples.getId().equalsIgnoreCase(SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.ID))) {
            holder.follow.setVisibility(View.GONE);
            holder.btn_remove.setVisibility(View.GONE);
        } else {
            //holder.btn_remove.setVisibility(View.GONE);

        }
        holder.follow.setVisibility(View.VISIBLE);

        holder.name.setText(Peoples.getFirstName() + " " + Peoples.getLastName());
        holder.name2.setText(Peoples.getUsername());
        Glide.with(activity).load(Peoples.getDisplayPicture()).placeholder(R.drawable.placeholder).into(holder.profile_img);

        if (Peoples.getFirendStatus().equalsIgnoreCase("Following")) {
            holder.follow.setText(activity.getString(R.string.unfollow));
            holder.follow.setTextColor(activity.getResources().getColor(R.color.gray));
            holder.follow.setBackgroundResource(R.drawable.bg_btn_remove);
        } else if (Peoples.getFirendStatus().equalsIgnoreCase("Waiting")) {
            holder.follow.setText(activity.getString(R.string.waiting));
            holder.follow.setTextColor(activity.getResources().getColor(R.color.gray));
            holder.follow.setBackgroundResource(R.drawable.bg_btn_remove);
        } else if (Peoples.getFirendStatus().equalsIgnoreCase("NoFriend") || Peoples.getFirendStatus().equalsIgnoreCase("follower")) {
            holder.follow.setText(activity.getString(R.string.follow));
            holder.follow.setTextColor(activity.getResources().getColor(R.color.white));
            holder.follow.setBackgroundResource(R.drawable.rounded_blue_background);
        }


        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Peoples.getFirendStatus().equalsIgnoreCase("Following")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomAlertDialog);
                    ViewGroup viewGroup = view.findViewById(android.R.id.content);
                    final View dView = LayoutInflater.from(activity).inflate(R.layout.custom_layout_remove_people_alert, viewGroup, false);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    TextView tvYes = dView.findViewById(R.id.tvYes);
                    ImageView ivPeople = dView.findViewById(R.id.ivPeople);
                    TextView tvNamePeople = dView.findViewById(R.id.tvNamePeople);
                    TextView tvPeopleTitle = dView.findViewById(R.id.tvPeopleTitle);
                    Glide.with(activity).load(Peoples.getDisplayPicture()).into(ivPeople);
                    tvNamePeople.setText(Peoples.getFirstName() + " " + Peoples.getLastName());
                    tvPeopleTitle.setText(Peoples.getUsername());

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
                } else if (Peoples.getFirendStatus().equalsIgnoreCase("Waiting")) {

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
                    Glide.with(activity).load(Peoples.getDisplayPicture()).into(ivPeople);
                    tvNamePeople.setText(Peoples.getFirstName() + " " + Peoples.getLastName());
                    tvPeopleTitle.setText(Peoples.getUsername());
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

                } else if (Peoples.getFirendStatus().equalsIgnoreCase("noFriend") || Peoples.getFirendStatus().equalsIgnoreCase("follower")) {
                    followRequest();
                }
            }

            // unfollow people Api call here......................................................

            private void unfollow() {
                Map<String, String> map = new HashMap<>();
                map.put("x-token", xToken);

                HashMap<String, Object> body = new HashMap<>();
                body.put("followingId", Peoples.getId());
                body.put("status", false);

                //image.setVisibility(View.VISIBLE);
                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<UnfollowResponse> call = webApi.followingStatus(map, body);
                call.enqueue(new Callback<UnfollowResponse>() {
                    @Override
                    public void onResponse(Call<UnfollowResponse> call, Response<UnfollowResponse> response) {
                        //  image.setVisibility(View.GONE);
                        if (response.code() == 200 && response.body().getSuccess()) {
                            Peoples.setFirendStatus("NoFriend");
                            Peopleslist.get(position).setFirendStatus("NoFriend");
                            holder.follow.setText(activity.getString(R.string.Follow));
                            holder.follow.setTextColor(activity.getResources().getColor(R.color.white));
                            holder.follow.setBackgroundResource(R.drawable.rounded_blue_background);
                        } else {
                            //image.setVisibility(View.GONE);
                            Utilss.showToast(activity, activity.getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<UnfollowResponse> call, Throwable t) {
                        //  image.setVisibility(View.GONE);
                        Log.w("error", t.toString());
                        Utilss.showToast(activity, activity.getString(R.string.error_msg), R.color.grey);
                    }
                });
            }

            public void followRequest() {
                Map<String, String> map = new HashMap<>();
                map.put("x-token", xToken);

                HashMap<String, String> body = new HashMap<>();
                body.put("followingId", Peoples.getId());

                //image.setVisibility(View.VISIBLE);
                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<ApiResponse> call = webApi.followingRequest(map, body);
                call.enqueue(new Callback<ApiResponse>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                        if (response.code() == 200 && response.body().getSuccess()) {
                            if (isPrivate) {
                                holder.follow.setText(activity.getString(R.string.waiting));
                                Peoples.setFirendStatus("Waiting");
                                Peopleslist.get(position).setFirendStatus("Waiting");
                                holder.follow.setTextColor(activity.getResources().getColor(R.color.gray));
                                holder.follow.setBackgroundResource(R.drawable.bg_btn_remove);
                            } else {
                                holder.follow.setText(activity.getString(R.string.unfollow));
                                holder.follow.setBackgroundColor(R.color.grey);
                                Peoples.setFirendStatus("Following");
                                Peopleslist.get(position).setFirendStatus("Following");
                                holder.follow.setTextColor(activity.getResources().getColor(R.color.gray));
                                holder.follow.setBackgroundResource(R.drawable.bg_btn_remove);
                            }

                        } else {
                            //image.setVisibility(View.GONE);
                            Utilss.showToast(activity, activity.getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        //image.setVisibility(View.GONE);
                        Log.w("error", t.toString());
                        Utilss.showToast(activity, activity.getString(R.string.error_msg), R.color.grey);
                    }
                });
            }

        });
    }

    @Override
    public int getItemCount() {
        return Peopleslist == null ? 0 : Peopleslist.size();
    }

    public class peopleViewHolder extends RecyclerView.ViewHolder {

        public de.hdodenhof.circleimageview.CircleImageView profile_img;
        public TextView name;
        public TextView name2;
        public TextView follow;
        private Button btn_remove;
        private RelativeLayout layout_main;

        public peopleViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_img = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.img_profile);
            name = (TextView) itemView.findViewById(R.id.name);
            name2 = (TextView) itemView.findViewById(R.id.name2);
            follow = (TextView) itemView.findViewById(R.id.follow);
            btn_remove = (Button) itemView.findViewById(R.id.btn_remove);
            layout_main = itemView.findViewById(R.id.layout_main);

        }
    }

    public void setOnItemClickListenerUnFollow(final PeopleAdapter.OnItemClickListenerUnFollow mOnItemClickListenerUnFollow) {
        this.mItemClickListener = mOnItemClickListenerUnFollow;
    }

    public interface OnItemClickListenerUnFollow {
        void onItemClick(int position);
    }

}
