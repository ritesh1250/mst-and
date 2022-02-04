package com.meest.metme;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.meest.Activities.base.ApiCallActivity;
import com.meest.Meeast;
import com.meest.R;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.adapter.ChatFragmentPagerAdapter;
import com.meest.metme.fragments.ChatListFragment;
import com.meest.metme.fragments.ChatRequestListFragment;
import com.meest.metme.network.NetworkUtil;
import com.meest.utils.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends ApiCallActivity {
    public static ImageView searchIcon;
    public static LinearLayout delete;
    public static EditText search;
    public static TextView chat_count, request_count;
    ImageView metme_logo;
    ImageView backBtn;
    LinearLayout appbar;
    ImageView close_search;
    LinearLayout logo_layout;
    private ViewPager viewPager;
    private RelativeLayout layoutChatLine, layoutRequestLine;
    private RelativeLayout layoutChat, layoutRequest, search_edit_rel;
    private ImageView iconChat, iconRequest;
    private TextView tvChat, tvRequest;
    private int selectedPos = 0;
    private Socket mSocket;
    ChatListFragment chatListFragment;
    ChatRequestListFragment chatRequestListFragment;
    //    BroadcastReceiver MyReceiver = null;
    Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            ChatActivity.this.runOnUiThread(new Runnable() {
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
                        jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
                        jsonObject.put("name", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.F_NAME) + " " + SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.L_NAME));
                        jsonObject.put("isGroup", false);
                        mSocket.emit("createSession", jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Meeast.activityResumed();
        Log.e("Tag", "onReceive: resume" + Meeast.isActivityVisible());
//        broadcastIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
//        MyReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String status = NetworkUtil.getConnectivityStatusString(context);
//                boolean isVisible = Meeast.isActivityVisible();
//                if (isVisible) {
//                    if (status.isEmpty()) {
//                        status = "No Internet Connection";
//                        return;
//                    }else {
//                        Log.e("TAG", "onReceive: " + status);
//                        Log.e("TAG", "onReceive: Visible" + true);
//                    }
//
//                }
//            }
//        };
        findIds();
        try {
            chatListFragment = new ChatListFragment();
            chatRequestListFragment = new ChatRequestListFragment();
            viewPager.setAdapter(new ChatFragmentPagerAdapter(getSupportFragmentManager(), chatListFragment, chatRequestListFragment));
            layoutChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectChats();
                }
            });
            layoutRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectRequests();
                }
            });
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    switch (position) {
                        case 0:
                            selectChats();
                            break;
                        case 1:
                            selectRequests();
                            break;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            Meeast app = (Meeast) getApplication();
            mSocket = app.getSocket();
            mSocket.on("connected", onConnect);
            mSocket.connect();
            initListener();


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPos == 0) {
                        dialogDeleteChatHead();
                    } else {
                        dialogDeleteRequest();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Toast.makeText(this, Global.isConnected(this) + "", Toast.LENGTH_SHORT).show();

//        checkInternetConnection();
//        broadcastIntent();

    }


    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(MyReceiver);
        Meeast.activityPaused();
        Log.e("Tag", "onReceive:pause" + Meeast.isActivityVisible());
    }

    public void broadcastIntent() {
//        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void checkInternetConnection() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                //For 3G check
                boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
                //For WiFi Check
                boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

                if (!is3g && !isWifi) {
                    Log.e("Internet", "Network Connection is OFF");
//                    runOnUiThread(() -> iconRequest.setVisibility(View.INVISIBLE));

                } else {
                    Log.e("Internet", "Network Connection is ON");
//                    runOnUiThread(() -> iconRequest.setVisibility(View.VISIBLE));
                }
            }
        }, 0, 500);
    }


    private void initListener() {
        close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logo_layout.setVisibility(View.VISIBLE);
                search_edit_rel.setVisibility(View.GONE);
                searchIcon.setVisibility(View.VISIBLE);
                close_search.setVisibility(View.GONE);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_edit_rel.getVisibility() == View.GONE) {
                    logo_layout.setVisibility(View.GONE);
                    search_edit_rel.setVisibility(View.VISIBLE);
                    search.requestFocus();
                    close_search.setVisibility(View.VISIBLE);
                    searchIcon.setVisibility(View.GONE);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                } else {
                    logo_layout.setVisibility(View.VISIBLE);
                    search_edit_rel.setVisibility(View.GONE);
                    close_search.setVisibility(View.GONE);
                    searchIcon.setVisibility(View.VISIBLE);
                }
            }

        });
