package com.sayi.vdim.dz_entity;


import android.content.pm.*;
import android.util.*;
import android.webkit.*;

import androidx.annotation.*;

import com.google.gson.*;
import com.ihsanbal.logging.*;
import com.sayi.*;

import java.io.*;
import java.security.cert.*;
import java.util.*;

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


    // 创建一个Cookie存储容器
    final static Map<String, List<Cookie>> cookieStore = new HashMap<>();

    // 实现CookieJar接口
    static CookieJar cookieJar = new CookieJar() {
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            // 保存服务器返回的Cookie
            cookieStore.put(url.host(), cookies);
            if(url.host().equals("i.lty.fan")){
                StringBuilder finalCookieBuilder= new StringBuilder();
                for(Cookie cookie:cookies){
                    Log.d("Cookie",cookie.toString());
                    finalCookieBuilder.append(cookie.name()).append("=").append(cookie.value()).append(";");
                }
                String finalCookie=finalCookieBuilder.toString();
                Log.d("finalCookie",finalCookie);
                MainApplication.getContext().putDzCookie(finalCookie);
            }

        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            // 加载保存的Cookie
            List<Cookie> unmodifiableCookies = cookieStore.get(url.host());
            List<Cookie> modifiableCookies = new ArrayList<>(); // 创建一个新的可修改列表

            // 如果存在不可修改的Cookie列表，则复制到新的可修改列表中
            if (unmodifiableCookies != null) {
                modifiableCookies.addAll(unmodifiableCookies);
            }

            // 获取其他来源的Cookie字符串
            String cookieString = MainApplication.getContext().getDzCookie();
            String[] cookiePairs = cookieString.split(";");
            for (String cookiePair : cookiePairs) {
                String[] nameValue = cookiePair.trim().split("=", 2);
                if (nameValue.length == 2) {
                    String name = nameValue[0];
                    String value = nameValue[1];
                    // 创建Cookie对象并添加到新的可修改列表中
                    Cookie cookie = Cookie.parse(url, name + "=" + value);
                    if (cookie != null) {
                        modifiableCookies.add(cookie);
                    }
                }
            }

            // 返回新的可修改列表
            return modifiableCookies;
        }

    };

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
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);


            builder.hostnameVerifier((hostname, session) -> true);

            // Add the interceptor for custom headers
            builder.addInterceptor(new CustomHeaderInterceptor());
            builder.cookieJar(cookieJar);


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
            //builder.removeHeader("User-Agent");//带cookie时不带ua也能过
            builder.addHeader("User-Agent", WebSettings.getDefaultUserAgent(MainApplication.getContext()));

            builder.addHeader("Referer","https://i.lty.fan/");

            builder.addHeader("Version-Name", getAppVersion());
            builder.addHeader("Version-Code", getAppCode());

            builder.addHeader("Package-Name",MainApplication.getContext().getPackageName());


            String dzCookie=MainApplication.getContext().getDzCookie();
            if(!Objects.equals(dzCookie, "")){
                builder.removeHeader("Cookie");
                builder.addHeader("Cookie",dzCookie);
            }


            Request newRequest = builder.build();

            Response response = chain.proceed(newRequest);


            Log.d("Request",response.toString());
            Headers headers=response.headers();
            Log.d("Cookie",headers.names().toString());
            return response;
        }
    }
}
