package com.meest.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.UserActivityResponse;
import com.meest.databinding.ActivityAdapterBinding;

import java.util.ArrayList;
import java.util.List;

public class UserActivityAdapter extends RecyclerView.Adapter<UserActivityAdapter.PeopleViewHolder> {
    List<UserActivityResponse.Row> userResponseList=new ArrayList<>();
    Context mContext;
    private LayoutInflater layoutInflater;

    UserActivityAdapter.OnItemClickListenerUnFollow mOnItemClickListenerUnFollow;


//    public UserActivityAdapter(List<UserActivityResponse.Row> userResponseList, Context mContext) {
//        this.userResponseList = userResponseList;
//        this.mContext = mContext;
//    }

    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new PeopleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {
        holder.userActivityList(userResponseList.get(position));

    }

    @Override
    public int getItemCount() {
        return userResponseList == null ? 0 : userResponseList.size();
    }

    public void addLoading(List<UserActivityResponse.Row> rows) {
        this.userResponseList.addAll(rows);
        Log.e("TAG", "addLoading: "+rows.get(1).getPostId());
        notifyDataSetChanged();
    }


    public class PeopleViewHolder extends RecyclerView.ViewHolder {

        ActivityAdapterBinding activitylistBinding;
//        public ImageView profile_img, img_post, ivIcon;
//        public TextView userActivityStatus;
//        public TextView userActivityDate;


        public PeopleViewHolder(@NonNull View itemView) {
            super(itemView);
            activitylistBinding=DataBindingUtil.bind(itemView);
//            profile_img = itemView.findViewById(R.id.img_profile);
//            userActivityStatus = itemView.findViewById(R.id.name);
//            userActivityDate = itemView.findViewById(R.id.name2);
//
//            img_post = itemView.findViewById(R.id.img_post);
//            ivIcon = itemView.findViewById(R.id.ivIcon);

        }


