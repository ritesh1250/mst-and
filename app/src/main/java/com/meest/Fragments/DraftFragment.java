package com.meest.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Adapters.DraftAdapter;
import com.meest.model.DraftItem;
import com.meest.R;

import java.util.ArrayList;

public class DraftFragment extends Fragment  {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draft, viewGroup, false);


        ArrayList<DraftItem> Ditems = new ArrayList<>();

        Ditems.add(new DraftItem(R.drawable.placeholder,"World Cancer Day","Goal 5000 Engagement","Rs 1.51 per Post Engagement","Scheduled :7days Left"));
        Ditems.add(new DraftItem(R.drawable.placeholder,"World Cancer Day","Goal 5000 Engagement","Rs 1.51 per Post Engagement","Scheduled :7days Left"));

        recyclerView = view.findViewById(R.id.recycleView_draft);
        // recyclerView.setHasFixedSize(true);
        adapter = new DraftAdapter(Ditems);
        manager = new LinearLayoutManager(getContext());
        // recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        // int numberOfColumns = 2;
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        recyclerView.setLayoutManager(manager);

        return view;
    }
}
