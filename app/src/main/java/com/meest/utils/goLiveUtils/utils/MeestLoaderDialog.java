package com.meest.utils.goLiveUtils.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.meest.R;

public class MeestLoaderDialog {

    private   Dialog dialog;
    private  TextView text_dialog;

    public  MeestLoaderDialog(Activity activity){
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loader_common);
        text_dialog =dialog.findViewById(R.id.text_dialog);
      ImageView imageView =dialog.findViewById(R.id.a);

        DrawableImageViewTarget target = new DrawableImageViewTarget(imageView);
        Glide.with(activity)
                .load(R.drawable.loader_video)
                .into(target);
    }



   public void showDialog()
    {
        if (dialog!=null)
            dialog.show();
    }


    public void hideDialog()
    {
        if (dialog!=null)
            dialog.dismiss();
    }


    public void setTexMsg( String texMsg)
    {
        if (text_dialog!=null)
            text_dialog.setText(texMsg);
    }
}
