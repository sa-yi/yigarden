package com.sayi.vdim.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sayi.vdim.activities.PostActivity;
import com.sayi.vdim.databinding.NickPostBinding;
import com.sayi.vdim.dz_entity.ThreadData;
import com.sayi.vdim.utils.DateFormatter;

import java.util.ArrayList;
import java.util.List;

public class ThreadDataAdapter {
    public static class DzDataAdapter extends RecyclerView.Adapter<DzDataViewHolder> {
        private final List<ThreadData> dzThreadDataList = new ArrayList<>();
        Activity activity;

        public DzDataAdapter(Activity activity) {
            this.activity = activity;
        }

        public void addData(ThreadData threadData) {
            dzThreadDataList.add(threadData);
        }

        @NonNull
        @Override
        public DzDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            NickPostBinding nickPostBinding = NickPostBinding.inflate(inflater, parent, false);
            return new DzDataViewHolder(activity, nickPostBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull DzDataViewHolder holder, int position) {
            ThreadData threadData = dzThreadDataList.get(position);
            holder.bind(threadData);
        }

        @Override
        public int getItemCount() {
            return dzThreadDataList.size();
        }
    }

    static class DzDataViewHolder extends RecyclerView.ViewHolder {
        Activity activity;
        NickPostBinding binding;

        public DzDataViewHolder(Activity activity, NickPostBinding binding) {
            super(binding.getRoot());
            this.activity = activity;
            this.binding = binding;
        }

        public void bind(ThreadData threadData) {
            int authorId = threadData.getAuthorId();
            binding.userName.setText(threadData.getAuthor());
            String sendTime = threadData.getLastpost();
            sendTime = DateFormatter.format(sendTime);
            binding.sendTime.setText(sendTime);
            binding.title.setText(threadData.getSubject());
            binding.expert.setText(threadData.getMessage());
            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(activity, PostActivity.class);
                //intent.setData();
                Uri uri = new Uri.Builder().scheme("vdim")
                        .authority("")  // authority 这里可以为空
                        .path("/viewthread")
                        .appendQueryParameter("tid", threadData.getTid())
                        .build();
                intent.setData(uri);
                activity.startActivity(intent);
            });
            //Glide.with(activity).asBitmap().load("https://i.lty.fan/uc_server/avatar.php?size=big&uid="+ authorId).error(R.drawable.default_avator).into(binding.avator);
        }
    }
}
