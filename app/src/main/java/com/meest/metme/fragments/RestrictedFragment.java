package com.meest.metme.fragments;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.meest.R;

import com.meest.databinding.RestrictLayoutBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;

import com.meest.metme.viewmodels.ChatRestrictedViewModel;

public class RestrictedFragment extends Fragment {

    String fullname;
    TextView user_full_name;
    RestrictLayoutBinding binding;
    String chatHeadID, toUserId;
    ChatRestrictedViewModel viewModel;
    String secondColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.restrict_layout, container, false);
        viewModel = new ChatRestrictedViewModel();
        binding.setViewModel(viewModel);
        if (getArguments().getString("fullName") != null) {
            fullname = getArguments().getString("fullName");
            chatHeadID = getArguments().getString("chatHeadID");
            toUserId = getArguments().getString("toUserId");
            secondColor = getArguments().getString("secondColor");
        }
        binding.userFullName.setText(fullname + " " + getResources().getString(R.string.restricted));
        initviews();
        changeButtonColor(secondColor);
        viewModel.fontName= SharedPrefreances.getSharedPreferenceString(getContext(),chatHeadID);
        return binding.getRoot();
    }

    private void initviews() {
        viewModel.x_token = SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN);
        viewModel.GetChatSetting(toUserId, chatHeadID, SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN), getContext(), binding);
        binding.llRestrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.isRestricted) {
                    viewModel.userRestricted(toUserId, chatHeadID, SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN), getContext(), binding, !viewModel.isRestricted);
                } else {
                    viewModel.userRestricted(toUserId, chatHeadID, SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN), getContext(), binding, !viewModel.isRestricted);
                }

            }
        });
    }
    private void changeButtonColor(String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(11f);
            binding.llRestrict.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getResources().getColor(R.color.first_color), getResources().getColor(R.color.first_color)});
            gd.setCornerRadius(0f);
            binding.llRestrict.setBackground(gd);
        }
    }

}
