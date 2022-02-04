package com.meest.videomvvmmodule.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.videomvvmmodule.utils.Global;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ReportViewModel extends ViewModel {

    public int reportType;
    public String postId;
    public String userId;
    public String reason;
    public String description = "";
    public String contactInfo = "";
    public MutableLiveData<Boolean> isValid = new MutableLiveData<>();
    public MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    public void afterUserNameTextChanged(CharSequence s) {
        description = s.toString();
    }

//    public void afterContactDetailsChanged(CharSequence s) {
//        contactInfo = s.toString();
//    }


    public void callApiToReport( ) {
        description=description.replaceAll("\\s+"," ").trim();
//        replaceAll("\\s{2,}", " "));
        Log.e("===========description",description+"mayank");
        if (!description.isEmpty() && !contactInfo.isEmpty() ) {

            HashMap<String, Object> hashMap = new HashMap<>();
            if (reportType == 1) {
                hashMap.put("report_type", "report_video");
                hashMap.put("post_id", postId);
            } else {
                hashMap.put("report_type", "report_user");
                hashMap.put("user_id", userId);
            }
            hashMap.put("reason", reason);
            hashMap.put("description", description);
            hashMap.put("contact_info", contactInfo);
            hashMap.put("user_id", userId);
            Log.e("=========Reports","===="+reason+","+description+","+contactInfo+","+userId);

            disposable.add(Global.initRetrofit().reportSomething(Global.apikey, hashMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe((report, throwable) -> {
                        if (report != null && report.getStatus()) {
                            isSuccess.setValue(true);
                        }
                    }));
        } else {
            isValid.setValue(false);
        }
    }

    private boolean checkDescription(String description) {
        String text=description.trim();
        if(text.matches("^[a-zA-Z]*$"))
            return false;
        else
            return true;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
