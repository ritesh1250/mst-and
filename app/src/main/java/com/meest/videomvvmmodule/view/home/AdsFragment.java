package com.meest.videomvvmmodule.view.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.meest.R;
import com.meest.databinding.ItemAdsLayBinding;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.meest.videomvvmmodule.utils.GlideLoader;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class AdsFragment extends Fragment {

    ItemAdsLayBinding binding;
    private UnifiedNativeAd unifiedNativeAd;
    private NativeAd facebookNativeAd;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.item_ads_lay, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        initAds();
    }

    @Override
    public void onResume() {
        super.onResume();
//        loadAds();
    }

    private void initAds() {

        AdLoader.Builder builder = new AdLoader.Builder(getActivity(), getResources().getString(R.string.admobe_native_ad_id));
        builder.forUnifiedNativeAd(unifiedNativeAd -> {
            // You must call destroy on old ads when you are done with them,
            // otherwise you will have a memory leak.
            if (unifiedNativeAd != null) {
                unifiedNativeAd.destroy();
            }
            AdsFragment.this.unifiedNativeAd = unifiedNativeAd;
//            loadAds();

        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.build();
        adLoader.loadAd(new AdRequest.Builder().build());
        NativeAd nativeAd = new NativeAd(getActivity(), getResources().getString(R.string.admobe_native_ad_id));

        nativeAd.setAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                facebookNativeAd = nativeAd;
//                loadAds();
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        });

        // Request an ad
        nativeAd.loadAd();

    }

    private void loadAds() {
        if (unifiedNativeAd != null) {
            binding.selfAd.getRoot().setVisibility(View.GONE);
            binding.frame.setVisibility(View.VISIBLE);
            LinearLayout frameLayout = binding.frame;
            UnifiedNativeAdView
                    adView = (UnifiedNativeAdView) LayoutInflater.from(binding.getRoot().getContext())
                    .inflate(R.layout.admob_native, null, false);
            populateUnifiedNativeAdView(unifiedNativeAd, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
            binding.unbind();
        } else if (facebookNativeAd != null) {
            inflateAd(facebookNativeAd, binding);
        } else {
            binding.selfAd.getRoot().setVisibility(View.VISIBLE);
        }
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd unifiedNativeAd, UnifiedNativeAdView adView) {

        adView.setMediaView(adView.findViewById(R.id.ad_media));
        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());


        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (unifiedNativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.GONE);

        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(unifiedNativeAd.getBody());
        }

        if (unifiedNativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
        }

        if (unifiedNativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            new GlideLoader(adView.getContext()).loadRoundDrawable(unifiedNativeAd.getIcon().getDrawable(), (ImageView) adView.getIconView());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(unifiedNativeAd.getPrice());
        }

        if (unifiedNativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(unifiedNativeAd.getStore());
        }

        if (unifiedNativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(unifiedNativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (unifiedNativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(unifiedNativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        // Updates the UI to say whether or not this ad has a video asset.

    }

    private void inflateAd(NativeAd nativeAd, ItemAdsLayBinding binding) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.

        binding.fbNative.setVisibility(View.VISIBLE);
        LayoutInflater inflater = LayoutInflater.from(binding.getRoot().getContext());
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        RelativeLayout adView = (RelativeLayout) inflater.inflate(R.layout.fb_native_full, binding.fbNative, false);
        binding.fbNative.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(adView.getContext(), nativeAd, binding.fbNative);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);


        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
    }
}