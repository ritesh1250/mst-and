package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;

public class CreateNewAdsActivity extends AppCompatActivity {
    private TextView txt_preview,txt_payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_ads);
        txt_preview = findViewById(R.id.txt_preview);
        txt_payment = findViewById(R.id.txt_payment);

        txt_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateNewAdsActivity.this,AdspreviewActivity.class);
                startActivity(intent);
            }
        });

        txt_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateNewAdsActivity.this,PaymentDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}