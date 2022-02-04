package com.meest.social.socialViewModel.view.trending;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meest.R;
import com.meest.databinding.TrendingFragModelBinding;
import com.meest.social.socialViewModel.adapter.HashtagNameAdapter;
import com.meest.social.socialViewModel.viewModel.trendingViewModel.TopPostFragViewModel;
import com.meest.social.socialViewModel.viewModel.trendingViewModel.TrendingFragViewModel;
import com.meest.social.socialViewModel.viewModel.trendingViewModel.TrendingFragmentViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.util.Objects;

import static com.meest.social.socialViewModel.viewModel.trendingViewModel.TrendingFragViewModel.datahashTagList;

public class TrendingFrag extends Fragment {
    Context context;
    boolean isSvs;
    String data;
    TrendingFragViewModel viewModel;
    TrendingFragModelBinding binding;
    public HashtagNameAdapter hashtagNameAdapter;

    public int pageno = 1;

    public TrendingFrag(Context context, boolean isSvs, String data) {
        this.context = context;
        this.isSvs = isSvs;
        this.data = data;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.trending_frag_model, container, false);
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(new TrendingFragViewModel(context, getActivity(), binding, isSvs)).createFor()).get(TrendingFragViewModel.class);
        binding.setTrendingFragViewModel(viewModel);

        initObserver();


        viewModel.fetchData(pageno,false);

        binding.hashtagRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    pageno++;
                    viewModel.fetchData( pageno,true);
                }

            }
        });
        return binding.getRoot();
    }

    private void initObserver()
    {
        viewModel.loader.observe(getActivity(), aBoolean -> {

            if (aBoolean)
            {
                binding.loading.setVisibility(View.VISIBLE);
            }
            else
            {
                binding.loading.setVisibility(View.GONE);

            }
        });
    }


}