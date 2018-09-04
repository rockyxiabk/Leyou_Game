package com.leyou.game.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.leyou.game.Constants;
import com.leyou.game.bean.Result;
import com.leyou.game.bean.UserData;
import com.leyou.game.util.api.AppApi;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description : com.leyou.game.util
 *
 * @author : rocky
 * @Create Time : 2017/3/24 上午11:10
 * @Modified Time : 2017/3/24 上午11:10
 */
public class HttpUtil {
    private static OkHttpClient okHttpClient;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static String userAgent;

    static {
        userAgent = String.format("%s/%s (Linux; Android %s; %s %s Build/%s)", System.getProperty("java.vm.name"),
                System.getProperty("java.vm.version"), Build.VERSION.RELEASE, Build.BRAND, Build.MODEL, Build.DISPLAY);

        Interceptor mTokenInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                HttpUrl httpUrl = originalRequest.url().newBuilder()
                        .addQueryParameter("imei", Constants.getIMEI())
                        .addQueryParameter("channelId", Constants.getChannelId())
                        .addQueryParameter("versionName", Constants.getVerName())
                        .build();

                Request authorised = originalRequest.newBuilder()
                        .header("userId", UserData.getInstance().getId())
                        .header("pkg", Constants.getPackageName())
                        .header("mobile", Build.BRAND + " " + Build.MODEL)
                        .header("User-Agent", userAgent)
                        .url(httpUrl)
                        .build();
                return chain.proceed(authorised);
            }
        };

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(mTokenInterceptor)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }
                });
        okHttpClient = builder.build();
    }

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public static <T> T createApi(Class<T> clazz, String host) {
        Object api = null;
        if (null == api) {
            api = new Retrofit.Builder().client(okHttpClient).baseUrl(host)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build().create(clazz);
        }
        return (T) api;
    }

    public static <T> Subscription subscribe(Observable<T> observable, Observer<T> observer) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    /**
     * 上传图片
     *
     * @param filePath
     * @param api
     * @param callback
     */
    public static void uploadImage(String filePath, String api, final Callback callback) {
        final String imageName = filePath.substring(filePath.lastIndexOf("/") + 1);
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        OkHttpClient mOkHttpClient = HttpUtil.getOkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", "file")
                .addFormDataPart("file", imageName, RequestBody.create(MEDIA_TYPE_PNG, new File(filePath)))
                .build();

        Request request = new Request.Builder()
                .url(Constants.URL + api)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                callback.onResponse(call, response);
            }
        });
    }

    /**
     * 更新个推id
     *
     * @param clientId
     */
    public static void updateClientId(String clientId) {
        HttpUtil.subscribe(HttpUtil.createApi(AppApi.class, Constants.URL).updateClientId(clientId), new Observer<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Result result) {
                if (result.result == 1) {
                    LogUtil.d("clientId", "---->clientId update success");
                } else {
                    LogUtil.d("clientId", "---->clientId update failed");
                }
            }
        });
    }

    /**
     * 判断wifi是否连接
     *
     * @return
     */
    public static boolean isWifiConnected(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 检查当前网络是否可用
     *
     * @return 网络是否可用（wifi 或者 mobile）
     */

    public static boolean isNetworkConnected(Context mContext) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
