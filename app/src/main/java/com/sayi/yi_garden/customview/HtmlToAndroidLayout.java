package com.sayi.yi_garden.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.VideoSize;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sayi.MainApplication;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HtmlToAndroidLayout {

    public static LinearLayout processHtml(Context context,String html) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        Document document = Jsoup.parse(html);
        int a = 0;
        for (Element element : document.body().children()) {
            a++;
            System.out.println(element + "" + a);
            View view = baseProcess(element, context);
            layout.addView(view);
        }
        return layout;
    }

    private static View baseProcess(Element element, Context context) {
        Log.d("htmltag", element.tagName());
        if (!element.ownText().isEmpty()) {
            Log.d("text", element.toString());
            TextView textView = new TextView(context);
            Spanned spannedTExt = fromHtml(element.html());
            textView.setText(spannedTExt);
            return textView;
        }
        switch (element.tagName()) {
            case "pre"://保留空格
            case "figure"://一段独立内容
            case "div"://区块
            case "p"://段落
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                for (Element subElement : element.children()) {
                    Log.d("taginp", subElement.toString());
                    View view = baseProcess(subElement, context);
                    linearLayout.addView(view);
                }
                return linearLayout;
            case "video":
                String url = element.attr("src");
                PlayerView playerView = new PlayerView(context);
                ExoPlayer exoPlayer = new ExoPlayer.Builder(context).build();
                exoPlayer.addListener(new Player.Listener() {
                    @Override
                    public void onVideoSizeChanged(@NonNull VideoSize videoSize) {
                        int videoWidth = videoSize.width;
                        int videoHeight = videoSize.height;

                        float ratio = (float) videoWidth / videoHeight;


                        LinearLayout.LayoutParams videoParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        int width = playerView.getMeasuredWidth();
                        int height = (int) (width / ratio);

                        videoParams.width = width;
                        videoParams.height = height;
                        //videoParams.setMargins(80,10,80,10);

                        playerView.setLayoutParams(videoParams);
                    }
                });
                playerView.setPlayer(exoPlayer);

                MediaItem mediaItem = MediaItem.fromUri(url);
                exoPlayer.setMediaItem(mediaItem);
                exoPlayer.prepare();
                return playerView;

            case "img":
                ImageView imageView = new ImageView(context);
                String imageUrl = element.attr("src");

                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                imageParams.setMargins(80, 10, 80, 10);
                //imageView.setLayoutParams(imageParams);

                Glide.with(context).load(imageUrl).listener(new RequestListener<>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource instanceof BitmapDrawable) {
                            Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();

                            int viewWidth = imageView.getMeasuredWidth();

                            int viewHeight = height * (viewWidth / width);
                            imageParams.height = viewHeight;
                            imageView.setLayoutParams(imageParams);
                        }
                        return false;
                    }
                }).into(imageView);

                imageView.setOnClickListener(v -> {
                    MainApplication.toast("clicked");
                });

                return imageView;
            case "h2":
            case "h3":
            case "a"://超链接
                TextView textView = new TextView(context);
                textView.setText(fromHtml(element.html()));
                textView.setMovementMethod(LinkMovementMethod.getInstance());
                return textView;

            case "ul"://列表
            case "ol"://编号列表
            case "code":
                TextView codeTextView = new TextView(context);
                codeTextView.setText(element.html());


                return codeTextView;
            case "iframe":
                Log.d("iframe", element.parent().toString());
                //element.parent();
                String content = "<html><body>" + element.parent().html() + "</body></html>";

                content=content.replaceAll("src=\"//", "src=\"https://");

                WebView webView = new WebView(context);
                //webView.loadData(content, "text/html", "utf-8");
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        //super.onReceivedError(view, request, error);
                        Log.d("webview", error.toString());
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        Log.d("webviewurl",url);
                    }
                });
                webView.loadData(content,"text/html","utf-8");

                return webView;
            default:
                Log.d("othertag", element.tagName());
                View view = new View(context);
                return view;
        }
    }

    public static String readFileToString(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                contentBuilder.append(currentLine).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return contentBuilder.toString();
    }


    private static Spanned fromHtml(String html) {
        return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
    }
}
