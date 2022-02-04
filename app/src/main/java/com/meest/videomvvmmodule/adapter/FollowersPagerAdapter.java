package com.meest.videomvvmmodule.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.meest.videomvvmmodule.view.profile.FollowersFragment;

public class FollowersPagerAdapter extends FragmentPagerAdapter {


    String userId;

    public FollowersPagerAdapter(@NonNull FragmentManager fm, int behavior, String user_id) {
        super(fm, behavior);
        this.userId=user_id;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return FollowersFragment.getNewInstance(String.valueOf(position),userId);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
