package com.sayi.yi_garden.activities;

import static com.sayi.MainApplication.getContext;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sayi.MainApplication;
import com.sayi.yi_garden.R;
import com.sayi.yi_garden.databinding.ActivityViewPostImageBinding;
import com.sayi.yi_garden.databinding.ModalBottomSheetContentBinding;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class PostViewImageActivity extends AppCompatActivity {
    ActivityViewPostImageBinding binding;
    String TAG = "PostViewImageActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewPostImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        List<String> imageUrls = intent.getStringArrayListExtra("imagelist");
        Log.d(TAG, imageUrls.toString());
        int imageIndex = intent.getIntExtra("index", 0);
        GalleryAdapter adapter = new GalleryAdapter(this, imageUrls);
        binding.imageGallery.setAdapter(adapter);


    }

    // 保存图片到相册的方法
    private static void saveImage(String imageUrl) {
        // 这里可以使用 Glide 来加载图片并保存到设备的存储空间，使用 MediaStore API 进行保存
        Glide.with(getContext())
                .asBitmap()
                .load(imageUrl)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            // 获取图片的输出流
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "Saved Image");
                            values.put(MediaStore.Images.Media.DISPLAY_NAME, "image_" + System.currentTimeMillis());
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            Uri uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                            OutputStream outputStream = getContext().getContentResolver().openOutputStream(uri);
                            if (outputStream != null) {
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                outputStream.flush();
                                outputStream.close();
                                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static class ModalBottomSheet extends BottomSheetDialogFragment {
        public static final String TAG = "ModalBottomSheet";
        private String imageUrl = "";

        public ModalBottomSheet(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ModalBottomSheetContentBinding sheetBinding = ModalBottomSheetContentBinding.inflate(inflater);
            sheetBinding.save.setOnClickListener(v -> {
                saveImage(imageUrl);
            });
            sheetBinding.share.setOnClickListener(v->{
                MainApplication.toast("share");
            });
            return sheetBinding.getRoot();
        }
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
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return imageUrls.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.image);
                imageView.setOnLongClickListener(v -> {
                    // 创建并展示 ModalBottomSheet
                    String imageUrl = imageUrls.get(getBindingAdapterPosition());
                    ModalBottomSheet modalBottomSheet = new ModalBottomSheet(imageUrl);
                    modalBottomSheet.show(PostViewImageActivity.this.getSupportFragmentManager(), ModalBottomSheet.TAG);


                    return true; // 返回 true 表示事件已被消费，防止触发点击事件
                });
            }
        }
    }
}
