package com.meest.social.socialViewModel.viewModel.loginViewModel;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;

import com.meest.R;
import com.meest.databinding.LoginSignupModelBinding;
import com.meest.meestbhart.login.model.LoginResponse;
import com.meest.meestbhart.register.fragment.otp.model.RegisterNewResponse;
import com.meest.meestbhart.register.fragment.otp.model.VerifiyEmailAndMobileParam;
import com.meest.meestbhart.register.fragment.otp.model.VerifyEmailResponse;
import com.meest.meestbhart.register.fragment.username.adapter.UsernameAdapter;
import com.meest.meestbhart.register.fragment.username.model.VerifyUserNameResponse;
import com.meest.meestbhart.register.fragment.username.model.VerifyUsernameParam;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.networkcheck.OnNoInternetRetry;
import com.meest.responses.UserResponse;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.social.socialViewModel.view.login.LoginSignUp;
import com.meest.social.socialViewModel.view.login.VerificationCode;
import com.meest.social.socialViewModel.view.splash.SplashScreenVideo;
import com.meest.videomvvmmodule.model.videos.UploadVideoResponse;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import static com.meest.social.socialViewModel.view.login.LoginSignUp.prepareYearMonthDateFromString;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel implements OnNoInternetRetry {
    public Activity activity;
    public SessionManager sessionManager;
    public LoginSignupModelBinding binding;
    public UsernameAdapter usernameAdapter;
    public boolean login = true;
    public String selected_date = "";
    //mutable data
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    public MutableLiveData<String> toast = new MutableLiveData<>();
    public MutableLiveData<RegisterNewResponse> userRegsiterLiveData = new MutableLiveData<>();
    public MutableLiveData<LoginResponse> islogin = new MutableLiveData<>();
    public MutableLiveData<LoginResponse> isloginChkMedley = new MutableLiveData<>();
    public MutableLiveData<Boolean> isRegister = new MutableLiveData<>();
    public MutableLiveData<UploadVideoResponse> isunique = new MutableLiveData<>();
    public MutableLiveData<UserResponse> userResoponse = new MutableLiveData<>();
    public MutableLiveData<Boolean> isUserName = new MutableLiveData<>();
    public MutableLiveData<Boolean> isVerify = new MutableLiveData<>();
    Drawable dr;
    String mobileNo = "", password = "";
    String firstName = "", lastName = "", dob = "", gender = "", userName;
    boolean isActive;
    private CompositeDisposable disposable = new CompositeDisposable();

    public LoginViewModel(Activity activity, LoginSignupModelBinding binding) {
        this.activity = activity;
        this.binding = binding;
        dr = binding.getRoot().getContext().getResources().getDrawable(R.drawable.ic_error_msg);
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
    }

    public void passwordShow() {
        if (binding.imgPasswordClose1.getDrawable().getConstantState() == binding.getRoot().getContext().getResources().getDrawable(R.drawable.ic_new_show).getConstantState()) {
            binding.imgPasswordClose1.setImageResource(R.drawable.ic_new_hide);
            binding.etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.etLoginPassword.setSelection(binding.etLoginPassword.getText().toString().length());

        } else {
            binding.imgPasswordClose1.setImageResource(R.drawable.ic_new_show);
            binding.etLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.etLoginPassword.setSelection(binding.etLoginPassword.getText().toString().length());
        }

    }

    public void passwordHide() {
        if (binding.imgPasswordClose2.getDrawable().getConstantState() == binding.getRoot().getContext().getResources().getDrawable(R.drawable.ic_new_show).getConstantState()) {
            binding.imgPasswordClose2.setImageResource(R.drawable.ic_new_hide);
            binding.editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.editPassword.setSelection(binding.editPassword.getText().toString().length());
        } else {
            binding.imgPasswordClose2.setImageResource(R.drawable.ic_new_show);
            binding.editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.editPassword.setSelection(binding.editPassword.getText().toString().length());
        }
    }


    public void checkValues(String userName, ProgressBar usernameProgress) {
        List<String> arrayList = new ArrayList<>();
        usernameProgress.setVisibility(View.GONE);
        VerifyUsernameParam verifyParam = new VerifyUsernameParam(userName);
        disposable.add(Global.initSocialRetrofit().verifyUsername(verifyParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    isloading.setValue(false);
                })
                .subscribe(new BiConsumer<VerifyUserNameResponse, Throwable>() {
                    @Override
                    public void accept(VerifyUserNameResponse userNameResponse, Throwable throwable) throws Exception {
                        if (userNameResponse.getCode() == 1) {
                            if (userNameResponse.getData().getSuggestions() != null && userNameResponse.getData().getSuggestions().size() > 0) {
                                isUserName.setValue(true);
                                arrayList.clear();
                                arrayList.addAll(userNameResponse.getData().getSuggestions());
                                usernameAdapter.updateData(arrayList);
                            } else {
                               // toast.setValue(userNameResponse.getData().getMessage());
                            }
                        } else {
                            toast.setValue(userNameResponse.getErrorMessage());
                        }


                    }
                }));
    }

    public void isUnique() {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(Global.initRetrofit().getUniqueKey(Global.password, Global.email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
//                                    .doOnSubscribe(it -> customDialogBuilder.showLoadingDialog())
//                                    .doOnTerminate(() -> customDialogBuilder.hideLoadingDialog())
                .subscribe((UploadVideoResponse uploadVideoResponse, Throwable throwable) -> {
                    isunique.setValue(uploadVideoResponse);

                }));

    }

    public void skip() {
        if (!SharedPrefreances.getSharedPreferenceString(binding.getRoot().getContext(), SharedPrefreances.GETINTENT_USER).isEmpty()) {
            //   toast.setValue(context.getString(R.string.social_login_to_see_user));
        } else {
            Global.userId = "";
            Intent intent = new Intent(binding.getRoot().getContext(), SplashScreenVideo.class);
            intent.putExtra("whereFrom", "skip");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            binding.getRoot().getContext().startActivity(intent);
        }
    }


    public void signUpLayout() {
        login = false;
        binding.loginRl.setTranslationX(0);
        binding.signupRl.setTranslationX(0);
        binding.llLogin.setVisibility(View.GONE);
        binding.forgorLl.setVisibility(View.GONE);
        binding.loginSubmit.setVisibility(View.VISIBLE);
        binding.llSignup.setVisibility(View.VISIBLE);
        binding.loginRl.setBackgroundResource(0);
        binding.signupRl.setBackgroundResource(R.drawable.drop_shadow_1);
        binding.tvLogin.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.white));
        binding.tvSignUp.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.social_background_blue));
        if (binding.loginRl.getTranslationX() == 0) {
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(binding.signupRl, "translationX", binding.loginRl.getTranslationX() - 200, binding.signupRl.getTranslationX());
            anim1.setDuration(300);
            anim1.start();
        }
        binding.mainLayout.setBottomLeftCornerRadius(20);
        binding.mainLayout.setBottomRightCornerRadius(20);
        binding.mainLayout.setTopLeftCornerRadius(20);
        binding.mainLayout.setTopRightCornerRadius(0);
        binding.mainLayout.setBackgroundResource(R.drawable.bg_stroke_main_1);
        final ChangeBounds transition = new ChangeBounds();
        transition.setDuration(600L);
        TransitionManager.beginDelayedTransition(binding.mainLayout, new ChangeBounds().setDuration(300L));
    }

    public void loginLayout() {
        login = true;
        binding.signupRl.setTranslationX(0);
        binding.loginRl.setTranslationX(0);
        binding.llLogin.setVisibility(View.VISIBLE);
        binding.llSignup.setVisibility(View.GONE);
        binding.loginSubmit.setVisibility(View.VISIBLE);
        binding.forgorLl.setVisibility(View.VISIBLE);
        binding.loginRl.setBackgroundResource(R.drawable.drop_shadow_1);
        binding.signupRl.setBackgroundResource(0);
        binding.tvLogin.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.social_background_blue));
        binding.tvSignUp.setTextColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.white));
        if (binding.signupRl.getTranslationX() == 0) {
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(binding.loginRl, "translationX", binding.signupRl.getTranslationX() + 200, binding.loginRl.getTranslationX());
            anim1.setDuration(300);
            anim1.start();
        }
        binding.mainLayout.setBottomLeftCornerRadius(20);
        binding.mainLayout.setBottomRightCornerRadius(20);
        binding.mainLayout.setTopLeftCornerRadius(0);
        binding.mainLayout.setTopRightCornerRadius(20);
        binding.mainLayout.setBackgroundResource(R.drawable.bg_stroke_main);
        TransitionManager.beginDelayedTransition(binding.mainLayout, new ChangeBounds().setDuration(300L));
    }

    public void login() {
        if (login) {
            if (loginvalidation()) {
                if (isPhone(mobileNo)) {
                    if (Utilss.isValidMobile(binding.etLoginMobile.getText().toString().trim())) {
                        checkPassword(mobileNo, password);
                    } else {
                        toast.setValue(binding.getRoot().getContext().getString(R.string.social_enter_valid_number));
                    }

                }
            }
        } else {
            if (validation()) {
                newRegister();
            }
        }
    }

    private boolean loginvalidation() {
        if (mobileNo.equals("")) {
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
        } else if (password.equals("")) {
            binding.etLoginPassword.setError(binding.getRoot().getContext().getString(R.string.social_enter_passsword_login), dr);
            binding.etLoginPassword.requestFocus();
            return false;
        } else {
            binding.etLoginMobile.setError(null);
            binding.etLoginMobile.setError(null);
        }
        return true;
    }

    private void checkPassword(String mobileNo, String password) {
        if (ConnectionUtils.isConnected(binding.getRoot().getContext())) {
            HashMap<String, String> body = new HashMap<>();
            body.put("username", "+91" + mobileNo);
            body.put("password", password);
            body.put("fcmToken", SocialPrefrences.getFireBaseToken(binding.getRoot().getContext()));
            disposable.add(Global.initSocialRetrofit().login(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable1 -> isloading.setValue(true))
                    .doOnTerminate(() -> {
                        isloading.setValue(false);
                    })
                    .subscribe(new BiConsumer<LoginResponse, Throwable>() {
                        @Override
                        public void accept(LoginResponse loginResponse, Throwable throwable) throws Exception {
                            if (loginResponse.getCode() == 1) {
                                SocialPrefrences.setMobileNumber(activity, loginResponse.getData().getUser().getMobile());
                                SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.AUTH_TOKEN, loginResponse.getData().getToken());
                                SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.F_NAME, loginResponse.getData().getUser().getFirstName());
                                SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.L_NAME, loginResponse.getData().getUser().getLastName());
                                SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.MOBILE, String.valueOf(loginResponse.getData().getUser().getMobile()));
                                SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.EMAil, loginResponse.getData().getUser().getEmail());
                                SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.GENDER, loginResponse.getData().getUser().getGender());
                                SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.USERNAME, loginResponse.getData().getUser().getUsername());
                                SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.AUTH_TOKEN, loginResponse.getData().getToken());
                                SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.PASSWORD, password);
                                SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.BIO, loginResponse.getData().getUser().getAbout());
                                SharedPrefreances.setSharedPreferenceString(activity, "login", "1");
                                SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.ID, loginResponse.getData().getUser().getId());
                                SharedPrefreances.setSharedPreferenceInt(activity, SharedPrefreances.NOIFICATION_COUNT, 0);
                                if (!SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.GETINTENT_USER).isEmpty()) {
                                    SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.WHEREFROM, "Video");
                                } else if (loginResponse.getData().getUser().getDefaultApp() != null && loginResponse.getData().getUser().getDefaultApp().equals("1")) {
                                    SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.WHEREFROM, "Video");
                                } else {
                                    SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.WHEREFROM, "Social");
                                }
                                islogin.setValue(loginResponse);


                            } else {
                                toast.setValue(loginResponse.getErrorMessage());
                            }


                        }
                    }));
        } else {
            ConnectionUtils.showNoConnectionDialog(activity, this);
        }


    }

    private boolean validation() {
        if (binding.editFName.getText().toString().trim().equals("")) {
            binding.editFName.setError(binding.getRoot().getContext().getString(R.string.social_error_first_name), dr);
            binding.editFName.requestFocus();
            return false;
        } else if (binding.editFName.getText().toString().trim().length() < 3) {
            binding.editFName.setError(binding.getRoot().getContext().getString(R.string.social_error_minimum_3_character));
            binding.editFName.requestFocus();
            return false;
        } else if (binding.editLName.getText().toString().trim().equals("")) {
            binding.editLName.setError(binding.getRoot().getContext().getString(R.string.social_error_last_name), dr);
            binding.editLName.requestFocus();
            return false;
        } else if (binding.editLName.getText().toString().trim().length() < 3) {
            binding.editLName.setError(binding.getRoot().getContext().getString(R.string.social_error_minimum_3_character));
            binding.editLName.requestFocus();
            return false;
        } else if (binding.editUsername.getText().toString().trim().equals("")) {
            binding.editUsername.setError(binding.getRoot().getContext().getString(R.string.social_error_username), dr);
            binding.editUsername.requestFocus();
            return false;
        } else if (binding.editUsername.getText().toString().length() < 4) {
            Utilss.showToast(binding.getRoot().getContext(), binding.getRoot().getContext().getString(R.string.social_error_username_4_letter), R.drawable.social_profile_bg_gradient);
        } else if (binding.editMobileNew.getText().toString().trim().equals("")) {
            binding.editMobileNew.setError("Mobile no. can't Empty", dr);
            binding.editMobileNew.requestFocus();
            return false;
        } else if (binding.editMobileNew.getText().toString().trim().length() < 10) {
            binding.editMobileNew.setError("Mobile no. is less than 10 digits", dr);
            binding.editMobileNew.requestFocus();
            return false;
        } else if (binding.editMobileNew.getText().toString().trim().length() > 10) {
            binding.editMobileNew.setError("Mobile no. is more than 10 digits", dr);
            binding.editMobileNew.requestFocus();
            return false;
        } else if (binding.editPassword.getText().toString().trim().equals("")) {
            binding.editPassword.setError("Password can't Empty", dr);
            binding.editPassword.requestFocus();
            return false;
        } else if (binding.editPassword.getText().toString().length() < 8) {
            binding.editPassword.setError("Password is less than 8 letter", dr);
            binding.editPassword.requestFocus();
            return false;
        } else if (binding.editPassword.getText().toString().length() > 15) {
            binding.editPassword.setError("Password is more than 15 letter", dr);
            binding.editPassword.requestFocus();
            return false;
        } else if (binding.editDob.getText().toString().trim().equals("")) {
            binding.editDob.setError("DOB can not Empty", dr);
            binding.editDob.requestFocus();
            return false;
        } else if (binding.editGender.getText().toString().trim().equals("")) {
            binding.editGender.setError("Gender can not Empty", dr);
            binding.editGender.requestFocus();
            return false;
        }
        return true;
    }


    public void newRegister() {
        selected_date = prepareYearMonthDateFromString(binding.editDob.getText().toString());

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("firstName", firstName);
        hashMap.put("lastName", lastName);
        hashMap.put("password", password);
        hashMap.put("username", binding.editUsername.getText().toString());
        hashMap.put("gender", binding.editGender.getText().toString());
        hashMap.put("mobile", "+91" + mobileNo);
        hashMap.put("email", "");
        hashMap.put("gToken", "");
        hashMap.put("fbToken", "");
        hashMap.put("location", "");
        hashMap.put("accountType", "PUBLIC");
        hashMap.put("deviceVoipToken", "");
        hashMap.put("friendReferral", "");
        hashMap.put("referral", "");
        hashMap.put("notification", "1");
        hashMap.put("mediaAutoDownload", "false");
        hashMap.put("dnd", "false");
        hashMap.put("lat", SocialPrefrences.getLatitude(binding.getRoot().getContext()));
        hashMap.put("lag", SocialPrefrences.getLongitude(binding.getRoot().getContext()));
        hashMap.put("deviceName", SocialPrefrences.getDeviceName(binding.getRoot().getContext()));
        hashMap.put("deviceModel", SocialPrefrences.getDeviceModel(binding.getRoot().getContext()));
        hashMap.put("deviceVersion", SocialPrefrences.getAndroidVersion(binding.getRoot().getContext()));
        hashMap.put("osType", "Android");
        hashMap.put("imeiNumber", SocialPrefrences.getIMEIID(binding.getRoot().getContext()));
        hashMap.put("timeZone", SocialPrefrences.getCurrentTimeZone(binding.getRoot().getContext()));
        hashMap.put("dateZone", SocialPrefrences.getCurrentTimeZone(binding.getRoot().getContext()));
        hashMap.put("dob", selected_date);
        hashMap.put("fcmToken", SocialPrefrences.getFireBaseToken(binding.getRoot().getContext()));
        disposable.add(Global.initSocialRetrofit().register_user(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    isloading.setValue(false);
                })
                .subscribe(new BiConsumer<RegisterNewResponse, Throwable>() {
                    @Override
                    public void accept(RegisterNewResponse loginResponse, Throwable throwable) throws Exception {
                        if (loginResponse.getCode() == 1) {
                            userRegsiterLiveData.setValue(loginResponse);
                            SharedPrefreances.setSharedPreferenceString(activity, SharedPrefreances.AUTH_TOKEN, loginResponse.getData().getToken());
                            SocialPrefrences.setMobileNumber(binding.getRoot().getContext(), loginResponse.getData().getNewUser().getMobile());
                            signUpwithNumber();
                        } else if (loginResponse.getCode() == 0) {
                            isActive = loginResponse.getData().isActive();

                            if (isActive) {
                                toast.setValue(loginResponse.getErrorMessage());
                                //  Utilss.showToast(activity, "Already have an Account, Please Login...", R.color.social_background_blue);
                                //    loginCondition();
                            } else if (!isActive) {
                                toast.setValue(loginResponse.getErrorMessage());

                            } else {
                                Intent intent = new Intent(activity, VerificationCode.class);
                                activity.startActivity(intent);
                                activity.finish();
                            }


                        }
                    }

                }));

    }

    public void signUpwithNumber() {
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
                    public void accept(VerifyEmailResponse verifyEmailResponse, Throwable throwable) throws Exception {
                        if (verifyEmailResponse.getCode() == 1) {
                            isVerify.setValue(true);
                        } else {
                            toast.setValue(verifyEmailResponse.getErrorMessage());
                        }
                    }
                }));
    }


    public void afterTextChanged(CharSequence c, int type) {
        switch (type) {
            case 1:
                mobileNo = c.toString();
                break;
            case 2:
                password = c.toString();
                break;
        }
    }


    public void signUpafterTextChanged(CharSequence c, int type) {
        switch (type) {
            case 1:
                firstName = c.toString();
                break;
            case 2:
                lastName = c.toString();
                break;
            case 3:
                mobileNo = c.toString();
            case 4:
                password = c.toString();


        }
    }

    private boolean isPhone(String phone) {
        if (phone.length() == 10) {
            return Patterns.PHONE.matcher(phone).matches();
        } else {
            return false;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    @Override
    public void onRetry() {
        if (ConnectionUtils.isConnected(binding.getRoot().getContext())) {
            checkPassword(mobileNo, password);
        } else {
            ConnectionUtils.showNoConnectionDialog(activity, this);
        }
    }


}