package com.meest.videomvvmmodule.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.meest.R;
import com.meest.databinding.ItemReportListBinding;
import com.meest.videomvvmmodule.model.comment.ReportResponse;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder>{
    private List<ReportResponse.ReportName> mList = new ArrayList<>();
    private OnRecyclerViewItemClick onRecyclerViewItemClick;

    public List<ReportResponse.ReportName> getmList() {
        return mList;
    }

    public void setmList(List<ReportResponse.ReportName> mList) {
        this.mList = mList;
    }

    public OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_list, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        holder.setModel(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateData(List<ReportResponse.ReportName> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<ReportResponse.ReportName> data) {
        for (int i = 0; i < data.size(); i++) {
            mList.add(data.get(i));
            notifyItemInserted(mList.size() - 1);
        }
    }

    public interface OnRecyclerViewItemClick {
        void onCommentItemClick(ReportResponse.ReportName data, int position, int type, ItemReportListBinding binding);
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {
        ItemReportListBinding binding;

        ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setModel(ReportResponse.ReportName comment, int position) {
            binding.setReport(comment);

//            if (!TextUtils.isEmpty(Global.userId) && Global.userId.equals(getUserId())) {
//                binding.imgDelete.setVisibility(View.VISIBLE);
//            }
            binding.itemLayout.setOnClickListener(v -> onRecyclerViewItemClick.onCommentItemClick(comment, position, 1,binding));
        }
    }
}
