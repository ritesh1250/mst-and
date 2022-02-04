package com.meest.videomvvmmodule.view.profile;

import static com.meest.videomvvmmodule.utils.BindingAdapters.loadMediaImage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.meest.R;
import com.meest.databinding.ActivityEditProfileBinding;
import com.meest.meestbhart.register.datePicker.date.DatePicker;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.social.socialViewModel.view.login.LoginSignUp;
import com.meest.videomvvmmodule.gallery.GalleryFoldersActivityMedley;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.UriUtils;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;
import com.meest.videomvvmmodule.view.media.BottomSheetImagePicker;
import com.meest.videomvvmmodule.viewmodel.EditProfileViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditProfileActivity extends BaseActivity {

    ActivityEditProfileBinding binding;
    private EditProfileViewModel viewModel;
    RelativeLayout otherBack, maleBack, femaleBack;
    TextView tvMale, tvFemale, tvOther;
    ImageView other, male, female;
    Button submitGender;
    String selectedGender = "";
    DatePicker datePicker;
    String selected_date;
    int CAPTURE_IMAGE = 100;
    int CAPTURE_IMAGE_COVER = 101;
    public static final int CAMERA_PERMISSION_CODE = 124;
    String format;
    int LAUNCH_SECOND_ACTIVITY = 114;
    String callType = "Image", uri;
    String path = "";


    private CustomDialogBuilder customDialogBuilder;

    private ActivityResultLauncher<String> requestPermissionLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        startReceiver();
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new EditProfileViewModel()).createFor()).get(EditProfileViewModel.class);
        initView();
        initObserve();
        initListener();
        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            String action = getIntent().getAction();
            String type = getIntent().getType();

            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if (type.startsWith("video/")) {
                    Uri videoUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
                    path = UriUtils.getPathFromUri(this, videoUri);
                }
            }

        } else {
            Intent intent = new Intent(EditProfileActivity.this, LoginSignUp.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        customDialogBuilder = new CustomDialogBuilder(EditProfileActivity.this);

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        //binding.tvSelect.performClick();
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });

    }


    private void initView() {
        viewModel.fetchProfileCategories();
        viewModel.user = sessionManager.getUser();
        viewModel.updateData();
        if (viewModel.user != null) {
            viewModel.cur_userName = sessionManager.getUser().getData().getUserName();
            if (viewModel.user.getData().getBio() != null && !viewModel.user.getData().getBio().isEmpty()) {
                viewModel.length.set(viewModel.user.getData().getBio().length());
            }
        }
        binding.setViewmodel(viewModel);
        viewModel.context = this;
    }

    private void initObserve() {

        CustomDialogBuilder customDialogBuilder = new CustomDialogBuilder(this);
        viewModel.isloading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    customDialogBuilder.showLoadingDialog();
                } else {
                    customDialogBuilder.hideLoadingDialog();
                }
            }
        });

        viewModel.toast.observe(this, s -> {
            if (s != null && !s.isEmpty()) {
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.updateProfile.observe(this, isUpdate -> {
            if (isUpdate != null && isUpdate) {
                Intent intent = new Intent();
                intent.putExtra("user", new Gson().toJson(viewModel.user));
                sessionManager.saveUser(viewModel.user);
                setResult(RESULT_OK, intent);
                //onBackPressed();
                finish();
            }
        });
    }

    private void initListener() {
        binding.btnProfilePhoto.setOnClickListener(view ->
                imageProfileAction()
        );
        binding.btnCoverPhoto.setOnClickListener(view ->
                imageCoverAction()
        );
        binding.imgBack.setOnClickListener(v -> onBackPressed());
        binding.etGender.setOnClickListener(v -> showGender());
        binding.etDOB.setOnClickListener(v -> {
            showDataPicker();
        });
    }

    private void showGender() {

        viewModel.setEdited();

        final Dialog genderDialog = new Dialog(EditProfileActivity.this);
        genderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        genderDialog.setContentView(R.layout.dialog_gender);
        genderDialog.setCancelable(false);
        genderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        genderDialog.show();
        otherBack = genderDialog.findViewById(R.id.otherback);
        maleBack = genderDialog.findViewById(R.id.maleBack);
        femaleBack = genderDialog.findViewById(R.id.femaleback);
        female = genderDialog.findViewById(R.id.female);
        male = genderDialog.findViewById(R.id.male);
        other = genderDialog.findViewById(R.id.other);
        tvFemale = genderDialog.findViewById(R.id.tvFemale);
        tvMale = genderDialog.findViewById(R.id.tvMale);
        tvOther = genderDialog.findViewById(R.id.tvOther);
        submitGender = genderDialog.findViewById(R.id.selectGender);

        selectedGender = viewModel.user.getData().getGender();

        switch (viewModel.user.getData().getGender()) {
            case Const.male:
                maleBack.setBackgroundResource(R.drawable.ic_selected_boundary);
                femaleBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
                otherBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
//        -------------------------------for image------------------------------------
                male.setImageResource(R.drawable.ic_male_color);
                female.setImageResource(R.drawable.ic_female_grey);
                other.setImageResource(R.drawable.ic_not_specify_grey);
//        ---------------------------------for text----------------------------------
                tvMale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.new_action_color));
                tvFemale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
                tvOther.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));

                break;
            case Const.female:
                maleBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
                femaleBack.setBackgroundResource(R.drawable.ic_selected_boundary);
                otherBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
