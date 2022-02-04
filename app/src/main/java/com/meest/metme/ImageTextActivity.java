package com.meest.metme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.meest.R;
import com.meest.databinding.ActivityImageTextBinding;
import com.meest.metme.adapter.ImageTextAdapter;
import com.meest.metme.components.SnapHelperOneByOne;
import com.meest.metme.model.gallery.GalleryPhotoModel;

import java.util.ArrayList;

public class ImageTextActivity extends AppCompatActivity {

    ActivityImageTextBinding activityImageTextBinding;
    ImageTextAdapter imageTextAdapter;
    ArrayList<GalleryPhotoModel> galleryPhotoModelList = new ArrayList<>();
    int LAUNCH_SECOND_ACTIVITY = 1231;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityImageTextBinding = DataBindingUtil.setContentView(this,R.layout.activity_image_text);

        galleryPhotoModelList = getIntent().getParcelableArrayListExtra("result");

        activityImageTextBinding.mMessagesView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(activityImageTextBinding.mMessagesView);
        imageTextAdapter = new ImageTextAdapter(this,galleryPhotoModelList);
        activityImageTextBinding.setImageTextAdapter(imageTextAdapter);
        imageTextAdapter.notifyDataSetChanged();



    }

    public void onClick(ArrayList<GalleryPhotoModel> galleryPhotoModelList){

        Intent returnIntent = new Intent();
        returnIntent.putParcelableArrayListExtra("result", galleryPhotoModelList);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}