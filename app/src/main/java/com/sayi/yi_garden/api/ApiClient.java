package com.sayi.yi_garden.api;

import android.util.Log;
import android.webkit.WebSettings;

import androidx.annotation.NonNull;

import com.sayi.MainApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static String cookie="";
    private static final String BASE_URL = "https://bbs.66ccff.cc/wp-json/";
    private static Retrofit retrofit;

    public static void setCookie(String cookie){
        ApiClient.cookie=cookie;
    }

    public static Retrofit getRetrofitInstance() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        okBuilder.addInterceptor(new CustomHeaderInterceptor());
        OkHttpClient okHttpClient=okBuilder.build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static class CustomHeaderInterceptor implements Interceptor {
        @NonNull
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();

            Request.Builder builder = originalRequest.newBuilder();
            builder.addHeader("Cookie", MainApplication.cookies);
            builder.removeHeader("User-Agent");
            builder.addHeader("User-Agent", WebSettings.getDefaultUserAgent(MainApplication.getContext()));
            Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }
    }

}
