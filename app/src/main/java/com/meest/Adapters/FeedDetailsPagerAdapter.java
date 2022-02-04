package com.meest.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.meest.Fragments.ExoRecyclerViewFragment;

import java.util.ArrayList;
import java.util.List;

public class FeedDetailsPagerAdapter extends FragmentStatePagerAdapter {
    private List<ExoRecyclerViewFragment> mFragmentList = new ArrayList<>();


    public FeedDetailsPagerAdapter(FragmentManager manager, List<ExoRecyclerViewFragment> mFragmentList) {
        super(manager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}