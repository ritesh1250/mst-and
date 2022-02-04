package com.meest.metme.viewmodels;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.meest.R;
import com.meest.responses.Message;
import com.meest.metme.encription.AESHelper;
import com.meest.metme.model.SaveTextAppearanceModel;
import com.meest.metme.model.chat.NewChatListResponse;
import com.meest.videomvvmmodule.utils.Global;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.socket.client.Socket;


public class ChatTextViewModel {
    static boolean isPlaying = false;
    static MediaPlayer mediaPlayer;
    static Handler mHandler;
    static Runnable runnable;
    static ObjectAnimator progressAnimator;
    public String message;
    public String time;
    public String read;
    public String image;
    public String imageText = "";
    public String date;
    public ObservableBoolean isDateVisible = new ObservableBoolean(false);
    public String replyText;
    public String replyImage;
    public Message chatTextModel;
    public String attachmentType;
    public String attachmentTypeMy;
    public String audioName;
    public String username;
    public String audioDuration;
    public String replyAudioName;
    public String fontName = "";
    public String messsageDate;
    public String isDownload;
    public int transparent;
    Activity context;
    String daysName;
    boolean isPause = false;
    ProgressBar progressBar;
    private NewChatListResponse newChatListResponse;
    private Socket mSocket;
    private String mFileUri;

