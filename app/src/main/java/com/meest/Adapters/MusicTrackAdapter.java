package com.meest.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Activities.VideoPost.BaseCameraActivity;
import com.meest.R;
import com.meest.responses.AudioCategoryResponse;
import com.meest.responses.AudioResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MusicTrackAdapter extends RecyclerView.Adapter<MusicTrackAdapter.TrackViewHolder> {


    private Context context;
    private AudioCategoryResponse audioCategoryResponse;
    private BottomSheetDialog currentDialog;

    //getting the context and product list with constructor
    public MusicTrackAdapter(Context context, AudioCategoryResponse audioCategoryResponse,
                             BottomSheetDialog currentDialog) {
        this.context = context;
        this.currentDialog = currentDialog;
        this.audioCategoryResponse = audioCategoryResponse;
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = inflater.inflate(R.layout.layout_music_track_item, null);
        view.setLayoutParams(layoutParams);
        return new TrackViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final TrackViewHolder holder, int position) {
        //getting the product of the specified position
        final AudioCategoryResponse.Row model = audioCategoryResponse.getData().getRows().get(position);

        //binding the data with the viewholder views
        holder.txt_dreamout.setText(model.getTitle());
        holder.track_number.setText(model.getDescription());

        //  holder.img_icon_track_category.setImageDrawable(mCtx.getResources().getDrawable(music_track.getIcon()));
        Glide.with(context)
                .load(model.getIcon())
                .into(holder.img_icon_track_category);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> header = new HashMap<>();
                header.put("Accept", "application/json");
                header.put("Content-Type", "application/json");
                header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                // body
                HashMap<String, String> body = new HashMap<>();
                body.put("catagoryId", model.getId());


                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<AudioResponse> call = webApi.getAudioByCategory(header, body);
//                Call<AudioResponse> call = webApi.getAudioByCategory(header);
                call.enqueue(new Callback<AudioResponse>() {
                    @Override
                    public void onResponse(Call<AudioResponse> call, Response<AudioResponse> response) {
                        if (response.code() == 200 && response.body().getSuccess()) {
                            showMusicDialog(response.body(), model.getIcon(), model.getTitle());
                        } else {
                            Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<AudioResponse> call, Throwable t) {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }

                });
            }
        });


    }

    @SuppressLint("SetTextI18n")
    public void showMusicDialog(AudioResponse response, String image, String categoryTitle) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_track_details, null);

        ImageView image_trackBackground = view.findViewById(R.id.image_trackBackground);
        TextView txt_category_name = view.findViewById(R.id.txt_category_name);
        TextView txt_track_num = view.findViewById(R.id.txt_track_num);
        txt_category_name.setText(categoryTitle);
        txt_track_num.setText(response.getData().getCount() + " Tracks");

        Glide.with(context)
                .load(image)
                .into(image_trackBackground);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_track_name_details);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);
        dialog.show();

        //creating recyclerview adapter
        TrackDetailsAdapter adapter = new TrackDetailsAdapter(context, response, dialog);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // dismissing current dialog as well
                currentDialog.dismiss();

                if (BaseCameraActivity.audioPlayer != null && BaseCameraActivity.audioPlayer.isPlaying()) {
                    BaseCameraActivity.audioPlayer.pause();
                }
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // dismissing current dialog as well
                currentDialog.dismiss();

                if (BaseCameraActivity.audioPlayer != null && BaseCameraActivity.audioPlayer.isPlaying()) {
                    BaseCameraActivity.audioPlayer.pause();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return audioCategoryResponse.getData().getRows().size();
    }


    class TrackViewHolder extends RecyclerView.ViewHolder {

        ImageView img_icon_track_category;
        TextView txt_dreamout, track_number;

        public TrackViewHolder(View itemView) {
            super(itemView);

            txt_dreamout = itemView.findViewById(R.id.txt_dreamout);
            track_number = itemView.findViewById(R.id.track_number);
            img_icon_track_category = itemView.findViewById(R.id.img_icon_track_category);


        }
    }
}
