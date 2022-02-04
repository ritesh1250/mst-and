package com.meest.social.socialViewModel.adapter.trendingAdapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.meest.social.socialViewModel.view.trending.TopPostFrag;
import com.meest.social.socialViewModel.view.trending.TrendingFrag;

public class TopTrendingSearchPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    // private String tabTitles[] = new String[] { "Tab1", "Tab2", "Tab3" };
    private Context context;
    private boolean isSvs;
    private String data;
    private Fragment fragment;

    public TopTrendingSearchPagerAdapter(@NonNull FragmentManager fm, Context context, boolean issvs, String data) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context=context;
        this.isSvs=issvs;
        this.data=data;
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                fragment = new TrendingFrag(context,  isSvs,data);
                return fragment;

            case 1:
                fragment = new TopPostFrag(context,  isSvs,data);
                return fragment;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
