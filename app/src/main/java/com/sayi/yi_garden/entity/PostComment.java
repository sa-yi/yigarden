package com.sayi.yi_garden.entity;

import com.google.gson.annotations.SerializedName;

public class PostComment {
    @SerializedName("id")
    private int id=-1;

    @SerializedName("post")
    private int post_id=-1;

    @SerializedName("author")
    private int author_id=-1;

    @SerializedName("author_name")
    private String author_name="";

    @SerializedName("content")
    private RenderedField content;


    public int getId() {
        return id;
    }

    public int getPost_id(){
        return post_id;
    }

    public int getAuthor_id(){
        return author_id;
    }

    public String getAuthor_name(){
        return author_name;
    }

    public RenderedField getContent(){
        return content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", post=" + post_id +
                ", author=" + author_id +
                ", author_name='" + author_name + '\'' +
                ", content=" + content +
                '}';
    }
}
