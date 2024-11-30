package com.sayi.vdim.activities.mainfragments.home;

import static android.text.Html.*;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.core.content.*;
import androidx.fragment.app.*;
import androidx.lifecycle.*;
import androidx.recyclerview.widget.*;
import androidx.viewpager2.widget.*;

import com.bumptech.glide.*;
import com.bumptech.glide.load.*;
import com.bumptech.glide.load.engine.*;
import com.bumptech.glide.request.*;
import com.bumptech.glide.request.target.*;
import com.sayi.*;
import com.sayi.vdim.R;
import com.sayi.vdim.activities.*;
import com.sayi.vdim.databinding.*;
import com.sayi.vdim.dz_entity.*;
import com.sayi.vdim.entity.*;
import com.sayi.vdim.utils.*;

import java.util.*;

import retrofit2.*;

public class HomeFragment extends Fragment {

    Ticker ticker;
    AnnounceAdapter announceAdapter;
    DzDataAdapter dzDataAdapter;
    View root;
    HomeViewModel homeViewModel;
    ApiService service = ApiClient.getRetrofitInstance().create(ApiService.class);
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        binding.statusbarPlaceholder.setHeight(Statusbar.getStatusBHeight(requireActivity()));
        binding.statusbarPlaceholder.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.default_background));
        return root;
    }

    //@SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding.nickPostView
                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.announceBar.setPageTransformer(new AnnounceVerticalPageTransformer());


        dzDataAdapter = new DzDataAdapter();
        binding.nickPostView.setAdapter(dzDataAdapter);
        homeViewModel.dzDataList.observe(getViewLifecycleOwner(), dzDatalist -> {
            binding.loadMore.setVisibility(View.GONE);
            for (ThreadData.Variables dzThreadData : dzDatalist) {
                Log.d("dz_data", dzThreadData.toString());
                dzDataAdapter.addData(dzThreadData);
                dzDataAdapter.notifyItemChanged(dzDataAdapter.getItemCount());
            }
            Log.d("dz_data_size", dzDatalist.size()+"");
            if (dzDatalist.size() == 25)
                binding.loadMore.setVisibility(View.VISIBLE);
        });


        binding.nickPostView.setVisibility(View.GONE);
        homeViewModel.fetchDzData(page);
        binding.nickPostView.setVisibility(View.VISIBLE);

        binding.loadMore.setOnClickListener(v ->
                homeViewModel.fetchDzData(++page)
        );

        announceAdapter = new AnnounceAdapter();
        binding.announceBar.setAdapter(announceAdapter);
        homeViewModel.announcementsDataList.observe(getViewLifecycleOwner(), announceAdapter::setAnnouncements);


        binding.searchInput.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivity(intent);
        });


        binding.publish.setOnClickListener(v -> {
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
    int page=1;
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

    class DzDataAdapter extends RecyclerView.Adapter<DzDataViewHolder> {
        private List<ThreadData.Variables> dzThreadDataList = new ArrayList<>();

        public void addData(ThreadData.Variables threadData) {
            dzThreadDataList.add(threadData);
        }

        @NonNull
        @Override
        public DzDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            NickPostBinding nickPostBinding = NickPostBinding.inflate(inflater, parent, false);
            return new DzDataViewHolder(nickPostBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull DzDataViewHolder holder, int position) {
            ThreadData.Variables threadData = dzThreadDataList.get(position);
            holder.bind(threadData);
        }

        @Override
        public int getItemCount() {
            return dzThreadDataList.size();
        }
    }

    class DzDataViewHolder extends RecyclerView.ViewHolder {
        NickPostBinding binding;

        public DzDataViewHolder(NickPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ThreadData.Variables threadData) {
            binding.userName.setText(threadData.getAuthor());
            String sendTime=threadData.getLastpost();
            sendTime=sendTime.replace("&nbsp;"," ");
            binding.sendTime.setText(sendTime);
            binding.title.setText(threadData.getSubject());
            binding.expert.setText(threadData.getMessage());
            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), PostActivity.class);
                //intent.setData();
                Uri uri = new Uri.Builder().scheme("vdim")
                        .authority("")  // authority 这里可以为空
                        .path("/viewpost")
                        .appendQueryParameter("id", threadData.getTid())
                        .build();
                intent.setData(uri);
                startActivity(intent);
            });
        }
    }
}