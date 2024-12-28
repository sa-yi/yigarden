package com.sayi.vdim.activities;

import android.annotation.*;
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
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.*;
import androidx.appcompat.content.res.*;
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

import org.xml.sax.*;

import java.net.*;
import java.nio.charset.*;
import java.util.*;

import retrofit2.*;

public class PostActivity extends AppCompatActivity {
    static String TAG = "POST-ACTIVITY";
    ActivityPostBinding binding;

    PostViewModel viewModel;
    UserBannerFragment userBanner;
    private int post_id = -1;
    private ArrayList<String> attachmentImages = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//实现状态栏图标和文字颜色为暗色
        //TODO 切换夜间模式时会触发重复添加fragment的错误
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


        post_id = Integer.parseInt(Objects.requireNonNull(uri.getQueryParameter("tid")));
        Log.e("uri:", "scheme:" + uri.getScheme()
                + ",path:" + uri.getPath()
                + ",id:" + post_id);

        setupUI();

        viewModel = new ViewModelProvider(this).get(PostViewModel.class);

        viewModel.getThreadData().observe(this,threadData -> {

            Log.d("post", threadData.toString());
            if(getSupportActionBar()!=null) {
                Objects.requireNonNull(getSupportActionBar()).setTitle(threadData.getSubject());
            }

            String author = threadData.getAuthor();
            String date = threadData.getLastpost();

            userBanner.setAuthorName(author);
            userBanner.setSendTime(date);

            int authorId = threadData.getAuthorId();
        });
        viewModel.getPostLiveData().observe(this, postFeedData -> {


            //Glide.with(PostActivity.this).asBitmap().load("https://i.lty.fan/uc_server/avatar.php?size=big&uid=" + authorId).error(R.drawable.default_avator).into(userBanner.getAvatorView());


            ArrayList<Post> posts = postFeedData;
            Post firstPost = posts.remove(0);

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


            for (int i = 0; i < posts.size(); i++) {
                Post post = posts.get(i);

                addCommentView(post, (i == posts.size() - 1));
            }

            if (firstPost.getAttachments() != null) {
                int key=-1;
                for (ThreadAttachment attachment : firstPost.getAttachments()) {
                    if (attachment == null) return;
                    if (attachment.getAttachimg()) {//attachimg表示以附件形式传递且为图片
                        key++;
                        String image_url = attachment.getUrl();
                        image_url="https://oss.lty.fan/forum/"+image_url;
                        Log.d("Attachment image", image_url);
                        attachmentImages.add(image_url);
                        ImageView imageView = new ImageView(this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(10, 10, 10, 10);
                        imageView.setLayoutParams(layoutParams);
                        int finalKey = key;
                        imageView.setOnClickListener(v -> {
                            Intent galleryIntent = new Intent(PostActivity.this, PostViewImageActivity.class);
                            galleryIntent.putExtra("images",attachmentImages);
                            galleryIntent.putExtra("index", finalKey);
                            startActivity(galleryIntent);
                        });
                        Glide.with(imageView).load(image_url).into(imageView);
                        binding.contentContainer.addView(imageView);
                        //} else {
                        Log.d("Attachment not image", attachment.toString());
                        //}
                    }
                }


            }
            DialogLoading.dismiss(PostActivity.this);
        });
        viewModel.getErrorMessage().observe(this, errorMsg -> {
            DialogLoading.dismiss(PostActivity.this);
            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
        });
        viewModel.fetchThread(post_id);
        viewModel.fetchPost(post_id);

    }

    private static String parseDzFormat2Html(String dzContent){
        String html="";



        return html;
    }

    @NonNull
    private SpannableString getFormattedHtml(String content, TextView textView) {
        content = content.replace("&amp;", "&");
        content = content.replace("&gt;", ">")
                .replace("&lt;", "<");
        Spanned sp = Html.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY, source -> {
            final LevelListDrawable drawable = new LevelListDrawable();

            // 占位图片
            Drawable empty = AppCompatResources.getDrawable(PostActivity.this, R.drawable.baseline_image_24);
            drawable.addLevel(0, 0, empty);
            drawable.setBounds(0, 0, 71, 71);

            // 使用 Glide 加载图片
            Glide.with(PostActivity.this)
                    .load(source)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            // 设置加载后的图片
                            drawable.addLevel(1, 1, resource);
                            if (source.contains("smiley"))
                                drawable.setBounds(0, 0, 100, 100);
                            else {
                                int width = binding.content.getMeasuredWidth();
                                int intrinsicWidth = drawable.getIntrinsicWidth();
                                int intrinsicHeight = drawable.getIntrinsicHeight();

                                float rate = (float) width / intrinsicWidth;
                                int height = (int) (intrinsicHeight * rate);

                                drawable.setBounds(0, 0, width, height);
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
        }, new CustomTagHandler());
        SpannableString spannableString = new SpannableString(sp);
        setLinksClickable(spannableString, textView);

        return spannableString;
    }

