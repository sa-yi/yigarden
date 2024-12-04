package com.sayi.vdim.activities;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.method.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;
import androidx.core.content.*;
import androidx.core.text.*;
import androidx.lifecycle.*;

import com.bumptech.glide.*;
import com.bumptech.glide.request.target.*;
import com.bumptech.glide.request.transition.*;
import com.sayi.*;
import com.sayi.vdim.R;
import com.sayi.vdim.activities.fragments.*;
import com.sayi.vdim.databinding.*;
import com.sayi.vdim.dz_entity.*;
import com.sayi.vdim.utils.*;

import java.util.*;

public class PostActivity extends AppCompatActivity {
    static String TAG = "POST-ACTIVITY";
    ActivityPostBinding binding;

    PostViewModel viewModel;
    private int post_id = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色

        DialogLoading.show(this, "加载中", true, dialog -> finish());
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        Uri uri = intent.getData();
        if (uri == null) {
            finish();
            return;
        }


        post_id = Integer.parseInt(uri.getQueryParameter("id"));
        Log.e("uri:", "scheme:" + uri.getScheme()
                + ",path:" + uri.getPath()
                + ",id:" + post_id);

        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(binding.toolbar.getOverflowIcon()).setColorFilter(ContextCompat.getColor(PostActivity.this, R.color.default_gray), PorterDuff.Mode.SRC_ATOP);


        UserBannerFragment userBanner = (UserBannerFragment) getSupportFragmentManager().findFragmentById(R.id.banner);


        viewModel = new ViewModelProvider(this).get(PostViewModel.class);

