package com.sayi.yi_garden.activities;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.sayi.yi_garden.R;
import com.sayi.yi_garden.activities.fragments.UserBannerFragment;
import com.sayi.yi_garden.customview.HtmlToAndroidLayout;
import com.sayi.yi_garden.entity.ApiClient;
import com.sayi.yi_garden.entity.PostFeed;
import com.sayi.yi_garden.entity.ApiService;
import com.sayi.yi_garden.databinding.ActivityPostBinding;
import com.sayi.yi_garden.utils.DialogLoading;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {
    static String TAG = "POST-ACTIVITY";
    ActivityPostBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色

        DialogLoading.show(this, "加载中", true, dialog -> finish());
        Intent intent = getIntent();
        if (null != intent) {
            Uri uri = intent.getData();
            if (null != uri) {
                int id = Integer.parseInt(uri.getQueryParameter("id"));
                Log.e("uri:", "scheme:" + uri.getScheme()
                        + ",path:" + uri.getPath()
                        + ",id:" + id);

                ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
                Call<PostFeed> call = apiService.getPost(id);

                call.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<PostFeed> call, @NonNull Response<PostFeed> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("post", response.body().toString());
                            binding = ActivityPostBinding.inflate(getLayoutInflater());
                            setContentView(binding.getRoot());
                            setSupportActionBar(binding.toolbar);
                            Objects.requireNonNull(binding.toolbar.getOverflowIcon()).setColorFilter(ContextCompat.getColor(PostActivity.this, R.color.default_gray), PorterDuff.Mode.SRC_ATOP);

                            Objects.requireNonNull(getSupportActionBar()).setTitle(response.body().getTitle().getRendered() + "");

                            UserBannerFragment userBanner= (UserBannerFragment) getSupportFragmentManager().findFragmentById(R.id.banner);


                            int author = response.body().getAuthor();
                            //binding.banner.author.setText(author + "");
                            String date = response.body().getDate();
                            //binding.banner.sendTime.setText(date);

                            userBanner.setUserName(author+"");
                            userBanner.setSendTime(date);

                            String content = response.body().getContent().getRendered();


                            Document document= Jsoup.parse(content);

                            // 创建一个NodeVisitor来访问每个节点
                            NodeVisitor nodeVisitor = new NodeVisitor() {
                                public void head(Node node, int depth) {
                                    // 在进入节点时调用
                                    if(depth>=3)
                                        Log.d(TAG,node.nodeName()+":"+node+",depth"+depth);
                                }

                                public void tail(Node node, int depth) {
                                    // 在离开节点时调用
                                    // 这里我们不执行任何操作
                                }
                            };

                            // 使用NodeTraversor来遍历文档中的所有节点
                            //NodeTraversor traversor = new NodeTraversor();

                            //NodeTraversor.traverse(nodeVisitor, document);




                            //Spanned spannedText = Html.fromHtml(content, FROM_HTML_MODE_COMPACT, new ImageGetter(), new CustomTagHandler());

                            //LinearLayout htmlToAndroidLayout=HtmlToAndroidLayout.parseHtmlToLayout(PostActivity.this,content);
                            LinearLayout htmlToAndroidLayout=HtmlToAndroidLayout.processHtml(PostActivity.this,content);
                            binding.content.addView(htmlToAndroidLayout);

                            /*
                            ViewGroup.LayoutParams layoutParams1 = binding.content.getLayoutParams();
                            layoutParams1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            binding.content.setLayoutParams(layoutParams1);
                            binding.content.setMovementMethod(LinkMovementMethod.getInstance());
                            */
                            setupUI();
                        } else {
                            Log.e("post", "call failed");
                        }
                        DialogLoading.dismiss(PostActivity.this);
                    }

                    @Override
                    public void onFailure(@NonNull Call<PostFeed> call, @NonNull Throwable t) {
                        Log.e("post failed", "call failed");
                        DialogLoading.dismiss(PostActivity.this);
                    }
                });

            }
        }


    }


    void setupUI(){
        binding.comment.setOnClickListener(v -> {

        });
        binding.thumb.setOnClickListener(v -> {

        });
        binding.like.setOnClickListener(v -> {

        });
        binding.share.setOnClickListener(v -> {

        });


        setSpannableText(binding.classification);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.share) {
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.report) {
            Toast.makeText(this, "举报", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void setSpannableText(TextView textView) {
        String text = "锦依卫报到处/同人二创/正文";
        String[] parts = text.split("/");

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        for (int i = 0; i < parts.length; i++) {
            final String part = parts[i];
            SpannableString spannableString = new SpannableString(part);

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Toast.makeText(PostActivity.this, part, Toast.LENGTH_SHORT).show();
                }
            };

            spannableString.setSpan(clickableSpan, 0, part.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.append(spannableString);

            if (i < parts.length - 1) {
                spannableStringBuilder.append(" / ");
            }
        }

        textView.setText(spannableStringBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);
    }

    private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            LevelListDrawable d = new LevelListDrawable();
            Drawable empty = ContextCompat.getDrawable(PostActivity.this, R.drawable.background);
            d.addLevel(0, 0, empty);
            d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

            new LoadImage().execute(source, d);

            return d;
        }

        class LoadImage extends AsyncTask<Object, Void, Bitmap> {

            private LevelListDrawable mDrawable;

            @Override
            protected Bitmap doInBackground(Object... params) {
                String source = (String) params[0];
                mDrawable = (LevelListDrawable) params[1];
                Log.d(TAG, "doInBackground " + source);
                try {
                    InputStream is = new URL(source).openStream();
                    return BitmapFactory.decodeStream(is);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                Log.d(TAG, "onPostExecute drawable " + mDrawable);
                Log.d(TAG, "onPostExecute bitmap " + bitmap);
                if (bitmap != null) {
                    BitmapDrawable d = new BitmapDrawable(getResources(), bitmap);
                    mDrawable.addLevel(1, 1, d);
                    mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                    mDrawable.setLevel(1);
                    // i don't know yet a better way to refresh TextView
                    // mTv.invalidate() doesn't work as expected
                    //CharSequence t = binding.content.getText();
                    //binding.content.setText(t);
                }
            }
        }
    }

    public class CustomTagHandler implements Html.TagHandler {
        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            if (tag.equalsIgnoreCase("img")) {
                int len = output.length();
                ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
                String src = output.subSequence(output.getSpanStart(images[0]), output.getSpanEnd(images[0])).toString();
                String imageSource = images[0].getSource();
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Toast.makeText(PostActivity.this, "Image clicked: " + src + "," + imageSource, Toast.LENGTH_SHORT).show();

                    }
                };

                output.setSpan(clickableSpan, len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
}
