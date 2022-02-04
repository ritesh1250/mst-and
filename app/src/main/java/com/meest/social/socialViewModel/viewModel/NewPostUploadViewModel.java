package com.meest.social.socialViewModel.viewModel;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meest.Activities.VideoPost.VideoPostVisibilitySetting;
import com.meest.Adapters.FriendsListAdapter;
import com.meest.Interfaces.FriendSelectCallback;
import com.meest.R;
import com.meest.Services.NewPostUploadService;
import com.meest.Services.StoryPostuploadService;
import com.meest.databinding.NewPostUploadActivityBinding;
import com.meest.mediapikcer.utils.PictureFacer;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.responses.FeedResponse;
import com.meest.responses.FriendsListResponse;
import com.meest.social.socialViewModel.view.NewPostUploadActivity;
import com.meest.social.socialViewModel.view.createTextPost.CreateTextPost;
import com.meest.videomvvmmodule.utils.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class NewPostUploadViewModel extends ViewModel {


    private static final String TAG = "NewPostUploadViewModel";

    public String postVisibility;
    public boolean isVideo = false, isMultiple;

    public List<PictureFacer> selectedItem;

    public int recode = 0;
    public String mediaPath;

    public String thumbnailImage, compressedVideo;
    public String postType, type;
    public boolean isStory = false, isPost = false;
    File imageFile;

    public String latitude, longitude;
    public String address_name;

    public boolean allowComment = true;
    public String postText;

    public Double post_lat = 0.0d;
    public Double post_long = 0.0d;


    Activity activity;
    NewPostUploadActivityBinding binding;

    public static   List<String> taggedUsers = new ArrayList<>();
    public static   List<FeedResponse.UserTags> taggedUsername = new ArrayList<>();

    public ArrayList<String> hashtagList = new ArrayList<>();

    public int PERMISSION_ID = 44;

    public FusedLocationProviderClient fusedLocationProviderClient;

    FriendsListResponse friendsListResponse;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    public BottomSheetDialog bottomSheetDialog;
    private static final String API_KEY = "AIzaSyDfcsomOyYAs3BXBUD58CpE0WulwtSz8Ms";
    private boolean isCompressed = false, isThumb = false;


    private CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>(false);
    public MutableLiveData<String> toast = new MutableLiveData<>();



    public NewPostUploadViewModel(NewPostUploadActivity newPostUploadActivity, NewPostUploadActivityBinding binding) {

        this.activity = newPostUploadActivity;
        this.binding = binding;
    }

    public void getData()
    {
        postVisibility = activity.getString(R.string.Public);
        if (activity.getIntent().getExtras() == null) {
            Utilss.showToast(activity, activity.getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            activity.finish();
        }
        isMultiple = activity.getIntent().getExtras().getBoolean("isMultiple", false);
        if (isMultiple) {
            String resul = activity.getIntent().getStringExtra("multipleMedia");
            Gson gson = new Gson();
            Type type = new TypeToken<List<PictureFacer>>() {
            }.getType();
            selectedItem = gson.fromJson(resul, type);
            recode = activity.getIntent().getExtras().getInt("recode");
        }
        mediaPath = activity.getIntent().getExtras().getString("mediaPath");
        thumbnailImage = activity.getIntent().getExtras().getString("thumbPath");
        isVideo = activity.getIntent().getExtras().getBoolean("isVideo", false);
        type = activity.getIntent().getStringExtra("type");
        isStory = activity.getIntent().getExtras().getBoolean("isStory", false);
    }

    public void setData()
    {
        imageFile = new File(mediaPath);
        if (type != null && type.equalsIgnoreCase("location")) {
            Glide.with(activity).load(mediaPath).into(binding.imgProfile);
            if (type.equalsIgnoreCase("location")) {
                latitude = activity.getIntent().getStringExtra("lat");
                longitude = activity.getIntent().getStringExtra("log");
                address_name = activity.getIntent().getStringExtra("address_name");
            }

//            add_location.setText(CommonUtils.getAddress(this, Double.parseDouble(latitude), Double.parseDouble(longitude)));
            binding.addLocation.setText(address_name);
            binding.addLocation.setVisibility(View.VISIBLE);
        } else if (isVideo) {
            Uri uri = Uri.fromFile(imageFile);
            Glide.with(activity).
                    load(imageFile).
                    thumbnail(0.1f).
                    into(binding.imgProfile);
        } else {
            Glide.with(activity).load(imageFile).into(binding.imgProfile);
        }

        binding.switchMap.setChecked(allowComment);


        binding.loading.setAnimation("loading.json");
        binding.loading.playAnimation();
        binding.loading.loop(true);
    }

    public void gotoNextScreen()
    {
        if (isStory) {
            Intent intent = new Intent(activity, StoryPostuploadService.class);
            intent.putExtra("storyMediaPath", mediaPath);
            intent.putExtra("isStoryVideo", isVideo);
            intent.putExtra("isStoryMultiple", isMultiple);
            intent.putExtra("storyPostText", postText);
            intent.putExtra("lat", latitude);
            intent.putExtra("log", longitude);
            intent.putExtra("post_lat", post_lat);
            intent.putExtra("post_long", post_long);
            intent.putExtra("address_name", address_name);
            intent.putExtra("taggedUsername", new Gson().toJson(taggedUsername));
            intent.putExtra("storyMultipleMedia", new Gson().toJson(selectedItem));
            intent.putExtra("storyHashtagList", hashtagList);
            intent.putExtra("allowStoryComment", allowComment);
            intent.putExtra("storyPostText", postText);
            intent.putExtra("storyThumbPath", thumbnailImage);
            intent.putExtra("storyRecode", recode);
            intent.putExtra("sensitiveContent", binding.switchSensitive.isChecked());
            activity.startService(intent);
            CreateTextPost.createpostActivity.finish();
            activity.finish();
        }
        else {
            Intent intent3 = new Intent(activity, NewPostUploadService.class);
            Log.e(TAG, "mediaPath: "+mediaPath);
            intent3.putExtra("mediaPath", mediaPath);
            intent3.putExtra("isVideo", isVideo);
            intent3.putExtra("isStory", isStory);
            intent3.putExtra("isPost", isPost);
            intent3.putExtra("isMultiple", isMultiple);
            intent3.putExtra("type", type);
            intent3.putExtra("postVisibility", postVisibility);
            intent3.putExtra("lat", latitude);
            intent3.putExtra("log", longitude);
            intent3.putExtra("post_lat", post_lat);
            intent3.putExtra("post_long", post_long);
            intent3.putExtra("address_name", address_name);
            intent3.putExtra("taggedUsername", new Gson().toJson(taggedUsername));
            intent3.putExtra("multipleMedia", new Gson().toJson(selectedItem));
            intent3.putExtra("hashtagList", hashtagList);
            intent3.putExtra("allowComment", allowComment);
            intent3.putExtra("postText", postText);
            intent3.putExtra("thumbPath", thumbnailImage);
            intent3.putExtra("recode", recode);
            intent3.putExtra("sensitiveContent", binding.switchSensitive.isChecked());
            activity.startService(intent3);
            CreateTextPost.createpostActivity.finish();
            activity.finish();
        }
    }

    public void startActivity()
    {
        Intent intent = new Intent(activity, VideoPostVisibilitySetting.class);
        intent.putExtra("postVisibility", postVisibility);
        activity.startActivityForResult(intent, 123);
    }

    public LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            if (binding.switchLocation.isChecked()) {
                post_lat = mLastLocation.getLatitude();
                post_long = mLastLocation.getLongitude();
            }
        }
    };

    public void getTagLocation() {
        if (ContextCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_ID);
        } else if (ContextCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
            requestNewLocationData();
        } else {
            Toast.makeText(activity, activity.getString(R.string.Permission_Denied_Goto_settings_and_allow_permission), Toast.LENGTH_SHORT).show();
        }

    }


    @SuppressLint("MissingPermission")
    public void requestNewLocationData() {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(0);
            mLocationRequest.setFastestInterval(0);
            mLocationRequest.setNumUpdates(1);

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
            fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }

    }

    public void showLocationPermissionDialog() {

        Dialog showLocation = new Dialog(activity);
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

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(activity.getString(R.string.Your_GPS_seems_to_be_disabled_do_you_want_to_enable_it))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(activity.getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean hasGpsEnabled() {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void showPeopleDialog() {
        Rect displayRectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.fragment_layout_search, viewGroup, false);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));

        TextView dialog_title = dialogView.findViewById(R.id.dialog_title);
        ImageView dialog_back = dialogView.findViewById(R.id.dialog_back);
        RecyclerView dialog_recycler = dialogView.findViewById(R.id.dialog_recycler);
        // RecyclerView selected_recycler = dialogView.findViewById(R.id.selected_recycler);
        AutoCompleteTextView tvAutoComplete = dialogView.findViewById(R.id.tvAutoComplete);

        BottomSheetDialog tagDialog = new BottomSheetDialog(activity);
        final FriendsListAdapter[] friendsListAdapter = new FriendsListAdapter[1];

        dialog_recycler.setHasFixedSize(true);
        dialog_recycler.setLayoutManager(new LinearLayoutManager(activity));
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
                    header.put("x-token", SharedPrefreances.getSharedPreferenceString(activity,
                            SharedPrefreances.AUTH_TOKEN));
                    disposable.add(Global.initSocialRetrofit().searchFriendsList(header,body)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.io())
                            .doOnSubscribe(disposable1 -> isloading.setValue(true))
                            .doOnTerminate(() -> isloading.setValue(false))
                            .subscribe((friendsListResponse, throwable) -> {
                                if (friendsListResponse != null && friendsListResponse.getSuccess()) {

                                    FriendsListAdapter friendsListAdapter1 = new FriendsListAdapter(activity, friendsListResponse, (FriendSelectCallback)activity);
                                    dialog_recycler.setAdapter(friendsListAdapter1);

                                } else {
                                    Utilss.showToast(activity, activity.getString(R.string.error_msg), R.color.grey);
                                }
                            }));


                } else {
                    dialog_recycler.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog_title.setText(activity.getString(R.string.Select_Friends_to_tag_them));
        dialog_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagDialog.dismiss();
            }
        });

        tagDialog.setContentView(dialogView);
        tagDialog.show();
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
            Utilss.showToast(activity, activity.getString(R.string.Something_went_wrong_please_try_again_later),
                    R.color.msg_fail);
        }

        return resultList;
    }

    public boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

}
