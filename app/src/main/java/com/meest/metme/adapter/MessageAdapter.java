package com.meest.metme.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.google.gson.Gson;
import com.meest.BR;
import com.meest.R;
import com.meest.responses.Message;
import com.meest.databinding.ChatItemReplyMyBinding;
import com.meest.databinding.ChatItemReplyOtherBinding;
import com.meest.databinding.ItemChatAudioMyBinding;
import com.meest.databinding.ItemChatAudioOtherBinding;
import com.meest.databinding.ItemChatDateBinding;
import com.meest.databinding.ItemChatImageMyBinding;
import com.meest.databinding.ItemChatImageOtherBinding;
import com.meest.databinding.ItemChatMyBinding;
import com.meest.databinding.ItemChatOtherBinding;
import com.meest.databinding.ItemChatVideoMyBinding;
import com.meest.databinding.ItemChatVideoOtherBinding;
import com.meest.metme.ChatImageViewActivity;
import com.meest.metme.ViewHolder.ChatAudioViewHolder;
import com.meest.metme.ViewHolder.ChatImageViewHolder;
import com.meest.metme.ViewHolder.ChatOtherAudioViewHolder;
import com.meest.metme.ViewHolder.ChatOtherImageViewHolder;
import com.meest.metme.ViewHolder.ChatOtherReplyViewHolder;
import com.meest.metme.ViewHolder.ChatOtherVideoViewHolder;
import com.meest.metme.ViewHolder.ChatOtherViewHolder;
import com.meest.metme.ViewHolder.ChatReplyViewHolder;
import com.meest.metme.ViewHolder.ChatVideoViewHolder;
import com.meest.metme.ViewHolder.ChatViewHolder;
import com.meest.metme.ViewHolder.DateLayoutViewHolder;
import com.meest.metme.model.SaveTextAppearanceModel;
import com.meest.metme.model.chat.NewChatListResponse;
import com.meest.metme.viewmodels.ChatTextViewModel;
import java.io.File;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.Socket;

import static androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int mychat_layout = R.layout.item_chat_my;
    private static final int friendchat_layout = R.layout.item_chat_other;
    private static final int myImage_layout = R.layout.item_chat_image_my;
    private static final int friendImage_layout = R.layout.item_chat_image_other;
    private static final int myVideo_layout = R.layout.item_chat_video_my;
    private static final int frinedVideo_layout = R.layout.item_chat_video_other;
    private static final int myAudio_layout = R.layout.item_chat_audio_my;
    private static final int friendAudio_layout = R.layout.item_chat_audio_other;
    private static final int replyChatMy_layout = R.layout.chat_item_reply_my;
    private static final int replyChatOther_layout = R.layout.chat_item_reply_other;
    private static final int mychat = 1;
    private static final int friendchat = 2;
    private static final int myImage = 3;
    private static final int friendImage = 4;
    private static final int myVideo = 5;
    private static final int frinedVideo = 6;
    private static final int myAudio = 7;
    private static final int friendAudio = 8;
    private static final int replyChatMy = 9;
    private static final int replyChatOther = 10;
    private static final int dateLayout = 11;

    private static final int dateLayout1 = R.layout.item_chat_date;
    //    public SaveTextAppearance saveTextResponseList;
