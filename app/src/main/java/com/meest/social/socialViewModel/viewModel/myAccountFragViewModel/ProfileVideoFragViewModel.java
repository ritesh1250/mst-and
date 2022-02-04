package com.meest.social.socialViewModel.viewModel.myAccountFragViewModel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;

import com.meest.R;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.social.socialViewModel.adapter.ProfileVideoAdapter1;
import com.meest.social.socialViewModel.model.ProfilesVideoResponse1;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileVideoFragViewModel extends ViewModel {
    public ProfileVideoAdapter1 adapter;
    public GridLayoutManager mLayoutManager;
    public int pageno = 1;
    public static final int PAGE_SIZE = 30;
    public boolean Loading = true;
    public ArrayList<ProfilesVideoResponse1.Row> arrayList = new ArrayList<>();
    public int totalCount = -1;
    public CompositeDisposable disposable = new CompositeDisposable();
    public MutableLiveData<Boolean> tvNoDataVisible = new MutableLiveData<>();
    public MutableLiveData<Boolean> loaderVisible = new MutableLiveData<>();

    public ProfileVideoFragViewModel() {
        adapter = new ProfileVideoAdapter1();
    }

    public void callApiGetVideos(boolean loadMore, Context context, int pageNumber) {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
        body.put("pageNumber", String.valueOf(pageNumber));
        body.put("pageSize", String.valueOf(PAGE_SIZE));
        body.put("userId", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID));
        disposable.add(Global.initSocialRetrofit().videoTabUserMedia(map, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> loaderVisible.setValue(true))
                .doOnTerminate(() -> loaderVisible.setValue(false))
                .subscribe((ProfilesVideoResponse1 response, Throwable throwable) -> {
                    if (response.getCode() == 1) {
                        totalCount = response.getData().getCount();
                        if (response.getData().getRows().size() > 0) {
                            arrayList.addAll(response.getData().getRows());
                            if(!loadMore){
                                adapter.update(response.getData().getRows());
                            }else {
                                adapter.loadMore(response.getData().getRows());
                            }
                        } else {
                            if (arrayList.size() == 0) {
                                tvNoDataVisible.setValue(true);
                            }
                        }

                    } else {
                        Utilss.showToast(context, context.getString(R.string.SOME_ERROR), R.color.grey);
                    }
                }));
    }
    public void checkNetAndFetchData(Context context) {
        if (ConnectionUtils.isConnected(context)){
            if(Loading){
                callApiGetVideos(false, context, pageno);
            }

        } else
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
