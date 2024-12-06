package com.sayi.vdim.adapter;

import android.app.*;
import android.content.*;
import android.net.*;
import android.view.*;

import androidx.annotation.*;
import androidx.recyclerview.widget.*;

import com.sayi.vdim.activities.*;
import com.sayi.vdim.databinding.*;
import com.sayi.vdim.dz_entity.*;

import java.util.*;

public class ThreadDataAdapter {
    public static class DzDataAdapter extends RecyclerView.Adapter<DzDataViewHolder> {
        private List<ThreadData> dzThreadDataList = new ArrayList<>();

        public void addData(ThreadData threadData) {
            dzThreadDataList.add(threadData);
        }

        Activity activity;
        public DzDataAdapter(Activity activity){
            this.activity=activity;
        }

        @NonNull
        @Override
        public DzDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            NickPostBinding nickPostBinding = NickPostBinding.inflate(inflater, parent, false);
            return new DzDataViewHolder(activity,nickPostBinding);
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
            this.activity=activity;
            this.binding = binding;
        }

        public void bind(ThreadData threadData) {

            binding.userName.setText(threadData.getAuthor());
            String sendTime = threadData.getLastpost();
            sendTime = sendTime.replace("&nbsp;", " ");
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
        }
    }
}
