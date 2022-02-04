package com.meest.social.socialViewModel.view.search;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Adapters.CollectionPointAdapter;
import com.meest.R;
import com.meest.databinding.FragmentPeopleBinding;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.social.socialViewModel.adapter.search.PeopleSearchAdapter;
import com.meest.social.socialViewModel.adapter.search.VideoPeopleAdapter;
import com.meest.social.socialViewModel.viewModel.searchViewModel.VideoPeopleFragViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;


public class VideoPeopleFrag extends Fragment {

    private static final String TAG = "VideoPeopleFragment";

    private Context context;
    int pageno = 1;
    public static boolean isLastPage = false;
    LinearLayoutManager manager;
    public static int totalCount = -1;
    FragmentPeopleBinding binding;
    VideoPeopleFragViewModel viewModel;
    boolean tempIsSvs;
    String tempData;
    public VideoPeopleFrag(Context context, boolean isSvs, String data) {
        tempIsSvs = isSvs;
        this.context = context;
        tempData = data;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_people, viewGroup, false);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new VideoPeopleFragViewModel()).createFor()).get(VideoPeopleFragViewModel.class);
        binding.setViewModel(viewModel);

        initObserver();

        viewModel.isSvs = tempIsSvs;
        viewModel.data = tempData;

        binding.loading.setAnimation("loading.json");
        binding.loading.playAnimation();
        binding.loading.loop(true);


        manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.peopleRecycleView.setLayoutManager(manager);
        binding.peopleRecycleView.setHasFixedSize(true);
        viewModel.videoPeopleAdapter = new VideoPeopleAdapter(context, viewModel.list, binding.loading);
        binding.peopleRecycleView.setAdapter(viewModel.videoPeopleAdapter);
        viewModel.peopleSearchAdapter = new PeopleSearchAdapter(viewModel.list, getActivity(), binding.loading);
        binding.peopleRecycleView.setAdapter(viewModel.peopleSearchAdapter);

        if (ConnectionUtils.isConnected(context)) {
            if (viewModel.Loading) {
                viewModel.fetchData(pageno, context);
            }
        } else {
            Toast.makeText(context, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


        binding.peopleRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        if(VideoPeopleFrag.this.viewModel.data.length()>0){
                            viewModel.fetchDataSearch( context, String.valueOf(pageno),VideoPeopleFrag.this.viewModel.data);
                        }else {
                            viewModel.fetchData(pageno, context);
                        }

                    }
                }

            }
        });

        return binding.getRoot();
    }


    public void updateSearch(String search) {
        VideoPeopleFrag.this.viewModel.data = search;
        pageno=1;
        viewModel.fetchDataSearch( context, String.valueOf(pageno),search);
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
                binding.peopleRecycleView.setVisibility(View.VISIBLE);
            else
                binding.peopleRecycleView.setVisibility(View.GONE);
        });

        viewModel.dataNotFound.observe(requireActivity(), aBoolean -> {
            if (aBoolean)
                binding.noData.setVisibility(View.VISIBLE);
            else
                binding.noData.setVisibility(View.GONE);
        });

        viewModel.elasticSearchResponse.observe(requireActivity(), data -> {
            if (data != null) {
                CollectionPointAdapter collectionPointAdapter = new CollectionPointAdapter(context, data);
                binding.peopleRecycleView.setAdapter(collectionPointAdapter);
            }

        });
    }


}

