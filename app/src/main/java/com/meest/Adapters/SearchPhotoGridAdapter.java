package com.meest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.R;
import com.meest.social.socialViewModel.model.TopResponse;

import java.util.List;

public class SearchPhotoGridAdapter extends RecyclerView.Adapter<SearchPhotoGridAdapter.hashtag_profileVIewHolder> {

    private List<TopResponse.Datum>  postList;
    private Context context;

    public SearchPhotoGridAdapter(Context context, List<TopResponse.Datum>  postList) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchPhotoGridAdapter.hashtag_profileVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_post_grid, parent, false);
        SearchPhotoGridAdapter.hashtag_profileVIewHolder HPVH = new SearchPhotoGridAdapter.hashtag_profileVIewHolder(v);
        return HPVH;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchPhotoGridAdapter.hashtag_profileVIewHolder holder, int position) {
        TopResponse.Datum model = postList.get(position);

        holder.cardSearch.setVisibility(View.INVISIBLE);

        if (model.getThumbnail().length() > 0) {

            Glide.with(context).load(model.getThumbnail())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.cardSearch.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .placeholder(R.drawable.image_placeholder).into(holder.img_post);
        } else {
            Glide.with(context).load(R.drawable.image_placeholder)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.cardSearch.setVisibility(View.VISIBLE);
                            return false;
                        }
                    }).into(holder.img_post);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotificationSocialFeedActivity.class);
                intent.putExtra("userId", model.getUserId());
                intent.putExtra("postId", model.getId());
                ((AppCompatActivity) context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class hashtag_profileVIewHolder extends RecyclerView.ViewHolder {

        private ImageView img_post;

        CardView cardSearch;

        public hashtag_profileVIewHolder(@NonNull View itemView) {
            super(itemView);
            img_post = itemView.findViewById(R.id.img_post);
            cardSearch = itemView.findViewById(R.id.card_search);
        }
    }

    public void notifyFeedsDataSetChanged(List<TopResponse.Datum>  postList) {
        this.postList = postList;
        notifyDataSetChanged();
    }


}
