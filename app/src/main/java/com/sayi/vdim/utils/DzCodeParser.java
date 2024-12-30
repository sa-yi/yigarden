package com.sayi.vdim.utils;

public class DzCodeParser {

    public static String parseBBCode(String input) {
        String output = input;

        // Handle [quote] tags
        output = output.replaceAll("\\[quote\\]", "<div class=\"reply_wrap\">")
                .replaceAll("\\[/quote\\]", "</div>");

        // Handle [size] tags
        output = output.replaceAll("\\[size=(\\d+)\\]", "<span style=\"font-size:$1px;\">")
                .replaceAll("\\[/size\\]", "</span>");

        // Handle [color] tags
        output = output.replaceAll("\\[color=([a-zA-Z]+|#[0-9a-fA-F]{6})\\]", "<font color=\"$1\">")
                .replaceAll("\\[/color\\]", "</font>");

        // Handle [url] tags with an explicit URL
        output = output.replaceAll("\\[url=(.*?)\\](.*?)\\[/url\\]", "<a href=\"$1\">$2</a>");

        // Handle [url] tags with just a URL
        output = output.replaceAll("\\[url\\](.*?)\\[/url\\]", "<a href=\"$1\">$1</a>");

        // Handle [b] tags
        output = output.replaceAll("\\[b\\]", "<strong>")
                .replaceAll("\\[/b\\]", "</strong>");

        // Handle [i] tags
        output = output.replaceAll("\\[i\\]", "<em>")
                .replaceAll("\\[/i\\]", "</em>");


        // Handle [i=s] tags
        output = output.replaceAll("\\[i=s\\]", "<i>")
                .replaceAll("\\[/i\\]", "</i>");

        // Handle [align=left] tags
        output = output.replaceAll("\\[align=left\\]", "<div style=\"text-align: left;\">")
                .replaceAll("\\[align=right\\]", "<div style=\"text-align: right;\">")
                .replaceAll("\\[align=center\\]", "<div style=\"text-align: center;\">")
                .replaceAll("\\[align=justify\\]", "<div style=\"text-align: justify;\">")
                .replaceAll("\\[/align\\]", "</div>");

        // Handle {:13_262:} format for smileys
        output = output.replaceAll("\\{:(\\d+)_(\\d+):\\}", "<img src=\"https://api.lty.fan/smiley/$1_$2\" alt=\":$1_$2:\"> ");


        // Handle newlines
        output = output.replaceAll("\\n", "<br>");

        return output;
    }
}
