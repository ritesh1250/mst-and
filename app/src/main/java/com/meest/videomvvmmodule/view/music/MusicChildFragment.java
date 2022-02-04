package com.meest.videomvvmmodule.view.music;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.meest.databinding.ItemMusicVideoBinding;
import com.meest.medley_camera2.camera2.utills.SoundEventListener;
import com.meest.videomvvmmodule.adapter.MusicsCategoryAdapter;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.meest.R;
import com.meest.databinding.FragmentMusicChildBinding;
import com.meest.videomvvmmodule.model.music.Musics;
import com.meest.videomvvmmodule.view.base.BaseFragment;
import com.meest.videomvvmmodule.viewmodel.MusicChildViewModel;
import com.meest.videomvvmmodule.viewmodel.MusicViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicChildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicChildFragment extends BaseFragment implements Player.EventListener {
    private static final String TAG = "MusicChildFragment";
    private FragmentMusicChildBinding binding;
    private MusicViewModel parentViewModel;
    private MusicChildViewModel viewModel;
    private SimpleExoPlayer player;
    private ItemMusicVideoBinding previousView;
    private String previousUrl = "none";
    private int type;
    private CustomDialogBuilder customDialogBuilder;
    SoundEventListener soundEventListener;

    public static MusicChildFragment newInstance(int type) {
        MusicChildFragment fragment = new MusicChildFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        soundEventListener = (SoundEventListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type", -1);
        }
        customDialogBuilder = new CustomDialogBuilder(getContext());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_music_child, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getParentFragment() != null) {
            parentViewModel = ViewModelProviders.of(getParentFragment()).get(MusicViewModel.class);
        }
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new MusicChildViewModel()).createFor()).get(MusicChildViewModel.class);
        initView();
        initObserve();
        iniListener();
        viewModel.context = getContext();
        binding.setViewModel(viewModel);
    }

    private void initView() {
        viewModel.type = type;
        if (type == 1) {
            if (sessionManager.getFavouriteMusic() != null && !sessionManager.getFavouriteMusic().isEmpty()) {
                viewModel.getFavMusicList(sessionManager.getFavouriteMusic());
            }
        } else {
            viewModel.getMusicList();
        }
    }

    private void initObserve() {
        parentViewModel.stopMusic.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean it) {
                MusicChildFragment.this.stopPlay();
            }
        });
        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                customDialogBuilder.showLoadingDialog();
            } else {
                customDialogBuilder.hideLoadingDialog();
            }
        });
    }

    private void iniListener() {
        viewModel.categoryAdapter.setOnItemClickListener(new MusicsCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemMusicVideoBinding view, int position, Musics.SoundList musics, int type) {
                switch (type) {
                    case 0:
                        MusicChildFragment.this.stopPlay();
                        MusicChildFragment.this.playAudio(view, musics);
                        break;
                    case 1:
                        sessionManager.saveFavouriteMusic(musics.getSoundId());
                        if (sessionManager != null && sessionManager.getFavouriteMusic() != null) {
                            view.setIsFav(sessionManager.getFavouriteMusic().contains(musics.getSoundId()));
                        }
                        if (MusicChildFragment.this.type == 1) {
                            viewModel.musicsListAdapter.removeItem(position);
                        }
                        if (view.getIsFav()) {
                            view.icFavourite.setColorFilter(getResources().getColor(R.color.colorTheme));
//                        view.icFavourite.setImageResource(R.drawable.ic_bookmark_selected);
                        } else {
                            view.icFavourite.setColorFilter(getResources().getColor(R.color.unselectedFav));
//                        view.icFavourite.setImageResource(R.drawable.ic_bookmarks_unselected);
                        }
                        break;
                    case 2:
                        MusicChildFragment.this.stopPlay();
                        if (musics != null) {
                            soundEventListener.clickEvent(musics.getSoundId(), musics.getSound(),musics.getSoundTitle());
                        }

                        parentViewModel.music.setValue(musics);
                        break;
                }
            }
        });
        viewModel.musicsListAdapter.setOnMusicClick(viewModel.categoryAdapter.getOnItemClickListener());
//        more sound
        Bundle bundle = new Bundle();
        SearchMusicFragment searchMusicFragment = new SearchMusicFragment();

        viewModel.categoryAdapter.setOnItemMoreClickListener((lists, soundCategoryId, soundCategoryName) -> {
            bundle.putString("categoryName", soundCategoryName);
            searchMusicFragment.setArguments(bundle);
            parentViewModel.categoryWiseSoundList(soundCategoryId);
            parentViewModel.isMore.set(true);
            if (getParentFragment() != null) {
                if (getParentFragment().getParentFragment() != null) {
                    getParentFragment().getParentFragment().getChildFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right)
                            .replace(R.id.frame, searchMusicFragment)
                            .addToBackStack(SearchMusicFragment.class.getSimpleName())
                            .commit();
                }
            }
            stopPlay();
        });
    }

    public void playAudio(ItemMusicVideoBinding view, final Musics.SoundList musics) {

        if (previousView != null) {
            previousView.btnSelect.setVisibility(View.GONE);
            previousView.spinKit.setVisibility(View.GONE);
        }
        previousView = view;
        if (previousUrl.equals(musics.getSound())) {
            previousUrl = "none";
            previousView.btnSelect.setVisibility(View.GONE);
        } else {
            if (getActivity() != null) {
                previousUrl = musics.getSound();
                previousView.btnSelect.setVisibility(View.VISIBLE);
                player = new SimpleExoPlayer.Builder(getActivity()).build();
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                        Util.getUserAgent(getActivity(), getActivity().getResources().getString(R.string.app_name)));
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(Uri.parse(musics.getSound()));
                player.prepare(videoSource);
                player.addListener(this);
                player.setPlayWhenReady(true);
            }
        }

    }

    private void stopPlay() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.removeListener(this);
            player.release();

        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {
            previousView.spinKit.setVisibility(View.VISIBLE);
        } else if (playbackState == Player.STATE_READY) {
            previousView.spinKit.setVisibility(View.GONE);
        }
    }
}