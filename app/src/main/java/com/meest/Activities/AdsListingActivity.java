package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Adapters.AdsListingAdapter;
import com.meest.model.AdsListingItem;
import com.meest.R;

import java.util.ArrayList;

public class AdsListingActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    Button btn_createNewAds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_listing);

        ArrayList<AdsListingItem> ALitems = new ArrayList<>();

        ALitems.add(new AdsListingItem(R.drawable.placeholder,"Learn latest programing language \n" +
                "here ","13-08-2020","18-08-2020","1000/-"));
        ALitems.add(new AdsListingItem(R.drawable.placeholder,"Learn latest programing language \n" +
                        "here ","18-08-2020","22-08-2020","1000/-"));

        recyclerView = findViewById(R.id.RecyclView_AdsListing);
        recyclerView.setHasFixedSize(true);
        adapter = new AdsListingAdapter(ALitems);

        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        btn_createNewAds = findViewById(R.id.btn_createNewAds);
        btn_createNewAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdsListingActivity.this,CreateNewAdsActivity.class);
                startActivity(intent);
            }
        });
    }
}