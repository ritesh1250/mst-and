package com.meest.meestcamera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.meest.Activities.WriteTextActivity;
import com.meest.R;
import com.meest.Services.GalUpload.StartAllService;
import com.meest.social.socialViewModel.view.createTextPost.CreateTextPost;
import com.meest.utils.Permission;
import com.meest.databinding.ActivityMeestMainCameraBinding;
import com.meest.meestcamera.fragment.LiveCameraFragment;
import com.meest.meestcamera.fragment.PhotosFragment;
import com.meest.meestcamera.fragment.StoryGalleryFragment;
import com.meest.meestcamera.fragment.VideosFragment;

public class MeestMainCameraActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MeestMainCameraActivity";

    private Fragment fragment;
    private ActivityMeestMainCameraBinding mainCameraBinding;
    private boolean switchToVideo = false;
    private static int currentSelection = -1;
    public static final int STORAGE_PERMISSION_CODE = 127;
    public static final String CALL_FEED = "feed";
    public static final String CALL_STORY = "story";
    public static String CALL_TYPE = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainCameraBinding = DataBindingUtil.setContentView(this, R.layout.activity_meest_main_camera);
        if (getIntent().hasExtra("call_type")) {

            if (getIntent().getStringExtra("call_type").equalsIgnoreCase(CALL_STORY)) {
                currentSelection = 2;
                CALL_TYPE = CALL_STORY;
                mainCameraBinding.tvStory.setVisibility(View.VISIBLE);
                mainCameraBinding.tvTextTitle.setVisibility(View.GONE);

            } else {
                currentSelection = 1;
                CALL_TYPE = CALL_FEED;
                mainCameraBinding.tvCreateText.setOnClickListener(null);
                mainCameraBinding.tvCamera.setVisibility(View.VISIBLE);
            }
        }

        Log.e(TAG, "call_type: "+CALL_TYPE );

        fragment = PhotosFragment.newInstance(switchToVideo, CALL_TYPE);
//        callingData();

        loadFragment(fragment);

        changeColor(currentSelection);
        initViewListeners();

    }

    private void initViewListeners() {
        mainCameraBinding.ivSwitchVideo.setOnClickListener(this);
        mainCameraBinding.tvCamera.setOnClickListener(this);
        mainCameraBinding.tvLive.setOnClickListener(this);
        mainCameraBinding.tvStory.setOnClickListener(this);

        mainCameraBinding.tvCreateText.setOnClickListener(this);
        mainCameraBinding.tvTextTitle.setOnClickListener(this);
        mainCameraBinding.linearCreate.setOnClickListener(this);


    }

    public void hidegone(Boolean status) {
        if (status) {
            mainCameraBinding.linearCreate.setVisibility(View.GONE);
            mainCameraBinding.ivSwitchVideo.setVisibility(View.GONE);
            mainCameraBinding.linearLayout.setVisibility(View.GONE);
        } else {
//            mainCameraBinding.linearCreate.setVisibility(View.VISIBLE);
            mainCameraBinding.ivSwitchVideo.setVisibility(View.VISIBLE);
            mainCameraBinding.linearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void changeColor(int switchPosition) {
        switch (switchPosition) {
            case 1: {


                mainCameraBinding.tvCamera.setTextColor(getResources().getColor(R.color.white, getTheme()));
                mainCameraBinding.tvStory.setTextColor(getResources().getColor(R.color.gray, getTheme()));
                mainCameraBinding.tvLive.setTextColor(getResources().getColor(R.color.gray, getTheme()));
//                mainCameraBinding.linearCreate.setVisibility(View.VISIBLE);

                currentSelection = 1;

                break;
            }

            case 2: {

                mainCameraBinding.tvCamera.setTextColor(getResources().getColor(R.color.gray, getTheme()));
                mainCameraBinding.tvStory.setTextColor(getResources().getColor(R.color.white, getTheme()));
                mainCameraBinding.tvLive.setTextColor(getResources().getColor(R.color.gray, getTheme()));
//                mainCameraBinding.linearCreate.setVisibility(View.VISIBLE);
                currentSelection = 2;
                break;
            }

            case 3: {
                mainCameraBinding.tvCamera.setTextColor(getResources().getColor(R.color.gray, getTheme()));
                mainCameraBinding.tvStory.setTextColor(getResources().getColor(R.color.gray, getTheme()));
                mainCameraBinding.tvLive.setTextColor(getResources().getColor(R.color.white, getTheme()));
//                mainCameraBinding.linearCreate.setVisibility(View.GONE);
                currentSelection = 3;

                break;
            }
        }


    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
//        transaction.addToBackStack(fragment.getClass().getName());
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void callingData() {
        Intent callData = new Intent(MeestMainCameraActivity.this, StartAllService.class);
        startService(callData);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_switchVideo: {
                if (currentSelection == 1)
                    CALL_TYPE = CALL_FEED;
                else
                    CALL_TYPE = CALL_STORY;

                ImageView view = (ImageView) v;

                if (!switchToVideo) {
                    switchToVideo = true;
                    view.setImageResource(R.drawable.camera_bottom);
                } else {
                    switchToVideo = false;
                    view.setImageResource(R.drawable.recorder_new);
                }
                fragment = PhotosFragment.newInstance(switchToVideo, CALL_TYPE);
                loadFragment(fragment);
                break;
            }

            case R.id.tvCamera: {
                changeColor(1);
                currentSelection = 1;
                CALL_TYPE = CALL_FEED;
                fragment = PhotosFragment.newInstance(switchToVideo, CALL_TYPE);
                loadFragment(fragment);
                changeColor(currentSelection);
                break;
            }

            case R.id.tvStory: {
                changeColor(2);
                currentSelection = 2;
                CALL_TYPE = CALL_STORY;
                fragment = PhotosFragment.newInstance(switchToVideo, CALL_TYPE);
                loadFragment(fragment);
                changeColor(currentSelection);
                break;
            }

            case R.id.tvLive: {
                changeColor(3);
                break;
            }

            case R.id.tvCreateText:
                if(CALL_TYPE.equalsIgnoreCase("post")){
                 mainCameraBinding.tvCreateText.setOnClickListener(null);
                }else {
                    startActivity(new Intent(MeestMainCameraActivity.this, WriteTextActivity.class));
                }
            case R.id.tvTextTitle:
            case R.id.linearCreate: {

                if (ContextCompat.checkSelfPermission(MeestMainCameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MeestMainCameraActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                } else {
                    ActivityCompat.requestPermissions(MeestMainCameraActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                    //Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }

                break;
            }


        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new PhotosFragment();
                    break;
                case 1:
                    fragment = new VideosFragment();
                    break;
                case 2:
                    fragment = new StoryGalleryFragment();
                    break;
                case 3:
                    fragment = new LiveCameraFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Photo";
                case 1:
                    return "Video";
                case 2:
                    return "Story";
                case 3:
                    return "Live";
            }
            return null;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (Permission.hasPermissions(MeestMainCameraActivity.this, permissions)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MeestMainCameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        if (currentSelection == 1) {
//                            Intent intent = new Intent(MeestMainCameraActivity.this, CreateTextPostActivity.class);
                            Intent intent = new Intent(MeestMainCameraActivity.this, CreateTextPost.class);

                            startActivity(intent);
                        }
                    }
                } else {
                    Toast.makeText(this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            fm.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        } else {
            finish();
        }
        Log.e("popping BACKSTRACK===> ", "" + fm.getBackStackEntryCount());
    }
}