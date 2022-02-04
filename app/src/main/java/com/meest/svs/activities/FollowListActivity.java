package com.meest.svs.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Fragments.FollowListFragment;
import com.meest.R;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.responses.FeedFollowResponse;
import com.meest.svs.models.FollowListResponse;

import de.hdodenhof.circleimageview.CircleImageView;


public class FollowListActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView profileText;
    LinearLayout layout_follower, layout_following;
    TextView follower_count, following_count, number_of_followers, number_of_following;
    View follower_view, following_view;
    boolean isFollowersOpen = true, isSvs = true;
    LottieAnimationView loading;
    String type, userId, userName;
    ImageView id_back;
    FollowListResponse followListResponse;
    FeedFollowResponse feedFollowResponse;
    String changerole;

    boolean Loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow_list_activity);
        if (getIntent().getExtras() == null) {
            Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            finish();
        }

        type = getIntent().getExtras().getString("type", "follower");
        userId = getIntent().getExtras().getString("userId");
        userName = getIntent().getExtras().getString("userName");
        isSvs = getIntent().getExtras().getBoolean("isSvs", true);
        changerole = getIntent().getExtras().getString("changerole");
        Log.e("TAG", "maintasvs=" + isSvs + " " + type);
        findIds();
        number_of_followers.setText("Get it from the API");
        id_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        profileText.setText(getIntent().getStringExtra("otherUserName"));
        if (type.equalsIgnoreCase(getString(R.string.follower))) {
            follower_count.setTextColor(getResources().getColor(R.color.social_background_blue));
            following_count.setTextColor(getResources().getColor(R.color.color_dark_grey));
            number_of_followers.setTextColor(getResources().getColor(R.color.social_background_blue));
            number_of_following.setTextColor(getResources().getColor(R.color.color_dark_grey));
            follower_view.setVisibility(View.VISIBLE);
            following_view.setVisibility(View.INVISIBLE);
            isFollowersOpen = true;
        } else {
            following_count.setTextColor(getResources().getColor(R.color.social_background_blue));
            follower_count.setTextColor(getResources().getColor(R.color.color_dark_grey));
            number_of_following.setTextColor(getResources().getColor(R.color.social_background_blue));
            number_of_followers.setTextColor(getResources().getColor(R.color.color_dark_grey));

            following_view.setVisibility(View.VISIBLE);
            follower_view.setVisibility(View.INVISIBLE);
            isFollowersOpen = false;
        }

        loading.setAnimation(getString(R.string.loading_json));
        loading.playAnimation();
        loading.loop(true);

        layout_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollowersOpen) {
                    return;
                }follower_count.setTextColor(getResources().getColor(R.color.social_background_blue));
                following_count.setTextColor(getResources().getColor(R.color.gray));
                number_of_followers.setTextColor(getResources().getColor(R.color.social_background_blue));
                number_of_following.setTextColor(getResources().getColor(R.color.color_dark_grey));

                follower_view.setVisibility(View.VISIBLE);
                following_view.setVisibility(View.GONE);

                isFollowersOpen = true;
                // type = "follower";
                type = getString(R.string.follower);
                pushFeedFragment();


            }
        });

        layout_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFollowersOpen) {
                    return;
                }
                following_count.setTextColor(getResources().getColor(R.color.social_background_blue));
                follower_count.setTextColor(getResources().getColor(R.color.gray));
                number_of_following.setTextColor(getResources().getColor(R.color.social_background_blue));
                number_of_followers.setTextColor(getResources().getColor(R.color.color_dark_grey));
                following_view.setVisibility(View.VISIBLE);
                follower_view.setVisibility(View.GONE);
                isFollowersOpen = false;

                type = getString(R.string.following);
                pushFeedFragment();
            }
        });
        pushFeedFragment();

    }

    private void pushFeedFragment() {
        getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FollowListFragment(FollowListActivity.this, feedFollowResponse, type, changerole, isSvs,userId))
                    .addToBackStack("fragment")
                    .commit();
    }

    private void findIds() {
        circleImageView = findViewById(R.id.img_prof);
        profileText = findViewById(R.id.text_profile);
        follower_count = findViewById(R.id.follower_count);
        following_count = findViewById(R.id.following_count);
        follower_view = findViewById(R.id.follower_view);
        following_view = findViewById(R.id.following_view);
        loading = findViewById(R.id.loading);
        layout_follower = findViewById(R.id.layout_one);
        layout_following = findViewById(R.id.layout_two);
        id_back = findViewById(R.id.id_back);
        number_of_followers = findViewById(R.id.number_of_followers);
        number_of_following = findViewById(R.id.number_of_following);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}