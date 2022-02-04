package com.meest.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.meest.Activities.VideoPost.VideoPostVisibilitySetting;
import com.meest.Adapters.FriendsListAdapter;
import com.meest.Adapters.RvAdapter;
import com.meest.Interfaces.FriendSelectCallback;
import com.meest.MainActivity;
import com.meest.R;
import com.meest.responses.FeedResponse;
import com.meest.responses.FriendsListResponse;
import com.meest.utils.goLiveUtils.utils.MeestLoaderDialog;
import com.meest.mediapikcer.utils.PictureFacer;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.networkcheck.OnNoInternetRetry;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class NewPostUploadActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, FriendSelectCallback, OnNoInternetRetry {

    private static final String TAG = "NewPostUploadActivity";

    private static final int COMPRESS_VIDEO_CODE = 712;
    private static final int CREATE_THUMB = 232;
    int PERMISSION_ID = 44;
    int recode = 0;
    String postVisibility;
    ImageView img_profile, img_back, ivTagAdd;
    //EmojiconEditText emojiconEditText;
    TextView tag_people, add_location, tv_toolbar;
    Button btPost;
    String mediaPath;
    ArrayList<String> multipleMedia;
    boolean isVideo = false, isMultiple;
    SwitchCompat switch_map, switch_location, switch_sensitive, switch_story, switch_post;
    String postText;
    String address_name;
    String mediaUrl;
    String selectedLocation;
    boolean allowComment = true;

    LottieAnimationView loading;
    private MeestLoaderDialog meestLoaderDialog;
    List<PictureFacer> selectedItem;
    ArrayList<String> hashtagList = new ArrayList<>();
    FriendsListResponse friendsListResponse;
    public static List<String> taggedUsers = new ArrayList<>();
    public static List<FeedResponse.UserTags> taggedUsername = new ArrayList<>();
    String postType, type;
    TextView tvPost_visibility, tv_post;
    File imageFile;
    String thumbnailImage, compressedVideo;
    RelativeLayout view_layout_cat;
    AutoCompleteTextView tvAutoComplete;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private BottomSheetDialog bottomSheetDialog;
    private static final String API_KEY = "AIzaSyDfcsomOyYAs3BXBUD58CpE0WulwtSz8Ms";
    private boolean isCompressed = false, isThumb = false;
    private String latitude, longitude;
    private boolean isStory = false, isPost = false;

    private Double post_lat = 0.0d;
    private Double post_long = 0.0d;
    private RecyclerView recyclerTagsPeople;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private AlertDialog alert_loc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_post_upload_activity);
        meestLoaderDialog = new MeestLoaderDialog(NewPostUploadActivity.this);
        postVisibility = getString(R.string.Public);
        if (getIntent().getExtras() == null) {
            Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            finish();
        }
        isMultiple = getIntent().getExtras().getBoolean("isMultiple", false);
        if (isMultiple) {
            String resul = getIntent().getStringExtra("multipleMedia");
            Gson gson = new Gson();
            Type type = new TypeToken<List<PictureFacer>>() {
            }.getType();
            selectedItem = gson.fromJson(resul, type);
            recode = getIntent().getExtras().getInt("recode");
        }
        mediaPath = getIntent().getExtras().getString("mediaPath");
        thumbnailImage = getIntent().getExtras().getString("thumbPath");
        isVideo = getIntent().getExtras().getBoolean("isVideo", false);
        type = getIntent().getStringExtra("type");
        isStory = getIntent().getExtras().getBoolean("isStory", false);
        findIds();


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(NewPostUploadActivity.this);
        if (!hasLocationPermission()) {
            showLocationPermissionDialog();
        }
        if (isStory) {
            switch_post.setVisibility(View.GONE);
            tv_post.setVisibility(View.GONE);
            tv_toolbar.setText(getResources().getString(R.string.create_story));
            switch_story.setChecked(true);
            switch_post.setChecked(false);
        } else {
            tv_toolbar.setText(getResources().getString(R.string.create_post));
            switch_post.setVisibility(View.GONE);
            tv_post.setVisibility(View.GONE);
            switch_story.setChecked(false);
        }

        imageFile = new File(mediaPath);
        if (type != null && type.equalsIgnoreCase("location")) {
            Glide.with(this).load(mediaPath).into(img_profile);
            if (type.equalsIgnoreCase("location")) {
                latitude = getIntent().getStringExtra("lat");
                longitude = getIntent().getStringExtra("log");
                address_name = getIntent().getStringExtra("address_name");
            }

//            add_location.setText(CommonUtils.getAddress(this, Double.parseDouble(latitude), Double.parseDouble(longitude)));
            add_location.setText(address_name);
            add_location.setVisibility(View.VISIBLE);
        } else if (isVideo) {
            Uri uri = Uri.fromFile(imageFile);
            Glide.with(this).
                    load(uri).
                    thumbnail(0.1f).
                    into(img_profile);
        } else {
            Glide.with(this).load(imageFile).into(img_profile);
        }

        switch_map.setChecked(allowComment);


        loading.setAnimation("loading.json");
        loading.playAnimation();
        loading.loop(true);

