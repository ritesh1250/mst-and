package com.meest.Activities.VideoPost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.meest.R;

public class VideoPostPrivacySetting extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    ImageView image_back;
    boolean allowComments, allowDuet, allowDownload, allowTrio;
    SwitchCompat switchAllowComments, switchAllowDuet, switchAllowDownload, switchAllowTrio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_post_privacy_setting);

        findIds();

        if(getIntent().getExtras() != null) {
            allowComments = getIntent().getExtras().getBoolean("allowComments", true);
            allowDuet = getIntent().getExtras().getBoolean("allowDuet", true);
            allowDownload = getIntent().getExtras().getBoolean("allowDownload", true);
            allowTrio = getIntent().getExtras().getBoolean("allowTrio", true);

            switchAllowComments.setChecked(allowComments);
            switchAllowDuet.setChecked(allowDuet);
            switchAllowDownload.setChecked(allowDownload);
            switchAllowTrio.setChecked(allowTrio);
        }

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // switch change listener
        switchAllowComments.setOnCheckedChangeListener(this);
        switchAllowDuet.setOnCheckedChangeListener(this);
        switchAllowDownload.setOnCheckedChangeListener(this);
        switchAllowTrio.setOnCheckedChangeListener(this);
    }

    private void findIds() {
        image_back = findViewById(R.id.image_back);
        switchAllowComments = findViewById(R.id.switch_allow_comments);
        switchAllowDuet = findViewById(R.id.switch_allow_duet);
        switchAllowDownload = findViewById(R.id.switch_allow_download);
        switchAllowTrio = findViewById(R.id.switch_allow_trio);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_allow_comments:
                allowComments = isChecked;
                break;
            case R.id.switch_allow_duet:
                allowDuet = isChecked;
                break;
            case R.id.switch_allow_download:
                allowDownload = isChecked;
                break;
            case R.id.switch_allow_trio:
                allowTrio = isChecked;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // sending result
        Intent intent=new Intent();
        intent.putExtra("allowComments", allowComments);
        intent.putExtra("allowDuet", allowDuet);
        intent.putExtra("allowDownload", allowDownload);
        intent.putExtra("allowTrio", allowTrio);
        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }
}