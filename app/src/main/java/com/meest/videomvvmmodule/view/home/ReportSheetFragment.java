package com.meest.videomvvmmodule.view.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.meest.R;
import com.meest.databinding.FragmentReportSheetBinding;
import com.meest.videomvvmmodule.view.web.WebViewActivity;
import com.meest.videomvvmmodule.viewmodel.ReportViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.jetbrains.annotations.NotNull;


public class ReportSheetFragment extends BottomSheetDialogFragment {

    FragmentReportSheetBinding binding;
    ReportViewModel viewModel;
    SessionManager sessionManager;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog dialog = (BottomSheetDialog) dialog1;
            dialog.setCanceledOnTouchOutside(false);
            FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
                BottomSheetBehavior.from(bottomSheet).setHideable(true);
            }
        });
        return bottomSheetDialog;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_report_sheet, container, false);
        sessionManager=new SessionManager(getContext());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new ReportViewModel()).createFor()).get(ReportViewModel.class);
        initView();
        initListeners();
        initObserve();
        binding.setViewmodel(viewModel);
    }

    private void initListeners() {
        if (getActivity() != null) {
            String[] reasons = getActivity().getResources().getStringArray(R.array.report_reasons);

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
        }

        binding.btnReport.setOnClickListener(v -> viewModel.callApiToReport());
        binding.imgBack.setOnClickListener(v -> dismiss());
        if(sessionManager.getUser().getData().getUserMobileNo()!=null|| !sessionManager.getUser().getData().getUserMobileNo().equals("")) {
            binding.reportMobile.setText(sessionManager.getUser().getData().getUserMobileNo());
        }
        else {
            binding.reportMobile.setText(sessionManager.getUser().getData().getUserEmail());
        }
        binding.tvTerm.setOnClickListener(v -> startActivity(new Intent(getActivity(), WebViewActivity.class).putExtra("type", 0)));
        viewModel.contactInfo=binding.reportMobile.getText().toString();
    }

    private void initView() {
        if (getArguments() != null) {
            viewModel.reportType = getArguments().getInt("reporttype", 0);
            viewModel.postId = getArguments().getString("postid");
            viewModel.userId = getArguments().getString("userid");

        }
    }

    private void initObserve() {
        viewModel.isSuccess.observe(getViewLifecycleOwner(), isValid -> {
            if (isValid != null && isValid) {
                Toast.makeText(getActivity(), getString(R.string.Reported_Successfully), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        viewModel.isValid.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isValid) {
                Toast.makeText(ReportSheetFragment.this.getContext(), getString(R.string.Please_fill_all_the_details), Toast.LENGTH_SHORT).show();
            }
        });
    }

}