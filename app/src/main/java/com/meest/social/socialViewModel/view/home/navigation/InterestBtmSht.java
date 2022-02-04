package com.meest.social.socialViewModel.view.home.navigation;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meest.Fragments.BottomSheetCommentFragment;
import com.meest.R;

public class InterestBtmSht extends BottomSheetCommentFragment {
    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.interest_btm_sht_model, container, false);
    }
}