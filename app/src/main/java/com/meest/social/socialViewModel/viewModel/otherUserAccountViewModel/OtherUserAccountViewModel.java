package com.meest.social.socialViewModel.viewModel.otherUserAccountViewModel;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meest.Activities.DiscoverPeopleActivity;
import com.meest.Activities.base.ApiCallActivity;
import com.meest.MainActivity;
import com.meest.Meeast;
import com.meest.R;
import com.meest.databinding.DialogPostBinding;
import com.meest.databinding.OtherUserAccountModelBinding;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.metme.ChatBoatActivity;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.social.socialViewModel.adapter.ReportAdapter;
import com.meest.social.socialViewModel.model.BlockUserResponse;
import com.meest.social.socialViewModel.model.CheckChatHeadNFollowResponse1;
import com.meest.social.socialViewModel.model.DataUser1;
import com.meest.social.socialViewModel.model.MessageResponse;
import com.meest.social.socialViewModel.model.RemoveBlockedUserResponse;
import com.meest.social.socialViewModel.model.ReportTypeResponse;
import com.meest.social.socialViewModel.model.SuggestionResponse;
import com.meest.social.socialViewModel.model.UnfollowResponse;
import com.meest.social.socialViewModel.model.UserProfileRespone1;
import com.meest.svs.activities.FollowListActivity;
import com.meest.utils.Helper;
import com.meest.utils.goLiveUtils.CommonUtils;
import com.meest.videomvvmmodule.utils.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class OtherUserAccountViewModel extends ViewModel {
    private static final String TAG = "OtherUserAccountViewMod";
    public Socket mSocket;
    public ApiCallActivity context;
    public Activity activity;
    public OtherUserAccountModelBinding binding;
    public static DataUser1 otherUserData;
    public ArrayList<SuggestionResponse.Datum> arrayList = new ArrayList<>();
    public int status_post = 0;
    public ArrayList<UserProfileRespone1> user_data = new ArrayList<>(), arrayList_user_data = new ArrayList<>();
    public String blockId, friendStatus = "", followStatus, accountType, profileUrl, first_name = "", last_name = "", user_name = "", userId, xToken;
    public int blockStatus, blockByMe;
    public UserProfileRespone1 response;
    public boolean isMessageButtonClicked, isPrivate = false;
    public MutableLiveData<String> blockText = new MutableLiveData();
    public CompositeDisposable disposable = new CompositeDisposable();

    public OtherUserAccountViewModel(ApiCallActivity context, Activity activity, OtherUserAccountModelBinding binding) {
        this.activity = activity;
        this.binding = binding;
        this.context = context;
        this.xToken = SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN);
    }

    public void initSocket() {
        Meeast app = (Meeast) activity.getApplication();
        mSocket = app.getSocket();
        mSocket.on("connected", onConnect);
        mSocket.on("session", session);
        mSocket.connect();
    }

    public Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID));
                    jsonObject.put("name", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.F_NAME) + " " + SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.L_NAME));
                    jsonObject.put("isGroup", false);
                    mSocket.emit("createSession", jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    };

    public Emitter.Listener session = args -> activity.runOnUiThread(() -> {
        JSONObject data = (JSONObject) args[0];
        String username;
        try {
            username = data.getString("msg");
        } catch (JSONException e) {
            return;
        }
    });

    public void BackPressed() {
        ActivityManager mngr = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
        if (taskList.get(0).numActivities == 1 &&
                taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            activity.finish();
        } else {
            int position = activity.getIntent().getIntExtra("position", -1);
            Intent intent = new Intent();
            intent.putExtra("friendStatus", friendStatus);
            intent.putExtra("position", position);
            activity.setResult(111, intent);
            activity.finish();
        }
    }

    public void openDialog(String id1, String id2) {
        DialogPostBinding dialogPostBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_post, null, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
//        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
//        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_post, viewGroup, false);
//        TextView txt_block = dialogView.findViewById(R.id.txt_delete);
//        TextView txt_report = dialogView.findViewById(R.id.txt_report);
//        TextView txt_copyLink = dialogView.findViewById(R.id.txt_copyLink);
//        TextView txt_shareTo = dialogView.findViewById(R.id.txt_shareTo);
        builder.setView(dialogPostBinding.getRoot());
        final AlertDialog alertDialog = builder.create();
        if (id1.equalsIgnoreCase(id2)) {
            dialogPostBinding.txtReport.setVisibility(View.GONE);
        }
        if (blockByMe == 0) {
            dialogPostBinding.txtDelete.setText(context.getResources().getString(R.string.block));
            dialogPostBinding.txtDelete.setOnClickListener((View.OnClickListener) v -> {
                View conView = activity.getLayoutInflater().inflate(R.layout.block_user_dialog, null);
                Button btnConfirm = conView.findViewById(R.id.btnConfirm);
                Button btnClose = conView.findViewById(R.id.btnClose);
                BottomSheetDialog dia = new BottomSheetDialog(context);
                dia.setContentView(conView);
                btnClose.setOnClickListener(v14 -> {
                    dia.dismiss();
                    alertDialog.dismiss();
                });
                btnConfirm.setOnClickListener(v13 -> {
                    blockUser();
                    alertDialog.dismiss();
                    dia.dismiss();
                });
                dia.show();
                alertDialog.show();
            });
        } else {
            dialogPostBinding.txtDelete.setText(context.getString(R.string.unblock));
            dialogPostBinding.txtDelete.setOnClickListener(v -> {
                View conView = activity.getLayoutInflater().inflate(R.layout.unblock_user_popup, null);
                Button btnConfirm = conView.findViewById(R.id.btnConfirm);
                Button btnClose = conView.findViewById(R.id.btnClose);
                BottomSheetDialog dia = new BottomSheetDialog(context);
                dia.setContentView(conView);
                btnClose.setOnClickListener(v1 -> {
                    dia.dismiss();
                    alertDialog.dismiss();
                });
                btnConfirm.setOnClickListener(v12 -> {
                    unblockUser();
                    alertDialog.dismiss();
                    dia.dismiss();
                });
                dia.show();
                alertDialog.show();
            });
        }

        dialogPostBinding.txtReport.setText(context.getString(R.string.Report_User));
        dialogPostBinding.txtReport.setOnClickListener(v -> {
            final androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.CustomAlertDialog);
            final View dialogView1 = LayoutInflater.from(context).inflate(R.layout.dialog_svs, null, false);
            builder1.setView(dialogView1);
            androidx.appcompat.app.AlertDialog alertDialog1 = builder1.create();
            TextView report_option = dialogView1.findViewById(R.id.report_option);
            ProgressBar progressBar = dialogView1.findViewById(R.id.progress_bar);
            RecyclerView report_recycler = dialogView1.findViewById(R.id.report_recycler);
            report_option.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            Map<String, String> myHeader = new HashMap<>();
            myHeader.put("Accept", "application/json");
            myHeader.put("Content-Type", "application/json");
            myHeader.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
            String type = "Profile";
            HashMap<String, Object> body = new HashMap<>();
            body.put("reportType", type);
            body.put("reportedData", false);


            disposable.add(Global.initSocialRetrofit().getReportTypes(myHeader, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable -> binding.loading.setVisibility(View.GONE))
                    .doOnTerminate(() -> binding.loading.setVisibility(View.GONE))
                    .subscribe((ReportTypeResponse response, Throwable throwable) -> {
                        progressBar.setVisibility(View.GONE);
                        if (response.getCode() == 1 && response.getSuccess()) {
                            ReportAdapter reportAdapter = new ReportAdapter(context,
                                    response, alertDialog1, progressBar, userId, type);
                            report_recycler.setLayoutManager(new LinearLayoutManager(context));
                            report_recycler.setAdapter(reportAdapter);
                            report_recycler.setVisibility(View.VISIBLE);
                        } else {
                            Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                            alertDialog1.dismiss();
                        }
                    }));
            alertDialog1.show();
        });
        dialogPostBinding.txtCopyLink.setVisibility(View.GONE);
        dialogPostBinding.txtShareTo.setVisibility(View.GONE);
        alertDialog.show();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public void unblockUser() {
        try {
            Map<String, String> header = new HashMap<>();
            header.put("Accept", "application/json");
            header.put("Content-Type", "application/json");
            header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
            HashMap<String, String> body = new HashMap<>();
            body.put("userId", userId);
            disposable.add(Global.initSocialRetrofit().removeBlockedUserProfile(header, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable -> binding.loading.setVisibility(View.GONE))
                    .doOnTerminate(() -> binding.loading.setVisibility(View.GONE))
                    .subscribe((RemoveBlockedUserResponse response, Throwable throwable) -> {
                        if (response.getCode() == 1) {
                            Utilss.showToast(context, context.getString(R.string.User_unblocked_successfully), R.color.green);
                            data();
                            binding.layoutBlock.setVisibility(View.GONE);
                        } else {
                            Utilss.showToast(context, context.getString(R.string.error_msg), R.color.green);
                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void blockUser() {
        if (ConnectionUtils.isConnected(context)) {
            Map<String, String> myHeader = new HashMap<>();
            myHeader.put("Accept", "application/json");
            myHeader.put("Content-Type", "application/json");
            myHeader.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
            HashMap<String, String> body = new HashMap<>();
            body.put("blockedTo", userId);
            disposable.add(Global.initSocialRetrofit().blockUser(myHeader, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable -> binding.loading.setVisibility(View.GONE))
                    .doOnTerminate(() -> binding.loading.setVisibility(View.GONE))
                    .subscribe((BlockUserResponse response, Throwable throwable) -> {
                        if (response.getCode() == 1) {
                            Utilss.showToast(context, context.getString(R.string.User_Blocked), R.color.social_background_blue);
                            data();
                        } else {
                            Utilss.showToast(context, response.getErrorMessage(), R.color.social_background_blue);
                        }
                    }));
        }
    }

//    public boolean pushFragment(Fragment fragment, String tag) {
//        try {
//            if (fragment != null) {
//                ((AppCompatActivity) context).getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.fragment_container_new1, fragment, tag)
//                        .addToBackStack("fragment")
//                        .commit();
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    public void data() {
        binding.loading.setVisibility(View.VISIBLE);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", xToken);
        HashMap<String, String> body = new HashMap<>();
        body.put("userId", userId);
        disposable.add(Global.initSocialRetrofit().userProfile(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> binding.loading.setVisibility(View.GONE))
                .doOnTerminate(() -> binding.loading.setVisibility(View.GONE))
                .subscribe((UserProfileRespone1 response, Throwable throwable) -> {
                    binding.loading.setVisibility(View.GONE);
                    if (response != null && response.getSuccess()) {
                        try {
                            OtherUserAccountViewModel.this.response = response;
                            otherUserData = new DataUser1();
                            otherUserData = response.getDataUser();
                            user_name = response.getDataUser().getUsername();
                            first_name = response.getDataUser().getFirstName();
                            last_name = response.getDataUser().getLastName();
                            profileUrl = response.getDataUser().getDisplayPicture();
                            profileUrl = response.getDataUser().getDisplayPicture();
                            binding.txtNameProfile.setText(response.getDataUser().getUsername());
                            binding.txtFollowingCount.setText(response.getDataUser().getTotalFollowings().toString());
                            binding.txtFollowersCount.setText(response.getDataUser().getTotalFollowers().toString());
                            if (response.getDataUser().getTotalFollowings().toString().equalsIgnoreCase("0") || response.getDataUser().getTotalFollowings().toString().equalsIgnoreCase("1")) {
                                binding.tvFollowerTitle.setText(R.string.title_follower);
                            }
                            binding.txtTotalPost.setText(response.getDataUser().getTotalPosts().toString());
                            if (response.getDataUser().getTotalPosts().toString().equalsIgnoreCase("0") || response.getDataUser().getTotalPosts().toString().equalsIgnoreCase("1")) {
                                binding.tvPostTitle.setText(R.string.title_post);
                            }
                            String test = URLDecoder.decode(response.getDataUser().getAbout(), "UTF-8");
//                            binding.tcxtLocation.setText(test);
                            user_data.add(response);
                            blockId = response.getDataUser().getId();
                            isPrivate = response.getDataUser().getAccountType().equalsIgnoreCase(context.getString(R.string.Private));
                            if (response.getDataUser().getGender().equalsIgnoreCase("Male")) {
                                CommonUtils.loadImage(binding.imgProfile, response.getDataUser().getDisplayPicture(), context, R.drawable.male_place_holder);
                            } else if (response.getDataUser().getGender().equalsIgnoreCase("Female")) {
                                CommonUtils.loadImage(binding.imgProfile, response.getDataUser().getDisplayPicture(), context, R.drawable.female_cardplaceholder);
                            } else {
                                CommonUtils.loadImage(binding.imgProfile, response.getDataUser().getDisplayPicture(), context, R.drawable.placeholder);
                            }
                            binding.ivMore.setOnClickListener(v -> openDialog(userId, SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID)));
                            followStatus = response.getDataUser().getFirendStatus();
                            friendStatus = response.getDataUser().getFirendStatus();
                            blockStatus = response.getDataUser().getIsBlocked();
                            accountType = response.getDataUser().getAccountType();
                            blockByMe = response.getDataUser().getBlockedByMe();
                            if (response.getDataUser().getFirendStatus().equalsIgnoreCase("Following")) {
                                binding.btnFollowFollowing.setText(context.getString(R.string.unfollow));
                            } else if (response.getDataUser().getFirendStatus().equalsIgnoreCase("Waiting")) {
                                binding.btnFollowFollowing.setText(context.getString(R.string.request));
                            } else if (response.getDataUser().getFirendStatus().equalsIgnoreCase("NoFriend") || response.getDataUser().getFirendStatus().equalsIgnoreCase("follower")) {
                                binding.btnFollowFollowing.setText(context.getString(R.string.follow));
                            }
                            if (response.getDataUser().getAccountType().equalsIgnoreCase("PUBLIC") || response.getDataUser().getFirendStatus().equalsIgnoreCase("Following")) {
                                if (response.getDataUser().getIsBlocked() == 0) {
                                    binding.layoutPrivateCard.setVisibility(View.GONE);
                                    binding.lockOnImage.setVisibility(View.GONE);
                                    binding.liForPrivateAccount.setVisibility(View.VISIBLE);
                                    binding.layoutDataFollow.setVisibility(View.VISIBLE);
                                    binding.layoutDataFollow2.setVisibility(View.VISIBLE);
                                    binding.layoutBlock.setVisibility(View.GONE);
                                    //pushFragment(new OtherFriendPhotoGridFrag(context, userId), "");
                                } else {
                                    binding.btnFollowFollowing.setVisibility(View.GONE);
                                    binding.message.setVisibility(View.GONE);
                                    binding.liForPrivateAccount.setVisibility(View.GONE);
                                    binding.layoutBlock.setVisibility(View.VISIBLE);
                                    binding.layoutDataFollow.setVisibility(View.GONE);
                                    binding.layoutDataFollow2.setVisibility(View.GONE);
                                    binding.layoutPrivateCard.setVisibility(View.GONE);
                                    binding.lockOnImage.setVisibility(View.GONE);
                                }
                            } else {
                                if (userId.equalsIgnoreCase(SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID))) {
                                    binding.layoutPrivateCard.setVisibility(View.GONE);
                                    binding.lockOnImage.setVisibility(View.GONE);
                                    binding.liForPrivateAccount.setVisibility(View.VISIBLE);
                                    binding.layoutDataFollow.setVisibility(View.VISIBLE);
                                    binding.layoutDataFollow2.setVisibility(View.VISIBLE);
                                    binding.layoutBlock.setVisibility(View.GONE);
                                    // pushFragment(new OtherFriendPhotoGridFrag(context, userId), "");
                                } else {
                                    binding.liForPrivateAccount.setVisibility(View.GONE);
                                    binding.layoutPrivateCard.setVisibility(View.VISIBLE);
                                    binding.lockOnImage.setVisibility(View.VISIBLE);
                                    binding.layoutBlock.setVisibility(View.GONE);
                                }
                            }
                            arrayList_user_data.add(response);
                        } catch (Exception e) {
                        }
                    } else {

                    }
                }));
    }

    public void followRequest() {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", xToken);
        HashMap<String, String> body = new HashMap<>();
        body.put("followingId", userId);
        binding.loading.setVisibility(View.VISIBLE);
        disposable.add(Global.initSocialRetrofit().followingRequest(map, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> binding.loading.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> binding.loading.setVisibility(View.GONE))
                .subscribe((ApiResponse response, Throwable throwable) -> {
                    if (response.getCode() == 1 && response.getSuccess()) {
                        data();
                    } else {
                        binding.loading.setVisibility(View.GONE);
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void unfollow() {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", xToken);
        HashMap<String, Object> body = new HashMap<>();
        body.put("followingId", userId);
        body.put("status", false);
        binding.loading.setVisibility(View.VISIBLE);
        disposable.add(Global.initSocialRetrofit().followingStatus(map, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> binding.loading.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> binding.loading.setVisibility(View.GONE))
                .subscribe((UnfollowResponse response, Throwable throwable) -> {
                    binding.loading.setVisibility(View.GONE);
                    if (response.getCode() == 1 && response.getSuccess()) {
                        data();
                    } else {
                        binding.loading.setVisibility(View.GONE);
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void dialogMessage(Boolean block) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        EditText message = dialog.findViewById(R.id.message);
        Button submit = dialog.findViewById(R.id.submit);
        submit.setOnClickListener(v -> {
            if (message.getText().toString().trim().isEmpty()) {
                Toast.makeText(context, context.getString(R.string.enter_message), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!block)
                sendMessageApi(message.getText().toString(), true);
            dialog.dismiss();

        });
    }

    public void sendMessageApi(String msg, boolean status) {
        Map<String, String> myHeader = new HashMap<>();
        myHeader.put("Accept", "application/json");
        myHeader.put("Content-Type", "application/json");
        myHeader.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        HashMap<String, Object> body = new HashMap<>();
        body.put("friendId", userId);
        body.put("status", status);
        body.put("msg", msg);
        disposable.add(Global.initSocialRetrofit().message(myHeader, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> binding.loading.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> binding.loading.setVisibility(View.GONE))
                .subscribe((MessageResponse messageResponse, Throwable throwable) -> {

                    if (messageResponse != null) {
                        if (messageResponse.getCode() == 1 && messageResponse.isSuccess()) {
                            createChatHeadApi(msg, true, false);
                        } else {
                            Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                        }
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));

    }

    public void createChatHeadApi(String msg, boolean isRequest, boolean isAccpeted) {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        Map request = new HashMap();
        request.put("toUserId", userId);
        request.put("isAccepted", isAccpeted);
        disposable.add(Global.initSocialRetrofit().createChatHeads2(header, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> binding.loading.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> binding.loading.setVisibility(View.GONE))
                .subscribe((createGroupResponse, throwable) -> {
                    try {
                        Log.e(TAG, "sendMessageApi: "+createGroupResponse.getCode());
                        if (isRequest) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID));
                                jsonObject.put("toUserId", userId);
                                jsonObject.put("chatHeadId", createGroupResponse.getData().getId());
                                jsonObject.put("msg", msg);
                                jsonObject.put("lastMsg", msg);
                                jsonObject.put("isGroup", false);
                                jsonObject.put("attachment", false);
                                jsonObject.put("attachmentType", "Message");
                                jsonObject.put("sendTime", Helper.getCurrentTime());
                                jsonObject.put("toReplyId", "");
                                mSocket.emit("send", jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (createGroupResponse.getCode() == 1) {
                                Intent intent = new Intent(context, ChatBoatActivity.class);
                                intent.putExtra("storyId", activity.getIntent().getStringExtra("storyId"));
                                intent.putExtra("url", activity.getIntent().getStringExtra("url"));
                                intent.putExtra("type", activity.getIntent().getStringExtra("type"));
                                intent.putExtra("thumbnail", activity.getIntent().getStringExtra("thumbnail"));
                                intent.putExtra("user_id_to", userId);
                                intent.putExtra("chatHeadId", createGroupResponse.getData().getId());
                                intent.putExtra("pro_file_pic", response.getDataUser().getDisplayPicture());
                                intent.putExtra("from_push", "0");
                                intent.putExtra("isSingle", true);
                                intent.putExtra("demoid", response.getDataUser().getId());
                                intent.putExtra("firstName", response.getDataUser().getFirstName());
                                intent.putExtra("lastName", response.getDataUser().getLastName());
                                intent.putExtra("username", response.getDataUser().getUsername());
                                intent.putExtra("profilePicture", response.getDataUser().getDisplayPicture());
                                activity.startActivity(intent);
                            } else {
                                Utilss.showToast(context, createGroupResponse.getSuccess().toString(), R.color.msg_fail);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
    }

    public void inits() {
        userId = activity.getIntent().getExtras().getString("userId");
        if (userId.equalsIgnoreCase(SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.ID))) {
            binding.ivMore.setVisibility(View.GONE);
        }
        binding.txtSeeAll.setOnClickListener(view -> {
            Intent intent = new Intent(context, DiscoverPeopleActivity.class);
            activity.startActivity(intent);
        });
//        tcxt_location.setOnClickListener((View.OnClickListener) v -> {
//          /*  Intent intent = new Intent(OtherUserActivity.context, ViewContactActivity.class);
//            intent.putExtra("test", tcxt_location.getText());
//            intent.putExtra("xyz", "abc");
//            startActivity(intent);*/
//        });
        binding.layoutFollowing.setOnClickListener(view -> {
            if (response.getDataUser().getAccountType().equalsIgnoreCase("PUBLIC") || response.getDataUser().getFirendStatus().equalsIgnoreCase("Following") || SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID).equals(userId)) {
                Intent intent = new Intent(context, FollowListActivity.class);
                intent.putExtra("type", "following");
                intent.putExtra("userId", userId);
                intent.putExtra("isSvs", false);
                intent.putExtra("changerole", "false");
                activity.startActivity(intent);
            }
        });
        binding.layoutFollowers.setOnClickListener(view -> {
            if (response.getDataUser().getAccountType().equalsIgnoreCase("PUBLIC") || response.getDataUser().getFirendStatus().equalsIgnoreCase("Following") || SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID).equals(userId)) {
                Intent intent = new Intent(context, FollowListActivity.class);
                intent.putExtra("type", "follower");
                intent.putExtra("userId", userId);
                intent.putExtra("isSvs", false);
                intent.putExtra("changerole", "false");
                activity.startActivity(intent);
            }
        });
        if (SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.ID).equals(userId)) {
            binding.btnFollowFollowing.setVisibility(View.GONE);
            binding.message.setVisibility(View.GONE);
        } else {
            binding.btnFollowFollowing.setVisibility(View.VISIBLE);
            binding.message.setVisibility(View.VISIBLE);
        }
        binding.message.setOnClickListener(view -> {
            if (response.getDataUser().getIsBlocked() == 0) {
                if (!isMessageButtonClicked) {
                    Log.e(TAG, "inits: " + "Message button clicked");
                    isMessageButtonClicked = true;
                    Map<String, String> header = new HashMap<>();
                    header.put("x-token", SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.AUTH_TOKEN));
                    Map request = new HashMap();
                    request.put("followingId", userId);
                    request.put("userId", SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.ID));
                    context.apiCallBack(context.getApi().checkFollowNChatHead(header, request));
                }
            } else {
                Toast.makeText(context, context.getString(R.string.block_other_user_account), Toast.LENGTH_LONG).show();
            }
        });
        binding.btnFollowFollowing.setOnClickListener(view -> {
            if (otherUserData == null) {
                return;
            }
            if (otherUserData.getFirendStatus().equalsIgnoreCase("Following")) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                final View dView = LayoutInflater.from(context).inflate(R.layout.custom_layout_remove_people_alert, viewGroup, false);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                TextView tvYes = dView.findViewById(R.id.tvYes);
                ImageView ivPeople = dView.findViewById(R.id.ivPeople);
                TextView tvNamePeople = dView.findViewById(R.id.tvNamePeople);
                TextView tvPeopleTitle = dView.findViewById(R.id.tvPeopleTitle);
                Glide.with(context).load(profileUrl).into(ivPeople);
                tvNamePeople.setText(first_name + " " + last_name);
                tvPeopleTitle.setText(user_name);
                TextView tvNo = dView.findViewById(R.id.tvNo);
                tvYes.setOnClickListener(v -> {
                    unfollow();
                    alertDialog.dismiss();
                });
                tvNo.setOnClickListener(v -> {
                    tvNo.setOnClickListener(v1 -> alertDialog.dismiss());
                    alertDialog.setContentView(dView);
                });
                alertDialog.setContentView(dView);
            } else if (otherUserData.getFirendStatus().equalsIgnoreCase("Waiting")) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                final View dView = LayoutInflater.from(context).inflate(R.layout.custom_layout_remove_people_alert, viewGroup, false);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                TextView tvYes = dView.findViewById(R.id.tvYes);
                ImageView ivPeople = dView.findViewById(R.id.ivPeople);
                TextView tvNamePeople = dView.findViewById(R.id.tvNamePeople);
                TextView tvPeopleTitle = dView.findViewById(R.id.tvPeopleTitle);
                Glide.with(context).load(profileUrl).into(ivPeople);
                tvNamePeople.setText(first_name + " " + last_name);
                tvPeopleTitle.setText(user_name);
                TextView tvNo = dView.findViewById(R.id.tvNo);
                tvYes.setOnClickListener(v -> {
                    unfollow();
                    alertDialog.dismiss();
                });
                tvNo.setOnClickListener(v -> {
                    tvNo.setOnClickListener(v12 -> alertDialog.dismiss());
                    alertDialog.setContentView(dView);
                });
                alertDialog.setContentView(dView);
            } else if (otherUserData.getFirendStatus().equalsIgnoreCase("NoFriend") || otherUserData.getFirendStatus().equalsIgnoreCase("follower")) {
                followRequest();
            }
        });

      /*  binding.layoutGrid.setOnClickListener(view -> {
//            pushFragment(new OtherFriendPhotoGridFrag(context, userId), "");

        });
        binding.layoutCamera.setOnClickListener(view -> {
//            pushFragment(new OtherUserCameraFrag(context, userId), "");

        });
        binding.layoutVideo.setOnClickListener(view -> {
            status_post = 1;
//            pushFragment(new OtherUserProfileVideoFrag(context, userId), "");

        });*/
        data();
    }
}
