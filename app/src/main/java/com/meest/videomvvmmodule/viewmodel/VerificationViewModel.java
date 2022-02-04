package com.meest.videomvvmmodule.viewmodel;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.videomvvmmodule.utils.Global;

import java.io.File;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VerificationViewModel extends ViewModel {

    public String proofUri = "", useUri = "";
    public Context context;
    public MutableLiveData<String> toast = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public ObservableBoolean isEnabled = new ObservableBoolean(false);
    private CompositeDisposable disposable = new CompositeDisposable();
    public String idNumber = "", name = "", address = "";
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    private String uploadedHoldingPhotoUrl = "";
    private String uploadedIDCardUrl = "";
    public MutableLiveData<Boolean> showPopUp = new MutableLiveData<>();
    public String client_id="";
    Dialog optDialog;
    EditText etOTP;
    Button submit;

    public void getHoldingPhotoUrl(String uri) {
        MultipartBody.Part body = null;
        if (uri != null && !uri.isEmpty()) {
            File file = new File(uri);
            RequestBody requestFile2 =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile2);

            disposable.add(Global.initRetrofit().uploadImageRequest(Global.apikey, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable1 -> isloading.setValue(true))
                    .doOnTerminate(() -> isloading.setValue(false))
                    .unsubscribeOn(Schedulers.io())
                    .subscribe((uploadImageResponse, throwable) -> {
                        if (uploadImageResponse != null && uploadImageResponse.getStatus()) {

                            uploadedHoldingPhotoUrl = uploadImageResponse.getData();
                            Log.e("Profile Url", uploadedHoldingPhotoUrl);
                        }
                    }));
        }
    }

    public void getIdCardUrl(String uri) {
        MultipartBody.Part body = null;
        if (uri != null && !uri.isEmpty()) {
            File file = new File(useUri);
            RequestBody requestFile2 =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile2);

            disposable.add(Global.initRetrofit().uploadImageRequest(Global.apikey, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable1 -> isloading.setValue(true))
                    .doOnTerminate(() -> isloading.setValue(false))
                    .unsubscribeOn(Schedulers.io())
                    .subscribe((uploadImageResponse, throwable) -> {
                        if (uploadImageResponse != null && uploadImageResponse.getStatus()) {

                            uploadedIDCardUrl = uploadImageResponse.getData();
                            Log.e("Profile Url", uploadedIDCardUrl);
                        }
                    }));
        }
    }

    public void requestVerify() {
        if (checkValidation()) {
            HashMap<String, RequestBody> hashMap = new HashMap<>();
            hashMap.put("id_number", toRequestBody(idNumber));
            hashMap.put("name", toRequestBody(name));
            hashMap.put("address", toRequestBody(address));
            hashMap.put("user_id", toRequestBody(Global.userId));
            if (!uploadedHoldingPhotoUrl.equals("")) {
                hashMap.put("verification_profile", toRequestBody(uploadedHoldingPhotoUrl));
            }
            if (!uploadedIDCardUrl.equals("")) {
                hashMap.put("id_card_image", toRequestBody(uploadedIDCardUrl));
            }
            disposable.add(Global.initRetrofit().verifyRequest(Global.apikey, hashMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable1 -> {
                        isLoading.setValue(true);
                        showPopUp.setValue(false);
                    })
                    .doOnTerminate(() -> {
                        isLoading.setValue(false);
                        showPopUp.setValue(true);
                    })
                    .subscribe((restResponse, throwable) -> {
                        Toast.makeText(context, restResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        client_id=restResponse.getClient_id();
                        showPopUpOTP();
                    }));
        } else {
            toast.setValue(context.getString(R.string.All_Fields_are_required));
        }
    }
    private void showPopUpOTP() {
        Log.e("============", "showPopUpOTP: ");

        optDialog = new Dialog(context);
        optDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        optDialog.setContentView(R.layout.dialog_aadhar_otp);
        optDialog.setCancelable(false);
        optDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        optDialog.show();
        etOTP=optDialog.findViewById(R.id.et_otp);
        submit=optDialog.findViewById(R.id.submit_otp);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpVerification(etOTP.getText().toString());
            }
        });

    }

    private void otpVerification(String otp) {
        disposable.add(Global.initRetrofit().verifyOtp(Global.apikey, Global.userId,otp,client_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> {
                    isLoading.setValue(true);
                })
                .doOnTerminate(() -> {
                    isLoading.setValue(false);
                })
                .subscribe(new BiConsumer<OtpResponse, Throwable>() {
                    @Override
                    public void accept(OtpResponse otpResponse, Throwable throwable) throws Exception {
                        Toast.makeText(context, otpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        optDialog.dismiss();
                    }
                }));
    }

    private boolean checkValidation() {
        if (idNumber != null &&
                !idNumber.isEmpty() &&
                name != null &&
                !name.isEmpty() &&
                address != null &&
                !address.isEmpty() &&
                uploadedHoldingPhotoUrl != null &&
                !uploadedHoldingPhotoUrl.isEmpty() &&
                uploadedIDCardUrl != null &&
                !uploadedIDCardUrl.isEmpty()) {
            return true;
        }
        return false;
    }

    public void afterTextChanged(CharSequence text, int type) {
        switch (type) {
            case 0:
                idNumber = text.toString();
                break;
//            case 1:
//                name = text.toString();
//                break;
            case 2:
                address = text.toString();
                break;
        }
//        checkValidData();
    }

    private void checkValidData() {
        isEnabled.set(idNumber != null &&
                !idNumber.isEmpty() &&
                name != null &&
                !name.isEmpty() &&
                address != null &&
                !address.isEmpty());
    }

    public RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
