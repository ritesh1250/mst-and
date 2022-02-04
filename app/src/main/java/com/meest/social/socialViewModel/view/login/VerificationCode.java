package com.meest.social.socialViewModel.view.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.meest.R;
import com.meest.databinding.ActivityVerficationModelBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.SmsBroadcastReceiver;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.social.socialViewModel.viewModel.loginViewModel.VerificationCodeViewModel;
import com.meest.videoEditing.videocollage.BitmapUtils.Utils;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerificationCode extends AppCompatActivity {
    ActivityVerficationModelBinding binding;
    VerificationCodeViewModel model;
    SmsBroadcastReceiver smsBroadcastReceiver;
    private static final int REQ_USER_CONSENT = 200;
    String device_name = "NA", device_model = "NA", android_version = "NA", android_id = "NA";
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(VerificationCode.this, R.layout.activity_verfication_model);
        model = new ViewModelProvider(this, new ViewModelFactory(new VerificationCodeViewModel(binding)).createFor()).get(VerificationCodeViewModel.class);
        initView();
        initObserve();
        binding.setVerficationModel(model);

    }

    public void getAllBackgroundData() {
        getLocation();
        SocialPrefrences.setIMEIID(this, getIMEIDeviceId());
        getCurrentTizeZone();
        getPhoneInfo();
        // getImeiNumber();
    }

    private void initView() {
        getAllBackgroundData();
        countTimer();
        binding.otpBtn.setOnClickListener(v -> {
            model.onOtp();
        });
        binding.txtSendAgain.setOnClickListener(v -> {
            countTimer();
            model.resendOtp();
        });
    }

    public void countTimer() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.txtCountLayout.setVisibility(View.VISIBLE);
                binding.tctExpire.setText(millisUntilFinished / 1000 + " sec");
                binding.txtSendAgain.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                binding.txtSendAgain.setVisibility(View.VISIBLE);
                binding.txtCountLayout.setVisibility(View.GONE);
            }
        }.start();
    }

    private void initObserve() {
        CustomDialogBuilder customDialogBuilder = new CustomDialogBuilder(this);
        model.isloading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isloading) {
                if (isloading) {
                    customDialogBuilder.showLoadingDialog();
                } else {
                    customDialogBuilder.hideLoadingDialog();
                }
            }
        });
        model.toast.observe(this, s -> {
            if (s != null && !s.isEmpty()) {
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            }
        });
        model.isVerify.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isVerify) {
                if (isVerify) {
                    SharedPrefreances.setSharedPreferenceString(VerificationCode.this, "login", "1");
                    SharedPrefreances.setSharedPreferenceBoolean(VerificationCode.this, SharedPrefreances.IS_OTP_VERIFY, true);
                    SocialPrefrences.setisVerified(VerificationCode.this, true);
                    Intent intent = new Intent(VerificationCode.this, InterestActivity.class);
                    startActivity(intent);
                }

            }
        });
        model.isResend.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isResend) {
                if (isResend) {
                    Utilss.showToast(VerificationCode.this, "Otp sent", R.color.social_background_blue);
                }

            }
        });
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

        SharedPrefreances.setSharedPreferenceString(this, SharedPrefreances.Reg_osType, "Android");
    }

    public void getCurrentTizeZone() {
        TimeZone tz = TimeZone.getDefault();
        System.out.println("TimeZone   " + tz.getDisplayName(false, TimeZone.SHORT) + " Timezone id :: " + tz.getID());
        //  TimeZone GMT+09:30 Timezone id :: Australia/Darwin  //timeZone format
        SocialPrefrences.setCurrentTimeZone(this, tz.getDisplayName(false, TimeZone.SHORT));

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

    public void getLocation() {
//        SharedPrefreances.setSharedPreferenceString(getActivity(), SharedPrefreances.Reg_lat, lat);
//        SharedPrefreances.setSharedPreferenceString(getActivity(), SharedPrefreances.Reg_lat, lng);
        if (!canGetLocation()) {
            Toast.makeText(this, getString(R.string.Please_Enable_Location), Toast.LENGTH_SHORT).show();
        }

        displayLocationSettingsRequest(this);
        requestLocationUpdates(this);
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
                            status.startResolutionForResult(VerificationCode.this, 12);
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

                        SharedPrefreances.setSharedPreferenceString(context, SharedPrefreances.Reg_lat, lat);
                        SharedPrefreances.setSharedPreferenceString(context, SharedPrefreances.Reg_lng, lng);
                        //String locationname = getAddress(location.getLatitude(), location.getLongitude());
                        Log.e("TAG", lat + "," + lng);

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

    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Toast.makeText(RegisterOtpActivity.this, "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //  Toast.makeText(RegisterOtpActivity.this, "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                // Toast.makeText(RegisterOtpActivity.this, message, Toast.LENGTH_LONG).show();
       /*         textViewMessage.setText(
                        String.format("%s - %s", getString(R.string.received_message), message));*/
                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            binding.pinView.setText(matcher.group(0));
        }
    }

}
