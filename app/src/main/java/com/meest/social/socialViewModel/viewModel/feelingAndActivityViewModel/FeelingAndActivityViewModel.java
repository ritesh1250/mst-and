package com.meest.social.socialViewModel.viewModel.feelingAndActivityViewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModel;

import com.meest.responses.FeelingResponse;
import com.meest.databinding.FeelingAndActivityModelBinding;
import com.meest.meestbhart.utilss.Constant;

import java.util.ArrayList;

public class FeelingAndActivityViewModel extends ViewModel {
    private static final String TAG = "FeelingAndActivityViewM";
    public ArrayList<String> data1 = new ArrayList<>();
    public ArrayList<String> imageUrls1 = new ArrayList<>();
    public FeelingResponse feelingResponse;
    public String activityId = null, subActivityId = null, activityTitle = null, subActivityTitle = null;
    public Bundle bundle = new Bundle();
    private Context context;
    private Activity activity;
    private FeelingAndActivityModelBinding binding;


    public FeelingAndActivityViewModel(Context context, Activity activity, FeelingAndActivityModelBinding binding) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;
    }

    public void feelActDoneFunc() {
        Intent intent = new Intent();
        intent.putExtra("feeling_ID", Constant.selectedFeelingID);
        intent.putExtra("sub_feeling_ID", Constant.selectedActivityID);
        intent.putExtra("feelingTitle", Constant.selectedFeelingTitle);
        intent.putExtra("subFeelingTitle", Constant.selectedActivityTitle);
        if(!(Constant.selectedFeelingID.equals("")) || !(Constant.selectedActivityID.equals(""))){
            intent.putExtra("isFeelingType", true);
        }
        else {
            intent.putExtra("isFeelingType", false);
        }
//            startActivity(intent);
        activity.setResult(Constant.REQUEST_FOR_FEELING, intent);
        activity.finish();
    }



}
