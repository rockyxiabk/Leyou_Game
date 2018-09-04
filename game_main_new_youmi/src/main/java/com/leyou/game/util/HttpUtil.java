package com.leyou.game.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.leyou.game.Constants;
import com.leyou.game.bean.UserData;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

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
import okio.Buffer;
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
                        .addQueryParameter("source", Constants.SOURCE)
                        .addQueryParameter("versionCode", Constants.getVerCode() + "")
                        .build();

                Request authorised = originalRequest.newBuilder()
                        .header("userId", UserData.getInstance().getId())
                        .header("token", UserData.getInstance().getToken())
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

        String cer = "-----BEGIN CERTIFICATE-----\n" +
                "MIIFYjCCBEqgAwIBAgIQTEzYoPxP6q4VVKh/CQ7ahzANBgkqhkiG9w0BAQsFADCB\n" +
                "yjELMAkGA1UEBhMCVVMxFzAVBgNVBAoTDlZlcmlTaWduLCBJbmMuMR8wHQYDVQQL\n" +
                "ExZWZXJpU2lnbiBUcnVzdCBOZXR3b3JrMTowOAYDVQQLEzEoYykgMjAwNiBWZXJp\n" +
                "U2lnbiwgSW5jLiAtIEZvciBhdXRob3JpemVkIHVzZSBvbmx5MUUwQwYDVQQDEzxW\n" +
                "ZXJpU2lnbiBDbGFzcyAzIFB1YmxpYyBQcmltYXJ5IENlcnRpZmljYXRpb24gQXV0\n" +
                "aG9yaXR5IC0gRzUwHhcNMTYwNjA3MDAwMDAwWhcNMjYwNjA2MjM1OTU5WjCBlDEL\n" +
                "MAkGA1UEBhMCVVMxHTAbBgNVBAoTFFN5bWFudGVjIENvcnBvcmF0aW9uMR8wHQYD\n" +
                "VQQLExZTeW1hbnRlYyBUcnVzdCBOZXR3b3JrMR0wGwYDVQQLExREb21haW4gVmFs\n" +
                "aWRhdGVkIFNTTDEmMCQGA1UEAxMdU3ltYW50ZWMgQmFzaWMgRFYgU1NMIENBIC0g\n" +
                "RzEwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCkN8hYylrZCZihZgN2\n" +
                "5FsiT+qfOv8rKi3MbRIsZ2TUqsS5e1eDLPXI8IP4XXUZLWt9hlqmDpqiZa5mLSBj\n" +
                "KDX3iWq/FaOc8l1AsbeOhr9ZESCoEorqm6S9wAL+HX7hLY/7p03SSNSAO+/gr2o7\n" +
                "ciWu3jhd+H4dzGNNDN0nCuRIOX7rTGYI5mOb8QWJRC6H/3MlUYpBt9VV+l2FVNhB\n" +
                "LJuofF3TNJojVHximZnTEkybg/r9AZc2TkDHJX1BA6rNjXG8l5iSCL9ICJCBUPB5\n" +
                "z/s3hQBQkOALXN88QTIrlj53XpWpqxYdQJrOFbtWi18WW3ZAnGAscd8vZ5UIg3KL\n" +
                "AmoBAgMBAAGjggF2MIIBcjASBgNVHRMBAf8ECDAGAQH/AgEAMC8GA1UdHwQoMCYw\n" +
                "JKAioCCGHmh0dHA6Ly9zLnN5bWNiLmNvbS9wY2EzLWc1LmNybDAOBgNVHQ8BAf8E\n" +
                "BAMCAQYwLgYIKwYBBQUHAQEEIjAgMB4GCCsGAQUFBzABhhJodHRwOi8vcy5zeW1j\n" +
                "ZC5jb20wYQYDVR0gBFowWDBWBgZngQwBAgEwTDAjBggrBgEFBQcCARYXaHR0cHM6\n" +
                "Ly9kLnN5bWNiLmNvbS9jcHMwJQYIKwYBBQUHAgIwGRoXaHR0cHM6Ly9kLnN5bWNi\n" +
                "LmNvbS9ycGEwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUFBwMCMCkGA1UdEQQi\n" +
                "MCCkHjAcMRowGAYDVQQDExFTeW1hbnRlY1BLSS0yLTU1NTAdBgNVHQ4EFgQUXGGe\n" +
                "sHZBqWqqQwvhx24wKW6xzTYwHwYDVR0jBBgwFoAUf9Nlp8Ld7LvwMAnzQzn6Aq8z\n" +
                "MTMwDQYJKoZIhvcNAQELBQADggEBAGHqRXEvjeE/CpuVSPHyPKJYFsqWxP/a4quX\n" +
                "cRCRsy+ki4EP8qT7NfPnkEogxZvlMctHsWgdtTbp9ShXbqCnqXPCw575BZH2rEKN\n" +
                "xI30CWr6U47n4h2hSnaJxJeeA+xKsA1Vk4v8eLu7xwRlBwhZEsYNFAVpD3YEToek\n" +
                "H877QzZrZ6EdG/3Vg6sdtHDQ4i/U87syTmyM2l8vXOGIZDd1Wr6dqee2FtCfhvAc\n" +
                "WMbvh/J6sBOHMq0Vn5G8Tp6iUwsRlY1z7LaQKAlnlOiiZVhhe+1gvzJBHC0t+Hr2\n" +
                "2YHwaoKDLhSB0F/gGkziNQ+py1hFne4MEOuvzOxJpjn0+wRIbBk=\n" +
                "-----END CERTIFICATE-----";

        SSLContext sslContext = null;
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            keyStore.setCertificateEntry("0", certificateFactory.generateCertificate(new Buffer().writeUtf8(cer).inputStream()));
            sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
//        if (null != sslContext) {
//            builder.sslSocketFactory(sslContext.getSocketFactory());
//        }
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
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
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
