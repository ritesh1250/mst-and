package com.meest.metme.viewmodels;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

import com.meest.Activities.base.ApiCallActivity;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.metme.adapter.ForwardMessageAdapter;
import com.meest.metme.model.GetForwardListResponse;
import com.meest.utils.IntentWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForwardViewModel extends ViewModel {
    public ForwardMessageAdapter adapter = new ForwardMessageAdapter();
    List<GetForwardListResponse.Data> fullData = new ArrayList<>();

    ApiCallActivity context;
    IntentWrapper intentWrapper;

    public ObservableBoolean isSelected = new ObservableBoolean(false);



    public ForwardViewModel(ApiCallActivity context, IntentWrapper intentWrapper) {
        this.context = context;
        this.intentWrapper = intentWrapper;

    }

    public void getFollower_FollowingList(String x_token) {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", x_token);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<GetForwardListResponse> call = webApi.getFollowerDataGet(header);
        call.enqueue(new Callback<GetForwardListResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<GetForwardListResponse> call, Response<GetForwardListResponse> response) {
                if (response.code() == 200 && response.body().isSuccess()) {

                    adapter.loadMore(response.body().getData(),isSelected, context);
                }
            }

            @Override
            public void onFailure(Call<GetForwardListResponse> call, Throwable t) {

            }
        });
    }

    public void forward(View view) {
        List<String> userIds = adapter.getSelectedUser();
        intentWrapper.getMessageIds();
        Map map = new HashMap();
        map.put("MsgId", intentWrapper.getMessageIds());
        map.put("toUsers", userIds);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        context.apiCallBack(context.getApi().forwardMultipleMessges(header, (HashMap<String, String>) map));
    }

}