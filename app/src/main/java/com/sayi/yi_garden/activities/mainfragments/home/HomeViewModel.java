package com.sayi.yi_garden.activities.mainfragments.home;

import android.util.Base64;
import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sayi.MainApplication;
import com.sayi.yi_garden.api.ApiClient;
import com.sayi.yi_garden.api.ApiPostFeed;
import com.sayi.yi_garden.api.ApiService;
import com.sayi.yi_garden.entity.Announcement;
import com.sayi.yi_garden.entity.Recommend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {


    public MutableLiveData<List<ApiPostFeed>> postFeedsDataList;
    public MutableLiveData<List<Announcement>> announcementsDataList;

    public HomeViewModel() {

        postFeedsDataList = new MutableLiveData<>(new ArrayList<>());

        List<ApiPostFeed> postFeeds = new ArrayList<>();
        /*
        ApiPostFeed postFeed = new ApiPostFeed();
        postFeed.setAuthor(1);
        postFeed.setDate("1145-1-4");
        ApiPostFeed.RenderedField content = new ApiPostFeed.RenderedField();
        content.setRendered("加载中");
        postFeed.setContent(content);
        postFeeds.add(postFeed);
        ApiPostFeed postFeed1 = new ApiPostFeed();
        postFeeds.add(postFeed1);
        */
        postFeedsDataList.setValue(postFeeds);


        List<Announcement> announcements = new ArrayList<>();
        Announcement announcement = new Announcement("aaa", "a", "d");
        announcements.add(announcement);
        Announcement announcement1 = new Announcement("114514", "a", "d");
        announcements.add(announcement1);
        announcementsDataList = new MutableLiveData<>(new ArrayList<>());
        announcementsDataList.setValue(announcements);

    }

    public void fetchData(int page){
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<ApiPostFeed>> call = apiService.getPosts();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<ApiPostFeed>> call, @NonNull Response<List<ApiPostFeed>> response) {
                if (response.isSuccessful()) {

                    if(response.body()!=null) {
                        List<ApiPostFeed> postFeedList = response.body();

                        postFeedsDataList.setValue(postFeedList);
                    }


                } else {
                    Log.e("failed", "Failed to retrieve posts");
                    Log.e("failed",response.toString());
                    MainApplication.toast("Failed to retrieve posts");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ApiPostFeed>> call, @NonNull Throwable t) {
                Log.e("Call Failure", t.getMessage());
                MainApplication.toast("Call Failure");
            }
        });
    }

}