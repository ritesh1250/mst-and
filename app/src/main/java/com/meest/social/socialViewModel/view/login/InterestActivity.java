package com.meest.social.socialViewModel.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.meest.R;
import com.meest.databinding.ActivityInterestModelBinding;
import com.meest.meestbhart.register.fragment.choosetopic.model.TopicsResponse;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.social.socialViewModel.adapter.ChooseAdapterSocial;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.social.socialViewModel.viewModel.loginViewModel.InterestViewModel;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InterestActivity extends AppCompatActivity implements ChooseAdapterSocial.OnItemClickListener {
    public GridLayoutManager layoutManager;
    ActivityInterestModelBinding binding;
    InterestViewModel model;
    List<TopicsResponse.Row> list_topic;
//    ChooseAdapterSocial adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_interest_model);
        model = new ViewModelProvider(this, new ViewModelFactory(new InterestViewModel(binding)).createFor()).get(InterestViewModel.class);
        initView();
        initObserve();
        binding.setInterestModel(model);
    }

    private void initObserve() {
        CustomDialogBuilder customDialogBuilder = new CustomDialogBuilder(this);
        model.isloading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isloading) {
                if (isloading) {
                    customDialogBuilder.showLoadingDialog();
                } else {
                    customDialogBuilder.hideLoadingDialog();
                }
            }
        });

        model.toast.observe(this, s -> {
            if (s != null && !s.isEmpty()) {
                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            }
        });
//        model.response_list.observe(this, rows -> {
//            adapter.update(rows);
//        });

        model.isInterest.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isInterest) {
                if (isInterest) {
                    SharedPrefreances.setSharedPreferenceString(InterestActivity.this, "ch_new", "0");
                    SharedPrefreances.setSharedPreferenceBoolean(InterestActivity.this, SharedPrefreances.IS_INTEREST, true);
                    SocialPrefrences.setisInterest(InterestActivity.this, true);
                    SocialPrefrences.setisLogin(InterestActivity.this, true);
                    Intent intent = new Intent(InterestActivity.this, MedleyOrMeest.class);
                    startActivity(intent);

                }

            }
        });

        model.adapter.setOnRecyclerViewItemClick((data, position, binding1) -> {
            if (data.isSelected()) {
                data.setSelected(false);
                Constant.array_list_.remove(data.getId());
                binding1.mainBg.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.white));
                binding1.textDone.setTextColor(binding.getRoot().getContext().getColor(R.color.greyTextColor));

            } else {
                data.setSelected(true);
                binding1.mainBg.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.social_background_blue));
                binding1.textDone.setTextColor(binding.getRoot().getContext().getColor(R.color.white));
                Constant.array_list_.add(data.getId());


            }


            //    model.adapter.setSelectedItem(position);
//            if (Constant.array_list_.contains(data.getId())) {
//                Constant.array_list_.remove(data.getId());
//                 //   holder.main_bg.setBackgroundResource(R.drawable.custom_bg_backgorund);
//                binding1.mainBg.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.white));
//                binding1.textDone.setTextColor(binding.getRoot().getContext().getColor(R.color.greyTextColor));
//                data.setSelected(false);
//            } else {
//                Constant.array_list_.add(data.getId());
//                binding1.mainBg.setBackgroundColor(ContextCompat.getColor(binding.getRoot().getContext(), R.color.social_background_blue));
//                binding1.textDone.setTextColor(binding.getRoot().getContext().getColor(R.color.white));
//                data.setSelected(true);
//            }
        });

    }

    private void initView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (ConnectionUtils.isConnected(this)) {
            model.chooseInterestApi();
        } else {
            // ConnectionUtils.showNoConnectionDialog(this, this);
        }

        //     adapter = new ChooseAdapterSocial();
//        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(this)
//                .setChildGravity(Gravity.CENTER)
//                .setScrollingEnabled(true)
//                .setOrientation(ChipsLayoutManager.HORIZONTAL)
//                .setGravityResolver(new IChildGravityResolver() {
//                    @Override
//                    public int getItemGravity(int position) {
//                        return Gravity.CENTER;
//                    }
//                })
//                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
//                .build();
//        binding.recyclerView.setLayoutManager(chipsLayoutManager);
        binding.interestBtn.setOnClickListener(v -> {
            showInterestItem();
        });
        binding.recyclerView.setLayoutManager(new GridLayoutManager(InterestActivity.this, 3));
    }

    private void showInterestItem() {
        if (Constant.array_list_.size() < 3) {
            Utilss.showToastSuccess(InterestActivity.this, getString(R.string.Please_select_three));
        } else {
            JSONArray array = new JSONArray();
            for (int i = 0; i < Constant.array_list_.size(); i++) {
                array.put(Constant.array_list_.get(i));
            }
            JSONObject obj = new JSONObject();
            try {
                // Add the JSONArray to the JSONObject
                obj.put("topic", array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String json = obj.toString();
            StringBuilder sbString = new StringBuilder("");

            for (String language : Constant.array_list_) {
                //append ArrayList element followed by comma
                sbString.append(language).append(",");
            }
            String strList = sbString.toString();
            //remove last comma from String if you want
            if (strList.length() > 0)
                strList = strList.substring(0, strList.length() - 1);
            model.selectedInterest(Constant.array_list_);
        }
    }

    @Override
    public void onItemClick(int position, TextView text_done) {
        {
            if (Constant.array_list_.size() < 3) {
                binding.interestBtn.setEnabled(true);
            } else {
                binding.interestBtn.setEnabled(false);
            }
        }
    }
}
