package com.meest.metme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.meest.R;
import com.meest.utils.TouchImageView;
import com.meest.databinding.ActivityChatImageViewBinding;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ChatImageViewActivity extends AppCompatActivity {

    ActivityChatImageViewBinding activityChatImageViewBinding;

    String type;
    //    PlayerView playerView;
    private SimpleExoPlayer player;
    ImageView iv_chat_back;
    TouchImageView iv_chat_images_items, iv_chat_images_item1;
    TextView tv_header_name, tv_dt_foruser;

    VideoView videoView;
    ImageView btnPlayVideo;
    private ProgressDialog pDialog;
    private String iv_url, name, username, dateAndTime, messageId;
    private String existPath;
    private String where = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_image_view);
        if (getIntent() != null) {
            where = getIntent().getStringExtra("whereFrom");
        }
        iv_chat_images_items = findViewById(R.id.iv_chat_images_item);
//        playerView = findViewById(R.id.videoView);
        iv_chat_back = findViewById(R.id.iv_chat_back);
        tv_header_name = findViewById(R.id.tv_header_name);
        tv_dt_foruser = findViewById(R.id.tv_dt_foruser);

        videoView = findViewById(R.id.player_view);
        btnPlayVideo = findViewById(R.id.btnPlayVideo);
        iv_url = getIntent().getStringExtra("iv_url");
        Log.e("TAGGGGGG", "imgae is" + iv_url);
        messageId = getIntent().getStringExtra("messageId");
        name = getIntent().getStringExtra("title");
        username = getIntent().getStringExtra("username");
        dateAndTime = getIntent().getStringExtra("dateAndTime");
        type = getIntent().getStringExtra("type");


        tv_header_name.setText(name);
        tv_dt_foruser.setText(dateAndTime);
        if (type.equalsIgnoreCase("Video")) {
            iv_chat_images_items.setVisibility(View.GONE);
            existPath = getExternalCacheDir() + "/MetMe_" + messageId + "_" + username + ".mp4";
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                        btnPlayVideo.setBackgroundResource(R.drawable.ic_play);
                    } else {
                        videoView.start();
                        btnPlayVideo.setBackgroundResource(0);
                    }
                }
            });
//            File file = new File(existPath);
//            if (file.exists()) {
//                playVideo(Uri.parse(existPath));
//            } else {
//                new DownloadFileFromURL().execute(iv_url);
//            }
            if (where.equalsIgnoreCase("singleMedia"))
                playVideo(Uri.fromFile(new File(iv_url)));
            else {
                File file = new File(existPath);
                if (file.exists()) {
                    playVideo(Uri.parse(existPath));
                } else {
                    new DownloadFileFromURL().execute(iv_url);
                }
            }

        } else {
            iv_chat_images_items.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            showImage(iv_url);
        }


        iv_chat_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null) {
                    player.stop();
                    player.release();
                }
                finish();

            }
        });

    }

    private File directoryName, fileName;

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
            pDialog = new ProgressDialog(ChatImageViewActivity.this);
            pDialog.setMessage(getResources().getString(R.string.please_wait));
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            String state;
            state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                try {
//                    if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT) {
//                        directoryName = new File(Environment.getExternalStorageDirectory().getPath()
//                                + "//Medley_Video");
//                    } else {
//                        directoryName = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES).getPath()
//                                + "//Medley_Video");
//                    }

                    directoryName = new File(String.valueOf(getExternalCacheDir()));
//                    directoryName = new File(Environment.getExternalStorageDirectory(), "Medley_Video");
                    if (!directoryName.exists()) {
                        directoryName.mkdirs();
                    }

                    fileName = new File(directoryName, "MetMe_" + messageId + "_" + username + ".mp4");
                    System.out.println("Downloading");
                    URL url = new URL(f_url[0]);

                    URLConnection conection = url.openConnection();
                    int fileLength = conection.getContentLength();
                    conection.connect();
                    // getting file length
                    int lenghtOfFile = conection.getContentLength();
                    // input stream to read file - with 8k buffer
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    // Output stream to write file
                    OutputStream output = new FileOutputStream(fileName);
                    byte data[] = new byte[1024];
                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // writing data to file
                        publishProgress(String.valueOf(total * 100 / fileLength));
                        output.write(data, 0, count);
                    }
                    // flushing output
                    output.flush();
                    // closing streams
                    output.close();
                    input.close();

                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            } else {
                Toast.makeText(ChatImageViewActivity.this, "Folder Not created", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            System.out.println("Downloaded");
            Uri uri = Uri.fromFile(fileName);
            playVideo(uri);
            pDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pDialog.setProgress(Integer.parseInt(values[0]));
        }
    }

    private void playVideo(Uri iv_url) {
        videoView.setVideoURI(iv_url);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoView.start();
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), "tunemist"));
//        player =   new  SimpleExoPlayer.Builder(this).build();
//
//        DefaultHlsExtractorFactory defaultHlsExtractorFactory = new DefaultHlsExtractorFactory();
//        MediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory)
//                .setExtractorFactory(defaultHlsExtractorFactory)
//                .createMediaSource(Uri.parse(iv_url));
//
//
//        player.prepare(mediaSource);
////        playerView.setPlayer(player);
//        player.setRepeatMode(Player.REPEAT_MODE_ALL);
//        player.setPlayWhenReady(true);
    }

    private void playVideoM3u8(String iv_url) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), Util.getUserAgent(getApplicationContext(), "tunemist"));
        player = new SimpleExoPlayer.Builder(this).build();
        DefaultHlsExtractorFactory defaultHlsExtractorFactory = new DefaultHlsExtractorFactory();
        MediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory)
                .setExtractorFactory(defaultHlsExtractorFactory)
                .createMediaSource(Uri.parse(iv_url));
        player.prepare(mediaSource);
//        playerView.setPlayer(player);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.setPlayWhenReady(true);

    }

    private void showImage(String iv_url) {
        Log.e("TAG", "showImage: " + iv_url);
        //    Uri uri= Uri.fromFile(new File(iv_url));
        Glide.with(ChatImageViewActivity.this).load(iv_url).placeholder(R.drawable.image_placeholder).into(iv_chat_images_items);
    }

}