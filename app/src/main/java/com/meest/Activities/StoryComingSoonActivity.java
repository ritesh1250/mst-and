package com.meest.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.meest.R;

public class StoryComingSoonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_coming_soon);
        (findViewById(R.id.back_arrow)).setOnClickListener(view -> {
            finish();
        });
        if (getIntent().getStringExtra("title")!=null)
        ((TextView)findViewById(R.id.tvTitle)).setText(getIntent().getStringExtra("title"));
            else
            ((TextView)findViewById(R.id.tvTitle)).setText("Coming Soon");


    }
}