    private void setLinksClickable(@NonNull SpannableString spannableString, TextView textView) {
        // 使用 URLSpan 处理超链接
        URLSpan[] spans = spannableString.getSpans(0, spannableString.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = spannableString.getSpanStart(span);
            int end = spannableString.getSpanEnd(span);

            spannableString.removeSpan(span);
            // 创建新的 ClickableSpan 并重写 onClick 方法
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    String url=span.getURL();
                    if(url.startsWith("http")) {
                        Uri uri = Uri.parse(url);

                        String authority=uri.getAuthority();
                        String path=uri.getPath();
                        String query=uri.getQuery();


                        Log.d("urldata",authority+":"+path+":"+query);
                        if(!Objects.equals(authority, "i.lty.fan")){//站外链接
                            Dialog.init(PostActivity.this)
                                    .setupDialog("是否跳转链接", span.getURL())
                                    .setPositiveButton("确认", (dialogInterface, i) -> {
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(span.getURL()));
                                        startActivity(intent);
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                            return;
                        }
                        HashMap<String,String> queryMap=getQueryParams(url);
                        if(Objects.equals(path, "/forum.php")){
                            String mod=queryMap.get("mod");
                            if(mod!=null){
                                if(mod.equals("viewthread")){
                                    String tid=queryMap.get("tid");
                                    Intent intent = new Intent(PostActivity.this, PostActivity.class);
                                    //intent.setData();
                                    Uri jumpUri = new Uri.Builder().scheme("vdim")
                                            .authority("")  // authority 这里可以为空
                                            .path("/viewthread")
                                            .appendQueryParameter("tid", tid)
                                            .build();
                                    intent.setData(jumpUri);
                                    startActivity(intent);
                                }
                            }
                        }else {
                            // 移除字符串开头的"/"
                            assert path != null;
                            String withoutLeadingSlash = path.substring(1);

                            // 使用"-"分割字符串
                            String[] partsByDash = withoutLeadingSlash.split("-");

                            // 检查是否至少有两个部分
                            if (partsByDash.length >= 2) {
                                String forumString = partsByDash[0]; // forum部分

                                // 使用"."分割第二部分，以提取数字
                                String[] partsByDot = partsByDash[1].split("\\.");

                                String index,page;
                                // 检查是否至少有两个部分
                                if (partsByDot.length >= 2) {
                                    index = partsByDot[0]; //板块id部分

                                    page = partsByDot[1]; // 页码部分 TODO 根据页码加载
                                } else {
                                    index = partsByDot[0];
                                }
                                if(Objects.equals(forumString, "forum")){
                                    Intent intent = new Intent(PostActivity.this, ForumActivity.class);
                                    Uri jumpUri = new Uri.Builder().scheme("vdim")
                                            .authority("")  // authority 这里可以为空
                                            .path("/forum")
                                            .appendQueryParameter("fid", index)
                                            .build();
                                    intent.setData(jumpUri);
                                    startActivity(intent);
                                }
                            } else {
                                Log.d(TAG,"error finding forumid");
                            }
                        }

                    }else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(span.getURL()));
                        startActivity(intent);
                    }
                }

