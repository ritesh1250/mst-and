package com.meest.videomvvmmodule.utils;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.animation.LinearInterpolator;

import androidx.databinding.DataBindingUtil;

import com.meest.R;
import com.meest.databinding.DailogLoaderBinding;
import com.meest.databinding.DailogTermConditionBinding;
import com.meest.databinding.LoutAudioMikeBinding;
import com.meest.databinding.LoutPopupBinding;
import com.meest.databinding.LoutPopupInvalidQrCodeBinding;
import com.meest.databinding.LoutPopupPermissionBinding;
import com.meest.databinding.LoutSendBubbleBinding;
import com.meest.databinding.LoutSendResultPopupBinding;
import com.meest.databinding.SendDiamondConfirmationBinding;
import com.meest.videomvvmmodule.view.web.WebViewActivity;

import java.util.concurrent.TimeUnit;

public class CustomDialogBuilder {
    private Context mContext;
    private Dialog mBuilder = null;

    public CustomDialogBuilder(Context context) {
        this.mContext = context;
        if (mContext != null) {
            mBuilder = new Dialog(mContext);
            mBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mBuilder.setCancelable(false);
            mBuilder.setCanceledOnTouchOutside(false);

            if (mBuilder.getWindow() != null) {
                mBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }

    public void showSimpleDialogMeest(String title, String message, String negativeText, String positiveText, OnDismissListener onDismissListener) {

        if (mContext == null)
            return;
        mBuilder.setCancelable(true);
        mBuilder.setCanceledOnTouchOutside(true);
        LoutPopupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.lout_popup, null, false);
        mBuilder.setContentView(binding.getRoot());
        Dialog1 dialog1 = new Dialog1();
        dialog1.setTitle(title);
        dialog1.setMessage(message);
        dialog1.setPositiveText(positiveText);
        dialog1.setNegativeText(negativeText);
        binding.setModel(dialog1);
        binding.tvPositive.setOnClickListener(v -> {
            mBuilder.dismiss();
            onDismissListener.onPositiveDismiss();
        });
        binding.tvCancel.setOnClickListener(v -> {
            mBuilder.dismiss();
            onDismissListener.onNegativeDismiss();
        });
        mBuilder.show();
    }

    public void showSimpleDialog(String title, String message, String negativeText, String positiveText, OnDismissListener onDismissListener) {

        if (mContext == null)
            return;

        mBuilder.setCancelable(true);
        mBuilder.setCanceledOnTouchOutside(true);
        LoutPopupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.lout_popup, null, false);
        mBuilder.setContentView(binding.getRoot());
        Dialog1 dialog1 = new Dialog1();
        dialog1.setTitle(title);
        dialog1.setMessage(message);
        dialog1.setPositiveText(positiveText);
        dialog1.setNegativeText(negativeText);
        binding.setModel(dialog1);
        binding.tvPositive.setOnClickListener(v -> {
            mBuilder.dismiss();
            onDismissListener.onPositiveDismiss();
        });
        binding.tvCancel.setOnClickListener(v -> {
            mBuilder.dismiss();
            onDismissListener.onNegativeDismiss();
        });
        mBuilder.show();
    }

