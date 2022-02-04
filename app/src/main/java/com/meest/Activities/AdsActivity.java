package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;

public class AdsActivity extends AppCompatActivity {

    private RelativeLayout create_ads;
    private Button see_more;
     private TextView txt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        txt1 = findViewById(R.id.txt_ads);
        String text1 = "If you spend"
                + "<font font COLOR=\'#000000\'><b>" + " Rs200" + "</b></font>"
                + ", We estimate " + "<font COLOR=\'#000000\'><b>" + "2400 - 3000" + "</b></font><br>"+
                "people will see your ads";

        txt1.setText(Html.fromHtml(text1));

        create_ads=(RelativeLayout)findViewById(R.id.layout_create_new_ads);
        create_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CreateNewAdsActivity.class);
                startActivity(intent);
            }
        });


        see_more=(Button)findViewById(R.id.btn_see_more);
        see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SeeMoreActivity.class);
                startActivity(intent);
            }
        });



    }
}