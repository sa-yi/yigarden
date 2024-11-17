package com.sayi.yi_garden.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sayi.yi_garden.R;
import com.sayi.yi_garden.activities.fragments.UserBannerFragment;
import com.sayi.yi_garden.customview.HtmlToAndroidLayout;
import com.sayi.yi_garden.databinding.ActivityPostBinding;
import com.sayi.yi_garden.databinding.PostCommentBinding;
import com.sayi.yi_garden.entity.PostComment;
import com.sayi.yi_garden.utils.DialogLoading;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostActivity extends AppCompatActivity {
    static String TAG = "POST-ACTIVITY";
    ActivityPostBinding binding;

    PostViewModel viewModel;

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


        int id = Integer.parseInt(uri.getQueryParameter("id"));
        Log.e("uri:", "scheme:" + uri.getScheme()
                + ",path:" + uri.getPath()
                + ",id:" + id);

        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(binding.toolbar.getOverflowIcon()).setColorFilter(ContextCompat.getColor(PostActivity.this, R.color.default_gray), PorterDuff.Mode.SRC_ATOP);


        binding.commentList
                .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        UserBannerFragment userBanner = (UserBannerFragment) getSupportFragmentManager().findFragmentById(R.id.banner);


        viewModel=new ViewModelProvider(this).get(PostViewModel.class);

        viewModel.getPostLiveData().observe(this, postFeed -> {
            Log.d("post", postFeed.toString());
            Objects.requireNonNull(getSupportActionBar()).setTitle(postFeed.getTitle().getRendered());

            int author = postFeed.getAuthor();
            String date= postFeed.getDate();

            userBanner.fetchUserData(author);
            userBanner.setSendTime(date);

            String content = postFeed.getContent().getRendered();
            LinearLayout htmlToAndroidLayout = HtmlToAndroidLayout.processHtml(PostActivity.this, content);
            binding.content.addView(htmlToAndroidLayout);


            DialogLoading.dismiss(PostActivity.this);
            setupUI();
        });

        viewModel.fetchPost(id);

        CommentAdapter commentAdapter=new CommentAdapter();
        binding.commentList.setAdapter(commentAdapter);

        viewModel.getCommentsLiveData().observe(this, commentAdapter::setComments);
        viewModel.fetchComments(id);
    }


    void setupUI() {
        binding.postComment.setOnClickListener(v -> {

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

    class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.PostCommentViewHolder>{
        private List<PostComment> postComments =new ArrayList<>();
        public void setComments(List<PostComment> postComments){
            Log.d("get comments", postComments.toString());
            this.postComments = postComments;
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public PostCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(PostActivity.this);
            PostCommentBinding postCommentBinding=PostCommentBinding.inflate(inflater,parent,false);
            return new PostCommentViewHolder(postCommentBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull PostCommentViewHolder holder, int position) {
            PostComment postComment = postComments.get(position);
            Log.d("bindingview holder",postComment.toString());
            holder.bind(postComment);
        }

        @Override
        public int getItemCount() {
            return postComments.size();
        }

        class PostCommentViewHolder extends RecyclerView.ViewHolder{
            private PostCommentBinding binding;
            public PostCommentViewHolder(PostCommentBinding binding) {
                super(binding.getRoot());
                this.binding=binding;
            }
            public void bind(PostComment postComment){
                Log.d("onBindingComment", postComment.toString());
                binding.userName.setText(postComment.getAuthor_name());
                binding.content.setText(postComment.getContent().getRendered());
            }
        }
    }
}
