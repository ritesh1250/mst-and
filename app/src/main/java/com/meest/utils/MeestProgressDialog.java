package com.meest.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.meest.R;


public class MeestProgressDialog extends Dialog {
    static MeestProgressDialog progressDialog;

    public MeestProgressDialog(Context a) {
        super(a);
    }

    /*public MeestProgressDialog(Context a) {
        super(a, android.R.style.Theme_Light);
    }*/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(R.layout.meest_progress_dialog);


    }

    public static MeestProgressDialog getInstance(Activity activity) {
        progressDialog = new MeestProgressDialog(activity);
        progressDialog.setCancelable(true);
        return progressDialog;
    }


    public static void setDismiss() {
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
