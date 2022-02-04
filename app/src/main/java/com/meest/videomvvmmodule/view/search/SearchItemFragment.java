package com.meest.videomvvmmodule.view.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.meest.R;
import com.meest.databinding.FragmentSearchItemBinding;
import com.meest.videomvvmmodule.adapter.VideoListAdapter;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.view.base.BaseFragment;
import com.meest.videomvvmmodule.view.video.PlayerActivity;
import com.meest.videomvvmmodule.viewmodel.SearchActivityViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class SearchItemFragment extends BaseFragment {

    public int searchType;
    FragmentSearchItemBinding binding;
    SearchActivityViewModel parentViewModel;
    CustomDialogBuilder customDialogBuilder;

    public static SearchItemFragment getNewInstance(int searchType) {
        SearchItemFragment fragment = new SearchItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", searchType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_item, container, false);

        customDialogBuilder = new CustomDialogBuilder(getContext());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            parentViewModel = new ViewModelProvider(getActivity()).get(SearchActivityViewModel.class);
        }
        if (getArguments() != null) {
            searchType = getArguments().getInt("type");
        }
        initView();
        initListeners();
        initObserve();
        binding.setFragment(this);
        binding.setViewmodel(parentViewModel);
        parentViewModel.sessionManager = sessionManager;
    }

    private void initListeners() {
        binding.refreshlout.setOnLoadMoreListener(refreshLayout -> {
            switch (searchType) {
                case 0:
                    parentViewModel.onUserLoadMore();
                    break;
                case 1:
//                    SearchNewFragment.count_view = 5;
                    parentViewModel.onHashtagLoadMore();
                    break;
                case 2:
//                    SearchNewFragment.count_view = 6;
                    parentViewModel.onVideoLoadMore();
                    break;
            }
//            if (searchType == 0) {
//
//            } else {
//                parentViewModel.onVideoLoadMore();
//            }
        });
    }

    private void initView() {

        parentViewModel.videoListAdapter = new VideoListAdapter(new VideoListAdapter.OnRecyclerViewItemClick() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(SearchItemFragment.this.requireContext(), PlayerActivity.class);
//                intent.putExtra("video_list", new Gson().toJson(parentViewModel.videoListAdapter.getmList()));
                List<Video.Data> singleList = new ArrayList<>();
                singleList.add(parentViewModel.videoListAdapter.getmList().get(position));
                intent.putExtra("video_list", new Gson().toJson(singleList));
                intent.putExtra("position", position);

                intent.putExtra("type", 3);
//                intent.putExtra("keyword", word);

                SearchItemFragment.this.requireActivity().startActivity(intent);
            }
        });
        binding.refreshlout.setEnableRefresh(false);
        switch (searchType) {
            case 0:
                binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                parentViewModel.userStart = 0;
                parentViewModel.searchForUser(false);
//            if (parentViewModel.videoListAdapter.mList.isEmpty()) {
//                parentViewModel.searchForVideos(false);
//            }
                break;
            case 1:
                binding.rvHashTag.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                binding.recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                parentViewModel.hashtagStart=0;
                parentViewModel.fetchExploreItems(false);
                parentViewModel.fetchHashTagName(false);
                break;
            case 2:
                binding.rvHashTag.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                binding.recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                parentViewModel.videoStart=0;
                parentViewModel.searchForVideos(false);
                break;
        }

    }

    private void initObserve() {
        parentViewModel.onLoadMoreComplete.observe(getViewLifecycleOwner(),
                onLoadMore -> binding.refreshlout.finishLoadMore());

//        parentViewModel.isloading.observe(getViewLifecycleOwner(), isLoading -> {
//                    if (isLoading) {
//                        customDialogBuilder.showLoadingDialog();
//                    } else {
//                        customDialogBuilder.hideLoadingDialog();
//                    }
//                }
//        );
    }

    @Override
    public void onResume() {
        super.onResume();
//        parentViewModel.userStart = 0;
//        parentViewModel.searchForUser(false);
//        parentViewModel.onLoadMoreComplete.setValue(false);
//        parentViewModel.isloading.setValue(false);
        Log.e("killer", "onResume: back");
//        binding.recyclerview.scroll
    }

}