package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.meest.R;
import com.meest.responses.UserProfileFetchResponse;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherUserPhotoListAdapter extends RecyclerView.Adapter<OtherUserPhotoListAdapter.ViewHolder> {

    private ArrayList<UserProfileFetchResponse.Row> profile_data;
    OtherUserPhotoListAdapter.OnItemClickListener mItemClickListener;
    Context context;
    OtherUserPhotoListAdapter.OnItemClickListenerDisLike mItemClickListenerdisike;
    OtherUserPhotoListAdapter.OnItemClickListenerComment mItemClickListenerComment;
    OtherUserPhotoListAdapter.OnItemClickListenerOptions OnItemClickListenerOptions;
    ArrayList<UserProfileFetchResponse> data1 = new ArrayList<>();
    ArrayList<UserProfileFetchResponse> data;
    ArrayList<Integer> like_count;
    ArrayList<Integer> dis_like_count;
    ArrayList<Integer> cmnt_like_count;
    ViewPagerAdapterForPost viewPagerAdapter;
    private int dotscount;
    private ImageView[] dots;
    ArrayList<UserProfileFetchResponse.Row> feed_data_post;
    ArrayList<Integer> liked_details;
    ArrayList<Integer> dis_liked_details;

    public OtherUserPhotoListAdapter(Context context, ArrayList<UserProfileFetchResponse> data, ArrayList<Integer> like_count, ArrayList<Integer> dis_like_count, ArrayList<Integer> cmnt_like_count,
                       ArrayList<UserProfileFetchResponse.Row> feed_data_post, ArrayList<Integer> liked_details, ArrayList<Integer> dis_liked_details) {
        this.context = context;
        this.data = data;
        this.profile_data = feed_data_post;
        this.like_count = like_count;
        this.dis_like_count = dis_like_count;
        this.cmnt_like_count = cmnt_like_count;
        this.liked_details = liked_details;
        this.feed_data_post = feed_data_post;
        this.dis_liked_details = dis_liked_details;
    }

    @NonNull
    @Override
    public OtherUserPhotoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_post_listview_adapter,parent,false);
        OtherUserPhotoListAdapter.ViewHolder HPVH = new OtherUserPhotoListAdapter.ViewHolder(v);
        return HPVH;

    }


    @Override
    public void onBindViewHolder(@NonNull OtherUserPhotoListAdapter.ViewHolder holder, int position) {
        final OtherUserPhotoListAdapter.ViewHolder userViewHolder = (OtherUserPhotoListAdapter.ViewHolder) holder;

       /* userViewHolder.img_profile_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, OtherUserActivity.class));
            }
        });

        if (liked_details.get(position) == 1) {
            userViewHolder.img_like.setImageResource(R.drawable.diamond_filled);
        } else {
            userViewHolder.img_like.setImageResource(R.drawable.ic_like_icon);
        }


        if (dis_liked_details.get(position) == 1) {
            userViewHolder.img_dislike.setImageResource(R.drawable.ic_feel_dislike_icon);
        } else {
            userViewHolder.img_dislike.setImageResource(R.drawable.ic_dislike_icon);
        }

        try {
            if (data.get(position).getData().getUser().getDisplayPicture() == null) {

            } else {
                Picasso.get().load(data.get(position).getData().getUser().getDisplayPicture()).into(userViewHolder.img_profile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        userViewHolder.txt_data.setText(data.get(position).getCaption());


        if (data1.get(position).getData().getUser().getPosts().get(i).getPostLikes() == 1) {
            Picasso.get().load(data.get(position).getPostLikes().get(0).getUser().getDisplayPicture()).into(userViewHolder.img1);
            userViewHolder.img1.setVisibility(View.VISIBLE);
            userViewHolder.img2.setVisibility(View.GONE);
            userViewHolder.img3.setVisibility(View.GONE);
            userViewHolder.img4.setVisibility(View.GONE);
        } else if (data.get(position).getPostLikes().size() == 2) {
            Picasso.get().load(data.get(position).getPostLikes().get(0).getUser().getDisplayPicture()).into(userViewHolder.img1);
            Picasso.get().load(data.get(position).getPostLikes().get(1).getUser().getDisplayPicture()).into(userViewHolder.img2);
            userViewHolder.img1.setVisibility(View.VISIBLE);
            userViewHolder.img2.setVisibility(View.VISIBLE);
            userViewHolder.img3.setVisibility(View.GONE);
            userViewHolder.img4.setVisibility(View.GONE);
        } else if (data.get(position).getPostLikes().size() == 3) {
            Picasso.get().load(data.get(position).getPostLikes().get(0).getUser().getDisplayPicture()).into(userViewHolder.img1);
            Picasso.get().load(data.get(position).getPostLikes().get(1).getUser().getDisplayPicture()).into(userViewHolder.img2);
            Picasso.get().load(data.get(position).getPostLikes().get(2).getUser().getDisplayPicture()).into(userViewHolder.img3);
            userViewHolder.img1.setVisibility(View.VISIBLE);
            userViewHolder.img2.setVisibility(View.VISIBLE);
            userViewHolder.img3.setVisibility(View.VISIBLE);
            userViewHolder.img4.setVisibility(View.GONE);
        } else if (data.get(position).getPostLikes().size() == 4) {
            Picasso.get().load(data.get(position).getPostLikes().get(0).getUser().getDisplayPicture()).into(userViewHolder.img1);
            Picasso.get().load(data.get(position).getPostLikes().get(1).getUser().getDisplayPicture()).into(userViewHolder.img2);
            Picasso.get().load(data.get(position).getPostLikes().get(2).getUser().getDisplayPicture()).into(userViewHolder.img3);
            Picasso.get().load(data.get(position).getPostLikes().get(3).getUser().getDisplayPicture()).into(userViewHolder.img4);
            userViewHolder.img1.setVisibility(View.VISIBLE);
            userViewHolder.img2.setVisibility(View.VISIBLE);
            userViewHolder.img3.setVisibility(View.VISIBLE);
            userViewHolder.img4.setVisibility(View.VISIBLE);
        } else if (data.get(position).getPostLikes().size() == 5) {
            Picasso.get().load(data.get(position).getPostLikes().get(0).getUser().getDisplayPicture()).into(userViewHolder.img1);
            Picasso.get().load(data.get(position).getPostLikes().get(1).getUser().getDisplayPicture()).into(userViewHolder.img2);
            Picasso.get().load(data.get(position).getPostLikes().get(2).getUser().getDisplayPicture()).into(userViewHolder.img3);
            Picasso.get().load(data.get(position).getPostLikes().get(3).getUser().getDisplayPicture()).into(userViewHolder.img4);
            userViewHolder.img1.setVisibility(View.VISIBLE);
            userViewHolder.img2.setVisibility(View.VISIBLE);
            userViewHolder.img3.setVisibility(View.VISIBLE);
            userViewHolder.img4.setVisibility(View.VISIBLE);
        }


        userViewHolder.txt_dis_like.setText(dis_like_count.get(position).toString());
        userViewHolder.txt_comment.setText(cmnt_like_count.get(position).toString());

        userViewHolder.txt_like.setText(like_count.get(position).toString());
        if (data.get(position).getPostLikes().size() != 0) {
            userViewHolder.txt_like_users.setText(" " + String.valueOf(like_count.get(position) - 1) + " Others");
            userViewHolder.txt_recent_user_name.setText(" " + data.get(position).getPostLikes().get(0).getUser().getUsername() + " ");
            userViewHolder.layout_likes_details.setVisibility(View.VISIBLE);
        } else {
            userViewHolder.layout_likes_details.setVisibility(View.GONE);
        }

        if (data.get(position).getPostLikes().get(0).getUser().getUsername() != null){

        }

        userViewHolder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.slider_not_select));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.slider_select));

                //Toast.makeText(getApplicationContext(),String.valueOf(position),Toast.LENGTH_LONG).show();

                if (position+1 == demo){
                    txt_next.setVisibility(View.VISIBLE);
                }else {
                    txt_next.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //
        // viewPagerAdapter.notifyDataSetChanged();
        // viewPagerAdapter = new ViewPagerAdapterForPost(sliderImg, context);
        viewPagerAdapter = new ViewPagerAdapterForPost(data.get(position).getPosts(), context);
        userViewHolder.viewPager.setAdapter(viewPagerAdapter);



        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];




        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(context);
            dots[i].setImageDrawable(ContextCompat.getDrawable(context,R.drawable.slider_not_select ));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);

            params.setMargins(8, 0, 8, 0);

            userViewHolder.SliderDots.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(context, R.drawable.slider_select));


        userViewHolder.txt_time.setText(data.get(position).getData().getUser().getCreatedAt());
        userViewHolder.txt_usernsme.setText(data.get(position).getData().getUser().getUsername());

        userViewHolder.layout_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);


                Map<String, String> map = new HashMap<>();
                map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);


                InsertLikeParameters insertLikeParameters = new InsertLikeParameters(data.get(position).getId());
                Call<InsertPostLikeResponse> call1 = webApi1.insertLike(map, insertLikeParameters);
                call1.enqueue(new Callback<InsertPostLikeResponse>() {
                    @Override
                    public void onResponse(Call<InsertPostLikeResponse> call, Response<InsertPostLikeResponse> response) {
                        try {
                            if (response.body().getCode() == 1) {
                                // Utilss.showToast(getContext(),"Liked",R.color.green);

                                if (userViewHolder.img_like.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.diamond_filled).getConstantState()) {
                                    userViewHolder.img_like.setImageResource(R.drawable.ic_like_icon);
                                    // new RegisterAsyntaskNew().execute();
                                } else {
                                    userViewHolder.img_like.setImageResource(R.drawable.diamond_filled);
                                    // new RegisterAsyntask().execute();
                                }

                                if (liked_details.get(position) == 0) {
                                    like_count.set(position, response.body().getData().getLikeCount());
                                    liked_details.set(position, 1);

                                    if (dis_liked_details.get(position) == 1) {
                                        dis_like_count.set(position, response.body().getData().getDislikeCount());
                                        dis_liked_details.set(position, 0);
                                    }
                                                else {
                                                    dis_like_count.set(position,dis_like_count.get(position) + 1);
                                                    dis_liked_details.set(position, 1);
                                                }

                                                FeedResponse.Row feedResponse = new FeedResponse.Row();
                                                feedResponse.setLiked(like_count.get(position) + 1);
                                                feed_data.set(position,feedResponse);
                                                //feed_data.add(position,feedResponse.setLiked(like_count.get(position) + 1));
                                                feed_data.add(feedResponse);
                                } else {
                                    like_count.set(position, response.body().getData().getLikeCount());
                                    liked_details.set(position, 0);

                                                if (dis_liked_details.get(position) == 0 ){
                                                    dis_like_count.set(position,response.body().getData().getDislikeCount());
                                                    dis_liked_details.set(position, 1);
                                                }
                                                if (dis_liked_details.get(position) == 0 ){
                                                    dis_like_count.set(position,dis_like_count.get(position) + 1);
                                                    dis_liked_details.set(position, 1);
                                                }

                                    // FeedResponse.Row feedResponse = new FeedResponse.Row();
                                    //  feed_data.add(position,feedResponse.setLiked(2));
                                    //FeedResponse.Row feedResponse = new FeedResponse.Row();
                                    //feedResponse.setLiked(like_count.get(position) - 1);
                                    // feed_data.set(position,feedResponse);

                                }


                                // notifyDataSetChanged();

                                //arrayList.set(position,String.valueOf(feed_data.get(position).getLikeCounts() + 1))





                                if (liked_details.get(position) == 1){
                                    userViewHolder.img_like.setImageResource(R.drawable.diamond_filled);
                                }else {
                                    userViewHolder.img_like.setImageResource(R.drawable.ic_like_icon);
                                }


                                userViewHolder.txt_like.setText(like_count.get(position).toString());
                                if (data.get(position).getPostLikes().size() != 0) {
                                    userViewHolder.txt_like_users.setText(" " + String.valueOf(like_count.get(position) - 1) + " Others");
                                    userViewHolder.txt_recent_user_name.setText(" " + data.get(position).getPostLikes().get(0).getUser().getUsername() + " ");
                                    userViewHolder.layout_likes_details.setVisibility(View.VISIBLE);
                                } else {
                                    userViewHolder.layout_likes_details.setVisibility(View.GONE);
                                }


                            } else {
                                Utilss.showToast(context, response.body().getSuccess().toString(), R.color.msg_fail);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<InsertPostLikeResponse> call, Throwable t) {
                        Log.w("error", t);
                        Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });*/

        userViewHolder.layout_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListenerdisike != null)
                    mItemClickListenerdisike.onItemClick(position);
            }
        });


        userViewHolder.layout_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListenerComment != null)
                    mItemClickListenerComment.onItemClick(position);
            }
        });


