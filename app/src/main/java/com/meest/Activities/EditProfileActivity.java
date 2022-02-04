package com.meest.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Interfaces.CompressMedia;
import com.meest.Paramaters.UserStoryParameter;
import com.meest.R;
import com.meest.meestbhart.register.datePicker.date.DatePicker;
import com.meest.social.socialViewModel.model.UserProfileRespone1;
import com.meest.utils.goLiveUtils.CommonUtils;
import com.meest.meestbhart.register.fragment.profile.model.UploadimageResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.meestbhart.login.model.ApiResponse;
import com.bumptech.glide.Glide;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.meest.meestbhart.utilss.SharedPrefreances.PROFILE_IMAGE;


public class EditProfileActivity extends AppCompatActivity implements CompressMedia {


    private int PROFILE_GALLERY = 1, PROFILE_CAMERA = 2;
    private int COVER_GALLERY = 11, COVER_CAMERA = 22;
    String uploadedImage, uploadedCover, uploadedFront, uploadedBack, output, uploadedthumb;
    ImageView inmg_back;
    private EditText edit_name, edit_username, edit_Email, edit_LastName;
    EmojiconEditText edit_bio;
    TextView edit_mobile, txt_country_code, edit_gender, edit_birthday;
    private Button img_done;
    String selectedImage, thumbselectedImage, selectedCoverImage, selectedNumber;
    private ImageView img_profile_pic, ivCoverPic;
    private String image_type = "";
    boolean isImageChanged = false;
    boolean isCoverPic = false;
    boolean isImageRemoved = false;
    boolean isCoverPicRemoved = false;
    private boolean isCoverPicChangeCall = false;
    private boolean isProfileChangeCall = false;
    int selected = 0;
    private boolean isCroppingStated = false;

    private LottieAnimationView image;
    Uri imageUri;
    String aadharNumber;
    public static final int CAMERA_PERMISSION_CODE = 124;
    LinearLayout btnEditProfilePic;
    ProgressBar profileImageProgress;
    private boolean showRemovePhoto = true;
    private boolean showRemoveCover = true;
    // restrict edit text character
// show gender

    RelativeLayout otherback, maleback, femaleback;
    TextView tvMale, tvFemale, tvOther;
    ImageView other, male, female;
    Button submitGender;
    String selectedGender = "";

    /*date picker*/
    DatePicker datePicker;
    String selected_date, new_convert_date;
    private String blockCharacterSet = "~#^|$%&*!@";

    //convert
    public static final String DATE_CONVERT_FORMAT = "yyyy/mm/dd";
    public static final String DATE_FORMAT = "dd/mm/yyyy";
    public static final String GET_DATE_FORMATE = "yyyy-mm-dd";

