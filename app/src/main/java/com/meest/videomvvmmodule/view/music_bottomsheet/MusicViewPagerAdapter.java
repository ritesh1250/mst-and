package com.meest.videomvvmmodule.view.music_bottomsheet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MusicViewPagerAdapter extends FragmentPagerAdapter {

    public MusicViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return MusicFragment.getNewInstance(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
