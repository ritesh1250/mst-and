package com.meest.videomvvmmodule.view.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.meest.R;
import com.meest.databinding.FragmentFollowersBinding;
import com.meest.videomvvmmodule.adapter.FollowersAdapter;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.base.BaseFragment;
import com.meest.videomvvmmodule.view.search.FetchUserActivity;
import com.meest.videomvvmmodule.viewmodel.FollowerFollowingViewModel;
import com.meest.videomvvmmodule.viewmodel.FragmentFollowersViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.jetbrains.annotations.NotNull;


public class FollowersFragment extends BaseFragment {


    FragmentFollowersBinding binding;
    FragmentFollowersViewModel viewModel;
    FollowerFollowingViewModel parentViewModel;
    static String user_id;
    CustomDialogBuilder customDialogBuilder;

//    ActivityResultLauncher<Intent> someActivityResultLauncher;

    public static FollowersFragment getNewInstance(String type, String _userId) {
        user_id = _userId;
        FollowersFragment fragment = new FollowersFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("user_id", user_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_followers, container, false);
        customDialogBuilder = new CustomDialogBuilder(getContext());
        return binding.getRoot();

//        someActivityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        // There are no request codes
//                        Intent data = result.getData();
//                        doSomeOperations();
//                    }
//                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            parentViewModel = new ViewModelProvider(getActivity()).get(FollowerFollowingViewModel.class);
        }
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new FragmentFollowersViewModel()).createFor()).get(FragmentFollowersViewModel.class);

        initView();
        initObserver();
        initListener();
        binding.setViewmodel(viewModel);
        viewModel.sessionManager = sessionManager;
    }

    private void initView() {
        // viewModel.userId = parentViewModel.user.getData().getUserId();
        binding.refreshlout.setEnableRefresh(false);
        viewModel.userId = user_id;
        if (getArguments() != null) {
            viewModel.itemType = getArguments().getString("type");
        }
        viewModel.adapter = new FollowersAdapter(viewModel.itemType);
        if (viewModel.itemType != null && viewModel.itemType.equals("0")) {
            viewModel.fetchFollowers(false);
        } else {
            viewModel.fetchFollowing(false);
        }

    }

    private void initListener() {
        viewModel.adapter.setOnRecyclerViewItemClick((data, i, position) -> {
            Intent intent = new Intent(getActivity(), FetchUserActivity.class);

//            Log.e("TAG", "itemType: "+viewModel.itemType);
            if (viewModel.itemType.equals("0")) {
                intent.putExtra("userid", data.getFromUserId());
            } else {
                intent.putExtra("userid", data.getToUserId());
            }
//            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
//            someActivityResultLauncher.launch(intent);
        });

        binding.refreshlout.setOnLoadMoreListener(refreshLayout -> {
            if (viewModel.itemType.equals("0")) {
                viewModel.onFollowersLoadMore();
            } else {
                viewModel.onFollowingLoadMore();
            }
        });
    }

    private void initObserver() {
        viewModel.onLoadMoreComplete.observe(getViewLifecycleOwner(), onLoadMore -> binding.refreshlout.finishLoadMore());
        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                customDialogBuilder.showLoadingDialog();
            } else {
                customDialogBuilder.hideLoadingDialog();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        for (String key : Global.followUnfollow.keySet()) {
            if (!Global.followUnfollow.get(key)) {
                viewModel.adapter.getmList().removeIf(data -> data.getToUserId().equals(key));
            }
        }
        viewModel.adapter.notifyDataSetChanged();
    }
}