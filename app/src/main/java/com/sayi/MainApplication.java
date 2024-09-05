package com.sayi;

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
    private static Context mContext;

    public static String cookies="";
    public String getCookies(){
        SharedPreferences sharedPreferences =getSharedPreferences("data", MODE_PRIVATE);
        cookies= sharedPreferences.getString("cookie","");
        return cookies;
    }
    public static void setCookies(String cookies){
        MainApplication.cookies=cookies;
    }

    public static void toast(final String str) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show());
    }

    public static void write(String fileName, String content, boolean add) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, add);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(String content, boolean add) {
        write("/sdcard/test.txt", content, add);
    }

    public static void write(String content) {
        write("/sdcard/test.txt", content, false);
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        CrashHandler.getInstance().init(mContext);
        DarkModeUtils.init(this);
        getCookies();
        cookies="wordpress_sec_bdf5fb41c0e0e935d2c6e30a9ee42a6c=%E9%A3%92%E7%BF%8A%7C1724588618%7C5tytNOpLXp6TE187bEbI0O7CSXOvQmXvF9jO867xCCp%7C9812c857b786e96c4d4ae9d0002c69744f2cf87491d5fbb150c6c72abfdcc21f";
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public MainApplication getApplication() {
        return this;
    }


}
