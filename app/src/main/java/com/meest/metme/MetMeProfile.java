package com.meest.metme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.databinding.ActivityMetMeProfileBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.viewmodels.MetMeProfileViewModel;
import com.meest.social.socialViewModel.model.UserProfileRespone1;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MetMeProfile extends AppCompatActivity {
    ActivityMetMeProfileBinding binding;
    String otherUserId;
    MetMeProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MetMeProfile.this, R.layout.activity_met_me_profile);
        if (getIntent() != null) {
            otherUserId = getIntent().getStringExtra("otherUserId");
        }
        viewModel = new MetMeProfileViewModel();
        Log.e("=======other", otherUserId);
        binding.setViewModel(viewModel);
        initView();
        initObserve();
        initListener();
    }

    private void initListener() {
        binding.goToMeest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MetMeProfile.this, OtherUserAccount.class);
                intent.putExtra("userId",otherUserId);
                startActivity(intent);
            }
        });
    }

    private void initObserve() {
        viewModel.userProfileRespone.observe(this, new Observer<UserProfileRespone1>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(UserProfileRespone1 userProfileRespone) {
                Glide.with(MetMeProfile.this).load(userProfileRespone.getDataUser().getDisplayPicture()).placeholder(R.drawable.ic_edit_profile_img).into(binding.imgProfile);
                binding.userName.setText(userProfileRespone.getDataUser().getFirstName() + " " + userProfileRespone.getDataUser().getLastName());
                binding.txtFollowers.setText(String.valueOf(userProfileRespone.getDataUser().getTotalFollowers()));
                binding.txtFollowingCount.setText(String.valueOf(userProfileRespone.getDataUser().getTotalFollowings()));
                binding.txtPost.setText(String.valueOf(userProfileRespone.getDataUser().getTotalPosts()));
                try {
                    binding.userBio.setText( URLDecoder.decode(userProfileRespone.getDataUser().getAbout(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if ((int)userProfileRespone.getDataUser().getTotalFollowers() > 1) {
                    binding.tvFollowerTitle.setText(getResources().getString(R.string.followers));
                } else {
                    binding.tvFollowerTitle.setText(getResources().getString(R.string.follower));
                }
                if ((int)userProfileRespone.getDataUser().getTotalPosts() > 1) {
                    binding.tvPostTitle.setText(getResources().getString(R.string.posts));
                } else {
                    binding.tvPostTitle.setText(getResources().getString(R.string.post));
                }
            }
        });
    }

    private void initView() {
        viewModel.fetchMetMeProfile(otherUserId, SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
    }
}