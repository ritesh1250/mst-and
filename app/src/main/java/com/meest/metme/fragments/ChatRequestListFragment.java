package com.meest.metme.fragments;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.meest.Activities.base.ApiCallFragemt;
import com.meest.Meeast;
import com.meest.R;
import com.meest.responses.BulkDeleteRequestResponse;
import com.meest.databinding.FragmentChatRequestListBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.ChatActivity;
import com.meest.metme.adapter.ChatRequestListAdapter;
import com.meest.metme.model.ChatListModel;
import com.meest.metme.model.ChatRequestListModel;

import org.greenrobot.eventbus.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatRequestListFragment extends ApiCallFragemt {

    FragmentChatRequestListBinding fragmentChatRequestListBinding;
    ChatRequestListAdapter chatRequestListAdapter;
    public List<ChatRequestListModel> chatListModelList;
    Socket mSocket;

    int position=1;

    Activity context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentChatRequestListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_request_list, container, false);
        fragmentChatRequestListBinding.setLifecycleOwner(this);

        ChatListModel chatListModel = new ChatListModel();
        Meeast app = (Meeast) context.getApplication();
        mSocket = app.getSocket();
        chatListModelList = new ArrayList<>();
        fragmentChatRequestListBinding.selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < chatListModelList.size(); i++) {
                    chatListModelList.get(i).setSelected(true);
                }
                chatRequestListAdapter.notifyDataSetChanged();
            }
        });


        try {
            ChatActivity.  search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        ChatRequestListAdapter.filteredList = chatListModelList;
                        ChatRequestListAdapter.responseList = chatListModelList;
                        chatRequestListAdapter.getFilter().filter(s.toString());
                    } catch (Exception e) {

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentChatRequestListBinding.unSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < chatListModelList.size(); i++) {
                    chatListModelList.get(i).setSelected(false);
                }
                chatRequestListAdapter.notifyDataSetChanged();
            }
        });


        fragmentChatRequestListBinding.recyclerView.setHasFixedSize(true);
        fragmentChatRequestListBinding.recyclerView.setFocusable(false);
        fragmentChatRequestListBinding.recyclerView.setNestedScrollingEnabled(false);
        fragmentChatRequestListBinding.recyclerView.getItemAnimator().setChangeDuration(0);
        fragmentChatRequestListBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return fragmentChatRequestListBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSocket.on("responseMessageCount", responseMessageCount);
        mSocket.on("requestList", requestList);
        mSocket.on("headsRefresh", headsRefresh);
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonMessageCount = new JSONObject();
        mSocket.connect();
        try {
            jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID));
            mSocket.emit("requestList", jsonObject);
            jsonMessageCount.put("userId", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.ID));
            mSocket.emit("MessageCount", jsonMessageCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private Emitter.Listener requestList = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                    try {
                        String username = String.valueOf(args[0]);
                        String new_str = "{" + "\"data\":" + username + "}";
                        Log.e("http response: ", new_str);
                        Gson gson = new Gson();
                        MyJsonConverter data = gson.fromJson(new_str, MyJsonConverter.class);
                        chatListModelList = data.getData();
                        if (null == chatListModelList) {
                            chatListModelList = new ArrayList<>();
                        }

                        chatRequestListAdapter = new ChatRequestListAdapter(context, chatListModelList, fragmentChatRequestListBinding.buttonLayout, ChatActivity. searchIcon, ChatActivity.  delete);
                        fragmentChatRequestListBinding.setChatRequestListAdapter(chatRequestListAdapter);

                        if (chatListModelList.size() == 0) {
                            fragmentChatRequestListBinding.layoutNochats.setVisibility(View.VISIBLE);
                        } else {
                            fragmentChatRequestListBinding.layoutNochats.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        Log.e("http error ", e.getMessage());
                        return;
                    }
                }
            });
        }
    };

    private Emitter.Listener headsRefresh = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.ID));
                        mSocket.emit("requestList", jsonObject);
                        mSocket.emit("MessageCount", jsonObject);
                    } catch (JSONException e) {
                        Log.e("onNewMessage", e.getMessage());
                        return;
                    }

                }
            });
        }
    };

    private Emitter.Listener responseMessageCount = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.w("dataMessageCount", String.valueOf(args[0]));
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(args[0]));
                        ChatActivity.unreadRequestCountChange(jsonObject.getInt("requestCount"));
                    } catch (Exception e) {
                        ChatActivity.unreadRequestCountChange(0);


                        Log.e("TAG", e.getMessage());
                    }
                }
            });
        }
    };


    class MyJsonConverter {
        List<ChatRequestListModel> data = new ArrayList<>();

        public List<ChatRequestListModel> getData() {
            return data;
        }

        public void setData(List<ChatRequestListModel> data) {
            this.data = data;
        }
    }





    @Subscribe
    public void delete(BulkDeleteRequestResponse response) {
        if (response.isSuccess() && response.getData() > 0) {
            chatRequestListAdapter.isChecked = false;
            fragmentChatRequestListBinding.buttonLayout.setVisibility(View.GONE);
            for (int i = 0; i < chatListModelList.size(); i++) {
                if (chatListModelList.get(i).isSelected()) {
                    chatListModelList.remove(i);
                    i--;
                }
            }

            chatRequestListAdapter.notifyDataSetChanged();
        } else {
            dialogFailed(getResources().getString(R.string.error_msg));
        }
    }
}