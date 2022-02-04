package com.meest.metme.camera2.preview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.state.State;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.metme.camera2.CameraActivity;
import com.meest.metme.camera2.photoEditor.EditImageActivity;
import com.meest.metme.camera2.sticker.StickerBottomSheet;
import com.meest.metme.camera2.sticker.view.StickerView;
import com.meest.metme.camera2.utills.CameraUtil;
import com.meest.metme.model.gallery.GalleryPhotoModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class PreviewImageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PreviewImageActivity";
    ImageView image_preview_close_btn, iv_image_preview_add_brush, iv_image_preview_add_text, iv_image_preview_stickers, iv_image_preview_adjust, image_preview_set_image;
    String imageUri;
    Uri newUri;
    Bitmap bitmap;
    private ImageView multiCaptureImage1, multiCaptureImage2, multiCaptureImage3;
    LinearLayout multiSnapCaptureLayout;
    private ArrayList<String> multiSnapsList = new ArrayList<>();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Saving Media..");
        progressDialog.setCanceledOnTouchOutside(false);
        // Initialising views
        initViews();

        // multi capture images thumbnail images IV initializing
        initMultiCaptureImages();

        // onClickListeners
        image_preview_close_btn.setOnClickListener(this);
        iv_image_preview_add_brush.setOnClickListener(this);
        iv_image_preview_add_text.setOnClickListener(this);
        iv_image_preview_stickers.setOnClickListener(this);
        iv_image_preview_adjust.setOnClickListener(this);

        // set image from the imageUri and set to the
        if (getIntent().getStringExtra("image") != null) {
            imageUri = getIntent().getStringExtra("image");

            image_preview_set_image.setImageURI(Uri.parse(imageUri));



        }

        //set the screenshot to the preview
        if (getIntent().getStringExtra("screenshotImage") != null) {

            imageUri =  getRealPathFromUri(CameraUtil.imageUri);

            Log.e(TAG, "imageUri 2: "+CameraUtil.imageUri);
            Log.e(TAG, "imageUri 3: "+imageUri);

            image_preview_set_image.setImageURI(CameraUtil.imageUri);
        }

        // mutli capture array list
        if (getIntent().getStringArrayListExtra("multiSnaps") != null) {
            multiSnapsList = getIntent().getStringArrayListExtra("multiSnaps");
            try {
                imageUri = multiSnapsList.get(0);
                setImageToPreview(multiSnapsList.get(0), image_preview_set_image);
                showMultiCaptureImages();
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        initSticker();

        findViewById(R.id.iv_image_preview_stickers).setOnClickListener(v -> {
            stickerBottomSheet = new StickerBottomSheet();
            stickerBottomSheet.show(getSupportFragmentManager(),
                    "StickerBottomSheet");
        });

        findViewById(R.id.downloadMedia).setOnClickListener(v -> {
            imageUriToBitmap(imageUri);
        });

        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("name", "");
                intent.putExtra("path",imageUri);
                intent.putExtra("isVideo",false);
                intent.putExtra("whereFrom",getIntent().getStringExtra("whereFrom"));
                setResult(Activity.RESULT_OK, intent);
                finish();
/*
                Intent intent=new Intent();
                GalleryPhotoModel model = new GalleryPhotoModel();
                model.setImageText("");
                model.setPicturePath(imageUri);
                model.setVideo(false);
                model.setType("Image");
                ArrayList<GalleryPhotoModel> selectedItem = new ArrayList<>();
                selectedItem.add(model);
                intent.putParcelableArrayListExtra("result",selectedItem);
                setResult(Activity.RESULT_OK, intent);
                finish();*/
            }
        });

    }

    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void initMultiCaptureImages() {
        multiSnapCaptureLayout = findViewById(R.id.multiSnapCaptureLayout);
        multiCaptureImage1 = findViewById(R.id.multiCaptureImage01);
        multiCaptureImage2 = findViewById(R.id.multiCaptureImage02);
        multiCaptureImage3 = findViewById(R.id.multiCaptureImage03);
        clickEventsMultiCapture();

    }

    private void clickEventsMultiCapture() {
        multiCaptureImage1.setOnClickListener(v -> {
            image_preview_set_image.setImageURI(Uri.parse((multiSnapsList.get(0))));
            imageUri = multiSnapsList.get(0);
        });

        multiCaptureImage2.setOnClickListener(v -> {
            image_preview_set_image.setImageURI(Uri.parse((multiSnapsList.get(1))));
            imageUri = multiSnapsList.get(1);

        });

        multiCaptureImage3.setOnClickListener(v -> {
            image_preview_set_image.setImageURI(Uri.parse((multiSnapsList.get(2))));
            imageUri = multiSnapsList.get(2);
        });
    }

    // method to convert bitmap to uri
    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    // method to set image to the preview image view
    private void setImageToPreview(String imageUri, ImageView image_preview_set_image) {
        runOnUiThread(() -> Glide.with(this)
                .load(imageUri)
                .into(image_preview_set_image));
    }

    // method to resize the captured image bitmap so as to fit in the image view for preview
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    private void showMultiCaptureImages() {
        multiSnapCaptureLayout.setVisibility(View.VISIBLE);
        if (multiSnapsList.get(0) != null) {
            multiCaptureImage1.setImageURI(Uri.parse(multiSnapsList.get(0)));
        }
        if (multiSnapsList.get(1) != null) {
            multiCaptureImage2.setImageURI(Uri.parse(multiSnapsList.get(1)));

        }
        if (multiSnapsList.get(2) != null) {
            multiCaptureImage3.setImageURI(Uri.parse(multiSnapsList.get(2)));
        }

    }


    private void initViews() {
        image_preview_close_btn = findViewById(R.id.image_preview_close_btn);
        iv_image_preview_add_brush = findViewById(R.id.iv_image_preview_add_brush);
        iv_image_preview_add_text = findViewById(R.id.iv_image_preview_add_text);
        iv_image_preview_stickers = findViewById(R.id.iv_image_preview_stickers);
        iv_image_preview_adjust = findViewById(R.id.iv_image_preview_adjust);
        image_preview_set_image = findViewById(R.id.image_preview_set_image);
    }

    public void showImage2(String imageUri, ImageView imageView) {
        runOnUiThread(() -> Glide.with(this)
                .load(imageUri)
                .into(imageView));
    }
    private static int IMAGE_PREVIEW = 1991;
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_preview_close_btn:
                startActivity();
                break;

            case R.id.iv_image_preview_add_brush:
                Intent j = new Intent(this, EditImageActivity.class);
                try {
                    if (CameraUtil.bitmap==null || CameraUtil.multiSnapEnable)
                    {

                        Log.e(TAG, "bitmap imageUri: "+imageUri);
                        CameraUtil.bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(new File(imageUri)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivityForResult(j,IMAGE_PREVIEW);
                break;

            case R.id.iv_image_preview_adjust:

                Uri myUri = Uri.parse(imageUri);

////            performCrop(myUri);
//            startCropImageActivity(myUri);
                Uri x = Uri.fromFile(new File(String.valueOf(myUri)));

               if (CameraUtil.bitmap!=null)
               {
                   startCropImageActivity(getImageUri(CameraUtil.bitmap));
               }
               else
               {
                   startCropImageActivity(x);
               }

                break;

        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setAllowFlipping(true)
//                .setFlipHorizontally(true)
                .setAllowFlipping(true)
//                .setFlipVertically(true)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAllowCounterRotation(true)
                .start(this);
    }





    //Sticker Code:-
    private StickerView mCurrentView;
    private ArrayList<View> mViews;
    private RelativeLayout mContentRootView;
    private View mAddSticker;

    public static StickerBottomSheet stickerBottomSheet;

    private void initSticker() {
        mContentRootView = findViewById(R.id.rl_content_root);

        mViews = new ArrayList<>();


    }

    public void addStickerView() {
        final StickerView stickerView = new StickerView(this);

        Bitmap image = null;
        try {
            URL url = new URL(CameraUtil.stickerURL);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stickerView.setBitmap(image);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                mContentRootView.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {

                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mViews.remove(position);
                mViews.add(mViews.size(), stickerTemp);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mContentRootView.addView(stickerView, lp);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }

    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }

        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }


    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            Log.e(TAG, "onActivityResult: "+requestCode);

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    String photoUri = resultUri.toString();
                    try {
                        Log.e(TAG, "photoUri: "+photoUri);
                        CameraUtil.bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(photoUri));
                    } catch (Exception e) {
                        Log.e(TAG, "Exception: "+e.getMessage() );
                        e.printStackTrace();
                    }
                    showImage2(photoUri, image_preview_set_image);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Log.e("setupError", error + "");
                }
            }


            if (requestCode == IMAGE_PREVIEW) {

//                String uri =   data.getStringExtra("imageUri");
//
//                Log.e(TAG, "Screenshot uri: "+uri);
//                imageUri = uri;
//                image_preview_set_image.setImageURI(Uri.parse(uri));

                image_preview_set_image.setImageBitmap(CameraUtil.bitmap);
            }
            if (requestCode == 123) {

                showImage2(String.valueOf(data.getData()), image_preview_set_image);
            }

            if (requestCode == 321) {

                if (data != null) {
                    // get the returned data
                    Bundle extras = data.getExtras();
                    // get the cropped bitmap
                    Bitmap selectedBitmap = extras.getParcelable("data");

                    image_preview_set_image.setImageBitmap(selectedBitmap);
                }

            }
        }
    }

    private void startActivity()
    {
//        Intent intent = new Intent(this, CameraActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity();
    }

    private void imageUriToBitmap(String imageUri) {
        try {
            String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date());
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(new File(imageUri)));
            saveImage(bitmap,"Meest",fname);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    OutputStream fos = null;
    File imageFile = null;
    Uri imageUriFile = null;

    private void saveImage(Bitmap bitmap, @NonNull String folderName, @NonNull String fileName) throws IOException {
        progressDialog.show();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(
                            MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + folderName);
                    imageUriFile = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                    if (imageUri == null)
                        try {
                            throw new IOException("Failed to create new MediaStore record.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    try {
                        fos = resolver.openOutputStream(imageUriFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    File imagesDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).toString() + File.separator + folderName);

                    if (!imagesDir.exists())
                        imagesDir.mkdir();

                    imageFile = new File(imagesDir, fileName + ".png");
                    try {
                        fos = new FileOutputStream(imageFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }


                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos))
                    try {
                        throw new IOException("Failed to save bitmap.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                try {
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //Background work here
            handler.post(() -> {
                progressDialog.dismiss();
                if (imageFile != null) {//pre Q
                    MediaScannerConnection.scanFile(this, new String[]{imageFile.toString()}, null, null);
                    imageUriFile = Uri.fromFile(imageFile);
                    Toast.makeText(this, "Image save successfully.", Toast.LENGTH_SHORT).show();
                }
                //UI Thread work here
            });
        });



    }

}