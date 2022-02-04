package com.meest.videomvvmmodule.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.videomvvmmodule.model.follower.Followerstatus;
import com.meest.videomvvmmodule.model.user.RestResponse;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.search.FetchUserActivity;
import com.meest.videomvvmmodule.view.search.QRScanActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class QRScanActivityViewModel extends ViewModel {
    public MutableLiveData<RestResponse> followApi = new MutableLiveData<>();
    private CompositeDisposable disposable = new CompositeDisposable();
    SessionManager sessionManager;
    public ObservableBoolean isFollowLoading = new ObservableBoolean(false);

    public void followUnfollow(String userId, Context context) {
        disposable.add(Global.initRetrofit().followUnFollow(Global.apikey, Global.userId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> {
                    isFollowLoading.set(true);
                })
                .doOnTerminate(() -> isFollowLoading.set(false))
                .subscribe((followRequest, throwable) -> {
                    if (followRequest != null && followRequest.getStatus() != null) {
                        // Toast.makeText(context, followRequest.getMessage(), Toast.LENGTH_SHORT).show();
                        if (followRequest.getMessage().equalsIgnoreCase("Follow Successfully")) {
                            Toast.makeText(context, context.getResources().getString(R.string.Follow_Successfully), Toast.LENGTH_SHORT).show();
                            Global.followUnfollow.put(userId, true);
                        } else if (followRequest.getMessage().equalsIgnoreCase("Unfollow Successfully")) {
                            Toast.makeText(context, context.getResources().getString(R.string.Unfollow_Successfully), Toast.LENGTH_SHORT).show();
                            Global.followUnfollow.put(userId, false);
                        }
                        followApi.setValue(followRequest);

                    }
                }));
    }

    public void follow_or_not(String userId, QRScanActivity qrScanActivity) {
        Log.e("=======userid", userId);
        disposable.add(Global.initRetrofit().follow_OR_NOT(Global.apikey, Global.userId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> {
                    isFollowLoading.set(true);
                })
                .doOnTerminate(() -> isFollowLoading.set(false))
                .subscribe((Followerstatus followRequest, Throwable throwable) -> {
                    if (followRequest != null) {
                        Log.e("=======Status==", String.valueOf(followRequest.getCode()));
                        if (followRequest.getCode() == 1) {
                            Toast.makeText(qrScanActivity, qrScanActivity.getResources().getString(R.string.Already_Followed), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(qrScanActivity, FetchUserActivity.class);
                            intent.putExtra("userid", userId);
                            Log.e("=======Status==1", String.valueOf(followRequest.getCode()));
                            qrScanActivity.startActivity(intent);
                        } else if (followRequest.getCode() == 2) {
                            Intent intent = new Intent(qrScanActivity, FetchUserActivity.class);
                            intent.putExtra("userid", userId);
                            Log.e("=======Status==2", String.valueOf(followRequest.getCode()));
                            followUnfollow(userId, qrScanActivity);
                            followRequest.setStatus("true");
                            qrScanActivity.startActivity(intent);
                        } else {
//                            Toast.makeText(qrScanActivity, qrScanActivity.getString(R.string.qr_error_msg), Toast.LENGTH_SHORT).show();
                            qrScanActivity.customDialogBuilder.showUserNotFoundDialog(new CustomDialogBuilder.OnDismissListener() {
                                @Override
                                public void onPositiveDismiss() {

                                    qrScanActivity.mCodeScanner.startPreview();
                                }

                                @Override
                                public void onNegativeDismiss() {
                                }
                            });
                        }
                    }
                }));
    }

}