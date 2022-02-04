package com.meest.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.elasticsearch.Hit;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.utils.goLiveUtils.CommonUtils;

import java.util.List;

public class CollectionPointAdapter extends RecyclerView.Adapter<CollectionPointAdapter.MyViewHolder> {

    private Context mContext;
    private List<Hit> collectionPointResults;

    public CollectionPointAdapter(Context mContext, List<Hit> collectionPointResults) {
        this.mContext = mContext;
        this.collectionPointResults = collectionPointResults;
    }

    @Override
    public int getItemCount() {
        return collectionPointResults.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public CollectionPointAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.people_other_item_list, parent, false);
        return new CollectionPointAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionPointAdapter.MyViewHolder holder, final int position) {
        try {
            holder.collectionPointId.setText(collectionPointResults.get(position).getSource().getUsername());
            holder.collectionPointName.setText(collectionPointResults.get(position).getSource().getFirstName() + " " + collectionPointResults.get(position).getSource().getLastName());

            CommonUtils.loadImage(holder.img_profile,collectionPointResults.get(position).getSource().getDisplayPicture(),mContext);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, OtherUserActivity.class);
                    Intent intent = new Intent(mContext, OtherUserAccount.class);
                    intent.putExtra("userId", collectionPointResults.get(position).getSource().getUserId());
                    mContext.startActivity(intent);

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        TextView collectionPointId;
        TextView collectionPointName;
        ImageView img_profile;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            collectionPointName = itemView.findViewById(R.id.name);
            collectionPointId = itemView.findViewById(R.id.name2);
            img_profile = itemView.findViewById(R.id.img_profile);

        }
    }
}