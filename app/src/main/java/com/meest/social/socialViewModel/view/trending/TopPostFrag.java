package com.meest.social.socialViewModel.view.trending;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.TopPostFragModelBinding;
import com.meest.social.socialViewModel.viewModel.trendingViewModel.TopPostFragViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;


public class TopPostFrag extends Fragment {
    private static final String TAG = "TopPostFrag";
    TopPostFragModelBinding binding;
    TopPostFragViewModel viewModel;
    public Context context;
    public boolean Loading = true;
    public static boolean isLastPage = false;
    public LinearLayoutManager manager = new LinearLayoutManager(getContext());
    public int pageno = 1;
    public String data;
    boolean isSvs;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public TopPostFrag(Context context, boolean isSvs, String data) {
        this.context = context;
        this.isSvs = isSvs;
        this.data = data;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.top_post_frag_model, container, false);
        viewModel = new ViewModelProvider(getActivity(), new ViewModelFactory(new TopPostFragViewModel(context, getActivity(), binding, isSvs)).createFor()).get(TopPostFragViewModel.class);
        binding.setTopPostFragViewModel(viewModel);
        viewModel.inits();

        binding.peopleRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    pageno++;
                    viewModel.fetchData(true, pageno);
                    //scrolled to BOTTOM
                }

            }
        });
        initObserver();
        return binding.getRoot();
    }

    private void initObserver() {
        viewModel.loader.observe(getActivity(), aBoolean -> {
            if (aBoolean) {
                binding.loading.setVisibility(View.VISIBLE);
            } else {
                binding.loading.setVisibility(View.GONE);
            }
        });
    }
}