//        userViewHolder.loading.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                userViewHolder.img_save.setVisibility(View.VISIBLE);
//                //userViewHolder.loading.setVisibility(View.GONE);
//            }
//        });


        userViewHolder.img_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          //      userViewHolder.loading.setVisibility(View.VISIBLE);
                userViewHolder.img_save.setVisibility(View.GONE);
            }
        });


    /*    userViewHolder.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (OnItemClickListenerOptions != null)
                    OnItemClickListenerOptions.onItemClick(position);
            }
        });*/


        userViewHolder.img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // context.startActivity(new Intent(context, OtherUserActivity.class).putExtra("userId",data.get(position).getData().getUser().getId()));
            }
        });


/*        holder.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(position);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return profile_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ViewPager viewPager;
        CircleImageView img_profile;
        RelativeLayout img_profile_layout;
        de.hdodenhof.circleimageview.CircleImageView img1,img2,img3,img4;
        TextView txt_usernsme,txt_time,txt_like,txt_comment,txt_data,txt_dis_like,txt_recent_user_name,txt_like_users;
        LinearLayout SliderDots;
        RelativeLayout layout_like,layout_dislike,layout_comment;
        LinearLayout layout_likes_details;
        ImageView img_dislike,img_like,img_save;

        RelativeLayout img_menu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_usernsme = itemView.findViewById(R.id.txt_usernsme);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_like = itemView.findViewById(R.id.txt_like);
            txt_comment = itemView.findViewById(R.id.txt_comment);

            txt_dis_like = itemView.findViewById(R.id.txt_dis_like);
            img_profile = itemView.findViewById(R.id.img_profile);
            img_profile_layout = itemView.findViewById(R.id.img_profile_layout);
            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            img3 = itemView.findViewById(R.id.img3);
            img4 = itemView.findViewById(R.id.img4);
            txt_data = itemView.findViewById(R.id.txt_data);
            SliderDots = itemView.findViewById(R.id.SliderDots);
            viewPager = itemView.findViewById(R.id.viewPager);
            layout_like = itemView.findViewById(R.id.layout_like);
            layout_dislike = itemView.findViewById(R.id.layout_dislike);
            txt_recent_user_name = itemView.findViewById(R.id.txt_recent_user_name);
            txt_like_users = itemView.findViewById(R.id.txt_like_users);
            layout_comment = itemView.findViewById(R.id.layout_comment);
            layout_likes_details = itemView.findViewById(R.id.layout_likes_details);

            img_dislike = itemView.findViewById(R.id.img_dislike);
            img_like = itemView.findViewById(R.id.img_like);
            img_save = itemView.findViewById(R.id.img_save);
          /*  loading = itemView.findViewById(R.id.loading);
            loading.setAnimation("saved_icon.json");
            loading.playAnimation();
            loading.loop(true);*/

//            img_menu = itemView.findViewById(R.id.img_menu);

        }
    }


    public void setOnItemClickListener(final OtherUserPhotoListAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }


    public void setOnItemClickListenerDislike(final OtherUserPhotoListAdapter.OnItemClickListenerDisLike mItemClickListenerdisike) {
        this.mItemClickListenerdisike = mItemClickListenerdisike;
    }

    public interface OnItemClickListenerDisLike {

        void onItemClick(int position);
    }


    public void setOnItemClickListenerComment(final OtherUserPhotoListAdapter.OnItemClickListenerComment mItemClickListenerComment) {
        this.mItemClickListenerComment = mItemClickListenerComment;
    }

    public interface OnItemClickListenerComment {

        void onItemClick(int position);
    }

    public void setOnItemClickListenerOptions(final OtherUserPhotoListAdapter.OnItemClickListenerOptions OnItemClickListenerOptions) {
        this.OnItemClickListenerOptions = OnItemClickListenerOptions;
    }

    public interface OnItemClickListenerOptions {

        void onItemClick(int position);
    }



}
