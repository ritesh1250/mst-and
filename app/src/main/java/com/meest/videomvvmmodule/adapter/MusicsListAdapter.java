package com.meest.videomvvmmodule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemMusicVideoBinding;
import com.meest.videomvvmmodule.model.music.Musics;
import com.meest.videomvvmmodule.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MusicsListAdapter extends RecyclerView.Adapter<MusicsListAdapter.MusicViewHolder> {
    List<Musics.SoundList> mList = new ArrayList<>();
    private boolean isChild = false;
    private Context context;
    boolean isFav = false;
    int type = 0;
    private MusicsCategoryAdapter.OnItemClickListener onMusicClick;

    public List<Musics.SoundList> getmList() {
        return mList;
    }

    public void setmList(List<Musics.SoundList> mList) {
        this.mList = mList;
    }

    public boolean isChild() {
        return isChild;
    }

    public void setChild(boolean child) {
        isChild = child;
    }

    public MusicsCategoryAdapter.OnItemClickListener getOnMusicClick() {
        return onMusicClick;
    }

    public void setOnMusicClick(MusicsCategoryAdapter.OnItemClickListener onMusicClick) {
        this .onMusicClick = onMusicClick;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_video, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.setModel(position);
    }

    @Override
    public int getItemCount() {
        if (isChild) {
            return Math.min(mList.size(), 4);
        }
        return mList.size();
    }

    public void updateData(List<Musics.SoundList> soundList) {

        mList = soundList;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mList.remove(mList.get(position));
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mList.size());
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder {
        private ItemMusicVideoBinding binding;
        private SessionManager sessionManager;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                sessionManager = new SessionManager(binding.getRoot().getContext());
            }
        }

        public void setModel(int position) {
            Musics.SoundList model = mList.get(position);
            if (sessionManager != null && sessionManager.getFavouriteMusic() != null) {
                binding.setIsFav(sessionManager.getFavouriteMusic().contains(model.getSoundId()));
            }
            binding.setModel(model);
//            if (type == 1) {
//                binding.icFavourite.setColorFilter(R.color.colorTheme);
////                binding.icFavourite.setImageResource(R.drawable.ic_bookmark_selected);
//            } else {
//                binding.icFavourite.setColorFilter(R.color.unselectedFav);
////                binding.icFavourite.setImageResource(R.drawable.ic_bookmarks_unselected);
//            }
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMusicClick.onItemClick(binding, position, model, 0);
                }
            });
            binding.icFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMusicClick.onItemClick(binding, position, model, 1);
                }
            });
            binding.btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMusicClick.onItemClick(binding, position, model, 2);
                }
            });
        }
    }
}
