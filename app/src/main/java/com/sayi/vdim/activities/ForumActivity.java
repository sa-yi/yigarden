package com.sayi.vdim.activities;

import android.content.*;
import android.os.*;
import android.util.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;
import androidx.recyclerview.widget.*;

import com.sayi.*;
import com.sayi.vdim.adapter.*;
import com.sayi.vdim.databinding.ActivityForumBinding;
import com.sayi.vdim.dz_entity.*;

import retrofit2.*;

public class ForumActivity extends AppCompatActivity {
    static String TAG="ForumActivity";
    ActivityForumBinding binding;
    ThreadDataAdapter.DzDataAdapter dzDataAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        int fid=intent.getIntExtra("fid",-1);

        setSupportActionBar(binding.toolbar);

        binding.thread
                .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dzDataAdapter = new ThreadDataAdapter.DzDataAdapter(this);
        binding.thread.setAdapter(dzDataAdapter);
        binding.thread.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == dzDataAdapter.getItemCount() - 1) {
                    // 滑动到底部，触发加载更多
                    if(!finished) {
                        page++;
                        fetch(fid, page);
                    }else if(showLastMessage){
                        MainApplication.toast("您已浏览完毕该版块下所有帖子");
                        showLastMessage=false;
                    }
                }
            }
        });


        dzService=DzClient.getRetrofitInstance().create(DzService.class);

        fetch(fid,page);
    }
    boolean showLastMessage=true;

    boolean finished=false;
    int threadCount=0;
    int page=1;
    DzService dzService;
    void fetch(int fid,int page){
        Call<ForumDetailed> call= dzService.getForumDetailed(fid,page);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ForumDetailed> call, Response<ForumDetailed> response) {
                if(response.isSuccessful()) {
                    ForumDetailed forumDetailed = response.body();
                    if(forumDetailed!=null) {
                        String name=forumDetailed.getForum().getName();
                        threadCount=forumDetailed.getForum().getThreadCount();
                        setTitle(name);
                        for(ThreadData threadData: forumDetailed.getThreadData()){
                            Log.d("ThreadData",threadData.getSubject());
                            dzDataAdapter.addData(threadData);
                            dzDataAdapter.notifyItemChanged(-1);
                        }

                        Log.d("ThreadCount",dzDataAdapter.getItemCount()+":"+threadCount);
                        if (dzDataAdapter.getItemCount()>= threadCount) {
                            //TODO 已加载完所有帖子
                            finished=true;
                        }
                    }

                }else {
                    MainApplication.toast("获取板块信息失败");
                }
            }

            @Override
            public void onFailure(Call<ForumDetailed> call, Throwable throwable) {
                MainApplication.toast("获取信息失败");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
