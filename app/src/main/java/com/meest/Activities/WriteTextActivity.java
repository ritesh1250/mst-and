package com.meest.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.meest.Adapters.ColorsAdapter;
import com.meest.Adapters.GradientAdapter;
import com.meest.Interfaces.CreateTextInterface;
import com.meest.MainActivity;
import com.meest.Paramaters.TextPostUploadParam;
import com.meest.R;
import com.meest.responses.ColorsResponseNew;
import com.meest.responses.TextPostImageBackgroundResponse;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.register.fragment.profile.model.UploadimageResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.model.StoryDataModel;
import com.meest.utils.goLiveUtils.CommonUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteTextActivity extends AppCompatActivity implements CreateTextInterface {

    TextView tvPostDone;
    EditText txtStatus;
    String selectedImageUrl, selectedColorCode, mPostText;
    RelativeLayout mainLayout;
    ImageView ivCancelVideo;
    RecyclerView recyclerView, colors_TextRecycler;
    ImageView gradient_back;
    TextPostImageBackgroundResponse imageBackgroundResponse;
    private GradientAdapter adapter;
    WebApi webApi;
    ImageView loading;
    ColorsResponseNew colorsResponse;
    String uploadedImageUrl;
    RelativeLayout background_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_status_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        findIds();
        ivCancelVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        onData();

        tvPostDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPostText = txtStatus.getText().toString().trim();
                mPostText = CommonUtils.encodeEmoji(txtStatus.getText().toString().trim());
                txtStatus.clearFocus();
                if (txtStatus.getText().toString().trim().isEmpty()) {
                    Toast.makeText(WriteTextActivity.this, "Please enter text!", Toast.LENGTH_SHORT).show();
                } else if (selectedImageUrl == null || selectedImageUrl.length() == 0) {
                    Toast.makeText(WriteTextActivity.this, "Please select background image", Toast.LENGTH_SHORT).show();
                } else {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    Bitmap bitmap = getScreenShot(view);
                    String filename = System.currentTimeMillis() + ".png";
                    store(bitmap, filename);

                    // createImagePost(mPostText, selectedColorCode, selectedImageUrl);
                }
            }
        });


    }

    private Bitmap getScreenShot(View view) {

        background_layout.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(background_layout.getDrawingCache());
        background_layout.setDrawingCacheEnabled(false);
        return bitmap;

    }

    public void store(Bitmap bm, String fileName) {
        File imagePath = new File(getFilesDir(), "external_files");
        imagePath.mkdir();
        File imageFile = new File(imagePath.getPath(), fileName);
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
        tvPostDone.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imageFile = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<UploadimageResponse> call = webApi.uploadImageRequest(map, imageFile);
        call.enqueue(new Callback<UploadimageResponse>() {
            @Override
            public void onResponse(Call<UploadimageResponse> call, Response<UploadimageResponse> response) {
                loading.setVisibility(View.GONE);
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    uploadedImageUrl = response.body().getData().getUrl();
                    postNow(uploadedImageUrl);
                } else {
                    Utilss.showToast(getApplicationContext(), "Server Error", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(@NotNull Call<UploadimageResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
                tvPostDone.setVisibility(View.VISIBLE);
                Utilss.showToast(getApplicationContext(), "Server Error", Toast.LENGTH_LONG);
            }
        });

    }

    private void onData() {
        loading.setVisibility(View.VISIBLE);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        webApi = ApiUtils.getClient().create(WebApi.class);
        Call<TextPostImageBackgroundResponse> call = webApi.getBackgroundImage(header);
        call.enqueue(new Callback<TextPostImageBackgroundResponse>() {
            @Override
            public void onResponse(Call<TextPostImageBackgroundResponse> call, Response<TextPostImageBackgroundResponse> response) {
                loading.setVisibility(View.GONE);
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    imageBackgroundResponse = response.body();

                    adapter = new GradientAdapter(WriteTextActivity.this,
                            imageBackgroundResponse, WriteTextActivity.this);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                            LinearLayoutManager.HORIZONTAL, false));
                    recyclerView.setAdapter(adapter);
                    fetchColorsData();
                    if (imageBackgroundResponse.getData().size() > 0)
                        backgroundSelected(0);

                } else {
                    Utilss.showToast(getApplicationContext(), "Server Error", R.color.msg_fail);
                }
            }

            @Override
            public void onFailure(Call<TextPostImageBackgroundResponse> call, Throwable t) {
                loading.setVisibility(View.VISIBLE);
                Utilss.showToast(getApplicationContext(), "Server Error", R.color.msg_fail);
            }
        });
    }


    private void fetchColorsData() {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        webApi = ApiUtils.getClient().create(WebApi.class);
        Call<ColorsResponseNew> call = webApi.getColors(header);
        call.enqueue(new Callback<ColorsResponseNew>() {
            @Override
            public void onResponse(Call<ColorsResponseNew> call, Response<ColorsResponseNew> response) {
                loading.setVisibility(View.GONE);
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    colorsResponse = response.body();

                    ColorsAdapter colorsAdapter = new ColorsAdapter(WriteTextActivity.this,
                            colorsResponse, WriteTextActivity.this);

                    colors_TextRecycler.setLayoutManager(new LinearLayoutManager(WriteTextActivity.this,
                            RecyclerView.VERTICAL, false));
                    colors_TextRecycler.setHasFixedSize(true);
                    colors_TextRecycler.setAdapter(colorsAdapter);
                } else {
                    Utilss.showToast(getApplicationContext(), "Server Error", R.color.msg_fail);
                }
            }

            @Override
            public void onFailure(Call<ColorsResponseNew> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Utilss.showToast(getApplicationContext(), "Server Error", R.color.msg_fail);
            }
        });
    }


    private void findIds() {
        tvPostDone = findViewById(R.id.tvPostDone);
        txtStatus = findViewById(R.id.txtStatus);
        colors_TextRecycler = findViewById(R.id.colors_TextRecycler);
        mainLayout = findViewById(R.id.mainLayout);
        gradient_back = findViewById(R.id.gradient_back);
        ivCancelVideo = findViewById(R.id.ivCancelVideo);
        recyclerView = findViewById(R.id.gradient_recycler);
        loading = findViewById(R.id.loading_writetext);
        background_layout = findViewById(R.id.background_layout);

        txtStatus.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

    }


    @Override
    public void colorsSelected(int position) {
        selectedColorCode = colorsResponse.getData().getRows().get(position).getColorCode();
        txtStatus.setTextColor(Color.parseColor(selectedColorCode));
    }

    @Override
    public void backgroundSelected(int position) {
        loading.setVisibility(View.VISIBLE);
        selectedImageUrl = imageBackgroundResponse.getData().get(position).getBackgroundImage();
        Glide.with(WriteTextActivity.this)
                .load(selectedImageUrl)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Utilss.showToast(getApplicationContext(), "Something went wrong, please try again", R.color.msg_fail);
                        loading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        loading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(gradient_back);
    }

    private void postNow(String imageUrl) {
        List<String> hashTagList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        StoryDataModel storyDataModel = new StoryDataModel();
        storyDataModel.setCaption(mPostText);
        TextPostUploadParam.Location loc = new TextPostUploadParam.Location();
        storyDataModel.setLocation(loc);
        loc.setLat(0.0);
        loc.setLong(0.0);
        storyDataModel.setHashTags(hashTagList);
        storyDataModel.setStory(imageUrl);
        storyDataModel.setStatus(true);
        storyDataModel.setImage(1);
        Call<ApiResponse> call = webApi.insertStory(map, storyDataModel);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NotNull Call<ApiResponse> call, @NotNull Response<ApiResponse> response) {
                try {
                    if (response.code() == 200 && response.body().getSuccess()) {
                        loading.setVisibility(View.GONE);
                        Utilss.showToast(WriteTextActivity.this, "Story posted successfully", R.color.green);
                        Intent intent = new Intent(WriteTextActivity.this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        loading.setVisibility(View.GONE);
                        Utilss.showToast(WriteTextActivity.this, "Server Error", Toast.LENGTH_LONG);
                    }
                } catch (Exception e) {
                    loading.setVisibility(View.GONE);
                    e.printStackTrace();
                    Utilss.showToast(WriteTextActivity.this, "Server Error", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ApiResponse> call, @NotNull Throwable t) {
                loading.setVisibility(View.GONE);
                Utilss.showToast(WriteTextActivity.this, "Server Error", Toast.LENGTH_LONG);
            }
        });
    }
}
