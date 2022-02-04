package com.meest.social.socialViewModel.view.myAccountFrag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.meest.MainActivity;
import com.meest.R;
import com.meest.chat_calling.utils.SharedPreferencesManager;
import com.meest.databinding.MyAccountFragModelBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.networkcheck.OnNoInternetRetry;
import com.meest.social.socialViewModel.viewModel.myAccountFragViewModel.MyAccountFragViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

public class MyAccountFrag extends Fragment implements OnNoInternetRetry {
    //    OnNoInternetRetry context;
    MyAccountFragViewModel viewModel;
    MyAccountFragModelBinding binding;
    int lastScrollPosition = 0;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.my_account_frag_model, container, false);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new MyAccountFragViewModel()).createFor()).get(MyAccountFragViewModel.class);
        binding.setMyAccountFragModel(viewModel);
        SharedPrefreances.setSharedPreferenceString(requireActivity(), "back", "2");
        viewModel.viewPagerInit(getChildFragmentManager());

        viewModel.viewPagerInit(getChildFragmentManager());

        binding.txtUsername.setText(SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.USERNAME));

        inItviews();
        clickEvents();
        viewpagerFunc();
        initObserver();
        viewModel.viewPagerAdapter.add(new MyPhotoGridFrag(getActivity()), "Page 1");
        viewModel.viewPagerAdapter.add(new ProfileTagFrag(getActivity()), "Page 2");
        viewModel.viewPagerAdapter.add(new ProfileVideoFrag(getActivity()), "Page 3");
        binding.pager.setAdapter(viewModel.viewPagerAdapter);
        binding.pager.setOffscreenPageLimit(3);

        return binding.getRoot();
    }

    private void inItviews() {
        if (ConnectionUtils.isConnected(requireActivity())) {
            viewModel.fetchCurrentUserData(getActivity());
        } else {
            ConnectionUtils.showNoConnectionDialog(getActivity(), this);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new MyAccountFragViewModel()).createFor()).get(MyAccountFragViewModel.class);
    }

    private void initObserver() {
        viewModel.profileImgUri.observe(requireActivity(), s -> Glide.with(getActivity()).load(s).placeholder(R.drawable.placeholder).into(binding.imgProfile));
        viewModel.profileImgUriMale.observe(requireActivity(), s -> Glide.with(getActivity()).load(s).placeholder(R.drawable.male_place_holder).into(binding.imgProfile));
        viewModel.profileImgUriFemale.observe(requireActivity(), s -> Glide.with(getActivity()).load(s).placeholder(R.drawable.female_cardplaceholder).into(binding.imgProfile));
        viewModel.followerCount.observe(requireActivity(), s -> binding.txtFollowers.setText(s));
        viewModel.followTitle.observe(requireActivity(), s -> binding.tvFollowerTitle.setText(s));
        viewModel.followingCount.observe(requireActivity(), s -> binding.txtFollowingCount.setText(s));
        viewModel.txtPostCount.observe(requireActivity(), s -> binding.txtPost.setText(s));
        viewModel.txtPostTitleSet.observe(requireActivity(), s -> binding.tvPostTitle.setText(s));
        viewModel.tvBioSet.observe(requireActivity(), s -> binding.tvBio.setText(s));
        viewModel.coverPicEmpty.observe(requireActivity(), s -> {
            binding.txtUsername.setText(s.getDataUser().getUsername());
        });
    }

    private void viewpagerFunc() {
        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    binding.imgGrid.setImageResource(R.drawable.ic_selected_grid);
                    binding.imgVideo.setImageResource(R.drawable.ic_unselected_videos);
                    binding.imgCamera.setImageResource(R.drawable.ic_unselected_tag);
                    binding.pagerParent.setBackgroundResource(R.drawable.ic_path_1);
                    binding.layoutGridLine.setVisibility(View.VISIBLE);
                    binding.layoutVideoLine.setVisibility(View.INVISIBLE);
                    binding.layoutCameraLine.setVisibility(View.INVISIBLE);
                } else if (position == 1) {
//                    pager.setCurrentItem(1, true);
                    binding.imgGrid.setImageResource(R.drawable.ic_unselected_grid);
                    binding.imgVideo.setImageResource(R.drawable.ic_unselected_videos);
                    binding.imgCamera.setImageResource(R.drawable.ic_selected_tag);
                    binding.pagerParent.setBackgroundResource(R.drawable.ic_path_2);
                    binding.layoutGridLine.setVisibility(View.INVISIBLE);
                    binding.layoutVideoLine.setVisibility(View.INVISIBLE);
                    binding.layoutCameraLine.setVisibility(View.VISIBLE);
                } else {
//                    pager.setCurrentItem(2, true);
                    binding.imgGrid.setImageResource(R.drawable.ic_unselected_grid);
                    binding.imgVideo.setImageResource(R.drawable.ic_selected_videos);
                    binding.imgCamera.setImageResource(R.drawable.ic_unselected_tag);
                    binding.pagerParent.setBackgroundResource(R.drawable.ic_path_3);
                    binding.layoutGridLine.setVisibility(View.INVISIBLE);
                    binding.layoutVideoLine.setVisibility(View.VISIBLE);
                    binding.layoutCameraLine.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private int getItem(int i) {
        return binding.pager.getCurrentItem() + 1;
    }

    @SuppressLint("WrongConstant")
    private void clickEvents() {
        binding.backArrow.setOnClickListener(v -> viewModel.backPressed(getActivity()));
        binding.btnEditProfile.setOnClickListener(v -> viewModel.editProfileIntent(getActivity()));
        binding.layoutFollowing.setOnClickListener(v -> viewModel.followListIntent(getActivity()));
        binding.layoutFollowers.setOnClickListener(v -> viewModel.followListIntent1(getActivity()));
        binding.imgMenu.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            ((MainActivity) requireActivity()).drawer.openDrawer(Gravity.END);
        });

        if (!SharedPrefreances.getSharedPreferenceString(getActivity(), "Profile").isEmpty()) {
            Glide.with(getActivity()).load(SharedPrefreances.getSharedPreferenceString(getActivity(), "Profile")).into(binding.imgProfile);
        }

        binding.imgProfile.setOnClickListener(v -> viewModel.editProfileIntent(getActivity()));

        binding.layoutGrid.setOnClickListener(v -> {
            binding.pager.setCurrentItem(0, true);
            binding.imgGrid.setImageResource(R.drawable.ic_selected_grid);
            binding.imgVideo.setImageResource(R.drawable.ic_unselected_videos);
            binding.imgCamera.setImageResource(R.drawable.ic_unselected_tag);
            binding.pagerParent.setBackgroundResource(R.drawable.ic_path_1);
            binding.layoutGridLine.setVisibility(View.VISIBLE);
            binding.layoutVideoLine.setVisibility(View.INVISIBLE);
            binding.layoutCameraLine.setVisibility(View.INVISIBLE);
        });

        binding.layoutCamera.setOnClickListener(v -> {
            binding.pager.setCurrentItem(1, true);
            binding.imgGrid.setImageResource(R.drawable.ic_unselected_grid);
            binding.imgVideo.setImageResource(R.drawable.ic_unselected_videos);
            binding.imgCamera.setImageResource(R.drawable.ic_selected_tag);
            binding.pagerParent.setBackgroundResource(R.drawable.ic_path_2);
            binding.layoutGridLine.setVisibility(View.INVISIBLE);
            binding.layoutVideoLine.setVisibility(View.INVISIBLE);
            binding.layoutCameraLine.setVisibility(View.VISIBLE);
        });

        binding.layoutVideo.setOnClickListener(v -> {
            binding.pager.setCurrentItem(2, true);
            viewModel.status_post = 1;
            binding.imgGrid.setImageResource(R.drawable.ic_unselected_grid);
            binding.imgVideo.setImageResource(R.drawable.ic_selected_videos);
            binding.imgCamera.setImageResource(R.drawable.ic_unselected_tag);
            binding.pagerParent.setBackgroundResource(R.drawable.ic_path_3);
            binding.layoutGridLine.setVisibility(View.INVISIBLE);
            binding.layoutVideoLine.setVisibility(View.VISIBLE);
            binding.layoutCameraLine.setVisibility(View.INVISIBLE);
        });
    }


    @Override
    public void onRetry() {
        if (ConnectionUtils.isConnected(getActivity())) {
            viewModel.fetchCurrentUserData(getActivity());
        } else {
            ConnectionUtils.showNoConnectionDialog(getActivity(), this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.isRunning = false;
    }


    @Override
    public void onResume() {
        viewModel.fetchCurrentUserData(getActivity());
//        binding.imgGrid.setImageResource(R.drawable.ic_selected_grid);
//        binding.imgVideo.setImageResource(R.drawable.ic_unselected_videos);
//        binding.imgCamera.setImageResource(R.drawable.ic_unselected_tag);
//        binding.pagerParent.setBackgroundResource(R.drawable.ic_path_1);
//        binding.layoutGridLine.setVisibility(View.VISIBLE);
//        binding.layoutVideoLine.setVisibility(View.INVISIBLE);
//        binding.layoutCameraLine.setVisibility(View.INVISIBLE);
        String lang_eng = SharedPreferencesManager.getLanguage();
        viewModel.updatelanguage(lang_eng, getActivity());
        super.onResume();
    }

}