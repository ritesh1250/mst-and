package com.meest.TopAndTrends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;

import java.util.List;

public class TrendHowManyAdapter extends RecyclerView.Adapter<TrendHowManyAdapter.HowManyTrendViewHolder> {

    private Context mCtx;
    //we are storing all the products in a list
    private List<String> howManyList;
    private HowManyTrendCallback howManyTrendCallback;

    public TrendHowManyAdapter(Context mCtx, List<String> howManyList, HowManyTrendCallback howManyTrendCallback) {
        this.mCtx = mCtx;
        this.howManyList = howManyList;
        this.howManyTrendCallback = howManyTrendCallback;
    }

    @NonNull
    @Override
    public HowManyTrendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.trending_how_many_list, null);
        return new HowManyTrendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HowManyTrendViewHolder holder, int position) {

       holder.tvHowManyTrend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               howManyTrendCallback.howManyCallback(position);
           }
       });
    }

    @Override
    public int getItemCount() {
        return howManyList.size();
    }

    public class HowManyTrendViewHolder extends RecyclerView.ViewHolder {

        TextView tvHowManyTrend;

        public HowManyTrendViewHolder(@NonNull View itemView) {
            super(itemView);


            tvHowManyTrend=itemView.findViewById(R.id.tvHowManyTrend);


        }
    }
}
