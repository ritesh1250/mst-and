package com.meest.videomvvmmodule.view.share;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.meest.databinding.FragmentForUBinding;
import com.meest.databinding.ItemVideoListBinding;
import com.meest.videoEditing.UtilCommand;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.Global;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.meest.R;
import com.meest.databinding.FragmentShareSheetBinding;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.view.home.ReportSheetFragment;
import com.meest.videomvvmmodule.viewmodel.ShareSheetViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.LinkProperties;
import timber.log.Timber;

public class ShareSheetFragment extends BottomSheetDialogFragment {

    private static final int MY_PERMISSIONS_REQUEST = 101;
    FragmentShareSheetBinding binding;
    ShareSheetViewModel viewModel;
    private CustomDialogBuilder customDialogBuilder;
    private int position;
    public ItemVideoListBinding parentBinding;
    private String y;
    Intent share;
    Context context;
    public ShareSheetFragment(ItemVideoListBinding binding, Context context) {
        // Required empty public constructor
        parentBinding=binding;
        this.context=context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog dialog = (BottomSheetDialog) dialog1;
            dialog.setCanceledOnTouchOutside(true);

        });

        return bottomSheetDialog;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_share_sheet, container, false);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new ShareSheetViewModel()).createFor()).get(ShareSheetViewModel.class);
        customDialogBuilder = new CustomDialogBuilder(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initListeners();
        initObserve();
    }

    private void initView() {
        binding.setViewModel(viewModel);
        if (getArguments() != null && getArguments().getString("video") != null) {
            viewModel.video = new Gson().fromJson(getArguments().getString("video"), Video.Data.class);
            position = getArguments().getInt("position");
        }
        createVideoShareLink();
    }


    private void initListeners() {
        /*binding.btnCopy.setOnClickListener(v -> {
            if (getActivity() != null) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Video link", viewModel.shareUrl);
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getActivity(), R.string.Copied_Clipboard_To_Successfully, Toast.LENGTH_SHORT).show();
                }
            }
        });*/
//        binding.btnDownload.setOnClickListener(view -> {
//            if (viewModel.video != null && viewModel.video.getCanSave() == 0) {
//                Toast.makeText(getActivity(), R.string.Download_is_disabled_by_the_creator, Toast.LENGTH_SHORT).show();
//            } else {
//                initPermission();
//            }
//        });
//        binding.btnReport.setOnClickListener(view -> {
//            ReportSheetFragment fragment = new ReportSheetFragment();
//            Bundle args = new Bundle();
//            args.putString("postid", viewModel.video.getPostId());
//            args.putInt("reporttype", 1);
//            args.putString("userid", Global.userId);
//            fragment.setArguments(args);
//            if (getParentFragment() != null) {
//                fragment.show(getParentFragment().getChildFragmentManager(), fragment.getClass().getSimpleName());
//            }
//            dismiss();
//        });
    }

    private void initObserve() {
        viewModel.onItemClick.observe(getViewLifecycleOwner(), type -> {
            share = new Intent(Intent.ACTION_SEND);
            switch (type) {
                case 1:     // Instagram
                    share.setPackage("com.instagram.android");
                    break;
                case 2:   // facebook
                    share.setPackage("com.facebook.katana");
                    break;
                case 3:   // whatsapp
                    share.setPackage("com.whatsapp");
                    break;
                case 4:   // messenger
                    share.setPackage("com.facebook.orca");
                    break;
                case 5:   // twitter
                    share.setPackage("com.twitter.android.PostActivity");
                    break;
                // other
                case 6:

                    break;
            }
//            String app_url="https://play.google.com/store/apps/details?id="+getActivity().getPackageName();
            String path = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                    "/" +
                    "Movies" +
                    "/" +
                    "medley" +
                    "/Medley" +
                    viewModel.video.getPostId() +
                    viewModel.video.getUserId() +
                    ".mp4";
            File file = new File(path);
            if (!file.exists()) {
                addWaterMarkOnVideo(viewModel.video.getPostVideo(), viewModel.video);
            } else {
                open_file(context, path, viewModel.video);
            }
            viewModel.sharePost(viewModel.video.getPostId(), context, viewModel.video, parentBinding, position);

//            String shareBody = viewModel.shareUrl + "\nWatch this amazing video on Meest App";
//            share.setType("text/plain");
//            share.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Video");
//            share.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//            startActivity(Intent.createChooser(share, "Share Video"));
            dismiss();
        });
    }


    private void initPermission() {
        if (getActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);
            } else {
                startDownload();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDownload();
            }
        }
    }

    private void startDownload() {
        Log.d("DOWNLOAD", "startDownload: ");
        PRDownloader.download(viewModel.video.getPostVideo(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), viewModel.video.getPostVideo())
//        PRDownloader.download(Const.ITEM_BASE_URL + viewModel.video.getPostVideo(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(), viewModel.video.getPostVideo())
                .build()
                .setOnStartOrResumeListener(() -> customDialogBuilder.showLoadingDialog())
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        customDialogBuilder.hideLoadingDialog();
                        Toast.makeText(getActivity(), R.string.save, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Error error) {
                        customDialogBuilder.hideLoadingDialog();
                        Log.d("DOWNLOAD", "onError: " + error.getConnectionException().getMessage());
                    }
                });
    }

    public File getPath() {
        if (getActivity() != null) {
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
        return new File(Environment.getRootDirectory().getAbsolutePath());
    }

    private void createVideoShareLink() {
        String json = new Gson().toJson(viewModel.video);
        String title = viewModel.video.getPostDescription();

        Log.i("ShareJson", "Json Object: " + json);
        BranchUniversalObject buo = new BranchUniversalObject()
                .setCanonicalIdentifier("content/12345")
                .setTitle(title)

                .setContentImageUrl(viewModel.video.getPostVideo())
//                .setContentImageUrl(Const.ITEM_BASE_URL + viewModel.video.getPostImage())
//                .setContentImageUrl(viewModel.video.getPostImage())
                .setContentDescription(viewModel.video.getPostDescription())
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setContentMetadata(new ContentMetadata().addCustomMetadata("data", json));

        LinkProperties lp = new LinkProperties()
                .setFeature("sharing")
                .setCampaign("Content launch")
                .setStage("Video")
                .addControlParameter("custom", "data")
                .addControlParameter("custom_random", Long.toString(Calendar.getInstance().getTimeInMillis()));

        if (getActivity() != null) {
            buo.generateShortUrl(getActivity(), lp, (url, error) -> {
                Log.e("VIDEO_URL", "shareProfile: " + viewModel.video.getPostVideo());
                Log.e("VIDEO_URL", "shareProfile: " + url);
                viewModel.shareUrl = url;
//                viewModel.shareUrl = viewModel.video.getPostVideo();
            });
        }
    }

    private void copyAssets() {
        AssetManager assetManager = context.getAssets();
        InputStream in;
        OutputStream out;
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
            Timber.e(e, "Failed to copy asset file: Mark_One.ttf");
        }
    }

    private void addWaterMarkOnVideo(String videoPath, Video.Data model) {
        customDialogBuilder.showLoadingDialog();
        String[] strArr;
        String format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        String sb = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                "/" +
                "Movies" +
                "/" +
                "medley";

        File file = new File(sb);

        if (!file.exists()) {
            if (file.mkdirs()) {
                Timber.d("Directory Created");
            }
        }

        y = Environment.getExternalStorageDirectory().getAbsoluteFile() +
                "/" +
                "Movies" +
                "/" +
                "medley" +
                "/Medley" +
                model.getPostId() +
                model.getUserId() +
                ".mp4";

        saveImage();
        copyAssets();
        String fontPath = context.getExternalCacheDir() + "/Mark_One.ttf";
        strArr = new String[]{"-i", videoPath, "-i", context.getExternalCacheDir() + "/medley_watermark.png", "-filter_complex", "[1]scale=iw/4:-1[wm];[0][wm]overlay=10:10,drawtext=fontfile=" + fontPath + ":text='@" + model.getUserName() + "':x=w-tw-20:y=h-th-20:fontsize=24:fontcolor=white", "-b:a", "192k", "-b", "4000k", "-minrate", "4000k", "-maxrate", "4000k", "-bufsize", "1835k", y};
        a(strArr, y, model);
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

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void a(String[] strArr, final String str, Video.Data model) {
        customDialogBuilder.showLoadingDialog();
        String ffmpegCommand = UtilCommand.main(strArr);
        FFmpeg.executeAsync(ffmpegCommand, (executionId, returnCode) -> {

            Config.printLastCommandOutput(Log.INFO);

            customDialogBuilder.hideLoadingDialog();
            if (returnCode == RETURN_CODE_SUCCESS) {
                customDialogBuilder.hideLoadingDialog();
                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                intent.setData(Uri.fromFile(new File(y)));
                context.sendBroadcast(intent);
//                    c();
                customDialogBuilder.hideLoadingDialog();
//                    Toast.makeText(context, "Video Downloaded Successfully", Toast.LENGTH_SHORT).show();
                refreshGallery(str);

//                    Intent fileIntent = new Intent(Intent.ACTION_VIEW);
//                    fileIntent.setDataAndType(Uri.fromFile(new File(str)), "video/*");
//                    getActivity().startActivity(fileIntent);
                open_file(context, str, model);

            } else if (returnCode == RETURN_CODE_CANCEL) {
//                    Log.d("ffmpegfailure", str);
                try {
                    boolean b = new File(str).delete();
                    if (b) {
                        Timber.d("Directory Deleted");
                    }
                    Toast.makeText(context, "Error Creating", Toast.LENGTH_SHORT).show();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            } else {
                try {
                    boolean v = new File(str).delete();
                    if (v) {
                        Timber.d("Directory Deleted");
                    }
                    Toast.makeText(context, "Video Failed", Toast.LENGTH_SHORT).show();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
        ((Activity)context).getWindow().clearFlags(16);
    }

    public void refreshGallery(String str) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(str)));
        context.sendBroadcast(intent);
    }

    public void open_file(Context mContext, String filename, Video.Data model) {
//        File path = new File(getFilesDir(), "dl");
//        if (ContentResolver.SCHEME_FILE.equals(attachmentUri.getScheme())) {
//            File file = new File(Objects.requireNonNull(attachmentUri.getPath()));
//            attachmentUri = FileProvider.getUriForFile(context, "com.meest.provider", file);
//        }

        File file = new File(filename);
//
//        // Get URI and MIME type of file
        Uri uri = FileProvider.getUriForFile(mContext, "com.meest.provider", file);
//        String mime = getActivity().getContentResolver().getType(uri);

        // Open file with user selected app
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setDataAndType(uri, "video/*");
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        context.startActivity(intent);

        // ProgressDialog dialog = ProgressDialog.show(context, "", "Please wait...", true);
        customDialogBuilder.showLoadingDialog();
        // dialog.setCancelable(false);
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.meest4bharat.com/?video=" + model.getPostId()))
                .setDomainUriPrefix(Const.deepLinkingUriPrefix)
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder(Const.iosBundleId).build())
                .buildShortDynamicLink()
                .addOnFailureListener(e -> {
                })
                .addOnCompleteListener(task -> {
                    customDialogBuilder.hideLoadingDialog();
                    if (task.isSuccessful()) {
//                        Intent share = new Intent(Intent.ACTION_SEND);
                        String shareBody = mContext.getResources().getString(R.string.Watch_more_of_such) + task.getResult().getShortLink();
                        share.setType("video/*");
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        share.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.Share_Video));
                        share.putExtra(Intent.EXTRA_TEXT, shareBody);
                        ((Activity)mContext).startActivity(Intent.createChooser(share, mContext.getResources().getString(R.string.Share_Video)));
                    } else {
                        Toast.makeText(context, mContext.getResources().getString(R.string.Error_Creating_Link), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}