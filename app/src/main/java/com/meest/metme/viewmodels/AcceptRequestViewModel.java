package com.meest.metme.viewmodels;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import com.meest.Activities.base.ApiCallActivity;
import com.meest.R;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.MetMeGallery;
import com.meest.metme.camera2.CameraActivity;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.utils.ParameterConstants;

import java.util.HashMap;
import java.util.Map;

public class AcceptRequestViewModel {
    public String action;
    public String attachmentType = "text";
    ApiCallActivity context;
    public boolean isClicked = false;
    String chatHeadId;
    public Dialog dialog;


    public AcceptRequestViewModel(ApiCallActivity context, String chatHeadId) {
        this.context = context;
        this.chatHeadId = chatHeadId;
    }


    public ObservableBoolean micro_gallery = new ObservableBoolean(true);

    public MutableLiveData<String> enteredMsg = new MutableLiveData<>();


    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.w("tag", "onTextChanged " + s);
    }


    public void afterTextChanged(Editable s) {
        if (s.length() > 0)
            micro_gallery.set(false);
        else
            micro_gallery.set(true);

        Log.w("tag", "afterTextChanges " + s);


    }

    public void onCameraClick() {
        attachmentType = "camera";
        context.startActivityForResult(new Intent(context, CameraActivity.class), ParameterConstants.PICKER);
    }

    public void onGalleryClick() {
        attachmentType = "gallery";
        Intent intent = new Intent(context, MetMeGallery.class);
        context.startActivityForResult(intent, ParameterConstants.LAUNCH_SECOND_ACTIVITY);

    }


    public void onSendClick() {
        if (enteredMsg.getValue() != null) {
            if (!enteredMsg.getValue().trim().isEmpty() && enteredMsg.getValue() != null) {
                isClicked = true;
                attachmentType = "text";
                action = "accept";
                Map<String, String> map = new HashMap<>();
                map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
                HashMap body = new HashMap<>();
                body.put("friendId", context.getIntent().getStringExtra("userId"));
                body.put("isAccepted", true);
                body.put("chatHeadId", chatHeadId);
                context.apiCallBack(context.getApi().acceptRejectRequest(map, body));
            }
        }
    }

    public void onDeleteClick() {
//        block_user_popup
        dialog = new Dialog(context);
        dialog.getWindow().requestFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.delete_request_popup);
        dialog.setCancelable(false);
        Button yes = dialog.findViewById(R.id.yes);
        Button no = dialog.findViewById(R.id.no);
        dialog.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action = "delete";
                Map<String, String> map = new HashMap<>();
                map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
                HashMap body = new HashMap<>();
                body.put("friendId", context.getIntent().getStringExtra("userId"));
                body.put("isAccepted", false);
                body.put("chatHeadId", chatHeadId);
                context.apiCallBack(context.getApi().acceptRejectRequest(map, body));
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



    }

    public void onBlockClick() {
        try {
            Dialog dialog = new Dialog(context);
            dialog.getWindow().requestFeature(1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setContentView(R.layout.block_user_dialog);
            dialog.setCancelable(false);
            Button yes = dialog.findViewById(R.id.btnConfirm);
            Button no = dialog.findViewById(R.id.btnClose);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    action = "block";
                    Map<String, String> map = new HashMap<>();
                    map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
                    HashMap body = new HashMap<>();
                    body.put("blockedTo", context.getIntent().getStringExtra("userId"));
                    context.apiCallBack(context.getApi().block(map, body));
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            if (dialog!=null)
                dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void onViewProfileClick() {
        Intent intent = new Intent(context, OtherUserAccount.class);
        intent.putExtra("userId", context.getIntent().getStringExtra("userId"));
        context.startActivity(intent);
    }
}
