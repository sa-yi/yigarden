package com.sayi.vdim.utils;

import android.content.*;

import com.bumptech.glide.*;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.*;
import com.bumptech.glide.load.model.*;
import com.bumptech.glide.module.*;
import com.sayi.vdim.dz_entity.*;

import java.io.*;

import okhttp3.*;
@GlideModule
public class MAppGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        // 使用自定义的 OkHttpClient
        OkHttpClient client = DzClient.getUnsafeOkHttpClient();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        // 在这里可以自定义其他 Glide 配置
    }
}