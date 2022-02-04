package com.meest.Activities.preview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.meest.MainActivity;
import com.meest.R;
import com.meest.Services.StoryPostuploadService;
import com.meest.medley_camera2.camera2.utills.CameraUtil;
import com.meest.social.socialViewModel.view.NewPostUploadActivity;
import com.meest.utils.LoadingDialog;
import com.bumptech.glide.Glide;
import com.seerslab.argear.session.ARGFrame;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PreviewPhotoActivity extends AppCompatActivity {

    private static final String TAG = "PreviewPhotoActivity";
    String type, imagePath;
   public ImageView iv_main;
    ImageView iv_main_bg;
    private boolean isStory = false;
    private ARGFrame.Ratio mScreenRatio = ARGFrame.Ratio.RATIO_4_3;
    ImageView imageView3;
    Button btnNext;
    //    LottieAnimationView loadingView;
    LoadingDialog loadingDialog;
    String latitude, longitude;
    String address_name;
    Intent mIntent;
    String shareOtherApp = "";
    private Double post_lat = 0.0d;
    private Double post_long = 0.0d;
    Bitmap afterConvert;

    LottieAnimationView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_photo);
        mIntent = getIntent();
        loadingDialog = new LoadingDialog(this);

        iv_main = findViewById(R.id.iv_main);
        iv_main_bg = findViewById(R.id.iv_main_bg);
         //loadingView = findViewById(R.id.loading);
        imagePath=null;
        if (getIntent().getExtras() == null) {
            finish();
        }

//        loadingView.setAnimation("loading.json");
//        loadingView.playAnimation();
//        loadingView.loop(true);

        btnNext = findViewById(R.id.ivVideoDone);
        imageView3 = findViewById(R.id.ivCoverPic);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (mIntent != null) {
            type = mIntent.getStringExtra("type");
            imagePath = mIntent.getStringExtra("imagePath");
            Log.e(TAG, "imagePath Preview: " + imagePath);
            isStory = mIntent.getBooleanExtra("isStory", false);
        }



        if (type != null && type.equalsIgnoreCase("location")) {
            latitude = getIntent().getStringExtra("lat");
            longitude = getIntent().getStringExtra("log");
            address_name = getIntent().getStringExtra("address_name");
        }
        // Outside app share
       /* String action = mIntent.getAction();
        String type1 = mIntent.getType();
        if (action!=null){
            if(action.equals(Intent.ACTION_SEND) && type1 != null){
                if(type1.startsWith("image/")){
                    Uri mUri = mIntent.getParcelableExtra(Intent.EXTRA_STREAM);
                    imagePath = mUri.toString();
                    shareOtherApp = "shareOtherApp";
                    isStory = false;
                }
            }
        }*/


        Glide.with(PreviewPhotoActivity.this).load(imagePath).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(iv_main);
        Glide.with(PreviewPhotoActivity.this).load(imagePath).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(iv_main_bg);

        if (isStory) {
            btnNext.setOnClickListener(v -> {
                Intent intent = new Intent(PreviewPhotoActivity.this, StoryPostuploadService.class);
                intent.putExtra("storyMediaPath", imagePath);
                intent.putExtra("lat", latitude);
                intent.putExtra("log", longitude);
                intent.putExtra("post_lat", post_lat);
                intent.putExtra("post_long", post_long);
                intent.putExtra("address_name", address_name);
                startService(intent);
                Intent intent1 = new Intent(PreviewPhotoActivity.this, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                finish();
            });
        } else {
            btnNext.setOnClickListener(v -> {
                Intent intent = new Intent(PreviewPhotoActivity.this, NewPostUploadActivity.class);
                intent.putExtra("mediaPath", imagePath);
                intent.putExtra("isStory", isStory);
                intent.putExtra("isVideo", false);
                intent.putExtra("type", type);
                intent.putExtra("shareOtherApp", shareOtherApp);
                if (type != null && type.equalsIgnoreCase("location")) {
                    intent.putExtra("lat", latitude);
                    intent.putExtra("log", longitude);
                    intent.putExtra("address_name", address_name);
                }
                startActivity(intent);
                finish();
            });
        }

       /* else if(action.equals(Intent.ACTION_SEND_MULTIPLE) && type != null){
            ArrayList<Uri> mUris = mIntent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            for(int i=0;i<mUris.size();i++){
                ImageModel m = new ImageModel();
                m.setName("Image "+ i);
                m.setUri(mUris.get((i)));
                data.add(m);
            }
        }*/

    }


    private File storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.e(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;

        }
        return pictureFile;
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 18) ? (originalSize / 9) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    public static Bitmap flip(Bitmap src) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();

        matrix.preScale(-1.0f, 1.0f);

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }




}