package com.meest.social.socialViewModel.view.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.meest.R;
import com.meest.databinding.FragmentHashtagBinding;

import com.meest.networkcheck.ConnectionUtils;
import com.meest.responses.HashtagSearchResponse;
import com.meest.social.socialViewModel.adapter.search.HashtagDataAdapter;
import com.meest.social.socialViewModel.adapter.search.HashtagNameAdapter;
import com.meest.social.socialViewModel.viewModel.searchViewModel.VideoHashTagFragViewModel;

import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.util.ArrayList;

import java.util.List;

public class VideoHashtagFrag extends Fragment {

    public static boolean isLastPage = false;
    int pagePerRecord = 10;
    int pageno = 1;
    StaggeredGridLayoutManager layoutManager;
    LinearLayoutManager manager;
    FragmentHashtagBinding binding;
    VideoHashTagFragViewModel viewModel;
    private Context context;
    private boolean isSvs;
    private TextView not_found;
    private String data;
    private double lastScrollPosition;

    public VideoHashtagFrag(Context context, boolean isSvs, String data) {
        this.isSvs = isSvs;
        this.context = context;
        this.data = data;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hashtag, viewGroup, false);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new VideoHashTagFragViewModel()).createFor()).get(VideoHashTagFragViewModel.class);
        binding.setViewModel(viewModel);

        initObserver();

        binding.loading.setAnimation("loading.json");
        binding.loading.playAnimation();
        binding.loading.loop(true);
        viewModel.data = data;


        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        binding.hashtagRecycleView.setLayoutManager(layoutManager);
        binding.hashtagRecycleView.setHasFixedSize(true);
        viewModel.hashtagAdapter = new HashtagDataAdapter(context, viewModel.datahashTagListimage);
        binding.hashtagRecycleView.setAdapter(viewModel.hashtagAdapter);
        manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.hashtagRecycleViewHorizental.setLayoutManager(manager);
        binding.hashtagRecycleViewHorizental.setHasFixedSize(true);
        viewModel.hashtagNameAdapter = new HashtagNameAdapter(context, viewModel.datahashTagList);
        binding.hashtagRecycleViewHorizental.setAdapter(viewModel.hashtagNameAdapter);

        binding.hashtagRecycleViewHorizental.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastScrollPosition = manager.findLastCompletelyVisibleItemPosition();

                if (viewModel.datahashTagList.size() - 1 == lastScrollPosition) {
                    if (viewModel.Loading) {
                        viewModel.Loading = false;
                        pageno++;
                        viewModel.fetchData(pageno, context, true);
                    }
                }


            }
        });

        binding.hashtagRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    if (viewModel.Loading) {
                        viewModel.Loading = false;
                        pageno++;
                        viewModel.fetchData(pageno, context, true);
                    }
                }

            }
        });

        if (ConnectionUtils.isConnected(context)) {
            viewModel.fetchData(pageno, context, false);

        } else {
            Toast.makeText(context, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        return binding.getRoot();
    }

    public void updateSearch(String search) {
        viewModel.data = search;
        this.data = search;
        pageno = 1;
        viewModel.fetchData(pageno, context, false);
    }

    public void initObserver() {

        viewModel.loader.observe(requireActivity(), aBoolean -> {
            if (aBoolean)
                binding.loading.setVisibility(View.VISIBLE);
            else
                binding.loading.setVisibility(View.GONE);
        });

        viewModel.showRecyclerView.observe(requireActivity(), aBoolean -> {
            if (aBoolean) {
                binding.hashtagRecycleView.setVisibility(View.VISIBLE);
                binding.hashtagRecycleViewHorizental.setVisibility(View.VISIBLE);
            } else {
                binding.hashtagRecycleView.setVisibility(View.GONE);
                binding.hashtagRecycleViewHorizental.setVisibility(View.GONE);
            }

        });

        viewModel.dataNotFound.observe(requireActivity(), aBoolean -> {
            if (aBoolean)
                binding.notFound.setVisibility(View.VISIBLE);
            else
                binding.notFound.setVisibility(View.GONE);
        });

    }
}