    public ChatTextViewModel(Activity context, Message message, NewChatListResponse newChatListResponse, Socket mSocket, ProgressBar progressBar, SaveTextAppearanceModel saveTextResponseList) {
        this.context = context;
        this.chatTextModel = message;
        this.mFileUri = message.getmFileUri();
        this.isDownload = message.getDownload();
        this.message = message.getMessage();
        this.time = message.getmCreated();
        this.read = message.getmRead();
//        this.imageText = message.getMessage();
        this.newChatListResponse = newChatListResponse;
        this.progressBar = progressBar;
        this.attachmentTypeMy = message.getmAttachmentType();
        this.fontName = Message.getFontType();
        Log.e("TAG", "font_name: "+this.fontName);
        this.username = newChatListResponse.getFirstName() + " " + newChatListResponse.getLastName();
        this.transparent = transparent;
        this.messsageDate = message.getMessageDate();

        if (message.getThumbnailURL() != null && message.getType() == 1)
            this.image = message.getThumbnailURL();
        else
            this.image = message.getmFileurl();
        Log.e("reply_image", image);
//        if(messsageDate!=null) {
//            String[] parts = messsageDate.split("T");
//            String part1 = parts[0];
//            this.date = part1;
//            isDateVisible.set(true);
//
//            System.out.println("Dateyyy"+date);
//
//      //      daysNameOfWeek(part1);
//        }
        if (message.getmFileurl() != null && message.getmAttachmentType().equalsIgnoreCase("Audio")) {
            try {
                URL url = new URL(message.getmFileurl());

                this.audioName = "AUD-" + FilenameUtils.getName(url.getPath());
                this.audioDuration = new JSONObject(message.getmJsonData()).getString("duration");
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        if (message.getOldMsg() != null && (!message.getOldMsg().equalsIgnoreCase("null")) && !"".equals(message.getOldMsg()) && !"[]".equals(message.getOldMsg())) {

            String json = message.getOldMsg();
            try {
                JSONArray jsonArray = new JSONArray(json);


                if (jsonArray.length() > 0) {

                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    JSONObject jsonObject1 = jsonObject.getJSONObject("chat");
                    this.imageText = jsonObject1.getString("msg");
                    this.replyText = AESHelper.msgDecrypt(jsonObject1.getString("msg"), newChatListResponse.getChatHeadId());
                    if (jsonObject1.getString("fileURL") != null) {
                        this.attachmentType = jsonObject1.getString("attachmentType");

                        if (jsonObject1.getString("attachmentType").equalsIgnoreCase("Audio")) {
                            String string = jsonObject1.getString("fileURL");
                            URL url = new URL(AESHelper.msgDecrypt(string, newChatListResponse.getChatHeadId()));
                            this.replyAudioName = "AUD-" + FilenameUtils.getName(url.getPath());
                        } else {
                            this.replyImage = AESHelper.msgDecrypt(jsonObject1.getString("fileURL"), newChatListResponse.getChatHeadId());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
        if (message.getmAttachmentType().equals("Date")) {
            Log.e("Dateyyyy", message.getMessage());
            if (message.getMessage() != null && (!message.getMessage().equalsIgnoreCase(""))) {
                this.date = message.getMessage() /*+ " (" + daysNameOfWeek(message.getMessage()) + ")"*/;
                isDateVisible.set(true);
            }
        }
        this.mSocket = mSocket;
    }

    @BindingAdapter("readCheck")
    public static void readRecievedCheck(ImageView imageView, String readStatus) {
//             if(read.equalsIgnoreCase("true"))
//                 readRecievedImage.setImageResource(R.drawable.ic_chat_read_blue);
//             else
//                 readRecievedImage.setImageResource(R.drawable.ic_delivered);
        if (readStatus != null) {
            if (readStatus.equals(Global.ReadStatus.read.name())) {
                imageView.setImageResource(R.drawable.ic_chat_read_inside);
            } else if (readStatus.equals(Global.ReadStatus.delivered.name())) {
                imageView.setImageResource(R.drawable.ic_chat_delivered_inside);
            } else {
                // imageView.setImageResource(R.drawable.ic_chat_send_gray);
                imageView.setImageResource(R.drawable.ic_chat_send_inside);
            }
        } else {
            imageView.setImageResource(R.drawable.ic_chat_read_inside);
        }
    }

    @BindingAdapter("bindSelectedColor")
    public static void bindSelectedColor(View view, ChatTextViewModel data) {
        if (data.chatTextModel.isSelected()) {
            view.setBackgroundColor(Color.parseColor("#70143988"));
        } else {
            view.setBackgroundColor(Color.TRANSPARENT);
        }
    }


    @BindingAdapter("bindImageChat")
    public static void bindImage(ImageView readRecievedImage, ChatTextViewModel data) {
        if (data.isDownload.equalsIgnoreCase("true")) {
            Uri uri = Uri.fromFile(new File(data.mFileUri));
            Glide.with(readRecievedImage.getContext()).
                    load(uri).
                    placeholder(R.drawable.image_placeholder).
                    into(readRecievedImage);
        } else {
            Glide
                    .with(readRecievedImage.getContext())
                    .asBitmap()
                    .load(data.image)
                    .into(readRecievedImage);
        }
    }

    @BindingAdapter("bindChat")
    public static void bindReplyImage(ImageView readRecievedImage, String image) {
        Glide.with(readRecievedImage.getContext()).load(image)
                .into(readRecievedImage);

    }

    public static void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mHandler.removeCallbacks(runnable);

        }
        isPlaying = false;

    }

    public void click() {
//        context.startActivity(new Intent(context, ViewContactActivity.class));
    }

    public void onAudioImageClick() {
        Log.e("Audio Clicked", "AudioClicked");

        if (isPause) {
            int length = mediaPlayer.getCurrentPosition();

            mediaPlayer.seekTo(length);
            mediaPlayer.start();
            mHandler.postDelayed(runnable, 1);
        } else {
            File fullpath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Metme/" + chatTextModel.getmMessageId() + ".mp3");
            if (fullpath.exists()) {

                OpenAudio(fullpath.getAbsolutePath());

            } else {
                download_audio();
            }
        }
    }

    public void OpenAudio(String filepath) {

        if (!isPlaying) {

            Log.e("maintaonnnnn: ", "file: " + filepath);
            Uri uri = Uri.parse(filepath);
            mediaPlayer = new MediaPlayer();
            try {
                // mediaPlayer.setDataSource(String.valueOf(myUri));
                mediaPlayer.setDataSource(context, uri);
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });

            JSONObject ContactObject = null;
            try {
                ContactObject = new JSONObject(chatTextModel.getmJsonData());
                progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 100, 0);

                audioDuration = ContactObject.getString("duration"); //mm:ss
                String[] units = audioDuration.split(":"); //will break the string up into an array
                int hours = Integer.parseInt(units[0]); //first element
                int minutes = Integer.parseInt(units[1]); //second element
                int seconds = Integer.parseInt(units[2]); //second element

                int duration = (3600 * hours) + (60 * minutes) + seconds + 1; //add up our values
                progressAnimator.setDuration(duration * 1000);
                progressAnimator.setInterpolator(new LinearInterpolator());
                progressAnimator.start();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mHandler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mCurrentPosition = mediaPlayer.getCurrentPosition();
                        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(mCurrentPosition),
                                TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mCurrentPosition)),
                                TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition)));

                        Log.e("mCur", "mCur:  " + mCurrentPosition);
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //change View Data
                                //          tv_duration.setText(hms);
                            }
                        });
                    }
                    mHandler.postDelayed(this, 1);
                }
            };
            mHandler.postDelayed(runnable, 1);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {


                    JSONObject ContactObject = null;
                    try {
                        ContactObject = new JSONObject(chatTextModel.getmJsonData());
                        //        tv_duration.setText(ContactObject.getString("duration"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    isPlaying = false;
                    mHandler.removeCallbacks(runnable);

                }
            });
            isPlaying = true;
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();

                progressAnimator.setDuration(0);
                progressAnimator.setInterpolator(new LinearInterpolator());
                progressAnimator.start();

            }
            isPlaying = false;
            mHandler.removeCallbacks(runnable);

            OpenAudio(filepath);

            //  OpenAudio(filepath);
        }
    }

    public void download_audio() {
//        p_bar.setVisibility(View.VISIBLE);
        File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Metme/");

        int downloadId = PRDownloader.download(chatTextModel.getmFileurl(), direct.getPath(), chatTextModel.getmMessageId() + ".mp3")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        File fullpath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Metme/" + chatTextModel.getmMessageId() + ".mp3");

                        OpenAudio(fullpath.getAbsolutePath());
//                        p_bar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError(Error error) {
                        Log.e("on download error: ", ":: " + error.getConnectionException());
                        Log.e("on download error: ", ":: " + error.getServerErrorMessage());
                    }

                });

    }

    public String daysNameOfWeek(String inputDate) {

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        try {

            Date date = df.parse(inputDate.trim());
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            daysName = outFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();

            Log.e("MessageDateey", inputDate);
        }


        return daysName;
    }
}
