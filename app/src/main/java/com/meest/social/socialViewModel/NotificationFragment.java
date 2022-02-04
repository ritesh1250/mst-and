package com.meest.social.socialViewModel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Interfaces.NotificationDeleteCallback;
import com.meest.MainActivity;
import com.meest.R;
import com.meest.databinding.NotificationFragmentBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.viewModel.NotifcationSocialViewModel;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.networkcheck.OnNoInternetRetry;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class NotificationFragment extends Fragment implements NotificationDeleteCallback, OnNoInternetRetry {
    private static final String TAG = "NotificationFragment";
    int pageno = 1;
    NotificationFragmentBinding binding;
    NotifcationSocialViewModel viewModel;
    private boolean isSVS;

    public NotificationFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.notification_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new NotifcationSocialViewModel(getActivity(), getActivity(), binding, NotificationFragment.this)).createFor()).get(NotifcationSocialViewModel.class);
        if (ConnectionUtils.isConnected(getActivity())) {
            viewModel.fetchNotificationData(pageno);

        } else {
            ConnectionUtils.showNoConnectionDialog(getActivity(), this);
        }
        inIt();
        inItListner();
    }

    private void inItListner() {
        binding.followAccept.setOnClickListener(v -> viewModel.acceptRequest());
        binding.followDecline.setOnClickListener(v -> viewModel.rejectRequest());
    }

    private void inIt() {
        if (!isSVS) {
            viewModel.followsData();
        }
        binding.setNotifcationViewModel(viewModel);
        ((MainActivity) requireActivity()).setNotificationCount("0");
        SharedPrefreances.setSharedPreferenceInt(getActivity(), SharedPrefreances.NOIFICATION_COUNT,0);
        initObserver();
        onRefreshListener();
    }

    private void onRefreshListener() {
        binding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    Log.e(TAG, "onScrolled BOTTOM: ");
                    if (viewModel.Loading) {
                        viewModel.Loading = false;
                        pageno++;
                        viewModel.fetchNotificationData(pageno );
                    }
                } else if (!recyclerView.canScrollVertically(-1) && dy < 0) {
                    Log.e(TAG, "onScrolled TOP: ");
                }
            }
        });
    }

    private void initObserver() {
        viewModel.isLoading.observe(getActivity(), aBoolean -> {
            if (aBoolean) {
                Log.e(TAG, "Loader Show: ");
                binding.loading.setVisibility(View.VISIBLE);
            } else {
                binding.loading.setVisibility(View.GONE);
                Log.e(TAG, "Loader Hide: ");
            }
        });
    }

    @Override
    public void delete() {
        pageno=1;
        viewModel.fetchNotificationData(pageno);
    }

    @Override
    public void onRetry() {
        if (ConnectionUtils.isConnected(getActivity())) {
            viewModel.fetchNotificationData(pageno);
        } else {
            ConnectionUtils.showNoConnectionDialog(getActivity(), this);
        }

    }


}