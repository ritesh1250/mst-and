package com.meest.social.socialViewModel.viewModel.loginViewModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.meest.Paramaters.UpdatePasswordParams;
import com.meest.R;
import com.meest.databinding.ChangePasswordModelBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.social.socialViewModel.model.UpdatePasswordResponse1;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.videomvvmmodule.utils.Global;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ChangePasswordViewModel extends ViewModel {
    @SuppressLint("StaticFieldLeak")
    Activity activity;
    ChangePasswordModelBinding binding;
    public CompositeDisposable disposable = new CompositeDisposable();

    public ChangePasswordViewModel(Activity activity, ChangePasswordModelBinding binding) {
        this.activity = activity;
        this.binding = binding;
    }

    @SuppressLint("UseCompatLoadingForDrawables")


    public boolean isValid() {
        if (binding.oldPassword.getText().toString().trim().length() < 8 || binding.oldPassword.getText().toString().trim().length() > 15) {
            Toast.makeText(activity, activity.getString(R.string.Please_enter_old_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.editPasswordCofirm.getText().toString().trim().length() < 8 || binding.editPasswordCofirm.getText().toString().trim().length() > 15) {
            Toast.makeText(activity, activity.getString(R.string.Password_should_8_to_15_characters), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.newPassword.getText().toString().trim().length() < 8 || binding.newPassword.getText().toString().trim().length() > 15) {
            Toast.makeText(activity, activity.getString(R.string.Password_should_8_to_15_characters), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!binding.newPassword.getText().toString().trim().equals(binding.editPasswordCofirm.getText().toString().trim())) {
            Toast.makeText(activity, activity.getString(R.string.Password_mismatched), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public void setToggleForPassword() {
        binding.imgPasswordClose1.setOnClickListener((View.OnClickListener) view -> {
            if (binding.imgPasswordClose1.getDrawable().getConstantState() == activity.getDrawable(R.drawable.ic_new_show).getConstantState()) {
                binding.imgPasswordClose1.setImageResource(R.drawable.ic_new_hide);
                binding.oldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                binding.imgPasswordClose1.setImageResource(R.drawable.ic_new_show);
                binding.oldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            binding.oldPassword.setSelection(binding.oldPassword.getText().toString().trim().length());
        });

        binding.imgPasswordClose2.setOnClickListener((View.OnClickListener) view -> {
            if (binding.imgPasswordClose2.getDrawable().getConstantState() == activity.getDrawable(R.drawable.ic_new_show).getConstantState()) {
                binding.imgPasswordClose2.setImageResource(R.drawable.ic_new_hide);
                binding.newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                binding.imgPasswordClose2.setImageResource(R.drawable.ic_new_show);
                binding.newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            binding.newPassword.setSelection(binding.newPassword.getText().toString().trim().length());
        });

        binding.imgPasswordClose3.setOnClickListener((View.OnClickListener) view -> {
            if (binding.imgPasswordClose3.getDrawable().getConstantState() == activity.getDrawable(R.drawable.ic_new_show).getConstantState()) {
                binding.imgPasswordClose3.setImageResource(R.drawable.ic_new_hide);
                binding.editPasswordCofirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                binding.imgPasswordClose3.setImageResource(R.drawable.ic_new_show);
                binding.editPasswordCofirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            binding.editPasswordCofirm.setSelection(binding.editPasswordCofirm.getText().toString().trim().length());
        });
    }

    public void resetBtnPress() {
        if (isValid()) {
            UpdatePasswordParams mParams = new UpdatePasswordParams(binding.oldPassword.getText().toString().trim(), binding.editPasswordCofirm.getText().toString().trim());
            uploadDataToServer(mParams);
        }
    }

    private void uploadDataToServer(UpdatePasswordParams mObject) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("x-token", SharedPrefreances.getSharedPreferenceString(binding.getRoot().getContext(), SharedPrefreances.AUTH_TOKEN));
            binding.loading.setVisibility(View.VISIBLE);
            disposable.add(Global.initSocialRetrofit().updateUserPassword(map, mObject)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable -> binding.loading.setVisibility(View.GONE))
                    .doFinally(() -> binding.loading.setVisibility(View.GONE))
                    .subscribe((UpdatePasswordResponse1 response, Throwable throwable) -> {
                        if (response.getCode()==1 && response.getSuccess()) {
                            Utilss.showToast(activity, activity.getString(R.string.Password_Updated_Successfully),R.color.social_background_blue);
                            activity.finish();
                        } else {
                            Utilss.showToast(activity, response.getErrorMessage(), R.color.social_background_blue);
                        }
                    }));
        } catch (Exception e) {
            binding.loading.setVisibility(View.GONE);
            e.printStackTrace();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
