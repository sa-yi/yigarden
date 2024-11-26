package com.sayi.yi_garden.activities.mainfragments.user;

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

public class UserViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    private final MutableLiveData<User> user;

    ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
    public UserViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is user fragment");

        user=new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<User> getUserData(){return user;}
    public void fetchUserData(int uid){

        Call<User> callUser= apiService.getUser(uid);
        callUser.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User userData = response.body();
                    user.postValue(userData);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {

            }
        });
    }
}