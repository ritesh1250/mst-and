package com.meest.metme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ChatFollowerListLayoutBinding;
import com.meest.databinding.ColorThemeLayoutBinding;
import com.meest.metme.model.GetForwardListResponse;
import com.meest.metme.viewmodels.FollowAdapterViewModel;

import java.util.ArrayList;
import java.util.List;

public class ForwardMessageAdapter extends RecyclerView.Adapter<ForwardMessageAdapter.ViewHolder> {
    List<GetForwardListResponse.Data> fullDataList = new ArrayList<>();
    private ForwardMessageAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick;
    private int clickPosition;
    private int row_index;
    ChatFollowerListLayoutBinding binding;
    Context context;
    public ObservableBoolean isSelected = new ObservableBoolean(false);
    public List<String> selectedItemList = new ArrayList<>();



    @NonNull
    @Override
    public ForwardMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.chat_follower_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());
        viewHolder.setBinding(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForwardMessageAdapter.ViewHolder holder, int position) {
        holder.setData(new FollowAdapterViewModel(context,isSelected,fullDataList.get(position),this));


    }

    public void onClick(View view, GetForwardListResponse.Data following) {


        if (selectedItemList.contains(following.getId())) {
            view.setBackgroundColor(context.getResources().getColor(R.color.white));
            selectedItemList.remove(following.getId());
        } else {
            view.setBackgroundColor(context.getResources().getColor(R.color.grayColor));
            selectedItemList.add(following.getId());
        }

        if (selectedItemList.size()>0){
            isSelected.set(true);
        }else {
            isSelected.set(false);
        }


    }


    @Override
    public int getItemCount() {
        return fullDataList.size();
    }


   public List<String> getSelectedUser() {
       return selectedItemList;
//       return binding.getRow().selectedItemList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        ChatFollowerListLayoutBinding binding;


        public void setBinding(ChatFollowerListLayoutBinding binding){

            this.binding = binding;
        }

        public void setData(FollowAdapterViewModel model){
            binding.setRow(model);
//            binding.setRow(new FollowAdapterViewModel(context,isSelected,model));

        }

        public FollowAdapterViewModel  getData(){
            return   binding.getRow();

        }

    }

    public void loadMore(List<GetForwardListResponse.Data> data, ObservableBoolean isSelected, Context context) {
        this.context = context;
        this.isSelected = isSelected;
//        GetForwardListResponse.Data.setContext(context,isSelected);
        fullDataList.clear();
        fullDataList.addAll(data);
//        notifyItemInserted(fullDataList.size() - 1);
        notifyDataSetChanged();
    }

    public interface OnRecyclerViewItemClick {
        void onItemClick(GetForwardListResponse.Data data, int position, ColorThemeLayoutBinding binding, ViewHolder holder);
    }

    public ForwardMessageAdapter.OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(ForwardMessageAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }
}