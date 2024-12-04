package com.sayi.vdim.activities.mainfragments.dashboard;

import android.util.*;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sayi.vdim.dz_entity.*;

import java.util.*;

import retrofit2.*;

public class DashboardViewModel extends ViewModel {
    String TAG="DashboardViewModel";

    public DashboardViewModel() {

        DzService dzService=DzClient.getRetrofitInstance().create(DzService.class);
        Call<ForumNav> call=dzService.getNav();
        /*call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ForumNav> call, Response<ForumNav> response) {
                Log.d(TAG,response.body().toString());
                ForumNav forumNav=response.body();
                ArrayList<ForumNav> forumNavs=forumNav.getVariables().getForumNavs();
                if(!forumNavs.isEmpty()) {
                    for (ForumNav nav : forumNavs) {
                        Log.d(TAG, nav.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<ForumNav> call, Throwable throwable) {
                Log.e(TAG,throwable.getMessage());
            }
        });*/
        Call<Forum> call2=dzService.getForum();
        call2.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Forum> call, Response<Forum> response) {
                Log.d(TAG,response.body().toString());
                Forum forumRes=response.body();
                ArrayList<Forum> forums=forumRes.getForums();
                if(!forums.isEmpty()) {
                    for (Forum forum : forums) {
                        Log.d(TAG, forum.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Forum> call, Throwable throwable) {
                Log.e(TAG,throwable.getMessage());
            }
        });
    }

}