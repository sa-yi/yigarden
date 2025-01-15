package com.sayi.vdim.utils;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.MessageFormat;


public class HTMLParser {
    public static void parseHtmlContent(Context context,LinearLayout parentLayout, String htmlContent) {
        Document document = Jsoup.parse(htmlContent);
        Elements elements = document.body().children();
        for (Element element : elements) {
            if (element.tagName().equals("h2")) {
                TextView textView = new TextView(context);
                textView.setText(element.text());
                textView.setTextSize(28);
                parentLayout.addView(textView);
            } else if (element.tagName().equals("a")) {
                TextView linkView = new TextView(context);
                linkView.setText(Html.fromHtml("<a href=\"" + element.attr("href") + "\">" + element.text() + "</a>"));
                linkView.setMovementMethod(LinkMovementMethod.getInstance());
                parentLayout.addView(linkView);
            }else if (element.tagName().equals("p")){
                TextView textView = new TextView(context);
                textView.setText(element.text());
                textView.setTextSize(20);
                parentLayout.addView(textView);
            }else if (element.tagName().equals("ul")){
                for(Element childElement:element.children()){

                    TextView textView = new TextView(context);
                    textView.setText(MessageFormat.format("• {0}", childElement.text()));
                    textView.setTextSize(18);
                    textView.setPadding(10,10,10,10);
                    parentLayout.addView(textView);
                }
            }
        }
    }
}
