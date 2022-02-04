package com.meest.social.socialViewModel.view.myAccountFrag;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ProfileVideoFragModelBinding;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.social.socialViewModel.viewModel.myAccountFragViewModel.ProfileVideoFragViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class ProfileVideoFrag extends Fragment {
    ProfileVideoFragModelBinding binding;
    ProfileVideoFragViewModel viewModel;
    GridLayoutManager mLayoutManager;
    Context context;
    int lastScrollPosition = 0;
    public int pageno = 1;

    public ProfileVideoFrag(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_video_frag_model, container, false);
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(new ProfileVideoFragViewModel()).createFor()).get(ProfileVideoFragViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.checkNetAndFetchData(getActivity());
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        viewModel.mLayoutManager = new GridLayoutManager(context, 3);
        binding.videoNodata.setVisibility(View.GONE);
        binding.loading.setVisibility(View.VISIBLE);
        viewModel.pageno = 1;
        initObserver();

        if (ConnectionUtils.isConnected(getActivity())) {
            if (viewModel.Loading) {
                viewModel.callApiGetVideos(false, context, viewModel.pageno);
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

        }
        pagination();

        return binding.getRoot();
    }

    private void initObserver() {
        viewModel.tvNoDataVisible.observe(requireActivity(), b -> {
            if (b)
                binding.videoNodata.setVisibility(View.VISIBLE);
            else
                binding.videoNodata.setVisibility(View.GONE);
        });

        viewModel.loaderVisible.observe(requireActivity(), b -> {
            if (b)
                binding.loading.setVisibility(View.VISIBLE);
            else
                binding.loading.setVisibility(View.GONE);
        });
    }

    private void pagination() {
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastScrollPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    pageno++;
                    viewModel.callApiGetVideos(true,getActivity(),pageno);
                    //scrolled to BOTTOM
                }


            }
        });
    }
}