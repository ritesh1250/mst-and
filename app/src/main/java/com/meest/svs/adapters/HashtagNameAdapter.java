package com.meest.svs.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Activities.PostListByHashTagActvity;
import com.meest.R;
import com.meest.responses.HashtagSearchResponse;
import com.meest.utils.goLiveUtils.CommonUtils;

import java.util.List;

public class HashtagNameAdapter extends RecyclerView.Adapter<HashtagNameAdapter.HashtagViewHolder> {

    private Context context;
    private List<HashtagSearchResponse.Datum> hashtagList;
    int[] androidColors;
    private boolean isShowIcon = false;

    public HashtagNameAdapter(Context context, List<HashtagSearchResponse.Datum> hashtagList) {
        this.context = context;
        this.hashtagList = hashtagList;
        androidColors = context.getResources().getIntArray(R.array.androidcolors);
    }

    public HashtagNameAdapter(Context context, List<HashtagSearchResponse.Datum> hashtagList, boolean isShowIcon) {
        this.context = context;
        this.hashtagList = hashtagList;
        this.isShowIcon = isShowIcon;
        androidColors = context.getResources().getIntArray(R.array.androidcolors);
    }

    @NonNull
    @Override
    public HashtagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hashtag_suggestion_list, parent, false);
        return new HashtagViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HashtagViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        HashtagSearchResponse.Datum model = hashtagList.get(position);

        if (model.getHashtag() != null && !model.getHashtag().equals("")) {
//            holder.hashtagName.setText("#" + model.getHashtag());
            holder.hashtagName.setText(model.getHashtag());

//            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
            int remainder = position % (androidColors.length);

            int randomAndroidColor = androidColors[remainder];

            holder.hashtagName.setBackgroundColor(randomAndroidColor);

//            Drawable unwrappedDrawable =  holder.hashtagName.getBackground();
//            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
//            DrawableCompat.setTint(wrappedDrawable,randomAndroidColor);

            holder.hashtagName.setBackground(CommonUtils.changeDrawableColor(context, R.drawable.hashtaground, randomAndroidColor));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostListByHashTagActvity.class);
                    intent.putExtra("hash_tag_name", model.getHashtag());
                    intent.putExtra("last_color_code", randomAndroidColor);
                    context.startActivity(intent);
                }
            });


            if (isShowIcon) {
                holder.cvHashtagIcon.setVisibility(View.VISIBLE);
                holder.relColor.setBackgroundColor(randomAndroidColor);
                CommonUtils.loadImage(holder.ivHashTagIcon, model.getDisplayPicture(), context);

            } else
                holder.cvHashtagIcon.setVisibility(View.GONE);

        } else {
            holder.cv_main.setVisibility(View.GONE);
        }


    }


    public void updateList(List<HashtagSearchResponse.Datum> hashtagList) {
        this.hashtagList.addAll(hashtagList);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return hashtagList.size() > 0 ? hashtagList.size() : 0;
    }

    public void notifyFeedsDataSetChanged(List<HashtagSearchResponse.Datum> datahashTagList) {
//        this.hashtagList.addAll(datahashTagList);
        notifyItemInserted(hashtagList.size()-1);
    }

    public void update(List<HashtagSearchResponse.Datum>  data) {
        this.hashtagList.clear();
        this.hashtagList.addAll(data);
        notifyDataSetChanged();
    }

    public class HashtagViewHolder extends RecyclerView.ViewHolder {

        public TextView hashtagName;
        public ConstraintLayout cv_main;
        public ImageView ivHashTagIcon;
        public CardView cvHashtagIcon;
        public RelativeLayout relColor;

        public HashtagViewHolder(@NonNull View itemView) {
            super(itemView);
            hashtagName = itemView.findViewById(R.id.tv_hashtag);
            cv_main = itemView.findViewById(R.id.cv_main);

            ivHashTagIcon = itemView.findViewById(R.id.ivHashTagIcon);
            cvHashtagIcon = itemView.findViewById(R.id.cvHashtagIcon);
            relColor = itemView.findViewById(R.id.relColor);

        }
    }
}
