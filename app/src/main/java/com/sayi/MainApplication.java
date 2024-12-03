package com.sayi;

import static com.sayi.vdim.Consts.sp_token;
import static com.sayi.vdim.Consts.sp_user_data;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.sayi.vdim.*;
import com.sayi.vdim.utils.DarkModeUtils;

public class MainApplication extends Application {
    private static MainApplication mContext;

    public static String token ="";
    public String getToken(){
        SharedPreferences sharedPreferences = getSharedPreferences(sp_user_data, MODE_PRIVATE);
        token = sharedPreferences.getString(sp_token,"");
        return token;
    }

    public void putDzCookie(String dzCookie){
        SharedPreferences sharedPreferences=getSharedPreferences(DzConsts.COOKIE_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(DzConsts.COOKIE,dzCookie);
        editor.apply();
    }

    public String getDzCookie(){
        SharedPreferences sharedPreferences=getSharedPreferences(DzConsts.COOKIE_PREFS,MODE_PRIVATE);
        return sharedPreferences.getString(DzConsts.COOKIE,"");
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
