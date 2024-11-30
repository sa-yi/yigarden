package com.sayi.vdim.activities;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sayi.vdim.dz_entity.*;
import com.sayi.vdim.entity.PostComment;
import com.sayi.vdim.entity.PostFeed;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewModel extends ViewModel {
    private static final String TAG = "PostViewModel";
    private final DzService dzService = DzClient.getRetrofitInstance().create(DzService.class);

    private final MutableLiveData<ThreadData> postLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PostComment>> commentsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<ThreadData> getPostLiveData() {
        return postLiveData;
    }

    public LiveData<List<PostComment>> getCommentsLiveData() {
        return commentsLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchPost(int postId) {
        dzService.getThread(postId).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ThreadData> call, Response<ThreadData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG,response.body().toString());
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

    /*public void fetchComments(int postId) {
        dzService.getComments(postId).enqueue(new Callback<List<PostComment>>() {
            @Override
            public void onResponse(Call<List<PostComment>> call, Response<List<PostComment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentsLiveData.postValue(response.body());
                } else {
                    errorMessage.postValue("Failed to fetch comments");
                    Log.e(TAG, "Failed to fetch comments");
                }
            }

            @Override
            public void onFailure(Call<List<PostComment>> call, Throwable t) {
                errorMessage.postValue(t.getMessage());
                Log.e(TAG, "Error fetching comments", t);
            }
        });
    }*/
}
