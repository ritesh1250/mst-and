package com.meest.videomvvmmodule.view.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.gson.Gson;
import com.meest.R;
import com.meest.utils.goLiveUtils.CommonUtils;
import com.meest.databinding.ActivitySettingsVideoBinding;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.SplashActivity;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.wallet.WalletActivity;
import com.meest.videomvvmmodule.view.web.WebViewActivity;
import com.meest.videomvvmmodule.viewmodel.SettingsActivityViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.util.Calendar;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.util.LinkProperties;

public class SettingsVideoActivity extends BaseActivity {

    ActivitySettingsVideoBinding binding;
    SettingsActivityViewModel viewModel;
    private CustomDialogBuilder customDialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings_video);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new SettingsActivityViewModel()).createFor()).get(SettingsActivityViewModel.class);
        initListeners();
        initObserve();
        customDialogBuilder = new CustomDialogBuilder(this);
    }

    @SuppressLint("ResourceAsColor")
    private void initListeners() {
        binding.notiSwitch.setOnClickListener(v -> {
            if (binding.notiSwitch.isChecked()) {
                binding.notiSwitch.setThumbResource(R.drawable.green_switch);
                if (!Global.firebaseDeviceToken.isEmpty()) {
                    viewModel.updateToken(Global.firebaseDeviceToken);
                    sessionManager.saveBooleanValue("notification", true);
                }
            } else {
                binding.notiSwitch.setThumbResource(R.drawable.red_switch);
                viewModel.updateToken(" ");
                sessionManager.saveBooleanValue("notification", false);
            }
        });

        binding.lnrHelp.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsVideoActivity.this, WebViewActivity.class);
            intent.putExtra("type", 2);
            startActivity(intent);
        });

        binding.lnrPrivacyPolicies.setOnClickListener(v -> {

            Intent intent = new Intent(SettingsVideoActivity.this, WebViewActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        });

        binding.lnrTerm.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsVideoActivity.this, WebViewActivity.class);
            intent.putExtra("type", 0);
            startActivity(intent);
        });
        binding.lnrCommunityGuide.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsVideoActivity.this, WebViewActivity.class);
            intent.putExtra("type", 3);
            startActivity(intent);
        });

//        if (sessionManager.getUser() != null && sessionManager.getUser().getData() != null && sessionManager.getUser().getData().isVerified()) {
//            binding.loutVerify.setVisibility(View.GONE);
//        } else {
//            binding.loutVerify.setVisibility(View.VISIBLE);
//        }

        binding.loutShareProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SettingsVideoActivity.this.shareProfile();
                FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("https://www.meest4bharat.com/?user=" + Global.userId))
                        .setDomainUriPrefix(Const.deepLinkingUriPrefix)
                        // Open links with this app on Android
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                        // Open links with com.example.ios on iOS
                        .setIosParameters(new DynamicLink.IosParameters.Builder(Const.iosBundleId).build())
                        .buildShortDynamicLink()
                        .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    String shareBody = getString(R.string.Watch_more_of_such) + task.getResult().getShortLink();
                                    share.setType("text/plain");
                                    share.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.Share_Profile));
                                    share.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                    startActivity(Intent.createChooser(share, getString(R.string.Share_Profile)));
                                } else {
                                    Toast.makeText(SettingsVideoActivity.this, getString(R.string.Error_Creating_Link), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        binding.imgBack.setOnClickListener(v -> onBackPressed());
        binding.loutWallet.setOnClickListener(v -> startActivity(new Intent(SettingsVideoActivity.this, WalletActivity.class)));
        binding.loutVerify.setOnClickListener(v -> startActivity(new Intent(SettingsVideoActivity.this, VerificationActivity.class)));
        binding.loutMycode.setOnClickListener(v -> startActivity(new Intent(SettingsVideoActivity.this, MyQRActivity.class)));

        binding.loutLogout.setOnClickListener(v -> new CustomDialogBuilder(this).showSimpleDialog(getString(R.string.Log_out), getString(R.string.Do_you_really_want_to_log_out), getString(R.string.cancel), getString(R.string.Log_out), new CustomDialogBuilder.OnDismissListener() {
            @Override
            public void onPositiveDismiss() {

//                viewModel.logOutUser();
//                customDialogBuilder.showLoadingDialog();

                CommonUtils.logoutNow(SettingsVideoActivity.this);
            }

            @Override
            public void onNegativeDismiss() {

            }
        }));
    }

    private void shareProfile() {
        String json = new Gson().toJson(sessionManager.getUser());
        //String title = sessionManager.getUser().getData().getFullName();

        Log.i("ShareJson", "Json Object: " + json);
        BranchUniversalObject buo = new BranchUniversalObject()
                .setCanonicalIdentifier("content/12345")
                .setTitle("Hello")
//                .setContentImageUrl(sessionManager.getUser().getData().getUserProfile())
                .setContentDescription("Hey There, Check This BubbleTok Profile")
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);
//                .setContentMetadata(new ContentMetadata().addCustomMetadata("data", json));

        LinkProperties lp = new LinkProperties()
                .setFeature("sharing")
                .setCampaign("Content launch")
                .setStage("User")
                .addControlParameter("custom", "data")
                .addControlParameter("custom_random", Long.toString(Calendar.getInstance().getTimeInMillis()));

        buo.generateShortUrl(this, lp, (url, error) -> {

            Log.d("PROFILEURL", "shareProfile: " + url);

            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            String shareBody = url + "\nHey, check my profile on BubbleTok App";
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Profile Share");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(intent, "Share Profile"));
        });

    }

    private void initObserve() {
        viewModel.updateToken.observe(this, updateToken -> {
            if (binding.notiSwitch.isChecked()) {
                Toast.makeText(this, getString(R.string.Notifications_Turned_On), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.Notifications_Turned_Off), Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.logOut.observe(this, logout -> logOutUser());
    }

    private void logOutUser() {
        Log.d("LOGOUT", "logOutUser: " + sessionManager.getUser().getData().getLoginType());
        // logout google
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();
       /* GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(SettingsVideoActivity.this, gso);
        googleSignInClient.signOut();*/

        // logout facebook
        LoginManager.getInstance().logOut();


        sessionManager.clear();
        Global.accessToken = "";
        Global.userId = "";
        customDialogBuilder.hideLoadingDialog();
        startActivity(new Intent(SettingsVideoActivity.this, SplashActivity.class));
        finishAffinity();
    }
}