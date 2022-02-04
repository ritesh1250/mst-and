package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.ViewPagerAdapterForAd;
import com.meest.model.AdMediaData;
import com.meest.R;
import com.meest.responses.CampaignPaymentResponse;
import com.meest.responses.CampaignPreviewResponse;
import com.meest.responses.CampaignUpdateResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.utils.HeightWrappingViewPager;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.bumptech.glide.Glide;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CampaignPreviewActivity extends AppCompatActivity implements PaymentResultListener {


    String campaignId, status;
    TextView tvAccountOverview;
    private ImageView back;
    private Button btn_pay_Amount;
    CampaignPreviewResponse campaignPreviewResponse;
    CampaignUpdateResponse campaignDraftedResponse;
    LottieAnimationView loadingView;
    HeightWrappingViewPager CampViewPagerDraft;
    int amount;
    ImageView imvCampProfile;
    TextView txtAdTitleName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_privew);

//        tvTextDetails=findViewById(R.id.tvTextDetails);


        imvCampProfile = findViewById(R.id.imvCampProfile);
        txtAdTitleName = findViewById(R.id.txtAdTitleName);
        //   tvCampaignDate=findViewById(R.id.tvCampaignDate);


        CampViewPagerDraft = findViewById(R.id.CampViewPagerDraft);
        loadingView = findViewById(R.id.loading);
        loadingView.setAnimation("loading.json");
        loadingView.playAnimation();
        loadingView.loop(true);

        if (getIntent().getExtras() == null) {
            Utilss.showToast(getApplicationContext(), getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            return;
        }

        campaignId = getIntent().getExtras().getString("id", "");
        status = getIntent().getExtras().getString("campaignStatus", "");

        amount = getIntent().getExtras().getInt("amount", 0);
        loadingView.setVisibility(View.VISIBLE);

        fetchData();

        btn_pay_Amount = findViewById(R.id.btn_pay_Amount);

        btn_pay_Amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });

        back = (ImageView) findViewById(R.id.img_back_arrow_CampaignPreview);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void saveDraftData() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        // body
        HashMap<String, Object> body = new HashMap<>();
        body.put("id", campaignId);
        body.put("campaignStatus", "Drafted");

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<CampaignUpdateResponse> call = webApi.getupdatecampaign(header, body);

        call.enqueue(new Callback<CampaignUpdateResponse>() {
            @Override
            public void onResponse(Call<CampaignUpdateResponse> call, Response<CampaignUpdateResponse> response) {
                loadingView.clearAnimation();
                loadingView.setVisibility(View.GONE);

                if (response.code() == 200 && response.body().getSuccess()) {
                    campaignDraftedResponse = response.body();
                    Toast.makeText(CampaignPreviewActivity.this, getString(R.string.Save_as_a_draft_successfully), Toast.LENGTH_SHORT).show();
                } else {
                    Utilss.showToast(CampaignPreviewActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<CampaignUpdateResponse> call, Throwable t) {
                loadingView.clearAnimation();
                loadingView.setVisibility(View.GONE);
                Utilss.showToast(CampaignPreviewActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void fetchData() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        // body
        HashMap<String, String> body = new HashMap<>();
        body.put("id", campaignId);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<CampaignPreviewResponse> call = webApi.getCampaignDetail(header, body);
        call.enqueue(new Callback<CampaignPreviewResponse>() {
            @Override
            public void onResponse(Call<CampaignPreviewResponse> call, Response<CampaignPreviewResponse> response) {
                loadingView.clearAnimation();
                loadingView.setVisibility(View.GONE);

                if (response.code() == 200 && response.body().getSuccess()) {
                    campaignPreviewResponse = response.body();
                    bindData();

                } else {
                    Utilss.showToast(CampaignPreviewActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<CampaignPreviewResponse> call, Throwable t) {
                loadingView.clearAnimation();
                loadingView.setVisibility(View.GONE);
                Utilss.showToast(CampaignPreviewActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    public void bindData() {
        // creating media list
        List<AdMediaData> mediaList = new ArrayList<>();
        AdMediaData adMediaData = new AdMediaData();
        adMediaData.setFileUrl(campaignPreviewResponse.getData().getFileURL());
        adMediaData.setButtonType(campaignPreviewResponse.getData().getCallToAction());
        adMediaData.setSubHeading(campaignPreviewResponse.getData().getCampaignText());
        adMediaData.setHeading(campaignPreviewResponse.getData().getCampaignTitle());
        adMediaData.setFileType(campaignPreviewResponse.getData().getFileType());
        adMediaData.setWebsiteUrl(campaignPreviewResponse.getData().getWebsiteUrl());
        mediaList.addAll(campaignPreviewResponse.getData().getOtherImageVideos());
        btn_pay_Amount.setVisibility(View.VISIBLE);
        ViewPagerAdapterForAd viewPagerAdapter = new ViewPagerAdapterForAd(CampaignPreviewActivity.this, mediaList, campaignPreviewResponse.getData().getStartDate() + " " + campaignPreviewResponse.getData().getEndDate(), campaignPreviewResponse.getData().getCampaignType());
        CampViewPagerDraft.setAdapter(viewPagerAdapter);
        CampViewPagerDraft.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //  tvCampaignDate.setText("Created at "+campaignPreviewResponse.getData().getStartDate().substring(0,10));
        txtAdTitleName.setText(SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.USERNAME));
        if (!SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.PROFILE).isEmpty()) {
            Glide.with(this).load(SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.PROFILE)).into(imvCampProfile);
        }

         /*// txtusername.setText(SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.USERNAME));
         // txtcampaighnTitle.setText(mypreviedata.getCampaignTitle());
          String createddate = mypreviedata.getCreatedAt().replace("T"," ");
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
          SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy HH:mm");
          String finaldate = "";
          try {
              Date formateddate = sdf.parse(createddate.split("\\.")[0]);
              finaldate  = sdf1.format(formateddate);
          }
          catch (Exception e)
          {
                e.printStackTrace();
          }
          txtcreatedDate.setText(finaldate);
          jtextview.setText(mypreviedata.getName()+"");

          btntext.setText(mypreviedata.getCallToAction());
          btntext.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                  Intent i = new Intent(Intent.ACTION_VIEW);
                  i.setData(Uri.parse(mypreviedata.getWebsiteUrl()));
                  startActivity(i);
              }
          });
*/

    }

    public void startPayment() {
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Meest");
            options.put("description", "Payment");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            // amount = amount * 100;
            options.put("amount", amount * 100);

//            JSONObject preFill = new JSONObject();
//            preFill.put("email", "kamal.bunkar07@gmail.com");
//            preFill.put("contact", "9144040888");

//            options.put("prefill", preFill);

            co.open(CampaignPreviewActivity.this, options);
        } catch (Exception e) {
            Toast.makeText(CampaignPreviewActivity.this, getString(R.string.Error_in_payment) + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, getString(R.string.Payment_successfully_done) + s, Toast.LENGTH_SHORT).show();
        paymentStatus(s);

    }

    private void paymentStatus(String paymentId) {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        // body
        HashMap<String, Object> body = new HashMap<>();
        body.put("campaignId", campaignId);
        body.put("s", paymentId);
        body.put("campaignStatus", "Drafted");
        body.put("paymentStatus", "Success");

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<CampaignPaymentResponse> call = webApi.getPaymentStatus(header, body);
        call.enqueue(new Callback<CampaignPaymentResponse>() {
            @Override
            public void onResponse(Call<CampaignPaymentResponse> call, Response<CampaignPaymentResponse> response) {

                if (response.code() == 200 && response.body().getSuccess()) {
                    if (paymentId.length() > 0) {
                        Intent intent = new Intent(CampaignPreviewActivity.this, CampaignViewActivity.class);
                        intent.putExtra("id", campaignId);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Utilss.showToast(CampaignPreviewActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<CampaignPaymentResponse> call, Throwable t) {
                Utilss.showToast(CampaignPreviewActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        try {
            Toast.makeText(this, getString(R.string.Error_in_payment) + amount, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }
}