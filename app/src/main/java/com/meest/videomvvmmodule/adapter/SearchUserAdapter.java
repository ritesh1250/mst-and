package com.meest.videomvvmmodule.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.databinding.ItemSearchUserBinding;
import com.meest.videomvvmmodule.model.user.SearchUser;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.search.FetchUserActivity;
import com.meest.videomvvmmodule.viewmodel.SearchActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.SearchUserViewHolder> {
    private List<SearchUser.User> mList = new ArrayList<>();

    public List<SearchUser.User> getmList() {
        return mList;
    }

    public SearchActivityViewModel viewModel;

    public void setmList(List<SearchUser.User> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public SearchUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user, parent, false);

        return new SearchUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUserViewHolder holder, int position) {
        holder.setModel(mList.get(position));
        Glide.with(holder.binding.getRoot().getContext()).load(mList.get(position).getUserProfile()).skipMemoryCache(true)
                .placeholder(R.drawable.ic_edit_profile_img).into(holder.binding.imgHash);
        holder.binding.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SearchActivityViewModel().followUnfollow(mList.get(position).getUserId(), view.getContext(), position, mList, 1, holder.binding);
            }
        });

        holder.binding.unFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_unfollow_layout);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ImageView imageView = dialog.findViewById(R.id.unFollowImage);
                TextView tvNamePeople = dialog.findViewById(R.id.tvNamePeople);
                TextView tvYes = dialog.findViewById(R.id.tvYes);
                TextView tvNo = dialog.findViewById(R.id.tvNo);
                tvNamePeople.setText(mList.get(position).getFirst_name());
                Log.e("position====", mList.get(position).getUserProfile() + "," + position);
//              loadMediaImage(imageView,mList.get(position).getUserProfile(),false);
                Glide.with(view.getContext()).load(mList.get(position).getUserProfile()).into(imageView);
//                Glide.with(view.getContext()).load(mList.get(position).getUserProfile()).skipMemoryCache(true).into(imageView);
                dialog.show();
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SearchActivityViewModel().followUnfollow(mList.get(position).getUserId(), view.getContext(), position, mList, 2, holder.binding);
                        dialog.dismiss();
                    }
                });
                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

//        if(Global.userId.equals(mList.get(position).getUserId())){
//            holder.binding.getRoot().setVisibility(View.GONE);
//        }else {
//            holder.binding.getRoot().setVisibility(View.VISIBLE);
//        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateData(List<SearchUser.User> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void loadMore(List<SearchUser.User> data) {
        for (int i = 0; i < data.size(); i++) {
            mList.add(data.get(i));
            notifyItemInserted(mList.size() - 1);
        }
    }

    static class SearchUserViewHolder extends RecyclerView.ViewHolder {
        ItemSearchUserBinding binding;

        SearchUserViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setModel(SearchUser.User user) {
            binding.setUser(user);

            //binding.tvDetails.setText(Global.prettyCount(user.getUserFollowerCount()).concat(" Fans  " + Global.prettyCount(user.getUserPostCount()) + " Videos"));
            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(binding.getRoot().getContext(), FetchUserActivity.class);
                intent.putExtra("userid", user.getUserId());
                intent.putExtra("friend_or_not", user.getFriend_or_not());
                binding.getRoot().getContext().startActivity(intent);
            });
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
