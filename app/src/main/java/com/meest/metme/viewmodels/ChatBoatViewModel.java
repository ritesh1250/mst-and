package com.meest.metme.viewmodels;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.google.gson.JsonObject;
import com.meest.R;
import com.meest.metme.camera2.utills.CameraUtil;
import com.meest.metme.jitsi.JitsiCallActivity;
import com.meest.responses.Message;
import com.meest.databinding.ActivityChatBoatBinding;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.metme.ChatBoatActivity;
import com.meest.metme.ChatUserContactActivity;
import com.meest.metme.MetMeGallery;
import com.meest.metme.WallpaperActivity;
import com.meest.metme.adapter.MessageAdapter;
import com.meest.metme.camera2.CameraActivity;
import com.meest.metme.encription.AESHelper;
import com.meest.metme.model.ChatBoatModel;
import com.meest.metme.model.SaveTextAppearanceModel;
import com.meest.metme.model.chat.CreateRoomRequestModel;
import com.meest.metme.model.chat.CreateRoomResponseModel;
import com.meest.metme.model.chat.NewChatListResponse;
import com.meest.responses.Message;
import com.meest.social.socialViewModel.model.UserProfileRespone1;
import com.meest.utils.Helper;
import com.meest.utils.ParameterConstants;
import com.meest.utils.Preferences;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;

