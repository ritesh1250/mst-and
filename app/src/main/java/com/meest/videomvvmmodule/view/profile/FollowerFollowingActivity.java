package com.meest.videomvvmmodule.view.profile;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.meest.R;
import com.meest.databinding.ActivityFollowerFollowingBinding;
import com.meest.videomvvmmodule.adapter.FollowersPagerAdapter;
import com.meest.videomvvmmodule.model.user.User;
import com.meest.videomvvmmodule.viewmodel.FollowerFollowingViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class FollowerFollowingActivity extends AppCompatActivity {

    ActivityFollowerFollowingBinding binding;
    FollowerFollowingViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_follower_following);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new FollowerFollowingViewModel()).createFor()).get(FollowerFollowingViewModel.class);
        initView();
        initListeners();
        binding.setViewmodel(viewModel);
    }

    private void initView() {

        viewModel.itemType.set(getIntent().getIntExtra("itemtype", 0));
        viewModel.user = new Gson().fromJson(getIntent().getStringExtra("user"), User.class);
        viewModel.user_id = getIntent().getStringExtra("userid");
        FollowersPagerAdapter adapter = new FollowersPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, viewModel.user_id);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setCurrentItem(viewModel.itemType.get());

    }

    private void initListeners() {
        binding.tvFollower.setOnClickListener(v -> binding.viewPager.setCurrentItem(0));
        binding.tvFollowing.setOnClickListener(v -> binding.viewPager.setCurrentItem(1));
        binding.imgBack.setOnClickListener(v -> onBackPressed());
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                viewModel.itemType.set(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}