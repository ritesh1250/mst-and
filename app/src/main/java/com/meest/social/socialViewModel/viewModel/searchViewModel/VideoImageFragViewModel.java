package com.meest.social.socialViewModel.viewModel.searchViewModel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.R;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.responses.VideoSearchResponse;
import com.meest.social.socialViewModel.adapter.search.HashTagImageAdapter;
import com.meest.social.socialViewModel.adapter.search.HashtagVideoAdapter;
import com.meest.svs.adapters.HashtagImageMeeshoAdapter;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VideoImageFragViewModel extends ViewModel {

    public MutableLiveData<Boolean> loader = new MutableLiveData<>();
    public MutableLiveData<Boolean> showRecyclerView = new MutableLiveData<>();
    public MutableLiveData<Boolean> dataNotFound = new MutableLiveData<>();

    private final CompositeDisposable disposable = new CompositeDisposable();
    private int totalCount = -1;
    public boolean Loading = true;
    public boolean isSvs;
    public String data;
    public String mediaType;
    public HashTagImageAdapter hashtagVideosAdapter;
    int pagePerRecord = 10;

    public ArrayList<VideoSearchResponse.Row> list =new ArrayList<>();

    public void fetchData(int pageno, Context context) {

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        HashMap<String, String> body = new HashMap<>();
        body.put("key", data);
        body.put("pageNumber", String.valueOf(pageno));
        body.put("pageSize", String.valueOf(pagePerRecord));
        if (isSvs) {
            body.put("svs", "true");
        }

        Single<VideoSearchResponse> searchCall;
        if (mediaType.equalsIgnoreCase("image")) {
            searchCall = Global.initSocialRetrofit().searchImages(header, body);
        } else {
            searchCall = Global.initSocialRetrofit().searchVideo(header, body);
        }
        disposable.add(searchCall
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {
                        if (response.getCode() == 1) {
                            totalCount=response.getData().getCount();
                            if (response.getData().getRows().size() > 0) {
                                list.addAll(response.getData().getRows());
                                dataNotFound.setValue(false);
                                showRecyclerView.setValue(true);
                                if(totalCount!=list.size()){
                                    Loading = true;
                                }
                                if (hashtagVideosAdapter == null && hashtagVideosAdapter.getItemCount()==0){
                                    hashtagVideosAdapter.update(response.getData().getRows());
                                }else {
                                    if(data.length()>0){
                                        hashtagVideosAdapter.update(response.getData().getRows());
                                    }else {
                                        hashtagVideosAdapter.notifyFeedsDataSetChanged(response.getData().getRows());
                                    }

                                }

                            }else{
                                if(list.size()==0){
                                    showRecyclerView.setValue(false);
                                    dataNotFound.setValue(true);
                                }
                            }

                        }
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

}
