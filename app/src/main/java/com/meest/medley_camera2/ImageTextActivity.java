package com.meest.medley_camera2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;

import com.meest.R;
import com.meest.databinding.ActivityImageTextBinding;
import com.meest.databinding.ActivityImageTextMedleyBinding;
import com.meest.medley_camera2.adapter.ImageTextAdapter;
import com.meest.medley_camera2.model.gallery.GalleryPhotoModel;

import java.util.ArrayList;

public class ImageTextActivity extends AppCompatActivity {

    ActivityImageTextMedleyBinding activityImageTextBinding;

    ImageTextAdapter imageTextAdapter;
    ArrayList<GalleryPhotoModel> galleryPhotoModelList = new ArrayList<>();
    int LAUNCH_SECOND_ACTIVITY = 1231;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityImageTextBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_text_medley);

        galleryPhotoModelList = getIntent().getParcelableArrayListExtra("result");

        activityImageTextBinding.mMessagesView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        linearSnapHelper.attachToRecyclerView(activityImageTextBinding.mMessagesView);
        imageTextAdapter = new ImageTextAdapter(ImageTextActivity.this,galleryPhotoModelList);
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