package com.meest.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.FollowerListResponse;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddpeopleAdapter extends RecyclerView.Adapter<AddpeopleAdapter.ViewHolder> {
    private final static int sBtnCloseSize = 16;
    private final static int sBtnCloseLeftMargin = 5;

    private final Context mContext;

    private ArrayList<FollowerListResponse.Following> mData_new = new ArrayList<>();
    private ArrayList<String> isAdded = new ArrayList<>();
    AddpeopleAdapter.OnItemClickListener mItemClickListener;
    public AddpeopleAdapter(Context context, ArrayList<FollowerListResponse.Following> mData_new,ArrayList<String> isAdded) {
        mContext = context;
        this.mData_new = mData_new;
        this.isAdded = isAdded;
    }

    @NonNull
    @Override
    public AddpeopleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_people_adapter, null);
        AddpeopleAdapter.ViewHolder viewHolder = new AddpeopleAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AddpeopleAdapter.ViewHolder holder, final int position) {


        holder.txt_user_name.setText(mData_new.get(position).getFirstName() + " " + mData_new.get(position).getLastName()) ;

        if (mData_new.get(position).getDisplayPicture().isEmpty()){

        }else {
            Glide.with(mContext).load(mData_new.get(position).getDisplayPicture()).into(holder.user_pro_chat);
        }
        if (isAdded.get(position).equals("1")){
            holder.img_add_people.setImageResource(R.drawable.ic_added_people_icon);
           // isAdded.set(position,"1");
        }else {
            holder.img_add_people.setImageResource(R.drawable.ic_addpeople_icons);
            //isAdded.set(position,"0");
        }

        holder.layout_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);
               /* if (isAdded.get(position).equals("0")){
                    holder.img_add_people.setImageResource(R.drawable.ic_added_people_icon);
                    isAdded.set(position,"1");
                }else {
                    holder.img_add_people.setImageResource(R.drawable.ic_addpeople_icons);
                    isAdded.set(position,"0");
                }*/
            }
        });


    }

    @Override
    public int getItemCount() {
        Log.w("data", String.valueOf(mData_new.size()));
        return mData_new.size();
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_user_name;
        CircleImageView user_pro_chat;
        RelativeLayout layout_main;
        ImageView img_add_people;
        LinearLayout layout_add;
        public ViewHolder(View itemView) {
            super(itemView);
            user_pro_chat = itemView.findViewById(R.id.user_pro_chat);
            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            img_add_people = itemView.findViewById(R.id.img_add_people);
            layout_main = itemView.findViewById(R.id.layout_main);
            layout_add = itemView.findViewById(R.id.layout_add);

        }


    }


    public void setOnItemClickListener(final AddpeopleAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }


}
