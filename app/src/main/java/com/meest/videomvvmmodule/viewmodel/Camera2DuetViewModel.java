package com.meest.videomvvmmodule.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.MeteringRectangle;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.meest.R;
import com.meest.databinding.ActivityCamera2DuetBinding;
import com.meest.databinding.DailogProgressBinding;
import com.meest.videomvvmmodule.only_camera2.Camera2DuetActivity;
import com.meest.videomvvmmodule.only_camera2.camera2.cameraUtils.AspectRatio;
import com.meest.videomvvmmodule.only_camera2.camera2.cameraUtils.AutoFocusHelper;
import com.meest.videomvvmmodule.only_camera2.camera2.utills.CameraConstants;
import com.meest.videomvvmmodule.only_camera2.camera2.utills.CameraUtil;
import com.meest.videomvvmmodule.only_camera2.camera2.utills.SizeMap;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.recordvideo.DuoActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.Semaphore;

public class Camera2DuetViewModel extends ViewModel {
    public Context context;
    public ActivityCamera2DuetBinding binding;

    public AspectRatio mAspectRatio = CameraConstants.DEFAULT_ASPECT_RATIO;

    public HandlerThread mBackgroundThread;

    public final SizeMap mPreviewSizes = new SizeMap();
    public final Semaphore mCameraOpenCloseLock = new Semaphore(1);
    public CameraCaptureSession mPreviewSession;

    public MediaRecorder mMediaRecorder;

    public CameraDevice mCameraDevice;

    public ImageReader mImageReader;

    public SurfaceTexture texture;

    public final int STATE_PICTURE_TAKEN = 4;

    public final int REQUEST_CAMERA_PERMISSION = 1;

    public static final SparseIntArray INTERNAL_FACINGS = new SparseIntArray();

    static {
        INTERNAL_FACINGS.put(CameraConstants.FACING_BACK, CameraCharacteristics.LENS_FACING_BACK);
        INTERNAL_FACINGS.put(CameraConstants.FACING_FRONT, CameraCharacteristics.LENS_FACING_FRONT);
    }


    public int mFacing = CameraConstants.FACING_BACK;

    public boolean mFacingSupported = true;
    public int count=0;

    private ArrayList<String> thumbnails = new ArrayList<>();

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

    public int mw;
    public int mh;

    ActivityCamera2DuetBinding activityBinding;

