package com.meest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;

public class UpdateAppDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity context;
    public TextView tvUpdateMessage;
    private Button btnUpdate;
    public String title, message;

    public UpdateAppDialog(Activity context, String title, String message) {
        super(context);
        this.context = context;
        this.title = title;
        this.message = message;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(false);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_app_dialog);
        tvUpdateMessage = (TextView) findViewById(R.id.tvUpdateMessage);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        tvUpdateMessage.setText(Html.fromHtml(message));
        btnUpdate.setText(title);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ApiUtils.PLAY_STORE_URL + context.getPackageName())));
                SharedPrefreances.setSharedPreferenceString(context, SharedPrefreances.APP_NEW_UPDATE_CHECK, "true");
                dismiss();
                break;
           /* case R.id.btn_no:
                dismiss();
                break;*/
            default:
                break;
        }
        dismiss();
    }
}