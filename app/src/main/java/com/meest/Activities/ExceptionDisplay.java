package com.meest.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.meest.R;

public class ExceptionDisplay extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception_display);

        TextView exception_text = (TextView) findViewById(R.id.exception_text);
        Button btnBack = (Button) findViewById(R.id.btnBack);
        exception_text.setText(getIntent().getExtras().getString("error"));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentData();
            }
        });
    }

    @Override
    public void onBackPressed() {
        intentData();
    }

    public void intentData() {

//        Intent setIntent = new Intent(ExceptionDisplay.this, AppDataSourceSelection.class);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(setIntent);
    }
}