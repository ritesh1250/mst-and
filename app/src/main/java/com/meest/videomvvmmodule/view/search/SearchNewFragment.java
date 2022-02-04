package com.meest.videomvvmmodule.view.search;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.meest.R;
import com.meest.databinding.ActivitySearchBinding;
import com.meest.videomvvmmodule.adapter.SearchItemPagerAdapter;
import com.meest.videomvvmmodule.utils.CommonUtils;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.view.base.BaseFragment;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;
import com.meest.videomvvmmodule.viewmodel.MainViewModel;
import com.meest.videomvvmmodule.viewmodel.SearchActivityViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.util.Objects;

public class SearchNewFragment extends BaseFragment {

    private static final String TAG = "SearchNewFragment";

    ActivitySearchBinding binding;
    //    FragmentNewSearchBindingImpl binding;
    SearchActivityViewModel viewModel;
    private MainViewModel parentViewModel;
    Context context;
    private static final int MY_PERMISSIONS_REQUEST = 101;
    public static int count_view = 0;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    public SearchNewFragment(Context context) {
        this.context = context;
//        count_view = count;
    }

    public CustomDialogBuilder customDialogBuilder, customDialogBuilderForPermission;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_search, container, false);


        Log.e(TAG, "onCreateView: ");

        customDialogBuilder = new CustomDialogBuilder(getContext());
        customDialogBuilderForPermission = new CustomDialogBuilder((getContext()));
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initListeners();
        initView();

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        binding.imgQr.performClick();
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
            viewModel = ViewModelProviders.of((MainVideoActivity) context, new ViewModelFactory(new SearchActivityViewModel()).createFor()).get(SearchActivityViewModel.class);
            initObserve();
        }
        binding.setViewmodel(viewModel);
//        viewModel.sessionManager=sessionManager;
    }

    private void initObserve() {
        parentViewModel.selectedPosition.observe(getViewLifecycleOwner(), position -> {
            if (position != null && position == 1) {
                binding.etSearch.setText("");
                if (viewModel.count_view == 1) {
                    viewModel.searchtype.set(0);
                }
            }
        });
    }

    private void initView() {
//        viewModel.search_text = binding.etSearch.getText().toString();
        binding.etSearch.setText("");
        binding.ivSearchBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                CommonUtils.hideSoftKeyboard(Objects.requireNonNull(requireContext()));
                getActivity().onBackPressed();
            }
        });
        SearchItemPagerAdapter adapter = new SearchItemPagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPager.setAdapter(adapter);
//        viewModel.searchtype.set(0);
        binding.viewPager.setCurrentItem(0);
//        binding.etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    binding.etSearch.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                        }
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable s) {
//                            if(s.length()>0) {
//                                viewModel.afterTextChanged(s);
//                            }
//                        }
//                    });
//                }
//            }
//        });
    }

    private void initListeners() {

        binding.tvUsers.setOnClickListener(v -> {
            viewModel.searchtype.set(0);
            binding.viewPager.setCurrentItem(0);
        });

        binding.tvHashtags.setOnClickListener(v -> {
            viewModel.searchtype.set(1);
            binding.viewPager.setCurrentItem(1);
        });

        binding.tvVideos.setOnClickListener(v -> {
            viewModel.searchtype.set(2);
            binding.viewPager.setCurrentItem(2);
        });

        binding.imgQr.setOnClickListener(v -> initPermission());

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewModel.searchtype.set(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        if (SharedPrefreances.getSharedPreferenceString((MainVideoActivity) getContext(), MEDLEY_QR_INTRO) == null || SharedPrefreances.getSharedPreferenceString((MainVideoActivity) getContext(), MEDLEY_QR_INTRO).equals("")) {
//            TapTargetSequence tapTargetSequence = new TapTargetSequence((MainVideoActivity)getContext()).targets(
//                    //getTargetView(this, img_home, getResources().getString(R.string.homeIntro), ""),
//                    //getTargetView(this, img_search, getResources().getString(R.string.searchIntro), ""),
//                    //getTargetView(this, img_coming_soon, getResources().getString(R.string.comingSoon), ""),
//                    getTargetView((MainVideoActivity) getContext(), binding.imgQr, getContext().getString(R.string.qrIntro), "")
//                    //   getTargetView((MainVideoActivity) getContext(), binding.fabButton, getContext().getString(R.string.spinButtonIntro), "")
//                    //getTargetView(this, img_notification, getResources().getString(R.string.notificationIntro), ""),
//                    //getTargetView(this, ivUser, getResources().getString(R.string.profileIntro), "")
//            ).listener(new TapTargetSequence.Listener() {
//                @Override
//                public void onSequenceFinish() {
//                    SharedPrefreances.setSharedPreferenceString((MainVideoActivity) getContext(), MEDLEY_QR_INTRO, "false");
//
//                }
//
//                @Override
//                public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
//
//                }
//
//                @Override
//                public void onSequenceCanceled(TapTarget lastTarget) {
//
//                }
//            });
//            tapTargetSequence.start();
//        }
    }

    private void initPermission() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                customDialogBuilderForPermission.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_permission, null), getContext().getResources().getString(R.string.to_scan_qr_code_denied),
                        getContext().getResources().getString(R.string.not_now), getContext().getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            } else {
                customDialogBuilderForPermission.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_permission, null), getContext().getResources().getString(R.string.to_scan_qr_code),
                        getContext().getResources().getString(R.string.not_now), getContext().getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                requestPermissionLauncher.launch(
                                        Manifest.permission.CAMERA);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            }
        } else {
            startActivity(new Intent(getContext(), QRScanActivity.class));
        }
//        if (getActivity() != null && getActivity().getPackageManager() != null) {
//            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
//            ) {
//                requestPermissions(new String[]{Manifest.permission.CAMERA},
//                        MY_PERMISSIONS_REQUEST);
//            } else {
//
//            }
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && getActivity() != null && getActivity().getPackageManager() != null) {
                startActivity(new Intent(getContext(), QRScanActivity.class));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.etSearch.setText("");

        /*if(viewModel.setviewpager==0){
         *//*SearchItemPagerAdapter adapter = new SearchItemPagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                binding.viewPager.setAdapter(adapter);*//*
            viewModel.searchtype.set(0);
            binding.viewPager.setCurrentItem(0);
        }*/
    }
}
