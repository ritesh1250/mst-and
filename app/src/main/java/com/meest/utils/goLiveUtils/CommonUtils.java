package com.meest.utils.goLiveUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.getkeepsafe.taptargetview.TapTarget;
import com.google.firebase.messaging.FirebaseMessaging;
import com.meest.R;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.utilss.*;
import com.meest.social.socialViewModel.view.login.LoginSignUp;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonUtils {


    public static final String COMMON_MSG = String.valueOf("Something wrong !");
    private static final String TAG = String.valueOf("LOCATIONS");

    public static RequestOptions requestOptions = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .skipMemoryCache(true)
            .centerCrop()
            .dontAnimate()
            .dontTransform()
            .placeholder(R.drawable.image_placeholder)
            .priority(Priority.IMMEDIATE)
            .encodeFormat(Bitmap.CompressFormat.PNG)
            .format(DecodeFormat.DEFAULT);

    public static String getAddress(Context context, double LATITUDE, double LONGITUDE) {

        String lastAddress = "";

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                //String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                Log.d(TAG, "getAddress:  address" + address);
                Log.d(TAG, "getAddress:  city" + city);
                Log.d(TAG, "getAddress:  state" + state);
                Log.d(TAG, "getAddress:  postalCode" + postalCode);


                lastAddress = address + " " + city + " " + state;
            }

        } catch (Exception e) {
            e.printStackTrace();
            lastAddress = "";

        }
        return lastAddress;
    }


    public static Drawable changeDrawableColor(Context context, int icon, int newColor) {
        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }

    public static String decodeEmoji(String message) {
        String myString = null;
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }


    public static String encodeEmoji(String message) {
        try {
            return URLEncoder.encode(message,
                    "UTF-8");


        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    public static void loadImage(ImageView imageView, String url, Context context) {
        if (url != null && !url.equals("")) {
            Glide.with(context).
                    load(url)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.image_placeholder)
                    .apply(requestOptions)
                    .into(imageView);
        } else {
            Glide.with(context).
                    load(R.drawable.image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .apply(requestOptions)
                    .into(imageView);
        }
    }


    public static void loadImage(ImageView imageView, String url, Context context, int placeHolder, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(context).
                load(url)
                .placeholder(placeHolder)
                .error(R.drawable.image_placeholder)
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);
    }

    public static void loadImage(ImageView imageView, String url, Context context, int placeHolder) {
        Glide.with(context).
                load(url)
                .placeholder(placeHolder)
                .apply(requestOptions)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);

    }

    public static TapTarget getTargetView(Activity context, View view, String title, String body) {
        return TapTarget.forView(view, title, body)
                // All options below are optional
                .outerCircleColor(R.color.outerCircleTargetView)      // Specify a color for the outer circle
                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                // .targetCircleColor(R.color.white)   // Specify a color for the target circle
                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                //.titleTextColor(R.color.white)      // Specify the color of the title text
                .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                //.descriptionTextColor(R.color.red)  // Specify the color of the description text
                .textColor(R.color.white)            // Specify a color for both the title and description text
                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                // .drawShadow(true)                   // Whether to draw a drop shadow or not
                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                .tintTarget(false)                   // Whether to tint the target view's color
                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                //    .icon(Drawable)                     // Specify a custom drawable to draw as the target
                .targetRadius(25);
    }

    public static TapTarget getTargetView(Activity context, View view, String title, String body, int radius) {
        return TapTarget.forView(view, title, body)
                // All options below are optional
                .outerCircleColor(R.color.outerCircleTargetView)      // Specify a color for the outer circle
                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                // .targetCircleColor(R.color.white)   // Specify a color for the target circle
                .titleTextSize(20)                  // Specify the size (in sp) of the title text
                //.titleTextColor(R.color.white)      // Specify the color of the title text
                .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                //.descriptionTextColor(R.color.red)  // Specify the color of the description text
                .textColor(R.color.white)            // Specify a color for both the title and description text
                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                // .drawShadow(true)                   // Whether to draw a drop shadow or not
                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                .tintTarget(false)                   // Whether to tint the target view's color
                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                //    .icon(Drawable)                     // Specify a custom drawable to draw as the target
                .targetRadius(40);
    }

    public static void logoutNow(Context context) {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json");
        map.put("Content-Type", "application/json");
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
//        body.put("fcmToken", SharedPrefreances.getSharedPreferenceString(context, "token"));
        body.put("isOnline", "false");
        body.put("fcmToken", "");
        Call<ApiResponse> call = webApi.updateUserProfile(map, body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID));
                // clearing stored data
                SharedPrefreances.setSharedPreferenceString(context, SharedPrefreances.ID, "");
                SharedPrefreances.setSharedPreferenceString(context, SharedPrefreances.PROFILE, "");
                SharedPrefreances.setSharedPreferenceString(context, "login", "0");
                SharedPrefreances.setSharedPreferenceString(context, "Profile", "");
                SharedPrefreances.setSharedPreferenceString(context, "token", "");
                SessionManager sessionManager = null;

                sessionManager = new SessionManager(context);

                sessionManager.saveBooleanValue(Const.IS_LOGIN, false);

                FirebaseMessaging.getInstance().deleteToken();

                Intent intent = new Intent(context, LoginSignUp.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d("Logout_TAG","e======================================"+"logout");
            }
        });


    }


    public static File store(Context context, Bitmap bm, String fileName) {
        File imagePath = new File(context.getFilesDir(), fileName);
        if (imagePath.exists()) {
            imagePath.delete();
        }
        imagePath = new File(context.getFilesDir(), "external_files");
        imagePath.mkdir();
        File imageFile = new File(imagePath.getPath(), fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile;
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static Bitmap checkImageRotation(Bitmap bitmap, String photoPath) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }

        return rotatedBitmap;
    }


    public static void loadVideoThumbnail(Context context, String videoUrl, ImageView imageView) {
        try {
            imageView.setImageBitmap(retriveVideoFrameFromVideo(videoUrl));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    //deepak code for some testing.
    public final static boolean validateEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
