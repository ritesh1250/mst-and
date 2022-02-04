package com.meest.social.socialViewModel.view.otherUserAccount;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.OtherUserCameraFragModelBinding;
import com.meest.social.socialViewModel.viewModel.otherUserAccountViewModel.OtherUserCameraFragViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

public class OtherUserCameraFrag extends Fragment {

    private static final String TAG = "OtherUserCameraFrag";
    Context context;
    OtherUserCameraFragModelBinding binding;
    OtherUserCameraFragViewModel viewModel;
    String userId;
    int pageNo = 1;

    public OtherUserCameraFrag(Context context, String userID) {
        this.context = context;
        this.userId = userID;
    }

    public OtherUserCameraFrag newInstance(Context context1, String userId1) {
        this.context = context1;
        this.userId = userId1;
        OtherUserCameraFrag fragment = new OtherUserCameraFrag(context1, userId1);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.other_user_camera_frag_model, container, false);
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(new OtherUserCameraFragViewModel(context, getActivity(), userId)).createFor()).get(OtherUserCameraFragViewModel.class);
        binding.setOtherUserCameraFragModel(viewModel);
        initObserver();
        binding.tvNoData.setVisibility(View.GONE);
        viewModel.fetchData(false, pageNo);
//        recyclerViewFunc();
        pagination();
        viewModel.adapterFunc();
        return binding.getRoot();
    }

    private void pagination() {


//        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastScrollPosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
//                if (!recyclerView.canScrollVertically(1) && dy > 0) {
//                    pageNo++;
//                    viewModel.homeFragmentFeedListingService(true,pageNo);
//                    //scrolled to BOTTOM
//                }
//
//
//            }
//        });

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    pageNo++;
                    viewModel.fetchData(true, pageNo);
                    //scrolled to BOTTOM
                }
            }
        });
    }

    private void initObserver() {
        viewModel.loaderVisibility.observe(requireActivity(), b -> {
            if (b)
                binding.loading.setVisibility(View.VISIBLE);
            else
                binding.loading.setVisibility(View.GONE);
        });
        viewModel.tvNoDataVis.observe(requireActivity(), b -> {
            if (b)
                binding.tvNoData.setVisibility(View.VISIBLE);
            else
                binding.tvNoData.setVisibility(View.GONE);
        });
        viewModel.recyV.observe(requireActivity(), b -> {
            if (b)
                binding.recyclerView.setVisibility(View.VISIBLE);
            else
                binding.recyclerView.setVisibility(View.GONE);
        });
    }


    private void recyclerViewFunc(){
//        binding.recyclerView.setAdapter(viewModel.adapter);
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
//        binding.recyclerView.setHasFixedSize(false);
//        binding.recyclerView.setLayoutManager(mLayoutManager);

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    pageNo++;
                    viewModel.fetchData(true, pageNo);
                } else if (!recyclerView.canScrollVertically(-1) && dy < 0) {
                    //scrolled to TOP
                }
            }
        });
    }
}