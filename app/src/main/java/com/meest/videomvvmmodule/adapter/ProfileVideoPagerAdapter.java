package com.meest.videomvvmmodule.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.meest.videomvvmmodule.view.profile.ProfileVideosFragment;

public class ProfileVideoPagerAdapter extends FragmentPagerAdapter {
    String userId;
    TextView tvViewCount;
    TextView tvPostCount;

    public ProfileVideoPagerAdapter(@NonNull FragmentManager fm, int behavior, TextView tvViewCount, TextView tvPostCount) {
        super(fm, behavior);
        this.tvViewCount = tvViewCount;
        this.tvPostCount = tvPostCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ProfileVideosFragment.getNewInstance(position,tvViewCount,tvPostCount);
    }

    @Override
    public int getCount() {
        return 2;
    }

}
