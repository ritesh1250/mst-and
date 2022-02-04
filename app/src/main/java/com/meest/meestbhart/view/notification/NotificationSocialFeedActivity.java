package com.meest.meestbhart.view.notification;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.meest.Activities.LikeDetailsActivity;

import com.meest.Adapters.FeedDetailsPagerAdapter;
import com.meest.Adapters.TagPersonAdaptor;
import com.meest.Adapters.ViewPagerAdapterForPost;
import com.meest.Fragments.ExoRecyclerViewFragment;
import com.meest.Insights.InsightsTabActivity;
import com.meest.MainActivity;
import com.meest.Paramaters.InsertLikeParameters;
import com.meest.R;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.responses.DeleteStoryResponse;
import com.meest.responses.FeedResponse;
import com.meest.responses.InsertPostLikeResponse;
import com.meest.responses.PostByIdResponse;
import com.meest.responses.SinglePostInsightResponse;
import com.meest.responses.UserFeedbackResponse;
import com.meest.responses.VideoCommentResponse;
import com.meest.social.socialViewModel.view.comment.VideoCommentActivityNew;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.svs.adapters.ReportAdapter;
//import com.meest.svs.models.ReportTypeResponse;
import com.meest.social.socialViewModel.model.ReportTypeResponse;
import com.meest.utils.goLiveUtils.CommonUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class NotificationSocialFeedActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    //    ToolTipWindow tipWindow;
    TextView txt_usernsme;
    TextView txt_time;
    TextView txt_comment;
    ImageView img_like;
    TextView txt_like_users;
    ViewPager viewPager;
    ProgressBar progressBar;
    ImageView img_save, img_back_arrow_Photo, iv_tag;
    PostByIdResponse postByIdResponse;
    TextView Insights_view;
    ImageView profile1, profile2, profile3, profile4;
    TextView name1, name2, name3, name4;
    EmojiconTextView comment1, comment2, comment3, comment4;
    TextView time1, time2, time3, time4;
    LottieAnimationView loading;
    LinearLayout view_more, layout_comment, layout_share;
    TextView user_name;
    private TextView likesCountTV, commentCountTV, shareCountTV, saveCountTV, totalFollowerCountTV, discovery, followingPercentTV, nameTV;
    VideoCommentResponse videoCommentResponse;
    RelativeLayout layout1, layout2, layout3, layout4;
    private FeedDetailsPagerAdapter viewPagerAdapter;
    private ViewPagerAdapterForPost viewPagerAdapterForPost;
    LinearLayout layout_like_list;
    private ImageView img_profile;
    private RelativeLayout rel_profile_layout;
    String postId, xToken, userId, postUrl;
    public static boolean volume = true;
    boolean isPrivate = false, isSaved = true,firstvideo=true;
    ImageView img_menu;
    LinearLayout pager_dots;
    SinglePostInsightResponse.Data singlePostInsightResponse;
    LottieAnimationView progress;
    CardView parent;
    TextView txt_like_txt;
    EmojiconTextView txt_data;
    ExoPlayer player;
    RelativeLayout rel_no_Data_found, rel_main;
    private int COMMENT_OPEN_CODE = 32;
    protected int mPreviousPos = 0;

    // new Changes in Home UI dated - 8/april /2021
    RelativeLayout likeUser1, likeUser2, likeUser3;
    ImageView ivLikeUser1, ivLikeUser2, ivLikeUser3;
    LinearLayout layout_like, layout_likes_details;
    TextView txt_like, txt_recent_user_name, text_others_like, and_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_social_feed);
        findIds();
        loading.setAnimation("loading.json");
        loading.playAnimation();
        loading.loop(true);
        progress.setAnimation("loading.json");
        progress.playAnimation();
        progress.loop(true);
//        tipWindow = new ToolTipWindow(this);
        img_back_arrow_Photo = findViewById(R.id.img_back_arrow_Photo);
        img_back_arrow_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent().getExtras() == null) {
            Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            finish();
        }

        postId = getIntent().getExtras().getString("postId");
        userId = getIntent().getExtras().getString("userId");
        xToken = SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN);

        Log.e("alhaj11", xToken);
        Insights_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInsightDialog();
            }
        });

        rel_profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(NotificationSocialFeedActivity.this, OtherUserAccount.class);
                intent.putExtra("userId", postByIdResponse.getData().getUserId());
                startActivity(intent);
            }
        });

        layout_like_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationSocialFeedActivity.this, LikeDetailsActivity.class);
                intent.putExtra("postId", postId);
                startActivity(intent);
            }
        });

        layout_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationSocialFeedActivity.this, VideoCommentActivityNew.class);
                intent.putExtra("videoId", postId);
                intent.putExtra("msg", "");
                intent.putExtra("position", 0);
                intent.putExtra("commentCount", postByIdResponse.getData().getCommentCounts());
                intent.putExtra("svs", false);
                intent.putExtra("isCommentAllowed", postByIdResponse.getData().getAllowComment());
                startActivityForResult(intent, COMMENT_OPEN_CODE);
            }
        });

        layout_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final String appPackageName = getPackageName();
