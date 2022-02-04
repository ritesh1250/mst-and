package com.meest.videomvvmmodule.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.videomvvmmodule.adapter.ProfileCategoryAdapter;
import com.meest.videomvvmmodule.model.user.ProfileCategory;
import com.meest.videomvvmmodule.model.user.User;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.Global;

import java.io.File;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileViewModel extends ViewModel {

    public MutableLiveData<String> toast = new MutableLiveData<>();
    public ObservableBoolean isUsernameLoading = new ObservableBoolean(false);
    public ObservableBoolean isUsernameAvailable = new ObservableBoolean(true);
    public ObservableInt length = new ObservableInt();
    public MutableLiveData<Boolean> updateProfile = new MutableLiveData<>();
    public User user = null;
    public String imageProfileUri = "";
    public String imageCoverUri = "";
    CompositeDisposable disposable = new CompositeDisposable();
    public String cur_userName = "";
    private String newUserName = "";
    //    public String fullName = "", bio = "", fbUrl = "", instaUrl = "", youtubeUrl = "", profile_cat_id = "", profile_cat_name = "";
    public String first_name = "", last_name = "", bio = "", user_email = "", user_mobile_no = "", gender = "", dob = "";
    public ProfileCategory profileCategoryList;
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    public String uploadedImageUrl = "";
    public String uploadCoverUrl = "";
    public boolean showRemovePhoto = false;

    public boolean isImageRemoved = false;

    public boolean isEdited = false;

    public Context context;


    public void afterUserNameTextChanged(CharSequence s) {
        newUserName = s.toString();
        if (!cur_userName.equals(newUserName)) {
            checkForUserName();
        } else {
            if (!newUserName.isEmpty()) {
                isUsernameAvailable.set(true);
            }
            isUsernameAvailable.set(false);
            isUsernameLoading.set(false);
        }
    }

    public ProfileCategoryAdapter adapter = new ProfileCategoryAdapter();

    public void afterTextChanged(CharSequence charSequence, int type) {
        switch (type) {
            case 1:
                first_name = charSequence.toString();
                break;
            case 2:
                last_name = charSequence.toString();
                break;
            case 3:
                newUserName = charSequence.toString();
                break;
            case 4:
                bio = charSequence.toString();
                length.set(bio.length());
                break;
            case 5:
                user_email = charSequence.toString();
                break;
            case 6:
                user_mobile_no = charSequence.toString();
                break;
            case 7:
                gender = charSequence.toString();
                break;
            case 8:
                dob = charSequence.toString();
                break;
        }
    }

    public void setEdited() {
        isEdited = true;
    }

    private void checkForUserName() {
        disposable.add(Global.initRetrofit().checkUsername(Global.apikey, newUserName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isUsernameLoading.set(true))
                .doOnTerminate(() -> isUsernameLoading.set(false))
                .subscribe((checkUsername, throwable) -> {
                    if (checkUsername != null && checkUsername.getStatus() != null) {
                        isUsernameAvailable.set(checkUsername.getStatus());
                    }
                }));
    }

    public void fetchProfileCategories() {
        disposable.add(Global.initRetrofit().getProfileCategoryList(Global.apikey, Global.accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isUsernameLoading.set(true))
                .doOnTerminate(() -> isUsernameLoading.set(false))
                .subscribe((profileCategoryList, throwable) -> {
                    if (profileCategoryList != null && profileCategoryList.getData() != null) {
                        this.profileCategoryList = profileCategoryList;
                        adapter.updateData(profileCategoryList.getData());
                    }
                }));
    }


    public void updateUser() {
        if (user_mobile_no == null || user_mobile_no.equals("")) {
            if (checkEmailValidation() | nullField()) {
                return;
            }
        } else {
            if (checkEmailValidation() | !checkMobileValidation() | nullField()) {
                return;
            }
        }

        HashMap<String, RequestBody> hashMap = new HashMap<>();

        hashMap.put("first_name", toRequestBody(first_name.trim()));
        hashMap.put("last_name", toRequestBody(last_name.trim()));
        hashMap.put("user_name", toRequestBody(newUserName.trim()));
        Log.e("======bio", "==========" + bio + "=============");
        if (bio != null) {
            if (!bio.equals("")) {
                hashMap.put("bio", toRequestBody(bio.replace("\n", " ")));
                Log.e("boi==============", bio);
            }
        }
        if (user_email != null) {
            if (!user_email.equals("")) {
                hashMap.put("user_email", toRequestBody(user_email));
                hashMap.put("identity", toRequestBody(user_email));
            }
        }
        if (user_mobile_no != null) {
            if (!user_mobile_no.equals("")) {
                hashMap.put("user_mobile_no", toRequestBody(user_mobile_no));
            }
        }

        hashMap.put("gender", toRequestBody(getGender(gender)));
        hashMap.put("dob", toRequestBody(dob));
        hashMap.put("user_id", toRequestBody(Global.userId));

        if(uploadedImageUrl!=null){
            if (!uploadedImageUrl.equals("")) {
                hashMap.put("user_profile", toRequestBody(uploadedImageUrl));
            }
        }

        if (isImageRemoved) {
            hashMap.put("user_profile", toRequestBody(Const.defaultProfileUrl));
        }

        disposable.add(Global.initRetrofit().updateUser(Global.apikey, hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> isloading.setValue(false))
                .unsubscribeOn(Schedulers.io())
                .subscribe((updateUser, throwable) -> {
                    if (updateUser != null && updateUser.getStatus()) {
                        user = updateUser;
                        toast.setValue(context.getString(R.string.Profile_Updated_Successfully));
                        Log.e("user_image========", user.getData().toString());
                        updateProfile.setValue(true);
                    }
                }));
          /*hashMap.put("fb_url", toRequestBody(fbUrl));
        hashMap.put("insta_url", toRequestBody(instaUrl));
        hashMap.put("youtube_url", toRequestBody(youtubeUrl));
        if (!TextUtils.isEmpty(profile_cat_id)) {
            hashMap.put("profile_category", toRequestBody(profile_cat_id));
        }*/

//        MultipartBody.Part body = null;
//        if (imageUri != null && !imageUri.isEmpty()) {
//            File file = new File(imageUri);
//            RequestBody requestFile =
//                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//            body = MultipartBody.Part.createFormData("user_profile", file.getName(), requestFile);
//        }
//        disposable.add(Global.initRetrofit().updateUser(Global.apikey, Global.accessToken, hashMap, body)
    }

    private String getGender(String gender) {
        return gender.replaceAll(" ", "_");
    }

    private boolean nullField() {
        if (first_name.trim().length() == 1) {
            toast.setValue(context.getString(R.string.minimum_two_char_fname));
            return true;
        } else if (last_name.trim().length() == 1) {
            toast.setValue(context.getString(R.string.minimum_two_char_lname));
            return true;
        } else if (first_name.trim().equals("") || last_name.trim().equals("") || gender.equals("") || dob.equals("")) {
            toast.setValue(context.getString(R.string.First_name_Last_name_Gender_and_DOB_are_compulsory));
            return true;
        }
        return false;
    }

    private boolean checkEmailValidation() {
        String input = user_email;
        String pattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//        if (input.isEmpty()) {
//            toast.setValue("Enter Email Address");
//            return false;
//        } else
        if (!input.isEmpty()) {
            if (!input.matches(pattern)) {
                toast.setValue(context.getString(R.string.Please_enter_correct_email_address));
                return true;
            }
        }
        return false;
    }

    public boolean checkMobileValidation() {
        String checkString = user_mobile_no;
        String pattern = "[0-9]+";
        if (!checkString.matches(pattern)) {
            toast.setValue(context.getString(R.string.Please_Enter_Only_Number));
            return false;
        } else if (checkString.length() > 10) {
            toast.setValue(context.getString(R.string.invalid_mobile_number));
            return false;
        } else if (checkString.length() < 10) {
            toast.setValue(context.getString(R.string.Mobile_digit_less_than_10_digit));
            return false;
        }
        return true;
    }


    public RequestBody toRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    public void updateData() {
        first_name = user.getData().getFirstName();
        last_name = user.getData().getLastName();
        newUserName = user.getData().getUserName();
        bio = user.getData().getBio();
        user_email = user.getData().getUserEmail();
        user_mobile_no = user.getData().getUserMobileNo();
        gender = user.getData().getGender();
        dob = user.getData().getDob();
        showRemovePhoto = user.getData().getUserProfile() != null && !user.getData().getUserProfile().equals(Const.defaultProfileUrl);
//        showRemoveCover = user.getData().getCoverProfile() != null && !user.getData().getCoverProfile().equals("");    viewModel.isImageRemoved = true;

       /* fullName = user.getData().getFullName();
        newUserName = user.getData().getUserName();
        bio = user.getData().getBio();
        fbUrl = user.getData().getFbUrl();
        instaUrl = user.getData().getInstaUrl();
        youtubeUrl = user.getData().getYoutubeUrl();
        profile_cat_id = user.getData().getProfileCategory();*/
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public void getProfileUrl(String uri) {
        Log.e("usrl================", uri);
        MultipartBody.Part body = null;
        if (uri != null && !uri.isEmpty()) {
            File file = new File(uri);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            disposable.add(Global.initRetrofit().uploadImageRequest(Global.apikey, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable1 -> isloading.setValue(true))
                    .doOnTerminate(() -> isloading.setValue(false))
                    .unsubscribeOn(Schedulers.io())
                    .subscribe((uploadImageResponse, throwable) -> {
                        if (uploadImageResponse != null && uploadImageResponse.getStatus()) {
                            uploadedImageUrl = uploadImageResponse.getData();
                            Log.e("Profile Url", uploadedImageUrl);
                        }
                    }));
        }
    }

    public void getCoverUrl(String uri) {
        MultipartBody.Part body = null;
        if (uri != null && !uri.isEmpty()) {
            File file = new File(uri);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            disposable.add(Global.initRetrofit().uploadImageRequest(Global.apikey, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable1 -> isloading.setValue(true))
                    .doOnTerminate(() -> isloading.setValue(false))
                    .unsubscribeOn(Schedulers.io())
                    .subscribe((uploadImageResponse, throwable) -> {
                        if (uploadImageResponse != null && uploadImageResponse.getStatus()) {

                            uploadCoverUrl = uploadImageResponse.getData();
                            Log.e("Cover Url", uploadCoverUrl);

                        }
                    }));
        }
    }
}
