package com.sayi.vdim.activities.mainfragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sayi.MainApplication;
import com.sayi.vdim.activities.SearchActivity;
import com.sayi.vdim.adapter.HotTopicAdapter;
import com.sayi.vdim.adapter.ThreadDataAdapter;
import com.sayi.vdim.databinding.FragmentHomeBinding;
import com.sayi.vdim.dz_entity.HotTopic;
import com.sayi.vdim.dz_entity.ThreadData;
import com.sayi.vdim.utils.Statusbar;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    HotTopicAdapter hotTopicAdapter;
    ThreadDataAdapter.DzDataAdapter dzDataAdapter;
    View root;
    HomeViewModel homeViewModel;
    int page = 1;
    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        binding.statusbarPlaceholder.setHeight(Statusbar.getStatusBHeight(requireActivity()));
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        Objects.requireNonNull(binding.toolbar.getTabAt(1)).select();



        binding.hotTopic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        hotTopicAdapter =new HotTopicAdapter();
        binding.hotTopic.setAdapter(hotTopicAdapter);

        homeViewModel.hotTopicList.observe(getViewLifecycleOwner(),hotTopics -> {
            for(HotTopic topic:hotTopics){
                Log.d("Topic",topic.getTopic());
            }
            hotTopicAdapter.addValue((ArrayList<HotTopic>) hotTopics);
        });


        binding.nickPostView
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        dzDataAdapter = new ThreadDataAdapter.DzDataAdapter(requireActivity());
        binding.nickPostView.setAdapter(dzDataAdapter);
        binding.nickPostView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == dzDataAdapter.getItemCount() - 1) {
                    // 滑动到底部，触发加载更多
                    //page++;
                    //homeViewModel.fetchDzData(page);
                }
            }
        });
        homeViewModel.dzDataList.observe(getViewLifecycleOwner(), dzDatalist -> {
            for (ThreadData dzThreadData : dzDatalist) {
                dzDataAdapter.addData(dzThreadData);
                dzDataAdapter.notifyItemChanged(dzDataAdapter.getItemCount());
            }
            Log.d("dz_data_size", dzDatalist.size() + "");
        });


        homeViewModel.fetchHotTopicData();
        homeViewModel.fetchDzData(page);

        binding.loadMore.setOnClickListener(v->homeViewModel.fetchDzData(++page));

        homeViewModel.getFailedData().observe(getViewLifecycleOwner(), failedData -> {
            binding.nickPostView.setVisibility(View.GONE);
            MainApplication.toast("加载帖子失败");
        });

        binding.searchInput.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivity(intent);
        });

        binding.sign.setOnClickListener(v->{
            binding.sign.setText("已签到");
            binding.sign.setTextColor(0xff757575);
        });

        binding.publish.setOnClickListener(v->{
            MainApplication.toast("发布帖子");
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}