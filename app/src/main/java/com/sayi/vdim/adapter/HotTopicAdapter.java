package com.sayi.vdim.adapter;

import android.annotation.*;
import android.app.*;
import android.graphics.*;
import android.view.*;

import androidx.annotation.*;
import androidx.recyclerview.widget.*;
import androidx.vectordrawable.graphics.drawable.*;

import com.sayi.*;
import com.sayi.vdim.*;
import com.sayi.vdim.databinding.*;
import com.sayi.vdim.dz_entity.*;

import java.util.*;

public class HotTopicAdapter extends RecyclerView.Adapter<HotTopicAdapter.HotTopicViewHolder> {
    private final List<HotTopic> hotTopicList = new ArrayList<>();
    private Activity activity;

    public HotTopicAdapter(Activity activity) {
        this.activity = activity;
    }

    public void addValue(ArrayList<HotTopic> hotTopics) {
        hotTopicList.addAll(hotTopics);
        notifyItemChanged(0,hotTopics.size());
    }

    @NonNull
    @Override
    public HotTopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemHotTopicBinding binding = ItemHotTopicBinding.inflate(inflater, parent, false);
        return new HotTopicViewHolder(activity, binding);
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

        public HotTopicViewHolder(Activity activity, ItemHotTopicBinding binding) {
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