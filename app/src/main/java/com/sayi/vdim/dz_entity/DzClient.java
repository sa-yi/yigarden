package com.sayi.vdim.dz_entity;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sayi.MainApplication;

import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DzClient {
    private static final String BASE_URL = "https://api.lty.fan/";

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = buildRetrofitInstance();
        }
        return retrofit;
    }

    public static Retrofit buildRetrofitInstance() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @NonNull
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            //TODO:加上允许抓包
            //builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);


            builder.hostnameVerifier((hostname, session) -> true);

            // Add the interceptor for custom headers
            builder.addInterceptor(new CustomHeaderInterceptor());


            //builder.addInterceptor(new LoggingInterceptor.Builder().build());

            return builder.build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getAppVersion() {
        try {
            PackageManager packageManager = MainApplication.getContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(MainApplication.getContext().getPackageName(), 0);
            return packageInfo.versionName;  // 或者使用 versionCode，如果你需要整型值
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "unknown"; // 如果获取失败，返回未知
    }

    @NonNull
    private static String getAppCode() {
        long appCode;
        try {
            PackageManager packageManager = MainApplication.getContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(MainApplication.getContext().getPackageName(), 0);
            appCode = packageInfo.getLongVersionCode();  // 或者使用 versionCode，如果你需要整型值
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "-1";
        }
        return String.valueOf(appCode);
    }

    public static class CustomHeaderInterceptor implements Interceptor {
        @NonNull
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();


            Request.Builder builder = originalRequest.newBuilder();
            builder.removeHeader("User-Agent");//带cookie时不带ua也能过
            builder.addHeader("User-Agent", "AndroidApp");//TODO 修改header的信息

            builder.addHeader("Version-Name", getAppVersion());
            builder.addHeader("Version-Code", getAppCode());

            builder.addHeader("Package-Name", MainApplication.getContext().getPackageName());
            builder.addHeader("Referer","https://api.lty.fan/");

            Log.d("token",MainApplication.getContext().getToken());
            builder.addHeader("Authorization","Bearer "+MainApplication.getContext().getToken());

            Request newRequest = builder.build();


            return chain.proceed(newRequest);
        }
    }
}
