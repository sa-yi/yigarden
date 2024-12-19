package com.sayi.vdim.customentity;

import okhttp3.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface CustomService {
    @POST("/auth/login")
    @FormUrlEncoded
    Call<CustomToken> login(@Field("username")String username,@Field("password")String password);
}
