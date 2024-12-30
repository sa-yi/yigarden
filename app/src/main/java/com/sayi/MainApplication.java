package com.sayi;

import static com.sayi.vdim.Consts.*;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.widget.*;


import com.sayi.vdim.utils.*;

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
