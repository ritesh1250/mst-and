package com.meest.meestbhart.view.notification;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meest.Interfaces.NotificationDeleteCallback;
import com.meest.R;
import com.meest.databinding.ItemNotificationNewBinding;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.model.AllNotificationResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.meestbhart.viewModel.NotifcationSocialViewModel;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private List<AllNotificationResponse.Row> notificationList;
    private NotificationDeleteCallback notificationDeleteCallback;


    public NotificationAdapter() {
        notificationList = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_new, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setModel(notificationList.get(position),position);

    }

    public void loadMore(List<AllNotificationResponse.Row> data, NotificationDeleteCallback notificationDeleteCallback) {
//        notifyItemInserted(data.size() - 1);
        this.notificationList.addAll(data);
        notifyDataSetChanged();
        this.notificationDeleteCallback = notificationDeleteCallback;
    }

    public void updateData(List<AllNotificationResponse.Row> list, NotificationDeleteCallback notificationDeleteCallback) {
        this.notificationList.clear();
        this.notificationList.addAll(list);
        notifyDataSetChanged();
        this.notificationDeleteCallback = notificationDeleteCallback;
    }


    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public List<AllNotificationResponse.Row> getDataList() {
        return notificationList;
    }

    public void notifyDataSetChanged(List<AllNotificationResponse.Row> feedNotification) {
        this.notificationList = feedNotification;
        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        ItemNotificationNewBinding binding;

        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            // showPopup(v);
            return false;
        }


        public void setModel(AllNotificationResponse.Row model,int position) {
            String s = "ABC";
            SpannableStringBuilder sb;
            StyleSpan b = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold


            binding.setNotificationModel(model);
            if (model.getNotificationBody() != null) {
                try {
                    switch (model.getNotificationBody().getNotificationType()) {
                        case "NewPost":
                            binding.name.setText(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName()+ " " + binding.getRoot().getContext().getResources().getString(R.string.is_new_post));
                            binding.imgNotificationType.setImageResource(R.drawable.follow_notification);
                            break;
                       case "Follower":
                            binding.name.setText(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName()+ " " + binding.getRoot().getContext().getResources().getString(R.string.is_now_following_you));
                            binding.imgNotificationType.setImageResource(R.drawable.follow_notification);
                            break;
                        case "FollowerRequest":
                            binding.name.setText(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName()+ " " + binding.getRoot().getContext().getResources().getString(R.string.sent_your_a_request));
                            binding.imgNotificationType.setImageResource(R.drawable.follow_notification);
                            break;
                        case "PostLike":
                            binding.name.setText(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName()+ " " + binding.getRoot().getContext().getResources().getString(R.string.like_your_post));
                            binding.imgNotificationType.setImageResource(R.drawable.notification_like);
                            break;
                        case "PostComment":
                            binding.name.setText(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName()+ " " + binding.getRoot().getContext().getResources().getString(R.string.Comment_on_your_post));
                            binding.imgNotificationType.setImageResource(R.drawable.comment_icon);
                            break;
                        case "CommentLike":
                            binding.name.setText(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName()+ " " + binding.getRoot().getContext().getResources().getString(R.string.liked_your_comment));
                            binding.imgNotificationType.setImageResource(R.drawable.notification_like);
                            break;
                        case "CommentofComment":
                            binding.name.setText(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName()+ " " + binding.getRoot().getContext().getResources().getString(R.string.reply_on_your_comment));
                            binding.imgNotificationType.setImageResource(R.drawable.comment_icon);
                            break;
                        case "PostTag":
                            binding.name.setText(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName()+ " " + binding.getRoot().getContext().getResources().getString(R.string.Tagged_you_in_a_post));
                            binding.imgNotificationType.setImageResource(R.drawable.comment_icon);
                            break;
                    }
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }

            }

            binding.rlTop.setOnClickListener(v -> {
                String userId = model.getNotificationBody().getUserId();
                String type = model.getNotificationBody().getType();
                String notificationType = model.getNotificationBody().getNotificationType();
                if (type.equalsIgnoreCase("Feed")) {
                    switch (notificationType) {
                        case "FollowerRequest":
                        {
                            Intent intent = new Intent(binding.getRoot().getContext(), OtherUserAccount.class);// open followers in profile activity
                            intent.putExtra("userId", userId);
                            binding.getRoot().getContext().startActivity(intent);
                            break;
                        }
                        case "Follower": {
                            Intent intent = new Intent(binding.getRoot().getContext(), OtherUserAccount.class);// open followers in profile activity
                            intent.putExtra("userId", userId);
                            binding.getRoot().getContext().startActivity(intent);
                            break;
                        }
                        case "User": {
                            String senderSocketId = model.getNotificationBody().getSenderSocketId();
                            Log.e("senderSocketId++++", senderSocketId);
                            break;
                        }
                        default: {
                            Intent intent = new Intent(binding.getRoot().getContext(), NotificationSocialFeedActivity.class);// open followers in profile activity
                            intent.putExtra("userId", userId);
                            intent.putExtra("postId", model.getNotificationBody().getPostId());
                            binding.getRoot().getContext().startActivity(intent);
                            break;
                        }
                    }
                }
            });

            binding.rlTop.setOnLongClickListener(v1 -> {
                showPopup(v1,position);
                return false;
            });



        }



        public void showPopup(View view, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            ViewGroup viewGroup = view.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_notificatio_popup, viewGroup, false);
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            LinearLayout ll_mute_noti = dialogView.findViewById(R.id.ll_mute_noti);
            LinearLayout ll_delete_noti = dialogView.findViewById(R.id.ll_remove_noti);
            ll_mute_noti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    Toast.makeText(v.getContext(), binding.getRoot().getContext().getString(R.string.mute_notification), Toast.LENGTH_SHORT).show();
                }
            });
            ll_delete_noti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    AllNotificationResponse.Row model = notificationList.get(position);
                    Map<String, String> header = new HashMap<>();
                    header.put("Accept", "application/json");
                    header.put("Content-Type", "application/json");
                    header.put("x-token", SharedPrefreances.getSharedPreferenceString(v.getContext(), SharedPrefreances.AUTH_TOKEN));

                    HashMap<String, String> body = new HashMap<>();
                    body.put("notificationId", model.getId());


                    View conView = LayoutInflater.from(v.getContext()).inflate(R.layout.delete_notification_new_popup, null);
                    Button yes = conView.findViewById(R.id.yes);
                    Button no = conView.findViewById(R.id.no);
                    BottomSheetDialog dia = new BottomSheetDialog(v.getContext());
                    dia.setContentView(conView);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                            Call<ApiResponse> call = webApi.deleteNotification(header, body);
                            call.enqueue(new Callback<ApiResponse>() {
                                @Override
                                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                    dia.dismiss();

                                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {

                                        Utilss.showToast(v.getContext(), binding.getRoot().getContext().getString(R.string.Deleted_Successfully), R.color.green);
                                        notificationDeleteCallback.delete();
                                    } else {
                                        Utilss.showToast(binding.getRoot().getContext(), binding.getRoot().getContext().getString(R.string.error_msg), R.color.grey);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ApiResponse> call, Throwable t) {
                                    Utilss.showToast(binding.getRoot().getContext(), binding.getRoot().getContext().getString(R.string.error_msg), R.color.grey);
                                }
                            });
                        }

                    });

                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dia.dismiss();
                        }
                    });

                    dia.show();


                }
            });

        }

    }

}


