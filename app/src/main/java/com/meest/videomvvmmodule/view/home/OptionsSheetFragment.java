package com.meest.videomvvmmodule.view.home;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.DOWNLOAD_SERVICE;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.meest.R;
import com.meest.databinding.FragmentOptionSheetBinding;
import com.meest.videoEditing.UtilCommand;
import com.meest.videomvvmmodule.model.user.RestResponse;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.only_camera2.Camera2DuetActivity;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.recordvideo.Utilss;
import com.meest.videomvvmmodule.view.video.PlayerActivity;
import com.meest.videomvvmmodule.viewmodel.OptionViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OptionsSheetFragment extends BottomSheetDialogFragment {
    FragmentOptionSheetBinding binding;
    OptionViewModel viewModel;
    public ImageView ivPause;
    public SimpleExoPlayer player;
    public Video.Data model;
    private static final int Permission_CODE = 1000;
    private static final int Permission_CODE_duet = 1001;
    DownloadManager downloadManager;
    private ProgressDialog pDialog;
    private File directoryName, fileName;
    Context context;
    private String y;
    private CustomDialogBuilder customDialogBuilder, customDialogBuilderForPermission;
    CompositeDisposable disposable = new CompositeDisposable();
    Boolean isFromForUFragment;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    public OptionsSheetFragment(ImageView ivPause, SimpleExoPlayer player, Video.Data model, Context context, Boolean isFromForUFragment) {
        this.ivPause = ivPause;
        this.player = player;
        this.model = model;
        this.context = context;
        this.isFromForUFragment = isFromForUFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog bottomSheetDialog = getActivity() != null ? new BottomSheetDialog(getActivity(), getTheme()) {

            @Override
            public void onBackPressed() {
                if (getChildFragmentManager().getBackStackEntryCount() > 0) {
                    getChildFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
            }
        } : (BottomSheetDialog) super.onCreateDialog(savedInstanceState);


//        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog dialog = (BottomSheetDialog) dialog1;
            dialog.setCanceledOnTouchOutside(true);

        });
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        binding.llDuet.performClick();
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // features requires a permission that the user has denied. At the
                        // same time, respect the user's decision. Don't link to system
                        // settings in an effort to convince the user to change their
                        // decision.
                    }
                });
        return bottomSheetDialog;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_option_sheet, container, false);
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new OptionViewModel()).createFor()).get(OptionViewModel.class);
        initView();
        initListeners();
