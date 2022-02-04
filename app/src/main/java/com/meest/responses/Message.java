package com.meest.responses;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {

    boolean isSelected=false;

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_MESSAGE_REC = 1;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;
    public static final int TYPE_Image_Message = 3;
    String selectItemId = "";
    private int mType;
    private String mMessage;
    private String mUsername;
    private String mRead;
    private String mCreated;
    private String mAttachment;

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    private String mFileurl;
    private String mFileUri;
    private String isDownload;
    private String mMessageId;
    private String mMessgaeStatus;
    private String mJsonData;
    private String mSenderName;

    public static String getFontType() {
        return fontType;
    }

    public static void setFontType(String fontType) {
        Message.fontType = fontType;
    }

    private int sender;
    private String oldMsg;
    private static String fontType;
    private String messageDate;
    private String thumbnailURL;
    int position=-1;

    public static int getTypeMessage() {
        return TYPE_MESSAGE;
    }

    public String getmFileUri() {
        return mFileUri;
    }

    public void setmFileUri(String mFileUri) {
        this.mFileUri = mFileUri;
    }

    public String getDownload() {
        return isDownload;
    }

    public void setDownload(String download) {
        isDownload = download;
    }

    public String getOldMsg() {
        return oldMsg;
    }

    public void setOldMsg(String oldMsg) {
        this.oldMsg = oldMsg;
    }

    public String getSelectItemId() {
        return selectItemId;
    }

    public void setSelectItemId(String selectItemId) {
        this.selectItemId = selectItemId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    protected Message(Parcel in) {
        mType = in.readInt();
        mMessage = in.readString();
        mUsername = in.readString();
        mRead = in.readString();
        mCreated = in.readString();
        mAttachment = in.readString();
        mFileurl = in.readString();
        mMessageId = in.readString();
        mMessgaeStatus = in.readString();
        mJsonData = in.readString();
        mSenderName = in.readString();
        mAttachmentType = in.readString();
        sender = in.readInt();
        oldMsg = in.readString();
        messageDate = in.readString();
        thumbnailURL = in.readString();
        position = in.readInt();
        mFileUri = in.readString();
        isDownload = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSenderName() {
        return mSenderName;
    }

    public String getmJsonData() {
        return mJsonData;
    }


    public String getmAttachmentType() {
        return mAttachmentType;
    }

    private String mAttachmentType;

    public String getmMessgaeStatus() {
        return mMessgaeStatus;
    }


    public String getmMessageId() {
        return mMessageId;
    }

    public String getmFileurl() {
        return mFileurl;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getmAttachment() {
        return mAttachment;
    }

    public String getmCreated() {
        return mCreated;
    }


    public Message() {
    }

    public String getmRead() {
        return mRead;
    }

    public int getType() {
        return mType;
    }


    public String getMessage() {
        return mMessage;
    }


    public String getUsername() {
        return mUsername;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mType);
        dest.writeString(mMessage);
        dest.writeString(mUsername);
        dest.writeString(mRead);
        dest.writeString(mCreated);
        dest.writeString(mAttachment);
        dest.writeString(mFileurl);
        dest.writeString(mMessageId);
        dest.writeString(mMessgaeStatus);
        dest.writeString(mJsonData);
        dest.writeString(mSenderName);
        dest.writeString(mAttachmentType);
        dest.writeInt(sender);
        dest.writeString(oldMsg);
        dest.writeString(messageDate);
        dest.writeString(thumbnailURL);
        dest.writeInt(position);
        dest.writeString(mFileUri);
        dest.writeString(isDownload);
    }


    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mMessage;
        private String mRead;
        private String mCreated;
        private String mAttachment;
        private String mFileurl;
        private String mMessageId;
        private String mMessgaeStatus;
        private String mAttachmentType;
        private String mJsonData;
        private String mSenderName;
        private int sender;
        private String mOldMsg;
        private String mMessageDate;
        private String mThumbnailURL;
        private int mPosition;
        private String mFileUri;
        private String isDownload;

        public Builder oldMsg(String oldMsg) {
            mOldMsg = oldMsg;
            return this;
        }

        public Builder mFileUri(String fileUri) {
            mFileUri = fileUri;
            return this;
        }

        public Builder isDownload(String download) {
            isDownload = download;
            return this;
        }

        public Builder thumbnailURL(String thumbnailURL) {
            mThumbnailURL = thumbnailURL;
            return this;
        }

        public Builder messageDate(String messageDate) {
            mMessageDate = messageDate;
            return this;
        }


        public Builder(int type) {
            mType = type;
        }

        public Builder mPosition(int position) {
            mPosition = position;
            return this;
        }


        public Builder senderName(String senderName) {
            mSenderName = senderName;
            return this;
        }


        public Builder jsondata(String jsondata) {
            mJsonData = jsondata;
            return this;
        }

        public Builder attachmentType(String attachmentType) {
            mAttachmentType = attachmentType;
            return this;
        }


        public Builder fileurl(String fileurl) {
            mFileurl = fileurl;
            return this;
        }


        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder read(String read) {
            mRead = read;
            return this;
        }

        public Builder create(String create) {
            mCreated = create;
            return this;
        }

        public Builder attach(String attach) {
            mAttachment = attach;
            return this;
        }

        public Builder messageid(String messageid) {
            mMessageId = messageid;
            return this;
        }


        public Builder status(String status) {
            mMessgaeStatus = status;
            return this;
        }

        public Builder sender(int mSender) {
            sender = mSender;
            return this;
        }


        public Message build() {
            Message message = new Message();
            message.mType = mType;
            message.mUsername = mUsername;
            message.mMessage = mMessage;
            message.mRead = mRead;
            message.mCreated = mCreated;
            message.mFileurl = mFileurl;
            message.mAttachment = mAttachment;
            message.mMessageId = mMessageId;
            message.mMessgaeStatus = mMessgaeStatus;
            message.mAttachmentType = mAttachmentType;
            message.mJsonData = mJsonData;
            message.mSenderName = mSenderName;
            message.sender = sender;
            message.oldMsg = mOldMsg;
            message.messageDate = mMessageDate;
            message.thumbnailURL = mThumbnailURL;
            message.mFileUri = mFileUri;
            message.isDownload = isDownload;
            return message;
        }
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }
}
