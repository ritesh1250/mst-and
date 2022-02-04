package com.meest.social.socialViewModel.adapter.feelActAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.meestbhart.utilss.Constant;
import com.meest.responses.FeelingResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FeelingRecyclerAdapter extends RecyclerView.Adapter<FeelingRecyclerAdapter.ViewHolder>{

    private ArrayList<FeelingResponse.Row> mData;
    private LayoutInflater mInflater;
    private ActivityRecyclerAdapter.ItemClickListener mClickListener;
    private Context context;
    private Activity mActivity;
    private String TAG = "FeelingActivityNewRecyclerAdapter";
    private EditText edt_search_feeling;
    int row_index = 100000000 ;

    public FeelingRecyclerAdapter(Context context, ArrayList<FeelingResponse.Row> data, Activity activity, FeelingResponse feelingResponse, EditText edt_search_feeling) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mActivity = activity;
        this.context = context;
        this.edt_search_feeling = edt_search_feeling;

    }

    @Override
    public @NotNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.feeling_recycler_item, parent, false);
        return new ViewHolder(view);
    }
    ArrayList<String> data = new ArrayList<>();
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeelingResponse.Row model = mData.get(position);
        holder.emoji_tv.setText(model.getTitle());
        Glide.with(context).load(model.getIcon()) // image url
                .placeholder(R.drawable.placeholder) // any placeholder to load at start
//                    .override(40, 40) // resizing
                .centerCrop()
                .into(holder.emoji_iv);  // imageview object

        holder.feeling_rl.setOnClickListener(v -> {

            if(!(Constant.selectedFeelingID.equals("")) && position == Constant.row_index_feeling){
                Constant.selectedFeelingID = ""; Constant.selectedFeelingTitle = "";
                Constant.row_index_feeling = -1;
            }
            else {
                Constant.selectedFeelingTitle = mData.get(position).getTitle();
                Constant.selectedFeelingID = mData.get(position).getId();
                Constant.row_index_feeling = position;
            }

            notifyDataSetChanged();

        });

        if(Constant.row_index_feeling==position){
            holder.emoji_tv.setTextColor(context.getResources().getColor(R.color.trending_grey));
            holder.feeling_rl.setBackgroundColor(context.getResources().getColor(R.color.colorTheme));
        }
        else {
            holder.emoji_tv.setTextColor(context.getResources().getColor(R.color.black));
            holder.feeling_rl.setBackgroundColor(context.getResources().getColor(R.color.trending_grey));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView emoji_tv;
        ImageView emoji_iv;
        RelativeLayout feeling_rl;

        ViewHolder(View itemView) {
            super(itemView);
            emoji_tv = itemView.findViewById(R.id.emoji_tv);
            emoji_iv = itemView.findViewById(R.id.emoji_iv);
            feeling_rl = itemView.findViewById(R.id.feeling_rl);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public void filterList(ArrayList<FeelingResponse.Row> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }


    OnEmojiClickListener onItemClickListener;


    public interface OnEmojiClickListener {
        void feelingData(String feelingID,String feelingTitle);

    }

    public void setOnItemClickListener(final OnEmojiClickListener onEmojiItemClickListener) {
        this.onItemClickListener = onEmojiItemClickListener;
    }
}