    //permission result
    private CustomDialogBuilder customDialogBuilder;
    private ActivityResultLauncher<String> requestPermissionLauncher;


    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains("" + source)) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);
        init();

        btnEditProfilePic = findViewById(R.id.btnEditProfilePic);
        profileImageProgress = findViewById(R.id.profile_image_progress);
        txt_country_code = findViewById(R.id.txt_country_code);
        ivCoverPic = findViewById(R.id.ivCoverPic);
        image = findViewById(R.id.loading);
        image.setAnimation(getString(R.string.loading_json));
        image.playAnimation();
        image.loop(true);


        inmg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        img_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                output = Constant.createOutputPath(EditProfileActivity.this, ".jpg");
                if (edit_name.getText().toString().trim().length() < 3) {
                    edit_name.setError(getString(R.string.please_enter_minimum_3_character));
                    return;
                }
                if (edit_LastName.getText().toString().trim().length() < 3) {
                    edit_LastName.setError(getString(R.string.please_enter_minimum_3_character));
                    return;
                }
                if (edit_bio.getText().toString().trim().length() > 50) {
                    edit_bio.setError(getString(R.string.Bio_cant_be_greater_than_50_characters));
                    return;
                }
                if (!edit_Email.getText().toString().trim().isEmpty()) {
                    if (!CommonUtils.validateEmail(edit_Email.getText().toString().trim())) {
                        CommonUtils.showToast(EditProfileActivity.this, getResources().getString(R.string.Enter_Valid_Email_Address));
                        return;
                    }
                }
                if (edit_gender.getText().toString().trim().length() < 4) {
                    edit_gender.setError(getString(R.string.enter_gendr));
                    return;
                }
                if (edit_birthday.getText().toString().trim().length() < 10) {
                    edit_birthday.setError(getString(R.string.enter_dob));
                    return;
                }

                if (isImageChanged && isCoverPic) {
                    uploadImage(thumbselectedImage, "thumb");
                } else if (isImageChanged) {
                    uploadImage(thumbselectedImage, "thumb");
                } else if (isCoverPic) {
                    uploadImage(selectedCoverImage, "cover_pic");
                } else {
                    updateProfile(false);
                }

            }
        });

        edit_username.setFocusable(false);
        edit_username.setEnabled(false);
        edit_mobile.setFocusable(false);
        edit_mobile.setEnabled(false);


        edit_gender.setOnClickListener(v -> {
            showGender();
        });

        edit_birthday.setOnClickListener(v -> {
            showDataPicker();
        });


        btnEditProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCoverPicChangeCall = false;
                isProfileChangeCall = true;
                checkpermission();

            }
        });


        image.setAnimation(getString(R.string.loading_json));
        image.playAnimation();
        image.loop(true);
        image.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        UserStoryParameter paramter = new UserStoryParameter(SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.ID));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<UserProfileRespone1> call = webApi.getUserProfileData(map);
        call.enqueue(new Callback<UserProfileRespone1>() {
            @Override
            public void onResponse(Call<UserProfileRespone1> call, Response<UserProfileRespone1> response) {
                image.setVisibility(View.GONE);
                try {
                    if (response.code() == 200) {

                        if (response.body().getCode() == 1) {
                            image.setVisibility(View.GONE);
                            edit_name.setText(response.body().getDataUser().getFirstName());
                            edit_LastName.setText(response.body().getDataUser().getLastName());
                            edit_username.setText(response.body().getDataUser().getUsername());
                            edit_Email.setText(response.body().getDataUser().getEmail());
                            if (response.body().getDataUser().getMobile() != null) {
                                selectedNumber = String.valueOf(response.body().getDataUser().getMobile());
                                String conutry = selectedNumber.substring(0, 2);
                                String number = selectedNumber.substring(2, selectedNumber.length());
                                txt_country_code.setText("+" + conutry);
                                edit_mobile.setText(number);
                            }


                            if (response.body().getDataUser().getDisplayPicture() == null || response.body().getDataUser().getDisplayPicture().equals("") || response.body().getDataUser().getDisplayPicture().equals("null")) {
                                showRemovePhoto = false;
                            }

                            if (response.body().getDataUser().getCoverPicture() == null || response.body().getDataUser().getCoverPicture().equals("") || response.body().getDataUser().getCoverPicture().equals("null")) {
                                showRemoveCover = false;
                            }
                            if (response.body().getDataUser().getThumbnail().equalsIgnoreCase("")) {
                                isImageRemoved = true;
                            }
                            //  edit_mobile.setText(String.valueOf(response.body().getDataUser().getMobile()));


                            if (response.body().getDataUser().getAbout() != null && !response.body().getDataUser().getAbout().isEmpty()) {
                                edit_bio.setText(CommonUtils.decodeEmoji(response.body().getDataUser().getAbout()));
                            } else {
                                edit_bio.setText(getString(R.string.na));
                            }
                            if (response.body().getDataUser().getGender() != null) {
                                edit_gender.setText(response.body().getDataUser().getGender());
                            }
                            if (response.body().getDataUser().getDob() != null) {

                                edit_birthday.setText(preparedaymonthyearFromString(response.body().getDataUser().getDob()));
                            }

                            if (response.body().getDataUser().getThumbnail() != null) {
                                Glide.with(EditProfileActivity.this).
                                        load((response.body().getDataUser().getThumbnail()))
                                        .into(img_profile_pic);
                                SharedPrefreances.setSharedPreferenceString(EditProfileActivity.this, PROFILE_IMAGE, response.body().getDataUser().getThumbnail());

                            }else {
                                if (response.body().getDataUser().getDisplayPicture()!=null && !response.body().getDataUser().getThumbnail().equals("")){
                                    Glide.with(EditProfileActivity.this).
                                            load((response.body().getDataUser().getDisplayPicture()))
                                            .into(img_profile_pic);
                                    SharedPrefreances.setSharedPreferenceString(EditProfileActivity.this, PROFILE_IMAGE, response.body().getDataUser().getDisplayPicture());
                                }
                            }

                            if (response.body().getDataUser().getGender().equalsIgnoreCase(getString(R.string.male))) {
                                Glide.with(EditProfileActivity.this).
                                        load((response.body().getDataUser().getDisplayPicture()))
                                        .placeholder(R.drawable.male_place_holder)
                                        .into(img_profile_pic);

                            } else if (response.body().getDataUser().getGender().equalsIgnoreCase(getString(R.string.female))) {
                                Glide.with(EditProfileActivity.this).
                                        load((response.body().getDataUser().getDisplayPicture()))
                                        .placeholder(R.drawable.female_cardplaceholder)
                                        .into(img_profile_pic);

                            }
                            if (response.body().getDataUser().getCoverPicture() != null && !response.body().getDataUser().getCoverPicture().isEmpty()) {
                                CommonUtils.loadImage(ivCoverPic, response.body().getDataUser().getCoverPicture(), EditProfileActivity.this);
                            }

//                            if (!response.body().getDataUser().getEmail().equals("") && response.body().getDataUser().getEmail() != null) {
//                                edit_Email.setText(response.body().getDataUser().getEmail());
//                                edit_Email.setFocusable(false);
//                                edit_Email.setEnabled(false);
//                            }
//                            else
//                            {
//                                edit_Email.setFocusable(true);
//                                edit_Email.setEnabled(true);
//                            }

                            if (String.valueOf(response.body().getDataUser().getMobile()).length() > 10) {
                                String mobiles = String.valueOf(response.body().getDataUser().getMobile()).substring(String.valueOf(response.body().getDataUser().getMobile()).length() - 10);
                                edit_mobile.setText(mobiles);
                                Log.e("Tag", "checkimggender=" + response.body().getDataUser().getGender());
                            }

                            //  edit_gender_spinner.setSelection(GenderList.get(1).);
                        }
                    } else if (response.code() == 500) {
                        image.setVisibility(View.GONE);
                        Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    } else {
                        image.setVisibility(View.GONE);
                        Utilss.showToast(getApplicationContext(), response.body().getSuccess().toString(), R.color.msg_fail);
                    }

                } catch (Exception e) {
                    image.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserProfileRespone1> call, Throwable t) {
                image.setVisibility(View.GONE);
                Log.e("Atg", "test =" + t.getMessage());
            }
        });
    }

    private void checkpermission() {
        if (ContextCompat.checkSelfPermission(EditProfileActivity.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(EditProfileActivity.this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_DENIED ||
                    ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
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
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
                                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
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


    private void showGender() {
        final Dialog genderDialog = new Dialog(EditProfileActivity.this);
        genderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        genderDialog.setContentView(R.layout.dialog_gender);
        genderDialog.setCancelable(false);
        genderDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        genderDialog.show();
        otherback = genderDialog.findViewById(R.id.otherback);
        maleback = genderDialog.findViewById(R.id.maleBack);
        femaleback = genderDialog.findViewById(R.id.femaleback);
        female = genderDialog.findViewById(R.id.female);
        male = genderDialog.findViewById(R.id.male);
        other = genderDialog.findViewById(R.id.other);
        tvFemale = genderDialog.findViewById(R.id.tvFemale);
        tvMale = genderDialog.findViewById(R.id.tvMale);
        tvOther = genderDialog.findViewById(R.id.tvOther);
        submitGender = genderDialog.findViewById(R.id.selectGender);
        selectedGender = edit_gender.getText().toString();
        if (edit_gender.getText().toString().trim().equalsIgnoreCase("male")) {
            male.setImageResource(R.drawable.ic_male_color);
            female.setImageResource(R.drawable.ic_female_grey);
            other.setImageResource(R.drawable.ic_not_specify_grey);
        } else if (edit_gender.getText().toString().trim().equalsIgnoreCase("Female")) {
            male.setImageResource(R.drawable.ic_male_grey);
            female.setImageResource(R.drawable.ic_female_color);
            other.setImageResource(R.drawable.ic_not_specify_grey);
        } else if (edit_gender.getText().toString().trim().equalsIgnoreCase("Prefer not to disclose")) {
            male.setImageResource(R.drawable.ic_male_grey);
            female.setImageResource(R.drawable.ic_female_grey);
            other.setImageResource(R.drawable.ic_not_specify_color);
        }
//        --------------------------male click relative layout
        maleback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleback.setBackgroundResource(R.drawable.ic_selected_boundary);
                femaleback.setBackgroundResource(R.drawable.ic_unselected_boundary);
                otherback.setBackgroundResource(R.drawable.ic_unselected_boundary);
//                -------------------------------for image------------------------------------
                male.setImageResource(R.drawable.ic_male_color);
                female.setImageResource(R.drawable.ic_female_grey);
                other.setImageResource(R.drawable.ic_not_specify_grey);
//                ---------------------------------for text----------------------------------
                tvMale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.social_background_blue));
                tvFemale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
                tvOther.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
                selectedGender = "male";
            }
        });
        //        --------------------------female click relative layout
        femaleback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleback.setBackgroundResource(R.drawable.ic_unselected_boundary);
                femaleback.setBackgroundResource(R.drawable.ic_selected_boundary);
                otherback.setBackgroundResource(R.drawable.ic_unselected_boundary);
