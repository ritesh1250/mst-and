package com.meest.social.socialViewModel.viewModel.searchViewModel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;

import com.meest.responses.VideoSearchResponse;
import com.meest.social.socialViewModel.adapter.search.HashtagDataAdapter;
import com.meest.social.socialViewModel.adapter.search.HashtagNameAdapter;
import com.meest.social.socialViewModel.adapter.search.HashtagVideoAdapter;
import com.meest.social.socialViewModel.model.search.HashtagSearchResponse;

import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VideoHashTagFragViewModel extends ViewModel {

    public MutableLiveData<Boolean> loader = new MutableLiveData<>();
    public MutableLiveData<Boolean> showRecyclerView = new MutableLiveData<>();
    public MutableLiveData<Boolean> dataNotFound = new MutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();
    public boolean Loading = true;
    public boolean isSvs;
    public String data;
    public String mediaType;
    int pagePerRecord = 10;

    public List<HashtagSearchResponse.Datum.Rows> datahashTagList= new ArrayList<>();
    public List<HashtagSearchResponse.Datum.Rows> datahashTagListimage= new ArrayList<>();

    public HashtagNameAdapter hashtagNameAdapter;
    public HashtagDataAdapter hashtagAdapter;

    public void fetchData(int pageno, Context context, boolean loadMore) {

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        HashMap<String, String> body = new HashMap<>();
        body.put("pageNumber", String.valueOf(pageno));
        body.put("pageSize", String.valueOf(pagePerRecord));
        if (data != null && !data.isEmpty()) {
            if (data.contains("#")) {
                data = data.replace("#", "");
                body.put("key", data);

            } else
                body.put("key", data);
        } else
            body.put("key", "");

        if (isSvs) {
            body.put("svs", "true");
        }
        disposable.add(Global.initSocialRetrofit().searchHashTags(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {
                           if (response.getCode() == 1) {
                               if (response.getData().getRows().size() > 0) {
                                   datahashTagList.addAll(response.getData().getRows());
                                   datahashTagListimage.addAll(response.getData().getRows());
                                   dataNotFound.setValue(false);
                                   showRecyclerView.setValue(true);
                                   Loading = true;
                                   if (hashtagAdapter == null || hashtagAdapter.getItemCount() == 10) {
                                       hashtagNameAdapter.update(response.getData().getRows());
                                       hashtagAdapter.update(response.getData().getRows());
                                   } else {

                                       if(data.length()>0){
                                           hashtagNameAdapter.update(response.getData().getRows());
                                           hashtagAdapter.update(response.getData().getRows());
                                       }else {
                                           hashtagAdapter.notifyFeedsDataSetChanged(response.getData().getRows());
                                           hashtagNameAdapter.notifyFeedsDataSetChanged(response.getData().getRows());
                                       }

                                   }

                               } else {
                                   if (datahashTagListimage.size() == 0) {
                                       dataNotFound.setValue(false);
                                       showRecyclerView.setValue(true);
                                   }
                               }
                           }
                    } else {
                        Log.e("TAG", "Enter in ELSE 3 PART: ");
                        showRecyclerView.setValue(false);
                        dataNotFound.setValue(true);
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

//    public void fetchData2(int pageno, Context context, boolean loadMore) {
//
//        Map<String, String> header = new HashMap<>();
//        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
//        header.put("Accept", "application/json");
//        header.put("Content-Type", "application/json");
//        HashMap<String, String> body = new HashMap<>();
//        body.put("pageNumber", String.valueOf(pageno));
//        body.put("pageSize", String.valueOf(pagePerRecord));
//        if (data != null && !data.isEmpty()) {
//            if (data.contains("#")) {
//                data = data.replace("#", "");
//                body.put("key", data);
//            } else
//                body.put("key", data);
//        } else
//            body.put("key", "");
//
//        if (isSvs) {
//            body.put("svs", "true");
//        }
//
//        disposable.add(Global.initSocialRetrofit().searchHashTags(header, body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .doOnSubscribe(disposable1 -> loader.setValue(true))
//                .doOnTerminate(() -> loader.setValue(false))
//                .subscribe((response, throwable) -> {
//                    if (response != null && response.getSuccess()) {
//                        if (response.getData().getRows().size() > 0) {
//                            datahashTagList.addAll(response.getData().getRows());
//                            dataNotFound.setValue(false);
//                            showRecyclerView.setValue(true);
//                            if (!loadMore) {
//                                hashtagNameAdapter.update(response.getData().getRows());
//                            }else {
//                                hashtagNameAdapter.notifyFeedsDataSetChanged(response.getData().getRows());
//                            }
//                        } else {
//                            if (datahashTagList.size() == 0) {
//                                showRecyclerView.setValue(false);
//                                dataNotFound.setValue(true);
//                            }
//                        }
//                    } else {
//                        showRecyclerView.setValue(false);
//                        dataNotFound.setValue(true);
//                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
//                    }
//                }));
//    }

}
