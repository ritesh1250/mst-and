package com.meest.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;

public class NewPostActivity extends AppCompatActivity {
    ImageView ing_profile;
    Uri image_uri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_post_upload_activity);
        ing_profile = findViewById(R.id.img_profile);
        image_uri = Uri.parse(getIntent().getStringExtra("image_uri"));
        ing_profile.setImageURI(image_uri);


        //image_uri
    }
}
