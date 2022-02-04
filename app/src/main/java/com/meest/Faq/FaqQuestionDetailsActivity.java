package com.meest.Faq;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;
import com.meest.responses.UserFAQResponse;

public class FaqQuestionDetailsActivity extends AppCompatActivity {

    ImageView backArrow;
    private WebView webView;
    TextView userQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_question_details);
        UserFAQResponse.Row userRow = (UserFAQResponse.Row) getIntent().getSerializableExtra("data");
        initViews();
        userQuestion.setText(userRow.getQuestion());
        webView.requestFocus();
        webView.getSettings().setLightTouchEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.setSoundEffectsEnabled(true);
        webView.loadData(userRow.getDescription(),
                "text/html", "UTF-8");
    }

    private void initViews() {
        userQuestion = findViewById(R.id.userQuestion);
        backArrow = findViewById(R.id.img_back_ques_faq);
        webView = findViewById(R.id.webView);
        userQuestion = findViewById(R.id.userQuestion);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}