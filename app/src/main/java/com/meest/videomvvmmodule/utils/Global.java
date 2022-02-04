package com.meest.videomvvmmodule.utils;

import static com.meest.meestbhart.utilss.ApiUtils.BASE_URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meest.BuildConfig;
import com.meest.R;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.model.SaveTextAppearanceModel;
import com.meest.utils.Helper;
import com.meest.videomvvmmodule.api.ApiService;
import com.meest.videomvvmmodule.model.LanguageResponseMedley;
import com.meest.videomvvmmodule.model.videos.UploadVideoResponse;
import com.meest.videomvvmmodule.model.videos.Video;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class Global {
    public static String accessToken;
    public static String firebaseDeviceToken = "";
    public static String userId;
    public static String apikey = "";
    public static String secondColor = "";
    public static double post_lat = 0.0d;
    public static double post_lng = 0.0d;
    public static String password =  "Bestone$@345";
    public static String email = "admin@meest4bharat.com";
//    public static String password =  "123456";
//    public static String email = "test@gmail.com";
    public static Map<String, Boolean> followUnfollow = new HashMap<>();
    public static Map<String, Boolean> likeCount = new HashMap<>();
    public static Map<String, Boolean> commentCount = new HashMap<>();
    public static Map<String, Boolean> shareCount = new HashMap<>();
    public static Boolean isMuted = false;
    public static Context mcontext;

    public static float currentVolume = 0f;
    public static ArrayList<Video.Data> tempList;
    public static Map<String, String> language = new HashMap<>();
    public static String gatewayPaytm = "PAYTM";
    public static String gatewayCashFree = "CASH_FREE";
    public static Map<String, Float> sizeMap = new HashMap<>();
    public static float size1 = 16;
    public static float size2 = 14;
    public static float size3 = 12;
    public static float size4 = 10;
    public static float size5 = 8;

    public static ApiService initRetrofit() {
        // For logging request & response (Optional)
        Interceptor interceptor = chain -> {
            Request request = chain.request();
            Request newRequest = request.newBuilder()
                    //.addHeader("secret_key", Global.ACCESS_TOKEN)
                    .build();
            return chain.proceed(newRequest);
        };
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.networkInterceptors().add(interceptor);
        OkHttpClient client;
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = builder.addInterceptor(loggingInterceptor)
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .build();
        } else {
            client = builder.connectTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                  //  .certificatePinner(ApiUtils.certPinnerMedley)
                    .build();
        }

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtils.MEDLEY_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(ApiService.class);
    }

    public static ApiService initSocialRetrofit() {
        Interceptor interceptor = chain -> {
            Request request = chain.request();
            Request newRequest = request.newBuilder()
                    .build();
            return chain.proceed(newRequest);
        };
        OkHttpClient.Builder builder =
                new OkHttpClient.Builder();
        builder.networkInterceptors().add(interceptor);
        OkHttpClient client;
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = builder.addInterceptor(loggingInterceptor)
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .build();
        } else {
          /*  HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);*/
            client = builder.connectTimeout(2, TimeUnit.MINUTES)
//                    .addInterceptor(loggingInterceptor)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .certificatePinner(ApiUtils.certPinnerMeest)
                    .build();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(ApiService.class);
    }

    public static String prettyCount(Number number) {
        try {
            char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
            long numValue = number.longValue();
            int value = (int) Math.floor(Math.log10(numValue));
            int base = value / 3;
            if (value >= 3 && base < suffix.length) {
                double value2 = numValue / Math.pow(10, base * 3);
                if (String.valueOf(value2).contains(".")) {
                    String num = String.valueOf(value2).split("\\.")[String.valueOf(value2).split("\\.").length - 1];
                    if (num.contains("0")) {
                        return new DecimalFormat("#0").format(value2) + suffix[base];
                    } else {
                        return new DecimalFormat("#0.0").format(value2) + suffix[base];
                    }
                } else {
                    return new DecimalFormat("#0").format(value2) + suffix[base];
                }
            } else {
                return new DecimalFormat("#,##0").format(numValue);
            }
        } catch (Exception e) {
            return String.valueOf(number);
        }
    }

    public static String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, Objects.requireNonNull(capMatcher.group(1)).toUpperCase() + Objects.requireNonNull(capMatcher.group(2)).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

    public enum ReadStatus {
        send,
        delivered,
        read
    }

    public static void initLanguage(List<LanguageResponseMedley.Data> list) {
        for (LanguageResponseMedley.Data data : list) {
            language.put(data.getLanguageText(), data.getTranslatedText());
        }
    }

    public static String getString(String key) {
        return language.get(key);
    }

    public static boolean getFollowOrNot(Video.Data model) {
        Boolean inList = followUnfollow.get(model.getUserId());
        for (String s : followUnfollow.keySet()) {
            Timber.e("getFollowOrNot: " + s + " :" + followUnfollow.get(s));
        }
        if (inList != null) {
            return model.getFollowOrNot() || inList;
        } else {
            return model.getFollowOrNot();
        }
    }

    public static void checkUniqueKey(Context context) {
        if (Helper.isNetworkAvailable(context)) {
            if (!SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.UNIQUE_KEY).isEmpty())
                Global.apikey = SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.UNIQUE_KEY);
            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(Global.initRetrofit().getUniqueKey(Global.password, Global.email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe((UploadVideoResponse uploadVideoResponse, Throwable throwable) -> {
                        if (uploadVideoResponse.getData() != null) {
                            Global.apikey = uploadVideoResponse.getData();
                            SharedPrefreances.setSharedPreferenceString(context, SharedPrefreances.UNIQUE_KEY, uploadVideoResponse.getData());
                        }
                    }));
        }
    }

    public static float getFontSize(String key) {
        return sizeMap.get(key);
    }

    public static void setFontSize(SaveTextAppearanceModel.FontData fontData) {
        if (fontData != null) {
            size1 = Float.valueOf(fontData.getSize1());
            size2 = Float.valueOf(fontData.getSize2());
            size3 = Float.valueOf(fontData.getSize3());
            size4 = Float.valueOf(fontData.getSize4());
            size5 = Float.valueOf(fontData.getSize5());
        } else {
            size1 = 16;
            size2 = 14;
            size3 = 12;
            size4 = 10;
            size5 = 8;
        }
    }

    public static boolean isConnected(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

}
