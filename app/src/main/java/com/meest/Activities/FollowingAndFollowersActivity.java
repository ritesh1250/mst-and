package com.meest.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.meest.Fragments.FollowersFragment;
import com.meest.Fragments.FollowingsFragment;
import com.meest.R;

public class FollowingAndFollowersActivity extends AppCompatActivity {
    private LinearLayout layout_one,layout_two;
    private TextView txt_2,txt_1;
    private View view_1,view_2;
    private ImageView id_back;
    private ImageView ic_back_arrow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_followings_activity);
        txt_2 = findViewById(R.id.txt_2);
        txt_1 = findViewById(R.id.txt_1);

        view_1 = findViewById(R.id.view_1);
        view_2 = findViewById(R.id.view_2);

        id_back = findViewById(R.id.id_back);
        id_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        layout_two = findViewById(R.id.layout_two);
        layout_one = findViewById(R.id.layout_one);
        pushFragment(new FollowersFragment(getIntent().getStringExtra("userId")),"followers");
        layout_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_1.setTextColor(getResources().getColor(R.color.blue));
                txt_2.setTextColor(getResources().getColor(R.color.lan_gray));
                pushFragment(new FollowersFragment(getIntent().getStringExtra("userId")),"followers");
                view_1.setVisibility(View.VISIBLE);
                view_2.setVisibility(View.INVISIBLE);
            }
        });

        layout_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_2.setTextColor(getResources().getColor(R.color.blue));
                txt_1.setTextColor(getResources().getColor(R.color.lan_gray));
                pushFragment(new FollowingsFragment(getIntent().getStringExtra("userId")),"followers");
                view_2.setVisibility(View.VISIBLE);
                view_1.setVisibility(View.INVISIBLE);
            }
        });

        ic_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    public boolean pushFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
                    .addToBackStack("fragment")
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}
