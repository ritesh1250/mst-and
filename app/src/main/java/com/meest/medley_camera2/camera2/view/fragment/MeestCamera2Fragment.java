
package com.meest.medley_camera2.camera2.view.fragment;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
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
import android.graphics.RectF;
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
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.math.MathUtils;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.meest.Activities.preview.PreviewPhotoActivity;
import com.meest.R;
import com.meest.databinding.ActivityCamera2MetmeBinding;
import com.meest.databinding.DailogProgressBinding;
import com.meest.databinding.MeestFragmentCamera2Binding;
import com.meest.medley_camera2.camera2.cameraUtils.AspectRatio;
import com.meest.medley_camera2.camera2.cameraUtils.AutoFocusHelper;
import com.meest.medley_camera2.camera2.cameraUtils.ImagesGallery;
import com.meest.medley_camera2.camera2.utills.CameraConstants;
import com.meest.medley_camera2.camera2.utills.CameraUtil;
import com.meest.medley_camera2.camera2.view.activity.MeestCameraActivity;
import com.meest.medley_camera2.camera2.view.activity.PreviewImageActivity;
import com.meest.medley_camera2.camera2.view.activity.PreviewVideoActivity;
import com.meest.medley_camera2.camera2.viewmodels.MeestCameraFragmentViewModel;
import com.meest.meestbhart.utilss.Constant;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.TranscoderOptions;
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategy;
import com.otaliastudios.transcoder.strategy.size.AtMostResizer;
import com.otaliastudios.transcoder.strategy.size.FractionResizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.meest.medley_camera2.camera2.viewmodels.MeestCameraFragmentViewModel.STATE_PICTURE_TAKEN;