        viewModel.getPostLiveData().observe(this, postFeedData -> {
            ThreadData.Variables postFeed = postFeedData.getSingleVariable();
            Log.d("post", postFeedData.toString());
            Objects.requireNonNull(getSupportActionBar()).setTitle(postFeed.getSubject());

            String author = postFeed.getAuthor();
            String date = postFeed.getLastpost();

            userBanner.setAuthorName(author);
            userBanner.setSendTime(date);

            ArrayList<ThreadData.Post> posts = postFeedData.getPost();
            ThreadData.Post firstPost = posts.remove(0);

            binding.replyCount.setText(posts.size() + "");


            String content = firstPost.getMessage();
            //HtmlToAndroidLayout processor = new HtmlToAndroidLayout();
            //LinearLayout htmlToAndroidLayout = processor.processHtml(PostActivity.this, content);
            //binding.content.addView(htmlToAndroidLayout);
            Spanned sp = getFormattedHtml(content, binding.content);
            //SpannableString spannableString = new SpannableString(sp);
            //TextView textView=new TextView(this);
            //textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //textView.setText(spannableString);
            //binding.content.addView(textView);
            binding.content.setText(sp);

            /*for(ThreadData.Variables.Post postComment:posts) {
                addCommentView(postComment);
            }*/
            for (int i = 0; i < posts.size(); i++) {
                ThreadData.Post post = posts.get(i);

                addCommentView(post, (i==posts.size()-1));
            }

            if(firstPost.getAttachments()!=null) {
                Log.d("Attachments", firstPost.getAttachments().toString());
                for (int key : firstPost.getAttachments().keySet()) {
                    ThreadAttachment attachment = firstPost.getAttachments().get(key);
                    if (attachment == null) return;
                    if (attachment.getIsimage() == 1) {
                        String image_url = attachment.getUrl() + attachment.getAttachment();
                        Log.d("Attachment image", image_url);
                        attachmentImages.add(image_url);
                        ImageView imageView = new ImageView(this);
                        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(10,10,10,10);
                        imageView.setLayoutParams(layoutParams);
                        Glide.with(imageView).load(image_url).into(imageView);
                        binding.contentContainer.addView(imageView);
                    } else {
                        Log.d("Attachment not image", attachment.toString());
                    }
                }
            }
            DialogLoading.dismiss(PostActivity.this);
            setupUI();
        });
        viewModel.getErrorMessage().observe(this, errorMsg -> {
            DialogLoading.dismiss(PostActivity.this);
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        });
        viewModel.fetchPost(post_id);

    }

    private ArrayList<String> attachmentImages=new ArrayList<>();


    private SpannableString getFormattedHtml(String content, TextView textView) {
        content=content.replace("&gt;",">")
                .replace("&lt;","<");
        Spanned sp = Html.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY, source -> {
            final LevelListDrawable drawable = new LevelListDrawable();

            // 占位图片
            Drawable empty = PostActivity.this.getDrawable(R.drawable.background);
            drawable.addLevel(0, 0, empty);
            drawable.setBounds(0, 0, 1920, 1080);

            // 使用 Glide 加载图片
            Glide.with(PostActivity.this)
                    .load(source)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            // 设置加载后的图片
                            drawable.addLevel(1, 1, resource);
                            if(source.contains("smiley"))
                                drawable.setBounds(0, 0, 100, 100);
                            else {
                                int width=binding.content.getWidth();
                                int intrinsicWidth=drawable.getIntrinsicWidth();
                                int intrinsicHeight=drawable.getIntrinsicHeight();

                                float rate= (float) width /intrinsicWidth;

                                int height= (int) (intrinsicHeight*rate);

                                drawable.setBounds(0, 0, width,height);
                            }
                            drawable.setLevel(1);


                            // 更新 TextView 的内容
                            textView.setText(textView.getText());
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            // 清理资源时的回调
                        }
                    });

            return drawable;
        }, null);
        SpannableString spannableString = new SpannableString(sp);
        setLinksClickable(spannableString, textView);

        return spannableString;
    }
    private void setLinksClickable(SpannableString spannableString, TextView textView) {
        // 使用 URLSpan 处理超链接
        URLSpan[] spans = spannableString.getSpans(0, spannableString.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = spannableString.getSpanStart(span);
            int end = spannableString.getSpanEnd(span);

            // 创建新的 ClickableSpan 并重写 onClick 方法
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Dialog.init(PostActivity.this).setupDialog("是否跳转链接",span.getURL()).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(span.getURL()));
                            startActivity(intent);
                        }
                    }).setPositiveButton("取消",null);
                }

                @Override
                public void updateDrawState(android.text.TextPaint ds) {
                    super.updateDrawState(ds);
                    //ds.setColor(Color.BLUE);  // 设置链接的颜色
                    ds.setUnderlineText(true);  // 设置下划线
                }
            }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 使得链接可点击
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    void setupUI() {
        binding.postComment.setOnClickListener(v -> {

        });
        binding.postComment.setMaxLines(1);
        binding.postComment.setOnEditorActionListener((v, actionId, keyEvent) -> {
            if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && actionId == EditorInfo.IME_ACTION_SEND
                    && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (binding.postComment.getText().toString().isEmpty()) {
                    MainApplication.toast("不可评论空内容");
                    return true;
                }
                MainApplication.toast("发送");
                return true;
            }
            return false;
        });
        binding.thumb.setOnClickListener(v -> {

        });
        binding.like.setOnClickListener(v -> {

        });
        binding.share.setOnClickListener(v -> {
            share();
        });
        binding.shareA.setOnClickListener(v -> {
            share();
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
            Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
            share();
        } else if (item.getItemId() == R.id.report) {
            Toast.makeText(this, "举报", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void share() {
        Intent sharedIntent = new Intent();
        //设置动作为Intent.ACTION_SEND
        sharedIntent.setAction(Intent.ACTION_SEND);
        //设置为文本类型
        sharedIntent.setType("text/*");
        sharedIntent.putExtra(Intent.EXTRA_TEXT, "https://i.lty.fan/forum.php?mod=viewthread&tid=" + post_id);    //设置要分享的内容
        startActivity(Intent.createChooser(sharedIntent, "分享到："));

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


    void addCommentView(ThreadData.Post comment, boolean isLast) {
        addCommentView(comment);
        if (!isLast) {
            View splitter = new View(this);
            splitter.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
            splitter.setBackgroundColor(getResources().getColor(R.color.default_gray,getTheme()));
            binding.commentContainer.addView(splitter);
        }
    }

    void addCommentView(ThreadData.Post comment) {
        PostCommentBinding commentBinding;
        commentBinding = PostCommentBinding.inflate(getLayoutInflater());

        commentBinding.userName.setText(comment.getAuthor());
        commentBinding.userName.setTextColor(ContextCompat.getColor(this, R.color.tianyi_blue));

        String sendTime = comment.getDateline();
        sendTime = sendTime.replace("&nbsp;", " ");
        commentBinding.sendTime.setText(sendTime);

        Log.d("comment", comment.getMessage());
        SpannableString spannableString = getFormattedHtml(comment.getMessage(), commentBinding.content);
        commentBinding.content.setText(spannableString);


        ArrayList<Integer> imagelist=comment.getImagelist();
        if(comment.getAttachments()!=null)
            for(int attachmentId:comment.getAttachments().keySet()){
                if(imagelist.contains(attachmentId)){
                    //TODO 带图评论的图片
                }
            }



        binding.commentContainer.addView(commentBinding.getRoot());

    }
}
