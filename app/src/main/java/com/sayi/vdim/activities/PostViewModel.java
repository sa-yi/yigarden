package com.sayi.vdim.activities;

import android.util.*;

import androidx.lifecycle.*;

import com.sayi.vdim.dz_entity.*;

import retrofit2.*;

public class PostViewModel extends ViewModel {
    private static final String TAG = "PostViewModel";
    private final DzService dzService = DzClient.getRetrofitInstance().create(DzService.class);

    private final MutableLiveData<ThreadData> postLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ThreadData> getPostLiveData() {
        return postLiveData;
    }


    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchPost(int postId) {
        dzService.getThread(postId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ThreadData> call, Response<ThreadData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, response.body().toString());
                    postLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch post");
                    Log.e(TAG, "Failed to fetch post");
                }
            }

            @Override
            public void onFailure(Call<ThreadData> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
                Log.e(TAG, "Error fetching post", t);
            }
        });
    }
}
