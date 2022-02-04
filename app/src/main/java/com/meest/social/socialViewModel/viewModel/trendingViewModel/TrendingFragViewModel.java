package com.meest.social.socialViewModel.viewModel.trendingViewModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.meest.Fragments.FollowListFragment;
import com.meest.R;
import com.meest.databinding.TrendingFragModelBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.social.socialViewModel.adapter.HashtagNameAdapter;
import com.meest.social.socialViewModel.adapter.trendingAdapters.TopTrendingAdapter;
import com.meest.social.socialViewModel.model.HashtagSearchResponse;
import com.meest.social.socialViewModel.model.TopResponse;
import com.meest.utils.PaginationListener;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

@SuppressLint("StaticFieldLeak")
public class TrendingFragViewModel extends ViewModel {
    private static final String TAG = "TrendingFragViewModel";
    public Context context;
    public TopResponse searchResponse;
    public boolean isSvs;
    public String data;
    public Double post_lat = 0.0d;
    public Double post_long = 0.0d;
    public int PERMISSION_ID = 45;
    public TrendingFragModelBinding binding;
    public Activity activity;
    public FusedLocationProviderClient fusedLocationProviderClient;
    //public HashtagNameAdapter hashtagNameAdapter;
    public static List<HashtagSearchResponse.Datum> datahashTagList;
    public int pagePerRecord = 10;
    public int pageno = 1;
    public boolean Loading = true;
    public CompositeDisposable disposable = new CompositeDisposable();

    public TopTrendingAdapter hashtagVideosAdapter = new TopTrendingAdapter();


    public MutableLiveData<Boolean> loader = new MutableLiveData<>();

    public TrendingFragViewModel(Context context, FragmentActivity activity, TrendingFragModelBinding binding, boolean isSvs) {
        this.context = context;
        this.activity = activity;
        this.isSvs = isSvs;
        this.binding = binding;
        requestNewLocationData();
    }

    boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void fetchData(int pageno, boolean isLoadMore) {
        binding.loading.setVisibility(View.VISIBLE);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        HashMap<String, String> body = new HashMap<>();
        body.put("pageNumber", String.valueOf(pageno));
        body.put("pageSize", String.valueOf(pagePerRecord));
        body.put("userId", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.ID));
        body.put("lat", String.valueOf(post_lat));
        body.put("long", String.valueOf(post_long));
        disposable.add(Global.initSocialRetrofit().hashTagMostTrending(header, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> loader.setValue(true))
                .doOnTerminate(() -> loader.setValue(false))
                .subscribe((TopResponse topResponse, Throwable throwable) -> {
                    binding.hashtagRecycleView.setVisibility(View.VISIBLE);
                    if (topResponse!=null) {
                        if (isLoadMore)
                            hashtagVideosAdapter.loadMore(topResponse.getData());
                        else
                            hashtagVideosAdapter.setData(topResponse.getData());
                    } else {
                        Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                    }
                }));

    }

    public void updateSearch(String search) {
        this.data = search;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchData(pageno,false);
            }
        }, 20);
    }

    @SuppressLint("MissingPermission")
    public void requestNewLocationData() {
        if (hasLocationPermission()) {
            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                LocationRequest mLocationRequest = new LocationRequest();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(0);
                mLocationRequest.setFastestInterval(0);
                mLocationRequest.setNumUpdates(1);
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                fetchData(pageno,false);
            }
        } else {
            fetchData(pageno,false);
        }
    }

    public LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            post_lat = mLastLocation.getLatitude();
            post_long = mLastLocation.getLongitude();
            fetchData(pageno,false);
        }
    };

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
