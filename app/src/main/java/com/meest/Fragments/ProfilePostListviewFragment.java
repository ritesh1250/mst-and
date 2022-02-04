package com.meest.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Adapters.ProfilePostListViewAdapter;
import com.meest.R;

import java.util.ArrayList;

public class ProfilePostListviewFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> arrayList = new ArrayList<>();
    ProfilePostListViewAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_post_listview_fragment, viewGroup, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        for (int i = 0; i < 20; i++) {
            arrayList.add("");
        }

        adapter = new ProfilePostListViewAdapter(arrayList);
        //RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //recyclerView.setLayoutParams(lp);
        // recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //adapter.setHasStableIds(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(layoutManager);

        // int numberOfColumns = 2;
        // RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        recyclerView.setLayoutManager(layoutManager);


        adapter.setOnItemClickListener(new ProfilePostListViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.my_post_dialog, viewGroup, false);


                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                alertDialog.show();

            }
        });

        return view;

    }
}
