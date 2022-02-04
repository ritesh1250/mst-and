package com.meest.metme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.responses.GetDocsMediaChatsResponse;
import com.meest.databinding.RowMediaLinkDocFullPageBinding;
import com.meest.metme.viewmodels.MediaFullPageViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MediaImageFullPageDocAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<GetDocsMediaChatsResponse.Data.LastWeek> responseList;
    String fullName;

    RowMediaLinkDocFullPageBinding binding;
    public MediaImageFullPageDocAdapter(List<GetDocsMediaChatsResponse.Data.LastWeek> responseList, String fullName, Context context) {
        this.responseList =  responseList;
        this.fullName = fullName;
        this.context = context;
    }


    @Override
    public int getItemCount() {
        return responseList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {


        binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.row_media_link_doc_full_page, parent, false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());
        viewHolder.setBinding(binding);

        return viewHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder holder=(ViewHolder)viewHolder;

        holder.setViewModel(responseList.get(position));

      /*  Glide.with(context).load(responseList.get(position).getFileURL()).into(holder.image);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatImageViewActivity.class);
                intent.putExtra("title", fullName);
                intent.putExtra("iv_url", responseList.get(position).getFileURL());
                intent.putExtra("type", responseList.get(position).getAttachmentType());
                intent.putExtra("dateAndTime", responseList.get(position).getCreatedAt());
                intent.putExtra("username", fullName);
                intent.putExtra("messageId", "");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });*/

    }


    class ViewHolder extends RecyclerView.ViewHolder{

        RowMediaLinkDocFullPageBinding binding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }

        public void setBinding(RowMediaLinkDocFullPageBinding binding){

            this.binding = binding;

        }

        public void setViewModel(GetDocsMediaChatsResponse.Data.LastWeek  response){

            binding.setViewModel(new MediaFullPageViewModel(response,fullName,context ));
        }
    }



}



