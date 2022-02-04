package com.meest.videoEditing.videojoiner;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.media.ThumbnailUtils;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.meest.R;
import com.meest.databinding.DailogProgressBinding;
import com.meest.videoEditing.UtilCommand;
import com.meest.videoEditing.VideoSliceSeekBar;
import com.meest.videoEditing.VideoSliceSeekBar.SeekBarChangeListener;
import com.meest.videoEditing.videojoiner.model.VideoPlayerState;
import com.meest.videomvvmmodule.AudioExtractor;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.SessionManager;
import com.meest.videomvvmmodule.view.media.BottomSheetImagePicker;
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
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@SuppressLint({"WrongConstant"})
public class VideoJoinerActivity extends AppCompatActivity implements OnClickListener {
    static final boolean r = true;
    int a = 0;
    int b = 0;
    Handler c = new Handler();
    private DailogProgressBinding progressBinding;
    String d;
    ImageView e;
    ImageView f;
    TextView g;
    TextView h;
    TextView i;
    TextView j;
    VideoSliceSeekBar k;
    VideoSliceSeekBar l;
    int m = 0;
    int n = 0;
    Button selectAnother;
    VideoView o;
    VideoView p;
    ImageView closeBtn;
    ImageView ivSelect;
    private Dialog mBuilder;
    Runnable q = new Runnable() {
        public void run() {
            VideoJoinerActivity.this.b();
        }
    };
    private CustomDialogBuilder customDialogBuilder;
    public VideoPlayerState s = new VideoPlayerState();

    public VideoPlayerState t = new VideoPlayerState();
    private a u = new a();
    private b v = new b();
    private String anotherUri;
    private String format;
    public MediaPlayer audio = new MediaPlayer();
    private ArrayList<String> videoPaths;
//    private Ads ads;

    private class a extends Handler {
        private boolean b;
        private Runnable c;

        private a() {
            this.b = false;
            this.c = new Runnable() {
                public void run() {
                    a.this.a();
                }
            };
        }


        public void a() {
            if (!this.b) {
                this.b = VideoJoinerActivity.r;
                sendEmptyMessage(0);
            }
        }

        @Override
        public void handleMessage(Message message) {
            this.b = false;
            VideoJoinerActivity.this.k.videoPlayingProgress(VideoJoinerActivity.this.o.getCurrentPosition());
            if (!VideoJoinerActivity.this.o.isPlaying() || VideoJoinerActivity.this.o.getCurrentPosition() >= VideoJoinerActivity.this.k.getRightProgress()) {
                if (VideoJoinerActivity.this.o.isPlaying()) {
                    VideoJoinerActivity.this.o.pause();
                    VideoJoinerActivity.this.e.setBackgroundResource(R.drawable.ic_edit_play);
                }
                VideoJoinerActivity.this.k.setSliceBlocked(false);
                VideoJoinerActivity.this.k.removeVideoStatusThumb();
                return;
            }
            postDelayed(this.c, 50);
        }
    }

    private class b extends Handler {
        private boolean b;
        private Runnable c;

        private b() {
            this.b = false;
            this.c = new Runnable() {
                public void run() {
                    b.this.a();
                }
            };
        }


        public void a() {
            if (!this.b) {
                this.b = VideoJoinerActivity.r;
                sendEmptyMessage(0);
            }
        }