//        btPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    btPost.setClickable(false);
//                    hashtagList.clear();
//                    String strhashtag = emojiconEditText.getText().toString().trim();
//                    if (strhashtag != null && strhashtag.length() > 0) {
//                        postText = URLEncoder.encode(emojiconEditText.getText().toString().trim(), "UTF-8");
//
//                        String[] words = strhashtag.split("\n");
//                        for (String word : words) {
//                            String[] spacewords = word.split(" ");
//                            for (String sword : spacewords) {
//                                if (sword.startsWith("#")) {
//                                    String[] hashwords = sword.split("#");
//                                    for (String hword : hashwords) {
//                                        hashtagList.add(hword);
//                                    }
//                                }
//                            }
//                        }
//                    } else {
//                        if (strhashtag.length() == 0) {
//                            emojiconEditText.setError(getString(R.string.Please_enter_caption));
//                            btPost.setClickable(true);
//                            return;
//                            //                            emojiconEditText.setText("");
////                            postText = URLEncoder.encode(emojiconEditText.getText().toString().trim(), "UTF-8");
//                        }
//                    }
//                        if (isStory) {
//                            Intent intent = new Intent(NewPostUploadActivity.this, StoryPostuploadService.class);
//                            intent.putExtra("storyMediaPath", mediaPath);
//                            intent.putExtra("isStoryVideo", isVideo);
//                            intent.putExtra("isStoryMultiple", isMultiple);
//                            intent.putExtra("storyPostText", postText);
//                            intent.putExtra("lat", latitude);
//                            intent.putExtra("log", longitude);
//                            intent.putExtra("post_lat", post_lat);
//                            intent.putExtra("post_long", post_long);
//                            intent.putExtra("address_name", address_name);
//                            intent.putExtra("taggedUsername", new Gson().toJson(taggedUsername));
//                            intent.putExtra("storyMultipleMedia", new Gson().toJson(selectedItem));
//                            intent.putExtra("storyHashtagList", hashtagList);
//                            intent.putExtra("allowStoryComment", allowComment);
//                            intent.putExtra("storyPostText", postText);
//                            intent.putExtra("storyThumbPath", thumbnailImage);
//                            intent.putExtra("storyRecode", recode);
//                            intent.putExtra("sensitiveContent", switch_sensitive.isChecked());
//                            startService(intent);
//                            Intent intent1 = new Intent(NewPostUploadActivity.this, MainActivity.class);
//                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent1);
//                            finish();
//                        }
//                        else {
//                            Intent intent3 = new Intent(NewPostUploadActivity.this, NewPostUploadService.class);
//                            intent3.putExtra("mediaPath", mediaPath);
//                            intent3.putExtra("isVideo", isVideo);
//                            intent3.putExtra("isStory", isStory);
//                            intent3.putExtra("isPost", isPost);
//                            intent3.putExtra("isMultiple", isMultiple);
//                            intent3.putExtra("type", type);
//                            intent3.putExtra("postVisibility", postVisibility);
//                            intent3.putExtra("lat", latitude);
//                            intent3.putExtra("log", longitude);
//                            intent3.putExtra("post_lat", post_lat);
//                            intent3.putExtra("post_long", post_long);
//                            intent3.putExtra("address_name", address_name);
//                            intent3.putExtra("taggedUsername", new Gson().toJson(taggedUsername));
//                            intent3.putExtra("multipleMedia", new Gson().toJson(selectedItem));
//                            intent3.putExtra("hashtagList", hashtagList);
//                            intent3.putExtra("allowComment", allowComment);
//                            intent3.putExtra("postText", postText);
//                            intent3.putExtra("thumbPath", thumbnailImage);
//                            intent3.putExtra("recode", recode);
//                            intent3.putExtra("sensitiveContent", switch_sensitive.isChecked());
//                            startService(intent3);
//
//                            Intent intent4 = new Intent(NewPostUploadActivity.this, MainActivity.class);
//                            intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent4);
//                            finish();
//                        }
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                    postText = emojiconEditText.getText().toString();
//                }
//
//            }
//        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        switch_map.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                allowComment = isChecked;
            }
        });
        switch_post.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isPost = isChecked;
        });
        switch_story.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isStory = isChecked;
        });


