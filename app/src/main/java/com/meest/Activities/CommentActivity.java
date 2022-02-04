package com.meest.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.CommentAdapter;
import com.meest.Paramaters.CommentGetParam;
import com.meest.Paramaters.InsertCommentParam;
import com.meest.R;
import com.meest.responses.CommentListResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class CommentActivity extends AppCompatActivity {
    ImageView img_back;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    ArrayList<String> like_list = new ArrayList<>();
    ArrayList<CommentListResponse.Row> data111 = new ArrayList<>();
    EmojiconEditText message_input;
    ImageView img_sent;
    String postid = "";
    LottieAnimationView image;
    TextView txt_new_comment;
    Context context = CommentActivity.this;
    Map<String, String> map = new HashMap<>();
    int pageno = 1;
    int pastVisibleItemCount = 0;
    int totalItemCount = 0;
    int visibleItemCount = 0;
    int pagePerRecord = 20;
    int totalCount = 0;
    boolean Loading = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        recyclerView = findViewById(R.id.recyclerView);
        commentAdapter = new CommentAdapter(CommentActivity.this, data111, like_list);
        txt_new_comment = findViewById(R.id.txt_new_comment);
        txt_new_comment.setText(getIntent().getStringExtra("msg"));
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        postid = getIntent().getStringExtra("postid");

        map.put("x-token", SharedPrefreances.getSharedPreferenceString(CommentActivity.this, SharedPrefreances.AUTH_TOKEN));

        image = findViewById(R.id.loading);

        image.setAnimation("loading.json");
        image.playAnimation();
        image.loop(true);
        message_input = findViewById(R.id.message_input);
        img_sent = findViewById(R.id.img_sent);
        ImageView emoji_btn = findViewById(R.id.emoji_btn);
        EmojIconActions emojIcon;
        RelativeLayout rootView = findViewById(R.id.root_view);
        emojIcon = new EmojIconActions(context, rootView, message_input, emoji_btn);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_smiley_icons, R.drawable.ic_smiley_icons);
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Ravi-test", "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Ravi-test", "Keyboard closed");
            }
        });

        img_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                JSONObject jsonObject = new JSONObject();
                try {
                    String etComment_str = message_input.getText().toString();

                    String toServer = etComment_str;
                    String test = "";

                    try {
                        test = URLEncoder.encode(toServer, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    if (message_input.getText().toString().isEmpty()) {
                        Utilss.showToast(getApplicationContext(), getString(R.string.Enter_Message), R.color.msg_fail);
                    } else {
                        image.setVisibility(View.VISIBLE);
                        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);
                        Map<String, String> map = new HashMap<>();
                        map.put("x-token", SharedPrefreances.getSharedPreferenceString(CommentActivity.this, SharedPrefreances.AUTH_TOKEN));
                        InsertCommentParam insertCommentParam = new InsertCommentParam(postid, test, true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        image.setVisibility(View.VISIBLE);
        CommentGetParam commentGetParam = new CommentGetParam(postid);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);

    }


    public void getNewData() {


        image.setVisibility(View.VISIBLE);
        CommentGetParam commentGetParam = new CommentGetParam(postid);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(CommentActivity.this, SharedPrefreances.AUTH_TOKEN));




    }
}
