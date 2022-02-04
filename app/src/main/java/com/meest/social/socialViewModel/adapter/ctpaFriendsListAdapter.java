package com.meest.social.socialViewModel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.Interfaces.FriendSelectCallback;
import com.meest.R;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.responses.FeedResponse;
import com.meest.responses.FriendsListResponse;
import com.meest.social.socialViewModel.viewModel.createTextPostViewModel.CreateTextPostViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.meest.Activities.CreateTextPostActivity;

public class ctpaFriendsListAdapter extends RecyclerView.Adapter<ctpaFriendsListAdapter.FriendsHolder> {

    private Context context;
    private FriendsListResponse friendsListResponse;
    private FriendSelectCallback friendSelectCallback;

    public ctpaFriendsListAdapter(Context context, FriendsListResponse friendsListResponse, FriendSelectCallback friendSelectCallback) {
        this.context = context;
        this.friendsListResponse = friendsListResponse;
        this.friendSelectCallback = friendSelectCallback;
    }

    @NonNull
    @Override
    public FriendsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_child, parent, false);
        return new FriendsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsHolder holder, int position) {

        FriendsListResponse.User model = friendsListResponse.getData().getRows().get(position);

        if (model.getId().equals(SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID))) {
            holder.itemView.setVisibility(View.GONE);
            return;
        }

        final boolean[] isSelected = {CreateTextPostViewModel.taggedUsers.contains(model.getId())};
        if (isSelected[0]) {
            holder.img_select.setImageResource(R.drawable.ic_check_icon);
        }

        Glide.with(context).load(friendsListResponse.getData().getRows().get(position).getDisplayPicture()).into(holder.img_profile);

        holder.username.setText(friendsListResponse.getData().getRows().get(position).getUsername());

        holder.name.setText(friendsListResponse.getData().getRows().get(position).getFirstName() + " " + friendsListResponse.getData().getRows().get(position).getLastName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected[0]) {
                    isSelected[0] = false;
                    CreateTextPostViewModel.taggedUsers.remove(model.getId());
                    FeedResponse.UserTags a = null;
                    for (FeedResponse.UserTags i : CreateTextPostViewModel.taggedUsername) {
                        if (i.getUsername().equalsIgnoreCase(model.getUsername())) {
                            a = i;
                        }
                    }
                    if (a != null) {
                        CreateTextPostViewModel.taggedUsername.remove(a);
                    }

                    holder.img_select.setImageResource(R.drawable.ic_cheakbox_uncheak);

                    friendSelectCallback.tagChanged();
                } else {
                    if (CreateTextPostViewModel.taggedUsers.size() <= 30) {
                        isSelected[0] = true;
                        CreateTextPostViewModel.taggedUsers.add(model.getId());
                        FeedResponse.UserTags user = new FeedResponse.UserTags();
                        user.setDisplayPicture(model.getDisplayPicture());
                        user.setId(model.getId());
                        user.setUsername(model.getUsername());
                        user.setFirstName(model.getFirstName());
                        user.setLastName(model.getLastName());
                        CreateTextPostViewModel.taggedUsername.add(user);

                        friendSelectCallback.tagChanged();
                        holder.img_select.setImageResource(R.drawable.ic_check_icon);
                    } else {
                        Toast.makeText(context, R.string.you_can_only_select_30_peoples, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsListResponse.getData().getRows().size();
    }

    public void updateData(FriendsListResponse friendsListResponse) {
        this.friendsListResponse = friendsListResponse;
        notifyDataSetChanged();
    }

    public class FriendsHolder extends RecyclerView.ViewHolder {

        CircleImageView img_profile;
        TextView name, username;
        ImageView img_select;

        public FriendsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            img_select = itemView.findViewById(R.id.img_select);
            username = itemView.findViewById(R.id.name2);
            img_profile = itemView.findViewById(R.id.img_profile);
        }
    }
}
