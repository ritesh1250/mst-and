package com.meest.Adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public MyAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;

    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
//            case 0:
//
//                CampaignViewLifetimeFragment campaignViewLifetimeFragment= new CampaignViewLifetimeFragment();
//                return  campaignViewLifetimeFragment;
//
//            case 1:
//                CampaignViewLifetimeFragment campaignView7daysFragment= new CampaignViewLifetimeFragment();
//                return  campaignView7daysFragment;
//            case 2:
//                CampaignViewLifetimeFragment campaignView1dayFragment= new CampaignViewLifetimeFragment();
//                return  campaignView1dayFragment;

            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
