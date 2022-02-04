package com.meest.videomvvmmodule.viewmodel;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.meest.videomvvmmodule.model.user.User;

public class FollowerFollowingViewModel extends ViewModel {

    public ObservableInt itemType = new ObservableInt(0);
    public User user;
    public String user_id;

}
