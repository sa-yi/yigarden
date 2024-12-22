package com.sayi.vdim.dz_entity;

import java.util.*;

import retrofit2.*;
import retrofit2.http.*;

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

    @GET("index.php?version=4&module=forumdisplay")
    Call<ForumDetailed> getForumDetailed(@Query("fid") int fid,@Query("page") int page);

    @GET("index.php?version=4&module=forumindex")
    Call<Forum> getForum();

}
