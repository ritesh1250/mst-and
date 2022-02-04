package com.meest.videomvvmmodule.view.search;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.meest.R;
import com.meest.databinding.FragmentSearchBinding;
import com.meest.videomvvmmodule.viewmodel.MainViewModel;
import com.meest.videomvvmmodule.viewmodel.SearchFragmentViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

public class SearchFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST = 101;
    SearchFragmentViewModel viewModel;
    FragmentSearchBinding binding;
    private MainViewModel parentViewModel;

    public SearchFragment() {

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getContext() fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            parentViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        }
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new SearchFragmentViewModel()).createFor()).get(SearchFragmentViewModel.class);
        initView();
        initObserve();
        initListeners();
        binding.setViewmodel(viewModel);
    }

    private void initView() {
        binding.refreshlout.setEnableRefresh(false);
    }


    private void initObserve() {
        parentViewModel.selectedPosition.observe(getViewLifecycleOwner(), position -> {
            if (position != null && position == 1) {
                viewModel.exploreStart = 0;
                viewModel.fetchExploreItems(false);
            }
        });
        viewModel.onLoadMoreComplete.observe(getViewLifecycleOwner(), onLoadMore -> binding.refreshlout.finishLoadMore());
    }

    private void initListeners() {

        binding.imgSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra("search", binding.etSearch.getText().toString());
            startActivity(intent);
        });

        binding.imgQr.setOnClickListener(v -> initPermission());

        binding.refreshlout.setOnLoadMoreListener(refreshLayout -> viewModel.onExploreLoadMore());

    }

    private void initPermission() {
        if (getActivity() != null && getActivity().getPackageManager() != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST);
            } else {
                startActivity(new Intent(getContext(), QRScanActivity.class));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && getActivity() != null && getActivity().getPackageManager() != null) {
                startActivity(new Intent(getContext(), QRScanActivity.class));
            }
        }
    }

}
