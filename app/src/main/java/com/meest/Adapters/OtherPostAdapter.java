package com.meest.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.master.exoplayer.MasterExoPlayer;
import com.master.exoplayer.MasterExoPlayerHelper;
import com.meest.Activities.LikeDetailsActivity;
import com.meest.Interfaces.PostItemsCallback;
import com.meest.R;
import com.meest.responses.FeedResponse;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;

import java.net.URLDecoder;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class OtherPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int SINGLE_MEDIA = 1;
    final int MULTIPLE_MEDIA = 2;
    final int IMAGE_MEDIA = 3;
    final int PROGRESS_LOADER = 6;
    PostItemsCallback itemCallback;
    private Context context;
    private ArrayList<FeedResponse.Row> feedResponse;
    private MasterExoPlayerHelper masterExoPlayerHelper;

    public OtherPostAdapter(ArrayList<FeedResponse.Row> feedResponse,
                            MasterExoPlayerHelper masterExoPlayerHelper,
                            Context context, PostItemsCallback itemCallback) {
        this.context = context;
        this.feedResponse = feedResponse;
        this.masterExoPlayerHelper = masterExoPlayerHelper;
        this.itemCallback = itemCallback;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case PROGRESS_LOADER:
                View progressLoaderView = LayoutInflater.from(context).inflate(R.layout.progress_loader, parent, false);
                return new ProgressLoaderViewHolder(progressLoaderView);
//                case SINGLE_MEDIA:
//                View singleMediaRow = LayoutInflater.from(context).inflate(R.layout.user_post_single_media_row, parent, false);
//                return new SingleMediaViewHolder(singleMediaRow);
//            case MULTIPLE_MEDIA:
//                View multipleMediaRow = LayoutInflater.from(context).inflate(R.layout.user_post_multiple_media, parent, false);
//                return new MultipleMediaViewHolder(multipleMediaRow);
//            case TEXT_MEDIA:
//                View textMediaRow = LayoutInflater.from(context).inflate(R.layout.user_post_text_media, parent, false);
//                return new TextMediaViewHolder(textMediaRow);
            default:
                View singleMediaRow = LayoutInflater.from(context).inflate(R.layout.user_post_single_media_row, parent, false);
                return new SingleMediaViewHolder(singleMediaRow);

//            default:
//                View imageMediaRow = LayoutInflater.from(context).inflate(R.layout.user_post_image_media, parent, false);
//                return new SingleImageViewHolder(imageMediaRow);
        }
    }

    @Override
    public int getItemViewType(int position) {
//    if(true){
//        return SINGLE_MEDIA;
//    }
        if (position == getItemCount() - 1) {
            return PROGRESS_LOADER;
        }

//        FeedResponse.Row model = feedResponse.get(position - 1);
//
//
//        if (model.getPostType().equalsIgnoreCase("writeText")) {
//            return TEXT_MEDIA;
//        } else if (model.getPostType().equalsIgnoreCase("feedPost")) {
//            return SINGLE_MEDIA;
//        } else if (model.getPostType().equalsIgnoreCase("feelingPost")) {
//            return TEXT_MEDIA;
//        }
        return SINGLE_MEDIA;
        /*if (model.getPostType().equalsIgnoreCase("feedPost")) {
            return MULTIPLE_MEDIA;
        } else if (model.getPostType().equalsIgnoreCase("writeText")) {
            if (model.getPosts().size() > 0) {
                if (model.getPosts().get(0).getIsText().equals("1")) {
                    return TEXT_MEDIA;
                } else {
                    return MULTIPLE_MEDIA;
                }
            }
        } else if (model.getPostType().equalsIgnoreCase("feelingPost")) {
            if (model.getPosts().size() > 0) {
                if (model.getPosts().get(0).getIsText().equals("1")) {
                    return TEXT_MEDIA;
                } else {
                    return MULTIPLE_MEDIA;
                }
            }
        } else if (feedResponse.get(position).getPosts().size() == 1 && feedResponse.get(position).getPosts().get(0).getImage() == 1) {
            return IMAGE_MEDIA;
        }
        return SINGLE_MEDIA;
    */
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ProgressLoaderViewHolder) {
            ProgressLoaderViewHolder mHolder = (ProgressLoaderViewHolder) holder;
            mHolder.image.setAnimation("loading.json");
            mHolder.image.playAnimation();
            mHolder.image.loop(true);
        } else {
            ViewHolder userViewHolder = (ViewHolder) holder;
            final FeedResponse.Row model = feedResponse.get(position);
            try {
                if (holder instanceof SingleMediaViewHolder) {
                    SingleMediaViewHolder mHolder = (SingleMediaViewHolder) holder;
                    if (model.getPosts().get(0).getVideo() == 1) {
                        mHolder.frame.setUrl(model.getPosts().get(0).getPost());
                        mHolder.image.setVisibility(GONE);
                        mHolder.frame.setVisibility(VISIBLE);
                        mHolder.frame.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mHolder.frame.setMute(!mHolder.frame.isMute());
                            }
                        });
                    } else if (model.getPosts().get(0).getImage() == 1) {
                        mHolder.frame.setUrl("");
                        Glide.with(mHolder.itemView.getContext())
                                .load(model.getPosts().get(0).getPost())
                                .into(mHolder.image);
                        mHolder.image.setVisibility(VISIBLE);
                        mHolder.frame.setVisibility(GONE);
                        mHolder.frame.setMute(true);
                    } else if (model.getPosts().get(0).getIsText().equals("1")) {
                        mHolder.frame.setUrl("");
                        mHolder.postText.setText(model.getPosts().get(0).getPost());
                        mHolder.postText.setVisibility(VISIBLE);
                        mHolder.progressBar.setVisibility(GONE);
                        mHolder.image.setVisibility(GONE);
                        mHolder.frame.setVisibility(GONE);
                        mHolder.frame.setMute(true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*try {

                if (holder instanceof SingleImageViewHolder) {
                    SingleImageViewHolder mHolder = (SingleImageViewHolder) holder;
                    Glide.with(mHolder.itemView.getContext())
                            .load(model.getPosts().get(0).getPost())
                            .into(mHolder.image);
                } else if (holder instanceof MultipleMediaViewHolder) {
                    MultipleMediaViewHolder mHolder = (MultipleMediaViewHolder) holder;
                    mHolder.setAdapter();
                } else if (holder instanceof SingleMediaViewHolder) {
                    SingleMediaViewHolder mHolder = (SingleMediaViewHolder) holder;
                    if (model.getPosts().get(0).getVideo() == 1) {
                        mHolder.frame.setUrl(model.getPosts().get(0).getPost());
                    } else if (model.getPosts().get(0).getImage() == 1) {
                        Glide.with(mHolder.itemView.getContext())
                                .load(model.getPosts().get(0).getPost())
                                .into(mHolder.image);
                    } else if (model.getPosts().get(0).getIsText().equals("1")) {
                        Glide.with(mHolder.itemView.getContext())
                                .load(model.getPosts().get(0).getPost())
                                .into(mHolder.image);
                    }
                } else {
                    try {
                        TextMediaViewHolder mHolder = (TextMediaViewHolder) holder;
//                    ((ViewHolder) holder).viewPager.setVisibility(GONE);
//                    mHolder.postText.setVisibility(VISIBLE);
//                    ((ViewHolder) holder).postText.setText(model.getPosts().get(0).getPost());
//                    ((ViewHolder) holder).postText.setTextColor(Color.parseColor(model.getPosts().get(0).getTextColorCode()));
                        Glide.with(mHolder.itemView.getContext())
                                .load(model.getPosts().get(0).getPost())
                                .into(mHolder.image);

                        if (model.getActivityPost() != null && model.getSubActivityPost() != null) {
                            userViewHolder.feeling_post_layout.setVisibility(VISIBLE);
                            Glide.with(context).load(model.getSubActivityPost().getIcon()).into(userViewHolder.imvFeelIcon);
                            userViewHolder.feeling1.setText(" " + "is " + " " + model.getActivityPost().getTitle() + " " + model.getSubActivityPost().getActivityType());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            try {
                if (model.getTags() != null) {
                    if (model.getTags().size() > 0) {
                        ((ViewHolder) holder).iv_tag.setVisibility(VISIBLE);
                        ((ViewHolder) holder).iv_tag.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                itemCallback.openTagsBottomSheet(model.getTags());
                            }
                        });
                    }
                }
                userViewHolder.txt_usernsme.setText(model.getUser().getUsername());

                if (model.getPostSaved() == 1) {
                    userViewHolder.img_save.setImageResource(R.drawable.ic_bookmark_fill);
                }


                if (model.getLiked() == 1) {
                    userViewHolder.txt_like_txt.setText(context.getString(R.string.Like));
                    userViewHolder.img_like.setImageResource(R.drawable.like_diamond_filled);
                } else {
                    userViewHolder.txt_like_txt.setText(context.getString(R.string.Like));
                    userViewHolder.img_like.setImageResource(R.drawable.diamond);
                }

                try {
                    if (model.getUser().getDisplayPicture() == null) {
                        userViewHolder.img_profile.setImageResource(R.drawable.ic_profile_icons_avtar);
                    } else {
                        Glide.with(context).load(model.getUser().getDisplayPicture()).into(userViewHolder.img_profile);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                String caption = URLDecoder.decode(model.getCaption(), "UTF-8");
                userViewHolder.txt_data.setText(caption);

                if (model.getCommentCounts() < 1) {
                    userViewHolder.txt_comment.setText(model.getCommentCounts() + " " + context.getResources().getString(R.string.Comment));
                } else {
                    userViewHolder.txt_comment.setText(model.getCommentCounts() + " " + context.getResources().getString(R.string.Comment));
                }
                if (model.getLikeCounts() != 0) {
                    userViewHolder.txt_like.setText("" + model.getLikeCounts());
                    userViewHolder.txt_recent_user_name.setText(" " + model.getPostLikes().get(0).getUser().getUsername() + " ");
                    if (model.getLikeCounts() == 1) {
                        userViewHolder.text_others_like.setVisibility(GONE);
                        userViewHolder.and_text.setVisibility(GONE);
                    } else {
                        userViewHolder.text_others_like.setText(" " + (model.getLikeCounts() - 1) + context.getString(R.string.others));
                    }
                    userViewHolder.layout_likes_details.setVisibility(VISIBLE);
                } else {
                    userViewHolder.txt_like.setText("0");
                    userViewHolder.layout_likes_details.setVisibility(GONE);
                }

                userViewHolder.layout_like_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, LikeDetailsActivity.class);
                        intent.putExtra("postId", model.getId());
                        context.startActivity(intent);
                    }
                });

                userViewHolder.txt_time.setText(model.getCreatedAt());

                userViewHolder.img_profile_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemCallback.openOtherUserProfile(model.getUserId());
                    }
                });

                userViewHolder.layout_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        Toast.makeText(context, "OtherPostAdapter", Toast.LENGTH_SHORT).show();
                        itemCallback.likePost(model.getId(), userViewHolder.txt_like_txt, userViewHolder.img_like, userViewHolder.txt_like,
                                userViewHolder.layout_likes_details, userViewHolder.txt_recent_user_name, userViewHolder.text_others_like, userViewHolder.and_text, position, userViewHolder.layout_like_list);
                    }
                });
                userViewHolder.layout_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemCallback.userCommentClicked(position, model.getId(), model.getCaption(), model.getAllowComment(), false, model.getCommentCounts());
                    }
                });

                userViewHolder.layout_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemCallback.sharePostClicked(position);
                    }
                });

                userViewHolder.img_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemCallback.saveClicked(model.getId(), position, userViewHolder.img_save);
                    }
                });

                userViewHolder.img_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            itemCallback.menuClicked(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                userViewHolder.img_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, OtherUserAccount.class)
                                .putExtra("userId", model.getUserId()));
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public int getItemCount() {
        return feedResponse == null || feedResponse.size() == 0 ? 0 : feedResponse.size() + 1;
//        return feedResponse == null ? 0 : feedResponse.size() ;
    }

    public void notifyFeedsDataSetChanged(ArrayList<FeedResponse.Row> feedArrayList) {
        this.feedResponse = feedArrayList;
        notifyDataSetChanged();
    }

    class SingleMediaViewHolder extends ViewHolder {
        MasterExoPlayer frame;
        ImageView image;

        public SingleMediaViewHolder(@NonNull View itemView) {
            super(itemView);
            frame = itemView.findViewById(R.id.frame);
            image = itemView.findViewById(R.id.image);
        }
    }

    class SingleImageViewHolder extends ViewHolder {
        ImageView image;

        public SingleImageViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

    class TextMediaViewHolder extends ViewHolder {
        ImageView image;

        public TextMediaViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

    class ProgressLoaderViewHolder extends ViewHolder {
        LottieAnimationView image;

        public ProgressLoaderViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.loading);


        }
    }

    class MultipleMediaViewHolder extends ViewHolder {
        MyAdapter adapter;
        RecyclerView recyclerView;

        public MultipleMediaViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);

        }

        PagerSnapHelper pagerSnapHelper;

        void setAdapter() {
            adapter = new MyAdapter(feedResponse.get(getAdapterPosition()));
            recyclerView.setAdapter(adapter);
            if (pagerSnapHelper == null) {
                pagerSnapHelper = new PagerSnapHelper();
            }
            pagerSnapHelper.attachToRecyclerView(recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false));
            masterExoPlayerHelper.attachToRecyclerView(recyclerView);
        }

        class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> {
            FeedResponse.Row row;

            public MyAdapter(FeedResponse.Row row) {
                this.row = row;
            }

            @NonNull
            @Override
            public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_rows, null));
            }

            @Override
            public void onBindViewHolder(@NonNull Holder holder, int position) {
//                holder.frame.setUrl(row.getPosts().get(position).getPost());
                holder.frame.setUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4");
            }

            @Override
            public int getItemCount() {
                return row.getPosts().size();
            }

            public class Holder extends RecyclerView.ViewHolder {
                MasterExoPlayer frame;
                ImageView image;

                public Holder(@NonNull View itemView) {
                    super(itemView);
                    frame = itemView.findViewById(R.id.frame);
                    image = itemView.findViewById(R.id.image);
                }
            }
        }


    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressForAd;
        CircleImageView img_profile;
        RelativeLayout img_profile_layout;
        EmojiconTextView txt_data;
        TextView txt_usernsme, txt_time, txt_like, txt_comment,
                txt_dis_like, txt_recent_user_name, text_others_like, and_text,
                txt_like_txt;
        LinearLayout pager_dots, layout_share;
        LinearLayout layout_like, layout_dislike, layout_comment;
        LinearLayout layout_likes_details;
        ImageView img_dislike, img_like, img_save;
        LottieAnimationView loading;
        RelativeLayout img_menu;
        ProgressBar progressBar;
        TextView feeling1, postText;
        LinearLayout feeling_post_layout;
        LinearLayout layout_like_list;
        ImageView iv_tag, imvFeelIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressForAd = itemView.findViewById(R.id.progressForAd);
            feeling_post_layout = itemView.findViewById(R.id.feeling_post_layout);
            iv_tag = itemView.findViewById(R.id.iv_tag);
            feeling1 = itemView.findViewById(R.id.feeling1);
            imvFeelIcon = itemView.findViewById(R.id.imvFeelIcon);
            txt_usernsme = itemView.findViewById(R.id.txt_usernsme);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_like_txt = itemView.findViewById(R.id.txt_like);
            txt_like = itemView.findViewById(R.id.txt_like_users);
            txt_comment = itemView.findViewById(R.id.txt_comment);
            txt_dis_like = itemView.findViewById(R.id.txt_dis_like);
            img_profile = itemView.findViewById(R.id.img_profile);
            and_text = itemView.findViewById(R.id.and_text);
            img_profile_layout = itemView.findViewById(R.id.img_profile_layout);
            txt_data = itemView.findViewById(R.id.txt_data);
            text_others_like = itemView.findViewById(R.id.text_others_like);
            pager_dots = itemView.findViewById(R.id.pager_dots);
            layout_like = itemView.findViewById(R.id.layout_like);
            layout_dislike = itemView.findViewById(R.id.layout_dislike);
            txt_recent_user_name = itemView.findViewById(R.id.txt_recent_user_name);
            layout_comment = itemView.findViewById(R.id.layout_comment);
            layout_share = itemView.findViewById(R.id.layout_share);
            layout_likes_details = itemView.findViewById(R.id.layout_likes_details);
            img_dislike = itemView.findViewById(R.id.img_dislike);
            img_like = itemView.findViewById(R.id.img_like);
            img_save = itemView.findViewById(R.id.img_save);
            loading = itemView.findViewById(R.id.loading);
            loading.setAnimation("saved_icon.json");
            loading.playAnimation();
            loading.loop(true);
            img_menu = itemView.findViewById(R.id.img_menu);
            progressBar = itemView.findViewById(R.id.progressBar);
            postText = itemView.findViewById(R.id.postText);
            layout_like_list = itemView.findViewById(R.id.layout_like_list);
        }
    }


}
