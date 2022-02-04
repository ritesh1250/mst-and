package com.meest.videomvvmmodule.gallery;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.meest.R;
import com.meest.mediapikcer.GalleryItemDisplayActivity;
import com.meest.mediapikcer.utils.PicHolder;
import com.meest.mediapikcer.utils.PictureFacer;

import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;

/**
 * Author CodeBoy722
 * <p>
 * A RecyclerView Adapter class that's populates a RecyclerView with images from
 * a folder on the device external storage
 */
public class PictureAdapterMedley extends RecyclerView.Adapter<PicHolder> {
    private ArrayList<PictureFacer> pictureList;
    private GalleryItemDisplayActivityMedley pictureContx;
    String call_type, filter;
    int counter = 0;

    public PictureAdapterMedley(ArrayList<PictureFacer> pictureList, GalleryItemDisplayActivityMedley pictureContx, String filter, String call_type) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.filter = filter;
        this.call_type = call_type;
    }

    @NonNull
    @Override
    public PicHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell = inflater.inflate(R.layout.item_gallery_holder_medley, container, false);
        return new PicHolder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull final PicHolder holder, final int position) {
        final PictureFacer imagedata = pictureList.get(position);
        Glide.with(pictureContx)
                .load(imagedata.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.picture);
        setTransitionName(holder.picture, String.valueOf(position) + "_image");
        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filter.equalsIgnoreCase("Image")) {
                    switch (call_type) {
                        case "story":
                            if (holder.checkbox.isChecked()) {
                                --counter;
                                holder.checkbox.setChecked(false);
                                pictureContx.onClick(imagedata, false);
                            } else {
                                if (counter < 1) {
                                    ++counter;
                                    holder.checkbox.setChecked(true);
                                    pictureContx.onClick(imagedata, true);
                                } else {
                                    buildAlertMessageMedia(pictureContx.getString(R.string.Please_select_only_one));
//                                    Utilss.showToast(pictureContx, "Please select only one", R.color.msg_fail);
                                }
                            }


                            break;
                        case "feed":
                            if (holder.checkbox.isChecked()) {
                                holder.checkbox.setChecked(false);
//                                pictureContx.onClick(imagedata, false);
//                                Intent returnIntent = new Intent();
//                                returnIntent.putExtra("result", new Gson().toJson(pictureList));
//                                returnIntent.putExtra("call_type", call_type);
//                                returnIntent.putExtra("filter", filter);
//                                pictureContx.setResult(Activity.RESULT_OK, returnIntent);
//                                pictureContx.finish();
                                --counter;
                            } else {
                                if (counter < 17) {
                                    ++counter;
                                    holder.checkbox.setChecked(true);
                                    pictureContx.onClick(imagedata, true);
                                } else {
                                    buildAlertMessageMedia(pictureContx.getString(R.string.Max_Selection_17_Images));
//                                    Utilss.showToast(pictureContx, "Max Selection 17 Images", R.color.msg_fail);
                                }
                            }
                            break;
                        case "Image":

                            Intent imageReturn = new Intent();
//                            imageReturn.putExtra("result", pictureList.get(position));
                            imageReturn.putExtra("result", pictureList.get(position).getPicturePath());
                            Log.e("REsult", ""+new Gson().toJson(pictureList));
                            imageReturn.putExtra("call_type", "Image");
                            imageReturn.putExtra("position", position);
                            imageReturn.putExtra("filter", "Image");
                            pictureContx.setResult(Activity.RESULT_OK, imageReturn);
                            pictureContx.finish();
                            break;

                    }


                } else {
                    switch (call_type) {
                        case "story":
                            if (holder.checkbox.isChecked()) {
                                --counter;
                                holder.checkbox.setChecked(false);
                                pictureContx.onClick(imagedata, false);
                            } else {

                                if (counter < 1) {
                                    ++counter;
                                    holder.checkbox.setChecked(true);
                                    pictureContx.onClick(imagedata, true);
                                } else {
                                    buildAlertMessageMedia(pictureContx.getString(R.string.Please_select_only_one));
//                                    Utilss.showToast(pictureContx, "Please select only one", R.color.msg_fail);
                                }
                            }
                            break;
                        case "feed":
                            if (holder.checkbox.isChecked()) {
                                holder.checkbox.setChecked(false);
//                                pictureContx.onClick(imagedata, false);
//                                Intent imageReturn = new Intent();
//                                imageReturn.putExtra("result", new Gson().toJson(pictureList));
//                                imageReturn.putExtra("call_type", call_type);
//                                imageReturn.putExtra("filter", filter);
//                                pictureContx.setResult(Activity.RESULT_OK, imageReturn);
//                                pictureContx.finish();
//                                --counter;
                            } else {

                                if (counter < 4) {
                                    ++counter;
                                    holder.checkbox.setChecked(true);
                                    pictureContx.onClick(imagedata, true);
                                } else {
                                    buildAlertMessageMedia(pictureContx.getString(R.string.Max_Selection_4_Images));
//                                    Utilss.showToast(pictureContx, "Max Selection 4 Videos", R.color.msg_fail);
                                }
                            }
                            break;
                        case "video":
                            Intent returnIntent = new Intent();
//                            imageReturn.putExtra("result", pictureList.get(position));
                            returnIntent.putExtra("result", pictureList.get(position).getPicturePath());
                            Log.e("REsult", ""+new Gson().toJson(pictureList));
                            returnIntent.putExtra("call_type", "video");
                            returnIntent.putExtra("position", position);
                            returnIntent.putExtra("filter", "Video");
                            pictureContx.setResult(Activity.RESULT_OK, returnIntent);
                            pictureContx.finish();
                            break;



                    }
                }
//
//                if (holder.checkbox.isChecked()) {
//                    holder.checkbox.setChecked(false);
//                    pictureContx.onClick(imagedata, false);
//                } else {
//                    holder.checkbox.setChecked(true);
//                    pictureContx.onClick(imagedata, true);
//                }
            }
        });
        if (imagedata.getType().equals("Video")) {
            holder.videos_icon.setVisibility(View.VISIBLE);
        } else {
            holder.videos_icon
                    .setVisibility(View.GONE);
        }
    }

    private void buildAlertMessageMedia(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(pictureContx);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {

                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public int getItemCount() {
        return pictureList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}