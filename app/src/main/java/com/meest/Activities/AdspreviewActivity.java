package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;

public class AdspreviewActivity extends AppCompatActivity {
    private TextView txt_payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adspreview);
        txt_payment = findViewById(R.id.txt_payment);
        txt_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdspreviewActivity.this,PaymentDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}