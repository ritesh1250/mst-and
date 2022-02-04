package com.meest.social.socialViewModel.view.otherUserAccount;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.OtherFriendPhotoGridFragModelBinding;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.social.socialViewModel.viewModel.otherUserAccountViewModel.OtherFriendPhotoGridFragViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class OtherFriendPhotoGridFrag extends Fragment {
    private static final String TAG = "OtherFriendPhotoGridFra";
//    Context context;
    String userId;
    OtherFriendPhotoGridFragModelBinding binding;
    OtherFriendPhotoGridFragViewModel viewModel;
    public int pageNo = 1;
    int lastScrollPosition = 0;

    public OtherFriendPhotoGridFrag(Context context, String userId) {
//        this.context = context;
        this.userId = userId;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
//        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.other_friend_photo_grid_frag_model, container, false);
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(new OtherFriendPhotoGridFragViewModel(getActivity(), getActivity(), userId)).createFor()).get(OtherFriendPhotoGridFragViewModel.class);
        binding.setOtherFriendPhotoGridFragModel(viewModel);
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        initObserver();

        if (ConnectionUtils.isConnected(requireActivity()))
            viewModel.homeFragmentFeedListingService(false, pageNo);
        else
            Toast.makeText(requireActivity(), requireActivity().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

        pagination();
//        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (!recyclerView.canScrollVertically(1) && dy > 0) {
//                    pageNo++;
//                    viewModel.homeFragmentFeedListingService(true, String.valueOf(pageNo));
//                    //scrolled to BOTTOM
//                } else if (!recyclerView.canScrollVertically(-1) && dy < 0) {
//                    //scrolled to TOP
//                }
//            }
//        });

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
                    viewModel.homeFragmentFeedListingService(true, pageNo);
                    //scrolled to BOTTOM
                    Log.e(TAG, "onScrolled: " + "SCROLLED TO BOTTOM" );
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
            if(b)
                binding.tvNoData.setVisibility(View.VISIBLE);
            else
                binding.tvNoData.setVisibility(View.GONE);
        });
    }

}