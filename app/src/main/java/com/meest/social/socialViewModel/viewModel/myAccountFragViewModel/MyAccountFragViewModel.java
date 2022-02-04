package com.meest.social.socialViewModel.viewModel.myAccountFragViewModel;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.Activities.EditProfileActivity;
import com.meest.Paramaters.UserStoryParameter;
import com.meest.R;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.social.socialViewModel.model.UserProfileRespone1;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.svs.activities.FollowListActivity;
import com.meest.utils.goLiveUtils.CommonUtils;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MyAccountFragViewModel extends ViewModel {
    public Boolean isRunning = false;
    public ViewPagerAdapter viewPagerAdapter;
    public int EDIT_INT = 10;
    public String image_type = "";
    public int status_post = 0;
    public MutableLiveData <String> profileImgUri = new MutableLiveData<>();
    public MutableLiveData <String> profileImgUriMale = new MutableLiveData<>();
    public MutableLiveData <String> profileImgUriFemale = new MutableLiveData<>();
    public MutableLiveData <String> followerCount = new MutableLiveData<>();
    public MutableLiveData <String> followTitle = new MutableLiveData<>();
    public MutableLiveData <String> followingCount = new MutableLiveData<>();
    public MutableLiveData <String> txtPostCount = new MutableLiveData<>();
    public MutableLiveData <String> txtPostTitleSet = new MutableLiveData<>();
    public MutableLiveData <String> tvBioSet = new MutableLiveData<>();
    public MutableLiveData<UserProfileRespone1> coverPicEmpty = new MutableLiveData<UserProfileRespone1>();

    public CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<Boolean> loaderVisibility = new MutableLiveData<>();


    public void backPressed(FragmentActivity activity) {
        if (activity != null)
            activity.onBackPressed();
    }

    public void editProfileIntent(FragmentActivity activity) {
        Intent intent = new Intent(activity, EditProfileActivity.class);
        intent.putExtra("image_type", image_type);
        activity.startActivityForResult(intent, EDIT_INT);
    }

    public void followListIntent(Context activity) {
        Intent intent = new Intent(activity, FollowListActivity.class);
        intent.putExtra("type", "following");
        intent.putExtra("userId", SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.ID));
        intent.putExtra("userName", SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.USERNAME));
        intent.putExtra("isSvs", false);
        intent.putExtra("changerole", "false");
        activity.startActivity(intent);
    }

    public void followListIntent1(Context activity) {
        Intent intent = new Intent(activity, FollowListActivity.class);
        intent.putExtra("type", "follower");
        intent.putExtra("userId", SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.ID));
        intent.putExtra("isSvs", false);
        intent.putExtra("changerole", "false");
        activity.startActivity(intent);
    }

    public void updatelanguage(String lang_eng, Context activity) {
        if (lang_eng.equals("en")) {
            setAppLocale(lang_eng, activity);
        } else if (lang_eng.equals("hi")) {
            setAppLocale(lang_eng, activity);
        } else if (lang_eng.equals("pa")) {
            setAppLocale(lang_eng, activity);
        } else if (lang_eng.equals("bn")) {
            setAppLocale(lang_eng, activity);
        } else if (lang_eng.equals("ur")) {
            setAppLocale(lang_eng, activity);
        } else if (lang_eng.equals("ma")) {
            setAppLocale(lang_eng, activity);
        } else if (lang_eng.equals("or")) {
            setAppLocale(lang_eng, activity);
        } else if (lang_eng.equals("as")) {
            setAppLocale(lang_eng, activity);
        } else if (lang_eng.equals("te")) {
            setAppLocale(lang_eng, activity);
        } else if (lang_eng.equals("ta")) {
            setAppLocale(lang_eng, activity);
        } else {
            Log.e("==========", "Not Language");
        }

    }

    public void setAppLocale(String localeCode, Context activity) {
        Resources resources = activity.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(localeCode.toLowerCase()));
        resources.updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(localeCode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);

    }

    public void fetchCurrentUserData(Context activity) {
        // image.setVisibility(View.VISIBLE);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.AUTH_TOKEN));
        UserStoryParameter paramter = new UserStoryParameter(SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.ID));
        disposable.add(Global.initSocialRetrofit().getUserProfileData(header)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> loaderVisibility.setValue(true))
                .doOnTerminate(() -> loaderVisibility.setValue(false))
                .subscribe((UserProfileRespone1 response, Throwable throwable) -> {
                    //  image.setVisibility(View.GONE);
                    try {
                        Log.e("TAG", "fetchCurrentUserData: " + "CODE : " + response.getCode());
                        if (response.getCode() == 1 && response.getSuccess()) {
                            SharedPrefreances.setSharedPreferenceString(activity, "following", response.getDataUser().getTotalFollowings().toString());
                            SharedPrefreances.setSharedPreferenceString(activity, "followers", response.getDataUser().getTotalFollowers().toString());
                            if (response.getDataUser().getThumbnail() != null) {
                                profileImgUri.setValue(response.getDataUser().getThumbnail());
                            }
                            if (response.getDataUser().getGender().equalsIgnoreCase(activity.getString(R.string.male))) {
                                image_type = "Male";
                                profileImgUriMale.setValue(response.getDataUser().getDisplayPicture());

                            } else if (response.getDataUser().getGender().equalsIgnoreCase(activity.getString(R.string.female))) {
                                image_type = "Female";
                                profileImgUriFemale.setValue(response.getDataUser().getDisplayPicture());
                            }
                            if (response.getDataUser().getTotalFollowers().toString().equals("0")) {
                                followerCount.setValue("0");

                            } else {
                                followerCount.setValue(response.getDataUser().getTotalFollowers().toString());
                            }

                            if (response.getDataUser().getTotalFollowers().toString().equals("0") || response.getDataUser().getTotalFollowers().toString().equals("1")) {
                                followTitle.setValue("Follower");
                            }

                            if (response.getDataUser().getTotalFollowings().toString().equals("0")) {
                                followingCount.setValue("0");
                            } else {
                                followingCount.setValue(response.getDataUser().getTotalFollowings().toString());
                            }

                            if (!response.getDataUser().getTotalPosts().equals("0")) {
                                txtPostCount.setValue(response.getDataUser().getTotalPosts().toString());
                            } else {
                                txtPostCount.setValue("0");

                            }
                            if (response.getDataUser().getTotalPosts().toString().equals("0") || response.getDataUser().getTotalPosts().toString().equals("1")) {
                                txtPostTitleSet.setValue("Post");
                            }
                            if (response.getDataUser().getIsBlocked() == 0) {
//                            pushFragment(new MyPhotoGridFragment(), "PhotoGrid");
                            }

                            if (response.getDataUser().getAbout() != null && !response.getDataUser().getAbout().isEmpty()) {
                                tvBioSet.setValue(CommonUtils.decodeEmoji(response.getDataUser().getAbout()));
                            } else {
                                tvBioSet.setValue("N/A");
                            }

                            SharedPrefreances.setSharedPreferenceString(activity, "post", response.getDataUser().getTotalPosts().toString());
                            SharedPrefreances.setSharedPreferenceString(activity, "fname", response.getDataUser().getFirstName());
                            SharedPrefreances.setSharedPreferenceString(activity, "lname", response.getDataUser().getFirstName());

                            if (response.getDataUser().getCoverPicture() != null && !response.getDataUser().getCoverPicture().isEmpty()) {
                                coverPicEmpty.setValue(response);
                            }
                        }
                        else {
                            loaderVisibility.setValue(false);
                            Utilss.showToast(activity, activity.getString(R.string.SOME_ERROR), R.color.grey);
                        }

                    } catch (Exception e) {

                    }
                }));
    }

    public void viewPagerInit(FragmentManager fm) {
        viewPagerAdapter = new ViewPagerAdapter(fm);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        public void add(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

}
