package com.meest.social.socialViewModel.view.trending;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.TrendingFragmentModelBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.networkcheck.OnNoInternetRetry;
import com.meest.social.socialViewModel.viewModel.trendingViewModel.TrendingFragmentViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;



public class TrendingFragment extends Fragment implements OnNoInternetRetry {
    TrendingFragmentModelBinding binding;
    TrendingFragmentViewModel viewModel;
    Context context;
    FragmentManager fragmentManager;
    int lastScrollPosition = 0;


    public int pageno = 1;
    public TrendingFragment() {
        this.context = getActivity();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.fragmentManager = getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.trending_fragment_model, container, false);
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(new TrendingFragmentViewModel(context, getActivity(), binding, fragmentManager)).createFor()).get(TrendingFragmentViewModel.class);
        binding.setTrendingFragmentModel(viewModel);
        SharedPrefreances.setSharedPreferenceString(context, "back", "2");
        viewModel.inits();

        viewModel.fetchHashTag(false, pageno);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL,false);
        binding.hashtagRecycleViewHorizental.setLayoutManager(linearLayoutManager);
        binding.hashtagRecycleViewHorizental.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastScrollPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (viewModel.hashTagResponseSize-1 == lastScrollPosition)
                {
                    pageno++;
                    viewModel.fetchHashTag(true,pageno);
                }

            }
        });

            viewModel.changeFragment();



        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRetry() {
        super.onResume();
    }
}