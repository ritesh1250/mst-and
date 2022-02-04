package com.meest.metme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.google.gson.Gson;
import com.meest.BR;
import com.meest.MainActivity;
import com.meest.Meeast;
import com.meest.R;
import com.meest.responses.BulkChatDeleteResponse;
import com.meest.responses.ChatSettingResponse;
import com.meest.responses.Message;
import com.meest.responses.ProfilePhotoUploadResponse;
import com.meest.responses.ThumbnailResponse;
import com.meest.databinding.ActivityChatBoatBinding;
import com.meest.meestbhart.splash.SplashScreenActivity;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.metme.GradientRecycler.util.AnimatedColor;
import com.meest.metme.GradientRecycler.util.Extentions;
import com.meest.metme.GradientRecycler.util.SpacesItemDecoration;
import com.meest.metme.ViewHolder.ChatAudioViewHolder;
import com.meest.metme.ViewHolder.ChatImageViewHolder;
import com.meest.metme.ViewHolder.ChatOtherImageViewHolder;
import com.meest.metme.ViewHolder.ChatOtherVideoViewHolder;
import com.meest.metme.ViewHolder.ChatReplyViewHolder;
import com.meest.metme.ViewHolder.ChatVideoViewHolder;
import com.meest.metme.ViewHolder.ChatViewHolder;
import com.meest.metme.ViewHolder.DateLayoutViewHolder;
import com.meest.metme.adapter.MessageAdapter;
import com.meest.metme.encription.AESHelper;
import com.meest.metme.model.ChatBoatModel;
import com.meest.metme.model.SaveTextAppearanceModel;
import com.meest.metme.model.chat.NewChatListResponse;
import com.meest.metme.model.gallery.GalleryPhotoModel;
import com.meest.metme.swipetoreply.ISwipeControllerActions;
import com.meest.metme.swipetoreply.SwipeControllerNew;
import com.meest.metme.viewmodels.ChatBoatViewModel;
import com.meest.utils.Helper;
import com.meest.utils.IntentWrapper;
import com.meest.utils.ParameterConstants;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.TranscoderOptions;
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategy;
import com.otaliastudios.transcoder.strategy.size.AtMostResizer;
import com.otaliastudios.transcoder.strategy.size.FractionResizer;

