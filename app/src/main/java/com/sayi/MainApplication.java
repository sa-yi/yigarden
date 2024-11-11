package com.sayi;

import static com.sayi.yi_garden.Consts.sp_token;
import static com.sayi.yi_garden.Consts.sp_user_data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.sayi.yi_garden.utils.DarkModeUtils;

import java.io.FileWriter;
import java.io.IOException;

public class MainApplication extends Application {
    private static MainApplication mContext;

    public static String token ="";
    public String getToken(){
        SharedPreferences sharedPreferences = getSharedPreferences(sp_user_data, MODE_PRIVATE);
        token = sharedPreferences.getString(sp_token,"");
        return token;
    }

    public static void toast(final String str) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show());
    }

    public static MainApplication getContext() {
        return mContext;
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