//        -------------------------------for image------------------------------------
                male.setImageResource(R.drawable.ic_male_grey);
                female.setImageResource(R.drawable.ic_female_color);
                other.setImageResource(R.drawable.ic_not_specify_grey);
//        ---------------------------------for text----------------------------------
                tvMale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
                tvFemale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.new_action_color));
                tvOther.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));

                break;

            default:
                maleBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
                femaleBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
                otherBack.setBackgroundResource(R.drawable.ic_selected_boundary);
//        -------------------------------for image------------------------------------
                male.setImageResource(R.drawable.ic_male_grey);
                female.setImageResource(R.drawable.ic_female_grey);
                other.setImageResource(R.drawable.ic_not_specify_color);
//        ---------------------------------for text----------------------------------
                tvMale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
                tvFemale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
                tvOther.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.new_action_color));
                break;
        }
//        --------------------------male click relative layout
        maleBack.setOnClickListener(v -> {
            maleBack.setBackgroundResource(R.drawable.ic_selected_boundary);
            femaleBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
            otherBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
//        -------------------------------for image------------------------------------
            male.setImageResource(R.drawable.ic_male_color);
            female.setImageResource(R.drawable.ic_female_grey);
            other.setImageResource(R.drawable.ic_not_specify_grey);
//        ---------------------------------for text----------------------------------
            tvMale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.new_action_color));
            tvFemale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
            tvOther.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
            selectedGender = Const.male;
            viewModel.user.getData().setGender(Const.male);
        });
//        --------------------------female click relative layout
        femaleBack.setOnClickListener(v -> {
            maleBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
            femaleBack.setBackgroundResource(R.drawable.ic_selected_boundary);
            otherBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
//        -------------------------------for image------------------------------------
            male.setImageResource(R.drawable.ic_male_grey);
            female.setImageResource(R.drawable.ic_female_color);
            other.setImageResource(R.drawable.ic_not_specify_grey);
//        ---------------------------------for text----------------------------------
            tvMale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
            tvFemale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.new_action_color));
            tvOther.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
            selectedGender = Const.female;
            viewModel.user.getData().setGender(Const.female);
        });
