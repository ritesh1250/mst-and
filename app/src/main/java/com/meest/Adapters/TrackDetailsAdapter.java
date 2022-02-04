package com.meest.Adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.Activities.VideoPost.BaseCameraActivity;
import com.meest.R;
import com.meest.responses.AudioResponse;
import com.meest.meestbhart.utilss.Utilss;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;

public class TrackDetailsAdapter extends RecyclerView.Adapter<TrackDetailsAdapter.TrackDetailsViewHolder> {

    private Context context;
    private AudioResponse audioResponse;
    private ImageView oldCirclePause;
    private Button oldUseButton;
    private ProgressBar oldProgressBar;
    private BottomSheetDialog currentDialog;

    //getting the context and product list with constructor
    public TrackDetailsAdapter(Context context, AudioResponse audioResponse,
                               BottomSheetDialog currentDialog) {
        this.context = context;
        this.currentDialog = currentDialog;
        this.audioResponse = audioResponse;
    }

    @Override
    public TrackDetailsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = inflater.inflate(R.layout.item_track_details, null);
        view.setLayoutParams(layoutParams);
        return new TrackDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TrackDetailsViewHolder holder, int position) {
        final AudioResponse.Row model = audioResponse.getData().getRows().get(position);
        Handler handler = new Handler(Looper.myLooper());

        holder.txt_title_track.setText(model.getDescription());

        holder.imv_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getAudioURL() == null || model.getAudioURL().length() == 0) {
                    Utilss.showToast(context, context.getString(R.string.Can_play_this_audio_try_another_one), R.color.msg_fail);
                    return;
                }

                if (oldCirclePause != null) {
                    if (oldCirclePause == holder.img_circle_pause) {
                        if (BaseCameraActivity.audioPlayer != null) {
                            if (BaseCameraActivity.audioPlayer.isPlaying()) {
                                BaseCameraActivity.audioPlayer.pause();
                                holder.img_circle_pause.setVisibility(View.GONE);
                            } else {
                                BaseCameraActivity.audioPlayer.start();
                                holder.img_circle_pause.setVisibility(View.VISIBLE);
                            }
                        } else {
                            // playing audio
                            holder.music_progress.setVisibility(View.VISIBLE);
                            holder.img_circle_pause.setVisibility(View.GONE);
                            // playing music after 10 sec delay
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    playMusic(model.getAudioURL(), holder.music_progress, holder.img_circle_pause);
                                }
                            }, 200);
                        }
                    } else {
                        oldUseButton.setVisibility(View.GONE);
                        oldCirclePause.setVisibility(View.GONE);
                        oldProgressBar.setVisibility(View.GONE);

                        // stopping if playing old audio
                        if (BaseCameraActivity.audioPlayer != null && BaseCameraActivity.audioPlayer.isPlaying()) {
                            BaseCameraActivity.audioPlayer.stop();
                        }

                        // playing audio
                        holder.music_progress.setVisibility(View.VISIBLE);
                        holder.img_circle_pause.setVisibility(View.GONE);
                        // playing music after 10 sec delay
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playMusic(model.getAudioURL(), holder.music_progress, holder.img_circle_pause);
                            }
                        }, 200);
                    }
                } else {
                    // stopping if playing old audio
                    if (BaseCameraActivity.audioPlayer != null && BaseCameraActivity.audioPlayer.isPlaying()) {
                        BaseCameraActivity.audioPlayer.stop();
                    }

                    // playing audio
                    holder.music_progress.setVisibility(View.VISIBLE);
                    holder.img_circle_pause.setVisibility(View.GONE);
                    // playing music after 10 sec delay
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playMusic(model.getAudioURL(), holder.music_progress, holder.img_circle_pause);
                        }
                    }, 200);
                }
                // changing icon
                holder.audio_use.setVisibility(View.VISIBLE);
                // saving current views
                oldCirclePause = holder.img_circle_pause;
                oldUseButton = holder.audio_use;
                oldProgressBar = holder.music_progress;
            }
        });

        holder.audio_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismissing current dialog
                currentDialog.dismiss();

                BaseCameraActivity.audioUrl = model.getAudioURL();
                BaseCameraActivity.audioId = model.getId();
                BaseCameraActivity.selectedAudioType = "api";
            }
        });
    }

    private void playMusic(String audioUrl, ProgressBar progressBar, ImageView pause) {
        try {
            if (BaseCameraActivity.audioPlayer != null) {
                BaseCameraActivity.audioPlayer.release();
            }
            BaseCameraActivity.audioPlayer = new MediaPlayer();
            BaseCameraActivity.audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            BaseCameraActivity.audioPlayer.setDataSource(context, Uri.parse(audioUrl));
            BaseCameraActivity.audioPlayer.setLooping(false);
            BaseCameraActivity.audioPlayer.prepare();
            BaseCameraActivity.audioPlayer.start();
            BaseCameraActivity.audioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Log.v("harsh", "prepared");
                    progressBar.setVisibility(View.GONE);
                    pause.setVisibility(View.VISIBLE);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Utilss.showToast(context,context.getString(R.string.Server_Error), R.color.msg_fail);
        }
    }

    @Override
    public int getItemCount() {
        return audioResponse.getData().getRows().size();
    }


    class TrackDetailsViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title_track, txt_singer;
        ImageView imv_track, img_circle_pause;
        Button audio_use;
        ProgressBar music_progress;

        public TrackDetailsViewHolder(View itemView) {
            super(itemView);
            txt_singer = itemView.findViewById(R.id.txt_singer);
            txt_title_track = itemView.findViewById(R.id.txt_title_track);
            imv_track = itemView.findViewById(R.id.imv_track);
            img_circle_pause = itemView.findViewById(R.id.img_circle_pause);
            audio_use = itemView.findViewById(R.id.audio_use);
            music_progress = itemView.findViewById(R.id.music_progress);
        }
    }
}

