package com.sayi.yi_garden.activities.mainfragments.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sayi.MainApplication;
import com.sayi.yi_garden.entity.ApiClient;
import com.sayi.yi_garden.entity.PostFeed;
import com.sayi.yi_garden.entity.ApiService;
import com.sayi.yi_garden.entity.Announcement;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeViewModel extends ViewModel {


    public MutableLiveData<List<PostFeed>> postFeedsDataList;
    public MutableLiveData<List<Announcement>> announcementsDataList;

    public HomeViewModel() {
        retrofit=ApiClient.buildRetrofitInstance();
        apiService = retrofit.create(ApiService.class);

        postFeedsDataList = new MutableLiveData<>(new ArrayList<>());

        List<PostFeed> postFeeds = new ArrayList<>();
        postFeedsDataList.setValue(postFeeds);


        List<Announcement> announcements = new ArrayList<>();
        Announcement announcement = new Announcement("aaa", "a", "d");
        announcements.add(announcement);
        Announcement announcement1 = new Announcement("114514", "a", "d");
        announcements.add(announcement1);
        announcementsDataList = new MutableLiveData<>(new ArrayList<>());
        announcementsDataList.setValue(announcements);

    }
    Retrofit retrofit;
    ApiService apiService;
    public void fetchData(int page) {
        Call<List<PostFeed>> call = apiService.getPosts(page);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<PostFeed>> call, @NonNull Response<List<PostFeed>> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        List<PostFeed> postFeedList = response.body();
                        MainApplication.toast(postFeedList.size()+"");
                        postFeedsDataList.setValue(postFeedList);
                    }else {
                        MainApplication.toast("暂无帖子");
                    }

                } else {
                    Log.e("failed", "Failed to retrieve posts,error response:"+response);
                    MainApplication.toast("获取帖子失败");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PostFeed>> call, @NonNull Throwable t) {
                Log.e("Call Failure", t.getMessage());
                MainApplication.toast("Call Failure");
            }
        });
    }

    public void fetchData(){
        fetchData(page);
        page++;
    }
    private int page=1;
}