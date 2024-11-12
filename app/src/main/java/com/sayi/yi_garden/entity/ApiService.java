package com.sayi.yi_garden.entity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("api/v1/token")
    Call<String> login(String username,String password);

    @GET("wp/v2/posts")
    Call<List<PostFeed>> getPosts();

    @GET("wp/v2/posts/{id}")
    Call<PostFeed> getPost(@Path("id") int id);


    @GET("wp/v2/users/{id}")
    Call<User> getUser(@Path("id") int id);
}
