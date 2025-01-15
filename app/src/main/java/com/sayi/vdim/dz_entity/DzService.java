package com.sayi.vdim.dz_entity;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DzService {
    @POST("/auth/login")
    @FormUrlEncoded
    Call<Map<String, Object>> login(@Field("username") String username, @Field("password") String password);

    @GET("threads")
    Call<ArrayList<ThreadData>> getHotThreads(@Query("page") int page);

    @GET("threads/{tid}")
    Call<ThreadData> getThread(@Path("tid") int tid);

    @GET("posts")
    Call<ArrayList<Post>> getPosts(@Query("tid") int tid);

    @POST("post_comment")
    @FormUrlEncoded
    Call<ResponseBody> comment(@Field("tid") int tid,@Field("message") String message);

    @GET("index.php?version=4&module=forumdisplay")
    Call<ForumDetailed> getForumDetailed(@Query("fid") int fid,@Query("page") int page);

    @GET("index.php?version=4&module=forumindex")
    Call<Forum> getForum();

}
