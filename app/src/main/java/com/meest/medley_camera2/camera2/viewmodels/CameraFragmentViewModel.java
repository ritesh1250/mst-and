package com.meest.medley_camera2.camera2.viewmodels;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.lifecycle.ViewModel;
import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.databinding.ActivityCamera2MedlyBinding;
import com.meest.databinding.FragmentCamera2MedlyBinding;
import com.meest.medley_camera2.camera2.cameraUtils.AspectRatio;
import com.meest.medley_camera2.camera2.cameraUtils.AutoFocusHelper;
import com.meest.medley_camera2.camera2.cameraUtils.ImagesGallery;
import com.meest.medley_camera2.camera2.utills.CameraConstants;
import com.meest.medley_camera2.camera2.utills.CameraUtil;
import com.meest.medley_camera2.camera2.utills.SizeMap;
import com.meest.medley_camera2.camera2.view.fragment.Camera2Fragment;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.Semaphore;

public class CameraFragmentViewModel extends ViewModel {

    private Context context;
    FragmentCamera2MedlyBinding binding;
    Camera2Fragment camera2Fragment;

    public AspectRatio mAspectRatio = CameraConstants.DEFAULT_ASPECT_RATIO;

    public HandlerThread mBackgroundThread;

    public final SizeMap mPreviewSizes = new SizeMap();
    public final Semaphore mCameraOpenCloseLock = new Semaphore(1);
    public CameraCaptureSession mPreviewSession;

    public MediaRecorder mMediaRecorder;

    public CameraDevice mCameraDevice;

    public ImageReader mImageReader;

    public  SurfaceTexture texture;

    public final int STATE_PICTURE_TAKEN = 4;

    public final int REQUEST_CAMERA_PERMISSION = 1;

    public static final SparseIntArray INTERNAL_FACINGS = new SparseIntArray();

     static {
        INTERNAL_FACINGS.put(CameraConstants.FACING_BACK, CameraCharacteristics.LENS_FACING_BACK);
        INTERNAL_FACINGS.put(CameraConstants.FACING_FRONT, CameraCharacteristics.LENS_FACING_FRONT);
    }


    public int mFacing = CameraConstants.FACING_BACK;

    public boolean mFacingSupported = true;

    private ArrayList<String> thumbnails = new ArrayList<>();


    private Activity activity;
    ActivityCamera2MedlyBinding activityBinding;

    private static final int STATE_PREVIEW = 0;

    public int mState = STATE_PREVIEW;

    public Size mPreviewSize;

    public boolean mFlashSupported = true;

    public int mFlash = CameraConstants.FLASH_OFF;

    public String mNextVideoAbsolutePath;

    public boolean mAutoFocus = true;

    public CaptureRequest.Builder mPreviewRequestBuilder;

    public CameraCharacteristics mCameraCharacteristics;

    public Rect mCropRegion;

    public MeteringRectangle[] mAFRegions = AutoFocusHelper.getZeroWeightRegion();

    public MeteringRectangle[] mAERegions = AutoFocusHelper.getZeroWeightRegion();

    public static final int MAX_PREVIEW_WIDTH = 1920;

    public static final int MAX_PREVIEW_HEIGHT = 1080;

    public boolean mAutoFocusSupported = true;

    public boolean mIsRecordingVideo;

    public static int screenHeight;
    public static int screenWidth;

    public  int mw;
    public  int mh;

    public CameraFragmentViewModel(Activity activity, Context mContext, FragmentCamera2MedlyBinding binding, ActivityCamera2MedlyBinding activityBinding, Camera2Fragment camera2Fragment) {
        this.activity  = activity;
        this.context = mContext;
        this.binding = binding;
        this.camera2Fragment = camera2Fragment;
        this.activityBinding = activityBinding;
    }

    public Size choosePictureSize(Size[] choices) {
        List<Size> pictureSizes = Arrays.asList(choices);
        Collections.sort(pictureSizes, new Camera2Fragment.CompareSizesByArea());
        int maxIndex = pictureSizes.size() - 1;
        for (int i = maxIndex; i >= 0; i--) {
            if (pictureSizes.get(i).getWidth() == pictureSizes.get(i).getHeight() *
                    mAspectRatio.getX() / mAspectRatio.getY()) {

                return pictureSizes.get(i);

            }
        }
        return pictureSizes.get(maxIndex);
    }

