package com.meest.svs.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.meest.R;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class VideoPlayerActivity extends AppCompatActivity {

    CircleImageView ivUser;
    TextView tvUsername, tvLikes, tvComments, tvDesc, hashtags, musicName, txt_follow, views;
    ImageView ivPlay, ivDiscover, ivLike, ivMore, ivMusic, option_menu;
    LinearLayout viewLike, viewComment, viewShare, layout_profile;
    RelativeLayout main;
    CircularProgressBar cpb;
    RelativeLayout viewProgressbar;
    com.meest.utils.CircularProgressBar downloadProgressbar;
    ScrollView scrollView;
    CardView ivMusicLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        findIds();
    }

    private void findIds() {
        ivUser = findViewById(R.id.ivUser);
        tvUsername = findViewById(R.id.tvUsername);
        layout_profile = findViewById(R.id.layout_profile);
        tvDesc = findViewById(R.id.tvDesc);
        ivMore = findViewById(R.id.ivMore);
        ivMusic = findViewById(R.id.ivMusic);
        txt_follow = findViewById(R.id.txt_follow);
        ivPlay = findViewById(R.id.ivPlay);
        tvLikes = findViewById(R.id.tvLikes);
        tvComments = findViewById(R.id.tvComments);
        option_menu = findViewById(R.id.option_menu);
        viewLike = findViewById(R.id.viewLike);
        viewComment = findViewById(R.id.viewComment);
        viewShare = findViewById(R.id.viewShare);
        views = findViewById(R.id.txt_videowatch);
        hashtags = findViewById(R.id.hashtags);
        ivMusicLayout = findViewById(R.id.ivMusicLayout);
        musicName = findViewById(R.id.musicName);
        cpb = findViewById(R.id.cpb);
        main = findViewById(R.id.main);
        viewProgressbar = findViewById(R.id.viewProgressbar);
        downloadProgressbar = findViewById(R.id.downloadProgressbar);
        ivDiscover = findViewById(R.id.ivDiscover);
        ivLike = findViewById(R.id.ivLike);
        scrollView = findViewById(R.id.scrollView);

    }
}