
package com.meest.medley_camera2.camera2.view.fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.math.MathUtils;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.meest.R;
import com.meest.databinding.ActivityCamera2MedlyBinding;
import com.meest.databinding.DailogProgressBinding;
import com.meest.databinding.FragmentCamera2Binding;
import com.meest.databinding.FragmentCamera2MedlyBinding;
import com.meest.medley_camera2.camera2.cameraUtils.AspectRatio;
import com.meest.medley_camera2.camera2.cameraUtils.AutoFocusHelper;
import com.meest.medley_camera2.camera2.utills.CameraConstants;
import com.meest.medley_camera2.camera2.utills.CameraUtil;
import com.meest.medley_camera2.camera2.view.activity.CameraActivity;
import com.meest.medley_camera2.camera2.view.activity.MeestCameraActivity;
import com.meest.medley_camera2.camera2.view.activity.PreviewVideoActivity;
import com.meest.medley_camera2.camera2.viewmodels.CameraFragmentViewModel;
import com.meest.meestbhart.utilss.Constant;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.TranscoderOptions;
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategy;
import com.otaliastudios.transcoder.strategy.size.AtMostResizer;
import com.otaliastudios.transcoder.strategy.size.FractionResizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Camera2Fragment extends Fragment {
    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;

    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();

    private static final String FRAGMENT_DIALOG = "dialog";

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

    /**
     * Tag for the {@link Log}.
     */
    private static final String TAG = "Camera2Fragment";

    private static final int MSG_CAPTURE_PICTURE_WHEN_FOCUS_TIMEOUT = 100;


    /**
     * ID of the current {@link CameraDevice}.
     */
    private String mCameraId;

    private CaptureRequest mPreviewRequest;

    public CameraCharacteristics mCameraCharacteristics;

    /**
     * Orientation of the camera sensor
     */
    private int mSensorOrientation;

    /**
     * The {@link Size} of camera preview.
     */

    /**
     * The {@link Size} of video recording.
     */
    private Size mVideoSize;


    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler mBackgroundHandler;

    /**
     * An {@link ImageReader} that handles still image capture.
     */


    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
     * {@link TextureView}.
     */
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            // Log.e(TAG, "onSurfaceTextureAvailable: "+width+":"+height);
            // Log.e(TAG, "onSurfaceTextureAvailable: "+CameraUtil.fsAspectRatio);
            openCamera(width, height);

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {


            viewModel.configureTransform(width, height);


        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            binding.cameraTexture.setSurfaceTextureListener(null);
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };


    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its state.
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            viewModel.mCameraOpenCloseLock.release();
            viewModel.mCameraDevice = cameraDevice;

            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            viewModel.mCameraOpenCloseLock.release();
            cameraDevice.close();
            viewModel.mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            viewModel.mCameraOpenCloseLock.release();
            cameraDevice.close();
            viewModel.mCameraDevice = null;
            Activity activity = getActivity();
            if (null != activity) {
                showToast("Camera is error: " + error);
                activity.finish();
            }
        }

    };


    private Activity mContext;

    @Override
    public void onAttach(Activity mContext) {
        super.onAttach(mContext);

        this.mContext = mContext;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    /**
     * A {@link CameraCaptureSession.CaptureCallback} that handles events related to JPEG capture.
     */
    private final CameraCaptureSession.CaptureCallback mCaptureCallback
            = new CameraCaptureSession.CaptureCallback() {
    };

    /**
     * Shows a {@link Toast} on the UI thread.
     *
     * @param text The message to show
     */
    private void showToast(final String text) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * We choose a largest picture size with mAspectRatio
     */

    public static Camera2Fragment newInstance() {

        return new Camera2Fragment();
    }


    FragmentCamera2MedlyBinding binding;
    CameraFragmentViewModel viewModel;


    ActivityCamera2MedlyBinding activityBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera2_medly, container, false);
        activityBinding = ((CameraActivity)mContext).binding;

        viewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity(), new ViewModelFactory(new CameraFragmentViewModel(mContext,getActivity(), binding,activityBinding, new Camera2Fragment())).createFor()).get(CameraFragmentViewModel.class);
        binding.setViewModel(viewModel);
        initProgressDialog();
        viewModel.initGridLayoutViews();


        return binding.getRoot();
    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {


        binding.cameraTexture.setGestureListener(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (CameraUtil.focusEnable) {
                    viewModel.setFocusViewWidthAnimation((int) e.getX(), (int) e.getY());
                    setManualFocusAt((int) e.getX(), (int) e.getY());
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isPaused = false;
        start();

    }
    public boolean isPaused = false;
    @Override
    public void onPause() {

        isPaused = true;
        if (isRecordingVideo())
        {
            stopRecordingVideo();
        }

        stop();
        super.onPause();
    }

    public void start() {

        startBackgroundThread();

        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).


        if (binding.cameraTexture.isAvailable()) {

            openCamera(binding.cameraTexture.getWidth(), binding.cameraTexture.getHeight());
        } else {
            binding.cameraTexture.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    private void startBackgroundThread() {
        viewModel.mBackgroundThread = new HandlerThread("CameraBackground");
        viewModel.mBackgroundThread.start();
        mBackgroundHandler = new Handler(viewModel.mBackgroundThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_CAPTURE_PICTURE_WHEN_FOCUS_TIMEOUT) {
                    viewModel.mState = viewModel.STATE_PICTURE_TAKEN;

                }

            }
        };
    }


    public void stop() {
        viewModel.closeCamera();
        stopBackgroundThread();
    }

    private void stopBackgroundThread() {


        try {
            viewModel.mBackgroundThread.quitSafely();
            viewModel.mBackgroundThread.join();
            viewModel.mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == viewModel.REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "Permission Accepted 3: ");

            } else {
                Log.e(TAG, "Permission Accepted 4: ");
            }
        }
    }


    /**
     * Setup member variables related to camera.
     *
     * @param width  The width of available size for camera preview
     * @param height The height of available size for camera preview
     */
    private void setupCameraOutputs(int width, int height) {
        Activity activity = getActivity();
        int internalFacing = CameraFragmentViewModel.INTERNAL_FACINGS.get(viewModel.mFacing);
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIds = manager.getCameraIdList();
            viewModel.mFacingSupported = cameraIds.length > 1;
            for (String cameraId : cameraIds) {
                mCameraCharacteristics
                        = manager.getCameraCharacteristics(cameraId);

                Integer facing = mCameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing == null || facing != internalFacing) {
                    continue;
                }

                StreamConfigurationMap map = mCameraCharacteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                // Find out if we need to swap dimension to get the preview size relative to sensor
                // coordinate.
                int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();


                //noinspection ConstantConditions
                mSensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                    default:
                        Log.e(TAG, "Display rotation is invalid: " + displayRotation);
                }

                Point displaySize = new Point();
                activity.getWindowManager().getDefaultDisplay().getRealSize(displaySize);


                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;

                viewModel.mw = maxPreviewWidth;
                viewModel.mh = maxPreviewHeight;



                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }

                if (maxPreviewWidth > viewModel.MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = viewModel.MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > viewModel.MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = viewModel.MAX_PREVIEW_HEIGHT;
                }

                // Danger, W.R.! Attempting to use too large a preview size could exceed the camera
                // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
                // garbage capture data.


                viewModel.mPreviewSize = viewModel.chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                        maxPreviewHeight);


                mVideoSize = viewModel.chooseVideoSize(map.getOutputSizes(MediaRecorder.class));

                // For still image captures, we use the largest available size.
                Size largest = viewModel.choosePictureSize(map.getOutputSizes(ImageFormat.JPEG));


                Log.e(TAG, "largest: " + largest.toString());
                viewModel.mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                        ImageFormat.JPEG, /*maxImages*/2);

                // We fit the aspect ratio of TextureView to the size of preview we picked.
                int orientation = getResources().getConfiguration().orientation;





                binding.cameraTexture.setAspectRatio(
                        viewModel.mPreviewSize.getHeight(), viewModel.mPreviewSize.getWidth());


                checkAutoFocusSupported();
                checkFlashSupported();

                viewModel.mCropRegion = AutoFocusHelper.cropRegionForZoom(mCameraCharacteristics,
                        CameraConstants.ZOOM_REGION_DEFAULT);

                mCameraId = cameraId;
                Log.i(TAG, "CameraId: " + mCameraId + " ,isFlashSupported: " + viewModel.mFlashSupported);
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            ErrorDialog.newInstance(getString(R.string.camera_error))
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        }
    }

    /**
     * Check if the auto focus is supported.
     */
    private void checkAutoFocusSupported() {
        int[] modes = mCameraCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        viewModel.mAutoFocusSupported = !(modes == null || modes.length == 0 ||
                (modes.length == 1 && modes[0] == CameraCharacteristics.CONTROL_AF_MODE_OFF));
    }

    /**
     * Check if the flash is supported.
     */
    private void checkFlashSupported() {
        Boolean available = mCameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
        viewModel.mFlashSupported = available != null && available;
    }

    /**
     * Opens the camera specified by {@link Camera2Fragment#mCameraId}.
     */
    @SuppressWarnings("MissingPermission")
    public void openCamera(int width, int height) {
        if (!viewModel.checkPermissions()) {
            viewModel.requestCameraPermission();
            return;
        }


//        mw = width;
//        mh = height;
        Log.e(TAG, "openCamera width: " + width);
        Log.e(TAG, "openCamera height: " + height);
//        if (CameraUtil.oneTime) {
//            Log.e(TAG, "oneTime IF: ");
//            CameraUtil.oneTime = false;
//            final Set<AspectRatio> ratios = getSupportedAspectRatios();
//            if (ratios != null) {
//                if (ratios.size() > 0) {
//                    CameraUtil.fsAspectRatio = Collections.max(ratios);
//                    setAspectRatio(CameraUtil.fsAspectRatio);
//                    CameraConstants.DEFAULT_ASPECT_RATIO = CameraUtil.fsAspectRatio;
//                    Log.e(TAG, "fsAspectRatio: "+CameraUtil.fsAspectRatio);
//                }
//            }
//        }

        setupCameraOutputs(width, height);
        viewModel.configureTransform(width, height);
        Activity activity = getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!viewModel.mCameraOpenCloseLock.tryAcquire(CameraConstants.OPEN_CAMERA_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            viewModel.mMediaRecorder = new MediaRecorder();
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
//            viewModel.getFirstImageFromGallery();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    /**
     * Creates a new {@link CameraCaptureSession} for camera preview.
     */

    private void createCameraPreviewSession() {
        try {
            viewModel.texture = binding.cameraTexture.getSurfaceTexture();
            assert  viewModel.texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            viewModel.texture.setDefaultBufferSize(viewModel.mPreviewSize.getWidth(), viewModel.mPreviewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface( viewModel.texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            viewModel.mPreviewRequestBuilder = viewModel.mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            viewModel.mPreviewRequestBuilder.addTarget(surface);

            // Here, we create a CameraCaptureSession for camera preview.
            viewModel.mCameraDevice.createCaptureSession(Arrays.asList(surface, viewModel.mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == viewModel.mCameraDevice) {
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            viewModel.mPreviewSession = cameraCaptureSession;
                            try {
                                // Auto focus should be continuous for camera preview.
//                            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
//                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                viewModel.updateAutoFocus();
                                // Flash is automatically enabled when necessary.
                                viewModel.updateFlash(viewModel.mPreviewRequestBuilder);

                                // Finally, we start displaying the camera preview.
                                mPreviewRequest = viewModel.mPreviewRequestBuilder.build();

                                viewModel.mPreviewSession.setRepeatingRequest(mPreviewRequest,
                                        mCaptureCallback, mBackgroundHandler);

                            } catch (Exception e) {
                                Log.e(TAG, "Exception: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }

//                        @Override
//                        public void onClosed(@NonNull CameraCaptureSession session) {
//                            super.onClosed(session);
//                            stopBackgroundThread();
//                        }

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

    public Set<AspectRatio> getSupportedAspectRatios() {

        return viewModel.mPreviewSizes.ratios();

    }

    public AspectRatio getAspectRatio() {

        return viewModel.mAspectRatio;
    }

    public void setAspectRatio(AspectRatio aspectRatio) {
        if (aspectRatio == null || aspectRatio.equals(viewModel.mAspectRatio) ||
                !viewModel.mPreviewSizes.ratios().contains(aspectRatio)) {
            return;
        }

        viewModel.mAspectRatio = aspectRatio;
        if (isCameraOpened()) {
            stop();
            start();
        }
    }

    public boolean isCameraOpened() {
        return viewModel.mCameraDevice != null;
    }

    public void setFacing(int facing) {
        if (viewModel.mFacing == facing) {
            return;
        }
        viewModel.mFacing = facing;
        if (isCameraOpened()) {
            stop();
            start();
        }
    }

    public int getFacing() {
        return viewModel.mFacing;
    }

    /**
     * The facing is supported or not.
     */
    public boolean isFacingSupported() {
        return viewModel.mFacingSupported;
    }

    public void setFlash(int flash) {
        if (viewModel.mFlash == flash) {
            return;
        }
        int saved = viewModel.mFlash;
        viewModel.mFlash = flash;
        if (viewModel.mPreviewRequestBuilder != null) {
            viewModel.updateFlash(viewModel.mPreviewRequestBuilder);
            if (viewModel.mPreviewSession != null) {
                try {
                    viewModel.mPreviewSession.setRepeatingRequest(viewModel.mPreviewRequestBuilder.build(),
                            mCaptureCallback, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    viewModel.mFlash = saved; // Revert
                }
            }
        }
    }

    public int getFlash() {
        return viewModel.mFlash;
    }


    /**
     * The flash is supported or not.
     */
    public boolean isFlashSupported() {
        return viewModel.mFlashSupported;
    }

    public void setAutoFocus(boolean autoFocus) {
        if (viewModel.mAutoFocus == autoFocus) {
            return;
        }
        viewModel.mAutoFocus = autoFocus;
        if (viewModel.mPreviewRequestBuilder != null) {
            viewModel.updateAutoFocus();
            if (viewModel.mPreviewSession != null) {
                try {
                    viewModel.mPreviewSession.setRepeatingRequest(viewModel.mPreviewRequestBuilder.build(),
                            mCaptureCallback, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    viewModel.mAutoFocus = !viewModel.mAutoFocus; // Revert
                }
            }
        }
    }


    void setManualFocusAt(int x, int y) {
        int mDisplayOrientation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        float[] points = new float[2];
        points[0] = (float) x / binding.cameraTexture.getWidth();
        points[1] = (float) y / binding.cameraTexture.getHeight();
        Matrix rotationMatrix = new Matrix();
        rotationMatrix.setRotate(mDisplayOrientation, 0.5f, 0.5f);
        rotationMatrix.mapPoints(points);
        if (viewModel.mPreviewRequestBuilder != null) {

            viewModel.updateManualFocus(points[0], points[1],mCameraCharacteristics);
            if (viewModel.mPreviewSession != null) {
                try {
                    viewModel.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                            CaptureRequest.CONTROL_AF_TRIGGER_START);
                    viewModel.mPreviewSession.capture(viewModel.mPreviewRequestBuilder.build(), null, mBackgroundHandler);
                    viewModel.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                            CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
                    viewModel.mPreviewSession.setRepeatingRequest(viewModel.mPreviewRequestBuilder.build(),
                            null, mBackgroundHandler);
                } catch (CameraAccessException | IllegalStateException e) {
                    Log.e(TAG, "Failed to set manual focus.", e);
                }
            }
            resumeAutoFocusAfterManualFocus();
        }
    }

    private final Runnable mAutoFocusRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewModel.mPreviewRequestBuilder != null) {

                viewModel.updateAutoFocus();
                if (viewModel.mPreviewSession != null) {
                    try {
                        viewModel.mPreviewSession.setRepeatingRequest(viewModel.mPreviewRequestBuilder.build(),
                                mCaptureCallback, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        Log.e(TAG, "Failed to set manual focus.", e);
                    }
                }
            }
        }
    };

    private void resumeAutoFocusAfterManualFocus() {
        mBackgroundHandler.removeCallbacks(mAutoFocusRunnable);
        mBackgroundHandler.postDelayed(mAutoFocusRunnable, CameraConstants.FOCUS_HOLD_MILLIS);
    }

    private void setupMediaRecorder() throws IOException {
        final Activity activity = getActivity();
        if (null == activity) {
            return;
        }
        viewModel.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        viewModel.mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        viewModel.mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        viewModel.mNextVideoAbsolutePath = viewModel.getVideoFilePath();
        viewModel.mMediaRecorder.setOutputFile(viewModel.mNextVideoAbsolutePath);
        viewModel.mMediaRecorder.setVideoEncodingBitRate(1000000);
        viewModel.mMediaRecorder.setVideoFrameRate(20);
        viewModel.mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        viewModel.mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        viewModel.mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (mSensorOrientation) {

            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                viewModel.mMediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
                break;
            case SENSOR_ORIENTATION_INVERSE_DEGREES:
                viewModel.mMediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
                break;
        }
        viewModel.mMediaRecorder.prepare();
    }

    /**
     * Start recording video
     */
    public void startRecordingVideo() {

        if (null == viewModel.mCameraDevice || !binding.cameraTexture.isAvailable() || null == viewModel.mPreviewSize) {
            return;
        }
        try {
            //binding.cameraTexture.setScaleX(-1);
            viewModel.mIsRecordingVideo = true;
            setupMediaRecorder();
            SurfaceTexture texture = binding.cameraTexture.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(viewModel.mPreviewSize.getWidth(), viewModel.mPreviewSize.getHeight());
            viewModel.mPreviewRequestBuilder = viewModel.mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<>();

            // Set up Surface for the camera preview
            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            viewModel.mPreviewRequestBuilder.addTarget(previewSurface);

            // Set up Surface for the MediaRecorder
            Surface recorderSurface = viewModel.mMediaRecorder.getSurface();
            surfaces.add(recorderSurface);
            viewModel.mPreviewRequestBuilder.addTarget(recorderSurface);

            // Start a capture session
            // Once the session starts, we can update the UI and start recording
            viewModel.mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    viewModel.mPreviewSession = cameraCaptureSession;
                    try {
                        // Auto focus should be continuous for camera preview.
//                        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
//                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_VIDEO);
                        viewModel.updateAutoFocus();
                        // Flash is automatically enabled when necessary.
                        viewModel.updateFlash(viewModel.mPreviewRequestBuilder);

                        // For test
                        Integer stabilizationMode = viewModel.mPreviewRequestBuilder.
                                get(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE);
                        if (stabilizationMode != null &&
                                stabilizationMode == CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE_OFF) {
                            viewModel.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE,
                                    CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE_ON);
                        }

                        // Finally, we start displaying the camera preview.
                        mPreviewRequest = viewModel.mPreviewRequestBuilder.build();
                        viewModel.mPreviewSession.setRepeatingRequest(mPreviewRequest, null, mBackgroundHandler);

                        try {
                            updatePreview(defaultZoom);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }

                        if (CameraUtil.superZoomEnable) {
                            changeZoom();
                        }

                        getActivity().runOnUiThread(() -> {
                            // Start recording
                            viewModel.mMediaRecorder.start();
                            if (!activityBinding.animationView.isAnimating()) {
                                activityBinding.animationView.setVisibility(View.VISIBLE);
                                activityBinding.animationView.playAnimation();
                            }

                        });
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                    // binding.cameraTexture.setRotation(270);
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    showToast("Start recording video configure failed");
                }
            }, mBackgroundHandler);

            activityBinding.chronometer.setVisibility(View.VISIBLE);
            activityBinding.tvSoundTitle.setVisibility(View.GONE);
            activityBinding.chronometer.setBase(SystemClock.elapsedRealtime());
            activityBinding.chronometer.start(); // start a chronometer
            activityBinding.chronometer.setFormat("%s");

            if (CameraUtil.progressTimer && !CameraUtil.music) {
                initProgressBar();
                Log.e(TAG, "startRecordingVideo: ");
            }
//            if (CameraUtil.music) {
//                startVideoProgress();
//                Log.e(TAG, "startRecordingVideo: ");
//            }

        } catch (CameraAccessException | IOException e) {
            e.printStackTrace();
        }

    }




    /**
     * Stop recording video
     */
    public void stopRecordingVideo() {

        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {

//                try {
//
//
//                    if (mCurrentFlashIndex == 1) {
//                        Log.e(TAG, "IF Part: " + mCurrentFlashIndex);
//                        setFlash(FLASH_OPTIONS[0]);
//                        activityBinding.switchFlash.setImageResource(FLASH_ICONS[0]);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                activityBinding.segmentedProgressBar.pause();

                getActivity().runOnUiThread(() -> {
                    activityBinding.chronometer.stop();
                    activityBinding.chronometer.setVisibility(View.GONE);
                    if (activityBinding.animationView.isAnimating()) {
                        activityBinding.animationView.cancelAnimation();
                        activityBinding.animationView.setVisibility(View.GONE);
                    }
                    viewModel.mIsRecordingVideo = false;
                });

                Log.e(TAG, "Video saved: " + viewModel.mNextVideoAbsolutePath);

                viewModel.closeCamera();

                if (CameraUtil.handsFreeEnable) {
                    openCamera(binding.cameraTexture.getWidth(), binding.cameraTexture.getHeight());
                    handsFreeVideos.add(viewModel.mNextVideoAbsolutePath);
                    if (handsFreeVideos != null) {
                        if (thumbsUp) {
                            Intent intent = new Intent(getContext(), PreviewVideoActivity.class);
                            intent.putStringArrayListExtra("handsFreeVideos", handsFreeVideos);
                            startActivityForResult(intent, 1001);
                            getActivity().finish();
                        }
                        if (handsFreeVideos.size() == 1) {
                            CameraUtil.setVideoThumbnail(activityBinding.handsFreeImage1, viewModel.mNextVideoAbsolutePath);

                        } else if (handsFreeVideos.size() == 2) {
                            CameraUtil.setVideoThumbnail(activityBinding.handsFreeImage2, viewModel.mNextVideoAbsolutePath);

                        } else if (handsFreeVideos.size() == 3 && !thumbsUp) {
                            getActivity().runOnUiThread(() -> {

                                Intent intent = new Intent(getContext(), PreviewVideoActivity.class);
                                intent.putStringArrayListExtra("handsFreeVideos", handsFreeVideos);
                                startActivityForResult(intent, 1001);
                                getActivity().finish();
                            });
                        }

                    }
                } else if (CameraUtil.speedEnable) {
//                    Intent intent = new Intent(getContext(), PreviewVideoActivity.class);
//                    intent.putExtra("speedVideoPath", viewModel.mNextVideoAbsolutePath);
//                    intent.putExtra("type", "slowmo");
//                    startActivityForResult(intent, 1001);
//                    getActivity().finish();

                    saveData("speedVideoPath","",viewModel.mNextVideoAbsolutePath);
                } else if (CameraUtil.gridLayoutHVideoActive) {
                    openCamera(binding.cameraTexture.getWidth(), binding.cameraTexture.getHeight());
                    mergeVideo.add(viewModel.mNextVideoAbsolutePath);
                    viewModel.grid2VideoConditionH();
                } else if (CameraUtil.music && !isPaused) {
//                    Intent intent = new Intent(getContext(), PreviewVideoActivity.class);
//                    intent.putExtra("videoWithSound", viewModel.mNextVideoAbsolutePath);
//                    intent.putExtra("soundID", soundID);
//                    startActivityForResult(intent, 1001);
//                    getActivity().finish();
                    saveData("videoWithSound",soundID,viewModel.mNextVideoAbsolutePath);
                }

                else if (!isPaused){
//                    Intent intent = new Intent(getContext(), PreviewVideoActivity.class);
//                    intent.putExtra("videoPath", viewModel.mNextVideoAbsolutePath);
//                    Log.e(TAG, "mNextVideoAbsolutePath: " + viewModel.mNextVideoAbsolutePath);
//                    startActivityForResult(intent, 1001);
//                    getActivity().finish();

                    saveData("Simple", "",viewModel.mNextVideoAbsolutePath);
                }


            });
        }

    }

    public boolean isRecordingVideo() {
        return viewModel.mIsRecordingVideo;
    }

    public void stopRecordingBoomVideo() {
        getActivity().runOnUiThread(() -> {
            getActivity().runOnUiThread(() -> {
                activityBinding.chronometer.stop();
                activityBinding.chronometer.setVisibility(View.GONE);
                if (activityBinding.animationView.isAnimating()) {
                    activityBinding.animationView.cancelAnimation();
                    activityBinding.animationView.setVisibility(View.GONE);
                }
                viewModel.mIsRecordingVideo = false;
            });

            Log.e(TAG, "Video saved: " + viewModel.mNextVideoAbsolutePath);
            viewModel.closeCamera();
            Intent intent = new Intent(getContext(), PreviewVideoActivity.class);
            intent.putExtra("boomerang", viewModel.mNextVideoAbsolutePath);
            startActivity(intent);
            getActivity().finish();
//            openCamera(mTextureView.getWidth(), mTextureView.getHeight());


        });
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

    /**
     * Shows an error message dialog.
     */
    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    })
                    .create();
        }

    }

    //Layout Grid Code:-


    public ArrayList<String> mergeVideo = new ArrayList<>();


    public void gridLayoutTwoVideoH() {
        CameraUtil.gridLayoutHVideoActive = true;
        binding.grid2LayoutHorizontal.setVisibility(View.VISIBLE);
    }

    //Super Zoom Code:-

    Zoom zoom;
    public static float zoomValue = 1f;
    public Float maxZoom;

    public Handler zoomHandler;

    public void changeZoom() {

        zoomValue = Zoom.DEFAULT_ZOOM_FACTOR;
        zoomHandler = new Handler();
        final int delay = 25; // 1000 milliseconds == 1 second

        maxZoom = mCameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
        Log.e(TAG, "maxZoom: " + maxZoom);


        zoomHandler.postDelayed(new Runnable() {
            public void run() {
                updateZoom(); // Do your work here

                if (zoomValue < mCameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM))
                    zoomHandler.postDelayed(this, delay);
                else {
                    try {
                        getActivity().runOnUiThread(() -> {

                            stopRecordingVideo();
                        });


                    } catch (Exception e) {

                        Log.e(TAG, "Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }, delay);
    }

    public void updateZoom() {
        try {
            zoomValue = zoomValue + 0.01f;

            updateZoomPreview(zoomValue);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    Float defaultZoom = Zoom.DEFAULT_ZOOM_FACTOR;

    private void updatePreview(float value) throws CameraAccessException {
        if (viewModel.mCameraDevice == null) {
            return;
        }
        viewModel.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        zoom = new Zoom(mCameraCharacteristics);
        zoom.setZoom(viewModel.mPreviewRequestBuilder, value);
        defaultZoom = value;

        viewModel.mPreviewSession.setRepeatingRequest(viewModel.mPreviewRequestBuilder.build(), null, mBackgroundHandler);
    }

    private void updateZoomPreview(float value) throws CameraAccessException {
        if (viewModel.mCameraDevice == null) {
            return;
        }
        viewModel.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        zoom = new Zoom(mCameraCharacteristics);

        zoom.setZoom(viewModel.mPreviewRequestBuilder, value);
        try {
            viewModel.mPreviewSession.setRepeatingRequest(viewModel.mPreviewRequestBuilder.build(), null, mBackgroundHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static final class Zoom {
        private static final float DEFAULT_ZOOM_FACTOR = 1.0f;

        @NonNull
        private final Rect mCropRegion = new Rect();

        public final float maxZoom;

        @Nullable
        private final Rect mSensorSize;

        public final boolean hasSupport;

        public Zoom(@NonNull final CameraCharacteristics characteristics) {
            this.mSensorSize = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);

            if (this.mSensorSize == null) {
                this.maxZoom = Zoom.DEFAULT_ZOOM_FACTOR;
                this.hasSupport = false;
                return;
            }

            final Float value = characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);

            this.maxZoom = ((value == null) || (value < Zoom.DEFAULT_ZOOM_FACTOR))
                    ? Zoom.DEFAULT_ZOOM_FACTOR
                    : value;

            this.hasSupport = (Float.compare(this.maxZoom, Zoom.DEFAULT_ZOOM_FACTOR) > 0);
        }

        public void setZoom(@NonNull final CaptureRequest.Builder builder, final float zoom) {
            if (!this.hasSupport) {
                return;
            }

            final float newZoom = MathUtils.clamp(zoom, Zoom.DEFAULT_ZOOM_FACTOR, this.maxZoom);

            final int centerX = this.mSensorSize.width() / 2;
            final int centerY = this.mSensorSize.height() / 2;
            final int deltaX = (int) ((0.5f * this.mSensorSize.width()) / newZoom);
            final int deltaY = (int) ((0.5f * this.mSensorSize.height()) / newZoom);

            this.mCropRegion.set(centerX - deltaX,
                    centerY - deltaY,
                    centerX + deltaX,
                    centerY + deltaY);

            builder.set(CaptureRequest.SCALER_CROP_REGION, this.mCropRegion);
        }
    }


    //Hands-free Record Video Code:-


    final Handler handler = new Handler();
    int finalTime = 15000;
    long recordTimer = 0;
    boolean thumbsUp = false;
    float initialTouchX;

    int limit = 0;
    public ArrayList<String> handsFreeVideos = new ArrayList<>();


    boolean goneFlag = false;
    //Put this into the class
    final Handler handlerLongPress = new Handler();
    Runnable mLongPressed = () -> {
        goneFlag = true;

        //Code for long click
        thumbsUp = false;
        handsFree();
    };

    @SuppressLint("ClickableViewAccessibility")
    public void handsFreeRecordMethod() {
        activityBinding.handsFreeRecord.setOnTouchListener((v, event) -> {
            boolean time;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    initialTouchX = event.getRawX();
                    handlerLongPress.postDelayed(mLongPressed, 300);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    handlerLongPress.removeCallbacks(mLongPressed);
                    if (Math.abs(event.getRawX() - initialTouchX) <= 2 && !goneFlag) {
                        //Code for single click
                        Toast toast = Toast.makeText(mContext, "Hold The Button", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        return false;
                    } else {
                        try {
                            handler.removeCallbacks(runnable);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        thumbsUp = true;
                        if (isRecordingVideo()) {
                            stopRecordingVideo();
                        }
                    }


                    return true;
            }

            return false;

        });
    }

    Runnable runnable;

    private void handsFree() {
        if (!thumbsUp) {
            getActivity().findViewById(R.id.handsFreeParentLayout).setVisibility(View.GONE);
            startRecordingVideo();
            recordTimer = 0;
            activityBinding.chronometer.setOnChronometerTickListener(chronometer -> {
                recordTimer = SystemClock.elapsedRealtime() - chronometer.getBase();

                if (recordTimer >= finalTime) {
                    limit++;
                    recordTimer = 0;
                    if (isRecordingVideo()) {
                        stopRecordingVideo();
                    }
                    if (limit < 3) {
                        if (!thumbsUp) {
                            runnable = this::startRecordingVideo;
                            handler.postDelayed(runnable, 500);
                        }

                    }
                }
            });
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1001) {
                Intent intent = new Intent();
                intent.putExtra("name", data.getStringExtra("name"));
                intent.putExtra("path", data.getStringExtra("path"));
                intent.putExtra("isVideo", data.getBooleanExtra("isVideo", false));
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }

        }
    }

    //Video Progressbar Code.

    public int progressTimer = 16000;

    public String soundID = "";


    public void initProgressBar() {

        activityBinding.segmentedProgressBar.enableAutoProgressView(0);
        activityBinding.segmentedProgressBar.setListener(mills -> {
            if (mills == progressTimer) {
                Log.e(TAG, "Progress Finish: ");
                if (isRecordingVideo()) {
                    stopRecordingVideo();
                }
            }
        });
        activityBinding.segmentedProgressBar.setShader(new int[]{ContextCompat.getColor(getActivity(), R.color.new_action_color), ContextCompat.getColor(getActivity(), R.color.new_action_color)});
        activityBinding.segmentedProgressBar.enableAutoProgressView(progressTimer);
        activityBinding.segmentedProgressBar.setVisibility(View.VISIBLE);
        activityBinding.segmentedProgressBar.resume();


    }

    public void fullscreenTexture() {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(binding.cameraTexture, "translationY", (float) viewModel.screenHeight / 2, 0);
        anim1.setDuration(200);
        anim1.start();

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(binding.cameraTexture, "translationX", (float) viewModel.screenWidth / 2, 0);
        anim2.setDuration(200);
        anim2.start();

    }

    public void hideTheGridLayout() {
        if (binding.grid2Layout.getVisibility() == View.VISIBLE)
            binding.grid2Layout.setVisibility(View.GONE);

        if (binding.grid3Layout.getVisibility() == View.VISIBLE)
            binding.grid3Layout.setVisibility(View.GONE);

        if (binding.grid4Layout.getVisibility() == View.VISIBLE)
            binding.grid4Layout.setVisibility(View.GONE);

        if (binding.grid6Layout.getVisibility() == View.VISIBLE)
            binding.grid6Layout.setVisibility(View.GONE);

        if (binding.grid2LayoutHorizontal.getVisibility() == View.VISIBLE)
            binding.grid2LayoutHorizontal.setVisibility(View.GONE);

        binding.twoGridImage1Horizontal.setBackgroundResource(0);
        binding.twoGridImage1Horizontal.setImageResource(0);

        binding.twoGridImage2Horizontal.setBackgroundResource(0);
        binding.twoGridImage2Horizontal.setImageResource(0);
        binding.twoGridImage2Horizontal.setBackgroundResource(R.drawable.transparent_image);
        binding.twoGridImage2Horizontal.setAlpha(0.9f);

    }


    public void saveData(String filter, String extra, String path) {

        DefaultVideoStrategy strategy = new DefaultVideoStrategy.Builder()
                .addResizer(new FractionResizer(0.7F))
                .addResizer(new AtMostResizer(1000))
                .build();
        String output = Constant.createOutputPath(mContext, ".mp4");
        TranscoderOptions.Builder options = Transcoder.into(output);

        options.addDataSource(path);
        options.setVideoTrackStrategy(strategy);
        options.setListener(new TranscoderListener() {
            public void onTranscodeProgress(double progress) {
                showProgressDialog();
                if (progressBinding != null) {
                    //  if (viewModel.audio != null) {
                    progressBinding.progressBar.publishProgress((float) progress / 2);
//                        } else {
//                            progressBinding.progressBar.publishProgress((float) progress);
//                        }
                }
                Log.d("TAG", "onTranscodeProgress: " + progress);
            }

            public void onTranscodeCompleted(int successCode) {
                Log.d("TAG", "onTranscodeCompleted: " + successCode);
//                    if (viewModel.audio != null) {
//
//                        Transcoder.into(getPath().getPath().concat("/finally.mp4"))
//                                .addDataSource(TrackType.VIDEO, getPath().getPath().concat("/append.mp4"))
//                                .addDataSource(TrackType.AUDIO, getPath().getPath().concat("/recordSound.aac"))
//                                .setVideoTrackStrategy(strategy)
//                                .setListener(new TranscoderListener() {
//                                    @Override
//                                    public void onTranscodeProgress(double progress) {
//                                        if (progressBinding != null && viewModel.audio != null) {
//                                            progressBinding.progressBar.publishProgress((float) (1 + progress) / 2);
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onTranscodeCompleted(int successCode) {
//                                        File thumbFile = new File(getPath(), "temp.jpg");
//                                        try {
//                                            FileOutputStream stream = new FileOutputStream(thumbFile);
//
//                                            Bitmap bmThumbnail;
//                                            bmThumbnail = ThumbnailUtils.createVideoThumbnail(getPath().getPath().concat("/append.mp4"),
//                                                    MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
//
//                                            if (bmThumbnail != null) {
//                                                bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                                            }
//                                            stream.flush();
//                                            stream.close();
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                        hideProgressDialog();
//
//                                        Intent intent = new Intent(getActivity(), PreviewVideoActivity.class);
//                                        intent.putExtra("post_video", getPath().getPath().concat("/finally.mp4"));
//                                        intent.putExtra("post_image", thumbFile.getPath());
////                                        if (viewModel.soundId != null && !viewModel.soundId.isEmpty()) {
////                                            intent.putExtra("soundId", viewModel.soundId);
////                                        }
////                                        intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.aac"));
////                                        intent.putExtra("sound_image", getPath().getPath().concat("/soundImage.jpeg"));
//                                        startActivityForResult(intent, 101);
//
//
//                                    }
//
//                                    @Override
//                                    public void onTranscodeCanceled() {
//
//                                    }
//
//                                    @Override
//                                    public void onTranscodeFailed(@NonNull Throwable exception) {
//
//                                    }
//                                }).transcode();
//
//                    } else {
                Log.i("TAG", "onCombineFinished: " + "is original sound");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                                Track audio;
//                                try {
//                                    Movie m1 = MovieCreator.build(getPath().getPath().concat("/append.mp4"));
//                                    audio = m1.getTracks().get(1);
//                                    Movie m2 = new Movie();
//                                    m2.addTrack(audio);
//                                    DefaultMp4Builder builder = new DefaultMp4Builder();
//                                    Container stdMp4 = builder.build(m2);
//                                    FileOutputStream fos = new FileOutputStream(getPath().getPath().concat("/originalSound.aac"));
//                                    stdMp4.writeContainer(fos.getChannel());
//                                    fos.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
                        String thumbnail = Constant.createOutputPath(mContext, ".JPEG");
                        File thumbFile = new File(thumbnail);
                        try {
                            FileOutputStream stream = new FileOutputStream(thumbFile);

                            Bitmap bmThumbnail;
                            bmThumbnail = ThumbnailUtils.createVideoThumbnail(output,
                                    MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                            if (bmThumbnail != null) {
                                bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            }
                            stream.flush();
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        hideProgressDialog();
                        Intent intent = new Intent(mContext, PreviewVideoActivity.class);
                        intent.putExtra("videoPath", output);
                        intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.aac"));
                        intent.putExtra("thumbPath", thumbFile.getPath());
                        intent.putExtra("Filter", filter);
                        intent.putExtra("Extra", extra);
                        if (MeestCameraActivity.CALL_TYPE.equalsIgnoreCase("story")) {
                            intent.putExtra("type", "story");
                        } else {
                            intent.putExtra("type", "feed");
                        }

                        intent.putExtra("typeCamera", true);
                        startActivity(intent);
                        getActivity().finish();

                    }
                }).start();
            }
            // }

            public void onTranscodeCanceled() {
                Log.d("TAG", "onTranscodeCanceled: ");
            }

            public void onTranscodeFailed(@NonNull Throwable exception) {
                Log.d("TAG", "onTranscodeCanceled: " + exception);
            }
        }).transcode();
    }

    public File getPath() {
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            filesDir = getActivity().getExternalFilesDir(null);
        } else {
            // Load another directory, probably local memory
            filesDir = getActivity().getFilesDir();
        }
//        if (filesDir != null) {
//            viewModel.parentPath = filesDir.getPath();
//        }
        return filesDir;
    }

    private DailogProgressBinding progressBinding;
    private Dialog mBuilder;

    public void initProgressDialog() {
        mBuilder = new Dialog(mContext);
        mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        if (mBuilder.getWindow() != null) {
            mBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        progressBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dailog_progress, null, false);
        progressBinding.progressBar.setShader(new int[]{ContextCompat.getColor(mContext, R.color.colorTheme), ContextCompat.getColor(mContext, R.color.colorTheme), ContextCompat.getColor(mContext, R.color.colorTheme)});


        DrawableImageViewTarget target = new DrawableImageViewTarget(progressBinding.ivParent);
//        Glide.with(this)
//                .load(R.drawable.loader_gif)
//                .into(target);


        //  Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        //  Animation reverseAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_reverse);
        //   progressBinding.ivParent.startAnimation(rotateAnimation);
        // progressBinding.ivChild.startAnimation(reverseAnimation);
        mBuilder.setContentView(progressBinding.getRoot());
    }

    public void showProgressDialog() {
        if (!mBuilder.isShowing()) {
            mBuilder.show();
        }
    }

    public void hideProgressDialog() {
        try {
            mBuilder.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void compressHandsFree(String filter, String extra, String path) {

        DefaultVideoStrategy strategy = new DefaultVideoStrategy.Builder()
                .addResizer(new FractionResizer(0.7F))
                .addResizer(new AtMostResizer(1000))
                .build();
        String output = Constant.createOutputPath(mContext, ".mp4");
        TranscoderOptions.Builder options = Transcoder.into(output);

        options.addDataSource(path);
        options.setVideoTrackStrategy(strategy);
        options.setListener(new TranscoderListener() {
            public void onTranscodeProgress(double progress) {
                showProgressDialog();
                if (progressBinding != null) {
                    //  if (viewModel.audio != null) {
                    progressBinding.progressBar.publishProgress((float) progress / 2);

                }
                Log.d("TAG", "onTranscodeProgress: " + progress);
            }

            public void onTranscodeCompleted(int successCode) {
                Log.d("TAG", "onTranscodeCompleted: " + successCode);

                Log.i("TAG", "onCombineFinished: " + "is original sound");
                new Thread(() -> {

                    String thumbnail = Constant.createOutputPath(mContext, ".JPEG");
                    File thumbFile = new File(thumbnail);
                    try {
                        FileOutputStream stream = new FileOutputStream(thumbFile);

                        Bitmap bmThumbnail;
                        bmThumbnail = ThumbnailUtils.createVideoThumbnail(output,
                                MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                        if (bmThumbnail != null) {
                            bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        }
                        stream.flush();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    hideProgressDialog();
                    Intent intent = new Intent(mContext, PreviewVideoActivity.class);
                    intent.putExtra("videoPath", output);
                    intent.putExtra("post_sound", getPath().getPath().concat("/originalSound.aac"));
                    intent.putExtra("thumbPath", thumbFile.getPath());
                    intent.putExtra("Filter", filter);
                    intent.putExtra("Extra", extra);
                    if (MeestCameraActivity.CALL_TYPE.equalsIgnoreCase("story")) {
                        intent.putExtra("type", "story");
                    } else {
                        intent.putExtra("type", "feed");
                    }

                    intent.putExtra("typeCamera", true);
                    startActivity(intent);
                    getActivity().finish();

                }).start();
            }
            // }

            public void onTranscodeCanceled() {
                Log.d("TAG", "onTranscodeCanceled: ");
            }

            public void onTranscodeFailed(@NonNull Throwable exception) {
                Log.d("TAG", "onTranscodeCanceled: " + exception);
            }
        }).transcode();



    }
}

