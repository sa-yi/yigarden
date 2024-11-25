package com.sayi.yi_garden.activities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.sayi.MainApplication;
import com.sayi.yi_garden.R;
import com.sayi.yi_garden.databinding.ActivityPublishBinding;
import com.sayi.yi_garden.entity.MediaItem;
import com.sayi.yi_garden.entity.MediaUpload;
import com.sayi.yi_garden.toolbarbutton.ImageButton;
import com.sayi.yi_garden.utils.Dialog;

import org.wordpress.aztec.Aztec;
import org.wordpress.aztec.ITextFormat;
import org.wordpress.aztec.glideloader.GlideImageLoader;
import org.wordpress.aztec.toolbar.IAztecToolbarClickListener;

import java.io.File;
import java.util.Map;
import java.util.Objects;

import retrofit2.Response;


public class PublishActivity extends AppCompatActivity implements IAztecToolbarClickListener {
    ActivityPublishBinding binding;
    private ActivityResultLauncher<Intent> imageSelectorLauncher;


    // 在 Activity 中注册 ActivityResult handler
    private ActivityResultLauncher<String[]> requestPermissions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                results -> {
                    // 处理权限请求结果
                    for (Map.Entry<String, Boolean> entry : results.entrySet()) {
                        String permission = entry.getKey();
                        boolean isGranted = entry.getValue();
                        if (isGranted) {
                            Log.d("Permissions", permission + " granted.");
                        } else {
                            Log.d("Permissions", permission + " denied.");
                        }
                    }
                }
        );

        // 触发权限请求逻辑
        requestPermissionsIfNeeded();


        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        binding = ActivityPublishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        //binding.statusbarPlaceholder.setHeight(Statusbar.getStatusBHeight(this));
        //binding.statusbarPlaceholder.setBackgroundColor(ContextCompat.getColor(this, R.color.default_background));
        Objects.requireNonNull(binding.toolbar.getOverflowIcon()).setColorFilter(ContextCompat.getColor(this, R.color.default_gray), PorterDuff.Mode.SRC_ATOP);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_keyboard_arrow_left_24);
        getSupportActionBar().setTitle("编辑文章");


        imageSelectorLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            /*Uri uri = data.getData();

                            DialogLoading.show(PublishActivity.this, "图片上传中...");
                            handleUploadFile(uri);*/
                            ClipData clipData = data.getClipData();
                            if (clipData != null && clipData.getItemCount() > 0) {
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    ClipData.Item item = clipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    if (uri != null) {
                                        handleUploadFile(uri);
                                    }
                                }
                            } else {
                                // 处理单选的情况
                                Uri uri = data.getData();
                                if (uri != null) {
                                    handleUploadFile(uri);
                                } else {
                                    MainApplication.toast("未选择图片");
                                }
                            }
                        } else {
                            MainApplication.toast("未选择图片");
                        }
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        MainApplication.toast("选择取消");

                    } else {
                        MainApplication.toast("操作失败");
                    }
                }
        );
        Aztec.with(binding.visual, binding.formattingToolbar, this).setImageGetter(new GlideImageLoader(this));
        ImageButton imageButton = new ImageButton(binding.formattingToolbar);
        imageButton.setMediaToolbarButtonClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            imageSelectorLauncher.launch(intent);

        });
        binding.formattingToolbar.addButton(imageButton);


        binding.visual.fromHtml(source, false);

    }

    String source="<img src=\"https://cdn.bbs.66ccff.cc/2024/04/20240429180741291-0041.jpg\"/>";

    private void handleUploadFile(Uri uri) {
        String path = getPathFromUri(PublishActivity.this, uri);
        try {
            MediaUpload.upload(new File(path), new MediaUpload.MediaUploadCallback() {
                @Override
                public void onSuccess(Response<MediaItem> response) {
                    Log.d("responseBody", response.body().toString());
                    MediaItem mediaItem = response.body();
                    String url = mediaItem.getGuid().getRendered();
                    MainApplication.toast(url);

                    source+="<img src=\""+url+"\"/>";
                    binding.visual.fromHtml(source, false);
                }

                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onParsedError() {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // Android 14 及以上
            requestPermissions.launch(new String[]{
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_VIDEO,
                    android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            });
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            // Android 13
            requestPermissions.launch(new String[]{
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_VIDEO
            });
        } else {
            // Android 12 及以下
            requestPermissions.launch(new String[]{
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.publish_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.preview) {
            Toast.makeText(this, "preview", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.publish) {
            Toast.makeText(this, "published", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.save) {
            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.exit || item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("PublishActivity", newConfig.toString());
    }

    @Override
    public void finish() {
        Dialog.init(this).setupDialog("是否保存并退出", "")
                .setPositiveButton("确认", (dialog, which) -> {
                    MainApplication.toast(" 已保存");
                    super.finish();
                }).setNegativeButton("取消", (dialog, which) -> {

                }).setNetrualButton("直接退出", (dialog, which) -> {
                    super.finish();
                }).show();
    }

    @Override
    public void onToolbarCollapseButtonClicked() {

    }

    @Override
    public void onToolbarExpandButtonClicked() {

    }

    @Override
    public void onToolbarFormatButtonClicked(@NonNull ITextFormat iTextFormat, boolean b) {

    }

    @Override
    public void onToolbarHeadingButtonClicked() {

    }

    @Override
    public void onToolbarHtmlButtonClicked() {

    }

    @Override
    public void onToolbarListButtonClicked() {

    }

    @Override
    public boolean onToolbarMediaButtonClicked() {
        return false;
    }


    private String getPathFromUri(Context context, Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            path = cursor.getString(columnIndex);
            cursor.close();
        }
        return path;
    }

}
