package com.sayi.vdim.dz_entity;

import com.sayi.vdim.entity.*;

import retrofit2.*;
import retrofit2.http.*;

public interface DzService {
    @GET("index.php?version=4&module=hotthread")
    Call<ThreadsResponse> getHotThreads(@Query("page") int page);

    @GET("index.php?version=4&module=viewthread")
    Call<ThreadData> getThread(@Query("tid") int tid);

    @GET("index.php?version=4&module=profile")
    Call<User> getSelf();
}
