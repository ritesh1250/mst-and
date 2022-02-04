package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.CommentListResponse;
import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubCommentAdapter extends RecyclerView.Adapter<SubCommentAdapter.ViewHolder> {
    private final static int sBtnCloseSize = 16;
    private final static int sBtnCloseLeftMargin = 5;

    private final Context mContext;


    private ArrayList<CommentListResponse.Row> mData = new ArrayList<>();
    ArrayList<String> like_list = new ArrayList<>();
    SubCommentAdapter.OnItemClickListener mItemClickListener;

    SubCommentAdapter.OnItemClickListenerLike mItemClickListenerlike;


    public SubCommentAdapter(Context context, ArrayList<CommentListResponse.Row> mData, ArrayList<String> like_list) {
        mContext = context;
        this.mData = mData;
        this.like_list = like_list;
    }


    @NonNull
    @Override
    public SubCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_comment_adapater, null);
        SubCommentAdapter.ViewHolder viewHolder = new SubCommentAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull final SubCommentAdapter.ViewHolder holder, final int position) {


        holder.txt_user_name.setText(mData.get(position).getUser().getUsername()) ;

        if (mData.get(position).getUser().getDisplayPicture().toString().isEmpty()){

        }else {
            Glide.with(mContext).load(mData.get(position).getUser().getDisplayPicture()).into(holder.img_profile);
        }


        try {
            String test = URLDecoder.decode(mData.get(position).getComment().toString(),"UTF-8");
            holder.txt_comment.setText(test);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        holder.layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);
            }
        });



        holder.txt_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListenerlike != null)
                    mItemClickListenerlike.onItemClick(position);
            }
        });


        if (like_list.get(position).equalsIgnoreCase("1")){
            holder.txt_like.setTextColor(mContext.getResources().getColor(R.color.blue));
        }else {
            holder.txt_like.setTextColor(mContext.getResources().getColor(R.color.simple_like_comment));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_user_name,txt_like;
        CircleImageView img_profile;
        RelativeLayout layout_main;
        hani.momanii.supernova_emoji_library.Helper.EmojiconTextView txt_comment;
        public ViewHolder(View itemView) {
            super(itemView);
            img_profile = itemView.findViewById(R.id.img_profile);
            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            layout_main = itemView.findViewById(R.id.layout_main);
            txt_comment = itemView.findViewById(R.id.txt_comment);
            txt_like = itemView.findViewById(R.id.txt_like);

        }


    }


    public void setOnItemClickListener(final SubCommentAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }



    public void setOnItemClickListenerLike(final SubCommentAdapter.OnItemClickListenerLike mItemClickListenerlike) {
        this.mItemClickListenerlike = mItemClickListenerlike;
    }

    public interface OnItemClickListenerLike {

        void onItemClick(int position);
    }
}
