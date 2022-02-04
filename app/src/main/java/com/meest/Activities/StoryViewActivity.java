package com.meest.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class StoryViewActivity extends AppCompatActivity {
    ImageView img_view,img_profile;
    TextView txt_usernsme;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_view_acrtivity);
        img_view = findViewById(R.id.img_view);
        img_profile = findViewById(R.id.img_profile);
        txt_usernsme = findViewById(R.id.txt_usernsme);
        txt_usernsme.setText(getIntent().getStringExtra("name"));
        String img_url =getIntent().getStringExtra("img_url");
       // Picasso.with(StoryViewActivity.this).load(img_url).into(img_view);

        Glide.with(getApplicationContext())
                .load( getIntent().getStringExtra("img_pro"))
                .apply(new RequestOptions().override(400, 600)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .error(R.drawable.placeholder))
                .into(img_profile);

        Glide.with(getApplicationContext())
                .load( img_url)
                .apply(new RequestOptions().override(400, 600)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .error(R.drawable.placeholder))
                .into(img_view);
    }
}
