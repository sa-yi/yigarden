package com.sayi.vdim.dz_entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ThreadData extends BaseResponse {
    @SerializedName("tid")
    private String tid;
    @SerializedName("fid")
    private String fid;
    @SerializedName("posttableid")
    private String posttableid;
    @SerializedName("typeid")
    private String typeid;
    @SerializedName("sortid")
    private String sortid;
    @SerializedName("readperm")
    private String readperm;
    @SerializedName("price")
    private String price;
    @SerializedName("author")
    private String author;
    @SerializedName("authorid")
    private int authorId;
    @SerializedName("subject")
    private String subject;
    @SerializedName("dateline")
    private String dateline;
    @SerializedName("lastpost")
    private String lastpost;
    @SerializedName("lastposter")
    private String lastposter;
    @SerializedName("views")
    private String views;
    @SerializedName("replies")
    private String replies;
    @SerializedName("displayorder")
    private String displayorder;
    @SerializedName("highlight")
    private String highlight;
    @SerializedName("digest")
    private String digest;
    @SerializedName("rate")
    private String rate;
    @SerializedName("special")
    private String special;
    @SerializedName("attachment")
    private String attachment;
    @SerializedName("moderated")
    private String moderated;
    @SerializedName("closed")
    private String closed;
    @SerializedName("stickreply")
    private String stickreply;
    @SerializedName("recommends")
    private String recommends;
    @SerializedName("recommend_add")
    private String recommendAdd;
    @SerializedName("recommend_sub")
    private String recommendSub;
    @SerializedName("heats")
    private String heats;
    @SerializedName("status")
    private String status;
    @SerializedName("isgroup")
    private String isgroup;
    @SerializedName("favtimes")
    private String favtimes;
    @SerializedName("sharetimes")
    private String sharetimes;
    @SerializedName("stamp")
    private String stamp;
    @SerializedName("icon")
    private String icon;
    @SerializedName("pushedaid")
    private String pushedaid;
    @SerializedName("cover")
    private String cover;
    @SerializedName("replycredit")
    private String replycredit;
    @SerializedName("relatebytag")
    private String relatebytag;
    @SerializedName("maxposition")
    private String maxposition;
    @SerializedName("bgcolor")
    private String bgcolor;
    @SerializedName("comments")
    private Object comments;
    @SerializedName("hidden")
    private String hidden;
    @SerializedName("lastposterenc")
    private String lastposterenc;
    @SerializedName("multipage")
    private String multipage;
    @SerializedName("recommendicon")
    private String recommendicon;
    @SerializedName("new")
    private String isNew;
    @SerializedName("heatlevel")
    private String heatlevel;
    @SerializedName("moved")
    private String moved;
    @SerializedName("icontid")
    private String icontid;
    @SerializedName("folder")
    private String folder;
    @SerializedName("dbdateline")
    private String dbdateline;
    @SerializedName("weeknew")
    private String weeknew;
    @SerializedName("istoday")
    private String isToday;
    @SerializedName("dblastpost")
    private String dblastpost;
    @SerializedName("id")
    private String id;
    @SerializedName("rushreply")
    private String rushreply;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("attachmentImageNumber")
    private String attachmentImageNumber;
    @SerializedName("message")
    private String message;
    @SerializedName("attachmentImagePreviewList")
    private List<Object> attachmentImagePreviewList;
    @SerializedName("expirations")
    private long expirations;
    @SerializedName("multiple")
    private int multiple;
    @SerializedName("maxchoices")
    private int maxchoices;
    @SerializedName("voterscount")
    private int voterscount;
    @SerializedName("visiblepoll")
    private int visiblepoll;
    @SerializedName("allowvote")
    private int allowvote;
    @SerializedName("remaintime")
    private Object remaintime;

    public String getSubject() {
        return subject;
    }

    public String getTid() {
        return tid;
    }

    public String getAuthor() {
        return author;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getLastpost() {
        return lastpost;
    }

    public String getMessage() {
        return message;
    }


}