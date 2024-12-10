package com.sayi.vdim.sayi_music_entity;

import java.util.*;

import retrofit2.*;
import retrofit2.http.*;

public interface SyService {
    @GET("songlist")
    Call<ArrayList<Music>> getSongList();

    @FormUrlEncoded
    @POST("info")
    Call<MusicFully> getInfo(@Field("id") int id);
}
