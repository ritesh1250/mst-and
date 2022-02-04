package com.meest.Adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public TabAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
//            case 0:
//
//               CampaignViewGenderTabFragment campaignViewGenderTabFragment= new CampaignViewGenderTabFragment();
//                return  campaignViewGenderTabFragment;
//
//            case 1:
//                CampaignViewGenderTabFragment campaignViewAgeTabFragment= new CampaignViewGenderTabFragment();
//                return  campaignViewAgeTabFragment;
//
//            case 2:
//                CampaignViewGenderTabFragment campaignViewLocationTabFragment= new CampaignViewGenderTabFragment();
//                return  campaignViewLocationTabFragment;
//

            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
