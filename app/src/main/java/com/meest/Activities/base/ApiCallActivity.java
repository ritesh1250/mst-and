package com.meest.Activities.base;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.meest.R;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.utils.Helper;
import com.meest.utils.ImageCompressor;
import com.meest.utils.ParameterConstants;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.WebApi;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCallActivity<T> extends ImageCompressor {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void timeOut(String msg) {
        dialogFailed(msg);
    }


    public WebApi getApi() {
        return ApiUtils.getClient().create(WebApi.class);
//        return Meeast.getApi(this);
    }



    public void apiCallBack(Call<T> callback) {
        if (!Helper.isNetworkAvailable(this)) {
            Dialog dialog = new Dialog(this);
            dialog.getWindow().requestFeature(1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setContentView(R.layout.dialog_internet);
            dialog.setCancelable(false);
            LinearLayout retry = (LinearLayout) dialog.findViewById(R.id.retry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    apiCallBack(callback);
                }
            });

            dialog.show();

        } else {
            callback.enqueue(new Local<T>());
        }
    }

    class Local<T> implements Callback<T> {

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            if (response.code() == 200) {
                T body = response.body();
                EventBus.getDefault().post(body);
            }else {
                EventBus.getDefault().post(ParameterConstants.ERROR + " " + response.code());
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            EventBus.getDefault().post(t.getMessage());
        }
    }

    public void dialogFailed(String msg) {
        try {
            Dialog dialog = new Dialog(this);
            dialog.getWindow().requestFeature(1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setContentView(R.layout.dialog_failed);
            dialog.setCancelable(false);
            LinearLayout retry = dialog.findViewById(R.id.retry);
            TextView message = dialog.findViewById(R.id.message);
            message.setText(msg);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getIntent(String key){
       return getIntent().getStringExtra(key);
    }
}
