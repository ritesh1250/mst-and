package com.meest.social.socialViewModel.viewModel.searchViewModel;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.meest.Adapters.CollectionPointAdapter;

import com.meest.R;
import com.meest.elasticsearch.Hit;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;

import com.meest.elasticsearch.ElasticSearchRespone;
import com.meest.social.socialViewModel.adapter.search.PeopleSearchAdapter;
import com.meest.social.socialViewModel.adapter.search.VideoPeopleAdapter;

import com.meest.social.socialViewModel.model.search.PeopleSearchResponse;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VideoPeopleFragViewModel extends ViewModel {

    public VideoPeopleAdapter videoPeopleAdapter;
    public PeopleSearchAdapter peopleSearchAdapter;
    public ArrayList<PeopleSearchResponse.Datum> list = new ArrayList();

    public MutableLiveData<Boolean> loader = new MutableLiveData<>();
    public MutableLiveData<Boolean> showRecyclerView = new MutableLiveData<>();
    public MutableLiveData<Boolean> dataNotFound = new MutableLiveData<>();
    public MutableLiveData<List<Hit>> elasticSearchResponse = new MutableLiveData<>();

    private final CompositeDisposable disposable = new CompositeDisposable();
    int pagePerRecord = 20;
    public String data;
    public boolean isSvs;
    public static int totalCount = -1;
    public boolean Loading = true;

    public VideoPeopleFragViewModel() {

    }


    public void fetchData(int pageno, Context context) {

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        HashMap<String, String> body = new HashMap<>();
        body.put("pageNumber", String.valueOf(pageno));
        body.put("pageSize", String.valueOf(pagePerRecord));
        body.put("key", data);
        if (isSvs) {
            body.put("svs", "true");
        }
        disposable.add(Global.initSocialRetrofit().searchPeople(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null && response.getSuccess()) {
                        if (response.getData().size() > 0) {

                            list.addAll(response.getData());
                            dataNotFound.setValue(false);
                            showRecyclerView.setValue(true);
                            Loading=true;

                            if (isSvs) {
                                if (videoPeopleAdapter == null && videoPeopleAdapter.getItemCount() == 0) {
                                    videoPeopleAdapter.update(response.getData());
                                } else {
                                    videoPeopleAdapter.notifyFeedsDataSetChanged(response.getData());
                                }
                            } else {
                                if (peopleSearchAdapter == null && peopleSearchAdapter.getItemCount() == 0) {
                                    peopleSearchAdapter.update(response.getData());
                                } else {
                                    peopleSearchAdapter.notifyFeedsDataSetChanged(response.getData());
                                }
                            }

                        } else {
                            if (list.size() == 0) {
                                showRecyclerView.setValue(false);
                                dataNotFound.setValue(true);

                            }
                        }
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void fetchDataSearch(Context context,String pageno, String search) {

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        HashMap<String, String> body = new HashMap<>();
        body.put("pageNumber", String.valueOf(pageno));
        body.put("pageSize", String.valueOf(pagePerRecord));
        body.put("key", search);
        if (isSvs) {
            body.put("svs", "true");
        }
        disposable.add(Global.initSocialRetrofit().searchPeoplelo(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {

                    if (response.getCode() == 1) {
                        totalCount = response.getData().size();
                        if (response.getData().size() > 0) {
                            list.clear();
                            list.addAll(response.getData());
                            dataNotFound.setValue(false);
                            showRecyclerView.setValue(true);
                            Loading=true;
                            peopleSearchAdapter.update(response.getData());
                        }else{
                            if(list.size()==0){
                                showRecyclerView.setValue(false);
                                dataNotFound.setValue(true);
                            }
                        }
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }

    public void getCollectionPoints(String newText,Context context) {

        String url = "";
      //  String url = ApiUtils.BASE_URL_ELASTIC_SEARCH + "meestusers/users/_search?q=" + newText + "*";

        disposable.add(Global.initSocialRetrofit().elasticSearch(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((response, throwable) -> {
                    if (response != null ) {
                        if (response.getHits() != null && response.getHits().getHits() != null && response.getHits().getHits().size() > 0) {
                            elasticSearchResponse.setValue(response.getHits().getHits());
                        }
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));
    }
}
