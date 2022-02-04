package com.meest.metme.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.meest.Activities.base.ApiCallActivity;
import com.meest.R;
import com.meest.responses.GetDocsMediaChatsResponse;
import com.meest.databinding.ActivityMediaImageFullPageBinding;
import com.meest.metme.components.SnapHelperOneByOne;

public class MediaImageFullPageActivity extends ApiCallActivity {

    ActivityMediaImageFullPageBinding binding;
    MediaImageFullPageDocAdapter adapter;
    public String secondColor;
    public String chatHeadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_media_image_full_page);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_media_image_full_page);

        binding.tvHeaderName.setText(getIntent("fullName"));
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("response");
        GetDocsMediaChatsResponse.Data response = gson.fromJson(strObj, GetDocsMediaChatsResponse.Data.class);
        if (getIntent() != null) {
            chatHeadId = getIntent().getStringExtra("chatHeadId");
            secondColor = getIntent().getStringExtra("secondColor");
        } else {
            chatHeadId = "";
            secondColor = "#143988";
        }
        binding.ivChatBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new MediaImageFullPageDocAdapter(response.getAll(), getIntent("fullName"), this);
        binding.recyclerView.setAdapter(adapter);
        binding.setMediaImageFullPageDocAdapter(adapter);

        binding.recyclerView.scrollToPosition(getIntent().getIntExtra("position", 0));
        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(binding.recyclerView);
        changeHeaderColor(secondColor);
    }

    private void changeHeaderColor(String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(0f);
//            binding.mainContainer.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getColor(R.color.first_color), getColor(R.color.first_color)});
            gd.setCornerRadius(0f);
//            binding.mainContainer.setBackground(gd);
        }
    }

}