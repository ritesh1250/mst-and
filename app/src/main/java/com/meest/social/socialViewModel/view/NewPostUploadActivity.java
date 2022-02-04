package com.meest.social.socialViewModel.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationServices;
import com.meest.Adapters.RvAdapter;
import com.meest.Interfaces.FriendSelectCallback;
import com.meest.R;
import com.meest.databinding.NewPostUploadActivityBinding;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.networkcheck.OnNoInternetRetry;
import com.meest.responses.FeedResponse;
import com.meest.social.MyEdittext;
import com.meest.social.socialViewModel.view.createTextPost.CreateTextPost;
import com.meest.social.socialViewModel.viewModel.NewPostUploadViewModel;
import com.meest.social.socialViewModel.viewModel.createTextPostViewModel.CreateTextPostViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;




public class NewPostUploadActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, FriendSelectCallback, OnNoInternetRetry {

    private static final String TAG = "NewPostUploadActivity";
    String selectedLocation;
    private RecyclerView recyclerTagsPeople;
    NewPostUploadViewModel viewModel;
    NewPostUploadActivityBinding binding;

   // EmojiconEditText edWritePost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.new_post_upload_activity);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new NewPostUploadViewModel(this, binding)).createFor()).get(NewPostUploadViewModel.class);
        binding.setViewModel(viewModel);

        viewModel.getData();


        findIds();


        viewModel.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(NewPostUploadActivity.this);
        if (!viewModel.hasLocationPermission()) {
            viewModel.showLocationPermissionDialog();
        }
        if (viewModel.isStory) {
            binding.switchPost.setVisibility(View.GONE);
            binding.tvPost.setVisibility(View.GONE);
            binding.tvToolbar.setText(getResources().getString(R.string.create_story));
            binding.switchStory.setChecked(true);
            binding.switchPost.setChecked(false);
        } else {
            binding.tvToolbar.setText(getResources().getString(R.string.create_post));
            binding.switchPost.setVisibility(View.GONE);
            binding.tvPost.setVisibility(View.GONE);
            binding.switchStory.setChecked(false);
        }

        viewModel.setData();
        clickEvents();

        //  switch_location.setChecked(hasLocationPermission());
    }


    public void clickEvents() {
        binding.btPost.setOnClickListener(v -> {
            try {
                binding.btPost.setClickable(false);
                viewModel.hashtagList.clear();
                String strhashtag = binding.edWritePost.getText().toString().trim();
                if (strhashtag != null && strhashtag.length() > 0) {
                    viewModel.postText = URLEncoder.encode(binding.edWritePost.getText().toString().trim(), "UTF-8");

                    String[] words = strhashtag.split("\n");
                    for (String word : words) {
                        String[] spacewords = word.split(" ");
                        for (String sword : spacewords) {
                            if (sword.startsWith("#")) {
                                String[] hashwords = sword.split("#");
                                for (String hword : hashwords) {
                                    viewModel.hashtagList.add(hword);
                                }
                            }
                        }
                    }
                } else {
                    if (strhashtag.length() == 0) {
                        binding.edWritePost.setError(getString(R.string.Please_enter_caption));
                        binding.btPost.setClickable(true);
                        return;
                        //                            emojiconEditText.setText("");
//                            postText = URLEncoder.encode(emojiconEditText.getText().toString().trim(), "UTF-8");
                    }
                }
                viewModel.gotoNextScreen();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                viewModel.postText = binding.edWritePost.getText().toString();
            }

        });

        binding.imgBack.setOnClickListener(v -> finish());

        binding.switchMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewModel.allowComment = isChecked;
            }
        });


        binding.switchPost.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.isPost = isChecked;
        });
        binding.switchStory.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.isStory = isChecked;
        });



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.taggedUsername.clear();
        viewModel.taggedUsers.clear();
    }


    private void findIds() {
        recyclerTagsPeople = findViewById(R.id.recyclerTagsPeople);
        CreateTextPostViewModel.taggedUsername.clear();
        CreateTextPostViewModel.taggedUsers.clear();

    }


    @Override
    protected void onResume() {
        super.onResume();
        viewModel.taggedUsername.clear();
        viewModel.taggedUsers.clear();

        if (viewModel.hasLocationPermission()) {
            viewModel.getTagLocation();
        }
    }

    @Override
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        selectedLocation = (String) adapterView.getItemAtPosition(position);

        if (selectedLocation != null && selectedLocation.length() > 0) {
            binding.addLocation.setText(selectedLocation);

            binding.addLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.location_icon, 0, 0, 0);
        }

        if (viewModel.bottomSheetDialog != null) {
            viewModel.bottomSheetDialog.dismiss();
        }
    }


    @Override
    public void tagChanged() {
      CreateTextPostViewModel.taggedUsername.clear();
        CreateTextPostViewModel.taggedUsers.clear();
        if (viewModel.taggedUsername.size() > 0) {
            recyclerTagsPeople.setVisibility(View.VISIBLE);
            binding.ivTagAdd.setVisibility(View.VISIBLE);
//            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(NewPostUploadActivity.this, LinearLayoutManager.HORIZONTAL, false);
//            recyclerTagsPeople.setLayoutManager(horizontalLayoutManagaer);
            recyclerTagsPeople.setAdapter(new RvAdapter(this, (ArrayList<FeedResponse.UserTags>) viewModel.taggedUsername, (model, pos) -> {
                viewModel.taggedUsername.remove(model);

                if (viewModel.taggedUsers.size() > 0) {
                    viewModel.taggedUsers.remove(pos);
                }

            }));
        } else {
            binding.ivTagAdd.setVisibility(View.GONE);
            recyclerTagsPeople.setVisibility(View.GONE);
        }


        // tag_people.setText(taggedData);
    }

    @Override
    public void onRetry() {
        if (ConnectionUtils.isConnected(NewPostUploadActivity.this)) {

        } else {
            ConnectionUtils.showNoConnectionDialog(NewPostUploadActivity.this, this::onRetry);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (data != null && data.getExtras() != null) {
                viewModel.postVisibility = data.getExtras().getString("postVisibility");
                binding.tvPostVisibility.setText(viewModel.postVisibility);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == viewModel.PERMISSION_ID) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.requestNewLocationData();

            } else {
                Toast.makeText(this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();
            }

        }


    }
}
