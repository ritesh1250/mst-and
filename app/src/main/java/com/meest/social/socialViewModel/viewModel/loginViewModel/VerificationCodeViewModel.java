package com.meest.social.socialViewModel.viewModel.loginViewModel;

import android.os.CountDownTimer;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.databinding.ActivityVerficationModelBinding;
import com.meest.meestbhart.login.model.ForgotPasswordOtpVerify;
import com.meest.meestbhart.login.model.ForogtPasswordMobile;
import com.meest.meestbhart.register.fragment.otp.model.MobileOtpVerificationParam;
import com.meest.meestbhart.register.fragment.otp.model.OtpVerificartionResponse;
import com.meest.meestbhart.register.fragment.otp.model.VerifiyEmailAndMobileParam;
import com.meest.meestbhart.register.fragment.otp.model.VerifiyMobileParam;
import com.meest.meestbhart.register.fragment.otp.model.VerifyEmailResponse;
import com.meest.social.socialViewModel.model.VerifyOtpResponse;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.videomvvmmodule.utils.Global;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class VerificationCodeViewModel extends ViewModel {
    public ActivityVerficationModelBinding binding;
    public MutableLiveData<String> toast = new MutableLiveData<>();
    public MutableLiveData<Boolean> isVerify = new MutableLiveData<>();
    public MutableLiveData<Boolean> isResend = new MutableLiveData<>();
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    private CompositeDisposable disposable = new CompositeDisposable();


    public VerificationCodeViewModel(ActivityVerficationModelBinding binding) {
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
        MobileOtpVerificationParam otpVerificartionResponse = new MobileOtpVerificationParam(SocialPrefrences.getMobileNumber(binding.getRoot().getContext()), binding.pinView.getText().toString());
        disposable.add(Global.initSocialRetrofit().otpVerification(otpVerificartionResponse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    isloading.setValue(false);
                })
                .subscribe(new BiConsumer<OtpVerificartionResponse, Throwable>() {
                    @Override
                    public void accept(OtpVerificartionResponse verifyEmailResponse, Throwable throwable) throws Exception {
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
        VerifiyEmailAndMobileParam verifiyMobileParam = new VerifiyEmailAndMobileParam(SocialPrefrences.getMobileNumber(binding.getRoot().getContext()));
        disposable.add(Global.initSocialRetrofit().verifyAll(verifiyMobileParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    isloading.setValue(false);
                })
                .subscribe(new BiConsumer<VerifyEmailResponse, Throwable>() {
                    @Override
                    public void accept(VerifyEmailResponse forogtPasswordMobile, Throwable throwable) throws Exception {
                        if (forogtPasswordMobile.getCode() == 1) {
                            isResend.setValue(true);

                        } else {
                            toast.setValue(forogtPasswordMobile.getErrorMessage());
                        }
                    }
                }));

    }


}
