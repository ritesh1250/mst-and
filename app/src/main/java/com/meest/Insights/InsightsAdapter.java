package com.meest.Insights;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.meest.responses.UserInsightsResponse;

public class InsightsAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    UserInsightsResponse insightResponse;

    public InsightsAdapter(Context c, FragmentManager fm, int totalTabs, UserInsightsResponse insightResponse) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
        this.insightResponse = insightResponse;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Content contentFragment = Content.newInstance(insightResponse);
                return contentFragment;
            case 1:
                InsightGraphFragment activityFragment = new InsightGraphFragment();
                return activityFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
