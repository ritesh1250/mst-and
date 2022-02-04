package com.meest.metme.model.chat;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.branch.referral.util.Product;

public class NewChatListResponse implements Parcelable {
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("displayPicture")
    @Expose
    private String displayPicture;
    @SerializedName("isOnline")
    @Expose
    private Boolean isOnline;
    @SerializedName("about")
    @Expose
    private String about;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("agoraId")
    @Expose
    private Object agoraId;
    @SerializedName("chatHeadId")
    @Expose
    private String chatHeadId;
    @SerializedName("chats")
    @Expose
    private List<Chat> chats = new ArrayList<>();
    @SerializedName("deleted")
    @Expose
    private Integer deleted;
    @SerializedName("deletedNot")
    @Expose
    private Integer deletedNot;
    @SerializedName("isBlock")
    @Expose
    private Integer isBlock;
    @SerializedName("blockByme")
    @Expose
    private Integer blockByme;
    boolean selected;

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    @SerializedName("isBlocked")
    @Expose
    private Boolean isBlocked;


    public NewChatListResponse(){

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected NewChatListResponse(Parcel in) {
        dob = in.readString();
        thumbnail = in.readString();
        displayPicture = in.readString();
        byte tmpIsOnline = in.readByte();
        isOnline = tmpIsOnline == 0 ? null : tmpIsOnline == 1;
        about = in.readString();
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        username = in.readString();
        mobile = in.readString();
        chatHeadId = in.readString();
        chats = in.createTypedArrayList(Chat.CREATOR);
        selected = in.readByte() != 0;
        if (in.readByte() == 0) {
            deleted = null;
        } else {
            deleted = in.readInt();
        }
        if (in.readByte() == 0) {
            deletedNot = null;
        } else {
            deletedNot = in.readInt();
        }
        if (in.readByte() == 0) {
            isBlock = null;
        } else {
            isBlock = in.readInt();
        }
        if (in.readByte() == 0) {
            blockByme = null;
        } else {
            blockByme = in.readInt();
        }
    }

    public static final Creator<NewChatListResponse> CREATOR = new Creator<NewChatListResponse>() {
        @Override
        public NewChatListResponse createFromParcel(Parcel in) {
            return new NewChatListResponse(in);
        }

        @Override
        public NewChatListResponse[] newArray(int size) {
            return new NewChatListResponse[size];
        }
    };

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDisplayPicture() {
        return displayPicture;
    }

    public void setDisplayPicture(String displayPicture) {
        this.displayPicture = displayPicture;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Object getAgoraId() {
        return agoraId;
    }

    public void setAgoraId(Object agoraId) {
        this.agoraId = agoraId;
    }

    public String getChatHeadId() {
        return chatHeadId;
    }

    public void setChatHeadId(String chatHeadId) {
        this.chatHeadId = chatHeadId;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getDeletedNot() {
        return deletedNot;
    }

    public void setDeletedNot(Integer deletedNot) {
        this.deletedNot = deletedNot;
    }

    public Integer getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(Integer isBlock) {
        this.isBlock = isBlock;
    }

    public Integer getBlockByme() {
        return blockByme;
    }

    public void setBlockByme(Integer blockByme) {
        this.blockByme = blockByme;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dob);
        dest.writeString(thumbnail);
        dest.writeString(displayPicture);
        dest.writeByte((byte) (isOnline == null ? 0 : isOnline ? 1 : 2));
        dest.writeString(about);
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(mobile);
        dest.writeString(chatHeadId);
        dest.writeTypedList(chats);
        dest.writeByte((byte) (selected ? 1 : 0));
        if (deleted == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(deleted);
        }
        if (deletedNot == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(deletedNot);
        }
        if (isBlock == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isBlock);
        }
        if (blockByme == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(blockByme);
        }
    }

    public static class Chat implements Parcelable {

        @SerializedName("last_msg")
        @Expose
        private String lastMsg;
        @SerializedName("readStatus")
        @Expose
        private String readStatus;
        @SerializedName("headMessage")
        @Expose
        private String headMessage;
        @SerializedName("read")
        @Expose
        private Boolean read;
        @SerializedName("unreadCount")
        @Expose
        private int unreadCount;
        @SerializedName("updateBy")
        @Expose
        private String updateBy;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("sent")
        @Expose
        private Boolean sent;

        public Chat(){

        }

        protected Chat(Parcel in) {
            lastMsg = in.readString();
            headMessage = in.readString();
            readStatus = in.readString();
            byte tmpRead = in.readByte();
            read = tmpRead == 0 ? null : tmpRead == 1;
            unreadCount = in.readInt();
            updateBy = in.readString();
            createdAt = in.readString();
            userId = in.readString();
            byte tmpSent = in.readByte();
            sent = tmpSent == 0 ? null : tmpSent == 1;
        }

        public static final Creator<Chat> CREATOR = new Creator<Chat>() {
            @Override
            public Chat createFromParcel(Parcel in) {
                return new Chat(in);
            }

            @Override
            public Chat[] newArray(int size) {
                return new Chat[size];
            }
        };

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getUnreadCount() {
            return unreadCount;
        }

        public void setUnreadCount(int unreadCount) {
            this.unreadCount = unreadCount;
        }

        public String getLastMsg() {
            return lastMsg;
        }

        public void setLastMsg(String lastMsg) {
            this.lastMsg = lastMsg;
        }

        public String getHeadMessage() {
            return headMessage;
        }

        public void setHeadMessage(String headMessage) {
            this.headMessage = headMessage;
        }

        public Boolean getRead() {
            return read;
        }

        public void setRead(Boolean read) {
            this.read = read;
        }

        public String getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(String readStatus) {
            this.readStatus = readStatus;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Boolean getSent() {
            return sent;
        }

        public void setSent(Boolean sent) {
            this.sent = sent;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(lastMsg);
            dest.writeString(readStatus);
            dest.writeString(headMessage);
            dest.writeByte((byte) (read == null ? 0 : read ? 1 : 2));
            dest.writeInt(unreadCount);
            dest.writeString(updateBy);
            dest.writeString(createdAt);
            dest.writeString(userId);
            dest.writeByte((byte) (sent == null ? 0 : sent ? 1 : 2));
        }
    }
}
