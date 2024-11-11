package com.sayi.yi_garden.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/v1/token")
    Call<String> login(String username,String password);

    @GET("wp/v2/posts")
    Call<List<ApiPostFeed>> getPosts();

    @GET("wp/v2/posts/{id}")
    Call<ApiPostFeed> getPost(@Path("id") int id);
}