//        switch_location.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//
//                if (!hasLocationPermission() || !hasGpsEnabled()) {
//                    switch_location.setChecked(false);
//
//                    if (!hasLocationPermission()) {
//                        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 0);
//                    }
//                    if (hasLocationPermission()) {
//                        statusCheck();
//                    }
//
//                    showLocationPermissionDialog();
//                } else {
//
//                }
//            }
//        });

        tag_people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPeopleDialog();
            }
        });
        ivTagAdd.setOnClickListener(v -> {

            showPeopleDialog();
        });


        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Rect displayRectangle = new Rect();
//                Window window = NewPostUploadActivity.this.getWindow();
//                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
//
//                View view = getLayoutInflater().inflate(R.layout.fragment_layout_search, null);
//                view.setMinimumWidth((int) (displayRectangle.width() * 1f));
//                view.setMinimumHeight((int) ((displayRectangle.height() * 1f) / 1.5));
//
//                tvAutoComplete = view.findViewById(R.id.tvAutoComplete);
//                tvAutoComplete.setAdapter(new GooglePlacesAutocompleteAdapter(NewPostUploadActivity.this, R.layout.list_item));
//                tvAutoComplete.setOnItemClickListener(NewPostUploadActivity.this);
//                bottomSheetDialog = new BottomSheetDialog(NewPostUploadActivity.this);
//
//                bottomSheetDialog.setContentView(view);
//                bottomSheetDialog.show();
            }
        });


        view_layout_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewPostUploadActivity.this, VideoPostVisibilitySetting.class);
                intent.putExtra("postVisibility", postVisibility);

                startActivityForResult(intent, 123);
            }
        });
        //  switch_location.setChecked(hasLocationPermission());
    }

    private void showLocationPermissionDialog() {

        Dialog showLocation = new Dialog(this);
        showLocation.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(showLocation.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        showLocation.setContentView(R.layout.new_post_permission_dialog);
        showLocation.setCancelable(false);
        //  showLocation.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        showLocation.getWindow().setAttributes(lp);
        showLocation.show();
        Button enableLocation = showLocation.findViewById(R.id.enableLocation);
        enableLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  statusCheck();
                getTagLocation();
                showLocation.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        taggedUsername.clear();
        taggedUsers.clear();
    }


    private void getTagLocation() {
        if (ContextCompat.checkSelfPermission(NewPostUploadActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewPostUploadActivity.this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_ID);
        } else if (ContextCompat.checkSelfPermission(NewPostUploadActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
            requestNewLocationData();
        } else {
            Toast.makeText(NewPostUploadActivity.this, getString(R.string.Permission_Denied_Goto_settings_and_allow_permission), Toast.LENGTH_SHORT).show();
        }

    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(0);
            mLocationRequest.setFastestInterval(0);
            mLocationRequest.setNumUpdates(1);

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }

    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            if (switch_location.isChecked()) {
                post_lat = mLastLocation.getLatitude();
                post_long = mLastLocation.getLongitude();
            }
            Log.e("Lat Long", post_lat + "   " + post_long);
        }
    };

