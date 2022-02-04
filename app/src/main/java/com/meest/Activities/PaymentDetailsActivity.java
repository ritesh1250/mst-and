package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;

public class PaymentDetailsActivity extends AppCompatActivity {
    private LinearLayout txt_new_payment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ads_payment_pre_details);
        txt_new_payment = findViewById(R.id.txt_new_payment);
        txt_new_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentDetailsActivity.this,AddPaymentMethod.class);
                startActivity(intent);
            }
        });
    }
}
