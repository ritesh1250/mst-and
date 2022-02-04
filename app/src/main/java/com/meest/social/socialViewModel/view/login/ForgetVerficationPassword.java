package com.meest.social.socialViewModel.view.login;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.meest.R;
import com.meest.databinding.ActivityForgetVerificationModelBinding;
import com.meest.databinding.ActivityVerficationModelBinding;
import com.meest.meestbhart.utilss.SmsBroadcastReceiver;
import com.meest.social.socialViewModel.viewModel.loginViewModel.ForgotVerificationCodeModel;
import com.meest.social.socialViewModel.viewModel.loginViewModel.MeedleyOrMeestViewModel;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetVerficationPassword extends AppCompatActivity {
    ActivityForgetVerificationModelBinding binding;
    ForgotVerificationCodeModel model;
    SmsBroadcastReceiver smsBroadcastReceiver;
    private static final int REQ_USER_CONSENT = 200;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ForgetVerficationPassword.this, R.layout.activity_forget_verification_model);
        model = new ViewModelProvider(this, new ViewModelFactory(new ForgotVerificationCodeModel(binding)).createFor()).get(ForgotVerificationCodeModel.class);
        initView();
        initObserve();
        binding.setForgotVerficationModel(model);
        model.context = this;
        model.binding = binding;


    }

    private void initView() {
        model.countTimer();
        binding.otpBtn.setOnClickListener(v -> {
            model.onOtp();
        });

        binding.txtSendAgain.setOnClickListener(v -> {
            model.countTimer();
            model.resendOtp();
        });
    }

    private void initObserve() {
        CustomDialogBuilder customDialogBuilder = new CustomDialogBuilder(this);
        model.isloading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isloading) {
                if (isloading) {
                    customDialogBuilder.showLoadingDialog();
                } else {
                    customDialogBuilder.hideLoadingDialog();
                }
            }
        });
        model.toast.observe(this, s -> {
            if (s != null && !s.isEmpty()) {
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            }
        });
        model.isVerify.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isVerify) {
                if (isVerify) {
                    Intent intent = new Intent(ForgetVerficationPassword.this, ResetPassword.class);
                    intent.putExtra("Otp", binding.pinView.getText().toString());
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }

    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }

                    @Override
                    public void onFailure() {
                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                // Toast.makeText(RegisterOtpActivity.this, message, Toast.LENGTH_LONG).show();
       /*         textViewMessage.setText(
                        String.format("%s - %s", getString(R.string.received_message), message));*/
                getOtpFromMessage(message);
            }
        }

    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            binding.pinView.setText(matcher.group(0));
        }
    }


}
