package com.sayi.vdim.activities.mainfragments.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sayi.MainApplication;
import com.sayi.vdim.dz_entity.*;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    public final MutableLiveData<List<ThreadData>> dzDataList=new MutableLiveData<>(new ArrayList<>());

    public final MutableLiveData<List<HotTopic>> hotTopicList=new MutableLiveData<>(new ArrayList<>());

    public void fetchHotTopicData(){
        List<HotTopic> hotTopics=new ArrayList<>();
        hotTopics.add(new HotTopic("洛天依 ilem——大吉",87214558));
        hotTopics.add(new HotTopic("看一看新闻怎么说",7214558));
        hotTopics.add(new HotTopic("好事也没提我",162000));
        hotTopics.add(new HotTopic("坏事也没提我",82710));
        //hotTopics.add(new HotTopic("今天也努力工作",5271));
        hotTopicList.postValue(hotTopics);
    }

    public void fetchDzData(int page) {

        DzService dzService = DzClient.getRetrofitInstance().create(DzService.class);
        Call<ArrayList<ThreadData>> call = dzService.getHotThreads(page);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ThreadData>> call, @NonNull Response<ArrayList<ThreadData>> response) {
                if (response.isSuccessful()) {

                    List<ThreadData> data = response.body();
                    if ((data != null)) {
                        for(ThreadData threadData:data){
                            threadData.setMessage("我想起来了\uD83D\uDE2D，我全都想起来了\uD83D\uDE2D，我想起来了\uD83D\uDE2D，我全都想起来了\uD83D\uDE2D，我想起来了\uD83D\uDE2D，我全都想起来了\uD83D\uDE2D我们\uD83D\uDE2D我们\uD83D\uDE2D走吧\uD83D\uDE2B就算我们无法让大雨停下\uD83D\uDE22还有我\uD83D\uDE2B陪你在雨里放肆......");
                        }
                        dzDataList.postValue(data);
                    }
                } else {
                    Log.e("dz_failed", response.toString());
                    MainApplication.toast("读取帖子失败");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ThreadData>> call, @NonNull Throwable throwable) {
                Log.e("dz_error", throwable.toString());
                failed.postValue("error");
            }
        });
    }

    public MutableLiveData<String> getFailedData() {
        return failed;
    }

    private final MutableLiveData<String> failed = new MutableLiveData<>();
}