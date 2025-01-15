package com.sayi.vdim.activities.mainfragments.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sayi.vdim.dz_entity.DzClient;
import com.sayi.vdim.dz_entity.DzService;
import com.sayi.vdim.dz_entity.Forum;

import java.util.ArrayList;

import retrofit2.Call;

public class DashboardViewModel extends ViewModel {
    String TAG="DashboardViewModel";

    public LiveData<ArrayList<Forum.Category>> getForumCategory() {
        return forumCategory;
    }
    private MutableLiveData<ArrayList<Forum.Category>> forumCategory;


    public MutableLiveData<ArrayList<Forum>> getForums() {
        return forums;
    }

    private MutableLiveData<ArrayList<Forum>> forums;


    public DashboardViewModel() {
        forumCategory =new MutableLiveData<>(new ArrayList<>());
        forums=new MutableLiveData<>(new ArrayList<>());

        DzService dzService=DzClient.getRetrofitInstance().create(DzService.class);

        Call<Forum> call2=dzService.getForum();
        /*call2.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Forum> call, Response<Forum> response) {
                Log.d(TAG,response.body().toString());
                Forum forumRes=response.body();
                ArrayList<Forum> forumsRes=forumRes.getForums();
                if(!forumsRes.isEmpty()){
                    forums.postValue(forumsRes);
                }
                ArrayList<Forum.Category> forumCategoriesRes=forumRes.getCategories();
                if(!forumCategoriesRes.isEmpty()) {
                    forumCategory.postValue(forumCategoriesRes);
                }
            }

            @Override
            public void onFailure(Call<Forum> call, Throwable throwable) {
                Log.e(TAG,throwable.getMessage());
            }
        });*/
    }

}