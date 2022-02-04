package com.meest.metme.camera2.videoEditing;/*
package com.meest.metme.camera2.videoEditing;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

//import com.google.android.gms.ads.AdRequest.Builder;
//import com.google.android.gms.ads.AdView;
import com.meest.R;
import com.meest.metme.camera2.videoEditing.listmusicandmymusic.ListMusicAndMyMusicActivity;
import com.meest.metme.camera2.videoEditing.listvideowithmymusic.ListVideoAndMyMusicActivity;

import java.io.File;
import java.util.concurrent.TimeUnit;

@SuppressLint({"WrongConstant"})
public class AudioPlayer extends AppCompatActivity implements OnSeekBarChangeListener {
    static final boolean BOOLEN = true;
    Bundle a;
    ImageView b;
    ImageView c;
    int d = 0;
    Handler e = new Handler();
    boolean f = false;
    boolean g = false;
    SeekBar h;
    Uri i;
    TextView j;
    TextView k;
    TextView l;
    TextView m;
    String n = "";
    Runnable o = new Runnable() {
        public void run() {
            if (com.meest.metme.camera2.videoEditing.AudioPlayer.this.r.isPlaying()) {
                int currentPosition = com.meest.metme.camera2.videoEditing.AudioPlayer.this.r.getCurrentPosition();
                com.meest.metme.camera2.videoEditing.AudioPlayer.this.h.setProgress(currentPosition);
                com.meest.metme.camera2.videoEditing.AudioPlayer.this.j.setText(com.meest.metme.camera2.videoEditing.AudioPlayer.formatTimeUnit((long) currentPosition));
                if (currentPosition == com.meest.metme.camera2.videoEditing.AudioPlayer.this.d) {
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.h.setProgress(0);
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.j.setText(zero);
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.e.removeCallbacks(com.meest.metme.camera2.videoEditing.AudioPlayer.this.o);
                    return;
                }
                com.meest.metme.camera2.videoEditing.AudioPlayer.this.e.postDelayed(com.meest.metme.camera2.videoEditing.AudioPlayer.this.o, 200);
                return;
            }
            com.meest.metme.camera2.videoEditing.AudioPlayer.this.h.setProgress(com.meest.metme.camera2.videoEditing.AudioPlayer.this.d);
            com.meest.metme.camera2.videoEditing.AudioPlayer.this.j.setText(com.meest.metme.camera2.videoEditing.AudioPlayer.formatTimeUnit((long) com.meest.metme.camera2.videoEditing.AudioPlayer.this.d));
            com.meest.metme.camera2.videoEditing.AudioPlayer.this.e.removeCallbacks(com.meest.metme.camera2.videoEditing.AudioPlayer.this.o);
        }
    };
    OnClickListener p = new OnClickListener() {
        @Override
        public void onClick(View view) {
            StringBuilder sb = new StringBuilder();
            sb.append("play status ");
            sb.append(com.meest.metme.camera2.videoEditing.AudioPlayer.this.f);
            Log.e("", sb.toString());
            if (com.meest.metme.camera2.videoEditing.AudioPlayer.this.f) {
                try {
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.r.pause();
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.e.removeCallbacks(com.meest.metme.camera2.videoEditing.AudioPlayer.this.o);
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.c.setBackgroundResource(R.drawable.ic_edit_play);
                } catch (IllegalStateException e1) {
                    e1.printStackTrace();
                }
            } else {
                try {
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.r.seekTo(com.meest.metme.camera2.videoEditing.AudioPlayer.this.h.getProgress());
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.r.start();
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.e.postDelayed(com.meest.metme.camera2.videoEditing.AudioPlayer.this.o, 200);
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.b.setVisibility(0);
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.c.setBackgroundResource(R.drawable.ic_edit_pause);
                } catch (IllegalStateException e2) {
                    e2.printStackTrace();
                }
            }
            com.meest.metme.camera2.videoEditing.AudioPlayer.this.f ^= com.meest.metme.camera2.videoEditing.AudioPlayer.BOOLEN;
        }
    };

    MediaPlayer r;
    private String zero = "00:00";

    public void onStartTrackingTouch(SeekBar seekBar) {
        //add
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        //add
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.audioplayer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(getString(R.string.My_Audio));
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (BOOLEN || supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(BOOLEN);
            supportActionBar.setDisplayShowTitleEnabled(false);
            this.a = getIntent().getExtras();
            if (this.a != null) {
                this.n = this.a.getString("song");
                this.g = this.a.getBoolean("isfrom", false);
            }
            Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            intent.setData(Uri.fromFile(new File(this.n)));
            sendBroadcast(intent);
            thumbAudio(this.n);
            this.m = (TextView) findViewById(R.id.dur);
            this.l = (TextView) findViewById(R.id.Filename);
            this.l.setText(new File(this.n).getName());
            this.b = (ImageView) findViewById(R.id.iv_Thumb);
            this.h = (SeekBar) findViewById(R.id.sbVideo);
            this.h.setOnSeekBarChangeListener(this);
            this.r = MediaPlayer.create(this, Uri.parse(this.n));
            this.j = (TextView) findViewById(R.id.tvStartVideo);
            this.k = (TextView) findViewById(R.id.tvEndVideo);
            this.c = (ImageView) findViewById(R.id.btnPlayVideo);
            this.r.setOnErrorListener(new OnErrorListener() {
                public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                    Toast.makeText(com.meest.metme.camera2.videoEditing.AudioPlayer.this.getApplicationContext(), R.string.Audio_Player_Not_Supproting, 0).show();
                    return com.meest.metme.camera2.videoEditing.AudioPlayer.BOOLEN;
                }
            });
            this.r.setOnPreparedListener(new OnPreparedListener() {
                public void onPrepared(MediaPlayer mediaPlayer) {
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.d = com.meest.metme.camera2.videoEditing.AudioPlayer.this.r.getDuration();
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.h.setMax(com.meest.metme.camera2.videoEditing.AudioPlayer.this.d);
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.j.setText(zero);
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.k.setText(com.meest.metme.camera2.videoEditing.AudioPlayer.formatTimeUnit((long) com.meest.metme.camera2.videoEditing.AudioPlayer.this.d));
                    TextView textView = com.meest.metme.camera2.videoEditing.AudioPlayer.this.m;
                    StringBuilder sb = new StringBuilder();
                    sb.append("duration : ");
                    sb.append(VideoPlayer.formatTimeUnit((long) com.meest.metme.camera2.videoEditing.AudioPlayer.this.d));
                    textView.setText(sb.toString());
                }
            });
            this.r.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.c.setBackgroundResource(R.drawable.ic_edit_play);
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.r.seekTo(0);
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.h.setProgress(0);
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.j.setText(zero);
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.e.removeCallbacks(com.meest.metme.camera2.videoEditing.AudioPlayer.this.o);
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.f ^= com.meest.metme.camera2.videoEditing.AudioPlayer.BOOLEN;
                }
            });
            this.c.setOnClickListener(this.p);
//            AdView s = (AdView) findViewById(R.id.banner_AdView);
//            s.loadAd(new Builder().build());
//            if (isOnline()) {
//                s.setVisibility(0);
//            } else {
//                s.setVisibility(8);
//            }
        } else {
            throw new AssertionError();
        }
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() == null || !connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting()) {
            return false;
        }
        return BOOLEN;
    }

    public void onProgressChanged(SeekBar seekBar, int i2, boolean z) {
        if (z) {
            this.r.seekTo(i2);
            this.j.setText(formatTimeUnit((long) i2));
        }
    }

    @SuppressLint({"NewApi"})
    public static String formatTimeUnit(long j2) {
        return String.format("%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j2)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j2) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j2)))});
    }

    public void thumbAudio(String str) {
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        String[] strArr = {"_data", "_id"};
        StringBuilder sb = new StringBuilder();
        sb.append("%");
        sb.append(str);
        sb.append("%");
        Cursor managedQuery = managedQuery(uri, strArr, "_data  like ?", new String[]{sb.toString()}, " _id DESC");
        int count = managedQuery.getCount();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("count");
        sb2.append(count);
        Log.e("", sb2.toString());
        if (count > 0) {
            managedQuery.moveToFirst();
            for (int i2 = 0; i2 < count; i2++) {
                Uri withAppendedPath = Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, getLong(managedQuery));
                StringBuilder sb3 = new StringBuilder();
                sb3.append("===******* uri ===");
                sb3.append(withAppendedPath);
                Log.e("", sb3.toString());
                this.i = withAppendedPath;
                managedQuery.moveToNext();
            }
        }
    }

    public String getLong(Cursor cursor) {
        return String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow("_id")));
    }

    public void delete() {
        new AlertDialog.Builder(this).setTitle(R.string.areSure).setMessage(R.string.delete_Ringtone).setPositiveButton(getString(R.string.Delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                File file = new File(com.meest.metme.camera2.videoEditing.AudioPlayer.this.n);
                if (file.exists()) {
                    if (file.delete()) {
                        //add
                    }
                    try {
                        Uri contentUriForPath = Media.getContentUriForPath(com.meest.metme.camera2.videoEditing.AudioPlayer.this.n);
                        StringBuilder sb = new StringBuilder();
                        sb.append("=====Enter ====");
                        sb.append(contentUriForPath);
                        Log.e("", sb.toString());
                        ContentResolver contentResolver = com.meest.metme.camera2.videoEditing.AudioPlayer.this.getContentResolver();
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("_data=\"");
                        sb2.append(com.meest.metme.camera2.videoEditing.AudioPlayer.this.n);
                        sb2.append("\"");
                        contentResolver.delete(contentUriForPath, sb2.toString(), null);
                    } catch (Exception unused) {
                        //add
                    }
                    com.meest.metme.camera2.videoEditing.AudioPlayer.this.onBackPressed();
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //add
            }
        }).setCancelable(BOOLEN).show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        getWindow().clearFlags(128);
        super.onStop();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Helper.ModuleId == 3) {
            Intent intent = new Intent(this, ListVideoAndMyMusicActivity.class);
            intent.setFlags(67108864);
            startActivity(intent);
            finish();
        } else if (Helper.ModuleId == 18) {
            Intent intent2 = new Intent(this, ListMusicAndMyMusicActivity.class);
            intent2.setFlags(67108864);
            startActivity(intent2);
            finish();
        } else if (Helper.ModuleId == 19) {
            Intent intent3 = new Intent(this, ListMusicAndMyMusicActivity.class);
            intent3.setFlags(67108864);
            startActivity(intent3);
            finish();
        } else if (Helper.ModuleId == 20) {
            Intent intent4 = new Intent(this, ListMusicAndMyMusicActivity.class);
            intent4.setFlags(67108864);
            startActivity(intent4);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_deleteshare, menu);
        return BOOLEN;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            if (this.r.isPlaying()) {
                this.r.stop();
            }
            super.onBackPressed();
            if (this.g) {
                try {
                    onBackPressed();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            finish();
            return BOOLEN;
        }
        if (menuItem.getItemId() == R.id.Delete) {
            delete();
        } else if (menuItem.getItemId() == R.id.Share) {
            try {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("audio/*");
                intent.putExtra("android.intent.extra.STREAM", this.i);
                startActivity(Intent.createChooser(intent, "Share File"));
            } catch (Exception unused) {
                //add
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
*/