//    public void statusCheck() {
//        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            buildAlertMessageNoGps();
//        }
//    }

    public boolean hasGpsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.Your_GPS_seems_to_be_disabled_do_you_want_to_enable_it))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private void showPeopleDialog() {
        Rect displayRectangle = new Rect();
        Window window = NewPostUploadActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_layout_search, viewGroup, false);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));

        TextView dialog_title = dialogView.findViewById(R.id.dialog_title);
        ImageView dialog_back = dialogView.findViewById(R.id.dialog_back);
        RecyclerView dialog_recycler = dialogView.findViewById(R.id.dialog_recycler);
        // RecyclerView selected_recycler = dialogView.findViewById(R.id.selected_recycler);
        AutoCompleteTextView tvAutoComplete = dialogView.findViewById(R.id.tvAutoComplete);

        BottomSheetDialog tagDialog = new BottomSheetDialog(NewPostUploadActivity.this);
        final FriendsListAdapter[] friendsListAdapter = new FriendsListAdapter[1];

        dialog_recycler.setHasFixedSize(true);
        dialog_recycler.setLayoutManager(new LinearLayoutManager(NewPostUploadActivity.this));
        dialog_recycler.setVisibility(View.VISIBLE);


//        if (taggedUsername.size()>0)
//        {
//            selected_recycler.setVisibility(View.VISIBLE);
//            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(NewPostUploadActivity.this, LinearLayoutManager.HORIZONTAL, false);
//            selected_recycler.setLayoutManager(horizontalLayoutManagaer);
//            selected_recycler.setAdapter(new RvAdapter(this, (ArrayList<FeedResponse.UserTags>) taggedUsername, new RvAdapter.Onclick() {
//                @Override
//                public void onEvent(FeedResponse.UserTags model, int pos) {
//                    taggedUsername.remove(model);
//                }
//            }));
//        }
//        else
//        {
//            selected_recycler.setVisibility(View.GONE);
//        }

        tvAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    String text = tvAutoComplete.getText().toString().trim();
                    dialog_recycler.setVisibility(View.VISIBLE);

                    Map<String, String> header = new HashMap<>();
                    Map<String, String> body = new HashMap<>();

                    body.put("searchText", text);
                    header.put("x-token", SharedPrefreances.getSharedPreferenceString(NewPostUploadActivity.this,
                            SharedPrefreances.AUTH_TOKEN));

                    WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                    Call<FriendsListResponse> call = webApi.searchFriendsList(header, body);
                    call.enqueue(new Callback<FriendsListResponse>() {
                        @Override
                        public void onResponse(Call<FriendsListResponse> call, Response<FriendsListResponse> response) {
                            if (response.code() == 200 && response.body().getSuccess()) {


                                if (response.body() != null) {
                                    friendsListResponse = response.body();

//                                    if (taggedUsername.size() > 0) {
//                                        if (friendsListResponse.getData().getRows() != null && friendsListResponse.getData().getRows().size() > 0) {
//
//                                            for(  int j=0 ; j<friendsListResponse.getData().getRows().size();j++)
//                                            {
//                                                for (int i=0 ; i<taggedUsername.size(); i++)
//                                                {
//                                                    if (taggedUsername.get(i).getUsername().equals(friendsListResponse.getData().getRows().get(j).getUsername()))
//                                                    {
//
//                                                        //friendsListResponse.getData().getRows().get(j).se()
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }

                                    FriendsListAdapter friendsListAdapter1 = new FriendsListAdapter(NewPostUploadActivity.this, friendsListResponse, NewPostUploadActivity.this);
                                    dialog_recycler.setAdapter(friendsListAdapter1);

                                }

//                                if (friendsListResponse == null || friendsListAdapter[0] == null) {
//                                    friendsListResponse = response.body();
//                                    friendsListAdapter[0] = new FriendsListAdapter(NewPostUploadActivity.this, friendsListResponse, NewPostUploadActivity.this);
//                                    dialog_recycler.setAdapter(friendsListAdapter[0]);
//                                } else {
//                                    friendsListResponse = response.body();
//                                    friendsListAdapter[0].updateData(friendsListResponse);
//                                    friendsListAdapter[0].notifyDataSetChanged();
//                                }
                            } else {
                                Utilss.showToast(NewPostUploadActivity.this, getString(R.string.error_msg), R.color.grey);
                            }
                        }

                        @Override
                        public void onFailure(Call<FriendsListResponse> call, Throwable t) {
                            Utilss.showToast(NewPostUploadActivity.this, getString(R.string.error_msg), R.color.grey);
                        }

                    });
                } else {
                    dialog_recycler.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog_title.setText(getString(R.string.Select_Friends_to_tag_them));
        dialog_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagDialog.dismiss();
            }
        });

        tagDialog.setContentView(dialogView);
        tagDialog.show();
    }

    private void findIds() {
        tvPost_visibility = findViewById(R.id.tvPost_visibility);
        img_profile = findViewById(R.id.img_profile);
        tv_toolbar = findViewById(R.id.tv_toolbar);
        tv_post = findViewById(R.id.tv_post);
        //emojiconEditText = findViewById(R.id.edWritePost);
        tag_people = findViewById(R.id.tag_people);
        add_location = findViewById(R.id.add_location);
        btPost = findViewById(R.id.btPost);
        switch_map = findViewById(R.id.switch_map);
        switch_location = findViewById(R.id.switch_location);
        switch_sensitive = findViewById(R.id.switch_sensitive);
        loading = findViewById(R.id.loading);
        img_back = findViewById(R.id.img_back);
        ivTagAdd = findViewById(R.id.ivTagAdd);
        switch_story = findViewById(R.id.switch_story);
        switch_post = findViewById(R.id.switch_post);
        recyclerTagsPeople = findViewById(R.id.recyclerTagsPeople);
        view_layout_cat = findViewById(R.id.view_layout_cat);
        add_location.setVisibility(View.GONE);
    }

   /* private void uploadPost(String fileUri, boolean isVideo, boolean isThumb) {
        // uploading media first
        RequestBody requestFile;
        MultipartBody.Part body;
        File file = new File(fileUri);
        if (isVideo) {
            Log.e("Video upload start", fileUri);
            requestFile = RequestBody.create(MediaType.parse("video/*"), file);

            body = MultipartBody.Part.createFormData("video", file.getName(), requestFile);
        } else {
            requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }


        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<UploadimageResponse> call = webApi.uploadImageRequest(map, body);

        call.enqueue(new Callback<UploadimageResponse>() {
            @Override
            public void onResponse(Call<UploadimageResponse> call, Response<UploadimageResponse> response) {
                try {
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        if (isThumb) {
                            Log.e("thumbnailImage", "upload done");
                            thumbnailImage = response.body().getData().getUrl();

                            if (isMultiple) {
                                postNow();
                            } else if (isCompressed) {

                                uploadPost(mediaPath, true, false);
                            }
//                            else {
//                                uploadPost(mediaPath, true, false);
//                            }
                        } else {
                            mediaUrl = response.body().getData().getUrl();
                            Log.e("Video upload done", mediaUrl);
                            postNow();
                        }
                    } else {
                        meestLoaderDialog.hideDialog();
//                        loading.setVisibility(View.GONE);
                        mediaUrl = "";
                        Utilss.showToast(getApplicationContext(), SOME_ERROR, R.color.grey);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    meestLoaderDialog.hideDialog();
                    Utilss.showToast(getApplicationContext(), SOME_ERROR, R.color.grey);
                    mediaUrl = "";
                }
            }

            @Override
            public void onFailure(Call<UploadimageResponse> call, Throwable t) {
                meestLoaderDialog.hideDialog();
                Utilss.showToast(getApplicationContext(), SOME_ERROR, R.color.grey);
            }
        });
    }*/