    public void showUserNotFoundDialog( OnDismissListener onDismissListener) {

        if (mContext == null)
            return;

        mBuilder.setCancelable(false);
        mBuilder.setCanceledOnTouchOutside(false);
        LoutPopupInvalidQrCodeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.lout_popup_invalid_qr_code, null, false);
        mBuilder.setContentView(binding.getRoot());
        binding.tvPositive.setOnClickListener(v -> {
            mBuilder.dismiss();
            onDismissListener.onPositiveDismiss();
        });
        mBuilder.show();
    }

    public void showPermissionDialog(Drawable drawable, String message, String negativeText, String positiveText, OnDismissListener onDismissListener) {

        if (mContext == null)
            return;

        mBuilder.setCancelable(true);
        mBuilder.setCanceledOnTouchOutside(true);

        LoutPopupPermissionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.lout_popup_permission, null, false);
        mBuilder.setContentView(binding.getRoot());
        PermissionDialog dialog1 = new PermissionDialog();
        dialog1.setDrawable(drawable);
        dialog1.setMessage(message);
        dialog1.setPositiveText(positiveText);
        dialog1.setNegativeText(negativeText);
        binding.setModel(dialog1);
        binding.tvPositive.setOnClickListener(v -> {
            mBuilder.dismiss();
            onDismissListener.onPositiveDismiss();
        });
        binding.tvCancel.setOnClickListener(v -> {
            mBuilder.dismiss();
            onDismissListener.onNegativeDismiss();
        });
        mBuilder.show();
    }


    public void showSendCoinDialogue(OnCoinDismissListener onCoinDismissListener) {
        if (mContext == null)
            return;

        mBuilder.setCancelable(true);
        mBuilder.setCanceledOnTouchOutside(true);

        LoutSendBubbleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.lout_send_bubble, null, false);
        mBuilder.setContentView(binding.getRoot());
        binding.tvCancel.setOnClickListener(view -> {
            mBuilder.dismiss();
            onCoinDismissListener.onCancelDismiss();
        });
        binding.lout20.setOnClickListener(view -> {
            mBuilder.dismiss();
            onCoinDismissListener.on20Dismiss();
        });
        binding.lout40.setOnClickListener(view -> {
            mBuilder.dismiss();
            onCoinDismissListener.on40Dismiss();
        });
        binding.lout80.setOnClickListener(view -> {
            mBuilder.dismiss();
            onCoinDismissListener.on80Dismiss();
        });
        binding.lout100.setOnClickListener(view -> {
            mBuilder.dismiss();
            onCoinDismissListener.on100Dismiss();
        });
        mBuilder.show();

    }

    public void showSendCoinResultDialogue(boolean success, OnResultButtonClick onResultButtonClick) {
        if (mContext == null)
            return;

        mBuilder.setCancelable(true);
        mBuilder.setCanceledOnTouchOutside(true);
        LoutSendResultPopupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.lout_send_result_popup, null, false);
        mBuilder.setContentView(binding.getRoot());
        binding.setSuccess(success);
        binding.loutButton.setOnClickListener(view -> {
            mBuilder.dismiss();
            onResultButtonClick.onButtonClick(success);
        });

        mBuilder.show();
    }

    public void showRequestTermDialogue(OnDismissListener onDismissListener) {
        if (mContext == null)
            return;

        mBuilder.setCancelable(true);
        mBuilder.setCanceledOnTouchOutside(true);
        DailogTermConditionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dailog_term_condition, null, false);
        mBuilder.setContentView(binding.getRoot());
        binding.tvTerm.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, WebViewActivity.class).putExtra("type", 0)));
        binding.tvPrivacy.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, WebViewActivity.class).putExtra("type", 1)));
        binding.tvPositive.setOnClickListener(v -> {
            mBuilder.dismiss();
            onDismissListener.onPositiveDismiss();
        });
        binding.tvCancel.setOnClickListener(v -> mBuilder.dismiss());
        mBuilder.show();
    }

    public void showLoadingDialog() {
        if (mContext == null)
            return;
        mBuilder.setCancelable(false);
//        mBuilder.setCanceledOnTouchOutside(true);
        DailogLoaderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dailog_loader, null, false);

//        Glide.with(mContext)
//                .load(R.drawable.loader_gif)
//                .into(binding.ivParent);
//        Animation rotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate);
//        Animation reverseAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_reverse);
//        binding.ivParent.startAnimation(rotateAnimation);
//        binding.ivChild.startAnimation(reverseAnimation);
        mBuilder.setContentView(binding.getRoot());
        mBuilder.show();
    }

    public void hideLoadingDialog() {
        try {
            mBuilder.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnResultButtonClick {
        void onButtonClick(boolean success);
    }

    public interface OnDismissListener {
        void onPositiveDismiss();

        void onNegativeDismiss();
    }

    public interface OnCoinDismissListener {

        void onCancelDismiss();

        void on20Dismiss();

        void on40Dismiss();

        void on80Dismiss();

        void on100Dismiss();
    }

    public void showSendDiamondConfirm(boolean success, String sendDiamond) {
        if (mContext == null)
            return;
        mBuilder.setCancelable(true);
        mBuilder.setCanceledOnTouchOutside(true);
        SendDiamondConfirmationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.send_diamond_confirmation, null, false);
        mBuilder.setContentView(binding.getRoot());
        binding.setSuccess(success);
        binding.tvCount.setText(sendDiamond);
        binding.sendMessage.setText(sendDiamond + " Diamonds sent to the creator\nsuccessfully ");
        binding.loutButton.setOnClickListener(view -> {
            mBuilder.dismiss();
        });
        mBuilder.show();
    }

    public void showAudioMikeDialog() {
        if (mContext == null)
            return;
        mBuilder.setCancelable(true);
        mBuilder.setCanceledOnTouchOutside(true);
        mBuilder.getWindow().setGravity(Gravity.BOTTOM);
        mBuilder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LoutAudioMikeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.lout_audio_mike, null, false);
        mBuilder.setContentView(binding.getRoot());
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(binding.progressBar, "progress", 100, 0);
        progressAnimator.setDuration(120000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
        new CountDownTimer(120000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                binding.tvTimer.setText("" + String.format("0%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                binding.tvTimer.setText("OO:OO");
            }
        }.start();
        mBuilder.show();
    }

}