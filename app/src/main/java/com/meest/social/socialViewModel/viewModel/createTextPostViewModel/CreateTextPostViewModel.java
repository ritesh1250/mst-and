package com.meest.social.socialViewModel.viewModel.createTextPostViewModel;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.meest.Adapters.ColorsAdapter;
import com.meest.Interfaces.CreateTextInterface;
import com.meest.Interfaces.FriendSelectCallback;
import com.meest.MainActivity;
import com.meest.Meeast;
import com.meest.Paramaters.TextPostUploadParam;
import com.meest.R;
import com.meest.databinding.CreateTextPostModelBinding;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.register.fragment.profile.model.UploadimageResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.responses.ColorsResponseNew;
import com.meest.responses.FeedResponse;
import com.meest.responses.FeelingResponse;
import com.meest.responses.FriendsListResponse;
import com.meest.responses.TextPostImageBackgroundResponse;
import com.meest.social.socialViewModel.adapter.CreateTextPostAdapter;
import com.meest.social.socialViewModel.adapter.ctpaFriendsListAdapter;
import com.meest.social.socialViewModel.model.UserProfileRespone1;
import com.meest.social.socialViewModel.view.postVisibilitySetting.PostVisibilitySetting;
import com.meest.social.socialViewModel.view.searchLocation.SearchLocation;
import com.meest.utils.LoadingDialog;
import com.meest.utils.PaginationListener;
import com.meest.utils.SingleShotLocationProvider;
import com.meest.utils.Variables;
import com.meest.utils.goLiveUtils.CommonUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.meest.Fragments.HomeFragments.BUNDLE_CALL_TYPE;

//import com.meest.Adapters.ctpaFriendsListAdapter;

public class CreateTextPostViewModel extends ViewModel {

    private static final String TAG = "CreateTextPostViewModel";
    private Context context;
    private Activity activity;
    public File imageFile;
    public String selectedColorCode = "#000000", selectedImageUrl, mPostText, postVisibility, feelingID, subFeelingID, feelingTitle, subFeelingTitle, uploadedImageUrl, thumbnail, latitude, longitude;
    public Boolean allowComment = true, isFeelingType = false, Loading = true;
    public ArrayList<String> hashtagList = new ArrayList<>();
    public ColorsResponseNew colorsResponse;
    public TextPostImageBackgroundResponse imageBackgroundResponse;
    public WebApi webApi;
    public FeelingResponse.Row feelingData;
    public Map<String, String> header = new HashMap<>();
    public View rootView;
    public int pageNo = 1;
    int pageSize=20;
    public static final int PERMISSION_CODE = 123;
    public static final int CAMERA_PERMISSION_CODE = 124;
    public static final int REQUEST_LOCATION = 1;
    public static int totalCount = -1;
    public CreateTextPostModelBinding binding;
    public List<ColorsResponseNew.Data.Row> list_response;
    public Double post_lat = 0.0d;
    public Double post_long = 0.0d;
    public FriendsListResponse friendsListResponse;
    public static List<String> taggedUsers = new ArrayList<>();
    public static List<FeedResponse.UserTags> taggedUsername = new ArrayList<>();
    public String stringData = " ";
    public String CALL_TYPE;
    public LoadingDialog loadingDialog;
    public CreateTextPostAdapter adapter;

