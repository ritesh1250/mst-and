package com.meest.metme.adapter;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.meest.metme.ChatActivity;
import com.meest.metme.fragments.ChatListFragment;
import com.meest.metme.fragments.ChatRequestListFragment;

public class ChatFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    LinearLayout delete;
    ImageView searchIcon;
    EditText search;
    int selectedPos;
    // private String tabTitles[] = new String[] { "Tab1", "Tab2", "Tab3" };
    private ChatActivity context;
    private Fragment fragment;
    ChatListFragment chatListFragment;
    ChatRequestListFragment chatRequestListFragment;

    public ChatFragmentPagerAdapter(@NonNull FragmentManager fm, ChatListFragment chatListFragment, ChatRequestListFragment chatRequestListFragment) {
        super(fm);
        this.context = context;
        this.chatListFragment = chatListFragment;
        this.chatRequestListFragment = chatRequestListFragment;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                fragment = chatListFragment;
                break;
            case 1:
                fragment = chatRequestListFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
