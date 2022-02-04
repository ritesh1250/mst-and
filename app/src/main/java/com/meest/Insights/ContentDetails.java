package com.meest.Insights;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;
import com.meest.responses.UserInsightsResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.net.URLDecoder;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class ContentDetails extends AppCompatActivity {
    EmojiconTextView userStatus;
    ImageView backArrow, userImage;
    TextView  statusDate, peopleViewedCountDetails, engagementCountDetails, peopleReachedTV,
            postEngagementTV, viewTV, postShareTV, postCommentsTV, postWatchTimeTV;
    ProgressBar peopleCountProgressBar, engagementCountProgressBar;

    private void initViews() {
        peopleCountProgressBar = findViewById(R.id.seekBarOne_details);
        engagementCountProgressBar = findViewById(R.id.seekBartwo_details);
        backArrow = findViewById(R.id.img_back_insights);
        userImage = findViewById(R.id.imv_detail);
        userStatus = findViewById(R.id.txt_details);
        statusDate = findViewById(R.id.txt_date_details);
        peopleViewedCountDetails = findViewById(R.id.txt_numberOne_details);
        engagementCountDetails = findViewById(R.id.txt_numberTwo_details);
        peopleReachedTV = findViewById(R.id.txt_514);
        postEngagementTV = findViewById(R.id.txt_224);
        viewTV = findViewById(R.id.txt_214);
        postShareTV = findViewById(R.id.txt_5);
        postCommentsTV = findViewById(R.id.txt_12);
        postWatchTimeTV = findViewById(R.id.txt_200_min);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_details);
        UserInsightsResponse.Row data = (UserInsightsResponse.Row) getIntent().getSerializableExtra("data");
        initViews();
        setData(data);
    }

    private void setData(UserInsightsResponse.Row rowData) {
        try {
            int totalEngagement = rowData.getShareCount() +
                    rowData.getCommentCounts() +
                    rowData.getPostSaveCounts() +
                    rowData.getLikeCounts() +
                    rowData.getDislikeCounts() +
                    rowData.getFollowersCount();

         //   userStatus.setText(rowData.getCaption());

            String test = URLDecoder.decode(rowData.getCaption(), "UTF-8");
             userStatus.setText(test);


            statusDate.setText(rowData.getCreatedAt());
            peopleViewedCountDetails.setText(rowData.getPeopleReached() + "");
            engagementCountDetails.setText(totalEngagement + "");
            postEngagementTV.setText(totalEngagement + "");
            peopleReachedTV.setText(rowData.getPeopleReached() + "");
            postShareTV.setText(rowData.getShareCount() + "");
            postCommentsTV.setText(rowData.getCommentCounts() + "");
//            postWatchTimeTV.setText(rowData.getW()+"");
            viewTV.setText(rowData.getPostViews()+"");
            peopleCountProgressBar.setProgress(rowData.getPeopleReached());
            engagementCountProgressBar.setProgress(totalEngagement);
            engagementCountProgressBar.setMax(rowData.getTotalUsers());
            peopleCountProgressBar.setMax(rowData.getTotalUsers());
            try{
                Glide.with(this).load(rowData.getUser().getDisplayPicture())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .error(R.drawable.placeholder)).into(userImage);
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}