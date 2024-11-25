package com.sayi.yi_garden.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class MediaItem {

    @SerializedName("_links")
    private Map<String, List<Link>> links;

    @SerializedName("alt_text")
    private String altText;

    @SerializedName("author")
    private int author;

    @SerializedName("caption")
    private Caption caption;

    @SerializedName("class_list")
    private List<String> classList;

    @SerializedName("comment_status")
    private String commentStatus;

    @SerializedName("date")
    private String date;

    @SerializedName("date_gmt")
    private String dateGmt;

    @SerializedName("description")
    private Description description;

    @SerializedName("featured_media")
    private int featuredMedia;

    @SerializedName("generated_slug")
    private String generatedSlug;

    public Guid getGuid() {
        return guid;
    }

    @SerializedName("guid")
    private Guid guid;

    @SerializedName("id")
    private int id;

    @SerializedName("link")
    private String link;

    @SerializedName("media_details")
    private MediaDetails mediaDetails;

    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("meta")
    private List<Object> meta;

    @SerializedName("mime_type")
    private String mimeType;

    @SerializedName("missing_image_sizes")
    private List<Object> missingImageSizes;

    @SerializedName("modified")
    private String modified;

    @SerializedName("modified_gmt")
    private String modifiedGmt;

    @SerializedName("permalink_template")
    private String permalinkTemplate;

    @SerializedName("ping_status")
    private String pingStatus;

    @SerializedName("post")
    private Object post;

    @SerializedName("slug")
    private String slug;

    @SerializedName("source_url")
    private String sourceUrl;

    @SerializedName("status")
    private String status;

    @SerializedName("template")
    private String template;

    @SerializedName("title")
    private Title title;

    @SerializedName("type")
    private String type;

    @Override
    public String toString() {
        return "MediaItem{" +
                "links=" + links +
                ", altText='" + altText + '\'' +
                ", author=" + author +
                ", caption=" + caption +
                ", classList=" + classList +
                ", commentStatus='" + commentStatus + '\'' +
                ", date='" + date + '\'' +
                ", dateGmt='" + dateGmt + '\'' +
                ", description=" + description +
                ", featuredMedia=" + featuredMedia +
                ", generatedSlug='" + generatedSlug + '\'' +
                ", guid=" + guid +
                ", id=" + id +
                ", link='" + link + '\'' +
                ", mediaDetails=" + mediaDetails +
                ", mediaType='" + mediaType + '\'' +
                ", meta=" + meta +
                ", mimeType='" + mimeType + '\'' +
                ", missingImageSizes=" + missingImageSizes +
                ", modified='" + modified + '\'' +
                ", modifiedGmt='" + modifiedGmt + '\'' +
                ", permalinkTemplate='" + permalinkTemplate + '\'' +
                ", pingStatus='" + pingStatus + '\'' +
                ", post=" + post +
                ", slug='" + slug + '\'' +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", status='" + status + '\'' +
                ", template='" + template + '\'' +
                ", title=" + title +
                ", type='" + type + '\'' +
                '}';
    }

    // Getters and setters for all the fields

    // Nested classes for complex types
    public static class Link {
        @SerializedName("href")
        private String href;

        @SerializedName("embeddable")
        private boolean embeddable;

        // Getters and setters
    }

    public static class Caption {
        @SerializedName("raw")
        private String raw;

        @SerializedName("rendered")
        private String rendered;

        // Getters and setters
    }

    public static class Description {
        @SerializedName("raw")
        private String raw;

        @SerializedName("rendered")
        private String rendered;

        // Getters and setters
    }

    public static class Guid {
        @SerializedName("raw")
        private String raw;

        public String getRendered() {
            return rendered;
        }

        @SerializedName("rendered")
        private String rendered;

        // Getters and setters
    }

    public static class MediaDetails {
        @SerializedName("file")
        private String file;

        @SerializedName("filesize")
        private int filesize;

        @SerializedName("height")
        private int height;

        @SerializedName("image_meta")
        private ImageMeta imageMeta;

        @SerializedName("original_image")
        private String originalImage;

        @SerializedName("sizes")
        private Map<String, Size> sizes;

        @SerializedName("width")
        private int width;

        // Getters and setters

        // Nested classes for complex types
        public static class ImageMeta {
            // Define fields according to the JSON structure
        }

        public static class Size {
            // Define fields according to the JSON structure
        }
    }

    public static class Title {
        @SerializedName("raw")
        private String raw;

        @SerializedName("rendered")
        private String rendered;

        // Getters and setters
    }
}
