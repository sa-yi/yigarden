package com.sayi.vdim.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sayi.vdim.databinding.ItemMemberBinding;
import com.sayi.vdim.dz_entity.DzUser;

import java.util.ArrayList;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {
    private List<DzUser> dzUsers = new ArrayList<>();

    public MemberAdapter(){
        for(int i=0;i<=10;i++) dzUsers.add(new DzUser());
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMemberBinding binding = ItemMemberBinding.inflate(inflater, parent, false);
        return new MemberViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        DzUser user = dzUsers.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return dzUsers.size();
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        ItemMemberBinding binding;

        public MemberViewHolder(ItemMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DzUser user) {
            binding.name.setText(user.getUsername());
        }
    }
}
