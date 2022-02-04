package com.meest.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.meest.R;

public class LoadingDialog {

    Context ctx;
    ProgressDialog progressDialog;

    public LoadingDialog(Context ctx) {
        this.ctx = ctx;
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle(R.string.loading);
    }

    public void show() {
        progressDialog.show();
    }

    public void hide() {
        progressDialog.hide();
    }
}
