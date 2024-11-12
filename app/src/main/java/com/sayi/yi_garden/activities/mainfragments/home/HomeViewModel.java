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

public class HomeViewModel extends ViewModel {


    public MutableLiveData<List<PostFeed>> postFeedsDataList;
    public MutableLiveData<List<Announcement>> announcementsDataList;

    public HomeViewModel() {

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

    public void fetchData(int page) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<List<PostFeed>> call = apiService.getPosts();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<PostFeed>> call, @NonNull Response<List<PostFeed>> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        List<PostFeed> postFeedList = response.body();
                        MainApplication.toast(postFeedList.size()+"");
                        postFeedsDataList.setValue(postFeedList);
                    }else {
                        MainApplication.toast("null post");
                    }

                } else {
                    Log.e("failed", "Failed to retrieve posts");
                    Log.e("failed", response.toString());
                    MainApplication.toast("Failed to retrieve posts");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PostFeed>> call, @NonNull Throwable t) {
                Log.e("Call Failure", t.getMessage());
                MainApplication.toast("Call Failure");
            }
        });
    }

}