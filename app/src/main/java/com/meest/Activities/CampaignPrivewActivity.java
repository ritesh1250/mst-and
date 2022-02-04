package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;

public class CampaignPrivewActivity  extends AppCompatActivity {

    private ImageView back;
    private Button btn_pay_Amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_privew);

        btn_pay_Amount = findViewById(R.id.btn_pay_Amount);
        btn_pay_Amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CampaignPrivewActivity.this,PaymentDetailsActivity.class);
                startActivity(intent);
            }
        });

        back = (ImageView) findViewById(R.id.img_back_arrow_CampaignPreview);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}