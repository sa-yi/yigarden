package com.sayi.vdim.activities.mainfragments.home;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.core.content.*;
import androidx.fragment.app.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.*;
import androidx.recyclerview.widget.*;
import androidx.viewpager2.widget.*;

import com.sayi.MainApplication;
import com.sayi.vdim.*;
import com.sayi.vdim.activities.*;
import com.sayi.vdim.adapter.*;
import com.sayi.vdim.databinding.*;
import com.sayi.vdim.dz_entity.*;
import com.sayi.vdim.utils.*;
import com.sayi.vdim.utils.Dialog;

import java.util.*;

public class HomeFragment extends Fragment {

    Ticker ticker;
    AnnounceAdapter announceAdapter;
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
        binding.announceBar.setPageTransformer(new AnnounceVerticalPageTransformer());


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
                    homeViewModel.fetchDzData(page);
                }
            }
        });
        homeViewModel.dzDataList.observe(getViewLifecycleOwner(), dzDatalist -> {
            binding.loadMore.setVisibility(View.GONE);
            for (ThreadData dzThreadData : dzDatalist) {
                dzDataAdapter.addData(dzThreadData);
                dzDataAdapter.notifyItemChanged(dzDataAdapter.getItemCount());
            }
            Log.d("dz_data_size", dzDatalist.size() + "");
            if (dzDatalist.size() == 15)
                binding.loadMore.setVisibility(View.VISIBLE);
        });


        binding.nickPostView.setVisibility(View.GONE);
        homeViewModel.fetchDzData(page);
        binding.nickPostView.setVisibility(View.VISIBLE);

        binding.loadMore.setOnClickListener(v ->
                homeViewModel.fetchDzData(++page)
        );
        homeViewModel.getFailedData().observe(getViewLifecycleOwner(),failedData->{
            MainApplication.toast("获取帖子失败，正在重新获取cookie...");
            Intent intent=new Intent(requireActivity(),WebLoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        announceAdapter = new AnnounceAdapter();
        binding.announceBar.setAdapter(announceAdapter);
        homeViewModel.announcementsDataList.observe(getViewLifecycleOwner(), announceAdapter::setAnnouncements);


        binding.searchInput.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivity(intent);
        });


        binding.publish.setOnClickListener(v -> {
            //TODO 发布帖子界面暂时不开启
            //Intent intent = new Intent(getContext(), WpPublishActivity.class);
            //startActivity(intent);
        });
        binding.message.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NotifyActivity.class);
            startActivity(intent);
        });


        ticker = new Ticker();
        ticker.setInterval(2000);
        ticker.addOnTickListener(
                () -> binding.announceBar.setCurrentItem(binding.announceBar.getCurrentItem() + 1, true));
        ticker.start();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) ticker.stop();
        else ticker.resume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ticker.stop();
        binding = null;
    }

    class AnnounceAdapter extends RecyclerView.Adapter<AnnounceViewHolder> {
        private List<Announcement> announcements;

        public void setAnnouncements(List<Announcement> _announcements) {
            announcements = _announcements;
        }

        @NonNull
        @Override
        public AnnounceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.announce_content, parent, false);
            return new AnnounceViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull AnnounceViewHolder holder, int position) {
            Announcement announcement = announcements.get(position % announcements.size());
            // String ann=announcements[position];
            holder.announceContent.setText(announcement.title);
            holder.announceContent.setTextColor(Color.WHITE);
            holder.announceContent.setOnClickListener(v -> {
                // Toast.makeText(getContext(),announceContent.getText(),Toast.LENGTH_SHORT).show();
                Dialog.init(getContext()).setupDialog(announcement.title, announcement.contents).show();
            });
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }

    }

    class AnnounceViewHolder extends RecyclerView.ViewHolder {
        public TextView announceContent;

        public AnnounceViewHolder(@NonNull View itemView) {
            super(itemView);
            announceContent = itemView.findViewById(R.id.announce_content);
        }
    }

    class AnnounceVerticalPageTransformer implements ViewPager2.PageTransformer {
        @Override
        public void transformPage(@NonNull View view, float position) {
            if (position >= -1 && position <= 1) {
                view.setTranslationX(view.getWidth() * -position);
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);
            }
        }
    }


}