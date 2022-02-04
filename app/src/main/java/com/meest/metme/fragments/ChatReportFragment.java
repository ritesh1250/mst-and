package com.meest.metme.fragments;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.meest.R;
import com.meest.databinding.ChatReportLayoutBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.viewmodels.ChatReportViewModel;

public class ChatReportFragment extends Fragment {

    ChatReportLayoutBinding reportLayoutBinding;
    String chatHeadID;
    ChatReportViewModel viewModel;
    String secondColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.report_layout, container, false);
        if (getArguments() != null) {
            chatHeadID = getArguments().getString("chatHeadID");
            secondColor = getArguments().getString("secondColor");
        }
        reportLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.chat_report_layout, container, false);
        viewModel = new ChatReportViewModel();
        reportLayoutBinding.setChatReportmodelview(viewModel);
        reportLayoutBinding.setLifecycleOwner(this);
        viewModel.ctx = getContext();
        viewModel.chatHeadID = chatHeadID;
        initListener();
        changeButtonColor(secondColor);
        viewModel.fontName= SharedPrefreances.getSharedPreferenceString(getContext(),chatHeadID);
        return reportLayoutBinding.getRoot();
    }

    private void initListener() {
        reportLayoutBinding.tvSpam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportLayoutBinding.tvSpam.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                reportLayoutBinding.tvInappropriate.setTextColor(ContextCompat.getColor(getContext(), R.color.greytext));
                viewModel.reportspam();
            }
        });
        reportLayoutBinding.tvInappropriate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportLayoutBinding.tvSpam.setTextColor(ContextCompat.getColor(getContext(), R.color.greytext));
                reportLayoutBinding.tvInappropriate.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                viewModel.reportinappropiate();
            }
        });
    }

    private void changeButtonColor(String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(11f);
            reportLayoutBinding.llReport.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getResources().getColor(R.color.first_color), getResources().getColor(R.color.first_color)});
            gd.setCornerRadius(11f);
            reportLayoutBinding.llReport.setBackground(gd);
        }
    }
}