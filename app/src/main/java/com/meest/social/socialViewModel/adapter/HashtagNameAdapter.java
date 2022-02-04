package com.meest.social.socialViewModel.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Activities.PostListByHashTagActvity;
import com.meest.R;
import com.meest.TopAndTrends.TopHashtagActivity;
import com.meest.databinding.HashtagSuggestionListBinding;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.social.socialViewModel.model.HashtagSearch;
import com.meest.social.socialViewModel.model.HashtagSearchResponse;
import com.meest.social.socialViewModel.model.TopResponse;
import com.meest.utils.goLiveUtils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class HashtagNameAdapter extends RecyclerView.Adapter<HashtagNameAdapter.HashtagViewHolder> {
    private static final String TAG = "HashtagNameAdapter";
    private Context context;
    private List<HashtagSearch.Data.Rows> hashtagList = new ArrayList<>();
    int[] androidColors;
    private boolean isShowIcon = false;

    public HashtagNameAdapter(Context context) {
        this.context = context;
        androidColors = this.context.getResources().getIntArray(R.array.androidcolors);
        this.isShowIcon = true;
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
        HashtagSearch.Data.Rows model = hashtagList.get(position);

        if (model.getHashtag() != null && !model.getHashtag().equals("")) {

            int remainder = position % (androidColors.length);
            int randomAndroidColor = androidColors[remainder];
            holder.binding.tvHashTagName.setBackgroundColor(randomAndroidColor);
            holder.binding.tvHashTagName.setBackground(CommonUtils.changeDrawableColor(context, R.drawable.hashtaground, randomAndroidColor));
            holder.binding.tvHashTagName.setText("#" + model.getHashtag());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, TopHashtagActivity.class);
                    intent.putExtra("hashtag", "#" + model.getHashtag());
                    context.startActivity(intent);
                }
            });


            if (isShowIcon) {
                holder.binding.cvHashtagIcon.setVisibility(View.VISIBLE);
                holder.binding.relColor.setBackgroundColor(randomAndroidColor);
                CommonUtils.loadImage(holder.binding.ivHashTagIcon, model.getDisplayPicture(), context);

            } else
                holder.binding.cvHashtagIcon.setVisibility(View.GONE);

        } else {
            holder.binding.cvMain.setVisibility(View.GONE);
        }


    }

    public void setData(List<HashtagSearch.Data.Rows> list) {
        hashtagList = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<HashtagSearch.Data.Rows> data) {
        for (int i = 0; i < data.size(); i++) {
            hashtagList.add(data.get(i));
            notifyItemInserted(hashtagList.size() - 1);
        }
    }

    @Override
    public int getItemCount() {
        if (hashtagList != null)
            return hashtagList.size();
        else
            return 0;
    }

    public class HashtagViewHolder extends RecyclerView.ViewHolder {

//        public TextView hashtagName;
//        public ConstraintLayout cv_main;
//        public ImageView ivHashTagIcon;
//        public CardView cvHashtagIcon;
//        public RelativeLayout relColor;

        HashtagSuggestionListBinding binding;

        public HashtagViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
//            hashtagName = itemView.findViewById(R.id.tv_hashtag);
//            cv_main = itemView.findViewById(R.id.cv_main);
//
//            ivHashTagIcon = itemView.findViewById(R.id.ivHashTagIcon);
//            cvHashtagIcon = itemView.findViewById(R.id.cvHashtagIcon);
//            relColor = itemView.findViewById(R.id.relColor);

        }
    }
}
