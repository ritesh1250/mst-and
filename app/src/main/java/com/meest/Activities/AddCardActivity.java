package com.meest.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;

public class AddCardActivity extends AppCompatActivity {
    private ImageView img_back_arrow_feedback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card_activity);
        img_back_arrow_feedback = findViewById(R.id.img_back_arrow_feedback);
        img_back_arrow_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
