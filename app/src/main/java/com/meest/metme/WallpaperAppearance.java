package com.meest.metme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.meest.R;
import com.meest.databinding.ActivityWallpaperAppearenceBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.model.SaveTextAppearance;
import com.meest.metme.viewmodels.WallpaperAppearanceViewModel;
import com.meest.metme.viewmodels.WallpaperViewModel;
import com.meest.utils.Helper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class WallpaperAppearance extends AppCompatActivity {

    ActivityWallpaperAppearenceBinding binding;
    private String original_wallpaper = "";
    private File directoryName, fileName;
    private String wallpaper_id;
    private ProgressDialog pDialog;
    private String filePath;
    String where, bgfirstColor = "", bgsecondColor = "";
    String toUserId, chatHeadId;
    WallpaperAppearanceViewModel viewModel;
    SaveTextAppearance saveTextAppearance;
    String secondColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wallpaper_appearence);
        if (getIntent() != null) {
         secondColor=getIntent().getStringExtra("secondColor");
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallpaper_appearence);
        viewModel = new WallpaperAppearanceViewModel();
        binding.setViewModel(viewModel);

        initView();
        initListener();
        intiObserver();
        changeHeaderColor(secondColor);
        changeButtonColor(secondColor);
        viewModel.fontName=SharedPrefreances.getSharedPreferenceString(this,chatHeadId);
    }
    private void changeButtonColor(String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(11f);
            binding.submitWallpaper.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getResources().getColor(R.color.first_color), getResources().getColor(R.color.first_color)});
            gd.setCornerRadius(11f);
            binding.submitWallpaper.setBackground(gd);
        }
    }
    private void changeHeaderColor(String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(0f);
            binding.mainContainer.setBackground(gd);
//            binding.dummyChat.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getColor(R.color.first_color), getColor(R.color.first_color)});
            gd.setCornerRadius(0f);
            binding.mainContainer.setBackground(gd);
//            binding.dummyChat.setBackground(gd);
        }
    }
    private void intiObserver() {
        viewModel.isloading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    Toast.makeText(WallpaperAppearance.this, "Wallpaper Successfully Updated", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.putExtra("return","true");
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    private void initView() {
        if (getIntent() != null) {
            where = getIntent().getStringExtra("where");
            toUserId = getIntent().getStringExtra("toUserId");
            chatHeadId = getIntent().getStringExtra("chatHeadId");
        }
        if (getIntent() != null) {
            if (where.equalsIgnoreCase("wallpaper")) {
                original_wallpaper = getIntent().getStringExtra("original_wallpaper");
                wallpaper_id = getIntent().getStringExtra("id");
                bgfirstColor = "";
                bgsecondColor = "";
                filePath = getExternalCacheDir() + "/MetMe_wallpaper_" + wallpaper_id + ".jpg";
                File temp = new File(filePath);
                if (temp.exists()) {
                    displayImage(filePath);
                } else {
                    new DownloadFileFromURL().execute(original_wallpaper);
                }
            } else {
                original_wallpaper = "";
                wallpaper_id = "";
                bgfirstColor = getIntent().getStringExtra("bg_first_color");
                bgsecondColor = getIntent().getStringExtra("bg_second_color");
                GradientDrawable gd1 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(bgfirstColor), Color.parseColor(bgsecondColor)});
                gd1.setCornerRadius(10f);
                binding.dummyMainLl.setBackground(gd1);
            }
        }
        saveTextAppearance = new SaveTextAppearance("", "", "", "", "", toUserId, bgfirstColor, bgsecondColor, original_wallpaper,"");
        saveTextAppearance.setToUserId(toUserId);
    }

    private void initListener() {
        binding.submitWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helper.isNetworkAvailable(getApplicationContext())) {
                    viewModel.saveWallpaper(saveTextAppearance, SharedPrefreances.getSharedPreferenceString(WallpaperAppearance.this, SharedPrefreances.AUTH_TOKEN), WallpaperAppearance.this);
                }else
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();

                }
                Log.e("click", "save");
            }
        });
        binding.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void displayImage(String filePath) {
        Glide.with(this)
                .asBitmap()
                .load(filePath)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        binding.dummyMainLl.setBackground(new BitmapDrawable(getResources(), resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
            pDialog = new ProgressDialog(WallpaperAppearance.this);
            pDialog.setMessage(getResources().getString(R.string.please_wait));
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            String state;
            state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {

                    directoryName = new File(String.valueOf(getExternalCacheDir()));
                    if (!directoryName.exists()) {
                        directoryName.mkdirs();
                    }

                    fileName = new File(directoryName, "MetMe_wallpaper_" + wallpaper_id + ".jpg");
                    System.out.println("Downloading");
                    URL url = new URL(f_url[0]);

                    URLConnection conection = url.openConnection();
                    int fileLength = conection.getContentLength();
                    conection.connect();
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    OutputStream output = new FileOutputStream(fileName);
                    byte data[] = new byte[1024];
                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // writing data to file
                        publishProgress(String.valueOf(total * 100 / fileLength));
                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    input.close();

                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            } else {
                Toast.makeText(WallpaperAppearance.this, "Folder Not created", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            Uri uri = Uri.fromFile(fileName);
            displayImage(String.valueOf(uri));
            pDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pDialog.setProgress(Integer.parseInt(values[0]));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("return","false");
        setResult(Activity.RESULT_OK,intent);
        super.onBackPressed();
    }
}