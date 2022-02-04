package com.meest.metme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.meest.R;
import com.meest.databinding.ActivityChatMediaBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.fragments.ChatDocFragment;
import com.meest.metme.fragments.ChatImageFragment;
import com.meest.metme.fragments.ChatLinkFragment;
import com.meest.metme.viewmodels.ChatMediaViewModel;

public class ChatMediaActivity extends AppCompatActivity {
    LinearLayout layout_media, layout_docs, layout_links;
    String chatheadId, fullname;
    ActivityChatMediaBinding binding;
    String secondColor,otherUserId;
    ChatMediaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_chat_media);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_media);

        chatheadId = getIntent().getStringExtra("chatHeadId");
        fullname = getIntent().getStringExtra("fullname");
        secondColor = getIntent().getStringExtra("secondColor");
        otherUserId = getIntent().getStringExtra("otherUserId");
        viewModel = new ChatMediaViewModel();
        viewModel.fontName = SharedPrefreances.getSharedPreferenceString(this, chatheadId);
        binding.setViewModel(viewModel);

        layout_media = (LinearLayout) findViewById(R.id.layout_media);
        layout_docs = (LinearLayout) findViewById(R.id.layout_docs);
        layout_links = (LinearLayout) findViewById(R.id.layout_links);
        Bundle bundle = new Bundle();
        bundle.putString("chatHeadId", chatheadId);
        bundle.putString("otherUserId", otherUserId);
        bundle.putString("fullname", fullname);
        ChatImageFragment chatImageFragment = new ChatImageFragment();
        chatImageFragment.setArguments(bundle);
        pushFragment(chatImageFragment, "");

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layout_media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                view_1.setVisibility(View.VISIBLE);
//                view_2.setVisibility(View.GONE);
//                view_3.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putString("chatHeadId", chatheadId);
                bundle.putString("otherUserId", otherUserId);
                ChatImageFragment chatImageFragment = new ChatImageFragment();
                chatImageFragment.setArguments(bundle);
                pushFragment(chatImageFragment, "");
            }
        });

        layout_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                view_1.setVisibility(View.GONE);
//                view_2.setVisibility(View.VISIBLE);
//                view_3.setVisibility(View.GONE);
                Log.e("TAG", "jjjjj=" + getIntent().getStringExtra("chatHeadId"));
                Bundle bundle = new Bundle();
                bundle.putString("chatHeadId", chatheadId);
                bundle.putString("otherUserId", otherUserId);
                ChatDocFragment chatDocFragment = new ChatDocFragment();
                chatDocFragment.setArguments(bundle);
                pushFragment(chatDocFragment, "");
            }
        });

        layout_links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                view_1.setVisibility(View.GONE);
//                view_2.setVisibility(View.GONE);
//                view_3.setVisibility(View.VISIBLE);
                Bundle bundle = new Bundle();
                bundle.putString("edttext", "From Activity");
                bundle.putString("chatHeadId", chatheadId);
                bundle.putString("otherUserId", otherUserId);
                ChatLinkFragment chatLinkFragment = new ChatLinkFragment();
                chatLinkFragment.setArguments(bundle);
                pushFragment(chatLinkFragment, "");
            }
        });

        changeHeaderColor(secondColor);
    }

    private void changeHeaderColor(String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(0f);
            binding.mainContainer.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getColor(R.color.first_color), getColor(R.color.first_color)});
            gd.setCornerRadius(0f);
            binding.mainContainer.setBackground(gd);
        }
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

    public Bundle getMyData() {
        Bundle hm = new Bundle();
        hm.putString("chatHeadId", getIntent().getStringExtra("chatHeadId"));
        return hm;
    }

    @Override
    public void onBackPressed() {
//       super.onBackPressed();
//      if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(),"sub_back").equalsIgnoreCase("1")){
//          finish();
//      }
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment_byID = fm.findFragmentById(R.id.fragment_container);
        if (fragment_byID instanceof ChatImageFragment) {
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

}