package com.meest.videomvvmmodule.view.base;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;

import com.facebook.login.LoginManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meest.BuildConfig;
import com.meest.R;
import com.meest.databinding.ItemLoginBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.videomvvmmodule.model.user.User;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.CrashHandler;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.NetWorkChangeReceiver;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;
import com.meest.videomvvmmodule.view.web.WebViewActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@SuppressLint("NewApi")
public abstract class BaseActivity extends AppCompatActivity {

    public SessionManager sessionManager = null;
    private NetWorkChangeReceiver netWorkChangeReceiver = null;
    private BottomSheetDialog dialog;
    private CompositeDisposable disposable = new CompositeDisposable();

   /* private FacebookCallback<LoginResult> mFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(), (object, response) -> {
                        try {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("device_token", Global.firebaseDeviceToken);
                            hashMap.put("user_email", object.getString("email"));
                            hashMap.put("full_name", object.getString("name"));
                            hashMap.put("login_type", Const.FACEBOOK_LOGIN);
                            hashMap.put("user_name", object.getString("id"));
                            hashMap.put("platform", "android");
                            hashMap.put("identity", object.getString("id"));
                            registerUser(hashMap);
                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(getApplicationContext()));
        }
//        else {
//            Toast.makeText(BaseActivity.this, "Debug Mode", Toast.LENGTH_SHORT).show();
//        }

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
//        else {
//            Timber.plant(new ReleaseTree());
//        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        sessionManager = new SessionManager(this);
    }

    protected void startReceiver() {
        netWorkChangeReceiver = new NetWorkChangeReceiver(this::showHideInternet);
        registerNetworkBroadcastForNougat();
    }

    private void showHideInternet(Boolean isOnline) {
        final TextView tvInternetStatus = findViewById(R.id.tv_internet_status);

        if (isOnline) {
            if (tvInternetStatus != null && tvInternetStatus.getVisibility() == View.VISIBLE && tvInternetStatus.getText().toString().equalsIgnoreCase(getString(R.string.no_internet_connection))) {
                tvInternetStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.kellygreen));
                tvInternetStatus.setText(getString(R.string.back_online));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaseActivity.this.slideToBottom(tvInternetStatus);
                    }
                }, 200);
            }
        } else {
            if (tvInternetStatus != null) {
                tvInternetStatus.setBackgroundColor(ContextCompat.getColor(this, R.color.india_red));
                tvInternetStatus.setText(getString(R.string.no_internet_connection));
                if (tvInternetStatus.getVisibility() == View.GONE) {
                    slideToTop(tvInternetStatus);
                }
            }
        }
    }

    private void slideToTop(View view) {
        TranslateAnimation animation = new TranslateAnimation(0f, 0f, view.getHeight(), 0f);
        animation.setDuration(300);
        view.startAnimation(animation);
        view.setVisibility(View.VISIBLE);
    }

    private void slideToBottom(final View view) {
        TranslateAnimation animation = new TranslateAnimation(0f, 0f, 0f, view.getHeight());
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    private void registerNetworkBroadcastForNougat() {
        registerReceiver(netWorkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(netWorkChangeReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide keyboard when user click anywhere on screen
     *
     * @param event contains int value for motion event actions
     * @return boolean value of touch event.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        closeKeyboard();
        return true;
    }

    public void closeKeyboard() {
        try {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                if (getCurrentFocus() != null) {
                    im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void openActivity(Activity activity) {
        startActivity(new Intent(this, activity.getClass()));
    }

/*    public void initFaceBook() {
        CallbackManager callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, mFacebookCallback);
    }*/

    public void initLogin(Context context, OnLoginSheetClose close) {

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        dialog = new BottomSheetDialog(context);

        ItemLoginBinding loginBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_login, null, false);
        dialog.setContentView(loginBinding.getRoot());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setDismissWithAnimation(true);

        FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        loginBinding.tvTerm.setOnClickListener(v -> startActivity(new Intent(this, WebViewActivity.class).putExtra("type", 0)));
        loginBinding.tvPrivacy.setOnClickListener(v -> startActivity(new Intent(this, WebViewActivity.class).putExtra("type", 1)));

//        loginBinding.setOnGoogleClick(v -> new CustomDialogBuilder(this).showRequestTermDialogue(new CustomDialogBuilder.OnDismissListener() {
//            @Override
//            public void onPositiveDismiss() {
//                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//                if (context instanceof MainVideoActivity) {
//                    ((MainVideoActivity) context).startActivityForResult(signInIntent, RC_SIGN_IN);
//                } else if (context instanceof PlayerActivity) {
//                    ((PlayerActivity) context).startActivityForResult(signInIntent, RC_SIGN_IN);
//                }
//            }
//
//            @Override
//            public void onNegativeDismiss() {
//                dismissBottom();
//            }
//        }));

        loginBinding.setOnFacebookClick(v -> {
            LoginManager.getInstance().logInWithReadPermissions((MainVideoActivity) context, Collections.singletonList("public_profile"));

            LoginManager.getInstance().logInWithReadPermissions(
                    (MainVideoActivity) context,
                    Arrays.asList("user_photos", "email", "user_birthday", "public_profile")
            );
        });

