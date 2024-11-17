package com.sayi.yi_garden.activities.fragments;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sayi.yi_garden.entity.ApiClient;
import com.sayi.yi_garden.entity.ApiService;
import com.sayi.yi_garden.entity.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserBannerViewModel extends ViewModel {

    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final MutableLiveData<String> sendTime = new MutableLiveData<>();

    public LiveData<String> getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        userName.setValue(name);
    }

    public LiveData<String> getSendTime() {
        return sendTime;
    }

    public void setSendTime(String time) {
        sendTime.setValue(time);
    }

    public void fetchUserData(int id) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<User> call = apiService.getUser(id);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        setUserName(user.getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {

            }
        });

        setSendTime("2024-11-17");
    }
}
