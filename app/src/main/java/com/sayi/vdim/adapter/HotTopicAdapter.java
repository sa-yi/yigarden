package com.sayi.vdim.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;

import com.sayi.MainApplication;
import com.sayi.vdim.R;
import com.sayi.vdim.databinding.ItemHotTopicBinding;
import com.sayi.vdim.dz_entity.HotTopic;

import java.util.ArrayList;
import java.util.List;

public class HotTopicAdapter extends RecyclerView.Adapter<HotTopicAdapter.HotTopicViewHolder> {
    private final List<HotTopic> hotTopicList = new ArrayList<>();

    public void addValue(ArrayList<HotTopic> hotTopics) {
        hotTopicList.addAll(hotTopics);
        notifyItemChanged(0,hotTopics.size());
    }

    @NonNull
    @Override
    public HotTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemHotTopicBinding binding = ItemHotTopicBinding.inflate(inflater, parent, false);
        return new HotTopicViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HotTopicViewHolder holder, int position) {
        if (position == getItemCount()-1) {
            holder.itemView.getRootView().findViewById(R.id.divider).setVisibility(View.GONE);
        }
        HotTopic hotTopic = hotTopicList.get(position);
        holder.bind(position,hotTopic);
    }

    @Override
    public int getItemCount() {
        return hotTopicList.size();
    }

    public static class HotTopicViewHolder extends RecyclerView.ViewHolder {
        static final int startColor = Color.parseColor("#FF0000");
        static final int endColor = Color.parseColor("#FFFF00");

        ItemHotTopicBinding binding;

        public HotTopicViewHolder(ItemHotTopicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int index,HotTopic hotTopic) {
            // 使用ArgbEvaluator计算颜色
            int rankColor; // 默认颜色，可以根据需要设置
            if (index >= 1 && index <= 5) {
                @SuppressLint("RestrictedApi") Object evaluatedColor = ArgbEvaluator.getInstance().evaluate(
                    (float) index / 5, startColor, endColor
                );
                if (evaluatedColor instanceof Integer) {
                    rankColor = (Integer) evaluatedColor;
                    binding.rank.setTextColor(rankColor);
                }
            }


            binding.rank.setText(index+1+"");
            binding.title.setText(hotTopic.getTopic());
            binding.hotpot.setText(hotTopic.getFormattedHotpot());
            binding.getRoot().setOnClickListener(v -> {
                MainApplication.toast(hotTopic.getTopic());
            });
        }

    }
}