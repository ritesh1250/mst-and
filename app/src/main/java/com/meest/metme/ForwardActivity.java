package com.meest.metme;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.meest.Activities.base.ApiCallActivity;
import com.meest.R;
import com.meest.databinding.ActivityForwardBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.model.ForwardMessageResponse;
import com.meest.metme.viewmodels.ForwardViewModel;
import com.meest.utils.IntentWrapper;

import org.greenrobot.eventbus.Subscribe;

public class ForwardActivity extends ApiCallActivity {
    ActivityForwardBinding binding;
    ForwardViewModel viewModel;
    IntentWrapper intentWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forward);
        intentWrapper = new Gson().fromJson(getIntent().getExtras().getString("intent"), IntentWrapper.class);
        viewModel = new ForwardViewModel(this, intentWrapper);
        binding.setViewModel(viewModel);
        initView();
        binding.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        viewModel.getFollower_FollowingList(SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        binding.forwardRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Subscribe
    public void forwardMessage(@NonNull ForwardMessageResponse response){
        if (response.isSuccess()){
            Intent intent=new Intent();
            setResult(RESULT_OK,intent);
            finish();
            ChatBoatActivity.context.finish();
        }else {
            dialogFailed(response.getErrorMessage());
        }

    }
}