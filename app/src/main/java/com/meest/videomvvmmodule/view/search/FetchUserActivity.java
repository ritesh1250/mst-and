package com.meest.videomvvmmodule.view.search;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.meest.R;
import com.meest.databinding.ActivityFetchUserBinding;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.profile.ProfileFragment;

public class FetchUserActivity extends BaseActivity {
    ActivityFetchUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fetch_user);

        String userid = getIntent().getStringExtra("userid");

        ProfileFragment fragment = new ProfileFragment(0);
        Bundle bundle = new Bundle();
        bundle.putString("userid", userid);
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lout_main, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}