package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;

public class ManagmentActivity extends AppCompatActivity {
    private TextView txt_Manage_ads,txt_Boost_post,txt_Manage_campaign;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.managment_activity);
        txt_Boost_post = findViewById(R.id.txt_Boost_post);
        txt_Manage_ads = findViewById(R.id.txt_Manage_ads);
        txt_Manage_campaign = findViewById(R.id.txt_Manage_campaign);
        txt_Manage_campaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagmentActivity.this,CampaignActivity.class);
                startActivity(intent);
            }
        });
        txt_Boost_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagmentActivity.this,BoostActivity.class);
                startActivity(intent);
            }
        });
        txt_Manage_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagmentActivity.this,AdsListingActivity.class);
                startActivity(intent);
            }
        });

        ImageView inmg_back = findViewById(R.id.inmg_back);
        inmg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}
