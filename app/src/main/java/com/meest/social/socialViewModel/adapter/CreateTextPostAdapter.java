package com.meest.social.socialViewModel.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.meest.Interfaces.CreateTextInterface;
import com.meest.R;
import com.meest.databinding.CreateTextBgImagesPostBinding;
import com.meest.responses.TextPostImageBackgroundResponse;

public class CreateTextPostAdapter extends RecyclerView.Adapter<CreateTextPostAdapter.SeeMoreViewHolder> {

    private final Context context;
    private TextPostImageBackgroundResponse imageBackgroundResponse;
    private CreateTextInterface createTextInterface;

    public CreateTextPostAdapter(Context context, TextPostImageBackgroundResponse imageBackgroundResponse,
                                 CreateTextInterface createTextInterface) {
        this.context = context;
        this.imageBackgroundResponse = imageBackgroundResponse;
        this.createTextInterface = createTextInterface;
    }


    @NonNull
    @Override
    public SeeMoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_text_bg_images_post,parent,false);
        return new SeeMoreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SeeMoreViewHolder holder, int position) {

        Glide.with(context)
                .load(imageBackgroundResponse.getData().get(position).getBackgroundImage())
                .error(R.drawable.placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.binding.progressPlaceholder.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.binding.imgBg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTextInterface.backgroundSelected(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageBackgroundResponse.getData().size();
    }

    public class SeeMoreViewHolder extends RecyclerView.ViewHolder{

        CreateTextBgImagesPostBinding binding;

        public SeeMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