//
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Checkout_this_awesome_app));
//                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
//                startActivity(Intent.createChooser(intent, "Share link"));
                sharePost();
            }
        });




        layout_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (!tipWindow.isTooltipShown()) {
//                    tipWindow.showToolTip(view);
//                }
                Map<String, String> map = new HashMap<>();
                map.put("x-token", SharedPrefreances.getSharedPreferenceString(NotificationSocialFeedActivity.this, SharedPrefreances.AUTH_TOKEN));

                WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);

                InsertLikeParameters insertLikeParameters = new InsertLikeParameters(postId);
                Call<InsertPostLikeResponse> call1 = webApi1.insertLike(map, insertLikeParameters);
                call1.enqueue(new Callback<InsertPostLikeResponse>() {
                    @Override
                    public void onResponse(Call<InsertPostLikeResponse> call, Response<InsertPostLikeResponse> response) {
                        try {
                            if (response.code() == 200 && response.body().getSuccess()) {
                                if (postByIdResponse.getData().getLiked() == 0) {
                                    txt_like_txt.setText(getString(R.string.like));
                                    postByIdResponse.getData().setLikeCounts(response.body().getData().getLikeCount());
                                    postByIdResponse.getData().setLiked(1);
                                    txt_like_users.setText("" + postByIdResponse.getData().getLikeCounts());
                                    img_like.setImageResource(R.drawable.like_diamond_filled);
                                } else {
                                    txt_like_txt.setText(getString(R.string.like));
                                    postByIdResponse.getData().setLikeCounts(response.body().getData().getLikeCount());
                                    postByIdResponse.getData().setLiked(0);
                                    txt_like_users.setText("" + postByIdResponse.getData().getLikeCounts());
                                    img_like.setImageResource(R.drawable.like_diamond);
                                }

                                // changing like counts and data
                                final int li = response.body().getData().getLikeCount();
                                if (li != 0) {
                                    txt_like.setText("" + li);
                                    txt_recent_user_name.setText(" " + response.body().getData().getRecentUsername() + " ");
//                                    txt_recent_user_name.setText(" " + model.getPostLikes().get(0).getUser().getUsername() + " ");
                                    if (li == 1) {
                                        text_others_like.setVisibility(View.GONE);
                                        and_text.setVisibility(View.GONE);
                                    } else {
                                        text_others_like.setVisibility(View.VISIBLE);
                                        and_text.setVisibility(View.VISIBLE);
                                        text_others_like.setText(" " + (li - 1) + " " + getString(R.string.others));
                                    }
                                    layout_likes_details.setVisibility(VISIBLE);
                                } else {
                                    txt_like.setText("0");
                                    layout_likes_details.setVisibility(View.GONE);
                                }


                            } else {
                                Utilss.showToast(NotificationSocialFeedActivity.this, response.body().getSuccess().toString(), R.color.msg_fail);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<InsertPostLikeResponse> call, Throwable t) {
                        Log.w("error", t);
                        Utilss.showToast(NotificationSocialFeedActivity.this, getString(R.string.error_msg), R.color.grey);
                    }
                });
            }
        });

        img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> map = new HashMap<>();
                map.put("Accept", "application/json");
                map.put("Content-Type", "application/json");
                map.put("x-token", SharedPrefreances.getSharedPreferenceString(NotificationSocialFeedActivity.this, SharedPrefreances.AUTH_TOKEN));

                WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);

                HashMap<String, Object> body = new HashMap<>();
                body.put("postId", postId);
                body.put("status", true);

                Call<ApiResponse> call1 = webApi1.savePost(map, body);
                call1.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        try {
                            if (response.code() == 200 && response.body().getSuccess()) {
                                loading.setVisibility(View.GONE);
                                if (postByIdResponse.getData().getPostSaved() == 0) {
                                    postByIdResponse.getData().setPostSaved(1);
                                    isSaved = true;
                                    img_save.setImageResource(R.drawable.ic_bookmark_fill);
                                } else {
                                    postByIdResponse.getData().setPostSaved(0);
                                    isSaved = false;
                                    img_save.setImageResource(R.drawable.ic_bookmark_outline);
                                }
                            } else {
                                Utilss.showToast(NotificationSocialFeedActivity.this, getResources().getString(R.string.error_msg), R.color.grey);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Utilss.showToast(NotificationSocialFeedActivity.this, getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.w("error", t);
                        Utilss.showToast(NotificationSocialFeedActivity.this, getString(R.string.error_msg), R.color.grey);
                    }
                });
            }
        });

        data();
    }

    AlertDialog sendFeedbackAlertDialog = null;

    private void sharePost() {

//        try {
//            final String appPackageName = this.getPackageName();
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setType("text/plain");
//            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Checkout_this_awesome_app));
//            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
//            startActivity(Intent.createChooser(intent, getString(R.string.Share_Post)));

        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = ((AppCompatActivity) this).findViewById(android.R.id.content);
            final View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_reshare_post, viewGroup, false);
            Button okBtn = dialogView.findViewById(R.id.okBtn);
            ImageView ivPostView = dialogView.findViewById(R.id.ivPostView);
            EmojiconEditText etCaptions = dialogView.findViewById(R.id.etCaptions);
            ImageView ivCancel = dialogView.findViewById(R.id.ivCancel);


            ivCancel.setOnClickListener(v -> {
                if (sendFeedbackAlertDialog != null && sendFeedbackAlertDialog.isShowing())
                    sendFeedbackAlertDialog.cancel();
            });

            CommonUtils.loadImage(ivPostView, postUrl, this);

            Log.d("TESSSSSS", "sharePostClicked: " + postUrl);


            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String encodedText = CommonUtils.encodeEmoji(etCaptions.getText().toString().trim());
                    if (encodedText.length() == 0) {
                        etCaptions.setError(getResources().getString(R.string.enter_post_caption));
//                        Utilss.showToast(NotificationSocialFeedActivity.this, getResources().getString(R.string.enter_post_caption), R.color.msg_fail);
                    } else {

                        HashMap<String, String> stringHashMap = new HashMap<>();
                        stringHashMap.put("postId", postId);
                        stringHashMap.put("userId", SharedPrefreances.getSharedPreferenceString(NotificationSocialFeedActivity.this, SharedPrefreances.ID));
                        stringHashMap.put("caption", encodedText);
                        uploadUserFeedback(stringHashMap);
                    }
                }
            });
            builder.setView(dialogView);
            sendFeedbackAlertDialog = builder.create();
            sendFeedbackAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void uploadUserFeedback(HashMap<String, String> stringHashMap) {
        try {
            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
            Map<String, String> header = new HashMap<>();

            header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
            Call<UserFeedbackResponse> call = webApi.reSharePost(header, stringHashMap);

            call.enqueue(new Callback<UserFeedbackResponse>() {
                @Override
                public void onResponse(Call<UserFeedbackResponse> call, Response<UserFeedbackResponse> response) {

                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        Utilss.showToastSuccess(NotificationSocialFeedActivity.this, getResources().getString(R.string.post_shared_wall));
                        try {
                            if (sendFeedbackAlertDialog != null && sendFeedbackAlertDialog.isShowing()) {
                                sendFeedbackAlertDialog.cancel();
                                Intent intent1 = new Intent(NotificationSocialFeedActivity.this, MainActivity.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent1);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserFeedbackResponse> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openDialog(String id1, String id2) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);

        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_post, viewGroup, false);

        TextView txt_delete = dialogView.findViewById(R.id.txt_delete);
        TextView txt_report = dialogView.findViewById(R.id.txt_report);
        TextView txt_copyLink = dialogView.findViewById(R.id.txt_copyLink);
        TextView txt_shareTo = dialogView.findViewById(R.id.txt_shareTo);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Log.e("alhaj", "" + userId + " " + SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.ID));

        if (id1.equalsIgnoreCase(id2)) {
            txt_report.setVisibility(View.GONE);
        } else {
            txt_delete.setVisibility(View.GONE);
        }

        txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO :   check_for_use
                final AlertDialog.Builder builder = new AlertDialog.Builder(NotificationSocialFeedActivity.this, R.style.CustomAlertDialog);
                final View dialogView = LayoutInflater.from(NotificationSocialFeedActivity.this).inflate(R.layout.delete_post_popup, null, false);
                Button btYes, btNo;
                btYes = dialogView.findViewById(R.id.btYes);
                btNo = dialogView.findViewById(R.id.btNo);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                btYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        detetePost();
                    }
                });
                btNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        txt_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final androidx.appcompat.app.AlertDialog.Builder builder = new
                        androidx.appcompat.app.AlertDialog.Builder(NotificationSocialFeedActivity.this, R.style.CustomAlertDialog);
                final View dialogView = LayoutInflater.from(NotificationSocialFeedActivity.this).inflate(R.layout.dialog_svs, null, false);

                builder.setView(dialogView);

                androidx.appcompat.app.AlertDialog alertDialog = builder.create();

                TextView report_option = dialogView.findViewById(R.id.report_option);
                ProgressBar progressBar = dialogView.findViewById(R.id.progress_bar);
                RecyclerView report_recycler = dialogView.findViewById(R.id.report_recycler);

                report_option.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Map<String, String> myHeader = new HashMap<>();
                myHeader.put("Accept", "application/json");
                myHeader.put("Content-Type", "application/json");
                myHeader.put("x-token", SharedPrefreances.getSharedPreferenceString(NotificationSocialFeedActivity.this, SharedPrefreances.AUTH_TOKEN));

                // body
                String type = "Post";
                HashMap<String, Object> body = new HashMap<>();
                body.put("reportType", type);
                body.put("reportedData", false);

                Call<ReportTypeResponse> call = webApi.getReportTypes(myHeader, body);
                call.enqueue(new Callback<ReportTypeResponse>() {
                    @Override
                    public void onResponse(Call<ReportTypeResponse> call, Response<ReportTypeResponse> response) {
                        progressBar.setVisibility(View.GONE);

                        if (response.code() == 200 && response.body().getSuccess()) {
                            ReportAdapter reportAdapter = new ReportAdapter(NotificationSocialFeedActivity.this,
                                    response.body(), alertDialog, progressBar, postId, type);
                            report_recycler.setLayoutManager(new LinearLayoutManager(NotificationSocialFeedActivity.this));
                            report_recycler.setAdapter(reportAdapter);

                            report_recycler.setVisibility(View.VISIBLE);
                        } else {
                            Utilss.showToast(NotificationSocialFeedActivity.this, getString(R.string.error_msg), R.color.grey);
                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportTypeResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);

                        Utilss.showToast(NotificationSocialFeedActivity.this, getString(R.string.error_msg), R.color.grey);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        txt_copyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = NotificationSocialFeedActivity.this.getPackageName();
                ClipboardManager clipboard = (ClipboardManager) NotificationSocialFeedActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", "https://play.google.com/store/apps/details?id=" + appPackageName);
                clipboard.setPrimaryClip(clip);

                alertDialog.dismiss();
                Utilss.showToast(NotificationSocialFeedActivity.this, getString(R.string.Copy_Link), R.color.green);
            }
        });

        txt_shareTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = NotificationSocialFeedActivity.this.getPackageName();

                alertDialog.dismiss();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Checkout_this_awesome_app));
                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
                startActivity(Intent.createChooser(intent, "Share link"));
            }
        });

