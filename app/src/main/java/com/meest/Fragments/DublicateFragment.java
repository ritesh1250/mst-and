package com.meest.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Adapters.Duplicate_Adapter;
import com.meest.model.DuplicateItem;
import com.meest.R;

import java.util.ArrayList;

public class DublicateFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dublicate, viewGroup, false);

        ArrayList<DuplicateItem> Ditems = new ArrayList<>();

        Ditems.add(new DuplicateItem(R.drawable.placeholder,"purpose","1 post Engagement","Rs 1.51 per Post Engagement","Completed"));
        Ditems.add(new DuplicateItem(R.drawable.placeholder,"purpose","1 post Engagement","Rs 1.51 per Post Engagement","Completed"));


        recyclerView = view.findViewById(R.id.recycleView_duplicate);
        // recyclerView.setHasFixedSize(true);
        adapter = new Duplicate_Adapter(Ditems);
        manager = new LinearLayoutManager(getContext());
        // recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        // int numberOfColumns = 2;
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        recyclerView.setLayoutManager(manager);


        return view;
    }
}
