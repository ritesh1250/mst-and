package com.meest.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meest.Interfaces.NotificationDeleteCallback;
import com.meest.R;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.model.AllNotificationResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private final LayoutInflater inflater;
    private List<AllNotificationResponse.Row> notificationList;
    private  NotificationDeleteCallback notificationDeleteCallback;
    Context ctx;
    AllNotificationResponse.Row model;

    public NotificationAdapter(Context ctx, List<AllNotificationResponse.Row> notificationList
                               ) {
        inflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notification_new, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String s = "ABC";
        SpannableStringBuilder sb;
        StyleSpan b = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold

        model = notificationList.get(position);


        Glide.with(ctx).load(model.getUser().getThumbnail()).placeholder(R.drawable.placeholder).into(holder.img_pro);
        //  CommonUtils.loadImage(holder.img_pro,model.getUser().getThumbnail(),ctx);
        holder.time.setText(model.getCreatedAt());
        if (model.getNotificationBody() != null) {
            SpannableStringBuilder str;
            try {
                switch (model.getNotificationBody().getNotificationType()) {
                    case "Follower":
//                      s = model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName(); /*   +" "+ctx.getString(R.string.is_now_following_you);   */


                        sb = new SpannableStringBuilder(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName() + " " + ctx.getString(R.string.is_now_following_you));
                        sb.setSpan(b, 0, model.getNotificationBody().getFirstName().length() + model.getNotificationBody().getLastName().length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        holder.name.setText(sb);


//                        s = model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName(); /*   +" "+ctx.getString(R.string.is_now_following_you);   */
//                        str = new SpannableStringBuilder(s);
//                        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, s.length()-1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        holder.name.setText(str +" "+ ctx.getString(R.string.is_now_following_you));

                        holder.imgNotificationType.setImageResource(R.drawable.follow_notification);
                        break;
                    case "FollowerRequest":
//                        s = "<b>" +model.getNotificationBody().getFirstName()  + " " + model.getNotificationBody().getLastName()+ "</b>"+" "+ctx.getString(R.string.sent_your_a_request);
//
                         sb = new SpannableStringBuilder(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName() + " " + ctx.getString(R.string.sent_your_a_request));
                        sb.setSpan(b, 0, model.getNotificationBody().getFirstName().length() + model.getNotificationBody().getLastName().length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        holder.name.setText(sb);

//                        s = model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName(); /*   +" "+ctx.getString(R.string.is_now_following_you);   */
//                        str = new SpannableStringBuilder(s);
//                        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, s.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                        holder.name.setText(str + " " + ctx.getString(R.string.sent_your_a_request));
                        //  holder.user.setVisibility(View.GONE);
                        holder.imgNotificationType.setImageResource(R.drawable.follow_notification);
                        break;
                    case "PostLike":
//                        s = "<b>" + model.getNotificationBody().getFirstName()  + " " + model.getNotificationBody().getLastName()+ "</b>"+" "+ctx.getString(R.string.like_your_post);
//                        s = model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName(); /*   +" "+ctx.getString(R.string.is_now_following_you);   */
//                        str = new SpannableStringBuilder(s);
//                        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, s.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        holder.name.setText(str + " " + ctx.getString(R.string.like_your_post));

                        sb = new SpannableStringBuilder(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName() + " " + ctx.getString(R.string.like_your_post));
                        sb.setSpan(b, 0, model.getNotificationBody().getFirstName().length() + model.getNotificationBody().getLastName().length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        holder.name.setText(sb);

                        // holder.actionType.setText("Your post have a like ");
                        // holder.user.setText("@" + model.getNotificationBody().getUsername());
                        holder.imgNotificationType.setImageResource(R.drawable.notification_like);
                        break;
                    case "PostComment":
//                        s = "<b>" +model.getNotificationBody().getFirstName()  + " " + model.getNotificationBody().getLastName()+ "</b>"+" "+ctx.getString(R.string.Comment_on_your_post);
//                        s = model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName(); /*   +" "+ctx.getString(R.string.is_now_following_you);   */
//                        str = new SpannableStringBuilder(s);
//                        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, s.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        holder.name.setText(str + " " + ctx.getString(R.string.Comment_on_your_post));

                        sb = new SpannableStringBuilder(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName() + " " + ctx.getString(R.string.Comment_on_your_post));
                        sb.setSpan(b, 0, model.getNotificationBody().getFirstName().length() + model.getNotificationBody().getLastName().length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        holder.name.setText(sb);

                        // holder.actionType.setText(model.getNotificationBody().getComment());
                        // holder.user.setText("@" + model.getNotificationBody().getUsername());
                        holder.imgNotificationType.setImageResource(R.drawable.comment_icon);
                        break;
                    case "CommentLike":
//                        s = "<b>" +model.getNotificationBody().getFirstName()  + " " + model.getNotificationBody().getLastName() + "</b>"+" "+ctx.getString(R.string.like_your_post);
//                        s = model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName();
//                        str = new SpannableStringBuilder(s);
//                        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, s.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        holder.name.setText(str + " " + ctx.getString(R.string.liked_your_comment));

                        sb = new SpannableStringBuilder(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName() + " " + ctx.getString(R.string.liked_your_comment));
                        sb.setSpan(b, 0, model.getNotificationBody().getFirstName().length() + model.getNotificationBody().getLastName().length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        holder.name.setText(sb);

                        // holder.actionType.setText(model.getNotificationBody().getComment());
                        // holder.user.setText("@" + model.getNotificationBody().getUsername());
                        holder.imgNotificationType.setImageResource(R.drawable.notification_like);
                        break;
                    case "CommentofComment":
//                        s = "<b>" +model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName() + "</b>"+" "+ ctx.getString(R.string.reply_on_your_comment);
//                        s = model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName();
//                        str = new SpannableStringBuilder(s);
//                        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, s.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        holder.name.setText(str + " " + ctx.getString(R.string.reply_on_your_comment));

                        sb = new SpannableStringBuilder(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName() + " " + ctx.getString(R.string.reply_on_your_comment));
                        sb.setSpan(b, 0, model.getNotificationBody().getFirstName().length() + model.getNotificationBody().getLastName().length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        holder.name.setText(sb);

                        //   holder.actionType.setText(model.getNotificationBody().getComment());
                        // holder.user.setText("@" + model.getNotificationBody().getUsername());
                        holder.imgNotificationType.setImageResource(R.drawable.comment_icon);
                        break;
                    case "PostTag":
//                        s = "<b>" +model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName() + "</b>"+" "+ ctx.getString(R.string.reply_on_your_comment);
//                        s = model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName();
//                        str = new SpannableStringBuilder(s);
//                        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, s.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        holder.name.setText(str + " " + ctx.getString(R.string.reply_on_your_comment));

                        sb = new SpannableStringBuilder(model.getNotificationBody().getFirstName() + " " + model.getNotificationBody().getLastName() + " " + ctx.getString(R.string.Tagged_you_in_a_post));
                        sb.setSpan(b, 0, model.getNotificationBody().getFirstName().length() + model.getNotificationBody().getLastName().length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                        holder.name.setText(sb);

                        //   holder.actionType.setText(model.getNotificationBody().getComment());
                        // holder.user.setText("@" + model.getNotificationBody().getUsername());
                        holder.imgNotificationType.setImageResource(R.drawable.comment_icon);
                        break;
                }
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }

        String post_id = model.getId().toString();

//        holder.option.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//              /*  View view1 = LayoutInflater.from(ctx).inflate(R.layout.bottom_comment_action, null);
//                BottomSheetDialog dialog = new BottomSheetDialog(ctx);
//                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, 700);
//
//                TextView remove_notification;
//                remove_notification = (TextView) view1.findViewById(R.id.remove_notification);
//
//                remove_notification.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        Map<String, String> header = new HashMap<>();
//                        header.put("Accept", "application/json");
//                        header.put("Content-Type", "application/json");
//                        header.put("x-token", SharedPrefreances.getSharedPreferenceString(ctx, SharedPrefreances.AUTH_TOKEN));
//
//                        HashMap<String, String> body = new HashMap<>();
//                        body.put("notificationId", model.getId());
//
//
//                        View conView = LayoutInflater.from(ctx).inflate(R.layout.delete_notification_new_popup, null);
//                        Button yes = conView.findViewById(R.id.yes);
//                        Button no = conView.findViewById(R.id.no);
//                        BottomSheetDialog dia = new BottomSheetDialog(ctx);
//                        dia.setContentView(conView);
//                        yes.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
//                                Call<ApiResponse> call = webApi.deleteNotification(header, body);
//                                call.enqueue(new Callback<ApiResponse>() {
//                                    @Override
//                                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
//                                        dia.dismiss();
//
//                                        if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//
//                                            Utilss.showToast(ctx, "Deleted Successfully", R.color.green);
//                                            notificationDeleteCallback.delete();
//                                        } else {
//                                            Utilss.showToast(ctx, getString(R.string.error_msg), R.color.grey);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<ApiResponse> call, Throwable t) {
//                                        //loading.setVisibility(View.GONE);
//                                        Log.w("error", t.toString());
//                                        Utilss.showToast(ctx, getString(R.string.error_msg), R.color.grey);
//                                    }
//                                });
//                            }
//
//                        });
//
//                        no.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                dia.dismiss();
//                            }
//                        });
//
//                        dia.show();
//
//
//                        dialog.dismiss();
//                    }
//                });
//                dialog.setContentView(view1);
//                dialog.show();*/
//
//            }
//        });


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    model = notificationList.get(position);
                    String userId = model.getNotificationBody().getUserId();
                    String type = model.getNotificationBody().getType();
                    String notificationType = model.getNotificationBody().getNotificationType();
                    if (type.equalsIgnoreCase("Feed")) {
                        switch (notificationType) {
                            case "Follower": {
                                Intent intent = new Intent(ctx, OtherUserAccount.class);// open followers in profile activity
                                intent.putExtra("userId", userId);
                                ctx.startActivity(intent);
                                break;
                            }
//                            case "PostLike": {
//                                Intent intent = new Intent(ctx, NotificationSocialFeedActivity.class);// open followers in profile activity
//
//                                intent.putExtra("userId", userId);
//                                intent.putExtra("postId", model.getNotificationBody().getPostId());
//                                ctx.startActivity(intent);
//                                break;
//                            }
//                            case "PostComment": {
//
//                            }
//                            case "CommentLike": {
//                                Intent intent = new Intent(ctx, NotificationSocialFeedActivity.class);// open followers in profile activity
//
//                                intent.putExtra("userId", userId);
//                                intent.putExtra("postId", model.getNotificationBody().getPostId());
//                                ctx.startActivity(intent);
//                                break;
//                            }
//                            case "CommentofComment": {
//                                Intent intent = new Intent(ctx, NotificationSocialFeedActivity.class);// open followers in profile activity
//
//                                intent.putExtra("userId", userId);
//                                intent.putExtra("postId", model.getNotificationBody().getPostId());
//                                ctx.startActivity(intent);
//                                break;
//                            }
                            case "User": {
                                String senderSocketId = model.getNotificationBody().getSenderSocketId();
                                Log.e("senderSocketId++++", senderSocketId);
                            /*    Intent intent = new Intent(ctx, ChatActivity.class);// open followers in profile activity

                                intent.putExtra("user_Id", userId);
                                intent.putExtra("user_id_socket", senderSocketId);
//                       intent.putExtra("senderSocketId", model.getNotificationBody().getPostId());
                                ctx.startActivity(intent);*/
                                break;
                            }
                            default: {
                                Intent intent = new Intent(ctx, NotificationSocialFeedActivity.class);// open followers in profile activity

                                intent.putExtra("userId", userId);
                                intent.putExtra("postId", model.getNotificationBody().getPostId());
                                ctx.startActivity(intent);
                                break;
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup(v, position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void notifyDataSetChanged(List<AllNotificationResponse.Row> feedNotification) {
        this.notificationList = feedNotification;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        TextView name;
        TextView name2;
        //  TextView actionType;
        TextView time;
        // TextView user;
        ImageView img_pro;
        // ImageView option;
        LinearLayout relativeLayout;
        ImageView imgNotificationType;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            name2 = (TextView) itemView.findViewById(R.id.name2);
//            user = (TextView) itemView.findViewById(R.id.user);
//            actionType = (TextView) itemView.findViewById(R.id.actionType);
            time = (TextView) itemView.findViewById(R.id.time);
            img_pro = (ImageView) itemView.findViewById(R.id.img_pro);
            // option = (ImageView) itemView.findViewById(R.id.option);
            relativeLayout = (LinearLayout) itemView.findViewById(R.id.rl_top);
            imgNotificationType = (ImageView) itemView.findViewById(R.id.img_notification_type);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            // showPopup(v);
            return false;
        }


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
                Toast.makeText(v.getContext(), ctx.getString(R.string.mute_notification), Toast.LENGTH_SHORT).show();
            }
        });
        ll_delete_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                model = notificationList.get(position);
                Map<String, String> header = new HashMap<>();
                header.put("Accept", "application/json");
                header.put("Content-Type", "application/json");
                header.put("x-token", SharedPrefreances.getSharedPreferenceString(ctx, SharedPrefreances.AUTH_TOKEN));

                HashMap<String, String> body = new HashMap<>();
                body.put("notificationId", model.getId());


                View conView = LayoutInflater.from(ctx).inflate(R.layout.delete_notification_new_popup, null);
                Button yes = conView.findViewById(R.id.yes);
                Button no = conView.findViewById(R.id.no);
                BottomSheetDialog dia = new BottomSheetDialog(ctx);
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

                                    Utilss.showToast(ctx, ctx.getString(R.string.Deleted_Successfully), R.color.green);
                                    notificationDeleteCallback.delete();
                                } else {
                                    Utilss.showToast(ctx, ctx.getString(R.string.error_msg), R.color.grey);
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                //loading.setVisibility(View.GONE);
                                Log.w("error", t.toString());
                                Utilss.showToast(ctx, ctx.getString(R.string.error_msg), R.color.grey);
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