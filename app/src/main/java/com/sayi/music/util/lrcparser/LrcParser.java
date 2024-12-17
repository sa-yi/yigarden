package com.sayi.music.util.lrcparser;

import android.util.*;

import androidx.annotation.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class LrcParser {
    public static ArrayList<LrcRow> lrcs = new ArrayList<>();
    static String regex = "\\[(.*?)\\](.+)";
    static Pattern p = Pattern.compile(regex);
    static LrcRow nullRow = new LrcRow("00:00", "");

    public static ArrayList<LrcRow> parse(String musicFilePath) {
        Log.d("LrcParser", musicFilePath);
        String lyricFilePath = musicFilePath.replaceAll(".mp3$|.flac$|.ogg$|.wma$|.m4a$", ".lrc");

        return metaParse(iparseFile(lyricFilePath));
    }

    public static ArrayList<LrcRow> parseString(String content){
        return metaParse(iparseString(content));
    }

    static String[] iparseFile(String path) {
        String lrc = read(path);
        return iparseString(lrc);
    }

    static String[] iparseString(String content) {
        String[] lrcs = content.split("\n");
        return lrcs;
    }

    public static ArrayList<LrcRow> metaParse(String[] lyrics) {
        lrcs.clear();
        for (String lrc : lyrics) {
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


    public static String read(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        try (FileInputStream in = new FileInputStream(file);
             InputStreamReader reader = new InputStreamReader(in);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }
        } catch (IOException e) {
            Log.e("LrcParser", Objects.requireNonNull(e.getMessage()));
        }
        return buffer.toString();
    }
}