public class MeestCamera2Fragment extends Fragment {
    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String FRAGMENT_DIALOG = "dialog";
    public CameraCharacteristics mCameraCharacteristics;
    private String mCameraId;
    private CaptureRequest mPreviewRequest;
    private Size mVideoSize;
    private boolean mIsManualFocusing;
    private String mNextPictureAbsolutePath;
    public static int mw;
    public static int mh;
    public static int screenHeight;
    public static int screenWidth;
    public MeestFragmentCamera2Binding binding;
    public MeestCameraFragmentViewModel viewModel;

    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            if (binding.texture != null) {
                binding.texture.setSurfaceTextureListener(null);
            }
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };

    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            viewModel.mCameraOpenCloseLock.release();
            viewModel.mCameraDevice = cameraDevice;
            viewModel.createCameraPreviewSession();
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
                viewModel.showToast("Camera is error: " + error);
                activity.finish();
            }
        }

    };

    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            mNextPictureAbsolutePath = null;
            mNextPictureAbsolutePath = getPictureFilePath(getActivity());
            viewModel.mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), new File(mNextPictureAbsolutePath)));
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
                            activityBinding.progressbarMC.setProgress(33);
                            activityBinding.multiCaptureNextButton.setVisibility(View.VISIBLE);
                            showImage(mNextPictureAbsolutePath, activityBinding.multiCaptureImage1);
                        } else if (multiSnapList.size() == 2) {
                            activityBinding.progressbarMC.setProgress(66);
                            showImage(mNextPictureAbsolutePath, activityBinding.multiCaptureImage2);
                        } else if (multiSnapList.size() == 3) {

                            Intent intent = new Intent(getContext(), PreviewImageActivity.class);
                            intent.putStringArrayListExtra("multiSnaps", multiSnapList);
                            startActivityForResult(intent, 1001);
                            activityBinding.progressbarMC.setProgress(100);
                            showImage(mNextPictureAbsolutePath, activityBinding.multiCaptureImage3);
                            activityBinding.multiSnapCapture.setEnabled(false);
                            getActivity().finish();
                        }
                    }
                });

            } else {
                Intent intent = new Intent(getActivity(), PreviewPhotoActivity.class);

                if (getFacing() == CameraConstants.FACING_FRONT) {
                    Handler handler = new Handler();
                    dialog = new ProgressDialog(mContext);
                    Runnable runnable = () -> new AsyncTasks() {
                        @Override
                        public void onPreExecute() {
                            dialog.setMessage("Image is preparing..");
                            dialog.setCancelable(false);
                            dialog.show();
                        }

                        @Override
                        public void doInBackground() {
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), Uri.fromFile(new File(mNextPictureAbsolutePath)));
                                bitmap = flip(bitmap);
                                mNextPictureAbsolutePath = storeImage(bitmap).getAbsolutePath();
                            } catch (Exception e) {
                                Log.e(TAG, "Exception: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onPostExecute() {
                            // Ui task here
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            intent.putExtra("imagePath", mNextPictureAbsolutePath);
                            if (MeestCameraActivity.CALL_TYPE.equalsIgnoreCase("story")) {
                                intent.putExtra("isStory", true);
                            } else {
                                intent.putExtra("isStory", false);
                            }
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }.execute();
                    handler.postDelayed(runnable, 500);

                } else {
                    getActivity().runOnUiThread(() -> {
                        intent.putExtra("imagePath", mNextPictureAbsolutePath);
                        if (MeestCameraActivity.CALL_TYPE.equalsIgnoreCase("story")) {
                            intent.putExtra("isStory", true);
                        } else {
                            intent.putExtra("isStory", false);
                        }
                        startActivity(intent);
                        getActivity().finish();
                    });

                }


            }


        }

    };

    private ProgressDialog dialog;

    public abstract class AsyncTasks {
        private final ExecutorService executors;

        public AsyncTasks() {

            this.executors = Executors.newSingleThreadExecutor();
        }

        private void startBackground() {
            onPreExecute();
            executors.execute(new Runnable() {
                @Override
                public void run() {
                    doInBackground();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            onPostExecute();
                        }
                    });
                }
            });
        }

        public void execute() {
            startBackground();
        }

        public void shutdown() {
            executors.shutdown();
        }

        public boolean isShutdown() {
            return executors.isShutdown();
        }

        public abstract void onPreExecute();

        public abstract void doInBackground();

        public abstract void onPostExecute();
    }

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

    private Dialog mBuilder;

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

    Size choosePictureSize(Size[] choices) {
        List<Size> pictureSizes = Arrays.asList(choices);
        Collections.sort(pictureSizes, new CompareSizesByArea());
        int maxIndex = pictureSizes.size() - 1;
        for (int i = maxIndex; i >= 0; i--) {
            if (pictureSizes.get(i).getWidth() == pictureSizes.get(i).getHeight() *
                    viewModel.mAspectRatio.getX() / viewModel.mAspectRatio.getY()) {

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
            if (videoSizes.get(i).getWidth() <= MeestCameraFragmentViewModel.MAX_PREVIEW_WIDTH &&
                    videoSizes.get(i).getHeight() <= MeestCameraFragmentViewModel.MAX_PREVIEW_HEIGHT) {
                supportedVideoSizes.add(videoSizes.get(i));
                if (videoSizes.get(i).getWidth() == videoSizes.get(i).getHeight() *
                        viewModel.mAspectRatio.getX() / viewModel.mAspectRatio.getY()) {
                    return videoSizes.get(i);
                }
            }
        }
        return supportedVideoSizes.size() > 0 ? supportedVideoSizes.get(0) : choices[choices.length - 1];
    }

    Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                           int textureViewHeight, int maxWidth, int maxHeight) {
        viewModel.mPreviewSizes.clear();
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w;
        int h;
        w = viewModel.mAspectRatio.getX();
        h = viewModel.mAspectRatio.getY();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight) {
                //Log.e(TAG, "choices Size: "+option.getWidth()  +" "+ option.getHeight());
                viewModel.mPreviewSizes.add(new com.meest.medley_camera2.camera2.utills.Size(option.getWidth(), option.getHeight()));
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

        if (bigEnough.size() > 0) {

            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {

            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            viewModel.mAspectRatio = AspectRatio.of(4, 3);
            SortedSet<com.meest.medley_camera2.camera2.utills.Size> sortedSet = viewModel.mPreviewSizes.sizes(viewModel.mAspectRatio);
            if (sortedSet != null) {
                com.meest.medley_camera2.camera2.utills.Size lastSize = sortedSet.last();
                return new Size(lastSize.getWidth(), lastSize.getHeight());
            }
            viewModel.mAspectRatio = AspectRatio.of(choices[0].getWidth(), choices[0].getHeight());

            return choices[0];
        }
    }

    public static MeestCamera2Fragment newInstance() {

        return new MeestCamera2Fragment();
    }

    ActivityCamera2MetmeBinding activityBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.meest_fragment_camera2, container, false);
        activityBinding = ((MeestCameraActivity) mContext).binding;
        viewModel = new ViewModelProvider((ViewModelStoreOwner) getActivity(), new ViewModelFactory(new MeestCameraFragmentViewModel(mContext, getActivity(), binding, activityBinding, new MeestCamera2Fragment())).createFor()).get(MeestCameraFragmentViewModel.class);
        binding.setViewModel(viewModel);
        initProgressDialog();
        initGridLayoutViews();
        multiSnapCaptureView();
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        binding.texture.setGestureListener(new GestureDetector.SimpleOnGestureListener() {
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
        start();
    }

    @Override
    public void onPause() {
        stop();
        super.onPause();
    }

    public void start() {

        startBackgroundThread();
        if (binding.texture.isAvailable()) {


            openCamera(binding.texture.getWidth(), binding.texture.getHeight());
        } else {
            binding.texture.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    public void stop() {
        closeCamera();
        stopBackgroundThread();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }

    private void setupCameraOutputs(int width, int height) {
        Activity activity = getActivity();
        int internalFacing = MeestCameraFragmentViewModel.INTERNAL_FACINGS.get(viewModel.mFacing);
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
                viewModel.mSensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (viewModel.mSensorOrientation == 90 || viewModel.mSensorOrientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (viewModel.mSensorOrientation == 0 || viewModel.mSensorOrientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                    default:
                }

                Point displaySize = new Point();
                activity.getWindowManager().getDefaultDisplay().getRealSize(displaySize);


                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;

                mw = maxPreviewWidth;
                mh = maxPreviewHeight;

                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }

                if (maxPreviewWidth > MeestCameraFragmentViewModel.MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MeestCameraFragmentViewModel.MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MeestCameraFragmentViewModel.MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MeestCameraFragmentViewModel.MAX_PREVIEW_HEIGHT;
                }

                viewModel.mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                        maxPreviewHeight);


                mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));

                // For still image captures, we use the largest available size.
                Size largest = choosePictureSize(map.getOutputSizes(ImageFormat.JPEG));

                viewModel.mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                        ImageFormat.JPEG, /*maxImages*/2);
                viewModel.mImageReader.setOnImageAvailableListener(
                        mOnImageAvailableListener, viewModel.mBackgroundHandler);


                binding.texture.setAspectRatio(
                        viewModel.mPreviewSize.getHeight(), viewModel.mPreviewSize.getWidth());


                checkAutoFocusSupported();
                checkFlashSupported();

                viewModel.mCropRegion = AutoFocusHelper.cropRegionForZoom(mCameraCharacteristics,
                        CameraConstants.ZOOM_REGION_DEFAULT);
                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            ErrorDialog.newInstance(getString(R.string.camera_error))
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        }
    }

    private void checkAutoFocusSupported() {
        int[] modes = mCameraCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        viewModel.mAutoFocusSupported = !(modes == null || modes.length == 0 ||
                (modes.length == 1 && modes[0] == CameraCharacteristics.CONTROL_AF_MODE_OFF));
    }

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

        setupCameraOutputs(width, height);
        configureTransform(width, height);
        Activity activity = getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!viewModel.mCameraOpenCloseLock.tryAcquire(CameraConstants.OPEN_CAMERA_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            viewModel.mMediaRecorder = new MediaRecorder();
            manager.openCamera(mCameraId, mStateCallback, viewModel.mBackgroundHandler);
            getImagesFromGallery();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    public void getImagesFromGallery() {
        Handler handler = new Handler();
        Runnable runnable = () -> {

            try {
                viewModel.thumbnails = ImagesGallery.listOfImages(getActivity());
                getActivity().runOnUiThread(() -> Glide.with(this)
                        .load(viewModel.thumbnails.get(0))
                        .centerCrop()
                        .into(activityBinding.gallery));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        handler.postDelayed(runnable, 1000);

    }

    public void closeCamera() {
        try {
            viewModel.mCameraOpenCloseLock.acquire();
            if (null != viewModel.mPreviewSession) {
                viewModel.mPreviewSession.close();
                viewModel.mPreviewSession = null;
            }
            if (null != viewModel.mCameraDevice) {
                viewModel.mCameraDevice.close();
                viewModel.mCameraDevice = null;
            }
            if (null != viewModel.mMediaRecorder) {
                viewModel.mMediaRecorder.release();
                viewModel.mMediaRecorder = null;
            }
            if (null != viewModel.mImageReader) {
                viewModel.mImageReader.close();
                viewModel.mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            viewModel.mCameraOpenCloseLock.release();
        }
    }

    private void startBackgroundThread() {
        viewModel.mBackgroundThread = new HandlerThread("CameraBackground");
        viewModel.mBackgroundThread.start();
        viewModel.mBackgroundHandler = new Handler(viewModel.mBackgroundThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == MeestCameraFragmentViewModel.MSG_CAPTURE_PICTURE_WHEN_FOCUS_TIMEOUT) {
                    viewModel.mState = STATE_PICTURE_TAKEN;
                    viewModel.captureStillPicture();
                }

            }
        };
    }

    private void stopBackgroundThread() {


        try {
            viewModel.mBackgroundThread.quitSafely();
            viewModel.mBackgroundThread.join();
            viewModel.mBackgroundThread = null;
            viewModel.mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (null == binding.texture || null == viewModel.mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, viewModel.mPreviewSize.getHeight(), viewModel.mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / viewModel.mPreviewSize.getHeight(),
                    (float) viewWidth / viewModel.mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        binding.texture.setTransform(matrix);
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
                            viewModel.mCaptureCallback, viewModel.mBackgroundHandler);
                } catch (CameraAccessException e) {
                    viewModel.mFlash = saved; // Revert
                }
            }
        }
    }

    public int getFlash() {
        return viewModel.mFlash;
    }

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
                            viewModel.mCaptureCallback, viewModel.mBackgroundHandler);
                } catch (CameraAccessException e) {
                    viewModel.mAutoFocus = !viewModel.mAutoFocus; // Revert
                }
            }
        }
    }

    public boolean getAutoFocus() {
        return viewModel.mAutoFocus;
    }

    public boolean isAutoFocusSupported() {
        return viewModel.mAutoFocusSupported;
    }

    void updateManualFocus(float x, float y) {
        @SuppressWarnings("ConstantConditions")
        int sensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        viewModel.mAFRegions = AutoFocusHelper.afRegionsForNormalizedCoord(x, y, viewModel.mCropRegion, sensorOrientation);
        viewModel.mAERegions = AutoFocusHelper.aeRegionsForNormalizedCoord(x, y, viewModel.mCropRegion, sensorOrientation);
        viewModel.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, viewModel.mAFRegions);
        viewModel.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_REGIONS, viewModel.mAERegions);
        viewModel.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
    }

    void setManualFocusAt(int x, int y) {
        int mDisplayOrientation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        float[] points = new float[2];
        points[0] = (float) x / binding.texture.getWidth();
        points[1] = (float) y / binding.texture.getHeight();
        Matrix rotationMatrix = new Matrix();
        rotationMatrix.setRotate(mDisplayOrientation, 0.5f, 0.5f);
        rotationMatrix.mapPoints(points);
        if (viewModel.mPreviewRequestBuilder != null) {
            mIsManualFocusing = true;
            updateManualFocus(points[0], points[1]);
            if (viewModel.mPreviewSession != null) {
                try {
                    viewModel.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                            CaptureRequest.CONTROL_AF_TRIGGER_START);
                    viewModel.mPreviewSession.capture(viewModel.mPreviewRequestBuilder.build(), null, viewModel.mBackgroundHandler);
                    viewModel.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                            CaptureRequest.CONTROL_AF_TRIGGER_IDLE);
                    viewModel.mPreviewSession.setRepeatingRequest(viewModel.mPreviewRequestBuilder.build(),
                            null, viewModel.mBackgroundHandler);
                } catch (CameraAccessException | IllegalStateException e) {

                }
            }
            resumeAutoFocusAfterManualFocus();
        }
    }

    private final Runnable mAutoFocusRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewModel.mPreviewRequestBuilder != null) {
                mIsManualFocusing = false;
                viewModel.updateAutoFocus();
                if (viewModel.mPreviewSession != null) {
                    try {
                        viewModel.mPreviewSession.setRepeatingRequest(viewModel.mPreviewRequestBuilder.build(),
                                viewModel.mCaptureCallback, viewModel.mBackgroundHandler);
                    } catch (CameraAccessException e) {
                    }
                }
            }
        }
    };

    private void resumeAutoFocusAfterManualFocus() {
        viewModel.mBackgroundHandler.removeCallbacks(mAutoFocusRunnable);
        viewModel.mBackgroundHandler.postDelayed(mAutoFocusRunnable, CameraConstants.FOCUS_HOLD_MILLIS);
    }

    public void takePicture() {
        if (!mIsManualFocusing && viewModel.mAutoFocus && viewModel.mAutoFocusSupported) {
            capturePictureWhenFocusTimeout(); //Sometimes, camera do not focus in some devices.
            lockFocus();
        } else {
            viewModel.captureStillPicture();
        }
    }

    public void takeSinglePicture() {
        if (!mIsManualFocusing && viewModel.mAutoFocus && viewModel.mAutoFocusSupported) {
            capturePictureWhenFocusTimeout(); //Sometimes, camera do not focus in some devices.
            lockFocus();
        } else {
            viewModel.captureStillPicture();
        }
    }

    private void capturePictureWhenFocusTimeout() {
        if (viewModel.mBackgroundHandler != null) {
            viewModel.mBackgroundHandler.sendEmptyMessageDelayed(MeestCameraFragmentViewModel.MSG_CAPTURE_PICTURE_WHEN_FOCUS_TIMEOUT,
                    CameraConstants.AUTO_FOCUS_TIMEOUT_MS);
        }
    }

    private void lockFocus() {
        try {
            // This is how to tell the camera to lock focus.
            viewModel.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the lock.
            viewModel.mState = MeestCameraFragmentViewModel.STATE_WAITING_LOCK;
            viewModel.mPreviewSession.capture(viewModel.mPreviewRequestBuilder.build(), viewModel.mCaptureCallback, viewModel.mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private String getVideoFilePath(Context context) {
        final File dir = context.getExternalFilesDir(null);
        return (dir == null ? "" : (dir.getAbsolutePath() + "/"))
                + System.currentTimeMillis() + ".mp4";
    }

    private String getPictureFilePath(Context context) {

        final File dir = context.getExternalFilesDir(null);
        return (dir == null ? "" : (dir.getAbsolutePath() + "/"))
                + System.currentTimeMillis() + ".jpg";
    }

    private void setupMediaRecorder() throws IOException {
        final Activity activity = getActivity();
        if (null == activity) {
            return;
        }
        viewModel.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        viewModel.mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        viewModel.mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        viewModel.mNextVideoAbsolutePath = getVideoFilePath(getActivity());
        viewModel.mMediaRecorder.setOutputFile(viewModel.mNextVideoAbsolutePath);
        viewModel.mMediaRecorder.setVideoEncodingBitRate(10000000);
        viewModel.mMediaRecorder.setVideoFrameRate(30);
        viewModel.mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        viewModel.mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        viewModel.mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (viewModel.mSensorOrientation) {
            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                viewModel.mMediaRecorder.setOrientationHint(MeestCameraFragmentViewModel.DEFAULT_ORIENTATIONS.get(rotation));
                break;
            case SENSOR_ORIENTATION_INVERSE_DEGREES:
                viewModel.mMediaRecorder.setOrientationHint(MeestCameraFragmentViewModel.INVERSE_ORIENTATIONS.get(rotation));
                break;
        }
        viewModel.mMediaRecorder.prepare();
    }

    private CountDownTimer countDownTimer;

    public void startRecordingVideo() {

        if (null == viewModel.mCameraDevice || !binding.texture.isAvailable() || null == viewModel.mPreviewSize) {
            return;
        }
        try {


            viewModel.mIsRecordingVideo = true;
            setupMediaRecorder();
            SurfaceTexture texture = binding.texture.getSurfaceTexture();
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

                        viewModel.updateAutoFocus();

                        viewModel.updateFlash(viewModel.mPreviewRequestBuilder);

                        Integer stabilizationMode = viewModel.mPreviewRequestBuilder.
                                get(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE);
                        if (stabilizationMode != null &&
                                stabilizationMode == CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE_OFF) {
                            viewModel.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE,
                                    CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE_ON);
                        }

                        mPreviewRequest = viewModel.mPreviewRequestBuilder.build();
                        viewModel.mPreviewSession.setRepeatingRequest(mPreviewRequest, null, viewModel.mBackgroundHandler);

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
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    viewModel.showToast("Start recording video configure failed");
                }
            }, viewModel.mBackgroundHandler);

            activityBinding.chronometer.setVisibility(View.VISIBLE);
            activityBinding.chronometer.setBase(SystemClock.elapsedRealtime());
            activityBinding.chronometer.start(); // start a chronometer
            activityBinding.chronometer.setFormat("%s");

            countDownTimer = new CountDownTimer(CameraUtil.timer, 1000) {
                public void onTick(long millisUntilFinished) {
                    timerRunning = true;
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    if (isRecordingVideo()) {
                        stopRecordingVideo();
                    }
                }

            }.start();


        } catch (CameraAccessException | IOException e) {
            e.printStackTrace();
        }

    }


    public boolean timerRunning = false;

    public void stopRecordingVideo() {

        if (getActivity() != null) {

            countDownTimer.cancel();
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


                closeCamera();

                if (CameraUtil.handsFreeEnable) {
                    openCamera(binding.texture.getWidth(), binding.texture.getHeight());
                    handsFreeVideos.add(viewModel.mNextVideoAbsolutePath);
                    if (handsFreeVideos != null) {
                        if (thumbsUp) {
                            Intent intent = new Intent(getContext(), PreviewVideoActivity.class);
                            intent.putStringArrayListExtra("handsFreeVideos", handsFreeVideos);
                            startActivityForResult(intent, 1001);
                            getActivity().finish();
                        }
                        if (handsFreeVideos.size() == 1) {
                            CameraUtil.setVideoThumbnail(binding.handsFreeImage1, viewModel.mNextVideoAbsolutePath);

                        } else if (handsFreeVideos.size() == 2) {
                            CameraUtil.setVideoThumbnail(binding.handsFreeImage2, viewModel.mNextVideoAbsolutePath);

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
                    saveData("speedVideoPath", "", viewModel.mNextVideoAbsolutePath);
                } else if (CameraUtil.gridLayoutHVideoActive) {
                    openCamera(binding.texture.getWidth(), binding.texture.getHeight());
                    mergeVideo.add(viewModel.mNextVideoAbsolutePath);
                    grid2VideoConditionH();
                } else if (CameraUtil.music) {
                    saveData("videoWithSound", soundID, viewModel.mNextVideoAbsolutePath);
                } else {
                    saveData("Simple", "", viewModel.mNextVideoAbsolutePath);
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

            closeCamera();

            saveData("Boomerang", "", viewModel.mNextVideoAbsolutePath);
//            Intent intent = new Intent(getContext(), PreviewVideoActivity.class);
//            intent.putExtra("boomerang", viewModel.mNextVideoAbsolutePath);
//            startActivity(intent);
//            getActivity().finish();

        });
    }

    private static class ImageSaver implements Runnable {

        private final Image mImage;
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

    private static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

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
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(binding.texture, "translationY", 0, viewWidth, viewHeight);
        anim1.setDuration(200);
        anim1.start();
    }

    public ArrayList<String> mergeVideo = new ArrayList<>();

    public void gridLayoutTwo() {
        activityBinding.progressBar.setProgress(0);
        activityBinding.progressBar.setMax(100);
        CameraUtil.gridLayout2Active = true;
        binding.grid2Layout.setVisibility(View.VISIBLE);
    }

    public void gridLayoutThree() {
        CameraUtil.gridLayout3Active = true;
        binding.grid3Layout.setVisibility(View.VISIBLE);
    }

    public void gridLayoutFour() {
        CameraUtil.gridLayout4Active = true;
        binding.grid4Layout.setVisibility(View.VISIBLE);
    }

    public void gridLayoutSix() {
        CameraUtil.gridLayout6Active = true;
        binding.grid6Layout.setVisibility(View.VISIBLE);
    }

    public void gridLayoutTwoHorizontal() {
        CameraUtil.gridLayout2ActiveHorizontal = true;
        binding.grid2LayoutHorizontal.setVisibility(View.VISIBLE);
    }

    public void gridLayoutTwoVideoH() {
        CameraUtil.gridLayoutHVideoActive = true;
        binding.grid2LayoutHorizontal.setVisibility(View.VISIBLE);
    }

    public Uri filePath;

    //Here Managed the Conditions of Grid Layout
    public void grid2Condition() {

        getActivity().runOnUiThread(() -> {
            CameraUtil.twoGridValue++;
            if (CameraUtil.twoGridValue == 1) {
                showImage(mNextPictureAbsolutePath, binding.twoGridImage1);
                activityBinding.progressBar.setProgress(50);
                updateTextureViewSize(MeestCameraFragmentViewModel.screenWidth, (int) (MeestCameraFragmentViewModel.screenHeight / 2));
                binding.twoGridImage2.setImageResource(0);

            } else if (CameraUtil.twoGridValue == 2) {
                activityBinding.progressBar.setProgress(100);
                setAlpha(binding.twoGridImage2);
                showImage(mNextPictureAbsolutePath, binding.twoGridImage2);
                activityBinding.checkImage.setImageResource(R.drawable.ic_outline_check_24);

                Handler handler = new Handler();
                Runnable runnable = () -> {

                    binding.grid2Layout.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(binding.grid2Layout.getDrawingCache());

                    if (storeImage(bitmap) != null) {
                        filePath = Uri.parse(storeImage(bitmap).getAbsolutePath());
                    }
                    CameraUtil.previewComplete = true;
                    binding.grid2Layout.setDrawingCacheEnabled(false);

                };
                handler.postDelayed(runnable, 500);

            }

        });

    }

    private static final String TAG = "Test";

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
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + mContext.getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
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

    public void grid2VideoConditionH() {

        getActivity().runOnUiThread(() -> {
            CameraUtil.twoGridValue++;
            if (CameraUtil.twoGridValue == 1) {
                setVideoThumbnail(binding.twoGridImage1Horizontal, viewModel.mNextVideoAbsolutePath);
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(binding.texture, "translationX", 0, (float) MeestCameraFragmentViewModel.screenWidth / 2);
                anim1.setDuration(200);
                anim1.start();
                binding.twoGridImage2Horizontal.setBackgroundResource(0);
                setAlpha(binding.twoGridImage2Horizontal);
            } else if (CameraUtil.twoGridValue == 2) {
                setVideoThumbnail(binding.twoGridImage2Horizontal, viewModel.mNextVideoAbsolutePath);
                activityBinding.checkImage.setImageResource(R.drawable.ic_outline_check_24);

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
                showImage(mNextPictureAbsolutePath, binding.threeGridImage1);
                activityBinding.progressBar.setProgress(33);
                updateTextureViewSize(MeestCameraFragmentViewModel.screenWidth, (int) (MeestCameraFragmentViewModel.screenHeight / 3.3));
                binding.threeGridImage2.setImageResource(0);
            } else if (CameraUtil.threeGridValue == 2) {
                showImage(mNextPictureAbsolutePath, binding.threeGridImage2);
                setAlpha(binding.threeGridImage2);
                activityBinding.progressBar.setProgress(66);
                updateTextureViewSize(MeestCameraFragmentViewModel.screenWidth, (int) ((MeestCameraFragmentViewModel.screenHeight) / 3.4));
                binding.threeGridImage3.setImageResource(0);

            } else if (CameraUtil.threeGridValue == 3) {
                activityBinding.progressBar.setProgress(100);
                setAlpha(binding.threeGridImage3);
                showImage(mNextPictureAbsolutePath, binding.threeGridImage3);
                activityBinding.checkImage.setImageResource(R.drawable.ic_outline_check_24);

                Handler handler = new Handler();
                Runnable runnable = () -> {
                    binding.grid3Layout.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(binding.grid3Layout.getDrawingCache());
                    if (storeImage(bitmap) != null) {
                        filePath = Uri.parse(storeImage(bitmap).getAbsolutePath());
                    }
                    CameraUtil.previewComplete = true;
                    binding.grid3Layout.setDrawingCacheEnabled(false);
                };
                handler.postDelayed(runnable, 500);

            }

        });

    }

    public void grid4Condition() {

        getActivity().runOnUiThread(() -> {
            CameraUtil.fourGridValue++;
            if (CameraUtil.fourGridValue == 1) {
                showImage(mNextPictureAbsolutePath, binding.fourGridImage1);
                activityBinding.progressBar.setProgress(25);
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(binding.texture, "translationX", 0, (float) MeestCameraFragmentViewModel.screenWidth / 2);
                anim1.setDuration(200);
                anim1.start();
                binding.fourGridImage2.setImageResource(0);
            } else if (CameraUtil.fourGridValue == 2) {
                showImage(mNextPictureAbsolutePath, binding.fourGridImage2);
                setAlpha(binding.fourGridImage2);
                activityBinding.progressBar.setProgress(50);
                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX", 0);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", 0, (float) (MeestCameraFragmentViewModel.screenHeight / 2));
                ObjectAnimator anim2 = ObjectAnimator.ofPropertyValuesHolder(binding.texture, pvhX, pvhY);
                anim2.setDuration(200);
                anim2.start();
                binding.fourGridImage3.setImageResource(0);

            } else if (CameraUtil.fourGridValue == 3) {
                showImage(mNextPictureAbsolutePath, binding.fourGridImage3);
                setAlpha(binding.fourGridImage3);
                activityBinding.progressBar.setProgress(75);
                ObjectAnimator anim3 = ObjectAnimator.ofFloat(binding.texture, "translationX", 0, (float) MeestCameraFragmentViewModel.screenWidth / 2);
                anim3.setDuration(200);
                anim3.start();
                binding.fourGridImage4.setImageResource(0);

            } else if (CameraUtil.fourGridValue == 4) {
                activityBinding.progressBar.setProgress(100);
                setAlpha(binding.fourGridImage4);
                showImage(mNextPictureAbsolutePath, binding.fourGridImage4);
                activityBinding.checkImage.setImageResource(R.drawable.ic_outline_check_24);

                Handler handler = new Handler();
                Runnable runnable = () -> {

                    binding.grid4Layout.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(binding.grid4Layout.getDrawingCache());
                    if (storeImage(bitmap) != null) {
                        filePath = Uri.parse(storeImage(bitmap).getAbsolutePath());
                    }
                    CameraUtil.previewComplete = true;
                    binding.grid4Layout.setDrawingCacheEnabled(false);
                };
                handler.postDelayed(runnable, 500);

            }

        });

    }

    public void grid6Condition() {

        getActivity().runOnUiThread(() -> {
            CameraUtil.sixGridValue++;
            if (CameraUtil.sixGridValue == 1) {
                showImage(mNextPictureAbsolutePath, binding.sixGridImage1);
                activityBinding.progressBar.setProgress(17);
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(binding.texture, "translationX", 0, (float) MeestCameraFragmentViewModel.screenWidth / 2);
                anim1.setDuration(200);
                anim1.start();
                binding.sixGridImage2.setImageResource(0);
            } else if (CameraUtil.sixGridValue == 2) {
                showImage(mNextPictureAbsolutePath, binding.sixGridImage2);
                setAlpha(binding.sixGridImage2);
                activityBinding.progressBar.setProgress(34);
                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("translationX", 0);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("translationY", 0, (float) (MeestCameraFragmentViewModel.screenHeight / 3.7));
                ObjectAnimator anim2 = ObjectAnimator.ofPropertyValuesHolder(binding.texture, pvhX, pvhY);
                anim2.setDuration(200);
                anim2.start();
                binding.sixGridImage3.setImageResource(0);

            } else if (CameraUtil.sixGridValue == 3) {
                showImage(mNextPictureAbsolutePath, binding.sixGridImage3);
                setAlpha(binding.sixGridImage3);
                activityBinding.progressBar.setProgress(50);
                ObjectAnimator anim3 = ObjectAnimator.ofFloat(binding.texture, "translationX", 0, (float) MeestCameraFragmentViewModel.screenWidth / 2);
                anim3.setDuration(200);
                anim3.start();
                binding.sixGridImage4.setImageResource(0);

            } else if (CameraUtil.sixGridValue == 4) {
                showImage(mNextPictureAbsolutePath, binding.sixGridImage4);
                setAlpha(binding.sixGridImage4);
                activityBinding.progressBar.setProgress(75);
                PropertyValuesHolder pvhX2 = PropertyValuesHolder.ofFloat("translationX", 0);
                PropertyValuesHolder pvhY2 = PropertyValuesHolder.ofFloat("translationY", (float) ((MeestCameraFragmentViewModel.screenHeight) * ((float) 2 / 3.7)));
                ObjectAnimator anim4 = ObjectAnimator.ofPropertyValuesHolder(binding.texture, pvhX2, pvhY2);
                anim4.setDuration(200);
                anim4.start();
                binding.sixGridImage5.setImageResource(0);

            } else if (CameraUtil.sixGridValue == 5) {
                showImage(mNextPictureAbsolutePath, binding.sixGridImage5);
                setAlpha(binding.sixGridImage5);
                activityBinding.progressBar.setProgress(84);
                ObjectAnimator anim3 = ObjectAnimator.ofFloat(binding.texture, "translationX", 0, (float) MeestCameraFragmentViewModel.screenWidth / 2);
                anim3.setDuration(200);
                anim3.start();
                binding.sixGridImage6.setImageResource(0);

            } else if (CameraUtil.sixGridValue == 6) {
                activityBinding.progressBar.setProgress(100);
                setAlpha(binding.sixGridImage6);
                showImage(mNextPictureAbsolutePath, binding.sixGridImage6);
                activityBinding.checkImage.setImageResource(R.drawable.ic_outline_check_24);

                Handler handler = new Handler();
                Runnable runnable = () -> {

                    binding.grid6Layout.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(binding.grid6Layout.getDrawingCache());
                    if (storeImage(bitmap) != null) {
                        filePath = Uri.parse(storeImage(bitmap).getAbsolutePath());
                    }
                    CameraUtil.previewComplete = true;
                    binding.grid6Layout.setDrawingCacheEnabled(false);
                };
                handler.postDelayed(runnable, 500);

            }

        });

    }

    public void grid2ConditionHorizontal() {

        getActivity().runOnUiThread(() -> {
            CameraUtil.twoGridValueHorizontal++;
            if (CameraUtil.twoGridValueHorizontal == 1) {
                showImage(mNextPictureAbsolutePath, binding.twoGridImage1Horizontal);
                activityBinding.progressBar.setProgress(50);
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(binding.texture, "translationX", 0, (float) MeestCameraFragmentViewModel.screenWidth / 2);
                anim1.setDuration(200);
                anim1.start();
                binding.twoGridImage2Horizontal.setBackgroundResource(0);

            } else if (CameraUtil.twoGridValueHorizontal == 2) {
                activityBinding.progressBar.setProgress(100);
                setAlpha(binding.twoGridImage2Horizontal);
                showImage(mNextPictureAbsolutePath, binding.twoGridImage2Horizontal);
                activityBinding.checkImage.setImageResource(R.drawable.ic_outline_check_24);

                Handler handler = new Handler();
                Runnable runnable = () -> {

                    binding.grid2LayoutHorizontal.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(binding.grid2LayoutHorizontal.getDrawingCache());
                    if (storeImage(bitmap) != null) {
                        filePath = Uri.parse(storeImage(bitmap).getAbsolutePath());
                    }
                    CameraUtil.previewComplete = true;
                    binding.grid2LayoutHorizontal.setDrawingCacheEnabled(false);
                };
                handler.postDelayed(runnable, 500);

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
        String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), inImage, "Title" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    public void initGridLayoutViews() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        MeestCameraFragmentViewModel.screenHeight = displayMetrics.heightPixels;
        MeestCameraFragmentViewModel.screenWidth = displayMetrics.widthPixels;

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

        viewModel.mPreviewSession.setRepeatingRequest(viewModel.mPreviewRequestBuilder.build(), null, viewModel.mBackgroundHandler);
    }

    private void updateZoomPreview(float value) throws CameraAccessException {
        if (viewModel.mCameraDevice == null) {
            return;
        }
        viewModel.mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        zoom = new Zoom(mCameraCharacteristics);

        zoom.setZoom(viewModel.mPreviewRequestBuilder, value);
        try {
            viewModel.mPreviewSession.setRepeatingRequest(viewModel.mPreviewRequestBuilder.build(), null, viewModel.mBackgroundHandler);
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
            activityBinding.handsFreeParentLayout.setVisibility(View.GONE);
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
    public int progressTimer = 15000;
    public String soundID = "";

    // Multi Snap Capture Code:-
    public ArrayList<String> multiSnapList = new ArrayList<>();

    public void multiSnapCaptureView() {
        if (multiSnapList != null) {
            multiSnapList.clear();
        }
        activityBinding.multiCaptureNextButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PreviewImageActivity.class);
            intent.putStringArrayListExtra("multiSnaps", multiSnapList);
            startActivityForResult(intent, 1001);
            getActivity().finish();
        });
    }


    public void hideGridLayouts() {
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

    public void fullScreenTexture() {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(binding.texture, "translationY", (float) MeestCamera2Fragment.screenHeight / 2, 0);
        anim1.setDuration(200);
        anim1.start();

        ObjectAnimator anim2 = ObjectAnimator.ofFloat(binding.texture, "translationX", (float) MeestCamera2Fragment.screenWidth / 2, 0);
        anim2.setDuration(200);
        anim2.start();
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

                                        progressBinding.progressBar.publishProgress((float) progress / 2);
                                    }
                                }

                                public void onTranscodeCompleted(int successCode) {
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

                                }).

                                start();
                            }
                // }

        public void onTranscodeCanceled () {
        }

        public void onTranscodeFailed (@NonNull Throwable exception){
        }
    }).

    transcode();


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
        return filesDir;
    }

    private DailogProgressBinding progressBinding;

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


    public static Bitmap flip(Bitmap src) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();

        matrix.preScale(-1.0f, 1.0f);

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

}

