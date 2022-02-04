package com.meest.videomvvmmodule.view.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.meest.R;
import com.meest.databinding.ActivityRedeemBinding;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;
import com.meest.videomvvmmodule.view.profile.SettingsVideoActivity;
import com.meest.videomvvmmodule.view.web.WebViewActivity;
import com.meest.videomvvmmodule.viewmodel.RedeemViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class RedeemActivity extends BaseActivity {

    ActivityRedeemBinding binding;
    RedeemViewModel viewModel;
    CustomDialogBuilder customDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_redeem);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new RedeemViewModel()).createFor()).get(RedeemViewModel.class);
        customDialogBuilder = new CustomDialogBuilder(this);
        initView();
        initListeners();
        initObserve();
        binding.setViewmodel(viewModel);
    }

    private void initView() {
        if (getIntent().getStringExtra("coins") != null) {
            viewModel.coindCount = getIntent().getStringExtra("coins");
            viewModel.coinRate = getIntent().getStringExtra("coinrate");
            binding.tvCount.setText(Global.prettyCount(Integer.parseInt(viewModel.coindCount)));
        }
    }

    private void initListeners() {
        String[] paymentTypes = getResources().getStringArray(R.array.payment);
        String[] coinRedeem = getResources().getStringArray(R.array.redeemPlan);
        String[] coinRedeemPrice = getResources().getStringArray(R.array.redeemPlanPrice);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.color_text_light));
                // ((TextView) parent.getChildAt(0)).setTextSize(5);
                viewModel.requestType = paymentTypes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.redeemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.color_text_light));
                // ((TextView) parent.getChildAt(0)).setTextSize(5);
                viewModel.redeemCoin = coinRedeem[position];
                viewModel.redeemCoinPrice = coinRedeemPrice[position];
                binding.redeemPlanRate.setText(coinRedeem[position] + getString(R.string.Diamonds) + " = " + " ₹ " + coinRedeemPrice[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.btnRedeem.setOnClickListener(v -> {
            if (viewModel.accountId != null && !TextUtils.isEmpty(viewModel.accountId)) {
                viewModel.callApiToRedeem();
            } else {
                Toast.makeText(this, getApplicationContext().getString(R.string.Please_enter_your_account_ID), Toast.LENGTH_SHORT).show();
            }
        });

        binding.termAndPolicy.setOnClickListener(v -> {
            Intent intent = new Intent(RedeemActivity.this, WebViewActivity.class);
            intent.putExtra("type", 0);
            startActivity(intent);
        });
    }

    private void initObserve() {
        viewModel.redeem.observe(this, redeem -> {
            if (redeem.getStatus()) {
                Toast.makeText(this, getString(R.string.Request_Submitted_Successfully), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainVideoActivity.class));
                finishAffinity();
            }
        });
        viewModel.isLoading.observe(this, isLoading -> {
            if (isLoading) {
                customDialogBuilder.showLoadingDialog();
            } else {
                customDialogBuilder.hideLoadingDialog();
            }
        });
    }

    public void backClick(View view) {
        finish();
    }

}