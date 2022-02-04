package com.meest.videomvvmmodule.view.music;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.meest.R;
import com.meest.databinding.FragmentSearchMusicBinding;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.viewmodel.MusicMainViewModel;

import org.jetbrains.annotations.NotNull;


public class SearchMusicFragment extends Fragment {

    public MusicMainViewModel viewModel;
    private FragmentSearchMusicBinding binding;
    public CustomDialogBuilder customDialogBuilder;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_music, container, false);
        if (getArguments() != null) {
            binding.categoryName.setText(getArguments().getString("categoryName"));
        }

        customDialogBuilder = new CustomDialogBuilder(getContext());
        return binding.getRoot();
    }

    private void initObserve() {
        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                customDialogBuilder.showLoadingDialog();
            } else {
                customDialogBuilder.hideLoadingDialog();
            }
        });
    }

    @Override

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getParentFragment() != null) {
            viewModel = ViewModelProviders.of(getParentFragment()).get(MusicMainViewModel.class);
        }
        initObserve();
        binding.setViewModel(viewModel);
    }

}