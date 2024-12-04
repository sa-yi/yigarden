package com.sayi.vdim.activities.mainfragments.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sayi.MainApplication;
import com.sayi.vdim.dz_entity.*;
import com.sayi.vdim.entity.ApiClient;
import com.sayi.vdim.entity.PostFeed;
import com.sayi.vdim.entity.ApiService;
import com.sayi.vdim.entity.Announcement;

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
        Announcement announcement = new Announcement("这里是公告", "公告的内容", "跳转的链接");
        announcements.add(announcement);
        Announcement announcement1 = new Announcement("这是另一个公告", "a", "d");
        announcements.add(announcement1);
        announcementsDataList = new MutableLiveData<>(new ArrayList<>());
        announcementsDataList.setValue(announcements);


        dzDataList=new MutableLiveData<>(new ArrayList<>());

    }

    public MutableLiveData<List<ThreadData.Variables>> dzDataList;

    public void fetchDzData(int page){

        DzService dzService=DzClient.getRetrofitInstance().create(DzService.class);
        Call<ThreadsResponse> call=dzService.getHotThreads(page);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ThreadsResponse> call, Response<ThreadsResponse> response) {
                if(response.isSuccessful()) {
                    //Log.d("dz",response.body().toString());
                    ThreadsResponse threadsResponse = response.body();
                    ThreadsResponse.Variables variables = threadsResponse.getVariables();

                    List<ThreadData.Variables> data = variables.getData();
                    if ((data!=null)) {
                        for (ThreadData.Variables datus : data) {
                            //Log.d("dz_data", data.toString());
                        }
                        dzDataList.postValue(data);
                    }else {

                    }
                }else {
                    Log.e("dz_failed",response.toString());
                    MainApplication.toast("读取帖子失败");
                }
            }

            @Override
            public void onFailure(Call<ThreadsResponse> call, Throwable throwable) {
                Log.e("dz_error",throwable.toString());
                MainApplication.toast("获取帖子失败");
            }
        });
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