        @Override
        public void handleMessage(Message message) {
            this.b = false;
            VideoJoinerActivity.this.l.videoPlayingProgress(VideoJoinerActivity.this.p.getCurrentPosition());
            if (!VideoJoinerActivity.this.p.isPlaying() || VideoJoinerActivity.this.p.getCurrentPosition() >= VideoJoinerActivity.this.l.getRightProgress()) {
                if (VideoJoinerActivity.this.p.isPlaying()) {
                    VideoJoinerActivity.this.p.pause();
                    VideoJoinerActivity.this.f.setBackgroundResource(R.drawable.ic_play);
                }
                VideoJoinerActivity.this.l.setSliceBlocked(false);
                VideoJoinerActivity.this.l.removeVideoStatusThumb();
                return;
            }
            postDelayed(this.c, 50);
        }
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.videojoineractivity);
        selectAnother = findViewById(R.id.selectAnother);
        format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        customDialogBuilder = new CustomDialogBuilder(VideoJoinerActivity.this);
        initProgressDialog();
        i();
        this.o.setVideoPath(getIntent().getStringExtra("videoPath"));
        closeBtn = (ImageView) findViewById(R.id.close_btn);
        ivSelect = (ImageView) findViewById(R.id.iv_select);
        this.o.seekTo(100);
        this.p.seekTo(100);
        d();
        f();
        this.o.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                VideoJoinerActivity.this.e.setBackgroundResource(R.drawable.ic_play);
            }
        });
        this.p.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                VideoJoinerActivity.this.f.setBackgroundResource(R.drawable.ic_play);
            }
        });
        selectAnother.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
        closeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(VideoJoinerActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.discard_edit_video);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                TextView tvYes = dialog.findViewById(R.id.tvYes);
                TextView tvNo = dialog.findViewById(R.id.tvNo);
                dialog.show();
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
//                onBackPressed();
            }
        });

        ivSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(VideoJoinerActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.post_edit_video);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                TextView tvYes = dialog.findViewById(R.id.tvYes);
                TextView tvNo = dialog.findViewById(R.id.tvNo);
                dialog.show();
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (o.isPlaying()) {
                            o.pause();
                            e.setBackgroundResource(R.drawable.ic_play);
                        } else if (p.isPlaying()) {
                            p.pause();
                            f.setBackgroundResource(R.drawable.ic_play);
                        }
                        h();
                        dialog.dismiss();
                    }
                });
                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private void chooseFile() {
        BottomSheetImagePicker bottomSheetImagePicker = BottomSheetImagePicker.Companion.getNewInstance(1);
        bottomSheetImagePicker.setOnDismiss(uri -> {
            String fileExtention = "";

            int i = uri.lastIndexOf('.');
            if (i > 0) {
                fileExtention = uri.substring(i + 1);
            }

            Log.e("filePath=======", fileExtention);
            if (fileExtention.equalsIgnoreCase("mp4")) {
                if (!uri.isEmpty()) {
                    File file = new File(uri);
                    // Get length of file in bytes
                    long fileSizeInBytes = file.length();
                    // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                    long fileSizeInMB = fileSizeInKB / 1024;
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(this, Uri.fromFile(new File(uri)));
                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    Log.e("time", "=======================" + time);

                    long timeInMilliSec = Long.parseLong(time);
                    Log.e("timeInMilliSec", "=======================" + timeInMilliSec);
                    if (timeInMilliSec >= 60200) {
                        customDialogBuilder.showSimpleDialog("Too long video", "This video is longer than 1 min.\nPlease select another..",
                                "Cancel", "Select another", new CustomDialogBuilder.OnDismissListener() {

                                    @Override
                                    public void onPositiveDismiss() {
                                        bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
                                    }

                                    @Override
                                    public void onNegativeDismiss() {

                                    }

                                });
                    } else if (fileSizeInMB < 200) {
//                        this.p.setVideoPath(uri);
                        videoPaths = new ArrayList<>();
                        videoPaths.add(uri);
                        anotherUri = uri;
                        if (fileSizeInMB > 5) {
                            saveData(true);
                        } else {
                            saveData(false);
                        }
                    } else {
                        customDialogBuilder.showSimpleDialog(getString(R.string.Too_long_videos_size), getString(R.string.This_videos_size_is_greater_than_200Mb_Please_select_another),
                                getString(R.string.cancel), getString(R.string.Select_another), new CustomDialogBuilder.OnDismissListener() {
                                    @Override
                                    public void onPositiveDismiss() {
                                        bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
                                    }

                                    @Override
                                    public void onNegativeDismiss() {

                                    }
                                });
                    }
                    retriever.release();
                }
            } else {
                Toast.makeText(this, getString(R.string.Please_Select_mp4_format_video), Toast.LENGTH_SHORT).show();
            }
        });
        bottomSheetImagePicker.show(getSupportFragmentManager(), BottomSheetImagePicker.class.getSimpleName());
    }


    public void b() {
//        ads.showInd(new Adclick() {
//            @Override
//            public void onclicl() {
        c();
//            }
//        });
    }


    public void c() {
//        Intent intent = new Intent(this, AddAudioActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("song", this.d);
//        intent.putExtras(bundle);
//        startActivity(intent);
        Intent intent = new Intent();
        intent.setFlags(67108864);
        intent.putExtra("song", this.d);
        setResult(RESULT_OK, intent);
//        startActivity(intent);
        finish();
    }

    public static String formatTimeUnit(long j2) throws ParseException {
        return String.format("%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j2)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j2) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j2)))});
    }

    private void d() {
        this.o.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                VideoJoinerActivity.this.k.setSeekBarChangeListener(new SeekBarChangeListener() {
                    public void SeekBarValueChanged(int i, int i2) {
                        if (VideoJoinerActivity.this.k.getSelectedThumb() == 1) {
                            VideoJoinerActivity.this.o.seekTo(VideoJoinerActivity.this.k.getLeftProgress());
                        }
                        VideoJoinerActivity.this.i.setText(VideoJoinerActivity.formatTimeUnit((long) i));
                        VideoJoinerActivity.this.g.setText(VideoJoinerActivity.formatTimeUnit((long) i2));
                        VideoJoinerActivity.this.s.setStart(i);
                        VideoJoinerActivity.this.s.setStop(i2);
                        VideoJoinerActivity.this.m = i / 1000;
                        VideoJoinerActivity.this.a = i2 / 1000;
                    }
                });
                VideoJoinerActivity.this.k.setMaxValue(mediaPlayer.getDuration());
                VideoJoinerActivity.this.k.setLeftProgress(0);
                VideoJoinerActivity.this.k.setRightProgress(mediaPlayer.getDuration());
                VideoJoinerActivity.this.k.setProgressMinDiff(0);
            }
        });
    }

    private void e() {
        if (this.p.isPlaying()) {
            this.p.pause();
            this.f.setBackgroundResource(R.drawable.ic_play);
        }
        if (this.o.isPlaying()) {
            this.o.pause();
            this.k.setSliceBlocked(false);
            this.e.setBackgroundResource(R.drawable.ic_play);
            this.k.removeVideoStatusThumb();
            return;
        }
        this.o.seekTo(this.k.getLeftProgress());
        this.o.start();
        this.k.videoPlayingProgress(this.k.getLeftProgress());
        this.e.setBackgroundResource(0);
        this.u.a();
    }

    private void f() {
        this.p.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                VideoJoinerActivity.this.l.setSeekBarChangeListener(new SeekBarChangeListener() {
                    public void SeekBarValueChanged(int i, int i2) {
                        if (VideoJoinerActivity.this.l.getSelectedThumb() == 1) {
                            VideoJoinerActivity.this.p.seekTo(VideoJoinerActivity.this.l.getLeftProgress());
                        }
                        VideoJoinerActivity.this.j.setText(VideoJoinerActivity.formatTimeUnit((long) i));
                        VideoJoinerActivity.this.h.setText(VideoJoinerActivity.formatTimeUnit((long) i2));
                        VideoJoinerActivity.this.t.setStart(i);
                        VideoJoinerActivity.this.t.setStop(i2);
                        VideoJoinerActivity.this.n = i / 1000;
                        VideoJoinerActivity.this.b = i2 / 1000;
                    }
                });
                VideoJoinerActivity.this.l.setMaxValue(mediaPlayer.getDuration());
                VideoJoinerActivity.this.l.setLeftProgress(0);
                VideoJoinerActivity.this.l.setRightProgress(mediaPlayer.getDuration());
                VideoJoinerActivity.this.l.setProgressMinDiff(0);
            }
        });
    }

    private void g() {
        if (this.o.isPlaying()) {
            this.o.pause();
            this.e.setBackgroundResource(R.drawable.ic_play);
        }
        if (this.p.isPlaying()) {
            this.p.pause();
            this.l.setSliceBlocked(false);
            this.f.setBackgroundResource(R.drawable.ic_play);
            this.l.removeVideoStatusThumb();
            return;
        }
        this.p.seekTo(this.l.getLeftProgress());
        this.p.start();
        this.l.videoPlayingProgress(this.l.getLeftProgress());
        this.f.setBackgroundResource(0);
        this.v.a();
    }

    @Override
    public void onClick(View view) {
        if (view == this.e) {
            e();
        }
        if (view == this.f) {
            g();
        }
    }

    private void h() {
//        anotherUri=getIntent().getStringExtra("videoPath");
        String format = new SimpleDateFormat("_HHmmss", Locale.US).format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(getExternalCacheDir());
//        sb.append("/");
//        sb.append(getResources().getString(R.string.MainFolderName));
//        sb.append("/");
//        sb.append(getResources().getString(R.string.VideoJoiner));
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(getExternalCacheDir());
//        sb2.append("/");
//        sb2.append(getResources().getString(R.string.MainFolderName));
//        sb2.append("/");
//        sb2.append(getResources().getString(R.string.VideoJoiner));
        sb2.append("/videojoiner");
        sb2.append(format);
        sb2.append(".mp4");
        this.d = sb2.toString();
//      its working but second video sound remove
        a(new String[]{ "-i", getIntent().getStringExtra("videoPath"), "-i", anotherUri, "-filter_complex", "[0:v]scale=640x360,setsar=1:1[v0];[1:v]scale=640x360,setsar=1:1[v1];[v0][v1] concat=n=2:v=1", "-s", "640x360", this.d}, this.d);
//        a(new String[]{"-y", "-ss", String.valueOf(this.m), "-t", String.valueOf(this.a), "-i", (String) FileUtils.myUri.get(0), "-ss", String.valueOf(this.n), "-t", String.valueOf(this.b), "-i", (String) FileUtils.myUri.get(1), "-strict", "experimental", "-filter_complex", "[0:v]scale=320x240,setsar=1:1[v0];[1:v]scale=320x240,setsar=1:1[v1];[v0][v1] concat=n=2:v=1", "-ab", "48000", "-ac", "2", "-ar", "22050", "-s", "320x240", "-r", "15", "-b", "2097k", "-vcodec", "mpeg4", this.d}, this.d);
//      its working but scaling not work
//      a(new String[]{"-i", getIntent().getStringExtra("videoPath"), "-i", anotherUri,"-filter_complex", "[0:v:0] [0:a:0] [1:v:0] [1:a:0] concat=n=2:v=1:a=1 [v] [a]","-map","[v]","-map", "[a]", this.d}, this.d);

    }

    private void a(String[] strArr, final String str) {
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
                    Intent intent = new Intent();
                    intent.setData(Uri.fromFile(new File(VideoJoinerActivity.this.d)));
                    VideoJoinerActivity.this.sendBroadcast(intent);
                    try {
                        MediaScannerConnection.scanFile(VideoJoinerActivity.this, new String[]{VideoJoinerActivity.this.d}, null, new OnScanCompletedListener() {
                            public void onScanCompleted(String str, Uri uri) {
                                VideoJoinerActivity.this.c.postDelayed(VideoJoinerActivity.this.q, 100);
                            }
                        });
                    } catch (Exception unused) {
                        VideoJoinerActivity.this.c.postDelayed(VideoJoinerActivity.this.q, 100);
                    }

                } else if (returnCode == RETURN_CODE_CANCEL) {
                    Log.d("ffmpegfailure", str);
                    try {
                        new File(str).delete();
                        VideoJoinerActivity.this.deleteFromGallery(str);
                        Toast.makeText(VideoJoinerActivity.this, getString(R.string.Error_Creating_Video), 0).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                } else {
                    Log.d("ffmpegfailure", str);
                    try {
                        new File(str).delete();
                        VideoJoinerActivity.this.deleteFromGallery(str);
                        Toast.makeText(VideoJoinerActivity.this, getString(R.string.Error_Creating_Video), 0).show();
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }

                VideoJoinerActivity.this.refreshGallery(str);

            }
        });

        getWindow().clearFlags(16);
    }

    public void deleteFromGallery(String str) {
        String[] strArr = {"_id"};
        String[] strArr2 = {str};
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor query = contentResolver.query(uri, strArr, "_data = ?", strArr2, null);
        if (query.moveToFirst()) {
            try {
                contentResolver.delete(ContentUris.withAppendedId(Media.EXTERNAL_CONTENT_URI, query.getLong(query.getColumnIndexOrThrow("_id"))), null, null);
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            }
        } else {
            try {
                new File(str).delete();
                refreshGallery(str);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        query.close();
    }

    public void refreshGallery(String str) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(new File(str)));
        sendBroadcast(intent);
    }

    private void i() {
        this.e = (ImageView) findViewById(R.id.buttonply1);
        this.f = (ImageView) findViewById(R.id.buttonply2);
        this.k = (VideoSliceSeekBar) findViewById(R.id.seek_bar1);
        this.l = (VideoSliceSeekBar) findViewById(R.id.seek_bar2);
        this.i = (TextView) findViewById(R.id.left_pointer1);
        this.j = (TextView) findViewById(R.id.left_pointer2);
        this.g = (TextView) findViewById(R.id.right_pointer1);
        this.h = (TextView) findViewById(R.id.right_pointer2);
        this.o = (VideoView) findViewById(R.id.videoView1);
        this.p = (VideoView) findViewById(R.id.videoView2);
        this.e.setOnClickListener(this);
        this.f.setOnClickListener(this);
    }

    public void k() {
        new AlertDialog.Builder(this).setIcon(17301543).setTitle("Device not supported").setMessage("FFmpeg is not supported on your device").setCancelable(false).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                VideoJoinerActivity.this.finish();
            }
        }).create().show();
    }


    public void onPause() {
        super.onPause();
        if (this.o.isPlaying()) {
            this.o.pause();
            this.e.setBackgroundResource(R.drawable.ic_play);
        } else if (this.p.isPlaying()) {
            this.p.pause();
            this.f.setBackgroundResource(R.drawable.ic_play);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picker, menu);
        return r;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
            return r;
        }
        if (menuItem.getItemId() == R.id.Done) {
            if (this.o.isPlaying()) {
                this.o.pause();
                this.e.setBackgroundResource(R.drawable.ic_play);
            } else if (this.p.isPlaying()) {
                this.p.pause();
                this.f.setBackgroundResource(R.drawable.ic_play);
            }
            h();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void saveData(boolean isCompress) {
        if (isCompress) {
            DefaultVideoStrategy strategy = new DefaultVideoStrategy.Builder()
                    .addResizer(new FractionResizer(0.6F))
                    .addResizer(new AtMostResizer(1000))
                    .build();
            TranscoderOptions.Builder options = Transcoder.into(getPath().getPath().concat("/" + "user_video_" + new SessionManager(VideoJoinerActivity.this).getUser().getData().getUserName() + format + ".mp4"));
            for (int i = 0; i < videoPaths.size(); i++) {
                options.addDataSource(videoPaths.get(i));
            }
            if (audio == null) {
                options.setVideoTrackStrategy(strategy);
            }
            options.setListener(new TranscoderListener() {
                public void onTranscodeProgress(double progress) {
                    showProgressDialog();
                    if (progressBinding != null) {
                        if (audio != null) {
                            progressBinding.progressBar.publishProgress((float) progress / 2);
//                            progressBinding.preparing.setText("Preparing..." + (long) ((progress / 2) * 100) + "%");
                            Log.e("=========Progress_0", "Preparing..." + (long) ((progress / 2) * 100) + "%");
                        } else {
                            progressBinding.progressBar.publishProgress((float) progress);
                            Log.e("=========Progress_1", String.valueOf(progressBinding.progressBar.getProgressPercent()));
//                            progressBinding.preparing.setText("Preparing..." + (long) Math.round(progress * 100) + "%");
                        }
                    }
                    Log.d("TAG", "onTranscodeProgress: " + progress);
                }

                public void onTranscodeCompleted(int successCode) {
                    Log.d("TAG", "onTranscodeCompleted: " + successCode);
                    if (audio != null) {

                        Transcoder.into(getPath().getPath().concat("/finally.mp4"))
                                .addDataSource(TrackType.VIDEO, getPath().getPath().concat("/" + "user_video_" + new SessionManager(VideoJoinerActivity.this).getUser().getData().getUserName() + format + ".mp4"))
//                                .addDataSource(TrackType.AUDIO, getPath().getPath().concat("/recordSound.aac"))
                                .setVideoTrackStrategy(strategy)
                                .setListener(new TranscoderListener() {
                                    @Override
                                    public void onTranscodeProgress(double progress) {
                                        if (progressBinding != null && audio != null) {
                                            progressBinding.progressBar.publishProgress((float) (1 + progress) / 2);
//                                            progressBinding.preparing.setText("Preparing..." + (long) (((1 + progress) / 2) * 100) + "%");
                                            Log.e("===========Progress_2", "Preparing..." + (long) (((1 + progress) / 2) * 100) + "%");
                                        }
                                    }

                                    @Override
                                    public void onTranscodeCompleted(int successCode) {
                                        File thumbFile = new File(getPath(), "/" + "user_thumbnail_" + new SessionManager(VideoJoinerActivity.this).getUser().getData().getUserName() + format + ".jpg");
                                        try {
                                            FileOutputStream stream = new FileOutputStream(thumbFile);

                                            Bitmap bmThumbnail;
                                            bmThumbnail = ThumbnailUtils.createVideoThumbnail(getPath().getPath().concat("/" + "user_video_" + new SessionManager(VideoJoinerActivity.this).getUser().getData().getUserName() + format + ".mp4"),
                                                    MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                                            if (bmThumbnail != null) {
                                                bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                            }
                                            stream.flush();
                                            stream.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        hideProgressDialog();
                                        anotherUri = getPath().getPath().concat("/finally.mp4");
                                        p.setVideoPath(anotherUri);
                                    }

                                    @Override
                                    public void onTranscodeCanceled() {

                                    }

                                    @Override
                                    public void onTranscodeFailed(@NonNull Throwable exception) {

                                    }
                                }).transcode();

                    } else {
                        Log.i("TAG", "onCombineFinished: " + "is original sound");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Track audio;
                                try {
//                                    Movie m1 = MovieCreator.build(getPath().getPath().concat("/" + "user_video_" + new SessionManager(VideoJoinerActivity.this).getUser().getData().getUserName() + format + ".mp4"));
//                                    audio = m1.getTracks().get(1);
//                                    Movie m2 = new Movie();
//                                    m2.addTrack(audio);
//                                    DefaultMp4Builder builder = new DefaultMp4Builder();
//                                    Container stdMp4 = builder.build(m2);
//                                    FileOutputStream fos = new FileOutputStream(getPath().getPath().concat("/originalSound.aac"));
//                                    stdMp4.writeContainer(fos.getChannel());
//                                    fos.close();

                                    File temp = new File(getPath().getPath().concat("/originalSound.aac"));

                                    if (!temp.exists()) {
                                        temp.createNewFile();
                                    }

                                    new AudioExtractor().genVideoUsingMuxer(getPath().getPath().concat("/" + "user_video_" + new SessionManager(VideoJoinerActivity.this).getUser().getData().getUserName() + format + ".mp4"), temp.getAbsolutePath(), -1, -1, true, false);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                                File thumbFile = new File(getPath(), "/" + "user_thumbnail_" + new SessionManager(VideoJoinerActivity.this).getUser().getData().getUserName() + format + ".jpg");
                                try {
                                    FileOutputStream stream = new FileOutputStream(thumbFile);

                                    Bitmap bmThumbnail;
                                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(getPath().getPath().concat("/" + "user_video_" + new SessionManager(VideoJoinerActivity.this).getUser().getData().getUserName() + format + ".mp4"),
                                            MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                                    if (bmThumbnail != null) {
                                        bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                                    }
                                    stream.flush();
                                    stream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Glide.with(VideoJoinerActivity.this)
                                        .asBitmap()
                                        .load(new SessionManager(VideoJoinerActivity.this).getUser().getData().getUserProfile())
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
                                                hideProgressDialog();
                                                CameraX.unbindAll();
                                                anotherUri = getPath().getPath().concat("/" + "user_video_" + new SessionManager(VideoJoinerActivity.this).getUser().getData().getUserName() + format + ".mp4");
                                                p.setVideoPath(anotherUri);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }

                                            @Override
                                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                                super.onLoadFailed(errorDrawable);

                                                hideProgressDialog();
                                                anotherUri = getPath().getPath().concat("/" + "user_video_" + new SessionManager(VideoJoinerActivity.this).getUser().getData().getUserName() + format + ".mp4");

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
                    Log.d("TAG", "onTranscodeCanceled: " + exception);
                }
            }).transcode();

        } else {
            new Thread(() -> {
//                Track audio;
//                try {
//                    Movie m1 = MovieCreator.build(viewModel.videoPaths.get(0));
//                    audio = m1.getTracks().get(1);
//                    Movie m2 = new Movie();
//                    m2.addTrack(audio);
//                    DefaultMp4Builder builder = new DefaultMp4Builder();
//                    Container stdMp4 = builder.build(m2);
//                    FileOutputStream fos = new FileOutputStream(getPath().getPath().concat("/originalSound.aac"));
//                    stdMp4.writeContainer(fos.getChannel());
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                File thumbFile = new File(getPath(), "/" + "user_thumbnail_" + new SessionManager(VideoJoinerActivity.this).getUser().getData().getUserName() + format + ".jpg");
                try {
                    FileOutputStream stream = new FileOutputStream(thumbFile);

                    Bitmap bmThumbnail;
                    bmThumbnail = ThumbnailUtils.createVideoThumbnail(videoPaths.get(0),
                            MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);

                    if (bmThumbnail != null) {
                        bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                    }
                    stream.flush();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(VideoJoinerActivity.this)
                        .asBitmap()
                        .load(new SessionManager(VideoJoinerActivity.this).getUser().getData().getUserProfile())
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

                                anotherUri = videoPaths.get(0);
                                p.setVideoPath(anotherUri);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);

                                anotherUri = videoPaths.get(0);
                            }
                        });
            }).start();
        }

    }

    public File getPath() {
        String state = Environment.getExternalStorageState();
        File filesDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            filesDir = getExternalCacheDir();
        } else {
            // Load another directory, probably local memory
            filesDir = getFilesDir();
        }
        return filesDir;
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

    public void initProgressDialog() {
        mBuilder = new Dialog(this);
        mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        if (mBuilder.getWindow() != null) {
            mBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        progressBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dailog_progress, null, false);
        progressBinding.progressBar.setShader(new int[]{ContextCompat.getColor(this, R.color.colorTheme), ContextCompat.getColor(this, R.color.colorTheme), ContextCompat.getColor(this, R.color.colorTheme)});
//        Glide.with(this)
//                .load(R.drawable.loader_gif)
//                .into(progressBinding.ivParent);

        mBuilder.setContentView(progressBinding.getRoot());
    }
}
