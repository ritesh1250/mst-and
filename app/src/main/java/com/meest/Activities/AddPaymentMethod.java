package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;

public class AddPaymentMethod extends AppCompatActivity {
    private TextView txt_add_payment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_method);
        txt_add_payment = findViewById(R.id.txt_add_payment);
        txt_add_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPaymentMethod.this,AddCardActivity.class);
                startActivity(intent);
            }
        });
    }
}
