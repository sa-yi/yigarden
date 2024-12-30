package com.sayi.vdim.activities.mainfragments.home;

import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;

import androidx.annotation.*;
import androidx.core.content.*;
import androidx.fragment.app.*;
import androidx.lifecycle.*;
import androidx.recyclerview.widget.*;

import com.sayi.*;
import com.sayi.vdim.*;
import com.sayi.vdim.activities.*;
import com.sayi.vdim.adapter.*;
import com.sayi.vdim.databinding.*;
import com.sayi.vdim.dz_entity.*;
import com.sayi.vdim.utils.*;

import java.util.*;

public class HomeFragment extends Fragment {


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
        binding.statusbarPlaceholder.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.default_background));
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding.nickPostView
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        Objects.requireNonNull(binding.toolbar.toolbar.getTabAt(2)).select();
        dzDataAdapter = new ThreadDataAdapter.DzDataAdapter(requireActivity());
        binding.nickPostView.setAdapter(dzDataAdapter);
        binding.nickPostView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == dzDataAdapter.getItemCount() - 1) {
                    // 滑动到底部，触发加载更多
                    page++;
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


        binding.publish.setOnClickListener(v -> {
            //TODO 发布帖子界面开发中
            //Intent intent = new Intent(getContext(), WpPublishActivity.class);
            //startActivity(intent);
            MainApplication.toast("开发中...");
        });
        binding.message.setOnClickListener(v -> {
            //TODO 消息界面开发中
            //Intent intent = new Intent(getContext(), NotifyActivity.class);
            //startActivity(intent);
            MainApplication.toast("开发中...");
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