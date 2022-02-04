package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Interfaces.CollectionInterface;
import com.meest.R;
import com.meest.responses.SavedDataResponse;
import com.bumptech.glide.Glide;

import java.util.List;

public class CollectionAdpter extends RecyclerView.Adapter<CollectionAdpter.CollectionViewHolder> {

    private List<SavedDataResponse.Row> collectionList;
    private Context context;
    private CollectionInterface collectionInterface;

    public CollectionAdpter(List<SavedDataResponse.Row> hashtag_profile_List ,
                            Context context, CollectionInterface collectionInterface) {
        collectionList = hashtag_profile_List;
        this.collectionInterface = collectionInterface;
        this.context=context;
    }


    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CollectionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_adapter, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        SavedDataResponse.Row model=collectionList.get(position);

        try {
            if (model.getPost().getPosts().size()>0)
                if (model.getPost().getThumbnail().length() > 0) {
                    Glide.with(context).load(model.getPost().getThumbnail())
                            .placeholder(R.drawable.image_placeholder).into(holder.userCollectionImage);
                } else {
                    Glide.with(context).load(R.drawable.image_placeholder).into(holder.userCollectionImage);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionInterface.collectionClicked(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder {
        ImageView userCollectionImage;


        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            userCollectionImage = itemView.findViewById(R.id.userCollectionImage);



        }
    }

}
