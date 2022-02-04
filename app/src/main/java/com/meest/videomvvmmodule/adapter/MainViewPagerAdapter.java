package com.meest.videomvvmmodule.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.view.home.HomeVideoFragment;
import com.meest.videomvvmmodule.view.home.HomeVideoFragmentNew;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;
import com.meest.videomvvmmodule.view.notification.NotificationFragment;
import com.meest.videomvvmmodule.view.profile.ProfileFragment;
import com.meest.videomvvmmodule.view.search.SearchNewFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    public CustomDialogBuilder customDialogBuilder;

    String path;

    public MainViewPagerAdapter(@NonNull FragmentManager fm, int behavior, MainVideoActivity context, CustomDialogBuilder customDialogBuilder, int count) {
        super(fm, behavior);
        this.context = context;
        this.customDialogBuilder = customDialogBuilder;
        this.path = path;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeVideoFragment(customDialogBuilder, context);
            case 1:
                return new SearchNewFragment(context);
            case 2:
                return new NotificationFragment(0);
//                return new SearchFragment();
            default:
                return new ProfileFragment(0);
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

}
