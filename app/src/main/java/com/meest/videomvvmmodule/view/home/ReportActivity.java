package com.meest.videomvvmmodule.view.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.meest.R;
import com.meest.databinding.FragmentReportSheetBinding;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.web.WebViewActivity;
import com.meest.videomvvmmodule.viewmodel.MainViewModel;
import com.meest.videomvvmmodule.viewmodel.ReportViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class ReportActivity extends AppCompatActivity {
    FragmentReportSheetBinding binding;
    ReportViewModel viewModel;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_report_sheet);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new ReportViewModel()).createFor()).get(ReportViewModel.class);
        sessionManager=new SessionManager(this);
        initView();
        initListeners();
        initObserve();
        binding.setViewmodel(viewModel);
    }

    private void initListeners() {

            String[] reasons = getResources().getStringArray(R.array.report_reasons);

            binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.report_text_color));
                    viewModel.reason = reasons[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        binding.btnReport.setOnClickListener(v -> viewModel.callApiToReport());
        binding.imgBack.setOnClickListener(v -> finish());
        if(sessionManager.getUser().getData().getUserMobileNo()!=null|| !sessionManager.getUser().getData().getUserMobileNo().equals("")) {
            binding.reportMobile.setText(sessionManager.getUser().getData().getUserMobileNo());
        }
        else {
            binding.reportMobile.setText(sessionManager.getUser().getData().getUserEmail());
        }
        binding.tvTerm.setOnClickListener(v -> startActivity(new Intent(this, WebViewActivity.class).putExtra("type", 0)));
        viewModel.contactInfo=binding.reportMobile.getText().toString();
    }

    private void initView() {
        viewModel.reportType = getIntent().getIntExtra("reporttype", 0);
        viewModel.postId = getIntent().getStringExtra("postid");
        viewModel.userId = getIntent().getStringExtra("userid");
    }

    private void initObserve() {
        viewModel.isSuccess.observe(this, isValid -> {
            if (isValid != null && isValid) {
                Toast.makeText(this, getString(R.string.Reported_Successfully), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        viewModel.isValid.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isValid) {
                Toast.makeText(ReportActivity.this, getString(R.string.Please_fill_all_the_details), Toast.LENGTH_SHORT).show();
            }
        });
    }
}