import org.apache.commons.io.FilenameUtils;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBoatViewModel {
    private static final int CAMERA_REQUEST = 5;
    public static ChatBoatActivity context;
    public float size1 = 16;
    public float size2 = 14;
    public float size3 = 12;
    public float size4 = 10;
    public float size5 = 8;
    public String name;
    public String lastSeen;
    public String image;
    public ChatBoatModel chatBoatModel;
    public ObservableBoolean micro_gallery = new ObservableBoolean(true);
    public MutableLiveData<String> enteredMsg = new MutableLiveData<>();
    public String _firstColor;
    public String _secondColor;
    public MutableLiveData<String> fontName = new MutableLiveData<>();
    public String xToken;
    public ObservableBoolean reply_layout;
    public boolean muteVideo;
    public MessageAdapter messageAdapter;
    public String wallPaper_name;
    public SaveTextAppearanceModel saveTextResponseModel = new SaveTextAppearanceModel();
    public MutableLiveData<SaveTextAppearanceModel.Data> saveTextMutable = new MutableLiveData<>();
    public MutableLiveData<Boolean> appearanceData = new MutableLiveData<>(false);
    public ProgressDialog pDialog;
    protected String[] permissionsCamera = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    int LAUNCH_SECOND_ACTIVITY = 1231;
    Uri imageUri;
    public ActivityChatBoatBinding activityChatBoatBinding;
    private NewChatListResponse newChatListResponse;
    private Socket mSocket;
    private ArrayList<Message> messageList;
    private CustomDialogBuilder customDialogBuilder;
    private Handler mHandler;
    //===============================
    private String filePath;
    private File directoryName, fileName;

    //===========================
    public ChatBoatViewModel(ChatBoatModel chatBoatModel, Activity context, NewChatListResponse newChatListResponse, Socket mSocket, ArrayList<Message> messageList, CustomDialogBuilder customDialogBuilder, ObservableBoolean reply_layout, ActivityChatBoatBinding activityChatBoatBinding) {

        this.chatBoatModel = chatBoatModel;
        this.customDialogBuilder = customDialogBuilder;
        this.context = (ChatBoatActivity) context;
//        if (chatBoatModel.getChatUserName().length() > 10) {
//            this.name = chatBoatModel.getChatUserName().substring(0, 10) + "...";
//        } else {
        this.name = chatBoatModel.getChatUserName();
//    }
        this.lastSeen = chatBoatModel.getLastSeen();
        this.image = chatBoatModel.getChatProfileImage();
        this.newChatListResponse = newChatListResponse;
        this.mSocket = mSocket;
//        this.messageAdapter = messageAdapter;
        this.messageList = messageList;
        this.reply_layout = reply_layout;
        mHandler = new Handler();
//        * contextgetResources().getDisplayMetrics().scaledDensity

    }

    @BindingAdapter("bindChatMainProfile")
    public static void chatMainProfile(ImageView imageView, String imageUrl) {
        if (!imageUrl.isEmpty()) {

            Glide.with(imageView.getContext()).load(imageUrl).placeholder(R.drawable.ic_edit_metme)
                    .into(imageView);
        } else {
            if (context != null)
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_edit_metme));
        }
    }

    public void onInfoClick() {
        Intent intent = new Intent(context, WallpaperActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void onBackPress() {
        context.onBackPressed();
    }

    public void onPersonalChatInfoClick() {
        if (Helper.isNetworkAvailable(context)) {
            data();
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    public void data() {
        xToken = SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", xToken);

        HashMap<String, String> body = new HashMap<>();
        body.put("userId", newChatListResponse.getId());

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<UserProfileRespone1> call = webApi.userProfile(header, body);
        call.enqueue(new Callback<UserProfileRespone1>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<UserProfileRespone1> call, Response<UserProfileRespone1> response) {
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    Intent intent = new Intent(context, ChatUserContactActivity.class);
                    intent.putExtra("chatHeadID", newChatListResponse.getChatHeadId());
                    intent.putExtra("userId", newChatListResponse.getId());
                    intent.putExtra("fullName", response.body().getDataUser().getFirstName() + " " + response.body().getDataUser().getLastName());
                    intent.putExtra("followerCount", String.valueOf(response.body().getDataUser().getTotalFollowers()));
                    intent.putExtra("followingCount", String.valueOf(response.body().getDataUser().getTotalFollowings()));
                    intent.putExtra("profileImage", response.body().getDataUser().getDisplayPicture());
                    intent.putExtra("userBio", response.body().getDataUser().getAbout());
                    intent.putExtra("posts", String.valueOf(response.body().getDataUser().getTotalPosts()));
                    intent.putExtra("firstColor", _firstColor);
                    intent.putExtra("secondColor", _secondColor);
                    intent.putExtra("fontName", fontName.getValue());
//                    intent.putExtra("chatTheme",saveTextResponseList);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivityForResult(intent, 100);
                    Log.e("TAG", "onResponse: " + response.body().getDataUser().getTotalFollowers());
                }
            }

            @Override
            public void onFailure(Call<UserProfileRespone1> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void onVideoCallClick() {
        if (Helper.isNetworkAvailable(context)) {
            muteVideo = true;
            callCreateRoom(muteVideo);
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();

        }
    }

    public void onCallClick() {
        if (Helper.isNetworkAvailable(context)) {
            muteVideo = false;
            callCreateRoom(muteVideo);
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();

        }
    }

    public void onSearchClick() {

    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.w("tag", "onTextChanged " + s);
        emitTyping();
    }

    public void afterTextChanged(Editable s) {
        if (s.length() > 0)
            micro_gallery.set(false);
        else
            micro_gallery.set(true);

        Log.w("tag", "afterTextChanges " + s);
    }

    public void onSendButtonClick() {
        if (Helper.isNetworkAvailable(context)) {
            if (!(enteredMsg.getValue().trim().equalsIgnoreCase(""))) {
                emitSend("Message", "", false, enteredMsg.getValue(), null, "");
            }
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    public void onCameraClick() {
//        openImageClick();

        if (Helper.isNetworkAvailable(context)) {
            if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    || PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    || PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(context, new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO},
                        103);
            } else {
                Intent intent = new Intent(context, CameraActivity.class);
                intent.putExtra("whereFrom", "camera");
                context.startActivityForResult(intent, ParameterConstants.PICKER);
            }
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    public void onMicroPhoneClick() {
        customDialogBuilder.showAudioMikeDialog();
    }

    public void onGalleryClick() {

        if (Helper.isNetworkAvailable(context)) {
            if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
            } else {
                Intent intent = new Intent(context, MetMeGallery.class);
                intent.putParcelableArrayListExtra("messageList", messageList);
                intent.putExtra("whereFrom", "gallery");
                intent.putExtra("Mediatype", "gallery");
                intent.putExtra("chatHeadId", newChatListResponse.getChatHeadId());
                intent.putExtra("secondColor", _secondColor);
                context.startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
            }
        } else {
            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    public void emitTyping() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", newChatListResponse.getChats().get(0).getUserId());
            jsonObject.put("chatHeadId", newChatListResponse.getChatHeadId());
            mSocket.emit("typing", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void emitSend(String attachmentType, String fileURL, Boolean attachment, String lastMsg, JSONObject jsonObject, String thumbnailFileUrl) {
       /* if (chatBoatModel.getPreviousDate() != null && (chatBoatModel.getPreviousDate().equals("0") || funDateCheck(chatBoatModel.getPreviousDate(), getCurrentTime().split(" ")[0]))) {
            dateObjectSend(attachmentType, fileURL, false, enteredMsg.getValue(), jsonObject,thumbnailFileUrl);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendSocket(attachmentType, fileURL, attachment, lastMsg,jsonObject,thumbnailFileUrl);
        } else {
            sendSocket(attachmentType, fileURL, attachment, lastMsg, jsonObject,thumbnailFileUrl);
        }*/
        sendSocket(attachmentType, fileURL, attachment, lastMsg, jsonObject, thumbnailFileUrl);
    }

    public void sendSocket(String attachmentType, String fileURL, Boolean attachment, String lastMsg, JSONObject JsonData, String thumbnailFileUrl) {
        JSONObject jsonObject = new JSONObject();
        String msg = lastMsg;
        if (attachmentType.equals("Message")) {
            msg = AESHelper.encrypt(msg, newChatListResponse.getChatHeadId());
            if (msg == null || msg.equals("")) {
                msg = lastMsg;
            }
        }
        try {
            jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID));
            jsonObject.put("toUserId", newChatListResponse.getId());
            jsonObject.put("chatHeadId", newChatListResponse.getChatHeadId());
            jsonObject.put("msg", msg);
            jsonObject.put("lastMsg", msg);
            jsonObject.put("isGroup", false);
            jsonObject.put("attachment", attachment);
            jsonObject.put("attachmentType", attachmentType);
            jsonObject.put("fileURL", fileURL);
            jsonObject.put("thumbnail", thumbnailFileUrl);
            jsonObject.put("sendTime", getCurrentTime());
            jsonObject.put("jsonData", JsonData);
            jsonObject.put("toReplyId", chatBoatModel.getPreviousMsg());
            mSocket.emit("send", jsonObject);
            chatBoatModel.setPreviousMsg("");
            enteredMsg.setValue("");
            reply_layout.set(false);
            activityChatBoatBinding.replyLayout.setVisibility(View.GONE);
            removeSelectedMessage();
            messageAdapter.notifyDataSetChanged();
            context.showToolbar(messageAdapter.getSelectedMessages());
            Log.e("onSentClick", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void dateObjectSend(String attachmentType, String fileURL, Boolean attachment, String lastMsg, JSONObject jsonData, String thumbnailFileUrl) {
        JSONObject jsonObject = new JSONObject();
        try {
            String date = getCurrentTime().split(" ")[0];
          /*  DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d-MMM-yyyy");
            LocalDate  d1 = LocalDate.parse(date, dateFormatter);*/
            jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID));
            jsonObject.put("toUserId", newChatListResponse.getId());
            jsonObject.put("chatHeadId", newChatListResponse.getChatHeadId());
            jsonObject.put("msg", date);
            jsonObject.put("lastMsg", "");
            jsonObject.put("isGroup", false);
            jsonObject.put("attachment", false);
            jsonObject.put("attachmentType", "Date");
            jsonObject.put("fileURL", "");
            jsonObject.put("sendTime", getCurrentTime());
            jsonObject.put("toReplyId", "");
            mSocket.emit("send", jsonObject);
          /*  new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            sendSocket(attachmentType, fileURL, attachment, lastMsg,jsonData);
                        }
                    });
                }
            }).start();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean funDateCheck(String previousDate, String currentDate) {
        Boolean before = false;
        try {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date mPreviousDate = sdf.parse(previousDate);
            Date mCurrentDate = sdf.parse(currentDate);
            before = (mPreviousDate.before(mCurrentDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return before;
    }

    public String getCurrentTime() {

    /*    DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        Date date = new Date();
        String time = dateFormat.format(date);
        System.out.println(time);*/
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return String.valueOf(timestamp);

    }


    public void callCreateRoom(boolean muteVideo) {

        if (muteVideo) {
            if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.RECORD_AUDIO}, 101);
            } else {
                apiCall(muteVideo);
            }
        } else {
            if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    || PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 101);
            } else {
                apiCall(muteVideo);
            }
        }


    }

    private void funJitsiConfig(CreateRoomResponseModel response, boolean video) {
        // Initialize default options for Jitsi Meet conferences.
        URL serverURL;
        try {
            // When using JaaS, replace "https://meet.jit.si" with the proper serverURL
             serverURL = new URL("https://meet.jit.si");
          //  serverURL = new URL("https://jistinew.meest4bharat.net/");
//            serverURL = new URL("http://192.168.100.93:8081/");

        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }

        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        JitsiMeetConferenceOptions options
                = new JitsiMeetConferenceOptions.Builder()
                .setRoom(response.getData().getCallRoomId())
                // Settings for audio and video
                .setAudioMuted(false)
                .setVideoMuted(video)
                // .setAudioOnly(false)
                .build();
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        JitsiMeetActivity.launch(context, options);

    }

    protected boolean permissionsAvailable(String[] permissions) {
        boolean granted = true;
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                break;
            }
        }
        return granted;
    }

    void openImageClick() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        try {
            imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            context.startActivityForResult(intent, CAMERA_REQUEST);
        } catch (ActivityNotFoundException e) {
            Utilss.showToast(context, e.getMessage(), R.color.red);
        }
    }

    public void deleteChatMessage(String list) {
        xToken = SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", xToken);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<JsonObject> call = webApi.deleteChat(header, list);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    try {
                        JsonObject data = response.body();
                        Log.e("deletemessage", data.getAsString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("ppppppp");
            }
        });
    }

    void apiCall(boolean muteVideo) {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

        CreateRoomRequestModel createRoomRequestModel = new CreateRoomRequestModel();
        createRoomRequestModel.setRecipientId(newChatListResponse.getId());
        createRoomRequestModel.setGroup(false);
        createRoomRequestModel.setVideo(muteVideo);
        retrofit2.Call<CreateRoomResponseModel> apiCall = webApi.createRoom(header, createRoomRequestModel);
        apiCall.enqueue(new Callback<CreateRoomResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<CreateRoomResponseModel> call, Response<CreateRoomResponseModel> response) {
                try {
                    if (response.code() == 200) {
                        if (response.body() != null && response.body().getData() != null) {
                         //   funJitsiConfig(response.body(), ChatBoatViewModel.this.muteVideo);
                            // Build options object for joining the conference. The SDK will merge the default
                            // one we set earlier and this one when joining.
                            Intent intent = new  Intent(context, JitsiCallActivity.class);
                            intent.putExtra("serverURL",response.body().getData().getUrl());
                            intent.putExtra("jwtToken",response.body().getData().getJitsiToken());
                            intent.putExtra("callRoomId",response.body().getData().getCallRoomId());
                            intent.putExtra("isVideo",response.body().getData().getVideo());
                            intent.putExtra("isAudio",true);
                            intent.putExtra("name", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.F_NAME)+" "+SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.L_NAME));
                            intent.putExtra("roomId",response.body().getData().getId());
                            intent.putExtra("toUserId",newChatListResponse.getId());
                            context.startActivity(intent);
                        }
                    } else {
                        Utilss.showErrorToast(context);
                        Log.e("TAG", "callCreateRoom=" + response.body().getCode());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<CreateRoomResponseModel> call, Throwable t) {
                Log.e("TAG", "callCreateRoom=" + t.getMessage());
            }
        });
    }

    public void getChatTheme() {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
        body.put("toUserId", newChatListResponse.getId());
//        body.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<SaveTextAppearanceModel> call = webApi.getChatTheme(header, body);
        call.enqueue(new Callback<SaveTextAppearanceModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<SaveTextAppearanceModel> call, Response<SaveTextAppearanceModel> response) {
                if (response.code() == 200 && response.body().isSuccess() && response.body().getData() != null) {
//                    appearanceData.setValue(true);
//                    saveTextMutable.postValue(response.body().getData());
                    setUserTheme(response.body().getData());
                } else {
                    messageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SaveTextAppearanceModel> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<SaveTextAppearanceModel.Data> getLiveData() {
        return saveTextMutable;
    }

    private void changeHeaderColor(String firstColor, String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(0f);
            activityChatBoatBinding.MainContainer.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{context.getColor(R.color.first_color), context.getColor(R.color.first_color)});
            gd.setCornerRadius(0f);
            activityChatBoatBinding.MainContainer.setBackground(gd);
        }
    }

    public void removeSelectedMessage() {
        for (int i = 0; i < messageAdapter.mMessages.size(); i++) {
            if (messageAdapter.mMessages.get(i).isSelected()) {
                messageAdapter.mMessages.remove(i);
                i--;
            }
        }
    }

    public void removeDuplicateDate() {
        try {
            for (int i = 0; i < messageAdapter.mMessages.size() - 1; i++) {
                if (messageAdapter.mMessages.get(i).getmAttachmentType().equals("Date") && messageAdapter.mMessages.get(i + 1).getmAttachmentType().equals("Date")) {
                    messageAdapter.mMessages.remove(i);
                    i--;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUserTheme(SaveTextAppearanceModel.Data saveModel) {
        if (saveModel.getFontName() != null && (saveModel.getFontName().length() > 0 || !saveModel.getFontName().equals(""))) {
            if (saveModel.getFontData() != null) {
                Global.setFontSize(saveModel.getFontData());
                activityChatBoatBinding.tvTitle.setTextSize(Float.parseFloat("" + saveModel.getFontData().getSize2()));
            }
            downloadFont(saveModel);
        }
        if (saveModel.getGradient() != null && (saveModel.getGradient().equalsIgnoreCase("false") || saveModel.getGradient().equals(""))) {
            _firstColor = saveModel.getFirstColor();
            _secondColor = saveModel.getSecondColor();
            changeHeaderColor(_firstColor, _secondColor);
            SharedPrefreances.setSharedPreferenceString(context, newChatListResponse.getChatHeadId() + "_color", _secondColor);
            saveTextResponseModel.setData(saveModel);
            if (saveModel.getSecondColor() != null && !saveModel.getSecondColor().equals("")) {
//                        animatedColor = new AnimatedColor(Color.parseColor(saveModel.getFirstColor()), Color.parseColor(saveModel.getSecondColor()));
                activityChatBoatBinding.fabLayoutCamera.setColorFilter(Color.parseColor(saveModel.getSecondColor()));
                activityChatBoatBinding.micro.setColorFilter(Color.parseColor(saveModel.getSecondColor()));
                activityChatBoatBinding.gallery.setColorFilter(Color.parseColor(saveModel.getSecondColor()));
                activityChatBoatBinding.send.setColorFilter(Color.parseColor(saveModel.getSecondColor()));
            } else {
                activityChatBoatBinding.fabLayoutCamera.setColorFilter(R.color.first_color);
                activityChatBoatBinding.micro.setColorFilter(R.color.first_color);
                activityChatBoatBinding.gallery.setColorFilter(R.color.first_color);
                activityChatBoatBinding.send.setColorFilter(R.color.first_color);
            }
            if (saveModel.getBgColor() != null && !saveModel.getBgColor().equals("")) {
                activityChatBoatBinding.roundLayout.setBackgroundColor(Color.parseColor(saveModel.getBgColor()));
            } else {
                if (saveModel.getWallPaper() != null && !saveModel.getWallPaper().equals("")) {
                    wallPaper_name = new File(saveModel.getWallPaper()).getName();
                    filePath = context.getExternalCacheDir() + "/" + newChatListResponse.getId() + "_" + wallPaper_name;
                    File temp = new File(filePath);
                    if (temp.exists()) {
                        displayImage(filePath);
                    } else {
                        new DownloadFileFromURL().execute(saveModel.getWallPaper());
//                        downloadImageOrVideo(saveModel.getWallPaper());
                    }
                } else {
                    if (!saveModel.getBgFirstColor().equals("") && !saveModel.getBgSecondColor().equals("") && saveModel.getBgFirstColor() != null && saveModel.getBgSecondColor() != null) {
                        activityChatBoatBinding.roundLayout.setBackground(null);
                        GradientDrawable gd1 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(saveModel.getBgFirstColor()), Color.parseColor(saveModel.getBgSecondColor())});
                        gd1.setCornerRadius(0f);
                        activityChatBoatBinding.roundLayout.setBackground(gd1);
                    } else {
                        activityChatBoatBinding.roundLayout.setBackgroundColor(Color.parseColor("#f4f4f4"));
                    }
                }
            }
        } else {
//              setupListeners();
//              if (data.getFirstColor() != null && data.getFirstColor() != "" && data.getFirstColor().length() > 0) {
//                  animatedColor = new AnimatedColor(Color.parseColor(data.getFirstColor()), Color.parseColor(data.getSecondColor()));
//                  activityChatBoatBinding.mMessagesView.smoothScrollBy(0, 1);
//              }
        }
        messageAdapter.notifyDataSetChanged();
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            getResources().getString(R.string.please_wait)
            System.out.println("Starting download");
            pDialog.setMessage(context.getResources().getString(R.string.please_wait));
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(true);
            pDialog.setMax(100);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            String state;
            state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {

                    directoryName = new File(String.valueOf(context.getExternalCacheDir()));
                    if (!directoryName.exists()) {
                        directoryName.mkdirs();
                    }
                    fileName = new File(directoryName, newChatListResponse.getId() + "_" + wallPaper_name);
                    System.out.println("Downloading");
                    URL url = new URL(f_url[0]);

                    URLConnection connection = url.openConnection();
                    int fileLength = connection.getContentLength();
                    connection.connect();
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    OutputStream output = new FileOutputStream(fileName);
                    byte data[] = new byte[1024];
                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // writing data to file
                        publishProgress(String.valueOf(total * 100 / fileLength));
                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    input.close();

                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            } else {
                Toast.makeText(context, "Folder Not created", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            Uri uri = Uri.fromFile(fileName);
            displayImage(String.valueOf(uri));
            Log.e("TAG", "onPostExecute: " + file_url);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pDialog.setProgress(Integer.parseInt(values[0]));
        }
    }

    public void downloadFont(SaveTextAppearanceModel.Data data) {
        try {
            filePath = FilenameUtils.getName(Uri.parse(data.getFontName()).getPath());

            File fontFile = new File(context.getExternalCacheDir() + "/" + filePath);
            if (!fontFile.exists()) {
                PRDownloader.download(data.getFontName(), context.getExternalCacheDir().getPath(), "/" + filePath)
                        .build()
                        .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                            @Override
                            public void onStartOrResume() {

                            }
                        })
                        .start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                fontName.setValue(context.getExternalCacheDir() + "/" + filePath);
                                SharedPrefreances.setSharedPreferenceString(context, newChatListResponse.getChatHeadId(), fontName.getValue());
                                Log.e("font------------boat", "font:" + SharedPrefreances.getSharedPreferenceString(context, newChatListResponse.getChatHeadId()));
//                                Message.setFontType(fontName);
                            }

                            @Override
                            public void onError(Error error) {
                                System.out.println("Font Error" + error.getServerErrorMessage());
                            }
                        });
            } else {
                fontName.setValue(context.getExternalCacheDir() + "/" + filePath);
                SharedPrefreances.setSharedPreferenceString(context, newChatListResponse.getChatHeadId(), fontName.getValue());
            }

            Typeface face = Typeface.createFromFile(fontName.getValue());
            activityChatBoatBinding.tvTitle.setTypeface(Typeface.createFromFile(fontName.getValue()));
//            Message.setFontType(fontName.getValue());
            messageAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayImage(String filePath) {
        Log.e("TAG", "displayImage: " + filePath);
        Glide.with(context)
                .asBitmap()
                .load(filePath)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        activityChatBoatBinding.roundLayout.setBackground(new BitmapDrawable(context.getResources(), resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }
}