//        progressDialog.show();
        alertDialog.show();

    }

    private void openInsightDialog() {
        View view = getLayoutInflater().inflate(R.layout.insigt__bottom_dialog, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);

        likesCountTV = view.findViewById(R.id.likesCountTV);
        commentCountTV = view.findViewById(R.id.commentCountTV);
        shareCountTV = view.findViewById(R.id.shareCountTV);
        saveCountTV = view.findViewById(R.id.saveCountTV);
        user_name = view.findViewById(R.id.user_name);
        followingPercentTV = view.findViewById(R.id.followingPercentTV);
        discovery = view.findViewById(R.id.discovery);

        user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (singlePostInsightResponse == null) {

                } else {
                    startActivity(new Intent(NotificationSocialFeedActivity.this, InsightsTabActivity.class).putExtra("id", singlePostInsightResponse.getUserId()));
                    dialog.dismiss();
                }
            }
        });

        getUserPosts();
        dialog.setContentView(view);
        dialog.show();
    }

    private void getUserPosts() {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
        body.put("postId", postId);

        Call<SinglePostInsightResponse> call = webApi.getAllInsightByPostId(header, body);
//        showHideLoader(true);
        call.enqueue(new Callback<SinglePostInsightResponse>() {
            @Override
            public void onResponse(Call<SinglePostInsightResponse> call, Response<SinglePostInsightResponse> response) {


//                showHideLoader(false);
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {

                    singlePostInsightResponse = response.body().getData();
                    txt_like_users.setText("" + response.body().getData().getLikeCounts());
                    likesCountTV.setText(String.valueOf(response.body().getData().getLikeCounts()));

                    commentCountTV.setText(response.body().getData().getCommentCounts() + "");
                    shareCountTV.setText(response.body().getData().getShareCount() + "");
                    saveCountTV.setText(response.body().getData().getPostSaveCounts() + "");
                    // totalFollowerCountTV.setText(response.body().getData().getFollowersCount() + "");
                    user_name.setText(response.body().getData().getUser().getUsername());
                    followingPercentTV.setText(response.body().getData().getFollowrPercent() + "% " + getResources().getString(R.string.following));
                    discovery.setText(String.valueOf(response.body().getData().getPeopleReached()));

                } else if (response.body().getData() == null) {


//                    showHideLoader(false);
                }
            }

            @Override
            public void onFailure(Call<SinglePostInsightResponse> call, Throwable t) {
//                showHideLoader(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
        if (taskList.get(0).numActivities == 1 && taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
            Log.e("Running", "This is last activity in the stack");
            Intent intent = new Intent(NotificationSocialFeedActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        finish();
    }

    private void findIds() {
        txt_like_txt = findViewById(R.id.txt_like_txt);
        rel_profile_layout = findViewById(R.id.rel_profile_layout);
        layout_like_list = findViewById(R.id.layout_like_list);
        pager_dots = findViewById(R.id.pager_dots);
        layout_share = findViewById(R.id.layout_share);
        progress = findViewById(R.id.progress);
        rel_no_Data_found = findViewById(R.id.rel_no_Data_found);
        rel_main = findViewById(R.id.rel_main);

        parent = findViewById(R.id.feedNoticeCardView);

        viewPager = findViewById(R.id.viewPager);
        loading = findViewById(R.id.loading);
        txt_data = findViewById(R.id.txt_data);

        txt_usernsme = findViewById(R.id.txt_usernsme);
        img_profile = findViewById(R.id.img_profile);
        txt_time = findViewById(R.id.txt_time);
        txt_comment = findViewById(R.id.txt_comment);
        img_like = findViewById(R.id.img_like);
        layout_like = findViewById(R.id.layout_like);
        txt_like_users = findViewById(R.id.txt_like_users);
        Insights_view = findViewById(R.id.Insights_view);
        progressBar = findViewById(R.id.progressBar);

        layout1 = findViewById(R.id.layout1);
        layout_comment = findViewById(R.id.layout_comment);
        layout2 = (RelativeLayout) findViewById(R.id.layout2);
        layout3 = (RelativeLayout) findViewById(R.id.layout3);
        layout4 = (RelativeLayout) findViewById(R.id.layout4);

        img_save = (ImageView) findViewById(R.id.img_save);
        profile1 = (ImageView) findViewById(R.id.profile1);
        profile2 = (ImageView) findViewById(R.id.profile2);
        profile3 = (ImageView) findViewById(R.id.profile3);
        profile4 = (ImageView) findViewById(R.id.profile4);

        name1 = (TextView) findViewById(R.id.name1);
        name2 = (TextView) findViewById(R.id.name2);
        name3 = (TextView) findViewById(R.id.name3);
        name4 = (TextView) findViewById(R.id.name4);

        comment1 = findViewById(R.id.comment1);
        comment2 = findViewById(R.id.comment2);
        comment3 = findViewById(R.id.comment3);
        comment4 = findViewById(R.id.comment4);

        time1 = findViewById(R.id.time1);
        time2 = findViewById(R.id.time2);
        time3 = findViewById(R.id.time3);
        time4 = findViewById(R.id.time4);

        view_more = findViewById(R.id.view_more);

        img_menu = findViewById(R.id.img_menu);

        iv_tag = findViewById(R.id.iv_tag);


        likeUser1 = findViewById(R.id.likeUser1);
        likeUser2 = findViewById(R.id.likeUser2);
        likeUser3 = findViewById(R.id.likeUser3);

        ivLikeUser1 = findViewById(R.id.ivLikeUser1);
        ivLikeUser2 = findViewById(R.id.ivLikeUser2);
        ivLikeUser3 = findViewById(R.id.ivLikeUser3);
        text_others_like = findViewById(R.id.text_others_like);
        txt_recent_user_name = findViewById(R.id.txt_recent_user_name);
        txt_like = findViewById(R.id.txt_like_users);
        and_text = findViewById(R.id.and_text);
        layout_like = findViewById(R.id.layout_like);
        layout_likes_details = findViewById(R.id.layout_likes_details);

    }

    private void detetePost() {
        progress.setVisibility(View.VISIBLE);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
        body.put("postId", postId);

        Call<DeleteStoryResponse> call = webApi.deletePostByID(header, body);
        call.enqueue(new Callback<DeleteStoryResponse>() {
            @Override
            public void onResponse(Call<DeleteStoryResponse> call, Response<DeleteStoryResponse> response) {
//                showHideLoader(false);
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {

                    Intent intent = new Intent();
                    setResult(4515, intent);
                    finish();

                } else {
                    Utilss.showToast(NotificationSocialFeedActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<DeleteStoryResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
//                showHideLoader(false);
                Utilss.showToast(NotificationSocialFeedActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    public void data() {
        loading.setVisibility(View.VISIBLE);
        progress.setVisibility(View.VISIBLE);
        parent.setVisibility(View.GONE);
        layout1.setVisibility(GONE);
        layout2.setVisibility(GONE);
        layout3.setVisibility(View.GONE);
        layout4.setVisibility(View.GONE);

        Map<String, String> header = new HashMap<>();
        header.put("x-token", xToken);

        HashMap<String, String> body = new HashMap<>();
        body.put("postId", postId);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<PostByIdResponse> call = webApi.postById(header, body);
        call.enqueue(new Callback<PostByIdResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<PostByIdResponse> call, Response<PostByIdResponse> response) {
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    postByIdResponse = response.body();
                    Log.d("TAG", "onResponse: " + response);

                    if (response.body().getData() == null) {
                        loading.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        layout1.setVisibility(GONE);
                        layout2.setVisibility(GONE);
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.GONE);
                        rel_no_Data_found.setVisibility(VISIBLE);
                        rel_main.setVisibility(GONE);
                        //     Utilss.showToast(getApplicationContext(), getString(R.string.No_Data_Found), R.color.msg_fail);
                        return;
                    }
                    else {
                        showViewPagerPost(postByIdResponse.getData());
                        img_menu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog(response.body().getData().getUserId(), SharedPrefreances.getSharedPreferenceString(NotificationSocialFeedActivity.this, SharedPrefreances.ID));
                            }
                        });
                        fetchComments();
                    }
                } else {
                    loading.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);

                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<PostByIdResponse> call, Throwable t) {
                layout1.setVisibility(GONE);
                layout2.setVisibility(GONE);
                Log.w("error", t.toString());
                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                progress.setVisibility(View.GONE);
            }
        });
    }


    private void showViewPagerPost(PostByIdResponse.Data model) {
        if (model.getPosts().size() > 1) {
            pager_dots.setVisibility(VISIBLE);
            addDotsIndicator(pager_dots, 0, model.getPosts().size());
        }
        if(postByIdResponse.getData().getPosts().get(0).getVideo()==1){
//            ((ExoRecyclerViewFragment) viewPagerAdapter.getItem(0)).imVisibleNow();

        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(firstvideo){
                    firstvideo=false;
                    ((ExoRecyclerViewFragment) viewPagerAdapter.getItem(position)).imVisibleNow();
                }


            }

            @Override
            public void onPageSelected(int position) {

                if (model.getPosts().size() > 1) {
                    pager_dots.setVisibility(VISIBLE);
                    addDotsIndicator(pager_dots, position, model.getPosts().size());
                }
                if(model.getPosts().get(position).getVideo()==1){
                    Log.v("harsh", "onPageSelected == " + position);
                    try {
                        ((ExoRecyclerViewFragment) viewPagerAdapter.getItem(mPreviousPos)).imHiddenNow();
                        ((ExoRecyclerViewFragment) viewPagerAdapter.getItem(position)).imVisibleNow();
                         mPreviousPos = position;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (model.getPosts() != null && model.getPosts().size() > 0) {
            viewPagerAdapterForPost = new ViewPagerAdapterForPost(NotificationSocialFeedActivity.this, model.getPosts(), progressBar);
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    private void addDotsIndicator(LinearLayout dotLayout, int position, int size) {
        TextView[] dots = new TextView[size];
        dotLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(this.getColor(R.color.light_grey));
            dotLayout.addView(dots[i]);
        }
        // adding the selected page indicator effect
        if (dots.length > 0) {
            dots[position].setTextColor(this.getColor(R.color.gray));
        }
    }

    private void fetchComments() {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");

        // creating body
        HashMap<String, String> body = new HashMap<>();
        body.put("postId", postId);

        // calling comments api
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<VideoCommentResponse> call = webApi.getComments(header, body);
        call.enqueue(new Callback<VideoCommentResponse>() {
            @Override
            public void onResponse(Call<VideoCommentResponse> call, Response<VideoCommentResponse> response) {
                loading.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getSuccess()) {
                    videoCommentResponse = response.body();
                    // binding data
                    if (videoCommentResponse == null || videoCommentResponse.getData() == null) {
                        layout1.setVisibility(GONE);
                        layout2.setVisibility(GONE);
                        layout3.setVisibility(View.GONE);
                        layout4.setVisibility(View.GONE);
                        Toast.makeText(NotificationSocialFeedActivity.this, getString(R.string.Data_not_found), Toast.LENGTH_LONG).show();
                    } else {
                        bindData();
                    }
                } else {
                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<VideoCommentResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }




    private void bindData() {
        parent.setVisibility(View.VISIBLE);

        txt_usernsme.setText(postByIdResponse.getData().getUser().getUsername());
        txt_time.setText(postByIdResponse.getData().getCreatedAt());
        txt_like_users.setText("" + postByIdResponse.getData().getLikeCounts());

        if (postByIdResponse.getData().getTags().size() > 0) {
            iv_tag.setVisibility(View.VISIBLE);
            iv_tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = getLayoutInflater().inflate(R.layout.tagged_users_adaptor, null);
                    RecyclerView rv = view.findViewById(R.id.recycler_view);
                    BottomSheetDialog dialog = new BottomSheetDialog(NotificationSocialFeedActivity.this);
                    dialog.setContentView(view);
                    dialog.show();
                    TagPersonAdaptor tagPersonAdaptor = new TagPersonAdaptor(postByIdResponse.getData().getTags());
                    rv.setAdapter(tagPersonAdaptor);
                    tagPersonAdaptor.notifyDataSetChanged();
                }
            });
        }
        //  txt_data.setText(postByIdResponse.getData().getCaption());

        String test1 = null;
        try {
            test1 = URLDecoder.decode(postByIdResponse.getData().getCaption(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        txt_data.setText(test1);

        if (postByIdResponse.getData().getCommentCounts() < 1) {
            txt_comment.setText(postByIdResponse.getData().getCommentCounts() + " " + getResources().getString(R.string.comment));
        } else {
            txt_comment.setText(postByIdResponse.getData().getCommentCounts() + " " + getResources().getString(R.string.comment));
        }
        if (postByIdResponse.getData().getLiked() == 1) {
            img_like.setImageResource(R.drawable.like_diamond_filled);
        } else {
            img_like.setImageResource(R.drawable.like_diamond);
        }

        if (postByIdResponse.getData().getLikeCounts() != 0) {
            txt_like.setText("" + postByIdResponse.getData().getLikeCounts());
            txt_recent_user_name.setText(" " + postByIdResponse.getData().getPostLikes().get(0).getUser().getUsername() + " ");
            if (postByIdResponse.getData().getLikeCounts() == 1) {
                text_others_like.setVisibility(GONE);
                and_text.setVisibility(GONE);
            } else {
                text_others_like.setText(" " + (postByIdResponse.getData().getLikeCounts() - 1) + " others");
            }
            layout_likes_details.setVisibility(VISIBLE);
        } else {

            txt_like.setText("");
            layout_likes_details.setVisibility(GONE);
        }

        if (postByIdResponse.getData().getPostLikes() != null && postByIdResponse.getData().getPostLikes().size() > 0) {
            if (postByIdResponse.getData().getPostLikes().size() == 1) {
                likeUser1.setVisibility(VISIBLE);
                likeUser2.setVisibility(GONE);
                likeUser3.setVisibility(GONE);
                Log.d("onBindViewHolder:", " " + postByIdResponse.getData().getPostLikes().size());
                CommonUtils.loadImage(ivLikeUser1, postByIdResponse.getData().getPostLikes().get(0).getUser().getDisplayPicture(), NotificationSocialFeedActivity.this);


            } else if (postByIdResponse.getData().getPostLikes().size() == 2) {
                likeUser1.setVisibility(VISIBLE);
                likeUser2.setVisibility(VISIBLE);
                likeUser3.setVisibility(GONE);
                CommonUtils.loadImage(ivLikeUser1, postByIdResponse.getData().getPostLikes().get(0).getUser().getDisplayPicture(), NotificationSocialFeedActivity.this);
                CommonUtils.loadImage(ivLikeUser2, postByIdResponse.getData().getPostLikes().get(1).getUser().getDisplayPicture(), NotificationSocialFeedActivity.this);
                Log.d("onBindViewHolder:", " " + postByIdResponse.getData().getPostLikes().size());

            } else if (postByIdResponse.getData().getPostLikes().size() == 3) {
                likeUser1.setVisibility(VISIBLE);
                likeUser2.setVisibility(VISIBLE);
                likeUser3.setVisibility(VISIBLE);
                CommonUtils.loadImage(ivLikeUser1, postByIdResponse.getData().getPostLikes().get(0).getUser().getDisplayPicture(), NotificationSocialFeedActivity.this);
                CommonUtils.loadImage(ivLikeUser2, postByIdResponse.getData().getPostLikes().get(1).getUser().getDisplayPicture(), NotificationSocialFeedActivity.this);
                CommonUtils.loadImage(ivLikeUser3, postByIdResponse.getData().getPostLikes().get(2).getUser().getDisplayPicture(), NotificationSocialFeedActivity.this);
                Log.d("onBindViewHolder:", " " + postByIdResponse.getData().getPostLikes().size());

            } else {
                likeUser1.setVisibility(VISIBLE);
                likeUser2.setVisibility(VISIBLE);
                likeUser3.setVisibility(VISIBLE);
                CommonUtils.loadImage(ivLikeUser1, postByIdResponse.getData().getPostLikes().get(0).getUser().getDisplayPicture(), NotificationSocialFeedActivity.this);
                CommonUtils.loadImage(ivLikeUser2, postByIdResponse.getData().getPostLikes().get(1).getUser().getDisplayPicture(), NotificationSocialFeedActivity.this);
                CommonUtils.loadImage(ivLikeUser3, postByIdResponse.getData().getPostLikes().get(2).getUser().getDisplayPicture(), NotificationSocialFeedActivity.this);
                Log.d("onBindViewHolder:", " " + postByIdResponse.getData().getPostLikes().size());
            }

        } else {
            likeUser1.setVisibility(GONE);
            likeUser2.setVisibility(GONE);
            likeUser3.setVisibility(GONE);

        }

        if (postByIdResponse.getData().getPostSaved() == 0) {

            img_save.setImageResource(R.drawable.ic_bookmark_outline);
        } else {
            img_save.setImageResource(R.drawable.ic_bookmark_fill);
        }

        if (postByIdResponse.getData().getPosts() != null && postByIdResponse.getData().getPosts().size() > 0) {
            for (int i = 0; i < postByIdResponse.getData().getPosts().size(); i++) {
                postUrl = String.valueOf(postByIdResponse.getData().getPosts().get(i).getPost());
            }
            List<ExoRecyclerViewFragment> exoRecyclerViewFragments = new ArrayList<>();
            if (postByIdResponse.getData().getPosts() != null && postByIdResponse.getData().getPosts().size() > 0) {
                for (FeedResponse.Post postInner : postByIdResponse.getData().getPosts()) {
                    String data = new Gson().toJson(postInner);
                    exoRecyclerViewFragments.add(ExoRecyclerViewFragment.newInstance(data));
                }
            }
            viewPagerAdapter = new FeedDetailsPagerAdapter(getSupportFragmentManager(), exoRecyclerViewFragments);
            viewPager.setCurrentItem(mPreviousPos, false);
            viewPager.setAdapter(viewPagerAdapter);

        }


        isPrivate = postByIdResponse.getData().getUser().getAccountType().equalsIgnoreCase("private");

        if (postByIdResponse.getData().getUser().getDisplayPicture() != null && !postByIdResponse.getData().getUser().getDisplayPicture().equals("")) {

            Glide.with(getApplicationContext()).
                    load(postByIdResponse.getData().getUser().getDisplayPicture()).
                    thumbnail(0.1f).
                    into(img_profile);
        }

        if (videoCommentResponse.getData().getRows().size() == 0) {
            layout1.setVisibility(GONE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.GONE);
            layout4.setVisibility(View.GONE);
        }
//        if (postByIdResponse.getData().getCommentCounts() == 1) {
        else if (videoCommentResponse.getData().getRows().size() == 1) {
            try {
                CommonUtils.loadImage(profile1, videoCommentResponse.getData().getRows().get(0).getUser().getDisplayPicture(), getApplicationContext());
//              Glide.with(this).load(videoCommentResponse.getData().getRows().get(0).getUser().getDisplayPicture()).into(profile1);

                name1.setText(videoCommentResponse.getData().getRows().get(0).getUser().getUsername());
                //comment1.setText(videoCommentResponse.getData().getRows().get(0).getComment());
                String c1 = videoCommentResponse.getData().getRows().get(0).getComment();
                String ct1 = c1.replace("+", " ");
                try {
                    String test = URLDecoder.decode(ct1, "UTF-8");
                    comment1.setText(test);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                time1.setText(videoCommentResponse.getData().getRows().get(0).getCreatedAt());
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                layout3.setVisibility(View.GONE);
                layout4.setVisibility(View.GONE);
            } catch (Exception e) {
                Log.e("Error",e.getMessage());
            }

//        } else if (postByIdResponse.getData().getCommentCounts() == 2) {
        } else if (videoCommentResponse.getData().getRows().size() == 2) {
            try {
                CommonUtils.loadImage(profile1, videoCommentResponse.getData().getRows().get(0).getUser().getDisplayPicture(), getApplicationContext());

//            Glide.with(this).load(videoCommentResponse.getData().getRows().get(0).getUser().getDisplayPicture()).into(profile1);
                name1.setText(videoCommentResponse.getData().getRows().get(0).getUser().getUsername());
                comment1.setText(videoCommentResponse.getData().getRows().get(0).getComment());


                time1.setText(videoCommentResponse.getData().getRows().get(0).getCreatedAt());
//            String c1 = videoCommentResponse.getData().getRows().get(0).getComment();
//            String ct1 = c1.replace("+", " ");
//            comment2.setText(ct1);

                CommonUtils.loadImage(profile2, videoCommentResponse.getData().getRows().get(1).getUser().getDisplayPicture(), getApplicationContext());

//            Glide.with(this).load(videoCommentResponse.getData().getRows().get(1).getUser().getDisplayPicture()).into(profile2);
                name2.setText(videoCommentResponse.getData().getRows().get(1).getUser().getUsername());
                // comment2.setText(videoCommentResponse.getData().getRows().get(1).getComment());
                time2.setText(videoCommentResponse.getData().getRows().get(1).getCreatedAt());
//            String c2 = videoCommentResponse.getData().getRows().get(1).getComment();
//            String ct2 = c2.replace("+", " ");
//            comment2.setText(ct2);


                String com2 = URLDecoder.decode(videoCommentResponse.getData().getRows().get(1).getComment(), "UTF-8");
                comment2.setText(com2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.GONE);
            layout4.setVisibility(View.GONE);

//        } else if (postByIdResponse.getData().getCommentCounts() == 3) {
        } else if (videoCommentResponse.getData().getRows().size() == 3) {
            CommonUtils.loadImage(profile1, videoCommentResponse.getData().getRows().get(0).getUser().getDisplayPicture(), getApplicationContext());

//            Glide.with(this).load(videoCommentResponse.getData().getRows().get(0).getUser().getDisplayPicture()).into(profile1);
            name1.setText(videoCommentResponse.getData().getRows().get(0).getUser().getUsername());
            //comment1.setText(videoCommentResponse.getData().getRows().get(0).getComment());
            time1.setText(videoCommentResponse.getData().getRows().get(0).getCreatedAt());
            //   String c1 = videoCommentResponse.getData().getRows().get(0).getComment();
            // String ct1 = c1.replace("+", " ");
            //  comment1.setText(ct1);

            //////////////
            try {
                String com1 = URLDecoder.decode(videoCommentResponse.getData().getRows().get(0).getComment(), "UTF-8");
                comment1.setText(com1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            ///////////////////
            CommonUtils.loadImage(profile2, videoCommentResponse.getData().getRows().get(1).getUser().getDisplayPicture(), getApplicationContext());

//            Glide.with(this).load(videoCommentResponse.getData().getRows().get(1).getUser().getDisplayPicture()).into(profile2);
            name2.setText(videoCommentResponse.getData().getRows().get(1).getUser().getUsername());
            // comment2.setText(videoCommentResponse.getData().getRows().get(1).getComment());
            time2.setText(videoCommentResponse.getData().getRows().get(1).getCreatedAt());
//            String c2 = videoCommentResponse.getData().getRows().get(1).getComment();
//            String ct2 = c2.replace("+", " ");
//            comment2.setText(ct2);

            try {
                String com2 = URLDecoder.decode(videoCommentResponse.getData().getRows().get(1).getComment(), "UTF-8");
                comment2.setText(com2);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            CommonUtils.loadImage(profile3, videoCommentResponse.getData().getRows().get(2).getUser().getDisplayPicture(), getApplicationContext());
//          Glide.with(this).load(videoCommentResponse.getData().getRows().get(2).getUser().getDisplayPicture()).into(profile3);
            name3.setText(videoCommentResponse.getData().getRows().get(2).getUser().getUsername());
            //comment3.setText(videoCommentResponse.getData().getRows().get(2).getComment());
            time3.setText(videoCommentResponse.getData().getRows().get(2).getCreatedAt());
//            String c3 = videoCommentResponse.getData().getRows().get(2).getComment();
//            String ct3 = c3.replace("+", " ");
//            comment3.setText(ct3);

            try {
                String com3 = URLDecoder.decode(videoCommentResponse.getData().getRows().get(2).getComment(), "UTF-8");
                comment3.setText(com3);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.VISIBLE);
            layout4.setVisibility(View.GONE);

//        } else if (postByIdResponse.getData().getCommentCounts() == 4) {
        } else if (videoCommentResponse.getData().getRows().size() == 4) {

            CommonUtils.loadImage(profile1, videoCommentResponse.getData().getRows().get(0).getUser().getDisplayPicture(), getApplicationContext());

//            Glide.with(this).load(videoCommentResponse.getData().getRows().get(0).getUser().getDisplayPicture()).into(profile1);
            name1.setText(videoCommentResponse.getData().getRows().get(0).getUser().getUsername());
            //comment1.setText(videoCommentResponse.getData().getRows().get(0).getComment());
            time1.setText(videoCommentResponse.getData().getRows().get(0).getCreatedAt());
//            String c1 = videoCommentResponse.getData().getRows().get(0).getComment();
//            String ct1 = c1.replace("+", " ");
//            comment1.setText(ct1);

            try {
                String com1 = URLDecoder.decode(videoCommentResponse.getData().getRows().get(0).getComment(), "UTF-8");
                comment1.setText(com1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            CommonUtils.loadImage(profile2, videoCommentResponse.getData().getRows().get(1).getUser().getDisplayPicture(), getApplicationContext());

//          Glide.with(this).load(videoCommentResponse.getData().getRows().get(1).getUser().getDisplayPicture()).into(profile2);
            name2.setText(videoCommentResponse.getData().getRows().get(1).getUser().getUsername());
            //comment2.setText(videoCommentResponse.getData().getRows().get(1).getComment());
            time2.setText(videoCommentResponse.getData().getRows().get(1).getCreatedAt());
//            String c2 = videoCommentResponse.getData().getRows().get(1).getComment();
//            String ct2 = c2.replace("+", " ");
//            comment2.setText(ct2);


            try {
                String com2 = URLDecoder.decode(videoCommentResponse.getData().getRows().get(1).getComment(), "UTF-8");
                comment2.setText(com2);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            CommonUtils.loadImage(profile3, videoCommentResponse.getData().getRows().get(2).getUser().getDisplayPicture(), getApplicationContext());

//          Glide.with(this).load(videoCommentResponse.getData().getRows().get(2).getUser().getDisplayPicture()).into(profile3);
            name3.setText(videoCommentResponse.getData().getRows().get(2).getUser().getUsername());
            // comment3.setText(videoCommentResponse.getData().getRows().get(2).getComment());
            time3.setText(videoCommentResponse.getData().getRows().get(2).getCreatedAt());
//            String c3 = videoCommentResponse.getData().getRows().get(2).getComment();
//            String ct3 = c3.replace("+", " ");
//            comment3.setText(ct3);

            try {
                String com3 = URLDecoder.decode(videoCommentResponse.getData().getRows().get(2).getComment(), "UTF-8");
                comment3.setText(com3);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            CommonUtils.loadImage(profile4, videoCommentResponse.getData().getRows().get(3).getUser().getDisplayPicture(), getApplicationContext());

//          Glide.with(this).load(videoCommentResponse.getData().getRows().get(3).getUser().getDisplayPicture()).into(profile4);
            name4.setText(videoCommentResponse.getData().getRows().get(3).getUser().getUsername());
            //comment4.setText(videoCommentResponse.getData().getRows().get(3).getComment());
            time4.setText(videoCommentResponse.getData().getRows().get(3).getCreatedAt());

//            String c4 = videoCommentResponse.getData().getRows().get(3).getComment();
//            String ct4 = c4.replace("+", " ");
//            comment4.setText(ct4);

            try {
                String com4 = URLDecoder.decode(videoCommentResponse.getData().getRows().get(3).getComment(), "UTF-8");
                comment4.setText(com4);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.VISIBLE);
            layout4.setVisibility(View.VISIBLE);

//        } else if (postByIdResponse.getData().getCommentCounts() >= 4) {
        } else if (videoCommentResponse.getData().getRows().size() >= 4) {

            CommonUtils.loadImage(profile1, videoCommentResponse.getData().getRows().get(0).getUser().getDisplayPicture(), getApplicationContext());

//          Glide.with(this).load(videoCommentResponse.getData().getRows().get(0).getUser().getDisplayPicture()).into(profile1);
            name1.setText(videoCommentResponse.getData().getRows().get(0).getUser().getUsername());
            //comment1.setText(videoCommentResponse.getData().getRows().get(0).getComment());
            time1.setText(videoCommentResponse.getData().getRows().get(0).getCreatedAt());
//          String c1 = videoCommentResponse.getData().getRows().get(0).getComment();
//          String ct1 = c1.replace("+", " ");
//          comment1.setText(ct1);

            try {
                String com1 = URLDecoder.decode(videoCommentResponse.getData().getRows().get(0).getComment(), "UTF-8");
                comment1.setText(com1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            CommonUtils.loadImage(profile2, videoCommentResponse.getData().getRows().get(1).getUser().getDisplayPicture(), getApplicationContext());

//            Glide.with(this).load(videoCommentResponse.getData().getRows().get(1).getUser().getDisplayPicture()).into(profile2);
            name2.setText(videoCommentResponse.getData().getRows().get(1).getUser().getUsername());
            // comment2.setText(videoCommentResponse.getData().getRows().get(1).getComment());
            time2.setText(videoCommentResponse.getData().getRows().get(1).getCreatedAt());
            time2.setText(videoCommentResponse.getData().getRows().get(1).getCreatedAt());
//            String c2 = videoCommentResponse.getData().getRows().get(1).getComment();
//            String ct2 = c2.replace("+", " ");
//            comment2.setText(ct2);

            try {
                String com2 = URLDecoder.decode(videoCommentResponse.getData().getRows().get(1).getComment(), "UTF-8");
                comment2.setText(com2);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            CommonUtils.loadImage(profile3, videoCommentResponse.getData().getRows().get(2).getUser().getDisplayPicture(), getApplicationContext());

//            Glide.with(this).load(videoCommentResponse.getData().getRows().get(2).getUser().getDisplayPicture()).into(profile3);
            name3.setText(videoCommentResponse.getData().getRows().get(2).getUser().getUsername());
            // comment3.setText(videoCommentResponse.getData().getRows().get(2).getComment());
            time3.setText(videoCommentResponse.getData().getRows().get(2).getCreatedAt());
//            String c3 = videoCommentResponse.getData().getRows().get(2).getComment();
//            String ct3 = c3.replace("+", " ");
//            comment3.setText(ct3);

            try {
                String com3 = URLDecoder.decode(videoCommentResponse.getData().getRows().get(2).getComment(), "UTF-8");
                comment3.setText(com3);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            CommonUtils.loadImage(profile4, videoCommentResponse.getData().getRows().get(3).getUser().getDisplayPicture(), getApplicationContext());

//            Glide.with(this).load(videoCommentResponse.getData().getRows().get(3).getUser().getDisplayPicture()).into(profile4);
            name4.setText(videoCommentResponse.getData().getRows().get(3).getUser().getUsername());
            // comment4.setText(videoCommentResponse.getData().getRows().get(3).getComment());
            time4.setText(videoCommentResponse.getData().getRows().get(3).getCreatedAt());
//            String c4 = videoCommentResponse.getData().getRows().get(3).getComment();
//            String ct4 = c4.replace("+", " ");
//            comment4.setText(ct4);

            try {
                String com4 = URLDecoder.decode(videoCommentResponse.getData().getRows().get(3).getComment(), "UTF-8");
                comment4.setText(com4);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.VISIBLE);
            layout4.setVisibility(View.VISIBLE);

            view_more.setVisibility(View.VISIBLE);


            view_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NotificationSocialFeedActivity.this, VideoCommentActivityNew.class);
                    intent.putExtra("videoId", postId);
                    intent.putExtra("svs", false);
                    intent.putExtra("position", 0);
                    intent.putExtra("msg", "");
                    intent.putExtra("isCommentAllowed", true);
                    startActivity(intent);
                }
            });
        } else if (videoCommentResponse.getData().getRows() == null) {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.VISIBLE);
            layout4.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMMENT_OPEN_CODE && data != null && postByIdResponse != null) {
            int commentCount = data.getExtras().getInt("commentCount", 0);
            postByIdResponse.getData().setCommentCounts(commentCount);
            if (postByIdResponse.getData().getCommentCounts() < 1) {
                txt_comment.setText(commentCount + " " + getString(R.string.comment));
            } else {
                txt_comment.setText(commentCount + " " + getString(R.string.comment));

            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (viewPagerAdapter != null) {
//            viewPagerAdapter.releasePlayer();
        }
    }

    @Override
    protected void onDestroy() {


        if (viewPagerAdapter != null) {
//            viewPagerAdapter.releasePlayer();
        }

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        volume = true;
        super.onPause();
    }


    @Override
    protected void onResume() {
        if (viewPagerAdapter != null) {
//            viewPagerAdapter.resumePlayer();
        }
        super.onResume();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}