//    ViewDataBinding viewBinding;
    Socket mSocket;
    public SaveTextAppearanceModel saveTextResponseList;
    int date = 0;

    View SelectedView;
    //ToolTipWindow tipWindow;
    ItemLongClick itemLongClick;
    private Activity context;
    public List<Message> mMessages;
    private NewChatListResponse newChatListResponse;

    public boolean longPressEnable = false;

    Object object;
    MessageAdapter.OnItemClickListener mItemClickListener;

    ViewHolder viewHolder;
    private String fontName;
    private boolean isDownload = false;
    private File directoryName;
    BroadcastReceiver receiver;
    private DownloadManager downloadManager;
    private File originalPath, dir;
    long downloadID;

    public MessageAdapter(Activity context, List<Message> messageModelList, NewChatListResponse newChatListResponse, Socket mSocket, SaveTextAppearanceModel saveTextResponseList) {
        this.context = context;
        this.mMessages = messageModelList;
        this.newChatListResponse = newChatListResponse;
        this.mSocket = mSocket;
        this.saveTextResponseList = saveTextResponseList;
        dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Meest");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        viewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);

//        ViewHolder viewHolder = new ViewHolder(viewBinding.getRoot());
//        viewHolder.setBinding(viewBinding);
//        return viewHolder;
//        View v = null;
        switch (viewType) {
            case mychat:
                ItemChatMyBinding itemChatMyBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_my, parent, false);
                viewHolder = new ChatViewHolder(itemChatMyBinding);
                break;
            case friendchat:
                ItemChatOtherBinding itemChatOtherBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_other, parent, false);
                viewHolder = new ChatOtherViewHolder(itemChatOtherBinding);
                break;
            case myImage:
                ItemChatImageMyBinding itemChatImageMyBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_image_my, parent, false);
                viewHolder = new ChatImageViewHolder(itemChatImageMyBinding);
                break;
            case friendImage:
                ItemChatImageOtherBinding itemChatImageOtherBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_image_other, parent, false);
                viewHolder = new ChatOtherImageViewHolder(itemChatImageOtherBinding);
                break;
            case myVideo:
                ItemChatVideoMyBinding itemChatVideoMyBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_video_my, parent, false);
                viewHolder = new ChatVideoViewHolder(itemChatVideoMyBinding);
                break;
            case frinedVideo:
                ItemChatVideoOtherBinding itemChatVideoOtherBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_video_other, parent, false);
                viewHolder = new ChatOtherVideoViewHolder(itemChatVideoOtherBinding);
                break;
            case myAudio:
                ItemChatAudioMyBinding itemChatAudioMyBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_audio_my, parent, false);
                viewHolder = new ChatAudioViewHolder(itemChatAudioMyBinding);
                break;
            case friendAudio:
                ItemChatAudioOtherBinding itemChatAudioOtherBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_audio_other, parent, false);
                viewHolder = new ChatOtherAudioViewHolder(itemChatAudioOtherBinding);
                break;
            case replyChatMy:
                ChatItemReplyMyBinding chatItemReplyMyBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.chat_item_reply_my, parent, false);
                viewHolder = new ChatReplyViewHolder(chatItemReplyMyBinding);
                break;
            case replyChatOther:
                ChatItemReplyOtherBinding chatItemReplyOtherBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.chat_item_reply_other, parent, false);
                viewHolder = new ChatOtherReplyViewHolder(chatItemReplyOtherBinding);
                break;
            case dateLayout:
                ItemChatDateBinding itemChatDateBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_chat_date, parent, false);
                viewHolder = new DateLayoutViewHolder(itemChatDateBinding);
                break;
        }
        return viewHolder;
