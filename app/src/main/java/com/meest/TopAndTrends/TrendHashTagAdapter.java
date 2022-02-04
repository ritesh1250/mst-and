package com.meest.TopAndTrends;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.TrendHashtagResponse;

import java.util.List;

public class TrendHashTagAdapter extends RecyclerView.Adapter<TrendHashTagAdapter.HashtagViewHolder> {

    private Context context;
    private List<TrendHashtagResponse.Datum> hashtagList;

    public TrendHashTagAdapter(Context context, List<TrendHashtagResponse.Datum> hashtagList) {
        this.context = context;
        this.hashtagList = hashtagList;
    }

    @NonNull
    @Override
    public HashtagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hashtag_itm_list,parent,false);
        return new HashtagViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HashtagViewHolder holder, int position) {
        TrendHashtagResponse.Datum model = hashtagList.get(position);
        holder.hashtagName.setText("#" + model.getHashtag());
        holder.videosCount.setText(String.valueOf(model.getCount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TopHashtagActivity.class);
                intent.putExtra("hashtag", model.getHashtag());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hashtagList.size();
    }

    public class HashtagViewHolder extends RecyclerView.ViewHolder{

        public TextView hashtagName,  videosCount;

        public HashtagViewHolder(@NonNull View itemView) {
            super(itemView);
            hashtagName = itemView.findViewById(R.id.txt_hashtag_name);
            videosCount = itemView.findViewById(R.id.txt_videos);
        }
    }
}