        public void userActivityList(UserActivityResponse.Row row) {

            activitylistBinding.setMActivityadaptermodel(row);

//            try {
//                if (row.getType().equalsIgnoreCase("Story")) {
//                   setStoryData(row);
//                } else if (row.getType().equalsIgnoreCase("Comment")) {
//                    setCommentData(row);
//                    activitylistBinding.ivIcon.setImageResource(R.drawable.comment_icon);
//                } else if (row.getType().equalsIgnoreCase("Following")) {
//                    setFollowingData(row);
////                      holder.ivIcon.setImageResource(R.drawable.diamond_fill);
//
//                } else if (row.getType().equalsIgnoreCase("like")) {
//                    setLikeData(row);
//                    activitylistBinding.ivIcon.setImageResource(R.drawable.like_diamond_filled);
//
//                } else if (row.getType().equalsIgnoreCase("post")) {
//                    setPostData(row);
//                    activitylistBinding.ivIcon.setImageResource(R.drawable.ic_add_post);
//
//                } else if (row.getType().equalsIgnoreCase("bookmark")) {
//                   setBookmarkData(row);
//                   activitylistBinding.ivIcon.setImageResource(R.drawable.ic_bookmark_fill);
//
//                }
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
/*
        public void setStoryData(UserActivityResponse.Row row) {
            try {
                Glide.with(activitylistBinding.getRoot().getContext())
                        .load(row.getStorieDatas().getStory())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .skipMemoryCache(false)
                                .error(R.drawable.placeholder))
                        .into(activitylistBinding.imgProfile);
                activitylistBinding.name.setText(mContext.getString(R.string.You_posted_a_story));
                activitylistBinding.name2.setText(row.getCreatedAt());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setFollowingData(UserActivityResponse.Row row) {
            try {

                activitylistBinding.name.setText(mContext.getString(R.string.You_stated_follrowing) + " " + row.getFollowingData().getFollowingData().getUserName());
                activitylistBinding.name2.setText(row.getCreatedAt());
                Glide.with(activitylistBinding.getRoot().getContext())
                        .load(row.getFollowingData().getFollowingData().getDisplayPicture())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .skipMemoryCache(false)
                                .error(R.drawable.placeholder))
                        .into(activitylistBinding.imgProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setCommentData(UserActivityResponse.Row row) {
            try {

                activitylistBinding.name.setText(mContext.getString(R.string.comment) + " " + CommonUtils.decodeEmoji(row.getPostcomments().getComment()));
                activitylistBinding.name2.setText(row.getCreatedAt());
                Glide.with(activitylistBinding.getRoot().getContext())
                        .load(row.getPostDatas().getPosts().get(0).getPost())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .skipMemoryCache(false)
                                .error(R.drawable.placeholder))
                        .into(activitylistBinding.imgProfile);


                Glide.with(activitylistBinding.getRoot().getContext())
                        .load(row.getPostDatas().getPosts().get(0).getPost())
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                .skipMemoryCache(false)
                                .error(R.drawable.placeholder))
                        .into(activitylistBinding.imgPost);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void setLikeData(UserActivityResponse.Row row) {
            try {
                activitylistBinding.name.setText(mContext.getString(R.string.You_liked_a_post));
                activitylistBinding.name2.setText(row.getCreatedAt());
                if (row.getPostDatas().getPosts().get(0).getVideo() != null && row.getPostDatas().getPosts().get(0).getVideo() == 1) {
                    Glide.with(activitylistBinding.getRoot().getContext())
                            .load(row.getPostDatas().getPosts().get(0).getThumbnail())
                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .skipMemoryCache(false)
                                    .error(R.drawable.placeholder))
                            .into(activitylistBinding.imgProfile);
                } else {
                    Glide.with(activitylistBinding.getRoot().getContext())
                            .load(row.getPostDatas().getPosts().get(0).getPost())

                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .skipMemoryCache(false)
                                    .error(R.drawable.placeholder))
                            .into(activitylistBinding.imgProfile);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public void setBookmarkData(UserActivityResponse.Row row) {
            try {
                activitylistBinding.name.setText(mContext.getString(R.string.You_bookmarked_a_post));
                activitylistBinding.name2.setText(row.getCreatedAt());
                if (row.getPostDatas().getPosts().get(0).getVideo() != null && row.getPostDatas().getPosts().get(0).getVideo() == 1) {
                    Glide.with(activitylistBinding.getRoot().getContext())
                            .load(row.getPostDatas().getPosts().get(0).getThumbnail())
                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .skipMemoryCache(false)
                                    .error(R.drawable.placeholder))
                            .into(activitylistBinding.imgProfile);
                } else {
                    Glide.with(activitylistBinding.getRoot().getContext())
                            .load(row.getPostDatas().getPosts().get(0).getPost())

                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .skipMemoryCache(false)
                                    .error(R.drawable.placeholder))
                            .into(activitylistBinding.imgProfile);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setPostData(UserActivityResponse.Row row) {
            try {
                activitylistBinding.name.setText(mContext.getString(R.string.You_created_a_post));
                activitylistBinding.name2.setText(row.getCreatedAt());
                if (row.getPostDatas().getPosts().get(0).getVideo() != null && row.getPostDatas().getPosts().get(0).getVideo() == 1) {
                    Glide.with(activitylistBinding.getRoot().getContext())
                            .load(row.getPostDatas().getPosts().get(0).getThumbnail())
                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .skipMemoryCache(false)
                                    .error(R.drawable.placeholder))
                            .into(activitylistBinding.imgProfile);
                } else {
                    Glide.with(activitylistBinding.getRoot().getContext())
                            .load(row.getPostDatas().getPosts().get(0).getPost())

                            .apply(new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .skipMemoryCache(false)
                                    .error(R.drawable.placeholder))
                            .into(activitylistBinding.imgProfile);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/


    }

    public void setOnItemClickListenerUnFollow(final UserActivityAdapter.OnItemClickListenerUnFollow mOnItemClickListenerUnFollow) {
        this.mOnItemClickListenerUnFollow = mOnItemClickListenerUnFollow;
    }

    public interface OnItemClickListenerUnFollow {
        void onItemClick(int position);
    }


}
