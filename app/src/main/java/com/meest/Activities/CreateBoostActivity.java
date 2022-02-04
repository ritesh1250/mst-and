package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;

public class CreateBoostActivity extends AppCompatActivity {
    private TextView txt_boost;
    private RelativeLayout layout_objective;
    private ImageView img_back_arrow_feedback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boost_data_activity);
        txt_boost = findViewById(R.id.txt_boost);
        layout_objective = findViewById(R.id.layout_objective);
        img_back_arrow_feedback = findViewById(R.id.img_back_arrow_feedback);
        img_back_arrow_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        layout_objective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateBoostActivity.this,ObjectiveBoostActivity.class);
                startActivity(intent);
            }
        });
        txt_boost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateBoostActivity.this,PaymentDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}