        loginBinding.setOnCloseClick(v -> dismissBottom());

        dialog.setOnDismissListener(view -> close.onClose());

        dialog.show();
    }

    public void dismissBottom() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void registerUser(HashMap<String, Object> hashMap) {
        CustomDialogBuilder customDialogBuilder = new CustomDialogBuilder(this);
        disposable.add(Global.initRetrofit().fetchUser(Global.apikey, hashMap, Global.firebaseDeviceToken)
//        disposable.add(Global.initRetrofit().registrationUser(Global.apikey, hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable it) {
                        customDialogBuilder.showLoadingDialog();
                    }
                })
                .doOnTerminate(() -> customDialogBuilder.hideLoadingDialog())
                .subscribe((User user, Throwable throwable) -> {
                    if (user != null) {
                        if (user.getStatus()) {
                            sessionManager.saveBooleanValue(Const.IS_LOGIN, true);
                            sessionManager.saveUser(user);
                            Global.accessToken = sessionManager.getUser().getData() != null ? sessionManager.getUser().getData().getToken() : "";
                            Global.userId = sessionManager.getUser().getData() != null ? sessionManager.getUser().getData().getUserId() : "";
//                        SharedPrefreances.setSharedPreferenceString(this, SharedPrefreances.WHEREFROM, "Video");
                            dismissBottom();
//                        Toast.makeText(this, "Login SuccessFully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(BaseActivity.this, MainVideoActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, user.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
//                        registerUser
                        Toast.makeText(this, getString(R.string.Something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    public void handleSignInResultpart2() {
        HashMap<String, Object> hashMap = new HashMap<>();
        Log.e("=========meest_id", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.ID));

        hashMap.put("meest_id", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.ID));
//        hashMap.put("meest_id", "086b03ed-2d1d-45ca-88a9-1158146ac234");
        registerUser(hashMap);
    }

/*    public void fetchUser(String meestId) {
        CustomDialogBuilder customDialogBuilder = new CustomDialogBuilder(this);
        disposable.add(Global.initRetrofit().fetchUser(Global.apikey, meestId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(it -> customDialogBuilder.showLoadingDialog())
                .doOnTerminate(() -> customDialogBuilder.hideLoadingDialog())
                .subscribe((User user, Throwable throwable) -> {
                    if (user != null && user.getStatus()) {
                        sessionManager.saveBooleanValue(Const0.IS_LOGIN, true);
//                        sessionManager.saveUser(user);
                        Global.accessToken = sessionManager.getUser().getData() != null ? sessionManager.getUser().getData().getToken() : "";
                        Global.userId = sessionManager.getUser().getData() != null ? sessionManager.getUser().getData().getUserId() : "";
                        dismissBottom();
                        Toast.makeText(this, "Login SuccessFully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BaseActivity.this, MainVideoActivity.class));
                        finish();
                    }
                }));
    }*/

//    public void handleSignInResult() {
//        try {
//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("device_token", Global.firebaseDeviceToken);
//            Log.e("firebase_token====", Global.firebaseDeviceToken);
//            hashMap.put("user_email", "mayanksharmaagra@gmail.com");
//            hashMap.put("first_name", "Mayank");
//            hashMap.put("last_name", "Sharma");
//            hashMap.put("login_type", "default");
//            hashMap.put("platform", "android");
//            hashMap.put("user_name", "killerHug");
//            hashMap.put("identity", "mayanksharmaagra@gmail.com");
//            hashMap.put("gender", getString(R.string.male));
//            hashMap.put("user_profile", "");
//            hashMap.put("cover_profile", "");
//            hashMap.put("user_mobile_no", "6396329713");
//            hashMap.put("dob", "17/09/1995");
//            registerUser(hashMap);
//
//        } catch (Exception e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
////            Log.w("Google login", "signInResult:failed code=" + e.getStatusCode());
//        }
//    }

    protected void setStatusBarTransparentFlag() {

        View decorView = getWindow().getDecorView();
        decorView.setOnApplyWindowInsetsListener((v, insets) -> {
            WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
            return defaultInsets.replaceSystemWindowInsets(
                    defaultInsets.getSystemWindowInsetLeft(),
                    0,
                    defaultInsets.getSystemWindowInsetRight(),
                    defaultInsets.getSystemWindowInsetBottom());
        });
        ViewCompat.requestApplyInsets(decorView);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    protected void removeStatusBarTransparentFlag() {
        View decorView = getWindow().getDecorView();
        decorView.setOnApplyWindowInsetsListener((v, insets) -> {
            WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
            return defaultInsets.replaceSystemWindowInsets(
                    defaultInsets.getSystemWindowInsetLeft(),
                    defaultInsets.getSystemWindowInsetTop(),
                    defaultInsets.getSystemWindowInsetRight(),
                    defaultInsets.getSystemWindowInsetBottom());
        });
        ViewCompat.requestApplyInsets(decorView);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }


    public interface OnLoginSheetClose {
        void onClose();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}