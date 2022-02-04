package com.meest.videomvvmmodule.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.meest.R;
import com.meest.databinding.ItemVidProfileListBinding;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.video.PlayerActivity;
import com.meest.videomvvmmodule.viewmodel.ProfileVideosViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileVideosAdapter extends RecyclerView.Adapter<ProfileVideosAdapter.ProfileVideoViewHolder> {
    private List<Video.Data> mList = new ArrayList<>();
    private OnRecyclerViewItemClick onRecyclerViewItemClick;
    private ProfileVideosViewModel videosViewModel;

    public List<Video.Data> getmList() {
        return mList;
    }

    public void setmList(List<Video.Data> mList) {
        this.mList = mList;
    }

    public OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    public ProfileVideosViewModel getVideosViewModel() {
        return videosViewModel;
    }

    public void setVideosViewModel(ProfileVideosViewModel videosViewModel) {
        this.videosViewModel = videosViewModel;
    }

    @NonNull
    @Override
    public ProfileVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vid_profile_list, parent, false);
        return new ProfileVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileVideoViewHolder holder, int position) {
        holder.setModel(position);
        /*if(mList.get(position).getThumbnail_image()!=null){
            Glide.with(holder.binding.getRoot().getContext())
                    .load(mList.get(position).getThumbnail_image())
                    .placeholder(R.drawable.video_loader)
                    .into(holder.binding.avcVideoThumbnail);
        }else {
            Glide.with(holder.binding.getRoot().getContext())
                    .load(mList.get(position).getPostVideo())
                    .placeholder(R.drawable.video_loader)
                    .into(holder.binding.avcVideoThumbnail);
        }*/

        /* .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.binding.progressbar.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.binding.progressbar.setVisibility(View.GONE);
                return false;
            }
        })*/

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateData(List<Video.Data> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<Video.Data> data) {
        for (int i = 0; i < data.size(); i++) {
            mList.add(data.get(i));
            notifyItemInserted(mList.size() - 1);
        }

    }

    public interface OnRecyclerViewItemClick {

        void onItemClick(Video.Data model, int position, ItemVidProfileListBinding binding);

    }

    class ProfileVideoViewHolder extends RecyclerView.ViewHolder {
        ItemVidProfileListBinding binding;

        ProfileVideoViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }

        public void setModel(int position) {
            binding.setModel(mList.get(position));

            if (videosViewModel.userId.equals(Global.userId) && videosViewModel.vidType == 0) {
                binding.getRoot().setOnLongClickListener(v -> {
                    onRecyclerViewItemClick.onItemClick(mList.get(position), position, binding);
                    return true;
                });
            }

            binding.getRoot().setOnClickListener(v -> {

                Intent intent = new Intent(binding.getRoot().getContext(), PlayerActivity.class);
//                Global.tempList = (ArrayList<Video.Data>) mList;
//                intent.putExtra("video_list", new Gson().toJson(mList));

                List<Video.Data> singleList = new ArrayList<>();
////                for (int i = position - 2; i <= position + 2; i++) {
////                    singleList.add(mList.get(i));
////                }
                singleList.add(mList.get(position));
                intent.putExtra("video_list", new Gson().toJson(singleList));


                intent.putExtra("position", position);
                intent.putExtra("type", videosViewModel.vidType);
                intent.putExtra("user_id", videosViewModel.userId);
                binding.getRoot().getContext().startActivity(intent);
            });

            /*if(mList.get(position).getPostViewCount().equals("1")){
                binding.avcViewCount.setText("0");
            }else {
                int counts = Integer.parseInt(mList.get(position).getPostViewCount());
                if ( counts % 2 == 0 ){
                    counts = counts/2;
                }else {
                    counts = (counts+1)/2;
                }
                binding.avcViewCount.setText(Global.prettyCount(Long.parseLong(String.valueOf(counts))));
            }*/

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
