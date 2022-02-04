package com.meest.social.socialViewModel.adapter.search;

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
import com.meest.social.socialViewModel.model.search.HashtagSearchResponse;
import com.meest.utils.goLiveUtils.CommonUtils;

import java.util.List;

public class HashtagNameAdapter extends RecyclerView.Adapter<HashtagNameAdapter.HashtagViewHolder> {

    private Context context;
    private List<HashtagSearchResponse.Datum.Rows> hashtagList;
    int[] androidColors;
    private boolean isShowIcon = false;

    public HashtagNameAdapter(Context context, List<HashtagSearchResponse.Datum.Rows> hashtagList) {
        this.context = context;
        this.hashtagList = hashtagList;
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
        HashtagSearchResponse.Datum.Rows model = hashtagList.get(position);

        if (model.getHashtag() != null && !model.getHashtag().equals("")) {
            holder.binding.tvHashTagName.setText("#" + model.getHashtag());

            int remainder = position % (androidColors.length);

            int randomAndroidColor = androidColors[remainder];

            holder.binding.tvHashTagName.setBackgroundColor(randomAndroidColor);

            holder.binding.tvHashTagName.setBackground(CommonUtils.changeDrawableColor(context, R.drawable.hashtaground, randomAndroidColor));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TopHashtagActivity.class);
                    intent.putExtra("hashtag", "#"+model.getHashtag());
                    context.startActivity(intent);
                }
            });

        } else {
            holder.binding.cvMain.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return hashtagList.size() > 0 ? hashtagList.size() : 0;
    }

    public void notifyFeedsDataSetChanged(List<HashtagSearchResponse.Datum.Rows> datahashTagList) {
        notifyItemInserted(datahashTagList.size()-1);
    }

    public void update(List<HashtagSearchResponse.Datum.Rows>  data) {
        this.hashtagList.clear();
        this.hashtagList.addAll(data);
        notifyDataSetChanged();
    }

    public class HashtagViewHolder extends RecyclerView.ViewHolder {
        HashtagSuggestionListBinding binding;
        public HashtagViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

        }
    }
}
