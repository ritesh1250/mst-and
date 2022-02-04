package com.meest.social.socialViewModel.viewModel.loginViewModel;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.databinding.ActivityForgetVerificationModelBinding;
import com.meest.meestbhart.login.model.ForgotPasswordOtpVerify;
import com.meest.meestbhart.login.model.ForogtPasswordMobile;
import com.meest.meestbhart.register.fragment.otp.model.VerifiyMobileParam;
import com.meest.social.socialViewModel.model.VerifyOtpResponse;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.videomvvmmodule.utils.Global;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class ForgotVerificationCodeModel extends ViewModel {
    public Context context;
    CountDownTimer countDownTimer;
    public ActivityForgetVerificationModelBinding binding;
    public MutableLiveData<String> toast = new MutableLiveData<>();
    public MutableLiveData<Boolean> isVerify = new MutableLiveData<>();
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    private CompositeDisposable disposable = new CompositeDisposable();

    public ForgotVerificationCodeModel(ActivityForgetVerificationModelBinding binding) {
        this.binding = binding;
    }


    public void onOtp() {
        if (!binding.pinView.getText().toString().equals("")) {
            verifyNumberOtp();
        } else {
            toast.setValue("Please Enter OTP");
        }

    }

    public void verifyNumberOtp() {
        ForgotPasswordOtpVerify forgotPasswordOtpVerify = new ForgotPasswordOtpVerify("+91" + SocialPrefrences.getMobileNumber(context), binding.pinView.getText().toString());
        disposable.add(Global.initSocialRetrofit().forgotPasswordVerifyOTP(forgotPasswordOtpVerify)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    isloading.setValue(false);
                })
                .subscribe(new BiConsumer<VerifyOtpResponse, Throwable>() {
                    @Override
                    public void accept(VerifyOtpResponse verifyEmailResponse, Throwable throwable) throws Exception {
                        if (verifyEmailResponse.getCode() == 1) {
                            isVerify.setValue(true);
                            toast.setValue(verifyEmailResponse.getData().getMessage());

                        } else {
                            toast.setValue(verifyEmailResponse.getErrorMessage().getMessage());
                        }
                    }
                }));

    }

    public void resendOtp() {
        VerifiyMobileParam verifyParam = new VerifiyMobileParam("+91" + SocialPrefrences.getMobileNumber(context));
        disposable.add(Global.initSocialRetrofit().forgotPassword(verifyParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    isloading.setValue(false);
                })
                .subscribe(new BiConsumer<ForogtPasswordMobile, Throwable>() {
                    @Override
                    public void accept(ForogtPasswordMobile forogtPasswordMobile, Throwable throwable) throws Exception {
                        if (forogtPasswordMobile.getCode() == 1) {
                            toast.setValue(forogtPasswordMobile.getData().getMessage());

                        } else {
                            toast.setValue(forogtPasswordMobile.getErrorMessage());
                        }
                    }
                }));

    }


    public void countTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.txtCountLayout.setVisibility(View.VISIBLE);
                binding.tctExpire.setText(millisUntilFinished / 1000 + " sec");
                binding.txtSendAgain.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                binding.txtSendAgain.setVisibility(View.VISIBLE);
                binding.txtCountLayout.setVisibility(View.GONE);
            }
        }.start();
    }

}
