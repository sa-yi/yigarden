package com.sayi.vdim.activities.mainfragments.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sayi.vdim.R;
import com.sayi.vdim.activities.ForumActivity;
import com.sayi.vdim.databinding.FragmentDashboardBinding;
import com.sayi.vdim.databinding.NavBlcockBinding;
import com.sayi.vdim.databinding.NavGridItemBinding;
import com.sayi.vdim.dz_entity.Forum;
import com.sayi.vdim.utils.Statusbar;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    DashboardViewModel dashboardViewModel;
    NavAdapter navAdapter;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.statusbarPlaceholder.setHeight(Statusbar.getStatusBHeight(requireActivity()));
        binding.statusbarPlaceholder.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.default_background));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        navAdapter = new NavAdapter();
        binding.navView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        binding.navView.setAdapter(navAdapter);
        dashboardViewModel.getForums().observe(getViewLifecycleOwner(), forums -> {
            navAdapter.setForums(forums);

        });
        dashboardViewModel.getForumCategory().observe(getViewLifecycleOwner(), forumCategories -> {
            for (Forum.Category category : forumCategories) {
                Log.d("Category", category.toString());
            }
            navAdapter.setCategories(forumCategories);
            navAdapter.notifyItemMoved(0, forumCategories.size());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class NavAdapter extends RecyclerView.Adapter<NavAdapter.ForumViewHolder> {
        private List<Forum.Category> categories = new ArrayList<>();
        private List<Forum> forums = new ArrayList<>();


        public void setCategories(List<Forum.Category> categories) {
            this.categories = categories;
        }

        public void setForums(List<Forum> forums) {
            this.forums = forums;
        }

        @NonNull
        @Override
        public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            NavBlcockBinding navBlcockBinding = NavBlcockBinding.inflate(inflater, parent, false);
            return new ForumViewHolder(navBlcockBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
            Forum.Category forum = categories.get(position);
            holder.bind(forum);
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }


        public class ForumViewHolder extends RecyclerView.ViewHolder {
            private NavBlcockBinding binding;

            public ForumViewHolder(NavBlcockBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(Forum.Category category) {
                binding.category.setText(category.getName());
                for (Forum forum : forums) {
                    if (category.getForums().contains(forum.getFid())) {
                        NavGridItemBinding gridItemBinding = NavGridItemBinding.inflate(getLayoutInflater());
                        gridItemBinding.name.setText(forum.getName());

                        Glide.with(requireActivity()).load(forum.getIconUrl()).override(240, 240).into(gridItemBinding.icon);

                        binding.grid.addView(gridItemBinding.getRoot());
                        gridItemBinding.getRoot().setOnClickListener(v -> {
                            Intent intent=new Intent(requireActivity(), ForumActivity.class);
                            Uri uri = new Uri.Builder().scheme("vdim")
                                    .authority("")  // authority 这里可以为空
                                    .path("/forum")
                                    .appendQueryParameter("fid", String.valueOf(forum.getFid()))
                                    .build();
                            intent.setData(uri);
                            requireActivity().startActivity(intent);
                        });
                    }
                }
            }
        }
    }
}