package com.meest.social.socialViewModel.view.otherUserAccount;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.meest.Activities.base.ApiCallActivity;
import com.meest.R;
import com.meest.databinding.OtherUserAccountModelBinding;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.metme.ChatBoatActivity;
//import com.meest.responses.CheckChatHeadNFollowResponse;
import com.meest.social.socialViewModel.adapter.OtherUserPagerAdapter;
import com.meest.social.socialViewModel.model.CheckChatHeadNFollowResponse1;
import com.meest.social.socialViewModel.viewModel.otherUserAccountViewModel.OtherUserAccountViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.greenrobot.eventbus.Subscribe;

public class OtherUserAccount extends ApiCallActivity {

    OtherUserAccountViewModel viewModel;
    OtherUserAccountModelBinding binding;
    private static final String TAG = "OtherUserAccount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.other_user_account_model);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new OtherUserAccountViewModel(this, OtherUserAccount.this, binding)).createFor()).get(OtherUserAccountViewModel.class);
        binding.setOtherUserAccountViewModel(viewModel);
        Observer();
        clickEvents();
        if (getIntent().getExtras() == null) {
            Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            finish();
        }
        viewModel.inits();
        viewPagerFunc();

        binding.pager.setAdapter(new OtherUserPagerAdapter(getSupportFragmentManager(),this,viewModel.userId));
        //binding.pager.setAdapter(new OtherUserPagerAdapter(getSupportFragmentManager(), getLifecycle(), this, viewModel.userId));
    }

    private void clickEvents() {
        binding.imgBackArroeProfile.setOnClickListener(view -> onBackPressed());
        binding.layoutGrid.setOnClickListener(v -> {
//            pushFragment(new OtherFriendPhotoGridFrag(context, userId), "");
            binding.pager.setCurrentItem(0);
            binding.imgList.setColorFilter(ContextCompat.getColor(this, R.color.profile_gray));
            binding.imgGrid.setImageResource(R.drawable.ic_selected_grid);
            binding.imgVideo.setImageResource(R.drawable.ic_unselected_videos);
            binding.imgCamera.setImageResource(R.drawable.ic_unselected_tag);
            binding.pagerParent.setBackgroundResource(R.drawable.ic_path_1);
            binding.layoutGridLine.setVisibility(View.VISIBLE);
            binding.layoutVideoLine.setVisibility(View.INVISIBLE);
            binding.layoutCameraLine.setVisibility(View.INVISIBLE);
        });
        binding.layoutCamera.setOnClickListener(v -> {
//            pushFragment(new OtherUserCameraFrag(context, userId), "");
            binding.pager.setCurrentItem(1);
            binding.imgList.setColorFilter(ContextCompat.getColor(this, R.color.profile_gray));
            binding.imgGrid.setImageResource(R.drawable.ic_unselected_grid);
            binding.imgVideo.setImageResource(R.drawable.ic_unselected_videos);
            binding.imgCamera.setImageResource(R.drawable.ic_selected_tag);
            binding.pagerParent.setBackgroundResource(R.drawable.ic_path_2);
            binding.layoutGridLine.setVisibility(View.INVISIBLE);
            binding.layoutVideoLine.setVisibility(View.INVISIBLE);
            binding.layoutCameraLine.setVisibility(View.VISIBLE);
        });
        binding.layoutVideo.setOnClickListener(v -> {
//            pushFragment(new OtherUserProfileVideoFrag(context, userId), "");
            binding.pager.setCurrentItem(2);
            binding.imgList.setColorFilter(ContextCompat.getColor(this, R.color.profile_gray));
            binding.imgGrid.setImageResource(R.drawable.ic_unselected_grid);
            binding.imgVideo.setImageResource(R.drawable.ic_selected_videos);
            binding.imgCamera.setImageResource(R.drawable.ic_unselected_tag);
            binding.pagerParent.setBackgroundResource(R.drawable.ic_path_3);
            binding.layoutGridLine.setVisibility(View.INVISIBLE);
            binding.layoutVideo.setVisibility(View.VISIBLE);
            binding.layoutCameraLine.setVisibility(View.INVISIBLE);
        });
    }

    private void viewPagerFunc() {
        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                switch (position) {
                    case 0:
                        binding.imgList.setColorFilter(ContextCompat.getColor(OtherUserAccount.this, R.color.profile_gray));
                        binding.imgGrid.setImageResource(R.drawable.ic_selected_grid);
                        binding.imgVideo.setImageResource(R.drawable.ic_unselected_videos);
                        binding.imgCamera.setImageResource(R.drawable.ic_unselected_tag);
                        binding.pagerParent.setBackgroundResource(R.drawable.ic_path_1);
                        binding.layoutGridLine.setVisibility(View.VISIBLE);
                        binding.layoutCameraLine.setVisibility(View.INVISIBLE);
                        binding.layoutVideoLine.setVisibility(View.INVISIBLE);
                        break;

                    case 1:
                        binding.imgList.setColorFilter(ContextCompat.getColor(OtherUserAccount.this, R.color.profile_gray));
                        binding.imgGrid.setImageResource(R.drawable.ic_unselected_grid);
                        binding.imgVideo.setImageResource(R.drawable.ic_unselected_videos);
                        binding.imgCamera.setImageResource(R.drawable.ic_selected_tag);
                        binding.pagerParent.setBackgroundResource(R.drawable.ic_path_2);
                        binding.layoutGridLine.setVisibility(View.INVISIBLE);
                        binding.layoutCameraLine.setVisibility(View.VISIBLE);
                        binding.layoutVideoLine.setVisibility(View.INVISIBLE);
                        break;

                    case 2:
                        binding.imgList.setColorFilter(ContextCompat.getColor(OtherUserAccount.this, R.color.profile_gray));
                        binding.imgGrid.setImageResource(R.drawable.ic_unselected_grid);
                        binding.imgVideo.setImageResource(R.drawable.ic_selected_videos);
                        binding.imgCamera.setImageResource(R.drawable.ic_unselected_tag);
                        binding.pagerParent.setBackgroundResource(R.drawable.ic_path_3);
                        binding.layoutGridLine.setVisibility(View.INVISIBLE);
                        binding.layoutCameraLine.setVisibility(View.INVISIBLE);
                        binding.layoutVideoLine.setVisibility(View.VISIBLE);
                        break;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        viewModel.BackPressed();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        viewModel.initSocket();
    }

    public void Observer() {
        viewModel.blockText.observe(this, s -> {
            binding.txtMsgBlock.setText(s);
        });
    }


    @Subscribe
    public void checkHeadNFollow(CheckChatHeadNFollowResponse1 response) {
        Log.e(TAG, "checkHeadNFollow: clicked");
        viewModel.isMessageButtonClicked = false;
        if (response.isSuccess()) {
            if (response.getData().getData().equals("following")) {
                viewModel.createChatHeadApi("", false, true);
//                dialogMessage();
            } else if (response.getData().getData().equals("chatHead")) {
                Intent intent = new Intent(this, ChatBoatActivity.class);
                intent.putExtra("storyId", getIntent().getStringExtra("storyId"));
                intent.putExtra("url", getIntent().getStringExtra("url"));
                intent.putExtra("type", getIntent().getStringExtra("type"));
                intent.putExtra("thumbnail", getIntent().getStringExtra("thumbnail"));
                intent.putExtra("user_id_to", viewModel.userId);
                intent.putExtra("chatHeadId", response.getData().getChatHeadId());
                intent.putExtra("pro_file_pic", viewModel.response.getDataUser().getDisplayPicture());
                intent.putExtra("from_push", "0");
                intent.putExtra("isSingle", true);
                intent.putExtra("demoid", viewModel.response.getDataUser().getId());
                intent.putExtra("user_id_to", viewModel.userId);
                intent.putExtra("firstName", viewModel.response.getDataUser().getFirstName());
                intent.putExtra("lastName", viewModel.response.getDataUser().getLastName());
                intent.putExtra("username", viewModel.response.getDataUser().getUsername());
                intent.putExtra("profilePicture", viewModel.response.getDataUser().getDisplayPicture());
                startActivity(intent);
            } else {
                viewModel.dialogMessage(response.getData().getBlock());
            }
        } else {
            viewModel.dialogMessage(response.getData().getBlock());
//            dialogFailed(response.getErrorMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.mSocket.disconnect();
    }
}