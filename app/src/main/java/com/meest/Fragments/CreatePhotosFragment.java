package com.meest.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Activities.VideoPost.ARGearAsset.ARGearApi;
import com.meest.Activities.VideoPost.ARGearAsset.ARGearDataModel;
import com.meest.Activities.VideoPost.ARGearAsset.ARGearWebService;
import com.meest.Activities.VideoPost.ARGearAsset.camera.ReferenceCamera;
import com.meest.Activities.VideoPost.ARGearAsset.camera.ReferenceCamera1;
import com.meest.Activities.VideoPost.ARGearAsset.camera.ReferenceCamera2;
import com.meest.Activities.VideoPost.ARGearAsset.rendering.CameraTexture;
import com.meest.Activities.VideoPost.ARGearAsset.rendering.ScreenRenderer;
import com.meest.Activities.VideoPost.ARGearItemsAdapter.ARGearFilterAdapter;
import com.meest.Activities.VideoPost.ARGearItemsAdapter.ARGearStickerAdapter;
import com.meest.Activities.VideoPost.HelperClasses.DownloadAsyncResponse;
import com.meest.Activities.VideoPost.HelperClasses.DownloadAsyncTask;
import com.meest.Activities.VideoPost.HelperClasses.FileDeleteAsyncTask;
import com.meest.Activities.VideoPost.HelperClasses.PreferenceUtil;
import com.meest.Activities.VideoPost.HelperClasses.VideoPermissionHelper;
import com.meest.Activities.preview.PreviewPhotoActivity;
import com.meest.R;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.Widget.CameraFilterAdapter;
import com.seerslab.argear.exceptions.InvalidContentsException;
import com.seerslab.argear.exceptions.NetworkException;
import com.seerslab.argear.exceptions.SignedUrlGenerationException;
import com.seerslab.argear.session.ARGAuth;
import com.seerslab.argear.session.ARGContents;
import com.seerslab.argear.session.ARGFrame;
import com.seerslab.argear.session.ARGMedia;
import com.seerslab.argear.session.ARGSession;
import com.seerslab.argear.session.config.ARGCameraConfig;
import com.seerslab.argear.session.config.ARGConfig;
import com.seerslab.argear.session.config.ARGInferenceConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreatePhotosFragment extends Fragment {

    private Activity activity;
    private ImageView cancelShoot, sticker_img, filter_img, flipCamera, ivRecord,
            flashCamera, doneCapture, iv_filter_back, iv_sticker_back;
    private RecyclerView rvFilterList, rvStickerList;
    private LinearLayout stickerLayout, filterLayout;
    private FrameLayout cameraView;
    private LottieAnimationView loading;

    // for ARGear
    GLView glView;
    ARGSession argsession;
    ReferenceCamera referenceCamera;
    private int mDeviceWidth = 0;
    private int mDeviceHeight = 0;
    private int mGLViewWidth = 0;
    private int mGLViewHeight = 0;
    private ARGFrame.Ratio mScreenRatio = ARGFrame.Ratio.RATIO_4_3;
    private ARGMedia.Ratio ratio = ARGMedia.Ratio.RATIO_4_3;
    private ARGMedia argMedia;
    private String parentFolderPath;
    private String mVideoFilePath;
    final int USE_CAMERA_API = 1;
    private ScreenRenderer screenRenderer;
    private CameraTexture cameraTexture;
    private boolean isShooting = false;
    ARGearDataModel arGearDataModel;
    boolean isFlashAvailable;
    CameraManager cameraManager;
    String cameraId, mediaFolderPath;
    boolean isFlashOn = false;
    List<ARGearDataModel.ARGearItem> filterList = new ArrayList<>();
    List<ARGearDataModel.ARGearItem> stickerList = new ArrayList<>();
    private ARGearDataModel.ARGearItem mCurrentStickeritem = null;
    private boolean mHasTrigger = false;
    private Toast mTriggerToast = null;
    private boolean isStory = false;

    public CreatePhotosFragment(Activity activity, boolean isStory) {
        this.activity = activity;
        this.isStory = isStory;

        if (isStory) {
            mScreenRatio = ARGFrame.Ratio.RATIO_FULL;
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_photos_post_fragment, viewGroup, false);

        findIds(view);

        initFlash();
        fetchARGearData();

        loading.setAnimation("loading.json");
        loading.playAnimation();
        loading.loop(true);

        parentFolderPath = activity.getFilesDir().getAbsolutePath();
        mediaFolderPath = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/Meest";
        File mediaFolder = new File(mediaFolderPath);
        if (!mediaFolder.exists()) {
            mediaFolder.mkdir();
        }

        Point realSize = new Point();
        Display display = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getRealSize(realSize);
        mDeviceWidth = realSize.x;
        mDeviceHeight = realSize.y;
        mGLViewWidth = realSize.x;
        mGLViewHeight = realSize.y;

        cancelShoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        ivRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShooting = true;
            }
        });

        filter_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterLayout.getVisibility() == View.VISIBLE) {
                    rvFilterList.setVisibility(View.GONE);
                    filterLayout.setVisibility(View.GONE);
                    return;
                }
                rvFilterList.setVisibility(View.VISIBLE);
                filterLayout.setVisibility(View.VISIBLE);

                rvStickerList.setVisibility(View.GONE);
                stickerLayout.setVisibility(View.GONE);
            }
        });

        iv_filter_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvFilterList.setVisibility(View.GONE);
                filterLayout.setVisibility(View.GONE);
            }
        });

        iv_sticker_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvStickerList.setVisibility(View.GONE);
                stickerLayout.setVisibility(View.GONE);
            }
        });


        sticker_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stickerLayout.getVisibility() == View.VISIBLE) {
                    stickerLayout.setVisibility(View.GONE);
                    rvStickerList.setVisibility(View.GONE);
                    return;
                }

                rvStickerList.setVisibility(View.VISIBLE);
                stickerLayout.setVisibility(View.VISIBLE);

                rvFilterList.setVisibility(View.GONE);
                filterLayout.setVisibility(View.GONE);
            }
        });

        flashCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFlashAvailable) {
                    Utilss.showToast(activity.getApplicationContext(), getString(R.string.Flash_is_not_available_in_your_device),R.color.msg_fail);
                    return;
                }

                // changing flash status
                try {
                    isFlashOn = !isFlashOn;
                    cameraManager.setTorchMode(cameraId, isFlashOn);
                } catch (CameraAccessException e) {
                    Utilss.showToast(activity.getApplicationContext(), getString(R.string.Some_error_occurred_while_changing_flash_status_please_try_again_later),
                            R.color.msg_fail);
                    e.printStackTrace();
                }
            }
        });

        flipCamera.setOnClickListener(v -> {
            if (referenceCamera == null) {
                Utilss.showToast(activity.getApplicationContext(), getString(R.string.Some_error_occurred_while_changing_flash_status_please_try_again_later),
                        R.color.msg_fail);
                return;
            }

            if (referenceCamera.isCameraFacingFront()) {
                if (referenceCamera.changeCameraFacing()) {
                    flashCamera.setVisibility(View.VISIBLE);
                } else {
                    Utilss.showToast(activity.getApplicationContext(), getString(R.string.Some_error_occurred_while_changing_flash_status_please_try_again_later),
                            R.color.msg_fail);
                }
            } else {
                if (referenceCamera.changeCameraFacing()) {
                    flashCamera.setVisibility(View.GONE);
                } else {
                    Utilss.showToast(activity.getApplicationContext(), getString(R.string.Some_error_occurred_while_changing_flash_status_please_try_again_later),
                            R.color.msg_fail);
                }
            }
        });

        return view;
    }

    private void initFlash() {
        isFlashAvailable = activity.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void fetchARGearData() {
        ARGearApi arGearApi = ARGearWebService.createContentsService();

        arGearApi.getContents(Constant.ARGEAR_API_KEY).enqueue(new Callback<ARGearDataModel>() {
            @Override
            public void onResponse(Call<ARGearDataModel> call, Response<ARGearDataModel> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    Log.v("harsh", "body == " + response.body());
                    arGearDataModel = response.body();

                    extractData();
                } else {
                    Utilss.showToast(activity.getApplicationContext(), getString(R.string.Some_error_occurred_while_changing_flash_status_please_try_again_later),
                            R.color.msg_fail);
                }
            }

            @Override
            public void onFailure(Call<ARGearDataModel> call, Throwable t) {
                Utilss.showToast(activity.getApplicationContext(), getString(R.string.Some_error_occurred_while_changing_flash_status_please_try_again_later),
                        R.color.msg_fail);
            }
        });
    }

    // extracting different types of data from ARGear
    private void extractData() {
        // clearing old items
        filterList.clear();
        stickerList.clear();

        for (ARGearDataModel.ARGearCategory arGearCategory : arGearDataModel.getCategories()) {
            for (ARGearDataModel.ARGearItem arGearItem : arGearCategory.getARGearItems()) {
                if (arGearItem.getType().equalsIgnoreCase("filter")) {
                    filterList.add(arGearItem);
                } else {
                    stickerList.add(arGearItem);
                }
            }
        }
        // binding data
        bindData();
    }

    private void bindData() {
        // binding filter data
        ARGearFilterAdapter filterAdapter = new ARGearFilterAdapter(activity.getApplicationContext(), filterList);
        rvFilterList.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvFilterList.setAdapter(filterAdapter);
        filterAdapter.setOnItemClickListener(new CameraFilterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                setFilter(filterList.get(position));
//                viewCapture.setVisibility(View.VISIBLE);
            }
        });

        // binding sticker data
        ARGearStickerAdapter stickerAdapter = new ARGearStickerAdapter(activity.getApplicationContext(), stickerList);
        rvStickerList.setLayoutManager(new LinearLayoutManager(activity.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvStickerList.setAdapter(stickerAdapter);
        stickerAdapter.setOnItemClickListener(new CameraFilterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                setSticker(stickerList.get(position));
//                viewCapture.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setFilter(ARGearDataModel.ARGearItem item) {
        String filePath = parentFolderPath + "/" + item.getUuid();
        loading.setVisibility(View.VISIBLE);
        if (getLastUpdateAt(activity.getApplicationContext()) > getFilterUpdateAt(activity.getApplicationContext(), item.getUuid())) {
            new FileDeleteAsyncTask(new File(filePath), new FileDeleteAsyncTask.OnAsyncFileDeleteListener() {
                @Override
                public void processFinish(Object result) {
                    setFilterUpdateAt(activity.getApplicationContext(), item.getUuid(), getLastUpdateAt(activity.getApplicationContext()));
                    requestSignedUrl(item, filePath, false);
                }
            }).execute();
        } else {
            if (new File(filePath).exists()) {
                setItem(ARGContents.Type.FilterItem, filePath, item);
            } else {
                requestSignedUrl(item, filePath, false);
            }
        }
    }

    // region - network
    private void requestSignedUrl(ARGearDataModel.ARGearItem item, String path, final boolean isArItem) {
        loading.setVisibility(View.VISIBLE);
        argsession.auth().requestSignedUrl(item.getZipFile(), item.getTitle(), item.getType(), new ARGAuth.Callback() {
            @Override
            public void onSuccess(String url) {
                requestDownload(path, url, item, isArItem);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SignedUrlGenerationException) {
                    Log.e("harsh", "SignedUrlGenerationException !! ");
                } else if (e instanceof NetworkException) {
                    Log.e("harsh", "NetworkException !!");
                }

                loading.setVisibility(View.GONE);
            }
        });
    }

    public void setSticker(ARGearDataModel.ARGearItem item) {
        String filePath = parentFolderPath + "/" + item.getUuid();
        loading.setVisibility(View.VISIBLE);

        if (getLastUpdateAt(activity.getApplicationContext()) > getStickerUpdateAt(activity.getApplicationContext(), item.getUuid())) {
            new FileDeleteAsyncTask(new File(filePath), new FileDeleteAsyncTask.OnAsyncFileDeleteListener() {
                @Override
                public void processFinish(Object result) {
                    Log.d("harsh", "file delete success!");

                    setStickerUpdateAt(activity.getApplicationContext(), item.getUuid(), getLastUpdateAt(activity.getApplicationContext()));
                    requestSignedUrl(item, filePath, true);
                }
            }).execute();
        } else {
            if (new File(filePath).exists()) {
                setItem(ARGContents.Type.ARGItem, filePath, item);
            } else {
                requestSignedUrl(item, filePath, true);
            }
        }
    }

    private void requestDownload(String targetPath, String url, ARGearDataModel.ARGearItem item,
                                 boolean isSticker) {
        new DownloadAsyncTask(targetPath, url, new DownloadAsyncResponse() {
            @Override
            public void processFinish(boolean result) {
                // hiding progress dialog
                loading.setVisibility(View.GONE);
                if (result) {
                    if (isSticker) {
                        setItem(ARGContents.Type.ARGItem, targetPath, item);
                    } else {
                        setItem(ARGContents.Type.FilterItem, targetPath, item);
                    }
                } else {
                    Utilss.showToast(activity.getApplicationContext(), getString(R.string.Some_error_occurred_while_changing_flash_status_please_try_again_later),
                            R.color.msg_fail);
                }
            }
        }).execute();
    }

    public void setItem(ARGContents.Type type, String path, ARGearDataModel.ARGearItem itemModel) {

        mCurrentStickeritem = null;
        mHasTrigger = false;

        argsession.contents().setItem(type, path, itemModel.getUuid(), new ARGContents.Callback() {
            @Override
            public void onSuccess() {
                loading.setVisibility(View.GONE);

                if (type == ARGContents.Type.ARGItem) {
                    mCurrentStickeritem = itemModel;
                    mHasTrigger = itemModel.getHasTrigger();

                    Utilss.showToast(activity.getApplicationContext(), getString(R.string.Sticker_applied),
                            R.color.green);
                } else {
                    Utilss.showToast(activity.getApplicationContext(), getString(R.string.Filter_applied),
                            R.color.green);
                }
            }

            @Override
            public void onError(Throwable e) {
                loading.setVisibility(View.GONE);

                mCurrentStickeritem = null;
                mHasTrigger = false;
                if (e instanceof InvalidContentsException) {
                    Log.e("harsh", "InvalidContentsException");
                }
                Utilss.showToast(activity.getApplicationContext(), getString(R.string.Some_error_occurred_while_changing_flash_status_please_try_again_later),R.color.msg_fail);
            }
        });
    }

    private void setupARGear() {
        if (argsession == null) {

            if (!VideoPermissionHelper.hasPermission(activity)) {
                if (VideoPermissionHelper.shouldShowRequestPermissionRationale(activity)) {
                    Toast.makeText(activity.getApplicationContext(), getString(R.string.Permissions_required_for_creating_video), Toast.LENGTH_SHORT).show();
                    return;
                }
                VideoPermissionHelper.requestPermission(activity);
                return;
            }

            ARGConfig config
                    = new ARGConfig(Constant.ARGEAR_API_URL, Constant.ARGEAR_API_KEY, Constant.ARGEAR_SECRET_KEY, Constant.ARGEAR_AUTH_KEY);
            Set<ARGInferenceConfig.Feature> inferenceConfig
                    = EnumSet.of(ARGInferenceConfig.Feature.FACE_HIGH_TRACKING);

            argsession = new ARGSession(activity.getApplicationContext(), config, inferenceConfig);
            argMedia = new ARGMedia(argsession);

            screenRenderer = new ScreenRenderer();
            cameraTexture = new CameraTexture();

            initGLView();
            initCamera();
        }
        referenceCamera.startCamera();
        setGLViewSize(referenceCamera.getPreviewSize());

        if (referenceCamera.isCameraFacingFront()) {
            flashCamera.setVisibility(View.GONE);
        } else {
            flashCamera.setVisibility(View.VISIBLE);
        }

        argsession.resume();
    }

    private void initGLView() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        glView = new GLView(activity.getApplicationContext(), glViewListener);
        glView.setZOrderMediaOverlay(true);

        cameraView.addView(glView, params);
    }

    private void initCamera() {
        if (USE_CAMERA_API == 1) {
            referenceCamera = new ReferenceCamera1(activity.getApplicationContext(), cameraListener);
        } else {
            referenceCamera = new ReferenceCamera2(activity.getApplicationContext(), cameraListener, activity.getWindowManager().getDefaultDisplay().getRotation());
        }
    }

    private void setGLViewSize(int[] cameraPreviewSize) {
        int previewWidth = cameraPreviewSize[1];
        int previewHeight = cameraPreviewSize[0];

        if (mScreenRatio == ARGFrame.Ratio.RATIO_FULL) {
            mGLViewHeight = mDeviceHeight;
            mGLViewWidth = (int) ((float) mDeviceHeight * previewWidth / previewHeight);
        } else {
            mGLViewWidth = mDeviceWidth;
            mGLViewHeight = (int) ((float) mDeviceWidth * previewHeight / previewWidth);
        }

        if (glView != null
                && (mGLViewWidth != glView.getViewWidth() || mGLViewHeight != glView.getHeight())) {
            cameraView.removeView(glView);
            glView.getHolder().setFixedSize(mGLViewWidth, mGLViewHeight);
            cameraView.addView(glView);
        }
    }

    public int getGLViewWidth() {
        return mGLViewWidth;
    }

    public int getGLViewHeight() {
        return mGLViewHeight;
    }

    public void setMeasureSurfaceView(View view) {
        if (view.getParent() instanceof FrameLayout) {
            view.setLayoutParams(new FrameLayout.LayoutParams(mGLViewWidth, mGLViewHeight));
        } else if (view.getParent() instanceof RelativeLayout) {
            view.setLayoutParams(new RelativeLayout.LayoutParams(mGLViewWidth, mGLViewHeight));
        }

        /* to align center */
        if ((mScreenRatio == ARGFrame.Ratio.RATIO_FULL) && (mGLViewWidth > mDeviceWidth)) {
            view.setX((mDeviceWidth - mGLViewWidth) / 2);
        } else {
            view.setX(0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setupARGear();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (argsession != null) {
            referenceCamera.stopCamera();
            argsession.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (argsession != null) {
            referenceCamera.destroy();
            argsession.destroy();
        }
    }

    private void takePictureOnGlThread(int textureId) {
        isShooting = false;

        if (mScreenRatio == ARGFrame.Ratio.RATIO_FULL) {
            ratio = ARGMedia.Ratio.RATIO_16_9;
        } else if (mScreenRatio == ARGFrame.Ratio.RATIO_4_3) {
            ratio = ARGMedia.Ratio.RATIO_4_3;
        } else {
            ratio = ARGMedia.Ratio.RATIO_1_1;
        }

        String path = mediaFolderPath + "/" + "Meest_" + System.currentTimeMillis() + ".jpg";

        argMedia.takePicture(textureId, path, ratio);
//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getContext(), PreviewPhotoActivity.class);
                intent.putExtra("imagePath", path);
                intent.putExtra("isStory", isStory);
                startActivity(intent);
            }
        });
    }

    public void updateTriggerStatus(final int triggerstatus) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCurrentStickeritem != null && mHasTrigger) {
                    String strTrigger = null;
                    if ((triggerstatus & 1) != 0) {
                        strTrigger = "Open your mouth.";
                    } else if ((triggerstatus & 2) != 0) {
                        strTrigger = "Move your head side to side.";
                    } else if ((triggerstatus & 8) != 0) {
                        strTrigger = "Blink your eyes.";
                    } else {
                        if (mTriggerToast != null) {
                            mTriggerToast.cancel();
                            mTriggerToast = null;
                        }
                    }

                    if (strTrigger != null) {
                        mTriggerToast = Toast.makeText(activity.getApplicationContext(), strTrigger, Toast.LENGTH_SHORT);
                        mTriggerToast.setGravity(Gravity.CENTER, 0, 0);
                        mTriggerToast.show();
                        mHasTrigger = false;
                    }
                }
            }
        });
    }

    private void setLastUpdateAt(Context context, long updateAt) {
        PreferenceUtil.putLongValue(context, Constant.USER_PREF_NAME, "ContentLastUpdateAt", updateAt);
    }

    private long getLastUpdateAt(Context context) {
        return PreferenceUtil.getLongValue(context, Constant.USER_PREF_NAME, "ContentLastUpdateAt");
    }

    private void setFilterUpdateAt(Context context, String itemId, long updateAt) {
        PreferenceUtil.putLongValue(context, Constant.USER_PREF_NAME_FILTER, itemId, updateAt);
    }

    private long getFilterUpdateAt(Context context, String itemId) {
        return PreferenceUtil.getLongValue(context, Constant.USER_PREF_NAME_FILTER, itemId);
    }

    private void setStickerUpdateAt(Context context, String itemId, long updateAt) {
        PreferenceUtil.putLongValue(context, Constant.USER_PREF_NAME_STICKER, itemId, updateAt);
    }

    private long getStickerUpdateAt(Context context, String itemId) {
        return PreferenceUtil.getLongValue(context, Constant.USER_PREF_NAME_STICKER, itemId);
    }

    GLViewListener glViewListener = new GLViewListener() {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            screenRenderer.create(gl, config);
            cameraTexture.createCameraTexture();
        }

        @Override
        public void onDrawFrame(GL10 gl, int width, int height) {
            if (cameraTexture.getSurfaceTexture() == null) {
                return;
            }

            if (referenceCamera != null) {
                referenceCamera.setCameraTexture(cameraTexture.getTextureId(), cameraTexture.getSurfaceTexture());
            }

            ARGFrame frame = argsession.drawFrame(gl, mScreenRatio, width, height);
            screenRenderer.draw(frame, width, height);

            if (mHasTrigger) updateTriggerStatus(frame.getItemTriggerFlag());

            if (argMedia != null) {
                if (argMedia.isRecording()) argMedia.updateFrame(frame.getTextureId());
                if (isShooting) takePictureOnGlThread(frame.getTextureId());
            }
        }
    };


    ReferenceCamera.CameraListener cameraListener = new ReferenceCamera.CameraListener() {
        @Override
        public void setConfig(int previewWidth, int previewHeight, float verticalFov, float horizontalFov, int orientation, boolean isFrontFacing, float fps) {
            argsession.setCameraConfig(new ARGCameraConfig(previewWidth,
                    previewHeight,
                    verticalFov,
                    horizontalFov,
                    orientation,
                    isFrontFacing,
                    fps));
        }

        // region - for camera api 1
        @Override
        public void updateFaceRects(Camera.Face[] faces) {
            argsession.updateFaceRects(faces);
        }

        @Override
        public void feedRawData(byte[] data) {
            argsession.feedRawData(data);
        }
        // endregion

        // region - for camera api 2
        @Override
        public void updateFaceRects(int numFaces, int[][] bbox) {
            argsession.updateFaceRects(numFaces, bbox);
        }

        @Override
        public void feedRawData(Image data) {
            argsession.feedRawData(data);
        }
        // endregion
    };

    private void findIds(View view) {
        cancelShoot = view.findViewById(R.id.ivCancelVideo);
        sticker_img = view.findViewById(R.id.sticker_img);
        filter_img = view.findViewById(R.id.filter_img);
        flipCamera = view.findViewById(R.id.flipCamera);
        ivRecord = view.findViewById(R.id.ivRecord);
        flashCamera = view.findViewById(R.id.flashCamera);
        doneCapture = view.findViewById(R.id.doneCapture);
        stickerLayout = view.findViewById(R.id.stickerLayout);
        filterLayout = view.findViewById(R.id.filterLayout);
        iv_filter_back = view.findViewById(R.id.iv_filter_back);
        iv_sticker_back = view.findViewById(R.id.iv_sticker_back);
        rvFilterList = view.findViewById(R.id.rvFilterList);
        rvStickerList = view.findViewById(R.id.rvStickerList);
        cameraView = view.findViewById(R.id.cameraView);
        loading = view.findViewById(R.id.loading);

//        view_1 = view.findViewById(R.id.view_1);
//        view_2 = view.findViewById(R.id.view_2);
//        view_3 = view.findViewById(R.id.view_3);
//        layout_ratio = view.findViewById(R.id.layout_ratio);
//        layout_background = view.findViewById(R.id.layout_background);
//        layout_zoom = view.findViewById(R.id.layout_zoom);
//        layout_ratio_data = view.findViewById(R.id.layout_ratio_data);
//        layout_zoom_data = view.findViewById(R.id.layout_zoom_data);
//        layout_background_data = view.findViewById(R.id.layout_background_data);
//        cameraView = view.findViewById(R.id.cameraView);
//        ivRecord = view.findViewById(R.id.ivRecord);
//        ivVideoDone = view.findViewById(R.id.ivVideoDone);
//        ivCancelVideo = view.findViewById(R.id.ivCancelVideo);
    }

    public class GLView extends GLSurfaceView implements GLSurfaceView.Renderer {

        private int viewWidth;
        private int viewHeight;
        private GLViewListener listener;

        public GLView(@NonNull Context context, GLViewListener listener) {
            super(context);

            this.listener = listener;

            setEGLContextClientVersion(2);
            setEGLConfigChooser(8, 8, 8, 8, 16, 8);
            getHolder().setFormat(PixelFormat.RGBA_8888);

            setRenderer(this);
            setZOrderOnTop(true);

            setRenderMode(RENDERMODE_CONTINUOUSLY);
            setPreserveEGLContextOnPause(false);
        }

        public int getViewWidth() {
            return viewWidth;
        }

        public int getViewHeight() {
            return viewHeight;
        }


        // region - GLSurfaceView
        @Override
        public void onResume() {
            super.onResume();
            setRenderMode(RENDERMODE_CONTINUOUSLY);
        }

        @Override
        public void onPause() {
            super.onPause();
            setRenderMode(RENDERMODE_CONTINUOUSLY);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            int glviewWidth = getGLViewWidth();
            int glviewHeight = getGLViewHeight();


            if (glviewWidth > 0 && glviewHeight > 0) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                setMeasuredDimension(glviewWidth, glviewHeight);
                setMeasureSurfaceView(this);
            } else {
                setMeasuredDimension(width, height);
            }
        }
        // endregion


        // region - Renderer
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            listener.onSurfaceCreated(gl, config);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            viewWidth = width;
            viewHeight = height;
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            listener.onDrawFrame(gl, viewWidth, viewHeight);
        }
        // endregion
    }

    public interface GLViewListener {
        void onSurfaceCreated(GL10 gl, EGLConfig config);

        void onDrawFrame(GL10 gl, int width, int height);
    }
}
