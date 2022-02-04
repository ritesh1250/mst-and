package com.meest.social.socialViewModel.view.feelingAndActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.meest.R;
import com.meest.databinding.FeelingAndActivityModelBinding;
import com.meest.social.socialViewModel.adapter.feelActAdapters.FeelingPagerAdapter;
import com.meest.social.socialViewModel.viewModel.feelingAndActivityViewModel.FeelingAndActivityViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class FeelingAndActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private FeelingAndActivityModelBinding binding;
    private FeelingAndActivityViewModel viewModel;
    public String activityId, activityTitle;
    private static final String TAG = "FeelingAndActivity";
//    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.feeling_and_activity_model);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new FeelingAndActivityViewModel(this, FeelingAndActivity.this, binding)).createFor()).get(FeelingAndActivityViewModel.class);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        viewPagerFun();
        clickEvents();
//        viewModel.viewPagerListener();
        init();
    }


    private void init() {
        binding.loading.playAnimation();
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Feeling"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Activity"));
        binding.pager.setAdapter(new FeelingPagerAdapter(getSupportFragmentManager(), binding.tabLayout.getTabCount(), this, binding.edtSearchFeeling));
        binding.tabLayout.setOnTabSelectedListener(this);
        activityId = viewModel.activityId;
        activityTitle = viewModel.activityTitle;


    }

    private void viewPagerFun() {
        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1) {
                    binding.activityTabBtn.setBackground(AppCompatResources.getDrawable(FeelingAndActivity.this, R.drawable.blue_bg_curved));
                    binding.feelingTabBtn.setBackground(AppCompatResources.getDrawable(FeelingAndActivity.this, R.drawable.grey_bg_curved));
                } else {
                    binding.feelingTabBtn.setBackground(AppCompatResources.getDrawable(FeelingAndActivity.this, R.drawable.blue_bg_curved));
                    binding.activityTabBtn.setBackground(AppCompatResources.getDrawable(FeelingAndActivity.this, R.drawable.grey_bg_curved));
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void clickEvents() {
        binding.feelActDone.setOnClickListener(v -> viewModel.feelActDoneFunc());
        binding.activityTabBtn.setOnClickListener(v -> binding.pager.setCurrentItem(getItem(+1), true));
        binding.feelingTabBtn.setOnClickListener(v -> binding.pager.setCurrentItem(getItem(-1), true));
        binding.backImage.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        binding.pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public class addListenerOnTextChange implements TextWatcher {
        private Context mContext;
        EditText mEdittextview;

        public addListenerOnTextChange(Context context, EditText edittextview) {
            super();
            this.mContext = context;
            this.mEdittextview = edittextview;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //What you want to do
        }
    }
    public int getItem(int i) {
        return binding.pager.getCurrentItem() + i;
    }


    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ArrayList<String> data)
    {
        if (data!=null)
        {
            viewModel.activityId = data.get(0);
            viewModel.activityTitle = data.get(1);
        }
    }
}