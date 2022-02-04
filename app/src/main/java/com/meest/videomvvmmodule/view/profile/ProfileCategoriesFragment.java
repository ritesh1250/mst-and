package com.meest.videomvvmmodule.view.profile;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.meest.R;
import com.meest.databinding.FragmentProfileCategoriesBinding;
import com.meest.videomvvmmodule.viewmodel.EditProfileViewModel;

public class ProfileCategoriesFragment extends BottomSheetDialogFragment {

    EditProfileViewModel viewModel;
    FragmentProfileCategoriesBinding binding;

    public ProfileCategoriesFragment() {

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog dialog = (BottomSheetDialog) dialog1;
            dialog.setCanceledOnTouchOutside(false);

        });
        return bottomSheetDialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_categories, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            viewModel = ViewModelProviders.of(getActivity()).get(EditProfileViewModel.class);
        }
        initView();
        initListeners();
        binding.setViewmodel(viewModel);

    }

    private void initListeners() {
        binding.imgClose.setOnClickListener(view -> dismiss());
    }

    private void initView() {
        //viewModel.fetchProfileCategories();
    }
}