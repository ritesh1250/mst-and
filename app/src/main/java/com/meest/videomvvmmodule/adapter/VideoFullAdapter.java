
package com.meest.videomvvmmodule.adapter;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.meest.R;
import com.meest.databinding.ItemVideoListBinding;
import com.meest.videomvvmmodule.customview.transformation.BlurTransformation;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.GlobalApi;

import java.util.ArrayList;
import java.util.List;

public class VideoFullAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //    private static final int AD_TYPE = 1;
    private static final int POST_TYPE = 2;
    private List<Video.Data> mList = new ArrayList<>();
    private OnRecyclerViewItemClick onRecyclerViewItemClick;
    int itemToPlay = 0;
    private String postId = "";
    //    private UnifiedNativeAd unifiedNativeAd;
//    private NativeAd facebookNativeAd;
    SimpleExoPlayer player;
    public Context context;

//    public UnifiedNativeAd getUnifiedNativeAd() {
//        return unifiedNativeAd;
//    }
//
//    public void setUnifiedNativeAd(UnifiedNativeAd unifiedNativeAd) {
//        this.unifiedNativeAd = unifiedNativeAd;
//    }
//
//    public NativeAd getFacebookNativeAd() {
//        return facebookNativeAd;
//    }
//
//    public void setFacebookNativeAd(NativeAd facebookNativeAd) {
//        this.facebookNativeAd = facebookNativeAd;
//    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

//    public int getItemToPlay() {
//        return itemToPlay;
//    }
//
//    public void setItemToPlay(int itemToPlay) {
//        this.itemToPlay = itemToPlay;
//    }

    public OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    public List<Video.Data> getmList() {
        return mList;
    }
    public void setmList(List<Video.Data> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /* if (viewType == AD_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ads_lay, parent, false);
            return new AdsViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list, parent, false);
            return new VideoFullViewHolder(view);
        }*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list, parent, false);
        return new VideoFullViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof VideoFullViewHolder) {
            VideoFullViewHolder holder = (VideoFullViewHolder) viewHolder;
            holder.setModel(position);
        } /*else {
            if (viewHolder instanceof AdsViewHolder) {
                AdsViewHolder holder = (AdsViewHolder) viewHolder;
                if (unifiedNativeAd != null) {
                    holder.binding.frame.setVisibility(View.VISIBLE);
                    LinearLayout frameLayout = holder.binding.frame;
                    UnifiedNativeAdView
                            adView = (UnifiedNativeAdView) LayoutInflater.from(holder.binding.getRoot().getContext())
                            .inflate(R.layout.admob_native, null, false);
                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                    holder.binding.unbind();
                } else if (facebookNativeAd != null) {
                    inflateAd(facebookNativeAd, holder.binding);
                } else {
                    holder.binding.selfAd.getRoot().setVisibility(View.VISIBLE);
                }
            }
        }*/
    }

