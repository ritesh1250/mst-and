package com.meest.videomvvmmodule.view.music_bottomsheet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.databinding.ItemMusicFavoriteBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MusicFavoriteAdapter extends RecyclerView.Adapter<MusicFavoriteAdapter.ViewHolder> {
    List<FavoriteMusicsResponse.Data> musicList = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MusicFavoriteAdapter.ViewHolder holder, int position) {
        holder.setModel(musicList.get(position));
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public void updateData(List<FavoriteMusicsResponse.Data> data) {
        musicList.addAll(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemMusicFavoriteBinding binding;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setModel(FavoriteMusicsResponse.Data row) {
            binding.setModel(row);
        }
    }
}