import org.apache.commons.io.FilenameUtils;
import org.greenrobot.eventbus.Subscribe;
import org.jitsi.meet.sdk.JitsiMeetView;
import org.jitsi.meet.sdk.JitsiMeetViewListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class ChatBoatActivity extends RecordingBaseActivity{
    private static final String TAG = "CHATBOAT";
    private static final String IMAGE_DIRECTORY = "/metme";
    public static Activity context;
    private static String CHANNEL_ID = "chat_upload";
    final int ID_INDETERMINATE_SERVICE = 9;
    final int ID_NOTIFICATION_COMPLETE = 4;
    public String wallPaper_name;
    public ObservableBoolean reply_layout = new ObservableBoolean(false);
    ActivityChatBoatBinding activityChatBoatBinding;
    //    MessageAdapter messageAdapter;
    ArrayList<Message> mMessages = new ArrayList<>();
    ArrayList<HashMap<String, String>> userList = new ArrayList<>();
    CustomDialogBuilder customDialogBuilder;
    NewChatListResponse newChatListResponse;
    ArrayList<HashMap<String, String>> messageHistory = new ArrayList<>();
    SaveTextAppearanceModel saveTextResponseList = new SaveTextAppearanceModel();
    Socket mSocket;
    RecyclerView mMessagesView;
    boolean userScrolled;
    int scrollOutitems;
    int pageNumber = 1;
    int limit = 30;
    LinearLayout mainMenu;
    LinearLayout ll_layout;
    ImageView img_copy;
    Intent intent;
    int LAUNCH_SECOND_ACTIVITY = 1231;
    ArrayList<GalleryPhotoModel> selectedItem;
    List<Message> selectedMessage = new ArrayList<>();
    ClipboardManager clipboardManager;
    ChatBoatModel chatBoatModel;
    NotificationCompat.Builder notification = null;
    NotificationManagerCompat notificationManager = null;
    boolean isRecStart = false, fromNotification = false;
    long startTime = 0L;
    long endTime = 0L;
    Handler audioTimeHandler = new Handler();
    ChatBoatViewModel viewModel;
    LinearLayoutManager layoutManager;
    int index, top;
    String previousDate = "";
    Runnable timeRun = new Runnable() {
        @Override
        public void run() {
            if (isRecStart) {
                audioTimeHandler.postDelayed(this, 50);
            }

            endTime = SystemClock.elapsedRealtime();
            long ellapsedTime = endTime - startTime;
            @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(ellapsedTime), TimeUnit.MILLISECONDS.toSeconds(ellapsedTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ellapsedTime)));
        }
    };

    Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatBoatActivity.this.runOnUiThread(new Runnable() {
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
                        jsonObject.put("userId", newChatListResponse.getChats().get(0).getUserId());
                        jsonObject.put("name", newChatListResponse.getFirstName() + " " + newChatListResponse.getLastName());
                        jsonObject.put("isGroup", false);
                        mSocket.emit("createSession", jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (!mMessages.get(viewHolder.getAdapterPosition()).getmAttachmentType().equalsIgnoreCase("Date")) {
                showQuotedMessage(mMessages.get(viewHolder.getAdapterPosition()));
                reply_layout.set(true);
            }
        }
    };

    SpacesItemDecoration spacesItemDecoration;
    AnimatedColor animatedColor;
    private String fontName = "";
    private String filePath;
    private File directoryName, fileName;
    private ProgressDialog pDialog;

    private Emitter.Listener sent = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatBoatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //                   Log.e("SENT DATA:", args.toString());
                        JSONObject data = (JSONObject) args[0];
                        String username = data.getString("msg");
                        String new_str = "{" + "\"usernew\":" + "[" + username + "]" + "}";
                        Log.e("SENT RECEIVED DATA:", new_str);

                        JSONObject jObj = new JSONObject(new_str);
                        JSONArray jsonArry = jObj.getJSONArray("usernew");
                        Log.w("onNewMessage-wow", jsonArry + "");
                        JSONObject obj = jsonArry.getJSONObject(0);
                        dataMessage("1", obj);
                        scrollToBottom();
                        readMessageIfPhoneScreenOPEN();
                    } catch (JSONException e) {
                        Log.e("SENT EXCEPTION", e.getMessage());
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener session = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatBoatActivity.this.runOnUiThread(new Runnable() {
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
    private Emitter.Listener chat_history = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatBoatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        userList.clear();
                        String username = String.valueOf(args[0]);
                        String new_str = "{" + "\"usernew\":" + username + "}";
                        System.out.println("ppppppppppppccccccccccc  "+new_str);
                        JSONObject jObj = new JSONObject(new_str);
                        JSONArray jsonArry = jObj.getJSONArray("usernew");
                        for (int i = 0; i < jsonArry.length(); i++) {
                            HashMap<String, String> user = new HashMap<>();
                            JSONObject obj = jsonArry.getJSONObject(i);
                           /* if (mMessages.size() == 0) {
                                addMessage("0", obj.getString("sendTime").split(" ")[0], "", "", "false", "", "", "", "Date", "", "");
                            }*/
                            if (obj.length() > 1) {
                                String sendTime = obj.getString("sendTime").split(" ")[0];
                                if (obj.getString("sendTime")=="null")
                                    sendTime = obj.getString("messageDate").split("T")[0];
                                if (previousDate.equals("") || funDateCheck(previousDate, sendTime)) {
                                    previousDate = sendTime;
                                    user.put("msg", previousDate);
                                    user.put("id", "");
                                    user.put("sent", "");
                                    user.put("read", "");
                                    user.put("createdAt", "");
                                    user.put("attachment", "");
                                    user.put("fileURL", "");
                                    user.put("status", "");
                                    user.put("attachmentType", "Date");
                                    user.put("sendTime", obj.getString("sendTime"));
                                    user.put("jsonData", "");
                                    user.put("thumbnail", "");
                                    userList.add(user);
                                    user = new HashMap<>();
                                    user = funGetUserData(user, obj);

                                } else {
                                    user = funGetUserData(user, obj);
                                }
                            }
                            userList.add(user);
                        }

                        for (int i = 0; i < userList.size(); i++) {

                            if (userList.get(i).get("sent").equalsIgnoreCase("0")) {
                                String msg = userList.get(i).get("msg");
                                String read = userList.get(i).get("read");
                                String created = userList.get(i).get("createdAt");
                                String attachment = userList.get(i).get("attachment");
                                String fileurl = userList.get(i).get("fileURL");
                                String id = userList.get(i).get("id");
                                String status = userList.get(i).get("status");
                                String attachmentype = userList.get(i).get("attachmentType");
                                String jsonData = userList.get(i).get("jsonData");
                                String replyChats = userList.get(i).get("replyChats");
                                String sendTime = userList.get(i).get("sendTime");
                                String thumbnailURL = userList.get(i).get("thumbnail");
//                                String messageDate = userList.get(i).get("messageDate");
                                addMessage("1", msg, read, created, attachment, fileurl, id, status, attachmentype, jsonData, replyChats, thumbnailURL, sendTime.split(" ")[0]);

                                Log.e("TAg", "maintaineduser=" + userList.get(i).get("attachment"));
                                Log.e("TAg", "fileurlYour=" + userList.get(i).get("fileURL"));
                            } else {
                                String msg = userList.get(i).get("msg");
                                String read = userList.get(i).get("read");
                                String created = userList.get(i).get("createdAt");
                                String attachment = userList.get(i).get("attachment");
                                String fileurl = userList.get(i).get("fileURL");
                                String id = userList.get(i).get("id");
                                String status = userList.get(i).get("status");
                                String attachmentype = userList.get(i).get("attachmentType");
                                String jsonData = userList.get(i).get("jsonData");
                                String replyChats = userList.get(i).get("replyChats");
                                String sendTime = userList.get(i).get("sendTime");
                                String thumbnailURL = userList.get(i).get("thumbnail");
//                                String messageDate = userList.get(i).get("messageDate");
                                addMessage("0", msg, read, created, attachment, fileurl, id, status, attachmentype, jsonData, replyChats, thumbnailURL, sendTime.split(" ")[0]);
                            }
                        }

                        if (jsonArry.length() > 0) {
                            userScrolled = true;
                            //  chatBoatModel.setPreviousDate(changeDateFormate(jsonArry.getJSONObject(jsonArry.length() - 1).getString("sendTime")));
                        } else {
                            userScrolled = false;
                            // chatBoatModel.setPreviousDate("0");
                        }

                        scrollToBottom();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TAG_UserHistory", e.getMessage());
                        return;
                    }
                }
            });
        }
    };
    private Emitter.Listener successFullySend = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatBoatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        JSONObject messageJSON = new JSONObject(data.getString("msg"));
                        Log.e("successFullySend", "+++" + messageJSON.getString("msg"));
                        dataMessage("0", messageJSON);
                        scrollToBottom();
                       // readMessageIfPhoneScreenOPEN();
                    } catch (Exception e) {
                        Log.e("onNewMessage", e.getMessage());
                        return;
                    }
                }
            });
        }
    };
    private Emitter.Listener messageRead = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatBoatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        if (data.getString("chatHeadId").equals(newChatListResponse.getChatHeadId())) {
                            Log.e(TAG, "run: shubham" + data.getString("readStatus"));
                            readMessagesByReciever(data);
                        }
                        Log.e("TAg", "maintaineddataread=" + data + "======" + SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
                    } catch (Exception e) {
                        Log.e("TAGMessageRead", e.getMessage());
                        return;
                    }
                }
            });
        }
    };
    private Emitter.Listener onMsgHistorywithPages = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatBoatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        userList.clear();
                        String username = String.valueOf(args[0]);
                        String new_str = "{" + "\"usernew\":" + username + "}";
                        Log.w("onHistory", username);
                        JSONObject jObj = new JSONObject(new_str);
                        JSONArray jsonArry = jObj.getJSONArray("usernew");

                        for (int i = 0; i < jsonArry.length(); i++) {
                            HashMap<String, String> user = new HashMap<>();
                            JSONObject obj = jsonArry.getJSONObject(i);
                            if (obj.length() > 1) {
                                String sendTime = obj.getString("sendTime").split(" ")[0];
                                if (funDateCheck(previousDate, sendTime)) {
                                    previousDate = sendTime;
                                    user.put("msg", previousDate);
                                    user.put("id", "");
                                    user.put("sent", "");
                                    user.put("read", "");
                                    user.put("createdAt", "");
                                    user.put("attachment", "");
                                    user.put("fileURL", "");
                                    user.put("status", "");
                                    user.put("attachmentType", "Date");
                                    user.put("sendTime", obj.getString("sendTime"));
                                    user.put("jsonData", "");
                                    userList.add(user);
                                    user = new HashMap<>();
                                    user = funGetUserData(user, obj);

                                } else {
                                    user = funGetUserData(user, obj);
                                }
                            }
                            userList.add(user);
                        }


                        for (int i = 0; i < userList.size(); i++) {

                            if (userList.get(i).get("sent").equalsIgnoreCase("0")) {
                                String msg = userList.get(i).get("msg");
                                String read = userList.get(i).get("read");
                                String created = userList.get(i).get("createdAt");
                                String attachment = userList.get(i).get("attachment");
                                String fileurl = userList.get(i).get("fileURL");
                                String id = userList.get(i).get("id");
                                String status = userList.get(i).get("status");
                                String attachmentype = userList.get(i).get("attachmentType");
                                String jsonData = userList.get(i).get("jsonData");
                                String replyChats = userList.get(i).get("replyChats");
                                String thumbnailURL = userList.get(i).get("thumbnail");
                                String sendTime = userList.get(i).get("sendTime");
                                addMessageNoScroll(0, "1", msg, read, created, attachment, fileurl, id, status, attachmentype, jsonData, replyChats, thumbnailURL, sendTime.split(" ")[0]);
                            } else {
                                String msg = userList.get(i).get("msg");
                                String read = userList.get(i).get("read");
                                String created = userList.get(i).get("createdAt");
                                String attachment = userList.get(i).get("attachment");
                                String fileurl = userList.get(i).get("fileURL");
                                String id = userList.get(i).get("id");
                                String status = userList.get(i).get("status");
                                String attachmentype = userList.get(i).get("attachmentType");
                                String jsonData = userList.get(i).get("jsonData");
                                String replyChats = userList.get(i).get("replyChats");
                                String thumbnailURL = userList.get(i).get("thumbnail");
                                String sendTime = userList.get(i).get("sendTime");
                                addMessageNoScroll(0, "0", msg, read, created, attachment, fileurl, id, status, attachmentype, jsonData, replyChats, thumbnailURL, sendTime.split(" ")[0]);
                            }
                        }

                        activityChatBoatBinding.loadmoremsg.setVisibility(View.GONE);

                        scrollToTop();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TAG_UserHistory", e.getMessage());
                        activityChatBoatBinding.loadmoremsg.setVisibility(View.GONE);
                    }


                }
            });
        }
    };
    private String firstColor;
    private String secondColor;
    private LinearLayoutManager linearLayoutManager;
    private int visiblePosition;
    private int lastvisiblePosition;
    private int firstCompletelyVisiblePosition;
    private int lastCompletelyVisiblePosition;
    private DownloadManager downloadManager;
    private long downloadID;
    private File dir;
    private File originalPath;

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
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

    private HashMap<String, String> funGetUserData(HashMap<String, String> user, JSONObject obj) throws JSONException {
        user.put("msg", obj.getString("msg"));
        user.put("id", obj.getString("id"));
        user.put("sent", obj.getString("sender"));
        user.put("read", obj.getString("readStatus"));
        user.put("createdAt", obj.getString("createdAt"));
        user.put("attachment", obj.getString("attachment"));
        user.put("fileURL", obj.getString("fileURL"));
        user.put("status", obj.getString("status"));
        user.put("attachmentType", obj.getString("attachmentType"));
        user.put("jsonData", obj.getString("jsonData"));
        user.put("replyChats", obj.getString("replyChats"));
        user.put("sendTime", obj.getString("sendTime"));
        user.put("thumbnail", obj.getString("thumbnail"));
        user.put("messageDate", "");
        return user;
    }

    private String changeDateFormate(String previousDate) {
        String s[] = previousDate.split(" ");
        return s[0];
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LAUNCH_SECOND_ACTIVITY) {
                selectedItem = data.getParcelableArrayListExtra("result");
                if (selectedItem.get(0).isVideo()) {
                    videoRecursion();
                } else {
                    imageRecursion();
                }
            }
            if (requestCode == ParameterConstants.PICKER) {
                if (data.getParcelableArrayListExtra("result") != null && data.getParcelableArrayListExtra("result").size() > 0) {
                    selectedItem = data.getParcelableArrayListExtra("result");
                    if (selectedItem.get(0).isVideo()) {
                        videoRecursion();
                    } else {
                        imageRecursion();
                    }
                } else {
                    GalleryPhotoModel model = new GalleryPhotoModel();
                    model.setImageText(data.getStringExtra("name"));
                    model.setPicturePath(data.getStringExtra("path"));
                    model.setVideo(data.getBooleanExtra("isVideo", false));
                    model.setType("Image");
                    if (selectedItem == null) {
                        selectedItem = new ArrayList<>();
                    }
                    selectedItem.add(model);
                    if (selectedItem.get(0).isVideo()) {
                        videoRecursion();
                    } else {
                        imageRecursion();
                    }
                }
            }
            if (requestCode == ParameterConstants.FINISH) {
                finish();
            }
            if (requestCode == 100) {
                viewModel.getChatTheme();
                Toast.makeText(context, "Get Chat Theme", Toast.LENGTH_SHORT).show();
            } else {
                showBar();

            }

        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        }
    }

    public void showBar() {
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("MetMe")
                .setContentText("Sending...")
                .setAutoCancel(false)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        notification.setProgress(0, 0, true);
        notificationManager.notify(ID_INDETERMINATE_SERVICE, notification.build());

    }

    public void showBarComplete(String msg) {
        notificationManager.cancel(ID_INDETERMINATE_SERVICE);
        notification.setContentText(msg)
                .setProgress(0, 0, false);
        notificationManager.notify(ID_NOTIFICATION_COMPLETE, notification.build());
    }

    void videoRecursion() {

        String selectedPath = selectedItem.get(0).getPicturePath();
        String videoThumbnail = getThumbnailVideoFile(new File(selectedItem.get(0).getPicturePath()));
        Log.e(TAG, "videoRecursion: " + selectedPath);
        Log.e(TAG, "videoThumbnails: " + videoThumbnail);
        try {
            saveVideo(selectedPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File videoFile = new File(selectedPath);
        long fileSizeInBytes = videoFile.length();
        long fileSizeInKB = fileSizeInBytes / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;
        if (fileSizeInMB > 3) {
            String compressedVideo = Constant.createOutputPath(ChatBoatActivity.this, selectedPath.substring(selectedPath.lastIndexOf(".")));
            videoCompress(selectedPath, compressedVideo, videoThumbnail);
        } else {
//            funVideoSendToServer(videoFile);
            uploadVideoWithThumbnail(videoFile, new File(videoThumbnail));
        }
    }

    public void saveVideo(String selectedPath) throws IOException {
        int count;
        directoryName = new File(String.valueOf(getExternalCacheDir()));

        if (!directoryName.exists()) {
            directoryName.mkdirs();
        }

        fileName = new File(directoryName, "MetMe_" + newChatListResponse.getFirstName() + "_" + newChatListResponse.getUsername() + ".mp4");
        System.out.println("Downloading");
        URL url = new URL(selectedPath);

        URLConnection conection = url.openConnection();

        conection.connect();

        int lenghtOfFile = conection.getContentLength();
        // input stream to read file - with 8k buffe
        InputStream input = new BufferedInputStream(url.openStream(), 8192);
        // Output stream to write file
        OutputStream output = new FileOutputStream(fileName);
        byte data[] = new byte[1024];
        long total = 0;
        while ((count = input.read(data)) != -1) {
            total += count;
            // writing data to file
            output.write(data, 0, count);
        }
        // flushing output
        output.flush();
        // closing streams
        output.close();

        input.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    void imageRecursion() {

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(new File(selectedItem.get(0).getPicturePath())));
            String path = saveImage(bitmap);
//                    Log.e(TAG, "maintainednewpaths=" + path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            InputStream originalFile = getContentResolver().openInputStream(Uri.fromFile(new File(selectedItem.get(0).getPicturePath())));
            InputStream thumbnailFile = getContentResolver().openInputStream(Uri.fromFile(new File(getThumbnailFile(new File(selectedItem.get(0).getPicturePath())))));
//            upload(getBytes(originalFile));
            uploadImageWithThumbnail(getBytes(originalFile), getBytes(thumbnailFile));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadImageWithThumbnail(byte[] originalImage, byte[] thumbnail) {
        RequestBody requestOriginalFile = RequestBody.create(MediaType.parse("image/jpeg"), originalImage);
        MultipartBody.Part originalBody = MultipartBody.Part.createFormData("file", "image.jpg", requestOriginalFile);
        RequestBody requestThumbnailFile = RequestBody.create(MediaType.parse("image/jpeg"), thumbnail);
        MultipartBody.Part thumbnailBody = MultipartBody.Part.createFormData("thumbnails", "imageThumbnail.jpg", requestThumbnailFile);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<ThumbnailResponse> call = webApi.getThumbnail(map, originalBody, thumbnailBody);

        call.enqueue(new Callback<ThumbnailResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onResponse(Call<ThumbnailResponse> call, Response<ThumbnailResponse> response) {
                try {
                    // image.setVisibility(View.GONE);
                    if (response.body().getCode() == 1) {
                        String msg = "";
                        if (selectedItem.get(0).getImageText() == null || selectedItem.get(0).getImageText().isEmpty()) {
                            msg = "";
                        } else {
                            msg = selectedItem.get(0).getImageText();
                        }
                        String originalFileUrl = AESHelper.encrypt(response.body().getData().getFile(), newChatListResponse.getChatHeadId());
                        String thumbnailFileUrl = AESHelper.encrypt(response.body().getData().getThumbnail(), newChatListResponse.getChatHeadId());
                        viewModel.emitSend("Image", originalFileUrl, true, msg, null, thumbnailFileUrl);
                        if (selectedItem.size() > 0) {
                            selectedItem.remove(0);
                            if (selectedItem.size() == 0) {
                                showBarComplete("Completed");
                            }
                            if (selectedItem.get(0).isVideo()) {
                                videoRecursion();
                            } else {
                                imageRecursion();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ThumbnailResponse> call, Throwable t) {
                Utilss.showToast(getApplicationContext(), "SOME_ERROR", R.color.grey);
            }
        });
    }

    private void videoCompress(String input, String output, String videoThumbnail) {
        DefaultVideoStrategy strategy = new DefaultVideoStrategy.Builder()
                .addResizer(new FractionResizer(0.7F))
                .addResizer(new AtMostResizer(1000))
                .build();
        TranscoderOptions.Builder options = Transcoder.into(output);
        options.addDataSource(input);
        options.setVideoTrackStrategy(strategy);
        options.setListener(new TranscoderListener() {
            @Override
            public void onTranscodeProgress(double progress) {

//                showProgressDialog();
//                progressBinding.progressBar.publishProgress((float) progress);

                Log.e("TAG", "onTranscodeProgress: " + progress);
            }

            @Override
            public void onTranscodeCompleted(int successCode) {
                if (successCode == 0) {
//                    funVideoSendToServer(new File(output));
                    uploadVideoWithThumbnail(new File(output), new File(videoThumbnail));
                }
            }

            @Override
            public void onTranscodeCanceled() {
                Log.e("transcode", "onTranscodeCanceled: ");
            }

            @Override
            public void onTranscodeFailed(@NonNull Throwable exception) {
                Log.e("transcode", "onTranscodeCanceled: " + exception);
            }
        }).transcode();
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
        int buffSize = 1024;
        byte[] buff = new byte[buffSize];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }
        return byteBuff.toByteArray();
    }

    public void upload(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<ProfilePhotoUploadResponse> call = webApi.insert(map, body);

        call.enqueue(new Callback<ProfilePhotoUploadResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onResponse(Call<ProfilePhotoUploadResponse> call, Response<ProfilePhotoUploadResponse> response) {
                try {
                    // image.setVisibility(View.GONE);
                    if (response.body().getCode() == 1) {
                        String msg = "";
                        if (selectedItem.get(0).getImageText() == null || selectedItem.get(0).getImageText().isEmpty()) {
                            msg = "";
                        } else {
                            msg = selectedItem.get(0).getImageText();
                        }
                        String fileUrl = AESHelper.encrypt(response.body().getData().getUrl(), newChatListResponse.getChatHeadId());
                        viewModel.emitSend("Image", fileUrl, true, msg, null, "");
                        if (selectedItem.size() > 0) {
                            selectedItem.remove(0);
                            if (selectedItem.size() == 0) {
                                showBarComplete("Completed");
                            } else {
                                if (selectedItem.get(0).isVideo()) {
                                    videoRecursion();
                                } else {
                                    imageRecursion();
                                }
                            }
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

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Log.e(TAG, "oncreate: " + "true");
        activityChatBoatBinding = DataBindingUtil.setContentView(ChatBoatActivity.this, R.layout.activity_chat_boat);
        activityChatBoatBinding.setLifecycleOwner(this);
        pDialog = new ProgressDialog(ChatBoatActivity.this);
        customDialogBuilder = new CustomDialogBuilder(this);
        newChatListResponse = getIntent( ).getParcelableExtra("newChatListResponse");
        dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Meest");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManagerNew = getSystemService(NotificationManager.class);
            NotificationChannel priorityNotificationChannel = new NotificationChannel(CHANNEL_ID, "chat upload channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManagerNew.createNotificationChannel(mChannel);
            notificationManagerNew.createNotificationChannel(priorityNotificationChannel);
        }

        notificationManager = NotificationManagerCompat.from(ChatBoatActivity.this);

        if (newChatListResponse == null) {
            fromNotification = getIntent().getBooleanExtra("notification", false);
            newChatListResponse = new NewChatListResponse();
            newChatListResponse.setId(getIntent("user_id_to"));
            newChatListResponse.setUsername(getIntent("username"));
            newChatListResponse.setFirstName(getIntent("firstName"));
            newChatListResponse.setLastName(getIntent("lastName"));
            newChatListResponse.setDisplayPicture(getIntent("profilePicture"));
            newChatListResponse.setChatHeadId(getIntent("chatHeadId"));
            NewChatListResponse.Chat chat = new NewChatListResponse.Chat();
            chat.setUserId(getIntent("user_id_to"));
            List<NewChatListResponse.Chat> chatList = new ArrayList<>();
            chatList.add(chat);
            newChatListResponse.setChats(chatList);
        }

        Meeast app = (Meeast) getApplication();
        mSocket = app.getSocket();
        mSocket.on("connected", onConnect);
        mSocket.on("session", session);
        mSocket.on("sent", sent);
        mSocket.on("sent_ios", successFullySend);
        mSocket.on("chat_history", chat_history);
        mSocket.on("chat_history_pages", onMsgHistorywithPages);
        readMessageIfPhoneScreenOPEN();
        mSocket.on("read", messageRead);

        mSocket.connect();

        chatBoatModel = new ChatBoatModel();
        if (getIntent().getStringExtra("username") != null)
            chatBoatModel.setChatUserName(getIntent().getStringExtra("username"));
        else
            chatBoatModel.setChatUserName(newChatListResponse.getFirstName() + " " + newChatListResponse.getLastName());
        chatBoatModel.setLastSeen("Last Seen Today at 08:23 am");
        chatBoatModel.setChatProfileImage(newChatListResponse.getDisplayPicture());

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        activityChatBoatBinding.mMessagesView.setLayoutManager(layoutManager);
        viewModel = new ChatBoatViewModel(chatBoatModel, ChatBoatActivity.this, newChatListResponse, mSocket, mMessages, customDialogBuilder, reply_layout, activityChatBoatBinding);
        activityChatBoatBinding.setChatBoatViewModel(viewModel);
        activityChatBoatBinding.executePendingBindings();
        activityChatBoatBinding.setLifecycleOwner(this);
        viewModel.fontName.setValue(SharedPrefreances.getSharedPreferenceString(this, newChatListResponse.getChatHeadId()));
        viewModel.messageAdapter = new MessageAdapter(ChatBoatActivity.this, mMessages, newChatListResponse, mSocket, viewModel.saveTextResponseModel);
        activityChatBoatBinding.setMessageAdapter(viewModel.messageAdapter);
        GetChatSetting(newChatListResponse.getId(), newChatListResponse.getChatHeadId(), SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        viewModel.messageAdapter.setItemLongClick(new MessageAdapter.ItemLongClick() {
            @Override
            public void onLongClick(List<Message> selectedViewsData) {
//                selectedMessage = selectedViewsData;
                showToolbar(selectedViewsData);
            }
        });
        ll_layout = findViewById(R.id.ll_layout);
        mainMenu = findViewById(R.id.mainMenu);

       /* activityChatBoatBinding.mMessagesView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    activityChatBoatBinding.mMessagesView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activityChatBoatBinding.mMessagesView.scrollToPosition(messageAdapter.getItemCount() - 1);
                        }
                    }, 100);
                }
            }
        });*/

        activityChatBoatBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        activityChatBoatBinding.mMessagesView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }

// =======================  Start Message Date set on top =================================

                visiblePosition = layoutManager.findFirstVisibleItemPosition();
                lastvisiblePosition = layoutManager.findFirstVisibleItemPosition();
                firstCompletelyVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                lastCompletelyVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                Log.e(TAG, "onScroll: First_visible:" + visiblePosition);
                Log.e(TAG, "onScroll: last_visible:" + lastvisiblePosition);
                Log.e(TAG, "onScroll: first_complete_visible:" + firstCompletelyVisiblePosition);
                Log.e(TAG, "onScroll: last_complete_visible:" + lastCompletelyVisiblePosition);
                if (visiblePosition > -1) {
                    if (firstCompletelyVisiblePosition > -1) {
                        Log.e(TAG, "onScrollStateChanged: 2" + mMessages.get(firstCompletelyVisiblePosition).getmAttachmentType());
                        if (mMessages.get(firstCompletelyVisiblePosition).getMessageDate() != null && !mMessages.get(firstCompletelyVisiblePosition).getMessageDate().equals("null")) {
                            activityChatBoatBinding.chatDate.setVisibility(View.VISIBLE);
                            activityChatBoatBinding.chatDate.setText(mMessages.get(firstCompletelyVisiblePosition).getMessageDate());
                        }
                    }
                }
//                if (!activityChatBoatBinding.mMessagesView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
//                    activityChatBoatBinding.chatDate.setVisibility(View.VISIBLE);
//                }
                int totalItemCount = layoutManager.getItemCount();

                if (lastCompletelyVisiblePosition == totalItemCount - 1) {
                    activityChatBoatBinding.chatDate.setVisibility(View.INVISIBLE);
                }
//==================================== End Message Date set on top =======================
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollOutitems = layoutManager.findFirstCompletelyVisibleItemPosition();
                Log.e("Message Scroll", scrollOutitems + "");
                if (userScrolled && (scrollOutitems == 0 && userList.size() >= 10)) {
                    activityChatBoatBinding.loadmoremsg.setVisibility(View.VISIBLE);
                    userScrolled = false;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        pageNumber = pageNumber + 1;
                        jsonObject.put("chatHeadId", newChatListResponse.getChatHeadId());
                        jsonObject.put("page", pageNumber);
                        jsonObject.put("limit", limit);
                        jsonObject.put("toUserId", newChatListResponse.getId());
                        jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(ChatBoatActivity.this, SharedPrefreances.ID));
                        if (mMessages.size() > 0)
                            jsonObject.put("previousDate", mMessages.get(mMessages.size() - 1).getMessageDate());
                        mSocket.emit("get_history_pages", jsonObject);
                        Log.e("history", "show msg" + jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        activityChatBoatBinding.cancelReplyChatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityChatBoatBinding.replyLayout.setVisibility(View.GONE);
            }
        });

        setOnStartRecord(new StartRecordListener() {
            @Override
            public void onStartRecord() {

            }
        });

        setRecordingFinishedListener(new RecordingFinishedListener() {
            @Override
            public void onRecordingFinished(File file) {
                sendAudio(file);
            }
        });

       /* activityChatBoatBinding.micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChatBoatActivity.this,"Hold To Speak",Toast.LENGTH_SHORT).show();
            }
        });*/
        activityChatBoatBinding.micro.setOnTouchListener(this);

        getHistoryEmit();

        swipeReply();

        initListener();

        initObserver();

//        viewModel.getChatTheme();
        activityChatBoatBinding.cancelReplyChatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityChatBoatBinding.replyLayout.setVisibility(View.GONE);
            }
        });

//        if (Helper.isNetworkAvailable(this)) {
//            getChatTheme();
//        } else {
//            Toast.makeText(ChatBoatActivity.this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
//        }
        clipboardManager = (ClipboardManager)

                getSystemService(CLIPBOARD_SERVICE);

//        inject();
//        setupRecyclerView();

        if (linearLayoutManager != null) {

            index = linearLayoutManager.findFirstVisibleItemPosition();
            View v = linearLayoutManager.getChildAt(0);
            top = (v == null) ? 0 : (v.getTop() - linearLayoutManager.getPaddingTop());
            Log.d("TAG", "visible position " + " " + index);
        } else {
            index = 0;
        }

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.scrollToPositionWithOffset(index, top);
//        goToTop();

    }

//    private void goToTop() {
//        activityChatBoatBinding.goToTop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LinearLayoutManager layoutManager = (LinearLayoutManager)activityChatBoatBinding.mMessagesView.getLayoutManager();
////                layoutManager.scrollToPositionWithOffset(0, 0);
//                layoutManager.scrollToPosition(mMessages.size()-1);
//            }
//        });
//    }

    private void initObserver() {
        viewModel.pDialog = new ProgressDialog(ChatBoatActivity.this);
        viewModel.activityChatBoatBinding = activityChatBoatBinding;
        viewModel.fontName.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Message.setFontType(s);
                viewModel.messageAdapter.notifyDataSetChanged();
            }
        });
//        viewModel.appearanceData.observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if (aBoolean) {
//                    Observer<SaveTextAppearanceModel.Data> liveDataObserver = data -> setUserTheme(viewModel.saveTextMutable.getValue());
//                    viewModel.getLiveData().observe(ChatBoatActivity.this, liveDataObserver);
//                }
//            }
//        });
    }

    public void showToolbar(List<Message> messageList) {

        this.selectedMessage = messageList;

        if (messageList.size() > 0) {
            Log.e("TAG", "showToolbar: " + messageList.get(0).getMessage());
            //show your toolbar options here hide default view
            mainMenu.setVisibility(View.GONE);
            ll_layout.setVisibility(View.VISIBLE);
            if (messageList.size() > 1) {
                activityChatBoatBinding.copyLl.setVisibility(View.INVISIBLE);
                activityChatBoatBinding.replyLl.setVisibility(View.INVISIBLE);
            } else {
                activityChatBoatBinding.copyLl.setVisibility(View.VISIBLE);
                activityChatBoatBinding.replyLl.setVisibility(View.VISIBLE);
            }
        } else {
            ll_layout.setVisibility(View.GONE);
            mainMenu.setVisibility(View.VISIBLE);
            viewModel.messageAdapter.longPressEnable = false;
        }
        for (Message message : messageList) {
            String msgType = message.getmAttachmentType();
            if (msgType.equalsIgnoreCase("image") || msgType.equalsIgnoreCase("video") || msgType.equalsIgnoreCase("audio")) {
                if (messageList.size() == 1) {
                    if (message.getMessage().trim().isEmpty()) {
                        activityChatBoatBinding.copyLl.setVisibility(View.INVISIBLE);
                    } else {
                        activityChatBoatBinding.copyLl.setVisibility(View.VISIBLE);
                    }
                } else {
                    activityChatBoatBinding.copyLl.setVisibility(View.INVISIBLE);
                }

            } else {
                if (messageList.size() > 1) {
                    activityChatBoatBinding.copyLl.setVisibility(View.INVISIBLE);
                } else {
                    activityChatBoatBinding.copyLl.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: " + "true");
        if (Helper.isNetworkAvailable(context)) {
            viewModel.getChatTheme();
            Log.e(TAG, "onResume: " + "getchattheme_call");
        }
        GetChatSetting(newChatListResponse.getId(), newChatListResponse.getChatHeadId(), SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off("connected", onConnect);
        mSocket.off("session", session);
        mSocket.off("sent", sent);
        mSocket.off("chat_history", chat_history);
        mSocket.off("sent_ios", successFullySend);
        mSocket.off("read", messageRead);
//        mSocket.disconnect();
    }

    public void getHistoryEmit() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("chatHeadId", newChatListResponse.getChatHeadId());
            jsonObject.put("limit", limit);
            jsonObject.put("page", pageNumber);
            jsonObject.put("toUserId", newChatListResponse.getId());
            jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
            mSocket.emit("get_history", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void readMessageIfPhoneScreenOPEN() {
        KeyguardManager myKM = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        if (myKM.isKeyguardLocked()) {
            System.out.println(getString(R.string.phone_is_locked));
        } else {
            readMessage();
            Log.e("Phone_is_not_locked", "ok");
        }
    }

    //when other user seen message
    public void readMessage() {
        Log.e("TAG", "run:" + "messageRead_chatboat");
        JSONObject jsonObjectRead = new JSONObject();
        try {
            jsonObjectRead.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
            jsonObjectRead.put("toUserId", newChatListResponse.getId());
            jsonObjectRead.put("chatHeadId", newChatListResponse.getChatHeadId());
            jsonObjectRead.put("readStatus", Global.ReadStatus.read.name());
            mSocket.emit("messageRead", jsonObjectRead);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addMessageNoScroll(int index, String username, String message, String
            read, String created, String attachment, String fileurl, String messageid,
                                    String messagestatus, String atachmentType, String jsondata, String replyChats,
                                    String thumbnailURL, String sendTime) {
        try {
            if (!atachmentType.equals("Date")) {
                fileurl = AESHelper.msgDecrypt(fileurl, newChatListResponse.getChatHeadId());
                message = AESHelper.msgDecrypt(message, newChatListResponse.getChatHeadId());
                replyChats = AESHelper.msgDecrypt(replyChats, newChatListResponse.getChatHeadId());
                thumbnailURL = AESHelper.msgDecrypt(thumbnailURL, newChatListResponse.getChatHeadId());
            }

            File originalPath = getFilePath(atachmentType,messageid);
            String download = "false";
            if (originalPath != null && originalPath.exists()) {
                download = "true";
            }
            mMessages.add(index, new Message.Builder(Message.TYPE_MESSAGE)
                    .username(username)
                    .message(message)
                    .read(read)
                    .create(created)
                    .attach(attachment)
                    .fileurl(fileurl)
                    .messageid(messageid)
                    .status(messagestatus)
                    .attachmentType(atachmentType)
                    .jsondata(jsondata)
                    .oldMsg(replyChats)
                    .thumbnailURL(thumbnailURL)
                    .messageDate(sendTime)
                    .mFileUri(originalPath.getAbsolutePath())
                    .isDownload(download)
                    .build());
            viewModel.messageAdapter.notifyItemInserted(index);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Tag____", e.getMessage());
        }
        scrollToLastMessage();
    }

    private void scrollToLastMessage() {
        if (viewModel.messageAdapter.getItemCount() > 20) {
//            activityChatBoatBinding.mMessagesView.scrollToPosition(20);
        }
    }

    private void readMessagesByReciever(JSONObject data) {
        for (int i = 0; i < mMessages.size(); i++) {
            String username = mMessages.get(i).getUsername();
            String message = mMessages.get(i).getMessage();
            String created = mMessages.get(i).getmCreated();
            String attachment = mMessages.get(i).getmAttachment();
            String fileurl = mMessages.get(i).getmFileurl();
            String messageid = mMessages.get(i).getmMessageId();
            String messagestatus = mMessages.get(i).getmMessgaeStatus();
            String atachmentType = mMessages.get(i).getmAttachmentType();
            String jsondata = mMessages.get(i).getmJsonData();
            String read = null;
            try {
                read = data.getString("readStatus");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "readMessagesByReciever: " + read);
            mMessages.set(i, new Message.Builder(Message.TYPE_MESSAGE)
                    .username(username).message(message).read(read).create(created).attach(attachment).fileurl(fileurl).messageid(messageid).status(messagestatus).attachmentType(atachmentType).jsondata(jsondata).build());
            viewModel.messageAdapter.notifyItemChanged(i);

        }
    }

    private void dataMessage(String user, JSONObject msgData) {
        try {
            Log.e("dataMessage", "messageJSON" + msgData + msgData.getString("msg"));

            if (newChatListResponse.getChatHeadId().equals(msgData.getString("chatHeadId"))) {
//                try {
//                    chatBoatModel.setPreviousDate(changeDateFormate(msgData.getString("sendTime")));
//                } catch (Exception e) {
//
//                }
                String sendTime = msgData.getString("sendTime").split(" ")[0];
                if (previousDate.equals("") || funDateCheck(previousDate, sendTime)) {
                    previousDate = sendTime;
                    addMessage(user, previousDate, "",
                            "", "",
                            "", "",
                            "", "Date",
                            "",
                            "",
                            "", sendTime);
                }
                addMessage(user, msgData.getString("msg"), msgData.getString("read"),
                        msgData.getString("createdAt"), msgData.getString("attachment"),
                        msgData.getString("fileURL"), msgData.getString("id"),
                        msgData.getString("status"), msgData.getString("attachmentType"),
                        msgData.getString("jsonData"),
                        msgData.getString("oldMsg"),
                        msgData.getString("thumbnail"),
                        sendTime);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }
    }

    private File getFilePath(String atachmentType,String messageid) {
        File originalPath = new File(dir,"Metme_");
        if (atachmentType.equalsIgnoreCase("Image"))
            originalPath = new File(dir, "Metme_" + messageid + ".jpg");
        else if(atachmentType.equalsIgnoreCase("Video"))
            originalPath = new File(dir, "Metme_" + messageid + ".mp4");

        return originalPath;
    }

    private void addMessage(String username, String message, String read, String
            created, String attachment, String fileurl, String messageid, String messagestatus,
                            String atachmentType, String jsondata, String oldMsg, String thumbnailURL, String sendTime) {
        try {
            Log.e("addMessage", "added");
            if (!atachmentType.equals("Date")) {
                fileurl = AESHelper.msgDecrypt(fileurl, newChatListResponse.getChatHeadId());
                message = AESHelper.msgDecrypt(message, newChatListResponse.getChatHeadId());
                oldMsg = AESHelper.msgDecrypt(oldMsg, newChatListResponse.getChatHeadId());
                thumbnailURL = AESHelper.msgDecrypt(thumbnailURL, newChatListResponse.getChatHeadId());
            }
            File originalPath = getFilePath(atachmentType,messageid);
            String download = "false";
            if (originalPath != null && originalPath.exists()) {
                download = "true";
            }
            mMessages.add(new Message.Builder(Message.TYPE_MESSAGE).username(username).
                    message(message).
                    read(read).
                    create(created).
                    attach(attachment).
                    fileurl(fileurl).messageid(messageid).
                    status(messagestatus).
                    attachmentType(atachmentType).
                    jsondata(jsondata).
                    oldMsg(oldMsg).
                    thumbnailURL(thumbnailURL).
                    messageDate(sendTime).
                    mFileUri(originalPath.getAbsolutePath()).
                    isDownload(download).
                    build());
            viewModel.messageAdapter.notifyItemInserted(mMessages.size() - 1);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Tag____", e.getMessage());
        }
//        scrollToBottom();
    }

    private void scrollToBottom() {
        activityChatBoatBinding.mMessagesView.scrollToPosition(viewModel.messageAdapter.getItemCount() - 1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activityChatBoatBinding.mMessagesView.scrollToPosition(viewModel.messageAdapter.getItemCount() - 1);
            }
        }, 200);

    }

    private void scrollToTop() {
        activityChatBoatBinding.mMessagesView.scrollToPosition(3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activityChatBoatBinding.mMessagesView.smoothScrollToPosition(3);
            }
        }, 200);

    }

    private void swipeReply() {
        SwipeControllerNew controllerNew = new SwipeControllerNew(this, new ISwipeControllerActions() {
            @Override
            public void onSwipePerformed(int position, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof DateLayoutViewHolder) {
                    Toast.makeText(ChatBoatActivity.this, "Date layout Activity", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    showQuotedMessage(mMessages.get(position));
                    reply_layout.set(true);
                }
            }
        });
        new ItemTouchHelper(controllerNew).attachToRecyclerView(activityChatBoatBinding.mMessagesView);
    }

    private void showQuotedMessage(Message message) {
        activityChatBoatBinding.messageInput.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(activityChatBoatBinding.messageInput, InputMethodManager.SHOW_IMPLICIT);

        activityChatBoatBinding.replyLayout.setVisibility(View.VISIBLE);
        chatBoatModel.setPreviousMsg(message.getmMessageId());

        String duration = "";
        JSONObject jsonObject = null;
        try {

            if (!message.getmJsonData().equalsIgnoreCase("null") && !message.getmJsonData().equalsIgnoreCase("")) {
                jsonObject = new JSONObject(message.getmJsonData());
                duration = jsonObject.getString("duration");
            }
            if (message.getmAttachmentType().equalsIgnoreCase("Audio") && (!duration.isEmpty())) {
                activityChatBoatBinding.replymsg.setText("Voice Message (" + duration + " )");
            } else {
                activityChatBoatBinding.replymsg.setText(message.getMessage());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        activityChatBoatBinding.replyUsername.setText(newChatListResponse.getFirstName() + " " + newChatListResponse.getLastName());
        Glide.with(activityChatBoatBinding.replyImage.getContext()).load(message.getThumbnailURL()).into(activityChatBoatBinding.replyImage);
    }

    private void sendAudio(File file) {
        Uri uri = Uri.fromFile(file);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(this, uri);
        String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int millSecond = Integer.parseInt(durationStr);

        long seconds = TimeUnit.MILLISECONDS.toSeconds(millSecond);
        if (seconds < 2) {
            Toast.makeText(this, getString(R.string.hold_and_long_press_to_record), Toast.LENGTH_SHORT).show();
            setRecordDialogDismiss();
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
                            String fileUrl = AESHelper.encrypt(response.body().getData().getUrl(), newChatListResponse.getChatHeadId());
                            JSONObject jsonObjectTime = new JSONObject();
                            jsonObjectTime.put("duration", hms);
                            viewModel.emitSend("Audio", fileUrl, true, "", jsonObjectTime, "");
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

    public void initListener() {

        activityChatBoatBinding.deleteLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDelete();
            }
        });

        activityChatBoatBinding.copyLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String copyContent = selectedMessage.get(0).getMessage();
                int position = selectedMessage.get(0).getPosition();
                if (copyContent != null) {
                    ClipData clipData = ClipData.newPlainText("text", copyContent);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(ChatBoatActivity.this, getResources().getString(R.string.message_copied), Toast.LENGTH_SHORT).show();
                    if (position!=-1)
                        mMessages.get(position).setSelected(false);
                    viewModel.messageAdapter.notifyDataSetChanged();
                }
            }
        });

        activityChatBoatBinding.forwardLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatBoatActivity.this, ForwardActivity.class);
                IntentWrapper intentWrapper = new IntentWrapper();
                intentWrapper.setMessageIds(getIds());
                intent.putExtra("intent", new Gson().toJson(intentWrapper));
                startActivity(intent);
//                for (MessageAdapter.SelectedViewsData bgColor : selectedViewsData) {
//                    bgColor.getView().setBackground(null);
//                }
                mainMenu.setVisibility(View.VISIBLE);
                ll_layout.setVisibility(View.GONE);
//                finish();
            }
        });

        activityChatBoatBinding.imgReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuotedMessage(selectedMessage.get(0));
            }
        });
    }

    public void GetChatSetting(String toUserId, String chatHeadID, String x_token) {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", x_token);
        HashMap<String, String> body = new HashMap<>();
        body.put("userId", toUserId);
        body.put("chatHeadId", chatHeadID);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<ChatSettingResponse> call = webApi.GetChatsSetting(map, body);
        call.enqueue(new Callback<ChatSettingResponse>() {
            @Override
            public void onResponse(Call<ChatSettingResponse> call, Response<ChatSettingResponse> response) {
                try {
                    if (response.code() == 200) {
                        if (response.body().getCode() == 1) {
                            try {
                                if (response.body().getData().getIsBlocked()) {
                                    activityChatBoatBinding.rootView.setVisibility(View.GONE);
                                    activityChatBoatBinding.llBlockLayout.setVisibility(View.VISIBLE);
                                    activityChatBoatBinding.tvBlockMst.setText("You can't message or video chat with " + activityChatBoatBinding.getChatBoatViewModel().name);
                                } else {
                                    activityChatBoatBinding.rootView.setVisibility(View.VISIBLE);
                                    activityChatBoatBinding.llBlockLayout.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ChatSettingResponse> call, Throwable t) {
                Log.e("Atg", "test =" + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (fromNotification) {
            Intent intent = new Intent(ChatBoatActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        viewModel.messageAdapter.stopMedia();
        mSocket.off("connected", onConnect);

        mSocket.off("session", session);
        mSocket.off("sent", sent);
        mSocket.off("sent_ios", successFullySend);
        mSocket.off("chat_history", chat_history);
        mSocket.off("chat_history_pages", onMsgHistorywithPages);

        mSocket.off("read", messageRead);

        if (selectedMessage.size() > 0) {
            viewModel.messageAdapter.onBackpressed();
            activityChatBoatBinding.llLayout.setVisibility(View.GONE);
            activityChatBoatBinding.mainMenu.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
        activityChatBoatBinding.replyLayout.setVisibility(View.GONE);
        activityChatBoatBinding.replymsg.setText(null);

        finish();
    }

    private void changeHeaderColor(String firstColor, String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(0f);
            activityChatBoatBinding.MainContainer.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getColor(R.color.first_color), getColor(R.color.first_color)});
            gd.setCornerRadius(0f);
            activityChatBoatBinding.MainContainer.setBackground(gd);
        }
    }

    public void downloadFont(SaveTextAppearanceModel.Data data) {

        try {
            filePath = FilenameUtils.getName(Uri.parse(data.getFontName()).getPath());

            File fontFile = new File(getExternalCacheDir() + "/" + filePath);
            Log.e("font_name------", "/" + filePath);
            Log.e("TAG", "setFont: " + fontFile.getName());
            if (!fontFile.exists()) {
                PRDownloader.download(data.getFontName(), getExternalCacheDir().getPath(), "/" + filePath)
                        .build()
                        .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                            @Override
                            public void onStartOrResume() {

                            }
                        })
                        .start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                fontName = getExternalCacheDir() + "/" + filePath;
                                SharedPrefreances.setSharedPreferenceString(ChatBoatActivity.this, newChatListResponse.getChatHeadId(), fontName);
                                Log.e("font------------boat", "font:" + SharedPrefreances.getSharedPreferenceString(ChatBoatActivity.this, newChatListResponse.getChatHeadId()));
                            }

                            @Override
                            public void onError(Error error) {
                                System.out.println("Font Error" + error.getServerErrorMessage());
                            }
                        });
            } else {
                fontName = getExternalCacheDir() + "/" + filePath;
                SharedPrefreances.setSharedPreferenceString(ChatBoatActivity.this, newChatListResponse.getChatHeadId(), fontName);
            }
            Typeface face = Typeface.createFromFile(fontName);
            activityChatBoatBinding.tvTitle.setTypeface(face);
//            Message.setFontType(fontName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayImage(String filePath) {
        Log.e(TAG, "displayImage: " + filePath);
        Glide.with(this)
                .asBitmap()
                .load(filePath)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        activityChatBoatBinding.roundLayout.setBackground(new BitmapDrawable(getResources(), resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    public void inject() {
        spacesItemDecoration = new SpacesItemDecoration(50);

        animatedColor = new AnimatedColor(
//                outgoing_start_color
                ContextCompat.getColor(this, R.color.first_color),
                ContextCompat.getColor(this, R.color.first_color)
        );
    }

    public void setupRecyclerView() {
        activityChatBoatBinding.mMessagesView.addItemDecoration(spacesItemDecoration);
        activityChatBoatBinding.mMessagesView.smoothScrollBy(0, 1);
    }

    public void setupListeners() {
        activityChatBoatBinding.mMessagesView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView recyclerView1 = recyclerView;
                int i$iv = 0;
                for (int var7 = recyclerView.getChildCount(); i$iv < var7; ++i$iv) {
                    RecyclerView.ViewHolder viewHolder = recyclerView1.getChildViewHolder(recyclerView1.getChildAt(i$iv));
                    if (viewHolder instanceof ChatViewHolder) {
                        changeDrawableColor((ChatViewHolder) viewHolder);
                    } else if (viewHolder instanceof ChatImageViewHolder) {
                        changeDrawableColor((ChatImageViewHolder) viewHolder);
                    } else if (viewHolder instanceof ChatVideoViewHolder) {
                        changeDrawableColor((ChatVideoViewHolder) viewHolder);
                    } else if (viewHolder instanceof ChatAudioViewHolder) {
                        changeDrawableColor((ChatAudioViewHolder) viewHolder);
                    } else if (viewHolder instanceof ChatReplyViewHolder) {

                        changeDrawableColor((ChatReplyViewHolder) viewHolder);
                    }
                }
            }
        });
    }

    private void changeDrawableColor(RecyclerView.ViewHolder holder) {
        holder.itemView.post(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = ContextCompat.getDrawable((Context) ChatBoatActivity.this, R.drawable.rounded_corner_chat_box_one_bg);
                if (drawable != null) {
                    Drawable var1 = drawable;
                    int color = animatedColor.with(getFloatRange(holder.itemView));
                    Extentions.updateTint(var1, color);
                    View var8 = holder.itemView;
                    RelativeLayout var9 = (RelativeLayout) var8.findViewById(R.id.rl_main_layout);
                    var9.setBackground(var1);
                }
            }
        });
    }

    private float getFloatRange(View itemView) {
        float var10001 = Extentions.absY(itemView);
        Resources var10002 = this.getResources();
        return 1.0F - var10001 / (float) var10002.getDisplayMetrics().heightPixels;
    }

    @Subscribe
    public void deleteChatResponse(BulkChatDeleteResponse response) {
        if (response.isSuccess()) {
            viewModel.removeSelectedMessage();
            viewModel.removeDuplicateDate();
            viewModel.messageAdapter.notifyDataSetChanged();
            showToolbar(viewModel.messageAdapter.getSelectedMessages());
        } else {
            dialogFailed(getString(R.string.error_msg));
        }
    }


    List<String> getIds() {
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < viewModel.messageAdapter.mMessages.size(); i++) {
            if (viewModel.messageAdapter.mMessages.get(i).isSelected()) {
                ids.add(viewModel.messageAdapter.mMessages.get(i).getmMessageId());
            }
        }
        return ids;
    }

    public void dialogDelete() {
        try {
            Dialog dialog = new Dialog(ChatBoatActivity.this);
            dialog.getWindow().requestFeature(1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setContentView(R.layout.delete_dialog);
            dialog.setCancelable(false);
            Button yes = dialog.findViewById(R.id.yes);
            Button no = dialog.findViewById(R.id.no);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Map body = new HashMap();

                    body.put("ids", getIds());
                    body.put("toUserId", newChatListResponse.getId());
                    Map<String, String> header = new HashMap<>();
                    header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
                    apiCallBack(getApi().bulkDeleteChat(header, body));
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.callCreateRoom(viewModel.muteVideo);
                } else {
                    Constant.buildAlertForPermission(this);
//                Toast.makeText(this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();
                }

                break;
            case 102:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.onGalleryClick();
                } else {
                    Constant.buildAlertForPermission(this);
//                Toast.makeText(this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();
                }

                break;
            case 103:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.onCameraClick();
                } else {
                    Constant.buildAlertForPermission(this);
//                Toast.makeText(this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();
                }
                break;

            case 999:
//                Constant.buildAlertForPermission(this);
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE)
                            || PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                            || PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Constant.buildAlertForPermission(this);
                    }
                } else {
                    Constant.buildAlertForPermission(this);
//                Toast.makeText(this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (isListenForRecord()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_BUTTON_PRESS:
                    Toast.makeText(this, getString(R.string.hold_and_long_press_to_record), Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_DOWN:
                    // soundPool.play(soundStart,1,1,0,0,1);
                    if (permissionsAvailable(permissionsRecord)) {

                        if (Helper.isNetworkAvailable(getApplicationContext())) {

                            startRecording();
                        } else {
                            Toast.makeText(this, getApplicationContext().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                        }
                        recordingDialog(v.getContext());
                    } else {
                        ActivityCompat.requestPermissions(this, permissionsRecord, 999);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    stopRecording();
                    //  soundPool.play(soundEnd,1,1,0,0,1);
                    break;
            }

        }
        return isListenForRecord();
    }

    private String getThumbnailVideoFile(File file) {
        Bitmap bmThumbnail;
        File thumbFile = new File(getExternalCacheDir(), "/" + "thumbnail_" + file.getName().replaceFirst("[.][^.]+$", "") + ".jpg");
        try {
            FileOutputStream stream = new FileOutputStream(thumbFile);

            bmThumbnail = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);

            if (bmThumbnail != null) {
                bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            }
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thumbFile.getAbsolutePath();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private String getThumbnailFile(File file) {
        Bitmap bmThumbnail;
        File thumbFile = new File(getExternalCacheDir(), "/" + "thumbnail_" + file.getName());
        try {
            FileOutputStream stream = new FileOutputStream(thumbFile);

            bmThumbnail = ThumbnailUtils.createImageThumbnail(file.getAbsolutePath(), MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);

            if (bmThumbnail != null) {
                bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 30, stream);
                bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 30, stream);
            }
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return thumbFile.getAbsolutePath();
    }

    private void uploadVideoWithThumbnail(File videoFile, File thumbnailFile) {
        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part vFile = MultipartBody.Part.createFormData("file", videoFile.getName(), videoBody);

        RequestBody requestThumbnailFile = RequestBody.create(MediaType.parse("image/jpeg"), thumbnailFile);
        MultipartBody.Part thumbnailBody = MultipartBody.Part.createFormData("thumbnails", "image.jpg", requestThumbnailFile);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<ThumbnailResponse> call = webApi.getThumbnail(map, vFile, thumbnailBody);
        call.enqueue(new Callback<ThumbnailResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onResponse(Call<ThumbnailResponse> call, Response<ThumbnailResponse> response) {
                try {
                    if (response.body().getCode() == 1) {
                        Log.e("TAG", "videouploadeddd=" + response.body().getData().getFile());
                        Log.e("TAG", "videouploadeddd=thumb " + response.body().getData().getThumbnail());
                        String msg = "";
                        if (selectedItem.get(0).getImageText() == null || selectedItem.get(0).getImageText().isEmpty()) {
                            msg = "";
                        } else {
                            msg = selectedItem.get(0).getImageText();
                        }
                        String fileUrl = AESHelper.encrypt(response.body().getData().getFile(), newChatListResponse.getChatHeadId());
                        String thumbFile = AESHelper.encrypt(response.body().getData().getFile(), newChatListResponse.getChatHeadId());
                        viewModel.emitSend("Video", fileUrl, true, msg, null, thumbFile);
                        if (selectedItem.size() > 0) {
                            selectedItem.remove(0);
                            if (selectedItem.size() == 1 || selectedItem.size() == 0) {
                                showBarComplete("Completed");
                            }
                            if (selectedItem.get(0).isVideo()) {
                                videoRecursion();
                            } else {
                                imageRecursion();
                            }
                        }
                    } else {
                        Utilss.showToast(getApplicationContext(), "SOME_ERROR", R.color.grey);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ThumbnailResponse> call, Throwable t) {
                Utilss.showToast(getApplicationContext(), "SOME_ERROR", R.color.grey);
            }
        });
    }

    private void setUserTheme(SaveTextAppearanceModel.Data saveModel) {
        if (saveModel.getFontName() != null && (saveModel.getFontName().length() > 0 || !saveModel.getFontName().equals(""))) {
            if (saveModel.getFontData() != null) {
                Global.setFontSize(saveModel.getFontData());
                activityChatBoatBinding.tvTitle.setTextSize(saveModel.getFontData().getSize2());
            }
            downloadFont(saveModel);
        }
        if (saveModel.getGradient() != null && (saveModel.getGradient().equalsIgnoreCase("false") || saveModel.getGradient().equals(""))) {
            firstColor = saveModel.getFirstColor();
            secondColor = saveModel.getSecondColor();
            viewModel._firstColor = firstColor;
            viewModel._secondColor = secondColor;
            changeHeaderColor(firstColor, secondColor);
            SharedPrefreances.setSharedPreferenceString(context, newChatListResponse.getChatHeadId() + "_color", secondColor);
            viewModel.saveTextResponseModel.setData(saveModel);
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
        viewModel.messageAdapter.notifyDataSetChanged();
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            getResources().getString(R.string.please_wait)
            System.out.println("Starting download");
            pDialog.setMessage(getResources().getString(R.string.please_wait));
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

                    directoryName = new File(String.valueOf(getExternalCacheDir()));
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
                Toast.makeText(ChatBoatActivity.this, "Folder Not created", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            Uri uri = Uri.fromFile(fileName);
            displayImage(String.valueOf(uri));
            Log.e(TAG, "onPostExecute: " + file_url);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pDialog.setProgress(Integer.parseInt(values[0]));
        }
    }
}
