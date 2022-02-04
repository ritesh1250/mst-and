package com.meest.social.socialViewModel.view.myAccountFrag;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ProfileTagFragModelBinding;
import com.meest.social.socialViewModel.viewModel.myAccountFragViewModel.ProfileTagFragViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;


public class ProfileTagFrag extends Fragment {
    Context context;
    FragmentActivity activity;
    ProfileTagFragModelBinding binding;
    ProfileTagFragViewModel viewModel;
    GridLayoutManager mLayoutManager;
    public int pageno = 1;
    int lastScrollPosition = 0;
    public ProfileTagFrag(Context context) {
        this.context = context;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_tag_frag_model, container, false);
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(new ProfileTagFragViewModel()).createFor()).get(ProfileTagFragViewModel.class);
        binding.setViewModel(viewModel);
        initObserver();
        mLayoutManager = new GridLayoutManager(getContext(), 3);
        binding.tvNoData.setVisibility(View.GONE);
        viewModel.checkNetAndFetchData(getActivity());
        paginationPicture();
        return binding.getRoot();
    }

    private void paginationPicture() {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
            binding.recyclerView.setLayoutManager(mLayoutManager);
            binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    lastScrollPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                    if (!recyclerView.canScrollVertically(1) && dy > 0) {
                        pageno++;
                        viewModel.homeFragmentFeedListingService(true,getActivity(),pageno);
                        //scrolled to BOTTOM
                    }


                }
            });

        }

    private void initObserver() {
        viewModel.loaderVisible.observe(requireActivity(), b -> {
            if (b)
                binding.loading.setVisibility(View.VISIBLE);
            else
                binding.loading.setVisibility(View.GONE);
        });

        viewModel.tvNoDataVisiblity.observe(requireActivity(), b -> {
            if (b)
                binding.tvNoData.setVisibility(View.VISIBLE);
            else
                binding.tvNoData.setVisibility(View.GONE);
        });


    }

}