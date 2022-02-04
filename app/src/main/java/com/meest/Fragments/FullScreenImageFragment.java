package com.meest.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.meest.R;

public class FullScreenImageFragment extends Fragment {
    View view;
    Context context;
    ImageButton close_gallery;


    ImageView single_image;

    String image_url;

    ProgressBar p_bar;


    int width,height;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_screen_image, container, false);
    }
}