//    private void inflateAd(NativeAd nativeAd, ItemAdsLayBinding binding) {
//
//        nativeAd.unregisterView();
//
//        // Add the Ad view into the ad container.
//
//        binding.fbNative.setVisibility(View.VISIBLE);
//        LayoutInflater inflater = LayoutInflater.from(binding.getRoot().getContext());
//        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
//        RelativeLayout adView = (RelativeLayout) inflater.inflate(R.layout.fb_native_full, binding.fbNative, false);
//        binding.fbNative.addView(adView);
//
//        // Add the AdOptionsView
//        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
//        AdOptionsView adOptionsView = new AdOptionsView(adView.getContext(), nativeAd, binding.fbNative);
//        adChoicesContainer.removeAllViews();
//        adChoicesContainer.addView(adOptionsView, 0);
//
//        // Create native UI using the ad metadata.
//        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
//        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
//        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
//        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
//        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
//        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
//        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
//
//        // Set the Text.
//        nativeAdTitle.setText(nativeAd.getAdvertiserName());
//        nativeAdBody.setText(nativeAd.getAdBodyText());
//        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
//        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
//        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
//        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
//
//        // Create a list of clickable views
//        List<View> clickableViews = new ArrayList<>();
//        clickableViews.add(nativeAdTitle);
//        clickableViews.add(nativeAdCallToAction);
//
//        // Register the Title and CTA button to listen for clicks.
//        nativeAd.registerViewForInteraction(
//                adView,
//                nativeAdMedia,
//                nativeAdIcon,
//                clickableViews);
//    }
//
//    private void populateUnifiedNativeAdView(UnifiedNativeAd unifiedNativeAd, UnifiedNativeAdView adView) {
//
//        adView.setMediaView(adView.findViewById(R.id.ad_media));
//        // Set other ad assets.
//        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
//        adView.setBodyView(adView.findViewById(R.id.ad_body));
//        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
//        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
//        adView.setPriceView(adView.findViewById(R.id.ad_price));
//        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
//        adView.setStoreView(adView.findViewById(R.id.ad_store));
//        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
//
//        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
//        ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
//
//
//        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
//        // check before trying to display them.
//        if (unifiedNativeAd.getBody() == null) {
//            adView.getBodyView().setVisibility(View.GONE);
//
//        } else {
//            adView.getBodyView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getBodyView()).setText(unifiedNativeAd.getBody());
//        }
//
//        if (unifiedNativeAd.getCallToAction() == null) {
//            adView.getCallToActionView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getCallToActionView().setVisibility(View.VISIBLE);
//            ((Button) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
//        }
//
//        if (unifiedNativeAd.getIcon() == null) {
//            adView.getIconView().setVisibility(View.GONE);
//        } else {
//            new GlideLoader(adView.getContext()).loadRoundDrawable(unifiedNativeAd.getIcon().getDrawable(), (ImageView) adView.getIconView());
//            adView.getIconView().setVisibility(View.VISIBLE);
//        }
//
//        if (unifiedNativeAd.getPrice() == null) {
//            adView.getPriceView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getPriceView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getPriceView()).setText(unifiedNativeAd.getPrice());
//        }
//
//        if (unifiedNativeAd.getStore() == null) {
//            adView.getStoreView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getStoreView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getStoreView()).setText(unifiedNativeAd.getStore());
//        }
//
//        if (unifiedNativeAd.getStarRating() == null) {
//            adView.getStarRatingView().setVisibility(View.INVISIBLE);
//        } else {
//            ((RatingBar) adView.getStarRatingView())
//                    .setRating(unifiedNativeAd.getStarRating().floatValue());
//            adView.getStarRatingView().setVisibility(View.VISIBLE);
//        }
//
//        if (unifiedNativeAd.getAdvertiser() == null) {
//            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
//        } else {
//            ((TextView) adView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());
//            adView.getAdvertiserView().setVisibility(View.VISIBLE);
//        }
//
//        // This method tells the Google Mobile Ads SDK that you have finished populating your
//        // native ad view with this native ad.
//        adView.setNativeAd(unifiedNativeAd);
//
//        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
//        // have a video asset.
//        // Updates the UI to say whether or not this ad has a video asset.
//    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {

        /*if (mList.get(position) == null) {
            return AD_TYPE;
        } else {
            return POST_TYPE;
        }*/
        return POST_TYPE;
    }

    public void updateData(List<Video.Data> list) {
        mList = list;
//        mList.add(null);
        notifyDataSetChanged();
    }

    public void loadMore(List<Video.Data> data) {
        mList.addAll(data);
//        mList.add(null);
        notifyDataSetChanged();
    }

    public interface OnRecyclerViewItemClick {

        void onItemClick(Video.Data model, int type, ItemVideoListBinding binding, int position , SimpleExoPlayer player);

        void onHashTagClick(String hashTag);

        void onDoubleClick(Video.Data model, MotionEvent event, ItemVideoListBinding binding);
    }

    class VideoFullViewHolder extends RecyclerView.ViewHolder {
        ItemVideoListBinding binding;

        VideoFullViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            if (binding != null) {
                binding.executePendingBindings();
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        public void setModel(int position) {
            binding.setModel(mList.get(position));
            if (position == itemToPlay || postId.equals(mList.get(position).getPostId())) {
                new GlobalApi().increaseView(mList.get(position).getPostId());
                Animation animation = AnimationUtils.loadAnimation(binding.getRoot().getContext(), R.anim.slow_rotate);
                binding.imgSound.startAnimation(animation);
                onRecyclerViewItemClick.onItemClick(mList.get(position), 9, binding, position, player);
            }
            String url = "";
            if (mList.get(position).getThumbnail_image() != null) {
                url = mList.get(position).getThumbnail_image();
            } else {
                url = mList.get(position).getPostVideo();
            }
            Glide.with(binding.getRoot().getContext()).load(url)
                    .transform(new BlurTransformation())
//                    .apply(
//                            new RequestOptions().error(
//                                    R.color.transparent
//                            ).priority(Priority.HIGH)
//                    )
                    .into(binding.ivThumb);

           /* Glide.with(binding.getRoot().getContext()).load(mList.get(position).getUserProfile())
                    .placeholder(R.drawable.ic_edit_profile_img)
                    .into(binding.imgUser);*/

            /*if (!TextUtils.isEmpty(mList.get(position).getProfileCategoryName())) {
                binding.tvProfileCategory.setVisibility(View.VISIBLE);
            } else {
                binding.tvProfileCategory.setVisibility(View.GONE);
            }*/

            GradientDrawable gradientDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{ContextCompat.getColor(context, R.color.black_55),
                            ContextCompat.getColor(context, R.color.transparent),
                            ContextCompat.getColor(context, R.color.transparent),
                            ContextCompat.getColor(context, R.color.transparent)});

            binding.topGradient.setBackground(gradientDrawable);
            binding.tvSoundName.setSelected(true);
            binding.tvLikeCount.setText(Global.prettyCount(Integer.parseInt(mList.get(position).getPostLikesCount())));
            binding.tvCommentCount.setText(Global.prettyCount(mList.get(position).getPostCommentsCount()));
            binding.tvShareCount.setText(Global.prettyCount(Integer.parseInt(mList.get(position).getShareCount())));
//            binding.tvLikeCount.setText(Global.prettyCount(mList.get(position).getDummyLikeCount()));
            binding.playerView.setOnTouchListener(new View.OnTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(binding.getRoot().getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        onRecyclerViewItemClick.onItemClick(mList.get(position), 2, binding, position, player);
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
//                        onRecyclerViewItemClick.onItemClick(mList.get(position), 8, binding, position);
                        super.onLongPress(e);
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        onRecyclerViewItemClick.onDoubleClick(mList.get(position), e, binding);
                        return true;
                    }
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return false;
                }
            });

