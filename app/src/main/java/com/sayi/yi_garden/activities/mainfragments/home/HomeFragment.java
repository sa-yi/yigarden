package com.sayi.yi_garden.activities.mainfragments.home;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sayi.MainApplication;
import com.sayi.yi_garden.R;
import com.sayi.yi_garden.activities.NotifyActivity;
import com.sayi.yi_garden.activities.PostActivity;
import com.sayi.yi_garden.activities.PublishActivity;
import com.sayi.yi_garden.activities.SearchActivity;
import com.sayi.yi_garden.activities.fragments.UserBannerFragment;
import com.sayi.yi_garden.entity.PostFeed;
import com.sayi.yi_garden.databinding.FragmentHomeBinding;
import com.sayi.yi_garden.databinding.NickPostBinding;
import com.sayi.yi_garden.entity.Announcement;
import com.sayi.yi_garden.utils.Dialog;
import com.sayi.yi_garden.utils.Statusbar;
import com.sayi.yi_garden.utils.Ticker;

import java.util.List;

public class HomeFragment extends Fragment {

    Ticker ticker;
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
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding.nickPostView
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.announceBar.setPageTransformer(new AnnounceVerticalPageTransformer());


        postFeedAdapter = new PostFeedAdapter();
        binding.nickPostView.setAdapter(postFeedAdapter);
        homeViewModel.postFeedsDataList.observe(getViewLifecycleOwner(), apiPostFeeds -> {
            for (PostFeed apiPostFeed : apiPostFeeds)
                Log.d("fetching posts", apiPostFeed.toString());
            postFeedAdapter.setPostFeeds(apiPostFeeds);
            postFeedAdapter.notifyDataSetChanged();
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
        private List<PostFeed> postFeeds;

        @NonNull
        @Override
        public PostFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            NickPostBinding nickPostBinding = NickPostBinding.inflate(inflater, parent, false);
            return new PostFeedViewHolder(nickPostBinding);
        }

        public void setPostFeeds(List<PostFeed> postFeeds) {
            this.postFeeds = postFeeds;
        }

        @Override
        public void onBindViewHolder(@NonNull PostFeedViewHolder holder, int position) {
            PostFeed postFeed = postFeeds.get(position);
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

        public void bind(PostFeed postFeed) {
            //binding.avator.setImageResource(postFeed.getAvatarResourceId());
            Log.d("onBinding", postFeed.toString());
            binding.userName.setText(postFeed.getAuthor() + "");
            binding.sendTime.setText(postFeed.getDate());
            binding.title.setText(postFeed.getTitle().getRendered());
            postFeed.getAvatarUrl(url -> {
                Log.d("getavatar",url);
                Glide.with(requireActivity()).load(url).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // 加载失败时的逻辑处理，例如：显示错误信息，记录日志等
                        MainApplication.toast("加载头像失败");
                        return false; // 返回false表示你不想处理这个事件，Glide会继续调用error()方法设置的占位图
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // 加载成功时的逻辑处理
                        return false; // 返回false表示你不想处理这个事件
                    }
                }).placeholder(R.drawable.default_avator).into(binding.avator);
            });

            Spanned expert = Html.fromHtml(postFeed.getExcerpt().getRendered(), FROM_HTML_MODE_LEGACY);
            //String content=expert.toString();
            binding.expert.setText(expert);


            //binding.score.setText(String.valueOf(postFeed.getScore()));
            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), PostActivity.class);
                //intent.setData();
                Uri uri = new Uri.Builder().scheme("sayi")
                        .authority("")  // authority 这里可以为空
                        .path("/viewpost")
                        .appendQueryParameter("id", postFeed.getId() + "")
                        .build();
                intent.setData(uri);
                startActivity(intent);
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