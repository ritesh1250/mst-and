package com.meest.metme.viewmodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.meest.Paramaters.UpdateChatSettingParam;
import com.meest.R;
import com.meest.responses.ChatSettingResponse;
import com.meest.databinding.RestrictLayoutBinding;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.WebApi;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRestrictedViewModel extends ViewModel {


    public ChatSettingResponse chatSettingResponse = new ChatSettingResponse();
    public String x_token;
    public boolean isRestricted;
    public String fontName;

    public void GetChatSetting(String toUserId, String chatHeadID, String x_token, Context context, RestrictLayoutBinding blockLayoutBinding) {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", x_token);
        HashMap<String, String> body = new HashMap<>();
        body.put("userId", toUserId);
        body.put("chatHeadId", chatHeadID);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<ChatSettingResponse> call = webApi.GetChatsSetting(map, body);
        call.enqueue(new Callback<ChatSettingResponse>() {

            @Override
            public void onResponse(Call<ChatSettingResponse> call, Response<ChatSettingResponse> response) {
                try {
                    if (response.code() == 200) {
                        if (response.body().getCode() == 1) {
                            try {
                                chatSettingResponse.setData(response.body().getData());
                                isRestricted = response.body().getData().isRestrict();
                                if (isRestricted) {
                                    blockLayoutBinding.tvRestrictText.setText(context.getResources().getString(R.string.unrestricted));
                                } else {
                                    blockLayoutBinding.tvRestrictText.setText(context.getResources().getString(R.string.restricted));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ChatSettingResponse> call, Throwable t) {
                Log.e("Atg", "test =" + t.getMessage());
            }
        });
    }

    public void userRestricted(String toUserId, String chatHeadID, String x_token, Context context, RestrictLayoutBinding restrictLayoutBinding, boolean isRestricted) {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", x_token);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        UpdateChatSettingParam updateChatSettingParam = new UpdateChatSettingParam();
        updateChatSettingParam.setRestrict(isRestricted);
        updateChatSettingParam.setChatHeadId(chatHeadID);
        Call<JSONObject> call = webApi.UpdateChatSetting(map, updateChatSettingParam);

        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                try {
                    if (response.isSuccessful()) {
                        if (isRestricted)
                            Toast.makeText(context, context.getResources().getString(R.string.restricted_Successfully), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context, context.getResources().getString(R.string.unrestricted_Successfully), Toast.LENGTH_SHORT).show();

                        GetChatSetting(toUserId, chatHeadID, x_token, context, restrictLayoutBinding);

                    } else if (response.code() == 500) {
                        Log.e("TAG", "bodychatssetting1=" + response.body());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("Atg", "test =" + t.getMessage());
            }
        });
    }
}
