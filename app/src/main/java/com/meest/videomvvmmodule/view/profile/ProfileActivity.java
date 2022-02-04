package com.meest.videomvvmmodule.view.profile;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.gson.Gson;
import com.meest.R;
import com.meest.databinding.ActivityProfileBinding;
import com.meest.videomvvmmodule.adapter.ProfileVideoPagerAdapter;
import com.meest.videomvvmmodule.model.user.User;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.home.ReportSheetFragment;
import com.meest.videomvvmmodule.viewmodel.MainViewModel;
import com.meest.videomvvmmodule.viewmodel.ProfileViewModel;

import java.util.Calendar;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;

public class ProfileActivity extends BaseActivity {

    public ProfileViewModel viewModel;
    private MainViewModel parentViewModel;
    private ActivityProfileBinding binding;

    ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            viewModel.user.setValue(new Gson().fromJson(result.getData().getStringExtra("user"), User.class));
                            binding.setViewModel(viewModel);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        parentViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        initView();
        initObserver();
        initListener();

        viewModel.sessionManager = sessionManager;
        binding.setViewModel(viewModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.fetchUserById(viewModel.userId);
        ProfileVideoPagerAdapter adapter = new ProfileVideoPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, binding.tvViewCount, binding.tvPostCount);
        binding.viewPager.setAdapter(adapter);
    }

    private void createDynamikLink(String userId) {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.meest4bharat.com/?user=" + userId))
                .setDomainUriPrefix(Const.deepLinkingUriPrefix)
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder(Const.iosBundleId).build())
                .buildShortDynamicLink()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        String shareBody = getString(R.string.Watch_more_of_such) + task.getResult().getShortLink();
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Share_Profile));
                        share.putExtra(Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(share, getString(R.string.Share_Profile)));
                    } else {
                        Toast.makeText(ProfileActivity.this, getString(R.string.Error_Creating_Link), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void initView() {

        Intent intent = getIntent();
        if (intent != null) {
            String userId = intent.getStringExtra("userid");
            if (userId != null && !userId.equals(Global.userId)) {
                viewModel.isMyAccount.set(-1);
            }
            viewModel.fetchUserById(intent.getStringExtra("userid"));
            viewModel.userId = intent.getStringExtra("userid");
            viewModel.isBackBtn.set(true);
        }

        ProfileVideoPagerAdapter adapter = new ProfileVideoPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, binding.tvViewCount, binding.tvPostCount);
        binding.viewPager.setAdapter(adapter);
    }

    private void initObserver() {

        binding.shareProfileButton.setOnClickListener(view -> createDynamikLink(viewModel.userId));
        CustomDialogBuilder customDialogBuilder = new CustomDialogBuilder(ProfileActivity.this);

        viewModel.isloading.observe(this, isLoading -> {
            if (isLoading) {
                customDialogBuilder.showLoadingDialog();
            } else {
                customDialogBuilder.hideLoadingDialog();
            }
        });

        parentViewModel.selectedPosition.observe(this, position -> {
            if (position != null && position == 3) {
                viewModel.selectPosition.setValue(position);
                if (sessionManager.getUser() != null) {
                    viewModel.user.setValue(sessionManager.getUser());
                    viewModel.fetchUserById(sessionManager.getUser().getData().getUserId());
                    viewModel.userId = sessionManager.getUser().getData().getUserId();
                    viewModel.isBackBtn.set(false);
                    binding.appBar.setExpanded(true);
                }
            }
        });

        viewModel.onItemClick.observe(this, type -> {
            if (type != null) {
                switch (type) {
                    // On option menu click
                    case 0:
                        if (viewModel.isMyAccount.get() == 1 || viewModel.isMyAccount.get() == 2) {
                            // other user profile
                            showPopMenu();
                        } else {
                            // my profile
                            startActivity(new Intent(ProfileActivity.this, SettingsVideoActivity.class));
                        }
                        break;
                    // On Follow, UnFollow, edit btn click
                    case 1:
                        handleButtonClick();
                        break;
                    // Back btn
                    case 2:
                        if (ProfileActivity.this != null) {
                            ProfileActivity.this.onBackPressed();
                        }
                        break;
                    // On My videos tab click
                    case 3:
                        viewModel.isLikedVideos.set(false);
                        binding.viewPager.setCurrentItem(0);
                        break;
                    // On liked videos click
                    case 4:
                        viewModel.isLikedVideos.set(true);
                        binding.viewPager.setCurrentItem(1);
                        break;
                    // On Followers Click
                    case 5:
                        handleFollowerClick(0);
                        break;
                    // On Following Click
                    case 6:
                        handleFollowerClick(1);
                        break;
                }
                viewModel.onItemClick.setValue(null);
            }
        });

        viewModel.intent.observe(this, intent -> {
            if (intent != null) {
                try {
                    startActivity(intent);
                } catch (Exception ignored) {
                }
            }
        });

        viewModel.user.observe(this, user -> {
            if (user != null) {
                binding.setViewModel(viewModel);
                if (user.getData().getUserProfile().equals("") || user.getData().getUserProfile() == null) {
                    binding.imgUser.setImageResource(R.drawable.ic_edit_profile_img);
                }
//                if (user.getData().getUserProfile().equals("") || user.getData().getUserProfile() == null) {
//                    binding.profileBanner.setImageResource(R.drawable.ic_card_placeholder_bg);
//                }
            }
//            if (user != null && !TextUtils.isEmpty(user.getData().getProfileCategoryName())) {
//                //binding.tvProfileCategory.setVisibility(View.VISIBLE);
//            }
        });
        viewModel.followApi.observe(this, checkUsername -> {
            if (viewModel.user.getValue() != null) {
                if (viewModel.isMyAccount.get() == 1) {
                    viewModel.user.getValue().getData().setFollowersCount(viewModel.user.getValue().getData().getFollowersCount() + 1);
                } else {
                    viewModel.user.getValue().getData().setFollowersCount(viewModel.user.getValue().getData().getFollowersCount() - 1);
                }
                binding.tvFansCount.setText(Global.prettyCount(viewModel.user.getValue().getData().getFollowersCount()));
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    private void showPopMenu() {
        if (ProfileActivity.this != null) {
            PopupMenu popupMenu = new PopupMenu(ProfileActivity.this, binding.imgOption);
            popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.share:
                        shareProfile();
                        return true;
                    case R.id.report:
                        ReportSheetFragment fragment = new ReportSheetFragment();
                        Bundle args = new Bundle();
                        args.putString("userid", viewModel.userId);
                        args.putInt("reporttype", 0);
                        args.putString("userid", Global.userId);
                        fragment.setArguments(args);
                        fragment.show(getSupportFragmentManager(), fragment.getClass().getSimpleName());
                        return true;

                }
                return false;
            });
            popupMenu.show();
        }
    }

    private void shareProfile() {
        if (ProfileActivity.this != null && viewModel.user.getValue() != null) {
            String json = new Gson().toJson(viewModel.user.getValue());
            String title = viewModel.user.getValue().getData().getUserEmail();

            BranchUniversalObject buo = new BranchUniversalObject()
                    .setCanonicalIdentifier("content/12345")
                    .setTitle(title)
                    .setContentImageUrl(viewModel.user.getValue().getData().getUserProfile())
//                    .setContentImageUrl(Const.ITEM_BASE_URL + viewModel.user.getValue().getData().getUserProfile())
                    .setContentDescription(getString(R.string.Hey_There_Check_This_Meest_Profile))
                    .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    .setContentMetadata(new ContentMetadata().addCustomMetadata("data", json));

            LinkProperties lp = new LinkProperties()
                    .setFeature(getString(R.string.sharing))
                    .setCampaign(getString(R.string.Content_launch))
                    .setStage("User")
                    .addControlParameter("custom", "data")
                    .addControlParameter("custom_random", Long.toString(Calendar.getInstance().getTimeInMillis()));

            buo.generateShortUrl(ProfileActivity.this, lp, (url, error) -> {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                String shareBody = url + getString(R.string.This_Profile_Is_Amazing_On_Meest_App);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Profile Share");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Share Profile"));
            });
        }
    }

    private void handleFollowerClick(int itemType) {

        Intent intent = new Intent(ProfileActivity.this, FollowerFollowingActivity.class);
        intent.putExtra("itemtype", itemType);
        intent.putExtra("userid", viewModel.userId);
        intent.putExtra("user", new Gson().toJson(viewModel.user.getValue()));
        startActivity(intent);
    }

    private void handleButtonClick() {

        if (sessionManager.getBooleanValue(Const.IS_LOGIN)) {
            if (viewModel.isMyAccount.get() == 1 || viewModel.isMyAccount.get() == 2) {
                if (viewModel.isMyAccount.get() == 1) {
                    final Dialog dialog = new Dialog(ProfileActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.custom_unfollow_layout);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    ImageView imageView = dialog.findViewById(R.id.unFollowImage);
                    TextView tvNamePeople = dialog.findViewById(R.id.tvNamePeople);
                    TextView tvYes = dialog.findViewById(R.id.tvYes);
                    TextView tvNo = dialog.findViewById(R.id.tvNo);
                    tvNamePeople.setText(String.format("%s %s", viewModel.user.getValue().getData().getFirstName(), viewModel.user.getValue().getData().getLastName()));
                    Glide.with(ProfileActivity.this)
                            .load(viewModel.user.getValue().getData().getUserProfile())
                            .placeholder(R.drawable.ic_edit_profile_img)
                            .into(imageView);
                    dialog.show();
                    tvYes.setOnClickListener(v -> {
                        viewModel.followUnfollow(ProfileActivity.this);
                        dialog.dismiss();
                    });
                    tvNo.setOnClickListener(v -> dialog.dismiss());
                } else {
                    viewModel.followUnfollow(ProfileActivity.this);
                }
            } else {
                editProfileLauncher.launch(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        } else {
            Toast.makeText(ProfileActivity.this, getString(R.string.You_have_to_login_first), Toast.LENGTH_SHORT).show();
        }
    }

    private void initListener() {
        binding.btnEditVideoProfile.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class)));

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewModel.isLikedVideos.set(position == 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}