//        initObserve();
        binding.setViewmodel(viewModel);

    }

    private void initView() {
//        if (android.os.Build.VERSION.SDK_INT < 30) {
//            binding.llDuet.setVisibility(View.VISIBLE);
//        }
        customDialogBuilder = new CustomDialogBuilder(getContext());
        customDialogBuilderForPermission = new CustomDialogBuilder(getContext());
        binding.llDuet.setVisibility(View.VISIBLE);


        if (getArguments() != null) {
            viewModel.reportType = getArguments().getInt("reporttype", 0);
            viewModel.postId = getArguments().getString("postid");
            viewModel.userId = getArguments().getString("userid");
            viewModel.videoPath = getArguments().getString("videoPath");
            viewModel.audioPath = getArguments().getString("audioPath");
            viewModel.soundId = getArguments().getString("soundId");
            viewModel.videoThumbnail = getArguments().getString("videoThumbnail");
//            if (getArguments().getInt("canSave") == 1) {
//                binding.llDownload.setVisibility(View.VISIBLE);
//            } else {
//                binding.llDownload.setVisibility(View.GONE);
//            }
//            if (getArguments().getInt("canDuet") == 1) {
//                binding.llDuet.setVisibility(View.VISIBLE);
//            } else {
//                binding.llDuet.setVisibility(View.GONE);
//            }
            if (!isFromForUFragment && viewModel.userId.equals(Global.userId))
                binding.llDelete.setVisibility(View.VISIBLE);
            if (viewModel.audioPath != null) {
                binding.llDuet.setVisibility(View.VISIBLE);
            } else {
                binding.llDuet.setVisibility(View.GONE);
            }
        }


    }

    private void initListeners() {
        binding.llDuet.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                    customDialogBuilderForPermission.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_permission, null), getContext().getResources().getString(R.string.to_capture_photos_and_videos),
                            getContext().getResources().getString(R.string.not_now), getContext().getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                                @Override
                                public void onPositiveDismiss() {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }

                                @Override
                                public void onNegativeDismiss() {

                                }
                            });
                } else {
                    customDialogBuilderForPermission.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_permission, null), getContext().getResources().getString(R.string.to_capture_photos_and_videos),
                            getContext().getResources().getString(R.string.not_now), getContext().getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                                @Override
                                public void onPositiveDismiss() {
                                    requestPermissionLauncher.launch(
                                            Manifest.permission.CAMERA);
                                }

                                @Override
                                public void onNegativeDismiss() {

                                }
                            });
                }
            } else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                    customDialogBuilderForPermission.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_microphone_permission, null), getContext().getResources().getString(R.string.to_capture_microphone),
                            getContext().getResources().getString(R.string.not_now), getContext().getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                                @Override
                                public void onPositiveDismiss() {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }

                                @Override
                                public void onNegativeDismiss() {

                                }
                            });
                } else {
                    customDialogBuilderForPermission.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_microphone_permission, null), getContext().getResources().getString(R.string.to_capture_microphone),
                            getContext().getResources().getString(R.string.not_now), getContext().getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                                @Override
                                public void onPositiveDismiss() {
                                    requestPermissionLauncher.launch(
                                            Manifest.permission.RECORD_AUDIO);
                                }

                                @Override
                                public void onNegativeDismiss() {

                                }
                            });
                }
            } else {
                assert getArguments() != null;
//            Log.e("TAG", "onClick: "+getArguments().getInt("canDuet") );
//            Log.e("TAG", "onClick:save "+getArguments().getInt("canSave") );
                if (getArguments().getInt("canDuet") == 1) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
////                        getActivity().requestPermissions(permissions, Permission_CODE);
//                            ActivityCompat.requestPermissions(getActivity(), new String[]{
//                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, Permission_CODE);
//                        } else {
//                            new DownloadFileFromURL().execute(viewModel.videoPath);
//                        }
//                    } else {
//                        new DownloadFileFromURL().execute(viewModel.videoPath);
//                    }
//                customDialogBuilder.hideLoadingDialog();
//                Log.e("======video===",viewModel.videoPath);
                    Intent intent = new Intent(context, Camera2DuetActivity.class);
                    intent.putExtra("videoPath", viewModel.videoPath);
                    intent.putExtra("music_url", viewModel.audioPath);
                    intent.putExtra("soundId", viewModel.soundId);
                    intent.putExtra("videoThumbnail", viewModel.videoThumbnail);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), R.string.duet_is_disabled_by_the_creator, Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }


        });

        binding.llDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
////                            rescaleURLVideo(model.getPostVideo());
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                            ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_storage_permission, null), getContext().getResources().getString(R.string.to_capture_storage_denied),
                                getContext().getResources().getString(R.string.not_now), getContext().getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                                    @Override
                                    public void onPositiveDismiss() {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onNegativeDismiss() {

                                    }
                                });
                    } else {
                        customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_storage_permission, null), getContext().getResources().getString(R.string.to_capture_storage),
                                getContext().getResources().getString(R.string.not_now), getContext().getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

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
                    assert getArguments() != null;
                    if (getArguments().getInt("canSave") == 1) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                        getActivity().requestPermissions(permissions, Permission_CODE);
                                ActivityCompat.requestPermissions(getActivity(), new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, Permission_CODE);
                            } else {
//                        downloadVideo(viewModel.videoPath, model);
                                addWaterMarkOnVideo(viewModel.videoPath, model);
                            }
                        } else {
//                    downloadVideo(viewModel.videoPath, model);
                            addWaterMarkOnVideo(viewModel.videoPath, model);
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.Download_is_disabled_by_the_creator, Toast.LENGTH_SHORT).show();
                    }
