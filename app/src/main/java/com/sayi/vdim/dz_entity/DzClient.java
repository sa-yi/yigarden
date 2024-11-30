package com.sayi.vdim.dz_entity;


import android.content.pm.*;
import android.util.*;
import android.webkit.*;

import androidx.annotation.*;

import com.android.volley.*;
import com.google.gson.*;
import com.sayi.*;

import java.io.*;
import java.security.cert.*;
import java.util.*;
import java.util.concurrent.*;

import javax.net.ssl.*;

import okhttp3.Request;
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






            //TODO:正式环境删除Cookie
            builder.addHeader("Cookie","__tst_cookieid=2790950423#; Hm_lvt_7fb4bf767e5250d350c2d11caf730a3b=1727272755; HMACCOUNT=F7E8CB4D3D0E641F; H65N_2132_nofavfid=1; H65N_2132_smile=5D1; H65N_2132_seccodecSe09gGS=11.481db5792ca291e558; H65N_2132_home_diymode=1; H65N_2132_seccodecSo9t1Fk=40.949f08b874c4dd188f; H65N_2132_seccodecSh22Slv=6.1363420493815a047c; H65N_2132_user_skin=blue; H65N_2132_onlineindex=1; H65N_2132_seccodecSakLi7l=2.d5cb6dba73aebd7720; H65N_2132_seccodecSTcwimZ=9.717319f6a1eaf5004b; H65N_2132_seccodecSSnu4an=1.1c98b709c2242a8023; Hm_lpvt_7fb4bf767e5250d350c2d11caf730a3b=1727927314; H65N_2132_seccodecSrY063f=6.1363420493815a047c; PHPSESSID=f0jicshdlu94oqhtmfalqj4ric; H65N_2132_seccodecSAr4zJZ74Er=135.437a9e154763475bd3; H65N_2132_seccodecSJZ74Er=136.2183d355da500b2c8a; H65N_2132_seccodecSr1UCUY=56.6f162fa9d646dc0343; H65N_2132_seccodecSY8F14F=474.54854a3ba52fd4d408; H65N_2132_seccodecSYDQFjl=482.9aebdb35c4454fe08c; H65N_2132_seccodecSy7j5BD=593.4e9a8f166aefeb2dd2; H65N_2132_seccodecSZFiVsH=643.a6ad3b8223f67615c3; H65N_2132_seccodecSUo8U9R=49.2f572cb47272f8a3e9; sl-waiting-state=done; H65N_2132_home_readfeed=1731680522; H65N_2132_seccodecSvA2tIw=527.fc0fe4c44135583eca; H65N_2132_secqaaqSvA2tIw=528.4ab932b6f7ccf82037; H65N_2132_lastviewtime=247%7C1732378542; H65N_2132_seccodecSIFWflI=47.56a7d6e10a71908f9c; H65N_2132_saltkey=ORbqNmwP; H65N_2132_lastvisit=1732550251; H65N_2132_auth=d2e10tnSwykGfcGDE3rtgEEF2hbsOHc4d%2Br5aNCEnlTJG9QJqSl%2FxnooPc0gkmam9V8EGCLsMZlu3Fqy9xFDK3A; H65N_2132_seccodecSnPw6tV=364.14ebe205e31bcc8835; guard=GY5XHaC/jx72a0XpOY9xVA==; guardret=345.71111111111117; H65N_2132_seccodecSF7H6OR=368.cc9b2d577c8912f246; H65N_2132_seccodecSP9p9gP=370.497f3feb6bf29517f3; __tst_cookieid=4096819337#; __tst_status=2790950423#; popup=hide; H65N_2132_ulastactivity=be8e0SS8%2B82ErPh4wdxEJkFDIo77q0I1zYfkucvxzrtVsiTnwHqP; H65N_2132_visitedfid=2D45D22D34D27D16D8; H65N_2132_seccodecSgq50o9G00=576.7fce6ff0cd0125972c; H65N_2132_seccodecSgq50o9=577.fc13c389331f5c80c0; sl-session=O/+2SoYxS2deZtAjF1dlvg==; H65N_2132_viewid=tid_620; H65N_2132_st_t=247%7C1732894807%7C03a1c65c7e5241b2b98f57044bdf9f73; H65N_2132_forum_lastvisit=D_45_1732711759D_2_1732894807; H65N_2132_seccodecSV1Vz26sPa=638.be8264725576fb4c98; H65N_2132_lastcheckfeed=247%7C1732894914; sl-waiting-session=737cbc86bf0146d6392cc12c935f36c9; H65N_2132_seccodecSV1Vz26=1084.52741943d961622da3; H65N_2132_sid=i9O1DD; H65N_2132_lip=39.144.196.220%2C1732939939; H65N_2132_lastact=1732941957%09index.php%09viewthread; H65N_2132_st_p=247%7C1732941957%7C99322a52d5559c2393975ff8ec63022a");




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
    }
}