//        return null;
    }

    @Override
    public int getItemViewType(int position) {
        try {
            if (mMessages.get(position).getOldMsg() != null && !mMessages.get(position).getOldMsg().equalsIgnoreCase("null") && (!"[]".equalsIgnoreCase(mMessages.get(position).getOldMsg()))) {
                if (mMessages.get(position).getUsername().equalsIgnoreCase("0")) {
                    return replyChatMy;
                }
                return replyChatOther;
            } else {

                if (mMessages.get(position).getmAttachmentType().equals("Message") && mMessages.get(position).getmMessgaeStatus().equals("true")) {
                    if (mMessages.get(position).getUsername().equalsIgnoreCase("0")) {
                        return mychat;
                    }
                    return friendchat;

                } else if (mMessages.get(position).getmAttachmentType().equals("Image") && mMessages.get(position).getmMessgaeStatus().equals("true")) {
                    if (mMessages.get(position).getUsername().equalsIgnoreCase("0")) {
                        return myImage;
                    }
                    return friendImage;
                } else if (mMessages.get(position).getmAttachmentType().equals("Video") && mMessages.get(position).getmMessgaeStatus().equals("true")) {
                    if (mMessages.get(position).getUsername().equalsIgnoreCase("0")) {
                        return myVideo;
                    }
                    return frinedVideo;
                } else if (mMessages.get(position).getmAttachmentType().equals("Audio") && mMessages.get(position).getmMessgaeStatus().equals("true")) {
                    if (mMessages.get(position).getUsername().equalsIgnoreCase("0")) {
                        return myAudio;
                    }
                    return friendAudio;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateLayout;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //  tipWindow = new ToolTipWindow(context);
        if (mMessages.get(position).getUsername().equalsIgnoreCase("0")) {
            object = new ChatTextViewModel(context, mMessages.get(position), newChatListResponse, mSocket, holder.itemView.findViewById(R.id.progressBarAudioMy), saveTextResponseList);
        } else {
            object = new ChatTextViewModel(context, mMessages.get(position), newChatListResponse, mSocket, holder.itemView.findViewById(R.id.progressBarAudioOther), saveTextResponseList);
        }
//        holder.getViewDataBinding().setVariable(BR.chatTextViewModel, object);
//        holder.getViewDataBinding().executePendingBindings();

//        View view = holder.itemView.getRootView();
        if (holder instanceof ChatViewHolder) {
            ((ChatViewHolder) holder).getBinding().setVariable(BR.chatTextViewModel, object);
        } else if (holder instanceof ChatOtherViewHolder) {
            ((ChatOtherViewHolder) holder).getBinding().setVariable(BR.chatTextViewModel, object);
        } else if (holder instanceof ChatImageViewHolder) {
            ((ChatImageViewHolder) holder).getBinding().setVariable(BR.chatTextViewModel, object);
        } else if (holder instanceof ChatOtherImageViewHolder) {
            ((ChatOtherImageViewHolder) holder).getBinding().setVariable(BR.chatTextViewModel, object);
        } else if (holder instanceof ChatVideoViewHolder) {
            ((ChatVideoViewHolder) holder).getBinding().setVariable(BR.chatTextViewModel, object);
        } else if (holder instanceof ChatOtherVideoViewHolder) {
            ((ChatOtherVideoViewHolder) holder).getBinding().setVariable(BR.chatTextViewModel, object);
        } else if (holder instanceof ChatAudioViewHolder) {
            ((ChatAudioViewHolder) holder).getBinding().setVariable(BR.chatTextViewModel, object);
        } else if (holder instanceof ChatOtherAudioViewHolder) {
            ((ChatOtherAudioViewHolder) holder).getBinding().setVariable(BR.chatTextViewModel, object);
        } else if (holder instanceof ChatReplyViewHolder) {
            ((ChatReplyViewHolder) holder).getBinding().setVariable(BR.chatTextViewModel, object);
        } else if (holder instanceof ChatOtherReplyViewHolder) {
            ((ChatOtherReplyViewHolder) holder).getBinding().setVariable(BR.chatTextViewModel, object);
        } else if (holder instanceof DateLayoutViewHolder) {
            ((DateLayoutViewHolder) holder).getBinding().setVariable(BR.chatTextViewModel, object);
        } else {
            System.out.println("======================================");
        }

//        set font on layout
//        TextView tvMessage = holder.itemView.findViewById(R.id.message);
//        TextView tvTime = holder.itemView.findViewById(R.id.tv_message_date_time);
//        setFont(tvMessage, tvTime, saveTextResponseList);
        if (!mMessages.get(position).getmAttachmentType().equalsIgnoreCase("Date")) {
            try {
                //        set font on layout
                RelativeLayout relativeLayout = holder.itemView.findViewById(R.id.rl_main_layout);
                Drawable buttonDrawable1 = relativeLayout.getBackground();
                buttonDrawable1 = DrawableCompat.wrap(buttonDrawable1);
                if (saveTextResponseList != null && saveTextResponseList.getData() != null) {
                    if (!saveTextResponseList.getData().getFirstColor().equals("") && !saveTextResponseList.getData().getSecondColor().equals("")) {
                        if (mMessages.get(position).getUsername().equalsIgnoreCase("0"))
                            DrawableCompat.setTint(buttonDrawable1, Color.parseColor(saveTextResponseList.getData().getSecondColor()));
                        else
                            DrawableCompat.setTint(buttonDrawable1, Color.parseColor(saveTextResponseList.getData().getFirstColor()));
                        relativeLayout.setBackground(buttonDrawable1);
                    } else {
                        if (mMessages.get(position).getUsername().equalsIgnoreCase("0"))
                            DrawableCompat.setTint(buttonDrawable1, ContextCompat.getColor(context, R.color.first_color));
                        else
                            DrawableCompat.setTint(buttonDrawable1, ContextCompat.getColor(context, R.color.gray_lightest));
                        relativeLayout.setBackground(buttonDrawable1);
                        // notifyDataSetChanged();
                    }
                } else {
                    if (mMessages.get(position).getUsername().equalsIgnoreCase("0"))
                        DrawableCompat.setTint(buttonDrawable1, ContextCompat.getColor(context, R.color.first_color));
                    else
                        DrawableCompat.setTint(buttonDrawable1, ContextCompat.getColor(context, R.color.gray_lightest));
                    relativeLayout.setBackground(buttonDrawable1);
                    // notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.itemView.getRootView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                   /* if (!tipWindow.isTooltipShown()) {
                        tipWindow.showToolTip(v);
                    }*/
                    longPressEnable = true;
                    updateList(position,holder.itemView);
                    itemLongClick.onLongClick(getSelectedMessages());
                    return true;
                }
            });

            holder.itemView.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date date = new Date();
                    DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String stringDate = sdf.format(date);
                    originalPath = new File(mMessages.get(position).getmFileUri());
                    if (!longPressEnable) {
//                        if (isDownload) {
                        if (originalPath != null) {
                            if (originalPath.exists()) {
                                if (mMessages.get(position).getmAttachmentType().equalsIgnoreCase("Image") || mMessages.get(position).getmAttachmentType().equalsIgnoreCase("Video")) {
                                    Intent intent = new Intent(context, ChatImageViewActivity.class);
                                    intent.putExtra("title", newChatListResponse.getFirstName() + " " + newChatListResponse.getLastName());
//                                intent.putExtra("iv_url", mMessages.get(position).getmFileurl());
                                    intent.putExtra("iv_url", originalPath.getAbsolutePath());
                                    intent.putExtra("type", mMessages.get(position).getmAttachmentType());
                                    intent.putExtra("dateAndTime", mMessages.get(position).getmCreated());
                                    intent.putExtra("username", mMessages.get(position).getUsername());
                                    intent.putExtra("messageId", mMessages.get(position).getmMessageId());
                                    intent.putExtra("whereFrom","singleMedia");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
//                                    ((ChatOtherImageViewHolder) holder).getBinding().downloadMedia.setVisibility(View.GONE);
//                                    ((ChatOtherImageViewHolder) holder).getBinding().circularProgressBar.setVisibility(View.GONE);
                                }
                            } else {
                                if (holder instanceof ChatOtherImageViewHolder) {
                                    ((ChatOtherImageViewHolder) holder).getBinding().downloadMedia.setVisibility(View.GONE);
                                    ((ChatOtherImageViewHolder) holder).getBinding().circularProgressBar.setVisibility(View.VISIBLE);
                                }
                                if (holder instanceof ChatOtherVideoViewHolder) {
                                    ((ChatOtherVideoViewHolder) holder).getBinding().downloadMedia.setVisibility(View.GONE);
                                    ((ChatOtherVideoViewHolder) holder).getBinding().circularProgressBar.setVisibility(View.VISIBLE);
                                }
                                downloadImageOrVideo(mMessages, position, stringDate, holder);
                            }
                        }
                    } else {
                        updateList(position, holder.itemView);
                        itemLongClick.onLongClick(getSelectedMessages());
                    }
                }
            });
        }

    }

    private void downloadImageOrVideo(List<Message> mMessages, int position, String stringDate, ViewHolder holder) {
        String mainPath = "";
        try {
            if (holder instanceof ChatOtherImageViewHolder || holder instanceof ChatImageViewHolder) {
                downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri downloadUri = Uri.parse(mMessages.get(position).getmFileurl());
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                mainPath = "Meest" + File.separator + "Metme_" + mMessages.get(position).getmMessageId() + ".jpg";
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle("Metme_" + stringDate)
                        .setMimeType("image/jpeg")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,mainPath );
                downloadID = downloadManager.enqueue(request);
                ((ChatOtherImageViewHolder) holder).getBinding().downloadMedia.setVisibility(View.GONE);
                ((ChatOtherImageViewHolder) holder).getBinding().circularProgressBar.setVisibility(View.GONE);
            } else if (holder instanceof ChatOtherVideoViewHolder || holder instanceof ChatVideoViewHolder) {
                downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri downloadUri = Uri.parse(mMessages.get(position).getmFileurl());
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                mainPath = "Meest" + File.separator + "Metme_" + mMessages.get(position).getmMessageId() + ".mp4";
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle("Metme_" + stringDate)
                        .setMimeType("video/mp4")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, mainPath);
                downloadID = downloadManager.enqueue(request);
            }
            isDownload = true;
