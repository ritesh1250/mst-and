package com.meest.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Adapters.DiscoverAdapter;
import com.meest.R;

import java.util.ArrayList;

public class DiscoverPeopleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    DiscoverAdapter followersAdapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ImageView back_arrow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_more_suggestion_activity);
        back_arrow = findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        for (int i =0; i < 3;i++){
            arrayList.add("");
        }



        followersAdapter = new DiscoverAdapter(getApplicationContext(),arrayList);
        recyclerView.setAdapter(followersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


    }
}
