package com.meest.medley_camera2.camera2.viewmodels;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.meest.databinding.ActivityPreviewImageBinding;
import com.meest.databinding.ActivityPreviewImageMetmeBinding;
import com.meest.medley_camera2.camera2.view.activity.PreviewImageActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.meest.medley_camera2.camera2.utills.CameraUtil.bitmap;
import static com.meest.medley_camera2.camera2.utills.CameraUtil.imageUri;


public class PreviewImageViewModel extends ViewModel {
    Context context;
    PreviewImageActivity previewImageActivity;
    ActivityPreviewImageMetmeBinding binding;
    OutputStream fos = null;
    File imageFile = null;
    Uri imageUriFile = null;
    public ProgressDialog progressDialog;

    public PreviewImageViewModel(Context context, PreviewImageActivity previewImageActivity, ActivityPreviewImageMetmeBinding binding) {
        this.context = context;
        this.previewImageActivity = previewImageActivity;
        this.binding = binding;

    }
    public void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setAllowFlipping(true)
                .setAllowFlipping(true)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true).setAllowCounterRotation(true).start(previewImageActivity);
    }
    public void saveImage(Bitmap bitmap, @NonNull String folderName, @NonNull String fileName) throws IOException {
        progressDialog.show();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
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
                    MediaScannerConnection.scanFile(context, new String[]{imageFile.toString()}, null, null);
                    imageUriFile = Uri.fromFile(imageFile);
                    Toast.makeText(context, "Image save successfully.", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public void imageUriToBitmap(String imageUri) {
        try {
            String fname = new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date());
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(new File(imageUri)));
            saveImage(bitmap, "Meest", fname);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // method to convert bitmap to uri
    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
