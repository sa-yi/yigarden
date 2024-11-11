package com.sayi.yi_garden.activities.mainfragments.home;

import static android.content.Context.MODE_PRIVATE;
import static android.text.Html.FROM_HTML_MODE_LEGACY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.fastjson.JSON;
import com.sayi.MainApplication;
import com.sayi.yi_garden.R;
import com.sayi.yi_garden.activities.NotifyActivity;
import com.sayi.yi_garden.activities.PostActivity;
import com.sayi.yi_garden.activities.PublishActivity;
import com.sayi.yi_garden.activities.SearchActivity;
import com.sayi.yi_garden.api.ApiPostFeed;
import com.sayi.yi_garden.databinding.FragmentHomeBinding;
import com.sayi.yi_garden.databinding.NickPostBinding;
import com.sayi.yi_garden.entity.Announcement;
import com.sayi.yi_garden.entity.Recommend;
import com.sayi.yi_garden.utils.Dialog;
import com.sayi.yi_garden.utils.Statusbar;
import com.sayi.yi_garden.utils.Ticker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class HomeFragment extends Fragment {

    Ticker ticker;
    RecommendBarAdapter recommendBarAdapter;
    AnnounceAdapter announceAdapter;
    PostFeedAdapter postFeedAdapter;
    View root;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        binding.statusbarPlaceholder.setHeight(Statusbar.getStatusBHeight(requireActivity()));
        binding.statusbarPlaceholder.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.default_background));
        return root;
    }

    HomeViewModel homeViewModel;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding.nickPostView
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.announceBar.setPageTransformer(new AnnounceVerticalPageTransformer());


        postFeedAdapter = new PostFeedAdapter();
        homeViewModel.postFeedsDataList.observe(getViewLifecycleOwner(), apiPostFeeds -> {
            for (ApiPostFeed apiPostFeed : apiPostFeeds)
                Log.d("fetching posts", apiPostFeed.toString());
            postFeedAdapter.setPostFeeds(apiPostFeeds);
            binding.nickPostView.setAdapter(postFeedAdapter);
        });


        fetchData(0);

        announceAdapter = new AnnounceAdapter();
        binding.announceBar.setAdapter(announceAdapter);
        homeViewModel.announcementsDataList.observe(getViewLifecycleOwner(), announceAdapter::setAnnouncements);


        binding.searchInput.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivity(intent);
        });


        binding.upload.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PublishActivity.class);
            startActivity(intent);
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
    public void fetchData(int page){
        binding.nickPostView.setVisibility(View.GONE);
        homeViewModel.fetchData(page);
        binding.nickPostView.setVisibility(View.VISIBLE);
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

    class RecommendBarAdapter extends RecyclerView.Adapter<RecommendBarAdapter.RecommendViewHolder> {
        private List<Recommend> recommendDataList;

        public RecommendBarAdapter() {
        }

        public void setRecommendDataList(List<Recommend> _recommendDataList) {
            recommendDataList = _recommendDataList;
        }

        @NonNull
        @Override
        public RecommendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecommendViewHolder holder = new RecommendViewHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.recommand_card, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecommendViewHolder holder, int position) {
            Recommend data = recommendDataList.get(position % recommendDataList.size());
            holder.title.setText(data.getTitle());
            holder.cover.setImageResource(R.drawable.background);
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }

        class RecommendViewHolder extends RecyclerView.ViewHolder {
            ImageView cover;
            TextView title;

            public RecommendViewHolder(@NonNull View itemView) {
                super(itemView);
                cover = itemView.findViewById(R.id.cover);
                title = itemView.findViewById(R.id.title);
            }
        }
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

    class PostFeedAdapter extends RecyclerView.Adapter<PostFeedViewHolder> {
        private List<ApiPostFeed> postFeeds;

        @NonNull
        @Override
        public PostFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            NickPostBinding nickPostBinding = NickPostBinding.inflate(inflater, parent, false);
            return new PostFeedViewHolder(nickPostBinding);
        }

        public void setPostFeeds(List<ApiPostFeed> _postFeeds) {
            postFeeds = _postFeeds;
        }

        @Override
        public void onBindViewHolder(@NonNull PostFeedViewHolder holder, int position) {
            ApiPostFeed postFeed = postFeeds.get(position);
            holder.bind(postFeed);
        }

        @Override
        public int getItemCount() {
            return postFeeds.size();
        }

    }

    class PostFeedViewHolder extends RecyclerView.ViewHolder {
        public NickPostBinding binding;

        public PostFeedViewHolder(NickPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ApiPostFeed postFeed) {
            //binding.avator.setImageResource(postFeed.getAvatarResourceId());
            Log.d("onBinding", postFeed.toString());
            binding.userName.setText(postFeed.getAuthor() + "");
            binding.sendTime.setText(postFeed.getDate());
            binding.title.setText(postFeed.getTitle().getRendered());

            //String content=postFeed.getContent().getRendered();
            Spanned spannedText = Html.fromHtml(postFeed.getContent().getRendered(), FROM_HTML_MODE_LEGACY);
            String content=spannedText.toString();
            binding.content.setText(content);
            //binding.score.setText(String.valueOf(postFeed.getScore()));
            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), PostActivity.class);
                //intent.setData();
                Uri uri = new Uri.Builder().scheme("yigarden")
                        .authority("")  // authority 这里可以为空
                        .path("/viewpost")
                        .appendQueryParameter("id", postFeed.getId() + "")
                        .build();
                intent.setData(uri);
                startActivity(intent);
            });

            binding.follow.setOnClickListener(v -> {
                MainApplication.toast("followed");
            });
            binding.chat.setOnClickListener(v -> {
                MainApplication.toast("chat");
            });


            binding.dashboard.setOnClickListener(v -> {
                MainApplication.toast("dashboard");
            });
            binding.score.setOnClickListener(v -> {
                MainApplication.toast("thumbed up");
            });
            binding.comment.setOnClickListener(v -> {
                MainApplication.toast("comment");
            });
            binding.share.setOnClickListener(v -> {
                MainApplication.toast("shared");
            });
        }

    }

}