//        GradientDrawable gd = new GradientDrawable(
//                GradientDrawable.Orientation.TOP_BOTTOM,
//                new int[] {0xFF616261,0xFF131313});
//        gd.setCornerRadius(0f);
//        appbar.setBackgroundDrawable(gd);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
            mSocket.emit("disconnect_user", jsonObject);
            mSocket.disconnect();
            finish();
        } catch (JSONException e) {
            finish();
            e.printStackTrace();
        }
    }

    private void findIds() {
        viewPager = findViewById(R.id.output);
        layoutChatLine = findViewById(R.id.layoutChatLine);
        layoutRequestLine = findViewById(R.id.layoutRequestLine);
        layoutChat = findViewById(R.id.layoutChat);
        layoutRequest = findViewById(R.id.layoutRequest);
        iconChat = findViewById(R.id.iconChat);
        iconRequest = findViewById(R.id.iconRequest);
        tvChat = findViewById(R.id.tvChat);
        tvRequest = findViewById(R.id.tvRequest);
        backBtn = findViewById(R.id.backButton);
        searchIcon = findViewById(R.id.search_icon);
        delete = findViewById(R.id.delete);
        appbar = findViewById(R.id.appbar);
        metme_logo = findViewById(R.id.metme_logo);
        search_edit_rel = findViewById(R.id.search_edit_rel);
        search = findViewById(R.id.search_edittext);
        chat_count = findViewById(R.id.chat_count);
        request_count = findViewById(R.id.request_count);
        close_search = findViewById(R.id.close_search);
        logo_layout = findViewById(R.id.logo_layout);
    }

    private void selectChats() {
        tvChat.setTextColor(ContextCompat.getColor(this, R.color.first_color));
        tvRequest.setTextColor(ContextCompat.getColor(this, R.color.gray_dark));
        iconChat.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_chats_selected));
        iconRequest.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_requests_unselected));
        layoutChatLine.setBackgroundResource(R.drawable.chat_head);
        layoutRequestLine.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        viewPager.setCurrentItem(0);
        selectedPos = 0;
    }

    private void selectRequests() {
        tvChat.setTextColor(ContextCompat.getColor(this, R.color.gray_dark));
        tvRequest.setTextColor(ContextCompat.getColor(this, R.color.first_color));
        iconChat.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_chats_unselected));
        iconRequest.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_requests_selected));
        layoutChatLine.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        layoutRequestLine.setBackgroundResource(R.drawable.chat_head);
        viewPager.setCurrentItem(1);
        selectedPos = 1;
    }

    public static void unreadMessageCountChange(int totalCount) {

        if (totalCount > 0) {
            chat_count.setVisibility(View.VISIBLE);
            chat_count.setText(String.valueOf(totalCount));
        } else {
            chat_count.setVisibility(View.GONE);
            chat_count.setText("0");
        }
    }

    public static void unreadRequestCountChange(int count) {

        if (count > 0) {
            request_count.setVisibility(View.VISIBLE);
            request_count.setText(String.valueOf(count));
        } else {
            request_count.setVisibility(View.GONE);
            request_count.setText("0");
        }

    }


    public void dialogDeleteRequest() {
        try {
            Dialog dialog = new Dialog(this);
            dialog.getWindow().requestFeature(1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setContentView(R.layout.delete_request_popup);
            dialog.setCancelable(false);
            Button yes = dialog.findViewById(R.id.yes);
            Button no = dialog.findViewById(R.id.no);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    List<String> idList = new ArrayList<>();
                    for (int i = 0; i < chatRequestListFragment.chatListModelList.size(); i++) {
                        if (chatRequestListFragment.chatListModelList.get(i).isSelected()) {
                            idList.add(chatRequestListFragment.chatListModelList.get(i).getId());
                        }
                    }

                    Map<String, String> map = new HashMap<>();
                    map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
                    HashMap body = new HashMap<>();
                    body.put("ids", idList);
                    apiCallBack(getApi().bulkDelete(map, body));
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


    public void dialogDeleteChatHead() {
        try {
            Dialog dialog = new Dialog(this);
            dialog.getWindow().requestFeature(1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setContentView(R.layout.delete_request_popup);
            dialog.setCancelable(false);
            Button yes = dialog.findViewById(R.id.yes);
            Button no = dialog.findViewById(R.id.no);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    List<String> idList = new ArrayList<>();
                    for (int i = 0; i < chatListFragment.chatHeadData.size(); i++) {
                        if (chatListFragment.chatHeadData.get(i).isSelected()) {
                            idList.add(chatListFragment.chatHeadData.get(i).getChatHeadId());
                        }
                    }

                    Map<String, String> map = new HashMap<>();
                    map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
                    HashMap body = new HashMap<>();
                    body.put("chatHeadId", idList);
                    apiCallBack(getApi().chatDelete(map, body));


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

}