//                dismiss();
                    dismiss();
                }

            }
        });

        binding.llDelete.setOnClickListener(view -> {
////                            rescaleURLVideo(model.getPostVideo());
            assert getArguments() != null;
            deletePost(getArguments().getString("postid"));
//                dismiss();
        });

        binding.llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionsSheetFragment.this.getContext(), ReportActivity.class);
                intent.putExtra("postid", viewModel.postId);
                intent.putExtra("reporttype", 1);
                intent.putExtra("userid", Global.userId);
                startActivity(intent);
                dismiss();
            }
        });
    }

    public void deletePost(String postId) {

        disposable.add(Global.initRetrofit().deletePost(Global.apikey, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((RestResponse deletePost, Throwable throwable) -> {

                    if (deletePost != null && deletePost.getStatus() != null) {
//                        getActivity().finish();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("videoDeleted",postId);
                        ((PlayerActivity) context).setResult(RESULT_OK, resultIntent);
                        ((PlayerActivity) context).finish();
//                        dismiss();
                    }
                }));
    }

    private void reportPost(Video.Data model) {

        Intent intent = new Intent(getActivity(), ReportActivity.class);
        intent.putExtra("postid", viewModel.postId);
        intent.putExtra("reporttype", 1);
        intent.putExtra("userid", Global.userId);
        startActivity(intent);
       /* ReportSheetFragment fragment = new ReportSheetFragment();
        Bundle args = new Bundle();
        args.putString("postid", model.getPostId());
        args.putInt("reporttype", 1);
        args.putString("userid", Global.userId);
        fragment.setArguments(args);
        fragment.show(getChildFragmentManager(), fragment.getClass().getSimpleName());
        dismiss();*/
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void downloadVideo(String url, Video.Data videoModel) {
//        File file = new File(Environment.getExternalStorageDirectory(), "Medley");
//        if (!file.exists()) {
//            file.mkdirs();
//        }
        Uri Download_Uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);
        // Visibility of the download Notification
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle("Downloading");
        //Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription("Downloading File");

        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, "medley/Medley_" + videoModel.getUserName() + "_" + videoModel.getPostId() + ".mp4"); // for public destination

        downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);

        Utilss.enqueue = downloadManager.enqueue(request);// enqueue puts the download request in the queue.

        dismiss();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Permission_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    downloadVideo(viewModel.videoPath, model);
                    addWaterMarkOnVideo(viewModel.videoPath, model);
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            case Permission_CODE_duet:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    new DownloadFileFromURL().execute(viewModel.videoPath);
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }


//    class DownloadFileFromURL extends AsyncTask<String, String, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            System.out.println("Starting download");
//            pDialog = new ProgressDialog(getContext());
//            pDialog.setMessage(getResources().getString(R.string.please_wait));
//            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            pDialog.setIndeterminate(false);
//            pDialog.setMax(100);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        /**
//         * Downloading file in background thread
//         */
//        @Override
//        protected String doInBackground(String... f_url) {
//            int count;
//            String state;
//            state = Environment.getExternalStorageState();
//            if (Environment.MEDIA_MOUNTED.equals(state)) {
//                try {
////                    if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT) {
////                        directoryName = new File(Environment.getExternalStorageDirectory().getPath()
////                                + "//Medley_Video");
////                    } else {
////                        directoryName = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES).getPath()
////                                + "//Medley_Video");
////                    }
//
//                    directoryName = new File(String.valueOf(context.getExternalCacheDir()));
////                    directoryName = new File(Environment.getExternalStorageDirectory(), "Medley_Video");
//                    if (!directoryName.exists()) {
//                        directoryName.mkdirs();
//                    }
//
//                    fileName = new File(directoryName, "Medley_" + model.getUserName() + "_" + model.getPostId() + ".mp4");
//                    fileName = new File(directoryName, "Medley_" + model.getUserName() + "_" + model.getPostId() + ".mp4");
//                    System.out.println("Downloading");
//                    URL url = new URL(f_url[0]);
//
//                    URLConnection conection = url.openConnection();
//                    int fileLength = conection.getContentLength();
//                    conection.connect();
//                    // getting file length
//                    int lenghtOfFile = conection.getContentLength();
//                    // input stream to read file - with 8k buffer
//                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
//                    // Output stream to write file
//                    OutputStream output = new FileOutputStream(fileName);
//                    byte data[] = new byte[1024];
//                    long total = 0;
//                    while ((count = input.read(data)) != -1) {
//                        total += count;
//                        // writing data to file
//                        publishProgress(String.valueOf(total * 100 / fileLength));
//                        output.write(data, 0, count);
//                    }
//                    // flushing output
//                    output.flush();
//                    // closing streams
//                    output.close();
//                    input.close();
//
//                } catch (Exception e) {
//                    Log.e("Error: ", e.getMessage());
//                }
//            } else {
//                Toast.makeText(context, "Folder Not created", Toast.LENGTH_LONG).show();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String file_url) {
//            System.out.println("Downloaded");
//            Uri uri = Uri.fromFile(fileName);
//            Log.e("uri===", "=========" + uri);
//            Intent intent = new Intent(context, DuoActivity.class);
//            intent.putExtra("videoPath", uri.toString());
//            intent.putExtra("music_url", viewModel.audioPath);
//            intent.putExtra("soundId", viewModel.soundId);
//            startActivity(intent);
//            pDialog.dismiss();
//            dismiss();
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);
//            pDialog.setProgress(Integer.parseInt(values[0]));
//        }
//    }

    private void addWaterMarkOnVideo(String videoPath, Video.Data model) {
        customDialogBuilder.showLoadingDialog();
        String[] strArr;
        String format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsoluteFile());
        sb.append("/");
        sb.append("Movies");
        sb.append("/");
        sb.append("medley");
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Environment.getExternalStorageDirectory().getAbsoluteFile());
        sb2.append("/");
        sb2.append("Movies");
        sb2.append("/");
        sb2.append("medley");
        sb2.append("/Medley" + System.currentTimeMillis());
        sb2.append(format);
        sb2.append(".mp4");
        y = sb2.toString();

        saveImage();
        copyAssets();
        String fontPath = context.getExternalCacheDir() + "/Mark_One.ttf";