    public CreateTextPostViewModel(Context context, Activity activity, CreateTextPostModelBinding binding) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;
    }

    public void getData() {
        if (activity.getIntent().getExtras() != null) {
//            isFeelingType = activity.getIntent().getExtras().getBoolean("isFeelingType", false);
//            String stringData = getIntent().getExtras().getString("feelingData");
//            feelingData = new Gson().fromJson(stringData, FeelingResponse.Row.class);
            CALL_TYPE = activity.getIntent().getExtras().getString(BUNDLE_CALL_TYPE);
            String stringData = activity.getIntent().getExtras().getString("feelingData");
            feelingData = new Gson().fromJson(stringData, FeelingResponse.Row.class);
            header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
            webApi = ApiUtils.getClient().create(WebApi.class);
            loadingDialog = new LoadingDialog(context);
        }
    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                Utilss.showToast(context, context.getString(R.string.Something_went_wrong_please_try_again_later),
                        R.color.msg_fail);
            }

            if (!gps_enabled) {
                Toast.makeText(context, context.getString(R.string.Please_enable_location_services_first), Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            } else {
//                loadingDialog.show();
                binding.loadingPostext.setVisibility(View.VISIBLE);
                SingleShotLocationProvider.requestSingleUpdate(context,
                        new SingleShotLocationProvider.LocationCallback() {
                            @Override
                            public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                                try {
                                    latitude = String.valueOf(location.latitude);
                                    longitude = String.valueOf(location.longitude);

                                    String url = Variables.local(latitude + "", longitude + "");
//                                    Intent i = new Intent(context, SearchLocationsActivity.class);
                                    Intent i = new Intent(context, SearchLocation.class);
                                    i.putExtra("imagePath", url);
                                    i.putExtra("type", "location");
                                    i.putExtra("lat", latitude);
                                    i.putExtra("log", longitude);
                                    context.startActivity(i);

//                                    loadingDialog.hide();
                                    binding.loadingPostext.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
//                                    loadingDialog.hide();
                                    binding.loadingPostext.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.Your_GPS_seems_to_be_disabled_do_you_want_to_enable_it))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            getLocation();
        }
    }

    public void showPeopleDialog() {
        Rect displayRectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.fragment_layout_search, viewGroup, false);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        TextView dialog_title = dialogView.findViewById(R.id.dialog_title);
        ImageView dialog_back = dialogView.findViewById(R.id.dialog_back);
        RecyclerView dialog_recycler = dialogView.findViewById(R.id.dialog_recycler);
        // RecyclerView selected_recycler = dialogView.findViewById(R.id.selected_recycler);
        AutoCompleteTextView tvAutoComplete = dialogView.findViewById(R.id.tvAutoComplete);

        BottomSheetDialog tagDialog = new BottomSheetDialog(context);
        final ctpaFriendsListAdapter[] friendsListAdapter = new ctpaFriendsListAdapter[1];

        dialog_recycler.setHasFixedSize(true);
        dialog_recycler.setLayoutManager(new LinearLayoutManager(context));
        dialog_recycler.setVisibility(View.VISIBLE);

        tvAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    String text = tvAutoComplete.getText().toString().trim();
                    dialog_recycler.setVisibility(View.VISIBLE);
                    hitTagApi(text,dialog_recycler);
                  //  pagination(text,dialog_recycler);
                } else {
                    dialog_recycler.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog_title.setText(context.getString(R.string.Select_Friends_to_tag_them));

        dialog_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagDialog.dismiss();
            }
        });
        tagDialog.setContentView(dialogView);
        tagDialog.show();
    }

    private void pagination(String text, RecyclerView dialog_recycler) {
    dialog_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!recyclerView.canScrollVertically(1) && dy > 0) {
                Log.e(TAG, "onScrolled BOTTOM: ");
                CreateTextPostViewModel.this.pageNo++;
               hitTagApi(text,dialog_recycler);
                //scrolled to BOTTOM
            }
        }
    });
    }

    private void hitTagApi(String text, RecyclerView dialog_recycler) {
        Map<String, String> header = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        body.put("searchText", text);
        body.put("pageNo", String.valueOf(pageNo));
        body.put("pageSize", String.valueOf(pageSize));
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context,
                SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FriendsListResponse> call = webApi.searchFriendsList(header, body);
        call.enqueue(new Callback<FriendsListResponse>() {
            @Override
            public void onResponse(Call<FriendsListResponse> call, @NotNull Response<FriendsListResponse> response) {
                if (response.code() == 200 && response.body().getSuccess()) {
                    if (response.body() != null) {
                        friendsListResponse = response.body();
                        ctpaFriendsListAdapter friendsListAdapter1 = new ctpaFriendsListAdapter(context, friendsListResponse, (FriendSelectCallback) activity);
                        dialog_recycler.setAdapter(friendsListAdapter1);


                    }
                } else {
                    Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<FriendsListResponse> call, Throwable t) {
                Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
            }

        });

    }

    private Bitmap getScreenShot() {
        binding.textLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(binding.textLayout.getDrawingCache());
        binding.textLayout.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void store(Bitmap bm, String fileName) {
        File imagePath = new File(context.getFilesDir(), "external_files");
        imagePath.mkdir();
        imageFile = new File(imagePath.getPath(), fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.PNG, 70, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        createImagePost(imageFile);
    }

    public void createImagePost(File file) {
        binding.loadingPostext.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(activity, SharedPrefreances.AUTH_TOKEN));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imageFile = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<UploadimageResponse> call = webApi.uploadImageRequest(map, imageFile);
        call.enqueue(new Callback<UploadimageResponse>() {
            @Override
            public void onResponse(Call<UploadimageResponse> call, Response<UploadimageResponse> response) {
                binding.loadingPostext.setVisibility(View.GONE);
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    uploadedImageUrl = response.body().getData().getUrl();
                    postTextSubmit(selectedImageUrl == null || selectedImageUrl.length() <= 0);
//                    postTemp();
                } else {
                    Utilss.showToast(context, context.getString(R.string.Something_went_wrong_please_try_again_later), R.color.social_background_blue);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UploadimageResponse> call, Throwable t) {
                binding.loadingPostext.setVisibility(View.GONE);
                Utilss.showToast(context, context.getString(R.string.Something_went_wrong_please_try_again_later), R.color.social_background_blue);
                binding.btnPostSubmit.setClickable(true);
            }
        });
    }

    public void postTextSubmit(boolean isText) {
        // extracting hashtags from text
        hashtagList.clear();
        String[] words = mPostText.split(" ");
        for (String word : words) {
            if (word.startsWith("#")) {
                hashtagList.add(word);
            }
        }
        binding.loadingPostext.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);

        TextPostUploadParam textPostUploadParam = new TextPostUploadParam();
        TextPostUploadParam.PostDatum postArray = new TextPostUploadParam.PostDatum();
        TextPostUploadParam.Location loc = new TextPostUploadParam.Location();
        try {
            String postType;
            if (isFeelingType) {
                postType = context.getString(R.string.feelingpost);
                if (!(Constant.selectedFeelingID.equals("")))
                    textPostUploadParam.setFeeling(Constant.selectedFeelingID);
                if (!(Constant.selectedActivityID.equals("")))
                    textPostUploadParam.setSubFeeling(Constant.selectedActivityID);
            }
//            if(binding.tvCelebreting.getText())
            else {
                postType = context.getString(R.string.writetext);
            }


            postArray.setTextColorCode(selectedColorCode);

            textPostUploadParam.setHashTags(hashtagList);
            textPostUploadParam.setLocation(loc);
            textPostUploadParam.setPostData(Collections.singletonList(postArray));
            if (isText) {
                postArray.setIsText(1);
                postArray.setImage(0);
                postArray.setVideo(0);
                postArray.setPost(mPostText);
            } else {
                postArray.setImage(1);
                postArray.setIsText(0);
                postArray.setVideo(0);
                postArray.setPost(uploadedImageUrl);
            }

            if (taggedUsername != null) {
                textPostUploadParam.setTaggedPersons(taggedUsername);
            }

            textPostUploadParam.setThumbnail(uploadedImageUrl);
            loc.setLat(0.0);
            loc.setLong(0.0);
            textPostUploadParam.setAllowComment(allowComment);
            textPostUploadParam.setText("");
            textPostUploadParam.setPostType(postType);
            textPostUploadParam.setViewPost(postVisibility.toUpperCase());
        } catch (JsonIOException e) {
            e.printStackTrace();
        }


        Call<ApiResponse> call = webApi.InsertTextPost(map, textPostUploadParam);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {

                if (response.code() == 200 && response.body().getSuccess()) {
                    binding.loadingPostext.setVisibility(View.GONE);
                    clearTheData();
                    taggedUsername.clear();
                    taggedUsers.clear();
                    Utilss.showToast(context, context.getString(R.string.Posted_successfully), R.color.green);
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    activity.finishAffinity();

                } else {
                    binding.btnPostSubmit.setVisibility(View.VISIBLE);
                    binding.loadingPostext.setVisibility(View.GONE);
                    Utilss.showToast(context, context.getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                binding.btnPostSubmit.setVisibility(View.VISIBLE);
                binding.loadingPostext.setVisibility(View.GONE);
                Utilss.showToast(context, context.getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            }
        });

    }

    public void userImageAndName() {

        Map<String, String> header = new HashMap<>();
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        Call<UserProfileRespone1> call = webApi.getUserProfileData(header);
        call.enqueue(new Callback<UserProfileRespone1>() {
            @Override
            public void onResponse(Call<UserProfileRespone1> call, Response<UserProfileRespone1> response) {
                //  image.setVisibility(View.GONE);
                try {
                    if (response.code() == 200 && response.body().getSuccess()) {

                        if (response.body().getDataUser().getGender().equalsIgnoreCase(context.getString(R.string.male))) {

                            CommonUtils.loadImage(binding.d, response.body().getDataUser().getThumbnail(), context, R.drawable.male_place_holder);

                        } else if (response.body().getDataUser().getGender().equalsIgnoreCase(context.getString(R.string.female))) {

                            CommonUtils.loadImage(binding.d, response.body().getDataUser().getThumbnail(), context, R.drawable.female_cardplaceholder);

                        } else {
                            CommonUtils.loadImage(binding.d, response.body().getDataUser().getThumbnail(), context, R.drawable.placeholder);

                        }

                    }

                    binding.userNamePost.setText(SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.F_NAME));

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<UserProfileRespone1> call, Throwable t) {

            }
        });
    }

    public void onBackgroundSelected(int position) {
        binding.loadingPostext.setVisibility(View.VISIBLE);
        selectedImageUrl = imageBackgroundResponse.getData().get(position).getBackgroundImage();
        Glide.with(context)
                .load(selectedImageUrl)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Utilss.showToast(context, context.getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
                        binding.loadingPostext.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.loadingPostext.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.ivPostbackground);
        Timber.e("onBackgroundSelected:**************** " + selectedImageUrl);
    }

    public void onData() {
        binding.loadingPostext.setVisibility(View.VISIBLE);

        Call<TextPostImageBackgroundResponse> call = webApi.getBackgroundImage(header);
        call.enqueue(new Callback<TextPostImageBackgroundResponse>() {
            @Override
            public void onResponse(Call<TextPostImageBackgroundResponse> call, Response<TextPostImageBackgroundResponse> response) {
                binding.loadingPostext.setVisibility(View.GONE);
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    imageBackgroundResponse = response.body();
                    adapter = new CreateTextPostAdapter(context, imageBackgroundResponse, (CreateTextInterface) context);
                    binding.recyclerView.setHasFixedSize(true);
                    //Adapter Calling
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                    binding.recyclerView.setAdapter(adapter);


                    if (imageBackgroundResponse.getData() != null && imageBackgroundResponse.getData().size() > 0)
                        onBackgroundSelected(0);
                    fetchColorsData(pageNo);
                } else {
                    Utilss.showToast(context, context.getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
                }
            }

            @Override
            public void onFailure(Call<TextPostImageBackgroundResponse> call, Throwable t) {
                binding.loadingPostext.setVisibility(View.VISIBLE);
                Utilss.showToast(context, context.getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            }
        });
    }

    public void fetchColorsData(int pageno) {

        Call<ColorsResponseNew> call = webApi.getColors(header);
        call.enqueue(new Callback<ColorsResponseNew>() {
            @Override
            public void onResponse(Call<ColorsResponseNew> call, Response<ColorsResponseNew> response) {
                binding.loadingPostext.setVisibility(View.GONE);
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    colorsResponse = response.body();
                    list_response = response.body().getData().getRows();
                    ColorsAdapter colorsAdapter = new ColorsAdapter(context,
                            colorsResponse, (CreateTextInterface) context);
                    binding.colorsRecycler.setAdapter(colorsAdapter);


                } else {
                    Utilss.showToast(context, context.getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
                }
            }

            @Override
            public void onFailure(Call<ColorsResponseNew> call, Throwable t) {
                binding.loadingPostext.setVisibility(View.GONE);
                Utilss.showToast(context, context.getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            }
        });
    }

    public void init() {
        list_response = new ArrayList<>();
        binding.colorsRecycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        binding.colorsRecycler.setHasFixedSize(true);
        binding.colorsRecycler.addOnScrollListener(new PaginationListener((LinearLayoutManager) binding.colorsRecycler.getLayoutManager(), PaginationListener.PAGE_SIZE_10) {
            @Override
            public void loadMoreItems() {
                System.out.println("==PAGINATION STARTED " + pageNo);
                if (Loading) {
                    if (totalCount > list_response.size()) {
                        pageNo = pageNo + 1;
                        fetchColorsData(pageNo);
                    }
                }

            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return false;
            }
        });

    }

    private void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            activity.getWindow().getDecorView();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
    }

    public void edtWriteTxtTextChangeListner() {
        binding.edtWriteTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPostText = binding.edtWriteTxt.getText().toString().trim();
                if (mPostText.isEmpty()) {
                    binding.txtErrorPost.setVisibility(View.VISIBLE);
                    binding.txtErrorPost.setText(context.getString(R.string.please_enter_text));
                    binding.edtWriteTxt.requestFocus();
                } else {
                    binding.txtErrorPost.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.txtErrorPost.setText("");
            }
        });
    }

    public void postSubmit() {
        mPostText = binding.edtWriteTxt.getText().toString().trim();
        if (mPostText.isEmpty()) {
            binding.txtErrorPost.setVisibility(View.VISIBLE);
            binding.txtErrorPost.setText(context.getString(R.string.please_enter_text));
            binding.edtWriteTxt.setCursorVisible(true);
            binding.edtWriteTxt.requestFocus();
            binding.edtWriteTxt.setText("");
        } else {
            binding.txtErrorPost.setVisibility(View.GONE);
            if (selectedImageUrl != null && selectedImageUrl.length() > 0) {
                binding.btnPostSubmit.setVisibility(View.INVISIBLE);
                binding.btnPostSubmit.setClickable(false);
                binding.edtWriteTxt.setCursorVisible(false);
                binding.edtWriteTxt.clearFocus();
                binding.txtErrorPost.setText("");
                Bitmap bitmap = getScreenShot();
                String filename = System.currentTimeMillis() + ".png";
                store(bitmap, filename);
            } else {
                binding.edtWriteTxt.setCursorVisible(false);
                binding.edtWriteTxt.clearFocus();

                Glide.with(context)
                        .load(R.color.white)
                        .into(binding.ivPostbackground);

                Bitmap bitmap = getScreenShot();
                String filename = System.currentTimeMillis() + ".png";
                store(bitmap, filename);
            }
        }
    }

    public void changePostVisibility() {
        Intent intent = new Intent(context, PostVisibilitySetting.class);
        intent.putExtra("postVisibility", postVisibility);
        activity.startActivityForResult(intent, 1);
    }

    public Meeast application() {
        return (Meeast) context;
    }


    public void clearTheData() {
        Constant.selectedFeelingID = "";
        Constant.selectedFeelingTitle = "";
        Constant.selectedActivityID = "";
        Constant.selectedActivityTitle = "";
        Constant.row_index_activity = -1;
        Constant.row_index_feeling = -1;
    }
}
