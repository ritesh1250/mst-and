package com.meest.social.socialViewModel.view.createTextPost;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.meest.Adapters.RvAdapter;
import com.meest.Interfaces.CreateTextInterface;
import com.meest.Interfaces.FriendSelectCallback;
import com.meest.R;
import com.meest.databinding.CreateTextPostModelBinding;
import com.meest.medley_camera2.camera2.view.activity.MeestCameraActivity;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.networkcheck.OnNoInternetRetry;
import com.meest.responses.FeedResponse;
import com.meest.social.socialViewModel.view.feelingAndActivity.FeelingAndActivity;
import com.meest.social.socialViewModel.viewModel.createTextPostViewModel.CreateTextPostViewModel;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CreateTextPost extends AppCompatActivity implements CreateTextInterface, AdapterView.OnItemClickListener, FriendSelectCallback, OnNoInternetRetry {
    public static final int PERMISSION_CODE = 123;
    public static final int CAMERA_PERMISSION_CODE = 124;
    public static final int REQUEST_LOCATION = 1;
    private static final String TAG = "CreateTextPost";
    private static final int PERMISSION_REQUEST_CODE = 200;
    public static Activity createpostActivity;
    public CreateTextPostViewModel viewModel;
    public String stringData = " ";
    private CreateTextPostModelBinding binding;
    private CustomDialogBuilder customDialogBuilder;
    private ActivityResultLauncher<String> requestPermissionLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.create_text_post_model);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new CreateTextPostViewModel(this, CreateTextPost.this, binding)).createFor()).get(CreateTextPostViewModel.class);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        viewModel.postVisibility = getString(R.string.Public);
        binding.tvCelebreting.setText("");
        clickEvents();
        createpostActivity = this;
        viewModel.init();
        viewModel.edtWriteTxtTextChangeListner();
        viewModel.getData();
        viewModel.userImageAndName();
        viewModel.onData();
        customDialogBuilder = new CustomDialogBuilder(CreateTextPost.this);
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        binding.cameraLlBtn.performClick();
                    } else {

                    }
                });
        CreateTextPostViewModel.taggedUsername.clear();
        CreateTextPostViewModel.taggedUsers.clear();

    }

    private void clickEvents() {
        binding.ivCloseOpen.setOnClickListener(v -> {
            if (binding.liText.getVisibility() == View.VISIBLE) {
                binding.ivCloseOpen.setImageResource(R.drawable.right_24);
                binding.liText.setVisibility(View.GONE);
            } else {
                binding.ivCloseOpen.setImageResource(R.drawable.left_24);
                binding.liText.setVisibility(View.VISIBLE);
            }
        });
        binding.cameraLlBtn.setOnClickListener(v -> {
            checkSelfPermission();
        });
        binding.feelingLlBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, FeelingAndActivity.class);
            startActivityForResult(intent, Constant.REQUEST_FOR_FEELING);
        });
        binding.locationLlBtn.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
                viewModel.statusCheck();
            } else {
                Toast.makeText(this, getString(R.string.Permission_Denied_Goto_settings_and_allow_permission), Toast.LENGTH_SHORT).show();
            }
        });
        binding.goLiveLlBtn.setOnClickListener(v -> Utilss.showToast(this, getString(R.string.comming_soon), R.color.grey));
        binding.tagPeopleLlBtn.setOnClickListener(v -> viewModel.showPeopleDialog());
        binding.btnPostSubmit.setOnClickListener(v -> viewModel.postSubmit());
        binding.viewLayout.setOnClickListener(v -> viewModel.changePostVisibility());
        binding.switchAllCommentPost.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) (buttonView, isChecked) -> viewModel.allowComment = isChecked);
        binding.imgBackArrowCraeteTextPost.setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.clearTheData();
    }

    public void checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{CAMERA, RECORD_AUDIO, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            checkPermission();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean audioaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternal = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean readStorage = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && audioaccepted && writeExternal && readStorage) {
                        afterPermissionGranted();
                    } else {
                        checkPermission();
                    }
                }
                break;
        }
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_permission, null), CreateTextPost.this.getResources().getString(R.string.social_to_capture_photos_and_videos),
                        CreateTextPost.this.getResources().getString(R.string.not_now), CreateTextPost.this.getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", CreateTextPost.this.getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            } else {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_permission, null), CreateTextPost.this.getResources().getString(R.string.social_to_capture_photos_and_videos),
                        CreateTextPost.this.getResources().getString(R.string.not_now), CreateTextPost.this.getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                requestPermissionLauncher.launch(
                                        Manifest.permission.CAMERA);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            }
        } else if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_permission, null), CreateTextPost.this.getResources().getString(R.string.social_to_record_audio),
                        CreateTextPost.this.getResources().getString(R.string.not_now), CreateTextPost.this.getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", CreateTextPost.this.getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            } else {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_permission, null), CreateTextPost.this.getResources().getString(R.string.social_to_record_audio),
                        CreateTextPost.this.getResources().getString(R.string.not_now), CreateTextPost.this.getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                requestPermissionLauncher.launch(
                                        Manifest.permission.CAMERA);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            }
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_storage_permission, null), CreateTextPost.this.getResources().getString(R.string.social_to_capture_storage),
                        CreateTextPost.this.getResources().getString(R.string.not_now), CreateTextPost.this.getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", CreateTextPost.this.getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            } else {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_storage_permission, null), CreateTextPost.this.getResources().getString(R.string.social_to_capture_storage),
                        CreateTextPost.this.getResources().getString(R.string.not_now), CreateTextPost.this.getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                requestPermissionLauncher.launch(
                                        Manifest.permission.READ_EXTERNAL_STORAGE);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            }
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_record_btn, null), CreateTextPost.this.getResources().getString(R.string.social_to_capture_storage),
                        CreateTextPost.this.getResources().getString(R.string.not_now), CreateTextPost.this.getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", CreateTextPost.this.getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            } else {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_record_btn, null), CreateTextPost.this.getResources().getString(R.string.social_to_capture_storage),
                        CreateTextPost.this.getResources().getString(R.string.not_now), CreateTextPost.this.getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                requestPermissionLauncher.launch(
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            }
        } else {
            afterPermissionGranted();
        }

    }

    private void afterPermissionGranted() {

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            viewModel.statusCheck();
//        }

//        Intent intent = new Intent(this, MeestMainCameraActivity.class);
        Intent intent = new Intent(this, MeestCameraActivity.class);
        intent.putExtra("call_type", viewModel.CALL_TYPE);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
    }

    @Override
    public void tagChanged() {
        if (viewModel.taggedUsername.size() > 0) {
            binding.recyclerTagsPeople.setVisibility(View.VISIBLE);
            binding.ivTagAdd.setVisibility(View.VISIBLE);
            binding.recyclerTagsPeople.setAdapter(new RvAdapter(this, (ArrayList<FeedResponse.UserTags>) viewModel.taggedUsername, (model, pos) -> {
                viewModel.taggedUsername.remove(model);

                if (viewModel.taggedUsers.size() > 0) {
                    viewModel.taggedUsers.remove(pos);
                }

            }));
        } else {
            binding.ivTagAdd.setVisibility(View.GONE);
            binding.recyclerTagsPeople.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRetry() {
        if (ConnectionUtils.isConnected(this)) {

        } else {
            ConnectionUtils.showNoConnectionDialog(this, this::onRetry);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null && data.getExtras() != null) {
                viewModel.postVisibility = data.getExtras().getString("postVisibility");
                binding.postVisibility.setText(viewModel.postVisibility);
            }
        } else if (requestCode == Constant.REQUEST_FOR_FEELING) {
            if (data != null) {

                viewModel.isFeelingType = data.getExtras().getBoolean("isFeelingType", false);
                viewModel.feelingID = data.getStringExtra("feeling_ID");
                viewModel.subFeelingID = data.getStringExtra("sub_feeling_ID");
                if (!(Constant.selectedFeelingID.equals("")) || !(Constant.selectedActivityID.equals(""))) {
                    binding.tvCelebreting.setVisibility(View.VISIBLE);
//                    tvCelebreting.setText(feelingID != null ? feelingTitle : "" + " "+ subFeelingID != null ? subFeelingTitle : "");
                    if (!(Constant.selectedFeelingID.equals("")) && Constant.selectedActivityID.equals("")) {
                        binding.tvCelebreting.setText(Constant.selectedFeelingTitle);
                    }
                    if (Constant.selectedFeelingID.equals("") && !(Constant.selectedActivityID.equals(""))) {
                        binding.tvCelebreting.setText(Constant.selectedActivityTitle);
                    }
                    if (!(Constant.selectedFeelingID.equals("")) && !(Constant.selectedActivityID.equals(""))) {
                        binding.tvCelebreting.setText(Constant.selectedFeelingTitle + " " + Constant.selectedActivityTitle);
                    }
                }
            }
        }
    }

    @Override
    public void colorsSelected(int position) {
        viewModel.selectedColorCode = viewModel.colorsResponse.getData().getRows().get(position).getColorCode();
        binding.edtWriteTxt.setTextColor(Color.parseColor(viewModel.selectedColorCode));
    }

    @Override
    public void backgroundSelected(int position) {
        viewModel.onBackgroundSelected(position);
    }

}