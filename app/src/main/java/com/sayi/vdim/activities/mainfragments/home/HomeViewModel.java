package com.sayi.vdim.activities.mainfragments.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sayi.MainApplication;
import com.sayi.vdim.dz_entity.DzClient;
import com.sayi.vdim.dz_entity.DzService;
import com.sayi.vdim.dz_entity.ThreadData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    public final MutableLiveData<List<ThreadData>> dzDataList=new MutableLiveData<>(new ArrayList<>());;


    public void fetchDzData(int page) {

        DzService dzService = DzClient.getRetrofitInstance().create(DzService.class);
        Call<ArrayList<ThreadData>> call = dzService.getHotThreads(page);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ThreadData>> call, @NonNull Response<ArrayList<ThreadData>> response) {
                if (response.isSuccessful()) {

                    List<ThreadData> data = response.body();
                    if ((data != null)) {
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