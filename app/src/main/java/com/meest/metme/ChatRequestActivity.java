package com.meest.metme;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.meest.Activities.base.ApiCallActivity;
import com.meest.Adapters.RequestedMessageAdapter;
import com.meest.Meeast;
import com.meest.R;
import com.meest.responses.AcceptRejectResponse;
import com.meest.responses.GetPendingUserResponse;
import com.meest.responses.ProfilePhotoUploadResponse;
import com.meest.metme.encription.AESHelper;
import com.meest.utils.Helper;
import com.meest.utils.ParameterConstants;
import com.meest.databinding.ActivityChatRequestBinding;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.metme.model.gallery.GalleryPhotoModel;
import com.meest.metme.viewmodels.AcceptRequestViewModel;
import com.meest.utils.RecordButton;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ChatRequestActivity extends ApiCallActivity {

    ActivityChatRequestBinding binding;
    TextView note, tvTitle;
    Socket mSocket;
    RequestedMessageAdapter adapter;
    GetPendingUserResponse response;
    ArrayList<GalleryPhotoModel> selectedItem;
    String userId;
    String chatHeadId;
    File file;
    boolean userScrolled;
    int scrollOutitems;
    int pageNumber = 1;
    int limit = 10;
    LinearLayoutManager layoutManager;
    List<GetPendingUserResponse.Friends> messageList = new ArrayList<>();
    private Boolean fromNotification = false;
    private ImageView img_call, backButton;
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatRequestActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.w("objjj", args.toString());
                    JSONObject data = (JSONObject) args[0];
                    try {
                        Log.w("demodemo", data.getString("msg"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("userId", userId);
                        jsonObject.put("name", getIntent("firstName") + " " + getIntent("lastName"));
                        jsonObject.put("isGroup", false);
                        mSocket.emit("createSession", jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    };
    private Emitter.Listener session = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatRequestActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("msg");
                        Log.w("session", username);

                    } catch (JSONException e) {
                        Log.e("TAGUserJoin", e.getMessage());
                        return;
                    }
                }
            });
        }
    };
    private Emitter.Listener sent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatRequestActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //                   Log.e("SENT DATA:", args.toString());
                        JSONObject data = (JSONObject) args[0];
                        JSONObject msg = data.getJSONObject("msg");

                        GetPendingUserResponse.Friends friends = new GetPendingUserResponse().new Friends();
                        friends.setMsg(msg.getString("msg"));
                        messageList.add(friends);
                        adapter.notifyDataSetChanged();
                        if (selectedItem != null) {
                            if (selectedItem.size() > 0) {
                                selectedItem.remove(0);
                                recursion();
                            }
                        } else {
                            moveToChatboat();
                        }

                    } catch (JSONException e) {
                        Log.e("SENT EXCEPTION", e.getMessage());
                        return;
                    }

                }
            });
        }
    };
    private Emitter.Listener successFullySend = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatRequestActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        JSONObject messageJSON = new JSONObject(data.getString("msg"));
                        Log.e("successFullySend", "+++" + messageJSON.getString("msg"));
                        if (selectedItem != null) {
                            selectedItem.remove(0);
                            if (selectedItem.size() > 0) {
                                recursion();
                            } else {
                                moveToChatboat();
                            }
                        } else {
                            moveToChatboat();
                        }
                    } catch (Exception e) {
                        Log.e("onNewMessage", e.getMessage());
                        return;
                    }
                }
            });
        }
    };


   /* @Subscribe
    public void createChatHead(CreateGroupResponse response) {
        chatHeadId = response.getData().getId();
        if ("text".equalsIgnoreCase(binding.getAcceptRequestViewModel().attachmentType)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
                jsonObject.put("toUserId", userId);
                jsonObject.put("chatHeadId", chatHeadId);
                jsonObject.put("msg", binding.getAcceptRequestViewModel().enteredMsg.getValue().trim());
                jsonObject.put("lastMsg", binding.getAcceptRequestViewModel().enteredMsg.getValue().trim());
                jsonObject.put("isGroup", "" + false);
                jsonObject.put("attachment", "" + false);
                jsonObject.put("attachmentType", "Message");
                mSocket.emit("send", jsonObject);
                binding.getAcceptRequestViewModel().enteredMsg.setValue("");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if ("gallery".equals(binding.getAcceptRequestViewModel().attachmentType)) {
            recursion();
        }else if ("camera".equals(binding.getAcceptRequestViewModel().attachmentType)) {
            recursion();
        }else if ("audio".equals(binding.getAcceptRequestViewModel().attachmentType)) {
            sendAudio(ChatRequestActivity.this.file);
        }


    }*/
    private Emitter.Listener chat_history = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        messageList.clear();
                        String username = String.valueOf(args[0]);
                        String new_str = "{" + "\"usernew\":" + username + "}";
                        Log.e("TAG", "run: " + new_str);
                        Timber.e(new_str);
                        JSONObject jObj = new JSONObject(new_str);
                        JSONArray jsonArry = jObj.getJSONArray("usernew");
                        for (int i = 0; i < jsonArry.length(); i++) {
                            JSONObject obj = jsonArry.getJSONObject(i);
                            if (obj.has("id")) {
                                GetPendingUserResponse.Friends friends = new GetPendingUserResponse().new Friends();
                                Log.e("msg=========", obj.getString("msg"));
                                friends.setMsg(obj.getString("msg"));
//                                binding.messageDate.setText(messageList.get(0).getMsg());
                                messageList.add(friends);
                            }
                        }
                        if (jsonArry.length() > 0) {
                            userScrolled = true;
                        } else {
                            userScrolled = false;
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TAG_UserHistory", e.getMessage());
                        return;
                    }
                }
            });
        }
    };
    private Emitter.Listener onMsgHistorywithPages = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        messageList.clear();
                        String username = String.valueOf(args[0]);
                        String new_str = "{" + "\"usernew\":" + username + "}";
                        Log.w("onHistory", username);
                        JSONObject jObj = new JSONObject(new_str);
                        JSONArray jsonArry = jObj.getJSONArray("usernew");

                        for (int i = 0; i < jsonArry.length(); i++) {

                            JSONObject obj = jsonArry.getJSONObject(i);
                            if (obj.has("id")) {
                                GetPendingUserResponse.Friends friends = new GetPendingUserResponse().new Friends();
                                friends.setMsg(obj.getString("msg"));
                                messageList.add(friends);
                            }
                        }

                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TAG_UserHistory", e.getMessage());

                        return;
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_request);
        img_call = findViewById(R.id.img_call);
        backButton = findViewById(R.id.backButton);

        note = findViewById(R.id.note);
        tvTitle = findViewById(R.id.tvTitle);
        img_call.setVisibility(View.GONE);

        userId = getIntent().getStringExtra("userId");
        fromNotification = getIntent().getBooleanExtra("notification", false);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        HashMap body = new HashMap<>();
        body.put("userId", userId);
        apiCallBack(getApi().getPendingUserDetails(map, body));

        binding.mainLayout.setVisibility(View.GONE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.micro.setOnStartRecord(new RecordButton.StartRecordListener() {
            @Override
            public void onStartRecord() {
                binding.getAcceptRequestViewModel().isClicked = true;
                binding.getAcceptRequestViewModel().action = "accept";
                binding.getAcceptRequestViewModel().attachmentType = "audio";
            }
        });

        binding.micro.setRecordingFinishedListener(new RecordButton.RecordingFinishedListener() {
            @Override
            public void onRecordingFinished(File file) {
                ChatRequestActivity.this.file = file;
                acceptApiCall();
            }
        });

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setFocusable(false);
        binding.recyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        adapter = new RequestedMessageAdapter(this, messageList);
        binding.recyclerView.setAdapter(adapter);
        initSocket();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void initSocket() {
        Meeast app = (Meeast) getApplication();
        mSocket = app.getSocket();
        mSocket.on("connected", onConnect);
        mSocket.on("session", session);
        mSocket.on("sent", sent);
        mSocket.on("sent_ios", successFullySend);
        mSocket.on("chat_history", chat_history);
        mSocket.on("chat_history_pages", onMsgHistorywithPages);
        mSocket.connect();

    }

    @Subscribe
    public void acceptReject(AcceptRejectResponse response) {
        if (response.isSuccess()) {
            if ("accept".equalsIgnoreCase(binding.getAcceptRequestViewModel().action)) {
                /*Map<String, String> header = new HashMap<>();
                header.put("x-token", SharedPrefreances.getSharedPreferenceString(ChatRequestActivity.this, SharedPrefreances.AUTH_TOKEN));
                Map request = new HashMap();
                request.put("toUserId", userId);
                request.put("isAccepted",true);
                apiCallBack(getApi().createChatHeads2(header, request));*/

                if ("text".equalsIgnoreCase(binding.getAcceptRequestViewModel().attachmentType)) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
                        jsonObject.put("toUserId", userId);
                        jsonObject.put("chatHeadId", chatHeadId);
                        jsonObject.put("msg", binding.getAcceptRequestViewModel().enteredMsg.getValue().trim());
                        jsonObject.put("lastMsg", binding.getAcceptRequestViewModel().enteredMsg.getValue().trim());
                        jsonObject.put("isGroup", "" + false);
                        jsonObject.put("attachment", "" + false);
                        jsonObject.put("attachmentType", "Message");
                        jsonObject.put("sendTime", Helper.getCurrentTime());
                        mSocket.emit("send", jsonObject);
                        binding.getAcceptRequestViewModel().enteredMsg.setValue("");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if ("gallery".equals(binding.getAcceptRequestViewModel().attachmentType)) {
                    recursion();
                } else if ("camera".equals(binding.getAcceptRequestViewModel().attachmentType)) {
                    recursion();
                } else if ("audio".equals(binding.getAcceptRequestViewModel().attachmentType)) {
                    sendAudio(ChatRequestActivity.this.file);
                }
            } else if ("block".equalsIgnoreCase(binding.getAcceptRequestViewModel().action)) {
                finish();
            } else if ("delete".equalsIgnoreCase(binding.getAcceptRequestViewModel().action)) {
                binding.getAcceptRequestViewModel().dialog.dismiss();
                if (fromNotification) {
                    Intent intent = new Intent(this, ChatActivity.class);
                    startActivity(intent);
                }
                finish();

            }
        } else {
            Utilss.showToast(getApplicationContext(), getString(R.string.error), R.color.msg_fail);
        }
    }

    void recursion() {
        Bitmap bitmap = null;
        try {
//                String filePath = compressImage(Uri.parse(selectedItem)).toString();
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Helper.getImageContentUri(getApplicationContext(), new File(selectedItem.get(0).getPicturePath())));
//                String path = Helper.saveImage(getApplicationContext(), bitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            InputStream is = getContentResolver().openInputStream(Helper.getImageContentUri(getApplicationContext(), new File(selectedItem.get(0).getPicturePath())));
            upload(Helper.getBytes(is), chatHeadId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void getPendingUserResponse(GetPendingUserResponse response) {
        if (response.isSuccess()) {

            this.response = response;
            if (response.getData().getResult() != null)
                chatHeadId = response.getData().getResult().getId();
            binding.setAcceptRequestViewModel(new AcceptRequestViewModel(this, chatHeadId));
            binding.mainLayout.setVisibility(View.VISIBLE);
            binding.messageDate.setText(response.getData().getResult().getCreatedAt());
            binding.name.setText(response.getData().getUser().getFirstName() + " " + response.getData().getUser().getLastName());
            binding.name2.setText(response.getData().getUser().getFirstName() + " " + response.getData().getUser().getLastName());
            tvTitle.setText(response.getData().getUser().getFirstName() + " " + response.getData().getUser().getLastName());
            binding.followerPost.setText("" + response.getData().getUser().getTotalFollowers() + " follower " + response.getData().getUser().getTotalPosts() + " post");
            String str = note.getText().toString().replace("Shivi Shukla", response.getData().getUser().getFirstName() + " " + response.getData().getUser().getLastName());
            note.setText(str);

            if (response.getData().getUser().getDisplayPicture() != null | !response.getData().getUser().getDisplayPicture().isEmpty()) {
                Glide.with(binding.image.getContext()).load(response.getData().getUser().getDisplayPicture()).placeholder(R.drawable.ic_edit_metme)
                        .into(binding.image);
                Glide.with(binding.image2.getContext()).load(response.getData().getUser().getDisplayPicture()).placeholder(R.drawable.ic_edit_metme)
                        .into(binding.image2);
                Glide.with(binding.imgProfile.getContext()).load(response.getData().getUser().getDisplayPicture()).placeholder(R.drawable.ic_edit_metme)
                        .into(binding.imgProfile);
            } else {
                binding.image.setBackgroundResource(R.drawable.ic_edit_metme);
                binding.image2.setBackgroundResource(R.drawable.ic_edit_metme);
            }

            binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        userScrolled = true;
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    scrollOutitems = layoutManager.findFirstCompletelyVisibleItemPosition();
                    Log.e("Message Scroll", scrollOutitems + "");
                    if (userScrolled && (scrollOutitems == 0 && messageList.size() >= 10)) {

                        userScrolled = false;
                        JSONObject jsonObject = new JSONObject();
                        try {
                            pageNumber = pageNumber + 1;
                            jsonObject.put("chatHeadId", chatHeadId);
                            jsonObject.put("page", pageNumber);
                            jsonObject.put("limit", limit);
                            jsonObject.put("toUserId", userId);
                            jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(ChatRequestActivity.this, SharedPrefreances.ID));
                            mSocket.emit("get_history_pages", jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            getHistoryEmit();
        } else {
            dialogFailed("" + response.getCode());
        }
    }

    public void getHistoryEmit() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("chatHeadId", chatHeadId);
            jsonObject.put("limit", 10);
            jsonObject.put("page", 1);
            jsonObject.put("toUserId", userId);
            jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
            mSocket.emit("get_history", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void acceptApiCall() {
        binding.getAcceptRequestViewModel().action = "accept";
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        HashMap body = new HashMap<>();
        body.put("friendId", userId);
        body.put("isAccepted", true);
        body.put("chatHeadId", chatHeadId);
        apiCallBack(getApi().acceptRejectRequest(map, body));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            binding.getAcceptRequestViewModel().isClicked = true;
            if (requestCode == ParameterConstants.LAUNCH_SECOND_ACTIVITY) {
                selectedItem = data.getParcelableArrayListExtra("result");
                acceptApiCall();
            }
            if (requestCode == ParameterConstants.PICKER) {
                GalleryPhotoModel model = new GalleryPhotoModel();
                model.setImageText(data.getStringExtra("name"));
                model.setPicturePath(data.getStringExtra("path"));
                model.setVideo(data.getBooleanExtra("isVideo", false));
                model.setType("Image");
                if (selectedItem == null) {
                    selectedItem = new ArrayList<>();
                }
                selectedItem.add(model);
                acceptApiCall();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == ParameterConstants.CAMERA_REQUEST) {
            Log.e("Phasa", "Re phasas");
        }
    }

    public void upload(byte[] imageBytes, String chatHeadId) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);

        /*File file = new File(filePath);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(),
                RequestBody.create(MediaType.parse("image/jpeg"), file));*/
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<ProfilePhotoUploadResponse> call = webApi.insert(map, filePart);

        call.enqueue(new Callback<ProfilePhotoUploadResponse>() {
            @Override
            public void onResponse(Call<ProfilePhotoUploadResponse> call, Response<ProfilePhotoUploadResponse> response) {
                try {
                    // image.setVisibility(View.GONE);
                    if (response.body().getCode() == 1) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            String fileUrl = AESHelper.encrypt(response.body().getData().getUrl(), chatHeadId);
                            jsonObject.put("msg", "");
                            jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
                            jsonObject.put("toUserId", userId);
                            jsonObject.put("chatHeadId", chatHeadId);
                            jsonObject.put("fileURL", fileUrl);
                            jsonObject.put("attachment", true);
                            jsonObject.put("attachmentType", "Image");
                            jsonObject.put("sendTime", Helper.getCurrentTime());
                            mSocket.emit("send", jsonObject);
                            Log.e("TAG", "imageuploadsocket=" + jsonObject);


                        } catch (JSONException e) {
                            Log.e("TAg", "maintaionedsocket=" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ProfilePhotoUploadResponse> call, Throwable t) {
                Utilss.showToast(getApplicationContext(), "SOME_ERROR", R.color.grey);
            }
        });


    }

    void moveToChatboat() {
        if (binding.getAcceptRequestViewModel().isClicked) {
            binding.getAcceptRequestViewModel().isClicked = false;
            Intent intent = new Intent(getApplicationContext(), ChatBoatActivity.class);
            intent.putExtra("firstName", ChatRequestActivity.this.response.getData().getUser().getFirstName());
            intent.putExtra("lastName", ChatRequestActivity.this.response.getData().getUser().getLastName());
            intent.putExtra("profilePicture", ChatRequestActivity.this.response.getData().getUser().getDisplayPicture());
            intent.putExtra("chatHeadId", chatHeadId);
            intent.putExtra("username", getIntent("username"));
            intent.putExtra("user_id_to", userId);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mSocket.off("connected", onConnect);
//        mSocket.off("session", session);
//        mSocket.off("sent", sent);
//        mSocket.off("sent_ios", successFullySend);
//        mSocket.disconnect();
    }

    private void sendAudio(File file) {
        Uri uri = Uri.fromFile(file);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(this, uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int millSecond = Integer.parseInt(durationStr);

        long seconds = TimeUnit.MILLISECONDS.toSeconds(millSecond);
        if (seconds < 1) {
            Toast.makeText(this, getString(R.string.hold_and_long_press_to_record), Toast.LENGTH_SHORT).show();
            RecordButton.setRecordDialogDismiss();
        } else {

            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millSecond),
                    TimeUnit.MILLISECONDS.toMinutes(millSecond) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millSecond)),
                    TimeUnit.MILLISECONDS.toSeconds(millSecond) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millSecond)));

            System.out.println("pppppppp  " + hms);
            RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), file);
            MultipartBody.Part AFile = MultipartBody.Part.createFormData("video", file.getName(), videoBody);
            Map<String, String> map = new HashMap<>();
            map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
            Call<ProfilePhotoUploadResponse> call = webApi.insert(map, AFile);
            call.enqueue(new Callback<ProfilePhotoUploadResponse>() {
                @Override
                public void onResponse(Call<ProfilePhotoUploadResponse> call, Response<ProfilePhotoUploadResponse> response) {
                    try {
                        if (response.body().getCode() == 1) {
                            Log.e("TAg", "maintainedpics=" + response.body().getData().getUrl());
                            JSONObject jsonObject = new JSONObject();
                            JSONObject jsonObjectTime = new JSONObject();
                            try {
                                String fileUrl = AESHelper.encrypt(response.body().getData().getUrl(), chatHeadId);
                                jsonObject.put("msg", "");
                                jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
                                jsonObject.put("toUserId", userId);
                                jsonObject.put("chatHeadId", chatHeadId);
                                jsonObject.put("fileURL", fileUrl);
                                jsonObject.put("attachment", true);
                                jsonObject.put("attachmentType", "Audio");
                                jsonObjectTime.put("duration", hms);
                                jsonObject.put("sendTime", Helper.getCurrentTime());
                                jsonObject.put("jsonData", jsonObjectTime);
                                mSocket.emit("send", jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//
                        } else {
                            Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ProfilePhotoUploadResponse> call, Throwable t) {
                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                }
            });

        }
    }
}