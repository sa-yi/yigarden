package com.sayi.vdim.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sayi.MainApplication;
import com.sayi.vdim.databinding.ItemFollowingClassBinding;
import com.sayi.vdim.dz_entity.FollowingClassification;

import java.util.ArrayList;
import java.util.List;

public class MemberClassAdapter extends RecyclerView.Adapter<MemberClassAdapter.MemberViewHolder> {
    private List<FollowingClassification> followingClassifications = new ArrayList<>();

    public MemberClassAdapter() {
        for(int i=0;i<=10;i++)
        followingClassifications.add(new FollowingClassification());
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemFollowingClassBinding binding = ItemFollowingClassBinding.inflate(inflater, parent, false);
        return new MemberViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        FollowingClassification classification = followingClassifications.get(position);
        holder.bind(classification);
    }

    @Override
    public int getItemCount() {
        return followingClassifications.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        ItemFollowingClassBinding binding;

        public MemberViewHolder(ItemFollowingClassBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(FollowingClassification followingClassification) {
            binding.tag.setText(followingClassification.getTag());

            binding.getRoot().setOnClickListener(v -> {
                MainApplication.toast(binding.tag.getText().toString());
                if (followingClassification.getId()==-1){
                    MainApplication.toast("显示全部");
                }
            });
        }
    }
}
