package com.meest.videomvvmmodule.view.music_bottomsheet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.meest.R;
import com.meest.databinding.ItemMusicVideoBinding;
import com.meest.databinding.MusicFragmentBinding;
import com.meest.medley_camera2.camera2.utills.SoundEventListener;
import com.meest.videomvvmmodule.adapter.MusicsCategoryAdapter;
import com.meest.videomvvmmodule.model.music.Musics;
import com.meest.videomvvmmodule.view.base.BaseFragment;
import com.meest.videomvvmmodule.view.music.MusicChildFragment;
import com.meest.videomvvmmodule.viewmodel.CameraViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

public class MusicFragment extends BaseFragment implements Player.EventListener {

    MusicFragmentBinding binding;
    MusicFragmentViewModel viewModel;
    private ItemMusicVideoBinding previousView;
    private SimpleExoPlayer player;
    private String previousUrl = "none";
    public MutableLiveData<Musics.SoundList> music = new MutableLiveData<>();
    private CameraViewModel parentViewModel;
    SoundEventListener soundEventListener;
    public static MusicFragment getNewInstance(int type) {
        MusicFragment fragment = new MusicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        soundEventListener = (SoundEventListener) context;
    }
    @NotNull
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.music_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            parentViewModel = ViewModelProviders.of(getActivity()).get(CameraViewModel.class);
        }
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new MusicFragmentViewModel()).createFor()).get(MusicFragmentViewModel.class);
        binding.setViewmodel(viewModel);
        initView();
        initListener();
        initObserve();
    }
    private void initObserve() {
        viewModel.music.observe(getViewLifecycleOwner(), music -> {
            if (music != null) {
                parentViewModel.onSoundSelect.setValue(music);
            }
        });
    }
    private void initListener() {
        viewModel.categoryAdapter.setOnItemClickListener(new MusicsCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemMusicVideoBinding view, int position, Musics.SoundList musics, int type) {
                switch (type) {
                    case 0:
                        stopPlay();
                        playAudio(view, musics);
                        break;
                    case 1:
                        viewModel.addFavoriteMusic(sessionManager.getUser().getData().getUserId(), musics.getSoundId());
                        break;
                    case 2:
                        stopPlay();
                        if (musics != null) {
                            soundEventListener.clickEvent(musics.getSoundId(), musics.getSound(),musics.getSoundTitle());
                        }
                        viewModel.music.setValue(musics);
                        break;
                }
            }
        });
    }

    private void initView() {
//        if ()
        if (getArguments() != null) {
            viewModel.itemType = getArguments().getInt("type");
        }
        if (viewModel.itemType == 0) {
            viewModel.getMusicList();
        } else if (viewModel.itemType == 1) {
            viewModel.getFavoriteList(sessionManager.getUser().getData().getUserId());
        }
        viewModel.isAdd.observe((LifecycleOwner) getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(getContext(), getResources().getString(R.string.successfully_added), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void stopPlay() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.removeListener(this);
            player.release();
        }
    }

    public void playAudio(ItemMusicVideoBinding view, final Musics.SoundList musics) {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.removeListener(this);
            player.release();
        }
        if (previousView != null) {
            previousView.btnSelect.setVisibility(View.GONE);
            previousView.spinKit.setVisibility(View.GONE);
        }
        previousView = view;

        if (previousUrl.equals(musics.getSound())) {
            previousUrl = "none";
            previousView.btnSelect.setVisibility(View.VISIBLE);

        } else {
            if (getActivity() != null) {
                previousUrl = musics.getSound();
                previousView.btnSelect.setVisibility(View.VISIBLE);

                player = new SimpleExoPlayer.Builder(getActivity()).build();

                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                        Util.getUserAgent(getActivity(), getActivity().getResources().getString(R.string.app_name)));

                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
//                        .createMediaSource(Uri.parse("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3"));
                        .createMediaSource(Uri.parse(musics.getSound()));
//                Const.ITEM_BASE_URL +
                player.prepare(videoSource);
                player.addListener(this);
                player.setPlayWhenReady(true);
            }
        }

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//        if (playbackState == Player.STATE_BUFFERING) {
//            previousView.spinKit.setVisibility(View.VISIBLE);
//        } else if (playbackState == Player.STATE_READY) {
//            previousView.spinKit.setVisibility(View.GONE);
//        }
    }
}
