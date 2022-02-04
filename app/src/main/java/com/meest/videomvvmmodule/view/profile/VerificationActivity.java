package com.meest.videomvvmmodule.view.profile;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.meest.R;

import com.meest.databinding.ActivityVerificationBinding;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.media.BottomSheetImagePicker;
import com.meest.videomvvmmodule.viewmodel.VerificationViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import static com.meest.videomvvmmodule.utils.BindingAdapters.loadMediaImage;
import static com.meest.videomvvmmodule.utils.BindingAdapters.loadMediaRoundBitmap;

public class VerificationActivity extends BaseActivity {

    private static int CAPTURE_IMAGE = 100;
    ActivityVerificationBinding binding;
    VerificationViewModel viewModel;
    CustomDialogBuilder customDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verification);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new VerificationViewModel()).createFor()).get(VerificationViewModel.class);
        customDialogBuilder = new CustomDialogBuilder(this);
        viewModel.context = VerificationActivity.this;
        initView();
        initObserve();
        initListeners();
        binding.setViewModel(viewModel);
    }

    private void initView() {
        binding.idName.setText(new SessionManager(this).getUser().getData().getFirstName() + " " + new SessionManager(this).getUser().getData().getLastName());
        viewModel.name=new SessionManager(this).getUser().getData().getFirstName() + " " + new SessionManager(this).getUser().getData().getLastName();
    }

    private void initObserve() {
        viewModel.isLoading.observe(this, isLoading -> {
            if (isLoading) {
                customDialogBuilder.showLoadingDialog();
            } else {
                customDialogBuilder.hideLoadingDialog();
            }
        });

    }


    private void initListeners() {
        binding.imgBack.setOnClickListener(v -> onBackPressed());
        binding.setOnCaptureClick(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 10);
            } else {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAPTURE_IMAGE);
            }
        });
        binding.setOnAttachClick(v -> showPhotoSelectSheet());
    }

    private void showPhotoSelectSheet() {
        BottomSheetImagePicker bottomSheetImagePicker = new BottomSheetImagePicker();
        bottomSheetImagePicker.setOnDismiss(uri -> {
            if (!uri.isEmpty()) {
                loadMediaImage(binding.ivProof, uri, false);
                viewModel.proofUri = uri;
                viewModel.getIdCardUrl(uri);
            }
        });
        bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE) {
            if (data != null) {
                Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                File thumbFile = new File(getPath(), new SessionManager(this).getUser().getData().getUserName()+"_aadhar_card.jpg");
                if (thumbFile.exists()) {
                    thumbFile.delete();
                }
                try {
                    FileOutputStream stream = new FileOutputStream(thumbFile);
                    if (photo != null) {
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    }
                    stream.flush();
                    stream.close();
                    viewModel.useUri = thumbFile.getAbsolutePath();
                    viewModel.getHoldingPhotoUrl(thumbFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                loadMediaRoundBitmap(binding.roundImg, photo);
            }
        }
    }

    public File getPath() {
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            filesDir = getExternalFilesDir(null);
        } else {
            // Load another directory, probably local memory
            filesDir = getFilesDir();
        }

        return filesDir;
    }
}