    public Size choosePictureSize(Size[] choices) {
        List<Size> pictureSizes = Arrays.asList(choices);
        Collections.sort(pictureSizes, new CompareSizesByArea());
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
                mPreviewSizes.add(new com.meest.videomvvmmodule.only_camera2.camera2.utills.Size(option.getWidth(), option.getHeight()));
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

            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {

            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            mAspectRatio = AspectRatio.of(4, 3);
            SortedSet<com.meest.videomvvmmodule.only_camera2.camera2.utills.Size> sortedSet = mPreviewSizes.sizes(mAspectRatio);
            if (sortedSet != null) {
                com.meest.videomvvmmodule.only_camera2.camera2.utills.Size lastSize = sortedSet.last();
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

    public void configureTransform(int viewWidth, int viewHeight) {

        if (null == binding.cameraTexture || null == mPreviewSize || null == context) {
            return;
        }
        int rotation = ((Activity) context).getWindowManager().getDefaultDisplay().getRotation();
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

    public void updateManualFocus(float x, float y, CameraCharacteristics mCameraCharacteristics) {
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
        Collections.sort(videoSizes, new CompareSizesByArea());
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

    public void setVideoThumbnail(ImageView imageView, String path) {
        Bitmap bmThumbnail;
        bmThumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
        bmThumbnail = CameraUtil.getResizedBitmap(bmThumbnail, mw, mh);
        imageView.setImageBitmap(bmThumbnail);
    }


    /**
     * Compares two {@code Size}s based on their areas.
     */
    public static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    private static final String TAG = "CameraActivityViewModel";
    public ObservableInt soundTextVisibility = new ObservableInt(View.INVISIBLE);
    public MutableLiveData<Long> onDurationUpdate = new MutableLiveData<>(15000L);
    public ObservableBoolean videoClickChk = new ObservableBoolean(false);
    public String parentPath = "";
    public MediaPlayer audio;
    public ObservableBoolean isEnabled = new ObservableBoolean(false);
    public ArrayList<String> videoPaths = new ArrayList<>();
    public long duration = 15000;
    public String soundId = "";

    private Dialog mBuilder;

    private DailogProgressBinding progressBinding;

    public void createAudioForCamera(SessionManager sessionManager, Camera2DuetActivity activity) {
        String parentPath = activity.getPath().getPath();
        File file = new File(parentPath.concat("/Medley_" + sessionManager.getUser().getData().getUserName() + "_" + soundId + ".mp3"));
        if (file.exists()) {
            audio = new MediaPlayer();
            try {
                audio.setDataSource(parentPath.concat("/Medley_" + sessionManager.getUser().getData().getUserName() + "_" + soundId + ".mp3"));
                audio.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("path=======:", "null");
        }
    }

    public boolean isRecordingVideo() {
        return mIsRecordingVideo;
    }

    public File getPath() {
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            filesDir = ((Activity)context).getExternalFilesDir(null);
        } else {
            // Load another directory, probably local memory
            filesDir = ((Activity)context).getFilesDir();
        }
        if (filesDir != null) {
            parentPath = filesDir.getPath();
        }
        return filesDir;
    }

    public void initProgressDialog() {
        mBuilder = new Dialog(context);
        mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        if (mBuilder.getWindow() != null) {
            mBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        progressBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dailog_progress, null, false);
        progressBinding.progressBar.setShader(new int[]{ContextCompat.getColor(context, R.color.colorTheme), ContextCompat.getColor(context, R.color.colorTheme), ContextCompat.getColor(context, R.color.colorTheme)});
//        Glide.with(this)
//                .load(R.drawable.loader_gif)
//                .into(progressBinding.ivParent);

        mBuilder.setContentView(progressBinding.getRoot());
    }

    public void disableOptions() {
        if (CameraUtil.bitmap != null) {
            CameraUtil.bitmap.recycle();
            CameraUtil.bitmap = null;
        }
        CameraUtil.currentCamera = "Photo";

        CameraUtil.twoGridValue = 0;
        CameraUtil.threeGridValue = 0;
        CameraUtil.fourGridValue = 0;
        CameraUtil.sixGridValue = 0;
        CameraUtil.twoGridValueHorizontal = 0;

        CameraUtil.previewComplete = false;
        CameraUtil.gridLayoutActive = false;

        CameraUtil.gridLayout2Active = false;
        CameraUtil.gridLayout3Active = false;
        CameraUtil.gridLayout4Active = false;
        CameraUtil.gridLayout6Active = false;
        CameraUtil.gridLayout2ActiveHorizontal = false;

        CameraUtil.gridLayoutHVideoActive = false;

        CameraUtil.gridCamera = false;
        CameraUtil.superZoomEnable = false;
        CameraUtil.handsFreeEnable = false;
        CameraUtil.multiSnapEnable = false;
        CameraUtil.timerEnable = false;
        CameraUtil.isRunningTimer = false;
        CameraUtil.speedEnable = false;
        CameraUtil.aspectRatioEnable = false;


        CameraUtil.focusEnable = false;
        CameraUtil.speed = "Normal";
        CameraUtil.imageUri = null;

    }

    public int absVal(int value) {
        if (value < 0)
            value = (-1 * value);
        return value;

    }

    public void hideLayouts() {
        binding.switchCamera.setVisibility(View.GONE);
        CameraUtil.progressTimer = false;
        binding.segmentedProgressBar.setVisibility(View.GONE);

        if (binding.gallery.getVisibility() == View.VISIBLE) {
            binding.gallery.setVisibility(View.GONE);
            binding.cardViewParentLayout.setVisibility(View.GONE);
        }


        if (binding.video.getVisibility() == View.VISIBLE)
            binding.video.setVisibility(View.GONE);

    }

    public void showLayouts() {

        binding.switchCamera.setVisibility(View.VISIBLE);
        binding.segmentedProgressBar.publishProgress(0);
        binding.video.setImageResource(R.drawable.ic_record_start);
        binding.cardViewParentLayout.setVisibility(View.VISIBLE);
//        binding.gallery.setVisibility(View.VISIBLE);
        binding.video.setVisibility(View.VISIBLE);
        CameraUtil.progressTimer = true;
        binding.segmentedProgressBar.setVisibility(View.VISIBLE);
    }
}
