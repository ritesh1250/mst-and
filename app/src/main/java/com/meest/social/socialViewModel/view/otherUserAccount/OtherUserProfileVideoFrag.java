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
import com.meest.databinding.OtherUserProfileVideoFragModelBinding;
import com.meest.social.socialViewModel.viewModel.otherUserAccountViewModel.OtherUserProfileVideoFragViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class OtherUserProfileVideoFrag extends Fragment {
    private static final String TAG = "OtherUserProfileVideoFr";
    OtherUserProfileVideoFragViewModel viewModel;
    OtherUserProfileVideoFragModelBinding binding;
    Context context;
    String UserID;
    int pageNo = 1;

     public OtherUserProfileVideoFrag(Context context, String userId) {
         this.context = context;
         this.UserID = userId;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public OtherUserProfileVideoFrag newInstance(Context context1, String userId1) {
        this.context = context1;
        this.UserID = userId1;
        OtherUserProfileVideoFrag fragment = new OtherUserProfileVideoFrag(context1, userId1);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.other_user_profile_video_frag_model, container, false);
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(new OtherUserProfileVideoFragViewModel(context, getActivity(), UserID)).createFor()).get(OtherUserProfileVideoFragViewModel.class);
        binding.setOtherUserProfileVideoFragModel(viewModel);
        binding.loading.setVisibility(View.VISIBLE);
        initObserve();
        viewModel.fetchData(false, pageNo);
//        recyclerViewFunc();
        pagination();
        viewModel.adapterFunc();
        return binding.getRoot();
    }

    private void pagination() {
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

    private void initObserve(){
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