//        --------------------------other click relative layout
        otherBack.setOnClickListener(v -> {
            maleBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
            femaleBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
            otherBack.setBackgroundResource(R.drawable.ic_selected_boundary);
//        -------------------------------for image------------------------------------
            male.setImageResource(R.drawable.ic_male_grey);
            female.setImageResource(R.drawable.ic_female_grey);
            other.setImageResource(R.drawable.ic_not_specify_color);
//        ---------------------------------for text----------------------------------
            tvMale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
            tvFemale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
            tvOther.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.new_action_color));
            selectedGender = Const.others;
            viewModel.user.getData().setGender(Const.others);
        });
        submitGender.setOnClickListener(v -> {
            binding.etGender.setText(selectedGender);
            viewModel.gender = selectedGender;
            genderDialog.dismiss();
        });
    }

    private void showDataPicker() {
        viewModel.setEdited();
        final Dialog setDate = new Dialog(EditProfileActivity.this);
        setDate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setDate.setContentView(R.layout.dialog_setdate_medley);
        setDate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setDate.show();
        datePicker = setDate.findViewById(R.id.datePicker);
        setDate.setCancelable(false);
        setDate.findViewById(R.id.save).setOnClickListener(v -> setDate.dismiss());
        datePicker.setOnDateSelectedListener((year, month, day) -> {
            selected_date = datePicker.getDate();
            if (checkAgeValidatin(selected_date)) {
                Log.e("selected date", "date: " + datePicker.getDate());
                binding.etDOB.setText(getCurrentDate());
                Log.e("after date", "date: " + selected_date);
            } else {
                Utilss.showToast(EditProfileActivity.this, getString(R.string.Age_should_be_greater_than_13), R.color.msg_fail);
            }
        });
    }

    public String getCurrentDate() {
        StringBuilder builder = new StringBuilder();
        if (datePicker.getDay() < 10) {
            builder.append("0").append(datePicker.getDay()).append("/");
        } else {
            builder.append(datePicker.getDay()).append("/");
        }
        if (datePicker.getMonth() < 10) {
            builder.append("0").append(datePicker.getMonth()).append("/");
        } else {
            builder.append(datePicker.getMonth()).append("/");
        }
        builder.append(datePicker.getYear());
        return builder.toString();
    }

    public static boolean checkAgeValidatin(String date) {
        int age = 0;
        boolean chkAge = false;
        DateFormat format = SimpleDateFormat.getDateInstance();
        try {
            Date date1 = format.parse(date);
            Calendar now = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            assert date1 != null;
            dob.setTime(date1);
            if (dob.after(now)) {
                throw new IllegalArgumentException("Can't be born in the future");
            }
            int year1 = now.get(Calendar.YEAR);
            int year2 = dob.get(Calendar.YEAR);
            age = year1 - year2;
            if (age > 13) {
                chkAge = true;
            }
            if (age == 13) {
                int month1 = now.get(Calendar.MONTH);
                int month2 = dob.get(Calendar.MONTH);
                if (month1 > month2) {
                    chkAge = true;
                } else if (month1 == month2) {
                    int day1 = now.get(Calendar.DAY_OF_MONTH);
                    int day2 = dob.get(Calendar.DAY_OF_MONTH);
                    if (day1 >= day2) {
                        chkAge = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chkAge;
    }

    private void imageProfileAction() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(EditProfileActivity.this).inflate(R.layout.gallery_video_layout, viewGroup, false);
        LinearLayout layout_1 = dialogView.findViewById(R.id.action_gallery);
        LinearLayout layout_2 = dialogView.findViewById(R.id.action_camera);
        LinearLayout layout_3 = dialogView.findViewById(R.id.action_remove);
        LinearLayout remove_view = dialogView.findViewById(R.id.remove_view);

        if (!viewModel.showRemovePhoto) {
            remove_view.setVisibility(View.GONE);
        }

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        layout_1.setOnClickListener(v -> {
            showProfilePhotoSelectedSheet();
            alertDialog.dismiss();
        });
        layout_2.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.CAMERA}, 10);
            } else {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAPTURE_IMAGE);
                alertDialog.dismiss();
            }
        });
        layout_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.isImageRemoved = true;
                ((ImageView) EditProfileActivity.this.findViewById(R.id.profile_img)).setImageResource(R.drawable.ic_edit_profile_img);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showProfilePhotoSelectedSheet() {
        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                    ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_storage_permission, null), EditProfileActivity.this.getResources().getString(R.string.to_capture_storage_denied),
                        EditProfileActivity.this.getResources().getString(R.string.not_now), EditProfileActivity.this.getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", EditProfileActivity.this.getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            } else {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_storage_permission, null), EditProfileActivity.this.getResources().getString(R.string.to_capture_storage),
                        EditProfileActivity.this.getResources().getString(R.string.not_now), EditProfileActivity.this.getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                requestPermissionLauncher.launch(
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                requestPermissionLauncher.launch(
                                        Manifest.permission.READ_EXTERNAL_STORAGE);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            }
        } else {
//            BottomSheetImagePicker bottomSheetImagePicker = new BottomSheetImagePicker();
//
//            bottomSheetImagePicker.setOnDismiss(uri -> {
//                if (!uri.isEmpty()) {
//                    loadMediaImage(binding.profileImg, uri, false);
//                    viewModel.imageProfileUri = uri;
//                    viewModel.getProfileUrl(uri);
//                }
//            });
//            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
            Intent i = new Intent(EditProfileActivity.this, GalleryFoldersActivityMedley.class);
            i.putExtra("filter", "Image");
            i.putExtra("call_type", callType);
            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
        }

    }

    private void imageCoverAction() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(EditProfileActivity.this).inflate(R.layout.gallery_video_layout, viewGroup, false);
        LinearLayout layout_1 = dialogView.findViewById(R.id.action_gallery);
        LinearLayout layout_2 = dialogView.findViewById(R.id.action_camera);
        LinearLayout layout_3 = dialogView.findViewById(R.id.action_remove);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        layout_1.setOnClickListener(v -> {
            showCoverPhotoSelectedSheet();
            alertDialog.dismiss();
        });
        layout_2.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAPTURE_IMAGE_COVER);
            alertDialog.dismiss();
        });
        layout_3.setOnClickListener(v -> {
            ((ImageView) findViewById(R.id.userProfileBanner)).setImageResource(R.drawable.ic_card_placeholder_bg);
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    private void showCoverPhotoSelectedSheet() {
        BottomSheetImagePicker bottomSheetImagePicker = new BottomSheetImagePicker();

        bottomSheetImagePicker.setOnDismiss(uri -> {
            if (!uri.isEmpty()) {
                loadMediaImage(binding.userProfileBanner, uri, false);
                viewModel.imageCoverUri = uri;
                viewModel.getCoverUrl(uri);
            }
        });
        bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
    }


    @Override
    protected void onDestroy() {
        unregisterNetworkChanges();
        super.onDestroy();
    }

    //    ==========================================================================================================================
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 101 && !path.equals("") && !data.getBooleanExtra("cancelled", false)) {
                Intent intent = new Intent(EditProfileActivity.this, MainVideoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                loadMediaImage(binding.profileImg, result, false);
                viewModel.imageProfileUri = result;
                viewModel.getProfileUrl(result);
            }
        }
        if (resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE) {
            if (data != null) {

                Uri selectedImageUri = data.getData();


                if (selectedImageUri != null) {

                    Bitmap bmp = null;
                    try {
                        bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    binding.profileImg.setImageBitmap(bmp);

                    File thumbFile = new File(getPath(), "user_profile_" + sessionManager.getUser().getData().getUserName() + format + ".jpg");
                    if (thumbFile.exists()) {
                        thumbFile.delete();
                    }
                    FileOutputStream stream = null;
                    try {
                        stream = new FileOutputStream(thumbFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (bmp != null) {
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    }
                    try {
                        stream.flush();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    viewModel.imageProfileUri = thumbFile.getAbsolutePath();
                    viewModel.getProfileUrl(thumbFile.getAbsolutePath());

                } else {
                    // If selectedImageUri is null check extras for bitmap
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    binding.profileImg.setImageBitmap(bmp);

                    File thumbFile = new File(getPath(), "user_profile_" + sessionManager.getUser().getData().getUserName() + format + ".jpg");
                    if (thumbFile.exists()) {
                        thumbFile.delete();
                    }
                    FileOutputStream stream = null;
                    try {
                        stream = new FileOutputStream(thumbFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (bmp != null) {
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    }
                    try {
                        stream.flush();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    viewModel.imageProfileUri = thumbFile.getAbsolutePath();
                    viewModel.getProfileUrl(thumbFile.getAbsolutePath());
                }
            }
        }
//        if (resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE_COVER) {
//            if (data != null) {
//                Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
//                File thumbFile = new File(getPath(), "cover_profile_" + sessionManager.getUser().getData().getUserName() + format + ".jpg");
//                if (thumbFile.exists()) {
//                    thumbFile.delete();
//                }
//                try {
//                    FileOutputStream stream = new FileOutputStream(thumbFile);
//                    if (photo != null) {
//                        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    }
//                    stream.flush();
//                    stream.close();
//                    viewModel.imageCoverUri = thumbFile.getAbsolutePath();
//                    viewModel.getCoverUrl(thumbFile.getAbsolutePath());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                loadMediaRoundBitmap(binding.userProfileBanner, photo);
//            }
//        }
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

    @Override
    public void onBackPressed() {
        if (viewModel.isEdited) {
            final Dialog dialog = new Dialog(EditProfileActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.discard_edit_video);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView tvYes = dialog.findViewById(R.id.tvYes);
            TextView tvNo = dialog.findViewById(R.id.tvNo);
            dialog.show();
            tvYes.setOnClickListener(v -> finish());
            tvNo.setOnClickListener(v -> dialog.dismiss());
        } else {
            finish();
        }
    }

    public void backClick(View view) {
        onBackPressed();
    }


}