                @Override
                public void updateDrawState(@NonNull android.text.TextPaint ds) {
                    super.updateDrawState(ds);
                    //ds.setColor(Color.BLUE);  // 设置链接的颜色
                    ds.setUnderlineText(true);  // 设置下划线
                }
            }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // 使得链接可点击
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(textView.getText());
    }

    @NonNull
    public static HashMap<String, String> getQueryParams(String urlString) {
        HashMap<String, String> params = new HashMap<>();

        // 使用 Uri 类来解析 URL 参数
        Uri uri = Uri.parse(urlString);

        Log.d("uri",uri.toString());



        // 获取 URL 查询参数部分
        String query = uri.getQuery();

        if (query != null && !query.isEmpty()) {
            // 遍历每一个参数键值对
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = Uri.decode(keyValue[0]);
                    String value = Uri.decode(keyValue[1]);
                    params.put(key, value);
                }
            }
        }

        return params;
    }

    void setupUI() {
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(binding.toolbar.getOverflowIcon()).setColorFilter(ContextCompat.getColor(PostActivity.this, R.color.default_gray), PorterDuff.Mode.SRC_ATOP);


        userBanner = (UserBannerFragment) getSupportFragmentManager().findFragmentById(R.id.banner);

        binding.postComment.setMaxLines(1);
        binding.postComment.setOnEditorActionListener((v, actionId, keyEvent) -> {
            if (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                String content = v.getText().toString().trim();
                if (content.isEmpty()) {
                    MainApplication.toast("不可评论空内容");
                    return true;
                }
                MainApplication.toast("发送");
                return true;
            }
            return false;
        });
        binding.like.setOnClickListener(v -> {

        });
        binding.share.setOnClickListener(v -> {
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

    void addCommentView(Post comment, boolean isLast) {
        addCommentView(comment);
        if (!isLast) {//不是最后一条评论下面会加一条分割线
            View splitter = new View(this);
            splitter.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
            splitter.setBackgroundColor(getResources().getColor(R.color.default_gray, getTheme()));
            binding.commentContainer.addView(splitter);
        }
    }

    void addCommentView(Post comment) {
        PostCommentBinding commentBinding;
        commentBinding = PostCommentBinding.inflate(getLayoutInflater());

        int authorId = comment.getAuthorid();
        //Glide.with(this).asBitmap().load("https://i.lty.fan/uc_server/avatar.php?size=big&uid=" + authorId).into(commentBinding.avator);

        commentBinding.userName.setText(comment.getAuthor());
        commentBinding.userName.setTextColor(ContextCompat.getColor(this, R.color.tianyi_blue));

        String sendTime = comment.getDateline();
        sendTime = sendTime.replace("&nbsp;", " ");
        commentBinding.sendTime.setText(sendTime);


        SpannableString spannableString = getFormattedHtml(comment.getMessage(), commentBinding.content);
        commentBinding.content.setText(spannableString);


        ArrayList<Integer> imageList = comment.getImagelist();
        if (comment.getAttachments() != null)
            for (ThreadAttachment attachment : comment.getAttachments()) {
                    //TODO 带图评论的图片
/*                    ImageView imageView=new ImageView(this);
                    imageView.setMaxWidth(20);
                    imageView.setMaxHeight(10);
                    String attachmentName=attachment.getAttachment();
                    String url=attachment.getUrl();

                    String finalUrl=url+attachmentName;

                    Log.d("attachment",finalUrl);
                    Glide.with(this).load(finalUrl).into(imageView);
                    commentBinding.contentContainer.addView(imageView);
*/
            }


        binding.commentContainer.addView(commentBinding.getRoot());

    }

    public static class PostViewModel extends ViewModel {
        private static final String TAG = "PostViewModel";
        private final DzService dzService = DzClient.getRetrofitInstance().create(DzService.class);

        private final MutableLiveData<ThreadData> threadData=new MutableLiveData<>();
        private final MutableLiveData<ArrayList<Post>> postLiveData = new MutableLiveData<>();
        private final MutableLiveData<String> errorMessage = new MutableLiveData<>();


        public LiveData<ThreadData> getThreadData(){
            return threadData;
        }

        public LiveData<ArrayList<Post>> getPostLiveData() {
            return postLiveData;
        }


        public LiveData<String> getErrorMessage() {
            return errorMessage;
        }


        public void fetchThread(int tid){
            dzService.getThread(tid).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<ThreadData> call, @NonNull Response<ThreadData> response) {
                    if(response.isSuccessful()){
                        ThreadData data= response.body();
                        if(data!=null){
                            threadData.postValue(data);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ThreadData> call, @NonNull Throwable throwable) {

                }
            });
        }

        public void fetchPost(int tid) {
            dzService.getPosts(tid).enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Post>> call, @NonNull Response<ArrayList<Post>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, response.body().toString());
                        postLiveData.postValue(response.body());
                    } else {
                        errorMessage.postValue("Failed to fetch post");
                        Log.e(TAG, "Failed to fetch post");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Post>> call, @NonNull Throwable t) {
                    errorMessage.postValue(t.getMessage());
                    Log.e(TAG, "Error fetching post", t);
                }
            });
        }
    }

    public class CustomTagHandler implements Html.TagHandler {
        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            if (tag.equalsIgnoreCase("img")) {
                int len = output.length();
                ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
                String imageSource = images[0].getSource();
                if(imageSource==null)return;
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        if (!imageSource.contains("smiley")) {//带smiley的为论坛表情
                            //Toast.makeText(PostActivity.this, imageSource, Toast.LENGTH_SHORT).show();
                            Intent galleryIntent = new Intent(PostActivity.this, PostViewImageActivity.class);

                            galleryIntent.putExtra("url", imageSource);
                            galleryIntent.putExtra("index", -1);
                            startActivity(galleryIntent);
                        }
                    }
                };
                output.setSpan(clickableSpan, len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
}
