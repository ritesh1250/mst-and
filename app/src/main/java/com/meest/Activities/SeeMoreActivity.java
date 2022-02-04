package com.meest.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Adapters.SeeMoreAdapter;
import com.meest.model.SeeMoreItem;
import com.meest.R;

import java.util.ArrayList;

public class SeeMoreActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_more);

        ArrayList<SeeMoreItem> SMitems = new ArrayList<>();

        SMitems.add(new SeeMoreItem("Spent","Rs 3000"));
        SMitems.add(new SeeMoreItem("People Reached","2,50,152"));
        SMitems.add(new SeeMoreItem("Impressions","15000"));
        SMitems.add(new SeeMoreItem("Post Engagement","3000"));
        SMitems.add(new SeeMoreItem("Post Reactions","1000"));
        SMitems.add(new SeeMoreItem("Photo Views","150000"));
        SMitems.add(new SeeMoreItem("Post Share","658"));
        SMitems.add(new SeeMoreItem("New Connection","458"));
        SMitems.add(new SeeMoreItem("Message Conversation","50"));
        SMitems.add(new SeeMoreItem("Post Comments","687"));


        recyclerView = findViewById(R.id.RecyclVIew_seeMore);
        recyclerView.setHasFixedSize(true);
        adapter = new SeeMoreAdapter(SMitems);

        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

    }
}