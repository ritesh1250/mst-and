package com.meest.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Faq.FaqActivity;
import com.meest.Paramaters.UserFeedbackParameter;
import com.meest.meestbhart.PrivacyPolicyActivity;
import com.meest.R;
import com.meest.responses.UserFeedbackResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity mActivity;
    ImageView backArrow;
    TextView sendFeedbackTV, helpCenterTV, privacyTV, aboutTV, faqTV, community;
    LottieAnimationView image;

    private void initViews() {
        image = findViewById(R.id.loading);
        sendFeedbackTV = findViewById(R.id.sendFeedbackTV);
        helpCenterTV = findViewById(R.id.helpCenterTV);
        privacyTV = findViewById(R.id.privacyTV);
        aboutTV = findViewById(R.id.aboutTV);
        community = findViewById(R.id.community);
        faqTV = findViewById(R.id.faqTV);
        backArrow = findViewById(R.id.img_back_arroe_help);

        backArrow.setOnClickListener(this);
        sendFeedbackTV.setOnClickListener(this);
        helpCenterTV.setOnClickListener(this);
        privacyTV.setOnClickListener(this);
        aboutTV.setOnClickListener(this);
        faqTV.setOnClickListener(this);
        community.setOnClickListener(this);
        image.setAnimation("loading.json");
        image.playAnimation();
        image.loop(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendFeedbackTV:
                showSendFeedbackDialog();
                break;
            case R.id.helpCenterTV:
//                Intent blockedIntent = new Intent(mActivity, PrivacyPolicyActivity.class);
//                blockedIntent.putExtra("type", "BlockList");
//                startActivity(blockedIntent);
                break;
            case R.id.privacyTV:
                Intent privacyPolicyIntent = new Intent(HelpActivity.this, PrivacyPolicyActivity.class);
                privacyPolicyIntent.putExtra("type", "Privacy");
                startActivity(privacyPolicyIntent);
                break;
            case R.id.aboutTV:
                Intent aboutIntent = new Intent(mActivity, PrivacyPolicyActivity.class);
                aboutIntent.putExtra("type", "About");
                startActivity(aboutIntent);
                break;
            case R.id.faqTV:
                Intent faqActivity = new Intent(mActivity, FaqActivity.class);
                startActivity(faqActivity);
                break;
            case R.id.community:
                Intent comActivity = new Intent(mActivity, CommunityActivity.class);
                startActivity(comActivity);
                break;
            case R.id.img_back_arroe_help:
                finish();
                break;
        }
    }

    AlertDialog sendFeedbackAlertDialog = null;
    AlertDialog sendFeedbackConfirmationAlertDialog = null;

    private void showSendFeedbackDialog() {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            final View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_send_feedback, viewGroup, false);
            Button okBtn = dialogView.findViewById(R.id.okBtn);
            EditText userFeedbackEt = dialogView.findViewById(R.id.userFeedbackEt);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userFeedbackEt.getText().toString().trim().length() == 0) {
                        Utilss.showToast(mActivity, getResources().getString(R.string.enter_feedback), R.color.msg_fail);
                    } else {
                        uploadUserFeedback(userFeedbackEt.getText().toString().trim());
                    }
                }
            });
            builder.setView(dialogView);
            sendFeedbackAlertDialog = builder.create();
            sendFeedbackAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSendFeedbackConfirmationDialog(String feedbackText) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            final View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_send_feedback_confirm, viewGroup, false);
            Button okBtn = dialogView.findViewById(R.id.okBtn);
            Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);
            TextView userFeedbackEt = dialogView.findViewById(R.id.userFeedbackEt);
            userFeedbackEt.setText(feedbackText);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userFeedbackEt.getText().toString().trim().length() == 0) {
                        Utilss.showToast(mActivity, getResources().getString(R.string.enter_feedback), R.color.msg_fail);
                    } else {
                        uploadUserFeedback(userFeedbackEt.getText().toString().trim());
                    }
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sendFeedbackAlertDialog != null && sendFeedbackAlertDialog.isShowing()) {
                        sendFeedbackAlertDialog.cancel();
                        sendFeedbackConfirmationAlertDialog.cancel();
                    }
                }
            });

            builder.setView(dialogView);
            sendFeedbackConfirmationAlertDialog = builder.create();
            sendFeedbackConfirmationAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);
        mActivity = this;
        initViews();

       /* report = (TextView) findViewById(R.id.txt_report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(HelpActivity.this, R.style.CustomAlertDialog);
                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog_help, viewGroup, false);

                TextView txt_report_spam = dialogView.findViewById(R.id.txt_report_spam);
                TextView txt_send_feedback = dialogView.findViewById(R.id.txt_send_feedback);
                TextView txt_report_problem = dialogView.findViewById(R.id.txt_Report_a_Problem);


                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                txt_report_spam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                txt_send_feedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });

                txt_report_problem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });


                alertDialog.show();
            }
        });*/


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

    private void uploadUserFeedback(String feedback) {
        try {
            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
            Map<String, String> header = new HashMap<>();
            header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
            UserFeedbackParameter parameter = new UserFeedbackParameter(feedback);
            Call<UserFeedbackResponse> call = webApi.uploadFeedback(header, parameter);
            showHideLoader(true);
            call.enqueue(new Callback<UserFeedbackResponse>() {
                @Override
                public void onResponse(Call<UserFeedbackResponse> call, Response<UserFeedbackResponse> response) {
                    showHideLoader(false);
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        Toast.makeText(mActivity, getResources().getString(R.string.feedback_uploaded), Toast.LENGTH_SHORT).show();
                        try {
                            if (sendFeedbackAlertDialog != null && sendFeedbackAlertDialog.isShowing()) {
                                sendFeedbackAlertDialog.cancel();
                                sendFeedbackConfirmationAlertDialog.cancel();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserFeedbackResponse> call, Throwable t) {
                    showHideLoader(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
