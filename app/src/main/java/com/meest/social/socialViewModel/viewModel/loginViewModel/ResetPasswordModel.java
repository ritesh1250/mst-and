package com.meest.social.socialViewModel.viewModel.loginViewModel;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.databinding.ActivityResetModelBinding;
import com.meest.meestbhart.login.model.LoginResponse;
import com.meest.meestbhart.login.model.ResetPasswordParam;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.videomvvmmodule.utils.Global;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class ResetPasswordModel extends ViewModel {
    public MutableLiveData<String> toast = new MutableLiveData<>();
    public MutableLiveData<Boolean> isReset = new MutableLiveData<>();
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    private CompositeDisposable disposable = new CompositeDisposable();
    public ActivityResetModelBinding binding;
    public Activity activity;
    private String oldpassword = "", password = "", confirmPassword = "";
    Drawable dr;

    public ResetPasswordModel(Activity activity, ActivityResetModelBinding binding) {
        this.binding = binding;
        this.activity = activity;
        dr = binding.getRoot().getContext().getResources().getDrawable(R.drawable.ic_error_msg);
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
    }


    public void onReset() {
        if (validation()) {
            resetNumberAccount();
        }
    }

    public void afterTextChanged(CharSequence c, int type) {
        switch (type) {
            case 1:
                oldpassword = c.toString();
                break;
            case 2:
                password = c.toString();
                break;
            case 3:
                confirmPassword = c.toString();
                break;
        }

    }



    private void resetNumberAccount() {
        ResetPasswordParam resetPasswordParam = new ResetPasswordParam(activity.getIntent().getStringExtra("Otp"), "+91" + SocialPrefrences.getMobileNumber(binding.getRoot().getContext()), binding.editPasswordCofirm.getText().toString());
        disposable.add(Global.initSocialRetrofit().resetPassword(resetPasswordParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    isloading.setValue(false);
                })
                .subscribe(new BiConsumer<LoginResponse, Throwable>() {
                    @Override
                    public void accept(LoginResponse resetResponse, Throwable throwable) throws Exception {
                        if (resetResponse.getCode() == 1) {
                            isReset.setValue(true);

                        } else {
                            toast.setValue(resetResponse.getErrorMessage());
                        }
                    }
                }));

    }

    private boolean validation() {
        if (binding.editPassword.getText().toString().trim().isEmpty()) {
            binding.editPassword.setError("Please Enter new Password", dr);
            binding.editPassword.requestFocus();
            return false;
        } else if (binding.editPassword.getText().toString().trim().length() < 8 || binding.editPassword.getText().toString().length() > 15) {
            binding.editPassword.setError("Enter password between 8 to 15 character", dr);
            binding.editPassword.requestFocus();
            return false;
        } else if (binding.editPasswordCofirm.getText().toString().trim().isEmpty()) {
            binding.editPasswordCofirm.setError("Please Enter Confirm Password", dr);
            binding.editPasswordCofirm.requestFocus();
            return false;
        } else if (binding.editPasswordCofirm.getText().toString().trim().length() < 8 || binding.editPasswordCofirm.getText().toString().length() > 15) {
            binding.editPasswordCofirm.setError("Enter password between 8 to 15 character", dr);
            binding.editPasswordCofirm.requestFocus();
            return false;
        } else if (!binding.editPassword.getText().toString().trim().equals(binding.editPasswordCofirm.getText().toString())) {
            toast.setValue("Password and Confirm Password does not match");
            return false;

        }
        return true;
    }

    public void newpasswordShow() {
        if (binding.imgPasswordClose1.getDrawable().getConstantState() == binding.getRoot().getContext().getResources().getDrawable(R.drawable.ic_new_show).getConstantState()) {
            binding.imgPasswordClose1.setImageResource(R.drawable.ic_new_hide);
            binding.editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.editPassword.setSelection(binding.editPassword.getText().toString().length());

        } else {
            binding.imgPasswordClose1.setImageResource(R.drawable.ic_new_show);
            binding.editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.editPassword.setSelection(binding.editPassword.getText().toString().length());
        }

    }

    public void confirmpasswordShow() {
        if (binding.imgPasswordClose1.getDrawable().getConstantState() == binding.getRoot().getContext().getResources().getDrawable(R.drawable.ic_new_show).getConstantState()) {
            binding.imgPasswordClose1.setImageResource(R.drawable.ic_new_hide);
            binding.editPasswordCofirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.editPasswordCofirm.setSelection(binding.editPasswordCofirm.getText().toString().length());

        } else {
            binding.imgPasswordClose1.setImageResource(R.drawable.ic_new_show);
            binding.editPasswordCofirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.editPasswordCofirm.setSelection(binding.editPasswordCofirm.getText().toString().length());
        }

    }


}
