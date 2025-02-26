package com.sayi;

import static com.sayi.vdim.Consts.sp_token;
import static com.sayi.vdim.Consts.sp_user_data;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.sayi.vdim.utils.DarkModeUtils;

public class MainApplication extends Application {
    public static String token = "";
    private static MainApplication mContext;

    public static void toast(final String str) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show());
    }

    public static MainApplication getContext() {
        return mContext;
    }

    public String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences(sp_user_data, MODE_PRIVATE);
        token = sharedPreferences.getString(sp_token, "");
        return token;
    }

    @SuppressLint("ApplySharedPref")
    public void putToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(sp_user_data, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(sp_token, token);
        editor.commit();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplication();
        CrashHandler.getInstance().init(mContext);
        DarkModeUtils.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public MainApplication getApplication() {
        return this;
    }
}
