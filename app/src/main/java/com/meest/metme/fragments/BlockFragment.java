package com.meest.metme.fragments;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.meest.R;
import com.meest.databinding.BlockLayoutBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.viewmodels.ChatBlockViewModel;


public class BlockFragment extends Fragment {

    BlockLayoutBinding blockLayoutBinding;
    String chatHeadID, toUserId, fullname;
    ChatBlockViewModel viewModel;
    String secondColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //    View view = inflater.inflate(R.layout.block_layout, container, false);
        blockLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.block_layout, container, false);
        viewModel = new ChatBlockViewModel();
        blockLayoutBinding.setViewModel(viewModel);
        chatHeadID = getArguments().getString("chatHeadID");
        toUserId = getArguments().getString("toUserId");

        if (getArguments().getString("fullName") != null) {
            fullname = getArguments().getString("fullName");
            chatHeadID = getArguments().getString("chatHeadID");
            toUserId = getArguments().getString("toUserId");
            secondColor = getArguments().getString("secondColor");
        }
        blockLayoutBinding.tvBlockUsername.setText(getResources().getString(R.string.your_meest_account_won_t_receive_cross_app_messages_or_call_from_meest_account_vinamra_aeran) + " " + fullname );
        initviews();
        changeButtonColor(secondColor);
        viewModel.fontName= SharedPrefreances.getSharedPreferenceString(getContext(),chatHeadID);
        return blockLayoutBinding.getRoot();
    }

    private void initviews() {
        viewModel.x_token = SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN);
        viewModel.GetChatSetting(toUserId, chatHeadID, SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN), getContext(), blockLayoutBinding);
        blockLayoutBinding.llBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.chatSettingResponse.getData().getIsBlocked()) {
                    viewModel.UserUnblockDialog(toUserId, chatHeadID, SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN), getContext(), blockLayoutBinding);
                } else {
                    viewModel.ChatUserBlock(toUserId, chatHeadID, SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN), getContext(), blockLayoutBinding);
                }

            }
        });
//        blockLayoutBinding.llCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
//                blockLayoutBinding.llCancel.setVisibility(View.GONE);
//            }
//        });

    }
    private void changeButtonColor(String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(11f);
            blockLayoutBinding.llBlock.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getResources().getColor(R.color.first_color), getResources().getColor(R.color.first_color)});
            gd.setCornerRadius(0f);
            blockLayoutBinding.llBlock.setBackground(gd);
        }
    }
}