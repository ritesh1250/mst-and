package com.meest.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.meest.Interfaces.OnHomeAnimClick;
import com.meest.R;
import com.meest.logPressAnim.LongPressMovingButton;
import com.meest.logPressAnim.enums.ButtonPosition;

public class HomeAnimDialog extends DialogFragment {



    private Context context;
    private OnHomeAnimClick onHomeAnimClick;
    private LongPressMovingButton movingButton;
    private int postPosition;
    private  String id;
    private TextView txt_like_txt;
    private ImageView img_like;
   private TextView txt_like;
    private LinearLayout layout_likes_details;
    private TextView txt_recent_user_name;
    private TextView text_others_like;
    private TextView and_text;
    private ImageView img_save;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public static HomeAnimDialog display(FragmentManager fragmentManager, OnHomeAnimClick onHomeAnimClick, String id, TextView txt_like_txt, ImageView img_like, TextView txt_like, LinearLayout layout_likes_details, TextView txt_recent_user_name, TextView text_others_like, TextView and_text, int postPosition, ImageView img_save) {
        HomeAnimDialog exampleDialog = new HomeAnimDialog(onHomeAnimClick,id,txt_like_txt,img_like,txt_like,layout_likes_details,txt_recent_user_name,text_others_like,and_text,postPosition,img_save);
        exampleDialog.show(fragmentManager, "ANIM_DIALOG");
        return exampleDialog;
    }

    public HomeAnimDialog(OnHomeAnimClick onHomeAnimClick, String id, TextView txt_like_txt, ImageView img_like, TextView txt_like, LinearLayout layout_likes_details, TextView txt_recent_user_name, TextView text_others_like, TextView and_text, int postPosition,ImageView img_save) {
        this.onHomeAnimClick=onHomeAnimClick;
        this.postPosition=postPosition;

        this.id=id;
        this.txt_like_txt=txt_like_txt;
        this.img_like=img_like;
        this.txt_like=txt_like;
        this.layout_likes_details=layout_likes_details;


        this.txt_recent_user_name=txt_recent_user_name;
        this.text_others_like=text_others_like;
        this.and_text=and_text;
        this.img_save=img_save;


    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.home_anim_dialog, container, false);
        movingButton=view.findViewById(R.id.moving_button);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movingButton.setOnPositionChangedListener(new LongPressMovingButton.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int action, ButtonPosition buttonPosition) {

                if (onHomeAnimClick != null) {

                    onHomeAnimClick.onHomeAnimClick(buttonPosition, id, txt_like_txt, img_like, txt_like, layout_likes_details, txt_recent_user_name, text_others_like, and_text, postPosition,HomeAnimDialog.this,img_save);


                }
            }
        });

    }


}