package com.sayi.yi_garden.entity;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/v1/token")
    Call<JwtToken> login(@Query("username") String username, @Query("password") String password);

    @GET("wp/v2/posts")
    Call<List<PostFeed>> getPosts(@Query("page") int page);

    @GET("wp/v2/posts/{id}")
    Call<PostFeed> getPost(@Path("id") int id);

    @GET("wp/v2/comments")
    Call<List<PostComment>> getComments(@Query("post") int id);


    @GET("wp/v2/users/{id}")
    Call<User> getUser(@Path("id") int id);

    @Multipart
    @POST("wp/v2/media")
    Call<MediaItem> uploadMedia(@Part MultipartBody.Part file);
}
