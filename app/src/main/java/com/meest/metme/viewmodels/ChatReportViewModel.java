package com.meest.metme.viewmodels;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.meest.Paramaters.UpdateChatSettingParam;
import com.meest.R;
import com.meest.databinding.ChatReportLayoutBinding;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatReportViewModel extends ViewModel {
    public Boolean isReport = false;
    public String ReportText = "";
    public Context ctx;
    public String chatHeadID;
    public String fontName;

    String forSpam = "normal", forInappropriate = "normal";

    public void reportspam() {

        forSpam="bold";
        forInappropriate="normal";
        ReportText = "Its Spam";

    }

    public void reportinappropiate() {

        forSpam="normal";
        forInappropriate="bold";
        ReportText = "Its Appropiate";

    }

    public void reportclick() {
        if (ReportText.length() < 1) {
            //show message to user please select some option
            Toast.makeText(ctx, "Please select reason", Toast.LENGTH_SHORT).show();
            return;
        }
        isReport = true;
        //UpdateChatSetting api call

        UpdateChatSetting();
    }


    public void UpdateChatSetting() {
        // image.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(ctx, SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        UpdateChatSettingParam updateChatSettingParam = new UpdateChatSettingParam();
        updateChatSettingParam.setReportText(ReportText);
        updateChatSettingParam.setReported(isReport);
        updateChatSettingParam.setChatHeadId(chatHeadID);
        Call<JSONObject> call = webApi.UpdateChatSetting(map, updateChatSettingParam);

        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.e("TAG", "bodychatssetting=" + response.body());
                        Toast.makeText(ctx, "Reported Successfull", Toast.LENGTH_SHORT).show();

                    } else if (response.code() == 500) {
                        Log.e("TAG", "bodychatssetting=" + response.body());
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
