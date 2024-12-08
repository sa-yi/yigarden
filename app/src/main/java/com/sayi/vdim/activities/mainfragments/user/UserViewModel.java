package com.sayi.vdim.activities.mainfragments.user;

import android.util.*;

import androidx.lifecycle.*;

import com.sayi.vdim.dz_entity.*;

import retrofit2.*;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    private final MutableLiveData<DzUser> user;

    public UserViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is user fragment");

        user = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<DzUser> getUserData() {
        return user;
    }

    public void fetchUserData(){
        DzService dzService = DzClient.getRetrofitInstance().create(DzService.class);
        Call<DzUser> call = dzService.getSelf();
        call.enqueue(new Callback<>() {

            @Override
            public void onResponse(Call<DzUser> call, Response<DzUser> response) {
                if(response.isSuccessful()){
                    DzUser dzUser= response.body();
                    Log.d("User",dzUser.toString());
                    user.postValue(dzUser);
                }
            }

            @Override
            public void onFailure(Call<DzUser> call, Throwable throwable) {

            }
        });
    }
}