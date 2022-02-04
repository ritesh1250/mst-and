package com.meest.videomvvmmodule.only_camera2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
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
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.math.MathUtils;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.meest.R;
import com.meest.databinding.ActivityCamera2DuetBinding;
import com.meest.databinding.DailogProgressBinding;
import com.meest.medley_camera2.camera2.view.activity.MeestCameraActivity;
import com.meest.medley_camera2.camera2.view.activity.PreviewVideoActivity;
import com.meest.medley_camera2.camera2.viewmodels.CameraFragmentViewModel;
import com.meest.meestbhart.utilss.Constant;
import com.meest.videoEditing.UtilCommand;
import com.meest.videomvvmmodule.AudioExtractor;
import com.meest.videomvvmmodule.gallery.GalleryFoldersActivityMedley;
import com.meest.videomvvmmodule.only_camera2.camera2.cameraUtils.AspectRatio;
import com.meest.videomvvmmodule.only_camera2.camera2.cameraUtils.AutoFocusHelper;
import com.meest.videomvvmmodule.only_camera2.camera2.utills.CameraConstants;
import com.meest.videomvvmmodule.only_camera2.camera2.utills.CameraUtil;
import com.meest.videomvvmmodule.only_camera2.camera2.utills.SoundEventListener;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.UriUtils;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.preview.PreviewActivity;
import com.meest.videomvvmmodule.view.recordvideo.DuoActivity;
import com.meest.videomvvmmodule.viewmodel.Camera2DuetViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.TranscoderOptions;
import com.otaliastudios.transcoder.engine.TrackType;
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategy;
import com.otaliastudios.transcoder.strategy.size.AtMostResizer;
import com.otaliastudios.transcoder.strategy.size.FractionResizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

public class Camera2DuetActivity extends BaseActivity implements SensorEventListener, SoundEventListener {
    private String videoURL;
    public String soundID = "";
    private String musicUrl;
    public MediaPlayer audio;
    private static final String TAG = "MainActivity";
    String callType = "video", uri;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    boolean sizeGreaterThan300 = false;
    String path = "";
    float minResolution = 0;
    private String thumbnail;
    public static final int[] FLASH_OPTIONS = {
            CameraConstants.FLASH_OFF,
            CameraConstants.FLASH_TORCH,
    };

    public static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    public static int mCurrentFlashIndex = 0;

    private CountDownTimer countDownTimer;

    public ActivityCamera2DuetBinding binding;

    Camera2DuetViewModel viewModel;

    int timerValue = 0;
    ArrayList<AspectRatio> ratios2 = new ArrayList<>();

    private final Handler stopVideoRecordingHandler = new Handler();
    private Runnable runnable;
    private final int timeLimit = 6000;

    int LAUNCH_SECOND_ACTIVITY = 1231;

    public ArrayList<String> videoPaths = new ArrayList<>();

    private SensorManager sensorMan;
    private Sensor accelerometer;

    float[] mGravity;
    String format;

    private CustomDialogBuilder customDialogBuilder;
    ActivityResultLauncher<Intent> someActivityResultLauncher;
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
    private Thread thread;
    MediaPlayer mediaPlayer;
    private int width, height;
    private String recordedVideoUri;
    private String rescaleVideo;
    private String recordedRescaleVideo;
    private String outFile;


    /**
     * An {@link ImageReader} that handles still image capture.
     */


    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a
     * {@link TextureView}.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera2_duet);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new Camera2DuetViewModel()).createFor()).get(Camera2DuetViewModel.class);
        binding.setViewModel(viewModel);
        initProgressDialog();
        binding.cameraTexture.setGestureListener(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (CameraUtil.focusEnable) {
                    setManualFocusAt((int) e.getX(), (int) e.getY());
                }
                return true;
            }
        });
        customDialogBuilder = new CustomDialogBuilder(this);
        CameraUtil.oneTime = true;
//        setContentView(R.layout.activity_camera2_main);
        format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        initViews();
        viewModel.showLayouts();
        initSensor();
        videoProgressbar();
        String type = getIntent().getType();
        if (type != null) {
            if (type.startsWith("video/")) {
                Uri videoUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
                path = UriUtils.getPathFromUri(this, videoUri);
                assert path != null;
//                handleSendVideo(path);
            }

        }
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        binding.gallery.performClick();
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });

        viewModel.disableOptions();

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        if (data != null && data.getBooleanExtra("is_preview", false)) {
                            finish();
                        }
