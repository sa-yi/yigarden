package com.sayi.yi_garden.entity;

public class PostFeed {
    private int id=-1;
    private String user_name="名字";
    private String title="标题";
    private String content="内容";
    private String class_type="类型";
    private String image_url="图片链接";

    private String send_time="2024-4-29";

    private int score=0;

    public PostFeed(){

    }

    public int getId(){return id;}

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getClass_type() {
        return class_type;
    }

    public String getImage_url() {
        return image_url;
    }

    public int getScore() {
        return score;
    }

    public void setClass_type(String class_type) {
        this.class_type = class_type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSend_time(String send_time){
        this.send_time=send_time;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSend_time() {
        return send_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
