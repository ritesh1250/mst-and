package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;

import java.util.List;

public class InterestChooseAdapter extends RecyclerView.Adapter<InterestChooseAdapter.ViewHolder> {

    private final Context mContext;
    private List<String> mData;
    InterestChooseAdapter.OnItemClickListener mItemClickListener;

    public OnItemClickListener getmItemClickListener() {
        return mItemClickListener;
    }

    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public InterestChooseAdapter(Context context, List<String> mData, OnItemClickListener mItemClickListener) {
        mContext = context;
        this.mData = mData;
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public InterestChooseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_fav_adapter, null);
        return new InterestChooseAdapter.ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull InterestChooseAdapter.ViewHolder holder, int position) {
        holder.text_done.setText(mData.get(position));
//        holder.text_done.setOnClickListener(v -> {
//            if(Constant.array_list_.contains(mData.get(position))) {
//                Constant.array_list_.remove(mData.get(position));
////                    holder.main_bg.setBackgroundResource(R.drawable.custom_bg_backgorund);
//                holder.main_bg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
//                holder.text_done.setTextColor(mContext.getColor(R.color.greyTextColor));
////                mData.get(position).setChecked(false);
//            } else {
//                Constant.array_list_.add(mData.get(position));
////                    holder.main_bg.setBackgroundResource(R.drawable.new_gradient_bg);
//                holder.main_bg.setBackgroundColor(ContextCompat.getColor(mContext, R.color.social_background_blue));
//                holder.text_done.setTextColor(mContext.getColor(R.color.white));
////                mData.get(position).setChecked(false);
//            }
//        });

        holder.main_bg.setOnClickListener(v -> {
            mItemClickListener.onItemClick(position,mData,holder);
        });

//        holder.main_bg.setOnClickListener(v -> {
//            int newPosition = holder.getBindingAdapterPosition();
//            for (int i = 0; i < Constant.rowList.size()-1; i++){
//                if(Constant.rowList.get(i).getTopic().get_topic().equals(Constant.mData.get(newPosition).getTopic())){
//                    Constant.rowList.remove(i);
//                }
//                notifyDataSetChanged();
//            }
////            String temp = Constant.allTopics.get(newPosition);
////            Constant.selectedTopicIDs.add(Constant.allTopics.get(newPosition));
//            insertApi(Constant.mData.get(newPosition).getId());
//            Constant.mData.remove(position);
////            Constant.allTopics.remove(newPosition);
////            Constant.selectedTopics.add(temp);
//
//            notifyItemRemoved(newPosition);
//            notifyItemRangeChanged(newPosition, Constant.mData.size());
//
////            if (position == Constant.allTopics.size() - 1) {
////                Constant.allTopics.remove(position);
////                notifyItemRemoved(position);
////            } else {
////                int shift = 1;
////                try {
////                    while (true){
////                        Constant.allTopics.remove(position - shift);
////                        notifyItemRemoved(position);
////                        break;
////                    }
////                } catch (IndexOutOfBoundsException e) {
////                    shift++;
////                    e.printStackTrace();
////                }
////            }
//        });


    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_done;
        RelativeLayout main_bg;
        ImageButton mDeleteButton;

        //        TextView layout_view;
        public ViewHolder(View itemView) {
            super(itemView);
//            layout_view = itemView.findViewById(R.id.layout_view);
            text_done = (TextView) itemView.findViewById(R.id.text_done);
            main_bg = (RelativeLayout) itemView.findViewById(R.id.main_bg);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, List<String> mData, ViewHolder holder);
    }
}
