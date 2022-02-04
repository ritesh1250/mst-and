package com.meest.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.meest.Interfaces.CreateTextInterface;
import com.meest.R;
import com.meest.responses.TextPostImageBackgroundResponse;

public class GradientAdapter extends RecyclerView.Adapter<GradientAdapter.GradientHolder> {

    private final Context context;
    private TextPostImageBackgroundResponse imageBackgroundResponse;
    private CreateTextInterface createTextInterface;

    public GradientAdapter(Context context, TextPostImageBackgroundResponse imageBackgroundResponse,
                           CreateTextInterface createTextInterface) {
        this.context = context;
        this.imageBackgroundResponse = imageBackgroundResponse;
        this.createTextInterface = createTextInterface;
    }

    @NonNull
    @Override
    public GradientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_text_bg_images_post,parent,false);
        return new GradientHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GradientHolder holder, final int position) {

        Glide.with(context)
                .load(imageBackgroundResponse.getData().get(position).getBackgroundImage())
                .error(R.drawable.meest_logo)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.img_bg);

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

    public class GradientHolder extends RecyclerView.ViewHolder {

        ImageView img_bg;
        ProgressBar progressBar;

        public GradientHolder(@NonNull View itemView) {
            super(itemView);
            img_bg = itemView.findViewById(R.id.img_bg);
            progressBar = itemView.findViewById(R.id.progress_placeholder);
        }
    }

}
