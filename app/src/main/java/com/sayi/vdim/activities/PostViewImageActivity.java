package com.sayi.vdim.activities;

import static com.sayi.MainApplication.getContext;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.*;
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
import androidx.core.content.*;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.*;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sayi.MainApplication;
import com.sayi.vdim.R;
import com.sayi.vdim.databinding.ActivityViewPostImageBinding;
import com.sayi.vdim.databinding.ModalBottomSheetContentBinding;

import java.io.*;
import java.util.*;
import android.webkit.MimeTypeMap;

public class PostViewImageActivity extends AppCompatActivity {
    ActivityViewPostImageBinding binding;
    String TAG = "PostViewImageActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewPostImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        ArrayList<String> imageUrls=new ArrayList<>();

        int imageIndex = intent.getIntExtra("index",-1);

        int index = 0;
        if(imageIndex==-1){
            imageUrls.add(intent.getStringExtra("url"));
        }else {
            Log.d(TAG, imageIndex + "");
            for (String key : intent.getExtras().keySet()) {
                Log.d(TAG, key + ":" + intent.getExtras().get(key));
                if (!Objects.equals(key, "index")) {
                    imageUrls.add(String.valueOf(intent.getExtras().get(key)));
                }
            }
            for (String key : intent.getExtras().keySet()) {
                if (!Objects.equals(key, "index")) {
                    if (Integer.valueOf(key) == imageIndex) {
                        break;
                    } else
                        index++;
                }
            }
            Log.d(TAG, index + "");
        }
        GalleryAdapter adapter = new GalleryAdapter(this, imageUrls);
        binding.imageGallery.setAdapter(adapter);
        binding.imageGallery.setCurrentItem(index,false);

    }

    // 保存图片到相册的方法
    private static void saveImage(String imageUrl) {
        // 使用Glide加载图片并保存到设备的存储空间，使用MediaStore API进行保存
        Glide.with(getContext())
                .asBitmap()
                .load(imageUrl)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            // 获取图片的输出流
                            ContentValues values = new ContentValues();
                            String fileName = "image_" + System.currentTimeMillis();
                            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(imageUrl);
                            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
                            values.put(MediaStore.Images.Media.TITLE, "Saved Image");
                            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName + "." + fileExtension);
                            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
                            Uri uri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                            OutputStream outputStream = getContext().getContentResolver().openOutputStream(uri);
                            if (outputStream != null) {
                                // 根据文件类型选择压缩格式
                                Bitmap.CompressFormat compressFormat = getCompressFormat(fileExtension);
                                resource.compress(compressFormat, 100, outputStream);
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

    private static Bitmap.CompressFormat getCompressFormat(String fileExtension) {
        switch (fileExtension.toLowerCase()) {
            case "png":
                return Bitmap.CompressFormat.PNG;
            case "webp":
                return Bitmap.CompressFormat.WEBP;
            case "jpeg":
            case "jpg":
            default:
                return Bitmap.CompressFormat.JPEG;
        }
    }

    private static void shareImage(String imageUrl){
        Glide.with(getContext())
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // 将Bitmap保存到文件中
                        File imageFile = new File(getContext().getCacheDir(), "shared_image.png");
                        try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
                            resource.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
                            // 获取图片文件的Uri
                            Uri imageUri = FileProvider.getUriForFile(
                                    getContext(),
                                    "com.sayi.vdim.fileprovider", // 使用你在AndroidManifest.xml中定义的authorities
                                    imageFile);

                            // 创建分享 Intent
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/png"); // 使用 PNG 格式
                            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            // 启动分享 Intent
                            Intent chooser = Intent.createChooser(shareIntent, "分享图片");
                            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(chooser);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // 可以在这里处理加载清除后的逻辑
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
                dismiss();
            });
            sheetBinding.share.setOnClickListener(v->{
                shareImage(imageUrl);
                dismiss();
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