//                -------------------------------for image------------------------------------
                male.setImageResource(R.drawable.ic_male_grey);
                female.setImageResource(R.drawable.ic_female_color);
                other.setImageResource(R.drawable.ic_not_specify_grey);
//                ---------------------------------for text----------------------------------
                tvMale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
                tvFemale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.social_background_blue));
                tvOther.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
                selectedGender = "Female";
            }
        });
        //        --------------------------other click relative layout
        otherback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleback.setBackgroundResource(R.drawable.ic_unselected_boundary);
                femaleback.setBackgroundResource(R.drawable.ic_unselected_boundary);
                otherback.setBackgroundResource(R.drawable.ic_selected_boundary);
//                -------------------------------for image------------------------------------
                male.setImageResource(R.drawable.ic_male_grey);
                female.setImageResource(R.drawable.ic_female_grey);
                other.setImageResource(R.drawable.ic_not_specify_color);
//                ---------------------------------for text----------------------------------
                tvMale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
                tvFemale.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.unselectedColor));
                tvOther.setTextColor(ContextCompat.getColor(EditProfileActivity.this, R.color.social_background_blue));
                selectedGender = "Prefer not to disclose";
            }
        });
        submitGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedGender.length() > 1) {
                    edit_gender.setText(selectedGender);
                    genderDialog.dismiss();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //    ============================date picker+++++++++++++++++++++++++++++
    private void showDataPicker() {
        final Dialog setDate = new Dialog(EditProfileActivity.this);
        setDate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setDate.setContentView(R.layout.dialog_setdate);
        setDate.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setDate.show();
        datePicker = setDate.findViewById(R.id.datePicker);
        datePicker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day) {
                selected_date = datePicker.getDate();
                if (checkAgeValidatin(selected_date)) {
                    Log.e("selected date", "date: " + datePicker.getDate());
                    edit_birthday.setText(getCurrentDate());
                    new_convert_date = prepareYearMonthDateFromString(getCurrentDate());
                } else {
                    Utilss.showToast(EditProfileActivity.this, "Age should be greater than 13 ", R.color.social_background_blue);
                }
            }
        });
    }


    public static String prepareYearMonthDateFromString(String dateStr) {
        try {
            Date date = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).parse(dateStr);
            DateFormat formatter = new SimpleDateFormat(DATE_CONVERT_FORMAT, Locale.getDefault());
            dateStr = formatter.format(date.getTime());
            Log.e("after select", dateStr);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateStr;
    } public static String preparedaymonthyearFromString(String dateStr) {
        try {
            Date date = new SimpleDateFormat(GET_DATE_FORMATE, Locale.ENGLISH).parse(dateStr);
            DateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            dateStr = formatter.format(date.getTime());
            Log.e("after select", dateStr);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateStr;
    }

    public String getCurrentDate() {
        StringBuilder builder = new StringBuilder();
        if (datePicker.getDay() < 10) {
            builder.append("0" + datePicker.getDay() + "/");
        } else {
            builder.append(datePicker.getDay() + "/");
        }
        if (datePicker.getMonth() < 10) {
            builder.append("0" + datePicker.getMonth() + "/");
        } else {
            builder.append(datePicker.getMonth() + "/");
        }
        if (datePicker.getYear() < 10) {
            builder.append("0" + datePicker.getYear());
        } else {
            builder.append(datePicker.getYear());
        }
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
                } else if (month1 == month2 && age == 13) {
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

    private void updateProfile(boolean isAadhar) {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);

        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json");
        map.put("Content-Type", "application/json");
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));

        HashMap<String, String> body = new HashMap<>();
        if (isAadhar) {
            body.put("adharFront", uploadedFront);
            body.put("adharBack", uploadedBack);
            body.put("adharNumber", aadharNumber);
        } else {


            if (isImageChanged) {
                body.put("displayPicture", uploadedImage);
                body.put("thumbnail", uploadedthumb);
            }

            if (isCoverPic) {
                body.put("coverPicture", uploadedCover);
            }

            if (isImageRemoved) {
                body.put("displayPicture", "null");
                body.put("thumbnail", "null");
            }

            if (isCoverPicRemoved)
                body.put("coverPicture", "null");

            try {
                String emoji = URLEncoder.encode(edit_bio.getText().toString(), "UTF-8");
                body.put("about", emoji);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            body.put("firstName", edit_name.getText().toString().trim());
            body.put("lastName", edit_LastName.getText().toString().trim());
            body.put("email", edit_Email.getText().toString().trim());
            body.put("gender", edit_gender.getText().toString().trim());
            body.put("dob", new_convert_date);
        }

        image.setVisibility(View.VISIBLE);

        Call<ApiResponse> call = webApi.updateUserProfile(map, body);
        Log.d("TAG", "updateProfile: " + body.toString());
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                image.setVisibility(View.GONE);
                try {
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        if (isAadhar) {
                            Toast.makeText(EditProfileActivity.this, getString(R.string.Account_Verification_request_sent_successfully), Toast.LENGTH_LONG).show();
                        } else {
                            Utilss.showToast(getApplicationContext(), getString(R.string.Updated_Successfully), R.color.green);
                            if (isImageChanged) {
                                SharedPrefreances.setSharedPreferenceString(EditProfileActivity.this, SharedPrefreances.PROFILE, uploadedImage);
                            }
                            SharedPrefreances.setSharedPreferenceString(EditProfileActivity.this, SharedPrefreances.EMAil, edit_Email.getText().toString());
                            String emoji = URLEncoder.encode(edit_bio.getText().toString(), "UTF-8");
                            SharedPrefreances.setSharedPreferenceString(EditProfileActivity.this, SharedPrefreances.BIO, emoji);
                            SharedPrefreances.setSharedPreferenceString(EditProfileActivity.this, SharedPrefreances.USERNAME, edit_username.getText().toString());

                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    } else {
                        Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    }

                } catch (Exception e) {
                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                image.setVisibility(View.GONE);
                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void uploadImage(String imagePath, String uploadType) {
        image.setVisibility(View.VISIBLE);

        File file = new File(imagePath);
        RequestBody requestFile;
        MultipartBody.Part body;
        requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<UploadimageResponse> call = webApi.uploadImageRequest(map, body);

        call.enqueue(new Callback<UploadimageResponse>() {
            @Override
            public void onResponse(Call<UploadimageResponse> call, Response<UploadimageResponse> response) {
                try {
                    if (response.code() == 200 && response.body().getSuccess()) {

                        if (isImageChanged && isCoverPic) {
                            if (uploadType.equalsIgnoreCase("thumb")) {
                                uploadedthumb = response.body().getData().getUrl();
                                uploadImage(selectedImage, "update");
                            } else if (uploadType.equalsIgnoreCase("update")) {
                                uploadedImage = response.body().getData().getUrl();
                                uploadImage(selectedCoverImage, "cover_pic");
                            } else if (uploadType.equalsIgnoreCase("cover_pic")) {
                                uploadedCover = response.body().getData().getUrl();
                                updateProfile(false);
                            }
                        } else if (isImageChanged) {
                            if (uploadType.equalsIgnoreCase("thumb")) {
                                uploadedthumb = response.body().getData().getUrl();
                                uploadImage(selectedImage, "update");
                            } else if (uploadType.equalsIgnoreCase("update")) {
                                uploadedImage = response.body().getData().getUrl();
                                updateProfile(false);
                            }
                        } else if (isCoverPic) {
                            uploadedCover = response.body().getData().getUrl();
                            updateProfile(false);
                        }

                    } else {
                        image.setVisibility(View.VISIBLE);
                        Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    image.setVisibility(View.VISIBLE);
                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);

                }
            }

            @Override
            public void onFailure(Call<UploadimageResponse> call, Throwable t) {
                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void imageAction(int ACTION_GALLERY_NUM, int ACTION_CAMERA_NUM, Uri pickUri) {
        int selectedImage;
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(EditProfileActivity.this).inflate(R.layout.gallery_layout, viewGroup, false);
        LinearLayout actionGallery = dialogView.findViewById(R.id.action_gallery);
        LinearLayout actionCamera = dialogView.findViewById(R.id.action_camera);
        LinearLayout actionRemove = dialogView.findViewById(R.id.action_remove);
        if (isImageRemoved) {
            actionRemove.setVisibility(View.GONE);
        } else {
            actionRemove.setVisibility(View.VISIBLE);
        }
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        actionGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallery(ACTION_GALLERY_NUM);
                alertDialog.dismiss();
            }
        });
        actionCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoFromCamera(ACTION_CAMERA_NUM, pickUri);
                alertDialog.dismiss();
            }
        });
        actionRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImage(ACTION_CAMERA_NUM, actionRemove);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void imageActionWithoutRemove(int ACTION_GALLERY_NUM, int ACTION_CAMERA_NUM, Uri
            pickUri) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(EditProfileActivity.this).inflate(R.layout.gallery_layout, viewGroup, false);
        LinearLayout actionGallery = dialogView.findViewById(R.id.action_gallery);
        LinearLayout actionCamera = dialogView.findViewById(R.id.action_camera);
        LinearLayout actionRemove = dialogView.findViewById(R.id.action_remove);

        dialogView.findViewById(R.id.remove_view).setVisibility(View.GONE);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        actionGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallery(ACTION_GALLERY_NUM);
                alertDialog.dismiss();
            }
        });
        actionCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoFromCamera(ACTION_CAMERA_NUM, pickUri);
                alertDialog.dismiss();
            }
        });
        actionRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImage(ACTION_CAMERA_NUM, actionRemove);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void removeImage(int ACTION_CAMERA_NUM, LinearLayout actionRemove) {
        if (ACTION_CAMERA_NUM == PROFILE_GALLERY || ACTION_CAMERA_NUM == PROFILE_CAMERA) {
            isImageRemoved = true;
            if (image_type.equalsIgnoreCase("Male")) {
                img_profile_pic.setImageResource(R.drawable.male_place_holder);
            } else if (image_type.equalsIgnoreCase("Female")) {
                img_profile_pic.setImageResource(R.drawable.female_cardplaceholder);
            } else {
                img_profile_pic.setImageResource(R.drawable.placeholder);
            }

        } else {
            isCoverPicRemoved = true;
            ivCoverPic.setImageResource(R.drawable.placeholder);
        }
    }

    public void choosePhotoFromGallery(int ACTION_NUM) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, ACTION_NUM);
    }

    private void takePhotoFromCamera(int ACTION_NUM, Uri pickUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
        intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
        intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pickUri);
        startActivityForResult(intent, ACTION_NUM);
    }

    String currentPhotoPath = "";
    String currentPhotoPathCover = "";

    private File getImageFile(String picType) {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File file = null;
        try {
            file = File.createTempFile(
                    imageFileName, ".jpg", storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (picType.equals("cover"))
            currentPhotoPath = "file:" + file.getAbsolutePath();
        else
            currentPhotoPathCover = "file:" + file.getAbsolutePath();
        return file;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            if (isCroppingStated) {
                if (isCoverPicChangeCall) {
                    isCoverPicChangeCall = false;
                    isCoverPic = false;
                    selectedCoverImage = "";
                    Log.d("URI__DATA", isCoverPic + "  " + isCoverPicChangeCall);
                } else {
                    isImageChanged = false;
                    thumbselectedImage = "";
                    Log.d("URI__DATA", isImageChanged + "  " + isCoverPicChangeCall);
                }
            }
            return;
        }
        if (requestCode == PROFILE_GALLERY) {
            Uri pickUri = data.getData();
            if (pickUri != null) {
                try {
                    //  Glide.with(EditProfileActivity.this).load(pickUri).into(img_profile_pic);
                    selectedImage = Constant.getPath(EditProfileActivity.this, pickUri);
                    selectedImage = Constant.compressImage(EditProfileActivity.this, selectedImage);
                    isImageChanged = true;
                    isImageRemoved = false;
                    thumbselectedImage = Constant.createOutputPath(EditProfileActivity.this, ".JPEG");
                    thumbselectedImage = Constant.compressImageThumb(EditProfileActivity.this, selectedImage);

                    startCropping(pickUri, false);
                /*    File thumbFile = new File(thumbselectedImage);
                    try {
                        FileOutputStream stream = new FileOutputStream(thumbFile);
                        Bitmap bmThumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(selectedImage), THUMBSIZE_W, THUMBSIZE_H);
                        if (bmThumbnail != null) {
                            bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        }
                        stream.flush();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EditProfileActivity.this, getString(R.string.Failed), Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == PROFILE_CAMERA) {
            try {
                // Glide.with(EditProfileActivity.this).load(imageUri).into(img_profile_pic);
                selectedImage = getRealPathFromURI(imageUri);
                selectedImage = Constant.compressImage(EditProfileActivity.this, selectedImage);
//                Uri uri = Uri.parse(currentPhotoPath);
//                openCropActivity(uri, PROFILE_CAMERA);

                isImageChanged = true;
                isImageRemoved = false;
                thumbselectedImage = Constant.createOutputPath(EditProfileActivity.this, ".JPEG");
                thumbselectedImage = Constant.compressImageThumb(EditProfileActivity.this, selectedImage);

                startCropping(imageUri, false);

              /*  File thumbFile = new File(thumbselectedImage);
                try {

                    FileOutputStream stream = new FileOutputStream(thumbFile);
                    Bitmap bmThumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(selectedImage), THUMBSIZE_W, THUMBSIZE_H);
                    if (bmThumbnail != null) {
                        bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    }
                    stream.flush();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == COVER_GALLERY) {
            Uri pickUri = data.getData();
            if (pickUri != null) {
                try {
                    //  Glide.with(EditProfileActivity.this).load(pickUri).into(ivCoverPic);
                    selectedCoverImage = Constant.getPath(EditProfileActivity.this, pickUri);
                    selectedCoverImage = Constant.compressImage(EditProfileActivity.this, selectedCoverImage);
                    isCoverPic = true;
                    isCoverPicRemoved = false;
                    startCropping(pickUri, isCoverPic);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EditProfileActivity.this, getString(R.string.Failed), Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == COVER_CAMERA) {
            try {
                // Glide.with(EditProfileActivity.this).load(imageUri).into(ivCoverPic);
                selectedCoverImage = getRealPathFromURI(imageUri);
                selectedCoverImage = Constant.compressImage(EditProfileActivity.this, selectedCoverImage);
                isCoverPic = true;
                isCoverPicRemoved = false;
                startCropping(imageUri, isCoverPic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (isCoverPicChangeCall) {
                    Uri resultUri = result.getUri();
                    String realPAth = Constant.getPathFromUriAllVersion(this, resultUri);
                    Glide.with(EditProfileActivity.this).load(resultUri).into(ivCoverPic);
                    selectedCoverImage = realPAth;
                    Log.d("URI__DATA", resultUri.toString() + "  " + realPAth + " " + isCoverPicChangeCall);
                } else {
                    Uri resultUri = result.getUri();
                    String realPAth = Constant.getPathFromUriAllVersion(this, resultUri);
                    Glide.with(EditProfileActivity.this).load(resultUri).into(img_profile_pic);
                    selectedImage = realPAth;
                    thumbselectedImage = Constant.createOutputPath(EditProfileActivity.this, ".JPEG");
                    thumbselectedImage = Constant.compressImageThumb(EditProfileActivity.this, realPAth);

                    Log.d("URI__DATA", resultUri.toString() + "  " + realPAth + " " + isCoverPicChangeCall);
                }

            }
        }



    }


    public void startCropping(Uri realPath, boolean isCoverPic) {
        isCroppingStated = true;
        if (isCoverPic) {
            CropImage.activity(realPath)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(16, 12) //You can skip this for free form aspect ratio)
                    .start(EditProfileActivity.this);
        } else {
            CropImage.activity(realPath)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1) //You can skip this for free form aspect ratio)
                    .start(EditProfileActivity.this);
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void init() {
        getIntent().getExtras();
        edit_name = findViewById(R.id.edit_name);
        edit_name.setFilters(new InputFilter[]{filter});
        edit_username = findViewById(R.id.edit_username);
        edit_bio = findViewById(R.id.edit_bio);
        edit_Email = findViewById(R.id.edit_Email);
        edit_mobile = findViewById(R.id.edit_mobile);
        edit_birthday = findViewById(R.id.edit_birthday);
        edit_gender = findViewById(R.id.edit_gender);
        img_done = findViewById(R.id.img_done);
        inmg_back = findViewById(R.id.inmg_back);
        edit_LastName = findViewById(R.id.edit_LastName);
        edit_LastName.setFilters(new InputFilter[]{filter});
        img_profile_pic = findViewById(R.id.img_profile_pic);
        image_type = getIntent().getExtras().getString("image_type");
        customDialogBuilder = new CustomDialogBuilder(EditProfileActivity.this);
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        btnEditProfilePic.performClick();
                    } else {
                     
                    }
                });

    }

    public void dialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(EditProfileActivity.this).inflate(R.layout.aadhar_verified_dialog, viewGroup, false);

        TextView txt_login = dialogView.findViewById(R.id.txt_login);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        txt_login.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    @Override
    public void compressDone(boolean isDone, int requestCode) {
        if (isDone && requestCode == 111) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    uploadImage(output, "update");
                }
            });
        }
    }


    public void afterPermissionGranted(){
       if (isCoverPicChangeCall) {
           ContentValues values = new ContentValues();
           values.put(MediaStore.Images.Media.TITLE, "New Picture");
           values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
           imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

           if (showRemoveCover) {
               imageAction(COVER_GALLERY, COVER_CAMERA, imageUri);
           } else {
               imageActionWithoutRemove(COVER_GALLERY, COVER_CAMERA, imageUri);
           }

       } else if (isProfileChangeCall) {
           ContentValues values = new ContentValues();
           values.put(MediaStore.Images.Media.TITLE, "New Picture");
           values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
           imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

           if (showRemovePhoto) {
               imageAction(PROFILE_GALLERY, PROFILE_CAMERA, imageUri);
           } else {
               imageActionWithoutRemove(PROFILE_GALLERY, PROFILE_CAMERA, imageUri);
           }
       }
    }
}
