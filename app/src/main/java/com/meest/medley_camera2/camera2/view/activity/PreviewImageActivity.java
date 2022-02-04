package com.meest.medley_camera2.camera2.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.databinding.ActivityPreviewImageBinding;
import com.meest.databinding.ActivityPreviewImageMetmeBinding;
import com.meest.medley_camera2.camera2.utills.CameraUtil;
import com.meest.medley_camera2.camera2.viewmodels.PreviewImageViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;


public class PreviewImageActivity extends AppCompatActivity {

    String imageUri;
    private ArrayList<String> multiSnapsList = new ArrayList<>();
    public PreviewImageViewModel viewModel;
    public ActivityPreviewImageMetmeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_preview_image_metme);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new PreviewImageViewModel(this,PreviewImageActivity.this, binding)).createFor()).get(PreviewImageViewModel.class);

        binding.setViewModel(viewModel);
        viewModel.progressDialog = new ProgressDialog(this);
        viewModel.progressDialog.setCancelable(false);
        viewModel.progressDialog.setMessage("Saving Media..");
        viewModel.progressDialog.setCanceledOnTouchOutside(false);

        // multi capture images thumbnail images IV initializing
        clickEventsMultiCapture();
        clicklistener();
        if (getIntent().getStringExtra("image") != null) {
            imageUri = getIntent().getStringExtra("image");
            binding.imagePreviewSetImage.setImageURI(Uri.parse(imageUri));

        }

        //set the screenshot to the preview
        if (getIntent().getStringExtra("screenshotImage") != null) {
            imageUri = viewModel.getRealPathFromUri(CameraUtil.imageUri);
            binding.imagePreviewSetImage.setImageURI(CameraUtil.imageUri);
        }

        // mutli capture array list
        if (getIntent().getStringArrayListExtra("multiSnaps") != null) {
            multiSnapsList = getIntent().getStringArrayListExtra("multiSnaps");
            try {
                imageUri = multiSnapsList.get(0);
                setImageToPreview(multiSnapsList.get(0), binding.imagePreviewSetImage);
                showMultiCaptureImages();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

       binding.downloadMedia.setOnClickListener(v -> {
            viewModel.imageUriToBitmap(imageUri);
        });

        binding.nextButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("name", "");
            intent.putExtra("path", imageUri);
            intent.putExtra("isVideo", false);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });

    }
    private void clickEventsMultiCapture() {
        binding.multiCaptureImage01.setOnClickListener(v -> {
            binding.imagePreviewSetImage.setImageURI(Uri.parse((multiSnapsList.get(0))));
            imageUri = multiSnapsList.get(0);
        });

        binding.multiCaptureImage02.setOnClickListener(v -> {
            binding.imagePreviewSetImage.setImageURI(Uri.parse((multiSnapsList.get(1))));
            imageUri = multiSnapsList.get(1);

        });

        binding.multiCaptureImage03.setOnClickListener(v -> {
            binding.imagePreviewSetImage.setImageURI(Uri.parse((multiSnapsList.get(2))));
            imageUri = multiSnapsList.get(2);
        });
    }

    // method to set image to the preview image view
    private void setImageToPreview(String imageUri, ImageView image_preview_set_image) {
        runOnUiThread(() -> Glide.with(this)
                .load(imageUri)
                .into(image_preview_set_image));
    }


    private void showMultiCaptureImages() {
        binding.multiSnapCaptureLayout.setVisibility(View.VISIBLE);
        if (multiSnapsList.get(0) != null) {
            binding.multiCaptureImage01.setImageURI(Uri.parse(multiSnapsList.get(0)));
        }
        if (multiSnapsList.get(1) != null) {
            binding.multiCaptureImage02.setImageURI(Uri.parse(multiSnapsList.get(1)));

        }
        if (multiSnapsList.get(2) != null) {
            binding.multiCaptureImage03.setImageURI(Uri.parse(multiSnapsList.get(2)));
        }

    }

    public void showImage2(String imageUri, ImageView imageView) {
        runOnUiThread(() -> Glide.with(this)
                .load(imageUri)
                .into(imageView));
    }

    private static int IMAGE_PREVIEW = 1991;


    public void clicklistener(){
        binding.imagePreviewCloseBtn.setOnClickListener(v -> {
            startActivity();
        });

        binding.ivImagePreviewAdjust.setOnClickListener(v -> {
            Uri myUri = Uri.parse(imageUri);
            Uri x = Uri.fromFile(new File(String.valueOf(myUri)));
            if (CameraUtil.bitmap != null) {
                viewModel.startCropImageActivity(viewModel.getImageUri(CameraUtil.bitmap));
            } else {
                viewModel.startCropImageActivity(x);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    String photoUri = resultUri.toString();
                    try {
                        CameraUtil.bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(photoUri));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    showImage2(photoUri, binding.imagePreviewSetImage);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            }


            if (requestCode == IMAGE_PREVIEW) {
                binding.imagePreviewSetImage.setImageBitmap(CameraUtil.bitmap);
            }
            if (requestCode == 123) {

                showImage2(String.valueOf(data.getData()), binding.imagePreviewSetImage);
            }

            if (requestCode == 321) {
                if (data != null) {
                    // get the returned data
                    Bundle extras = data.getExtras();
                    // get the cropped bitmap
                    Bitmap selectedBitmap = extras.getParcelable("data");
                    binding.imagePreviewSetImage.setImageBitmap(selectedBitmap);
                }

            }
        }
    }

    private void startActivity() {
        Intent i = new Intent(this, MeestCameraActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity();
    }


}
