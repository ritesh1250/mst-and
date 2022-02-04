package com.meest.videomvvmmodule.view.music;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import com.meest.databinding.ItemMusicVideoBinding;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.meest.R;
import com.meest.databinding.FragmentMusicFrameBinding;
import com.meest.videomvvmmodule.model.music.Musics;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.viewmodel.CameraViewModel;
import com.meest.videomvvmmodule.viewmodel.MusicMainViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class MusicFrameFragment extends BottomSheetDialogFragment implements Player.EventListener {

    private FragmentMusicFrameBinding binding;
    private MusicMainViewModel viewModel;
    private CameraViewModel parentViewModel;
    private SimpleExoPlayer player;
    private ItemMusicVideoBinding previousView;
    private String previousUrl = "none";
    private SessionManager sessionManager;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = getActivity() != null ? new BottomSheetDialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                if (getChildFragmentManager().getBackStackEntryCount() > 0) {
                    getChildFragmentManager().popBackStack();
                    viewModel.isMore.set(false);
                    viewModel.isSearch.set(false);
                    binding.etSearch.clearFocus();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_music_frame, container, false);
        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
//        stopPlay();
        viewModel.stopMusic.setValue(true);
    }

    @Override
    public void onStop() {
        super.onStop();
//        stopPlay();
        viewModel.stopMusic.setValue(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            parentViewModel = ViewModelProviders.of(getActivity()).get(CameraViewModel.class);
        }
        binding.myLayout.requestFocus();
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new MusicMainViewModel()).createFor()).get(MusicMainViewModel.class);
        closeKeyboard();
        if (getActivity() != null) {
            sessionManager = new SessionManager(getActivity());
        }
        initListener();
        initObserve();
        getChildFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left)
                .add(R.id.frame, new MusicMainFragment())
                .commit();
        binding.setViewModel(viewModel);
    }

    private void initObserve() {
        viewModel.music.observe(getViewLifecycleOwner(), music -> {
            if (music != null) {
                parentViewModel.onSoundSelect.setValue(music);
                dismiss();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initListener() {
        binding.setOnBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicFrameFragment.this.getDialog() != null) {
//                stopPlay();
                    viewModel.stopMusic.setValue(true);
                    MusicFrameFragment.this.getDialog().onBackPressed();
                    binding.etSearch.clearFocus();
                    viewModel.isSearch.set(false);
                    getChildFragmentManager().popBackStack();
                }
            }
        });
        viewModel.searchMusicAdapter.setOnMusicClick((view, position, musics, type) -> {
            Log.e("musicPlay","==========="+"other");
            switch (type) {

                case 0:
                    stopPlay(view);
                    playAudio(view, musics);
                    break;
                case 1:
                    sessionManager.saveFavouriteMusic(musics.getSoundId());
                    if (sessionManager != null && sessionManager.getFavouriteMusic() != null) {
                        view.setIsFav(sessionManager.getFavouriteMusic().contains(musics.getSoundId()));
                    }
                    break;
                case 2:
                    stopPlay(view);
                    viewModel.music.setValue(musics);
                    break;
            }
        });

        binding.etSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !viewModel.isSearch.get()) {
                viewModel.isSearch.set(true);
                getChildFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right)
                        .replace(R.id.frame, new SearchMusicFragment())
                        .addToBackStack(SearchMusicFragment.class.getSimpleName())
                        .commit();
                viewModel.stopMusic.setValue(true);
            }
        });

        binding.tvCancel.setOnClickListener(v -> {
            if (binding.tvCancel.getText().equals(getResources().getString(R.string.cancel))) {
                closeKeyboard();
                binding.etSearch.clearFocus();
                viewModel.isSearch.set(false);
                getChildFragmentManager().popBackStack();
            } else {
                viewModel.onSearchTextChanged(binding.etSearch.getText());
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (getActivity() != null) {
//            stopPlay();
            viewModel.stopMusic.setValue(true);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public void closeKeyboard() {
        binding.etSearch.clearFocus();
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

    public void playAudio(ItemMusicVideoBinding view, final Musics.SoundList musics) {
        Log.e("musicPlay","==========="+"playAudio");
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

    private void stopPlay(ItemMusicVideoBinding view) {
        Log.e("musicPlay","==========="+"stopPlay");
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