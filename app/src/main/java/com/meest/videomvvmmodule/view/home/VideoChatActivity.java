package com.meest.videomvvmmodule.view.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meest.R;

public class VideoChatActivity extends AppCompatActivity {
    private ImageView back_arrow;
    private TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);
        back_arrow = findViewById(R.id.back_arrow);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.coming_soon));
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}