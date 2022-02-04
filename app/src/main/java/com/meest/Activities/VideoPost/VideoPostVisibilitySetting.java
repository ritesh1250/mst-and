package com.meest.Activities.VideoPost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;

public class VideoPostVisibilitySetting extends AppCompatActivity {

    RadioGroup radioGroup;
    ImageView image_back;
    RadioButton publicRadio, privateRadio;
    String selectedVisibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_post_visibility_setting);

        findIds();

        if (getIntent().getExtras() != null) {
            selectedVisibility = getIntent().getExtras().getString("postVisibility", getString(R.string.Public));
            // setting unselected to all radio buttons
            publicRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cheakbox_uncheak, 0);
            privateRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cheakbox_uncheak, 0);
            if (selectedVisibility.equalsIgnoreCase("public")) {
                // selecting one on the basis of selected one
                publicRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_blue, 0);
            }  else if (selectedVisibility.equalsIgnoreCase(getString(R.string.Private))) {
                // selecting one on the basis of selected one
                privateRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_blue, 0);
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.v("harsh", "checkedId == " + checkedId);
                if (checkedId == R.id.vpvs_public) {
                    // adding icon manually
                    publicRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_blue, 0);
                    privateRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cheakbox_uncheak, 0);
                    selectedVisibility = getString(R.string.Public);
                } else  {
                    // adding icon manually
                    privateRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_blue, 0);
                    publicRadio.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cheakbox_uncheak, 0);
                    selectedVisibility = getString(R.string.Private);
                }
            }
        });

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // sending result
        Intent intent=new Intent();
        intent.putExtra("postVisibility", selectedVisibility);
        setResult(123, intent);
        super.onBackPressed();
    }

    private void findIds() {
        image_back = findViewById(R.id.image_back);
        radioGroup = findViewById(R.id.vpvs_radioGroup);
        publicRadio = findViewById(R.id.vpvs_public);
        privateRadio = findViewById(R.id.vpvs_private);
  //      friendsRadio = findViewById(R.id.vpvs_friends);
    }
}