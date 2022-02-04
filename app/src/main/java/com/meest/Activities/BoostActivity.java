package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;

public class BoostActivity extends AppCompatActivity {
    private TextView txt_boost;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boost_activity);
        txt_boost = findViewById(R.id.txt_boost);
        txt_boost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BoostActivity.this,CreateBoostActivity.class);
                startActivity(intent);
            }
        });
    }
}
