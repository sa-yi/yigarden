package com.sayi.vdim.activities.mainfragments.dashboard;

import android.os.*;
import android.util.*;
import android.view.*;

import androidx.annotation.*;
import androidx.core.content.*;
import androidx.fragment.app.*;
import androidx.lifecycle.*;
import androidx.recyclerview.widget.*;

import com.sayi.vdim.*;
import com.sayi.vdim.databinding.*;
import com.sayi.vdim.dz_entity.*;
import com.sayi.vdim.utils.*;

import java.util.*;

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
        binding.navView.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false));
        binding.navView.setAdapter(navAdapter);
        dashboardViewModel.getForumCategory().observe(getViewLifecycleOwner(), forums -> {
            for (Forum forum : forums) {
                Log.d("Forum", forum.toString());
            }
            navAdapter.setForums(forums);
            navAdapter.notifyItemMoved(0, forums.size());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class NavAdapter extends RecyclerView.Adapter<ForumViewHolder> {
        private List<Forum> forums = new ArrayList<>();


        public void setForums(List<Forum> forums) {
            this.forums = forums;
            Log.d("Adapter",forums.toString());
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
            Forum forum = forums.get(position);
            holder.bind(forum);
        }

        @Override
        public int getItemCount() {
            return forums.size();
        }
    }

    public class ForumViewHolder extends RecyclerView.ViewHolder {
        private NavBlcockBinding binding;

        public ForumViewHolder(NavBlcockBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Forum forum) {
            binding.category.setText(forum.getName());
        }
    }
}