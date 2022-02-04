package com.meest.videomvvmmodule.view.notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.meest.R;
import com.meest.databinding.FragmentNotificationBinding;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.view.video.PlayerActivity;
import com.meest.videomvvmmodule.viewmodel.MainViewModel;
import com.meest.videomvvmmodule.viewmodel.NotificationViewModel;

import org.jetbrains.annotations.NotNull;

public class NotificationFragment extends Fragment {

    FragmentNotificationBinding binding;
    NotificationViewModel viewModel;
    private MainViewModel parentViewModel;
    CustomDialogBuilder customDialogBuilder;
    int count;

    public NotificationFragment(int count) {
        this.count = count;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            parentViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        }
        viewModel = new ViewModelProvider(requireActivity()).get(NotificationViewModel.class);
        initView();
        initListeners();
        initObserve();
        binding.setViewModel(viewModel);
    }

    private void initView() {
        binding.refreshlout.setEnableRefresh(false);
        viewModel.fetchNotificationData(false);
    }

    private void initListeners() {
        binding.refreshlout.setOnLoadMoreListener(refreshLayout -> viewModel.fetchNotificationData(true));
        viewModel.adapter.setOnRecyclerViewItemClick(data -> {
            customDialogBuilder = new CustomDialogBuilder(getActivity());
            customDialogBuilder.showLoadingDialog();
            viewModel.fetchPostById(data.getItemId());
        });
        binding.icBackArrow.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
    }

    private void initObserve() {
        parentViewModel.selectedPosition.observe(getViewLifecycleOwner(), position -> {
            if (position != null && position == 2) {
                viewModel.start = 0;
                viewModel.fetchNotificationData(false);
            }
        });
        viewModel.onLoadMoreComplete.observe(getViewLifecycleOwner(), onLoadMore -> binding.refreshlout.finishLoadMore());
        viewModel.video.observe(getViewLifecycleOwner(), video -> {
            if (video.getData() != null && !video.getData().isEmpty()) {
                customDialogBuilder.hideLoadingDialog();
                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                intent.putExtra("video_list", new Gson().toJson(video.getData()));
                intent.putExtra("type", 5);
                binding.getRoot().getContext().startActivity(intent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.Video_not_found), Toast.LENGTH_SHORT).show();
                if (customDialogBuilder != null) {
                    customDialogBuilder.hideLoadingDialog();
                }
            }
        });
    }
}