//            binding.tvDescreption.setOnHashtagClickListener((view, text) -> onRecyclerViewItemClick.onHashTagClick(text.toString()));
//          binding.imgUser.setOnClickListener(view -> onRecyclerViewItemClick.onItemClick(mList.get(position), 1, binding, position));
            binding.loutUser.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(mList.get(position), 1, binding, position, player));
//            binding.fabButton.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(mList.get(position), 1, binding, position, player));
//            binding.fabButton.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(mList.get(position), 11, binding, position));
//            binding.fabView.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(mList.get(position), 12, binding, position));
            binding.imgSendBubble.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(mList.get(position), 3, binding, position, player));
//            binding.likebtn.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(mList.get(position), 4, binding, position));
//            binding.tvProfileCategory.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(mList.get(position), 13, binding, position));
            binding.likebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.likebtn.setEnabled(false);
                    onRecyclerViewItemClick.onItemClick(mList.get(position), 4, binding, position, player);
                }
            });

            if (mList.get(position).getVideoIsLiked()) {
//                binding.likebtn.setLiked(true);
                binding.likebtn.setImageResource(R.drawable.filled_diamond_icon);
            } else {
//                binding.likebtn.setLiked(false);
                binding.likebtn.setImageResource(R.drawable.ic_diamond);
            }

         /*   binding.likebtn.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    onRecyclerViewItemClick.onItemClick(mList.get(position), 4, binding, position);
                    mList.get(position).setPostLikesCount(String.valueOf(Integer.parseInt(mList.get(position).getPostLikesCount()) + 1));
                    binding.tvLikeCount.setText(Global.prettyCount(Long.parseLong(mList.get(position).getPostLikesCount())));
                    mList.get(position).setVideoIsLiked(1);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    onRecyclerViewItemClick.onItemClick(mList.get(position), 4, binding, position);
                    mList.get(position).setPostLikesCount(String.valueOf(Integer.parseInt(mList.get(position).getPostLikesCount()) - 1));
                    binding.tvLikeCount.setText(Global.prettyCount(Long.parseLong(mList.get(position).getPostLikesCount())));
                    mList.get(position).setVideoIsLiked(0);
                }
            });*/
           /* if(mList.get(position).getPostViewCount().equals("1")){
                binding.tvWatchViews.setText("0");
            }else {
                int counts = Integer.parseInt(mList.get(position).getPostViewCount());
                if ( counts % 2 == 0 ){
                    counts = counts/2;
                }else {
                    counts = (counts+1)/2;
                }
                binding.tvWatchViews.setText(Global.prettyCount(Long.parseLong(String.valueOf(counts))));
            }*/
            binding.llSound.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(mList.get(position), 7, binding, position, player));
            binding.viewShare.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(mList.get(position), 6, binding, position, player));