//            SharedPrefreances.getSharedPreferenceBoolean(context.getApplicationContext(), "DownloadImage", isDownload);
            BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (downloadID == id) {
                        if (holder instanceof ChatOtherImageViewHolder) {
                            ((ChatOtherImageViewHolder) holder).getBinding().downloadMedia.setVisibility(View.GONE);
                            ((ChatOtherImageViewHolder) holder).getBinding().circularProgressBar.setVisibility(View.GONE);
                            Uri uri = Uri.fromFile(originalPath.getAbsoluteFile());
                            Glide.with(context).load(uri).placeholder(R.drawable.image_placeholder).into(((ChatOtherImageViewHolder) holder).getBinding().ivRoundImage);
                        } else if (holder instanceof ChatOtherVideoViewHolder) {
                            ((ChatOtherVideoViewHolder) holder).getBinding().downloadMedia.setVisibility(View.GONE);
                            ((ChatOtherVideoViewHolder) holder).getBinding().circularProgressBar.setVisibility(View.GONE);
                            Uri uri = Uri.fromFile(originalPath.getAbsoluteFile());
                            Glide.with(context).load(uri).placeholder(R.drawable.image_placeholder).into(((ChatOtherVideoViewHolder) holder).getBinding().ivRoundVideo);
                            ((ChatOtherVideoViewHolder) holder).getBinding().ivVideoPlay.setVisibility(View.VISIBLE);
                        }
//                        Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            context.registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch (Exception e) {
            Log.e("TAG", "downloadImageOrVideo:" + "Image download failed.");
//            Toast.makeText(context, "Image download failed.", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateList(int position, View itemView) {
        if (mMessages.get(position).isSelected()) {
            mMessages.get(position).setSelected(false);
        } else {
            mMessages.get(position).setSelected(true);
        }
        mMessages.get(position).setPosition(position);
        notifyDataSetChanged();
    }


    private void setFont(TextView tvMessage, TextView tvTime, SaveTextAppearanceModel data) {
        if ((data.getData().getFontName().length() > 0 || data.getData().getFontName().equalsIgnoreCase("")) && data.getData().getFontName() != null) {
            File fontFile = new File(context.getExternalCacheDir() + "/" + data.getData().getUserId() + "_font.ttf");
            Log.e("font_name------", "/" + data.getData().getUserId() + "_font.ttf");
            Log.e("TAG", "setFont: " + fontFile.getName());
            if (!fontFile.exists()) {
                PRDownloader.download(data.getData().getFontName(), context.getExternalCacheDir().getPath(), "/" + data.getData().getUserId() + "_font.ttf")
                        .build()
                        .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                            @Override
                            public void onStartOrResume() {
                            }
                        })
                        .start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                Typeface typeface = Typeface.createFromFile(context.getExternalCacheDir() + "/" + data.getData().getUserId() + "_font.ttf");
                                tvMessage.setTypeface(typeface);
                                tvTime.setTypeface(typeface);
                            }

                            @Override
                            public void onError(Error error) {
                                Log.e("Font Error", error.getServerErrorMessage());
                            }
                        });
            } else {
                Typeface typeface = Typeface.createFromFile(context.getExternalCacheDir() + "/" + data.getData().getUserId() + "_font.ttf");
                tvMessage.setTypeface(typeface);
                tvTime.setTypeface(typeface);
            }
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }


    public void setItemLongClick(ItemLongClick itemLongClick) {
        this.itemLongClick = itemLongClick;
    }

    public void onBackpressed() {
        longPressEnable = false;
    }

    public interface ItemLongClick {
        void onLongClick(List<Message> messages);
    }

//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        ViewDataBinding viewDataBinding;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            viewDataBinding = DataBindingUtil.bind(itemView);
//
//            ProgressBar progressBar = itemView.findViewById(R.id.progressBarAudioMy);
//
//        }
//
//        public void setBinding(ViewDataBinding viewDataBinding) {
//
//            this.viewDataBinding = viewDataBinding;
//
//        }
//
//        public ViewDataBinding getViewDataBinding() {
//            return viewDataBinding;
//        }
//    }

    public class SelectedViewsData {
        View view;
        Message message;
        int position;

        public SelectedViewsData(View view, Message message, int position) {
            this.view = view;
            this.message = message;
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, TextView text_done);
    }

    public void stopMedia() {
        try {
            ChatTextViewModel.stopMedia();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Message> getSelectedMessages() {
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < mMessages.size(); i++) {
            if (mMessages.get(i).isSelected()) {
                messageList.add(mMessages.get(i));
            }
        }
        return messageList;
    }
}