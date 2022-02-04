package com.meest.social.socialViewModel.viewModel.postVisibilitySettingViewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.databinding.PostVisibilitySettingModelBinding;

public class PostVisibilitySettingViewModel extends ViewModel {
    private Context context;
    private Activity activity;
    private PostVisibilitySettingModelBinding binding;
    public String selectedVisibility;

    public PostVisibilitySettingViewModel(Context context, Activity activity, PostVisibilitySettingModelBinding binding) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;
    }

    public void getData(){
        if (activity.getIntent().getExtras() != null) {
            selectedVisibility = activity.getIntent().getExtras().getString("postVisibility", context.getString(R.string.Public));
            // setting unselected to all radio buttons
            binding.vpvsPublic.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cheakbox_uncheak, 0);
            binding.vpvsPrivate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cheakbox_uncheak, 0);
            if (selectedVisibility.equalsIgnoreCase("public")) {
                // selecting one on the basis of selected one
                binding.vpvsPublic.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_blue, 0);
            }  else if (selectedVisibility.equalsIgnoreCase(context.getString(R.string.Private))) {
                // selecting one on the basis of selected one
                binding.vpvsPrivate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_blue, 0);
            }
        }
    }

    public void radioGroupCheckChange(int checkedId) {
        if (checkedId == R.id.vpvs_public) {
            // adding icon manually
            binding.vpvsPublic.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_blue, 0);
            binding.vpvsPrivate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cheakbox_uncheak, 0);
            selectedVisibility = context.getString(R.string.Public);
        } else  {
            // adding icon manually
            binding.vpvsPrivate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_blue, 0);
            binding.vpvsPublic.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cheakbox_uncheak, 0);
            selectedVisibility = context.getString(R.string.Private);
        }
    }

    public void backPressed() {
        // sending result
        Intent intent=new Intent();
        intent.putExtra("postVisibility", selectedVisibility);
        activity.setResult(123, intent);
       activity.finish();
    }
}
