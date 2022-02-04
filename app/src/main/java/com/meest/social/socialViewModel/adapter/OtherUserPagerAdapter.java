package com.meest.social.socialViewModel.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.meest.social.socialViewModel.view.otherUserAccount.OtherFriendPhotoGridFrag;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserCameraFrag;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserProfileVideoFrag;

import org.jetbrains.annotations.NotNull;

public class OtherUserPagerAdapter extends FragmentStatePagerAdapter {


    private static final String TAG = "TempPagerAdapter";

    Context context;
    String userId;

    //Constructor to the class
    public OtherUserPagerAdapter(FragmentManager fm, Context context, String userId) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        //Initializing tab count

        this.context = context;
        this.context = context;
        this.userId = userId;

    }

    //Overriding method getItem
    @Override
    public @NotNull Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                OtherFriendPhotoGridFrag tab1 = new OtherFriendPhotoGridFrag(context, userId);

                return tab1;
            case 1:
                OtherUserCameraFrag tab2 = new OtherUserCameraFrag(context, userId);
                return tab2;
            case 2:
                OtherUserProfileVideoFrag tab3 = new OtherUserProfileVideoFrag(context, userId);
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

}
