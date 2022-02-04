package com.meest.social.socialViewModel.viewModel.editProfileViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.videomvvmmodule.model.user.User;

public class EditProfileViewModel extends ViewModel {
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    public boolean showRemovePhoto = false;
    public boolean isImageRemoved = false;
    public User user = null;


}
