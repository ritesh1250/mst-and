package com.meest.medley_camera2.camera2.viewmodels;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.lifecycle.ViewModel;

import com.meest.databinding.ActivityCamera2MetmeBinding;
import com.meest.databinding.MeestFragmentCamera2Binding;
import com.meest.medley_camera2.camera2.cameraUtils.AspectRatio;
import com.meest.medley_camera2.camera2.cameraUtils.AutoFocusHelper;
import com.meest.medley_camera2.camera2.utills.CameraConstants;
import com.meest.medley_camera2.camera2.utills.CameraUtil;
import com.meest.medley_camera2.camera2.utills.SizeMap;
import com.meest.medley_camera2.camera2.view.fragment.MeestCamera2Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;


public class MeestCameraFragmentViewModel extends ViewModel {

    public Context context;
    MeestFragmentCamera2Binding binding;
    MeestCamera2Fragment meestCamera2Fragment;
    ActivityCamera2MetmeBinding activityBinding;
    public AspectRatio mAspectRatio = CameraConstants.DEFAULT_ASPECT_RATIO;

    public HandlerThread mBackgroundThread;

    public static final int MSG_CAPTURE_PICTURE_WHEN_FOCUS_TIMEOUT = 100;
    public final SizeMap mPreviewSizes = new SizeMap();

    public final Semaphore mCameraOpenCloseLock = new Semaphore(1);

    public CameraCaptureSession mPreviewSession;

    public CaptureRequest mPreviewRequest;

    public MediaRecorder mMediaRecorder;

    public CameraDevice mCameraDevice;

    public ImageReader mImageReader;

    public int mSensorOrientation;

    public static SurfaceTexture texture;

    public Handler mBackgroundHandler;

    public static final int STATE_PICTURE_TAKEN = 4;

    public static final int REQUEST_CAMERA_PERMISSION = 1;

    public static final SparseIntArray INTERNAL_FACINGS = new SparseIntArray();

    static {
        INTERNAL_FACINGS.put(CameraConstants.FACING_BACK, CameraCharacteristics.LENS_FACING_BACK);
        INTERNAL_FACINGS.put(CameraConstants.FACING_FRONT, CameraCharacteristics.LENS_FACING_FRONT);
    }

    public int mFacing = CameraConstants.FACING_BACK;

    public boolean mFacingSupported = true;

    public ArrayList<String> thumbnails = new ArrayList<>();

    public static final int STATE_PREVIEW = 0;

    public Size mPreviewSize;

    public boolean mFlashSupported = true;

    public int mFlash = CameraConstants.FLASH_OFF;

    public String mNextVideoAbsolutePath;

    public boolean mAutoFocus = true;

    public CaptureRequest.Builder mPreviewRequestBuilder;


    public Rect mCropRegion;
    public MeteringRectangle[] mAFRegions = AutoFocusHelper.getZeroWeightRegion();

    public MeteringRectangle[] mAERegions = AutoFocusHelper.getZeroWeightRegion();

    public static final int MAX_PREVIEW_WIDTH = 1920;

    public static final int MAX_PREVIEW_HEIGHT = 1080;

    public boolean mAutoFocusSupported = true;
    public int mState = STATE_PREVIEW;

    private static final int STATE_WAITING_PRECAPTURE = 2;

    private static final int STATE_WAITING_NON_PRECAPTURE = 3;

    public static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    public static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();
    public static final int STATE_WAITING_LOCK = 1;

    public boolean mIsRecordingVideo;
    public static int screenHeight;
    public static int screenWidth;
    public Activity activity;
    ActivityCamera2MetmeBinding activitybinding;
    private static final String[] VIDEO_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    static {
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    static {
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
    }
    public MeestCameraFragmentViewModel(Context mContext, Activity activity, MeestFragmentCamera2Binding binding, ActivityCamera2MetmeBinding activitybinding, MeestCamera2Fragment meestCamera2Fragment) {
        this.activity = activity;
        this.context = mContext;
        this.binding = binding;
        this.meestCamera2Fragment = meestCamera2Fragment;
        this.activitybinding = activitybinding;

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
            ActivityCompat.requestPermissions(binding.getViewModel().activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CAMERA_PERMISSION);
            return false;
        }

        return true;
    }
    public void gridLayoutTwo() {

        CameraUtil.gridLayout2Active = true;
        binding.grid2Layout.setVisibility(View.VISIBLE);
    }


    public final CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {

        private void process(CaptureResult result) {
            //  Log.i(TAG, "CaptureCallback mState: " + mState);
            switch (mState) {
                case STATE_PREVIEW: {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                            CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // CONTROL_AE_STATE can be null on some devices
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                        @NonNull CaptureRequest request,
                                        @NonNull CaptureResult partialResult) {
            process(partialResult);
        }

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
            process(result);
        }
    };
    public void captureStillPicture() {
        try {
            removeCaptureMessage();
            
            if (null == activity || null == mCameraDevice) {
                return;
            }
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            updateFlash(captureBuilder);
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            CameraCaptureSession.CaptureCallback CaptureCallback = new CameraCaptureSession.CaptureCallback() {

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                               @NonNull CaptureRequest request,
                                               @NonNull TotalCaptureResult result) {
                    unlockFocus();
                }
            };

            mPreviewSession.stopRepeating();
            mPreviewSession.capture(captureBuilder.build(), CaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    public void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE;
            mPreviewSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

       public void removeCaptureMessage() {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.removeMessages(MSG_CAPTURE_PICTURE_WHEN_FOCUS_TIMEOUT);
        }
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

    private void unlockFocus() {
        try {
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            mPreviewSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,mBackgroundHandler);

            updateAutoFocus();
            updateFlash(mPreviewRequestBuilder);
            mState = STATE_PREVIEW;
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
            mPreviewSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback,mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
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
    private int getOrientation(int rotation) {
        return (DEFAULT_ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    public void showToast(final String text) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setFocusViewWidthAnimation(float x, float y) {
        binding.focusView.setVisibility(View.VISIBLE);

        binding.focusView.setX(x - binding.focusView.getWidth() / 2);
        binding.focusView.setY(y - binding.focusView.getHeight() / 2);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.focusView, "scaleX", 1, 0.6f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.focusView, "scaleY", 1, 0.6f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(binding.focusView, "alpha", 1f, 0.3f, 1f, 0.3f, 1f, 0.3f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                binding.focusView.setVisibility(View.GONE);
            }
        });
        animSet.play(scaleX).with(scaleY).before(alpha);
        animSet.setDuration(300);
        animSet.start();
    }
    public void createCameraPreviewSession() {
        try {
            texture = binding.texture.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            Surface surface = new Surface(texture);

            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            if (null == mCameraDevice) {
                                return;
                            }

                            mPreviewSession = cameraCaptureSession;
                            try {

                                updateAutoFocus();
                                updateFlash(mPreviewRequestBuilder);
                                mPreviewRequest = mPreviewRequestBuilder.build();

                                mPreviewSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback,  mBackgroundHandler);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            showToast("Create preview configure failed");
                        }
                    }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
