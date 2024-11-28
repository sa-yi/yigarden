package com.sayi.yi_garden.entity;

public class Announcement {
    public String title="默认标题";
    public String contents="默认内容";
    public String jumpto_url="";
    public Announcement(String title,String contents,String jumpto_url){
        this.title=title;
        this.contents=contents;
        this.jumpto_url=jumpto_url;
    }
}
