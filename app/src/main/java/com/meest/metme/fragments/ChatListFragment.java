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
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.meest.Activities.base.ApiCallFragemt;
import com.meest.Meeast;
import com.meest.R;
import com.meest.responses.BulkDeleteChatHeadResponse;
import com.meest.databinding.FragmentChatListBinding;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import com.meest.metme.ChatActivity;
import com.meest.metme.ChatBoatActivity;
import com.meest.metme.adapter.ChatListAdapter;
import com.meest.metme.model.chat.NewChatListResponse;
import com.meest.metme.model.chat.ChatListSocketResponse;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.videomvvmmodule.utils.Global;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import timber.log.Timber;

public class ChatListFragment extends ApiCallFragemt {
    public ArrayList<NewChatListResponse> chatHeadData = new ArrayList<>();
    FragmentChatListBinding fragmentChatListBinding;
    ChatListAdapter chatListAdapter;
    int pageNo;
    int limit = 10;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager;
    int position = 0;
    Activity context;
    Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.w("OnConnectFragment", args.toString());
                }
            });
        }
    };

    private Socket mSocket;

    private boolean loading = true;

    private Emitter.Listener messageRead = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("TAG", "run:"+"messageRead_chatlist" );
                    try {
                       // mSocket.on("headsRefresh", headsRefresh);
                        JSONObject data = (JSONObject) args[0];
                        for (int i = 0; i <chatHeadData.size(); i++) {
                            if (data.getString("chatHeadId").trim().equals(chatHeadData.get(i).getChatHeadId().trim())) {
                                chatHeadData.get(i).getChats().get(0).setReadStatus(Global.ReadStatus.read.name());
                            }
                        }
                        chatListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("TAGMessageRead", e.getMessage());
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
                    Timber.tag("dataMessageCount").w(String.valueOf(args[0]));
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(args[0]));
                        ChatActivity.unreadMessageCountChange(jsonObject.getInt("count"));
                    } catch (Exception e) {
                        ChatActivity.unreadMessageCountChange(0);
                        Log.e("TAG", e.getMessage());
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
                        pageNo = 1;
                        loading = true;
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.ID));
                        jsonObject.put("limit", limit);
                        jsonObject.put("page", pageNo);
                        mSocket.emit("getChatHeads", jsonObject);
                        mSocket.emit("MessageCount", jsonObject);
                    } catch (JSONException e) {
                        Log.e("onNewMessage", e.getMessage());
                        return;
                    }

                }
            });
        }
    };
    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                    try {
                        // image.setVisibility(View.GONE);
                        String username = String.valueOf(args[0]);
                        System.out.println("http chat list: " + username);
                        String new_str = "{" + "\"data\":" + username + "}";
//                        Log.e("userJoinedFragment ", new_str);
                        Log.e("TAG", "run:"+"messageRead_chat_on_joined" );
                        Gson gson = new Gson();
                        ChatListSocketResponse data = gson.fromJson(new_str, ChatListSocketResponse.class);

                        if (pageNo == 1) {
                            if (data.getData().size() > 0) {
                                chatHeadData.clear();
                                chatHeadData.addAll(data.getData());
                                chatListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            if (data.getData().size() > 0) {
                                chatHeadData.addAll(data.getData());
                                chatListAdapter.notifyDataSetChanged();
                                loading = true;
                            } else {
                                loading = false;
                            }
                        }
                        if (chatHeadData.size() == 0) {
                            fragmentChatListBinding.layoutNochats.setVisibility(View.VISIBLE);
                        } else {
                            fragmentChatListBinding.layoutNochats.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        Log.e("TAG", e.getMessage());
                        return;
                    }
                }
            });
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
    }

    @Override
    public void onDestroy() {
        mSocket.disconnect();
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragmentrecyclerView

        fragmentChatListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_list, container, false);
        fragmentChatListBinding.setLifecycleOwner(this);
        Meeast app = (Meeast) context.getApplication();
        mSocket = app.getSocket();

        mLayoutManager = new LinearLayoutManager(getContext());


        fragmentChatListBinding.recyclerView.setLayoutManager(mLayoutManager);
        fragmentChatListBinding.recyclerView.setHasFixedSize(true);
        chatListAdapter = new ChatListAdapter(context, chatHeadData, fragmentChatListBinding.buttonLayout, ChatActivity.searchIcon, ChatActivity.delete);
        fragmentChatListBinding.setChatListAdapter(chatListAdapter);

        try {
            ChatActivity.search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        ChatListAdapter.filteredList = chatHeadData;
                        ChatListAdapter.responseList = chatHeadData;
                        chatListAdapter.getFilter().filter(s.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentChatListBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                loading = false;
                                pageNo = pageNo + 1;
                                jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID));
                                jsonObject.put("limit", limit);
                                jsonObject.put("page", pageNo);
                                mSocket.emit("getChatHeads", jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        });

        fragmentChatListBinding.selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < chatHeadData.size(); i++) {
                    chatHeadData.get(i).setSelected(true);
                }
                chatListAdapter.notifyDataSetChanged();
            }
        });
        fragmentChatListBinding.unSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < chatHeadData.size(); i++) {
                    chatHeadData.get(i).setSelected(false);
                }
                chatListAdapter.notifyDataSetChanged();
            }
        });
        return fragmentChatListBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        mSocket.off("connected", onConnect);
        mSocket.off("chatHeads", onUserJoined);
        mSocket.off("headsRefresh", headsRefresh);
        mSocket.off("responseMessageCount", responseMessageCount);
        mSocket.off("read", messageRead);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSocket.on("connected", onConnect);
        mSocket.on("chatHeads", onUserJoined);
        mSocket.on("headsRefresh", headsRefresh);
        mSocket.on("responseMessageCount", responseMessageCount);
        mSocket.on("read", messageRead);
 
        mSocket.connect();

        JSONObject jsonMessageCount = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            pageNo = 1;
            loading = true;
            jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID));
            jsonObject.put("limit", limit);
            jsonObject.put("page", pageNo);
            mSocket.emit("getChatHeads", jsonObject);
            jsonMessageCount.put("userId", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.ID));
            mSocket.emit("MessageCount", jsonMessageCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void delete(BulkDeleteChatHeadResponse response) {
        if (response.isSuccess() && response.getData() != null) {
            chatListAdapter.isChecked = false;
            fragmentChatListBinding.buttonLayout.setVisibility(View.GONE);
            for (int i = 0; i < chatHeadData.size(); i++) {
                if (chatHeadData.get(i).isSelected()) {
                    chatHeadData.remove(i);
                    i--;
                }
            }
            chatListAdapter.notifyDataSetChanged();
        } else {
            dialogFailed(getResources().getString(R.string.error_msg));
        }
    }
}