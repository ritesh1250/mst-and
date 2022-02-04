package com.meest.videomvvmmodule.view.wallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.meest.BuildConfig;
import com.meest.R;
import com.meest.chat_calling.utils.SharedPreferencesManager;
import com.meest.databinding.ActivityWalletBinding;
import com.meest.videomvvmmodule.model.wallet.MyWallet;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.viewmodel.WalletViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class WalletActivity extends BaseActivity {

    ActivityWalletBinding binding;
    WalletViewModel viewModel;
    String string = "\u20B9";
    private CustomDialogBuilder customDialogBuilder;
    CoinPurchaseSheetFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new WalletViewModel()).createFor()).get(WalletViewModel.class);
        customDialogBuilder = new CustomDialogBuilder(this);
        initView();
        initListeners();
        initObserve();

        if (getIntent().getBooleanExtra("openCoin", false)) {
            showBuyBottomSheet();
        }

    }

    private void initView() {
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        viewModel.fetchMyWallet();
        viewModel.fetchCoinRate();
        viewModel.fetchRewardingActions();
    }

    private void initListeners() {
        binding.imgBack.setOnClickListener(v -> onBackPressed());
        binding.setOnMoreClick(v -> showBuyBottomSheet());
        binding.setOnRedeemClick(v -> {
            if (viewModel.myWallet.getValue() != null) {
                if (Integer.parseInt(viewModel.myWallet.getValue().getData().getMyWallet()) >= 10000) {
                    Intent intent = new Intent(this, RedeemActivity.class);
                    intent.putExtra("coins", viewModel.myWallet.getValue().getData().getMyWallet());
                    intent.putExtra("coinrate", viewModel.coinRate.getValue() != null ? viewModel.coinRate.getValue().getData().getInr_rate() : "0");
                    startActivity(intent);
                } else {
                    Toast.makeText(WalletActivity.this, getString(R.string.You_can_redeem_minimum_10000_Diamonds), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initObserve() {
        viewModel.myWallet.observe(this, new Observer<MyWallet>() {
            @Override
            public void onChanged(MyWallet wallet) {
                binding.tvCount.setText(Global.prettyCount(Integer.parseInt(wallet.getData().getMyWallet())));
                binding.tvDailyCheckin.setText(Global.prettyCount(Integer.parseInt(wallet.getData().getCheckIn())));
                binding.tvFromYourFans.setText(Global.prettyCount(Integer.parseInt(wallet.getData().getFromFans())));
                binding.tvPurchased.setText(Global.prettyCount(Integer.parseInt(wallet.getData().getPurchased())));
                binding.tvTimeSpent.setText(Global.prettyCount(Integer.parseInt(wallet.getData().getSpenInApp())));
                binding.tvVideoUpload.setText(Global.prettyCount(Integer.parseInt(wallet.getData().getUploadVideo())));
                binding.tvTotalEarning.setText(Global.prettyCount(Integer.parseInt(wallet.getData().getTotalReceived())));
                binding.tvTotalSpending.setText(Global.prettyCount(Integer.parseInt(wallet.getData().getTotalSend())));
            }
        });

        viewModel.isLoading.observe(this, isLoading -> {
            if (isLoading) {
                customDialogBuilder.showLoadingDialog();
            } else {
                customDialogBuilder.hideLoadingDialog();
            }
        });


//        viewModel.coinRate.observe(this, coinRate -> binding.tvCoinRate.setText("1 Coin = ".concat(string + " 1")));
        viewModel.coinRate.observe(this, coinRate -> binding.tvCoinRate.setText("1 Coin = ".concat(coinRate.getData().getInr_rate() + " INR")));
        viewModel.rewardingActions.observe(this, rewardingActions -> {
            if (viewModel.rewardingActionsList.size() > 0) {
                binding.tvEvery10Reward.setText(viewModel.rewardingActionsList.get(0).getCoin());
                binding.tvDailyReward.setText(viewModel.rewardingActionsList.get(1).getCoin());
                binding.tvUploadReward.setText(viewModel.rewardingActionsList.get(2).getCoin());
            } else {
                binding.tvEvery10Reward.setText("00");
                binding.tvDailyReward.setText("00");
                binding.tvUploadReward.setText("00");
            }
        });
    }

    private void showBuyBottomSheet() {
        fragment = new CoinPurchaseSheetFragment();
        fragment.show(getSupportFragmentManager(), fragment.getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.fetchMyWallet();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Same request code for all payment APIs.
//        Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        Log.d("WalletActivity", "API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        Log.d("======WalletActivity", key + " : " + bundle.getString(key));

                    }
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewModel.fetchMyWallet();
                        viewModel.fetchCoinRate();
                        viewModel.fetchRewardingActions();
                    }
                }, 5000);
                fragment.viewModel.add_transaction_details(bundle.getString("orderId"), bundle.getString("referenceId"), bundle.getString("txStatus").equals("SUCCESS"), Global.gatewayCashFree);
            }
        }
    }

}