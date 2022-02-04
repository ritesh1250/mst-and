package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.R;
import com.meest.responses.UserSettingsResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteFriendActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_actiivty);
        initViews();

    }

    LottieAnimationView image;
    Button inviteButton;
    ImageView backImg;
    TextView referralCodeTV;

    private void initViews() {
        backImg = findViewById(R.id.inmg_back);
        referralCodeTV = findViewById(R.id.referralCodeTV);
        image = findViewById(R.id.loading);
        inviteButton = findViewById(R.id.inviteButton);
        backImg.setOnClickListener(this);
        inviteButton.setOnClickListener(this);
        getDataFromServer();
    }

    private void shareCode() {
        try {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            String shareBody = getResources().getString(R.string.referral_msg) + referralCodeTV.getText().toString()
                  + " "  + Constant.PLAY_STORE_URL;
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            /*Fire!*/
            startActivity(Intent.createChooser(intent, getString(R.string.share_using)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showHideLoader(boolean showLoader) {
        if (showLoader) {
            if (image.getVisibility() != View.VISIBLE) {
                image.setVisibility(View.VISIBLE);
            }
        } else {
            image.setVisibility(View.GONE);
        }
    }

    private void getDataFromServer() {
        try {
            final WebApi webApi = ApiUtils.getClientHeader(this);
            Map<String, String> map = new HashMap<>();
            map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
            HashMap<String, Object> body = new HashMap<>();
            body.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
            Call<UserSettingsResponse> call = webApi.getUserSettings(map,body);
            showHideLoader(true);
            call.enqueue(new Callback<UserSettingsResponse>() {
                @Override
                public void onResponse(Call<UserSettingsResponse> call, Response<UserSettingsResponse> response) {
                    showHideLoader(false);
                    UserSettingsResponse userResponse = response.body();
                    referralCodeTV.setText(userResponse.getData().getUser().getReferral());
                }

                @Override
                public void onFailure(Call<UserSettingsResponse> call, Throwable t) {
                    showHideLoader(false);
                    System.out.println(t.getStackTrace());
                }
            });
        } catch (Exception e) {
            showHideLoader(false);
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.inviteButton:
                shareCode();
                break;
            case R.id.inmg_back:
                finish();
                break;
        }
    }
}
