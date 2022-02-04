package com.meest.videomvvmmodule.view.search;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.budiyev.android.codescanner.CodeScanner;
import com.meest.R;
import com.meest.databinding.ActivityQRScanBinding;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.viewmodel.QRScanActivityViewModel;

public class QRScanActivity extends BaseActivity {
    QRScanActivityViewModel viewModel;
    ActivityQRScanBinding binding;
    public CodeScanner mCodeScanner;

    public CustomDialogBuilder customDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_q_r_scan);
        initListeners();
        viewModel = ViewModelProviders.of(this).get(QRScanActivityViewModel.class);
        binding.setViewmodel(viewModel);
        mCodeScanner.startPreview();

        customDialogBuilder = new CustomDialogBuilder(this);

    }

    private void initListeners() {
        mCodeScanner = new CodeScanner(this, binding.scannerView);
        mCodeScanner.setDecodeCallback(result -> QRScanActivity.this.runOnUiThread(() -> {
            viewModel.follow_or_not(result.getText(), QRScanActivity.this);

         /*   Intent intent = new Intent(QRScanActivity.this, FetchUserActivity.class);
            intent.putExtra("userid", result.getText());


            if (result.getText().equals(false)){
                viewModel.followUnfollow(result.getText(),QRScanActivity.this);
                startActivity(intent);
            }
*/

        }));
        binding.scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
        binding.imgBack.setOnClickListener(v -> {
                    onBackPressed();
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }


}