//        strArr = new String[]{"-i", videoPath, "-i", context.getExternalCacheDir() + "/medley_watermark.png", "-filter_complex", "overlay=10:10", y};
        strArr = new String[]{"-i", videoPath, "-i", context.getExternalCacheDir() + "/medley_watermark.png", "-filter_complex", "[1]scale=iw/4:-1[wm];[0][wm]overlay=10:10,drawtext=fontfile=" + fontPath + ":text='@" + model.getUserName() + "':x=w-tw-20:y=h-th-20:fontsize=24:fontcolor=white", "-b:a", "192k", "-b", "4000k", "-minrate", "4000k", "-maxrate", "4000k", "-bufsize", "1835k", y};
//        strArr = new String[]{ "-i", videoPath, "-vf", "drawtext=fontfile="+fontPath+":text='My text starting at 640x360':x=64:y=36:fontsize=24:fontcolor=black", "-codec:a", "copy", y};

        a(strArr, y);
    }

    private void saveImage() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.medley_watermark);
        String extStorageDirectory = context.getExternalCacheDir().toString();
        File file = new File(extStorageDirectory, "medley_watermark.png");
        if (!file.exists()) {
            try {
                FileOutputStream outStream = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void copyAssets() {
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open("Mark_One.ttf");

            String outDir = context.getExternalCacheDir().getAbsolutePath();

            File outFile = new File(outDir, "Mark_One.ttf");

            out = new FileOutputStream(outFile);
            copyFile(in, out);
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + "Mark_One.ttf", e);
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void a(String[] strArr, final String str) {
//        final ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Please Wait");
//        progressDialog.show();
        customDialogBuilder.showLoadingDialog();
        String ffmpegCommand = UtilCommand.main(strArr);
        FFmpeg.executeAsync(ffmpegCommand, new ExecuteCallback() {

            @Override
            public void apply(final long executionId, final int returnCode) {
//                Log.d("TAG", String.format("FFmpeg process exited with rc %d.", returnCode));

//                Log.d("TAG", "FFmpeg process output:");

                Config.printLastCommandOutput(Log.INFO);

                customDialogBuilder.hideLoadingDialog();
                if (returnCode == RETURN_CODE_SUCCESS) {
                    customDialogBuilder.hideLoadingDialog();
                    Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                    intent.setData(Uri.fromFile(new File(y)));
                    context.sendBroadcast(intent);
//                    c();
                    customDialogBuilder.hideLoadingDialog();
                    Toast.makeText(context, "Video Downloaded Successfully", Toast.LENGTH_SHORT).show();
                    refreshGallery(str);

//                    Intent fileIntent = new Intent(Intent.ACTION_VIEW);
//                    fileIntent.setDataAndType(Uri.fromFile(new File(str)), "video/*");
//                    getActivity().startActivity(fileIntent);
                    open_file(context, str);

                } else if (returnCode == RETURN_CODE_CANCEL) {
//                    Log.d("ffmpegfailure", str);
                    try {
                        new File(str).delete();
                        Toast.makeText(context, "Error Creating", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                } else {
//                    Log.d("ffmpegfailure", str);
                    try {
                        new File(str).delete();
                        Toast.makeText(context, "Video Failed", Toast.LENGTH_SHORT).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }


            }
        });
        getActivity().getWindow().clearFlags(16);
    }

    public void refreshGallery(String str) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(str)));
        context.sendBroadcast(intent);
    }

    public void open_file(Context context, String filename) {
//        File path = new File(getFilesDir(), "dl");
//        if (ContentResolver.SCHEME_FILE.equals(attachmentUri.getScheme())) {
//            File file = new File(Objects.requireNonNull(attachmentUri.getPath()));
//            attachmentUri = FileProvider.getUriForFile(getContext(), "com.meest.provider", file);
//        }

        File file = new File(filename);
//
//        // Get URI and MIME type of file
        Uri uri = FileProvider.getUriForFile(context, "com.meest.provider", file);
//        String mime = getActivity().getContentResolver().getType(uri);

        // Open file with user selected app
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "video/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }
}
