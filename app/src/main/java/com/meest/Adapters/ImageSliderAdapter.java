package com.meest.Adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;
import java.util.Objects;

import com.meest.R;
import com.meest.utils.TouchImageView;
import com.meest.utils.goLiveUtils.CommonUtils;

public class ImageSliderAdapter extends PagerAdapter {

    // Context object
    Context context;

    // Array of images
    List<String> images;

    // Layout Inflater
    LayoutInflater mLayoutInflater;


    // Viewpager Constructor
    public ImageSliderAdapter(Context context,  List<String> images) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.item_image_slider, container, false);

        // referencing the image view from the item.xml file
        TouchImageView ivPinchView = itemView.findViewById(R.id.ivPinchView);

        // setting the image in the imageView

        CommonUtils.loadImage(ivPinchView,images.get(position),context);

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }
}