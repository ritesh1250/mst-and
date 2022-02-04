package com.meest.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Fragments.InterestBottomSheetFragment;
import com.meest.Fragments.LanguageBottomSheet;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.meest.MainActivity;
import com.meest.Paramaters.UserSettingParams;
import com.meest.R;
import com.meest.responses.UserSettingUpdateResponse;
import com.meest.responses.UserSettingsResponse;
import com.meest.chat_calling.utils.SharedPreferencesManager;
import com.meest.meestbhart.register.DefaultAppResponse;
import com.meest.meestbhart.register.DefaultParam;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.social.socialViewModel.view.login.ChangePassword;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout account_privacy, layout_change_lau, layout_blocklist, layout_change_pass, layout_theme, layout_font, rl1, layout_interest;
    TextView status;
    private ImageView backImage;
    TextView userLanguage;
    TextView accountPrivacyStatus;
    TextView userPasswordStatus;
    SwitchCompat notificationSwitch;
    SwitchCompat privacyaccountSwitch;
    SwitchCompat mediaAutoDownloadSwitch;
    SwitchCompat switchDefaultApp;
    //  SwitchCompat dndSwitch;
    LottieAnimationView image;
    SharedPreferences sharedPreferences;
    RadioButton defaultTheme, darkTheme;
    RadioGroup group;
    String themeName;
    BottomSheetDialog themedia;
    String TAG = "SettingActivity.java";

    private void initViews() {
        image = findViewById(R.id.loading);
        backImage = findViewById(R.id.backImage);
        layout_blocklist = findViewById(R.id.layout_blocklist);
        layout_change_lau = findViewById(R.id.layout_change_lau);
        layout_change_pass = findViewById(R.id.layout_change_pass);
        account_privacy = findViewById(R.id.layout_privacy);
        layout_theme = findViewById(R.id.layout_theme);
        layout_font = findViewById(R.id.layout_font);
        layout_interest = findViewById(R.id.layout_interest);
        layout_interest.setOnClickListener(this);
        layout_theme.setOnClickListener(this);
        layout_font.setOnClickListener(this);
//        backImage.setOnClickListener(v -> onBackPressed());
        rl1 = findViewById(R.id.rl1);
        rl1.setOnClickListener(v -> onBackPressed());
        layout_change_lau.setOnClickListener(this);
        layout_blocklist.setOnClickListener(this);
        layout_change_pass.setOnClickListener(this);
        account_privacy.setOnClickListener(this);
        accountPrivacyStatus = findViewById(R.id.txt_account_privacy_status);
        userLanguage = findViewById(R.id.txt_launguage);
        userPasswordStatus = findViewById(R.id.txt_pass);
        notificationSwitch = findViewById(R.id.switch_notification);
        privacyaccountSwitch = findViewById(R.id.switch_privacy);
        mediaAutoDownloadSwitch = findViewById(R.id.switch_media_auto_download);
        switchDefaultApp = findViewById(R.id.switch_default);
//        dndSwitch = findViewById(R.id.switch_Do_not_disturb);
        image.setAnimation("loading.json");
        image.playAnimation();
        image.loop(true);

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    UserSettingParams params = new UserSettingParams();
                    params.setNotification(notificationSwitch.isChecked());
                    uploadDataToServer(params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        privacyaccountSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (userAccountStatus.equalsIgnoreCase(getString(R.string.Public))) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        privacyaccountSwitch.setChecked(false);
                        createAccountPrivacyDialog();
                    }
                } else {
                    privacyaccountSwitch.setChecked(true);
                    UserSettingParams params = new UserSettingParams();
                    params.setAccountType("PUBLIC");
                    params.setNotification(notificationSwitch.isChecked());
                    userAccountStatus = getString(R.string.Public);
                    ;
                    uploadDataToServer(params);
                }
            }
        });


        if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.WHEREFROM).equalsIgnoreCase("Video")) {
            switchDefaultApp.setChecked(false);
        } else {
            switchDefaultApp.setChecked(true);
        }

        switchDefaultApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.WHEREFROM).equalsIgnoreCase("Video")) {
                        setdefaultApp(0);
                    } else {
                        setdefaultApp(1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mediaAutoDownloadSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    UserSettingParams params = new UserSettingParams();
                    params.setNotification(notificationSwitch.isChecked());
//                  params.setDnd(dndSwitch.isChecked());
                    params.setMediaAutoDownload(b);
                    params.setAccountType(userAccountStatus.toUpperCase());
                    uploadDataToServer(params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setdefaultApp(int str) {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));

        DefaultParam defaultParam = new DefaultParam(str);
        //  VerifyParam verifyParam1 = new  VerifyParam(edit_email_mobile.getText().toString());
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<DefaultAppResponse> call = webApi.defaultApp(map, defaultParam);
        call.enqueue(new Callback<DefaultAppResponse>() {
            @Override
            public void onResponse(Call<DefaultAppResponse> call, Response<DefaultAppResponse> response) {
                try {
                    if (response.code() == 200 && response.body() != null) {
                        if (response.body().getCode() == 1) {
                            if (response.body().getData().getDefaultApp().equals("1")) {
                                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.WHEREFROM, "Video");
                                Toast.makeText(SettingActivity.this, getString(R.string.Default_App_changed_to_Medley), Toast.LENGTH_SHORT).show();
                            } else {
                                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), SharedPrefreances.WHEREFROM, "Social");
                                Toast.makeText(SettingActivity.this, getString(R.string.Default_App_changed_to_Medley), Toast.LENGTH_SHORT).show();
                            }

                            finish();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<DefaultAppResponse> call, Throwable t) {
                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Theme", Context.MODE_PRIVATE);
        String lang_id = SharedPreferencesManager.getLanguage();
        Log.e(TAG, "LANGUAGE ID" + lang_id);
        themeName = sharedPreferences.getString("ThemeName", "Default");
        Log.v("1111111111111", themeName);
        switch (themeName) {
            case "DarkTheme":
                Log.v("1111111111111", "DarkTheme");
                // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                setTheme(R.style.DarkTheme);
                break;
            case "Default":
                Log.v("1111111111111", "Default");
                setTheme(R.style.AppTheme);
                break;
            default:

                Log.v("1111111111111", "Default  DarkTheme");//Oh no, it's working day
                //This code is executed when value of variable 'day'
                //doesn't match with any of case above
                break;
        }
        setContentView(R.layout.setting_activity);
        initViews();
        getDataFromServer();
        userLanguage.setText(lang_id);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.inmg_back:
                finish();
                break;
            case R.id.layout_blocklist:
                Intent blockActivityIntent = new Intent(SettingActivity.this, BlockActivity.class);
                startActivity(blockActivityIntent);
                break;
            case R.id.layout_change_lau:
                LanguageBottomSheet bottomSheet = new LanguageBottomSheet();
                bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
                break;
            case R.id.layout_change_pass:
//                Intent changePasswordIntent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                Intent changePasswordIntent = new Intent(SettingActivity.this, ChangePassword.class);
                startActivity(changePasswordIntent);
                break;
            case R.id.layout_privacy:

                break;
            case R.id.layout_theme:
                View conView = getLayoutInflater().inflate(R.layout.themechange, null);
                Button btnConfirm = conView.findViewById(R.id.btnConfirm);
                Button btnClose = conView.findViewById(R.id.btnClose);
                themedia = new BottomSheetDialog(SettingActivity.this);
                themedia.setContentView(conView);

                sharedPreferences = getApplicationContext().getSharedPreferences("Theme", Context.MODE_PRIVATE);
                themeName = sharedPreferences.getString("ThemeName", "Default");

                group = conView.findViewById(R.id.group);
                defaultTheme = conView.findViewById(R.id.defaultTheme);
                darkTheme = conView.findViewById(R.id.darkTheme);

                if (themeName.equalsIgnoreCase("DarkTheme")) {
                    darkTheme.setChecked(true);
                } else {
                    defaultTheme.setChecked(true);
                }

                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.defaultTheme) {
                            setTheme("Default");
                        } else if (checkedId == R.id.darkTheme) {
                            setTheme("DarkTheme");
                        }
                    }
                });

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themedia.dismiss();
                    }
                });

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themedia.dismiss();
                    }
                });


                themedia.show();
                break;
            case R.id.layout_font:
                font_Dialog();
                break;

            case R.id.layout_interest:
                InterestBottomSheetFragment interestBottomSheet = new InterestBottomSheetFragment();
                interestBottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");

                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createAccountPrivacyDialog() {
        try {
            Rect displayRectangle = new Rect();
            Window window = SettingActivity.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_layout_privacy, viewGroup, false);
            dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
            dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
            Button privacy = dialogView.findViewById(R.id.privacy_acnt);
            BottomSheetDialog tagDialog = new BottomSheetDialog(SettingActivity.this);
            privacy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserSettingParams params = new UserSettingParams();
                    params.setAccountType("PRIVATE");
                    params.setNotification(notificationSwitch.isChecked());
                    userAccountStatus = getString(R.string.Private);
                    ;
                    uploadDataToServer(params);
                    tagDialog.dismiss();
                }
            });
            tagDialog.setContentView(dialogView);
            BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) dialogView.getParent());
            mBehavior.setPeekHeight(displayRectangle.height() / 2);
            tagDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String userAccountStatus = "Public";

    private void setData(UserSettingsResponse.User user) {
        userAccountStatus = user.getAccountType().equalsIgnoreCase(getString(R.string.Public)) ? getString(R.string.Public) : getString(R.string.Private);
        ;
        accountPrivacyStatus.setText(userAccountStatus);
        //  userLanguage.setText(user.getLanguage());
        notificationSwitch.setChecked(user.getNotification());
        mediaAutoDownloadSwitch.setChecked(user.getMediaAutoDownload());
        userAccountStatus = user.getAccountType().equalsIgnoreCase(getString(R.string.Public)) ? getString(R.string.Public) : getString(R.string.Private);
        if (userAccountStatus.equalsIgnoreCase(getString(R.string.Private))) {
            privacyaccountSwitch.setChecked(true);
        } else {
            privacyaccountSwitch.setChecked(false);
        }
//        accountPrivacyStatus.setText(userAccountStatus);
        //  userLanguage.setText(user.getLanguage());
//        dndSwitch.setChecked(user.getDnd());
    }

    private void updateData(UserSettingUpdateResponse.Data user) {
        userAccountStatus = user.getAccountType().equalsIgnoreCase(getString(R.string.Public)) ? getString(R.string.Public) : getString(R.string.Private);
        accountPrivacyStatus.setText(userAccountStatus);
        //userLanguage.setText(user.getLanguage());
        notificationSwitch.setChecked(user.getNotification());
        mediaAutoDownloadSwitch.setChecked(user.getMediaAutoDownload());
        userAccountStatus = user.getAccountType().equalsIgnoreCase(getString(R.string.Public)) ? getString(R.string.Public) : getString(R.string.Private);
        if (userAccountStatus.equalsIgnoreCase(getString(R.string.Private))) {
            privacyaccountSwitch.setChecked(true);
        } else {
            privacyaccountSwitch.setChecked(false);
        }

//        accountPrivacyStatus.setText(userAccountStatus);
//        dndSwitch.setChecked(user.getDnd());
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

    private void uploadDataToServer(UserSettingParams mObject) {
        try {
            final WebApi webApi = ApiUtils.getClientHeader(this);
            Map<String, String> map = new HashMap<>();
            map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
            showHideLoader(true);
            Call<UserSettingUpdateResponse> call = webApi.updateUserSettings(map, mObject);
            call.enqueue(new Callback<UserSettingUpdateResponse>() {
                @Override
                public void onResponse(Call<UserSettingUpdateResponse> call, Response<UserSettingUpdateResponse> response) {
                    showHideLoader(false);
                    UserSettingUpdateResponse userResponse = response.body();
                    updateData(userResponse.getData());
                }

                @Override
                public void onFailure(Call<UserSettingUpdateResponse> call, Throwable t) {
                    showHideLoader(false);
                    System.out.println(t.getStackTrace());
                }
            });
        } catch (Exception e) {
            showHideLoader(false);
            e.printStackTrace();
        }
    }

    private void getDataFromServer() {
        try {
            final WebApi webApi = ApiUtils.getClientHeader(this);
            Map<String, String> map = new HashMap<>();
            map.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
            HashMap<String, Object> body = new HashMap<>();
            body.put("userId", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
            Call<UserSettingsResponse> call = webApi.getUserSettings(map, body);
            showHideLoader(true);
            call.enqueue(new Callback<UserSettingsResponse>() {
                @Override
                public void onResponse(Call<UserSettingsResponse> call, Response<UserSettingsResponse> response) {
                    showHideLoader(false);

                    UserSettingsResponse userResponse = response.body();
                    setData(userResponse.getData().getUser());
                }

                @Override
                public void onFailure(Call<UserSettingsResponse> call, Throwable t) {
                    showHideLoader(false);
                    System.out.println(t.getStackTrace());
                }
            });
        } catch (Exception e) {
            showHideLoader(false);
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("language")) {
                UserSettingParams params = new UserSettingParams();
                params.setNotification(notificationSwitch.isChecked());
//                params.setDnd(dndSwitch.isChecked());
                params.setMediaAutoDownload(mediaAutoDownloadSwitch.isChecked());
                params.setAccountType(userAccountStatus.toUpperCase());
                params.setLanguage(data.getStringExtra("language"));
                uploadDataToServer(params);
            }
        }
    }

    public void setTheme(String name) {
        // Create preference to store theme name
        SharedPreferences preferences = getSharedPreferences("Theme", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ThemeName", name);
        editor.apply();
        recreate();
    }

    private void setAppLocale(String localeCode) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(localeCode.toLowerCase()));
        resources.updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(localeCode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public void recreateActivity() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void font_Dialog() {
        final Dialog dialog = new Dialog(SettingActivity.this);
        dialog.setContentView(R.layout.custom_font_dialog);
        Button btn_done = (Button) dialog.findViewById(R.id.btn_done);
        RadioGroup rg_btn_font = (RadioGroup) dialog.findViewById(R.id.rg_btn_font);
        rg_btn_font.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int fid = rg_btn_font.getCheckedRadioButtonId();
                switch (fid) {
                    case R.id.rbtn_Regular:
                        SharedPreferencesManager.save_font("Regular");
                        SetFont_Method("Regular");
                        break;
                    case R.id.rbtn_bold:
                        SharedPreferencesManager.save_font("Bold");
                        SetFont_Method("Bold");
                        break;
                    case R.id.rbtn_italic:
                        SharedPreferencesManager.save_font("Italic");
                        SetFont_Method("Italic");
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "No font selection..!!", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

        dialog.show();
    }

    public void SetFont_Method(String fontType) {
        Log.d("CDA", "onBackPressed Called");
        //Intent intent = new Intent(getBaseContext(), MainActivity.class);
        // intent.putExtra("fonts", fontType);
        // startActivity(intent);

        Log.d("CDA", "onBackPressed Called");
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        intent.putExtra("fonts", fontType);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onBack() {
        onBackPressed();
    }
}
