package com.meest.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;

import java.util.ArrayList;

public class OwnStatusAdapter  extends RecyclerView.Adapter<OwnStatusAdapter.hashtag_profileVIewHolder> {

    private ArrayList<String> Hashtag_profile_List;


    public OwnStatusAdapter(ArrayList<String> hashtag_profile_List) {
        Hashtag_profile_List = hashtag_profile_List;
    }

    @NonNull
    @Override
    public OwnStatusAdapter.hashtag_profileVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_fragment_image_post,parent,false);
        OwnStatusAdapter.hashtag_profileVIewHolder HPVH = new OwnStatusAdapter.hashtag_profileVIewHolder(v);
        return HPVH;

    }


    @Override
    public void onBindViewHolder(@NonNull OwnStatusAdapter.hashtag_profileVIewHolder holder, int position) {

        Bitmap myBitmap = BitmapFactory.decodeFile(Hashtag_profile_List.get(position));


        holder.img_post.setImageBitmap(myBitmap);

    }

    @Override
    public int getItemCount() {
        return Hashtag_profile_List.size();
    }

    public class hashtag_profileVIewHolder extends RecyclerView.ViewHolder{

        ImageView img_post;

        public hashtag_profileVIewHolder(@NonNull View itemView) {
            super(itemView);
            img_post = itemView.findViewById(R.id.img_post);
        }
    }

}
