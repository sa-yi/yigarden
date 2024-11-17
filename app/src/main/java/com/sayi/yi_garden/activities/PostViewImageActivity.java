package com.sayi.yi_garden.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import com.bumptech.glide.Glide;
import com.sayi.yi_garden.R;
import com.sayi.yi_garden.databinding.ActivityViewPostImageBinding;

public class PostViewImageActivity extends AppCompatActivity {
    ActivityViewPostImageBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityViewPostImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        List<String> imageUrls=intent.getStringArrayListExtra("imageUrls");
        int imageIndex=intent.getIntExtra("imageIndex",0);
        GalleryAdapter adapter=new GalleryAdapter(this,imageUrls);
        binding.imageGallery.setAdapter(adapter);


    }

    public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
        private final List<String> imageUrls;
        private final Context context;

        public GalleryAdapter(Context context, List<String> imageUrls) {
            this.context = context;
            this.imageUrls = imageUrls;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Glide.with(context)
                    .load(imageUrls.get(position)) // 加载 URL
                    .placeholder(android.R.drawable.ic_menu_gallery) // 占位图
                    .error(android.R.drawable.ic_dialog_alert) // 错误图
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return imageUrls.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.image);
            }
        }
    }

}
