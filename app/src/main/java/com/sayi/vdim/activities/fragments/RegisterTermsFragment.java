package com.sayi.vdim.activities.fragments;

import android.content.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.method.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.core.text.*;
import androidx.fragment.app.*;

import com.sayi.vdim.databinding.*;

public class RegisterTermsFragment extends Fragment {
    RegisterTermsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RegisterTermsBinding.inflate(getLayoutInflater());
        String html = "同意<a href=\"https://i.lty.fan/bangui\">《V次元站点总版规》</a>、<a href=\"http://i.lty.fan/shouze\">《锦依卫守则》</a>和<a href=\"https://i.lty.fan/caidan\">《洛天依今天晚饭的菜单》</a>";

        binding.terms.setText(html);


        // 在TextView中使用getFormattedHtml方法
        SpannableString formattedText = getFormattedHtml(html, getContext());
        binding.terms.setText(formattedText);
        binding.terms.setMovementMethod(LinkMovementMethod.getInstance());
        return binding.getRoot();
    }

    public boolean isTermAccepted() {
        return binding.checkbox.isChecked();
    }

    @NonNull
    private SpannableString getFormattedHtml(String htmlContent, final Context context) {
        // 使用Html.fromHtml转换HTML文本
        Spanned spanned = HtmlCompat.fromHtml(htmlContent, HtmlCompat.FROM_HTML_MODE_LEGACY);

        // 将Spanned转换为SpannableString
        final SpannableString spannableString = new SpannableString(spanned);

        // 查找所有的URLSpan并替换为自定义的ClickableSpan
        URLSpan[] urls = spannableString.getSpans(0, spannableString.length(), URLSpan.class);
        for (final URLSpan url : urls) {
            int start = spannableString.getSpanStart(url);
            int end = spannableString.getSpanEnd(url);
            int flags = spannableString.getSpanFlags(url);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    // 弹出对话框而不是直接打开链接
                    Toast.makeText(context, "Clicked URL: " + url.getURL(), Toast.LENGTH_SHORT).show();
                }
            };
            // 移除旧的URLSpan并添加新的ClickableSpan
            spannableString.removeSpan(url);
            spannableString.setSpan(clickableSpan, start, end, flags);
        }

        return spannableString;
    }


}
