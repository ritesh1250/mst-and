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

import com.meest.Paramaters.CommentDisLikeParam;
import com.meest.Paramaters.CommentLikeParam;
import com.meest.R;
import com.meest.responses.CommentDislikeResponse;
import com.meest.responses.CommentLikeResponse;
import com.meest.responses.CommentListResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final static int sBtnCloseSize = 16;
    private final static int sBtnCloseLeftMargin = 5;

    private final Context mContext;


    private ArrayList<CommentListResponse.Row> mData = new ArrayList<>();
    ArrayList<String> like_list = new ArrayList<>();
    CommentAdapter.OnItemClickListener mItemClickListener;

    CommentAdapter.OnItemClickListenerLike mItemClickListenerlike;


    public CommentAdapter(Context context, ArrayList<CommentListResponse.Row> mData, ArrayList<String> like_list) {
        mContext = context;
        this.mData = mData;
        this.like_list = like_list;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_adapter, null);
        CommentAdapter.ViewHolder viewHolder = new CommentAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.ViewHolder holder, final int position) {

        holder.txt_user_name.setText(mData.get(position).getUser().getUsername());

        if (mData.get(position).getUser().getDisplayPicture().toString().isEmpty()) {

        } else {
            Glide.with(mContext).load(mData.get(position).getUser().getDisplayPicture()).into(holder.img_profile);
        }

        try {
            String test = URLDecoder.decode(mData.get(position).getComment().toString(), "UTF-8");
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

        if (mData.get(position).getLiked() == 1) {
            holder.txt_like.setImageResource(R.drawable.like_diamond_filled);
        } else {
            holder.txt_like.setImageResource(R.drawable.like_diamond);
        }
        holder.ll_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> map = new HashMap<>();
                map.put("x-token", SharedPrefreances.getSharedPreferenceString(mContext, SharedPrefreances.AUTH_TOKEN));

                WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);

                if (mData.get(position).getLiked() == 0) {
                    //image.setVisibility(View.VISIBLE);
                    CommentLikeParam commentLikeParam = new CommentLikeParam(mData.get(position).getPostId(),
                            mData.get(position).getId(), true);
                    Call<CommentLikeResponse> call1 = webApi1.commentLike(map, commentLikeParam);
                    call1.enqueue(new Callback<CommentLikeResponse>() {
                        @Override
                        public void onResponse(Call<CommentLikeResponse> call, Response<CommentLikeResponse> response) {
                            //image.setVisibility(View.GONE);
                            if (response.body().getCode() == 1) {
                              mData.get(position).setLiked(1);
                                //   commentAdapter.notifyDataSetChanged();
                                holder.txt_like.setImageResource(R.drawable.like_diamond_filled);
                            } else {
                                Utilss.showToast(mContext, response.body().getSuccess().toString(), R.color.msg_fail);
                            }
                        }

                        @Override
                        public void onFailure(Call<CommentLikeResponse> call, Throwable t) {
                            // image.setVisibility(View.GONE);
                            Log.w("demo", t);
                            Utilss.showToast(mContext, mContext.getString(R.string.error_msg), R.color.grey);
                        }
                    });

                } else if(mData.get(position).getLiked()==1){
                    //image.setVisibility(View.VISIBLE);
                    CommentDisLikeParam commentDisLikeParam = new CommentDisLikeParam(mData.get(position).getCommentLikes().get(0).getCommentId());
                    Call<CommentDislikeResponse> call1 = webApi1.removeCommentLike(map, commentDisLikeParam);
                    call1.enqueue(new Callback<CommentDislikeResponse>() {
                        @Override
                        public void onResponse(Call<CommentDislikeResponse> call, Response<CommentDislikeResponse> response) {
                            //  image.setVisibility(View.GONE);
                            if (response.body().getCode() == 1) {
                                mData.get(position).setLiked(0);
                                holder.txt_like.setImageResource(R.drawable.like_diamond); // commentAdapter.notifyDataSetChanged();
                            } else {
                                Utilss.showToast(mContext, mContext.getString(R.string.error_msg), R.color.grey);
                            }
                        }

                        @Override
                        public void onFailure(Call<CommentDislikeResponse> call, Throwable t) {
                            //     image.setVisibility(View.GONE);
                            Utilss.showToast(mContext, mContext.getString(R.string.error_msg), R.color.grey);
                        }
                    });

                }





              /*  if (mItemClickListenerlike != null)
                    mItemClickListenerlike.onItemClick(position);*/
            }
        });

        holder.txt_time.setText(mData.get(position).getCreatedAt());
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    final class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_user_name, txt_time;
        ImageView txt_like;
        CircleImageView img_profile;
        RelativeLayout layout_main;

        EmojiconTextView txt_comment;

        LinearLayout ll_like;

        public ViewHolder(View itemView) {
            super(itemView);
            img_profile = itemView.findViewById(R.id.img_profile);
            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            layout_main = itemView.findViewById(R.id.layout_main);
            ll_like = itemView.findViewById(R.id.ll_like);
            txt_comment = itemView.findViewById(R.id.txt_comment);
            txt_like = itemView.findViewById(R.id.txt_like);
            txt_time = itemView.findViewById(R.id.txt_time);

        }


    }


    public void setOnItemClickListener(final CommentAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }


    public void setOnItemClickListenerLike(final CommentAdapter.OnItemClickListenerLike mItemClickListenerlike) {
        this.mItemClickListenerlike = mItemClickListenerlike;
    }

    public interface OnItemClickListenerLike {

        void onItemClick(int position);
    }
}
