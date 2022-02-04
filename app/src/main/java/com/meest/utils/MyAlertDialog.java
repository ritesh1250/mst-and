package com.meest.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.meest.Adapters.ImageSliderAdapter;
import com.meest.R;

import java.util.List;

public class MyAlertDialog extends DialogFragment {


    private List<String> imagesList;
    private Context context;
    private int lastPosition =0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public static MyAlertDialog display(FragmentManager fragmentManager,List<String> imagesList,int lastPosition) {
        MyAlertDialog exampleDialog = new MyAlertDialog(imagesList,lastPosition);
        exampleDialog.show(fragmentManager, "IMAGE_DIALOG");
        return exampleDialog;
    }

    public MyAlertDialog(List<String> imagesList,int lastPosition) {
        this.imagesList=imagesList;
        this.lastPosition=lastPosition;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.my_alert_dialog, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView ivCancel = view.findViewById(R.id.ivCancel);



        // Initializing the ViewPager Object
      ViewPager mViewPager = view.findViewById(R.id.ivViewPager);

        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        // Initializing the ViewPagerAdapter
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(context, imagesList);

        // Adding the Adapter to the ViewPager
        mViewPager.setAdapter(imageSliderAdapter);
        mViewPager.setCurrentItem(lastPosition);

        ivCancel.setOnClickListener(v->{
            dismiss();
        });
    }


    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
}