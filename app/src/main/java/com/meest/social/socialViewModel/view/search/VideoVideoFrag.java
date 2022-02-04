package com.meest.social.socialViewModel.view.search;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.CollectionPointAdapter;
import com.meest.R;
import com.meest.databinding.FragmentvideoshBinding;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.responses.VideoSearchResponse;
import com.meest.social.socialViewModel.adapter.search.HashtagVideoAdapter;
import com.meest.social.socialViewModel.viewModel.searchViewModel.VideoPeopleFragViewModel;
import com.meest.social.socialViewModel.viewModel.searchViewModel.VideoVideoFragViewModel;
import com.meest.svs.adapters.HashtagVideoMeeshoAdapter;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VideoVideoFrag extends Fragment {

    private Context context;


    StaggeredGridLayoutManager layoutManager;

    int pageno = 1;

    public static boolean isLastPage = false;

    VideoVideoFragViewModel viewModel;
    FragmentvideoshBinding binding;

    boolean tempIsSvs;
    String tempData, tempMediaType;

    public VideoVideoFrag(Context context, boolean isSvs, String data, String mediaType) {
        this.tempIsSvs = isSvs;
        this.context = context;
        this.tempData = data;
        this.tempMediaType = mediaType;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragmentvideosh, viewGroup, false);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new VideoVideoFragViewModel()).createFor()).get(VideoVideoFragViewModel.class);
        binding.setViewModel(viewModel);

        initObserver();

        viewModel.isSvs = tempIsSvs;
        viewModel.data = tempData;
        viewModel.mediaType = tempMediaType;

        binding.loading.setAnimation("loading.json");
        binding.loading.playAnimation();
        binding.loading.loop(true);


        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        binding.hashtagRecycleView.setLayoutManager(layoutManager);
        binding.hashtagRecycleView.setHasFixedSize(true);
        viewModel.hashtagVideosAdapter = new HashtagVideoAdapter(context, viewModel.list, true, "");
        binding.hashtagRecycleView.setAdapter(viewModel.hashtagVideosAdapter);

        if (ConnectionUtils.isConnected(context)) {
            if (viewModel.Loading) {
                viewModel.fetchData(pageno,context);
            }
        } else {
            Toast.makeText(context, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


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
                        viewModel.fetchData(pageno,context);
                    }
                }

            }
        });


        return binding.getRoot();
    }

    public void updateSearch(String search) {
        viewModel.data = search;
        pageno=1;
        viewModel.fetchData(pageno,context);
    }

    public void initObserver() {

        viewModel.loader.observe(requireActivity(), aBoolean -> {
            if (aBoolean)
                binding.loading.setVisibility(View.VISIBLE);
            else
                binding.loading.setVisibility(View.GONE);
        });

        viewModel.showRecyclerView.observe(requireActivity(), aBoolean -> {
            if (aBoolean)
                binding.hashtagRecycleView.setVisibility(View.VISIBLE);
            else
                binding.hashtagRecycleView.setVisibility(View.GONE);
        });

        viewModel.dataNotFound.observe(requireActivity(), aBoolean -> {
            if (aBoolean)
            {
                if (viewModel.mediaType.equalsIgnoreCase("image")) {
                    binding.trendNotFound.setText(getString(R.string.No_image_found));
                } else  {
                    binding.trendNotFound.setText(getString(R.string.No_videos_found));
                }
            }
            else
                binding.trendNotFound.setVisibility(View.GONE);
        });

    }

}
