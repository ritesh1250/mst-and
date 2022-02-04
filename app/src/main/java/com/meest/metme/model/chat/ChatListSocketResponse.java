package com.meest.metme.model.chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChatListSocketResponse implements Parcelable {
    @SerializedName("data")
    @Expose
    private ArrayList<NewChatListResponse> data;

    protected ChatListSocketResponse(Parcel in) {
        data = in.createTypedArrayList(NewChatListResponse.CREATOR);
    }

    public static final Creator<ChatListSocketResponse> CREATOR = new Creator<ChatListSocketResponse>() {
        @Override
        public ChatListSocketResponse createFromParcel(Parcel in) {
            return new ChatListSocketResponse(in);
        }

        @Override
        public ChatListSocketResponse[] newArray(int size) {
            return new ChatListSocketResponse[size];
        }
    };

    public ArrayList<NewChatListResponse> getData() {
        return data;
    }

    public void setData(ArrayList<NewChatListResponse> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
    }
}
