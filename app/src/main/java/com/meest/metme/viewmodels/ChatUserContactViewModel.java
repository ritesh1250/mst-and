package com.meest.metme.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.meest.Paramaters.UpdateChatSettingParam;
import com.meest.R;
import com.meest.responses.GetDocsMediaChatsResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.metme.ChatMediaActivity;
import com.meest.metme.model.ChatUserContactModel;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatUserContactViewModel {
    public String image;
    public static Context context;
    public ChatUserContactModel chatBoatModel;
    public static String userid;
    public static String chatheadId;
    public String followers;
    public String posts;
    public String name;
    public String bio;
    public boolean isMute, isCallMute;
    public MutableLiveData<String> fontName = new MutableLiveData<>("");
    public MutableLiveData<Boolean> checkFont = new MutableLiveData<>(false);
    public String secondColor;
    public String otherUserId;

    public ChatUserContactViewModel(ChatUserContactModel chatBoatModel, Context context, String secondColor, String otherUserId) {
        this.chatBoatModel = chatBoatModel;
        this.image = chatBoatModel.getChatProfileImage();
        this.context = context;
        this.userid = chatBoatModel.getUserId();
        this.chatheadId = chatBoatModel.getChatHeadId();
        this.otherUserId = otherUserId;
        this.name = chatBoatModel.getChatUserName();
        this.followers = chatBoatModel.getFollower();
        this.posts = chatBoatModel.getPost();
        try {
            this.bio = URLDecoder.decode(chatBoatModel.getBio(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.name = chatBoatModel.getChatUserName();
        this.secondColor = secondColor;
        Log.e("TAG", "ChatUserContactViewModel: " + chatBoatModel.getFollower());
    }

    @BindingAdapter("bindChatContactProfile")
    public static void chatContactProfile(ImageView imageView, String imageUrl) {

        if (!imageUrl.isEmpty()) {
            Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
        } else {
            if (context != null)
                imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.placeholder));
        }
    }

    public void openmedia() {
        Intent intent = new Intent(context, ChatMediaActivity.class);
        intent.putExtra("chatHeadId", chatheadId);
        intent.putExtra("secondColor", secondColor);
        intent.putExtra("otherUserId", otherUserId);
        intent.putExtra("fullname", name);
        context.startActivity(intent);
    }

    public void getMedia() {
        Map<String, String> header = new HashMap<>();
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        HashMap body = new HashMap<>();
        body.put("chatHeadId", chatheadId);
        body.put("attachmentType", new String[]{"Image", "Video"});
        Call<GetDocsMediaChatsResponse> call = webApi.getDocsMedia(header, body);
        call.enqueue(new Callback<GetDocsMediaChatsResponse>() {
            @Override
            public void onResponse(Call<GetDocsMediaChatsResponse> call, Response<GetDocsMediaChatsResponse> response) {

                if (response.code() == 200) {
                    Log.e("TAg", "getmmediadocument=" + response.body().getData());
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("err", "err::" + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDocsMediaChatsResponse> call, Throwable t) {
                Log.w("error", t.toString());
            }
        });
    }

    public void changeMuteSetting(boolean isChecked, String chatHeadId, String x_token) {
        Log.e("click mute", String.valueOf(isChecked));
        Map<String, String> map = new HashMap<>();
        map.put("x-token", x_token);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        UpdateChatSettingParam updateChatSettingParam = new UpdateChatSettingParam();
        updateChatSettingParam.setIsNotificationMute(isChecked);
        updateChatSettingParam.setChatHeadId(chatHeadId);
        Call<JSONObject> call = webApi.UpdateChatSetting(map, updateChatSettingParam);

        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                try {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Notification Changed", Toast.LENGTH_SHORT).show();
                        //  setTextBlock()
                    } else if (response.code() == 500) {
                        Log.e("TAG", "bodychatssetting1=" + response.body());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("TAG", "test =============" + t.getMessage());
            }
        });
    }

    @BindingAdapter({"setFont"})
    public static void setFont(TextView textView, String fontName) {
        try {
            if (fontName != null && !fontName.equals(""))
                textView.setTypeface(Typeface.createFromFile(fontName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<String> getFontName() {
        return fontName;
    }

    public void setFontName() {
        fontName.postValue(SharedPrefreances.getSharedPreferenceString(context, chatBoatModel.getChatHeadId()));
    }
}
