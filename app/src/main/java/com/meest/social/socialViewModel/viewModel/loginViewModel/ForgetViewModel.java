package com.meest.social.socialViewModel.viewModel.loginViewModel;

import android.graphics.drawable.Drawable;
import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.databinding.ActivityForgetPasswordModelBinding;
import com.meest.meestbhart.login.model.ForogtPasswordMobile;
import com.meest.meestbhart.register.fragment.otp.model.VerifiyMobileParam;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.videomvvmmodule.utils.Global;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class ForgetViewModel extends ViewModel {
    public ActivityForgetPasswordModelBinding binding;
    public String phoneNumber = "";
    Drawable dr;
    public MutableLiveData<String> toast = new MutableLiveData<>();
    public MutableLiveData<Boolean> isforgot = new MutableLiveData<>();
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    private CompositeDisposable disposable = new CompositeDisposable();

    public ForgetViewModel(ActivityForgetPasswordModelBinding binding) {
        this.binding = binding;
        dr = binding.getRoot().getContext().getResources().getDrawable(R.drawable.ic_error_msg);
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
    }

    public void afterTextChanged(CharSequence c, int type) {
        switch (type) {
            case 1:
                phoneNumber = c.toString();
                break;
        }
    }

    public void forgotLogin() {
        if (validation()) {
            if (isPhone(phoneNumber)) {
                forgotApi(phoneNumber);
            }
        }
    }

    private void forgotApi(String phoneNumber) {
        VerifiyMobileParam verifyParam = new VerifiyMobileParam("+91" + phoneNumber);
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
                            isforgot.setValue(true);
                            SocialPrefrences.setMobileNumber(binding.getRoot().getContext(), phoneNumber);
                        } else {
                            toast.setValue(forogtPasswordMobile.getErrorMessage());
                        }


                    }
                }));

    }

    private boolean validation() {
        if (phoneNumber.equals("")) {
            binding.etLoginMobile.setError(binding.getRoot().getContext().getString(R.string.social_enter_mobile), dr);
            binding.etLoginMobile.requestFocus();
            return false;
        } else if (binding.etLoginMobile.getText().toString().trim().length() < 10) {
            binding.etLoginMobile.setError(binding.getRoot().getContext().getString(R.string.social_less_mobile_number), dr);
            binding.etLoginMobile.requestFocus();
            return false;
        } else if (binding.etLoginMobile.getText().toString().trim().length() > 10) {
            binding.etLoginMobile.setError(binding.getRoot().getContext().getString(R.string.social_more_mobile_number), dr);
            binding.etLoginMobile.requestFocus();
            return false;
        }
        return true;

    }

    private boolean isPhone(String phone) {
        if (phone.length() == 10) {
            return Patterns.PHONE.matcher(phone).matches();
        } else {
            return false;
        }
    }
}
