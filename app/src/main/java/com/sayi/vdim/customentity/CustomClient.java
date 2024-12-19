package com.sayi.vdim.customentity;

import androidx.annotation.*;

import com.google.gson.*;
import com.sayi.vdim.dz_entity.*;

import java.security.cert.*;

import javax.net.ssl.*;

import okhttp3.*;
import retrofit2.*;
import retrofit2.converter.gson.*;

public class CustomClient {
    private static final String BASE_URL="http://dzapi.sa-yi.cn/";

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
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);


            builder.hostnameVerifier((hostname, session) -> true);

            // Add the interceptor for custom headers
            builder.addInterceptor(new DzClient.CustomHeaderInterceptor());


            //builder.addInterceptor(new LoggingInterceptor.Builder().build());

            return builder.build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
