package com.sayi.music.util.lrcparser;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcParser {
    public static ArrayList<LrcRow> lrcs = new ArrayList<>();
    static LrcRow nullRow = new LrcRow("00:00", "");

    public static ArrayList<LrcRow> parse(String path) {
        Log.d("LrcParser", path);
        lrcs.clear();
        path = path.replaceAll(".mp3$|.flac$|.ogg$|.wma$|.m4a$", ".lrc");

        String regex = "\\[(.*?)\\](.+)";
        Pattern p = Pattern.compile(regex);

        for (String lrc : iparse(path)) {
            Matcher m = p.matcher(lrc);
            if (m.find()) {
                Matcher m2 = p.matcher(m.group(2));
                if (m2.find()) {
                    lrcs.add(new LrcRow(m.group(1), m2.group(2)));
                    lrcs.add(new LrcRow(m2.group(1), m2.group(2)));
                } else {
                    lrcs.add(new LrcRow(m.group(1), m.group(2)));
                }
            }
            Collections.sort(lrcs, (p1, p2) -> (int) (p1.time - p2.time));
        }
        return lrcs;
    }

    public static LrcRow getLrc(float time) {
        int n = 0;
        while (n < lrcs.size()) {
            LrcRow lrcRow = lrcs.get(n);
            LrcRow lrcRow2 = n + 1 == lrcs.size() ? null : lrcs.get(n + 1);
            if (time >= lrcRow.time && lrcRow2 != null && time < lrcRow2.time || time > lrcRow.time && lrcRow2 == null) {
                return lrcRow;
            }
            ++n;
        }
        return nullRow;
    }

    static String[] iparse(String path) {
        String lrc = read(path);
        String[] lrcs = lrc.split("\n");
        return lrcs;
    }

    static String read(String path) {
        String s = "";
        File file = new File(path);
        if (!file.exists())
            return "";
        try {
            FileInputStream in = new FileInputStream(file);
            s = readTextFromSDcard(in);
        } catch (Exception e) {
        }
        return s;
    }

    static String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer();
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
