package com.sayi.vdim.sayi_music_entity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SyService {
    @GET("songlist")
    Call<ArrayList<Music>> getSongList();

    @FormUrlEncoded
    @POST("info")
    Call<MusicFully> getInfo(@Field("id") int id);
}
