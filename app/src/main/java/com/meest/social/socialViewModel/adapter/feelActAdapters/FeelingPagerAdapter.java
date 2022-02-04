package com.meest.social.socialViewModel.adapter.feelActAdapters;

import android.content.Context;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.meest.social.socialViewModel.view.feelingAndActivity.ActivityFragment;
import com.meest.social.socialViewModel.view.feelingAndActivity.FeelingFragment;

public class FeelingPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount, selectedFeelingPos;
    EditText edt_search_feeling;
    private static final String TAG = "FeelingPagerAdapter";

    //Constructor to the class
    public FeelingPagerAdapter(FragmentManager fm, int tabCount, Context context, EditText edt_search_feeling) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //Initializing tab count
        this.tabCount = tabCount;
        this.edt_search_feeling = edt_search_feeling;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                FeelingFragment tab1 = new FeelingFragment(edt_search_feeling);
                return tab1;
            case 1:
                ActivityFragment tab2 = new ActivityFragment(edt_search_feeling);
                return tab2;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

}
