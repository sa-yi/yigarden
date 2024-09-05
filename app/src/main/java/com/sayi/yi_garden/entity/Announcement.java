package com.sayi.yi_garden.entity;

public class Announcement {
    public String title="默认标题";
    public String contents="默认内容";
    public String jumpto_url="";
    public Announcement(String _title,String _contents,String _jumpto_url){
        title=_title;
        contents=_contents;
        jumpto_url=_jumpto_url;
    }
}
