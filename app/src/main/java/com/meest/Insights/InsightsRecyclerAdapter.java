package com.meest.Insights;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.UserInsightsResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

public class InsightsRecyclerAdapter extends RecyclerView.Adapter<InsightsRecyclerAdapter.EngagementViewHolder> {

    //this context we will use to inflate the layout
    private Activity mCtx;
    //we are storing all the products in a list
    private List<UserInsightsResponse.Row> engagementList;

    //getting the context and product list with constructor
    public InsightsRecyclerAdapter(Activity mCtx, List<UserInsightsResponse.Row> engagementList) {
        this.mCtx = mCtx;
        this.engagementList = engagementList;
    }

    @Override
    public EngagementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_layout, null);
        return new EngagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EngagementViewHolder holder, int position) {
        UserInsightsResponse.Row rowData = engagementList.get(position);
      //  holder.textViewTitle.setText(rowData.getCaption());
        String test = null;
        try {
            test = URLDecoder.decode(rowData.getCaption(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        holder.textViewTitle.setText(test);

        holder.textViewDate.setText(rowData.getCreatedAt());
        int totalEngagement = rowData.getShareCount() +
                rowData.getCommentCounts() +
                rowData.getPostSaveCounts() +
                rowData.getLikeCounts() +
                rowData.getDislikeCounts() +
                rowData.getFollowersCount();
        holder.engagementCountTv.setText(totalEngagement + "");
        holder.peopleReachedCountTV.setText(rowData.getPeopleReached() + "");
        holder.peopleCountProgressBar.setProgress(rowData.getPeopleReached());
        holder.engagementCountProgressBar.setProgress(totalEngagement);
        holder.engagementCountProgressBar.setMax(rowData.getTotalUsers());
        holder.peopleCountProgressBar.setMax(rowData.getTotalUsers());
        try {
            Glide.with(mCtx).load(rowData.getPosts().get(0).getPost())
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .error(R.drawable.placeholder)).into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, ContentDetails.class)
                        .putExtra("data", rowData);
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return engagementList.size();
    }


    class EngagementViewHolder extends RecyclerView.ViewHolder {

        EmojiconTextView textViewTitle;
        TextView  engagementCountTv, peopleReachedCountTV, textViewDate, tvheading;
        ImageView imageView, infoIcon;
        ProgressBar peopleCountProgressBar, engagementCountProgressBar;

        public void showDialog() {
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                LayoutInflater inflater = mCtx.getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.alert_layout, null);
                //  builder.setPositiveButton("OK", null);
                builder.setView(dialogLayout);
                builder.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public EngagementViewHolder(View itemView) {
            super(itemView);
            infoIcon = itemView.findViewById(R.id.infoIcon);
            peopleCountProgressBar = itemView.findViewById(R.id.seekBarOne);
            engagementCountProgressBar = itemView.findViewById(R.id.seekBarTwo);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            engagementCountTv = itemView.findViewById(R.id.engagementCountTv);
            peopleReachedCountTV = itemView.findViewById(R.id.peopleReachedCountTV);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            tvheading = itemView.findViewById(R.id.tvheading);
            imageView = itemView.findViewById(R.id.imageView);
            infoIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
        }
    }
}
