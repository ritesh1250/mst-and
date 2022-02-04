package com.meest.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;

public class FeedPostFragment extends Fragment {

    RecyclerView fpf_recycler;

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_activity, viewGroup, false);

//        fpf_recycler = view.findViewById(R.id.fpf_recycler);

        return view;
    }
}
