package com.sayi.vdim.dz_entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Post {
    @SerializedName("pid")
    private int pid;
    @SerializedName("tid")
    private int tid;
    @SerializedName("first")
    private boolean first;
    @SerializedName("author")
    private String author;
    @SerializedName("authorid")
    private int authorid;
    @SerializedName("dateline")
    private String dateline;
    @SerializedName("message")
    private String message;
    @SerializedName("anonymous")
    private boolean anonymous;
    @SerializedName("attachment")
    private boolean attachment;
    @SerializedName("status")
    private int status;
    @SerializedName("replycredit")
    private int replycredit;
    @SerializedName("position")
    private int position;
    @SerializedName("username")
    private String username;
    @SerializedName("adminid")
    private int adminid;
    @SerializedName("groupid")
    private int groupid;
    @SerializedName("memberstatus")
    private int memberstatus;
    @SerializedName("number")
    private int number;
    @SerializedName("dbdateline")
    private int dbdateline;
    @SerializedName("attachments")
    private ArrayList<ThreadAttachment> attachments;
    @SerializedName("imagelist")
    private ArrayList<Integer> imagelist;
    @SerializedName("groupiconid")
    private String groupiconid;

    public String getAuthor() {
        return author;
    }

    public int getAuthorid() {
        return authorid;
    }

    public String getDateline() {
        return dateline;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<ThreadAttachment> getAttachments() {
        return attachments;
    }

    public ArrayList<Integer> getImagelist() {
        return imagelist;
    }

    @Override
    public String toString() {
        return "Post{" +
                "pid=" + pid +
                ", tid=" + tid +
                ", first=" + first +
                ", author='" + author + '\'' +
                ", authorid=" + authorid +
                ", dateline='" + dateline + '\'' +
                ", message='" + message + '\'' +
                ", anonymous=" + anonymous +
                ", attachment=" + attachment +
                ", status=" + status +
                ", replycredit=" + replycredit +
                ", position=" + position +
                ", username='" + username + '\'' +
                ", adminid=" + adminid +
                ", groupid=" + groupid +
                ", memberstatus=" + memberstatus +
                ", number=" + number +
                ", dbdateline=" + dbdateline +
                ", attachments=" + attachments +
                ", imagelist=" + imagelist +
                ", groupiconid='" + groupiconid + '\'' +
                '}';
    }
}