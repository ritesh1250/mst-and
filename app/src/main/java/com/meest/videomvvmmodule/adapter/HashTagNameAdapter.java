package com.meest.videomvvmmodule.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemHashtagNameBinding;
import com.meest.videomvvmmodule.model.Explore;
import com.meest.videomvvmmodule.view.search.HashTagActivity;

import java.util.ArrayList;
import java.util.List;

public class HashTagNameAdapter extends RecyclerView.Adapter<HashTagNameAdapter.HashTagViewHolder> {

    private List<Explore.Data> mList = new ArrayList<>();
//    static  String[] colors = new String[] {"#F54051","#DF1D79","#0A23F9","#CF1100","#7F1BB9","#ffff4444","#FFC36A"};
    static int previousColor;
    public List<Explore.Data> getmList() {
        return mList;
    }

    public void setmList(List<Explore.Data> mList) {
        this.mList = mList;
    }


    @NonNull
    @Override
    public HashTagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hashtag_name, parent, false);
        return new HashTagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HashTagViewHolder holder, int position) {
        holder.setModel(mList.get(position));
//        holder.hashTagBack.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(String.valueOf(colors[position]))));
        displayColor(getRandomNumber(0,6),holder);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.binding.getRoot().getContext(), HashTagActivity.class);
                intent.putExtra("hashtag", mList.get(position).getHashTagName());
                int color = Color.TRANSPARENT;
                Drawable background = holder.hashTagBack.getBackground();
                if (background instanceof ColorDrawable)
                    color = ((ColorDrawable) background).getColor();
                intent.putExtra("colorPosition", color);
                holder.binding.getRoot().getContext().startActivity(intent);
            }
        });
    }

    public int getRandomNumber(int min, int max) {
//        return (int) ((Math.random() * (max - min)) + min);
        previousColor=0;
        int newCode=(int) ((Math.random() * (max - min)) + min);
        if(previousColor==newCode)
        {
            getRandomNumber(0,6);
        }
        else {
            previousColor=newCode;
        }
        return previousColor;
    }

    private void displayColor(int position, HashTagViewHolder holder) {
        Log.e("======colorCode", String.valueOf(position));
//        holder.hashTagBack.setBackgroundColor(Color.parseColor(colors[position]));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateData(List<Explore.Data> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<Explore.Data> data) {
        for (int i = 0; i < data.size(); i++) {
            mList.add(data.get(i));
            notifyItemInserted(mList.size() - 1);
        }

    }


    static class HashTagViewHolder extends RecyclerView.ViewHolder {
        ItemHashtagNameBinding binding;
        RelativeLayout hashTagBack;
        ExploreHashTagVideoAdapter adapter = new ExploreHashTagVideoAdapter();

        HashTagViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            hashTagBack=itemView.findViewById(R.id.hashTagBack);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }

        public void setModel(Explore.Data explore) {
            binding.setExplore(explore);
            adapter.setHashTag(explore.getHashTagName());
//            binding.tvHashVidCount.setText(Global.prettyCount(explore.getHashTagVideosCount()).concat(" Videos"));
        }

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
