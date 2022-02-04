package com.meest.videomvvmmodule.view.home;

import static com.meest.meestbhart.utilss.SharedPrefreances.MEDLEY_INTRO;
import static com.meest.utils.goLiveUtils.CommonUtils.getTargetView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.exoplayer2.C;
import com.meest.R;
import com.meest.databinding.FragmentMainNewBinding;
import com.meest.databinding.NewContainMainModelBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.view.base.BaseFragment;
import com.meest.videomvvmmodule.viewmodel.HomeViewModel;
import com.meest.videomvvmmodule.viewmodel.MainViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

public class HomeVideoFragmentNew extends BaseFragment {
    private FragmentMainNewBinding binding;
    private MainViewModel parentViewModel;
    private HomeViewModel viewModel;
    private MediaPlayer mediaPlayer;
    private CustomDialogBuilder customDialogBuilder;

    public NewContainMainModelBinding newContainMainModelBinding;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_new, container, false);
        customDialogBuilder = new CustomDialogBuilder(requireContext());
        if (SharedPrefreances.getSharedPreferenceString(getActivity(), MEDLEY_INTRO) == null ||
                SharedPrefreances.getSharedPreferenceString(getActivity(), MEDLEY_INTRO).equals("")) {
            TapTargetSequence tapTargetSequence = new TapTargetSequence(getActivity()).targets(
                    getTargetView(getActivity(), binding.ivCamera, getResources().getString(R.string.createVideoIntro), "")
                    // getTargetView(getActivity(), binding.ivChatpage, getResources().getString(R.string.sharePost), "")
                    //  getTargetView(getActivity(), img_camera, getResources().getString(R.string.comingsoon), "")
            ).listener(new TapTargetSequence.Listener() {
                @Override
                public void onSequenceFinish() {
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
                        binding.ivCamera.performClick();
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            parentViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        }
        viewModel = ViewModelProviders.of(requireActivity(), new ViewModelFactory(new HomeViewModel()).createFor()).get(HomeViewModel.class);
        initView();
        initViewPager();
//        initObserve();
//        initListener();
    }

    private void initView() {
        customDialogBuilder.hideLoadingDialog();
        binding.swipeRefresh.setProgressViewOffset(true, 0, 250);

        binding.swipeRefresh.setOnRefreshListener(() -> {
            viewModel.onRefresh.setValue(true);
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.refresh_);
            mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
            mediaPlayer.setOnCompletionListener(mp -> {
                mediaPlayer.release();
                mediaPlayer = null;
            });
        });
   /*   binding.tvForu.setTypeface(bold);
        binding.tvFollowing.setTypeface(regular);
        binding.tvLive.setTypeface(regular);*/
    }

    private void initViewPager() {

        viewModel.onPageSelect.postValue(1);
        ForUFragment fragment = ForUFragment.getNewInstance("1");

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.fl_container, fragment);
        transaction.commit();
    }
}
