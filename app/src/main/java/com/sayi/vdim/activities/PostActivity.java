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
import androidx.recyclerview.widget.*;

import com.bumptech.glide.*;
import com.bumptech.glide.request.target.*;
import com.bumptech.glide.request.transition.*;
import com.sayi.*;
import com.sayi.vdim.R;
import com.sayi.vdim.activities.fragments.*;
import com.sayi.vdim.customview.*;
import com.sayi.vdim.databinding.*;
import com.sayi.vdim.dz_entity.*;
import com.sayi.vdim.utils.*;

import android.text.Html;

import java.util.*;

public class PostActivity extends AppCompatActivity {
    static String TAG = "POST-ACTIVITY";
    ActivityPostBinding binding;

    PostViewModel viewModel;
    CommentAdapter commentAdapter = new CommentAdapter();
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


        binding.commentList
                .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        UserBannerFragment userBanner = (UserBannerFragment) getSupportFragmentManager().findFragmentById(R.id.banner);


        viewModel = new ViewModelProvider(this).get(PostViewModel.class);

        viewModel.getPostLiveData().observe(this, postFeedData -> {
            ThreadData.Variables postFeed = postFeedData.getVariables().getSingleVariable();
            Log.d("post", postFeedData.toString());
            Objects.requireNonNull(getSupportActionBar()).setTitle(postFeed.getSubject());

            String author = postFeed.getAuthor();
            String date = postFeed.getLastpost();

            userBanner.setAuthorName(author);
            userBanner.setSendTime(date);

            ArrayList<ThreadData.Variables.Post> posts = postFeedData.getVariables().getPost();
            ThreadData.Variables.Post firstPost = posts.remove(0);

            binding.replyCount.setText(posts.size() + "");


            String content = firstPost.getMessage();
            //HtmlToAndroidLayout processor = new HtmlToAndroidLayout();
            //LinearLayout htmlToAndroidLayout = processor.processHtml(PostActivity.this, content);
            //binding.content.addView(htmlToAndroidLayout);
            Spanned sp=Html.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY, source -> {
                final LevelListDrawable drawable = new LevelListDrawable();

                // 占位图片
                Drawable empty = PostActivity.this.getDrawable(R.drawable.background);
                drawable.addLevel(0, 0, empty);
                drawable.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

                // 使用 Glide 加载图片
                Glide.with(PostActivity.this)
                        .load(source)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                // 设置加载后的图片
                                drawable.addLevel(1, 1, resource);
                                drawable.setBounds(0, 0, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                                drawable.setLevel(1);

                                // 更新 TextView 的内容
                                binding.content.setText(binding.content.getText());
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                // 清理资源时的回调
                            }
                        });

                return drawable;
            }, null);
            SpannableString spannableString = new SpannableString(sp);
            //TextView textView=new TextView(this);
            //textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //textView.setText(spannableString);
            //binding.content.addView(textView);
            binding.content.setText(spannableString);

            for (ThreadData.Variables.Post post : posts)
                commentAdapter.addComment(post);

            DialogLoading.dismiss(PostActivity.this);
            setupUI();
        });
        viewModel.getErrorMessage().observe(this, errorMsg -> {
            DialogLoading.dismiss(PostActivity.this);
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        });
        viewModel.fetchPost(post_id);


        binding.commentList.setAdapter(commentAdapter);

        //viewModel.getCommentsLiveData().observe(this, commentAdapter::setComments);
        //viewModel.fetchComments(id);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        binding.commentList.addItemDecoration(dividerItemDecoration);
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

    class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.PostCommentViewHolder> {
        private List<ThreadData.Variables.Post> postComments = new ArrayList<>();

        public void setComments(List<ThreadData.Variables.Post> postComments) {
            Log.d("get comments", postComments.toString());
            this.postComments = postComments;
            notifyDataSetChanged();
        }

        public void addComment(ThreadData.Variables.Post comment) {
            postComments.add(comment);
            notifyItemChanged(-1);
        }

        @NonNull
        @Override
        public PostCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(PostActivity.this);
            PostCommentBinding postCommentBinding = PostCommentBinding.inflate(inflater, parent, false);
            return new PostCommentViewHolder(postCommentBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull PostCommentViewHolder holder, int position) {
            ThreadData.Variables.Post postComment = postComments.get(position);
            Log.d("bindingview holder", postComment.toString());
            holder.bind(postComment);
        }

        @Override
        public int getItemCount() {
            return postComments.size();
        }

        class PostCommentViewHolder extends RecyclerView.ViewHolder {
            private PostCommentBinding binding;

            public PostCommentViewHolder(PostCommentBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(ThreadData.Variables.Post postComment) {
                Log.d("onBindingComment", postComment.toString());
                binding.userName.setText(postComment.getAuthor());
                binding.content.setText(postComment.getMessage());

                int authorid = postComment.getAuthorid();


                String avator_url = "https://i.lty.fan/uc_server/avatar.php?uid=%s&size=small&ts=1";

                avator_url = String.format(avator_url, authorid);
                Log.d("avator", avator_url);
                //Glide.with(PostActivity.this).load(avator_url).into(binding.avator);
            }
        }
    }
}
