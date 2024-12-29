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

        // Handle [url] tags
        output = output.replaceAll("\\[url=(.*?)\\](.*?)\\[/url\\]", "<a href=\"$1\">$2</a>");

        // Handle [b] tags
        output = output.replaceAll("\\[b\\]", "<strong>")
                .replaceAll("\\[/b\\]", "</strong>");

        // Handle [i] tags
        output = output.replaceAll("\\[i\\]", "<em>")
                .replaceAll("\\[/i\\]", "</em>");


        // Handle {:13_262:} format for smileys
        output = output.replaceAll("\\{:(\\d+)_(\\d+):\\}", "<img src=\"https://api.lty.fan/smiley/$1_$2\" alt=\":$1_$2:\"> ");


        // Handle newlines
        output = output.replaceAll("\\n", "<br>");

        return output;
    }
}
