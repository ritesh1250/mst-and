package com.meest.social.socialViewModel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Interfaces.CollectionInterface;
import com.meest.R;
import com.meest.databinding.CollectionAdapterBinding;
import com.meest.responses.SavedDataResponse;

import java.util.ArrayList;
import java.util.List;

public class MyBookmarksAdapterModel extends RecyclerView.Adapter<MyBookmarksAdapterModel.MyBookmarksViewHolder> {

    private List<SavedDataResponse.Row> collectionList= new ArrayList<>();;
    private Context context;
    private CollectionInterface collectionInterface;



    @NonNull
    @Override
    public MyBookmarksAdapterModel.MyBookmarksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyBookmarksAdapterModel.MyBookmarksViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_adapter, parent, false));
    }



    @Override
    public void onBindViewHolder(@NonNull MyBookmarksAdapterModel.MyBookmarksViewHolder holder, int position) {
        holder.setList(collectionList.get(position));
        holder.itemView.setOnClickListener(v -> {collectionInterface.collectionClicked(position);});
    }


    @Override
    public int getItemCount() {
        return collectionList.size();
    }


    public void addLoading(List<SavedDataResponse.Row> rows,CollectionInterface collectionInterface) {
        this.collectionList.addAll(rows);
        notifyDataSetChanged();
        this.collectionInterface = collectionInterface;
    }

    public class MyBookmarksViewHolder extends RecyclerView.ViewHolder {
        CollectionAdapterBinding collectionAdapterBinding;
        public MyBookmarksViewHolder(@NonNull View itemView) {
            super(itemView);
            collectionAdapterBinding= DataBindingUtil.bind(itemView);

        }
        public void setList(SavedDataResponse.Row row) {
            collectionAdapterBinding.setMCollectinAdaptermodel(row);
        }


    }

}