//                            doSomeOperations();
                    }
                });
    }


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
            showToast("Camera is error: " + error);
            finish();
        }

    };
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
        final Activity activity = this;
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

    @Override
    public void onResume() {
        super.onResume();
        isPaused = false;
        start();

    }

    public boolean isPaused = false;

    public void start() {

        startBackgroundThread();

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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        int internalFacing = CameraFragmentViewModel.INTERNAL_FACINGS.get(viewModel.mFacing);
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
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
                int displayRotation = getWindowManager().getDefaultDisplay().getRotation();


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
                getWindowManager().getDefaultDisplay().getRealSize(displaySize);


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


                binding.cameraTexture.setAspectRatio(viewModel.mPreviewSize.getHeight(), viewModel.mPreviewSize.getWidth());


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
                    .show(getFragmentManager(), FRAGMENT_DIALOG);
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
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
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
            assert viewModel.texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            viewModel.texture.setDefaultBufferSize(viewModel.mPreviewSize.getWidth(), viewModel.mPreviewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(viewModel.texture);

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
        int mDisplayOrientation = getWindowManager().getDefaultDisplay().getRotation();
        float[] points = new float[2];
        points[0] = (float) x / binding.cameraTexture.getWidth();
        points[1] = (float) y / binding.cameraTexture.getHeight();
        Matrix rotationMatrix = new Matrix();
        rotationMatrix.setRotate(mDisplayOrientation, 0.5f, 0.5f);
        rotationMatrix.mapPoints(points);
        if (viewModel.mPreviewRequestBuilder != null) {

            viewModel.updateManualFocus(points[0], points[1], mCameraCharacteristics);
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
        viewModel.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        viewModel.mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        viewModel.mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        viewModel.mNextVideoAbsolutePath = viewModel.getVideoFilePath();
        viewModel.mMediaRecorder.setOutputFile(viewModel.videoPaths.get(0));
        viewModel.mMediaRecorder.setVideoEncodingBitRate(1000000);
        viewModel.mMediaRecorder.setVideoFrameRate(20);
        viewModel.mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        viewModel.mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        viewModel.mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
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
        File file = new File(getPath(), "video".concat(String.valueOf(viewModel.count)).concat(".mp4"));
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewModel.videoPaths.add(getPath().getPath().concat("/video").concat(String.valueOf(viewModel.count)).concat(".mp4"));
        try {

            binding.videoViewThumbnail.setVisibility(View.GONE);
            binding.videoView.start();
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

                        runOnUiThread(() -> {
                            // Start recording
                            viewModel.mMediaRecorder.start();
                            if (!binding.animationView.isAnimating()) {
                                binding.animationView.setVisibility(View.VISIBLE);
                                binding.animationView.playAnimation();
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

            binding.chronometer.setVisibility(View.VISIBLE);
            binding.tvSoundTitle.setVisibility(View.GONE);
            binding.chronometer.setBase(SystemClock.elapsedRealtime());
            binding.chronometer.start(); // start a chronometer
            binding.chronometer.setFormat("%s");

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
        binding.video.setEnabled(false);
        runOnUiThread(() -> {
            viewModel.count += 1;

            binding.segmentedProgressBar.pause();
            binding.videoView.pause();
            runOnUiThread(() -> {
                binding.chronometer.stop();
                binding.chronometer.setVisibility(View.GONE);
                if (binding.animationView.isAnimating()) {
                    binding.animationView.cancelAnimation();
                    binding.animationView.setVisibility(View.GONE);
                }
                viewModel.mIsRecordingVideo = false;
            });

            Log.e(TAG, "Video saved: " + viewModel.mNextVideoAbsolutePath);


            viewModel.closeCamera();
            if (!isPaused) {
//                saveData("Simple", "", viewModel.mNextVideoAbsolutePath);
                saveData(true);
            }


        });
    }

    public void stopRecordingBoomVideo() {
        runOnUiThread(() -> {
            runOnUiThread(() -> {
                binding.chronometer.stop();
                binding.chronometer.setVisibility(View.GONE);
                if (binding.animationView.isAnimating()) {
                    binding.animationView.cancelAnimation();
                    binding.animationView.setVisibility(View.GONE);
                }
                viewModel.mIsRecordingVideo = false;
            });

            Log.e(TAG, "Video saved: " + viewModel.mNextVideoAbsolutePath);
            viewModel.closeCamera();
            Intent intent = new Intent(this, PreviewVideoActivity.class);
            intent.putExtra("boomerang", viewModel.mNextVideoAbsolutePath);
            startActivity(intent);
            finish();
//            openCamera(mTextureView.getWidth(), mTextureView.getHeight());


        });
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

    Zoom zoom;
    public static float zoomValue = 1f;
    public Float maxZoom;

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

    public ArrayList<String> handsFreeVideos = new ArrayList<>();

    public int progressTimer = 16000;

    //change when use duet
    public void initProgressBar() {

        binding.segmentedProgressBar.enableAutoProgressView(0);
        binding.segmentedProgressBar.setListener(mills -> {
            if (mills == progressTimer) {
                Log.e(TAG, "Progress Finish: ");
                if (viewModel.isRecordingVideo()) {
                    stopRecordingVideo();
                }
            }
        });
        binding.segmentedProgressBar.setShader(new int[]{ContextCompat.getColor(this, R.color.new_action_color), ContextCompat.getColor(this, R.color.new_action_color)});
        binding.segmentedProgressBar.enableAutoProgressView(progressTimer);
        binding.segmentedProgressBar.setVisibility(View.VISIBLE);
        binding.segmentedProgressBar.resume();


    }

    private DailogProgressBinding progressBinding;
    private Dialog mBuilder;

    public void initProgressDialog() {
        mBuilder = new Dialog(Camera2DuetActivity.this);
        mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        if (mBuilder.getWindow() != null) {
            mBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        progressBinding = DataBindingUtil.inflate(LayoutInflater.from(Camera2DuetActivity.this), R.layout.dailog_progress, null, false);
        progressBinding.progressBar.setShader(new int[]{ContextCompat.getColor(Camera2DuetActivity.this, R.color.colorTheme), ContextCompat.getColor(Camera2DuetActivity.this, R.color.colorTheme), ContextCompat.getColor(this, R.color.colorTheme)});
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

    private void initViews() {
        viewModel.binding = binding;
        viewModel.context = this;
        if (getIntent().getExtras() != null) {
            musicUrl = getIntent().getStringExtra("music_url");
//            Log.e("sound_url", musicUrl);
        }

        if (musicUrl != null && !musicUrl.isEmpty()) {
            if (getIntent().getStringExtra("soundId") != null) {
                viewModel.soundId = getIntent().getStringExtra("soundId");
                downLoadMusic(getIntent().getStringExtra("music_url"));
            }
        }

        if (getIntent().getStringExtra("videoPath") != null) {
            videoURL = getIntent().getStringExtra("videoPath");
            thumbnail = getIntent().getExtras().getString("videoThumbnail");
            Glide.with(this).load(thumbnail).into(binding.videoViewThumbnail);
//            Log.e("videoPath++++", getIntent().getStringExtra("videoPath"));
        }

//        Log.e("==========videoFile", videoURL.toString());
        customDialogBuilder.showLoadingDialog();
        thread = new Thread(() -> {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(videoURL, new HashMap<>());
            width = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            height = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            retriever.release();

            long timeInMilliSec = Long.parseLong(time);
            viewModel.onDurationUpdate.postValue(timeInMilliSec);
            customDialogBuilder.hideLoadingDialog();
        });

        thread.start();

        binding.videoView.setVideoPath(videoURL);
        binding.videoView.setOnPreparedListener(mp -> mediaPlayer = mp);
        binding.segmentedProgressBar.enableAutoProgressView(0);
        layoutClickEvents();

    }


    private void layoutClickEvents() {

        binding.layoutSwitchCamera.setOnClickListener(v -> {
            int facing = getFacing();
            setFacing(facing == CameraConstants.FACING_FRONT ?
                    CameraConstants.FACING_BACK : CameraConstants.FACING_FRONT);
        });

        binding.video.setOnClickListener(v -> {
            if (viewModel.isRecordingVideo()) {
                stopRecordingVideo();
                binding.segmentedProgressBar.pause();

                if (viewModel.audio != null) {
                    viewModel.audio.pause();
                }
                binding.video.setImageResource(R.drawable.ic_record_start);
                //showLayouts();

            } else {

                viewModel.videoClickChk.set(true);
                viewModel.hideLayouts();

                CameraUtil.music = false;
                CameraUtil.progressTimer = true;


                binding.segmentedProgressBar.setVisibility(View.VISIBLE);
                binding.video.setVisibility(View.VISIBLE);
                startRecordingVideo();

                binding.video.setImageResource(R.drawable.ic_record_stop_);
                if (viewModel.audio != null) {
                    viewModel.audio.start();
                }
            }
        });


        binding.layoutclose.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.layoutFlash.setOnClickListener(v -> {
            mCurrentFlashIndex = (mCurrentFlashIndex + 1) % FLASH_OPTIONS.length;
            Log.e(TAG, "mCurrentFlashIndex: " + mCurrentFlashIndex);
            binding.switchFlash.setImageResource(FLASH_ICONS[mCurrentFlashIndex]);
            setFlash(FLASH_OPTIONS[mCurrentFlashIndex]);
        });


        binding.gallery.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(Camera2DuetActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(Camera2DuetActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(Camera2DuetActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                        ContextCompat.checkSelfPermission(Camera2DuetActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(Camera2DuetActivity.this.getResources(), R.drawable.ic_storage_permission, null), Camera2DuetActivity.this.getResources().getString(R.string.to_capture_storage_denied),
                            Camera2DuetActivity.this.getResources().getString(R.string.not_now), Camera2DuetActivity.this.getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {
                                @Override
                                public void onPositiveDismiss() {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", Camera2DuetActivity.this.getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }

                                @Override
                                public void onNegativeDismiss() {

                                }
                            });
                } else {
                    customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(Camera2DuetActivity.this.getResources(), R.drawable.ic_storage_permission, null), Camera2DuetActivity.this.getResources().getString(R.string.to_capture_storage),
                            Camera2DuetActivity.this.getResources().getString(R.string.not_now), Camera2DuetActivity.this.getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                                @Override
                                public void onPositiveDismiss() {
                                    requestPermissionLauncher.launch(
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                    requestPermissionLauncher.launch(
                                            Manifest.permission.READ_EXTERNAL_STORAGE);
                                }

                                @Override
                                public void onNegativeDismiss() {

                                }
                            });
                }
            } else {
                Intent i = new Intent(Camera2DuetActivity.this, GalleryFoldersActivityMedley.class);
                i.putExtra("filter", "Video");
                i.putExtra("call_type", callType);
                Camera2DuetActivity.this.startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
                Camera2DuetActivity.this.invalidateOptionsMenu();
            }
        });
    }

    public void saveData(boolean isCompress) {
        customDialogBuilder.showLoadingDialog();
        if (isCompress) {
            DefaultVideoStrategy strategy;
            if (sizeGreaterThan300) {
                float a = 1f / (minResolution / 550f);
                if (a > 1) {
                    strategy = new DefaultVideoStrategy.Builder()
                            .addResizer(new FractionResizer(0.8f))
                            .addResizer(new AtMostResizer(540))
                            .build();
                } else {
                    strategy = new DefaultVideoStrategy.Builder()
                            .addResizer(new FractionResizer(a))
                            .addResizer(new AtMostResizer(540))
                            .build();
                }
            } else {
                float a = 1f / (minResolution / 650f);
                if (a > 1) {
                    strategy = new DefaultVideoStrategy.Builder()
                            .addResizer(new FractionResizer(0.8f))
                            .addResizer(new AtMostResizer(640))
                            .build();
                } else {
                    strategy = new DefaultVideoStrategy.Builder()
                            .addResizer(new FractionResizer(a))
                            .addResizer(new AtMostResizer(640))
                            .build();
                }
            }
            TranscoderOptions.Builder options = Transcoder.into(getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"));
            for (int i = 0; i < viewModel.videoPaths.size(); i++) {
                options.addDataSource(viewModel.videoPaths.get(i));
//                options.addDataSource(viewModel.mNextVideoAbsolutePath);
            }
            if (viewModel.audio == null) {
                options.setVideoTrackStrategy(strategy);
            }
            options.setListener(new TranscoderListener() {
                public void onTranscodeProgress(double progress) {
//                    showProgressDialog();
//                    if (progressBinding != null) {
//                        if (viewModel.audio != null) {
//                            progressBinding.progressBar.publishProgress((float) progress / 2);
//                        } else {
//                            progressBinding.progressBar.publishProgress((float) progress);
//                        }
//                    }
                }

                public void onTranscodeCompleted(int successCode) {
//                    Log.d("TAG", "onTranscodeCompleted: " + successCode);
                    if (viewModel.audio != null) {
                        Transcoder.into(getPath().getPath().concat("/finally.mp4"))
                                .addDataSource(TrackType.VIDEO, getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"))
                                .addDataSource(TrackType.AUDIO, getPath().getPath().concat("/Medley_" + sessionManager.getUser().getData().getUserName() + "_" + viewModel.soundId + ".mp3"))
                                .setVideoTrackStrategy(strategy)
                                .setListener(new TranscoderListener() {
                                    @Override
                                    public void onTranscodeProgress(double progress) {
                                        if (progressBinding != null && viewModel.audio != null) {
                                            progressBinding.progressBar.publishProgress((float) (1 + progress) / 2);
//                                            progressBinding.preparing.setText("Preparing..." + (long) (((1 + progress) / 2) * 100) + "%");
                                            Log.e("===========Progress_2", "Preparing..." + (long) (((1 + progress) / 2) * 100) + "%");
                                        }
                                    }

                                    @Override
                                    public void onTranscodeCompleted(int successCode) {
                                        File thumbFile = new File(getPath(), "/" + "user_thumbnail_" + sessionManager.getUser().getData().getUserName() + format + ".jpg");
                                        try {
                                            FileOutputStream stream = new FileOutputStream(thumbFile);

                                            Bitmap bmThumbnail;
                                            bmThumbnail = ThumbnailUtils.createVideoThumbnail(getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"),
                                                    MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                                            if (bmThumbnail != null) {
                                                bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                                bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                            }
                                            stream.flush();
                                            stream.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        customDialogBuilder.hideLoadingDialog();
//                                        recordedVideoUri = getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4");
                                        recordedVideoUri = getPath().getPath().concat("/finally.mp4");
                                        concatenate();
                                    }

                                    @Override
                                    public void onTranscodeCanceled() {

                                    }

                                    @Override
                                    public void onTranscodeFailed(@NonNull Throwable exception) {

                                    }
                                }).transcode();

                    } else {
//                        Log.i("TAG", "onCombineFinished: " + "is original sound");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
//                                Track audio;
                                try {
//                                    Movie m1 = MovieCreator.build(getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"));
//                                    audio = m1.getTracks().get(0);
//                                    Movie m2 = new Movie();
//                                    m2.addTrack(audio);
//                                    DefaultMp4Builder builder = new DefaultMp4Builder();
//                                    Container stdMp4 = builder.build(m2);
//                                    FileOutputStream fos = new FileOutputStream(getPath().getPath().concat("/originalSound.mp3"));
//                                    stdMp4.writeContainer(fos.getChannel());
//                                    fos.close();

                                    File temp = new File(getPath().getPath().concat("/originalSound.mp3"));

                                    if (!temp.exists()) {
                                        temp.createNewFile();
                                    }

                                    try {
                                        new AudioExtractor().genVideoUsingMuxer(getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"), temp.getAbsolutePath(), -1, -1, true, false);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                File thumbFile = new File(getPath(), "/" + "user_thumbnail_" + sessionManager.getUser().getData().getUserName() + format + ".jpg");
                                try {
                                    FileOutputStream stream = new FileOutputStream(thumbFile);

                                    Bitmap bmThumbnail;
                                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4"),
                                            MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                                    if (bmThumbnail != null) {
                                        bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                    }
                                    stream.flush();
                                    stream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Glide.with(Camera2DuetActivity.this)
                                        .asBitmap()
                                        .load(sessionManager.getUser().getData().getUserProfile())
//                                        .load(Const.ITEM_BASE_URL + sessionManager.getUser().getData().getUserProfile())
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                File soundImage = new File(getPath(), "soundImage.jpeg");
                                                try {
                                                    FileOutputStream stream = new FileOutputStream(soundImage);
                                                    resource.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                                    stream.flush();
                                                    stream.close();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
//                                                Camera2.unbindAll();
                                                customDialogBuilder.hideLoadingDialog();
                                                recordedVideoUri = getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4");
                                                concatenate();
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }

                                            @Override
                                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                                super.onLoadFailed(errorDrawable);
                                                customDialogBuilder.hideLoadingDialog();
                                                recordedVideoUri = getPath().getPath().concat("/" + "user_video_" + sessionManager.getUser().getData().getUserName() + format + ".mp4");
                                                concatenate();
                                            }
                                        });
                            }
                        }).start();
                    }
                }

                public void onTranscodeCanceled() {
                    Log.d("TAG", "onTranscodeCanceled: ");
                }

                public void onTranscodeFailed(@NonNull Throwable exception) {

                    Log.d("TAG", "onTranscodeCanceled:/// " + exception);
                }
            }).transcode();

        } else {
            new Thread(() -> {
//                Track audio;
                try {
//                    File tempFile = new File(getExternalCacheDir(), "/" + "myAwesomeFile.mp3");

//                    if (!tempFile.exists())
//                    {
//                        try {
//                            tempFile.createNewFile();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    File temp = new File(getPath().getPath().concat("/originalSound.mp3"));

                    if (!temp.exists()) {
                        temp.createNewFile();
                    }

                    new AudioExtractor().genVideoUsingMuxer(viewModel.videoPaths.get(0), temp.getAbsolutePath(), -1, -1, true, false);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                File thumbFile = new File(getPath(), "/" + "user_thumbnail_" + sessionManager.getUser().getData().getUserName() + format + ".jpg");

                try {

                    FileOutputStream stream = new FileOutputStream(thumbFile);

                    Bitmap bmThumbnail;
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(viewModel.videoPaths.get(0),
                            MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                    if (bmThumbnail != null) {
                        bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                    }

                    stream.flush();
                    stream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera2DuetActivity.this)
                        .asBitmap()
                        .load(sessionManager.getUser().getData().getUserProfile())
//                        .load(Const.ITEM_BASE_URL + sessionManager.getUser().getData().getUserProfile())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                File soundImage = new File(getPath(), "soundImage.jpeg");
                                try {
                                    FileOutputStream stream = new FileOutputStream(soundImage);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                    stream.flush();
                                    stream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                customDialogBuilder.hideLoadingDialog();
                                recordedVideoUri = viewModel.videoPaths.get(0);
                                concatenate();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                customDialogBuilder.hideLoadingDialog();
                                recordedVideoUri = viewModel.videoPaths.get(0);
                                concatenate();
                            }
                        });
            }).start();
        }
    }

    public File getPath() {
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            filesDir = getExternalFilesDir(null);
        } else {
            filesDir = getFilesDir();
        }
        return filesDir;
    }

    private void initSensor() {
        sensorMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];

            int value1 = Math.round(x);
            int value = viewModel.absVal(value1);
            Log.e(TAG, "value: " + value);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sensorMan != null) {
            sensorMan.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }


    @Override
    public void onBackPressed() {
        customDialogBuilder.showSimpleDialog(getString(R.string.are_you_sure), getString(R.string.Do_you_really_wan_to_go_back),
                getString(R.string.no), getString(R.string.yes), new CustomDialogBuilder.OnDismissListener() {
                    @Override
                    public void onPositiveDismiss() {
                        //1.86
                        if (viewModel.audio != null) {
                            if (viewModel.audio.isPlaying()) {
                                viewModel.audio.stop();
                            }
                        }
                        finish();
                    }

                    @Override
                    public void onNegativeDismiss() {
                    }
                });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*        if (data != null) {
            if (requestCode == 101 && !path.equals("") && !data.getBooleanExtra("cancelled", false)) {
//                Intent intent = new Intent(Camera2DuetActivity.this, MainVideoActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//            finish();

                mCamera2Fragment.saveData("Simple", "",path);
            }
        }*/
        if (data != null) {
            if (requestCode == 101) {
                finish();
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1001) {
                Intent intent = new Intent();
                intent.putExtra("name", data.getStringExtra("name"));
                intent.putExtra("path", data.getStringExtra("path"));
                intent.putExtra("isVideo", data.getBooleanExtra("isVideo", false));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        }

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {

                assert data != null;
                String result = data.getStringExtra("result");
                int position = data.getIntExtra("position", 0);
//                Gson gson = new Gson();
//                Type type = new TypeToken<List<PictureFacer>>() {
//                }.getType();
//                selectedItem = gson.fromJson(result, type);

                String fileExtention = "";

//                uri = selectedItem.get(position).getPicturePath();
                uri = result;
//                Log.e("ImageUri", data.getStringExtra("result"));
//                uri = data.getStringExtra("result");
                int i = uri.lastIndexOf('.');
                if (i > 0) {
                    fileExtention = uri.substring(i + 1);
                }

                if (fileExtention.equalsIgnoreCase("mp4") || fileExtention.equalsIgnoreCase("3gp") || fileExtention.equalsIgnoreCase("mkv") || fileExtention.equalsIgnoreCase("mov") || fileExtention.equalsIgnoreCase("webm")) {
                    if (!uri.isEmpty()) {
                        File file = new File(uri);
                        // Get length of file in bytes
                        long fileSizeInBytes = file.length();
                        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                        long fileSizeInMB = fileSizeInKB / 1024;

                        if (fileSizeInMB > 300) {
                            sizeGreaterThan300 = true;
                        }


                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(Camera2DuetActivity.this, Uri.fromFile(new File(uri)));
                        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                        minResolution = Math.min(Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)), Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)));

                        long timeInMilliSec = Long.parseLong(time);
//                        Log.e("timeInMilliSec", "=======================" + timeInMilliSec);
                        if (timeInMilliSec < 4900) {
                            customDialogBuilder.showSimpleDialog(Camera2DuetActivity.this.getResources().getString(R.string.Video_Too_Short), Camera2DuetActivity.this.getResources().getString(R.string.This_video_is_shorter_than_5_seconds_Please_select_another),
                                    Camera2DuetActivity.this.getResources().getString(R.string.cancel), Camera2DuetActivity.this.getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {

                                        @Override
                                        public void onPositiveDismiss() {
//                                            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
                                            Intent i = new Intent(Camera2DuetActivity.this, GalleryFoldersActivityMedley.class);
                                            i.putExtra("filter", "Video");
                                            i.putExtra("call_type", callType);
                                            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
                                        }

                                        @Override
                                        public void onNegativeDismiss() {

                                        }
                                    });
                        } else if (timeInMilliSec >= 60200) {
                            customDialogBuilder.showSimpleDialog(Camera2DuetActivity.this.getResources().getString(R.string.Video_Too_Long), Camera2DuetActivity.this.getResources().getString(R.string.This_video_is_longer_than_1_min_Please_select_another),
                                    Camera2DuetActivity.this.getResources().getString(R.string.cancel), Camera2DuetActivity.this.getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {

                                        @Override
                                        public void onPositiveDismiss() {
//                                            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
                                            Intent i = new Intent(Camera2DuetActivity.this, GalleryFoldersActivityMedley.class);
                                            i.putExtra("filter", "Video");
                                            i.putExtra("call_type", callType);
                                            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
                                        }

                                        @Override
                                        public void onNegativeDismiss() {

                                        }
                                    });
                        } else if (fileSizeInMB < 500) {
                            viewModel.videoPaths = new ArrayList<>();
                            viewModel.videoPaths.add(uri);
                            if (fileSizeInMB > 5) {
                                Camera2DuetActivity.this.saveData(true);
                            } else {
                                Camera2DuetActivity.this.saveData(false);
                            }
                        } else {
                            customDialogBuilder.showSimpleDialog(Camera2DuetActivity.this.getResources().getString(R.string.Too_long_video_s_size), Camera2DuetActivity.this.getResources().getString(R.string.This_video_s_size_is_greater_than_500MbPlease_select_another),
                                    Camera2DuetActivity.this.getResources().getString(R.string.cancel), Camera2DuetActivity.this.getResources().getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {
                                        @Override
                                        public void onPositiveDismiss() {
//                                            bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
                                            Intent i = new Intent(Camera2DuetActivity.this, GalleryFoldersActivityMedley.class);
                                            i.putExtra("filter", "Video");
                                            i.putExtra("call_type", callType);
                                            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
                                        }

                                        @Override
                                        public void onNegativeDismiss() {

                                        }
                                    });
                        }
                        retriever.release();
                    }
                } else {
                    Toast.makeText(Camera2DuetActivity.this, Camera2DuetActivity.this.getString(R.string.Please_Select_mp4_format_video), Toast.LENGTH_SHORT).show();
                }

            } /*else {
                onBackPressed();
            }*/
        }
        /* else {
            onBackPressed();
        }*/
    }

    //Horizontal Progressbar for Video
    private void videoProgressbar() {
        CameraUtil.progressTimer = true;
    }

    //Sound Library Click Handle
    @Override
    public void clickEvent(String soundID, String soundUrl) {
        this.soundID = soundID;
        downLoadMusic(soundUrl);
        if (binding.videoWithSoundParent.getVisibility() == View.GONE) {
            binding.videoWithSoundParent.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
        }
        isPaused = true;
        if (viewModel.isRecordingVideo()) {
            stopRecordingVideo();
        }
        stop();
        try {

            if (mCurrentFlashIndex == 1) {
                setFlash(FLASH_OPTIONS[0]);
                binding.switchFlash.setImageResource(FLASH_ICONS[0]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downLoadMusic(String endPoint) {
        PRDownloader.download(endPoint, getPath().getPath(), "Medley_" + sessionManager.getUser().getData().getUserName() + "_" + viewModel.soundId + ".mp3")
                .build()
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        viewModel.createAudioForCamera(sessionManager, Camera2DuetActivity.this);
                    }

                    @Override
                    public void onError(Error error) {
                        Log.e(TAG, "onError: " + error.toString());
                        customDialogBuilder.hideLoadingDialog();
                    }
                });
    }

    public void concatenate() {
        StringBuilder sb1 = new StringBuilder();
        sb1.append(getExternalCacheDir());
        File file = new File(sb1.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder stringBuilder_recorded = new StringBuilder();
        stringBuilder_recorded.append(getExternalCacheDir());
        stringBuilder_recorded.append("/recorded_rescale");
        stringBuilder_recorded.append(format);
        stringBuilder_recorded.append(".mp4");
        rescaleVideo = stringBuilder_recorded.toString();
        String scale = "scale=" + width + ":" + height;
        Log.e("TAG", "concatenate: " + scale);
        Log.e("TAG", "rescaleVideo: " + rescaleVideo);
        recordedRescaleVideo = rescaleVideoFFMPEG(new String[]{"-y", "-i", recordedVideoUri, "-preset", "ultrafast", "-vf", scale, rescaleVideo}, rescaleVideo);

//        ===================================================================recording video scalling end ==================================================================

        StringBuilder sb2 = new StringBuilder();
        sb2.append(getExternalCacheDir());
        sb2.append("/Duet_video_" + sessionManager.getUser().getData().getUserName() + "_");
        sb2.append(format);
        sb2.append(".mp4");
        outFile = sb2.toString();
        Log.e("TAG", "concatenate: " + outFile);
        Log.e("recordingUrl", "====================================" + recordedRescaleVideo);
//      execFFmpegBinaryForConcat(new String[]{"-i", recordedVideoUri, "-i", videoFile, "-filter_complex", "[0:v]pad=iw*2:ih[int];[int][1:v]overlay=W/2:0[vid]", "-map", "[vid]", "-crf"," 23", "-preset", "veryfast", outFile}, outFile);//working code without sound
    }

    private String rescaleVideoFFMPEG(String[] strings, String rescaleFile) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        String ffmpegCommand = UtilCommand.main(strings);
        FFmpeg.executeAsync(ffmpegCommand, new ExecuteCallback() {
            @Override
            public void apply(final long executionId, final int returnCode) {

                progressDialog.dismiss();

                Log.d("TAG", String.format("FFmpeg process exited with rc %d.", returnCode));

                Log.d("TAG", "FFmpeg process output:");

                Config.printLastCommandOutput(Log.INFO);

                if (returnCode == RETURN_CODE_SUCCESS) {
                    Intent intent1 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent1.setData(Uri.fromFile(new File(Camera2DuetActivity.this.rescaleVideo)));
                    Camera2DuetActivity.this.sendBroadcast(intent1);
                    execFFmpegBinaryForConcat(new String[]{"-y", "-i", rescaleVideo, "-i", videoURL, "-filter_complex", "[0:v]pad=iw*2:ih:0:0[intv];[intv][1:v]overlay=W/2:0[vid]", "-map", "[vid]", "-map", "1:a", "-crf", "22", "-preset", "ultrafast", outFile}, outFile);//working code without sound
//                  execFFmpegBinaryForConcat(new String[]{"-i", recordedVideoUri, "-i", videoFile, "-filter_complex", "[0:v]pad=iw*2:ih:0:0[intv];[intv][1:v]overlay=W/2:0[vid]", "-map", "[vid]", "-map", "0:a", "-crf", "22", "-preset", "veryfast", outFile}, outFile);//working code without sound
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    Log.d("ffmpegFailure", outFile);
                    try {
                        Toast.makeText(Camera2DuetActivity.this, "Error Creating Video", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    try {
                        Toast.makeText(Camera2DuetActivity.this, "Error Creating Video", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", returnCode));
                }
            }
        });
        getWindow().clearFlags(16);
        return Camera2DuetActivity.this.rescaleVideo;
    }

    private void execFFmpegBinaryForConcat(String[] strArr, final String str) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.show();
        String ffmpegCommand = UtilCommand.main(strArr);
        FFmpeg.executeAsync(ffmpegCommand, new ExecuteCallback() {

            @Override
            public void apply(final long executionId, final int returnCode) {
                Log.d("TAG", String.format("FFmpeg process exited with rc %d.", returnCode));

                Log.d("TAG", "FFmpeg process output:");

                Config.printLastCommandOutput(Log.INFO);

                progressDialog.dismiss();
                if (returnCode == RETURN_CODE_SUCCESS) {
                    progressDialog.dismiss();
                    Intent intent1 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent1.setData(Uri.fromFile(new File(Camera2DuetActivity.this.outFile)));
                    Camera2DuetActivity.this.sendBroadcast(intent1);
//                    new File(str).delete();
                    File thumbFile = new File(getPath(), "/" + "user_thumbnail_" + sessionManager.getUser().getData().getUserName() + format + ".jpg");
                    try {
                        FileOutputStream stream = new FileOutputStream(thumbFile);

                        Bitmap bmThumbnail;

                        bmThumbnail = ThumbnailUtils.createVideoThumbnail(Camera2DuetActivity.this.outFile,
                                MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                        if (bmThumbnail != null) {
                            bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                        }
                        stream.flush();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(Camera2DuetActivity.this, PreviewActivity.class);
                    intent.putExtra("post_video", outFile);
                    intent.putExtra("post_image", thumbFile.getPath());
                    intent.putExtra("soundId", viewModel.soundId);
//                    intent.putExtra("post_sound", getPath().getPath().concat("/Medley_" + sessionManager.getUser().getData().getUserName() + "_" + viewModel.soundId + ".mp3"));
                    intent.putExtra("sound_image", getPath().getPath().concat("/soundImage.jpeg"));
                    startActivityForResult(intent, 101);
//                    viewModel.fetchVideoUrl(outFile);
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    Log.d("ffmpegfailure", str);
                    new File(str).delete();
                    try {
                        Toast.makeText(Camera2DuetActivity.this, "Error Creating Video", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    new File(str).delete();
                    try {
                        Toast.makeText(Camera2DuetActivity.this, "Error Creating Video", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                    Log.i(Config.TAG, String.format("Async command execution failed with rc=%d.", returnCode));
                }
            }
        });
        getWindow().clearFlags(16);
    }
}