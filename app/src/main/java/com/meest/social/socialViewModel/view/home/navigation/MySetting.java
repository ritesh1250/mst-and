package com.meest.social.socialViewModel.view.home.navigation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meest.Activities.BlockActivity;
import com.meest.Fragments.InterestBottomSheetFragment;
import com.meest.Fragments.LanguageBottomSheet;
import com.meest.MainActivity;
import com.meest.Paramaters.UserSettingParams;
import com.meest.R;
import com.meest.chat_calling.utils.SharedPreferencesManager;
import com.meest.databinding.SocialMySettingBinding;
import com.meest.social.socialViewModel.view.login.ChangePassword;
import com.meest.social.socialViewModel.viewModel.navigationViewModel.MySettingviewModel;

public class MySetting extends AppCompatActivity {

    MySettingviewModel mySettingviewModel;
    SocialMySettingBinding socialMySettingBinding;
    BottomSheetDialog themedia;
    SharedPreferences sharedPreferences;
    RadioButton defaultTheme, darkTheme;
    RadioGroup group;
    String themeName;
    String userAccountStatus = "Public";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socialMySettingBinding = DataBindingUtil.setContentView(this, R.layout.social_my_setting);
        mySettingviewModel = new ViewModelProvider(this).get(MySettingviewModel.class);
        socialMySettingBinding.setSocialMySettingBinding(mySettingviewModel);
        initView();
        initListeners();
        initObserve();
    }


    private void initView() {
        String lang_id = SharedPreferencesManager.getLanguage();

        socialMySettingBinding.loading.setAnimation("loading.json");
        socialMySettingBinding.loading.playAnimation();
        socialMySettingBinding.loading.loop(true);
        mySettingviewModel.getDataFromServer(MySetting.this);
        socialMySettingBinding.txtLaunguage.setText(lang_id);
    }

    private void initListeners() {

        socialMySettingBinding.layoutChangeLau.setOnClickListener(v -> {
            LanguageBottomSheet bottomSheet = new LanguageBottomSheet();
            bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
        });

        socialMySettingBinding.layoutChangePass.setOnClickListener(v -> {
//            startActivity(new Intent(MySetting.this, ChangePasswordActivity.class));
            startActivity(new Intent(MySetting.this, ChangePassword.class));
        });

        socialMySettingBinding.layoutTheme.setOnClickListener(v -> {

            View conView = getLayoutInflater().inflate(R.layout.themechange, null);
            Button btnConfirm = conView.findViewById(R.id.btnConfirm);
            Button btnClose = conView.findViewById(R.id.btnClose);
            themedia = new BottomSheetDialog(MySetting.this);
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
                        mySettingviewModel.setTheme("Default", MySetting.this);
                        recreate();
                    } else if (checkedId == R.id.darkTheme) {
                        mySettingviewModel.setTheme("DarkTheme", MySetting.this);
                        recreate();
                    }
                }
            });
            btnClose.setOnClickListener(v1 -> {
                themedia.dismiss();
            });
            btnConfirm.setOnClickListener(v1 -> {
                themedia.dismiss();
            });
            themedia.show();

        });
        socialMySettingBinding.backImage.setOnClickListener(v -> {
            finish();
        });
        socialMySettingBinding.layoutBlocklist.setOnClickListener(v -> {
            startActivity(new Intent(MySetting.this, BlockActivity.class));
        });
        socialMySettingBinding.layoutFont.setOnClickListener(v -> {
            font_Dialog();
        });
        socialMySettingBinding.layoutInterest.setOnClickListener(v -> {
            InterestBottomSheetFragment interestBottomSheet = new InterestBottomSheetFragment();
            interestBottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
        });


        socialMySettingBinding.switchPrivacy.setOnClickListener(v -> {
            if (userAccountStatus.equalsIgnoreCase(getString(R.string.Public))) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    socialMySettingBinding.switchPrivacy.setChecked(false);
                    createAccountPrivacyDialog();
                }
            } else {
                socialMySettingBinding.switchPrivacy.setChecked(true);
                UserSettingParams params = new UserSettingParams();
                params.setAccountType("PUBLIC");
                params.setNotification(socialMySettingBinding.switchPrivacy.isChecked());
                userAccountStatus = getString(R.string.Public);
                mySettingviewModel.uploadDataToServer(params, MySetting.this);
            }
        });
        socialMySettingBinding.switchNotification.setOnClickListener(v -> {
            try {
                UserSettingParams params = new UserSettingParams();
                params.setNotification(socialMySettingBinding.switchNotification.isChecked());
                mySettingviewModel.uploadDataToServer(params, MySetting.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initObserve() {

        mySettingviewModel.bindUserDataUser.observe(this, user -> {

            userAccountStatus = user.getAccountType().equalsIgnoreCase(getString(R.string.Public)) ? getString(R.string.Public) : getString(R.string.Private);
            socialMySettingBinding.txtAccountPrivacyStatus.setText(userAccountStatus);
            socialMySettingBinding.switchNotification.setChecked(user.getNotification());
            userAccountStatus = user.getAccountType().equalsIgnoreCase(getString(R.string.Public)) ? getString(R.string.Public) : getString(R.string.Private);
            if (userAccountStatus.equalsIgnoreCase(getString(R.string.Private))) {
                socialMySettingBinding.switchPrivacy.setChecked(true);
            } else {
                socialMySettingBinding.switchPrivacy.setChecked(false);
            }

        });
        mySettingviewModel.updateUser.observe(this, data -> {
            userAccountStatus = data.getAccountType().equalsIgnoreCase(getString(R.string.Public)) ? getString(R.string.Public) : getString(R.string.Private);
            socialMySettingBinding.txtAccountPrivacyStatus.setText(userAccountStatus);
            socialMySettingBinding.switchNotification.setChecked(data.getNotification());
            userAccountStatus = data.getAccountType().equalsIgnoreCase(getString(R.string.Public)) ? getString(R.string.Public) : getString(R.string.Private);
            if (userAccountStatus.equalsIgnoreCase(getString(R.string.Private))) {
                socialMySettingBinding.switchPrivacy.setChecked(true);
            } else {
                socialMySettingBinding.switchPrivacy.setChecked(false);
            }
        });

    }


    public void font_Dialog() {
        final Dialog dialog = new Dialog(MySetting.this);
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
                        SetFont_Method(MySetting.this, "Regular");
                        break;
                    case R.id.rbtn_bold:
                        SharedPreferencesManager.save_font("Bold");
                        SetFont_Method(MySetting.this, "Bold");
                        break;
                    case R.id.rbtn_italic:
                        SharedPreferencesManager.save_font("Italic");
                        SetFont_Method(MySetting.this, "Italic");
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "No font selection..!!", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

        dialog.show();
    }

    public void SetFont_Method(Context context, String fontType) {

        Intent intent = new Intent(MySetting.this, MainActivity.class);
        intent.putExtra("fonts", fontType);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createAccountPrivacyDialog() {
        try {
            Rect displayRectangle = new Rect();
            Window window = MySetting.this.getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_layout_privacy, viewGroup, false);
            dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
            dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
            Button privacy = dialogView.findViewById(R.id.privacy_acnt);
            BottomSheetDialog tagDialog = new BottomSheetDialog(MySetting.this);
            tagDialog.setContentView(dialogView);
            privacy.setOnClickListener(v -> {
                UserSettingParams params = new UserSettingParams();
                params.setAccountType("PRIVATE");
                params.setNotification(socialMySettingBinding.switchNotification.isChecked());
                userAccountStatus = getString(R.string.Private);
                mySettingviewModel.uploadDataToServer(params, MySetting.this);
                tagDialog.dismiss();
            });

            BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) dialogView.getParent());
            mBehavior.setPeekHeight(displayRectangle.height() / 2);
            tagDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("language")) {
                UserSettingParams params = new UserSettingParams();
                params.setNotification(socialMySettingBinding.switchNotification.isChecked());
                params.setAccountType(userAccountStatus.toUpperCase());
                params.setLanguage(data.getStringExtra("language"));
                mySettingviewModel.uploadDataToServer(params, MySetting.this);
            }
        }
    }
}