//            binding.viewReport.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(mList.get(position), 8, binding, position,player));
            binding.viewComment.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(mList.get(position), 5, binding, position, player));
            binding.flFollowButton.setOnClickListener(v -> onRecyclerViewItemClick.onItemClick(mList.get(position), 9, binding, position, player));

//            binding.viewReport.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (player != null) {
//                        player.setPlayWhenReady(false);
//                        if (player.getPlayWhenReady()) {
//                            binding.ivPause.setVisibility(View.GONE);
//                        } else {
//                            binding.ivPause.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    onRecyclerViewItemClick.onItemClick(mList.get(position), 8, binding, position,player);
//                }
//            });
            if (mList.get(position).getPostViewCount() != null && mList.get(position).getPostViewCount() != 0) {
                binding.llViews.setVisibility(View.VISIBLE);
            } else {
                binding.llViews.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(mList.get(position).getPostDescription())) {
                binding.llDescription.setVisibility(View.VISIBLE);
            } else {
                binding.llDescription.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(mList.get(position).getSoundTitle())) {
                binding.llSound.setVisibility(View.VISIBLE);
            } else {
                binding.llSound.setVisibility(View.GONE);
            }
//            if (!TextUtils.isEmpty(mList.get(position).getPostHashTag())) {
//                binding.tvDescreption.setVisibility(View.VISIBLE);
//            } else {
//                binding.tvDescreption.setVisibility(View.GONE);
//            }
            ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(binding.soundIcon,
                    PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.2f));
            objectAnimator.setDuration(500);
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimator.start();
//            binding.fabButton.animate().scaleX(1.0f);
//            binding.fabButton.animate().scaleY(1.0f);
        }
    }

//    class AdsViewHolder extends RecyclerView.ViewHolder {
//        ItemAdsLayBinding binding;
//
//        public AdsViewHolder(@NonNull View itemView) {
//            super(itemView);
//            binding = DataBindingUtil.bind(itemView);
//            binding.executePendingBindings();
//        }
//    }

}
