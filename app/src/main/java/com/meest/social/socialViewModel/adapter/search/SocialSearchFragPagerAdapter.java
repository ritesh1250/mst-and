package com.meest.social.socialViewModel.adapter.search;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.meest.social.socialViewModel.view.search.VideoHashtagFrag;
import com.meest.social.socialViewModel.view.search.VideoImageFrag;
import com.meest.social.socialViewModel.view.search.VideoPeopleFrag;
import com.meest.social.socialViewModel.view.search.VideoVideoFrag;

public class SocialSearchFragPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    // private String tabTitles[] = new String[] { "Tab1", "Tab2", "Tab3" };
    private Context context;
    private boolean isSvs;
    private String data;
    private Fragment fragment;

    public SocialSearchFragPagerAdapter(@NonNull FragmentManager fm, Context context, boolean issvs, String data) {
        super(fm);
        this.context=context;
        this.isSvs=issvs;
        this.data=data;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                fragment = new VideoPeopleFrag(context,  isSvs,data);
                break;
            case 1:
                fragment = new VideoVideoFrag(context,  isSvs,data,"video");
                break;
            case 2:
                fragment = new VideoHashtagFrag(context,  isSvs,data);
                break;
            case 3:
                fragment = new VideoImageFrag(context,  isSvs,data,"image");
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
