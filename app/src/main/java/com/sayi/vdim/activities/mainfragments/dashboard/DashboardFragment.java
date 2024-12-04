package com.sayi.vdim.activities.mainfragments.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.*;

import com.sayi.vdim.databinding.*;
import com.sayi.vdim.dz_entity.*;

import java.util.*;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class NavAdapter extends RecyclerView.Adapter<NavViewHolder>{
        private List<ForumNav> forumNavs=new ArrayList<>();

        @NonNull
        @Override
        public NavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(parent.getContext());
            NavBlcockBinding navBlcockBinding=NavBlcockBinding.inflate(inflater,parent,false);
            return new NavViewHolder(navBlcockBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull NavViewHolder holder, int position) {
            ForumNav forumNav= forumNavs.get(position);
            holder.bind(forumNav);
        }

        @Override
        public int getItemCount() {
            return forumNavs.size();
        }
    }

    public class NavViewHolder extends RecyclerView.ViewHolder{
        private NavBlcockBinding binding;
        public NavViewHolder(NavBlcockBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        public void bind(ForumNav forumNav){

        }
    }
}