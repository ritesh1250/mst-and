package com.meest.videomvvmmodule.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.videomvvmmodule.model.user.RestResponse;
import com.meest.videomvvmmodule.model.user.User;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends ViewModel  {
    public SessionManager sessionManager;
    public MutableLiveData<User> user = new MutableLiveData<>();
    public MutableLiveData<Integer> onItemClick = new MutableLiveData<>();
    public MutableLiveData<Integer> selectPosition = new MutableLiveData<>();
    public MutableLiveData<Intent> intent = new MutableLiveData<>();
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    public ObservableBoolean isBackBtn = new ObservableBoolean(false);
    public String userId = "";
    public MutableLiveData<RestResponse> followApi = new MutableLiveData<>();
    public ObservableBoolean isLikedVideos = new ObservableBoolean(false);
    public ObservableInt isMyAccount = new ObservableInt(0);
    CompositeDisposable disposable = new CompositeDisposable();
    public ObservableBoolean isFollowLoading = new ObservableBoolean(false);
    public static MutableLiveData<Boolean> followUnfollow=new MutableLiveData<>(false);
    public void setOnItemClick(int type) {
        onItemClick.setValue(type);
    }

//    public void onSocialClick(int type) {
//        String url = "";
//        if (user.getValue() != null) {
//            switch (type) {
//                case 1:
//                    url = user.getValue().getData().getFbUrl();
//                    break;
//                case 2:
//                    url = user.getValue().getData().getInstaUrl();
//                    break;
//                default:
//                    url = user.getValue().getData().getYoutubeUrl();
//                    break;
//            }
//        }
//
//        intent.setValue(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//    }

    public void fetchUserById(String userid) {
        disposable.add(Global.initRetrofit().getUserDetails(Global.apikey, userid, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> isloading.setValue(false))
                .subscribe((user, throwable) -> {
                    if (user != null && user.getData() != null) {
//                        Log.d("AAAAAAAAAAA", "fetchUserById: " + user.getData().toString());
//                        Log.d("AAAAAAAAAAA", "fetchUserById: " + user.getData().getProfileCategoryName());
//                        Log.d("AAAAAAAAAAA", "totalpost: -==================================" + user.getData().getTotalPost());
                        this.user.setValue(user);
                        if (Global.userId.equals(user.getData().getUserId())) {
                            sessionManager.saveUser(user);
                        }
                        if (isMyAccount.get() != 0) {
                            if (user.getData().getIsFollowing() == 1) {
                                isMyAccount.set(1);
                            } else {
                                isMyAccount.set(2);
                            }
                        }
                    }
                }));
    }

    public void followUnfollow(Context context) {

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
                        followUnfollow.setValue(true);
                        if (isMyAccount.get() == 1) {
                            isMyAccount.set(2);
                        } else {
                            isMyAccount.set(1);
                        }
                        //Toast.makeText(context, followRequest.getMessage(), Toast.LENGTH_SHORT).show();
                        followApi.setValue(followRequest);

                        if (followRequest.getMessage().equalsIgnoreCase("Follow Successfully")) {
                            Toast.makeText(context, context.getResources().getString(R.string.Follow_Successfully), Toast.LENGTH_SHORT).show();
                            Global.followUnfollow.put(userId, true);
                        } else if (followRequest.getMessage().equalsIgnoreCase("Unfollow Successfully")) {
                            Toast.makeText(context, context.getResources().getString(R.string.Unfollow_Successfully), Toast.LENGTH_SHORT).show();
                            Global.followUnfollow.put(userId, false);
                        }
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
