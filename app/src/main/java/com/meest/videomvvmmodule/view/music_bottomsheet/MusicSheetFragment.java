package com.meest.videomvvmmodule.view.music_bottomsheet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.exoplayer2.Player;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.meest.R;
import com.meest.databinding.FragmentMusicSheetBinding;
import com.meest.videomvvmmodule.view.music.MusicFrameFragment;
import com.meest.videomvvmmodule.view.music.MusicMainFragment;
import com.meest.videomvvmmodule.view.music.SearchMusicFragment;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;


public class MusicSheetFragment extends BottomSheetDialogFragment{

    private FragmentMusicSheetBinding binding;
    private MusicSheetViewModel viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = getActivity() != null ? new BottomSheetDialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                if (getChildFragmentManager().getBackStackEntryCount() > 0) {
                    getChildFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
            }
        } : (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        bottomSheetDialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog dialog = (BottomSheetDialog) dialog1;
            FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
                BottomSheetBehavior.from(bottomSheet).setHideable(true);
                if (getActivity() != null) {
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    getActivity().getWindow().setNavigationBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                }

            }
        });

        return bottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_music_sheet, container, false);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new MusicSheetViewModel()).createFor()).get(MusicSheetViewModel.class);
        binding.setViewModel(viewModel);
        closeKeyboard();
        initListener();
        initViewPager();
        initView();
        return binding.getRoot();
    }

    private void initView() {
        binding.setOnBackClick(v -> {
            if (MusicSheetFragment.this.getDialog() != null) {
                MusicSheetFragment.this.getDialog().onBackPressed();
                binding.etSearch.clearFocus();
                viewModel.isSearch.set(false);
                getChildFragmentManager().popBackStack();
            }
        });
        binding.etSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !viewModel.isSearch.get()) {
                viewModel.isSearch.set(true);
                binding.tvCancel.setVisibility(View.VISIBLE);
            }
        });

        binding.tvCancel.setOnClickListener(v -> {
            closeKeyboard();
            binding.etSearch.clearFocus();
            viewModel.isSearch.set(false);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initViewPager() {
        viewModel.selectPosition.set(0);
        MusicViewPagerAdapter adapter = new MusicViewPagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setCurrentItem(viewModel.selectPosition.get());
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                checkTab(position);
                viewModel.selectPosition.set(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.tvDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvFavourite.setTypeface(null, Typeface.NORMAL);
                binding.tvDiscover.setTypeface(null, Typeface.BOLD);
                selectTab(0);
            }
        });
        binding.tvFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvFavourite.setTypeface(null, Typeface.BOLD);
                binding.tvDiscover.setTypeface(null, Typeface.NORMAL);
                selectTab(1);

            }
        });
    }

    private void selectTab(int i) {
        viewModel.selectPosition.set(i);
        binding.viewPager.setCurrentItem(i);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initListener() {

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getActivity() != null) {

        }
    }

    public void closeKeyboard() {
        if (getDialog() != null) {
            InputMethodManager im = (InputMethodManager) getDialog().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null && getDialog().getCurrentFocus() != null) {
                im.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
            }
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        }
    }

    public void checkTab(int check) {
        binding.viewPager.setCurrentItem(check);
    }
}