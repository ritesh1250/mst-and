
package com.meest.meestbhart.utilss;
import android.content.Context;
import com.meest.BuildConfig;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiUtils {
    public static String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=";
    public static String CONTEST_URL = "https://medley-contest.com";
    public static String BASE_URL = BuildConfig.BASE_URL;
    public static String SOCKET_URL = BuildConfig.BASE_URL_SOCKET;
    public static String MEDLEY_URL = BuildConfig.BASE_URL_MEDLEY;

    public static CertificatePinner certPinnerMeest = new CertificatePinner.Builder()
            .add("mist-one.com", BuildConfig.MEEST_SSL)
            .build();
    public static CertificatePinner certPinnerSocket = new CertificatePinner.Builder()
            .add("mist360online", BuildConfig.SOCKET_SSL)
            .build();
    public static CertificatePinner certPinnerMedley = new CertificatePinner.Builder()
            .add("themeestbharat.com", BuildConfig.MEDLEY_SSL)
            .build();

//    public static String BASE_URL_MEEST_LOCAL = "http://192.168.100.181:3003/";
//    public static String BASE_URL_MEEST_STAGING = "http://65.1.89.89:3003/";
//    public static String BASE_URL_MEEST_RELEASE = "https://www.mist-one.com/";
//    public static String BASE_URL_MEEST_RELEASE_TESTING = "http://3.108.162.50:3005/";
//    public static String BASE_URL = Global.currentConfig == Global.Config.Local ? BASE_URL_MEEST_LOCAL : Global.currentConfig == Global.Config.Release ? BASE_URL_MEEST_RELEASE : Global.currentConfig == Global.Config.ReleaseTesting ? BASE_URL_MEEST_RELEASE_TESTING : BASE_URL_MEEST_STAGING;
//
//    public static String SOCKET_URL_LOCAL = "http://192.168.100.181:3004/";
//    public static String SOCKET_URL_STAGING = "http://15.206.235.65:3004/";
//    public static String SOCKET_URL_RELEASE = "https://www.mist360online.com/";
//    public static String SOCKET_URL_RELEASE_TESTING = "http://3.109.93.90:3006/";
//    public static String SOCKET_URL = Global.currentConfig == Global.Config.Local ? SOCKET_URL_LOCAL : Global.currentConfig == Global.Config.Release ? SOCKET_URL_RELEASE : Global.currentConfig == Global.Config.ReleaseTesting ? SOCKET_URL_RELEASE_TESTING : SOCKET_URL_STAGING;
//
//    public static String BASE_URL_ELASTIC_SEARCH_RELEASE = "https://Mist24.com/";
//    public static String BASE_URL_ELASTIC_SEARCH_STAGING = "http://65.0.48.47:9200/";
//    public static String BASE_URL_ELASTIC_SEARCH = Global.currentConfig == Global.Config.Local || Global.currentConfig == Global.Config.Staging ? BASE_URL_ELASTIC_SEARCH_STAGING : BASE_URL_ELASTIC_SEARCH_RELEASE;
    static WebApi requestAPI;
    public static Retrofit getClient() {
        Retrofit retrofit = null;
        OkHttpClient client;
        if (BuildConfig.DEBUG){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS).build();
        }else {
            client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .certificatePinner(ApiUtils.certPinnerMeest)
                    .build();
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    public static WebApi getClientHeader(final Context context) {
        Retrofit retrofit = null;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
//                Request request = chain.request().newBuilder().addHeader("APP_KEY", "8447126401").build();
                Request request = chain.request().newBuilder().addHeader("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN)).build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client;
        if (BuildConfig.DEBUG){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(12000, TimeUnit.SECONDS)
                    .readTimeout(12000, TimeUnit.SECONDS)
                    .writeTimeout(12000, TimeUnit.SECONDS).build();
        }else {
            client = new OkHttpClient.Builder().connectTimeout(12000, TimeUnit.SECONDS)
                    .readTimeout(12000, TimeUnit.SECONDS)
                    .writeTimeout(12000, TimeUnit.SECONDS)
                    .certificatePinner(ApiUtils.certPinnerMeest)
                    .build();
        }
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        requestAPI = retrofit.create(WebApi.class);
        return requestAPI;
    }

}

