package com.meest.videomvvmmodule.view.home;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static com.meest.meestbhart.utilss.SharedPrefreances.MEDLEY_INTRO;
import static com.meest.utils.goLiveUtils.CommonUtils.getTargetView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.meest.R;
import com.meest.databinding.FragmentMainBinding;
import com.meest.medley_camera2.camera2.utills.CameraUtil;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.videomvvmmodule.adapter.HomeViewPagerAdapter;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.base.BaseFragment;
import com.meest.videomvvmmodule.view.recordvideo.Utilss;
import com.meest.videomvvmmodule.viewmodel.HomeViewModel;
import com.meest.videomvvmmodule.viewmodel.MainViewModel;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class HomeVideoFragment extends BaseFragment {

    FragmentMainBinding binding;

    private MainViewModel parentViewModel;
    private HomeViewModel viewModel;

    private MediaPlayer mediaPlayer;

    private static int CAMERA = 101;

    CustomDialogBuilder customDialogBuilder;
    Typeface bold, regular;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    public HomeVideoFragment(CustomDialogBuilder customDialogBuilder, Context context) {
        this.customDialogBuilder = customDialogBuilder;

        bold = Typeface.createFromAsset(context.getAssets(),
                "fonts/poppins_bold.ttf");
        regular = Typeface.createFromAsset(context.getAssets(),
                "fonts/poppins_regular.ttf");

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        assert getActivity() != null;
        if (SharedPrefreances.getSharedPreferenceString(getActivity(), MEDLEY_INTRO) == null || SharedPrefreances.getSharedPreferenceString(getActivity(), MEDLEY_INTRO).equals("")) {
            TapTargetSequence tapTargetSequence = new TapTargetSequence(getActivity()).targets(
                    getTargetView(getActivity(), binding.ivAddPost, getResources().getString(R.string.createVideoIntro), "")
            ).listener(new TapTargetSequence.Listener() {
                @Override
                public void onSequenceFinish() {
                    assert getContext() != null;
                    ((MainVideoActivity) getContext()).showTargetForMainVideoActivity();
                }

                @Override
                public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                }

                @Override
                public void onSequenceCanceled(TapTarget lastTarget) {

                }
            });
            tapTargetSequence.start();
        }

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        binding.ivAddPost.performClick();
                    }
                });


        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            parentViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        }


        viewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);


        initView();
        initViewPager();
        initObserve();
        initListener();

    }


    @Override
    public void onResume() {
        super.onResume();
//        binding.tvForu.setTypeface(bold);
//        binding.tvFollowing.setTypeface(regular);
//        binding.tvNearBy.setTypeface(regular);
//        binding.viewPager.setCurrentItem(1);
    }

    private void initView() {
        customDialogBuilder.hideLoadingDialog();
        binding.swipeRefresh.setProgressViewOffset(true, 0, 250);
//        assert getActivity() != null;
//        binding.swipeRefresh.setColorSchemeColors(
//                ContextCompat.getColor(getActivity(), R.color.napier_green),
//                ContextCompat.getColor(getActivity(), R.color.india_red),
//                ContextCompat.getColor(getActivity(), R.color.kellygreen),
//                ContextCompat.getColor(getActivity(), R.color.tufs_blue),
//                ContextCompat.getColor(getActivity(), R.color.tiffanyblue),
//                ContextCompat.getColor(getActivity(), R.color.Sanddtorm),
//                ContextCompat.getColor(getActivity(), R.color.salmonpink_1)
//        );

        binding.swipeRefresh.setOnRefreshListener(() -> {
            viewModel.onRefresh.setValue(true);
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.refresh_);
            mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
            mediaPlayer.setOnCompletionListener(mp -> {
                mediaPlayer.release();
                mediaPlayer = null;
            });
        });
    }

    private void initViewPager() {
        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPager.setAdapter(homeViewPagerAdapter);

        binding.viewPager.setCurrentItem(1);
        binding.tvForu.setTypeface(bold);
        binding.tvFollowing.setTypeface(regular);
        binding.tvNearBy.setTypeface(regular);
        viewModel.onPageSelect.postValue(1);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    assert getContext() != null;
                    if (new SessionManager(getContext()).getBooleanValue(Const.IS_LOGIN)) {
                        binding.tvForu.setTypeface(regular);
                        binding.tvFollowing.setTypeface(bold);
                        binding.tvNearBy.setTypeface(regular);
//                        binding.swipeRefresh.setEnabled(true);
                    } else {
                        Utilss.callLoginSign(getContext());
                    }

                } else if (position == 1) {
                    binding.tvFollowing.setTypeface(regular);
                    binding.tvForu.setTypeface(bold);
                    binding.tvNearBy.setTypeface(regular);
                } else {
                    assert getContext() != null;
                    if (new SessionManager(getContext()).getBooleanValue(Const.IS_LOGIN)) {
                        binding.tvFollowing.setTypeface(regular);
                        binding.tvForu.setTypeface(regular);
                        binding.tvNearBy.setTypeface(bold);
                    } else {
                        Utilss.callLoginSign(getContext());
                    }
                }
                viewModel.onPageSelect.postValue(position);
                viewModel.onStop.setValue(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Timber.e("onPageScrolled: ");
            }
        });
    }

    private void initObserve() {
        parentViewModel.onStop.observe(getViewLifecycleOwner(), onStop -> {
            viewModel.onPageSelect.setValue(binding.viewPager.getCurrentItem());
            viewModel.onStop.postValue(onStop);
        });
        viewModel.loadingVisibility = parentViewModel.loadingVisibility;
        viewModel.isShowLoaderInHome = parentViewModel.isShowLoaderInHome;
        viewModel.scroll = parentViewModel.scroll;
        viewModel.onRefresh.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null && !aBoolean) {
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void initListener() {
        binding.tvFollowing.setOnClickListener(v -> binding.viewPager.setCurrentItem(0));

        binding.tvForu.setOnClickListener(v -> binding.viewPager.setCurrentItem(1));

        binding.tvNearBy.setOnClickListener(v -> binding.viewPager.setCurrentItem(2));

        binding.ivAddPost.setOnClickListener(v -> {
            assert getContext() != null;
            if (new SessionManager(getContext()).getBooleanValue(Const.IS_LOGIN)) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (SharedPrefreances.getSharedPreferenceBoolean(requireContext(), SharedPrefreances.PERMISSION_PREFERENCE, false)) {
                        customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_permission, null), getContext().getResources().getString(R.string.to_capture_photos_and_videos),
                                getContext().getResources().getString(R.string.not_now), getContext().getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                                    @Override
                                    public void onPositiveDismiss() {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        assert getActivity() != null;
                                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onNegativeDismiss() {

                                    }
                                });
                    } else {
                        customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_permission, null), getContext().getResources().getString(R.string.to_capture_photos_and_videos),
                                getContext().getResources().getString(R.string.not_now), getContext().getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                                    @Override
                                    public void onPositiveDismiss() {
                                        SharedPrefreances.setSharedPreferenceBoolean(requireContext(), SharedPrefreances.PERMISSION_PREFERENCE, true);
                                        requestPermissionLauncher.launch(
                                                Manifest.permission.CAMERA);
                                    }

                                    @Override
                                    public void onNegativeDismiss() {

                                    }
                                });
                    }

                } else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    if (SharedPrefreances.getSharedPreferenceBoolean(requireContext(), SharedPrefreances.PERMISSION_PREFERENCE_MICRO, false)) {
                        customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_microphone_permission, null), requireContext().getResources().getString(R.string.to_capture_microphone),
                                requireContext().getResources().getString(R.string.not_now), requireContext().getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                                    @Override
                                    public void onPositiveDismiss() {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        assert getActivity() != null;
                                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onNegativeDismiss() {

                                    }
                                });
                    } else {
                        customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_microphone_permission, null), getContext().getResources().getString(R.string.to_capture_microphone),
                                getContext().getResources().getString(R.string.not_now), getContext().getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                                    @Override
                                    public void onPositiveDismiss() {
                                        SharedPrefreances.setSharedPreferenceBoolean(requireContext(), SharedPrefreances.PERMISSION_PREFERENCE_MICRO, true);

                                        requestPermissionLauncher.launch(
                                                Manifest.permission.RECORD_AUDIO);
                                    }

                                    @Override
                                    public void onNegativeDismiss() {

                                    }
                                });
                    }
                } else {
//                    startActivity(new Intent(getContext(), CameraActivity.class));
                    startActivityForResult(new Intent(getContext(), com.meest.medley_camera2.camera2.view.activity.CameraActivity.class), CAMERA);
                    CameraUtil.comeFrom = "Medley";
                }
            } else {
                Utilss.callLoginSign(getContext());
            }
        });

        binding.ivChatpage.setOnClickListener(v ->

        {
            assert getContext() != null;
            if (new SessionManager(getContext()).getBooleanValue(Const.IS_LOGIN)) {
                Intent intent = new Intent(getActivity(), VideoChatActivity.class);
                startActivity(intent);
            } else {
                Utilss.callLoginSign(getContext());
            }
        });
    }
}
