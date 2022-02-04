package com.meest.social.socialViewModel.view.home.navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.R;
import com.meest.databinding.SocialMyBlockListBinding;
import com.meest.social.socialViewModel.viewModel.navigationViewModel.MyBlockListviewModel;

public class MyBlockList extends AppCompatActivity {
    MyBlockListviewModel blockListviewModel;
    SocialMyBlockListBinding blockListBinding;
    private static final int PAGE_SIZE = 20;
    int pagePerRecord = 10;
    int pageno = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        blockListBinding= DataBindingUtil.setContentView(this,R.layout.social_my_block_list);
        blockListviewModel= new ViewModelProvider(this).get(MyBlockListviewModel.class);
        blockListBinding.setSocialMyBLockListBinding(blockListviewModel);
        initView();
        initListeners();
        initObserve();
        
;    }

    private void initView() {
        blockListBinding.loading.setAnimation("loading.json");
        blockListBinding.loading.playAnimation();
        blockListBinding.loading.loop(true);
    }
    
    private void initListeners() {
    }
    
    private void initObserve() {
    }
}