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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.meestbhart.utilss.Constant;
import com.meest.responses.FeelingResponse;

import java.util.ArrayList;

public class ActivityRecyclerAdapter extends RecyclerView.Adapter<ActivityRecyclerAdapter.ViewHolder> {

    String TAG = "ActivityRecyclerAdapter";
    //    private ArrayList<String> mData = new ArrayList<>();
//    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<FeelingResponse.Row> mData = new ArrayList<>();
    private Context context;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity mActivity;
    private FeelingResponse feelingResponse;
    private EditText edt_search_feeling;

    // data is passed into the constructor

    public ActivityRecyclerAdapter(Context context, Activity activity, ArrayList<FeelingResponse.Row> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mActivity = activity;
        this.mData = data;
//        this.mImageUrls = imageUrls;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    public void filterList(ArrayList<FeelingResponse.Row> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }

    int row_index = 100000000 ;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        FeelingResponse.Row model = feelingResponse.getData().getRows().get(position);
        if (mData.size() > 0) {
//            holder.emoji_iv.setImageBitmap(getBitmapFromURL(mImageUrls.get(position)));
            Glide.with(context)
//                    .load(mImageUrls.get(position)) // image url
//                    .load(feelingResponse.getData().getRows().get(position).getIcon())
                    .load(mData.get(position).getIcon())
                    .placeholder(R.drawable.placeholder) // any placeholder to load at start
//                    .override(40, 40) // resizing
                    .centerCrop()
                    .into(holder.activity_iv);  // imageview object
        } else {
            Toast.makeText(context, "No Image Data", Toast.LENGTH_SHORT).show();
        }
        if (mData.size() > 0) {


            holder.activity_tv.setText(mData.get(position).getTitle());

            holder.activity_rl.setOnClickListener(v -> {



//                FeelActActivity feelActActivity = ((FeelActActivity)context);
//                feelActActivity.subActivityId = feelingResponse.getData().getRows().get(position).getId();
//                feelActActivity.subActivityTitle = feelingResponse.getData().getRows().get(position).getTitle();
                /*if (!(feelActActivity.subActivityId == null) && position == row_index) {
                    feelActActivity.subActivityId = null;
                    feelActActivity.subActivityTitle = null;
                    row_index = -1;
                }
                else {
                    feelActActivity.subActivityId = mData.get(position).getId();
                    feelActActivity.subActivityTitle = mData.get(position).getTitle();
//                Toast.makeText(feelActActivity, feelingResponse.getData().getRows().get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    row_index=position;
                }*/

                if(!(Constant.selectedActivityID.equals("")) && position == Constant.row_index_activity){
                    Constant.selectedActivityID = ""; Constant.selectedActivityTitle = "";
                    Constant.row_index_activity = -1;
                }
                else {
                    Constant.selectedActivityID = mData.get(position).getId();
                    Constant.selectedActivityTitle = mData.get(position).getTitle();
                    Constant.row_index_activity = position;
                }

                notifyDataSetChanged();
            });

            if(Constant.row_index_activity==position){
                holder.activity_tv.setTextColor(context.getResources().getColor(R.color.white));
                holder.activity_rl.setBackgroundColor(context.getResources().getColor(R.color.colorTheme));
            }
            else
            {
                holder.activity_tv.setTextColor(context.getResources().getColor(R.color.black));
                holder.activity_rl.setBackgroundColor(context.getResources().getColor(R.color.white));
            }


        } else {
            Toast.makeText(mActivity, "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }



    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView activity_tv;
        ImageView activity_iv;
        RelativeLayout activity_rl;

        ViewHolder(View itemView) {
            super(itemView);
            activity_tv = itemView.findViewById(R.id.activity_tv);
            activity_iv = itemView.findViewById(R.id.activity_iv);
            activity_rl = itemView.findViewById(R.id.activity_rl);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    FeelingResponse.Row getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
