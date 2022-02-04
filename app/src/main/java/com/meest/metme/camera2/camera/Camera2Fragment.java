
package com.meest.metme.camera2.camera;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.core.math.MathUtils;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.meestbhart.utilss.Constant;
import com.meest.metme.camera2.FocusView;
import com.meest.metme.camera2.gallery.ImagesGallery;
import com.meest.metme.camera2.preview.PreviewImageActivity;
import com.meest.metme.camera2.preview.PreviewVideoActivity;
import com.meest.metme.camera2.utills.CameraConstants;
import com.meest.metme.camera2.utills.CameraUtil;
import com.meest.metme.camera2.utills.SizeMap;
import com.meest.utils.ParameterConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Camera2Fragment extends Fragment {
    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;

    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();

    private static final SparseIntArray INTERNAL_FACINGS = new SparseIntArray();

    static {
        INTERNAL_FACINGS.put(CameraConstants.FACING_BACK, CameraCharacteristics.LENS_FACING_BACK);
        INTERNAL_FACINGS.put(CameraConstants.FACING_FRONT, CameraCharacteristics.LENS_FACING_FRONT);
    }

    private static final int REQUEST_CAMERA_PERMISSION = 1;

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

    /**
     * Camera state: Showing camera preview.
     */
    private static final int STATE_PREVIEW = 0;

    /**
     * Camera state: Waiting for the focus to be locked.
     */
    private static final int STATE_WAITING_LOCK = 1;

    /**
     * Camera state: Waiting for the exposure to be precapture state.
     */
    private static final int STATE_WAITING_PRECAPTURE = 2;

    /**
     * Camera state: Waiting for the exposure state to be something other than precapture.
     */
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;

    /**
     * Camera state: Picture was taken.
     */
    private static final int STATE_PICTURE_TAKEN = 4;

    /**
     * Max preview width that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_WIDTH = 1920;

    /**
     * Max preview height that is guaranteed by Camera2 API
     */
    private static final int MAX_PREVIEW_HEIGHT = 1080;


    private static final int MSG_CAPTURE_PICTURE_WHEN_FOCUS_TIMEOUT = 100;

    private Rect mCropRegion;

    private MeteringRectangle[] mAFRegions = AutoFocusHelper.getZeroWeightRegion();

    private MeteringRectangle[] mAERegions = AutoFocusHelper.getZeroWeightRegion();

    private AspectRatio mAspectRatio = CameraConstants.DEFAULT_ASPECT_RATIO;

    private final SizeMap mPreviewSizes = new SizeMap();

    /**
     * ID of the current {@link CameraDevice}.
     */
    private String mCameraId;

    /**
     * An {@link AutoFitTextureView} for camera preview.
     */
    public AutoFitTextureView mTextureView;

    /**
     * The view for manual tap to focus
     */
    public FocusView mFocusView;

    /**
     * A {@link CameraCaptureSession } for camera preview.
     */
    private CameraCaptureSession mPreviewSession;

    /**
     * A reference to the opened {@link CameraDevice}.
     */
    private CameraDevice mCameraDevice;


    /**
     * {@link CaptureRequest.Builder} for the camera preview
     */
    private CaptureRequest.Builder mPreviewRequestBuilder;

    /**
     * {@link CaptureRequest} generated by {@link #mPreviewRequestBuilder}
     */
    private CaptureRequest mPreviewRequest;


    private CameraCharacteristics mCameraCharacteristics;

    /**
     * The current state of camera state for taking pictures.
     *
     * @see #mCaptureCallback
     */
    private int mState = STATE_PREVIEW;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private final Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /**
     * The current camera auto focus mode
     */
    private boolean mAutoFocus = true;

    /**
     * Whether the current camera device supports auto focus or not.
     */
    private boolean mAutoFocusSupported = true;

    /**
     * The current camera flash mode
     */
    private int mFlash = CameraConstants.FLASH_OFF;

    /**
     * Whether the current camera device supports flash or not.
     */
    private boolean mFlashSupported = true;

    /**
     * The current camera facing mode
     */
    private int mFacing = CameraConstants.FACING_BACK;

    /**
     * Whether the current camera device can switch back/front or not.
     */
    private boolean mFacingSupported = true;

    /**
     * Orientation of the camera sensor
     */
    private int mSensorOrientation;

    /**
     * The {@link Size} of camera preview.
     */
    private Size mPreviewSize;

    /**
     * The {@link Size} of video recording.
     */
    private Size mVideoSize;

    /**
     * MediaRecorder
     */
    private MediaRecorder mMediaRecorder;

    /**
     * Whether the camera is recording video now
     */
    private boolean mIsRecordingVideo;

    /**
     * Whether the camera is manual focusing now
     */
    private boolean mIsManualFocusing;

    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread mBackgroundThread;

    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler mBackgroundHandler;

    /**
     * An {@link ImageReader} that handles still image capture.
     */
    private ImageReader mImageReader;

    /**
     * The output file path of video recording.
     */
    private String mNextVideoAbsolutePath;

    /**
     * The output file path of take picture.
     */
    private String mNextPictureAbsolutePath;


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


            configureTransform(width, height);


        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            if (mTextureView != null) {
                mTextureView.setSurfaceTextureListener(null);
            }
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
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;

            createCameraPreviewSession();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            Activity activity = getActivity();
            if (null != activity) {
                showToast("Camera is error: " + error);
                activity.finish();
            }
        }

    };


    /**
     * This a callback object for the {@link ImageReader}. "onImageAvailable" will be called when a
     * still image is ready to be saved.
     */
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {


        @Override
        public void onImageAvailable(ImageReader reader) {

//            if (SDK_INT >= Build.VERSION_CODES.Q) {
//
//                mNextPictureAbsolutePath = saveImageToGallery(reader,mContext);
//                Intent intent = new Intent(mContext, PreviewImageActivity.class);
//                intent.putExtra("image", mNextPictureAbsolutePath);
//                mContext.startActivity(intent);
//
//            }
//            else
//            {

            mNextPictureAbsolutePath = getPictureFilePath(getActivity());
            mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), new File(mNextPictureAbsolutePath)));
            if (CameraUtil.gridLayout2Active) {
                grid2Condition();
            } else if (CameraUtil.gridLayout3Active) {
                grid3Condition();
            } else if (CameraUtil.gridLayout4Active) {
                grid4Condition();
            } else if (CameraUtil.gridLayout6Active) {
                grid6Condition();
            } else if (CameraUtil.gridLayout2ActiveHorizontal) {
                grid2ConditionHorizontal();
            } else if (CameraUtil.multiSnapEnable) {
                multiSnapList.add(mNextPictureAbsolutePath);
                getActivity().runOnUiThread(() -> {
                    if (multiSnapList != null) {

                        if (multiSnapList.size() == 1) {
                            progressbarMC.setProgress(33);
                            multiCaptureNextButton.setVisibility(View.VISIBLE);
//                                Size mSize = new Size(300,300);
//                                CancellationSignal ca = new CancellationSignal();
//                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//                                    try {
////                                        Bitmap bitmapThumbnail = mContext.getContentResolver().loadThumbnail(Uri.parse(mNextPictureAbsolutePath), mSize, ca);
////                                        multiCaptureImage1.setImageBitmap(bitmapThumbnail);
//
//                                           // multiCaptureImage1.setImageURI(Uri.parse(multiSnapList.get(0)));
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
                            showImage(mNextPictureAbsolutePath, multiCaptureImage1);

                            // multiCaptureImage1.setImageURI(Uri.parse(mNextPictureAbsolutePath));
                            //CameraUtil.setVideoThumbnail(multiCaptureImage1,mNextPictureAbsolutePath);
                        } else if (multiSnapList.size() == 2) {
                            progressbarMC.setProgress(66);
                            showImage(mNextPictureAbsolutePath, multiCaptureImage2);
                            //CameraUtil.setVideoThumbnail(multiCaptureImage2,mNextPictureAbsolutePath);
                        } else if (multiSnapList.size() == 3) {
                            /// showImage(mNextPictureAbsolutePath, multiCaptureImage3);
                            Intent intent = new Intent(getContext(), PreviewImageActivity.class);
                            intent.putStringArrayListExtra("multiSnaps", multiSnapList);
                            startActivityForResult(intent, ParameterConstants.PICKER);

                            progressbarMC.setProgress(100);
                            showImage(mNextPictureAbsolutePath, multiCaptureImage3);
                            multiSnapCapture.setEnabled(false);
//                            Intent intent = new Intent(getContext(), PreviewImageActivity.class);
//                            intent.putStringArrayListExtra("multiSnaps", multiSnapList);
//                            startActivity(intent);
                        }
                    }
                });

            } else {
                getActivity().runOnUiThread(() -> {
                    Log.e(TAG, "onImageAvailable: " + mNextPictureAbsolutePath);
                    Intent intent = new Intent(getActivity(), PreviewImageActivity.class);
                    intent.putExtra("image", mNextPictureAbsolutePath);
                    startActivityForResult(intent, ParameterConstants.PICKER);

                });

            }
            //   }


        }

    };

    LottieAnimationView animationView;

    public static String saveImageToGallery(ImageReader reader, Context context) {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, "your SD card is not available", Toast.LENGTH_SHORT).show();
            return null;
        }
        Image image = reader.acquireNextImage();
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        image.close();//after you use the image's content ,you can close it
        String filePath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
        String picturePath = System.currentTimeMillis() + ".jpg";
        File imgFile = new File(filePath, picturePath);
        Uri uri = Uri.fromFile(imgFile);


        try {//Store to folder
            FileOutputStream fileOutputStream = new FileOutputStream(imgFile);
            fileOutputStream.write(data);
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return uri.getPath();
    }

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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

        private void process(CaptureResult result) {
            //  Log.i(TAG, "CaptureCallback mState: " + mState);
            switch (mState) {
                case STATE_PREVIEW: {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    Log.i(TAG, "STATE_WAITING_LOCK afState: " + afState);
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
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
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
    Size choosePictureSize(Size[] choices) {
        List<Size> pictureSizes = Arrays.asList(choices);
        Collections.sort(pictureSizes, new CompareSizesByArea());
        int maxIndex = pictureSizes.size() - 1;
        for (int i = maxIndex; i >= 0; i--) {
            if (pictureSizes.get(i).getWidth() == pictureSizes.get(i).getHeight() *
                    mAspectRatio.getX() / mAspectRatio.getY()) {
                Log.e(TAG, "pictureSizes: " + pictureSizes.get(i));
                return pictureSizes.get(i);

            }
        }
        return pictureSizes.get(maxIndex);
    }

    Size chooseVideoSize(Size[] choices) {
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

    public static int mw;
    public static int mh;

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, choose the smallest one that
     * is at least as large as the respective texture view size, and that is at most as large as the
     * respective max size, and whose aspect ratio matches with the specified value. If such size
     * doesn't exist, choose the largest one that is at most as large as the respective max size,
     * and whose aspect ratio matches with the specified value.
     *
     * @param choices           The list of sizes that the camera supports for the intended output
     *                          class
     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
     * @param textureViewHeight The height of the texture view relative to sensor coordinate
     * @param maxWidth          The maximum width that can be chosen
     * @param maxHeight         The maximum height that can be chosen
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                           int textureViewHeight, int maxWidth, int maxHeight) {
        mPreviewSizes.clear();
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w;
        int h;

        if (CameraUtil.fsEnable) {

            w = maxWidth;
            h = maxHeight;
        } else {
            w = mAspectRatio.getX();
            h = mAspectRatio.getY();
        }


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


        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight) {
                //Log.e(TAG, "choices Size: "+option.getWidth()  +" "+ option.getHeight());
                mPreviewSizes.add(new com.meest.metme.camera2.utills.Size(option.getWidth(), option.getHeight()));
                if (option.getHeight() == option.getWidth() * h / w) {
                    if (option.getWidth() >= textureViewWidth &&
                            option.getHeight() >= textureViewHeight) {
                        //Log.e(TAG, "option 1 : "+option);
                        bigEnough.add(option);
                    } else {
                        // Log.e(TAG, "option 2 : "+option);
                        notBigEnough.add(option);
                    }
                }
            }
        }
//            Handler handler = new Handler();
//        Runnable runnable = () -> {
//            final Set<AspectRatio> ratios = getSupportedAspectRatios();
//            if (ratios != null) {
//                if (ratios.size() > 0) {
//                    CameraUtil.fsAspectRatio = Collections.max(ratios);
//                    CameraConstants.DEFAULT_ASPECT_RATIO = CameraUtil.fsAspectRatio;
//                }
//            }
//        };
//        handler.postDelayed(runnable,1000);


        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {

            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {

            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            mAspectRatio = AspectRatio.of(1, 1);
            SortedSet<com.meest.metme.camera2.utills.Size> sortedSet = mPreviewSizes.sizes(mAspectRatio);
            if (sortedSet != null) {
                com.meest.metme.camera2.utills.Size lastSize = sortedSet.last();
                return new Size(lastSize.getWidth(), lastSize.getHeight());
            }
            mAspectRatio = AspectRatio.of(choices[0].getWidth(), choices[0].getHeight());

            return choices[0];
        }
    }

    private Chronometer chronometer;

    public static Camera2Fragment newInstance() {

        return new Camera2Fragment();
    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_camera2, container, false);
        mTextureView = view.findViewById(R.id.texture);
        chronometer = getActivity().findViewById(R.id.chronometer);


//        chronometer.setOnChronometerTickListener(chronometer -> {
//            recordTimer = SystemClock.elapsedRealtime() - chronometer.getBase();
//            Log.e(TAG, "recordTimer: "+recordTimer);
//        });
//
        initGridLayoutViews();
        initSuperZoom();
        initHandsFreeImageView();
        multiSnapCaptureView();


        return view;

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        mTextureView = view.findViewById(R.id.texture);
        mFocusView = view.findViewById(R.id.focusView);
        mTextureView.setGestureListener(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (CameraUtil.focusEnable) {
                    setFocusViewWidthAnimation((int) e.getX(), (int) e.getY());
                    setManualFocusAt((int) e.getX(), (int) e.getY());
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        start();

    }

    @Override
    public void onPause() {
        stop();
        super.onPause();
    }

    public void start() {

        startBackgroundThread();

        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).


        if (mTextureView.isAvailable()) {


            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    public void stop() {
        closeCamera();
        stopBackgroundThread();
    }

    /**
     * Focus view animation
     */
    private void setFocusViewWidthAnimation(float x, float y) {
        mFocusView.setVisibility(View.VISIBLE);

        mFocusView.setX(x - mFocusView.getWidth() / 2);
        mFocusView.setY(y - mFocusView.getHeight() / 2);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mFocusView, "scaleX", 1, 0.6f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mFocusView, "scaleY", 1, 0.6f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mFocusView, "alpha", 1f, 0.3f, 1f, 0.3f, 1f, 0.3f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mFocusView.setVisibility(View.GONE);
            }
        });
        animSet.play(scaleX).with(scaleY).before(alpha);
        animSet.setDuration(300);
        animSet.start();
    }

    private boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final int permission_camera = PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
            final int record_audio = PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
            final int permission_readexternal = PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            final int permission_writeexternal = PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission_camera == PermissionChecker.PERMISSION_GRANTED
                    && permission_readexternal == PermissionChecker.PERMISSION_GRANTED
                    && permission_writeexternal == PermissionChecker.PERMISSION_GRANTED
                    && record_audio == PermissionChecker.PERMISSION_GRANTED) {

                Log.e(TAG, "Permission Accepted 1: ");
            } else {
                Log.e(TAG, "Permission Denied: ");
                checkPermissions();
            }
        } else {
            Log.e(TAG, "Permission Accepted 3: ");

        }
    }

    private boolean checkPermissions() {
//        int result;
//        List<String> listPermissionsNeeded = new ArrayList<>();
//        for (String p : VIDEO_PERMISSIONS) {
//            result = ContextCompat.checkSelfPermission(getActivity(), p);
//            if (result != PackageManager.PERMISSION_GRANTED) {
//                listPermissionsNeeded.add(p);
//
//            }
//        }
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[0]), REQUEST_CAMERA_PERMISSION);
//            return false;
//        }

        int camera = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA);
        int record_audio = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO);

        int permission_readexternal = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE);

        int permission_writeexternal = ContextCompat.checkSelfPermission(getActivity(),
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
//        if (permission_writeexternal != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
        if (permission_writeexternal != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CAMERA_PERMISSION);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
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
        int internalFacing = INTERNAL_FACINGS.get(mFacing);
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            String[] cameraIds = manager.getCameraIdList();
            mFacingSupported = cameraIds.length > 1;
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

                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }

                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                // Danger, W.R.! Attempting to use too large a preview size could exceed the camera
                // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
                // garbage capture data.
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                        maxPreviewHeight);

                mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));

                // For still image captures, we use the largest available size.
                Size largest = choosePictureSize(map.getOutputSizes(ImageFormat.JPEG));
                Log.e(TAG, "largest: " + largest.toString());
                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                        ImageFormat.JPEG, /*maxImages*/2);
                mImageReader.setOnImageAvailableListener(
                        mOnImageAvailableListener, mBackgroundHandler);

                // We fit the aspect ratio of TextureView to the size of preview we picked.
                int orientation = getResources().getConfiguration().orientation;


                mTextureView.setAspectRatio(
                        mPreviewSize.getHeight(), mPreviewSize.getWidth());


                checkAutoFocusSupported();
                checkFlashSupported();

                mCropRegion = AutoFocusHelper.cropRegionForZoom(mCameraCharacteristics,
                        CameraConstants.ZOOM_REGION_DEFAULT);

                mCameraId = cameraId;
                Log.i(TAG, "CameraId: " + mCameraId + " ,isFlashSupported: " + mFlashSupported);
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
        mAutoFocusSupported = !(modes == null || modes.length == 0 ||
                (modes.length == 1 && modes[0] == CameraCharacteristics.CONTROL_AF_MODE_OFF));
    }

    /**
     * Check if the flash is supported.
     */
    private void checkFlashSupported() {
        Boolean available = mCameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
        mFlashSupported = available != null && available;
    }

    /**
     * Opens the camera specified by {@link Camera2Fragment#mCameraId}.
     */
    @SuppressWarnings("MissingPermission")
    public void openCamera(int width, int height) {
        if (!checkPermissions()) {
            requestCameraPermission();
            return;
        }


        mw = width;
        mh = height;
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
        configureTransform(width, height);
        Activity activity = getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(CameraConstants.OPEN_CAMERA_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            mMediaRecorder = new MediaRecorder();
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
            getImagesFromGallery();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }


    private ArrayList<String> thumbnails = new ArrayList<>();
    ImageView gallery;

    public void getImagesFromGallery() {
        gallery = getActivity().findViewById(R.id.gallery);
        Handler handler = new Handler();
        Runnable runnable = () -> {

            try {
                thumbnails = ImagesGallery.listOfImages(getActivity());
                getActivity().runOnUiThread(() -> Glide.with(this)
                        .load(thumbnails.get(0))
                        .centerCrop()
                        .into(gallery));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        handler.postDelayed(runnable, 1000);

    }


    /**
     * Closes the current {@link CameraDevice}.
     */
    public void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mPreviewSession) {
//                mPreviewSession.close();
//                mPreviewSession = null;
            }
            if (null != mCameraDevice) {
//                mCameraDevice.close();
//                mCameraDevice = null;
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

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MSG_CAPTURE_PICTURE_WHEN_FOCUS_TIMEOUT) {
                    mState = STATE_PICTURE_TAKEN;
                    captureStillPicture();
                }

            }
        };
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {


        try {
            mBackgroundThread.quitSafely();
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new {@link CameraCaptureSession} for camera preview.
     */
    public static SurfaceTexture texture;

    private void createCameraPreviewSession() {
        try {
            texture = mTextureView.getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == mCameraDevice) {
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            mPreviewSession = cameraCaptureSession;
                            try {
                                // Auto focus should be continuous for camera preview.
//                            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
//                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                updateAutoFocus();
                                // Flash is automatically enabled when necessary.
                                updateFlash(mPreviewRequestBuilder);

                                // Finally, we start displaying the camera preview.
                                mPreviewRequest = mPreviewRequestBuilder.build();

                                        mPreviewSession.setRepeatingRequest(mPreviewRequest,
                                                mCaptureCallback, mBackgroundHandler);

                            } catch (Exception e) {
                                Log.e(TAG, "Exception: "+e.getMessage());
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
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configures the necessary {@link Matrix} transformation to `mTextureView`.
     * This method should be called after the camera preview size is determined in
     * setupCameraOutputs and also the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (null == mTextureView || null == mPreviewSize || null == activity) {
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
        mTextureView.setTransform(matrix);
    }

    public Set<AspectRatio> getSupportedAspectRatios() {

        return mPreviewSizes.ratios();

    }

    public AspectRatio getAspectRatio() {

        return mAspectRatio;
    }

    public void setAspectRatio(AspectRatio aspectRatio) {
        if (aspectRatio == null || aspectRatio.equals(mAspectRatio) ||
                !mPreviewSizes.ratios().contains(aspectRatio)) {
            return;
        }

        mAspectRatio = aspectRatio;
        if (isCameraOpened()) {
            stop();
            start();
        }
    }

    public boolean isCameraOpened() {
        return mCameraDevice != null;
    }

    public void setFacing(int facing) {
        if (mFacing == facing) {
            return;
        }
        mFacing = facing;
        if (isCameraOpened()) {
            stop();
            start();
        }
    }

    public int getFacing() {
        return mFacing;
    }

    /**
     * The facing is supported or not.
     */
    public boolean isFacingSupported() {
        return mFacingSupported;
    }

    public void setFlash(int flash) {
        if (mFlash == flash) {
            return;
        }
        int saved = mFlash;
        mFlash = flash;
        if (mPreviewRequestBuilder != null) {
            updateFlash(mPreviewRequestBuilder);
            if (mPreviewSession != null) {
                try {
                    mPreviewSession.setRepeatingRequest(mPreviewRequestBuilder.build(),
                            mCaptureCallback, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    mFlash = saved; // Revert
                }
            }
        }
    }

    public int getFlash() {
        return mFlash;
    }

    /**
     * Updates the internal state of flash to {@link #mFlash}.
     */
    void updateFlash(CaptureRequest.Builder requestBuilder) {
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

    /**
     * The flash is supported or not.
     */
    public boolean isFlashSupported() {
        return mFlashSupported;
    }

    public void setAutoFocus(boolean autoFocus) {
        if (mAutoFocus == autoFocus) {
            return;
        }
        mAutoFocus = autoFocus;
        if (mPreviewRequestBuilder != null) {
            updateAutoFocus();
            if (mPreviewSession != null) {
                try {
                    mPreviewSession.setRepeatingRequest(mPreviewRequestBuilder.build(),
                            mCaptureCallback, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    mAutoFocus = !mAutoFocus; // Revert
                }
            }
        }
    }

    public boolean getAutoFocus() {
        return mAutoFocus;
    }

    /**
     * The auto focus is supported or not.
     */
    public boolean isAutoFocusSupported() {
        return mAutoFocusSupported;
    }

    /**
     * Updates the internal state of auto-focus to {@link #mAutoFocus}.
     */
    void updateAutoFocus() {
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

    /**
     * Updates the internal state of manual focus.
     */
    void updateManualFocus(float x, float y) {
        @SuppressWarnings("ConstantConditions")
        int sensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        mAFRegions = AutoFocusHelper.afRegionsForNormalizedCoord(x, y, mCropRegion, sensorOrientation);
        mAERegions = AutoFocusHelper.aeRegionsForNormalizedCoord(x, y, mCropRegion, sensorOrientation);
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, mAFRegions);
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_REGIONS, mAERegions);
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
    }

    void setManualFocusAt(int x, int y) {
        int mDisplayOrientation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        float[] points = new float[2];
        points[0] = (float) x / mTextureView.getWidth();
        points[1] = (float) y / mTextureView.getHeight();
        Matrix rotationMatrix = new Matrix();
        rotationMatrix.setRotate(mDisplayOrientation, 0.5f, 0.5f);
        rotationMatrix.mapPoints(points);
        if (mPreviewRequestBuilder != null) {
            mIsManualFocusing = true;
            updateManualFocus(points[0], points[1]);
            if (mPreviewSession != null) {
                try {
                    mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                            CaptureRequest.CONTROL_AF_TRIGGER_START);
                    mPreviewSession.capture(mPreviewRequestBuilder.build(), null, mBackgroundHandler);
                    mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                            CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
                    mPreviewSession.setRepeatingRequest(mPreviewRequestBuilder.build(),
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
            if (mPreviewRequestBuilder != null) {
                mIsManualFocusing = false;
                updateAutoFocus();
                if (mPreviewSession != null) {
                    try {
                        mPreviewSession.setRepeatingRequest(mPreviewRequestBuilder.build(),
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

    /**
     * Initiate a still image capture.
     */
    public void takePicture() {
        if (!mIsManualFocusing && mAutoFocus && mAutoFocusSupported) {
            Log.i(TAG, "takePicture lockFocus");
            capturePictureWhenFocusTimeout(); //Sometimes, camera do not focus in some devices.
            lockFocus();
        } else {
            Log.i(TAG, "takePicture captureStill");
            captureStillPicture();
        }
    }

    public void takeSinglePicture() {
        if (!mIsManualFocusing && mAutoFocus && mAutoFocusSupported) {
            Log.i(TAG, "takePicture lockFocus");
            capturePictureWhenFocusTimeout(); //Sometimes, camera do not focus in some devices.
            lockFocus();
        } else {
            Log.i(TAG, "takePicture captureStill");
            captureStillPicture();
        }
    }

    private void playShutterSound() {

//        Log.v(TAG, "Initializing sounds...");
//
//        final MediaPlayer mp = MediaPlayer.create(this, R.raw.shutter_sound);
//                Log.v(TAG, "Playing sound...");
//                mp.start();
    }

    /**
     * Capture picture when auto focus timeout
     */
    private void capturePictureWhenFocusTimeout() {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.sendEmptyMessageDelayed(MSG_CAPTURE_PICTURE_WHEN_FOCUS_TIMEOUT,
                    CameraConstants.AUTO_FOCUS_TIMEOUT_MS);
        }
    }

    /**
     * Remove capture message, because auto focus work correctly.
     */
    private void removeCaptureMessage() {
        if (mBackgroundHandler != null) {
            mBackgroundHandler.removeMessages(MSG_CAPTURE_PICTURE_WHEN_FOCUS_TIMEOUT);
        }
    }

    /**
     * Lock the focus as the first step for a still image capture.
     */
    private void lockFocus() {
        try {
            // This is how to tell the camera to lock focus.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the lock.
            mState = STATE_WAITING_LOCK;
            mPreviewSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unlock the focus. This method should be called when still image capture sequence is
     * finished.
     */
    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            mPreviewSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
                    mBackgroundHandler);

            updateAutoFocus();
            updateFlash(mPreviewRequestBuilder);
            // After this, the camera will go back to the normal state of preview.
            mState = STATE_PREVIEW;
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
            mPreviewSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }catch (IllegalStateException e) {
            getActivity().finish();
            e.printStackTrace();
        }
    }

    /**
     * Run the precapture sequence for capturing a still image. This method should be called when
     * we get a response in {@link #mCaptureCallback} from {@link #lockFocus()}.
     */
    private void runPrecaptureSequence() {
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

    /**
     * Capture a still picture. This method should be called when we get a response in
     * {@link #mCaptureCallback} from both {@link #lockFocus()}.
     */
    private void captureStillPicture() {
        try {
            removeCaptureMessage();
            final Activity activity = getActivity();
            if (null == activity || null == mCameraDevice) {
                return;
            }
            // This is the CaptureRequest.Builder that we use to take a picture.
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            // Use the same AE and AF modes as the preview.
//            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
//                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
//            updateAutoFocus();
            updateFlash(captureBuilder);

            // Orientation
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            CameraCaptureSession.CaptureCallback CaptureCallback
                    = new CameraCaptureSession.CaptureCallback() {

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

    /**
     * Retrieves the JPEG orientation from the specified screen rotation.
     *
     * @param rotation The screen rotation.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */
    private int getOrientation(int rotation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from DEFAULT_ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (DEFAULT_ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    private String getVideoFilePath(Context context) {
      /*  final File dir = context.getExternalFilesDir(null);
        return (dir == null ? "" : (dir.getAbsolutePath() + "/"))
                + System.currentTimeMillis() + ".mp4";*/
        return Constant.createOutputPath(context,".mp4");
    }

    private String getPictureFilePath(Context context) {

      /*  final File dir = context.getExternalFilesDir(null);
        return (dir == null ? "" : (dir.getAbsolutePath() + "/"))
                + System.currentTimeMillis() + ".jpg";*/
        return Constant.createOutputPath(context,".jpg");
    }

    private void setupMediaRecorder() throws IOException {
        final Activity activity = getActivity();
        if (null == activity) {
            return;
        }
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mNextVideoAbsolutePath = getVideoFilePath(getActivity());
        mMediaRecorder.setOutputFile(mNextVideoAbsolutePath);
        mMediaRecorder.setVideoEncodingBitRate(10000000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (mSensorOrientation) {
            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                mMediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
                break;
            case SENSOR_ORIENTATION_INVERSE_DEGREES:
                mMediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
                break;
        }
        mMediaRecorder.prepare();
    }


    /**
     * Start recording video
     */
    public void startRecordingVideo() {

        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }
        try {


            mIsRecordingVideo = true;
            setupMediaRecorder();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<>();

            // Set up Surface for the camera preview
            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            mPreviewRequestBuilder.addTarget(previewSurface);

            // Set up Surface for the MediaRecorder
            Surface recorderSurface = mMediaRecorder.getSurface();
            surfaces.add(recorderSurface);
            mPreviewRequestBuilder.addTarget(recorderSurface);

            // Start a capture session
            // Once the session starts, we can update the UI and start recording
            mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    mPreviewSession = cameraCaptureSession;
                    try {
                        // Auto focus should be continuous for camera preview.
//                        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
//                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_VIDEO);
                        updateAutoFocus();
                        // Flash is automatically enabled when necessary.
                        updateFlash(mPreviewRequestBuilder);

                        // For test
                        Integer stabilizationMode = mPreviewRequestBuilder.
                                get(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE);
                        if (stabilizationMode != null &&
                                stabilizationMode == CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE_OFF) {
                            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE,
                                    CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE_ON);
                        }

                        // Finally, we start displaying the camera preview.
                        mPreviewRequest = mPreviewRequestBuilder.build();
                        mPreviewSession.setRepeatingRequest(mPreviewRequest, null, mBackgroundHandler);

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
                            mMediaRecorder.start();
                            if (!animationView.isAnimating()) {
                                animationView.setVisibility(View.VISIBLE);
                                animationView.playAnimation();
                            }

                        });
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    showToast("Start recording video configure failed");
                }

                @Override
                public void onClosed(@NonNull CameraCaptureSession session) {
                    super.onClosed(session);
                    stopBackgroundThread();
                }
            }, mBackgroundHandler);

            chronometer.setVisibility(View.VISIBLE);
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start(); // start a chronometer
            chronometer.setFormat("%s");

            if (CameraUtil.progressTimer) {
                startVideoProgress();
                Log.e(TAG, "startRecordingVideo: ");
            }

        } catch (CameraAccessException | IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Stop recording video
     */
    public void stopRecordingVideo() {
        getActivity().runOnUiThread(() -> {
            getActivity().runOnUiThread(() -> {
                chronometer.stop();
                chronometer.setVisibility(View.GONE);
                if (animationView.isAnimating()) {
                    animationView.cancelAnimation();
                    animationView.setVisibility(View.GONE);
                }
                mIsRecordingVideo = false;
            });

            Log.e(TAG, "Video saved: " + mNextVideoAbsolutePath);


            closeCamera();
//            openCamera(mTextureView.getWidth(), mTextureView.getHeight());


            if (CameraUtil.handsFreeEnable) {
                handsFreeVideos.add(mNextVideoAbsolutePath);
                if (handsFreeVideos != null) {
                    if (thumbsUp) {
                        Intent intent = new Intent(getContext(), PreviewVideoActivity.class);
                        intent.putStringArrayListExtra("handsFreeVideos", handsFreeVideos);
                        startActivityForResult(intent, ParameterConstants.PICKER);
                    }
                    if (handsFreeVideos.size() == 1) {
                        CameraUtil.setVideoThumbnail(handsFreeImage1, mNextVideoAbsolutePath);

                    } else if (handsFreeVideos.size() == 2) {
                        CameraUtil.setVideoThumbnail(handsFreeImage2, mNextVideoAbsolutePath);

                    } else if (handsFreeVideos.size() == 3 && !thumbsUp) {
                        getActivity().runOnUiThread(() -> {

                            Intent intent = new Intent(getContext(), PreviewVideoActivity.class);
                            intent.putStringArrayListExtra("handsFreeVideos", handsFreeVideos);
                            startActivityForResult(intent, ParameterConstants.PICKER);
                        });
                    }

                }
            } else if (CameraUtil.speedEnable) {
                Intent intent = new Intent(getContext(), PreviewVideoActivity.class);
                intent.putExtra("speedVideoPath", mNextVideoAbsolutePath);
                intent.putExtra("type", "slowmo");
                startActivityForResult(intent, ParameterConstants.PICKER);
            } else if (CameraUtil.gridLayoutHVideoActive) {
                mergeVideo.add(mNextVideoAbsolutePath);
                grid2VideoConditionH();
            } else {
                Intent intent = new Intent(getContext(), PreviewVideoActivity.class);

//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("videoPath", mNextVideoAbsolutePath);
                startActivityForResult(intent, ParameterConstants.PICKER);
            }

        });
    }

    public boolean isRecordingVideo() {
        return mIsRecordingVideo;
    }

    public void stopRecordingBoomVideo() {
        getActivity().runOnUiThread(() -> {
            getActivity().runOnUiThread(() -> {
                chronometer.stop();
                chronometer.setVisibility(View.GONE);
                if (animationView.isAnimating()) {
                    animationView.cancelAnimation();
                    animationView.setVisibility(View.GONE);
                }
                mIsRecordingVideo = false;
            });

            Log.e(TAG, "Video saved: " + mNextVideoAbsolutePath);
            closeCamera();
            Intent intent = new Intent(getContext(), PreviewVideoActivity.class);
            intent.putExtra("boomerang", mNextVideoAbsolutePath);
            startActivityForResult(intent,ParameterConstants.PICKER);
//            openCamera(mTextureView.getWidth(), mTextureView.getHeight());


        });
    }


    /**
     * Saves a JPEG {@link Image} into the specified {@link File}.
     */
    private static class ImageSaver implements Runnable {

        /**
         * The JPEG image
         */
        private final Image mImage;
        /**
         * The file we save the image into.
         */
        private final File mFile;

        ImageSaver(Image image, File file) {
            mImage = image;
            mFile = file;
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(mFile);
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
    private static class CompareSizesByArea implements Comparator<Size> {

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




    //Here is Layout Grid Code:-
    public void updateTextureViewSize(int viewWidth, int viewHeight) {

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(mTextureView, "translationY", 0, viewWidth, viewHeight);
        anim1.setDuration(200);
        anim1.start();


    }

    //Layout Grid Code:-

    public ImageView checkImage;

    public ImageView twoGridImage1, twoGridImage2;
    public ImageView threeGridImage1, threeGridImage2, threeGridImage3;
    public ImageView fourGridImage1, fourGridImage2, fourGridImage3, fourGridImage4;
    public ImageView sixGridImage1, sixGridImage2, sixGridImage3, sixGridImage4, sixGridImage5, sixGridImage6;
    public ImageView twoGridImage1Horizontal, twoGridImage2Horizontal;

    public LinearLayout grid2Layout, grid3Layout, grid4Layout, grid6Layout, grid2LayoutHorizontal;

    ProgressBar progressBar;
    public static int screenHeight;
    public static int screenWidth;

    public ArrayList<String> mergeVideo = new ArrayList<>();

    private void initGridLayoutViews() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        grid2Layout = view.findViewById(R.id.grid2Layout);
        grid3Layout = view.findViewById(R.id.grid3Layout);
        grid4Layout = view.findViewById(R.id.grid4Layout);
        grid6Layout = view.findViewById(R.id.grid6Layout);
        grid2LayoutHorizontal = view.findViewById(R.id.grid2LayoutHorizontal);

        twoGridImage1 = view.findViewById(R.id.twoGridImage1);
        twoGridImage2 = view.findViewById(R.id.twoGridImage2);

        threeGridImage1 = view.findViewById(R.id.threeGridImage1);
        threeGridImage2 = view.findViewById(R.id.threeGridImage2);
        threeGridImage3 = view.findViewById(R.id.threeGridImage3);

        fourGridImage1 = view.findViewById(R.id.fourGridImage1);
        fourGridImage2 = view.findViewById(R.id.fourGridImage2);
        fourGridImage3 = view.findViewById(R.id.fourGridImage3);
        fourGridImage4 = view.findViewById(R.id.fourGridImage4);

        sixGridImage1 = view.findViewById(R.id.sixGridImage1);
        sixGridImage2 = view.findViewById(R.id.sixGridImage2);
        sixGridImage3 = view.findViewById(R.id.sixGridImage3);
        sixGridImage4 = view.findViewById(R.id.sixGridImage4);
        sixGridImage5 = view.findViewById(R.id.sixGridImage5);
        sixGridImage6 = view.findViewById(R.id.sixGridImage6);

        twoGridImage1Horizontal = view.findViewById(R.id.twoGridImage1Horizontal);
        twoGridImage2Horizontal = view.findViewById(R.id.twoGridImage2Horizontal);

        if (getActivity() != null) {
            progressBar = getActivity().findViewById(R.id.progressBar);
            checkImage = getActivity().findViewById(R.id.checkImage);
        }

    }

    public void gridLayoutTwo() {
        progressBar.setProgress(0);
        progressBar.setMax(100);
        CameraUtil.gridLayout2Active = true;
        grid2Layout.setVisibility(View.VISIBLE);
    }


    public void gridLayoutThree() {
        CameraUtil.gridLayout3Active = true;
        grid3Layout.setVisibility(View.VISIBLE);
    }

    public void gridLayoutFour() {
        CameraUtil.gridLayout4Active = true;
        //mTextureView.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth / 2, screenHeight / 2));
        grid4Layout.setVisibility(View.VISIBLE);
    }

    public void gridLayoutSix() {
        CameraUtil.gridLayout6Active = true;
        //mTextureView.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth / 2, screenHeight / 3));
        grid6Layout.setVisibility(View.VISIBLE);
    }

    public void gridLayoutTwoHorizontal() {
        CameraUtil.gridLayout2ActiveHorizontal = true;
        grid2LayoutHorizontal.setVisibility(View.VISIBLE);
    }

    public void gridLayoutTwoVideoH() {
        CameraUtil.gridLayoutHVideoActive = true;
        grid2LayoutHorizontal.setVisibility(View.VISIBLE);
    }

    //Here Managed the Conditions of Grid Layout
    public void grid2Condition() {

        getActivity().runOnUiThread(() -> {
            CameraUtil.twoGridValue++;
            if (CameraUtil.twoGridValue == 1) {
                showImage(mNextPictureAbsolutePath, twoGridImage1);
                progressBar.setProgress(50);
                updateTextureViewSize(screenWidth, (int) (screenHeight / 2.5));
                twoGridImage2.setImageResource(0);

            } else if (CameraUtil.twoGridValue == 2) {
                progressBar.setProgress(100);
                setAlpha(twoGridImage2);
                showImage(mNextPictureAbsolutePath, twoGridImage2);
                checkImage.setImageResource(R.drawable.ic_outline_check_24);

                Handler handler = new Handler();
                Runnable runnable = () -> {
                    CameraUtil.previewComplete = true;
                    grid2Layout.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(grid2Layout.getDrawingCache());
                    CameraUtil.imageUri = getImageUri(bitmap);

                    grid2Layout.setDrawingCacheEnabled(false);
                };
                handler.postDelayed(runnable, 1000);

            }

        });

    }


    public void grid2VideoConditionH() {

        getActivity().runOnUiThread(() -> {
            CameraUtil.twoGridValue++;
            if (CameraUtil.twoGridValue == 1) {
                setVideoThumbnail(twoGridImage1Horizontal, mNextVideoAbsolutePath);
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(mTextureView, "translationX", 0, (float) screenWidth / 2);
                anim1.setDuration(200);
                anim1.start();
                twoGridImage2Horizontal.setBackgroundResource(0);
                setAlpha(twoGridImage2Horizontal);
            } else if (CameraUtil.twoGridValue == 2) {
                setVideoThumbnail(twoGridImage2Horizontal, mNextVideoAbsolutePath);
                checkImage.setImageResource(R.drawable.ic_outline_check_24);

            }

        });

    }

    public static void setVideoThumbnail(ImageView imageView, String path) {
        Bitmap bmThumbnail;
        bmThumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
        bmThumbnail = CameraUtil.getResizedBitmap(bmThumbnail, mw, mh);
        imageView.setImageBitmap(bmThumbnail);
    }

    public void grid3Condition() {

        getActivity().runOnUiThread(() -> {
            CameraUtil.threeGridValue++;
            if (CameraUtil.threeGridValue == 1) {
                showImage(mNextPictureAbsolutePath, threeGridImage1);
                progressBar.setProgress(33);
                updateTextureViewSize(screenWidth, (int) (screenHeight / 3.7));
                threeGridImage2.setImageResource(0);
            } else if (CameraUtil.threeGridValue == 2) {
                showImage(mNextPictureAbsolutePath, threeGridImage2);
                setAlpha(threeGridImage2);
                progressBar.setProgress(66);
                updateTextureViewSize(screenWidth, (int) ((screenHeight) * 2 / 3.7));
                threeGridImage3.setImageResource(0);

            } else if (CameraUtil.threeGridValue == 3) {
                progressBar.setProgress(100);
                setAlpha(threeGridImage3);
                showImage(mNextPictureAbsolutePath, threeGridImage3);
                checkImage.setImageResource(R.drawable.ic_outline_check_24);

                Handler handler = new Handler();
                Runnable runnable = () -> {
                    CameraUtil.previewComplete = true;
                    grid3Layout.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(grid3Layout.getDrawingCache());
                    CameraUtil.imageUri = getImageUri(bitmap);
                    grid3Layout.setDrawingCacheEnabled(false);
                };
                handler.postDelayed(runnable, 1500);

            }

        });

    }

    public void grid4Condition() {

        getActivity().runOnUiThread(() -> {
            CameraUtil.fourGridValue++;
            if (CameraUtil.fourGridValue == 1) {
                showImage(mNextPictureAbsolutePath, fourGridImage1);
                progressBar.setProgress(25);
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(mTextureView, "translationX", 0, (float) screenWidth / 2);
                anim1.setDuration(200);
                anim1.start();
                fourGridImage2.setImageResource(0);
            } else if (CameraUtil.fourGridValue == 2) {
                showImage(mNextPictureAbsolutePath, fourGridImage2);
                setAlpha(fourGridImage2);
                progressBar.setProgress(50);
                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX", 0);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", 0, (float) (screenHeight / 2.5));
                ObjectAnimator anim2 = ObjectAnimator.ofPropertyValuesHolder(mTextureView, pvhX, pvhY);
                anim2.setDuration(200);
                anim2.start();
                fourGridImage3.setImageResource(0);

            } else if (CameraUtil.fourGridValue == 3) {
                showImage(mNextPictureAbsolutePath, fourGridImage3);
                setAlpha(fourGridImage3);
                progressBar.setProgress(75);
                ObjectAnimator anim3 = ObjectAnimator.ofFloat(mTextureView, "translationX", 0, (float) screenWidth / 2);
                anim3.setDuration(200);
                anim3.start();
                fourGridImage4.setImageResource(0);

            } else if (CameraUtil.fourGridValue == 4) {
                progressBar.setProgress(100);
                setAlpha(fourGridImage4);
                showImage(mNextPictureAbsolutePath, fourGridImage4);
                checkImage.setImageResource(R.drawable.ic_outline_check_24);

                Handler handler = new Handler();
                Runnable runnable = () -> {
                    CameraUtil.previewComplete = true;
                    grid4Layout.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(grid4Layout.getDrawingCache());
                    CameraUtil.imageUri = getImageUri(bitmap);
                    grid4Layout.setDrawingCacheEnabled(false);
                };
                handler.postDelayed(runnable, 1500);

            }

        });

    }

    public void grid6Condition() {

        getActivity().runOnUiThread(() -> {
            CameraUtil.sixGridValue++;
            if (CameraUtil.sixGridValue == 1) {
                showImage(mNextPictureAbsolutePath, sixGridImage1);
                progressBar.setProgress(17);
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(mTextureView, "translationX", 0, (float) screenWidth / 2);
                anim1.setDuration(200);
                anim1.start();
                sixGridImage2.setImageResource(0);
            } else if (CameraUtil.sixGridValue == 2) {
                showImage(mNextPictureAbsolutePath, sixGridImage2);
                setAlpha(sixGridImage2);
                progressBar.setProgress(34);
                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX", 0);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", 0, (float) (screenHeight / 3.7));
                ObjectAnimator anim2 = ObjectAnimator.ofPropertyValuesHolder(mTextureView, pvhX, pvhY);
                anim2.setDuration(200);
                anim2.start();
                sixGridImage3.setImageResource(0);

            } else if (CameraUtil.sixGridValue == 3) {
                showImage(mNextPictureAbsolutePath, sixGridImage3);
                setAlpha(sixGridImage3);
                progressBar.setProgress(50);
                ObjectAnimator anim3 = ObjectAnimator.ofFloat(mTextureView, "translationX", 0, (float) screenWidth / 2);
                anim3.setDuration(200);
                anim3.start();
                sixGridImage4.setImageResource(0);

            } else if (CameraUtil.sixGridValue == 4) {
                showImage(mNextPictureAbsolutePath, sixGridImage4);
                setAlpha(sixGridImage4);
                progressBar.setProgress(75);
                PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("translationX", 0);
                PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("translationY", (float) ((screenHeight) * ((float) 2 / 3.7)));
                ObjectAnimator anim4 = ObjectAnimator.ofPropertyValuesHolder(mTextureView, pvhX2, pvhY2);
                anim4.setDuration(200);
                anim4.start();
                sixGridImage5.setImageResource(0);

            } else if (CameraUtil.sixGridValue == 5) {
                showImage(mNextPictureAbsolutePath, sixGridImage5);
                setAlpha(sixGridImage5);
                progressBar.setProgress(84);
                ObjectAnimator anim3 = ObjectAnimator.ofFloat(mTextureView, "translationX", 0, (float) screenWidth / 2);
                anim3.setDuration(200);
                anim3.start();
                sixGridImage6.setImageResource(0);

            } else if (CameraUtil.sixGridValue == 6) {
                progressBar.setProgress(100);
                setAlpha(sixGridImage6);
                showImage(mNextPictureAbsolutePath, sixGridImage6);
                checkImage.setImageResource(R.drawable.ic_outline_check_24);

                Handler handler = new Handler();
                Runnable runnable = () -> {
                    CameraUtil.previewComplete = true;
                    grid6Layout.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(grid6Layout.getDrawingCache());
                    CameraUtil.imageUri = getImageUri(bitmap);
                    grid6Layout.setDrawingCacheEnabled(false);
                };
                handler.postDelayed(runnable, 1500);

            }

        });

    }

    public void grid2ConditionHorizontal() {

        getActivity().runOnUiThread(() -> {
            CameraUtil.twoGridValueHorizontal++;
            if (CameraUtil.twoGridValueHorizontal == 1) {
                showImage(mNextPictureAbsolutePath, twoGridImage1Horizontal);
                progressBar.setProgress(50);
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(mTextureView, "translationX", 0, (float) screenWidth / 2);
                anim1.setDuration(200);
                anim1.start();
                twoGridImage2Horizontal.setBackgroundResource(0);

            } else if (CameraUtil.twoGridValueHorizontal == 2) {
                progressBar.setProgress(100);
                setAlpha(twoGridImage2Horizontal);
                showImage(mNextPictureAbsolutePath, twoGridImage2Horizontal);
                checkImage.setImageResource(R.drawable.ic_outline_check_24);

                Handler handler = new Handler();
                Runnable runnable = () -> {
                    CameraUtil.previewComplete = true;
                    grid2LayoutHorizontal.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(grid2LayoutHorizontal.getDrawingCache());
                    CameraUtil.imageUri = getImageUri(bitmap);
                    grid2LayoutHorizontal.setDrawingCacheEnabled(false);
                };
                handler.postDelayed(runnable, 1500);

            }

        });

    }

    public void showImage(String imageUri, ImageView imageView) {
        getActivity().runOnUiThread(() -> Glide.with(getActivity())
                .load(imageUri)
                .into(imageView));
    }

    private void setAlpha(View v) {
        v.setAlpha(1);
    }

    public Uri getImageUri(Bitmap inImage) {

        File sdcard = Environment.getExternalStorageDirectory();
        if (sdcard != null) {
            File mediaDir = new File(sdcard, "DCIM/Camera");
            if (!mediaDir.exists()) {
                mediaDir.mkdirs();
            }
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    //Super Zoom Code:-

    Zoom zoom;
    public static float zoomValue = 1f;
    public static Float maxZoom;


    private void initSuperZoom() {
        animationView = getActivity().findViewById(R.id.animationView);
    }

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
        if (mCameraDevice == null) {
            return;
        }
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        zoom = new Zoom(mCameraCharacteristics);
        zoom.setZoom(mPreviewRequestBuilder, value);
        defaultZoom = value;

        mPreviewSession.setRepeatingRequest(mPreviewRequestBuilder.build(), null, mBackgroundHandler);
    }

    private void updateZoomPreview(float value) throws CameraAccessException {
        if (mCameraDevice == null) {
            return;
        }
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        zoom = new Zoom(mCameraCharacteristics);

        zoom.setZoom(mPreviewRequestBuilder, value);

        mPreviewSession.setRepeatingRequest(mPreviewRequestBuilder.build(), null, mBackgroundHandler);

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

    ImageView handsFreeRecord;

    private ImageView handsFreeImage1, handsFreeImage2, handsFreeImage3;

    final Handler handler = new Handler();
    int finalTime = 5000;
    long recordTimer = 0;
    boolean thumbsUp = false;
    int limit = 0;
    public ArrayList<String> handsFreeVideos = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    public void handsFreeRecordMethod() {
        handsFreeRecord.setOnTouchListener((v, event) -> {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    thumbsUp = false;
                    handsFree();
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    thumbsUp = true;
                    if (isRecordingVideo()) {
                        stopRecordingVideo();

                    }

                    return true;
            }

            return false;

        });
    }

    private void handsFree() {
        startRecordingVideo();
        recordTimer = 0;
        chronometer.setOnChronometerTickListener(chronometer -> {
            recordTimer = SystemClock.elapsedRealtime() - chronometer.getBase();
            if (recordTimer >= finalTime) {
                limit++;
                recordTimer = 0;
                if (isRecordingVideo()) {
                    stopRecordingVideo();
                }
                if (limit < 3) {
                    Runnable runnable = this::startRecordingVideo;
                    handler.postDelayed(runnable, 500);
                }
            }
        });
    }

    private void initHandsFreeImageView() {
        handsFreeRecord = getActivity().findViewById(R.id.handsFreeRecord);
        handsFreeImage1 = getActivity().findViewById(R.id.handsFreeImage1);
        handsFreeImage2 = getActivity().findViewById(R.id.handsFreeImage2);
        handsFreeImage3 = getActivity().findViewById(R.id.handsFreeImage3);
    }

    // Multi Snap Capture Code:-
    public ArrayList<String> multiSnapList = new ArrayList<>();
    ImageView multiCaptureNextButton;
    public ImageView multiCaptureImage1, multiCaptureImage2, multiCaptureImage3;
    private ProgressBar progressbarMC;
    private ImageView multiSnapCapture;

    public void multiSnapCaptureView() {
        if (multiSnapList != null) {
            multiSnapList.clear();
        }

        multiSnapCapture = getActivity().findViewById(R.id.multiSnapCapture);
        progressbarMC = getActivity().findViewById(R.id.progressbarMC);
        multiCaptureNextButton = getActivity().findViewById(R.id.multiCaptureNextButton);
        multiCaptureImage1 = getActivity().findViewById(R.id.multiCaptureImage1);
        multiCaptureImage2 = getActivity().findViewById(R.id.multiCaptureImage2);
        multiCaptureImage3 = getActivity().findViewById(R.id.multiCaptureImage3);

        multiCaptureNextButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PreviewImageActivity.class);
            intent.putStringArrayListExtra("multiSnaps", multiSnapList);

//            startActivity(intent);

            startActivityForResult(intent, ParameterConstants.PICKER);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ParameterConstants.PICKER) {
                Intent intent = new Intent();
                intent.putExtra("name", data.getStringExtra("name"));
                intent.putExtra("path", data.getStringExtra("path"));
                intent.putExtra("isVideo", data.getBooleanExtra("isVideo", false));
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }

        }else {
            getActivity().finish();
        }
    }


    //Video Progreebar Code.
    private int mProgressStatus = 0;

    public Handler mHandler = new Handler();

    public boolean handlerRunning = false;

    public int progressTimer = 600;
    ProgressBar mProgressBar;

    @SuppressLint("UseCompatLoadingForDrawables")
    public void startVideoProgress() {
        mProgressBar = getActivity().findViewById(R.id.progressbar);

        mProgressBar.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progressbar_bg));
        new Thread(() -> {
            while (mProgressStatus < 100) {
                mProgressStatus++;
                handlerRunning = true;
                SystemClock.sleep(progressTimer);
                Log.e(TAG, "startVideoProgress: ");
                mHandler.post(() -> mProgressBar.setProgress(mProgressStatus));
            }
            mHandler.post(() -> {
                handlerRunning = false;
                if (isRecordingVideo()) {
                    stopRecordingVideo();
                }
            });
        }).start();
    }



}

