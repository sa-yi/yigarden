package com.sayi.vdim.dz_entity;


import android.content.pm.*;
import android.webkit.*;

import androidx.annotation.*;

import com.google.gson.*;
import com.sayi.*;

import java.io.*;
import java.security.cert.*;
import java.util.*;
import java.util.concurrent.*;

import javax.net.ssl.*;

import okhttp3.Response;
import okhttp3.*;
import retrofit2.*;
import retrofit2.converter.gson.*;

public class DzClient {
    private static final String BASE_URL = "https://i.lty.fan/api/mobile/";
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
    private static OkHttpClient getUnsafeOkHttpClient() {
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
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            // Add the interceptor for custom headers
            builder.addInterceptor(new CustomHeaderInterceptor());
            builder.cookieJar(new PersistentCookieJar());

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

    private static String getAppCode() {
        long appCode = -1;
        try {
            PackageManager packageManager = MainApplication.getContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(MainApplication.getContext().getPackageName(), 0);
            appCode = packageInfo.getLongVersionCode();  // 或者使用 versionCode，如果你需要整型值
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return String.valueOf(appCode);
    }

    public static class CustomHeaderInterceptor implements Interceptor {
        @NonNull
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();


            Request.Builder builder = originalRequest.newBuilder();
            builder.removeHeader("User-Agent");
            builder.addHeader("User-Agent", WebSettings.getDefaultUserAgent(MainApplication.getContext()));
            builder.addHeader("Version-Name", getAppVersion());
            builder.addHeader("Version-Code", getAppCode());
            Request newRequest = builder.build();

            return chain.proceed(newRequest);
        }
    }
    public static class PersistentCookieJar implements CookieJar {
        private final Map<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            // Save cookies from the response
            cookieStore.put(url.host(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            // Load cookies for the request
            List<Cookie> cookies = cookieStore.get(url.host());
            return cookies != null ? cookies : new ArrayList<>();
        }

        // ... 其他代码 ...
    }
}
