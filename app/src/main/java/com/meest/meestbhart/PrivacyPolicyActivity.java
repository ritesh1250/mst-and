package com.meest.meestbhart;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Paramaters.AboutRequestParameter;
import com.meest.R;
import com.meest.responses.UserPrivacyResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private TextView privacyTextView, headerText;
    private ImageView backImage;
    LottieAnimationView image;
    AboutRequestParameter parameter = null;

    private void initViews() {
        image = findViewById(R.id.loading);
        privacyTextView = findViewById(R.id.privacyTextView);
        backImage = findViewById(R.id.inmg_back);
        headerText = findViewById(R.id.headerText);
        image.setAnimation("loading.json");
        image.playAnimation();
        image.loop(true);

        String type = getIntent().getStringExtra("type");

        if (type.equalsIgnoreCase("BlockList")) {
            parameter = new AboutRequestParameter("BlockList");
            headerText.setText(getResources().getString(R.string.blockedList));
        } else if (type.equalsIgnoreCase("Privacy")) {
            parameter = new AboutRequestParameter("Privacy");
            headerText.setText(getResources().getString(R.string.privacy_policy));
        } else if (type.equalsIgnoreCase("About")) {
            parameter = new AboutRequestParameter("About");
            headerText.setText(getResources().getString(R.string.about_us));
        }

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy_avtivity);
        initViews();

        getDataFromServer();
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
            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
            Map<String, String> header = new HashMap<>();
            header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
            Call<UserPrivacyResponse> call = webApi.getPrivacyPolicy(header, parameter);
            showHideLoader(true);
            call.enqueue(new Callback<UserPrivacyResponse>() {
                @Override
                public void onResponse(Call<UserPrivacyResponse> call, Response<UserPrivacyResponse> response) {
                    showHideLoader(false);
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        UserPrivacyResponse serverResponse = response.body();
                        if (serverResponse.getData().size() > 0) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                privacyTextView.setText(Html.fromHtml(serverResponse.getData().get(0).getText(), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                privacyTextView.setText(Html.fromHtml(serverResponse.getData().get(0).getText()));
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserPrivacyResponse> call, Throwable t) {
                    showHideLoader(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
