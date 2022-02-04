package com.meest.videomvvmmodule.view.music;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.meest.R;
import com.meest.databinding.FragmentMusicMainBinding;
import com.meest.videomvvmmodule.view.base.BaseFragment;
import com.meest.videomvvmmodule.viewmodel.MusicMainViewModel;
import com.meest.videomvvmmodule.viewmodel.MusicViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

public class MusicMainFragment extends BaseFragment {


    private FragmentMusicMainBinding binding;
    private MusicViewModel viewModel;
    private MusicMainViewModel parentViewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_music_main, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getParentFragment() != null) {
            parentViewModel = ViewModelProviders.of(getParentFragment()).get(MusicMainViewModel.class);
        }
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new MusicViewModel()).createFor()).get(MusicViewModel.class);
        initListener();
        binding.setViewModel(viewModel);
    }

    private void initListener() {
        openFragment(0);
        viewModel.music = parentViewModel.music;
        viewModel.isMore = parentViewModel.isMore;
        viewModel.searchMusicAdapter = parentViewModel.searchMusicAdapter;
        viewModel.stopMusic = parentViewModel.stopMusic;
        binding.tvDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvFavourite.setTypeface(null, Typeface.NORMAL);
                binding.tvDiscover.setTypeface(null, Typeface.BOLD);
                MusicMainFragment.this.openFragment(0);
            }
        });
        binding.tvFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvFavourite.setTypeface(null, Typeface.BOLD);
                binding.tvDiscover.setTypeface(null, Typeface.NORMAL);
                MusicMainFragment.this.openFragment(1);
            }
        });

    }

    private void openFragment(int position) {
        viewModel.selectPosition.set(position);
        parentViewModel.stopMusic.setValue(true);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frame, MusicChildFragment.newInstance(position))
                .commit();
    }
}