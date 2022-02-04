package com.meest.svs.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.TopAndTrends.TopHashtagActivity;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.responses.HashtagSearchResponse;
import com.meest.utils.goLiveUtils.CommonUtils;

import java.util.List;

public class HashtagDataAdapter extends RecyclerView.Adapter<HashtagDataAdapter.HashtagViewHolder> {

    private Context context;
    private List<HashtagSearchResponse.Datum> hashtagList;

    public HashtagDataAdapter(Context context, List<HashtagSearchResponse.Datum> hashtagList) {
        this.context = context;
        this.hashtagList = hashtagList;
    }

    @NonNull
    @Override
    public HashtagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_trending_item,parent,false);
        return new HashtagViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HashtagViewHolder holder, int position) {
        HashtagSearchResponse.Datum model = hashtagList.get(position);
       // Glide.with(context).load(model.getDisplayPicture()).into();

        CommonUtils.loadImage(holder.vhc_image,model.getDisplayPicture(),context,R.drawable.image_placeholder);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getCount()>1) {
                    Intent intent = new Intent(context, TopHashtagActivity.class);
                    intent.putExtra("hashtag", "#" + model.getHashtag());
                    context.startActivity(intent);
                }else {
                    Utilss.showToast(context,context.getResources().getString(R.string.there_is_no_Hashtag),context.getResources().getColor(R.color.social_background_blue));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return hashtagList.size();
    }

    public void notifyFeedsDataSetChanged(List<HashtagSearchResponse.Datum> datahashTagListimage) {
//        this.hashtagList.addAll(datahashTagListimage);
        notifyItemInserted(hashtagList.size()-1);
    }
    public void update(List<HashtagSearchResponse.Datum>  data) {
        this.hashtagList.clear();
        this.hashtagList.addAll(data);
        notifyDataSetChanged();
    }

    public class HashtagViewHolder extends RecyclerView.ViewHolder{

        public ImageView vhc_image;

        public HashtagViewHolder(@NonNull View itemView) {
            super(itemView);
            vhc_image = itemView.findViewById(R.id.vhc_image);
        }
    }


}
