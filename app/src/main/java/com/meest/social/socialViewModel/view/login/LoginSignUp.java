package com.meest.social.socialViewModel.view.login;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.meest.MainActivity;
import com.meest.R;
import com.meest.databinding.LoginSignupModelBinding;
import com.meest.meestbhart.login.model.LoginResponse;
import com.meest.meestbhart.register.datePicker.date.DatePicker;
import com.meest.meestbhart.register.fragment.username.adapter.UsernameAdapter;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.social.socialViewModel.viewModel.loginViewModel.LoginViewModel;
import com.meest.videomvvmmodule.SplashScreenVideoActivity;

import com.meest.videomvvmmodule.model.videos.UploadVideoResponse;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static com.meest.Activities.EditProfileActivity.DATE_CONVERT_FORMAT;
import static com.meest.Activities.EditProfileActivity.DATE_FORMAT;
import static com.meest.social.socialViewModel.utils.ParameterConstant.SocialPrefrences.FEMALE;
import static com.meest.social.socialViewModel.utils.ParameterConstant.SocialPrefrences.MALE;
import static com.meest.social.socialViewModel.utils.ParameterConstant.SocialPrefrences.OTHER;

public class LoginSignUp extends AppCompatActivity implements UsernameAdapter.OnItemClickListener {
    private static final int PERMISSION_REQUEST_CODE = 200;
    public UsernameAdapter usernameAdapter;
    public SessionManager sessionManager = null;
    Drawable dr;
    String token;
    DatePicker datePicker;
    String selected_date, new_convert_date;
    RelativeLayout otherBack, maleBack, femaleBack;
    ImageView other, male, female;
    Button submitGender;
    String selectedGender = "";
    TextView tvMale, tvFemale, tvOther;
    Dialog userNameDialog;
    boolean isSelected = false;
    String device_name = "NA", device_model = "NA", android_version = "NA", android_id = "NA";
    private LoginSignupModelBinding binding;
    private LoginViewModel model;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.login_signup_model);
        model = new ViewModelProvider(this, new ViewModelFactory(new LoginViewModel(this, binding)).createFor()).get(LoginViewModel.class);
        inItView();
        initObserve();
        binding.setLoginModel(model);
        model.usernameAdapter = usernameAdapter;

    }

    private void initObserve() {
        CustomDialogBuilder customDialogBuilder = new CustomDialogBuilder(this);
        model.isloading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    customDialogBuilder.showLoadingDialog();
                } else {
                    customDialogBuilder.hideLoadingDialog();
                }
            }
        });

        model.isUserName.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isExist) {
                if (isExist) {
                 checkUserNameDialogue();

                }
            }
        });

        model.toast.observe(this, s -> {
            if (s != null && !s.isEmpty()) {
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            }
        });

        model.islogin.observe(this, loginResponse -> {
            if (loginResponse.getCode() == 1) {
                SocialPrefrences.setisLogin(LoginSignUp.this, true);
                SharedPrefreances.setSharedPreferenceString(LoginSignUp.this, "login", "1");
                SharedPrefreances.setSharedPreferenceString(LoginSignUp.this, SharedPrefreances.AUTH_TOKEN, loginResponse.getData().getToken());
                SharedPrefreances.setSharedPreferenceString(LoginSignUp.this, SharedPrefreances.USERNAME, loginResponse.getData().getUser().getUsername());
                SharedPrefreances.setSharedPreferenceString(LoginSignUp.this, SharedPrefreances.F_NAME, loginResponse.getData().getUser().getUsername());
                SharedPrefreances.setSharedPreferenceString(LoginSignUp.this, SharedPrefreances.L_NAME, loginResponse.getData().getUser().getUsername());
                if (loginResponse.getData().getUser().getThumbnail() != null && !loginResponse.getData().getUser().getThumbnail().equals(""))
                    SharedPrefreances.setSharedPreferenceString(LoginSignUp.this, SharedPrefreances.PROFILE_IMAGE, loginResponse.getData().getUser().getThumbnail());
                else
                    SharedPrefreances.setSharedPreferenceString(LoginSignUp.this, SharedPrefreances.PROFILE_IMAGE, loginResponse.getData().getUser().getDisplayPicture());
                SharedPrefreances.setSharedPreferenceInt(LoginSignUp.this, SharedPrefreances.NOIFICATION_COUNT, 0);

                if (!SharedPrefreances.getSharedPreferenceString(LoginSignUp.this, SharedPrefreances.GETINTENT_USER).isEmpty()) {
                    SharedPrefreances.setSharedPreferenceString(LoginSignUp.this, SharedPrefreances.WHEREFROM, "Video");
                } else if (loginResponse.getData().getUser().getDefaultApp() != null && loginResponse.getData().getUser().getDefaultApp().equals("1")) {
                    SharedPrefreances.setSharedPreferenceString(LoginSignUp.this, SharedPrefreances.WHEREFROM, "Video");
                } else {
                    SharedPrefreances.setSharedPreferenceString(LoginSignUp.this, SharedPrefreances.WHEREFROM, "Social");
                }
                if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.WHEREFROM).equalsIgnoreCase("Video")) {
                    model.isUnique();

                    model.isunique.observe(this, uploadVideoResponse -> {
                        Log.e("TAG", "onResponse: " + uploadVideoResponse.getData());
                        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
                            Global.apikey = uploadVideoResponse.getData();
                            Global.accessToken = "";
                            Global.userId = sessionManager.getUser().getData().getUserId();
                            Intent intent = new Intent(LoginSignUp.this, MainVideoActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LoginSignUp.this, SplashScreenVideoActivity.class);
                            intent.putExtra("whereFrom", "register");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        finish();

                    });
                }
                Intent intent = new Intent(LoginSignUp.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else if (loginResponse.getCode() == 0) {
                String errorMessage = loginResponse.getErrorMessage();
                if (errorMessage.equalsIgnoreCase("Please Verify Your Account")) {
                    model.signUpwithNumber();
                } else if (loginResponse.getErrorMessage().equalsIgnoreCase("Admin Suspend Your Account")) {
                    showAdminDialogue();
                } else {
                    Utilss.showToast(LoginSignUp.this, loginResponse.getErrorMessage(), R.color.social_background_blue);
                }
            }
        });

        model.isVerify.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isVerify) {
                if (isVerify) {
                    SocialPrefrences.setisVerified(LoginSignUp.this, true);
                    Intent intent = new Intent(LoginSignUp.this, VerificationCode.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void showAdminDialogue() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginSignUp.this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        final View dView = LayoutInflater.from(LoginSignUp.this).inflate(R.layout.admin_block_popup, viewGroup, false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        Button btnConfirm = dView.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
    }

    private void inItView() {
        sessionManager = new SessionManager(this);
        usernameAdapter = new UsernameAdapter(this, this);
        dr = getResources().getDrawable(R.drawable.ic_error_msg);
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
        mCheckPermission();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(), "token").equals("")) {
                            if (task.getResult() != null) {
                                token = task.getResult().getToken();
                                Global.firebaseDeviceToken = token;
                                Log.e("ravi_testing_token", token);
                            }
                            Log.e("firebase_token", "Login_0:" + token);
                            SocialPrefrences.setFireBaseToken(LoginSignUp.this, token);
                        }
                    }
                });

        binding.editLName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String fullname = binding.editFName.getText().toString().toLowerCase() + "_" + binding.editLName.getText().toString().toLowerCase();
                fullname = fullname.replaceAll(" ", "_");
                binding.editUsername.setText(fullname);
                binding.editUsername.setSelection(binding.editUsername.getText().toString().length());

            }
        });

        binding.editUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.usernameProgress.setVisibility(View.VISIBLE);
                if (s.toString().trim().length() > 4) {
                    if (!isSelected) {
                        showUserNamedialogue(s.toString(), binding.usernameProgress);

                    } else {
                        userNameDialog.dismiss();
                        binding.usernameProgress.setVisibility(View.GONE);
                    }
                } else {
                    binding.usernameProgress.setVisibility(View.GONE);
//                    Toast.makeText(LoginSignUpActivity.this, "Your Username length is less than 4", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.forgetBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginSignUp.this, ForgetPassword.class);
            startActivity(intent);
        });

        binding.skipLogin.setOnClickListener(v -> {
            if (!SharedPrefreances.getSharedPreferenceString(LoginSignUp.this, SharedPrefreances.GETINTENT_USER).isEmpty()) {
                Toast.makeText(LoginSignUp.this, "Please login to see User's Profile!!", Toast.LENGTH_SHORT).show();
            } else {
                Global.userId = "";
                Intent intent = new Intent(LoginSignUp.this, SplashScreenVideoActivity.class);
                intent.putExtra("whereFrom", "skip");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        binding.editGender.setOnClickListener(v -> {
            showGender();
        });
        binding.editDob.setOnClickListener(v -> {
            showDatePicker();
        });
        getAllBackgroundData();
    }

    private void mCheckPermission() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean phoneStateAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && phoneStateAccepted) {
                        Log.d("PERRRR", "Permission Granted, Now you can access location data and camera.");
                        SharedPrefreances.setSharedPreferenceString(this, SharedPrefreances.SHOW_PERMISSION, "false");
                    }
                }
                break;
        }
    }

    private String getCurrentDate() {
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

    //show gender
    public void showGender() {
        final Dialog genderDialog = new Dialog(LoginSignUp.this);
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
        if (selectedGender.equalsIgnoreCase(MALE)) {
            male.setImageResource(R.drawable.ic_male_color);
            female.setImageResource(R.drawable.ic_female_grey);
            other.setImageResource(R.drawable.ic_not_specify_grey);
        } else if (selectedGender.equalsIgnoreCase(FEMALE)) {
            male.setImageResource(R.drawable.ic_male_grey);
            female.setImageResource(R.drawable.ic_female_color);
            other.setImageResource(R.drawable.ic_not_specify_grey);
        } else if (selectedGender.equalsIgnoreCase(OTHER)) {
            male.setImageResource(R.drawable.ic_male_grey);
            female.setImageResource(R.drawable.ic_female_grey);
            other.setImageResource(R.drawable.ic_not_specify_color);
        }
        maleBack.setOnClickListener(v -> {
            showMale();
        });
        femaleBack.setOnClickListener(v -> {
            showFemale();
        });
        otherBack.setOnClickListener(v -> {
            showOthers();
        });
        submitGender.setOnClickListener(v -> {
            selectGender(genderDialog);
        });
    }

    public void showMale() {
        maleBack.setBackgroundResource(R.drawable.ic_selected_boundary);
        femaleBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
        otherBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
//                -------------------------------for image------------------------------------
        male.setImageResource(R.drawable.ic_male_color);
        female.setImageResource(R.drawable.ic_female_grey);
        other.setImageResource(R.drawable.ic_not_specify_grey);
//                ---------------------------------for text----------------------------------
        tvMale.setTextColor(ContextCompat.getColor(this, R.color.social_background_blue));
        tvFemale.setTextColor(ContextCompat.getColor(this, R.color.unselectedColor));
        tvOther.setTextColor(ContextCompat.getColor(this, R.color.unselectedColor));
        selectedGender = MALE;
    }

    public void showFemale() {
        maleBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
        femaleBack.setBackgroundResource(R.drawable.ic_selected_boundary);
        otherBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
//                -------------------------------for image------------------------------------
        male.setImageResource(R.drawable.ic_male_grey);
        female.setImageResource(R.drawable.ic_female_color);
        other.setImageResource(R.drawable.ic_not_specify_grey);
//                ---------------------------------for text----------------------------------
        tvMale.setTextColor(ContextCompat.getColor(this, R.color.unselectedColor));
        tvFemale.setTextColor(ContextCompat.getColor(this, R.color.social_background_blue));
        tvOther.setTextColor(ContextCompat.getColor(this, R.color.unselectedColor));
        selectedGender = FEMALE;
    }

    public void showOthers() {
        maleBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
        femaleBack.setBackgroundResource(R.drawable.ic_unselected_boundary);
        otherBack.setBackgroundResource(R.drawable.ic_selected_boundary);
//                -------------------------------for image------------------------------------
        male.setImageResource(R.drawable.ic_male_grey);
        female.setImageResource(R.drawable.ic_female_grey);
        other.setImageResource(R.drawable.ic_not_specify_color);
//                ---------------------------------for text----------------------------------
        tvMale.setTextColor(ContextCompat.getColor(this, R.color.unselectedColor));
        tvFemale.setTextColor(ContextCompat.getColor(this, R.color.unselectedColor));
        tvOther.setTextColor(ContextCompat.getColor(this, R.color.social_background_blue));
        selectedGender = OTHER;
    }

    public void selectGender(Dialog genderDialog) {
        if (selectedGender.length() > 1) {
            binding.editGender.setText(selectedGender);
            genderDialog.dismiss();
        } else {
            //     Utilss.showToast(LoginSignUp.this,getString(R.string.social_select_gender,R.color.social_background_blue));
//            binding.toast.setValue(binding.getRoot().getContext().getString(R.string.social_select_gender));
        }
    }

    //show date picker
    public void showDatePicker() {
        final Dialog dateDialog = new Dialog(LoginSignUp.this);
        dateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dateDialog.setContentView(R.layout.dialog_setdate);
        dateDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dateDialog.show();
        datePicker = dateDialog.findViewById(R.id.datePicker);
        datePicker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day) throws ParseException {
                selected_date = datePicker.getDate();
                if (checkAgeValidatin(selected_date)) {
                    Log.e("selected date", "date: " + datePicker.getDate());
                    binding.editDob.setText(getCurrentDate());
                    new_convert_date = prepareYearMonthDateFromString(getCurrentDate());

                    Log.e("after date", "date: " + selected_date);
                } else {
                    Utilss.showToastSuccess(LoginSignUp.this, "Age should be greater than 13");
                }
            }
        });
    }


    //show username dialogue
    private void showUserNamedialogue(String username, ProgressBar usernameProgress) {
        model.checkValues(username, usernameProgress);

    }

    //check userNameDialogue if exist
    private void checkUserNameDialogue() {
        userNameDialog = new Dialog(this);
        userNameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        userNameDialog.setContentView(R.layout.custom_username_dialog);
        userNameDialog.setCancelable(false);
        userNameDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        userNameDialog.show();
        isSelected = true;


        Button closeDialog = userNameDialog.findViewById(R.id.closeDialog);
        RecyclerView recyclerView_username = userNameDialog.findViewById(R.id.recyclerView_username);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        recyclerView_username.setLayoutManager(linearLayoutManager);
        recyclerView_username.setItemAnimator(new DefaultItemAnimator());
        recyclerView_username.setAdapter(usernameAdapter);
        closeDialog.setOnClickListener(v -> {
            binding.usernameProgress.setVisibility(View.GONE);
            userNameDialog.dismiss();
        });

    }


    @Override
    public void onItemClick(String name) {
        binding.editUsername.setText(name);
        binding.editUsername.setSelection(name.length());
        userNameDialog.dismiss();
    }


    public void getAllBackgroundData() {
        getLocation();
        SocialPrefrences.setIMEIID(this, getIMEIDeviceId());
        getCurrentTizeZone();
        getPhoneInfo();
        // getImeiNumber();
    }

    public void getLocation() {
        if (!canGetLocation()) {
            Toast.makeText(this, "Please Enable Location", Toast.LENGTH_SHORT).show();
        }

        displayLocationSettingsRequest(this);
        requestLocationUpdates(this);
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);
        locationRequest.setNumUpdates(5);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("TAG", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("TAG", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(LoginSignUp.this, 12);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("TAG", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("TAG", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private void requestLocationUpdates(Context context) {
//Specify how often your app should request the device’s location//
        LocationRequest request = new LocationRequest();
        request.setInterval(2000);
        request.setNumUpdates(6);

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {


            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        String lat = location.getLatitude() + "";
                        String lng = location.getLongitude() + "";

                        SocialPrefrences.setLatitude(LoginSignUp.this, lat);
                        SocialPrefrences.setLongitude(LoginSignUp.this, lng);

                        //String locationname = getAddress(location.getLatitude(), location.getLongitude());
                        Log.e("TAG_LOCATION", "==========================================" + lat + "," + lng);

                    } else {
                        Log.d("TAG", "location is not enabled");
                    }

                }
            }, null);
        } else {
            //SaveAttInDB("NA", "LOCATION PERMISSION IS NOT ENABLE FOR APPLICATION");
            Log.d("TAG", "location permission is not enabled");
            // showno();

        }
    }

    public boolean canGetLocation() {
        return isLocationEnabled(this); // application context
    }

    public boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public void getPhoneInfo() {
        device_name = Build.MANUFACTURER;
        device_model = Build.MODEL;
        android_version = Build.VERSION.RELEASE;
        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.e("data", "data: " + device_name + "::" + device_model + "::" + android_id);

        SocialPrefrences.setDeviceName(this, device_name);
        SocialPrefrences.setDeviceModel(this, device_model);
        SocialPrefrences.setAndroidVersion(this, android_version);
    }

    public void getCurrentTizeZone() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezone id :: " + tz.getID());
        System.out.println("Time   " + ts);
        SocialPrefrences.setCurrentTimeZone(this, ts);

    }


    public String getIMEIDeviceId() {
        String deviceId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deviceId = mTelephony.getImei();
                } else {
                    deviceId = mTelephony.getDeviceId();
                }
            } else {
                deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        Log.d("deviceId", deviceId);
        return deviceId;
    }
}
