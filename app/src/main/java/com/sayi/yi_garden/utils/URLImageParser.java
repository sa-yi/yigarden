package com.sayi.yi_garden.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class URLImageParser implements Html.ImageGetter {
    private Context context;
    private TextView textView;

    public URLImageParser(TextView textView, Context context) {
        this.textView = textView;
        this.context = context;
    }

    @Override
    public Drawable getDrawable(String source) {
        Log.d("source",source);
        final URLDrawable urlDrawable = new URLDrawable();

        Glide.with(context)
                .load(source)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        urlDrawable.setBounds(0, 0, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                        urlDrawable.setDrawable(resource);
                        textView.setText(textView.getText());  // 更新 TextView 的内容
                        textView.postInvalidate();  // 重绘 TextView
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        // 清除时的处理逻辑，可以留空
                    }
                });

        return urlDrawable;
    }

    private static class URLDrawable extends Drawable {
        private Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

        @Override
        public void setAlpha(int alpha) {
            if (drawable != null) {
                drawable.setAlpha(alpha);
            }
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
            if (drawable != null) {
                drawable.setColorFilter(colorFilter);
            }
        }

        @Override
        public int getOpacity() {
            return drawable != null ? drawable.getOpacity() : PixelFormat.UNKNOWN;
        }
    }
}