package com.sayi.yi_garden.api;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.sayi.yi_garden.R;

import java.util.Arrays;

public class ApiPostFeed {
    @SerializedName("id")
    private int id=0;
    @SerializedName("date")
    private String date="114-5-14";

    @SerializedName("date_gmt")
    private String date_gmt="11-45-14";

    @SerializedName("guid")
    private RenderedField guid;

    @SerializedName("modified")
    private String modified="no";

    @SerializedName("modified_gmt")
    private String modified_gmt="yet";

    @SerializedName("slug")
    private String slug="what's this";

    @SerializedName("status")
    private String status="safe";

    @SerializedName("type")
    private String type="2333";

    @SerializedName("link")
    private String link="null";

    @SerializedName("title")
    private RenderedField title=new RenderedField();

    @SerializedName("content")
    private RenderedField content=new RenderedField();

    @SerializedName("excerpt")
    private RenderedField excerpt=new RenderedField();

    @SerializedName("author")
    private int author=0;

    @SerializedName("featured_media")
    private int featured_media=0;

    @SerializedName("comment_status")
    private String comment_status="???";

    @SerializedName("ping_status")
    private String ping_status="loss";

    @SerializedName("sticky")
    private boolean sticky=false;

    @SerializedName("template")
    private String template="no template";

    @SerializedName("format")
    private String format="gogogo";

    @SerializedName("meta")
    private MetaData metaData;

    @SerializedName("categories")
    private int[] categories=new int[]{};

    @SerializedName("tags")
    private int[] tags=new int[]{};

    public int getId(){return id;}
    public void setId(int _id){id=_id;}


    public String getDate(){return date;}

    public void setDate(String date){
        this.date=date;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }


    public RenderedField getTitle() {
        return title;
    }

    public void setTitle(RenderedField title) {
        this.title = title;
    }

    public RenderedField getContent() {
        return content;
    }

    public void setContent(RenderedField content) {
        this.content = content;
    }

    public RenderedField getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(RenderedField excerpt) {
        this.excerpt = excerpt;
    }



    public static class RenderedField {
        private String rendered="defalut content";
        private boolean isProtected=false;

        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }

        @NonNull
        @Override
        public String toString() {
            return "RenderedField{" +
                    "rendered='" + rendered + "'" +
                    ", isProtected=" + isProtected +
                    '}';
        }
    }

    public static class MetaData {
        private String footnotes;

        public String getFootnotes() {
            return footnotes;
        }

        public void setFootnotes(String footnotes) {
            this.footnotes = footnotes;
        }

        @Override
        public String toString() {
            return "MetaData{" +
                    "footnotes='" + footnotes + "'" +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PostFeed{" +
                "id=" + id +
                ", date='" + date + "'" +
                ", date_gmt='" + date_gmt + "'" +
                ", guid=" + guid +
                ", modified='" + modified + "'" +
                ", modified_gmt='" + modified_gmt + "'" +
                ", slug='" + slug + "'" +
                ", status='" + status + "'" +
                ", type='" + type + "'" +
                ", link='" + link + "'" +
                ", title=" + title +
                ", content=" + content +
                ", excerpt=" + excerpt +
                ", author=" + author +
                ", featured_media=" + featured_media +
                ", comment_status='" + comment_status + "'" +
                ", ping_status='" + ping_status + "'" +
                ", sticky=" + sticky +
                ", template='" + template + "'" +
                ", format='" + format + "'" +
                ", metaData=" + metaData +
                ", categories=" + Arrays.toString(categories) +
                ", tags=" + Arrays.toString(tags) +
                '}';
    }
}
