package com.meest.TopAndTrends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;


public class TrendVideosFragment extends Fragment {

    RecyclerView recyclerTrendVideos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_trend_videos, container, false);

        recyclerTrendVideos=view.findViewById(R.id.recyclerTrendVideos);
        return  view;
    }
}