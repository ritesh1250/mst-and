package com.meest.Insights;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.UserInsightsResponse;


public class Content extends Fragment {


    //the recyclerview
    RecyclerView recyclerView;

    public Content() {
        // Required empty public constructor
    }

    public static Content newInstance(UserInsightsResponse parameter) {

        Bundle args = new Bundle();
        args.putSerializable("parameter", parameter);
        Content fragment = new Content();
        fragment.setArguments(args);
        return fragment;
    }

    UserInsightsResponse userInsightResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        userInsightResponse = (UserInsightsResponse) getArguments().getSerializable("parameter");
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //creating recyclerview adapter
        InsightsRecyclerAdapter adapter = new InsightsRecyclerAdapter(getActivity(), userInsightResponse.getData().getRows());

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);

        return view;
    }
}