    public Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                           int textureViewHeight, int maxWidth, int maxHeight) {
        mPreviewSizes.clear();
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w;
        int h;

        w = mAspectRatio.getX();
        h = mAspectRatio.getY();

        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight) {
                mPreviewSizes.add(new com.meest.medley_camera2.camera2.utills.Size(option.getWidth(), option.getHeight()));
                if (option.getHeight() == option.getWidth() * h / w) {
                    if (option.getWidth() >= textureViewWidth &&
                            option.getHeight() >= textureViewHeight) {

                        bigEnough.add(option);
                    } else {

                        notBigEnough.add(option);
                    }
                }
            }
        }


        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {

            return Collections.min(bigEnough, new Camera2Fragment.CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {

            return Collections.max(notBigEnough, new Camera2Fragment.CompareSizesByArea());
        } else {
            mAspectRatio = AspectRatio.of(4, 3);
            SortedSet<com.meest.medley_camera2.camera2.utills.Size> sortedSet = mPreviewSizes.sizes(mAspectRatio);
            if (sortedSet != null) {
                com.meest.medley_camera2.camera2.utills.Size lastSize = sortedSet.last();
                return new Size(lastSize.getWidth(), lastSize.getHeight());
            }
            mAspectRatio = AspectRatio.of(choices[0].getWidth(), choices[0].getHeight());

            return choices[0];
        }
    }

    public void closeCamera() {
        try {
           mCameraOpenCloseLock.acquire();
            if (null != mPreviewSession) {
                mPreviewSession.close();
                mPreviewSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mMediaRecorder) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    public void setFocusViewWidthAnimation(float x, float y) {

        binding.focusViewLayout.setVisibility(View.VISIBLE);


        binding.focusViewLayout.setX(x - binding.focusViewLayout.getWidth() / 2);
        binding.focusViewLayout.setY(y - binding.focusViewLayout.getHeight() / 2);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.focusViewLayout, "scaleX", 1, 0.6f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.focusViewLayout, "scaleY", 1, 0.6f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(binding.focusViewLayout, "alpha", 1f, 0.3f, 1f, 0.3f, 1f, 0.3f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.focusViewLayout.setVisibility(View.GONE);
            }
        });
        animSet.play(scaleX).with(scaleY).before(alpha);
        animSet.setDuration(300);
        animSet.start();
    }


    public void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final int permission_camera = PermissionChecker.checkSelfPermission(context, Manifest.permission.CAMERA);
            final int record_audio = PermissionChecker.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);
            final int permission_readexternal = PermissionChecker.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
            final int permission_writeexternal = PermissionChecker.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission_camera == PermissionChecker.PERMISSION_GRANTED
                    && permission_readexternal == PermissionChecker.PERMISSION_GRANTED
                    && permission_writeexternal == PermissionChecker.PERMISSION_GRANTED
                    && record_audio == PermissionChecker.PERMISSION_GRANTED) {


            } else {

                checkPermissions();
            }
        }
    }

    public boolean checkPermissions() {

        int camera = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        int record_audio = ContextCompat.checkSelfPermission(context,
                Manifest.permission.RECORD_AUDIO);

        int permission_readexternal = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        int permission_writeexternal = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (record_audio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (permission_readexternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (permission_readexternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (permission_writeexternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CAMERA_PERMISSION);
            return false;
        }

        return true;
    }


    public void getFirstImageFromGallery()
    {
        Handler handler = new Handler();
        Runnable runnable = () -> {
            try {
                thumbnails = ImagesGallery.listOfImages(activity);
                if (thumbnails.size()>0)
                {
                    activity.runOnUiThread(() -> Glide.with(activity)
                            .load(thumbnails.get(0))
                            .centerCrop()
                            .into(activityBinding.gallery));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public void configureTransform(int viewWidth, int viewHeight) {

        if (null == binding.cameraTexture || null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }

        binding.cameraTexture.setTransform(matrix);
    }

    public void updateFlash(CaptureRequest.Builder requestBuilder) {
        if (!mFlashSupported) {
            return;
        }

        switch (mFlash) {

            case CameraConstants.FLASH_OFF:
                requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON);
                requestBuilder.set(CaptureRequest.FLASH_MODE,
                        CaptureRequest.FLASH_MODE_OFF);
                break;
            case CameraConstants.FLASH_ON:
                requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
                requestBuilder.set(CaptureRequest.FLASH_MODE,
                        CaptureRequest.FLASH_MODE_OFF);
                break;
            case CameraConstants.FLASH_TORCH:
                requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON);
                requestBuilder.set(CaptureRequest.FLASH_MODE,
                        CaptureRequest.FLASH_MODE_TORCH);

                break;
            case CameraConstants.FLASH_AUTO:
                requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                requestBuilder.set(CaptureRequest.FLASH_MODE,
                        CaptureRequest.FLASH_MODE_OFF);
                break;
            case CameraConstants.FLASH_RED_EYE:
                requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH_REDEYE);
                requestBuilder.set(CaptureRequest.FLASH_MODE,
                        CaptureRequest.FLASH_MODE_OFF);
                break;
        }
    }


    public String getVideoFilePath() {
        final File dir = context.getExternalFilesDir(null);
        return (dir == null ? "" : (dir.getAbsolutePath() + "/"))
                + System.currentTimeMillis() + ".mp4";
    }

    public void updateManualFocus(float x, float y,CameraCharacteristics mCameraCharacteristics) {
        @SuppressWarnings("ConstantConditions")
        int sensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        mAFRegions = AutoFocusHelper.afRegionsForNormalizedCoord(x, y, mCropRegion, sensorOrientation);
        mAERegions = AutoFocusHelper.aeRegionsForNormalizedCoord(x, y, mCropRegion, sensorOrientation);
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, mAFRegions);
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_REGIONS, mAERegions);
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
    }

    public void updateAutoFocus() {
        if (mAutoFocus) {
            if (!mAutoFocusSupported) {
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                        CaptureRequest.CONTROL_AF_MODE_OFF);
            } else {
                if (mIsRecordingVideo) {
                    mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_VIDEO);
                } else {
                    mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                }
            }
        } else {
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                    CaptureRequest.CONTROL_AF_MODE_OFF);
        }
        mAFRegions = AutoFocusHelper.getZeroWeightRegion();
        mAERegions = AutoFocusHelper.getZeroWeightRegion();
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, mAFRegions);
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_REGIONS, mAERegions);
    }

    public Size chooseVideoSize(Size[] choices) {
        List<Size> videoSizes = Arrays.asList(choices);
        List<Size> supportedVideoSizes = new ArrayList<>();
        Collections.sort(videoSizes, new Camera2Fragment.CompareSizesByArea());
        for (int i = videoSizes.size() - 1; i >= 0; i--) {
            if (videoSizes.get(i).getWidth() <= MAX_PREVIEW_WIDTH &&
                    videoSizes.get(i).getHeight() <= MAX_PREVIEW_HEIGHT) {
                supportedVideoSizes.add(videoSizes.get(i));
                if (videoSizes.get(i).getWidth() == videoSizes.get(i).getHeight() *
                        mAspectRatio.getX() / mAspectRatio.getY()) {
                    return videoSizes.get(i);
                }
            }
        }
        return supportedVideoSizes.size() > 0 ? supportedVideoSizes.get(0) : choices[choices.length - 1];
    }


    //Layout Grid Code:-

    public void initGridLayoutViews() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

    }


    public void grid2VideoConditionH() {
        activity.runOnUiThread(() -> {
            CameraUtil.twoGridValue++;
            if (CameraUtil.twoGridValue == 1) {
                setVideoThumbnail(binding.twoGridImage1Horizontal, mNextVideoAbsolutePath);
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(binding.cameraTexture, "translationX", 0, (float) screenWidth / 2);
                anim1.setDuration(200);
                anim1.start();
                binding.twoGridImage2Horizontal.setBackgroundResource(0);
                setAlpha(binding.twoGridImage2Horizontal);
            } else if (CameraUtil.twoGridValue == 2) {
                setVideoThumbnail(binding.twoGridImage2Horizontal, mNextVideoAbsolutePath);
                activityBinding.checkImage.setImageResource(R.drawable.ic_outline_check_24);

            }

        });

    }

    private void setAlpha(View v) {
        v.setAlpha(1);
    }

    public void setVideoThumbnail(ImageView imageView, String path) {
        Bitmap bmThumbnail;
        bmThumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
        bmThumbnail = CameraUtil.getResizedBitmap(bmThumbnail, mw, mh);
        imageView.setImageBitmap(bmThumbnail);
    }



}

