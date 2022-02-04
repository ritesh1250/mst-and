package com.meest.videomvvmmodule.view.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.meest.R;
import com.meest.databinding.FragmentProfileVideosBinding;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.base.BaseFragment;
import com.meest.videomvvmmodule.viewmodel.ProfileVideosViewModel;
import com.meest.videomvvmmodule.viewmodel.ProfileViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.jetbrains.annotations.NotNull;


public class ProfileVideosFragment extends BaseFragment {
    FragmentProfileVideosBinding binding;
    ProfileVideosViewModel viewModel;
    ProfileViewModel parentViewModel;
    TextView tvViewCount;
    TextView tvPostCount;
//    static String userId;

    public ProfileVideosFragment(TextView tvViewCount, TextView tvPostCount) {
        this.tvViewCount = tvViewCount;
        this.tvPostCount = tvPostCount;
    }

    public static ProfileVideosFragment getNewInstance(int vidType, TextView tvViewCount, TextView tvPostCount) {
//        userId=_userId;
        ProfileVideosFragment fragment = new ProfileVideosFragment(tvViewCount, tvPostCount);
        Bundle bundle = new Bundle();
        bundle.putInt("type", vidType);
//        bundle.putString("userId",userId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_videos, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getParentFragment() != null) {
            parentViewModel = ViewModelProviders.of(getParentFragment()).get(ProfileViewModel.class);
        }
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new ProfileVideosViewModel()).createFor()).get(ProfileVideosViewModel.class);
        initView();
        initListeners();
        initObserve();
        viewModel.sessionManager = sessionManager;

        binding.setViewModel(viewModel);
    }

    private void initView() {
        if (getArguments() != null) {
            viewModel.vidType = getArguments().getInt("type");
        }
        viewModel.userId = parentViewModel.userId;
        if (viewModel.vidType == 0) {
            viewModel.userVidStart = 0;
            parentViewModel.isLikedVideos.set(true);
//            Log.e("other=======id", viewModel.userId);
            viewModel.fetchUserVideos(false);
        } else {
            viewModel.likeVidStart = 0;
            viewModel.fetchUserLikedVideos(false);
            parentViewModel.isLikedVideos.set(false);
        }
        binding.refreshlout.setEnableRefresh(false);
    }

    private void initObserve() {
        parentViewModel.selectPosition.observe(getViewLifecycleOwner(), position -> {
            if (position != null && position == 3) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fall_in);
                binding.recyclerview.startAnimation(animation);
                if (viewModel.userId == null || viewModel.userId.isEmpty()) {
                    viewModel.userId = Global.userId;
                }
                viewModel.likeVidStart = 0;
                viewModel.userVidStart = 0;
                if (viewModel.vidType == 0) {
                    viewModel.fetchUserVideos(false);
                    if (sessionManager.getUser().getData().getTotalViewCount() != null)
                        viewModel.viewCount = sessionManager.getUser().getData().getTotalViewCount();
                    if (sessionManager.getUser().getData().getTotalPost() != null)
                        viewModel.postCount = sessionManager.getUser().getData().getTotalPost();
                } else {
                    viewModel.fetchUserLikedVideos(false);
                }
                binding.setViewModel(viewModel);

            }
        });
        viewModel.onLoadMoreComplete.observe(getViewLifecycleOwner(), onLoadMore -> binding.refreshlout.finishLoadMore());
    }

    private void initListeners() {
        viewModel.adapter.setOnRecyclerViewItemClick((model, position, binding) -> new CustomDialogBuilder(getContext()).showSimpleDialog(getString(R.string.Delete), getString(R.string.Do_you_really_want_to_delete_this_post), getString(R.string.no), getString(R.string.yes), new CustomDialogBuilder.OnDismissListener() {
            @Override
            public void onPositiveDismiss() {
                viewModel.deletePost(model.getPostId(), position, tvViewCount, model.getPostViewCount(), tvPostCount);
            }

            @Override
            public void onNegativeDismiss() {

            }
        }));

        binding.refreshlout.setOnLoadMoreListener(refreshLayout -> {
            if (viewModel.vidType == 0) {
                viewModel.onUserVideoLoadMore();
            } else {
                viewModel.onUserLikedVideoLoadMore();
            }
        });
    }

}