package com.sayi.vdim.activities.mainfragments.home;

import android.util.*;

import androidx.lifecycle.*;

import com.sayi.*;
import com.sayi.vdim.dz_entity.*;

import java.util.*;

import retrofit2.*;

public class HomeViewModel extends ViewModel {

    public MutableLiveData<List<Announcement>> announcementsDataList;
    public MutableLiveData<List<ThreadData>> dzDataList;

    public HomeViewModel() {

        List<Announcement> announcements = new ArrayList<>();
        Announcement announcement = new Announcement("这里是公告", "公告的内容", "跳转的链接");
        announcements.add(announcement);
        Announcement announcement1 = new Announcement("这是另一个公告", "a", "d");
        announcements.add(announcement1);
        announcementsDataList = new MutableLiveData<>(new ArrayList<>());
        announcementsDataList.setValue(announcements);


        dzDataList = new MutableLiveData<>(new ArrayList<>());

    }

    public void fetchDzData(int page) {

        DzService dzService = DzClient.getRetrofitInstance().create(DzService.class);
        Call<ThreadsResponse> call = dzService.getHotThreads(page);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ThreadsResponse> call, Response<ThreadsResponse> response) {
                if (response.isSuccessful()) {
                    ThreadsResponse threadsResponse = response.body();

                    List<ThreadData> data = threadsResponse.getData();
                    if ((data != null)) {
                        dzDataList.postValue(data);
                    }
                } else {
                    Log.e("dz_failed", response.toString());
                    MainApplication.toast("读取帖子失败");
                }
            }

            @Override
            public void onFailure(Call<ThreadsResponse> call, Throwable throwable) {
                Log.e("dz_error", throwable.toString());
                failed.postValue("error");
            }
        });
    }

    public MutableLiveData<String> getFailedData() {
        return failed;
    }

    private MutableLiveData<String> failed= new MutableLiveData<>();
}