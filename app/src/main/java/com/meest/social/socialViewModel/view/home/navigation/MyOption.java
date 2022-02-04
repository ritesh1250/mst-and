package com.meest.social.socialViewModel.view.home.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.meest.R;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.responses.UserActivityResponse;
import com.meest.databinding.MyOptionModelBinding;
import com.meest.social.socialViewModel.viewModel.navigationViewModel.MyOptionViewModel;

import java.util.ArrayList;
import java.util.List;


public class MyOption extends AppCompatActivity {
    MyOptionViewModel viewModel;
    MyOptionModelBinding myOptionBinding;

    List<UserActivityResponse.Row> userResponseList = new ArrayList<>();
    LinearLayoutManager manager;
    private int pageno = 1;
    int lastScrollPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myOptionBinding = DataBindingUtil.setContentView(this, R.layout.my_option_model);
        viewModel = new ViewModelProvider(this).get(MyOptionViewModel.class);
        myOptionBinding.setMyOptionModel(viewModel);
        initView();
        initListeners();
        initObserve();

    }

    private void initView() {
        myOptionBinding.loading.setAnimation("loading.json");
        myOptionBinding.loading.playAnimation();
        myOptionBinding.loading.loop(true);
        if (ConnectionUtils.isConnected(MyOption.this)) {
            viewModel.getDataFromServer(false,String.valueOf(pageno), MyOption.this);

        } else {
            Utilss.showToast(MyOption.this, getString(R.string.no_internet), getResources().getColor(R.color.social_background_blue));
        }

        manager = new LinearLayoutManager(this);
        myOptionBinding.recyclerView.setLayoutManager(manager);
        myOptionBinding.recyclerView.setHasFixedSize(true);
    }

    private void initListeners() {
        myOptionBinding.imgBackArroeHelp.setOnClickListener(v -> onBackPressed());

        myOptionBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastScrollPosition = manager.findLastCompletelyVisibleItemPosition();
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    if (viewModel.Loading) {
                        viewModel.Loading = false;
                        pageno++;
                        viewModel.getDataFromServer(true,String.valueOf(pageno), MyOption.this);

                    }
                }
            }
        });

    }

    private void initObserve() {
        viewModel.userResponseList.observe(this, rows -> {
            if (rows.size() > 0) {
                viewModel.adapter.addLoading(rows);
                viewModel.adapter.notifyDataSetChanged();

                if (userResponseList.size() == viewModel.totalPage) {
                    viewModel.isLastPage.set(true);
                } else {
                    viewModel.isLastPage.set(false);
                }
//              viewModel.totalPage
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}