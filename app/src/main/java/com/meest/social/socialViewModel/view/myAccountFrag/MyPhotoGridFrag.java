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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.MyPhotoGridFragModelBinding;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.social.socialViewModel.viewModel.myAccountFragViewModel.MyPhotoGridFragViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class MyPhotoGridFrag extends Fragment {
    Context context;
    String userId;
    MyPhotoGridFragModelBinding binding;
    MyPhotoGridFragViewModel viewModel;
    public int pageno = 1;
    GridLayoutManager mLayoutManager;
    int lastScrollPosition = 0;
    public MyPhotoGridFrag(Context context) {
        this.context = context;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public MyPhotoGridFrag newInstance(Context context1) {
        this.context = context1;
        return new MyPhotoGridFrag(context1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater, R.layout.my_photo_grid_frag_model, container, false);
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(new MyPhotoGridFragViewModel(getActivity())).createFor()).get(MyPhotoGridFragViewModel.class);
        binding.setViewModel(viewModel);
        binding.tvNoData.setVisibility(View.GONE);
        binding.recyclerView.setHasFixedSize(false);

        if (ConnectionUtils.isConnected(context)) {
            viewModel.homeFragmentFeedListingService(false, context, viewModel.pageno);
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
        paginationPicture();
        initObserver();
        return binding.getRoot();
    }

    void paginationPicture() {
        mLayoutManager = new GridLayoutManager(context, 3);
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

        viewModel.tvNoDataVisible.observe(requireActivity(), b -> {
            if (b)
                binding.tvNoData.setVisibility(View.VISIBLE);
            else
                binding.tvNoData.setVisibility(View.GONE);
        });


    }


}