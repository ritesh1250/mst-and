package com.meest.networkcheck;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.meest.R;

public class ConnectionUtils {

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        } else {
            return false;
        }
    }

    public static void showNoConnectionDialog(Activity mActivity, OnNoInternetRetry onNoInternetRetry) {
        LayoutInflater factory = LayoutInflater.from(mActivity);
        final View deleteDialogView = factory.inflate(R.layout.no_internet_dialog, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(mActivity).create();
        deleteDialog.setCancelable(false);
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.okBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onNoInternetRetry!=null)
                    onNoInternetRetry.onRetry();
                deleteDialog.dismiss();

            }
        });
        deleteDialogView.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();

                System.exit(0);
            }
        });

        deleteDialog.show();
    }
}
