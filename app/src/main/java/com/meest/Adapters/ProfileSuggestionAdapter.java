package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
//import com.meest.responses.SuggestionResponse;
import com.meest.social.socialViewModel.model.SuggestionResponse;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSuggestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ProfileSuggestionAdapter.OnItemClickListener mItemClickListener;
    Context context;
    List<SuggestionResponse.Datum> daTA;

    public ProfileSuggestionAdapter(Context context, ArrayList<SuggestionResponse.Datum> daTA) {
        this.context = context;
        this.daTA = daTA;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_suggestion_adapter, parent, false);
        return new ProfileSuggestionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ProfileSuggestionAdapter.ViewHolder userViewHolder = (ProfileSuggestionAdapter.ViewHolder) holder;

        userViewHolder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //context.startActivity(new Intent(context, OtherUserActivity.class).putExtra("userId",data.get(position).getId()));
            }
        });

        if (daTA.get(position).getDisplayPicture() != null){
            Glide.with(context).load(daTA.get(position).getDisplayPicture()).into(userViewHolder.profile_pic);
        }

        userViewHolder.txt_name.setText(daTA.get(position).getUsername());

    }

    @Override
    public int getItemCount() {
        return daTA == null ? 0 : daTA.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile_pic;
        TextView txt_name;
        LinearLayout layout_main;
        public ViewHolder(View itemView) {
            super(itemView);
            profile_pic = itemView.findViewById(R.id.profile_pic);
            txt_name = itemView.findViewById(R.id.txt_name);
            layout_main = itemView.findViewById(R.id.layout_main);

        }
    }


    public void setOnItemClickListener(final ProfileSuggestionAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }


}
