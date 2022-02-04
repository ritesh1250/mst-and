package com.meest.social.socialViewModel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ChooseFavAdapterBinding;
import com.meest.databinding.ItemCoinPlansBinding;
import com.meest.meestbhart.register.fragment.choosetopic.model.TopicsResponse;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.view.notification.NotificationAdapter;
import com.meest.videomvvmmodule.adapter.CoinPlansAdapter;
import com.meest.videomvvmmodule.model.wallet.CoinPlan;

import java.util.ArrayList;
import java.util.List;

public class ChooseAdapterSocial extends RecyclerView.Adapter<ChooseAdapterSocial.ViewHolder> {
    static String[] colors = new String[]{"#F54051", "#DF1D79", "#0A23F9", "#CF1100", "#7F1BB9", "#ffff4444", "#FFC36A"};
    private List<TopicsResponse.Row> mData = new ArrayList<>();
    private OnRecyclerViewItemClick onRecyclerViewItemClick;

    public OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_fav_adapter, parent, false);

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.setModel(mData.get(position), position);
        TopicsResponse.Row chip = mData.get(position);

        if (chip.isSelected()) {
            holder.binding.mainBg.setBackgroundColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.social_background_blue));
        } else {
            holder.binding.mainBg.setBackgroundColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.white));
        }

//        if (mData.get(position).isSelected()) {
//            holder.binding.mainBg.setBackgroundColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.white));
//            holder.binding.textDone.setTextColor(holder.binding.getRoot().getContext().getColor(R.color.greyTextColor));
//        }else{
//            holder.binding.mainBg.setBackgroundColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), R.color.social_background_blue));
//            holder.binding.textDone.setTextColor(holder.binding.getRoot().getContext().getColor(R.color.white));
//        }
    }

    public List<TopicsResponse.Row> getSelectedInterest() {
        List<TopicsResponse.Row> selected = new ArrayList<>();
        for (TopicsResponse.Row r : mData) {
            if (r.isSelected())
                selected.add(r);
        }
        return selected;
    }

    public List<TopicsResponse.Row> getAllPlayers() {
        return new ArrayList<>(mData);
    }

    public TopicsResponse.Row getSelected() {
        for (TopicsResponse.Row d : mData) {
            if (d.isSelected())
                return d;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void update(List<TopicsResponse.Row> list) {
        this.mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public interface OnRecyclerViewItemClick {
        void onClickInterest(TopicsResponse.Row data, int position, ChooseFavAdapterBinding binding);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, TextView text_done);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ChooseFavAdapterBinding binding;


        //        TextView layout_view;
        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

        }

        public void setModel(TopicsResponse.Row row, int position) {
            binding.setChooseAdapter(row);
            binding.textDone.setOnClickListener(v -> {
                onRecyclerViewItemClick.onClickInterest(row, position, binding);
            });
        }
    }
}