/*    private void postNow() {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);

        // extracting hashtags from text
        TextPostUploadParam textPostUploadParam = new TextPostUploadParam();
        TextPostUploadParam.PostDatum postArray = new TextPostUploadParam.PostDatum();
        TextPostUploadParam.Location loc = new TextPostUploadParam.Location();
        try {


            if (isMultiple) {
                List<TextPostUploadParam.PostDatum> postDatumList = new ArrayList<>();
                for (String uploadUrl : multipleMedia) {
                    TextPostUploadParam.PostDatum mediaData = new TextPostUploadParam.PostDatum();
                    if (isVideo) {
                        mediaData.setVideo(1);
                        mediaData.setImage(0);
                    } else {
                        mediaData.setVideo(0);
                        mediaData.setImage(1);
                    }
                    mediaData.setIsText(0);
                    mediaData.setPost(uploadUrl);
                    postDatumList.add(mediaData);
                }

                if (multipleMedia != null && multipleMedia.size() > 0) {
                    thumbnailImage = multipleMedia.get(0);
                }
                textPostUploadParam.setThumbnail(thumbnailImage);
                textPostUploadParam.setPostData(postDatumList);
            } else if (isVideo) {
                postArray.setVideo(1);
                postArray.setImage(0);
                postArray.setIsText(0);
                textPostUploadParam.setThumbnail(thumbnailImage);
                postArray.setPost(mediaUrl);
                textPostUploadParam.setPostData(Collections.singletonList(postArray));
            } else {
                postArray.setImage(1);
                postArray.setIsText(0);
                postArray.setVideo(0);
                textPostUploadParam.setThumbnail(mediaUrl);
                postArray.setPost(mediaUrl);
                textPostUploadParam.setPostData(Collections.singletonList(postArray));


            }

            if (type != null && type.equalsIgnoreCase("location")) {
                loc.setLat(Double.parseDouble(latitude));
                loc.setLong(Double.parseDouble(longitude));
            } else {
                loc.setLat(post_lat);
                loc.setLong(post_long);

//                if (post_lat != 0.0d && post_long != 0.0d) {
//
//                } else {
//                    loc.setLat(0.0d);
//                    loc.setLong(0.0d);
//                }
            }


            textPostUploadParam.setLocation(loc);
            textPostUploadParam.setHashTags(hashtagList);
            textPostUploadParam.setAllowComment(allowComment);
            textPostUploadParam.setCaption(postText);
            if (taggedUsername.size() > 0) {
                textPostUploadParam.setTaggedPersons(taggedUsername);
            }
            textPostUploadParam.setPostType("feedPost");
            textPostUploadParam.setViewPost("PUBLIC");
            if (selectedLocation != null && selectedLocation.length() > 0) {
                textPostUploadParam.setTagLocation(selectedLocation);
            }
        } catch (JsonIOException e) {
            meestLoaderDialog.hideDialog();
            e.printStackTrace();
        }

//        HashMap<String, Object> body = new HashMap<>();
//        body.put("postData", postsList);
//        body.put("location", locationModel);
//        body.put("postType", "feedPost");
//        body.put("allowComment", allowComment);
//        body.put("text", "");
//        body.put("caption", postText);
//        body.put("viewPost", "PUBLIC");
//        if (selectedLocation != null && selectedLocation.length() > 0) {
//            body.put("tagLocation", selectedLocation);
//        }
//        if(taggedUsers.size() > 0) {
//            body.put("tagPerson", taggedUsers);
//        }
//        if(hashtagList.size() > 0) {
//            body.put("hashTags", hashtagList);
//        }


        Call<ApiResponse> call = webApi.InsertTextPost(map, textPostUploadParam);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                try {
                    if (response.code() == 200 && response.body().getSuccess()) {
//                        loading.setVisibility(View.GONE);
                        meestLoaderDialog.hideDialog();

                        Utilss.showToast(getApplicationContext(), "Successfully Post", R.color.green);
                        Intent intent = new Intent(NewPostUploadActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
//                        loading.setVisibility(View.GONE);
                        meestLoaderDialog.hideDialog();
                        Utilss.showToast(getApplicationContext(), SOME_ERROR, R.color.grey);
                    }
                } catch (Exception e) {
//                    loading.setVisibility(View.GONE);
                    meestLoaderDialog.hideDialog();
                    e.printStackTrace();
                }
            }

            // gghhgg
            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
//                loading.setVisibility(View.GONE);
                meestLoaderDialog.hideDialog();
            }
        });
    }*/

    @Override
    protected void onResume() {
        super.onResume();

        if (alert_loc != null) {
            alert_loc.cancel();
        }
        //requestNewLocationData();

        if (hasLocationPermission()) {
            getTagLocation();
        }
    }

    @Override
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        selectedLocation = (String) adapterView.getItemAtPosition(position);

        if (selectedLocation != null && selectedLocation.length() > 0) {
            add_location.setText(selectedLocation);

            add_location.setCompoundDrawablesWithIntrinsicBounds(R.drawable.location_icon, 0, 0, 0);
        }

        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
        }
    }

    @SuppressLint("LongLogTag")
    public ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {

            String sb = PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON + "?key=" + API_KEY +
                    "&components=country:in" +
                    "&input=" + URLEncoder.encode(input, "utf8");
            URL url = new URL(sb);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (IOException e) {
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Utilss.showToast(NewPostUploadActivity.this, getString(R.string.Something_went_wrong_please_try_again_later),
                    R.color.msg_fail);
        }

        return resultList;
    }

    boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void tagChanged() {
        String taggedData;
//        if (taggedUsername.size() > 1) {
//            taggedData = taggedUsername.get(0).getUsername() + " + " + (taggedUsername.size() - 1) + " more";
//        } else if (taggedUsername.size() == 1) {
//            taggedData = taggedUsername.get(0).getUsername();
//        } else {
//            taggedData = "Tag People";
//        }

        Log.e(TAG, "tagChanged: "+taggedUsername.size() );
        if (taggedUsername.size() > 0) {
            recyclerTagsPeople.setVisibility(View.VISIBLE);
            ivTagAdd.setVisibility(View.VISIBLE);
//            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(NewPostUploadActivity.this, LinearLayoutManager.HORIZONTAL, false);
//            recyclerTagsPeople.setLayoutManager(horizontalLayoutManagaer);
            recyclerTagsPeople.setAdapter(new RvAdapter(this, (ArrayList<FeedResponse.UserTags>) taggedUsername, (model, pos) -> {
                taggedUsername.remove(model);

                if (taggedUsers.size() > 0) {
                    taggedUsers.remove(pos);
                }

            }));
        } else {
            ivTagAdd.setVisibility(View.GONE);
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

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (data != null && data.getExtras() != null) {
                postVisibility = data.getExtras().getString("postVisibility");
                tvPost_visibility.setText(postVisibility);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestNewLocationData();

            } else {
                Toast.makeText(this, getString(R.string.Permission_Denied), Toast.LENGTH_SHORT).show();
            }

        }


    }
}
